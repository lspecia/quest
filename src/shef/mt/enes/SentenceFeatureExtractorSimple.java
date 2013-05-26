package shef.mt.enes;



import shef.mt.util.PropertiesManager;
import shef.mt.util.Logger;
import shef.mt.util.XMLReader;
import shef.mt.util.XMLWriter;
import shef.mt.tools.NGramProcessor;
import shef.mt.tools.NGramExec;
import shef.mt.tools.ResourceManager;
import shef.mt.tools.Giza;
import shef.mt.tools.LanguageModel;
import shef.mt.tools.FileModel;
import shef.mt.tools.PPLProcessor;
//import shef.mt.tools.PosTagger;
import shef.mt.tools.Tokenizer;
import shef.mt.features.util.ParallelSentence;
import shef.mt.features.util.Sentence;
import shef.mt.features.util.FeatureManager;
import org.apache.commons.cli.*;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import shef.mt.features.impl.Feature;

/**
 * FeatureExtractor extracts Glassbox and/or Blackbox features from a pair of
 * source-target input files and a set of additional resources specified as
 * input parameters Usage: FeatureExtractor -input <source><target> -lang
 * <source lang><target lang> -feat [list of features] -mode [gb|bb|all] -gb
 * [list of GB resources] -rebuild -log <br> The valid arguments are:<br> -help
 * : print project help information<br> -input <source file> <target file> <word
 * alignment file>: the input source and target files<br> -lang <source
 * language> <target language> : source and target language<br> -feat : the list
 * of features. By default, all features corresponding to the selected mode will
 * be included<br> -gb [list of files] input files required for computing the
 * glassbox features<br> The arguments sent to the gb option depend on the MT
 * system -mode <GB|BB|ALL><br> -rebuild : run all preprocessing tools<br> -log
 * : enable logging<br> -config <config file> : use the configuration file
 * <config file>
 *
 *
 * @author Catalina Hallett & Mariano Felice<br>
 * Modified by Kashif Shah and Eleftherios Avramidis
 * University of Sheffield and DFKI
 */
public class SentenceFeatureExtractorSimple{

    private static int mtSys;
     private static String workDir;
    private static String wordLattices;
	
    private static String gizaAlignFile;
    /**
     * path to the input folder
     */
    private static String input;
    /**
     * running mode: bb , gb or all
     */
    private String mod;
    /**
     * path to the output folder
     */
    private static String output;
    private static String xmlFile;
    private static String sourceFile;
    private static String targetFile;
    private static String sourceLang;
    private static String targetLang;
    private static String features;
	private static String nbestInput;
	private static String onebestPhrases;
	private static String onebestLog;

    private static boolean forceRun = false;
 private static PropertiesManager resourceManager;
    private static FeatureManager featureManager;
    private static int ngramSize = 3;
	private static int IBM = 0;
	private static int CMU = 1;
    private static String configPath;
	private static String gbXML;

	
    /**
	 * set to 0 if the parameter sent to the -gb option is an xml file, 0 otherwise
	 */
	private int gbMode;

	/**
     * Initialises the FeatureExtractor from a set of parameters, for example
     * sent as command-line arguments
     *
	 * @param args
	 *            The list of arguments
     *
     */
    public SentenceFeatureExtractorSimple(String[] args) {
        workDir = System.getProperty("user.dir");
        new Logger("log.txt");
        parseArguments(args);

        input = workDir + File.separator + resourceManager.getString("input");
        output = workDir + File.separator + resourceManager.getString("output");
        System.out.println("input=" + input + "  output=" + output);

    }

    public static void main(String[] args) {
        long start = System.currentTimeMillis();
        SentenceFeatureExtractorSimple fe = new SentenceFeatureExtractorSimple(args);

        fe.run();
        long end = System.currentTimeMillis();
        Logger.log("processing completed in " + (end - start) / 1000 + " sec");
        Logger.close();
        System.out.println("processing completed in " + (end - start) / 1000
                + " sec");

    }

    /**
     * Parses the command line arguments and sets the respective fields
     * accordingly. This function sets the input source and target files, the
     * source and target language, the running mode (gb or bb), the additional
     * files required by the GB feature extractor, the rebuild and log options
     *
     * @param args The command line arguments
     */
    public void parseArguments(String[] args) {

        Option help = OptionBuilder.withArgName("help").hasArg()
                .withDescription("print project help information")
                .isRequired(false).create("help");

        Option input = OptionBuilder.withArgName("input").hasArgs(3)
                .isRequired(true).create("input");

        Option lang = OptionBuilder.withArgName("lang").hasArgs(2)
                .isRequired(false).create("lang");

        Option feat = OptionBuilder.withArgName("feat").hasArgs(1)
                .isRequired(false).create("feat");

		Option gb = OptionBuilder.withArgName("gb")
				.withDescription("GlassBox input files").hasOptionalArgs(2)
				.hasArgs(3).create("gb");

        Option mode = OptionBuilder
                .withArgName("mode")
                .withDescription("blackbox features, glassbox features or both")
                .hasArgs(1).isRequired(true).create("mode");

        Option config = OptionBuilder
                .withArgName("config")
                .withDescription("cofiguration file")
                .hasArgs(1).isRequired(false).create("config");

        Option rebuild = new Option("rebuild", "run all preprocessing tools");
        rebuild.setRequired(false);


		
        CommandLineParser parser = new PosixParser();
        Options options = new Options();

        options.addOption(help);
        options.addOption(input);
        options.addOption(mode);
        options.addOption(lang);
        options.addOption(feat);
		options.addOption(gb);
        options.addOption(rebuild);
        options.addOption(config);

        try {
            // parse the command line arguments
            CommandLine line = parser.parse(options, args);

            if (line.hasOption("config")) {
                resourceManager = new PropertiesManager(line.getOptionValue("config"));
            } else {
                resourceManager = new PropertiesManager();
            }


            if (line.hasOption("lang")) {
                String[] langs = line.getOptionValues("lang");
                sourceLang = langs[0];
                targetLang = langs[1];
            } else {
                sourceLang = resourceManager.getString("sourceLang.default");
                targetLang = resourceManager.getString("targetLang.default");
            }


            if (line.hasOption("input")) {
                // print the value of block-size
                String[] files = line.getOptionValues("input");
                xmlFile = files[0];
                sourceFile = xmlFile + sourceLang;
                targetFile = xmlFile + targetLang;
            }            
            
			if (line.hasOption("gb")) {
				String[] gbOpt = line.getOptionValues("gb");
				for (String s : gbOpt)
					System.out.println(s);
				if (gbOpt.length > 1) {
					mtSys = CMU;
					nbestInput = gbOpt[0];
					onebestPhrases = gbOpt[1];
					onebestLog = gbOpt[2];
					gbMode = 1;
				} else 
				{
					File f = new File(gbOpt[0]);
					if (f.isDirectory()){
						mtSys = IBM;
						wordLattices = gbOpt[0];
						gbMode = 1;
					}
					else {
						gbMode = 0;
						gbXML = gbOpt[0];
					}

				}
				
					

			}

            if (line.hasOption("mode")) {
                String[] modeOpt = line.getOptionValues("mode");
                setMod(modeOpt[0].trim());
                System.out.println(getMod());
                configPath = resourceManager.getString("featureConfig." + getMod());
                System.out.println("feature config:" + configPath);
                featureManager = new FeatureManager(configPath);
            }

            if (line.hasOption("feat")) {
                // print the value of block-size
                features = line.getOptionValue("feat");
                featureManager.setFeatureList(features);
            } else {
                featureManager.setFeatureList("all");
            }

            if (line.hasOption("rebuild")) {
                forceRun = true;
            }


        } catch (ParseException exp) {
            System.out.println("Unexpected exception:" + exp.getMessage());
        }
    }

   
	
 

    private static void loadGiza() {

        String gizaPath = resourceManager.getString("pair." + sourceLang
                + targetLang + ".giza.path");
        System.out.println(gizaPath);
        Giza giza = new Giza(gizaPath);
    }

    /*
     * Computes the perplexity and log probability for the source file Required
     * by features 8-13
     */
    private static void runNGramPPL() {
        // required by BB features 8-13
        NGramExec nge = new NGramExec(
                resourceManager.getString("tools.ngram.path"));
        System.out.println("runNgramPPL");
        File f = new File(sourceFile);
        String sourceOutput = input
                + File.separator + sourceLang + File.separator + f.getName()
                + ".ppl";
        f = new File(targetFile);
        String targetOutput = input
                + File.separator + targetLang + File.separator + f.getName()
                + ".ppl";
        nge.runNGramPerplex(sourceFile, sourceOutput,
                resourceManager.getString(sourceLang + ".lm"));
        System.out.println(resourceManager.getString(targetLang + ".lm"));
        nge.runNGramPerplex(targetFile, targetOutput,
                resourceManager.getString(targetLang + ".lm"));
    }

    /**
     * Computes the perplexity and log probability for the POS tagged target
     * file<br> Required by BB features 68-69<br> This function could be merged
     * with
     *
     * @seerunNGramPPL() but I separated them to make the code more readable
     *
     * @param posFile file tagged with parts-of-speech
     */
    private String runNGramPPLPos(String posFile) {
        NGramExec nge = new NGramExec(
                resourceManager.getString("tools.ngram.path"), forceRun);

        File f = new File(posFile);
        String posTargetOutput = input
                + File.separator + targetLang + File.separator + f.getName()
                + resourceManager.getString("tools.ngram.output.ext");
        nge.runNGramPerplex(posFile, posTargetOutput,
                resourceManager.getString(targetLang + ".poslm"));
        return posTargetOutput;
    }

    /**
     * Performs some basic processing of the input source and target files For
     * English, this consists of converting the input to lower case and
     * tokenizing For Arabic, this consists of transliteration and tokenization.
     * Please note that the current tools used for tokenizing Arabic also
     * perform POS tagging and morphological analysis Although we could separate
     * the tokenization process from the more in-depth text analysis performed
     * by these tools, for efficiency reasons this is not desirable The input
     * files are also copied to the /input folder. This is necessary because the
     * MADA analyser produces its output in the same folder as the input file,
     * which may cause problems if the right access rights are not available for
     * that particular folder
     */
    private static void preprocessing() {
        String sourceInputFolder = input + File.separator + sourceLang;
        String targetInputFolder = input + File.separator + targetLang;
        File origSourceFile = new File(sourceFile);
        File inputSourceFile = new File(sourceInputFolder + File.separator + origSourceFile.getName());

        System.out.println("source input:" + sourceFile);
        System.out.println("target input:" + targetFile);
        File origTargetFile = new File(targetFile);
        File inputTargetFile = new File(targetInputFolder + File.separator + origTargetFile.getName());
        try {
            System.out.println("copying input to " + inputSourceFile.getPath());
            copyFile(origSourceFile, inputSourceFile);
            System.out.println("copying input to " + inputTargetFile.getPath());
            copyFile(origTargetFile, inputTargetFile);
        } catch (Exception e) {
            e.printStackTrace();
        }

        //run tokenizer for source (English)
        System.out.println("running tokenizer");
       
        String truecasePath = "";
        truecasePath = resourceManager.getString("english.truecase") + "|" + resourceManager.getString("english.truecase.model");
        Tokenizer enTok = new Tokenizer(inputSourceFile.getPath(), inputSourceFile.getPath() + ".tok", truecasePath, resourceManager.getString("english.tokenizer"), "en", forceRun);
        
        
        // Tokenizer enTok = new Tokenizer(inputSourceFile.getPath(), inputSourceFile.getPath() + ".tok", resourceManager.getString("english.lowercase"), resourceManager.getString("english.tokenizer"), "en", forceRun);
        enTok.run();
        sourceFile = enTok.getTok();
        System.out.println(sourceFile);

        //run tokenizer for target (Spanish)
        System.out.println("running tokenizer");
//        Tokenizer esTok = new Tokenizer(inputTargetFile.getPath(), inputTargetFile.getPath() + ".tok", resourceManager.getString("spanish.lowercase"), resourceManager.getString("spanish.tokenizer"), "es", forceRun);
       
         truecasePath = resourceManager.getString("spanish.truecase") + "|" + resourceManager.getString("spanish.truecase.model");
         Tokenizer esTok = new Tokenizer(inputTargetFile.getPath(),inputTargetFile.getPath()+".tok", truecasePath,resourceManager.getString("spanish.tokenizer"), "es", forceRun);
        
        esTok.run();
        targetFile = esTok.getTok();
        System.out.println(targetFile);

        // Normalize files to avoid strange characters in UTF-8 that may break the PoS tagger
        //normalize_utf8();

    }

    private static LanguageModel processNGrams() {
        // required by BB features 30-44
        NGramProcessor ngp = new NGramProcessor(
                resourceManager.getString(sourceLang + ".ngram"));
        return ngp.run();
    }

    /**
     * constructs the folders required by the application. These are, typically:
     * <br> <ul><li>/input and subfolders <ul> <li>/input/<i>sourceLang</i>,
     * /input/<i>targetLang</i> (for storing the results of processing the input
     * files with various tools, such as pos tagger, transliterator,
     * morphological analyser),<br> <li>/input/systems/<i>systemName</i> (for
     * storing system specific resources - for example, the compiled and
     * processed word lattices in the case of the IBM system </ul> <li> /output
     * (for storing the resulting feature files), </ul>
     */
    public void constructFolders() {

        File f = new File(input);
        if (!f.exists()) {
            f.mkdir();
            System.out.println("folder created " + f.getPath());
        }


        f = new File(input + File.separator + sourceLang);
        if (!f.exists()) {
            f.mkdir();
            System.out.println("folder created " + f.getPath());
        }
        f = new File(input + File.separator + targetLang);
        if (!f.exists()) {
            f.mkdir();
            System.out.println("folder created " + f.getPath());
        }
 /*       f = new File(input + File.separator + targetLang + File.separator
                + "temp");
        if (!f.exists()) {
            f.mkdir();
            System.out.println("folder created " + f.getPath());
        }

        f = new File(input + File.separator + "systems");
        if (!f.exists()) {
            f.mkdir();
            System.out.println("folder created " + f.getPath());
        }

        f = new File(input + File.separator + "systems" + File.separator
                + "IBM");
        if (!f.exists()) {
            f.mkdir();
            System.out.println("folder created " + f.getPath());
        }

        f = new File(input + File.separator + "systems" + File.separator
                + "CMU");
        if (!f.exists()) {
            f.mkdir();
            System.out.println("folder created " + f.getPath());
        }
*/
        String output = resourceManager.getString("output");
        f = new File(output);
        if (!f.exists()) {
            f.mkdir();
            System.out.println("folder created " + f.getPath());
        }
    }

    /**
     * Runs the Feature Extractor<br> <ul> <li>constructs the required folders
     * <li>runs the pre-processing tools <li>uuns the BB features, GB features
     * or both according to the command line parameters </ul>
     */

    private static void extractXmlSentences(){
    	XMLReader sentenceReader = new XMLReader(xmlFile);
    	System.err.print("Writing plain sentences to "+ sourceFile +" and "+targetFile+"\n");
    	try {
    		
	    	File sf = new File(sourceFile);
	    	sf.createNewFile();
	    	FileWriter sfw = new FileWriter(sf.getAbsoluteFile());
	    	BufferedWriter sourceWriter = new BufferedWriter(sfw);
	    	
	    	File tf = new File(targetFile);
	    	tf.createNewFile();
	    	FileWriter tfw = new FileWriter(tf.getAbsoluteFile());
	    	BufferedWriter targetWriter = new BufferedWriter(tfw);

                int i = 0;
    	
	    	while (sentenceReader.hasNext()) {
	    		ParallelSentence parallelSentence = sentenceReader.next();
	    		String sourceText = parallelSentence.getSource().getText();
	    		sourceWriter.write(sourceText);
	    		sourceWriter.newLine();
	    		
	    		for (Sentence targetSentence:parallelSentence.getTargetSentences()){
	    			targetWriter.write(targetSentence.getText());
	    			targetWriter.newLine();
	    		} 
                        System.out.println(String.valueOf(i++));  				
	    	}
    	
	    	targetWriter.close();
	    	//tfw.close();
	    	sourceWriter.close();
	    	//sfw.close();
    	
    	} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	    	
    }
    
	
    /**
     * runs the BB features
     */
    public void runBB() {
    	extractXmlSentences();
        File f = new File(sourceFile);
        String sourceFileName = f.getName();
        f = new File(targetFile);
        String targetFileName = f.getName();
        String outputFileName = sourceFileName + "_to_" + targetFileName
               + ".out.jcml";
        String out = resourceManager.getString("output") + File.separator + outputFileName;
        System.out.println("Output will be: " + out);
        
        String pplSourcePath = resourceManager.getString("input")
                + File.separator + sourceLang + File.separator + sourceFileName
                + resourceManager.getString("tools.ngram.output.ext");
        String pplTargetPath = resourceManager.getString("input")
                + File.separator + targetLang + File.separator + targetFileName
                + resourceManager.getString("tools.ngram.output.ext");


      
      
        runNGramPPL();

        PPLProcessor pplProcSource = new PPLProcessor(pplSourcePath,
                new String[]{"logprob", "ppl", "ppl1"});
        PPLProcessor pplProcTarget = new PPLProcessor(pplTargetPath,
                new String[]{"logprob", "ppl", "ppl1"});

        FileModel fm = new FileModel(sourceFile,
                resourceManager.getString(sourceLang + ".corpus"));
  

        loadGiza();
        processNGrams();

        try {
            XMLReader sentenceReader = new XMLReader(xmlFile);
            XMLWriter output = new XMLWriter(out);
            BufferedReader posSource = null;
            BufferedReader posTarget = null;
   
            ResourceManager.printResources();
            int sentCount = 0;

            ParallelSentence parallelSentence;

            //read in each line from the source and target files
            //create a sentence from each
            //process each sentence
            //run the features on the sentences
            while (sentenceReader.hasNext()) {            	
                parallelSentence = sentenceReader.next();
                Sentence sourceSentence = parallelSentence.getSource();
                HashMap<String,Object> origSourceFeatures = sourceSentence.getAttributes();
                
                List<Sentence> targetSentences = parallelSentence.getTargetSentences();

                ngramSize = Integer.parseInt(resourceManager.getString("ngramsize"));
                
                sourceSentence.computeNGrams(ngramSize);
                pplProcSource.processNextSentence(sourceSentence);
                //sourceParserProcessor.processNextSentence(sourceSent);
                int targetId = 0;
                
                List<Sentence> annotatedTargetSentences = new ArrayList<Sentence>(); 
                
                for (Sentence targetSentence:targetSentences){
                    targetId++;
                    HashMap<String,Object> origTargetFeatures = targetSentence.getAttributes();
                    
                    targetSentence.computeNGrams(ngramSize);
                    pplProcTarget.processNextSentence(targetSentence);
            	    //targetParserProcessor.processNextSentence(targetSent);
                    
                    //append the new attributes to the existing ones
                    
                    Sentence origTargetSentence = new Sentence(targetSentence.getText(), origTargetFeatures);
                    origTargetSentence.addValues(featureManager.getFeaturesMap(sourceSentence, targetSentence, targetId));
                    annotatedTargetSentences.add(origTargetSentence);
                }
                
                Sentence origSourceSentence = new Sentence(sourceSentence.getText(), origSourceFeatures);
                
                
                //create a new copy of the parallel sentence by adding the hashmap with the features   
                ParallelSentence annotatedParallelSentence = new ParallelSentence(origSourceSentence, annotatedTargetSentences, parallelSentence.getAttributes());
                output.write(annotatedParallelSentence);
                ++sentCount;
            }
            if (posSource != null) {
                posSource.close();
            }
            if (posTarget != null) {
                posTarget.close();
            }

            sentenceReader.close();
            output.close();
            Logger.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
		//pplProcSource.close();
		//pplProcTarget.close();
	//	pplPosTarget.close();
    }

    
  private static void copyFile(File sourceFile, File destFile)
            throws IOException {
        if (sourceFile.equals(destFile)) {
            System.out.println("source=dest");
            return;
        }
        if (!destFile.exists()) {
            destFile.createNewFile();
        }

        java.nio.channels.FileChannel source = null;
        java.nio.channels.FileChannel destination = null;
        try {
            source = new FileInputStream(sourceFile).getChannel();
            destination = new FileOutputStream(destFile).getChannel();
            destination.transferFrom(source, 0, source.size());
        } finally {
            if (source != null) {
                source.close();
            }
            if (destination != null) {
                destination.close();
            }
        }
    }

    /**
     * returns the working mode: bb, gb or all
     *
     * @return the working mode
     */
    public String getMod() {
        return mod;
    }

    /**
     * sets the working mode
     *
     * @param mod the working mode. Valid values are bb, gb and all
     */
    public void setMod(String mod) {
        this.mod = mod;
    }


	/**
	 * runs the GB features
	 */
	

public void run() {
        constructFolders();
        //preprocessing();
        runBB();
    }
        
        
 }

	
