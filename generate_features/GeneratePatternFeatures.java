package myBioASQ;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;  
import java.io.FileReader;  
import java.io.FileWriter;  
import java.io.IOException;  
import java.io.PrintWriter;
import java.util.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.commons.collections.map.ListOrderedMap;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.ParseException;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;


import com.clearnlp.component.AbstractComponent;
import com.clearnlp.dependency.DEPTree;
import com.clearnlp.nlp.NLPGetter;
import com.clearnlp.nlp.NLPMode;
import com.clearnlp.reader.AbstractReader;
import com.clearnlp.segmentation.AbstractSegmenter;
import com.clearnlp.tokenization.AbstractTokenizer;
import com.clearnlp.util.UTInput;
import com.clearnlp.util.UTOutput;

import com.aliasi.chunk.CharLmHmmChunker;  
import com.aliasi.chunk.Chunk;  
import com.aliasi.chunk.Chunker;  
import com.aliasi.chunk.Chunking;
import com.aliasi.chunk.ConfidenceChunker;
import com.aliasi.corpus.Parser;  
import com.aliasi.dict.DictionaryEntry;  
import com.aliasi.dict.MapDictionary;  
import com.aliasi.dict.ExactDictionaryChunker;  
import com.aliasi.hmm.HmmCharLmEstimator;  
import com.aliasi.sentences.IndoEuropeanSentenceModel;  
import com.aliasi.sentences.SentenceModel;  
import com.aliasi.tokenizer.IndoEuropeanTokenizerFactory;  
import com.aliasi.tokenizer.Tokenizer;  
import com.aliasi.tokenizer.TokenizerFactory;  
import com.aliasi.util.AbstractExternalizable;
import com.aliasi.util.Strings;

import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;  
//import org.apache.http.ParseException;  
import org.apache.http.client.ClientProtocolException;  
import org.apache.http.client.HttpClient;  
import org.apache.http.client.methods.HttpGet;  
import org.apache.http.client.methods.HttpPost;  
import org.apache.http.impl.client.HttpClientBuilder;  
import org.apache.http.util.EntityUtils;  
import org.json.JSONException;  
import org.json.JSONObject;  

public class GeneratePatternFeatures {
public static void main(String[] args) {
		
		String fullFileName = "E:/BioASQ_question_classfication/BioASQ-trainingDataset_1b-4b.json";  //要读取的JSON文件的路径
		//File file = new File(fullFileName);
        File testset = new File(fullFileName);
        FileWriter outOne = null;
        BufferedWriter outTwo = null;
        
        FileReader inOne = null;
        BufferedReader inTwo = null;
        File writePatternFeatures = new File("E:/BioASQ_question_classfication/BioASQ-1b-4bTraining_PatternFeatures_addPatterns_test.txt");
        
        File readPredictedLabelNum = new File("C:/libsvm-3.21/python/4b_testset1_predictedQuestionLabel.txt");
        try {
			outOne = new FileWriter(writePatternFeatures);
			outTwo= new BufferedWriter(outOne);
			
		} catch (IOException e3) {
			// TODO Auto-generated catch block
			e3.printStackTrace();
		}
        try {
			inOne= new FileReader(readPredictedLabelNum);
			inTwo= new BufferedReader(inOne);
		} catch (FileNotFoundException e3) {
			// TODO Auto-generated catch block
			e3.printStackTrace();
		}
        
        JSONObject jsonObjQuery;
		try {
			jsonObjQuery = new JSONObject(new JSONTokener(new FileReader(testset)));
			JSONArray jsonArrQues = jsonObjQuery.getJSONArray("questions");
			JSONArray jsonArrAnswerQues =new JSONArray();
	        for (int i = 0; i <jsonArrQues.length(); i++) {//jsonArrQues.length()  
				JSONObject jsonObjQues = jsonArrQues.getJSONObject(i);
				JSONObject annotatedJson;
				
				
			
				String id = jsonObjQues.getString("id").trim();
				String questionBody=jsonObjQues.getString("body");
				//String questionType=jsonObjQues.getString("type");  //得到问题类型
				String body=null;
				try {
					body = URLEncoder.encode(jsonObjQues.getString("body"), "utf-8");
				} catch (UnsupportedEncodingException e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				}
				char[] quesBody=questionBody.toCharArray();
				String inputFile  = "media.txt";
				File inQuesFile=new File(inputFile);
				FileWriter out;
				try {
					out = new FileWriter(inQuesFile);
					out.write(quesBody,0,quesBody.length);
					out.close();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
				String questionType=jsonObjQues.getString("type");//得到问题类型
				
				int classNum=0;
				System.out.println(questionBody+"is "+questionType);
				LinkedList<String> patternFeatures = new LinkedList<String>();  //存放各个patternfeature 的值
				int index = 0;  //pattern feature's index
				if(questionType.equals("factoid")||questionType.equals("list")){
					String regex="[\\s\\p{Punct}]+";
					String [] words=questionBody.split(regex);
					String semanticType = null;
					
						if ((words[0].equals("Which")||words[0].equals("List")||words[0].equals("What"))&&(words[1].equals("gene")||words[1].equals("genes"))) {
							patternFeatures.add(String.valueOf(1));							
						}
						else {
							patternFeatures.add(String.valueOf(0));
						}
						index++;
						if(words[0].equals("Which")&&words[1].equals("mutated")&&(words[2].equals("genes")||words[2].equals("gene"))){
							patternFeatures.add(String.valueOf(1));							
						}
						else {
							patternFeatures.add(String.valueOf(0));
						}
						index++;
						if(words[0].equals("Which")&&words[1].equals("class")&&(words[3].equals("genes")||words[3].equals("gene"))){
							patternFeatures.add(String.valueOf(1));							
						}
						else {
							patternFeatures.add(String.valueOf(0));
						}
						index++;
						if ((words[0].equals("Which")||words[0].equals("List")||words[0].equals("What"))&&(words[1].equals("symptom")||words[1].equals("symptoms"))) {
							patternFeatures.add(String.valueOf(1));							
						}
						else {
							patternFeatures.add(String.valueOf(0));
						}
						if(words[0].equals("Which")&&words[1].equals("bacterium")&&words[2].equals("has")){
							patternFeatures.add(String.valueOf(1));							
						}else{
							patternFeatures.add(String.valueOf(0));
						}
						index++;
						if(words[0].equals("Which")&&words[1].equals("cellular")&&words[2].equals("function")){
							patternFeatures.add(String.valueOf(1));							
						}
						else{
							patternFeatures.add(String.valueOf(0));
						}
						index++;
						if(words[0].equals("Which")&&words[1].equals("is")&&words[3].equals("major")&&words[4].equals("RNA")&&words[5].equals("editing")&&words[6].equals("enzyme")){
							patternFeatures.add(String.valueOf(1));							
						}
						else{
							patternFeatures.add(String.valueOf(0));
						}
						index++;
						if ((words[0].equals("Which")||words[0].equals("What")||words[0].equals("List"))&&(words[1].equals("protein")||words[1].equals("proteins"))) {
							patternFeatures.add(String.valueOf(1));							
						} 
						else{
							patternFeatures.add(String.valueOf(0));
						}
						index++;
						if (words[0].equals("Which")&&words[1].equals("ApoE")&& words[2].equals("isoform")) {
							patternFeatures.add(String.valueOf(1));							
						} 
						else{
							patternFeatures.add(String.valueOf(0));
						}
						index++;
						if (words[0].equals("Which")&&words[1].equals("clotting")&&words[2].equals("factor")) {
							patternFeatures.add(String.valueOf(1));							
						}
						else{
							patternFeatures.add(String.valueOf(0));
						}
						index++;
//						if (words[words.length-4].equals("what")&&words[words.length-3].equals("is")&&words[words.length-1].equals("disease")) {
//							semanticType = "T047";
//							
//						}
//						else if (words[words.length-3].equals("clotting")&&words[words.length-2].equals("factor")&&words[words.length-1].equals("inhibitors")) {
//							semanticType = "T116";
//							
//						}
						if (words[words.length-3].equals("for")&&words[words.length-2].equals("which")&&words[words.length-1].equals("disease")) {
							patternFeatures.add(String.valueOf(1));							
						}
						else{
							patternFeatures.add(String.valueOf(0));
						}
						index++;
						if (words[0].equals("Which")&&words[1].equals("NADPH")&&words[2].equals("oxidase")) {
							patternFeatures.add(String.valueOf(1));//,T116							
						}
						else{
							patternFeatures.add(String.valueOf(0));
						}
						index++;
						if (words[0].equals("Which")&&words[1].equals("fusion")&&words[2].equals("protein")) {
							patternFeatures.add(String.valueOf(1));							
						}
						else{
							patternFeatures.add(String.valueOf(0));
						}
						index++;
						if (words[0].equals("Which")&&(words[1].equals("peptide")||words[1].equals("peptides"))) {
							patternFeatures.add(String.valueOf(1));						
						} 
						else{
							patternFeatures.add(String.valueOf(0));
						}
						index++;
						if (words[0].equals("Which")&&(words[1].equals("miRNA")||words[1].equals("miRNAs"))) {
							patternFeatures.add(String.valueOf(1));
						} 
						else{
							patternFeatures.add(String.valueOf(0));
						}
						index++;
						if (words[0].equals("Which")&&(words[1].equals("cells")||words[1].equals("cell"))) {
							patternFeatures.add(String.valueOf(1));
						} 
						else{
							patternFeatures.add(String.valueOf(0));
						}
						index++;
						if (words[0].equals("Which")&&(words[1].equals("antibody")||words[1].equals("antibodies"))) {
							patternFeatures.add(String.valueOf(1));							
						} 
						else{
							patternFeatures.add(String.valueOf(0));
						}
						index++;
						if (words[0].equals("Which")&&(words[1].equals("technique")||words[1].equals("techniques"))) {
							patternFeatures.add(String.valueOf(1));	
						} 
						else{
							patternFeatures.add(String.valueOf(0));	
						}
						index++;
						if (words[0].equals("Which")&&words[1].equals("is")&&words[3].equals("protein")) {
							patternFeatures.add(String.valueOf(1));	
						}
						else{
							patternFeatures.add(String.valueOf(0));	
						}
						index++;
						if (words[0].equals("Which")&&words[1].equals("is")&&words[3].equals("primary")&&words[4].equals("protein")) {
							patternFeatures.add(String.valueOf(1));	
						}
						/*else if (words[1].equals("molecule")&&words[5].equals("Daratumumab")) {
							semanticType = "T116";
							
						}*/
						else{
							patternFeatures.add(String.valueOf(0));	
						}
						index++;	
						if (words[0].equals("Which")&&words[1].equals("transcription")&&(words[2].equals("factor")||words[2].equals("factors"))) {
							patternFeatures.add(String.valueOf(1));	
						}
						else{
							patternFeatures.add(String.valueOf(0));	
						}
						index++;
						if (words[0].equals("Which")&&words[1].equals("is")&&words[3].equals("target")&&words[4].equals("protein")) {
							patternFeatures.add(String.valueOf(1));	
						}
						/*else if (words[1].equals("is")&&words[4].equals("abundant")&&words[6].equals("protein")) {
							semanticType = "T116";
							
						}*/
						else{
							patternFeatures.add(String.valueOf(0));	
						}
						index++;				
						if (words[0].equals("Which")&&(words[1].equals("molecule")||words[1].equals("molecules"))&&words[3].equals("targeted")) {//&&words[7].equals("Gevokizumab")
							patternFeatures.add(String.valueOf(1));								
						}
						else{
							patternFeatures.add(String.valueOf(0));	
						}
						index++;
						if ((words[0].equals("Which")||words[0].equals("What")||words[0].equals("List"))&&(words[1].equals("disease")||words[1].equals("diseases")||words[1].equals("syndrome")||words[1].equals("syndromes")||words[1].equals("disorder")||words[1].equals("disorders"))) {
							patternFeatures.add(String.valueOf(1));							
						} 
						else{
							patternFeatures.add(String.valueOf(0));
						}
						index++;
						if(words[0].equals("Which")&&words[1].equals("is") && words[2].equals("the")&& words[3].equals("main")&& words[4].equals("abnormality")){
							patternFeatures.add(String.valueOf(1));
						}
						else{
							patternFeatures.add(String.valueOf(0));
						}
						index++;
						if(words[0].equals("Which")&&words[1].equals("human") && (words[2].equals("disease")||words[2].equals("diseases")||words[2].equals("syndrome")||words[2].equals("syndromes"))){
							patternFeatures.add(String.valueOf(1));
						}
						else{
							patternFeatures.add(String.valueOf(0));
						}
						index++;
						if (words[0].equals("Which")&&(words[1].equals("disorder")||words[1].equals("disorders"))) {
							patternFeatures.add(String.valueOf(1));
						}
						else{
							patternFeatures.add(String.valueOf(0));
						}
						index++;
						
						if (words[0].equals("Which")&&(words[1].equals("enzyme")||words[1].equals("enzymes"))&& !words[3].equals("MLN4924")) {
							patternFeatures.add(String.valueOf(1));
						}
						else{
							patternFeatures.add(String.valueOf(0));
						}
						index++;
						if (words[0].equals("Which")&&words[1].equals("mutated")&&(words[2].equals("enzyme")||words[2].equals("enzymes"))) {
							patternFeatures.add(String.valueOf(1));							
						}
						else{
							patternFeatures.add(String.valueOf(0));	
						}
						index++;
						
						if ((words[0].equals("Which")||words[0].equals("What")||words[0].equals("List"))&&(words[1].equals("drug")||words[1].equals("drugs"))) {
							patternFeatures.add(String.valueOf(1));
						} 
						else{
							patternFeatures.add(String.valueOf(0));
						}
						index++;					
						if (words[0].equals("Which")&&words[2].equals("drugs")) {
							patternFeatures.add(String.valueOf(1));
						}
						else{
							patternFeatures.add(String.valueOf(0));
						}
						if (words[0].equals("Which")&&words[1].equals("oncogenes")) {
							patternFeatures.add(String.valueOf(1));
						}
						else{
							patternFeatures.add(String.valueOf(0));
						}
						if (words[0].equals("What")&&words[2].equals("kinase")) {
							patternFeatures.add(String.valueOf(1));
						}
						else{
							patternFeatures.add(String.valueOf(0));
						}
						index++;
						if ((words[0].equals("Which")||words[0].equals("What"))&&(words[1].equals("virus")||words[1].equals("viruses"))) {
							patternFeatures.add(String.valueOf(1));
						} 						
						else{
							patternFeatures.add(String.valueOf(0));
						}
						index++;
						if ((words[0].equals("Which")||words[0].equals("What"))&&(words[1].equals("receptor")||words[1].equals("receptors"))) {
							patternFeatures.add(String.valueOf(1));
						} 
						else{
							patternFeatures.add(String.valueOf(0));
						}
						index++;
						if (words[0].equals("Which")&&words[1].equals("is")&&words[3].equals("receptor")) {
							patternFeatures.add(String.valueOf(1));
						} 
						else{
							patternFeatures.add(String.valueOf(0));
						}
						if (words[0].equals("What")&&words[1].equals("types")&&words[4].equals("mutations")) {
							patternFeatures.add(String.valueOf(1));
						} 
						else{
							patternFeatures.add(String.valueOf(0));
						}				
						if (words[0].equals("Which")&&words[1].equals("type")&&words[3].equals("genes")) {
							patternFeatures.add(String.valueOf(1));
						} 
						else{
							patternFeatures.add(String.valueOf(0));
						}
						
						if (words[0].equals("Which")&&words[1].equals("type")&&words[3].equals("GTPases")) {
							patternFeatures.add(String.valueOf(1));
						} 
						else{
							patternFeatures.add(String.valueOf(0));
						}
						
						if (words[0].equals("Which")&&words[1].equals("type")&&words[3].equals("myeloma")) {
							patternFeatures.add(String.valueOf(1));
						} 
						else{
							patternFeatures.add(String.valueOf(0));
						}
						
						if (words[0].equals("Which")&&words[1].equals("is")&&words[3].equals("target")) {
							patternFeatures.add(String.valueOf(1));
						}
						else{
							patternFeatures.add(String.valueOf(0));
						}
						index++;
						if (words[0].equals("Which")&&words[1].equals("signalling")&&words[2].equals("pathway")) {
							patternFeatures.add(String.valueOf(1));
						}
						else {
							patternFeatures.add(String.valueOf(0));
						}
						if (words[0].equals("Which")&&words[1].equals("species") && words[3].equals("bacteria")) {
							patternFeatures.add(String.valueOf(1));
						}
						else{
							patternFeatures.add(String.valueOf(0));
						}
						index++;
						if (words[0].equals("Which")&&words[3].equals("bonding") && words[4].equals("hormone")) {
							patternFeatures.add(String.valueOf(1));
						}
						else{
							patternFeatures.add(String.valueOf(0));
						}
						index++;
						if (words[0].equals("Which")&&words[1].equals("hormone") && words[2].equals("abnormalities")) {
							patternFeatures.add(String.valueOf(1));
						}
						else{
							patternFeatures.add(String.valueOf(0));
						}
						index++;
						
						if (words[0].equals("What")&&words[1].equals("is") && words[2].equals("the") && words[3].equals("function") ) {
							patternFeatures.add(String.valueOf(1));
							
						} 
						else{
							patternFeatures.add(String.valueOf(0));
						}
						index++;
						if (words[0].equals("What")&&words[1].equals("is") && words[2].equals("the") && words[3].equals("role")
								&& words[4].equals("of")) {
							patternFeatures.add(String.valueOf(1));
							
						} 
						else{
							patternFeatures.add(String.valueOf(0));
						}
						index++;
						/*else if(words[1].equals("is") && words[2].equals("the") && words[3].equals("function") ) {
							semanticType = "T038,T039,T040,T042,T044,T045,T043";
							
						}*/
						if (words[0].equals("What")&&words[1].equals("is") && words[2].equals("the") && words[3].equals("indication")) {
							patternFeatures.add(String.valueOf(1));
							
						} 
						else{
							patternFeatures.add(String.valueOf(0));
						}
						index++;
						if (words[0].equals("What")&&words[1].equals("is") && words[2].equals("the") && words[3].equals("target")) {
							patternFeatures.add(String.valueOf(1));
							
						} 
						else{
							patternFeatures.add(String.valueOf(0));
						}
						index++;
						if (words[0].equals("What")&&words[1].equals("organism") && words[2].equals("causes") ) {
							patternFeatures.add(String.valueOf(1));
							
						}
						else{
							patternFeatures.add(String.valueOf(0));
						}
						index++;
					    if (words[0].equals("What")&& words[1].equals("tissue") && words[2].equals("is") ) {
					    	patternFeatures.add(String.valueOf(1));
							
						}
						else{
							patternFeatures.add(String.valueOf(0));
						}
					    index++;
						
						
						if (words[0].equals("What")&& words[1].equals("is") && words[2].equals("the") && words[3].equals("drug")&& words[4].equals("target")) {
							patternFeatures.add(String.valueOf(1));						
						} 
						else{
							patternFeatures.add(String.valueOf(0));
						}
						index++;
						if (words[0].equals("What")&&words[1].equals("is") && words[2].equals("the") && words[3].equals("main")
								&& words[4].equals("symptom")) {
							patternFeatures.add(String.valueOf(1));	
							
						} 
						else{
							patternFeatures.add(String.valueOf(0));	
						}
						index++;
						if (words[0].equals("What")&& (words[1].equals("gene")||words[1].equals("genes"))) {
							patternFeatures.add(String.valueOf(1));	
							
						} 				
						else{
							patternFeatures.add(String.valueOf(0));	
						}
						index++;
						if (words[0].equals("What")&& words[1].equals("is") && words[2].equals("the") && words[3].equals("molecular")
								&& words[4].equals("function")) {
							patternFeatures.add(String.valueOf(1));	
							
						}
						else{
							patternFeatures.add(String.valueOf(0));	
						}
						if (words[0].equals("List")&& words[1].equals("the") && words[2].equals("components")) {
							patternFeatures.add(String.valueOf(1));	
							
						}
						else{
							patternFeatures.add(String.valueOf(0));	
						}
						if (words[0].equals("Which")&& words[1].equals("type") && words[2].equals("of")&& words[3].equals("myeloma")) {
							patternFeatures.add(String.valueOf(1));	
							
						}
						else{
							patternFeatures.add(String.valueOf(0));	
						}
						if (words[0].equals("Inhibition")&& words[1].equals("of") && words[2].equals("which")&& words[3].equals("enzyme")) {
							patternFeatures.add(String.valueOf(1));	
							
						}
						else{
							patternFeatures.add(String.valueOf(0));	
						}
						index++;
						/*else if (words[1].equals("is")&&words[3].equals("main")&&words[4].equals("component")&&words[7].equals("Lewy")) {
							semanticType = "T023,T026";
							
						}*/
						/*else if (words[1].equals("is")&& !words[2].equals("MRSA")&& words[3].equals("enzymatic")&& words[4].equals("activity")) {
							semanticType = "T052";
							
						}*/
						/*else if (words[1].equals("is")&& words[2].equals("targeted")&& words[4].equals("Palbociclib")) {
							semanticType = "T126,T116";
							
						}*/
						if (words[0].equals("What")&& (words[1].equals("enzyme")||words[1].equals("enzymes"))) {
							patternFeatures.add(String.valueOf(1));
							
						}
						else{
							patternFeatures.add(String.valueOf(0));
						}
						index++;	
						if (words[0].equals("What")&& words[1].equals("kind")&& words[2].equals("of")&& words[3].equals("enzyme")) {
							patternFeatures.add(String.valueOf(1));
							
						}
						else{
							patternFeatures.add(String.valueOf(0));
						}
					
						if (words[0].equals("What")&& words[1].equals("is") && words[2].equals("the") && words[3].equals("function") ) { //&& words[6].equals("spliceosome")
							patternFeatures.add(String.valueOf(1));
							
						} 
						else{
							patternFeatures.add(String.valueOf(0));
						}
						if (words[0].equals("What")&& words[1].equals("memory") && (words[2].equals("problems")||words[2].equals("problem"))) {
							patternFeatures.add(String.valueOf(1));
							
						} 
						else{
							patternFeatures.add(String.valueOf(0));
						}
						if (words[0].equals("What")&& words[1].equals("is") && words[2].equals("the") && words[3].equals("mode")
								&& words[4].equals("of")&& words[4].equals("inheritance")) {
							patternFeatures.add(String.valueOf(1));
							
						} 
						else {
							patternFeatures.add(String.valueOf(0));
						}
						/* else if (words[1].equals("is") && words[2].equals("the") && words[3].equals("indication")) {
							semanticType = "T184";
							
						} */
						if (words[1].equals("is") && words[2].equals("the") && words[3].equals("number")) {
							patternFeatures.add(String.valueOf(1));
							
						}
						else{
							patternFeatures.add(String.valueOf(0));
						}
						if (words[1].equals("is") && words[2].equals("the") && words[3].equals("incidence")) {
							patternFeatures.add(String.valueOf(1));
							
						}
						else{
							patternFeatures.add(String.valueOf(0));
						}
						if ((words[0].equals("Mutation")||words[0].equals("Mutations"))&&words[2].equals("which")&&words[3].equals("gene")) {
							patternFeatures.add(String.valueOf(1));
							
						}
						else{
							patternFeatures.add(String.valueOf(0));
						}
						if(words[0].equals("To") &&words[1].equals("which") && words[2].equals("disease")){
							patternFeatures.add(String.valueOf(1));
							
						}
						else {
							patternFeatures.add(String.valueOf(0));
						}
						if(words[0].equals("Symptoms") &&words[1].equals("of") && words[2].equals("which")&& words[2].equals("disorder")){
							patternFeatures.add(String.valueOf(1));
							
						}
						else{
							patternFeatures.add(String.valueOf(0));
						}
						if (words[0].equals("Where")) {
							patternFeatures.add(String.valueOf(1));
							
						}
						else {
							patternFeatures.add(String.valueOf(0));
						}
						
						if (words[words.length-2].equals("which")&&words[words.length-1].equals("disorder")) {
							patternFeatures.add(String.valueOf(1));
						}
						else {
							patternFeatures.add(String.valueOf(0));
						}
						if (words[words.length-2].equals("which")&&(words[words.length-1].equals("cancers")||words[words.length-1].equals("cancer"))) {
							patternFeatures.add(String.valueOf(1));
								
						}
						else{
							patternFeatures.add(String.valueOf(0));
						}
						if (words[0].equals("Name")&&words[1].equals("monoclonal")&&words[2].equals("antibody")) {
							patternFeatures.add(String.valueOf(1));
								
						}
						else{
							patternFeatures.add(String.valueOf(0));
						}
						if (words[0].equals("Is") || words[0].equals("Are") || words[0].equals("Do")  //对choice问题
								|| words[0].equals("Does")) {
							int exitOr = 0;
							for (int c = 0; c < words.length; c++) {
								if (words[c].equals("or")) {
									exitOr =1;

								}
							}
							if(exitOr ==1){
								patternFeatures.add(String.valueOf(1));
							}
							else{
								patternFeatures.add(String.valueOf(0));
							}
						} 
						if (words[0].equals("How")&&words[0].equals("many")){
							semanticType = "T081";
							patternFeatures.add(String.valueOf(1));
							
						}
						else{
							patternFeatures.add(String.valueOf(0));
						}
						if(words[0].equals("Which")&&words[1].equals("are")&&words[2].equals("the")&&(words[3].equals("typical")||words[3].equals("clinical"))&&words[4].equals("symptoms")){
							patternFeatures.add(String.valueOf(1));					
						}
						else{
							patternFeatures.add(String.valueOf(0));
						}
						if(words[0].equals("Which")&&(words[1].equals("RNA")||words[1].equals("RNAs"))){
							patternFeatures.add(String.valueOf(1));							
						}
						else {
							patternFeatures.add(String.valueOf(0));
						}
						if(words[0].equals("Which")&& words[1].equals("genetic")&&words[2].equals("defects")){
							patternFeatures.add(String.valueOf(1));						
						}
						else{
							patternFeatures.add(String.valueOf(0));
						}
						if(words[0].equals("Which")&& words[1].equals("are")&&words[2].equals("the")&&words[4].equals("clinical")&&words[5].equals("characteristics")){
							patternFeatures.add(String.valueOf(1));						
						}
						else{
							patternFeatures.add(String.valueOf(0));
						}
						if(words[0].equals("Which")&& words[1].equals("are")&&words[2].equals("the")&&words[3].equals("drugs")){
							patternFeatures.add(String.valueOf(1));								
						}	
						else{
							patternFeatures.add(String.valueOf(0));	
						}
						if(words[0].equals("Which")&&words[1].equals("receptors")){
							patternFeatures.add(String.valueOf(1));	
							
						}
						else{
							patternFeatures.add(String.valueOf(0));	
						}
						
						if(words[0].equals("List")&&words[1].equals("bacteria")&&words[2].equals("that")){
							patternFeatures.add(String.valueOf(1));	
							
						}
						else{
							patternFeatures.add(String.valueOf(0));	
						}
						if(words[0].equals("List")&&words[2].equals("classical")&&words[3].equals("symptoms")){
							patternFeatures.add(String.valueOf(1));								
						}
						else{
							patternFeatures.add(String.valueOf(0));	
						}
						if(words[0].equals("List")&&words[1].equals("functions")){
							patternFeatures.add(String.valueOf(1));								
						}
						else{
							patternFeatures.add(String.valueOf(0));	
						}
						if(words[0].equals("List")&&words[1].equals("the")&&words[2].equals("diseases")){
							patternFeatures.add(String.valueOf(1));						
						}		
						else{
							patternFeatures.add(String.valueOf(0));
						}
						if(words[0].equals("Please")&&words[1].equals("List")&&words[3].equals("diseases")){
							patternFeatures.add(String.valueOf(1));							
						}
						else{
							patternFeatures.add(String.valueOf(0));
						}
						if(words[0].equals("List")&&words[1].equals("variants")&&words[5].equals("gene")){
							patternFeatures.add(String.valueOf(1));							
						}
						else{
							patternFeatures.add(String.valueOf(0));
						}
						if(words[0].equals("List")&&words[1].equals("human")&&words[3].equals("proteins")){
							patternFeatures.add(String.valueOf(1));						
						}
						else{
							patternFeatures.add(String.valueOf(0));
						}
						if(words[0].equals("List")&&words[1].equals("symptoms")&&words[2].equals("of")){
							patternFeatures.add(String.valueOf(1));								
						}
						else{
							patternFeatures.add(String.valueOf(0));	
						}
						if(words[0].equals("List")&&words[1].equals("features")&&words[2].equals("of")){
							patternFeatures.add(String.valueOf(1));								
						}
						else{
							patternFeatures.add(String.valueOf(0));	
						}
						if(words[0].equals("List")&&words[1].equals("autoimmune")&&words[2].equals("disorders")){
							patternFeatures.add(String.valueOf(1));							
						}
						else{
							patternFeatures.add(String.valueOf(0));
						}
//						if(words[0].equals("List")&&words[3].equals("Syndrome")){
//							patternFeatures.add(String.valueOf(1));						
//						}
//						else{
//							patternFeatures.add(String.valueOf(0));
//						}
						if(words[0].equals("List")&&words[2].equals("indications")){
							patternFeatures.add(String.valueOf(1));								
						}
						else{
							patternFeatures.add(String.valueOf(0));	
						}
						if (words[0].equals("What")&&words[1].equals("are") && words[2].equals("the") && words[3].equals("indications")) {
							patternFeatures.add(String.valueOf(1));								
						} 
						else{
							patternFeatures.add(String.valueOf(0));	
						}
						if (words[0].equals("What")&&words[1].equals("are") && words[2].equals("the") && words[3].equals("symptoms")) {
							patternFeatures.add(String.valueOf(1));	
						}
						else{
							patternFeatures.add(String.valueOf(0));	
						}
						if (words[0].equals("What")&&words[1].equals("are") && words[2].equals("the") && words[3].equals("roles")&& words[4].equals("of")) {
							patternFeatures.add(String.valueOf(1));							
						}
						else{
							patternFeatures.add(String.valueOf(0));
						}
						if (words[0].equals("What")&& words[1].equals("hand") && words[2].equals("deformities")){
							patternFeatures.add(String.valueOf(1));							
					    }
						else{
							patternFeatures.add(String.valueOf(0));	
						}
						if(words[0].equals("for") && words[1].equals("what") && (words[4].equals("indications")||words[4].equals("indications"))){
							patternFeatures.add(String.valueOf(1));	
						}
						else{
							patternFeatures.add(String.valueOf(0));	
						}
						
						
						
						System.out.println("patternFeatures.size() is "+String.valueOf(patternFeatures.size()));
						for(int j=0;j<patternFeatures.size();j++){
							try {
								outTwo.write(" "+patternFeatures.get(j));
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
						try {
							outTwo.newLine();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
				}
				
	        }
	        
	        try {
	        	//System.out.println("come on!!");
				outTwo.close();
				outOne.close();
				inTwo.close();
				inOne.close();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		catch (JSONException e) {
		  // TODO Auto-generated catch block
		  //e.printStackTrace();
		} catch (FileNotFoundException e) {
		  // TODO Auto-generated catch block
			e.printStackTrace();
		}
}

}
