package com.fmi.fkt.main;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;

import org.apache.commons.io.FileUtils;

import com.fmi.fkt.preprocessor.StopWordPreprocessor;

public class Preprocessor {

	public static void preprocessNewsGroupDir(String dirPath) throws IOException {
		Iterator<File> iter = FileUtils.iterateFiles(new File(dirPath), null, true);
		while (iter.hasNext()) {
			File file = (File) iter.next();
			String content = FileUtils.readFileToString(file, StandardCharsets.UTF_8);
			// Ignore text until first empty line
			content = content.substring(content.indexOf("\n\n") + 1);
			content = processFile(content);
			saveFile(content, file.getAbsolutePath(), dirPath);
		}
	}
	
	public static void preprocessDir(String dirPath) throws IOException {
		Iterator<File> iter = FileUtils.iterateFiles(new File(dirPath), null, true);
		while (iter.hasNext()) {
			File file = (File) iter.next();
			String content = FileUtils.readFileToString(file, StandardCharsets.UTF_8);
			content = processFile(content);
			saveFile(content, file.getAbsolutePath(), dirPath);
		}
	}

	private static String processFile(String content) throws IOException {
		content = content.toLowerCase();
		content = StopWordPreprocessor.processText(content);
		//content = PorterStemmer.stemText(content);
		
		return content;
	}

	private static void saveFile(String content, String filePath, String dirPath) throws IOException {
		FileUtils.writeStringToFile(new File(filePath.replace(dirPath, dirPath + "_processed")), content, StandardCharsets.UTF_8);
	}
}
