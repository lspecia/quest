#!/bin/bash
#java -Xmx1g -XX:+UseConcMarkSweepGC -classpath build/classes:lib/commons-cli-1.2.jar:lib/stanford-postagger.jar:lib/BerkeleyParser-1.7.jar:lib/lucene-core-3.6.2.jar shef.mt.enes.FeatureExtractorSimple -lang english spanish -input input/source.en input/target.es -mode bb -config config/config_en-es.WMT13.properties
# Train set
#java -Xmx1g -XX:+UseConcMarkSweepGC -classpath build/classes:lib/commons-cli-1.2.jar:lib/stanford-postagger.jar:lib/BerkeleyParser-1.7.jar:lib/lucene-core-3.6.2.jar shef.mt.enes.FeatureExtractorSimple -lang english spanish -input /home/ebicici/SMT/Data/QET2013/train/Task1.1/source.eng /home/ebicici/SMT/Data/QET2013/train/Task1.1/target_system.spa -mode bb -config config/config_en-es.WMT13.properties
# Test set
java -Xmx1g -XX:+UseConcMarkSweepGC -classpath build/classes:lib/commons-cli-1.2.jar:lib/stanford-postagger.jar:lib/BerkeleyParser-1.7.jar:lib/lucene-core-3.6.2.jar shef.mt.enes.FeatureExtractorSimple -lang english spanish -input /home/ebicici/SMT/Data/QET2013/test/Task1.1/source.eng /home/ebicici/SMT/Data/QET2013/test/Task1.1/target_system.spa -mode bb -config config/config_en-es.WMT13.test.properties
