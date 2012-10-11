package wlv.mt.tools;

import java.util.*;

/**
 * A LanguageModel stores information about the ngram content of language model
 * file It provides access to information such as the frequency of ngrams, and
 * the cut-off points for various ngram frequencies
 *
 * @author cat
 */
public class LanguageModel {

    private static int sliceNo = 4;
    private static HashMap<String, Integer>[] ngrams;
//	private ArrayList freqs;	//for storing frequencies in a sortable container
    private static int[][] cutOffs;	//for storing cut-off frequencies

    public LanguageModel(int nSize) {
        ngrams = new HashMap[sliceNo];
        for (int i = 0; i < sliceNo; i++) {
            ngrams[i] = new HashMap<String, Integer>();
        }
        //	freqs = new ArrayList();
        cutOffs = new int[nSize][sliceNo];
    }

    public void setCutOffs(int[][] cutoffs) {
        this.cutOffs = cutoffs;
    }

    public void addNGram(String ngram, int freq, int size) {
        ngrams[size - 1].put(ngram, new Integer(freq));
        //freqs.add(freq);
    }

    public void addNGram(String ngram, int freq) {
        String[] ngramSplit = ngram.split(" ");
        int size = ngramSplit.length;
        ngrams[size - 1].put(ngram, new Integer(freq));
        //freqs.add(freq);
    }

    public static int getCutOff(int ngramSize, int pos) {
        return cutOffs[ngramSize - 1][pos - 1];
    }

    public static int getFreq(String word) {
        String[] split = word.split(" ");
        return ngrams[split.length - 1].get(word);
    }

    public static int getFreq(String ngram, int size) {
        if (ngrams[size - 1].get(ngram) == null) {
            return 0;
        }
        return ngrams[size - 1].get(ngram);
    }
}
