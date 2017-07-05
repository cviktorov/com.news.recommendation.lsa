
package com.fmi.fkt.word2vec;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * This class is used to parse a raw dataset of news from Reuters. The dataset is in XML-like format. After the news are parsed, each article will be
 * a file and the name of the file will be the title of the article.
 * 
 * @author I319962
 *
 */
public class NewsParser {

  private static final String REUTERS_NEWS_RAW_DATASET = "C:\\Users\\i319962\\Documents\\fmi\\reuters\\reut2-021.sgm";

  /**
   * The folder where the parsed news will be saved.
   */
  public static final String REUTERS_NEWS_FOLER = "C:\\Users\\i319962\\Documents\\fmi\\reuters\\news";

  /**
   * Some words that should be removed from the news.
   */
  private static Map<String, String> wordsToBeReplaced = new HashMap<String, String>();
  static {
    wordsToBeReplaced.put(">", " ");
    wordsToBeReplaced.put("<", " ");
    wordsToBeReplaced.put(",", " ");
    wordsToBeReplaced.put("\\.", " ");
    wordsToBeReplaced.put("/", " ");
    wordsToBeReplaced.put("\\n", " ");
    wordsToBeReplaced.put("\\rn", " ");
  }

  public static void main(String[] args) throws ParserConfigurationException, SAXException, IOException {

    SAXParserFactory factory = SAXParserFactory.newInstance();
    SAXParser saxParser = factory.newSAXParser();
    DefaultHandler handler = new DefaultHandler() {

      boolean titleTag = false;
      boolean textTag = false;
      private Path currentNew = null;

      @Override
      public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        if (qName.equalsIgnoreCase("TITLE")) {
          System.out.println("*****Title is:");
          titleTag = true;
        } else if (qName.equalsIgnoreCase("BODY")) {
          System.out.println("*****Text is:");
          textTag = true;
        }
      }

      @Override
      public void endElement(String uri, String localName, String qName) throws SAXException {
        if (qName.equalsIgnoreCase("TITLE")) {
          titleTag = false;
        } else if (qName.equalsIgnoreCase("BODY")) {
          textTag = false;
          currentNew = null;
        }
      }

      @Override
      public void characters(char[] ch, int start, int length) throws SAXException {
        if (titleTag) {
          try {
            String newFile = REUTERS_NEWS_FOLER + File.separator + new String(ch, start, length);
            System.out.println("newFile: " + newFile);
            String preProcessTitle = preProcessTitle(newFile);
            System.out.println("preprocessed: " + preProcessTitle);
            currentNew = Files.createFile(Paths.get(preProcessTitle));
            System.out.println("Create file:" + currentNew.getFileName());
          } catch (IOException e) {
            e.printStackTrace();
          }
        } else if (textTag) {
          try {
            Files.write(currentNew, new String(ch, start, length).getBytes("UTF-8"), StandardOpenOption.APPEND);
          } catch (IOException e) {
            e.printStackTrace();
          }
          // System.out.println(new String(ch, start, length));
        }
      }
    };

    saxParser.parse(REUTERS_NEWS_RAW_DATASET, handler);
  }

  private static String preProcessTitle(String title) {

    String result = title;
    for (Entry<String, String> words : wordsToBeReplaced.entrySet()) {
      result = result.replaceAll(words.getKey(), words.getValue());
    }
    return result.trim();
  }
}
