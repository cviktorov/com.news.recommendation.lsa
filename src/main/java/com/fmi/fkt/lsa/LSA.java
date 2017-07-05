package com.fmi.fkt.lsa;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.fmi.fkt.comparator.MyComparator;

import edu.ucla.sspace.basis.StringBasisMapping;
import edu.ucla.sspace.common.Similarity;
import edu.ucla.sspace.common.Similarity.SimType;
import edu.ucla.sspace.lsa.LatentSemanticAnalysis;
import edu.ucla.sspace.matrix.NoTransform;
import edu.ucla.sspace.matrix.factorization.SingularValueDecompositionLibC;
import edu.ucla.sspace.text.StringDocument;
import edu.ucla.sspace.vector.DoubleVector;

public class LSA {

	private LatentSemanticAnalysis lsa;
	private Map<String, String> docs;
	
	public LSA(int dimension,String folderPath) throws IOException{
		lsa = new LatentSemanticAnalysis(true, dimension, new NoTransform(),
				new SingularValueDecompositionLibC(), false, new StringBasisMapping());
		
		docs = new HashMap<String, String>();
		readFilesForFolder(new File(folderPath), docs);
		
		for (String doc : docs.values())
			lsa.processDocument(new BufferedReader(new StringReader(doc)));
		lsa.processSpace(System.getProperties());
	}
	
	public List<String> search(String query, int maxElementToReturn){
		DoubleVector projected = lsa.project(new StringDocument(query));

		Map<Integer, Double> similarityMap = new HashMap<Integer, Double>();
		Map<Integer, String> absolutePathsMap = new HashMap<Integer, String>();

		int i = 0;
		for (String doubleVector : docs.keySet()) {
			double sim = Similarity.getSimilarity(SimType.COSINE, lsa.getDocumentVector(i), projected);
			similarityMap.put(i, sim);
			absolutePathsMap.put(i, doubleVector);
			i++;
		}

		MyComparator comparator = new MyComparator(similarityMap);
		Map<Integer, Double> orderedSimilarityMap = new TreeMap<Integer, Double>(comparator);
		orderedSimilarityMap.putAll(similarityMap);
		
		List<String> orderedDocs = new ArrayList<>();
		int k = 0;
		for (Integer doubleVector : orderedSimilarityMap.keySet()) {
			if (k++ == maxElementToReturn)
				break;
			orderedDocs.add(absolutePathsMap.get(doubleVector));
		}
		
		return orderedDocs;
	}
	
	public static void readFilesForFolder(final File folder, Map<String, String> files) throws IOException {
		for (final File fileEntry : folder.listFiles()) {
			if (fileEntry.isDirectory()) {
				readFilesForFolder(fileEntry, files);
			} else {
				files.put(fileEntry.getAbsolutePath(), readFile(fileEntry.getAbsolutePath(), StandardCharsets.UTF_8));
			}
		}
	}

	static String readFile(String path, Charset encoding) throws IOException {
		byte[] encoded = Files.readAllBytes(Paths.get(path));
		return new String(encoded, encoding);
	}
}
