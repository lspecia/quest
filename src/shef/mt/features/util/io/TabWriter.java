package shef.mt.features.util.io;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Set;

import shef.mt.features.util.ParallelSentence;

public class TabWriter implements FeatureWriter {
	
	BufferedWriter writer;
	
	public TabWriter(String filename) {

		try {
			writer = Files.newBufferedWriter(Paths.get(filename), StandardCharsets.UTF_8);
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
		}
		
		try {
			writer.write(line);
			writer.newLine();
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
