This implementation uses the CCG parser and supertagger from the C&C tools http://svn.ask.it.usyd.edu.au/trac/candc which supports only English. That’s why English should be always the target language for this implementation. 

Features in this implementation: 
Minimum number of phrases which span the output
Minimum number of CCG constituents which span the output
Percentage of argument mismatches in subsequent CCG supertags 
Percentage of argument mismatches in subsequent CCG categories.
5-gram supertag LM probability 
5-gram supertag LM perplexity.

Code added to the QuEst tool:
Classes which correspond to the previous features:
src/shef/mt/features/impl/bb/FeatureCCG_ConstituentMismatches.java
src/shef/mt/features/impl/bb/FeatureCCG_ConstituentNum.java
src/shef/mt/features/impl/bb/FeatureCCG_PhraseNum.java
src/shef/mt/features/impl/bb/FeatureCCG_SuperLMPerplexity.java
src/shef/mt/features/impl/bb/FeatureCCG_SuperLMProb.java
src/shef/mt/features/impl/bb/FeatureCCG_SupertagMismatches.java

In addition to the CCG processor class: 

src/shef/mt/tools/CCGprocessor.java

runCCG method in the FeatureExtractorSimple class, called in the run method. 

The following should be added to the configuration file:
mode=CCG
CCGworkdir (this directory should be created before running the tool, it will contain the files needed for calculating the CCG features)
feature_CCG.xml : CCG features file
english.superlm: path to the supertag language model
CCGresource directory which should contain the following:
CCGtools.py:  a script which performs calculations on CCG suerptags. It uses the CCGoperators.py and myCCGparser.py scripts which should be in the same directory. 
filter-model-given-input.pl script provided by Moses for filtering phrase table 
models directory which contains the CCG parser and supertagger models
moses.ini and moses_reverse.ini: ini files used by the filter-model-given-input.pl script. 
phrase-table.0-0.1.1: the CCG-augmented phrase table which takes the following form: 
source phrase |||  target phrase ||| CCG-augmented  target phrase |||  scores
Each word in the CCG-augmented target phrase takes the following form:
 word|POStag|CCGsupertag
The word alignment is performed on a raw text training data. 

 parser: the binary of of the CCG parser, modified to output the whole chart as an xml file when parsing the sentence. 





