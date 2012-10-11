/**
 *
 */
package wlv.mt.test;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.LineNumberReader;
import java.util.Iterator;
import java.util.TreeSet;

/**
 *
 * Builds a set of test data given an input file, a percentage
 *
 * @author Catalina Hallett
 *
 */
public class TestDataBuilder {

    String testName;
    int n;
    TreeSet<Integer> positions;
    String[] files;
    int totalLines;
    int noLines;

    public TestDataBuilder(String testName, String indexName, String[] files) {
        this.testName = testName;
        positions = new TreeSet<Integer>();
        this.files = files;
        loadPositions(indexName);
        run();

    }

    public TestDataBuilder(String testName, int n, String[] files) {
        this.testName = testName;
        this.n = n;
        positions = new TreeSet<Integer>();
        this.files = files;
        generatePositions();
        run();
    }

    public void run() {
        try {

            File folder = new File(testName);
            if (!folder.exists()) {
                folder.mkdir();
            }


            BufferedReader[] brs = new BufferedReader[files.length];
            BufferedWriter[] bws = new BufferedWriter[2 * files.length];

            BufferedWriter index = new BufferedWriter(new FileWriter(folder.getPath() + File.separator + "indeces.txt"));
            BufferedWriter info = new BufferedWriter(new FileWriter(folder.getPath() + File.separator + "testSet.nfo"));
            info.write("Test set: " + testName);
            info.newLine();
            info.newLine();

            info.write("Percentage: " + n);
            info.newLine();
            info.write("Total number of lines: " + totalLines);
            info.newLine();
            info.write("Training lines: " + (totalLines - noLines));
            info.newLine();
            info.write("Test lines: " + noLines);
            info.newLine();
            info.newLine();

            info.write("Indeces file: " + "indeces.txt");
            info.newLine();
            info.newLine();
            info.write("Files:");
            info.newLine();

            //number of input files
            int nFiles = files.length;
            File f;
            //initialise BufferedReaders and writers
            for (int i = 0; i < nFiles; i++) {
                f = new File(files[i]);
                brs[i] = new BufferedReader(new FileReader(files[i]));
                bws[i] = new BufferedWriter(new FileWriter(folder.getPath() + File.separator + f.getName() + ".train"));
                bws[nFiles + i] = new BufferedWriter(new FileWriter(folder.getPath() + File.separator + f.getName() + ".test"));
                info.write("Input: " + f.getPath());
                info.newLine();
                info.write("Training (generated): " + folder.getPath() + File.separator + f.getName() + ".train");
                info.newLine();
                info.write("Testing  (generated):" + folder.getPath() + File.separator + f.getName() + ".test");
                info.newLine();
                info.newLine();
            }

            StringBuffer index1 = new StringBuffer();
            StringBuffer index2 = new StringBuffer();
            //iterate through line indeces
            Iterator<Integer> it = positions.iterator();
            int pos;
            int lineCount = -1;

            //lines will hold the most recently read line from the input files
            String[] lines = new String[nFiles];
            boolean fullLine = true;
            int trainCount = 0;

            while (it.hasNext()) {
                pos = it.next().intValue();
                index1.append(pos + "\t");
                for (int i = 0; i < nFiles; i++) {
                    lines[i] = brs[i].readLine();
                    if (lines[i] == null && lineCount < totalLines - 1) {
                        System.out.println("File " + files[i] + " has fewer lines than expected! " + lineCount + " instead of " + totalLines);
                        return;
                    }
                    fullLine &= (lines[i] != null);
                }
                lineCount++;
                //iterate through the input files until a selected index is found
                while (lineCount < pos && fullLine) {
                    //write to training files
                    trainCount++;
                    for (int i = 0; i < nFiles; i++) {
                        bws[i].write(lines[i]);
                        bws[i].newLine();
                        lines[i] = brs[i].readLine();
                        if (lines[i] == null && lineCount < totalLines - 1) {
                            System.out.println("File " + files[i] + " has fewer lines than expected!" + lineCount + " instead of " + totalLines);
                            return;
                        }

                        fullLine &= (lines[i] != null);

                    }
                    index2.append(lineCount + "\t");
                    lineCount++;
                }
                //reached a selected index - write the lines to the test files
                if (fullLine) {
                    for (int i = 0; i < nFiles; i++) {
                        bws[nFiles + i].write(lines[i]);
                        bws[nFiles + i].newLine();
                    }
                }
            }
            for (int i = 0; i < nFiles; i++) {
                lines[i] = brs[i].readLine();
                if (lines[i] == null && lineCount < totalLines - 1) {
                    System.out.println("File " + files[i] + " has fewer lines than expected!" + lineCount + " instead of " + totalLines);
                    return;
                }

                fullLine &= (lines[i] != null);
            }
            lineCount++;
            //finished writing all selected lines. now write the remainder to the training files
            while (fullLine) {
                trainCount++;
                index2.append(lineCount + "\t");
                for (int i = 0; i < nFiles; i++) {
                    bws[i].write(lines[i]);
                    bws[i].newLine();
                    lines[i] = brs[i].readLine();
                    if (lines[i] == null && lineCount < totalLines - 1) {
                        System.out.println("File " + files[i] + " has fewer lines than expected!" + lineCount + " instead of " + totalLines);
                        return;
                    }
                    fullLine &= (lines[i] != null);
                }
                lineCount++;
            }


            //close the streams
            for (int i = 0; i < nFiles; i++) {
                brs[i].close();
                bws[i].flush();
                bws[i].close();
                bws[nFiles + i].flush();
                bws[nFiles + i].close();
            }
            System.out.println(trainCount);
            index.write(index1.toString());
            index.newLine();
            index.write(index2.toString());
            index.close();
            info.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * Randomly generates approximately n% line indeces and store them in order
     * in a TreeSet
     */
    public void generatePositions() {

        java.util.Random rand = new java.util.Random();
        //get the number of lines in the input file
        try {
            LineNumberReader lnr = new LineNumberReader(new FileReader(new File(files[0])));
            lnr.skip(Integer.MAX_VALUE);

            totalLines = lnr.getLineNumber();
            System.out.println("Total lines:" + totalLines);
            System.out.println("Percentage:" + n);
            //compute the number of lines to be extracted for testing
            noLines = totalLines * n / 100;
            System.out.println("Selected " + noLines + " lines");

            while (positions.size() < noLines) {
                positions.add(rand.nextInt(totalLines));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        if (args.length < 3) {
            System.out.println("Wrong number of arguments. Expected at least 3, received " + args.length);
            System.out.println("USAGE: \r\n"
                    + "wlv.mt.test.TestDataBuilder <test name> <percentage> <list of files>"
                    + "or\r\n"
                    + "wlv.mt.test.TestDataBuilder <test name> <indeces file> <list of files>");
            return;
        }

        String[] files = new String[args.length - 2];
        for (int i = 0; i < args.length - 2; i++) {
            files[i] = args[i + 2];
        }

        String pArg = args[1];
        try {
            Integer i = new Integer(pArg);
            TestDataBuilder tdb = new TestDataBuilder(args[0], i, files);
        } catch (Exception e) {
            TestDataBuilder tdb = new TestDataBuilder(args[0], args[1], files);
        }

    }

    /**
     * Loads line indeces from a file and stores them in a TreeSet
     *
     * @param indexFile
     */
    public void loadPositions(String indexFile) {
        try {
            BufferedReader br = new BufferedReader(new FileReader(indexFile));
            String[] indeces = br.readLine().split("\t");
            for (String index : indeces) {
                positions.add(new Integer(index));
            }
            noLines = positions.size();

            LineNumberReader lnr = new LineNumberReader(new FileReader(new File(files[0])));
            lnr.skip(Integer.MAX_VALUE);

            totalLines = lnr.getLineNumber();
            n = 100 * noLines / totalLines;
            br.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
