/**
 *
 */
package shef.mt.tools;

import shef.mt.features.util.Sentence;
import shef.mt.util.PropertiesManager;
import shef.mt.features.util.FeatureManager;

/**
 * Abstract class that is the base class for:
 * POSProcessorSource.java
 * POSProcessorTarget.java
 *
 * @author Lukas Poustka
 */
public abstract class POSProcessor extends ResourceProcessor {

	public abstract String runPOS(PropertiesManager propertiesManager, boolean forceRun, String file, String lang, String type);
}
