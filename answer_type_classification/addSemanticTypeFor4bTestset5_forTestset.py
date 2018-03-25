import nltk
from nltk import word_tokenize
f=open('test-scale_4btestset5_basicTfidfWithMT2Class1.txt','r+')#features4b_testset4_rm1166_tfidf_1.txt
f1=open('phaseB_4b_05_features_basicTfidf3_addSemanticTypes__CorrectlyDividedByWordsNu_scaled_ano.txt','r+')
f2=open('BioASQ-4b-testset5_0-97_writeSemanticTypeNum_DividedByWordsNum.txt','r+')
result=[]


cause_deal=[582,588,602,619,683,771,879,1241]
chemical_deal=[465,579,615,646,651,965,975,1038,1213,1259,1270,1288]
choice_deal=[38,314,439,803,804,813,934,942,998,1105,1239]
disease_deal=[8,20,28,60,86,124,127,138,141,148,153,159,163,204,205,212,224,231,233,234,248,249,250,259,274,298,350,358,482,491,514,532,550,694,714,715,738,857,958,967,1019,1035,1037,1069,1138,1199,1201,1248,1264,1278]
drug_deal=[5,29,35,36,79,131,349,544,632,672,686,731,829,856,1010,1016,1031,1076,1078,1080,1168,1190,1252,1277]

enzyme_deal=[23,40,41,111,114,188,190,192,220,254,262,287,490,494,499,523,529,648,828,830,862,867,875,922,963,971,1004,1049,1050,1051,1075,1172,1205,1282]
function_deal=[32,103,217,284,291,327,398,477,493,508,520,521,534,562,587,589,626,629,768,876,883,899,1053,1958,1217]
gene_deal=[51,92,107,112,119,155,156,251,255,260,269,302,335,338,340,361,364,386,396,434,478,498,515,571,594,617,634,658,666,684,699,766,786,794,869,894,908,917,923,938,944,959,1065,1071,1098,1115,1129,1130,1167,1195,1247,1274,1296,1297]
inheritance_deal=[2,67,570,681,721,748,780,782,831,841,941,993,995,1028,1057,1094]
location_deal=[39,55,196,322,372,654,678,778,837,952,1017,1018,1211,1221]
mutation_deal=[195,240,286,320,387,391,425,530,538,561,620,621,622,729,783,855,874]
name_deal=[48,82,90,182,331,365,415,584,789,827,877,957,1143,1202]

number_deal=[65,87,140,278,310,311,325,326,373,427,503,569,592,647,698,705,712,767,784,787,796,798,836,949,1034,1066,1068,1081,1132,1162,1180,1227,1228,1235,1281,1307]
protein_deal=[11,14,18,21,27,31,42,43,75,105,106,115,116,117,118,120,134,139,178,180,181,186,188,189,191,193,194,221,226,230,237,241,244,247,257,263,264,275,278,283,341,345,357,376,379,388,406,437,440,441,455,466,479,516,548,553,652,671,693,697,701,754,757,797,814,815,854,878,895,916,919,920,925,926,928,930,931,933,939,966,1043,1073,1109,1110,1169,1210,1223,1257,1258,1261,1267,1302,1304]

receptor_deal=[22,132,174,197,351,375,443,542,600,997,1303]
symptom_deal=[56,154,166,169,173,177,259,289,295,301,317,343,416,502,518,528,536,607,623,628,633,641,657,689,691,692,703,708,740,744,755,769,772,842,962,981,988,1046,1048,1140,1142,1193,1226,1234]

technique_deal=[187,252,306,389,431,448,461,462,525,539,743,761,795,817,884,900,973,984,1044,1061,1118,1157,1177,1301]
tool_deal=[902,907,947,1079,1116,1178,1179,1260,1262]
type_deal=[37,54,159,414,429,454,468,489,491,499,507,554,727,749,1019,1030,1300]


histone_modification_deal=[531,799,838,871,873,936,1023,1155,1240]

RNA_deal=[307,333,433,945,953,1032]
target_deal=[18,359,442,759,811,858]
family_deal=[44,467,1059,1229,1236]
definition_deal=[124,213,710,888,957]


bacterium_deal=[58,410,485,583]
database_type=[422,447,449,450]
component_deal=[43,185,225,292]
trial_deal=[610,1137,1189,1225]
test_teal=[929,1192,1198,1269]
application_deal=[820,823,825,891]
judgement_deal=[107,977,979,980]

sequence_deal=[198,319,444]
situation_deal=[363,625,927]
hormone_deal=[303,598,606,773]
tissue_deal=[580,801,1003]
tumor_deal=[160,394,1280]
relationship_deal=[334,563,682,184,392]

cell_deal=[54,554]
#relationship_deal=[184,392]
signaling_pathway_deal=[401,734]
motif_deal=[687,1083]
#interaction partner_deal=[726,751]
status_deal=[739,1151]
criteria_deal=[45,912,526]
species_deal=[1021,1150]
enzymatic_activity_deal=[288,360]
nucleotide_deal=[775,935]
virus_deal=[996,1121]

# disease_deal=[8,20,28,60,86,124,127,138,141,148,153,159,163,204,205,212,224,231,233,234,248,249,250,259,274,298,350,358,482,491,514,532,550,694,714,715,738,857,958,967,1019,1035,1037,1069,1138,1199,1201,1248,1264,1278]
# protein_deal=[11,14,18,21,27,31,42,43,75,105,106,115,116,117,118,120,134,139,178,180,181,186,188,189,191,193,194,221,226,230,237,241,244,247,257,263,264,275,278,283,341,345,357,376,379,388,406,437,440,441,455,466,479,516,548,553,652,671,693,697,701,754,757,797,814,815,854,878,895,916,919,920,925,926,928,930,931,933,939,966,1043,1073,1109,1110,1169,1210,1223,1257,1258,1261,1267,1302,1304]
# virus_deal=[996,1121]

#testSetQuestionNum=[7,13,0,32,13,5,0,0,26,10,13,13,3,16,5,3,7,13,12,26,4,2,7,13,9,13,6,0,0,6,5,0,13,5,4,13,3,30,26,13,3,13,5,12,15,15]
testSetQuestionNum=[0,13,3,0,13,3,19,37,15,1,0,1,25,13,5,6,25,7,0,25,3,0,0,0,0,0,0,16,0,4,6,25,7,25,0,15,15,9,0,2,10,19,0,13,6,13,6,6,0,24,9,3]
one=1
minusOne=-1
zero=0
i=0
lineCount=0
semanticTypeNum=[]
for line in f2:
   semanticTypeNum.append(line)

for line in f:
   lineContent=line.split()
   allSemanticTypeNum=semanticTypeNum[i].split()
   print(len(allSemanticTypeNum))
   for j in range(0,len(allSemanticTypeNum)):
       lineContent.append(str(1831+j)+":"+str(allSemanticTypeNum[j]))
   #num=float(lineContent[0])
   #lineContent[0]=str(testSetQuestionNum[i])
   lineResult=" ".join(lineContent)
   f1.write(lineResult)
   f1.write('\n')
   i=i+1
#f.write('\n')
s=str(16734)
#f.write(' '+s)
f.close()
f1.close()
print(result)
# if lineCount==0:
      # lineContent[0]=str(one) 
   # elif num in choice_deal:
           # lineContent[0]=str(2) 
   # elif num in disease_deal:
           # lineContent[0]=str(3) 
   # elif num in drug_deal:
           # lineContent[0]=str(4) 
   # elif num in enzyme_deal:
           # lineContent[0]=str(5) 
   # elif num in function_deal:
           # lineContent[0]=str(6)
   # elif num in gene_deal:
           # lineContent[0]=str(7) 
   # elif num in inheritance_deal:
           # lineContent[0]=str(8)
   # elif num in location_deal:
           # lineContent[0]=str(9) 
   # elif num in mutation_deal:
           # lineContent[0]=str(10)
   # elif num in name_deal:
           # lineContent[0]=str(11) 
   # elif num in number_deal:
           # lineContent[0]=str(12)
   # elif num in protein_deal:
           # lineContent[0]=str(13) 
   # elif num in receptor_deal:
           # lineContent[0]=str(14) 
   # elif num in symptom_deal:
           # lineContent[0]=str(15) 
   # elif num in technique_deal:
           # lineContent[0]=str(16)
   # elif num in tool_deal:
           # lineContent[0]=str(17) 
   # elif num in type_deal:
           # lineContent[0]=str(18)
   # elif num in histone_modification_deal:
           # lineContent[0]=str(19) 
   # elif num in cause_deal:
           # lineContent[0]=str(20)
   # elif num in RNA_deal:
           # lineContent[0]=str(21) 
   # elif num in target_deal:
           # lineContent[0]=str(22)
   # elif num in family_deal:
           # lineContent[0]=str(23) 
   # elif num in definition_deal:
           # lineContent[0]=str(24)
   # elif num in bacterium_deal:
           # lineContent[0]=str(25)      #增加在训练集中出现次数大于等于3次，小于5次的问题
   # elif num in database_type:
           # lineContent[0]=str(26)
   # elif num in component_deal:
           # lineContent[0]=str(27) 
   # elif num in trial_deal:
           # lineContent[0]=str(28)
   # elif num in test_teal:
           # lineContent[0]=str(29) 
   # elif num in application_deal:
           # lineContent[0]=str(30)
   # elif num in judgement_deal:
           # lineContent[0]=str(31) 
   # elif num in sequence_deal:
           # lineContent[0]=str(32)
   # elif num in situation_deal:
           # lineContent[0]=str(33) 
   # elif num in hormone_deal:
           # lineContent[0]=str(34)
   # elif num in tissue_deal:
           # lineContent[0]=str(35)      
   # elif num in tumor_deal:
           # lineContent[0]=str(36)
   # elif num in relationship_deal:
           # lineContent[0]=str(37)
   # else:
       # lineContent[0]=str(0)