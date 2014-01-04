#!/bin/bash
#java -Xmx1g -XX:+UseConcMarkSweepGC -classpath build/classes:lib/commons-cli-1.2.jar:lib/stanford-postagger.jar:lib/BerkeleyParser-1.7.jar:lib/lucene-core-3.6.2.jar shef.mt.enes.FeatureExtractorSimple -lang english spanish -input input/source.en input/target.es -mode bb -config config/config_en-es.WMT13.properties
# Train set
#java -Xmx1g -XX:+UseConcMarkSweepGC -classpath build/classes:lib/commons-cli-1.2.jar:lib/stanford-postagger.jar:lib/BerkeleyParser-1.7.jar:lib/lucene-core-3.6.2.jar shef.mt.enes.FeatureExtractorSimple -lang english spanish -input /home/ebicici/SMT/Data/QET2013/train/Task1.1/source.eng /home/ebicici/SMT/Data/QET2013/train/Task1.1/target_system.spa -mode bb -config config/config_en-es.WMT13.properties
# Test set
#java -Xmx1g -XX:+UseConcMarkSweepGC -classpath build/classes:lib/commons-cli-1.2.jar:lib/stanford-postagger.jar:lib/BerkeleyParser-1.7.jar:lib/lucene-core-3.6.2.jar shef.mt.enes.FeatureExtractorSimple -lang english spanish -input /home/ebicici/SMT/Data/QET2013/test/Task1.1/source.eng /home/ebicici/SMT/Data/QET2013/test/Task1.1/target_system.spa -mode bb -config config/config_en-es.WMT13.test.properties

### de-en
# Train set
#java -Xmx1g -XX:+UseConcMarkSweepGC -classpath build/classes:lib/commons-cli-1.2.jar:lib/stanford-postagger.jar:lib/BerkeleyParser-1.7.jar:lib/lucene-core-3.6.2.jar shef.mt.enes.FeatureExtractorSimple -lang german english -input ~/SMT/Systems/de-en_QET2013_Task1.2_selection_75/training/test.de ~/SMT/Systems/de-en_QET2013_Task1.2_selection_75/training/test.en -mode bb -config config/config_de-en.WMT13.Task1.2.properties
# Test set
#java -Xmx1g -XX:+UseConcMarkSweepGC -classpath build/classes:lib/commons-cli-1.2.jar:lib/stanford-postagger.jar:lib/BerkeleyParser-1.7.jar:lib/lucene-core-3.6.2.jar shef.mt.enes.FeatureExtractorSimple -lang german english -input ~/SMT/Systems/de-en_QET2013_Task1.2_test_selection_75/training/test.de ~/SMT/Systems/de-en_QET2013_Task1.2_test_selection_75/training/test.en -mode bb -config config/config_de-en.WMT13.test.Task1.2.properties

### en-es
# Train set
#java -Xmx1g -XX:+UseConcMarkSweepGC -classpath build/classes:lib/commons-cli-1.2.jar:lib/stanford-postagger.jar:lib/BerkeleyParser-1.7.jar:lib/lucene-core-3.6.2.jar shef.mt.enes.FeatureExtractorSimple -lang english spanish -input ~/SMT/Systems/en-es_QET2013_Task1.2_selection_85/training/test.en ~/SMT/Systems/en-es_QET2013_Task1.2_selection_85/training/test.es -mode bb -config config/config_en-es.WMT13.Task1.2.properties
# Test set
java -Xmx1g -XX:+UseConcMarkSweepGC -classpath build/classes:lib/commons-cli-1.2.jar:lib/stanford-postagger.jar:lib/BerkeleyParser-1.7.jar:lib/lucene-core-3.6.2.jar shef.mt.enes.FeatureExtractorSimple -lang english spanish -input ~/SMT/Systems/en-es_QET2013_Task1.2_test_selection_85/training/test.en ~/SMT/Systems/en-es_QET2013_Task1.2_test_selection_85/training/test.es -mode bb -config config/config_en-es.WMT13.test.Task1.2.properties
#java -Xmx1g -XX:+UseConcMarkSweepGC -classpath build/classes:lib/commons-cli-1.2.jar:lib/stanford-postagger.jar:lib/BerkeleyParser-1.7.jar:lib/lucene-core-3.6.2.jar shef.mt.enes.FeatureExtractorSimple -lang german english -input ~/SMT/Systems/de-en_QET2013_Task1.2_test_selection_75/training/test.de ~/SMT/Systems/de-en_QET2013_Task1.2_test_selection_75/training/test.en -mode bb -config config/config_de-en.WMT13.test.Task1.2.properties
