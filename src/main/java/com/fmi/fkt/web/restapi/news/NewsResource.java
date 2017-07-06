package com.fmi.fkt.web.restapi.news;

import java.io.IOException;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.bean.ApplicationScoped;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.fmi.fkt.data.Article;
import com.fmi.fkt.service.LSAService;
import com.fmi.fkt.service.Word2VecService;

@Path("news")
@ApplicationScoped
public class NewsResource {
	
	private static final int RESULT_SIZE = 10;
	
	@EJB
	private LSAService lsaService;

	@EJB
  private Word2VecService word2VecService;
	
	@Path("search")
	@POST
	@Consumes(MediaType.TEXT_PLAIN)
	@Produces(MediaType.APPLICATION_JSON)
	public Response search(String query) throws IOException {
		GenericEntity<List<Article>> entity = new GenericEntity<List<Article>>(lsaService.search(query, RESULT_SIZE)) {};
		return Response.ok(entity).header("Access-Control-Allow-Origin", "*").build();
	}	
	
	@Path("suggestions")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response search(Article document) throws IOException {
		GenericEntity<List<Article>> entity = new GenericEntity<List<Article>>(lsaService.searchSuggestions(document, RESULT_SIZE)) {};
		return Response.ok(entity).header("Access-Control-Allow-Origin", "*").build();
	}
	
	 @Path("searchwv")
	  @POST
	  @Consumes(MediaType.TEXT_PLAIN)
	  @Produces(MediaType.APPLICATION_JSON)
	  public Response searchWord2Vec(String query) throws IOException {
	    GenericEntity<List<Article>> entity = new GenericEntity<List<Article>>(word2VecService.search(query, RESULT_SIZE)) {};
	    return Response.ok(entity).header("Access-Control-Allow-Origin", "*").build();
	  } 
	  
	  @Path("suggestionswv")
	  @POST
	  @Consumes(MediaType.APPLICATION_JSON)
	  @Produces(MediaType.APPLICATION_JSON)
	  public Response searchWord2Vec(Article document) throws IOException {
	    GenericEntity<List<Article>> entity = new GenericEntity<List<Article>>(word2VecService.searchSuggestions(document, RESULT_SIZE)) {};
	    return Response.ok(entity).header("Access-Control-Allow-Origin", "*").build();
	  }
}
