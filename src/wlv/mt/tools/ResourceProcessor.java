/**
 *
 */
package wlv.mt.tools;

import wlv.mt.features.util.*;

/**
 * Abstract class that is the base class for all classes that process the output
 * files of pre-processing tools
 *
 * @author Catalina Hallett
 *
 */
public abstract class ResourceProcessor {

    public abstract void processNextSentence(Sentence source);
}
