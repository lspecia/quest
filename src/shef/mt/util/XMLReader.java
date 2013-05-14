/**
 * 
 */
package shef.mt.util;

import javax.xml.stream.*;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

import shef.mt.features.util.ParallelSentence;
import shef.mt.features.util.Sentence;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 * @author Eleftherios Avramidis
 *
 */
public class XMLReader implements Iterator<ParallelSentence>{

	/**
	 * 
	 */
	
	private InputStream inputStream;
	private XMLInputFactory factory;
	private XMLEventReader reader;
	private XMLEvent event;
	
	static final String PARALLELSENTENCE = "judgedsentence";
	static final String SOURCESENTENCE = "src";
	static final String TARGETSENTENCE = "tgt";
	static final String ROOTELEMENT = "jcml";

	static final String ENDDOCUMENT = "ENDDOCUMENT";

	
	/**
	 * Initialize a class-level event reader from the given filename
	 */
	public XMLReader(String fileName) {
		try {
			inputStream = new FileInputStream(fileName);
			factory = XMLInputFactory.newInstance();
			reader = factory.createXMLEventReader(inputStream);
			//always look one event forward, so that you can predict the end of the file
			event = reader.nextEvent();
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (XMLStreamException e) {		// TODO Auto-generated method stub
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
	/**
	 * Close all open inherent objects
	 */	
	public void close(){
		try {
			reader.close();
		} catch (XMLStreamException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			inputStream.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	@Override	
	/**
	 * Xmlfile has next, as long as the inherent event reader has next
	 * The reader will not have a next event, if the pointer is at the closing element
	 */
	public boolean hasNext(){
		boolean isEnd = (event.isEndElement() && (event.asEndElement().getName().getLocalPart()==ROOTELEMENT));
		return (reader.hasNext() 
				&& !event.isEndDocument()
				&& !isEnd);
		
		
	}
	
	/**
	 * Utility function for returning a hashmap quest_bparserof attributes out
	 * of a given StAX start element 
	 */
	private HashMap<String,Object> getAttributes(StartElement element){
		HashMap<String,Object> attributes = new HashMap<String,Object>();
		
		Iterator<Attribute> attributesIter = element.getAttributes();
		while (attributesIter.hasNext()){
			Attribute attribute = attributesIter.next();
			attributes.put(attribute.getName().toString(), attribute.getValue());
		}
		return attributes;
	}

	@Override
	/**
	 * Reads the next parallel sentence from the XML file and returns 
	 * one populated parallel sentence object
	 */
	public ParallelSentence next() {
		ParallelSentence parallelSentence = null;
		try {
			boolean endDocument = false;
			Sentence sourceSentence = null;			
			List<Sentence> targetSentences = new ArrayList<Sentence>();
			HashMap<String,Object> generalAttributes = new HashMap<String,Object>();
			
			//iterate until one (more) parallel sentence has been created
			while (parallelSentence == null && !endDocument){
				
				//if element starting, then get the attributes
				if (event.isStartElement()) {
					StartElement startElement = event.asStartElement();
					switch (startElement.getName().getLocalPart()){
						case PARALLELSENTENCE:
							generalAttributes = getAttributes(startElement);
							break;
						case SOURCESENTENCE:
							HashMap<String,Object> sourceAttributes = getAttributes(startElement);
							event = reader.nextEvent();
							sourceSentence = new Sentence(event.asCharacters().getData(), sourceAttributes);
							break;
						case TARGETSENTENCE:
							HashMap<String,Object> targetAttributes = getAttributes(startElement);
							event = reader.nextEvent();
							Sentence targetSentence = new Sentence(event.asCharacters().getData(), targetAttributes);
							targetSentences.add(targetSentence);
							break;
					}
				//if element closing, then create the object
				} else if (event.isEndElement()) {
					EndElement endElement = event.asEndElement();
					switch (endElement.getName().getLocalPart()){
						case PARALLELSENTENCE:
							parallelSentence = new ParallelSentence(sourceSentence, targetSentences, generalAttributes);
							break;
						case ENDDOCUMENT:
							endDocument = true;
				
					}
				}
				
				if (reader.hasNext()){
					event = reader.nextEvent();
					while ((!event.isEndElement())&&(!event.isStartElement())&&(!event.isEndDocument())){						
						System.out.println(event.toString());
						event = reader.nextEvent();
					}
				}
			}
		
		} catch (XMLStreamException e) {
			e.printStackTrace();
		}
		return parallelSentence;
		
		
	}

	@Override
	public void remove() {
		// This does not do anything since we cannot edit the reader items
		
	}
	
}
