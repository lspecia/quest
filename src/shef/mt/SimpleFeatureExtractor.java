/**
 * @author Catalina Hallett, Mariano Felice, Eleftherios Avramidis
 */

package shef.mt;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import shef.mt.features.impl.Feature;
import shef.mt.features.util.FeatureConfigReader;
import shef.mt.features.util.FeatureManager;
import shef.mt.features.util.ParallelSentence;
import shef.mt.features.util.Sentence;
import shef.mt.features.util.io.TabWriter;
import shef.mt.features.util.io.FeatureWriter;
import shef.mt.pipelines.ProcessorPipeline;
import shef.mt.tools.BasicPipeline;
import shef.mt.tools.Resource;
import shef.mt.util.Logger;
import shef.mt.util.PropertiesManager;

public class SimpleFeatureExtractor {

	private static PropertiesManager resourceManager;
    private static String workDir;
    private static String inputFileName;
    private static String outputFileName;
    private static String sourceFileName;
    private static ArrayList<String> targetFileNames;
    private static String sourceLang;
    private static String targetLang;
    private static String configPath;
    private static boolean forceRun;

    	
	
    
    
	public SimpleFeatureExtractor() {
	}
	
	public static void run() throws IOException{
		
		//initialize feature reader file and read the feature names
		FeatureConfigReader f = new FeatureConfigReader(configPath);
		Set<String> requestedFeatures = f.getFeatures();		
		
		//initialize a pipeline and restrict it to run only the requested features
		ProcessorPipeline pipeline = new BasicPipeline();
		//pass language and configuration
		pipeline.prepare(resourceManager, sourceLang, targetLang);
		pipeline.restrictToFeatures(requestedFeatures);
		
		
		//do not initialize any shared resources for the moment
		HashMap<String, Resource> initializedResources = new HashMap<String, Resource>();
		pipeline.initialize(initializedResources);
			
		Scanner sourceFile;
		sourceFile = new Scanner(Paths.get(sourceFileName));
		// TODO Auto-generated catch block
		
		ArrayList<Scanner> targetFiles = new ArrayList<Scanner>();
		
		//open multiple target files (e.g. one for each system), sentence-aligned with the source
		for (String targetFilename:targetFileNames){
			targetFiles.add(new Scanner(Paths.get(targetFilename)));
			// TODO Auto-generated catch block
		}
		
		FeatureWriter writer = new TabWriter(outputFileName); 
		
		
		//get one sentence from the file and process it with the annotation processors
		while (sourceFile.hasNextLine()){
			//process each line in some way
			String sourceText = sourceFile.nextLine();
			Sentence sourceSentence = new Sentence(sourceText);
						
			//create as many target sentences as the target files
			ArrayList<Sentence> targetSentences = new ArrayList<Sentence>();
			for (Scanner targetFile:targetFiles){
				String targetText = targetFile.nextLine();
				targetSentences.add(new Sentence(targetText));
			}
			
			ParallelSentence parallelsentence = new ParallelSentence(sourceSentence, targetSentences);
			parallelsentence = pipeline.processSentence(parallelsentence, sourceLang, targetLang);
			
			writer.write(parallelsentence, requestedFeatures);
			
			
		}      

		sourceFile.close();
		for (Scanner targetFile:targetFiles){
			targetFile.close();
		}

		writer.close();
		
		
		
	}
	
	
	public SimpleFeatureExtractor(String[] args) {
        workDir = System.getProperty("user.dir");
        new Logger("log.txt");
        parseArguments(args);

        inputFileName = workDir + File.separator + resourceManager.getString("input");
        outputFileName = workDir + File.separator + resourceManager.getString("output");
        System.out.println("input=" + inputFileName + "  output=" + outputFileName);

    }

    
    public static void main(String[] args) {
        long start = System.currentTimeMillis();
//        FeatureExtractor fe = new FeatureExtractor(args);

//        fe.run();
        parseArguments(args);
        try {
			run();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
    public static void parseArguments(String[] args) {

        Option help = OptionBuilder.withArgName("help").hasArg()
                .withDescription("print project help information")
                .isRequired(false).create("help");

        Option input = OptionBuilder.withArgName("input").hasArgs(3)
                .isRequired(true).create("input");
        
        Option output = OptionBuilder.withArgName("output").hasArgs(1)
                .isRequired(true).create("output");

        Option lang = OptionBuilder.withArgName("lang").hasArgs(2)
                .isRequired(false).create("lang");

        Option feat = OptionBuilder.withArgName("feat").hasArgs(1)
                .isRequired(false).create("feat");

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
        options.addOption(output);
        options.addOption(mode);
        options.addOption(lang);
        options.addOption(feat);
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
                sourceFileName = files[0];
                int i = 1;
                targetFileNames = new ArrayList<String>();
                while (i < files.length){
	                targetFileNames.add(files[i]);
	                resourceManager.setProperty("sourceFile", sourceFileName);
//	                resourceManager.setProperty("targetFiles", targetFileNames);
	                i++;
                }
            }
            
            if (line.hasOption("output")) {
                // print the value of block-size
                String[] files = line.getOptionValues("output");
                outputFileName = files[0];
               
            }

            if (line.hasOption("lang")) {
                String[] langs = line.getOptionValues("lang");
                sourceLang = langs[0];
                targetLang = langs[1];
                resourceManager.setProperty("sourceLang", sourceLang);
                resourceManager.setProperty("targetLang", targetLang);
            } else {
                sourceLang = resourceManager.getString("sourceLang.default");
                targetLang = resourceManager.getString("targetLang.default");
                resourceManager.setProperty("sourceLang", sourceLang);
                resourceManager.setProperty("targetLang", targetLang);
            }

            if (line.hasOption("mode")) {
                String[] modeOpt = line.getOptionValues("mode");
//                setMod(modeOpt[0].trim());
                
//                featureManager = new FeatureManager(configPath);
            }

            if (line.hasOption("feat")) {
                // print the value of block-size
//                features = line.getOptionValue("feat");
//                featureManager.setFeatureList(features);
            } else {
//                featureManager.setFeatureList("all");
            }

            if (line.hasOption("rebuild")) {
            	resourceManager.setProperty("forceRun", "true");
                forceRun = true;
            } else {
            	resourceManager.setProperty("forceRun", "false");
            }
            configPath = resourceManager.getString("featureConfig");

        } catch (ParseException exp) {
            System.out.println("Unexpected exception:" + exp.getMessage());
        }
    }

}
