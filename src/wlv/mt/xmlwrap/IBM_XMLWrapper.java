package wlv.mt.xmlwrap;

import java.io.*;

import org.w3c.dom.*;

import java.util.*;
import java.util.regex.Pattern;

import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.stream.*;
import javax.xml.transform.dom.*;

import java.io.*;
import wlv.mt.features.util.*;

public class IBM_XMLWrapper implements XMLWrapper {

    final static String featName[] = {
        "text",
        "prob",
        "rank",
        "me",
        "phrasePrior",
        "dist_sc",
        "m1s2t",
        "m1t2s",
        "tsize",
        "srcSize",
        "bcost",
        "deletion_score",
        "hole_wt",
        "afc",
        "target_len",
        "numcovered",
        "reordering",
        "constraint_block_wt",
        "lm1",
        "lm2",
        "lm3"
    };
    private int nrFeatures = 12;
    private String inFile;
    private String outFile;
    private Element root;
    private String sentIndex;
    private Element crtSent;
    private Document doc;
    private String openfstPath;
    private String latticePath;
    private String nbestPath;
    private TreeSet<String> nbestFileList;
    private Iterator<String> nbestFileIt;

    public IBM_XMLWrapper(String outputFile, String latticePath, String nbestPath, String openfstPath) {
        //	inFile = inputFile;
        outFile = outputFile;
        System.out.println("creating xml " + outFile);
        nrFeatures = featName.length;
        sentIndex = "";
        this.nbestPath = nbestPath;
        this.latticePath = latticePath;
        this.openfstPath = openfstPath;
        createXMLDoc();
    }

    private TreeSet<Translation> getTranslations(String nbestFile) {
        System.out.println("getting translations from " + nbestFile);
        TreeSet<Translation> translations = new TreeSet<Translation>(new TranslationComparatorProb());
        try {
            BufferedReader transReader = new BufferedReader(new FileReader(nbestFile));
            Translation trans = getNextTranslation(transReader);
            while (trans != null) {
                translations.add(trans);
                //     			System.out.println(trans);
                trans = getNextTranslation(transReader);
            }
            transReader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return translations;
    }

    private Translation getNextTranslation(BufferedReader transReader) {
        Translation trans = new Translation();
        HashMap<String, String> values = new HashMap<String, String>();
        float totalProb = 0;
        StringBuffer totalText = new StringBuffer();
        String line = "";
        try {
            line = transReader.readLine();

            if (line == null || line.isEmpty()) {
                return null;
            }
            String[] nodes = line.trim().split("\\|");
            //    		if (nodes==null || nodes.length==0)
            //      			System.out.println(nodes.length);
            float[] features = new float[featName.length - 3];
            for (int i = 0; i < featName.length - 3; i++) {
                features[i] = 0;
            }
            for (String node : nodes) {
//        			System.out.println("node="+node);
                String[] comps = node.trim().split("\t");
                String feats = comps[0].trim();
                //     			if (comps.length<3)
                //      				System.out.println("line="+node);
                //	System.out.println(feats);
                String[] splitFeatures = feats.split(",");
                int count = 0;
                //	System.out.println(feats);
                for (String feature : splitFeatures) {

                    if (!feature.contains("=") && !feature.isEmpty()) {
//        					System.out.println("feature:"+feature);
                        features[count] += Float.parseFloat(feature);
                        count++;
                    }
                }
                totalText.append(comps[1] + " ");
                totalProb += Float.parseFloat(comps[2]);
            }

            trans.setText(totalText.toString());
            trans.setProb(totalProb);
            for (int i = 3; i < featName.length; i++) {
                trans.setAttribute(featName[i], String.valueOf(features[i - 3]));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return trans;
    }

    private void createXMLDoc() {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            DOMImplementation impl = builder.getDOMImplementation();

            doc = impl.createDocument(null, null, null);
            root = doc.createElement("text");
            doc.appendChild(root);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void run() {
        // TODO Auto-generated method stub
        LatticeTranslator lt = new LatticeTranslator(latticePath, nbestPath, openfstPath);
        lt.run();
        java.io.FilenameFilter filter = new java.io.FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return name.endsWith(".lst");
            }
        };
        System.out.println(nbestPath);
        File[] nbestFiles = (new File(nbestPath)).listFiles(filter);
        System.out.println(nbestFiles.length);
        nbestFileList = new TreeSet<String>();

        //order the files by name
        for (File f : nbestFiles) {
            nbestFileList.add(f.getPath());
        }

        nbestFileIt = nbestFileList.iterator();
        int sentCount = 0;
        while (nbestFileIt.hasNext()) {
            String file = nbestFileIt.next();
            TreeSet<Translation> translations = getTranslations(file);
            Element sent = writeSentenceToXML(sentCount);
            writeTranslationsToXML(sent, translations);
            sentCount++;
        }


        writeXML();
        System.out.println(sentCount);
    }

    private Element writeSentenceToXML(int sentIndex) {
        Element sent = doc.createElement("Sentence");
        sent.setAttribute("index", sentIndex + "");
        root.appendChild(sent);
        return sent;
    }

    private void writeTranslationsToXML(Element sent, TreeSet<Translation> translations) {
        try {
            int rank = 0;

            Iterator<Translation> it = translations.iterator();
            while (it.hasNext()) {
                Translation translation = it.next();
                translation.setRank(rank);
                Element trans = doc.createElement("translation");
                trans.setAttribute("rank", String.valueOf(rank));
                trans.setAttribute("text", translation.getText());
                trans.setAttribute("prob", String.valueOf(translation.getProb()));
                for (int i = 3; i < nrFeatures; i++) {
                    trans.setAttribute(featName[i], translation.getAttribute(featName[i]));

                }

                rank++;
                sent.setAttribute("text", translations.first().getText());
                sent.appendChild(trans);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void writeXML() {
        try {
            DOMSource domSource = new DOMSource(doc);
            TransformerFactory tf = TransformerFactory.newInstance();
            Transformer transformer = tf.newTransformer();
            transformer.setOutputProperty(OutputKeys.METHOD, "xml");
            transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            File f = new File(outFile);
            System.out.println("creating xml " + outFile);
            StreamResult sr = new StreamResult(f);
            transformer.transform(domSource, sr);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void main(String[] args) {
        //	IBM_XMLWrapper ibm = new IBM_XMLWrapper(args[0], args[1]);
//		ibm.toOpenFSTFormat();

        IBM_XMLWrapper ibm = new IBM_XMLWrapper(args[2], args[0], args[1], "/home/catalina/tools/open-fst/bin");
        ibm.run();
    }
}
