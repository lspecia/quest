/**
 * 
 */
package shef.mt.features.util;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 * @author dupo
 *
 */
public class FeatureConfigReader {

	/**
	 * 
	 */
	
	String filename;
	
	public FeatureConfigReader(String xmlFile) {
		filename = xmlFile;
	}
	
	/**
     * Creates a list of feature names by inspecting the XML feature configuration
     * file and creating a Feature object from each <feature> node
     */
    public Set<String> getFeatures(){
    	Set<String> nodes = new HashSet<String>();
        try {

        	Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new File(filename));
            NodeList links = doc.getElementsByTagName("feature");

            for (int i = 0; i < links.getLength(); i++) {
                // for every link tag
                Element link = (Element) links.item(i);
                String index = link.getAttribute("index");
                nodes.add(index);
            }

        } catch (Exception e) {
        	System.err.println("Error opening features' configuration file "+filename);
            e.printStackTrace();
        }
		return nodes;

    }    

}
