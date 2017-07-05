package com.fmi.fkt.main;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;

import com.fmi.fkt.lsa.LSA;

public class LatentSemanticAnalysisMain {

	public static void main(String[] args) throws Exception {

		final String folder = "news";
		// String f1 = readFile("test/comp.sys.mac.hardware/52166",
		// StandardCharsets.UTF_8);

		String query = "race crash oil helmet";

		LSA lsa = new LSA(110, folder);
		lsa.search(query, 50).forEach(System.out::println);

	}

	static String readFile(String path, Charset encoding) throws IOException {
		byte[] encoded = Files.readAllBytes(Paths.get(path));
		return new String(encoded, encoding);
	}
}
