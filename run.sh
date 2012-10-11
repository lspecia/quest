#!/bin/bash
# Remove any intruding character (still necessary for native UTF-8 files)
iconv --from-code=UTF-8 --to-code=ISO-8859-1//TRANSLIT input/source.en | iconv --from-code=ISO-8859-1 --to-code=UTF-8//TRANSLIT --output input/source.en
iconv --from-code=UTF-8 --to-code=ISO-8859-1//TRANSLIT input/target.es | iconv --from-code=ISO-8859-1 --to-code=UTF-8//TRANSLIT --output input/target.es
java -Xmx1g -XX:+UseConcMarkSweepGC -classpath build/classes:lib/commons-cli-1.2.jar:lib/stanford-postagger.jar wlv.mt.enes.FeatureExtractorSimple -lang english spanish -input input/source.en input/target.es -mode bb -config config/config_enes.properties
