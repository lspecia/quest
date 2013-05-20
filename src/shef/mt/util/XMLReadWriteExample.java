package shef.mt.util;

import java.util.ArrayList;
import java.util.List;

import shef.mt.features.util.ParallelSentence;
import shef.mt.features.util.Sentence;

public class XMLReadWriteExample {

	public XMLReadWriteExample() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		System.out.println("Hello World");
		
		XMLReader reader = new XMLReader("/home/dupo/taraxu_data/r2/glassbox/wmt11.gb.jcml");
		XMLWriter writer = new XMLWriter("/home/dupo/taraxu_data/r2/glassbox/wmt11.gb.test.jcml");
		int i=0;
		while (reader.hasNext()){
			i++;
			ParallelSentence ps = reader.next();
			System.out.println(String.valueOf(i));
			System.out.println(ps.getSource().getText());
			List<Sentence> t = ps.getTargetSentences();
			System.out.println(ps.getAttributes().toString());
			for (Sentence s:t){
				System.out.println(s.getText());
				System.out.println(s.getAttributes().toString());
			}
			writer.write(ps);

		} 
		writer.close();
		reader.close();

	}

}
