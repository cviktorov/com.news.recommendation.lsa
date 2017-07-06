
package com.fmi.fkt.word2vec;

import java.io.File;
import java.io.IOException;
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

import org.deeplearning4j.models.embeddings.loader.WordVectorSerializer;
import org.deeplearning4j.models.embeddings.wordvectors.WordVectors;

import com.fmi.fkt.lsa.LSA;

public class Word2VecSimilarity {

  private WordVectors word2vecModel;
  private Map<String, String> docs;
  private Map<String, double[]> docsSumVector = new HashMap<>();
  private int word2VecDimension;

  public Word2VecSimilarity(String word2VecFile, int word2VecDimension, String folderPath) throws IOException {
    word2vecModel = WordVectorSerializer.readWord2VecModel(new File(word2VecFile));
    docs = new HashMap<String, String>();
    this.word2VecDimension = word2VecDimension;
    LSA.readFilesForFolder(new File(folderPath), docs);
    for (Entry<String, String> document : docs.entrySet()) {
      List<String> wordsInDoc = extractOnlyWords(document.getValue());
      double[] sumVector = createSumVector(word2VecDimension, wordsInDoc);
      docsSumVector.put(document.getKey(), sumVector);
    }
  }

  private double[] createSumVector(int word2VecDimension, List<String> wordsInDoc) {
    double[] sumVector = new double[word2VecDimension];
    for (String word : wordsInDoc) {
      double[] wordVector = word2vecModel.getWordVector(word);
      if (wordVector == null) {
        wordVector = new double[word2VecDimension];
        Arrays.fill(wordVector, 0.0);
      }
      sumVector = vectorAddition(sumVector, wordVector);
    }
    return sumVector;
  }

  public List<String> search(String query, int maxElementToReturn) {
    double[] sumVector = createSumVector(word2VecDimension, extractOnlyWords(query));

    Map<String, double[]> sortedMap = new TreeMap<String, double[]>(new Comparator<String>() {

      @Override
      public int compare(String o1, String o2) {
        double cosineSimilarityA = cosineSimilarity(sumVector, docsSumVector.get(o1));
        double cosineSimilarityB = cosineSimilarity(sumVector, docsSumVector.get(o2));
        return Double.compare(cosineSimilarityB, cosineSimilarityA);
      }
    });
    sortedMap.putAll(docsSumVector);
    int i = 0;
    List<String> orderedDocs = new ArrayList<>();
    for (String docTitle : sortedMap.keySet()) {
      orderedDocs.add(docTitle);
      if (i++ == maxElementToReturn) {
        break;
      }
    }
    return orderedDocs;
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
