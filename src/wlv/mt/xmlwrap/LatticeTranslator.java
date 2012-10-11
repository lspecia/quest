package wlv.mt.xmlwrap;

import java.io.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

/**
 * LatticeTranslator transforms an ibm word lattice into an fst in a format
 * required by OpenFST.<br> It creates a subfolder with the same name as the
 * input file and within the subfolder produces both a .fst file and a list of
 * input symbols (isysms.txt) and a list of output symbols (osysms.txt) for each
 * word lattice
 */
public class LatticeTranslator {

    private String inputFolder;
    private String outputFolder;
    private String openfst;

    public LatticeTranslator(String inputFolder, String outputFolder, String openfst) {
        this.inputFolder = inputFolder;
        this.outputFolder = outputFolder;
        this.openfst = openfst;
    }

    public void run() {

        File outF = new File(outputFolder);
        if (outF.exists() && !outF.isDirectory()) {
            System.out.println(outputFolder + " is not a directory!");
            return;
        }

        if (!outF.exists()) {
            outF.mkdir();
        }


        File folder = new File(inputFolder);
        java.io.FilenameFilter filter = new java.io.FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return name.endsWith(".nbest");
            }
        };

        File[] listOfFiles = folder.listFiles(filter);
//        System.out.println(listOfFiles.length);
        for (File in : listOfFiles) {
            System.out.println("Processing..." + in.getAbsolutePath());
            String fileName = in.getName();
            String subfolder = fileName.substring(0, fileName.lastIndexOf('.'));
            File out = new File(outF + File.separator + subfolder);
            if (!out.exists()) {
                out.mkdir();
            }
            System.out.println("out=" + out.getAbsolutePath());
            String fstPath = out.getAbsolutePath() + File.separator + in.getName();
            String isymsPath = out.getAbsolutePath() + File.separator + "isyms.txt";
            String osymsPath = out.getAbsolutePath() + File.separator + "osyms.txt";
            System.out.println("converted to " + fstPath);
            try {

                BufferedWriter fw = new BufferedWriter(new FileWriter(fstPath));

                BufferedWriter fwISyms = new BufferedWriter(new FileWriter(isymsPath));
                BufferedWriter fwOSyms = new BufferedWriter(new FileWriter(osymsPath));
                BufferedReader br = new BufferedReader(
                        new InputStreamReader(
                        new FileInputStream(in)));

                String line = br.readLine();
                String first;
                String second;
                int count = 1;
                fwISyms.write("<eps>" + " 0");
                fwISyms.newLine();
                fwOSyms.write("<eps>" + " 0");
                fwOSyms.newLine();
                while (line != null) {

                    if (!line.startsWith("#")) {
                        StringTokenizer st = new StringTokenizer(line);
                        if (st.countTokens() == 1) {
                            fw.write(st.nextToken() + " 0");
                            fw.newLine();
                        } else {
                            first = st.nextToken();
                            second = st.nextToken();
                            if (line.contains("BOUNDARY_WORD")) {
//    						fw.write(first+"\t"+second+"\t"+"\t #"+"\t #\t0\r\n");
//    						fwISyms.write("#"+" "+count+"\r\n");
//    						fwOSyms.write("#"+" "+count+"\r\n");
                                //fw.write("(INITIAL ("+first+" \"\" \"\" 0))\r\n");
                                //fw.write("("+first+ " ("+second+ " \"\" \"\" 0))\r\n");
                                //	br.readLine();
                            } else {
                                String inputString = "\"" + st.nextToken() + "\"";
                                fw.write(first + "\t" + second + "\t" + inputString);

                                fwISyms.write(inputString + " " + count);
                                fwISyms.newLine();
                                String text = st.nextToken();
                                text = text.replaceAll("\"", "\\\\\"");
                                double prob = Double.parseDouble(st.nextToken());
                                fw.write("\t" + text + "\t" + prob);
                                fw.newLine();
                                fwOSyms.write(text + " " + count);
                                fwOSyms.newLine();
//    						fw.write(text+" "+prob+"\r\n");
                            }
                        }
                        count++;
                    }
                    line = br.readLine();
                }
                fw.close();
                fwISyms.close();
                fwOSyms.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            String nbest = out + File.separator + in.getName();
            wlv.mt.tools.OpenFstWrapper.run(openfst, fstPath, isymsPath, osymsPath, fstPath + ".print", 100, false);
            wlv.mt.xmlwrap.fst.FST fst = new wlv.mt.xmlwrap.fst.FST(fstPath + ".print", outputFolder + File.separator + new File(fstPath).getName() + ".lst");
            fst.dfs();
        }
    }

    /*
     private void toOpenFSTFormat(){
     try {
     String path = new File(outFile).getPath();

     FileWriter fw = new FileWriter(fstFile);
     FileWriter fwISyms = new FileWriter("isysms.txt");
     FileWriter fwOSyms = new FileWriter("osysms.txt");
     BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(inFile), "utf-8"));
     String line=br.readLine();
     String first;
     String second;
     int count = 1;
     fwISyms.write("<eps>"+"\t0\r\n");
     fwOSyms.write("<eps>"+"\t0\r\n");

     while (line!=null){

     if (!line.startsWith("#")){
     StringTokenizer st = new StringTokenizer(line);
     if (st.countTokens()==1)
     fw.write(st.nextToken()+"\t0\r\n");
     else {
     first = st.nextToken();
     second = st.nextToken();
     if (line.contains("BOUNDARY_WORD")){
     //						fw.write(first+"\t"+second+"\t"+"\t #"+"\t #\t0\r\n");
     //						fwISyms.write("#"+" "+count+"\r\n");
     //						fwOSyms.write("#"+" "+count+"\r\n");
     //fw.write("(INITIAL ("+first+" \"\" \"\" 0))\r\n");
     //fw.write("("+first+ " ("+second+ " \"\" \"\" 0))\r\n");
     //	br.readLine();
     }
     else{
     String inputString = "\""+st.nextToken()+"\"";
     fw.write(first+ "\t"+second+ "\t"+inputString);
     fwISyms.write(inputString+" "+count+"\r\n");
     String text = st.nextToken();
     text = text.replaceAll("\"", "\\\\\"");
     double prob =  Double.parseDouble(st.nextToken());
     fw.write("\t"+text+"\t"+prob+"\r\n");
     fwOSyms.write(text+" "+count+"\r\n");
     //						fw.write(text+" "+prob+"\r\n");
     }
     }
     count++;
     }
     line = br.readLine();
     }
     fw.close();
     fwISyms.close();
     fwOSyms.close();
     }catch (Exception e){e.printStackTrace();}
     }
     */
    public static void main(String[] args) {
        if (args.length < 2) {
            System.out.println("Wrong number of arguments. Expected: 2. Usage: LatticeTranslator [input_folder] [output_folder]");
        } else {
            LatticeTranslator lt = new LatticeTranslator(args[0], args[1], args[2]);
            lt.run();
        }

    }
}
