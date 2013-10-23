package shef.mt.features.util.io;

import java.io.*;
import java.util.Scanner;
import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.Set;

import shef.mt.features.util.ParallelSentence;
import shef.mt.features.util.Sentence;

public class TabWriter implements FeatureWriter {
	
	Writer writer;
	
	public TabWriter(String filename) {

		try {
			writer = new OutputStreamWriter(new FileOutputStream(filename), "utf-8");
//			only works on JRE 1.7
//			writer = Files.newBufferedWriter(Paths.get(filename), StandardCharsets.UTF_8);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void write(ParallelSentence parallelSentence, Set<String> requiredFeatures) {
		//the line which will contain the output
		String line = new String();
		for (String requiredFeature:requiredFeatures){
			HashMap<String,Object> sourceFeatures = parallelSentence.getSource().getValues();
			if (sourceFeatures.containsKey(requiredFeature)){
				line += sourceFeatures.get(requiredFeature) + "\t";
			}
			for (Sentence targetSentence:parallelSentence.getTargetSentences()){
				HashMap<String,Object> targetFeatures = targetSentence.getValues();
				if (targetFeatures.containsKey(requiredFeature)){
					line += targetFeatures.get(requiredFeature) + "\t";
				}
				
			}
		}
		
		try {
			writer.write(line);
			writer.write("\n");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void close(){
		try {
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
