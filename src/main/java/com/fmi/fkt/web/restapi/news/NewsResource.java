package com.fmi.fkt.web.restapi.news;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.io.FileUtils;

import com.fmi.fkt.data.Article;
import com.fmi.fkt.lsa.LSA;

@Path("news")
public class NewsResource {

	private static final String FOLDER_PATH = "news";
	
	private static final int DIMENSIONS = 110;
	
	@Path("search")
	@POST
	@Consumes(MediaType.TEXT_PLAIN)
	@Produces(MediaType.APPLICATION_JSON)
	public Response search(String query) throws IOException {
		LSA lsa = new LSA(DIMENSIONS, FOLDER_PATH);
		GenericEntity<List<Article>> entity = new GenericEntity<List<Article>>(convertToArticles(lsa.search(query, 10))) {};
		return Response.ok(entity).header("Access-Control-Allow-Origin", "*").build();
	}
	
	@Path("search")
	@GET
	@Consumes(MediaType.TEXT_PLAIN)
	@Produces(MediaType.APPLICATION_JSON)
	public Response search() throws IOException {
		LSA lsa = new LSA(DIMENSIONS, FOLDER_PATH);
		GenericEntity<List<Article>> entity = new GenericEntity<List<Article>>(convertToArticles(lsa.search("race crash oil helmet", 10))) {};
		return Response.ok(entity).header("Access-Control-Allow-Origin", "*").build();
	}
	
	private List<Article> convertToArticles(List<String> filePaths) throws IOException {
		List<Article> articles = new ArrayList<>();
		for (String path : filePaths) {
			articles.add(new Article(path, FileUtils.readFileToString(new File(path), StandardCharsets.UTF_8)));
		}
		return articles;
	}
}
