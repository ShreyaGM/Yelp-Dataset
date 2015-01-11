Yelp-Dataset
============
Yelp DataSet Challenge

Task1) Predicting Category of a Business by using user reviews.
      Method - Information Retrieval Approach
      
      We used text mining to collect all the useful reviews for one Business ID and worked on those reviews to predict the Business Category. All the useful reviews were parsed and normalised. Also stop words were removed with the help of StopwordAnalyzer(). We created a Unigram Dictionary with a set of words for each Category. We designed and implemented own algorithm to accurately predict the Category, by calculating relevance score for each Category in the Dictionary. The Category with the highest relevance score is the category of the business.

      Evaluation Metric - Recall

Task2) Predicting rating of Business from user reviews
      Method - Information Retrieval Approach
      
      As in Task1, we used text mining to collect all the user reviews for one Business ID. Parsing, normalisation and stop word removal was performed on the user reviews. Sentiment Analysis of each user review was done and a sentiment score was derived. All such scores were summed up and averaged out to get a final sentiment score. This was used to predict the rating of a business.
      
      Evaluation Metric - Accuracy

References
1) SentiWordNet.com for Sentiment Analysis and Dictionary.

Contributions
1) Khusbhoo Modi - Implementing logic for merging 9 json files so we could work with all files at the same time. 
2) Mrunal Lele - Implementing logic for Relevance score calculation in task1, by which Category of Business was predicted.
3) Shambhavi Dhargalkar - Finding Evaluation Metric(Rate of Change) in task2.
4) Shreya Ghattamaraju Maruthi - Implementing logic for getting rating of business from Sentiment Analysis.

Dictionary words - 
1) Khusbhoo M - Active life etc.
2) Mrunal L - Doctors & Health
3) Shambhavi - Restaurants & Cuisine
4) Shreya G M - Services & Entertainment.
