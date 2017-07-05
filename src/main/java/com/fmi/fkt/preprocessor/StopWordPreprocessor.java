package com.fmi.fkt.preprocessor;

import java.util.Arrays;
import java.util.List;

public class StopWordPreprocessor {

	private static final List<String> DEFAULT_ENLGISH_STOPWORDS = Arrays.asList("a", "an", "and", "are", "as", "at",
			"be", "but", "by", "for", "if", "in", "into", "is", "it", "no", "not", "of", "on", "or", "such", "that",
			"the", "their", "then", "there", "these", "they", "this", "to", "was", "will", "with");
	
	public static String processText(String text) {
		return removeStopWords(removeNonLetters(text)).replaceAll(" +", " ");
	}
	
	private static String removeNonLetters(String text) {
		return text.replaceAll("[^a-zA-Z\n ]", "").trim();
	}
	
	private static String removeStopWords(String text) {
		for (String stopWord : DEFAULT_ENLGISH_STOPWORDS) {
			text = text.replaceAll(String.format("\\b%s\\b", stopWord), "");
		}
		return text.trim();
	}
}
