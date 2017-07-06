package com.fmi.fkt.web.restapi;

import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.core.Application;

import org.nd4j.shade.jackson.jaxrs.json.JacksonJsonProvider;

import com.fmi.fkt.web.restapi.news.NewsResource;

public class RestAPIBase extends Application {

	@Override
	public Set<Class<?>> getClasses() {

		Set<Class<?>> classes = new HashSet<Class<?>>();
		classes.add(JacksonJsonProvider.class);
		classes.add(NewsResource.class);

		return classes;
	}
}
