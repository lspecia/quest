import sys,os,imp,subprocess

myCCGparser=imp.load_source('myCCGparser',os.path.dirname(sys.argv[0])+'/myCCGparser.py')
CCGoperators=imp.load_source('CCGoperators',os.path.dirname(sys.argv[0])+'/CCGoperators.py')


def numOfUmatchedArgs(supertagsFile):
	file=open(supertagsFile,'r')
	supertags=file.next().split(" ")
	mismatches=0
	for i in range(len(supertags)-1):
		super1=supertags[i]
		super2=supertags[i+1]
		res=myCCGparser.combineSupertags(CCGoperators.extractSupertagInfo(super1),CCGoperators.extractSupertagInfo(super2))
		if res==None:
			mismatches+=1
	file.close()
	return mismatches



def reverseTable(tableFile):

	table=open(tableFile,'r')
	invtable=open(tableFile+'.inv','w')

	for line in table:
        	sep=' ||| '
        	seplen=len(sep)
        	sindex=line.find(sep)
        	source=line[:sindex]
        	tindex=line.find(sep,sindex+seplen)
        	target=line[sindex+seplen:tindex]
        	stindex=line.find(sep,tindex+seplen)
        	starget=line[tindex+seplen:stindex]
        	rest=line[stindex+seplen:]
        	invtable.write(target+sep+starget+sep+source+sep+rest)

	table.close()
	invtable.close()

def filterPhraseTable(workdir,sourceFileName,targetFileName):
	os.system("rm -r "+workdir+"/*.superppl")
	os.system("cp "+workdir+"/"+targetFileName+ " "+workdir+"/sent.tgt")
        os.system("cp "+workdir+"/"+sourceFileName+ " "+workdir+"/sent.src")
        os.system("rm -r "+workdir+"/filteredSource"+targetFileName)
        os.system("rm -r "+workdir+"/filteredSource")
	os.system("rm -r "+workdir+"/*.superppl")
	os.system(currentdir+"/filter-model-given-input.pl "+workdir+"/filteredSource  "+currentdir+"/moses.ini "+workdir+"/sent.src")
        #os.system("python "+workdir+"/reverseTable.py "+workdir+"/filteredSource/phrase-table.0-0.1.1")
        reverseTable(workdir+"/filteredSource/phrase-table.0-0.1.1")
	os.system("rm -r "+workdir+"/filteredModel"+targetFileName)
        os.system(currentdir+"/filter-model-given-input.pl "+workdir+"/filteredModel "+currentdir+"/moses_reverse.ini "+workdir+"/sent.tgt")
        os.system("mv "+workdir+"/filteredSource "+ workdir+"/filteredSource"+targetFileName)
        os.system("mv "+workdir+"/filteredModel "+ workdir+"/filteredModel"+targetFileName)
        return

def parseCCG(inputfile):
	inputfiledir=os.path.dirname(inputfile)
	#os.system("cd "+inputfiledir)
	os.system(currentdir+"/parser --parser "+currentdir+"/models/parser --super "+currentdir+"/models/super --input="+inputfile+"  --output="+inputfile+'.ccg --ifmt="%w|%p|%s \n" --oracle --ext_super')
	
	#subprocess.call([(workdir+"/parser", "--parser "+workdir+"/models/parser", "--super "+workdir+"/models/super, "--input="+inputfile , "--output="+inputfile+".ccg", '--ifmt\=\"%w|%p|%s \n\"','--oracle', '--ext_super'])
	#os.system("mv "+inputfiledir+"/relaxedTrees.xml "+inputfile+".chartlabels.xml")
	os.system("mv relaxedTrees.xml "+inputfile+".chartlabels.xml")

funct=sys.argv[1]
currentdir=os.path.dirname(sys.argv[0]) #path to  the directory containing the  scripts 


def buildMosesiniFiles(workdir):
	mosesini=open(currentdir+"/moses.ini",'w')
	mosesini.write("[ttable-file]\n")
	mosesini.write("0 0 0 5 "+currentdir+"/phrase-table.0-0.1.1\n\n")
	mosesini.close()
	mosesreverse=open(currentdir+"/moses_reverse.ini",'w')
	mosesreverse.write("[ttable-file]\n")
        mosesreverse.write("0 0 0 5 "+workdir+"/filteredSource/phrase-table.0-0.1.1.inv\n\n")
        mosesreverse.close()



if funct=="filter":
	buildMosesiniFiles(sys.argv[2])
	filterPhraseTable(sys.argv[2],sys.argv[3],sys.argv[4])
	
elif funct=="parse":
	parseCCG(sys.argv[2])
elif funct=="args":
	print(numOfUmatchedArgs(sys.argv[2]))
	
