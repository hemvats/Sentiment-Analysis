#### AIM : 

**Your task is to classify whether a given review has a positive or negative tone using naive
  Bayes classifier.**
  
**DATASET** 

[http://ai.stanford.edu/~amaas/data/sentiment/](http://ai.stanford.edu/~amaas/data/sentiment/)

**USAGE**

Test with **jdk Version 1.8.0_131 Java 8**
 
1.In the project directory in terminal type
  __cd out/production/Sentiment_Analysis/__
    
2.Next:
  **java com.company.Main -dataFolder <Absolute Path for Data> -testFolder <Absolute Path for tests> -useScore True**
    
    **Example:** java com.company.Main -dataFolder /home/shuttle3468/aclImdb/ -testFolder /home/shuttle3468/IdeaProjects/Sentiment_Analysis/logs/ -useScore True
    
3.Results in detail can be seen in logs Folder in your respository Directory


### OUTPUT

0. Using the given Expected Ratings

    - 11108.000000 + 1392.000000 = 12500.000000
    
    - Accuracy for positive tests = 88.864000%
    
    - Log Created : negative_reviews.txt
    
    - 2267.000000 + 10233.000000 = 12500.000000
    
    - Accuracy for negative tests = 81.864000%
    
1. Using Naive Bayes and StopWords (java com.company.Main -dataFolder /home/shuttle3468/aclImdb/ -testFolder /home/shuttle3468/IdeaProjects/Sentiment_Analysis/logs/ -useScore False -useStopWords True -useBinaryNB False)

    - **Accuracy for Positive Set of test Cases 71.024000%**
    
    - **Accuracy for Negative Set of test Cases 69.040000%**
    
    - **Precision for Negative Set of test Cases 69.040000%**
    
    - **Precision for Positive Set of test Cases 86.227661%**
    
    - **Recall for Negative Set of test Cases 85.887739%**
    
    - **Recall for Positive Set of test Cases 69.642297%**
    
    - **F-measure for Negative Set of test Cases 76.547809**
    
    - **F-measure for Positive Set of test Cases 77.052595**
    
2. Using Extended Binary Naive Bayes and StopWords (java com.company.Main -dataFolder /home/shuttle3468/aclImdb/ -testFolder /home/shuttle3468/IdeaProjects/Sentiment_Analysis/logs/ -useScore False -useStopWords True -useBinaryNB True)

    - **Accuracy for Positive Set of test Cases 69.784000%**
    
    - **Accuracy for Negative Set of test Cases 68.928000%**
    
    - **Precision for Negative Set of test Cases 68.928000%**
    
    - **Precision for Positive Set of test Cases 85.553158%**
    
    - **Recall for Negative Set of test Cases 85.399941%**
    
    - **Recall for Positive Set of test Cases 69.191719%**
    
    - **F-measure for Negative Set of test Cases 76.284917**
    
    - **F-measure for Positive Set of test Cases 76.507477**
    
3. Using Extended Binary Naive Bayes, StopWords and Scores ( 2 layer)

    - **Accuracy for Positive Set of test Cases 88.216000%**
    
    - **Accuracy for Negative Set of test Cases 86.088000%**
    
4. Using Extended Naive Bayes, StopWords and Scores ( 2 layer)

    - **Accuracy for Positive Set of test Cases 88.656000%**
    
    - **Accuracy for Negative Set of test Cases 85.304000%**

5. Using Extended Binary Naive Bayes without stopword Removal (java com.company.Main -dataFolder /home/shuttle3468/aclImdb/ -testFolder /home/shuttle3468/IdeaProjects/Sentiment_Analysis/logs/ -useScore False -useStopWords False -useBinaryNB True)
    
    - **Accuracy for Positive Set of test Cases 60.904000%**
    
    - **Accuracy for Negative Set of test Cases 66.160000%**
    
    - **Precision for Negative Set of test Cases 66.160000%**
    
    - **Precision for Positive Set of test Cases 80.731707%**
    
    - **Recall for Negative Set of test Cases 81.986716%**
    
    - **Recall for Positive Set of test Cases 64.282699%**
    
    - **F-measure for Negative Set of test Cases 73.227963**
    
    - **F-measure for Positive Set of test Cases 71.574296**







 