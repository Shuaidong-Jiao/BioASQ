package myBioASQ;
import java.util.Collection;
import java.util.List;
import java.util.*;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringReader;

import edu.stanford.nlp.process.Tokenizer;
import edu.stanford.nlp.process.TokenizerFactory;
import edu.stanford.nlp.process.CoreLabelTokenFactory;
import edu.stanford.nlp.process.DocumentPreprocessor;
import edu.stanford.nlp.process.PTBTokenizer;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.ling.HasWord;
import edu.stanford.nlp.ling.SentenceUtils;
import edu.stanford.nlp.trees.*;
import edu.stanford.nlp.parser.lexparser.LexicalizedParser;

public class generateParsingAndChunkFeatures {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String parserModel = "edu/stanford/nlp/models/lexparser/englishPCFG.ser.gz";
		LexicalizedParser lp = LexicalizedParser.loadModel(parserModel);
		String inputQuestions = "E:/BioASQ_question_classfication/BioASQ-trainingDataset_1b-4b.json";  //要读取的文件名
		generateAndOutput(lp,inputQuestions);

	}
	
	public static void generateAndOutput(LexicalizedParser lp, String filename) {
		JSONObject jsonObjQuery = null;
		FileWriter outOne = null;
		BufferedWriter outTwo = null;
		FileWriter outThree = null;
		BufferedWriter outFour = null;
		try {
			//FileReader inOne = new FileReader(inputQuestions);
			//BufferedReader inTwo = new BufferedReader(inOne);
			try {
				outOne = new FileWriter("E:/BioASQ_question_classfication/BioASQ-trainingDataset_1b-4b_ParsingFeatures-all-correct.txt");
				outTwo = new BufferedWriter(outOne);
				outThree = new FileWriter("E:/BioASQ_question_classfication/BioASQ-trainingDataset_1b-4b_ParsingFeatures-dictionary.txt");
				outFour = new BufferedWriter(outThree);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			jsonObjQuery = new JSONObject(new JSONTokener(new FileReader(filename)));   // read json file
			JSONArray jsonArrQues = jsonObjQuery.getJSONArray("questions");//得到question的JSONArray
			int factoidOrListCount =0;
			HashMap<String,String> nsubj_wh_dict = new HashMap<String,String>();//与which或what有nsubj关系的词的词典
			
			HashMap<String,String> det_wh_dict = new HashMap<String,String>();//与which或what有det关系的词的词典
			
			HashMap<String,String> dobj_list_dict = new HashMap<String,String>();//与list或name有dobj关系的词的词典
			
			String regex ="[\\s\\p{Punct}]+"; //used to split 
			float factoidOrListCountQuestionNumber[] = new float[903];
			for(int f=0;f<903;f++){
				 factoidOrListCountQuestionNumber[f] = 0;
			}
			for(int i=0;i<jsonArrQues.length();i++){//jsonArrQues.length()
				JSONObject jsonObjQues = jsonArrQues.getJSONObject(i);
				String questionBody = jsonObjQues.getString("body");
				
				String questionType = jsonObjQues.getString("type");
				if (questionType.equals("factoid")||questionType.equals("list")) {
					int[] nsubj_wh_features = new int[136];//136
					for(int j=0;j<nsubj_wh_features.length;j++){
						nsubj_wh_features[j] = 0;
					}
					int[] det_wh_features = new int[120];
					for(int j=0;j<det_wh_features.length;j++){
						det_wh_features[j] = 0;
					}
					
					int[] dobj_list_features = new int[56];
					for(int j =0;j<dobj_list_features.length;j++){
						dobj_list_features[j] = 0;
					}
					
					System.out.println("question is "+questionBody);
					TokenizerFactory<CoreLabel> tokenizerFactory =
					        PTBTokenizer.factory(new CoreLabelTokenFactory(), "");
					Tokenizer<CoreLabel> tok =
					        tokenizerFactory.getTokenizer(new StringReader(questionBody));
					List<CoreLabel> rawWords2 = tok.tokenize();
					Tree parse = lp.apply(rawWords2);

					TreebankLanguagePack tlp = lp.treebankLanguagePack(); // PennTreebankLanguagePack for English
					GrammaticalStructureFactory gsf = tlp.grammaticalStructureFactory();
					GrammaticalStructure gs = gsf.newGrammaticalStructure(parse);
					List<TypedDependency> tdl = gs.typedDependenciesCCprocessed();
					System.out.println(tdl);
					System.out.println();
					
					Iterator<TypedDependency> iter = tdl.iterator();
					while(iter.hasNext()){
						String wordDependencytuple = iter.next().toString();
						//System.out.println(wordDependencytuple);
						String[] wordAndDependency = wordDependencytuple.split(regex);
						if(wordAndDependency[0].equals("nsubj")){
							if(wordAndDependency[1].equals("What")||wordAndDependency[1].equals("Which")){
								if(!nsubj_wh_dict.containsKey(wordAndDependency[3])){
									String index = String.valueOf(nsubj_wh_dict.size());
									nsubj_wh_dict.put(wordAndDependency[3], index);
									System.out.println("wordAndDependency[3] is "+wordAndDependency[3]+"index is "+index);
									nsubj_wh_features[Integer.parseInt(index)] =1;
								}
								else{
									String index = nsubj_wh_dict.get(wordAndDependency[3]);
									nsubj_wh_features[Integer.parseInt(index)] =1;
								}
								
							}
						}
						if(wordAndDependency[0].equals("det")){
							if(wordAndDependency[3].equals("What")||wordAndDependency[3].equals("Which")){
								if(!det_wh_dict.containsKey(wordAndDependency[1])){   //???3 -> 1
									String index = String.valueOf(det_wh_dict.size());
									det_wh_dict.put(wordAndDependency[1], index);
									System.out.println("wordAndDependency[1] is "+wordAndDependency[1]+"index is "+index);
									det_wh_features[Integer.parseInt(index)] =1;
								}
								else{
									String index = det_wh_dict.get(wordAndDependency[1]);
									det_wh_features[Integer.parseInt(index)] =1;
								}
							}
						}
						if(wordAndDependency[0].equals("dobj")){
							if(wordAndDependency[1].equals("List")||wordAndDependency[1].equals("Name")){
								if(!dobj_list_dict.containsKey(wordAndDependency[3])){
									String index = String.valueOf(dobj_list_dict.size());
									dobj_list_dict.put(wordAndDependency[3], index);
									System.out.println("wordAndDependency[3] is "+wordAndDependency[3]+"index is "+index);
									dobj_list_features[Integer.parseInt(index)] =1;
								}
								else{
									String index = dobj_list_dict.get(wordAndDependency[3]);
									dobj_list_features[Integer.parseInt(index)] =1;
								}
							}
							
						}
					}
					for(int j=0;j<nsubj_wh_features.length;j++){
						try {
							outTwo.write(String.valueOf(nsubj_wh_features[j])+" ");
						} catch (IOException e) {						
							e.printStackTrace();
						}
					}
					
					for(int j=0;j<det_wh_features.length;j++){
						try {
							outTwo.write(String.valueOf(det_wh_features[j])+" ");
						} catch (IOException e) {						
							e.printStackTrace();
						}
					}
					
					for(int j=0;j<dobj_list_features.length;j++){
						try {
							outTwo.write(String.valueOf(dobj_list_features[j])+" ");
						} catch (IOException e) {						
							e.printStackTrace();
						}
					}
					try {
						outTwo.newLine();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				
					
			}
			System.out.println("nsubj_wh_dict size is "+String.valueOf(nsubj_wh_dict.size()));
			System.out.println("det_wh_dict size is "+String.valueOf(det_wh_dict.size()));
			System.out.println("dobj_list_dict size is "+String.valueOf(dobj_list_dict.size()));
			Set<String> nsubj_wh_dict_set  = nsubj_wh_dict.keySet();
			Iterator<String> nsubj_wh_dict_iterator = nsubj_wh_dict_set.iterator();
			while(nsubj_wh_dict_iterator.hasNext()){
				try {
					outFour.write(nsubj_wh_dict_iterator.next()+"\n");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			Set<String> det_wh_dict_set = det_wh_dict.keySet();
			Iterator<String> det_wh_dict_iterator = det_wh_dict_set.iterator();
			while(det_wh_dict_iterator.hasNext()){
				try {
					outFour.write(det_wh_dict_iterator.next()+"\n");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			Set<String> dobj_list_dict_set = dobj_list_dict.keySet();
			Iterator<String> dobj_list_dict_iterator = dobj_list_dict_set.iterator();
			while(dobj_list_dict_iterator.hasNext()){
				try {
					outFour.write(dobj_list_dict_iterator.next()+"\n");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
		
			
			try {
				outTwo.close();
				outOne.close();
				outFour.close();
				outThree.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	
	}

}
