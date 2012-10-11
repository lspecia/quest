/**
 *
 */
package wlv.mt.tools;

import java.io.*;
import java.util.*;
import wlv.mt.util.Logger;
import wlv.mt.util.StreamGobbler;

/**
 * A wrapper around the Arabic part-of-speech tagger
 *
 * @author Catalina Hallett
 *
 */
public class ArabicPosTagger extends PosTagger {

    String tempInput;
    private static String TOK_EXT = ".TOK";
    private static String POS_EXT = ".TOK.POS";
    String exeName;

    public ArabicPosTagger() {
        Logger.log("Initiating ArabicPosTagger...");
    }

    public ArabicPosTagger(String lang, String tagName, String tagPath, String exeName, String input) {
        this.name = tagName;
        this.path = tagPath;
        this.lang = lang;
        this.input = input;
        this.tempInput = input + ".temp";
        this.exeName = exeName;
    }

    @Override
    public String run() {
        // TODO Auto-generated method stub
//		preprocess();
        try {
            long start = System.currentTimeMillis();
            System.out.println("Running ArabicPosTagger");
            String output = input + POS_EXT;
            System.out.println(output);
            File outClean = new File(output + ".clean");
            File out = new File(output);
            if (!alwaysRun && out.exists()) {
                Logger.log("POS output " + out.getName() + " already exists. Arabic POS Tagger will not run.");
                System.out.println("POS output " + out.getName() + " already exists. Arabic POS Tagger will not run.");
                System.out.println("Converting POS output...");
                Logger.log("...Converting output");
                if (!alwaysRun && outClean.exists()) {
                    Logger.log("POS output " + outClean.getName() + " already exists. Arabic POS Tagger will not run.");
                    ResourceManager.registerResource(lang + "PosTagger");
                    return output + ".clean";
                } else {
                    convertOutput(output);
                    ResourceManager.registerResource(lang + "PosTagger");
                    return output + ".clean";
                }
            }
            Logger.log("...Running ArabicPosTagger");
            String[] args = new String[]{"perl", exeName, "configs/default.amiraconfig", input};
            ProcessBuilder pb = new ProcessBuilder(args);
            pb.directory(new File(path));
            Map<String, String> env = pb.environment();
            env.put("ASVMT", path);

            Process process = pb.start();
            Logger.log("Executing: " + process.toString());


            StreamGobbler errorGobbler = new StreamGobbler(process.getErrorStream(), "STDERR", true);
            errorGobbler.start();
            StreamGobbler outGobbler = new StreamGobbler(process.getInputStream(), "STDOUT", true);
            outGobbler.start();

            process.waitFor();

            Logger.log("...converting output");
            convertOutput(output);
            out = new File(output);
            if (out.exists()) {
                ResourceManager.registerResource(lang + "PosTagger");
            }
            available = out.exists();
            //	File f = new File(tempInput);
            //	f.delete();
            System.out.println("ArabicPosTagger done!");
            long elapsed = System.currentTimeMillis() - start;
            Logger.log("ArabicPosTagger succesfully completed in " + elapsed / 1000f + " sec");
            return output + ".clean";
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public void preprocess() {
        // TODO Auto-generated method stub
        separateSentences();

    }

    private void separateSentences() {
        try {
            InputStream f = new FileInputStream(input);
            FileWriter bw = new FileWriter(tempInput);
            BufferedReader br =
                    new BufferedReader(new InputStreamReader(f));
            String line;
            while ((line = br.readLine()) != null) {
                bw.write(line + " \r\n----- \r\n");
            }
            br.close();
            bw.close();

        } catch (java.io.IOException e) {
            e.printStackTrace();
        }
    }

    private static void convertOutput(String output) {
        try {
            BufferedWriter posOutput2 = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(output + ".clean"), "utf-8"));
            BufferedWriter posXOutput = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(output + getXPOS()), "utf-8"));
            BufferedReader posOutput = new BufferedReader(new InputStreamReader(new FileInputStream(output), "utf-8"));
            String line = posOutput.readLine();
            String word;
            String tag;
            while (line != null) {
                String[] split = line.split(" ");
                for (int i = 0; i < split.length - 1; i++) {
                    String[] comps = split[i].split("/");
                    word = comps[0];
                    tag = comps[1];
                    posOutput2.write(word + "\t" + tag + "\r\n");
                    posXOutput.write(tag + " ");
                }
                //mark last token as end of sentence
                String[] comps = split[split.length - 1].split("/");
                word = comps[0];
                tag = comps[1];
                posOutput2.write(word + "\t" + tag + " SENT" + "\r\n");
                posXOutput.write(tag + " ");
                posXOutput.newLine();
                line = posOutput.readLine();
            }
            posOutput.close();
            posOutput2.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void main(String[] args) {
        ArabicPosTagger apt = new ArabicPosTagger(args[0], args[1], args[2], args[3], args[4]);
        apt.run();
//		apt.convertOutput(args[0]);
    }

    public String getTok() {
        return input + TOK_EXT;
    }

    public String getPos() {
        return input + POS_EXT + ".clean";
    }
}
