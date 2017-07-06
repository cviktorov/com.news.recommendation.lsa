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
import com.fmi.fkt.lsa.LSA;

@Singleton
@Startup
public class LSAService {

	/**
	 * Replace <absolute_path_to_project> with actual path
	 */
	private static final String FOLDER_PATH = "<absolute_path_to_project>/NewsRecommendationLSA/news";
	
	private static final int DIMENSIONS = 110;
	
	private LSA lsa;
	
	@PostConstruct
	public void init() {
		try {
			lsa = new LSA(DIMENSIONS, FOLDER_PATH);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public List<Article> search(String query, int resultSize) throws IOException {
		return convertToArticles(lsa.search(query, resultSize));
	}
	
	public List<Article> searchSuggestions(Article document, int resultSize) throws IOException {
		List<Article> result = convertToArticles(lsa.search(document.getContent(), resultSize));
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
