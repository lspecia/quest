#!/bin/bash
java -Xmx1g -XX:+UseConcMarkSweepGC -classpath build/classes:lib/commons-cli-1.2.jar shef.mt.enes.SentenceFeatureExtractorSimple -lang english spanish -input input/input.jcml -mode bb -config config/config_en-es.vanilla.properties
