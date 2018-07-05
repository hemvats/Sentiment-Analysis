package com.company;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

class WordRateFeatures {
	double rating, probOfWordGivenPositives, probOfWordGivenNegatives;
	int positiveReviewsCount;
	int negativeReviewsCount;
	WordRateFeatures(double r, int p, int n) {
		this.rating = r; this.positiveReviewsCount = p;
		this.negativeReviewsCount = n;
	}
}
class MovieReview {
	private File[] posFiles;
	private File[] negFiles;
	private boolean useBinaryNB;
	HashMap<String, WordRateFeatures> rateWord = new HashMap<>();
	private Stemmer st = new Stemmer();
	private Logger logger = new Logger();
	MovieReview(String poses, String negs, boolean useBinarNB) {
		File pf = new File(poses);
		posFiles = pf.listFiles();
		File nf = new File(negs);
		negFiles = nf.listFiles();
		useBinaryNB = useBinarNB;
	}
	/*
	 * stopwords: ArrayList of all StopWords
	 *
	 *
	 */
	void getReqWordsFromReviews(ArrayList<String> stopwords, boolean type, boolean useSW) throws IOException{
		double score;
		File[] localBase;
		if (type) {
			localBase = posFiles;
		} else {
			localBase = negFiles;
		}
		for (File pfile: localBase) {
			String f = pfile.getName();
			String filename[] = f.split("\\.")[0].split("_");
			int rating = Integer.parseInt(filename[1]) - 5;
			Scanner sc = new Scanner(new File(pfile.getAbsolutePath()));
			ArrayList<String> req_words;
			String each_data = "";
			while (sc.hasNextLine()) {
				each_data = each_data.concat(sc.nextLine());
			}
			each_data = st.cleanWords(each_data);
			req_words = st.removeAllStopWords(each_data, stopwords, useSW);

			HashMap<String, Double> frequencyMap = st.getFrequency(each_data, req_words);
			for (String key: frequencyMap.keySet()) {
				int frqScore = (!useBinaryNB)?frequencyMap.get(key).intValue(): 1;
				score = frequencyMap.get(key) * rating; // Importance on Frequency and Rating for Word Sentiment Score
				if (score == 0) {continue;} // Since we are considering positive ot negative for training, it is necessary for value to >< 0 not = 0
				if (!rateWord.containsKey(key)) {
					if (score > 0) {
						rateWord.put(key, new WordRateFeatures(score, frqScore, 0));
					} else {
						rateWord.put(key, new WordRateFeatures(score, 0, frqScore));
					}
				} else {
					if (score > 0) {
						rateWord.put(key, new WordRateFeatures(rateWord.get(key).rating + score,
								rateWord.get(key).positiveReviewsCount + frqScore,
								rateWord.get(key).negativeReviewsCount));
					} else {
						rateWord.put(key, new WordRateFeatures(rateWord.get(key).rating + score, rateWord.get(key).positiveReviewsCount,
								rateWord.get(key).negativeReviewsCount + frqScore));
					}
				}
			}
		}
	}

	int docClassifier(ArrayList<String> word_list, String filename, File f, boolean useS) throws IOException{
		double posProb = 1, negProb = 1, score = 0;
		for (String dword: word_list) {
			if (rateWord.containsKey(dword)) {
				score += rateWord.get(dword).rating;
				posProb *= rateWord.get(dword).probOfWordGivenPositives;
				negProb *= rateWord.get(dword).probOfWordGivenNegatives;
			}
		}
		// System.out.println(score);
		logger.LogTestsResults(f, filename, score, (posProb/negProb > 1)? 1: ((posProb/ negProb < 1)? -1: 0));
		int score_b = (score > 0)? 1: (score < 0)? -1: 0;
		if (f.getName().contains("positive") && useS) {
			return (posProb/negProb > 1)? 1: ((posProb/ negProb < 1)? -1: score_b);
		}
		return (posProb/negProb > 1)? 1: ((posProb/ negProb < 1)? -1: 0);
	}

}
