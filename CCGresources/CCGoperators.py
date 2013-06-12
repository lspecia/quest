import sys



def removeParens(s): # this function removes the perenthesis at the beginign and end of the string

        if s.startswith('('):
                s=s[1:]
        if s.endswith(')'):
                s=s[:len(s)-1] # remove the left and right peranthesis
        return s


def extractSupertagInfo(supertagOrig):
        supertag=supertagOrig
        numOfOpenParens=0
        rightTag=''
        leftTag=''
        i=0
        slen=len(supertag)
        tag=['','',''] # the list of the right and left tags and the result
        tagRef=0 # 0 the right t tag, 1 the left  tag,  2 for the result

        while i<slen:
                if supertag[i]=='(':
                        numOfOpenParens+=1
                if supertag[i]==')':
                        numOfOpenParens-=1
                if numOfOpenParens==0:
                        if supertag[i] in ['\\','/']:
				
                                if supertag[i]=='/':
                                        tagRef=0#right
                                else:
                                        if supertag[i]=='\\':
                                                tagRef=1 # left


                                if (tag[tagRef]==''): # the  tag has not been assigned yet

                                        tag[tagRef]=supertag[i+1:]
                                        #print ("right tag found in supertag:", supertag)
                                        supertag=removeParens(supertag[0:i])
                                        #print ("new supertag:", supertag)

                                        slen=len(supertag)
                                        i=-1 # start from the begining, the counter is increased at the end of the loop

                                        tag[tagRef]=removeParens(tag[tagRef]) # remove the left and right peranthesis


                                        if tag[0]!='' and tag[1]!='':
						tag[2]=supertag
                                                return supertagInfo(tag[2],tag[1],tag[0],supertagOrig)


                i+=1
                #print ('right and left tags:', [rightTag,leftTag])
	tag[2]=supertag
        return supertagInfo(tag[2],tag[1],tag[0],supertagOrig)



 
class supertagInfo(object):

	def __init__(self,res='',left='',right='',all=''):
		self.result=res
		self.left=left
		self.right=right
		self.all=all
	
	def isComplex(self, cat): #determines if the input category is complex (has at least one arguemnt)
                return cat.find('\\')!=-1 or cat.find('/')!=-1

        def printSupertag(self):
                	s=''
                #if self.all!='':
                #        return self.all
               # else:
			L='' #left argument
		        if (self.isComplex(self.left)): # complex arguments need parenthesis
				L='('+self.left+')'
			else:
				L=self.left
	
			R=''# right argument
			if (self.isComplex(self.right)): # complex arguments need parenthesis
                                R='('+self.right+')'
                        else:
                                R=self.right


                        if self.left=='' and self.right=='':
                                return self.result #no left or right arguments
                        else:
                                if self.isComplex(self.result):
                                        s='('+self.result+')'
				else:
					s=self.result

                                if self.left!='':
                                        s=s+'\\'+L

                                        if self.right!='': # left and right arguments exist
                                                return '('+s+')/'+R
					else:
						return s # only left argument
				if self.right!='':
					return s+'/'+R # only right argument 

def forwardAp (supertag1, supertag2): # X/Y Y -->X
	res=supertagInfo()


	if supertag1.right==supertag2.all or (supertag1.right=='NP' and supertag2.all=='N'):
		Xcat=supertagInfo(supertag1.result,supertag1.left,'') # X might contatin another right argument as in ((S\NP)/NP)/NP that's whay the information from X are rextracted in the next step in order to assign the left and right arguemnts correctly for the results
	
	 	res=extractSupertagInfo(Xcat.printSupertag())
		
		return res
	else:
		return None
			

def backwardAp (supertag1, supertag2): # Y X\Y -->X 
        res=supertagInfo()


        if supertag2.left==supertag1.all or (supertag2.left=='NP' and supertag1.all=='N'):
                Xcat=supertagInfo(supertag2.result,'', supertag2.right)
		res=extractSupertagInfo(Xcat.printSupertag())
                return res
        else:
               
	        return None


def forwardComp (supertag1, supertag2): #X/Y Y/Z --> X/Z
        res=supertagInfo()
	mid=supertagInfo(supertag2.result, supertag2.left,'') # Y=res+L	
	mid.all=mid.printSupertag()
        if supertag1.right==mid.all or ( supertag1.right=='NP' and mid.all=='N'):
                res.left=supertag1.left # no effect on the left argument
                res.result=supertag1.result # no effect on the result
                res.right=supertag2.right
                res.all=res.printSupertag()
                return res
        else:
                return None

def forwardCrossedComp(supertag1, supertag2):  # X/Y Y\Z --> X\Z
	res=supertagInfo()
	mid=supertagInfo(supertag2.result,'',supertag2.right)  #Y=res+R
	mid.all=mid.printSupertag()
	if supertag1.right==mid.all or  ( supertag1.right=='NP' and mid.all=='N'):
		Xcat=supertagInfo(supertag1.result,supertag1.left,'') #midium category represents the cat X
                compres=supertagInfo(Xcat.printSupertag(), supertag2.left, '') #X\Z
		res=extractSupertagInfo(compres.printSupertag())
                return res
        else:
                return None

def backwardCrossedComp(supertag1, supertag2):  # Y/Z X\Y  --> X/Z
        res=supertagInfo()
        midY=supertagInfo(supertag1.result,supertag1.left,'')  #Y=res+L
        midY.all=midY.printSupertag()
        if supertag2.left==midY.all or  ( supertag2.left=='NP' and midY.all=='N'):
                Xcat=supertagInfo(supertag2.result,'',supertag2.right)
		compres=supertagInfo(Xcat.printSupertag(),'',supertag1.right) #X/Z
		res=extractSupertagInfo(compres.printSupertag()) # extract the arguments
                
                return res
        else:
                return None


def backwardComp (supertag1, supertag2): #Y\Z X\Y --> X\Z
        res=supertagInfo()
        mid=supertagInfo(supertag1.result,'',  supertag1.right) # Y=res+R
        mid.all=mid.printSupertag()
	#print ('Mid: ', mid.printSupertag())
        if supertag2.left==mid.all or (supertag2.left=='NP' and mid.all=='N'):
                res.right=supertag2.right # no effect on the right argument
                res.result=supertag2.result # no effect on the result
                res.left=supertag1.left
                res.all=res.printSupertag()
                return res
        else:
                return None




if __name__ == '__main__':
	s1=extractSupertagInfo(sys.argv[1])
	print ('S1=', s1.result,s1.left,s1.right)	
	s2=extractSupertagInfo(sys.argv[2])
	fa= forwardCrossedComp (s1,s2) 
	print ('S2=',s2.result, s2.left,s2.right)
	if fa!=None:
		print ('FCrossedComp: ', fa.printSupertag())
	else:
		print ('None')
