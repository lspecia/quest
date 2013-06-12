/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package shef.mt.tools;

/**
 *
 * @author Hala Almaghout
 */
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeSet;
import shef.mt.features.util.Sentence;


/**
 * A class which calculates the CCG features:
 *      number of maximal phrases in the target
 *      number of maximal constituents in the target
 *      percentage of supertag mismatches 
 *      percentage of constituent category mismatches
 *      supertag lm probability
 *      supertag lm perplexity
 * @author Hala Almaghout
 */
public class CCGprocessor {
    private String workdir;
    private final String sep=" \\|\\|\\| ";
    String sourceSentFileName;
    String targetSentFileName;
    Sentence sourceSent;
    Sentence targetSent;
    TreeSet targetwords; // the set of target words fromt the phraes table, used to detect OOV
    HashMap dict; // phrase table dictionary
    String[] words; //  words of the target sentence
    int sentlen; // length of the target sentence in terms of number of words
    TreeSet oovs; // set of oovs
    final String oovTag="NNS|N"; // the supetag assigned to the oov
    String ccgResourceDir;
    String rawSuperFileName;
    String superfileName;
    String superPPLfileName;
    String superLMfile;
    String ngramPath;
    
    public CCGprocessor(String workdir,String CCGresourcedir, Sentence sourceSent,Sentence targetSent, String ngramPath, String superLMfile){
        
        this.workdir=workdir;
        this.sourceSentFileName=sourceSent.getIndex()+".src";
        this.targetSentFileName=targetSent.getIndex()+".tgt";
        this.sourceSent=sourceSent;
        this.targetSent=targetSent;
        this.words=targetSent.getText().split(" ");
        this.sentlen=words.length;
        targetwords=new TreeSet();
        oovs=new TreeSet();
        dict=new HashMap();
        ccgResourceDir=CCGresourcedir;
        rawSuperFileName=workdir+File.separator+targetSentFileName+".rawSuper";
        superfileName=workdir+File.separator+targetSentFileName+".super";
        superPPLfileName = workdir+File.separator+targetSentFileName+ ".superppl";
        this.ngramPath=ngramPath;
        this.superLMfile=superLMfile;

        

       
    }
    
    public void processNextSentence(){
       
    try{
       
        BufferedWriter bwsrc=new BufferedWriter(new FileWriter(workdir+File.separator+sourceSentFileName));
        bwsrc.write(sourceSent.getText());
        bwsrc.close();
        
        BufferedWriter bwtgt=new BufferedWriter(new FileWriter(workdir+File.separator+targetSentFileName));
        bwtgt.write(targetSent.getText());
        bwtgt.close();
        
       extractCCGFeatures();
      
      
        }catch(Exception e){
            
          e.printStackTrace();
        }
    }

/**
 * filters phrase table according to the source AND target sentences. The result is a phrase table which contains only the phrases that contributed in the translation
 */
void filterPhraseTable(){
    
    try{
        
       
      executeCommand("python "+ccgResourceDir+"/CCGtools.py filter "+workdir+" "+sourceSentFileName+" "+targetSentFileName);
     

    
    }catch(Exception e){
            
           e.printStackTrace();
        }
    
}

/**
 * executes the input command and returns the output of  command execution
 * @param command
 * @return 
 */
String executeCommand(String  command){
    Runtime r = Runtime.getRuntime();

    try {
        Process p = r.exec(command);
        

        BufferedReader br = new BufferedReader(new InputStreamReader(p.getErrorStream()));

        String line = br.readLine();
        while (line != null) {
            System.out.println(line);
            line = br.readLine();
        }

        br.close();
        
        BufferedReader instream=new BufferedReader(new InputStreamReader(p.getInputStream()));
        String processOutput=instream.readLine();
        instream.close();
        return processOutput;
    } catch (Exception e) {
        e.printStackTrace();
    }
    return "";
}

/**
 * fills a dictionary with the phrases form the phrase table 
 * @param tableFileName
 * @return 
 */

void getPhraseTableDictionary(String tableFileName){
    
    try{
        BufferedReader tablebr=new BufferedReader(new FileReader(tableFileName));
        
        String line=tablebr.readLine();
        //line=StringEscapeUtils.escapeJava(line);
        while (line!=null){
            String[] tokens=line.split(sep);
            String target=tokens[0].trim();
            String[] targetphraseWords=target.split(" ");
            for (int i=0; i<targetphraseWords.length;i++){
                targetwords.add(targetphraseWords[i]);
                
            }
                String starget=tokens[1].trim(); //supertagged target
                String source=tokens[2].trim();
                String scores=tokens[3].trim();
                float directProb=Float.parseFloat(scores.split(" ")[2]); // direct translation probability the third score
               
                Object t[]={starget,directProb};
                if  (dict.get(target)==null)
                        dict.put(target,new TableDictEntry(starget, directProb));
                else{
                    TableDictEntry current= (TableDictEntry) dict.get(target);
                    if (directProb>= current.directprob)
                         dict.put(target,new TableDictEntry(starget, directProb)); // keep the highest probablity entry
                }
         line=tablebr.readLine();               

        }
        
        tablebr.close();
        
    }catch(Exception e){
         e.printStackTrace();

    }
    
}

/**
 * extracts CCG features:
 * number of maximal phrases in the target
 * number of maximal constituents in the target
 * percentage of supertag mismatches 
 * percentage of constituent category mismatches
 * supertag lm probability
 * supertag lm perplexity

*/
void extractCCGFeatures(){
   filterPhraseTable();
   getPhraseTableDictionary(workdir+"/filteredModel"+targetSentFileName+"/phrase-table.0-0.1.1");
    
    
    Chart chart=new Chart(sentlen,1000);// chart for phrases in the phrase table
    Chart links=new Chart(sentlen,null);// chartlinks for min phrases form the phrase table
    Chart ccgchart=new Chart(sentlen,1000); // chart for min phrases tagged by the parser
    Chart ccglinks= new Chart(sentlen,null); //chart links for min phrases tagged by the paresr 
    Chart ccgconstituentsChart=new Chart(sentlen,"");// chart for CCG constituent labels
    
    
    
    for (int i=0; i<sentlen; i++){
        if ( ! targetwords.contains( words[i])){
            chart.setChartCell(1, i,1);
            oovs.add(i);
        }else if (dict.containsKey(words[i])){
            chart.setChartCell(1, i,1);
        }
        
        
                   
    }
    
    findMinPhrases(chart, links, false);
    
    ArrayList phrases=extractPhrasesFromChart(links, 0, sentlen);
    String supertaggedTarget=supertagTarget(phrases); // supertagged target sentence 
    String rawsupertags=extractSupertags(supertaggedTarget);//supertags without words, for lm feature calculation
    
        try{
            
            //------------------------min number of phrases----------------------//
            BufferedWriter outputFile=new BufferedWriter(new FileWriter(workdir+File.separator+targetSentFileName+".CCGlog"));
            BufferedWriter supertaggedTargetFile=new BufferedWriter(new FileWriter(superfileName));
            BufferedWriter rawsupertagsFile=new BufferedWriter(new FileWriter(rawSuperFileName));

            float totalphrases= ((Integer)chart.getChartCell( 0, sentlen)).intValue();
            outputFile.write("PHRASES TOTAL="+totalphrases+"\n");
            supertaggedTargetFile.write(supertaggedTarget);
            rawsupertagsFile.write(rawsupertags);
            targetSent.setValue("CCG.phraseNum", totalphrases);
            supertaggedTargetFile.close();
            rawsupertagsFile.close();
                 
            //------------------------min number of CCG constituents----------------------//
            executeCommand("python "+ccgResourceDir+"/CCGtools.py parse "+superfileName); 
            BufferedReader xmlchart=new BufferedReader(new FileReader(workdir+File.separator+targetSentFileName+".super.chartlabels.xml"));
            String chartline=xmlchart.readLine();
            fillCCGchart(chartline, ccgchart,ccgconstituentsChart);
            xmlchart.close();
            findMinPhrases(ccgchart, ccglinks, true);
            float totalconstituents= ((Integer)ccgchart.getChartCell( 0, sentlen)).intValue();
            outputFile.write("CONSTITUENTS TOTAL="+totalconstituents+"\n");
            targetSent.setValue("CCG.constituentNum", totalconstituents);
           
            //------------------------percentage of supertag argument mismatches----------------------//

            String numOfSupertagMismatches=executeCommand("python "+ccgResourceDir+"/CCGtools.py args "+rawSuperFileName);
            float supertagPairsNum=rawsupertags.split(" ").length-1;
            float superMismatchPer=0;
            if (supertagPairsNum!=0)
                superMismatchPer=Integer.parseInt(numOfSupertagMismatches)/(supertagPairsNum);
            outputFile.write("MISMATCHES SUPER="+superMismatchPer+"\n");
            targetSent.setValue("CCG.superMismatches", superMismatchPer);
            
            
            //------------------------percentage of constituent argument mismatches----------------------//

            ArrayList constituentList=extractPhrasesFromChart(ccglinks, 0, sentlen);
            String constituents=getConstituents(constituentList, ccgconstituentsChart);
            String constituentFileName=workdir+File.separator+targetSentFileName+".ccgconstituents";
            BufferedWriter constituentsFile=new BufferedWriter(new FileWriter(constituentFileName));
            constituentsFile.write(constituents);
            constituentsFile.close();
            String numOfConstituentMismatches=executeCommand("python "+ccgResourceDir+"/CCGtools.py args "+constituentFileName);
            float constituentPairsNum=constituents.split(" ").length-1;
            float constituentMismatchPer=0;
            if (constituentPairsNum!=0)
                constituentMismatchPer=Integer.parseInt(numOfConstituentMismatches)/(constituentPairsNum);
            outputFile.write("MISMATCHES CONSTITUENTS="+constituentMismatchPer+"\n");
            targetSent.setValue("CCG.constituentMismatches", constituentMismatchPer);
           
             //------------------------supertag lm probability and perplexity----------------------//

            calculateCCGLMfeatures();
              outputFile.write("SuperlmLogProb="+targetSent.getValue("CCG.superlogprob") +"\n");

             outputFile.write("SuperPPL="+targetSent.getValue("CCG.superppl") +"\n");

            
            //-------------------------output phrases and constituents to the log file-----------//
            
            String phraseString=getPhrasesString(phrases,false,null);
            outputFile.write("PHRASES= "+phraseString+"\n");
            String phrasesPlusConstituents=getPhrasesString(constituentList,true,ccgconstituentsChart);
            outputFile.write("CONSTITUENTS="+phrasesPlusConstituents+"\n");

            outputFile.close();


        }catch(Exception e){
            e.printStackTrace();
        
        }
}

String getPhrasesString(ArrayList phrases,boolean constituents,Chart ccgconstituentsChart){
    String s="";
    for (int i=0;i<phrases.size();i++){
            Phrase phrase=(Phrase)phrases.get(i);
            String phraseWords=getPhrase(words, phrase.startIndex, phrase.length);
            if (constituents)
                s+=phraseWords+"|"+ccgconstituentsChart.getChartCell(phrase.startIndex, phrase.length)+" ||| ";
            else
                s+=phraseWords+ " ||| ";
    }
    return s;
}



/**
 * calculates the supertag language model log probability and perplexity and freatures.  
 */
void calculateCCGLMfeatures(){
    
        NGramExec nge = new NGramExec(ngramPath);
        System.out.println("runNgramPPL for CCG");
        nge.runNGramPerplex(rawSuperFileName, superPPLfileName, superLMfile);
        PPLProcessor superPPLProcessor = new PPLProcessor(superPPLfileName, new String[]{"CCG.superlogprob", "CCG.superppl", "CCG.superppl1"});
        superPPLProcessor.processNextSentence(targetSent);
        superPPLProcessor.close();
    }
  
/**
 * returns the phrase which consists of the words starting at the input start index and length 
 * @param words
 * @param start
 * @param end 
 */
String getPhrase(String[] words, int start, int length){
    String phrase="";
    for (int i=0;i<length;i++)
        phrase+=words[start+i]+ " ";
    return phrase.trim();
}



/**
 * finds the minimum number of phrases constituting the target sentence according to the chart 
 * and saves the cells corresponding to these phrases in the links chart
 * @param chart
 * @param links
 * @param isParseChart : if the chart is a CCG parsing chart 
 */    
    
void findMinPhrases(Chart chart, Chart links, boolean isParseChart){
    
    for (int len=2; len<=sentlen;len++){
        for (int i=0;i<=sentlen-len;i++){
            int j=i+len-1;
            String phrase=getPhrase(words, i, len);
            boolean shouldSetChartCell=false;
            if (! isParseChart){//check if the input is a parse chart or a chart for finding min phrases from phrase table
                shouldSetChartCell=dict.containsKey(phrase);
            }else{
                Integer cell=(Integer)chart.getChartCell( i, len);
                shouldSetChartCell=cell.intValue()==1;
            }
                if (shouldSetChartCell){
                    chart.setChartCell( 1, i, len);
                }else{
                    int min=1000;
                    for (int k=i;k<j;k++){
                        int phrasenum= ((Integer)chart.getChartCell( i, k-i+1)).intValue()+ 
                                ((Integer)chart.getChartCell( k+1, j-k)).intValue();
                        if (phrasenum<min){
                            chart.setChartCell( phrasenum, i, len);
                            int cell1[]={i,k-i+1};
                            int cell2[]={k+1,j-k};
                            
                            links.setChartCell(new Link(cell1,cell2), i, len);
                            
                            min=phrasenum;
                        }
                    }
                }
                    
            
            
        }
    }



} 

/**
 * extracts the supertags from the supertagged input sentence (of the form word|supertag) and returns them
 * @param supertaggedSentence
 * @return 
 */

String extractSupertags(String supertaggedSentence){
    String supertags="";
    String[] superwords=supertaggedSentence.split(" ");
            for (int w=0;w<superwords.length;w++){
                String superword=superwords[w];
                int firstBarInd=superword.indexOf("|");
                int secondBarInd=superword.indexOf("|",firstBarInd+1);
                String supertag=superword.substring(secondBarInd+1);
                supertags+=supertag+" ";
            }
    return supertags.trim();
}
/**
 * supertags the input phrase by assigning it the most probable supertag sequence according to the CCG-augmented phrase table
 * @param phrases
 * @return 
 */


String supertagTarget(ArrayList phrases){
    String supertaggedSentence="";
    for (int i=0;i<phrases.size();i++){
        Phrase phrase=(Phrase)phrases.get(i);
        String phraseWords=getPhrase(words, phrase.startIndex, phrase.length);

        if (phrase.length==1 && oovs.contains( phrase.startIndex)){
            supertaggedSentence+=phraseWords+"|"+oovTag+" "; //oov
        }
        else{
            TableDictEntry phraseEntry=(TableDictEntry)dict.get(phraseWords);
            String supertaggedPhrase=phraseEntry.starget;
            supertaggedSentence+=supertaggedPhrase+" ";
            
        }
            
        
    }
    
    return supertaggedSentence.trim();
}

/**
 * returns a list of constituent labels corresponding to the input phrases from the input chart
 * @param phrases
 * @param ccgchart
 * @return 
 */
String getConstituents(ArrayList phrases, Chart ccgchart){
    
    String constituents="";
    for (int i=0;i<phrases.size();i++){
        Phrase phrase=(Phrase) phrases.get(i);
        constituents+=(String)ccgchart.getChartCell(phrase.startIndex, phrase.length)+" ";
    }
    
    return constituents.trim();
    
    
}


/**
 * extract the maximal phrases from the link chart, each phrase  is defined in terms of the start index and length

 * @param links
 * @param i
 * @param l
 * @return 
 */
    
ArrayList extractPhrasesFromChart(Chart linksChart,int i,int len){

    if (linksChart.getChartCell( i, len)==null){
       ArrayList phrases=new ArrayList();
       phrases.add(new Phrase(i,len));
        return phrases;
    }
    else{
        
        Link link=(Link)linksChart.getChartCell( i, len);
        int[] cell1=link.cell1;
        ArrayList phrases1=extractPhrasesFromChart(linksChart, link.cell1[0], cell1[1]);
        int [] cell2=link.cell2;
        ArrayList phrases2=extractPhrasesFromChart(linksChart, link.cell2[0], cell2[1]);
        phrases1.addAll(phrases2);
        return phrases1;

    }
}
 
/**
 * reads the output of a CCG parser in the form of xml file and then 
 * fills accordingly a chart for maximal CCG constituents (ccgchart) and another one 
 * for CCG constituent labels (ccgConstituentChart)
 * @param xmlline
 * @param ccgchart
 * @param ccgConstituentChart 
 */
void fillCCGchart(String xmlline, Chart ccgchart, Chart ccgConstituentChart){
   
    int i=0;
    int eqIndex=xmlline.indexOf("=",i);
   
    while (eqIndex!=-1){
        int dashIndex=xmlline.indexOf("-",i);
        int start=Integer.parseInt(xmlline.substring(eqIndex+2, dashIndex)); 
        int quoteIndex=xmlline.indexOf("\"",dashIndex);
        int end=Integer.parseInt(xmlline.substring(dashIndex+1,quoteIndex));
        int eqIndex2=xmlline.indexOf("=",quoteIndex); 
        int quoteIndex2=xmlline.indexOf("\"",eqIndex2+2);
        String label=xmlline.substring(eqIndex2+2,quoteIndex2);
        
        ccgchart.setChartCell( 1, start, end-start+1);
        ccgConstituentChart.setChartCell( label, start, end-start+1);

        i=quoteIndex2;
        eqIndex=xmlline.indexOf("=",i);

        
    }
    
    
}

}



class Chart{ // a class representing the chart
    
    ArrayList chart;
    
    public Chart(int size, Object initialValue){
         chart=new ArrayList(size);// chart for phrases in the phrase table
    
    
    for (int i=0;i<size;i++){
        
        int listsize=size-i+1;
        ArrayList chartlist=new ArrayList(size);
        

        for (int j=0;j<listsize;j++){
            chartlist.add(initialValue);
            
        }
            
        chart.add(i,chartlist);            
}   }
    
    Object getChartCell(int i, int j){
    
    ArrayList chartlist=(ArrayList) chart.get(i);
    return chartlist.get(j);
}

void setChartCell(Object o, int i, int j){
    
    ArrayList chartlist=(ArrayList) chart.get(i);
     chartlist.set(j,o);
}
    
    
}


class TableDictEntry{// phrase tabel entry in the dictionary
    public String starget; // supertagged target
    public float directprob; //direct probability p(s,e|f)

    public TableDictEntry(String starget,float directprob) {
        this.starget=starget;
        this.directprob=directprob;
    }
   }

class Link{ // link object which specifies the indices of the previous chart cells in the chart
    int[] cell1;
    int[]  cell2;
    
  public  Link(int[] cell1, int[] cell2){
        this.cell1=cell1;
        this.cell2=cell2;
    }
    
}


class Phrase{ // the phrase identified with its start index in a sentence and its length 
    
    int startIndex;
    int length;
    
    public Phrase(int startIndex, int length){
        this.startIndex=startIndex;
        this.length=length;
    }
}
    
