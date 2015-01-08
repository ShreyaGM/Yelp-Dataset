 import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Vector;
import java.util.Map.Entry;

import org.apache.commons.lang.StringUtils;

    public class task2 {
        private String pathToSWN = "C:/Users/Shreya/Desktop/home/swn/www/admin/dump/SentiWordNet_3.0.0_20130122.txt";
        private HashMap<String, Double> _dict;
        
        //Taken from SentiWordNet.com
        //performs Sentiment Anlaysis on user reviews to give either a Positive or a Negative score
        //from the list of words given in their dictionary
        public task2(){

            _dict = new HashMap<String, Double>();
            HashMap<String, Vector<Double>> _temp = new HashMap<String, Vector<Double>>();
            try{
                BufferedReader csv =  new BufferedReader(new FileReader(pathToSWN));
                String line = "";           
                while((line = csv.readLine()) != null)
                {
                    String[] data = line.split("\t");
                    Double score = Double.parseDouble(data[2])-Double.parseDouble(data[3]);
                    String[] words = data[4].split(" ");
                    for(String w:words)
                    {
                        String[] w_n = w.split("#");
                        w_n[0] += "#"+data[0];
                        int index = Integer.parseInt(w_n[1])-1;
                        if(_temp.containsKey(w_n[0]))
                        {
                            Vector<Double> v = _temp.get(w_n[0]);
                            if(index>v.size())
                                for(int i = v.size();i<index; i++)
                                    v.add(0.0);
                            v.add(index, score);
                            _temp.put(w_n[0], v);
                        }
                        else
                        {
                            Vector<Double> v = new Vector<Double>();
                            for(int i = 0;i<index; i++)
                                v.add(0.0);
                            v.add(index, score);
                            _temp.put(w_n[0], v);
                        }
                    }
                }
                Set<String> temp = _temp.keySet();
                for (Iterator<String> iterator = temp.iterator(); iterator.hasNext();) {
                    String word = (String) iterator.next();
                    Vector<Double> v = _temp.get(word);
                    double score = 0.0;
                    double sum = 0.0;
                    for(int i = 0; i < v.size(); i++)
                        score += ((double)1/(double)(i+1))*v.get(i);
                    for(int i = 1; i<=v.size(); i++)
                        sum += (double)1/(double)i;
                    score /= sum;
                    
                    /**/
                    
                    _dict.put(word, score);
                }
                csv.close();
            }
            catch(Exception e){e.printStackTrace();}  
            
        }

public Double extract(String word)
{
    Double total = new Double(0);
    if(_dict.get(word+"#n") != null)
         total = _dict.get(word+"#n") + total;
    if(_dict.get(word+"#a") != null)
        total = _dict.get(word+"#a") + total;
    if(_dict.get(word+"#r") != null)
        total = _dict.get(word+"#r") + total;
    if(_dict.get(word+"#v") != null)
        total = _dict.get(word+"#v") + total;
    return total;
}

public static void main(String[] args) throws IOException {
    task2 test = new task2();
    //String bid = "Yq8LiVymGA7vBpGCQuDfRw";//542
    String bid = "-vHWAsiX0iHWJw-pkqv32Q"; //121 reviews
    
    
   final File folder = new File("C:\\Desktop\\yelp");
	int count=0;
	File business;
	Path path1;
	String filepath="";
	char flag='n';
	//We have split the Review.json into 9 files so working with it becomes easier
	//Here we are seraching through each file to find the input Business ID
	for (final File fileEntry : folder.listFiles()) {

		//Initially flag is set to 'n', which means that Business ID has not yet been found.
		if(flag=='n')
		{
		//If the entry in the folder is a file then do the following
		if (fileEntry.isFile()) {
			   //get the path for each file
		           business= new File(fileEntry.getPath());
		           path1 = Paths.get(business.getPath());
		           //convert the file to string
		           filepath = new String(Files.readAllBytes(path1));
		           //Put all the contents of one review file into a String [] reviews,
			   //so we can extract reviewtext and votes later
		           String [] reviews= StringUtils.substringsBetween(filepath, "<\"votes\"", "end>");
		           //Iterate through the array to find the input Business ID
		           for(String temp: reviews){
		        	   //If Business ID is found
		        	   if(temp.contains(bid))
		        	   {
		        	   	//then set flag as 'y' and break out of this loop
		        	   	flag='y';
		        	   	break;
		        	   }
		           }
			}
		}

		//If Business ID is found, then we need to stop iterating through other files
		else
		{
			break;
		}
		//System.out.println(count);
	}
	//Again store all contents of the file containing the Business ID in a String [] review
	String [] review= StringUtils.substringsBetween(filepath, "<\"votes\"", "end>");

	//This hashmap is craeted on the fly and stores all the reviews of the input Business ID
	//Key is simply an integer value, starting with 1,2,...,no-of-reviews-for-the-business-id
	//Value is the user text review
	Map<Integer,String> brev= new HashMap<Integer, String>();

	//This variable is used for the key value of the Hash Map
	int i=0;

	//Used Stop Analyzer to perform Normalisation and removing stop words
	    Analyzer analyzer = new StopAnalyzer();
	    QueryParser q=new QueryParser("",analyzer);

	    //This varaible stores review rating as given by the user
	    String rates="";
	    int stars=0;
	    int totalstars=0;


	    //Iterate through the review array 
		for(String temp:review){

			//if the array entry contains input Business ID
			if(temp.contains(bid)){

				//get the user rating from the review
				rates = StringUtils.substringBetween(temp, "\"stars\": ", ", \"date\"");
				stars = Integer.parseInt(rates);

				//Get the review text to perform Sentiment Analysis and get our own rating for the review
				String reviewtext = (StringUtils.substringBetween(temp, "\"text\": ", ", \"type\""));

				//Get rid of all special characters
				reviewtext = reviewtext.replaceAll("[-+,.^:!?]","");

				//Parse the review text
				reviewtext = "" + q.parse(reviewtext);

				//store the text in the Hash Map
				brev.put(i,reviewtext);
				//i++ for the next entry in Hash Map
				i++;
				//summing up rating of all reviews for one Business ID
				totalstars += stars;
			}
		}

	//Rating sum is divided by total number of reviews
	//this is done to get a average fair rating for the Business
	int TOTALSTARS = totalstars/i;
	double rate = 0.0;
	int j=0;

	//for every entry in the Hash Map perform Sentiment Analysis
	for(Entry<Integer,String> te1:brev.entrySet()){

		//Value of hash map is the review text
		String sentence = te1.getValue();
		//Split the review into words
		String[] words = sentence.split("\\s+"); 
		 double totalScore = 0;

		    //for each word, perform the following
		    //replace everything execpt for [a-zA-Z] with void
		    for(String word : words) {
		        word = word.replaceAll("([^a-zA-Z\\s])", "");

		        //Call SentiWordNet method to perform analysis and give Sentiment score
		        if (test.extract(word) == null)
		            continue;
		        totalScore += test.extract(word);
		    	}

		    //Summing up sentiment scores for all reviews of one Business ID
		    rate += totalScore;
		   //This variable keeps count of the number of reviews
		    j++;
	}

	//This variable gives a star rating for different range of final Sentiment score
	int  finalrate = (int) (rate/j);
	System.out.println("# of reviews is "+ j);
	System.out.println("final rating is "+ finalrate);
	int finalfinal=0;

		if(finalrate < 0)
			finalfinal = 1;
		else if(finalrate == 0)
			finalfinal = 2;
		else if(finalrate > 0 && finalrate <1.5) 
			finalfinal =3;
		else if(finalrate > 1.5 && finalrate < 3.0)
			finalfinal = 4;
		else
			finalfinal = 5;

	//System.out.println(finalfinal);
	double ROC=0.0f;
	ROC = Math.abs(finalfinal-TOTALSTARS);
	//System.out.println(ROC);
	ROC = (ROC/TOTALSTARS);

	System.out.println("Rating from the Review is "+ TOTALSTARS+" stars");
	System.out.println("Rating from Sentiment Analysis is "+finalfinal+" stars");
	System.out.println("Rate of Change is "+ROC);
    
	}

}
