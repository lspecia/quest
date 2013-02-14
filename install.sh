te Quest installation directory, if it doesn't exist
installDir='quest'
if ! [ -d $installDir ]; then
    mkdir $installDir
    echo 'Installation directory created: '$installDir
fi

#cd $installDir
#echo 'cd '$installDir

# download and unpack automated
wget -O quest https://github.com/lspecia/quest/archive/customer-inst.zip
echo 'quest downloaded'
unzip quest.zip
echo 'quest unzipped'
rm quest.zip
echo 'quest.zip removed'

# download eng_sm6.gr
# http://berkeleyparser.googlecode.com/files/eng_sm6.gr

# download tree-tagger
#http://www.ims.uni-stuttgart.de/projekte/corplex/TreeTagger/
#ftp://ftp.ims.uni-stuttgart.de/pub/corpora/tree-tagger-linux-3.2.tar.gz
#ftp://ftp.ims.uni-stuttgart.de/pub/corpora/tagger-scripts.tar.gz
#ftp://ftp.ims.uni-stuttgart.de/pub/corpora/install-tagger.sh
#ftp://ftp.ims.uni-stuttgart.de/pub/corpora/bulgarian-par-linux-3.1.bin.gz
#ftp://ftp.ims.uni-stuttgart.de/pub/corpora/dutch-par-linux-3.1.bin.gz
#ftp://ftp.ims.uni-stuttgart.de/pub/corpora/dutch-par-linux-3.2-utf8.bin.gz
#ftp://ftp.ims.uni-stuttgart.de/pub/corpora/dutch2-par-linux-3.1.bin.gz
#ftp://ftp.ims.uni-stuttgart.de/pub/corpora/english-par-linux-3.2.bin.gz
#ftp://ftp.ims.uni-stuttgart.de/pub/corpora/french-par-linux-3.2.bin.gz
#ftp://ftp.ims.uni-stuttgart.de/pub/corpora/french-par-linux-3.2-utf8.bin.gz
#ftp://ftp.ims.uni-stuttgart.de/pub/corpora/german-par-linux-3.2.bin.gz
#ftp://ftp.ims.uni-stuttgart.de/pub/corpora/german-par-linux-3.2-utf8.bin.gz
#ftp://ftp.ims.uni-stuttgart.de/pub/corpora/italian-par-linux-3.1.bin.gz
#ftp://ftp.ims.uni-stuttgart.de/pub/corpora/italian-par-linux-3.2-utf8.bin.gz
#ftp://ftp.ims.uni-stuttgart.de/pub/corpora/spanish-par-linux-3.2.bin.gz
#ftp://ftp.ims.uni-stuttgart.de/pub/corpora/spanish-par-linux-3.2-utf8.bin.gz
#ftp://ftp.ims.uni-stuttgart.de/pub/corpora/estonian-par-linux-3.2.bin.gz
#ftp://ftp.ims.uni-stuttgart.de/pub/corpora/swahili-par-linux-3.2.bin.gz
#ftp://ftp.ims.uni-stuttgart.de/pub/corpora/latin-par-linux-3.2.bin.gz
#ftp://ftp.ims.uni-stuttgart.de/pub/corpora/mongolian-par-linux-3.2.bin.gz
#ftp://ftp.ims.uni-stuttgart.de/pub/corpora/english-chunker-par-linux-3.2.bin.gz
#ftp://ftp.ims.uni-stuttgart.de/pub/corpora/french-chunker-par-linux-3.1.bin.gz
#ftp://ftp.ims.uni-stuttgart.de/pub/corpora/french-chunker-par-linux-3.2-utf8.bin.gz
#ftp://ftp.ims.uni-stuttgart.de/pub/corpora/german-chunker-par-linux-3.1.bin.gz
#ftp://ftp.ims.uni-stuttgart.de/pub/corpora/german-chunker-par-linux-3.2-utf8.bin.gz
# run the installation script in the directory where the files have been downloaded:
#sh install-tagger.sh

# download stanford-parser.jar
#http://mvnrepository.com/artifact/edu.stanford.nlp/stanford-parser/

# download commons-cli-1.2.jar
#http://commons.apache.org/cli/download_cli.cgi
#http://www.java2s.com/Code/Jar/c/Downloadcommonscli12jar.htm
#http://www.java2s.com/Code/JarDownload/commons-cli/commons-cli-1.2.jar.zip

# download stanford-postagger.jar
#http://nlp.stanford.edu/software/stanford-postagger-full-2012-11-11.zip  164 MB sized package!!! ...lib itself 1.5 MB

#cd quest-master
#echo 'quest-master'
# compile java source code files
#echo 'compiling Lucia java source files'
#javac -classpath build/classes:lib/*:config -d build/classes ./src/wlv/mt/enes/*.java ./src/wlv/mt/*.java ./src/wlv/mt/features/impl/*.java ./src/wlv/mt/features/impl/bb/*.java ./src/wlv/mt/features/impl/gb/*.java ./src/wlv/mt/features/util/*.java ./src/wlv/mt/features/wce/*.java ./src/wlv/mt/syntree/*.java ./src/wlv/mt/test/*.java ./src/wlv/mt/tools/*.java ./src/wlv/mt/tools/stf/*.java ./src/wlv/mt/util/*.java ./src/wlv/mt/xmlwrap/*.java ./src/wlv/mt/xmlwrap/fst/*.java
#echo 'Lucia java source files compiled'

# launch program
#sh run.sh

# move stanford-postagger.jar into quest-master/lib
#cp stanford-postagger.jar quest-master/lib
#echo 'Stanford-postagger.jar copied to quest-master/lib'

#http://nlp.stanford.edu/software/ - what exactly??? I guess this: http://nlp.stanford.edu/software/stanford-postagger-full-2012-11-11.zip
#wget -O stanford.zip 'http://nlp.stanford.edu/software/stanford-postagger-full-2012-11-11.zip'
#echo 'Stanford-postagger.zip downloaded...'
#unzip stanford.zip
#echo 'Stanford-postagger unzipped...'
#rm stanford.zip
#echo 'Stanford-postagger.zip removed...'

# move stanford-postagger.jar into quest-master/lib
#cp stanford-postagger-full-2012-11-11/stanford-posttagger.jar quest-master/lib
#echo 'Stanford-postagger.jar copied to downloaded...'

