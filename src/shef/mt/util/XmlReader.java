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
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 * @author Eleftherios Avramidis
 *
 */
public class XmlReader implements Iterator<ParallelSentence>{

	/**
	 * 
	 */
	
	private InputStream inputStream;
	private XMLInputFactory factory;
	private XMLEventReader reader;
	
	static final String PARALLELSENTENCE = "judgedsentence";
	static final String SOURCESENTENCE = "src";
	static final String TARGETSENTENCE = "tgt";
	
	public XmlReader(String fileName) {
		try {
			inputStream = new FileInputStream(fileName);
			factory = XMLInputFactory.newInstance();
			reader = factory.createXMLEventReader(inputStream);
			
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (XMLStreamException e) {		// TODO Auto-generated method stub
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	@Override	
	public boolean hasNext(){
		
		return reader.hasNext();
		
	}
	
	private HashMap<String,Object> getAttributes(StartElement element){
		/**
		 * Utility function for returning a hashmap of attributes out of a given StAX start element 
		 */
		
		HashMap<String,Object> attributes = new HashMap<String,Object>();
		
		Iterator<Attribute> attributesIter = element.getAttributes();
		while (attributesIter.hasNext()){
			Attribute attribute = attributesIter.next();
			attributes.put(attribute.getName().toString(), attribute.getValue());
		}
		return attributes;
	}

	@Override
	public ParallelSentence next() {
		try {
			boolean parallelSentenceComplete = false;
			ParallelSentence parallelSentence;
			Sentence sourceSentence;			
			List<Sentence> targetSentences = new ArrayList<Sentence>();
			HashMap<String,Object> generalAttributes;
			HashMap<String,Object> sourceAttributes;
			HashMap<String,Object> targetAttributes;
			
			while (!parallelSentenceComplete){
				XMLEvent event = reader.nextEvent();
				//if element starting, then get the attributes
				if (event.isStartElement()) {
					StartElement startElement = event.asStartElement();
					switch (startElement.getName().getLocalPart()){
						case PARALLELSENTENCE:
							generalAttributes = getAttributes(startElement);
						case SOURCESENTENCE:
							sourceAttributes = getAttributes(startElement);
						case TARGETSENTENCE:
							targetAttributes = getAttributes(startElement);
					}
				//if element closing, then create the objects and populate them
				} else if (event.isEndElement()) {
					EndElement endElement = event.asEndElement();
					switch (endElement.getName().getLocalPart()){
						case PARALLELSENTENCE:
							
							parallelSentence = new ParallelSentence(sourceSentence, targetSentences, generalAttributes);
						case SOURCESENTENCE:

						case TARGETSENTENCE:

				
				}
				
			}
			
		} catch (XMLStreamException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
		
		
	}

	@Override
	public void remove() {
		// TODO Auto-generated method stub
		
	}
	
}
