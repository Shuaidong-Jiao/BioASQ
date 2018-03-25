import nltk
import json
import numpy as np
import re
from nltk import word_tokenize
from sklearn.feature_extraction.text import CountVectorizer
from nltk.corpus import stopwords # Import the stop word list
from gensim import corpora, models, similarities
from gensim.models import Word2Vec
from gensim.models.word2vec import LineSentence

from nltk.stem.wordnet import WordNetLemmatizer
def jx_json():
    print('你好！')
    print (stopwords.words("english"))
    stops = set(stopwords.words("english"))
    vectorizer = CountVectorizer(analyzer = "word", tokenizer = None, preprocessor = None,  stop_words = None, max_features = 5000)
    jsf=open('BioASQ-trainingDataset4b_4btestset2.json', 'r')
    res=json.load(jsf)
    clean_train_questions = []
    lmtzr = WordNetLemmatizer()
    beginWithDoOrBeTag=np.zeros(654+52)
    containTokenOrTag=np.zeros(654+52)
    containQuantityPhrase=np.zeros(654+52)
    #res=json.load(open(fls, 'r'))
    number=np.zeros(654+52)
    num=-1
    for i in range(0,1407):
        questionType=res['questions'][i]['type']
        if questionType=='factoid' or questionType=='list':
            num=num+1
            #rawQuestionBody=res['questions'][i]['body'].split()         #对原本的问题进行分词
            rawQuestionBody=re.split(r'[ "\t\n\?\.]+',res['questions'][i]['body'])
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
			
    # f3=open('BioASQ_4b_04_trainingWords5.txt','r+')
    # for j in range(0,654+52):
        # f3.write(clean_train_questions[j])
        # f3.write('\n')
    # f3.close()
		
		
    train_vocab_dictionary = vectorizer.fit(clean_train_questions)
    print(train_vocab_dictionary)
    train_question_features = vectorizer.fit_transform(clean_train_questions)
    print(train_question_features.shape)
    vectorizer_feature_names=vectorizer.get_feature_names()
    print(vectorizer_feature_names)
    print(len(vectorizer_feature_names))
    tfidf_value=np.zeros((654+52,1772))
    #train_question_features = train_question_features.toarray()train_question_features.sum(axis=i)
    wordsCountInthisQuery=np.zeros(654+52)
    thisWordAppearInQuerysCount=np.zeros(1772)
    for i in range(0,654+52):
        wordsCountInthisQuery[i]=train_question_features[i,0:1772].sum()
    for j in range(0,1772):
        thisWordAppearInQuerysCount[j]=train_question_features[0:700,j].sum()
    for i in range(0,654+52):
        for j in range(0,1772):
            tfidf_value[i,j]=(train_question_features[i,j]/wordsCountInthisQuery[i])*np.log(700/thisWordAppearInQuerysCount[j])
    keyWord=[]
    f5=open('BioASQ_4b_4_word2vec_text_minCount_1.vector','r+') #BioASQ_4b_4_word2vec_text_minCount_1.vector
    for line in f5:
        lineContent=line.split()     #167 60
        keyWord.append(lineContent[0])
    print(len(keyWord))
    f6=open('features4b_testset4_rmwenhao.txt','r+')
    tf_idf_lineContent=[]
    for line2 in f6:
        tf_idf_lineContent.append(line2.split())
    model = Word2Vec.load('BioASQ_4b_4_word2vec_minCount_1.model')
    question_word=[]
    f7=open('BioASQ_4b_4_questions_word2vecAddTfidf_minCount_1_notscaled_vector.txt','r+')
    for i in range(0,654): #+52
        question_word=clean_train_questions[i].split()
        question_word2vec=np.zeros(60)
        match=0
        f7.write(str(number[i])+' ')
        for j in range(0,len(question_word)):
            if question_word[j] in keyWord:
                match=match+1
                for k in range(1,len(tf_idf_lineContent[i])-2):
                    tf_idf=re.split(r'[:]+',tf_idf_lineContent[i][k])  #119 philadelphia translocation philadelphia
                    print(tf_idf)
                    tf_idf_num=float(tf_idf[0])
                    tf_idf_value=float(tf_idf[1])
                    print(tf_idf_num)
                    print(tf_idf_value)
                    print(number[i])
                    print(vectorizer_feature_names[int(tf_idf_num)-1])
                    if vectorizer_feature_names[int(tf_idf_num)-1]== question_word[j]:
                        print(question_word2vec)
                        question_word2vec=question_word2vec+model[question_word[j]]*tf_idf_value
                        print(question_word2vec)
        for j in range(0,1772):
            if tfidf_value[i,j]!=0:
                s=str(tfidf_value[i,j])
                n=str(j+1)
                f7.write(n+':'+s+' ')  
        f7.write('1773:')
        s1=str(beginWithDoOrBeTag[i])
        f7.write(s1+' ')
        f7.write('1774:')
        s2=str(containTokenOrTag[i])
        f7.write(s2+' ')
        question_word2vec=question_word2vec/match
        
        for k in range(0,60):
            f7.write(str(k+1774+1)+':'+str(question_word2vec[k])+' ')
        f7.write('\n')
    f7.close()
    f8=open('BioASQ_4b_4_testsetquestions_word2vecAddTfidf_minCount_1_notscaled_vector.txt','r+')
    f9=open('phaseB_4b_04_features_rmwenhao.txt','r+')
    tf_idf_lineContent=[]
    for line2 in f9:
        tf_idf_lineContent.append(line2.split())
    for i in range(654,654+52): #+52
        question_word=clean_train_questions[i].split()
        question_word2vec=np.zeros(60)
        match=0
        for j in range(0,len(question_word)):
            if question_word[j] in keyWord:
                match=match+1
                for k in range(0,len(tf_idf_lineContent[i-654])-2):#-2    注意这里是否输出测试问题的编号，不输出则为从0开始
                    tf_idf=re.split(r'[:]+',tf_idf_lineContent[i-654][k])  #119 philadelphia translocation philadelphia
                    print("i is : "+str(i))
                    print(tf_idf)
                    tf_idf_num=float(tf_idf[0])
                    tf_idf_value=float(tf_idf[1])
                    print(tf_idf_num)
                    print(tf_idf_value)
                    print(number[i]) 
                    print(vectorizer_feature_names[int(tf_idf_num)-1])
                    if vectorizer_feature_names[int(tf_idf_num)-1]== question_word[j]:
                        print(question_word2vec)
                        question_word2vec=question_word2vec+model[question_word[j]]*tf_idf_value
                        print(question_word2vec)
        question_word2vec=question_word2vec/match
        f8.write(str(number[i])+' ')
        for j in range(0,1772):
            if tfidf_value[i,j]!=0:
                s=str(tfidf_value[i,j])
                n=str(j+1)
                f8.write(n+':'+s+' ')  
        f8.write('1773:')
        s1=str(beginWithDoOrBeTag[i])
        f8.write(s1+' ')
        f8.write('1774:')
        s2=str(containTokenOrTag[i])
        f8.write(s2+' ')
        for k in range(0,60):
            f8.write(str(k+1774+1)+':'+str(question_word2vec[k])+' ')
        f8.write('\n')
    
    f6.close()
    f5.close()
    f8.close()
    #f3.close()
		#if questionType=='yesno':
            
        #print('choiceType is',choiceType)
        #print('questionType is',questionType)
        #print(res['questions'][i]['body'])
        #print(res['questions'][i]['ideal_answer'])
    #print(clean_train_questions)
    # train_vocab_dictionary = vectorizer.fit(clean_train_questions)
    # train_question_features = vectorizer.fit_transform(clean_train_questions)
    # print(train_question_features.shape)
    # tfidf_value=np.zeros((654+52,1811))
    # #train_question_features = train_question_features.toarray()train_question_features.sum(axis=i)
    # wordsCountInthisQuery=np.zeros(654+52)
    # thisWordAppearInQuerysCount=np.zeros(1811)
    # for i in range(0,654+52):
        # wordsCountInthisQuery[i]=train_question_features[i,0:1811].sum()
    # for j in range(0,1811):
        # thisWordAppearInQuerysCount[j]=train_question_features[0:700,j].sum()
    # for i in range(0,654+52):
        # for j in range(0,1811):
            # tfidf_value[i,j]=(train_question_features[i,j]/wordsCountInthisQuery[i])*np.log(700/thisWordAppearInQuerysCount[j])
    # #print(train_question_features)
    # print(tfidf_value)
    # f=open('features4b_testset4.txt','r+')
    # for i in range(0,654):
        # s0=str(number[i])
        # f.write(s0+' ')
        # for j in range(0,1811):
            # if tfidf_value[i,j]!=0:
                # s=str(tfidf_value[i,j])
                # n=str(j+1)
                # f.write(n+':'+s+' ')
            # # s=str(tfidf_value[i,j])
            # # f.write(s+' ')
        # f.write('1812:')
        # s1=str(beginWithDoOrBeTag[i])
        # f.write(s1+' ')
        # f.write('1813:')
        # s2=str(containTokenOrTag[i])
        # f.write(s2+' ')
        # f.write('\n')
    # f.close()
    # f1=open('phaseB_4b_04_features.txt','r+')
    # for i in range(654,654+52):
        # #s0=str(number[i])
        # #f1.write(' 1:')
        # for j in range(0,1811):
            # if tfidf_value[i,j]!=0:
                # s=str(tfidf_value[i,j])
                # n=str(j+1)
                # f1.write(n+':'+s+' ')
        # f1.write('1812:')
        # s1=str(beginWithDoOrBeTag[i])
        # f1.write(s1+' ')
        # f1.write('1813:')
        # s2=str(containTokenOrTag[i])
        # f1.write(s2+' ')
        # f1.write('\n')
    # f1.close()
    # print(tfidf_value)
	
    # #train_question_np_features = np.array(train_question_features)
    # vocab = vectorizer.get_feature_names()
    # #print (vocab)
    # print (train_question_features.shape)
    #dist = np.sum(train_question_features, axis=0)
    #for tag, count in zip(vocab, dist):
       #print(count, tag)
    #print(res['db']['ip'])
    #print(res['db']['port'])
    jsf.close()
 
if __name__ == "__main__":
    jx_json()