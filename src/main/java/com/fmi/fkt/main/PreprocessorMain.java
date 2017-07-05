package com.fmi.fkt.main;

import java.io.IOException;

public class PreprocessorMain {

	private static final String NEWSGROUP_FOLDER_PATH = "/Users/i323283/Downloads/20_newsgroups";

	public static void main(String[] args) throws IOException {
		Preprocessor.preprocessNewsGroupDir(NEWSGROUP_FOLDER_PATH);
	}
}
