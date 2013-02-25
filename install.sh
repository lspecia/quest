#/bin/sh

# download and unpack quest from FTP
wget 15.ph-litice.cz/resources/quest.tar.gz
tar -zxvf quest.tar.gz
rm quest.tar.gz

# download BerkeleyParser
wget http://berkeleyparser.googlecode.com/files/BerkeleyParser-1.7.jar
mv ./BerkeleyParser-1.7.jar ./quest/lib

# download commons-cli
wget http://ftp.halifax.rwth-aachen.de/apache//commons/cli/binaries/commons-cli-1.2-bin.tar.gz
tar -zxvf commons-cli-1.2-bin.tar.gz
rm commons-cli-1.2-bin.tar.gz
cp ./commons-cli-1.2/commons-cli-1.2.jar ./quest/lib
rm -r commons-cli-1.2

# download eng_sm6.gr
wget http://berkeleyparser.googlecode.com/files/eng_sm6.gr
mv ./eng_sm6.gr ./quest/lang_resources

<<tree-tagger
# download and install tree-tagger from http://www.ims.uni-stuttgart.de/projekte/corplex/TreeTagger/
cd quest
mkdir tree-tagger
cd tree-tagger
wget ftp://ftp.ims.uni-stuttgart.de/pub/corpora/tree-tagger-linux-3.2.tar.gz
wget ftp://ftp.ims.uni-stuttgart.de/pub/corpora/tagger-scripts.tar.gz
wget ftp://ftp.ims.uni-stuttgart.de/pub/corpora/bulgarian-par-linux-3.1.bin.gz
wget ftp://ftp.ims.uni-stuttgart.de/pub/corpora/dutch-par-linux-3.1.bin.gz
wget ftp://ftp.ims.uni-stuttgart.de/pub/corpora/dutch-par-linux-3.2-utf8.bin.gz
wget ftp://ftp.ims.uni-stuttgart.de/pub/corpora/dutch2-par-linux-3.1.bin.gz
wget ftp://ftp.ims.uni-stuttgart.de/pub/corpora/english-par-linux-3.2.bin.gz
wget ftp://ftp.ims.uni-stuttgart.de/pub/corpora/french-par-linux-3.2.bin.gz
wget ftp://ftp.ims.uni-stuttgart.de/pub/corpora/french-par-linux-3.2-utf8.bin.gz
wget ftp://ftp.ims.uni-stuttgart.de/pub/corpora/german-par-linux-3.2.bin.gz
wget ftp://ftp.ims.uni-stuttgart.de/pub/corpora/german-par-linux-3.2-utf8.bin.gz
wget ftp://ftp.ims.uni-stuttgart.de/pub/corpora/italian-par-linux-3.1.bin.gz
wget ftp://ftp.ims.uni-stuttgart.de/pub/corpora/italian-par-linux-3.2-utf8.bin.gz
wget ftp://ftp.ims.uni-stuttgart.de/pub/corpora/spanish-par-linux-3.2.bin.gz
wget ftp://ftp.ims.uni-stuttgart.de/pub/corpora/spanish-par-linux-3.2-utf8.bin.gz
wget ftp://ftp.ims.uni-stuttgart.de/pub/corpora/estonian-par-linux-3.2.bin.gz
wget ftp://ftp.ims.uni-stuttgart.de/pub/corpora/swahili-par-linux-3.2.bin.gz
wget ftp://ftp.ims.uni-stuttgart.de/pub/corpora/latin-par-linux-3.2.bin.gz
wget ftp://ftp.ims.uni-stuttgart.de/pub/corpora/mongolian-par-linux-3.2.bin.gz
wget ftp://ftp.ims.uni-stuttgart.de/pub/corpora/english-chunker-par-linux-3.2.bin.gz
wget ftp://ftp.ims.uni-stuttgart.de/pub/corpora/french-chunker-par-linux-3.1.bin.gz
wget ftp://ftp.ims.uni-stuttgart.de/pub/corpora/french-chunker-par-linux-3.2-utf8.bin.gz
wget ftp://ftp.ims.uni-stuttgart.de/pub/corpora/german-chunker-par-linux-3.1.bin.gz
wget ftp://ftp.ims.uni-stuttgart.de/pub/corpora/german-chunker-par-linux-3.2-utf8.bin.gz
wget ftp://ftp.ims.uni-stuttgart.de/pub/corpora/install-tagger.sh
sh install-tagger.sh
rm install-tagger.sh
rm *.gz
cd ../..
tree-tagger

