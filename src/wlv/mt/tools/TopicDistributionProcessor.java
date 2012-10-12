/**
 *
 */
package wlv.mt.tools;

import wlv.mt.features.util.*;
import java.io.*;

/**
 * Processes a file containing topic distributions, one line (document, sentence, segment, etc.) is the topic vector
 * corresponding the line of the source or target sentences.
 *
 * @author Raphael Rubino
 *
 */
public class TopicDistributionProcessor extends ResourceProcessor {

    private BufferedReader bufferedReader; // BufferReader used to process the topic distribution file, line by line
    private static String topicDistributionFile; // String of the topic distribution file name
    private static String resourceName; // String of the resource name to register in the ResourceManager

    /**
    * @param	topicDistributionFile	a String giving the location of the topic distribution file
    * @param	resourceName	a String giving the resource name to register in the ResourceManager
    *
    */
    public TopicDistributionProcessor(String topicDistributionFile, String resourceName) {
        try {
            this.bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(topicDistributionFile), "utf-8"));
            this.topicDistributionFile = topicDistributionFile;
	    ResourceManager.registerResource(resourceName);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
    * 
    * @param    s	Sentence object 
    *
    */
    public void processNextSentence(Sentence s) {
        try {
            String line = bufferedReader.readLine();
	    Float[] topicVector = parseLine( line );
	    s.setValue( "topicDistribution", topicVector );
	} catch ( NullPointerException e ) {
	    System.err.println( "NullPointerException: The topic distribution file does not contain so many lines!\nIt is probably shorter than the source and/or the target text files.\nI am trying to process the file " + this.topicDistributionFile + ", line number " + ( s.getIndex() + 1 ) );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
    * @param    line	a String containing one row of the topic distribution file
    * @return		a Float[] containing each topic probability for a given topic distribution
    *
    */
    private Float[] parseLine(String line) {
        String[] components = line.split("\\s+");
        Float[] topicVector = new Float[ components.length ];
        for ( int i = 0 ; i < components.length ; i++ ) {
                topicVector[i] = Float.parseFloat( components[ i ] );
        }
        return topicVector;
    }

    /**
    * 
    */
    public void close() {
        try {
            bufferedReader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
