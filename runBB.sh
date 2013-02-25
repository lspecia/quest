#!/bin/bash
java -Xmx1g -XX:+UseConcMarkSweepGC -classpath build/classes:lib/commons-cli-1.2.jar:lib/stanford-postagger.jar:lib/BerkeleyParser-1.7.jar shef.mt.enes.FeatureExtractorSimple -lang english spanish -input input/source.en input/target.es -mode bb -config config/config_en-es.properties
