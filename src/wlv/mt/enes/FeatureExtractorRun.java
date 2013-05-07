package wlv.mt.enes;

import java.util.ArrayList;

public class FeatureExtractorRun {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		
		String sourcesentence = "This is a car";
		String targetsentence1 = "Das ist ein Auto";
		String targetsentence2 = "Das ist Auto";
		String targetsentence3 = "Ein Auto ist das";
		String targetsentence4 = "Das Auto";
		ArrayList<String> targetSentences = new ArrayList<String>();
		
		targetSentences.add(targetsentence1);
		targetSentences.add(targetsentence2);
		targetSentences.add(targetsentence3);
		targetSentences.add(targetsentence4);
		
		FeatureExtractor fx = new FeatureExtractor(args);
		
		String pplSourcePath = "";
		String pplTargetPath = "";
		int ngramSize = 3;
		String sourceFile = "";
		String sourceLang = "german";
		String targetLang = "english";
		String sourceCorpus = "/home/dupo/project_data/taraxu/text.txt";
		String sourceLM = "";
		String targetLM = "";
		String gizaPath = "";
		
		
		fx.initializeBB(pplSourcePath, pplTargetPath, ngramSize, sourceFile, 
				sourceLang, targetLang, sourceCorpus, sourceLM, targetLM, gizaPath);
		
		
		
	}

}
