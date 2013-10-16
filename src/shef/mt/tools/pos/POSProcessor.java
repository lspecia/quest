package shef.mt.tools.pos;

import shef.mt.tools.ResourceProcessor;
import shef.mt.util.PropertiesManager;

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
