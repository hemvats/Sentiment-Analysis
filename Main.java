package com.company;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class Main {
	private File imdb_vocab_file;
	private File imdb_er_file;
	private File stopwords_file;
	private File wordrating_file;
	private File logpos, logneg;
	private HashMap<String, Double> dictionaryForVocab_ER;
	private ArrayList<String> stopwords;
	private static String dataFolder;
	private static String[] trainingFolder = new String[2];
	private static String[] testingFolder = new String[2];
	private Logger logger = new Logger();
	private Stemmer st = new Stemmer();
	private Main() {
		imdb_vocab_file = new File(dataFolder + "imdb_vocab.txt");
		imdb_er_file = new File(dataFolder + "imdbEr.txt");
		dictionaryForVocab_ER= new HashMap<>();
		stopwords_file = new File(dataFolder + "stopwords.txt");
		stopwords = new ArrayList<>();
	}
	private static void helpOptions() {
		System.out.println("Usage:\n Parameters Required:");
		System.out.println("-dataFolder\t<pathname>");
		System.out.println("-testFolder\t<pathname>");
		System.out.println("-useScore\ttrue/false");
		System.out.println("-useStopWords\ttrue/false");
		// TODO: Option for Use Plain Naive Bayes of Use Binary Naive Bayes
		System.out.println("-useBinaryNB\ttrue/false");
	}
    private void getBagOfWords() throws IOException{
		Scanner sc1 = new Scanner(imdb_vocab_file);
		Scanner sc2 = new Scanner(imdb_er_file);
		Scanner sc3 = new Scanner(stopwords_file);
		String s1, s2;
		while (sc1.hasNextLine()) {
			s1 = sc1.nextLine();
			s2 = sc2.nextLine();
			dictionaryForVocab_ER.put(s1, Double.parseDouble(s2));
		}
		sc1.close(); sc2.close();
		while (sc3.hasNextLine()) {
			stopwords.add(sc3.nextLine());
		}
		sc3.close();
    }
    /*
    	Function: getFilesFromLocation
    	Given Location of Data we get the Inodes there and use it for getting the test and train Data
     */
    private void getFilesFromLocation() {
		File folder = new File(dataFolder);
		File[] listOfFiles = folder.listFiles();
		if (listOfFiles != null) {
			for(File node: listOfFiles) {
				if (node.isDirectory()) {
					String name = node.getName();
					switch (name) {
						case "train":
							trainingFolder[0] = dataFolder + name + "/pos/";
							trainingFolder[1] = dataFolder + name + "/neg/";
							break;
						case "test":
							testingFolder[0] = dataFolder + name + "/pos/";
							testingFolder[1] = dataFolder + name + "/neg/";
							break;
						default:
							System.out.println("Not the folder to look for");
					}
				}
			}
			System.out.println("Training Data Located at " + trainingFolder[0] + " " + trainingFolder[1]);
			System.out.println("Testing Data Located at " + testingFolder[0] + " " + testingFolder[1]);
		}
	}

	private double useTestFiles(int index) throws IOException{
    	File tests = new File(testingFolder[index]);
    	File[] test_cases = tests.listFiles();
    	ArrayList<String> req_words;
    	if (test_cases != null) {
			double positives = 0.0, negatives = 0, total = test_cases.length;
			for (File test : test_cases) {
				String each_data = "";
				test = new File(test.getAbsolutePath());
				Scanner sc = new Scanner(test);
				while (sc.hasNextLine()) {
					each_data = each_data.concat(sc.nextLine());
				}
				each_data = st.cleanWords(each_data);
				req_words = st.removeAllStopWords(each_data, stopwords, true);
				double sentimentScore = 0.0;
				for (String s: req_words) {
					if (dictionaryForVocab_ER.containsKey(s)) {
						sentimentScore += dictionaryForVocab_ER.get(s);
						// System.out.println(s + " " + dictionaryForVocab_ER.get(s).toString());
					}
				}
				// System.out.println(sentimentScore);
				if (sentimentScore > 0) {
					if (index == 0) {
						logger.LogSentimentScores(logpos, test.getName() + " has a sentiment of " + sentimentScore);
					} else {
						logger.LogSentimentScores(logneg, test.getName() + " has a sentiment of " + sentimentScore);
					}
					positives++;
				} else if (sentimentScore < 0) {
					if (index == 1) {
						logger.LogSentimentScores(logneg, test.getName() + " has a sentiment of " + sentimentScore);
					} else {
						logger.LogSentimentScores(logpos, test.getName() + " has a sentiment of " + sentimentScore);
					}
					negatives++;
				}
				sc.close();
			}
			System.out.println(String.format("%f + %f = %f", positives, negatives, total));
			return (index == 0) ? positives / total : negatives/total;
		} else {
    		return -1.0; //  Incase Folder is Empty
		}
	}
    public static void main(String[] args) throws IOException{
		// write your code here
		boolean useScore = true, useStopWord = true, useBinaryNB = false;
		HashMap<String, String> arguments = new HashMap<>();
		String testFolder;
		for (int i= 0; i< args.length; i+=2) {
			arguments.put(args[i], args[i + 1]);
		}
		if (arguments.containsKey("-dataFolder") && arguments.containsKey("-testFolder")) {
			dataFolder = arguments.get("-dataFolder");
			testFolder = arguments.get("-testFolder");
			if (arguments.containsKey("-useStopWords")) {
				useStopWord = Boolean.parseBoolean(arguments.get("-useStopWords"));
			}
			if (arguments.containsKey("-useScore")) {
				useScore = Boolean.parseBoolean(arguments.get("-useScore"));
			}
			if (arguments.containsKey("-useBinaryNB")) {
				useBinaryNB = Boolean.parseBoolean(arguments.get("-useBinaryNB"));
			}
			System.out.println("Use Stopward Status" + useStopWord);
			System.out.println("Use Binary NB Status " + useBinaryNB);
			Main startProgram = new Main();
			// Step1: Put the words and corresponding ratings into Dictionary
			startProgram.getBagOfWords();
			System.out.println(startProgram.dictionaryForVocab_ER.size());
			// Now get the data from stopwords File and check with the test using the Expected ratings
			// Get test and train folders from the given location
			startProgram.getFilesFromLocation();
			// Now get the test Files
			startProgram.logpos = startProgram.logger.createLogFile(testFolder, "postive_reviews", false);
			double res_poses = startProgram.useTestFiles(0);
			System.out.println(String.format("Accuracy for positive tests = %f", res_poses * 100) + "%");
			startProgram.logger.LogSentimentScores(startProgram.logpos, "Accuracy for positive tests " + res_poses * 100);

			startProgram.logneg = startProgram.logger.createLogFile(testFolder, "negative_reviews", false);
			double res_neg = startProgram.useTestFiles(1);
			System.out.println(String.format("Accuracy for negative tests = %f", res_neg * 100) + "%");
			startProgram.logger.LogSentimentScores(startProgram.logneg, "Accuracy for negative tests" + res_neg * 100);

			// Training Data Starts Here. THE REAL WORK IS HERE
			MovieReview movieReview = new MovieReview(trainingFolder[0], trainingFolder[1], useBinaryNB);
			movieReview.getReqWordsFromReviews(startProgram.stopwords, true, useStopWord);
			movieReview.getReqWordsFromReviews(startProgram.stopwords, false, useStopWord);
			int vocabularySize = movieReview.rateWord.size();
			// This part rates the words based on their sentiment
			startProgram.wordrating_file = startProgram.logger.createLogFile(testFolder,"trainedRatings", false);
			for (String w: movieReview.rateWord.keySet()) {
				WordRateFeatures wordR = movieReview.rateWord.get(w);
				startProgram.logger.LogWordRatingFromTraining(startProgram.wordrating_file, w, wordR.rating,
						wordR.positiveReviewsCount, wordR.negativeReviewsCount);
				// Probability of the word is negative given given the review is negative
				wordR.probOfWordGivenNegatives = ((double) wordR.negativeReviewsCount + 1)/ ((double)vocabularySize +
						(double)wordR.negativeReviewsCount + (double)wordR.positiveReviewsCount);
				// Probability of the word is positive given given the review is positive
				wordR.probOfWordGivenPositives = ((double)wordR.positiveReviewsCount + 1)/ ((double)vocabularySize +
						(double)wordR.negativeReviewsCount + (double)wordR.positiveReviewsCount);
			}
			// Now create the probabilities for training.
			// Here is for positive tests accuracy
			File psf= startProgram.logger.createLogFile(testFolder,"positive_tests_details", false);
			File positiveFolder[] = new File(testingFolder[0]).listFiles();
			double pos = 0, neg = 0, negp = 0, posn = 0;
			if (positiveFolder != null) {
				for (File f : positiveFolder) {
					Scanner sc = new Scanner(new File(f.getAbsolutePath()));
					String each_rev = "";
					while (sc.hasNextLine()) {
						each_rev = each_rev.concat(sc.nextLine());
					}
					each_rev = startProgram.st.cleanWords(each_rev);
					int res = movieReview.docClassifier(startProgram.st.removeAllStopWords(each_rev, startProgram.stopwords, useStopWord),
							f.getName(), psf, useScore);
					if (res == 1) {pos++;}
					else if(res == -1) {negp++;}
				}
				System.out.println(String.format("Accuracy for Positive Set of test Cases %f",
						(pos/ 12500.0) * 100) + "%");
			}
			// Here is for negative tests accuracy
			File nsf= startProgram.logger.createLogFile(testFolder,"negative_tests_details", false);
			File negativeFolder[] = new File(testingFolder[1]).listFiles();
			if (negativeFolder != null) {
				for (File f : negativeFolder) {
					Scanner sc = new Scanner(new File(f.getAbsolutePath()));
					String each_rev = "";
					while (sc.hasNextLine()) {
						each_rev = each_rev.concat(sc.nextLine());
					}
					each_rev = startProgram.st.cleanWords(each_rev);
					int res = movieReview.docClassifier(startProgram.st.removeAllStopWords(each_rev, startProgram.stopwords,
							useStopWord), f.getName(), nsf, useScore);
					if (useScore) {if (res <=0) {neg++;} else {posn++;}}
					else if (!useScore) {
						if (res < 0) {
							neg++;
						} else {posn++;}
					}
				}
				System.out.println(String.format("Accuracy for Negative Set of test Cases %f", (neg/ 12500.0)
						* 100)+ "%");
			}
			double recallp, precp, recalln, precn;
			recallp = (pos/(pos + posn) ) * 100;
			recalln = (neg/(neg + negp) ) * 100;
			precp = (pos/(pos + negp) ) * 100;
			precn = (neg/(neg + posn) ) * 100;
			System.out.println(String.format("Precision for Negative Set of test Cases %f", precn)+ "%");
			System.out.println(String.format("Precision for Positive Set of test Cases %f", precp)+ "%");

			System.out.println(String.format("Recall for Negative Set of test Cases %f", recalln) + "%");
			System.out.println(String.format("Recall for Positive Set of test Cases %f", recallp)+ "%");

			System.out.println(String.format("F-measure for Negative Set of test Cases %f", (2 * precn* recalln/(precn + recalln) ) ));
			System.out.println(String.format("F-measure for Positive Set of test Cases %f", (2 * precp* recallp/(precp + recallp) ) ));
		} else {
			helpOptions();
		}

    }
}
