/**
 * 
 */
package shef.mt.util;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.HashMap;

import javax.xml.stream.XMLEventFactory;
import javax.xml.stream.XMLEventWriter;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Characters;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartDocument;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

import shef.mt.features.util.ParallelSentence;
import shef.mt.features.util.Sentence;

public class XMLWriter {
	private XMLOutputFactory outputFactory;
	private XMLEventWriter eventWriter;
	private XMLEventFactory eventFactory;
	private XMLEvent end;

	public XMLWriter(String fileName){
		// Create a XMLOutputFactory
		// Create XMLEventWriter
		// Create a EventFactory
		outputFactory = XMLOutputFactory.newInstance();
		try {
			eventWriter = outputFactory.createXMLEventWriter(new FileOutputStream(fileName));
		} catch (FileNotFoundException | XMLStreamException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		eventFactory = XMLEventFactory.newInstance();
		end = eventFactory.createDTD("\n");

		// Create and write Start Tag
		StartDocument startDocument = eventFactory.createStartDocument();
		try {
			eventWriter.add(startDocument);
			// Create config open tag
			StartElement startElement = eventFactory.createStartElement("",
					"", "jcml");
			eventWriter.add(startElement);
			eventWriter.add(end);

		} catch (XMLStreamException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


	}


	
	public void write(ParallelSentence parallelSentence) throws Exception {
		// Write the different nodes

		writeParallelSentence(eventWriter, parallelSentence);
		
		
		
	}

	public void close(){

		try {
			eventWriter.add(eventFactory.createEndElement("", "", "jcml"));
			eventWriter.add(end);
			eventWriter.add(eventFactory.createEndDocument());
			eventWriter.close();

		} catch (XMLStreamException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void writeParallelSentence(XMLEventWriter eventWriter, ParallelSentence parallelSentence) throws XMLStreamException{
		XMLEventFactory eventFactory = XMLEventFactory.newInstance();
		XMLEvent end = eventFactory.createDTD("\n");
		XMLEvent tab = eventFactory.createDTD("\t");
		
		HashMap<String,Object> attributes = parallelSentence.getAttributes();
		//TODO: convert attributes to iterators
		StartElement sElement = eventFactory.createStartElement("", "", "judgedsentence");
		
		eventWriter.add(tab);
		eventWriter.add(sElement);
		
		writeSentence(eventWriter, "src",  parallelSentence.getSource());
		
		for (Sentence targetSentence:parallelSentence.getTargetSentences()){
			writeSentence(eventWriter, "tgt",  targetSentence);
		}
		
		EndElement eElement = eventFactory.createEndElement("", "", "judgedsentence");
		eventWriter.add(eElement);
		eventWriter.add(end);
		
	}

	private void writeSentence(XMLEventWriter eventWriter, String name, Sentence sentence) throws XMLStreamException {
		
		String value = sentence.getText();
		XMLEventFactory eventFactory = XMLEventFactory.newInstance();
		XMLEvent end = eventFactory.createDTD("\n");
		XMLEvent tab = eventFactory.createDTD("\t\t");
		// Create Start node
		
		HashMap<String,Object> attributes = sentence.getAttributes();
		//TODO: convert attributes to iterators
		StartElement sElement = eventFactory.createStartElement("", "", name);
		
		eventWriter.add(tab);
		eventWriter.add(sElement);
		// Create Content
		Characters characters = eventFactory.createCharacters(value);
		eventWriter.add(characters);
		// Create End node
		EndElement eElement = eventFactory.createEndElement("", "", name);
		eventWriter.add(eElement);
		eventWriter.add(end);

	}

} 