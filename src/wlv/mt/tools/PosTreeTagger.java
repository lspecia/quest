package wlv.mt.tools;

import java.io.*;
import java.util.Scanner;

import wlv.mt.util.Logger;

/**
 * PosTreeTagger is a wrapper around the TreeTagger
 *
 * @author Catalina Hallett & Mariano Felice
 *
 */
public class PosTreeTagger extends PosTagger {

    private static String tempInput = "tempPOSInput";

    public PosTreeTagger() {
    }

    public PosTreeTagger(String lang, String tagName, String tagPath, String input, String output, ResourceProcessor resProc) {
        super(lang, tagName, tagPath, input, output, resProc);
        tempInput = input + ".temp";
    }

    @Override
    public String run() {
        try {
            long start = System.currentTimeMillis();
            System.out.println("Running TreeTagger on " + input + " into " + output);
            File out = new File(output);
            if (!alwaysRun && out.exists()) {
                System.out.println("pos output already exists. POS Tagger will not run.");
                Logger.log("POS output already exists. TreeTagger will not run.");
                ResourceManager.registerResource(lang + "PosTagger");
                return output;
            }
            String[] args = {path, input};
            ProcessBuilder pb = new ProcessBuilder(args);

            Logger.log("Running TreeTagger...");
            System.out.println("Running treetagger...");

            Process p = pb.start();
            InputStream stderr = p.getErrorStream();
            InputStream stdout = p.getInputStream();
            BufferedReader brIn = new BufferedReader(new FileReader(input));
            //BufferedReader brIn = new BufferedReader(new InputStreamReader(new FileInputStream(input), "UTF8"));
            BufferedWriter bw = new BufferedWriter(new FileWriter(output));
            BufferedWriter bwXPos = new BufferedWriter(new FileWriter(output + getXPOS()));
            BufferedReader brOut = new BufferedReader(new InputStreamReader(stdout));
            String[] split;
            String inputLine;
            String line = "";
            int tokCount = 0;
            //int lineCount = 0;

            String completeLine;
            while ((inputLine = brIn.readLine()) != null) {
                //lineCount++;
                tokCount = inputLine.split("\\s+").length;
                //System.out.println("LINE Nº: " + lineCount + " TOKENS: "+tokCount+" \nLINE: "+inputLine);
                inputLine = inputLine.replaceAll("\\s+", "");
                completeLine = "";
                while (completeLine.length() != inputLine.length() && (line = brOut.readLine()) != null) {
                    //System.out.println("PROCESSED LINE: " + line);
                    split = line.split("\t");
                    completeLine = completeLine + split[0].replaceAll(" ", "");
                    //System.out.println("COMPLETE LINE: *" + completeLine + "*" + completeLine.length());
                    //System.out.println("FULL LINE    : *" + inputLine + "*" + inputLine.length());
                    //System.out.println("Press <Enter> to continue =)))");
                    //System.in.read();

                    bwXPos.write(split[1] + " ");
                    bw.write(line);
                    bw.newLine();
                }

                bwXPos.newLine();
                bw.write(line);
                bw.newLine();
            }

            brOut.close();
            bw.close();
            bwXPos.close();

            BufferedReader brCleanUp = new BufferedReader(new InputStreamReader(stderr));
            while ((line = brCleanUp.readLine()) != null) {
                System.out.println("[Stderr] " + line);
            }
            brCleanUp.close();

            p.waitFor();
            Logger.log("TreeTagger exited with exit value " + p.exitValue());
            out = new File(output);
            if (out.exists()) {
                ResourceManager.registerResource(lang + "PosTagger");
            }
            available = out.exists();
            //	File f = new File(tempInput);
            //	f.delete();
            long elapsed = System.currentTimeMillis() - start;
            Logger.log("TreeTagger completed in " + elapsed / 1000f + " sec");
            System.out.println("TreeTagger done!");
            return output;

        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }
}
