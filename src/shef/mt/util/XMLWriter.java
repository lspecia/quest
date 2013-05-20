/**
 * 
 */
package shef.mt.util;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import javax.xml.namespace.QName;
import javax.xml.stream.Location;
import javax.xml.stream.XMLEventFactory;
import javax.xml.stream.XMLEventWriter;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.Characters;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartDocument;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

import shef.mt.features.util.ParallelSentence;
import shef.mt.features.util.Sentence;

/**
 * An object that writes parallel sentences incrementally into an XML file
 * @author Eleftherios Avramidis
 *
 */
public class XMLWriter {
	private XMLOutputFactory outputFactory;
	private XMLEventWriter eventWriter;
	private XMLEventFactory eventFactory;
	private XMLEvent end;
	private FileOutputStream fileOutputStream;

	/**
	 * Initialize an object that writes parallel sentences incrementally into the given XML file
	 * The .close() function should be fired so that there is a valid XML file
	 * @param fileName : the filename of the XML file that will be created an populated.
	 */
	public XMLWriter(String fileName){
		// Create a XMLOutputFactory
		outputFactory = XMLOutputFactory.newInstance();
		try {
			// Create XMLEventWriter
			fileOutputStream = new FileOutputStream(fileName);
			eventWriter = outputFactory.createXMLEventWriter(fileOutputStream);
		} catch (FileNotFoundException | XMLStreamException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// Create a EventFactory
		eventFactory = XMLEventFactory.newInstance();
		end = eventFactory.createDTD("\n");

		// Create and write Start Tag
		StartDocument startDocument = eventFactory.createStartDocument();
		try {
			eventWriter.add(startDocument);
			// Create XML open tag
			StartElement startElement = eventFactory.createStartElement("",
					"", "jcml");
			eventWriter.add(startElement);
			eventWriter.add(end);

		} catch (XMLStreamException e) {
			e.printStackTrace();
		}


	}

	/**
	 * Write the closing element of the document and close the file
	 */
	public void close(){
		
		try {
			eventWriter.add(eventFactory.createEndElement("", "", "jcml"));
			eventWriter.add(end);
			eventWriter.add(eventFactory.createEndDocument());
			eventWriter.close();
			fileOutputStream.close();
		} catch (XMLStreamException e) {
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Write a parallel sentence onto the file
	 * @param parallelSentence the parallel sentence to be written
	 * @throws Exception 
	 */
	public void write(ParallelSentence parallelSentence) {
		try {
			writeParallelSentence(eventWriter, parallelSentence);
		} catch (XMLStreamException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Helper functio that writes one parallel sentence
	 * @param eventWriter
	 * @param parallelSentence
	 * @throws XMLStreamException
	 */
	private void writeParallelSentence(XMLEventWriter eventWriter, ParallelSentence parallelSentence) throws XMLStreamException{
		XMLEventFactory eventFactory = XMLEventFactory.newInstance();
		XMLEvent end = eventFactory.createDTD("\n");
		XMLEvent tab = eventFactory.createDTD("\t");
		
		HashMap<String,Object> attributes = parallelSentence.getAttributes();
		Iterator<Attribute> attributesIterator = getAttributesIterator(attributes);
		Iterator nsIterator = Arrays.asList().iterator();
		StartElement sElement = eventFactory.createStartElement("", "", "judgedsentence", attributesIterator, nsIterator);
		
		eventWriter.add(tab);
		eventWriter.add(sElement);
		
		writeSentence(eventWriter, "src", parallelSentence.getSource());
		
		for (Sentence targetSentence:parallelSentence.getTargetSentences()){
			writeSentence(eventWriter, "tgt",  targetSentence);
		}
		
		EndElement eElement = eventFactory.createEndElement("", "", "judgedsentence");
		eventWriter.add(eElement);
		eventWriter.add(end);
		
	}
	
	/**
	 * Helper function that creates and returns an Iterator object over
	 * the attributes 
	 * @param attributes
	 * @return 
	 */
	private Iterator<Attribute> getAttributesIterator(HashMap<String,Object> attributes){
		ArrayList<Attribute> attributeList = new ArrayList<Attribute>();
		for (Entry<String,Object> entry:attributes.entrySet()){
			Attribute att = eventFactory.createAttribute(entry.getKey(), entry.getValue().toString());
			attributeList.add(att);
		}
		return attributeList.iterator();
		
	}

	/**
	 * Helper function that writes one source or target sentence
	 * @param eventWriter
	 * @param name Specify the name of the element
	 * @param sentence A sentence instance
	 * @throws XMLStreamException
	 */
	private void writeSentence(XMLEventWriter eventWriter, String name, Sentence sentence) throws XMLStreamException {
		
		String value = sentence.getText();
		XMLEventFactory eventFactory = XMLEventFactory.newInstance();
		XMLEvent end = eventFactory.createDTD("\n");
		XMLEvent tab = eventFactory.createDTD("\t\t");
		// Create Start node
		
		HashMap<String,Object> attributes = sentence.getAttributes();
		Iterator<Attribute> attributesIterator = getAttributesIterator(attributes);
		Iterator nsIterator = Arrays.asList().iterator();
		
		StartElement sElement = eventFactory.createStartElement("", "", name, attributesIterator, nsIterator);
		
		
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

