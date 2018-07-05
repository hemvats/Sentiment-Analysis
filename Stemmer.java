package com.company;

import java.util.*;

class Stemmer {
	String cleanWords(String paragraph) {
		paragraph = paragraph.trim();
		paragraph = paragraph.toLowerCase();
		String regex = "[\\t\\n\\r,.:;?!]";
		paragraph = paragraph.replaceAll(regex, "");
		return paragraph;
	}

	ArrayList<String> removeAllStopWords(String para, ArrayList<String> stopwords, boolean flag) {
		Set<String> all_words = new HashSet<>(Arrays.asList(para.split(" ")));
		if (!flag) {
			return new ArrayList<>(all_words);
		}
		for (String s: stopwords) {
			if (all_words.contains(s)) {
				all_words.remove(s);
			}
		}
		return new ArrayList<>(all_words);
	}
	HashMap<String, Double> getFrequency(String para, ArrayList<String> words) {
		HashMap<String, Double> frqMap = new HashMap<>();
		List<String> word_list = Arrays.asList(para.split(" "));
		for (String word: words) {
			if (word.matches("[a-zA-Z]+") && word.length() > 1) {
				frqMap.put(word, (double)(Collections.frequency(word_list, word)));
			}
		}
		return frqMap;
	}
}
