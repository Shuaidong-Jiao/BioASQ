import nltk
import json
import re
import numpy as np
from nltk import word_tokenize
from sklearn.feature_extraction.text import CountVectorizer
from nltk.corpus import stopwords # Import the stop word list

from nltk.stem.wordnet import WordNetLemmatizer
def jx_json():
    print('你好！')
    print (stopwords.words("english"))
    stops = set(stopwords.words("english"))
    print(len(stops))
    vectorizer = CountVectorizer(analyzer = "word", tokenizer = None, preprocessor = None,  stop_words = stops, max_features = 5000) #None
    jsf=open('BioASQ-trainingDataset5b_testset3.json', 'r')
    res=json.load(jsf)
    clean_train_questions = []
    lmtzr = WordNetLemmatizer()
    beginWithDoOrBeTag=np.zeros(654+41)
    containTokenOrTag=np.zeros(654+41)
    containQuantityPhrase=np.zeros(654+41)
    #res=json.load(open(fls, 'r'))
    number=np.zeros(654+41)
    num=-1
    for i in range(0,1407):
        questionType=res['questions'][i]['type']
        if questionType=='factoid' or questionType=='list':
            num=num+1
            #rawQuestionBody=res['questions'][i]['body'].split()         #对原本的问题进行分词
            rawQuestionBody=re.split(r'[ "\t\n\.]+',res['questions'][i]['body'])#\?
            lemmaQuestionBody=[]
            number[num]=i+1
            answerType='entity'
            choiceType=False
            j=0
            word=lmtzr.lemmatize(rawQuestionBody[j])
            if word== 'Do' or word== 'Be':
                beginWithDoOrBeTag[num]=1;
            for j in range(0,len(rawQuestionBody)):
                word=lmtzr.lemmatize(rawQuestionBody[j])
                if word == 'or':
                   containTokenOrTag[num]=1
                lemmaQuestionBody.append(word)
               # print(lemmaQuestionBody[j])
            meaningful_words = [w for w in lemmaQuestionBody if not w in stops]
            clean_question=" ".join(lemmaQuestionBody)
            clean_train_questions.append(clean_question)
		#if questionType=='yesno':
            
        #print('choiceType is',choiceType)
        #print('questionType is',questionType)
        #print(res['questions'][i]['body'])
        #print(res['questions'][i]['ideal_answer'])
    #print(clean_train_questions)
    train_vocab_dictionary = vectorizer.fit(clean_train_questions)
    print(train_vocab_dictionary)
    train_question_features = vectorizer.fit_transform(clean_train_questions)
    print(train_question_features.shape)
    tfidf_value=np.zeros((654+41,1749))
    #train_question_features = train_question_features.toarray()train_question_features.sum(axis=i)
    wordsCountInthisQuery=np.zeros(654+41)
    thisWordAppearInQuerysCount=np.zeros(1749)
    for i in range(0,654+41):
        wordsCountInthisQuery[i]=train_question_features[i,0:1749].sum()
    for j in range(0,1749):
        thisWordAppearInQuerysCount[j]=train_question_features[0:695,j].sum()
    for i in range(0,654+41):
        for j in range(0,1749):
            tfidf_value[i,j]=(train_question_features[i,j]/wordsCountInthisQuery[i])*np.log(695/thisWordAppearInQuerysCount[j])
    #print(train_question_features)
    print(tfidf_value)
    f=open('features5b_testset3_basicTfidf_newest_2017_4_6_rmStops.txt','r+')
    for i in range(0,654):
        lineCount=0
        s0=str(number[i])
        f.write(s0+' ')
        for j in range(0,1749):
            if tfidf_value[i,j]!=0:
               if lineCount<5:
                  s=str(tfidf_value[i,j])
                  n=str(j+1)
                  f.write(n+':'+s+' ')
                  #f.write(str(1749+3+lineCount)+':'+s+' ')
               else:
                  s=str(tfidf_value[i,j])
                  n=str(j+1)
                  f.write(n+':'+s+' ')
            lineCount=lineCount+1
            # s=str(tfidf_value[i,j])
            # f.write(s+' ')
        f.write('1750:')
        s1=str(beginWithDoOrBeTag[i])
        f.write(s1+' ')
        f.write('1751:')
        s2=str(containTokenOrTag[i])
        f.write(s2+' ')
        f.write('\n')
    f.close()
    f1=open('phaseB_5b_03_features_basicTfidf_newest_2017_4_6_rmStops.txt','r+')
    for i in range(654,654+41):
        #s0=str(number[i])
        #f1.write(' 1:')
        lineCount=0
        s0=str(number[i])
        f1.write(s0+' ')
        for j in range(0,1749):
            if tfidf_value[i,j]!=0:
                
               if lineCount<5:
                  s=str(tfidf_value[i,j])
                  n=str(j+1)
                  f1.write(n+':'+s+' ')
                  #f1.write(str(1749+3+lineCount)+':'+s+' ')
               else:
                  s=str(tfidf_value[i,j])
                  n=str(j+1)
                  f1.write(n+':'+s+' ')
            lineCount=lineCount+1
                # s=str(tfidf_value[i,j]) if tfidf_value[i,j]!=0:
                # n=str(j+1)
                # f1.write(n+':'+s+' ')
	
        f1.write('1750:')
        s1=str(beginWithDoOrBeTag[i])
        f1.write(s1+' ')
        f1.write('1751:')
        s2=str(containTokenOrTag[i])
        f1.write(s2+' ')
        f1.write('\n')
    f1.close()
    print(tfidf_value)
	
    #train_question_np_features = np.array(train_question_features)
    vocab = vectorizer.get_feature_names()
    #print (vocab)
    print (train_question_features.shape)
    #dist = np.sum(train_question_features, axis=0)
    #for tag, count in zip(vocab, dist):
       #print(count, tag)
    #print(res['db']['ip'])
    #print(res['db']['port'])
    jsf.close()
 
if __name__ == "__main__":
    jx_json()