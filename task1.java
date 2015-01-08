import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.apache.commons.lang.StringUtils;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.core.StopAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.Query;



public class task1 {


	public static String result;

	public static void main(String[] args) throws ParseException, IOException{


		//*************BID testing**********************

	//String bid = "UMP4EbpIcKD4urk0qXbncg"; //eye wear
	//String bid = "QAWdgDgWaFJYzJ2pGDjmdw";
	//String bid = "C7YKuZIZ-h-DpaSeBK69Fg"; //nutrition
	//String bid = "ykxf0XiJt-d76dBwfO_yew";
	//String bid = "-TIyxSc-lLmJzunje6MD9Q";

		//***********reviews used for testing*************

		//health and medicine
		//String r="dr. goldberg offers everything i look for in a general practitioner.  he's nice and easy to talk to without being patronizing; he's always on time in seeing his patients; he's affiliated with a top-notch hospital (nyu) which my parents have explained to me is very important in case something happens and you need surgery; and you can get referrals to see specialists without having to see him first.  really, what more do you need?  i'm sitting here trying to think of any complaint\n\ns i have about him, but i'm really drawing\n a blank.";
    	//grocery
		//String r="I\n like this place to pick up a few odds and ends now and again. Mainly when I have forgotten some exotic ingredient or other. The staff are friendly the shop well laid out but it doesnt gel with me as much as the shops in newington and leith. One of the main reasons is that it can be quite expensive for some items that can be had for half price else where.";
    	//System.out.println(r);
		//eyewear and optician
		//String r="I wandered in this shop while killing time waiting for someone.  It was a very happy accident. They have killer frames.  I always see the same old stuff at every place I look.  Not so with this place.  They take risks... Big risks.  The result is a shop with the hottest styles around and at very reasonable (not cheap) prices.\n\nThe selection is hot.  Lots of places carry names like Maui Jim... they have a more interesting list along with a few expected choices.  Vendors include Harry Lary (Paris), Kyoto, Booth & Bruce England), Etnia (Barcelona), ProDesign (Denmark), Theo (Belgium) and others.  My faves this week are the frames by harry lary and by etnia.\n\nThe employees were very helpful, considering that i didn't get anything.  I suspect that they were not employees, probably the owner. \n\nI will go back for a pair of glasses from them when i get my eye issues resolved (surgery).  Until then, I will just have to stop in once in a while to try on a few and add them to my wishlist.";
    	//entertainment->media
		//String r="A nice (unassuming) place to throw a few bowling balls and drink beers with your friends.\n\nIt's not the embodiment of the latest in bowling technology, but who cares? The food is greasy, the beer is cold, and it's your turn -- let's see what ya got!\n\nAlso, the fact that this is the only alley in town that has helped me procure balls for Mondo Croquet (http://twurl.nl/gqx3il) easily outweighs their lack of cutting edge scoring systems.";
		//String r="Got invited here by a few friends the other night. It's primarily a softball/volleyball/recreational center, although I spent my time inside in the bar.\n\nThe bar area is massive and includes a stage and dance floor. When I inquired as to whether there were ever any bands, I was told that they only do karaoke every so often. Seems like a waste of a nice entertainment area.\n\nVery limited taps; most beverages are served in bottles and cans. Prices are super cheap; tall boys of most anything run in the $2.25 - $2.50 range.\n\nVery limited menu; only a few easy-to-prepare items.\n\nBest suited for grabbing a cold one after a game of softball than as a destination point. \n\nThe bartenders were friendly and super fast - a bonus in my book. Thanks to Gretchen & Staci for the excellent service.";
		String r="Excellent lunch buffet, quick service.";
		//String r="Their food was alright, but their flan was so, so, so good. Damn, I regret that I didn't just go and grab more bowls of it. It IS a buffet, after all.";
		//String r="Valet is not exactly the smoothest process in the world here, but it could be worse.  Overall though, we've always enjoyed our stays here since we've lucked out each time we've checked in early and received a larger corner room in return (definitely ask for it when checking in).  Room service has fairly good burgers, but the fries are what seal the deal.  :)  Of course, the crepes are delish!  It's a good place to stay, but if you have a larger budget...go to the Wynn.  ;)";
		//String r="LOVE going to Agave... Love the food... Love the decor... Love the MARGARITAS!!!! and great, great prices..I even picked it to eat at after our wedding. They were very accommadating letting me drop off my cake before hand.";
		predict(r);
}

	//Predict(input) takes the input review
	//extarcts business ID of that review
	//extracts all the reviews for that particular Business ID
	//and work with only USEFUL reviews 
	//to predict category of the Business
	public static void predict( String input) throws IOException, ParseException{

		//eliminate all line breaks in the review
		input=input.replace("\n","");

		String filepath=null;
		String [] review={};
		String bid=null;
		Map<String,String> recall_hm=new HashMap<String,String>();
		File f2;
		Path path1;
		char flag ='n';
		final File folder = new File("C:\\Desktop\\yelp");
		int z=0;

		//We have split the Review.json into 9 files so working with it becomes easier
		//Here we are seraching through each file to find the Business ID of the input review

		for (final File fileEntry : folder.listFiles()) {	

			//flag is used to check if Business ID was found in the current file or not
			//If found, then break out of this loop
			//if not, iterate through other JSON files to find the input review's Business ID
			if(flag=='y') 
			{   

				break;
			}

			else
			{
				if (fileEntry.isFile()) 
				{
					   z++;
					   //get the filepath for a file
			            f2 = new File(fileEntry.getPath());
			            path1 = Paths.get(f2.getPath());
			            //convert file to string for easy processing
			            filepath = new String(Files.readAllBytes(path1));

			            //store each entry in review file in String [] review
			            review= StringUtils.substringsBetween(filepath, "<\"votes\"", "end>");

			            //iterate through the array to find a macth for the input review
			            for(String t1:review)  
			            {
			            		//to find a match, we need to extract only the review text
			                	String textreview=StringUtils.substringBetween(t1,"\"text\": \"", "\", \"type\""); 

			                	//replace any line breaks
			                	textreview=textreview.replace("\\n","");

			                	//check if textreview matches the input review
			                	if(textreview.equals(input))
			                	{
			                		//if it does, then extraxt the Business ID for that review
			                	   	bid=StringUtils.substringBetween(t1,"\"business_id\": \"",">");	
				                	System.out.println("Business id:: "+bid);

				                	//set flag to 'y', indicating that Business ID for the input review has been found
				                	flag='y';
			                	}

			            }
			    }

			}//else

		}//for1


		//Hash Map to store all the USEFUL and voted reviews
		//Key is simply an integer value, starting with 1,2,...,no-of-USEFUL and VOTED-reviews-for-the-business-id
		//Value is the user text review
		Map<Integer,String> brev= new HashMap<Integer, String>();

		//Hash Map to store all the USEFUL but not voted reviews
		//Key is simply an integer value, starting with 1,2,...,no-of-USEFUL AND NON_VOTED-reviews-for-the-business-id
		//Value is the user text review
		Map<Integer,String> brev1= new HashMap<Integer, String>();

		//This variable is used for the key value of the Hash Map brev
		int i=0;
		//This variable is used for the key value of the Hash Map brev1
		int l=0;

		//store the content of the current review file in String [] review1
		String [] review1= StringUtils.substringsBetween(filepath, "<\"votes\"", "end>");

		//iterate through this array to extract all the reviews for one Busniess ID
		for(String temp1: review1){

			//if the array entry conatins Business ID
			//this evaluates true for all the reviews for a particular Business ID

			if(temp1.contains(bid)){

				//extract review text, required for predicting Category of the Business
				//funny - stores the funny tag score of the review
				//useful - stores the useful tag score of the review
				//cool -stores the cool tag score of the review
				String reviewtext = (StringUtils.substringBetween(temp1, "\"text\": ", ", \"type\""));
				String funny=StringUtils.substringBetween(temp1,"\"funny\": ", ",");
	            String useful=StringUtils.substringBetween(temp1,"\"useful\": ",",");
	            String cool=StringUtils.substringBetween(temp1,"\"cool\": ","},");

	            int fun=Integer.parseInt(funny);
	            int use=Integer.parseInt(useful);
	            int coo=Integer.parseInt(cool);

	            //if review is useful and voted, put in Hash map brev
	            if(use>=1 || coo >=1){
	          	  brev.put(i,reviewtext);
	          	  i++;
	          	 }//end if

	            //else if review is useful but not voted, put in hash map brev1
	          	  else if(use==0 && coo==0 && fun==0){
	          		  brev1.put(l,reviewtext);
	          		  l++;
	          	  }//end else

	           }//end outer-if

		}//end for

		//to store number of reviews, from the hashmap brev
		int numberofreviews=0;


		//*************************print hashmap-testing **********************************

		/*Set set = brev.entrySet();
	      Iterator k = set.iterator();
	      while(k.hasNext()) {
	         Map.Entry me = (Map.Entry)k.next();
	        // System.out.println(me.getKey() + ": ");
	      //  System.out.println(me.getValue());
	        numberofreviews++;
	      }
	      System.out.println("Number of useful reviews for this business id:: "+numberofreviews);*/


		  //this hash map stores all the categories and words
		  Map<String,String> category=task1.create_category(); 
	      Map<String,Float> hm1=new HashMap<String, Float>(); 

	      String value="";
	      //Used Stop Analyzer to perform Normalisation and removing stop words
	      Analyzer analyzer = new StopAnalyzer();

	      //for parsing the review text
	      QueryParser q=new QueryParser("",analyzer);

	      //iterate through every entry in the brev hashmap
	      //replace all special characters
	      //and parse each review text
	      for(Entry<Integer,String> temp2:brev.entrySet()){
	    	value = temp2.getValue(); 
	    	value = value.replaceAll("[-+,.^:!?]","");
	    	value = "" + q.parse(value);
	    	brev.put(temp2.getKey(),value);
	      }

	    	//
	    	float cnt=0f,j=0f,count_of_words=0f;
	    	//for every category entry in the hash map
			for(Entry<String,String> te1:category.entrySet()){

				//store all the category words in the array, all words are separated by **, 
				//so substringBetween get eavh word and stores in String [] cat_arr
				String[] cat_arr=StringUtils.substringsBetween(te1.getValue(), "*", "*");

				//to get the number of words in every Category
				int length_arr=cat_arr.length;

				//for every review in the hash map
				for(Entry<Integer, String> t1 :brev.entrySet()){

					//to get review text from the hashmap and split the sentence into words
					String[] rev_arr=t1.getValue().split("\\s+");

					//for each review word
					for(String str: rev_arr){
						//for each Category word
						for(String t:cat_arr){

							//if word in review matches word in Category
							if((str.toLowerCase()).contains(t.toLowerCase())){

								//increment count of words
								count_of_words++;
								//to calculate recall
								// adding an entry in haspmap with key as category and vaue is total number of words in the category 
								// appended to the words that are matching.
								//this is used while calculating recall value for categories that have words present in the review. 
								//again just a way to filter the categories set for the calculation of recall

								//checking if the entry for category is already present
								if(recall_hm.containsKey(te1.getKey()))
								{
									recall_hm.put(te1.getKey()+"*"+length_arr+"*",recall_hm.get(te1.getKey())+"*"+str+"*");
								}
								//if not,creating a new hashmap entry
								else
								{
									recall_hm.put(te1.getKey()+"*"+length_arr+"*","*" + str + "*");
								}
							}//end of review word if
					}//end of for - cat_arr
				}//end of for - rev_arr
					//storing the relevance score for a category.
					//used in category prediction
					//cnt => stores relevance score for a category for all reviews
					cnt=cnt+(count_of_words/length_arr);
					//initialising the variable for other category
					count_of_words=0; 
					}//for every review
				//inserting relevance score for each categry, for all reviews
				hm1.put(te1.getKey(), cnt);
				cnt=0;				
			}//for every category			


			Entry<String,Float> maxEntry=null;
			//to calculate maximum relevance score to predict the category
			for(Entry<String,Float> temp:hm1.entrySet()){
				if(maxEntry==null || temp.getValue()>maxEntry.getValue()){
					maxEntry=temp;
				}
			}


			System.out.println("The category for this business id is:: "+maxEntry.getKey());
			double recall=0.0f;

			//Evaluation metrics-recall

			//While calculating relevance score we have created a hasmap recall_hm to store data required for
			//recall calculation. 
			//to traverse through every entry in the recall hashmap
			for(Entry<String, String> temp: recall_hm.entrySet()){
				//to calculate recall for the category predicted by our algorithm 
				if(temp.getKey().contains(maxEntry.getKey())){
					String[] number=StringUtils.substringsBetween(temp.getValue(), "*", "*");
					int numerator=number.length;
					int denominator=Integer.parseInt(StringUtils.substringBetween(temp.getKey(),"*","*"));
					recall=(double) numerator/denominator;
					System.out.println("The recall value is " + recall);
					break;
				}
			}
			result = maxEntry.getKey();
	}//end of predict

	public static Map<String,String> create_category(){
		//*************Dictionary*************

		//To add words in our dictionary 
		Map<String,String> category=new HashMap<String,String>();
		  category.put("Eyewear & Opticians","*dispensing**optician**correction**vision**opthalmic**eyesight**sunglasses**glasses**goggles**designer**diesel**adidas**frames**lens**sight**eyewear*"); 
	      category.put("Nutritionists", "*nutrition**impacts**health**dietitian**nutritionists**vitamins**minerals**carbs*"); 
	      category.put("Orthopedists", "*arthritis**osteoarthritis**fractures**joint**replacement**spine**hip**foot**ankle**bone**joint**tendon**nerve**shoulder**elbow**runners**knee**erbs**palsy**tendonitis**neurofibromatosis**rickets**lyme**sciatica**cauda**equina**scoliosis**spinal**tenosis**cubital**carpal*"); 
	      category.put("Turkish","*ottoman**kebab**simit**menemen**pilaf**baklava**kadayif**manti**karniyarik**hunkarbegendi**dolma**tekmil**lahana**baklali**enginar**turkish*");
	      category.put("Chinese","*dimsum**szechuan**cantonese**mandarin**anhui**fujian**hunan**jiangsu**shandong**zhejiang**springroll**momos**wontons**dumplings**chow**mein**peking**chopsuey**manchow**kung**chinese*");
	      category.put("Slovakian","*bryndza**kapustnica**dolky**bryndzove**halushky**gulash**slovakian*");
	      category.put("French","*bisque**french**croissant**eclair**macarons**madeleine**souffle**crepes**tarte**quiche**fondue**potaufeu**foie**gras**confit**bouillabaisse**ratatouille**creperies*");
	   	  category.put("Indonesian","*tumpeng**sambal**satay**bakso**soto**nasi**gudeg**baso**gado**ayam**goreng**cabai**indonesian*");
	      category.put("Cambodian","*khmer**amok**prahok**kuyteav**sach**cambodian*");
	      category.put("American","*hotdog**pretzels**tex-mex**bagels**burgers**cookies**applepie**fries**corndog**cupcakes**salads**macroni**crabcakes**penutbutter**blt**pickles**cajun**creole**southern**american*");
	      category.put("Japanese","*sushi**donburi**onigiri**kare**raisu**chahan**chazuke**kayu**sashimi**yakizakana**soba**ramen**somen**yakisoba**nabe**oden**sukiyaki**shabu**chanko**yakitori**tonkatsu**yakiniku**nikujaga**teppanyaki**hiyayakko**yudofu**agedashidofu**yoshoku**korokke**miso**omuraisu**hayashi**hambagu**tempura**okonomiyaki**monjayaki**gyoza**chawanmushi**japanese*");
	      category.put("Czech","*dumplings**goulash**schnitzel**koleno**czech*");
	      category.put("Iranian and Persian","*koloocheh**masgati**pashmak**baghlavaa**qottab**chelow**kabab**khoresh**kuku**polo**dates**doogh**naan**dolma**saffron**kebab**morgh**shirazi**shir**berenj**maast**desser**miveh**iranian**persian*");
	      category.put("Russian","*beef**stroganov**bliny**caviar**coulibiac**dressed**herring**kasha**kissel**knish**kalduny**kholodets**kulich**kvass**lymonnyk**medovukha**okroshka**shchi**shashlyk**pirozhki**russian*");
	      category.put("Canadian","*maple**syrup**poutine**butter**tarts**nanaimo**bar**bannock**tourtiere**ketchup**chips**bacon**beavertails**fiddleheads**swiss**chalet**sauce**montreal**smoked**meat**canadian*");
	      category.put("Mexican","*tacos**nachos**burrito**tortilla**enchilada**guacamole**fajitas**baja**black**beans**chimichangas**queso**blanco**dip**salsa**chili**peppers**jalepeno**corn**rice**quesadilla**casserole**chalupa**chorizo**taquitos**chimichangas**pico**gallo**churros**cheese**mexican*");
	      category.put("Indian","*samosas**chicken**tikka**punjabi**curry**naan**paneer**paratha**sambhar**dhokla**rice**chaat**pakora**masala**chutney**rajma**tandoor**jalebi**chai**jamun**indian*");
	      category.put("Mongolian","*aaruul**airag**khorhog**boodog**gulyash**khuushuur**buuz**bansh**lapsha**dumplings**tsuivan**guriltai**shul**bantan**boortsog**mongolian*");
	      category.put("Ukrainian","*borshch**varenyky**banosh**brynza**uzvar**paska**ukrainian*");
	      category.put("Latin American","*cachapas**arepa**venezuelan**churrasco**empanada**tamal**chipa**feijoada**cod**fritters**pernil**alfajores**chimichurri**quinoa**pozole**cassava**gallo**pinto**latin**american**caribbean**brazilian**argentine**peruvian**columbian*");
	      category.put("Optometrists","*vision**health**problems**refractice**errors**therapy**pre**post**operative**routine**exams**refer**testing**glasses**tonometer**drops**dilation**examination**topical**therapeutic**measure**optics**retina**lens*"); 
	      category.put("Burmese","*tea**leaf**salad**shan**fish**mohinga**lahpet**kauk**swe**thoke**burmese*");
	      category.put("Polish","*pierogi**rosol**zurek**soup**barszcz**bigos**golabki**kopytka**goulash**polish*");
	      category.put("Middle Eastern","*hummus**manakeesh**halloumi**meddamas**falafel**tabouleh**ghanoush**fattoush**shanklish**shawarma**shish**tawook**dolma**baklava**knafeh**masgouf**gyro**pita**kibbeh**chickpea**garlic**mediterranean**arabian**middle**eastern**labanese*");
	      category.put("Malaysian","*roti**canai**jala**asam**laksa**curry**satay**nasi**lemak**popiah**asam**pedas**malaysian*");
	      category.put("Scottish","*porridge**haggis**tatties**neeps**scottish**shortbread**bridie**apple**bramble**pie**kippers**pudding**stovies**lorne**sausage**cranachan**atholl**brose**tablet**clootie**dumpling**scotch**scottish*");
	      category.put("Italian","*pizza**pasta**spheghetti**penne**macroni**cheese**lasagne**garlic**bread**bolognese**sauce**mozarella**fusilli**ravioli**tortellini**gnocchi**ricotta**pesto**risotto**tiramisu**minestrone**truffles**italian*");
	      category.put("Portugese","*frango**piri**piri**sardinhas**grelhadas**grilled**bacalhau**salted**cod**fish**arroz**tamboril**monkfish**rice**cozida**portuguesa**pork**cabbage**stew**feijoada**bean**caldeirada**camarao**prawns**pastel**nata**custard**pastry**doce**fino**do**algarve**marzipan**portuguese*");
	      category.put("Irish","*dublin**coddle**slow**cooker**corned**beef**cabbage**irish**soda**bread**colcannon**coffee**steak**guinness**pie**green**beer**lamb**stew**shepherd**champ**crubeens*");
	      category.put("British","*beef**wellington**scones**pies**mince**eccles**cakes**kedgeree**scotch**eggs**toad**hole**welsh**rarebit**roast**steak**ale**eton**mess**welsh**cawl**shepherd**yorkshire**puddings**sausage**rolls**rumbledethumps**cornish**pasties**jam**roly**poly**fish**chips**english**british*");
	      category.put("Greek","*greek**feta**moussaka**tiropites**baklava**horta**vrasta**avgolemono**tzatziki**pastitsio**galaktoboureko**fassolatha**spanakopita**youvetsi**dolmathakia**taramosalata**fassolakia**lathera**melomakarona**pork**souvlaki**domates**yemistes**tsoureki**keftethes**kourabiethes*");
	      category.put("Jewish","*challah**lox**gefilte**matzah**knishes**blintzes**cholent**jewish*");
	      category.put("Moroccan","*b’ssara**tagine**fish**chermoula**harira**kefta**couscous**makouda**zaalouk**b’stilla**moroccan*");
	      category.put("Himalayan/Nepalese","*daal**bhat**tarkari**chatamari**dheedo**aloo**tama**himalayan**nepalese*");
	      category.put("Taiwanese","*taiwanese**meatballs**chitterlings**scallion*");
	      category.put("German","*apfelstrudel**eintopf**kasespatzle**kartoffelpuffer**rote**grutze**sauerbraten**brezel**schwarzwalder**kirschtorte**schnitzel**wurst**bratwurst**german*");
	      category.put("Australian","*crab**sticks**barramundi**vegemite**spag**bol**chiko**roll**australian*");
	      category.put("Scandinavian","*smorrebrod**cloudberry**danish**hash**gravlax**scandinavian**lefse*");
	      category.put("Filipino","*lumpia**sinigang**chicken**afritada**cassava**cake**pancit**palabok**ube**pork**adobo**bistek**chicharon**calamansi**whiskey**sour**bibingka**sizzling**sisig**kare**halo**lechon**biko**kaldereta**arroz**caldo**ukoy**tocino**filipino*");
	      category.put("Education","*pre**schools**swimming**cooking**nurseries**highschool**middleschool**gardening**barre**musical**instruments**camps**library**language**summer**camps**recording**rehearsal**studio**driving**school**tutor**vocational**technical**tutor**art**class**first**aid**class**college**university**dance**elementary**education*");
	      category.put("Beauty/Salons","*hair**extensions**lash**removal**waxing**tattoo**spray**tanning**hair**salon**piercing**cosmetics**laser**nail**stylist**beauty*");
	      category.put("Entertainment","*tour**horseback**stadium**arenas**opera**ballet**resorts**golf**boxing**dj**bars**boating**hiking**gastro**pubs**clubs**skatingrinks**arts**venues**paintball**mountain**biking**golf**balloon**paddle**rock**climbing**pool**halls**magicians**fencing**tennis**comedy**nightlife**gokart**jazz**lasertag**lounge**ski**resort**surf**soccer**karaoke**casino**bowling**skydiving**scuba**horse**racing**archery**arcade**rafting**kayaking**hookah*");
	      category.put("Grocery","*supermarket**grocery**butcher**drug**farmers**baker**icecream**yogurt**meat**cheese**seafood**market*");
	      category.put("Stores","*fabric**costume**discount**formal**wear**outdoor**gear**glass**mirrors**firewood**books**mag**video**music**leather**goods**toy**uniform**shoe**aquarium**kitchen**bath**outlet**lingerie**mattress**electronics**men's**accessories**cards**stationery**vinyl**records**knitting**supplies**wholesale**bespoke**thrift**gift**vape**brasseries**candy**clothing**hardware**sports**antiques**furniture**store*");
	      category.put("Real estate","*estate**agents**mortgage**broker*");
	      category.put("Event planning","*wedding**planning**event**party**photography*");
	      category.put("Installation and Repair","*windshield**appliances**drywall**watch**windows**insulation**sewing**alteration**solar**computer**garage**carpet*");
	      category.put("Home Decor","*home**decor**cleaning**flooring**chimney**sweep**tinting**roofing*");
	      category.put("Auto","*auto**parts**supplies**customization**detailing**loan**tires**car**wash**share**dealers**automotive**towing**stereo**wheel**rim**automobile*");
	      category.put("Contractor","*contractor**buliding**movers**masonry**concrete*");
	      category.put("Rental","*rental**motorcycle**taxis**photobooth**video**Game**truck**vacation**limos*");
	      category.put("Bank","*bank**credit**union**insurance*");
	      category.put("Recreational","*dog**trampoline**recreational**amusement**parks**skate**playground**leisure**non-profit**community**centers**gym**sport**trainer**stadiums**arenas*");
	      category.put("Law","*law**DUI**attorny**criminal**defense**lawyer**personal**injury**bankruptcy**estate**notary*");
	      category.put("Hotel","*hotel**housing**retirement**home**guest**house*");
	      category.put("Office","*equipment**assistants**professional**office*");
	      category.put("Counselling","*college**counselling**career**financial**advising**life**coach*");
	      category.put("Professional","*artist**interior**design**employment**agencies**landscape**architect**barber**painter**electrician**profession**accountant**groomer*");
	      category.put("Film","*video**film**production**cinema**videographer**director**studio*");
	      category.put("Printing Service","*graphic**design**screen**printing**print*");
	      category.put("Services","*service**post**office**bail**bondsmen**police**nanny**community**non-profit**handyman**gardener**public**government**funeral**cemeteries**matchmaker**plumbing**dry**cleaning** laundry**registration**property**management**museum**shipping**courier**delivery**airport**airlines**shuttle**caterer**catering**tranportation**travel**gas**oil**change**station*");
	      category.put("Massage","*massage**day**spas**relax**calm**clean**yoga**oils**acupuncture**physical**therapy**chiropractor**reiki*");
	      category.put("Yoga","*peace**quiet**relax**calm**yoga**stress**meditation**center**hot**instructors**studio**practice**class**massage**vibe**peaceful**meditate**pilates*");
	      category.put("Fitness","*diet**goals**staff**gym**work**out**results**exercise**mirrors**machines**lost**cardio**weight**workout**equipments**motivation**positive**loss**trainers**tanning**coach**membership**fitness*");
	      category.put("Spa","*spa**dealer**pools**landscape**chemical**pedicures**massage**chairs**technicians**ankle**mini**retreat**hydrotherapy**facial**skin**care**hair**manicures**brows*");
	      category.put("Physical Therapy","*encouraging**atmosphere**upbeat**manual**physical**therapy**therapeutic**exercise**work**conditioning**hardening**functional**capacity**evaluations**vestibular**sports**medicine*");
	      category.put("Reflexology","*anxiety**asthma**cancer**treatment**cardiovascular**issues**diabetes**headaches**kidney**function**PMS**sinusitis**cure**peace**pressure**acupuncture**acupressure**massage*");
	      category.put("Hearing Aid Providers","*hearing**loss**impairment**hearing aid**ear**deaf**plastic**case**microphone**amplifier**speaker**vibration**physician*");
	      category.put("Cannabis Clinics","*psychoactive**euphoria**relaxation**psychological**dependence**primary**caregiver**cannbis*");
	      category.put("Naturopathic/Holistic","*naturopathic**holistic**mental**factors**social**factors**healing**power**nature**underlying causes**self-healing**self**responsibilty**counseling**hygiene**homeopathy**acupuncture**intravenous**injection**therapy**naturopathic**obstetrics**holistic**proactive*");
	      category.put("Neurologist","*neurologist**brain**spinal**cord**nervous**peripheral**system**headaches**epilepsy**stroke**tremor**parkinson**disease**chronic**pain**dizziness**numbness**tingling**movement**problem**seizures**nerves**memory**stiff**neck*");
	      category.put("Diagnostic Service","*diagnose**prescribe**monitor**patient**results**laboratory**testing**imaging**services**adhd**audiology**laboratory**bone**mineral**dentistometry**echocardiography**ultrasound**holter**monitoring**event**monitoring**sleep**study**diagnostic**services**Radiology**teraradiology**nurse*");
	      category.put("Cosmetic Dentist","*appearance**gums**orthodontics**prosthodontics**bonding**porcelain**veneers**laminates**caps**crowns**enameloplasty**gingivectomy**whitening**bleaching**gum**depigmentation**pontics**false**teeth**ultra-thin**sculpts**gold**amalgam**porcelain**bridges**resin**zirconia**oral**self**esteem**invisible**braces**implants**reshape**straighten**misaligned**lemgthen**sedation**contouring**stained**crooked**shape*");
	      category.put("Veterinarians","*nonhumans**companion**livestock**zoo**cats**docking**tails**debarking**dogs**crop**diagnose**treatment**after**care**clinical**signs**exotic**animal**conservation**laboratory**animals**equine**reptiles**husbandry**meat**milk**egg**poultry**flocks**ovine**sheep**bovine**cattle**porcine**swine**food**foodborne**zooologists**aquatic**fish**farms**pet**horses**veterinarians**vaccinations**wild**creatures**sharks**bite**kick**scratch**dirty*");
	      category.put("Rehabilitation Center","*inpatient**hospitals**less**costly**facilities**speech**occupational**therapy**mursing**care**lifetime**days**physical**medicine**rehab*");
	      category.put("Dentistry","*cavity**mucosa**odontology**tooth**size**structure**abnormalities**dentist**root**molar**abscessed**drill**cavity**decay**periodontal**pyorrhea**restoration**extraction**removal**implants**prosthetic**endodontic**checkup**epidemology**dentures**bridges**smiles**systemic**disease**decay**teeth**gum**oral**cleaning**maintainence**filling**extraction**polishing**floss**brush**developing**plaque**flouride**strong**healthy**X-ray**silver**filling*");
	      category.put("Life Coach","*counsels**encourage**careers**personal**challenges**examining**coach**client**growth*");
	      category.put("Psychologists","*behavior**studies**mental**counseling**social**consultation**psychotherapy**issues**depressed**angry**anxious**addictions**psychologist*");
	      category.put("Home Health Care","*home**illness**injury**convenient**effective**wound**care**nurtition**therapy**injections**monitoring**health**independecne**self**sufficient**needs**questions**blood**pressure**temperature**breathing**heartrate**pain**safety*");
	      category.put("Midwives","*care**mothers**infants**baby**skill**expertise**pregnancy**birth**optimal**recovery**maternity**trusting**women**primary**health**labor**postpartum**contraceptive**family**planning**medication**responsible**accountable**home**vaginal**reproduction**childbearing**cycle**midwives*");
	      category.put("Pediatricians","*care**infants**children**child**adolescents**congenital**defects**genetic**variance**development**issues**minors**decisions**guardianship**privacy**informed**family**emptional**medical**pediatric**nurse**acute**infectious**malignancies**development**functional**depression**critical**newborn**immunizations**behavior**fitness*");
	      category.put("Fertility Clinics","*clinics**fertility**infertility**miscarriages**semen**analysis**reprodcutive**technology**child**birth**clinic**quality**couples**assist**parents**unachieve**conception**pregnancy*");
	      category.put("Counselling and Mental Health","*absence**mental**health**well**being**self**worth**stress**dilema**decision**day**genetic**traumatic**physical**confused**anxious**scared**emotional**friend**family**counsellor**confiding**empathise**physical**sport**eat**healthy**balaned**break**neurotic**anxiety**depression**phobia**panic**schizophrenia**bipolar**personality**disorder**loss**dirty**untidy**reckless**psychological**social**nutrition**risk**perception**grief**service*");
	      category.put("Urologists","*urologists**male**female**urinary**tract**system**enlarge**prostate**cancers**testicular**infertility**kidney**stones**sexual**erectile**dysfunction*");
	      category.put("Allergists","*allergists**hypersensitive**immune**response**allergen**allergy**allergies**rash**antibodies**sensitization**symptoms**blocked**itchy**runny**watery**cough**peeling**swelling**bleeding**anaphylaxis**hives**anxiety**swollen**lips**asthma*");
	      category.put("Cardiologists","*cardiologists**heart**attack**stroke**cardiac**arrest**chest**pressure**heartbeat**weight**gain**lifestyle**ecg**dizzy**history**blood**vessels**echocardiogram**catheterization**angiogram**coronary*");
	      category.put("Periodontists","*periodontal**disease**dental**implants**oral**inflamation**cosmetic**gums**teeth**gingiva**alveolar**cementum**plaque**gingivitis**peri**implantis**scaling**root**planning**tartar**bleeding**swelling**mouthrinse**chlorhexidine**doxycycline**minocycline**antimicrobial**mouthwash**flap**brush**floss*");
	      category.put("Audiologist","*auditory**vestibular**hearing**balance**aid**loss**assistive**listening**device**cochlear**implant**communicate**speech**pathology*");
	      category.put("Podiatrists","*foot**ankle**leg**podiatric**medicine**physician**lower**extremity**anatomy**physiology*");
	      category.put("Ear, Nose, Throat Specialist","*otolarynology**otolaryngologists**ear**nose**throat**sinus**hearing**nasal**larynx**oral**pharynx**facial**cranial**paranasal**smell**respiration**aerodigestive**esophagus**infectious**deformities**reconstructive**inhalant*");
	      category.put("Endocrinologists","*glands**hormonal**menopause**diabetes**metabolic**osteoporosis**thyroid**endocrine**cholesterol**hypertension**infertility*");
	      category.put("Chiropractors","*chiropractor**neuromuscular**disorder**spine**exercise**ergonomy**biomechanical**structural**dearrangement**restore**neurological**pressure**chiropractic**spinal**nerve**lower**back**leg**neck**arthritic**injury**musculoskeletal**nervous**drug**free**handson**nutritional**dietary**joints**tissues**system**massage**sublaxation**biomechanics**table**adjustment**dislocate**manipulation**posture**self**care**stress**trigger**point*");
	      category.put("Health & Medical","*family**surgeons**medical**centers**walk-in**clinic**appointment**doctors**waiting**emergency**diagnostic**imaging**xrays**ct**scans**mri**hospitals**internal**medicine*");
	      category.put("Obstetricians & Gynecologists","*pregnancy**labor**peuperium**obstetrician**gynecologist**female**reproductive**reproduction**prenatal**pap**screening**family**planning**adolescent**endocrinology**infertile**infertility**delivery**urinary**tract**sonography**ultrasonography**ultrasound**maternal**fetal**newborn**baby**pelvic**childbirth**menopause**postpartum**ultrasound**weeks**blood**rubella**urinalysis**count**trimester**midwife**first**second**third**glucose**gestational**screening*");
	      category.put("Cosmetic Surgeon","*elective**liposuction**breast**augmentation**reduction**abdominoplasty**botox**hyaluronic**acid**laser**hair**removal**skin**microdermabrasion**tummy**tuck**plastic**boob**job**eyelid**silicon**gel**prosthetics**mastectomy**grafting**saline**lift**buttock**chemical**peel**wrinkled**nose**ears**ear**facelift**brow**chin**cheek**collagen*");
	      category.put("Dermatologists","*skin**hair**nails**nail**mucous**membrane**eczema**acne**psorasis**infection**wrinkles**aging**acne**dermatology**scars**birthmark**neonatal**skin**fat**oral**membrane**dermatohistopathology**cosmetic**laser**radiotherapy**photodynamic**warts**dermatitis**atopic*");
	      category.put("Psychiatrists","*psychological**mental**emotional**behavioral**psychotherapy**stress**crisis**addiction**forensic**geriatric**psychosomatic**sleep**neurophysiology**crosscultural**neurodevelopmental**anxiety**schizophrenia**consultation*");
	      category.put("Radiologists","*medical**imaging**mri**cancer**symptom**radiology**diagnostic**interventional**oncology*");
	      category.put("Oncologists","*cancer**chemotherapy**targeted**tumor**oncologist**biopsy**cells**uterine**cervical**leukemia**osteosarcoma**ewings**sarcoma**lymphomas**myelomas**biopsies**endoscopy**ultrasound**malignant**mammography**neurooncology**hematooncology**urooncology*");
	      category.put("Speech Therapists","*speech**voice**language**sound**stutter**fluency**rhythm**pitch**harsh**counselling**articulation**resonance**dysphagia**receptive**expressive**phonation**syntax**semantics**cognitive**swallowing*");
	      category.put("Ophthalmologists","*eye**visual**vision**lenses**spectacles**ocular**manifestation**distorted**vision**floaters**flashes**haloes**peripheral**tearing**eyelid**cornea**pupil**intraocular**iris**drops**glaucomia**vitreoretinal*");

	      return category;		
	}	
}
 
API 
