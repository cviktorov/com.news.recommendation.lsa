
package com.fmi.fkt.service;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;

import org.apache.commons.io.FileUtils;

import com.fmi.fkt.data.Article;
import com.fmi.fkt.data.DocumentCategory;
import com.fmi.fkt.word2vec.Word2VecSimilarity;

@Singleton
@Startup
public class Word2VecService {

  /**
   * Replace <absolute_path_to_project> with actual path
   */
  private static final String FOLDER_PATH = "<absolute_path_to_project>/NewsRecommendationLSA/news";
  /**
   * Path to word of vector file.
   */
  public static final String WORD_VECTORS_PATH = "C:\\Users\\i319962\\Documents\\Projects\\Inoweek2016 - Tweets\\glove.twitter.27B\\glove.twitter.27B.200d.txt";
  public static final String WORD_GOOGLE_VECTORS_PATH = "C:\\Users\\i319962\\Documents\\fmi\\GoogleNews-vectors-negative300.bin";
  /**
   * The size of the vectors of word2vec model.
   */
  public static final int VECTORS_SIZE = 300;
  
  private Word2VecSimilarity word2VecSimilarity;

  @PostConstruct
  public void init() {
    try {
      word2VecSimilarity = new Word2VecSimilarity(WORD_GOOGLE_VECTORS_PATH, VECTORS_SIZE, FOLDER_PATH);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public List<Article> search(String query, int resultSize) throws IOException {
    return convertToArticles(word2VecSimilarity.search(query, resultSize));
  }

  public List<Article> searchSuggestions(Article document, int resultSize) throws IOException {
    List<Article> result = convertToArticles(word2VecSimilarity.search(document.getContent(), resultSize));
    document.setId(document.getId().replace("news_original", "news"));
    result.remove(document);
    return result;
  }

  private List<Article> convertToArticles(List<String> filePaths) throws IOException {
    List<Article> articles = new ArrayList<>();
    for (String path : filePaths) {
      String content = FileUtils.readFileToString(new File(path.replace("news", "news_original")), StandardCharsets.UTF_8);
      content = content.substring(content.indexOf("\n\n"));
      articles.add(new Article(path, content, extractCategory(path)));
    }
    return articles;
  }

  private DocumentCategory extractCategory(String path) {
    if (path.contains("comp.sys.ibm.pc.hardware")) {
      return DocumentCategory.PC;
    } else if (path.contains("comp.sys.mac.hardware")) {
      return DocumentCategory.MAC;
    } else if (path.contains("rec.autos")) {
      return DocumentCategory.CAR;
    } else if (path.contains("rec.motorcycles")) {
      return DocumentCategory.BIKE;
    } else if (path.contains("rec.sport.baseball")) {
      return DocumentCategory.BASEBALL;
    } else if (path.contains("rec.sport.hockey")) {
      return DocumentCategory.HOCKEY;
    }
    return null;
  }

}
