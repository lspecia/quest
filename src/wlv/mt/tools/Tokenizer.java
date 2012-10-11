/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package wlv.mt.tools;

import java.io.*;

import wlv.mt.util.*;

/**
 * A wrapper around the moses tokenizer
 *
 * @author Catalina Hallett & Mariano Felice
 */
public class Tokenizer extends Resource {

    private String input;
    private String output;
    private String lowercasePath;
    private String tokPath;
    private String lang;
    private boolean forceRun = false;
    private static PropertiesManager resourceManager;

    public Tokenizer(String input, String output, String lowercasePath, String tokPath, String lang, boolean run) {
        super(null);
        this.input = input;
        this.output = output;
        this.lowercasePath = lowercasePath;
        this.tokPath = tokPath;
        this.forceRun = run;
        this.lang = lang;
    }

    public String getTok() {
        return output;
    }

    public void run() {
        System.out.println("running tokenizer on " + input);
        File f = new File(output);
        if (f.exists() && !forceRun) {
            Logger.log("Output file " + output + " already exists. Tokenizer will not run");
            System.out.println("Output file " + output + " already exists. Tokenizer will not run");
            return;
        }


        long start = System.currentTimeMillis();

        try {
            System.out.println(lowercasePath);
            System.out.println(tokPath);
            Logger.log("Transforming the input to lower case...");
            //Logger.log("Transforming the input to true case...");

            //run lowercase first into a temporary file
            String tempOut = output + ".temp";
            System.out.println("running lowercase");
            //System.out.println("running truecase");
            String[] args = new String[]{"perl", lowercasePath, "-l", lang};
            //String[] truecaseOptions = lowercasePath.split("\\|");
            //String[] args = new String[]{"perl",truecaseOptions[0], "--model", truecaseOptions[1]};
            ProcessBuilder pb = new ProcessBuilder(args);
            Process process = pb.start();
            Logger.log("Executing: " + process.toString());

            //pipe the standard input and output to the lowercase input and output streams so it accepts input from the file
            FileOutputStream fos = new FileOutputStream(tempOut);

            // any error message form the process?
            StreamGobbler errorGobbler = new StreamGobbler(process.getErrorStream(), "STDERR");
            // any output from the process?
            StreamGobbler outputGobbler = new StreamGobbler(process.getInputStream(), "STDOUT", fos);

            // Start listeners for the process's errors and output
            errorGobbler.start();
            outputGobbler.start();

            // Process any input to the process
            if (input != null) {
                BufferedReader br = new BufferedReader(new FileReader(input));
                PrintWriter writer = new PrintWriter(new OutputStreamWriter(new BufferedOutputStream(process.getOutputStream())), true);

                // Send input to the process
                int ch;
                while ((ch = br.read()) != -1) {
                    writer.print((char) ch);
                    //System.out.print((char)ch);
                }

                writer.flush();
                writer.close();
                br.close();
            }

            process.getOutputStream().close();

            // Let the process finish
            process.waitFor();

            // Wait until we're done with remaining error and output
            errorGobbler.join();
            outputGobbler.join();

            fos.close();

            System.out.println("done");

            //finished running lowercase
            //now run the tokenizer
            System.out.println("tokenizing...");
            System.out.println("perl " + tokPath + " -a -l " + lang + " " + tempOut);
            Logger.log("Tokenizing the input...");
            args = new String[]{"perl", tokPath, "-a -l", lang};
            pb = new ProcessBuilder(args);
            process = pb.start();
            Logger.log("Executing: " + process.toString());

            // Create the final output file
            fos = new FileOutputStream(output);

            // any error message from the process?
            errorGobbler = new StreamGobbler(process.getErrorStream(), "STDERR");
            // any output from the process?
            outputGobbler = new StreamGobbler(process.getInputStream(), "STDOUT", fos);

            // Start listeners for the process's errors and output
            errorGobbler.start();
            outputGobbler.start();

            // Process any input to the process
            if (input != null) {
                BufferedReader br = new BufferedReader(new FileReader(tempOut));
                PrintWriter writer = new PrintWriter(new OutputStreamWriter(new BufferedOutputStream(process.getOutputStream())), true);

                // Send input to the process
                int ch;
                while ((ch = br.read()) != -1) {
                    writer.print((char) ch);
                    //System.out.print((char)ch);
                }
                writer.flush();
                writer.close();
                br.close();
            }

            process.getOutputStream().close();

            // Let the process finish
            process.waitFor();

            // Wait until we're done with remaining error and output
            errorGobbler.join();
            outputGobbler.join();

            fos.close();

            System.out.println("done");

            //we don't need the lowercase temporary output, we can delete it
            f = new File(tempOut);
            f.delete();

        } catch (Exception e) {
            e.printStackTrace();
            Logger.log(e.getStackTrace().toString());
        }

        long end = System.currentTimeMillis() - start;
        Logger.log("Finished tokenising in " + end / 1000f + " sec");

    }

    public static void main(String[] args) {
        Tokenizer et = new Tokenizer(args[0], args[1], args[2], args[3], args[4], true);
        et.run();
    }
}
