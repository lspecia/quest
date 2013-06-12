import sys 
from CCGoperators import *


# this parser uses :
#	forward application
#	backward application
#	forward composition
#	backward composition
#	conj has category (X\X)/X when it's followed  by a category C, the result is  C\C  (see steedman paper)
#	conj has category (X\X)/X when it's proceeded  by a category C, the result is  C/C  (see steedman paper)
#	any punct+supertag =the same supertag
#	if any of the operations can't be done because N is found instead of NP, replace N with NP and do the operatoin

# this parser is designed to work on feature-stripped supertags. running it on supertags with featrues willl yield failure on most of the phrases


	
#this function takes a upertagged line whose words is of the form word|supertag and returns supertags list and words list.
def mapSupertags(supline):

        wordstags=supline.split(' ')
        supertags=[]
	words=[]
        for wordtag in wordstags:
                wspair=wordtag.split('|')
                supertags.append(wspair[1])
		words.append (wspair[0])


        return (supertags,words)

def getPhrase(words,start,end):
        p=''
       
        for i in range (start,end+1):
                p+=words[i]+' '
        return p




#this method tries to combine the input supertags using one of the four combinatroy operators
def combineSupertags(supertag1, supertag2):
	
	puncts=[',','.',';','(',')','[',']',':','?','!','\'','"','<','>']
	if (supertag2.all in puncts):  # any supertag + punct is the same supertag
		return supertagInfo(supertag1.result,supertag1.left, supertag1.right,supertag1.all)
	else:
		if (supertag1.all in puncts):
	
			return supertagInfo(supertag2.result,supertag2.left, supertag2.right,supertag2.all)
	
	if (supertag1.all=='conj'): # conj has category (X\X)/X when it's followed  by a category C, the result is  C\C  (see steedman paper)
		mid=supertagInfo( supertag2.all, supertag2.all, '', '')
		mid.all=mid.printSupertag()
		return mid
	else:
		if (supertag2.all=='conj'): # conj has category (X\X)/X when it's proceeded  by a category C, the result is  C/C  (see steedman paper)
                	mid=supertagInfo( supertag1.all,'', supertag1.all,'')
                	mid.all=mid.printSupertag()
                	return mid
	
		
 
	combRes=forwardAp(supertag1, supertag2)
	if combRes!=None: return combRes
	
	combRes=backwardAp(supertag1, supertag2)
        if combRes!=None: return combRes

	combRes=forwardComp(supertag1, supertag2)
        if combRes!=None: return combRes

 	combRes=backwardComp(supertag1, supertag2)
        if combRes!=None: return combRes
	 
	combRes=backwardCrossedComp(supertag1, supertag2)
        if combRes!=None: return combRes

	combRes=forwardCrossedComp(supertag1, supertag2)
	if combRes!=None: return combRes

        #combRes=backwardCrossedComp(supertag1, supertag2)
	#if combRes!=None: return combRes

#assigns a supertag for each possible sub phrase using a chart and returns the chart
def parseSentence(supertags): 
	spans=[]
	for j in range (len(supertags)):
		
		spans.insert(j,[])
		for i in range (len(supertags)-j):
			spans[j].insert(i,None)		
	

	sindex=0 # index of the sueprtag
	for s in supertags:
		spans[0][sindex]=extractSupertagInfo(s)
		sindex+=1
	
#	print ('spans',spans[0][0].all,spans[0][1].all)

	for j in range (1,len(supertags)): # length-1 of the phrase to parse, begins at 1 because first level 0 is filled in the earlier stage
	        for s in range (len(supertags)-j): # start index of the phrase
			end=s+j # end of the phrase
			 
			for l1 in range (0,j): # length of the first subhrase
					s1=s # start of the first phrase
					end1=s1+l1
					s2=end1+1 #start of the second subphrase
					l2= j-1-l1#length of the second phrase
					end2=s2+l2   #end of the second subphrase
	 				#print ('j,s,l1,s1,l2,s2', j,s,l1,s1,l2,s2)
				#	print (spans[0][0].all,'with',spans[0][1].all)
                                        #print ( 'res',combineSupertags(spans[l1][s1].,spans[l2][s2] ))
		
					if (spans[l1][s1]!= None and spans[l2][s2]!=None and spans[j][s]==None):
					#	print (spans[l1][s1],'with',spans[l2],[s2])
						spans[j][s]=combineSupertags(spans[l1][s1],spans[l2][s2] )
	#					print ('j,s,l1,s1,l2,s2', j,s,l1,s1,l2,s2)


	return spans

#this function takes a supertagged line and returns the chart corresponding to it
def getSpans(superline): 

		line=superline.strip()
                supertags=mapSupertags(line)[0]
                words=mapSupertags(line)[1]
                spans=parseSentence(supertags)
		return spans

# this function returns the CCG supertag from the input chart corresponding to the phrase between start and end
#if no supertag found, a _FAIL string is returned 
def getPhraseSupertag(spans, start, end):
		 plen= end-start+1 #length of the phrase
		 supertaginfo=spans[plen-1][start]
                 supertag='_FAIL'
                 if supertaginfo!=None:
                     supertag=supertaginfo.printSupertag() 

		 return (supertag,supertaginfo)


if __name__ == '__main__':


	#supertaginfo=extractSupertagInfo('((S\NP)/NP)/NP')
	#print (supertaginfo.left,supertaginfo.right, supertaginfo.result)
	print ('enfile, supertagged file')
	enfile=open(sys.argv[1],'r')
	superfile=open(sys.argv[2],'r')
	outfile=open (sys.argv[1]+'.CCGsuperAnnotated','w')
	info=open(sys.argv[2]+'.parseinfo','w')

	sentnum=1
	
	for superline in superfile:
        	annotline=enfile.next().strip()
		sentlen=annotline.count(' ')+1
		#print ('sentlne',sentlen)
        	spans=getSpans(superline)
		words=mapSupertags(superline)[1]
                info.write('Sent# '+str(sentnum)+' : '+getPhrase(words,0,len(words)-1)+'\n')
        	for plen in range (1,sentlen+1):
                	        for start in range (0,sentlen-plen+1):
					#print (plen,start)
					end=start+plen-1
					info.write('        **Range**: '+str(start)+','+str(end)+'\n')
                        	        
                                	supertag=getPhraseSupertag(spans,start,end)
                                	annotline=annotline+' <tree span="'+str(start)+'-'+str(end)+'" label="'+supertag[0]+'"/>'
					info.write('        '+getPhrase(words,start,end)+'   supertag: '+supertag[0]+'\n')
					if (supertag[1]!=None):	
							info.write('	left'+supertag[1].left+' res '+ supertag[1].result+ ' right '+ supertag[1].right)
							info.write('\n')


        	annotline+='\n'
        	outfile.write(annotline)
		sentnum+=1

	enfile.close()
	superfile.close()
	outfile.close()
	info.close()

