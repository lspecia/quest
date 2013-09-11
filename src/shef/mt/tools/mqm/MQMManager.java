package shef.mt.tools.mqm;

import shef.mt.features.util.Sentence;
import shef.mt.tools.ResourceManager;
import shef.mt.tools.ResourceProcessor;
import shef.mt.tools.ResourceProcessorTwoSentences;
import shef.mt.tools.mqm.core.fluency.register.VariantsSlangProcessor;
import shef.mt.tools.mqm.resources.SlangDictionary;
import shef.mt.util.PropertiesManager;

import java.util.ArrayList;
import java.util.List;

/**
 * @author <a href="mailto:mail.jie.jiang@gmail.com">Jie Jiang</a>
 * @date 9/11/13
 */
public class MQMManager {
    private static MQMManager ourInstance = new MQMManager();

    public static MQMManager getInstance() {
        return ourInstance;
    }

    private MQMManager() {
    }

    private boolean isInitialized = false;
    private PropertiesManager propertiesManager = null;
    private ResourceManager resourceManager = null;
    private List<GlobalProcessor> globalProcessors = new ArrayList<GlobalProcessor>();
    private List<ResourceProcessor> srcResourceProcessors = new ArrayList<ResourceProcessor>();
    private List<ResourceProcessor> trgResourceProcessors = new ArrayList<ResourceProcessor>();
    private List<ResourceProcessorTwoSentences> srcTrgResourceProcessors = new ArrayList<ResourceProcessorTwoSentences>();

    public String getSrcLang() {
        return this.propertiesManager.getString("sourceLang.default");
    }

    public String getTrgLang() {
        return this.propertiesManager.getString("targetLang.default");
    }

    /**
     * Initialize resources and processors that has been config, and register them in Resource Manager
     * @param propertiesManager
     * @param resourceManager
     * @return
     */
    public boolean initialize(PropertiesManager propertiesManager, ResourceManager resourceManager) {
        assert propertiesManager != null;
        assert resourceManager != null;
        this.propertiesManager = propertiesManager;
        this.resourceManager = resourceManager;
        //this.configurables = new ArrayList<Configurable>();
        //read the config and initialize those resources and processors that has been config
        //TODO; only initialize the resources and processors by reflection from the feature sets, has to change the framework to start
        try {
            String srcLang = this.getSrcLang();
            String trgLang = this.getTrgLang();

            //slang dict only for the target
            SlangDictionary trgSlangDict = new SlangDictionary(trgLang);
            if (trgSlangDict.isConfigured(this)) {
                trgSlangDict.load(this);
                trgSlangDict.register(this);
                VariantsSlangProcessor p1 = new VariantsSlangProcessor(trgSlangDict);
                this.trgResourceProcessors.add(p1);
                this.globalProcessors.add(p1);
            }

            //other features

        } catch (Exception e) {
            isInitialized = false;
            return false;
        }
        isInitialized = true;
        return true;
    }

    public void globalProcessing() {
        for (GlobalProcessor processor : globalProcessors) {
            processor.globalProcessing(this);
        }
    }

    public boolean isInitialized() {
        return isInitialized;
    }

    public void processNextParallelSentences(Sentence source,Sentence target) {
        for (ResourceProcessor processor : srcResourceProcessors) {
            processor.processNextSentence(source);
        }
        for (ResourceProcessor processor : trgResourceProcessors) {
            processor.processNextSentence(target);
        }
        for (ResourceProcessorTwoSentences processor : srcTrgResourceProcessors) {
            processor.processNextParallelSentences(source, target);
        }
    }
}
