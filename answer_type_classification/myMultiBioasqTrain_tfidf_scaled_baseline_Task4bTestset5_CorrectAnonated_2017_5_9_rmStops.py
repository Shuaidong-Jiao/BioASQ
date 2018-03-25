import os
import sys

os.chdir('C:\libsvm-3.21\python')
from svmutil import *
# f2=open('classLabelNum.txt','r+')

# for line in f2:
   # lineContent=line.split()
   # className=str(lineContent[0])
   # f1=open(className+'.txt','r+')

y,x= svm_read_problem('features4b_testset5_basicTfidf_newest_2017_5_9_rmStops_corrected_ano_TrainingsSetCorrected.txt')
m=svm_train(y[:654],x[:654], '-t 0 -b 1 -c 10')      #核函数  -v 5 -c 10 -c 8
#svm_save_model('BiggerThan2_model.txt',m)

#m1=svm_load_model('BiggerThan2_model.txt')

y,x, = svm_read_problem('phaseB_4b_05_features_basicTfidf_newest_2017_5_9_rmStops_corrected_ano_CorrectAnotated.txt')#test-scale.txt
p_label, p_acc, p_val=svm_predict(y[0:],x[0:],m, '-b 1')
print(p_label)
print(p_acc)
f=open('4b_testset5_Tfidf_baseline_predictedQuestionLabel_2017_5_9_rmStops.txt','r+')
for i in range(0,len(p_label)):
    f.write(str(p_label[i])+' \n')
f.close()
#print(p_val)
svm_save_model('BioASQ_Task4b_testset5_Tfidf_scaled_c_10_2017_5_9_model_rmStops.txt',m)