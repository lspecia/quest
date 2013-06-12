#!/bin/bash
java -Xmx1g -XX:+UseConcMarkSweepGC -classpath build/classes:lib/commons-cli-1.2.jar:lib/stanford-postagger.jar:lib/BerkeleyParser-1.7.jar shef.mt.enes.FeatureExtractorSimple -lang english spanish -input ../quest-vanilla/input/arEn_news/source.ar.5  ../quest-vanilla/input/arEn_news/target.en.5  -mode ccg -config config/config_en-es.properties
