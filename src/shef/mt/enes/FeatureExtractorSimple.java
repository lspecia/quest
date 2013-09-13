package shef.mt.enes;



import shef.mt.tools.mqm.Context;
import shef.mt.tools.mqm.MQMManager;
import shef.mt.xmlwrap.MOSES_XMLWrapper;
import shef.mt.util.PropertiesManager;
import shef.mt.util.Logger;
//import shef.mt.tools.NGramExec;
import shef.mt.tools.ResourceManager;
import shef.mt.tools.FileModel;
//import shef.mt.tools.LanguageModel;
import shef.mt.tools.POSProcessor;
import shef.mt.tools.MTOutputProcessor;
import shef.mt.tools.Tokenizer;
import shef.mt.tools.Giza;
import shef.mt.tools.TopicDistributionProcessor;
import shef.mt.tools.BParserProcessor;
//import shef.mt.tools.NGramProcessor;
//import shef.mt.tools.PPLProcessor;
import shef.mt.tools.PosTagger;
import shef.mt.tools.GlobalLexicon;
import shef.mt.tools.Triggers;
import shef.mt.tools.TriggersProcessor;
import shef.mt.features.util.Sentence;
import shef.mt.features.util.FeatureManager;
import org.apache.commons.cli.*;
import java.io.*;

import shef.mt.features.impl.Feature;
import shef.mt.tools.DiscourseRepetition;
import shef.mt.tools.TerminologyProcessor;

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
 */
public class FeatureExtractorSimple{

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
	private static int MOSES = 1;
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
    public FeatureExtractorSimple(String[] args) {
        workDir = System.getProperty("user.dir");
        new Logger("log.txt");
        parseArguments(args);

        input = workDir + File.separator + resourceManager.getString("input");
        output = workDir + File.separator + resourceManager.getString("output");
        System.out.println("input=" + input + "  output=" + output);

    }

    public static void main(String[] args) {
        long start = System.currentTimeMillis();
        FeatureExtractorSimple fe = new FeatureExtractorSimple(args);

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

            if (line.hasOption("input")) {
                // print the value of block-size
                String[] files = line.getOptionValues("input");
                sourceFile = files[0];
                targetFile = files[1];
            }

            if (line.hasOption("lang")) {
                String[] langs = line.getOptionValues("lang");
                sourceLang = langs[0];
                targetLang = langs[1];
            } else {
                sourceLang = resourceManager.getString("sourceLang.default");
                targetLang = resourceManager.getString("targetLang.default");
            }

			if (line.hasOption("gb")) {
				String[] gbOpt = line.getOptionValues("gb");
				for (String s : gbOpt)
					System.out.println(s);
				if (gbOpt.length > 1) {
					mtSys = MOSES;
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

    public void runPOSTagger() {
        // required by BB features 65-69, 75-80
        String sourceOutput = runPOS(sourceFile, sourceLang, "source");
        String targetOutput = runPOS(targetFile, targetLang, "target");

    }

	
    /**
     * runs the part of speech tagger
     * @param file input file
     * @param lang language
     * @param type source or target
     * @return path to the output file of the POS tagger
     */
    public String runPOS(String file, String lang, String type) {
        String posName = resourceManager.getString(lang + ".postagger");
        String langResPath = input + File.separator + lang;
        File f = new File(file);
        String absoluteSourceFilePath = f.getAbsolutePath();
        String fileName = f.getName();
        String relativeFilePath = langResPath + File.separator + fileName
                + ".pos";
        String absoluteOutputFilePath = (new File(relativeFilePath))
                .getAbsolutePath();
        String posSourceTaggerPath = resourceManager.getString(lang
                    + ".postagger.exePath");
        String outPath = "";
        try {
            Class c = Class.forName(posName);
            PosTagger tagger = (PosTagger) c.newInstance();
            tagger.setParameters(type, posName, posSourceTaggerPath,
                    absoluteSourceFilePath, absoluteOutputFilePath);
            PosTagger.ForceRun(forceRun);
            outPath = tagger.run();
        } catch (Exception e) {
            e.printStackTrace();
        }
        // returns the path of the output file; this is for convenience only so
        // we do't have to calculate it again
        return outPath;

    }

    private static void loadGiza() {

        String gizaPath = resourceManager.getString("pair." + sourceLang
                + targetLang + ".giza.path");
        System.out.println(gizaPath);
        Giza giza = new Giza(gizaPath);
    }
    
    private static void loadGlobalLexicon() {
        final String glmodelpath = resourceManager.getString("pair." + sourceLang
                + targetLang + ".glmodel.path");
        final Double minweight = Double.valueOf(
                resourceManager.getString("pair." + sourceLang
                    + targetLang + ".glmodel.minweight"));
        GlobalLexicon globalLexicon = new GlobalLexicon(glmodelpath, minweight);
    }

    /*
     * Computes the perplexity and log probability for the source file Required
     * by features 8-13
     */
    private static void runNGramPPL() {
        // required by BB features 8-13
//COMMENTED OUT FOR MTM    	
//        NGramExec nge = new NGramExec(
//                resourceManager.getString("tools.ngram.path"));
        System.out.println("runNgramPPL");
        File f = new File(sourceFile);
        String sourceOutput = input
                + File.separator + sourceLang + File.separator + f.getName()
                + ".ppl";
        f = new File(targetFile);
        String targetOutput = input
                + File.separator + targetLang + File.separator + f.getName()
                + ".ppl";
//COMMENTED OUT FOR MTM        
//        nge.runNGramPerplex(sourceFile, sourceOutput,
//                resourceManager.getString(sourceLang + ".lm"));
//        System.out.println(resourceManager.getString(targetLang + ".lm"));
//        nge.runNGramPerplex(targetFile, targetOutput,
//                resourceManager.getString(targetLang + ".lm"));
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
//COMMENTED OUT FOR MTM    
//    private String runNGramPPLPos(String posFile) {
//        NGramExec nge = new NGramExec(
//                resourceManager.getString("tools.ngram.path"), forceRun);
//
//        File f = new File(posFile);
//        String posTargetOutput = input
//                + File.separator + targetLang + File.separator + f.getName()
//                + resourceManager.getString("tools.ngram.output.ext");
//        nge.runNGramPerplex(posFile, posTargetOutput,
//                resourceManager.getString(targetLang + ".poslm"));
//        return posTargetOutput;
//    }

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
       
       String src_abbr = ""; 
        if (sourceLang.equals ("english"))
                src_abbr = "en";
            else if (sourceLang.equals ("spanish"))
                src_abbr = "es";
            else if (sourceLang.equals ("french"))
                src_abbr = "fr";
            else if (sourceLang.equals ("german"))
                src_abbr = "de"; 
            else 
                System.out.println("Don't recognise the source language");
        
        
        String tgt_abbr = ""; 
        if (targetLang.equals ("english"))
                tgt_abbr = "en";
            else if (targetLang.equals ("spanish"))
                tgt_abbr = "es";
            else if (targetLang.equals ("french"))
                tgt_abbr = "fr";
            else if (targetLang.equals ("german"))
                tgt_abbr = "de"; 
            else 
                System.out.println("Don't recognise the target language");
        
                
                String truecasePath = "";
        truecasePath = resourceManager.getString(sourceLang + ".truecase") + "|" + resourceManager.getString(sourceLang + ".truecase.model");
        Tokenizer enTok = new Tokenizer(inputSourceFile.getPath(), inputSourceFile.getPath() + ".tok", truecasePath, resourceManager.getString(sourceLang + ".tokenizer"), src_abbr, forceRun);
        
        
        // Tokenizer enTok = new Tokenizer(inputSourceFile.getPath(), inputSourceFile.getPath() + ".tok", resourceManager.getString("english.lowercase"), resourceManager.getString("english.tokenizer"), "en", forceRun);
        enTok.run();
        sourceFile = enTok.getTok();
        System.out.println(sourceFile);

        //run tokenizer for target (Spanish)
        System.out.println("running tokenizer");
//        Tokenizer esTok = new Tokenizer(inputTargetFile.getPath(), inputTargetFile.getPath() + ".tok", resourceManager.getString("spanish.lowercase"), resourceManager.getString("spanish.tokenizer"), "es", forceRun);
       
         truecasePath = resourceManager.getString(targetLang + ".truecase") + "|" + resourceManager.getString(targetLang + ".truecase.model");
         Tokenizer esTok = new Tokenizer(inputTargetFile.getPath(),inputTargetFile.getPath() + ".tok", truecasePath, resourceManager.getString(targetLang + ".tokenizer"), tgt_abbr, forceRun);
        
        esTok.run();
        targetFile = esTok.getTok();
        System.out.println(targetFile);

        // Normalize files to avoid strange characters in UTF-8 that may break the PoS tagger
        //normalize_utf8();

    }

//COMMENTED OUT FOR MTM
/**    private static LanguageModel processNGrams() {
        // required by BB features 30-44
        NGramProcessor ngp = new NGramProcessor(
                resourceManager.getString(sourceLang + ".ngram"));
        return ngp.run();
    }
*/
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
        f = new File(input + File.separator + targetLang + File.separator
                + "temp");
        if (!f.exists()) {
            f.mkdir();
            System.out.println("folder created " + f.getPath());
        }
/*
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
                + "MOSES");
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
     * <li>runs the pre-processing tools <li>runs the BB features, GB features
     * or both according to the command line parameters </ul>
     */
	public  String initialiseGBResources() {
		// transform the m output to xml
		String xmlOut = resourceManager.getString("input") + File.separator
				+ "systems" + File.separator;
		File f = new File(sourceFile);
		if (mtSys == MOSES) {
			xmlOut += "moses_" + f.getName() + ".xml";
			System.out.println(xmlOut);
			MOSES_XMLWrapper cmuwrap = new MOSES_XMLWrapper(nbestInput, xmlOut,
					onebestPhrases, onebestLog);
			cmuwrap.run();
   
			// now send the xml output from cmuwrap to be processed
		} 
                
		return xmlOut;
	}

	
    /**
     * runs the BB features
     */
    public void runBB() {
        File f = new File(sourceFile);
        String sourceFileName = f.getName();
        f = new File(targetFile);
        String targetFileName = f.getName();
        String outputFileName = sourceFileName + "_to_" + targetFileName
                + ".out";
        String out = resourceManager.getString("output") + File.separator + outputFileName;
        System.out.println("Output will be: " + out);

        String pplSourcePath = resourceManager.getString("input")
                + File.separator + sourceLang + File.separator + sourceFileName
                + resourceManager.getString("tools.ngram.output.ext");
        String pplTargetPath = resourceManager.getString("input")
                + File.separator + targetLang + File.separator + targetFileName
                + resourceManager.getString("tools.ngram.output.ext");


        String pplPOSTargetPath = resourceManager.getString("input")
                + File.separator + targetLang + File.separator + targetFileName + PosTagger.getXPOS()
                + resourceManager.getString("tools.ngram.output.ext");
        runNGramPPL();

        TerminologyProcessor termProc = new TerminologyProcessor();
        termProc.initialize(resourceManager, featureManager);
        
        //discourse features
        DiscourseRepetition discRepTarget = new DiscourseRepetition();
        discRepTarget.initialize(resourceManager, featureManager, "target");
        
        DiscourseRepetition discRepSource = new DiscourseRepetition();
        discRepSource.initialize(resourceManager, featureManager, "source");

//COMMENTED OUT FOR MTM
//        PPLProcessor pplProcSource = new PPLProcessor(pplSourcePath,
//                new String[]{"logprob", "ppl", "ppl1"});
//        PPLProcessor pplProcTarget = new PPLProcessor(pplTargetPath,
//                new String[]{"logprob", "ppl", "ppl1"});
//

          FileModel fm = new FileModel(sourceFile,
                resourceManager.getString(sourceLang + ".corpus"));

         // FileModel fm = new FileModel(sourceFile,
           //     resourceManager.getString("source" + ".corpus"));

//COMMENTED OUT FOR MTM
        String sourcePosOutput = runPOS(sourceFile, sourceLang, "source");
        String targetPosOutput = runPOS(targetFile, targetLang, "target");

//COMMENTED OUT FOR MTM
//        String targetPPLPos = runNGramPPLPos(targetPosOutput + PosTagger.getXPOS());
//        System.out.println("---------TARGET PPLPOS: " + targetPPLPos);
//        PPLProcessor pplPosTarget = new PPLProcessor(targetPPLPos,
//                new String[]{"poslogprob", "posppl", "posppl1"});

        loadGiza();
//COMMENTED OUT FOR MTM
//        processNGrams();
       boolean gl = false;
            String temp0 = resourceManager.getString("GL");
            if (temp0.equals("1")) {
                gl = true ;
            }

        if (gl) {
         loadGlobalLexicon();
        }

        //MQM kicks in
        MQMManager.getInstance().initialize(resourceManager);
        Context context = new Context();
        context.setSourceFilePath(sourceFile);
        context.setTargetFilePath(targetFile);
        MQMManager.getInstance().globalProcessing(context);

        try {
            BufferedReader brSource = new BufferedReader(new FileReader(
                    sourceFile));
            BufferedReader brTarget = new BufferedReader(new FileReader(
                    targetFile));
            BufferedReader brPosSource = new BufferedReader(new FileReader(
                    sourcePosOutput+".XPOS.lemm"));
            BufferedReader brPosTarget = new BufferedReader(new FileReader(
                    targetPosOutput+".XPOS.lemm"));

            BufferedWriter output = new BufferedWriter(new FileWriter(out));
            BufferedReader posSource = null;
            BufferedReader posTarget = null;
            boolean posSourceExists = ResourceManager
                    .isRegistered("sourcePosTagger");
            boolean posTargetExists = ResourceManager
                    .isRegistered("targetPosTagger");
            POSProcessor posSourceProc = null;
            POSProcessor posTargetProc = null;





            //lefterav: Berkeley parser modifications start here
            //Check if user has defined the grammar files for source
            //and target language

           //   if ( ResourceManager.isRegistered("BParser")){
            boolean bp = false;
            String temp = resourceManager.getString("BP");
            if (temp.equals("1")) {
                bp = true ;
            }

//COMMENTED OUT FOR MTM
//            BParserProcessor sourceParserProcessor = null;
//             BParserProcessor targetParserProcessor = null;
//
//          if (bp) {
//COMMENTED OUT FOR MTM
//            sourceParserProcessor = new BParserProcessor();
//            targetParserProcessor = new BParserProcessor();
//            sourceParserProcessor.initialize(sourceFile, resourceManager, sourceLang);
//            targetParserProcessor.initialize(targetFile, resourceManager, targetLang);
//          }
   // }


     /**
            * BEGIN: Added by Raphael Rubino for the Topic Model Features
	    */

          boolean tm = false;
            String temp1 = resourceManager.getString("TM");
            if (temp1.equals("1")) {
                tm = true ;
            }
          TopicDistributionProcessor sourceTopicDistributionProcessor = null;
          TopicDistributionProcessor targetTopicDistributionProcessor = null;
          if (tm) {
            String sourceTopicDistributionFile = resourceManager.getString(sourceLang + ".topic.distribution");
            String targetTopicDistributionFile = resourceManager.getString(targetLang + ".topic.distribution");
             sourceTopicDistributionProcessor = new TopicDistributionProcessor(sourceTopicDistributionFile, "sourceTopicDistribution");
             targetTopicDistributionProcessor = new TopicDistributionProcessor(targetTopicDistributionFile, "targetTopicDistribution");

          }
            /* END: Added by Raphael Rubino for the Topic Model Features
            */
            
            if (posSourceExists) {
                posSourceProc = new POSProcessor(sourcePosOutput);
                posSource = new BufferedReader(new InputStreamReader(new FileInputStream(sourcePosOutput), "utf-8"));
            }
            if (posTargetExists) {
                posTargetProc = new POSProcessor(targetPosOutput);
                posTarget = new BufferedReader(new InputStreamReader(new FileInputStream(targetPosOutput)));
            }
            ResourceManager.printResources();
            Sentence sourceSent;
            Sentence targetSent;
            int sentCount = 0;

            String lineSource = brSource.readLine();
            String lineTarget = brTarget.readLine();
				
            String linePosSource = brPosSource.readLine();
            String linePosTarget = brPosTarget.readLine();


             /**
             * Triggers (by David Langlois)
             */

            boolean tr = false;
            String temp2 = resourceManager.getString("TR");
            if (temp2.equals("1")) {
                tr = true ;
            }


            Triggers itl_target = null;
            TriggersProcessor itl_target_p = null;
            Triggers itl_source = null;
            TriggersProcessor itl_source_p = null;
            //TriggersProcessor itl_source_p = null;
            Triggers itl_source_target = null;
            TriggersProcessor itl_source_target_p = null;

            if (tr){


             itl_target =
                    new Triggers(
                            resourceManager.getString("target.intra.triggers.file"),
                            Integer.parseInt(resourceManager.getString("nb.max.triggers.target.intra")),
                            resourceManager.getString("phrase.separator"));
             itl_target_p = new TriggersProcessor(itl_target);

             itl_source =
                    new Triggers(
                            resourceManager.getString("source.intra.triggers.file"),
                            Integer.parseInt(resourceManager.getString("nb.max.triggers.source.intra")),
                            resourceManager.getString("phrase.separator"));
             itl_source_p = new TriggersProcessor(itl_source);


             itl_source_target =
                    new Triggers(
                            resourceManager.getString("source.target.inter.triggers.file"),
                            Integer.parseInt(resourceManager.getString("nb.max.triggers.source.target.inter")),
                            resourceManager.getString("phrase.separator"));
             itl_source_target_p =
                    new TriggersProcessor(itl_source_target);

            }
            /*
             * End modification for Triggers
             */





            //read in each line from the source and target files
            //create a sentence from each
            //process each sentence
            //run the features on the sentences
            while ((lineSource != null) && (lineTarget != null)) {

                //lineSource = lineSource.trim().substring(lineSource.indexOf(" ")).replace("+", "");
                sourceSent = new Sentence(lineSource, linePosSource, sentCount);
                targetSent = new Sentence(lineTarget, linePosTarget, sentCount);

         //       System.out.println("Processing sentence "+sentCount);
           //     System.out.println("SORCE: " + sourceSent.getText());
             //   System.out.println("TARGET: " + targetSent.getText());



                termProc.processNextSentence(targetSent);
                
                discRepTarget.processNextSentence(targetSent);
                discRepSource.processNextSentence(sourceSent);
                
                if (posSourceExists) {
                    posSourceProc.processSentence(sourceSent);
                }
                if (posTargetExists) {
                    posTargetProc.processSentence(targetSent);
                }
//COMMENTED OUT FOR MTM
//                sourceSent.computeNGrams(3);
//                targetSent.computeNGrams(3);
//                pplProcSource.processNextSentence(sourceSent);
//                pplProcTarget.processNextSentence(targetSent);
//COMMENTED OUT FOR MTM
//                pplPosTarget.processNextSentence(targetSent);

                   //lefterav: Parse code here
//COMMENTED OUT FOR MTM
//                if(bp){
//                sourceParserProcessor.processNextSentence(sourceSent);
//            	targetParserProcessor.processNextSentence(targetSent);
//                }

                if(tm){

                sourceTopicDistributionProcessor.processNextSentence(sourceSent);
                 targetTopicDistributionProcessor.processNextSentence(targetSent);
                }


                  // modified by David
                if(tr){
                itl_source_p.processNextSentence(sourceSent);
                itl_target_p.processNextSentence(targetSent);
                itl_source_target_p.processNextParallelSentences(sourceSent, targetSent);
                }
                // end modification by David

                //MQM kicks in
                MQMManager.getInstance().processNextParallelSentences(sourceSent, targetSent);

                ++sentCount;
                output.write(featureManager.runFeatures(sourceSent, targetSent));
                output.newLine();
                lineSource = brSource.readLine();
                lineTarget = brTarget.readLine();
                
                linePosSource = brPosSource.readLine();
                linePosTarget = brPosTarget.readLine();
            }
            if (posSource != null) {
                posSource.close();
            }
            if (posTarget != null) {
                posTarget.close();
            }

            brSource.close();
            brTarget.close();
            output.close();
            Logger.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
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
	public  void runGB() {
		MTOutputProcessor mtop = null;

		if (gbMode == 1)
			gbXML = initialiseGBResources();

		String nbestSentPath = resourceManager.getString("input")
				+ File.separator + targetLang + File.separator + "temp";
		String ngramExecPath = resourceManager.getString("tools.ngram.path");

		mtop = new MTOutputProcessor(gbXML, nbestSentPath, ngramExecPath,
				ngramSize);
//		MorphAnalysisProcessor map = new MorphAnalysisProcessor(madaFile);

		File f = new File(sourceFile);
		String sourceFileName = f.getName();
		f = new File(targetFile);
		String targetFileName = f.getName();

		String outputFileName = sourceFileName + "_to_" + targetFileName
				+ ".out";

		String out = resourceManager.getString("output") + File.separator + getMod()
				+ outputFileName;
		System.out.println("Output will be: " + out);

		String lineTarget;

		try {
			BufferedReader brSource = new BufferedReader(new FileReader(
					sourceFile));
			BufferedReader brTarget = new BufferedReader(new FileReader(
					targetFile));
			BufferedWriter output = new BufferedWriter(new FileWriter(out));

			ResourceManager.printResources();

			Sentence targetSent;
			Sentence sourceSent;
			int sentCount = 0;

			String lineSource;

			while (((lineSource = brSource.readLine()) != null)
					&& ((lineTarget = brTarget.readLine()) != null)) {

				lineSource = lineSource.trim().substring(lineSource.indexOf(" "));
				sourceSent = new Sentence(lineSource,
						sentCount);
				targetSent = new Sentence(lineTarget, sentCount);

                //map.processNextSentence(sourceSent);
				mtop.processNextSentence(sourceSent);

				++sentCount;
				output.write(featureManager.runFeatures(sourceSent, targetSent));
				output.write("\r\n");

}
			brSource.close();
			brTarget.close();
			output.close();
			featureManager.printFeatureIndeces();
			Logger.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
        }
/*
public void run() {
        constructFolders();
        preprocessing();
        runBB();
    }*/
        
        
 

 
 	public void runAll() {
		File f = new File(sourceFile);
                String sourceFileName = f.getName();
                f = new File(targetFile);
                String targetFileName = f.getName();
                String outputFileName = sourceFileName + "_to_" + targetFileName
                + ".out";
                String out = resourceManager.getString("output") + File.separator + outputFileName;
                System.out.println("Output will be: " + out);

		
		MTOutputProcessor mtop = null;

		if (gbMode == 1)
			gbXML = initialiseGBResources();

		String nbestSentPath = resourceManager.getString("input")
				+ File.separator + targetLang + File.separator + "temp";
		String ngramExecPath = resourceManager.getString("tools.ngram.path");

		mtop = new MTOutputProcessor(gbXML, nbestSentPath, ngramExecPath,
				ngramSize);
		

		
		
		//wlv.mt.features.coherence.Coherence coh = new wlv.mt.features.coherence.Coherence(
		//		getTargetFile());

//COMMENTED OUT FOR MTM		
//		String pplSourcePath = resourceManager.getString("input")
//                + File.separator + sourceLang + File.separator + sourceFileName
//                + resourceManager.getString("tools.ngram.output.ext");
//        String pplTargetPath = resourceManager.getString("input")
//                + File.separator + targetLang + File.separator + targetFileName
//                + resourceManager.getString("tools.ngram.output.ext");
//
//
//        String pplPOSTargetPath = resourceManager.getString("input")
//                + File.separator + targetLang + File.separator + targetFileName + PosTagger.getXPOS()
//                + resourceManager.getString("tools.ngram.output.ext");
//        runNGramPPL();

//        PPLProcessor pplProcSource = new PPLProcessor(pplSourcePath,
//                new String[]{"logprob", "ppl", "ppl1"});
//        PPLProcessor pplProcTarget = new PPLProcessor(pplTargetPath,
//                new String[]{"logprob", "ppl", "ppl1"});

        FileModel fm = new FileModel(sourceFile,
                resourceManager.getString(sourceLang + ".corpus"));
        String sourcePosOutput = runPOS(sourceFile, sourceLang, "source");
        String targetPosOutput = runPOS(targetFile, targetLang, "target");

//COMMENTED OUT FOR MTM        
//        String targetPPLPos = runNGramPPLPos(targetPosOutput + PosTagger.getXPOS());
//        System.out.println("---------TARGET PPLPOS: " + targetPPLPos);
//        PPLProcessor pplPosTarget = new PPLProcessor(targetPPLPos,
//                new String[]{"poslogprob", "posppl", "posppl1"});

        loadGiza();
//COMMENTED OUT FOR MTM        
//        processNGrams();

		try {
			BufferedReader brSource = new BufferedReader(new FileReader(
					sourceFileName));
			BufferedReader brTarget = new BufferedReader(new FileReader(
					targetFileName));
			BufferedWriter output = new BufferedWriter(new FileWriter(out));
			BufferedReader posSource = null;
			BufferedReader posTarget = null;
			boolean posSourceExists = ResourceManager
					.isRegistered("sourcePosTagger");
			boolean posTargetExists = ResourceManager
					.isRegistered("targetPosTagger");
			POSProcessor posSourceProc = null;
			POSProcessor posTargetProc = null;
			if (posSourceExists) {
				posSourceProc = new POSProcessor(sourcePosOutput);
				 posSource = new BufferedReader(new InputStreamReader(new
				 FileInputStream(sourcePosOutput), "utf-8"));
			}
			if (posTargetExists) {
				posTargetProc = new POSProcessor(targetPosOutput);
				 posTarget = new BufferedReader(new InputStreamReader(new
				 FileInputStream(targetPosOutput)));
			}
			ResourceManager.printResources();

                        Sentence targetSent;
			// HACK
			Sentence sourceSent;
			int sentCount = 0;
			// HACK
			String lineSource = brSource.readLine();
			String lineTarget = brTarget.readLine();
			// HACK
			int result;
			
			while ((lineSource != null)	&& (lineTarget != null)) {

	//the MADA-tokenised files contain start each sentence with the setence ID. We put it there (why?) - no we've got to remove it
                           
                                lineSource = lineSource.trim().substring(lineSource.indexOf(" ")).replace("+", "");
				sourceSent = new Sentence(lineSource,
						sentCount);
				targetSent = new Sentence(lineTarget, sentCount);
				System.out.println("Processing sentence "+sentCount);
				if (posSourceExists) {

					posSourceProc.processSentence(sourceSent);

				}
				if (posTargetExists) {

					posTargetProc.processSentence(targetSent);
				}

				
//COMMENTED OUT FOR MTM				
//				sourceSent.computeNGrams(3);
//				targetSent.computeNGrams(3);

//				pplProcSource.processNextSentence(sourceSent);
//
//				pplProcTarget.processNextSentence(targetSent);

//COMMENTED OUT FOR MTM				
//				pplPosTarget.processNextSentence(targetSent);

//				coh.processNextSentence(targetSent);

				
				
                                
				mtop.processNextSentence(sourceSent);

				++sentCount;
				output.write(featureManager.runFeatures(sourceSent, targetSent));
				output.write("\r\n");
				
				 lineSource = brSource.readLine();
				lineTarget = brTarget.readLine();
			
			}
	//		featureManager.printFeatureIndeces();
			if (posSource != null) {
				posSource.close();
			}
			if (posTarget != null) {
				posTarget.close();
			}

			brSource.close();
			brTarget.close();
			output.close();
			
			Logger.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

 public void run() {
        constructFolders();
        preprocessing();
        if (getMod().equals("bb")) {
            runBB();
        } else if (getMod().equals("gb")) {
            runGB();
        } else {
            runAll();
        }
    }

}

	
