/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package wlv.mt.tools;

import java.io.*;

import wlv.mt.util.*;

/**
 * A wrapper around the Arabic transliterator
 *
 * @author cat
 */
public class ArabicTransliterator {

    private String input;
    private String output;
    private String lowercasePath;
    private String trPath;
    private String trName;
    private boolean forceRun = false;

    public ArabicTransliterator(String input, String output, String trPath, String trName, boolean run) {
        this.input = input;
        this.output = output;
        this.trPath = trPath;
        this.trName = trName;
        this.forceRun = run;
    }

    public void run() {
        File f = new File(output);
        if (f.exists() && !forceRun) {
            Logger.log("Output file " + output + " already exists. Transliterator will not run");
            System.out.println("Output file " + output + " already exists. Ngram will not run");
            return;
        }

        System.out.println("Running the transliterator");
        long start = System.currentTimeMillis();

        try {
            Logger.log("Running the transliterator...");
            String[] args = new String[]{trName, input};
            System.out.println(trPath + " " + trName + " " + input);
            ProcessBuilder pb = new ProcessBuilder(args);
            System.out.println(pb);
            pb.directory(new File(trPath));
            Process process = pb.start();

//                        StreamGobbler out = new StreamGobbler(process.getInputStream(), "STDOUT");
            try {
                BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream()));
                PrintWriter pw = new PrintWriter(output);

                String line = null;

                while ((line = br.readLine()) != null) {
                    if (pw != null) {
//                                	System.out.println(line);
                        line = line.replaceAll("_bw", "");
                        pw.println(line);
//                                    System.out.println(line);
                    }


                }
                if (pw != null) {
                    pw.flush();
                }
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }

            StreamGobbler errorGobbler = new StreamGobbler(process.getErrorStream(), "STDERR");

            // any output?
            errorGobbler.start();
//                    out.start();
            process.waitFor();
        } catch (Exception e) {
            e.printStackTrace();
            Logger.log(e.getStackTrace().toString());
        }

        long end = System.currentTimeMillis() - start;
        Logger.log("Finished running transliterator in " + end / 1000f + " sec");

    }

    public String getOutput() {
        return output;
    }

    public static void main(String[] args) {
        ArabicTransliterator at = new ArabicTransliterator(args[0], args[1], "/home/najeh/Arabe/TRANSLITERATION", "./utf2bwP", true);
        at.run();
    }
}
