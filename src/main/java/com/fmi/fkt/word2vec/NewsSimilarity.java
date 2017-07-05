
package com.fmi.fkt.word2vec;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import org.deeplearning4j.models.embeddings.loader.WordVectorSerializer;
import org.deeplearning4j.models.embeddings.wordvectors.WordVectors;

public class NewsSimilarity {

  /**
   * Path to word of vector file.
   */
  public static final String WORD_VECTORS_PATH = "C:\\Users\\i319962\\Documents\\Projects\\Inoweek2016 - Tweets\\glove.twitter.27B\\glove.twitter.27B.200d.txt";

  /**
   * The size of the vectors of word2vec model.
   */
  public static final int VECTORS_SIZE = 200;

  /**
   * Path to the folder that contains the news.
   */
  public static final String NEWS_GROUPS_PATH = "C:\\Users\\i319962\\Documents\\fmi\\newsgroups\\autos";

  /**
   * Map that contains title of article to its sum vector. The sum vector is received by adding all vectors of the words in the article.
   */
  public static Map<String, double[]> newsSumVectors = new HashMap<>();

  public static void main(String[] args) throws FileNotFoundException, UnsupportedEncodingException {

    final WordVectors word2vecModel = loadWord2vecModel();
    calculateNewsSumVectors(word2vecModel);
    System.out.println(newsSumVectors);

    // System.out.println(getMostRelatedNews("PUMA FIRST-HALF LOSS ABOUT 14 MLN MARKS"));
    Map<String, double[]> mostRelatedNews = getMostRelatedNewsUsingCosineSimilarity("75389m");
    System.out.println(mostRelatedNews);
    for (Entry<String, double[]> entry : mostRelatedNews.entrySet()) {
      System.out.println("entry: " + entry.getKey());
      System.out.println("cosine: " + cosineSimilarity(newsSumVectors.get("75389m"), entry.getValue()));
    }
  }

  /**
   * Load Word2vec model.
   * 
   * @return
   */
  private static WordVectors loadWord2vecModel() {
    System.out.println("Loading word vectors (" + WORD_VECTORS_PATH + ") ....");
    WordVectors wordVectors = WordVectorSerializer.readWord2VecModel(new File(WORD_VECTORS_PATH));
    return wordVectors;
  }

  /**
   * Iterate over all news and calculate their sum vectors.
   * 
   * @param wordVectors
   */
  private static void calculateNewsSumVectors(final WordVectors wordVectors) {
    // try (Stream<Path> paths = Files.walk(Paths.get(NEWS_FOLER))) {
    try (Stream<Path> paths = Files.walk(Paths.get(NEWS_GROUPS_PATH))) {
      paths.skip(1).forEach(path -> {
        List<String> wordsInArticle = extractOnlyWords(readFile(path));
        double[] sumVector = new double[VECTORS_SIZE];
        Arrays.fill(sumVector, 0.0);
        for (String word : wordsInArticle) {
          double[] wordVector = wordVectors.getWordVector(word);
          if (wordVector == null) {
            wordVector = new double[VECTORS_SIZE];
            Arrays.fill(wordVector, 0.0);
          }
          sumVector = vectorAddition(sumVector, wordVector);
        }
        newsSumVectors.put(path.getFileName().toString(), sumVector);
      });
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * Extract only the words from a string.
   * 
   * @param content
   * @return
   */
  private static List<String> extractOnlyWords(String content) {
    List<String> words = new ArrayList<String>();
    String input = content;
    Pattern p = Pattern.compile("[\\w']+");
    Matcher m = p.matcher(input);
    while (m.find()) {
      words.add(input.substring(m.start(), m.end()));
    }
    return words;
  }

  /**
   * Get the content of a file.
   * 
   * @param path - the file.
   * @return - the content of the file.
   */
  private static String readFile(Path path) {
    byte[] encoded;
    String fileContent = "";
    try {
      encoded = Files.readAllBytes(path);
      fileContent = new String(encoded, "UTF-8");
    } catch (IOException e) {
      e.printStackTrace();
    }
    return fileContent;
  }

  /**
   * Mathematical addition of two vectors.
   * 
   * @param vectorA
   * @param vectorB
   * @return
   */
  public static double[] vectorAddition(double[] vectorA, double[] vectorB) {
    double[] resultVector = new double[vectorA.length];
    for (int i = 0; i < vectorA.length; i++) {
      resultVector[i] = vectorA[i] + vectorB[i];
    }
    return resultVector;
  }

  /**
   * Get a sorted map of all news. The sorting is done based on similarity to provided article. So the first articles in the map will be more similar
   * to the provided article, and the last will be least similar.
   * 
   * @param currentArticle - the provided article based on which will calculate the similarity.
   * @return - the sorted map with most similar articles to the provided article.
   */
  private static Map<String, double[]> getMostRelatedNewsUsingCosineSimilarity(String currentArticle) {
    double[] currentNewVector = newsSumVectors.get(currentArticle);
    Map<String, double[]> sortedMap = new TreeMap<String, double[]>(new Comparator<String>() {

      @Override
      public int compare(String o1, String o2) {
        double cosineSimilarityA = cosineSimilarity(currentNewVector, newsSumVectors.get(o1));
        double cosineSimilarityB = cosineSimilarity(currentNewVector, newsSumVectors.get(o2));
        return Double.compare(cosineSimilarityB, cosineSimilarityA);
      }
    });
    sortedMap.putAll(newsSumVectors);
    return sortedMap;
  }

  /**
   * Calculate the cosine similarity of two vectors.
   * 
   * @param vectorA
   * @param vectorB
   * @return
   */
  private static double cosineSimilarity(double[] vectorA, double[] vectorB) {
    double dotProduct = 0.0;
    double normA = 0.0;
    double normB = 0.0;
    for (int i = 0; i < vectorA.length; i++) {
      dotProduct += vectorA[i] * vectorB[i];
      normA += Math.pow(vectorA[i], 2);
      normB += Math.pow(vectorB[i], 2);
    }
    return dotProduct / (Math.sqrt(normA) * Math.sqrt(normB));
  }

}
