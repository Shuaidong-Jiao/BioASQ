package myBioASQ;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.LinkedList;

import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

public class DealWithQuestions_addSemanticTypeNums_robust {
	public static void main(String[] args) {
		
		String fullFileName = "E:/BioASQ_question_classfication/BioASQ-trainingDataset_1b-4b.json";
		//File file = new File(fullFileName);
        File testset = new File(fullFileName);
        FileWriter outOne = null;
        BufferedWriter outTwo = null;
        FileWriter outThree = null;
        BufferedWriter outFour = null;
        FileReader inOne = null;
        BufferedReader inTwo = null;
        File writeClassNum = new File("E:/BioASQ-task5bPhaseB-testset1-rule-based_notCompleted_questionLabelNum.txt");//rule-basedClassResult_trainingDataset4b_29.txt
        File writeSemanticTypeNum = new File("E:/BioASQ_question_classfication/BioASQ-trainingDataset_1b-4b_1500-1550_writeSemanticTypeNum.txt");//rule-basedClassResult_trainingDataset4b_29.txt
        File readPredictedLabelNum = new File("E:/4b_testset5_predictedQuestionLabel.txt");
        try {
			outOne = new FileWriter(writeClassNum);
			outTwo= new BufferedWriter(outOne);
			outThree = new FileWriter(writeSemanticTypeNum);
			outFour= new BufferedWriter(outThree);
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
	        for (int i = 1500; i <1550 ; i++) {//jsonArrQues.length()289 548  jsonArrQues.length() 1150  jsonArrQues.length()
				JSONObject jsonObjQues = jsonArrQues.getJSONObject(i);
				JSONObject annotatedJson;
				JSONArray jsonQuesExactAnswer=null;
				
			
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
				boolean isChoiceQuestion=false;
				String answerOfChoiceQuestion=null;
				String anotherAnswerOfChoiceQuestion=null;
				boolean isQuantityQuestion=false;
				String answerOfQuantityQuestion=null;
				boolean isHandQuestion=false;
				boolean isInherModeQuestion=false;
				boolean isRANKLQuestion=false;
				boolean ismTORC1Question=false;
				int classNum=0;
				System.out.println(questionBody+"is "+questionType);
				
				if(questionType.equals("factoid")|| questionType.equals("list")|| questionType.equals("yesno")|| questionType.equals("summary")){   //如果问题类型是factoid或者list || questionType.equals("list")
					isChoiceQuestion=false;
					isQuantityQuestion=false;
					isHandQuestion=false;
					isInherModeQuestion=false;
					isRANKLQuestion=false;
					ismTORC1Question=false;
					
					String regex="[\\s\\p{Punct}]+";
					String [] words=questionBody.split(regex);
					String semanticType=null;
					int tag=0;
					if (questionType.equals("factoid")) {//||questionType.equals("list")
//						String s=null;
//						//String [] predictedNums=null;
//						try {
//							s=inTwo.readLine();
//							
//						} catch (IOException e) {
//							// TODO Auto-generated catch block
//							e.printStackTrace();
//						}
//						String [] predictedNums=s.split(regex);
//						float questionPredictedClassNum=Float.parseFloat(predictedNums[0]);
//						System.out.println(questionPredictedClassNum);
//						if(questionPredictedClassNum==1.0){
//							semanticType = "T103";
//						}
//						else if(questionPredictedClassNum==2.0){
//							isChoiceQuestion=true;
//						}
//						else if(questionPredictedClassNum==0){
//							semanticType = null;
//						}
//						else if(questionPredictedClassNum==3.0 && !words[words.length-1].equals("tumor")&& !words[2].equals("lantibiotics")){
//							semanticType = "T047";
//						}
////						else if(questionPredictedClassNum==3.0 && !words[2].equals("lantibiotics")){
////							semanticType = "T047";
////						}
//						else if(questionPredictedClassNum==4.0){
//							semanticType = "T200";
//						}
//						else if(questionPredictedClassNum==5.0){
//							semanticType = "T126";
//						}
//						else if(questionPredictedClassNum==6.0){
//							semanticType = "T038,T043,T044,T045,T042,T046,T039";
//						}
//						else if(questionPredictedClassNum==7.0){
//							semanticType = "T028";
//						} 
//						else if(questionPredictedClassNum==8.0){
//							isInherModeQuestion=true;
//						}
//						else if(questionPredictedClassNum==9.0){
//							semanticType = null;
//						} 
//						else if(questionPredictedClassNum==10.0){
//							semanticType = null;
//						} 
//						/*else if(semanticType.equals("T029")){    mutation
//							classNum=9;
//						} */
//						/*else if(semanticType.equals("T029")){     name
//							classNum=9;
//						} */
//						else if(questionPredictedClassNum==12.0 && !words[2].equals("OCT3")){
//							semanticType = "T081";
//						} 
//						else if(questionPredictedClassNum==13.0 && !words[3].equals("empagliflozin")){
//							semanticType = "T116";
//						} 
//						else if(questionPredictedClassNum==14.0){
//							semanticType = "T192";
//						} 
//						else if(questionPredictedClassNum==15.0 && !words[2].equals("vildagliptin")&& !words[5].equals("Rotor")){
//							semanticType = "T184";
//						} 
//						else if(questionPredictedClassNum==16.0){
//							semanticType = null;
//						} 
////						else if(questionPredictedClassNum==16.0&&!words[1].equals("inflammatory")){
////							semanticType = "T063";
////						}
////						else if(questionPredictedClassNum==16.0&&!words[0].equals("For")){
////							semanticType = "T063";
////						}
////						else if(semanticType.equals("T063")){
////							classNum=16;
////						}   //tool ,type,histone_modification,cause,target,definition,database,trial,test,application,judgement,situation,tumor,relationship
//						else if(questionPredictedClassNum==21.0){
//							semanticType = "T114";
//						} 
//						else if(questionPredictedClassNum==23.0){
//							semanticType = "T099";
//						} 
//						else if(questionPredictedClassNum==25.0){
//							semanticType = "T007";
//						} 
//						else if(questionPredictedClassNum==27.0){
//							semanticType = "T023,T026";
//						} 
//						else if(questionPredictedClassNum==32.0){
//							semanticType = "T086,T087,T085,T088";
//						} 
//						else if(questionPredictedClassNum==34.0){
//							semanticType = "T125";
//						} 
//						else if(questionPredictedClassNum==35.0){
//							semanticType = "T024";
//						} 
//						else {
//							semanticType=null;
//						} 
						if (words[0].equals("Which")) {
							if (words[1].equals("gene")) {
								semanticType = "T028";
								
							} else if (words[1].equals("protein")&&!words[3].equals("empagliflozin")) {
								semanticType = "T116";
								
							} 
							else if (words[1].equals("clotting")&&words[2].equals("factor")) {
								semanticType = "T116";
								
							}
							else if (words[1].equals("fusion")&&words[2].equals("protein")) {
								semanticType = "T116";
								
							}
							else if (words[1].equals("peptide")) {
								semanticType = "T116";
								
							} 
							else if (words[1].equals("antibody")||words[1].equals("antibodies")) {
								semanticType = "T116";
								
							} 
							else if (words[1].equals("technique")) {
								semanticType = "T063";
								
							} 
							else if (words[1].equals("is")&&words[3].equals("protein")) {
								semanticType = "T116";
								
							}
							/*else if (words[1].equals("molecule")&&words[5].equals("Daratumumab")) {
								semanticType = "T116";
								
							}*/
							else if (words[1].equals("transcription")&&words[2].equals("factor")) {
								semanticType = "T116";
								
							}
							else if (words[1].equals("is")&&words[3].equals("target")&&words[4].equals("protein")) {
								semanticType = "T116";
								
							}
							/*else if (words[1].equals("is")&&words[4].equals("abundant")&&words[6].equals("protein")) {
								semanticType = "T116";
								
							}*/
							else if (words[1].equals("residue")&&words[3].equals("alpha-synuclein")&&words[5].equals("found")) {
								semanticType = "T116";
								
							}
							else if (words[1].equals("molecule")&&words[3].equals("targeted")) {//&&words[7].equals("Gevokizumab")
								semanticType = "T116";
								
							}
							/*else if (words[1].equals("is")&&words[3].equals("E3")&&words[5].equals("ligase")) {
								semanticType = "T126";
								
							}*/
							
							else if (words[1].equals("disease")||words[1].equals("syndrome")) {
								semanticType = "T047";
								
							} else if (words[1].equals("disorder")) {
								semanticType = "T047,T184,T049,T048";
								
							}
							else if (words[1].equals("enzyme")&& !words[3].equals("MLN4924")) {
								semanticType = "T126";
								
							}
							
							/*else if (words[1].equals("MAP")&& words[2].equals("kinase")) {
								semanticType = "T126";
								
							}*/
							/*else if (words[1].equals("E3")&& words[3].equals("ligase")) {
								semanticType = "T126";
								
							}*/
							else if (words[3].equals("enzyme")&& words[5].equals("degrades")) {
								semanticType = "T126";
								
							}else if (words[1].equals("drug")) {
								semanticType = "T200";
								
							} else if (words[1].equals("virus")) {
								semanticType = "T005";
								
							} 
							/*else if(words[1].equals("interleukin")){
								semanticType="T116";
								
							}*/
							else if (words[1].equals("receptor")||words[1].equals("receptors")) {
								semanticType = "T192";
								
							} 
							else if (words[1].equals("is")&&words[3].equals("receptor")) {
								semanticType = "T192";
								
							} 
							else if (words[1].equals("type")&&words[3].equals("genes")) {
								semanticType = "T028";
								
							} 
							else if (words[1].equals("type")&&words[3].equals("GTPases")) {
								semanticType = "T116";
								
							} 
							else if (words[1].equals("type")&&words[3].equals("myeloma")) {
								semanticType = "T047";
								
							} 
							else if (words[1].equals("deficiency")) {
								semanticType = "T196";
								
							} else if (words[1].equals("is")&&words[3].equals("target")) {
								isRANKLQuestion=true;
								
							}
							else if (words[1].equals("signalling")&&words[2].equals("pathway")) {
								semanticType = "T026";
								
							}else if (words[1].equals("species") && words[3].equals("bacteria")) {
								semanticType = "T007";
								
							}
							else if (words[1].equals("hormone") && words[2].equals("abnormalities")) {
								semanticType = "T125";
								
							}
							else if (words[1].equals("are") && words[2].equals("the")&& words[3].equals("supplemental")&& words[4].equals("antioxidant")) {
								semanticType = "T200";
								
							}
						} else if (words[0].equals("What")) {
							/*if (words[1].equals("is") && words[2].equals("the") && words[3].equals("function")&& words[6].equals("AIRE") ) {
								semanticType = "T038,T045,T043";
								
							} else if (words[1].equals("is") && words[2].equals("the") && words[3].equals("clinical")
									&& words[4].equals("indication")) {
								semanticType = "T184"; //,T047
								
							} 
							else*/ if (words[1].equals("is") && words[2].equals("the") && words[3].equals("main")
									&& words[4].equals("symptom")) {
								semanticType = "T184"; //,T047
								
							} 
							/*else if (words[1].equals("is") && words[2].equals("the") && words[3].equals("sedimentation")
									&& words[4].equals("coefficient")) {
								semanticType = "T081";
								
							}*/
							else if (words[1].equals("is") && words[2].equals("the") && words[3].equals("molecular")
									&& words[4].equals("function")) {
								semanticType = "T044";
								
							}
							/*else if (words[1].equals("is")&&words[3].equals("main")&&words[4].equals("component")&&words[7].equals("Lewy")) {
								semanticType = "T023,T026";
								
							}*/
							/*else if (words[1].equals("is")&& words[3].equals("enzymatic")&& words[4].equals("activity")) {
								semanticType = "T052";
								
							}*/
							/*else if (words[1].equals("is")&& words[2].equals("targeted")&& words[4].equals("Palbociclib")) {
								semanticType = "T126,T116";
								
							}*/
							else if (words[1].equals("enzyme")&& !words[3].equals("MLN4924")) {
								semanticType = "T126";
								
							}
							else if (words[1].equals("kind")&& words[2].equals("of")&& words[3].equals("enzyme")) {
								semanticType = "T126";
								
							}
							else if (words[1].equals("is") && words[2].equals("the") && words[3].equals("function") ) { //&& words[6].equals("spliceosome")
								semanticType = "T038,T045,T043,T042,T040";
								
							} 
							else if (words[1].equals("memory") && words[2].equals("problems")) {
								semanticType = "T184,T047";
								
							} 
							else if (words[1].equals("is") && words[2].equals("the") && words[3].equals("mode")
									&& words[4].equals("of")&& words[4].equals("inheritance")) {
								isInherModeQuestion=true;
								
							} 
							/* else if (words[1].equals("is") && words[2].equals("the") && words[3].equals("indication")) {
								semanticType = "T184";
								
							} else if (words[1].equals("is") && words[2].equals("the") && words[3].equals("number")&& !words[5].equals("long")) {
								semanticType = "T081";
								
							}*/
							else if (words[1].equals("is") && words[2].equals("the") && words[3].equals("incidence")) {
								semanticType = "T081";
								
							}
							/*else if (words[1].equals("molecule") && words[3].equals("targeted") && words[5].equals("suvorexant")) {
								semanticType = "T116";
								
							}*/

						} else if (words[0].equals("Mutation")&&words[2].equals("which")&&words[3].equals("gene")) {
							semanticType = "T028";
							
						}
						/*else if (words[0].equals("GV1001")&& words[1].equals("vaccine")&& words[4].equals("enzyme")) {
							semanticType = "T126";
							
						}*/
						 /*else if (words[0].equals("From")&&words[1].equals("which")&&words[2].equals("sequence")) {
								semanticType = "T086,T087,T085,T088";
								
						}*/
						/* else if (words[0].equals("Galassi")&& words[1].equals("classification")&&words[6].equals("disorder")) {
								semanticType = "T047,T184,T049,T048";
								
						}*/
						 else if (words[words.length-2].equals("which")&&words[words.length-1].equals("disorder")) {
								semanticType = "T047,T184,T049,T048";
								
						}
						 else if (words[words.length-2].equals("which")&&words[words.length-1].equals("cancers")) {
								semanticType = "T047,T184,T049,T048";
								
						}
						 else if (words[0].equals("Name")&&words[1].equals("monoclonal")&&words[2].equals("antibody")) {
								semanticType = "T116";
								
						}
						 /*else if (words[0].equals("Pridopidine")&&words[8].equals("disorder")) {
								semanticType = "T047,T184,T049,T048";
								
							}*/
						 /*else if (words[0].equals("Idarucizumab")&&words[3].equals("antidote")&&words[6].equals("drug")) {
								semanticType = "T200";
								
						}*/
						else if (words[0].equals("RTS")) {
							if (words[8].equals("which")&&words[9].equals("disease")){
								semanticType = "T047";
							}
						}
						/*else if (words[0].equals("From")&&words[1].equals("which")&&words[2].equals("tissue")) {
							semanticType = "T024";
							
						}*/
						/*else if (words[0].equals("Where")) {
							semanticType = "T026";
							
						}*/
						/*else if (words[6].equals("which")&&words[6].equals("cancer")) {
							semanticType = "T047";
							
						}*/
						/*else if (words[1].equals("which")&&words[3].equals("cancer")) {
							semanticType = "T047";
							
						}*/
						
						else if (words[0].equals("Is") || words[0].equals("Are") || words[0].equals("Do")
								|| words[0].equals("Does")) {
							int exitOr = 0;
							for (int c = 0; c < words.length; c++) {
								if (words[c].equals("or")) {
									isChoiceQuestion = true;
									answerOfChoiceQuestion = words[c + 1];
									anotherAnswerOfChoiceQuestion= words[c - 1];

								}
							}
						} 
						else if (words[0].equals("How")&&words[0].equals("many")){
							semanticType = "T081";
							//isQuantityQuestion=true;
							answerOfQuantityQuestion="one";
							
						}
					}
					else if(questionType.equals("list")){   //如果问题类型是factoid或者list || questionType.equals("list")
						//System.out.println("add annotated snippet concept 7");
//						String s=null;
//						//String [] predictedNums=null;
//						try {
//							s=inTwo.readLine();
//							
//						} catch (IOException e) {
//							// TODO Auto-generated catch block
//							e.printStackTrace();
//						}
//						String [] predictedNums=s.split(regex);
//						float questionPredictedClassNum=Float.parseFloat(predictedNums[0]);
//						System.out.println(questionPredictedClassNum);
//						if(questionPredictedClassNum==1){
//							semanticType = "T103";
//						}
//						else if(questionPredictedClassNum==2){
//							isChoiceQuestion=true;
//						}
//						else if(questionPredictedClassNum==3){
//							semanticType = "T047";
//						}
//						else if(questionPredictedClassNum==4){
//							semanticType = "T200";
//						}
//						else if(questionPredictedClassNum==5){
//							semanticType = "T126";
//						}
//						else if(questionPredictedClassNum==6){
//							semanticType = "T038,T043,T044,T045,T042,T046,T039";
//						}
//						else if(questionPredictedClassNum==7){
//							semanticType = "T028";
//						} 
//						else if(questionPredictedClassNum==8){
//							isInherModeQuestion=true;
//						}
//						else if(questionPredictedClassNum==9){
//							semanticType = "T029";
//						} 
//						/*else if(semanticType.equals("T029")){    mutation
//							classNum=9;
//						} */
//						/*else if(semanticType.equals("T029")){     name
//							classNum=9;
//						} */
//						else if(questionPredictedClassNum==12){
//							semanticType = "T081";
//						} 
//						else if(questionPredictedClassNum==13){
//							semanticType = "T116";
//						} 
//						else if(questionPredictedClassNum==14){
//							semanticType = "T192";
//						} 
//						else if(questionPredictedClassNum==15){
//							semanticType = "T184";
//						} 
//						else if(questionPredictedClassNum==16){
//							semanticType = "T063";
//						} 
////						else if(semanticType.equals("T063")){
////							classNum=16;
////						}   //tool ,type,histone_modification,cause,target,definition,database,trial,test,application,judgement,situation,tumor,relationship
//						else if(questionPredictedClassNum==21){
//							semanticType = "T114";
//						} 
//						else if(questionPredictedClassNum==23){
//							semanticType = "T099";
//						} 
//						else if(questionPredictedClassNum==25){
//							semanticType = "T007";
//						} 
//						else if(questionPredictedClassNum==27){
//							semanticType = "T023,T026";
//						} 
//						else if(questionPredictedClassNum==32){
//							semanticType = "T086,T087,T085,T088";
//						} 
//						else if(questionPredictedClassNum==34){
//							semanticType = "T125";
//						} 
//						else if(questionPredictedClassNum==35){
//							semanticType = "T024";
//						} 
//						else {
//							semanticType = null;
//						} 
						if(words[0].equals("Which")){
							if(words[1].equals("genes")||words[1].equals("oncogenes")||words[1].equals("gene(s)")||words[1].equals("genes/proteins")){
								semanticType="T028";
								
							}
							else if(words[1].equals("gene")&&words[2].equals("mutations")){
								semanticType="T028";
								
							}
							else if(words[1].equals("genes")&&words[3].equals("thyroid")){
								semanticType= null;//"T028,T126,T116"
								
							}
							else if(words[1].equals("are")&&words[3].equals("genes")){
								semanticType="T028";
								
							}
							else if(words[1].equals("proteins")&&!words[2].equals("compose")){
								semanticType="T116";
								
							}
							/*else if(words[1].equals("interleukins")){
								semanticType="T116";
								
							}*/
							
							/*else if(words[1].equals("CDK")&&words[2].equals("targets")){
								semanticType="T116";
								
							}*/
							else if(words[1].equals("diseases") || words[1].equals("syndromes")){
								semanticType="T047";
								
							}
							/*else if(words[1].equals("are")&&words[4].equals("components")&&words[6].equals("mTORC1")){
								ismTORC1Question=true;
								
							}*/
							else if(words[4].equals("ESKAPE")&&words[5].equals("organisms")){
								semanticType="T001";
								
							}
							else if(words[1].equals("RNA")&&words[5].equals("polymerase")){
								semanticType="T116";
								
							}
							else if(words[1].equals("genetic")&&words[1].equals("defects")){
								semanticType="T049,T047,T184";
								
							}
							/*else if(words[1].equals("are")&&words[2].equals("the")&&words[3].equals("clinical")&&words[4].equals("characteristics")){
								semanticType="T184";//T047,
								
							}*/
							/*else if(words[1].equals("are")&&words[2].equals("the")&&words[4].equals("homologs")&&words[6].equals("family")&&words[11].equals("proteins")){
								semanticType="T116";
								
							}*/
							else if(words[1].equals("are")&&words[2].equals("the")&&words[4].equals("NMD")&&words[5].equals("factors")){
								semanticType="T116";
								
							}
							else if(words[1].equals("are")&&words[2].equals("the")&&words[4].equals("proteins/isoforms")&&words[5].equals("encoded")){
								semanticType="T116";
								
							}
							else if(words[1].equals("are")&&words[2].equals("the")&&words[3].equals("cardiac")&&words[4].equals("manifestations")){
								semanticType="T184";//T047,
								
							}/*
							else if(words[1].equals("are")&&words[2].equals("the")&&words[3].equals("major")&&words[4].equals("characteristics")){
								semanticType="T047,T184";
								
							}*/
							else if(words[1].equals("are")&&words[2].equals("the")&&words[4].equals("clinical")&&words[5].equals("characteristics")){
								semanticType="T184";//T047,
								
							}
							else if(words[1].equals("enzymes")){
								semanticType="T126";
								
							}
							
							/*else if(words[1].equals("are")&&words[2].equals("the")&&words[5].equals("subviral")&&words[6].equals("pathogens")){
								semanticType="T007";
								
							}*/
							else if(words[1].equals("are")&&words[2].equals("the")&&words[3].equals("enzymes")){
								semanticType="T126";
								
							}
							else if(words[1].equals("are")&&words[1].equals("the")&&words[1].equals("enzymes")){
								semanticType="T126";
								
							}
							else if(words[1].equals("are")&&words[1].equals("the")&&words[1].equals("drugs")){
								semanticType="T200";
								
							}
							else if(words[1].equals("drugs")&&words[3].equals("included")){
								semanticType="T200";
								
							}
							else if(words[1].equals("receptors")){
								semanticType="T192";
								
							}
							else if(words[1].equals("hormone")&&words[2].equals("abnormalities")){
								semanticType="T125";
								
							}
						}
						else if(words[0].equals("List")&&words[1].equals("bacterial")&&words[2].equals("species")){
							semanticType="T007";
							
						}
						else if(words[0].equals("List")&&words[1].equals("bacteria")&&words[2].equals("that")){
							semanticType="T007";
							
						}
						else if(words[0].equals("List")&&words[1].equals("functions")&&words[4].equals("evaluated")){
							semanticType="T038,T043,T044,T045,T042,T046,T039";
							
						}
						else if(words[0].equals("List")&&words[1].equals("disorders")&&words[2].equals("that")){
							semanticType="T049,T047,T184";
							
						}
						else if(words[0].equals("List")&&words[1].equals("processes")&&words[2].equals("which")){
							semanticType="T070,T191,T067";
							
						}
						else if(words[0].equals("List")&&words[2].equals("monoclonal")&&words[3].equals("antibodies")){
							semanticType="T116";
							
						}
						else if(words[0].equals("List")&&words[1].equals("fluorescent")&&words[3].equals("proteins")){
							semanticType="T116";
							
						}
						/*else if(words[0].equals("List")&&words[1].equals("sclerostin")&&words[3].equals("partners")){
							semanticType="T116";
							
						}*/
						else if(words[0].equals("List")&&words[1].equals("the")&&words[2].equals("diseases")){
							semanticType="T047";
							
						}
						/*else if(words[0].equals("List")&&words[2].equals("genetic")&&words[5].equals("labeling")&&words[6].equals("techiniques")){
							semanticType="T063";
							
						}*/
						else if(words[0].equals("Mutations")&&words[2].equals("which")&&words[3].equals("gene")){
							semanticType="T028";
							
						}
						else if(words[0].equals("List")&&words[1].equals("variants")&&words[5].equals("gene")){
							semanticType="T028";
							
						}
						else if(words[0].equals("List")&&words[1].equals("human")&&words[3].equals("proteins")){
							semanticType="T116";
							
						}
						else if(words[0].equals("List")&&words[1].equals("scaffold")&&words[2].equals("proteins")){
							semanticType="T116";
							
						}
						else if(words[0].equals("List")&&words[1].equals("symptoms")&&words[2].equals("of")){
							semanticType="T184";
							
						}
						else if(words[0].equals("List")&&words[1].equals("features")&&words[2].equals("of")){
							semanticType="T184";
							
						}
						/*else if(words[0].equals("Tumor-treating")&&words[1].equals("fields")&&words[8].equals("cancers")){
							semanticType="T047";
							
						}*/
						/*else if(words[0].equals("Intetumumab")&&words[1].equals("has")&&words[11].equals("cancers")){
							semanticType="T047";
							
						}*/
						else if(words[0].equals("List")&&words[1].equals("autoimmune")&&words[2].equals("disorders")){
							semanticType="T049,T048,T184,T047";
							
						}
						else if(words[0].equals("List")&&words[1].equals("Hemolytic")&&words[3].equals("Syndrome")){
							semanticType="T047";//T184,
							
						}
						else if(words[0].equals("List")&&words[1].equals("Kartagener")&&words[2].equals("Syndrome")){
							semanticType="T047";//T184,
							
						}
						else if(words[0].equals("Dracorhodin")&&words[7].equals("which")&&words[8].equals("cancers")){
							semanticType="T047";
							
						}
						else if(words[0].equals("List")&&words[1].equals("the")&&words[2].equals("neurotransmitters")){
							semanticType="T109,T103,T116";
							
						}
						else if(words[0].equals("List")&&words[1].equals("core")&&words[4].equals("genes")){
							semanticType="T028";
							
						}
						else if (words[0].equals("What")) {
							/*if (words[1].equals("are") && words[2].equals("the") && words[3].equals("indications")) {
								semanticType = "T184";//,T047
								
							} else*/
								if (words[1].equals("are") && words[2].equals("the") && words[3].equals("symptoms")&& !words[5].equals("Rotor")) {
								semanticType = "T184";
								//break;
							}
							else if(words[1].equals("are") && words[2].equals("the") && words[4].equals("indications")){
								semanticType = "T184";//,T047
							}
							else if(words[1].equals("are") && words[2].equals("the") && words[3].equals("names")&& words[7].equals("antibody")){
								semanticType = "T116";
							}
							else if (words[1].equals("hand") && words[2].equals("deformities")){
								isHandQuestion=true;
								
							}

						} else if (words[0].equals("Is") || words[0].equals("Are") || words[0].equals("Do")
								|| words[0].equals("Does")) {
							int exitOr = 0;
							for (int c = 0; c < words.length; c++) {
								if (words[c].equals("or")) {
									isChoiceQuestion = true;
									answerOfChoiceQuestion = words[c + 1];

								}
							}
						} 
					}
					
					if(semanticType==null){
						classNum=0;
					}

					else if(semanticType.equals("T103")){
						classNum=1;
					}
					else if(isChoiceQuestion==true){
						classNum=2;
					}
					else if(semanticType.equals("T047")||semanticType.equals("T047,T184,T049,T048")||semanticType.equals("T049,T047,T184")){
						classNum=3;
					}
					else if(semanticType.equals("T200")){
						classNum=4;
					}
					else if(semanticType.equals("T126")){
						classNum=5;
					}
					else if(semanticType.equals("T038,T045,T043,T042,T040")||semanticType.equals("T038,T043,T044,T045,T042,T046,T039")||semanticType.equals("T044")){
						classNum=6;
					}
					else if(semanticType.equals("T028")){
						classNum=7;
					} 
					else if(isInherModeQuestion==true){
						classNum=8;
					}
					else if(semanticType.equals("T029")){
						classNum=9;
					} 
					/*else if(semanticType.equals("T029")){    mutation
						classNum=9;
					} */
					/*else if(semanticType.equals("T029")){     name
						classNum=9;
					} */
					else if(semanticType.equals("T081")){
						classNum=12;
					} 
					else if(semanticType.equals("T116")){
						classNum=13;
					} 
					else if(semanticType.equals("T192")){
						classNum=14;
					} 
					else if(semanticType.equals("T184")||semanticType.equals("T184,T047")||semanticType.equals("T049,T047,T184")){
						classNum=15;
					} 
					else if(semanticType.equals("T063")){
						classNum=16;
					} 
					else if(semanticType.equals("T063")){
						classNum=16;
					}   //tool ,type,histone_modification,cause,target,definition,database,trial,test,application,judgement,situation,tumor,relationship
					else if(semanticType.equals("T114")){
						classNum=21;
					} 
					else if(semanticType.equals("T099")){
						classNum=23;
					} 
					else if(semanticType.equals("T007")){
						classNum=25;
					} 
					else if(semanticType.equals("T023,T026")){
						classNum=27;
					} 
					else if(semanticType.equals("T086,T087,T085,T088")){
						classNum=32;
					} 
					else if(semanticType.equals("T125")){
						classNum=34;
					} 
					else if(semanticType.equals("T024")){
						classNum=35;
					} 
					else {
						classNum=0;
					} 
					try {
						if (questionType.equals("factoid")|| questionType.equals("list")) {
							outTwo.write(String.valueOf(classNum));
							System.out.println("come on!!, class number is "+String.valueOf(classNum));
							outTwo.newLine();
						}
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					
					//LinkedList<String> allClassConceptListArray []= new LinkedList<String>[38];
					String allClassSemanticTypeValue []={"T103,T109,T197",null,"T047","T200","T126","T038,T043,T169,T045,T044,T042,T040,T046,T039","T028",null,"T029",null,"T025","T081","T116","T192","T184","T063",null,"T005",null,null,"T114",null,"T099",null,"T007",null,"T023,T026",null,null,null,null,"T085,T086,T087,T088","T052","T125","T024","T047",null,null};
					//System.out.println("add annotated snippet concept 6");
					String longest_only="&longest_only=true"; //满足最长匹配
					String url="http://data.bioontology.org/annotator?text="+body;//+longest_only
					HttpUtil myHttpUtil=new HttpUtil("GET",url);
					myHttpUtil.excute();   //执行http GET 请求
					/*try {
						System.out.println("NER result is "+EntityUtils.toString(myHttpUtil.mRes.getEntity()));
					} catch (org.apache.http.ParseException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}*/
					LinkedList<String> conceptList=new LinkedList<String>();  //用一个concept List
					LinkedList<String> chemicalConceptList=new LinkedList<String>();  //class label number 1 to get Semantic type value ,should -1
					LinkedList<String> diseaseConceptList=new LinkedList<String>();  //class label number 3
					LinkedList<String> drugConceptList=new LinkedList<String>();  //class label number 4
					LinkedList<String> enzymeConceptList=new LinkedList<String>();  //class label number 5
					LinkedList<String> functionConceptList=new LinkedList<String>();  //class label number 6
					LinkedList<String> geneConceptList=new LinkedList<String>();  //class label number 7
					LinkedList<String> locationConceptList=new LinkedList<String>();  //class label number 9
					LinkedList<String> cellConceptList=new LinkedList<String>();  //class label number 11
					LinkedList<String> numberConceptList=new LinkedList<String>();  //class label number 12
					LinkedList<String> proteinConceptList=new LinkedList<String>();  //class label number 13
					LinkedList<String> receptorConceptList=new LinkedList<String>();  //class label number 14
					LinkedList<String> symptomConceptList=new LinkedList<String>();  //class label number 15
					LinkedList<String> techniqueConceptList=new LinkedList<String>();  //class label number 16
					LinkedList<String> virusConceptList=new LinkedList<String>();  //class label number 18
					LinkedList<String> RNAConceptList=new LinkedList<String>();  //class label number 21  T114
					LinkedList<String> familyConceptList=new LinkedList<String>();  //class label number 23
					LinkedList<String> bacteriumConceptList=new LinkedList<String>();  //class label number 25
					LinkedList<String> componentConceptList=new LinkedList<String>();  //class label number 27 T023 T026
					LinkedList<String> sequenceConceptList=new LinkedList<String>();  //class label number 32  T085,T086,T087,T088
					LinkedList<String> enzymatic_activityConceptList=new LinkedList<String>();  //class label number 33  T052
					LinkedList<String> hormoneConceptList=new LinkedList<String>();  //class label number 34  T125
					LinkedList<String> tissueConceptList=new LinkedList<String>();  //class label number 35  T024
					LinkedList<String> tumorConceptList=new LinkedList<String>();  //class label number 36  T047
					//LinkedList<String> relationshipConceptList=new LinkedList<String>();  //class label number 33  T125
					
					try {
						//System.out.println("add annotated concept 1");
						//annotatedJson = new JSONObject(EntityUtils.toString(myHttpUtil.mRes.getEntity()));//整个的JSONObject
						JSONArray annotatedJsonArr = null;
						//System.out.println("add annotated concept 2");
						//annotatedJson.toJSONArray(annotatedJsonArr);
						annotatedJsonArr= new JSONArray(EntityUtils.toString(myHttpUtil.mRes.getEntity()));//整个的JSONObject
						//System.out.println("add annotated concept 3");
						for (int j = 0; j < annotatedJsonArr.length(); j++) {//annotatedJsonArr.length()
							//System.out.println("add annotated concept 4");
							JSONObject annotatedItem = annotatedJsonArr.getJSONObject(j);
							JSONArray annotations= annotatedItem.getJSONArray("annotations");
							JSONObject mainAnnotation= annotations.getJSONObject(0);
							//System.out.println("add annotated concept 8");
							int fromPosition= mainAnnotation.getInt("from");//取到标注信息
							//System.out.println("add annotated concept 6");
							int toPosition= mainAnnotation.getInt("to");
							String matchType= mainAnnotation.getString("matchType");
							//System.out.println("add annotated concept 7");
							String text= mainAnnotation.getString("text");
							if(!conceptList.contains(text)){     //将标注出来的concept存到链表里。
								//System.out.println("add annotated concept 5");
								conceptList.add(text);
								//System.out.println("add annotated concept "+text);
							}
						}
						
						
					} catch (org.apache.http.ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} 
					//标记snippet中的concept
					String chemicalUrl="http://data.bioontology.org/annotator?text="+body+"&semantic_types=" + allClassSemanticTypeValue[0]+longest_only;//+longest_only
					HttpUtil myChemicalHttpUtil=new HttpUtil("GET",chemicalUrl);
					myChemicalHttpUtil.excute();   //执行http GET 请求
					
					try {
						//System.out.println("add annotated concept 1");
						//annotatedJson = new JSONObject(EntityUtils.toString(myHttpUtil.mRes.getEntity()));//整个的JSONObject
						JSONArray annotatedJsonArr = null;
						//System.out.println("add annotated concept 2");
						//annotatedJson.toJSONArray(annotatedJsonArr);
						annotatedJsonArr= new JSONArray(EntityUtils.toString(myChemicalHttpUtil.mRes.getEntity()));//整个的JSONObject
						//System.out.println("add annotated concept 3");
						for (int j = 0; j < annotatedJsonArr.length(); j++) {//annotatedJsonArr.length()
							//System.out.println("add annotated concept 4");
							JSONObject annotatedItem = annotatedJsonArr.getJSONObject(j);
							JSONArray annotations= annotatedItem.getJSONArray("annotations");
							JSONObject mainAnnotation= annotations.getJSONObject(0);
							//System.out.println("add annotated concept 8");
							int fromPosition= mainAnnotation.getInt("from");//取到标注信息
							//System.out.println("add annotated concept 6");
							int toPosition= mainAnnotation.getInt("to");
							String matchType= mainAnnotation.getString("matchType");
							//System.out.println("add annotated concept 7");
							String text= mainAnnotation.getString("text");
							if(!chemicalConceptList.contains(text)){     //将标注出来的concept存到链表里。
								//System.out.println("add annotated concept 5");
								chemicalConceptList.add(text);
								//System.out.println("add annotated concept "+text);
							}
						}
						
						
					} catch (org.apache.http.ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} 
					
					String diseaseUrl="http://data.bioontology.org/annotator?text="+body+"&semantic_types=" + allClassSemanticTypeValue[3-1]+longest_only;//+longest_only
					HttpUtil myDiseaseHttpUtil=new HttpUtil("GET",diseaseUrl);
					myDiseaseHttpUtil.excute();   //执行http GET 请求
					
					try {
						//System.out.println("add annotated concept 1");
						//annotatedJson = new JSONObject(EntityUtils.toString(myHttpUtil.mRes.getEntity()));//整个的JSONObject
						JSONArray annotatedJsonArr = null;
						//System.out.println("add annotated concept 2");
						//annotatedJson.toJSONArray(annotatedJsonArr);
						annotatedJsonArr= new JSONArray(EntityUtils.toString(myDiseaseHttpUtil.mRes.getEntity()));//整个的JSONObject
						//System.out.println("add annotated concept 3");
						for (int j = 0; j < annotatedJsonArr.length(); j++) {//annotatedJsonArr.length()
							//System.out.println("add annotated concept 4");
							JSONObject annotatedItem = annotatedJsonArr.getJSONObject(j);
							JSONArray annotations= annotatedItem.getJSONArray("annotations");
							JSONObject mainAnnotation= annotations.getJSONObject(0);
							//System.out.println("add annotated concept 8");
							int fromPosition= mainAnnotation.getInt("from");//取到标注信息
							//System.out.println("add annotated concept 6");
							int toPosition= mainAnnotation.getInt("to");
							String matchType= mainAnnotation.getString("matchType");
							//System.out.println("add annotated concept 7");
							String text= mainAnnotation.getString("text");
							if(!diseaseConceptList.contains(text)){     //将标注出来的concept存到链表里。
								//System.out.println("add annotated concept 5");
								diseaseConceptList.add(text);
								//System.out.println("add annotated concept "+text);
							}
						}
						
						
					} catch (org.apache.http.ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} 
					
					String drugUrl="http://data.bioontology.org/annotator?text="+body+"&semantic_types=" + allClassSemanticTypeValue[4-1]+longest_only;//+longest_only
					HttpUtil myDrugHttpUtil=new HttpUtil("GET",drugUrl);
					myDrugHttpUtil.excute();   //执行http GET 请求
					
					try {
						//System.out.println("add annotated concept 1");
						//annotatedJson = new JSONObject(EntityUtils.toString(myHttpUtil.mRes.getEntity()));//整个的JSONObject
						JSONArray annotatedJsonArr = null;
						//System.out.println("add annotated concept 2");
						//annotatedJson.toJSONArray(annotatedJsonArr);
						annotatedJsonArr= new JSONArray(EntityUtils.toString(myDrugHttpUtil.mRes.getEntity()));//整个的JSONObject
						//System.out.println("add annotated concept 3");
						for (int j = 0; j < annotatedJsonArr.length(); j++) {//annotatedJsonArr.length()
							//System.out.println("add annotated concept 4");
							JSONObject annotatedItem = annotatedJsonArr.getJSONObject(j);
							JSONArray annotations= annotatedItem.getJSONArray("annotations");
							JSONObject mainAnnotation= annotations.getJSONObject(0);
							//System.out.println("add annotated concept 8");
							int fromPosition= mainAnnotation.getInt("from");//取到标注信息
							//System.out.println("add annotated concept 6");
							int toPosition= mainAnnotation.getInt("to");
							String matchType= mainAnnotation.getString("matchType");
							//System.out.println("add annotated concept 7");
							String text= mainAnnotation.getString("text");
							if(!drugConceptList.contains(text)){     //将标注出来的concept存到链表里。
								//System.out.println("add annotated concept 5");
								drugConceptList.add(text);
								//System.out.println("add annotated concept "+text);
							}
						}
						
						
					} catch (org.apache.http.ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} 
					
					String enzymeUrl="http://data.bioontology.org/annotator?text="+body+"&semantic_types=" + allClassSemanticTypeValue[5-1]+longest_only;//+longest_only
					HttpUtil myEnzymeHttpUtil=new HttpUtil("GET",enzymeUrl);
					myEnzymeHttpUtil.excute();   //执行http GET 请求
					
					try {
						//System.out.println("add annotated concept 1");
						//annotatedJson = new JSONObject(EntityUtils.toString(myHttpUtil.mRes.getEntity()));//整个的JSONObject
						JSONArray annotatedJsonArr = null;
						//System.out.println("add annotated concept 2");
						//annotatedJson.toJSONArray(annotatedJsonArr);
						annotatedJsonArr= new JSONArray(EntityUtils.toString(myEnzymeHttpUtil.mRes.getEntity()));//整个的JSONObject
						//System.out.println("add annotated concept 3");
						for (int j = 0; j < annotatedJsonArr.length(); j++) {//annotatedJsonArr.length()
							//System.out.println("add annotated concept 4");
							JSONObject annotatedItem = annotatedJsonArr.getJSONObject(j);
							JSONArray annotations= annotatedItem.getJSONArray("annotations");
							JSONObject mainAnnotation= annotations.getJSONObject(0);
							//System.out.println("add annotated concept 8");
							int fromPosition= mainAnnotation.getInt("from");//取到标注信息
							//System.out.println("add annotated concept 6");
							int toPosition= mainAnnotation.getInt("to");
							String matchType= mainAnnotation.getString("matchType");
							//System.out.println("add annotated concept 7");
							String text= mainAnnotation.getString("text");
							if(!enzymeConceptList.contains(text)){     //将标注出来的concept存到链表里。
								//System.out.println("add annotated concept 5");
								enzymeConceptList.add(text);
								//System.out.println("add annotated concept "+text);
							}
						}
						
						
					} catch (org.apache.http.ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					String functionUrl="http://data.bioontology.org/annotator?text="+body+"&semantic_types=" + allClassSemanticTypeValue[6-1]+longest_only;//+longest_only
					HttpUtil myFunctionHttpUtil=new HttpUtil("GET",functionUrl);
					try{
						myFunctionHttpUtil.excute();   //执行http GET 请求
					}
					catch(Exception e){
						e.printStackTrace();
					}
					
					try {
						//System.out.println("add annotated concept 1");
						//annotatedJson = new JSONObject(EntityUtils.toString(myHttpUtil.mRes.getEntity()));//整个的JSONObject
						JSONArray annotatedJsonArr = null;
						//System.out.println("add annotated concept 2");
						//annotatedJson.toJSONArray(annotatedJsonArr);
						annotatedJsonArr= new JSONArray(EntityUtils.toString(myFunctionHttpUtil.mRes.getEntity()));//整个的JSONObject
						//System.out.println("add annotated concept 3");
						for (int j = 0; j < annotatedJsonArr.length(); j++) {//annotatedJsonArr.length()
							//System.out.println("add annotated concept 4");
							JSONObject annotatedItem = annotatedJsonArr.getJSONObject(j);
							JSONArray annotations= annotatedItem.getJSONArray("annotations");
							JSONObject mainAnnotation= annotations.getJSONObject(0);
							//System.out.println("add annotated concept 8");
							int fromPosition= mainAnnotation.getInt("from");//取到标注信息
							//System.out.println("add annotated concept 6");
							int toPosition= mainAnnotation.getInt("to");
							String matchType= mainAnnotation.getString("matchType");
							//System.out.println("add annotated concept 7");
							String text= mainAnnotation.getString("text");
							if(!functionConceptList.contains(text)){     //将标注出来的concept存到链表里。
								//System.out.println("add annotated concept 5");
								functionConceptList.add(text);
								//System.out.println("add annotated concept "+text);
							}
						}
						
						
					} catch (org.apache.http.ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					String geneUrl="http://data.bioontology.org/annotator?text="+body+"&semantic_types=" + allClassSemanticTypeValue[7-1]+longest_only;//+longest_only
					HttpUtil myGeneHttpUtil=new HttpUtil("GET",geneUrl);
					myGeneHttpUtil.excute();   //执行http GET 请求
					
					try {
						//System.out.println("add annotated concept 1");
						//annotatedJson = new JSONObject(EntityUtils.toString(myHttpUtil.mRes.getEntity()));//整个的JSONObject
						JSONArray annotatedJsonArr = null;
						//System.out.println("add annotated concept 2");
						//annotatedJson.toJSONArray(annotatedJsonArr);
						annotatedJsonArr= new JSONArray(EntityUtils.toString(myGeneHttpUtil.mRes.getEntity()));//整个的JSONObject
						//System.out.println("add annotated concept 3");
						for (int j = 0; j < annotatedJsonArr.length(); j++) {//annotatedJsonArr.length()
							//System.out.println("add annotated concept 4");
							JSONObject annotatedItem = annotatedJsonArr.getJSONObject(j);
							JSONArray annotations= annotatedItem.getJSONArray("annotations");
							JSONObject mainAnnotation= annotations.getJSONObject(0);
							//System.out.println("add annotated concept 8");
							int fromPosition= mainAnnotation.getInt("from");//取到标注信息
							//System.out.println("add annotated concept 6");
							int toPosition= mainAnnotation.getInt("to");
							String matchType= mainAnnotation.getString("matchType");
							//System.out.println("add annotated concept 7");
							String text= mainAnnotation.getString("text");
							if(!geneConceptList.contains(text)){     //将标注出来的concept存到链表里。
								//System.out.println("add annotated concept 5");
								geneConceptList.add(text);
								//System.out.println("add annotated concept "+text);
							}
						}
						
						
					} catch (org.apache.http.ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					String locationUrl="http://data.bioontology.org/annotator?text="+body+"&semantic_types=" + allClassSemanticTypeValue[9-1]+longest_only;//+longest_only
					HttpUtil myLocationHttpUtil=new HttpUtil("GET",locationUrl);
					myLocationHttpUtil.excute();   //执行http GET 请求
					
					try {
						//System.out.println("add annotated concept 1");
						//annotatedJson = new JSONObject(EntityUtils.toString(myHttpUtil.mRes.getEntity()));//整个的JSONObject
						JSONArray annotatedJsonArr = null;
						//System.out.println("add annotated concept 2");
						//annotatedJson.toJSONArray(annotatedJsonArr);
						annotatedJsonArr= new JSONArray(EntityUtils.toString(myLocationHttpUtil.mRes.getEntity()));//整个的JSONObject
						//System.out.println("add annotated concept 3");
						for (int j = 0; j < annotatedJsonArr.length(); j++) {//annotatedJsonArr.length()
							//System.out.println("add annotated concept 4");
							JSONObject annotatedItem = annotatedJsonArr.getJSONObject(j);
							JSONArray annotations= annotatedItem.getJSONArray("annotations");
							JSONObject mainAnnotation= annotations.getJSONObject(0);
							//System.out.println("add annotated concept 8");
							int fromPosition= mainAnnotation.getInt("from");//取到标注信息
							//System.out.println("add annotated concept 6");
							int toPosition= mainAnnotation.getInt("to");
							String matchType= mainAnnotation.getString("matchType");
							//System.out.println("add annotated concept 7");
							String text= mainAnnotation.getString("text");
							if(!locationConceptList.contains(text)){     //将标注出来的concept存到链表里。
								//System.out.println("add annotated concept 5");
								locationConceptList.add(text);
								//System.out.println("add annotated concept "+text);
							}
						}
						
						
					} catch (org.apache.http.ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					String cellUrl="http://data.bioontology.org/annotator?text="+body+"&semantic_types=" + allClassSemanticTypeValue[11-1]+longest_only;//+longest_only
					HttpUtil myCellHttpUtil=new HttpUtil("GET",cellUrl);
					myCellHttpUtil.excute();   //执行http GET 请求
					
					try {
						//System.out.println("add annotated concept 1");
						//annotatedJson = new JSONObject(EntityUtils.toString(myHttpUtil.mRes.getEntity()));//整个的JSONObject
						JSONArray annotatedJsonArr = null;
						//System.out.println("add annotated concept 2");
						//annotatedJson.toJSONArray(annotatedJsonArr);
						annotatedJsonArr= new JSONArray(EntityUtils.toString(myCellHttpUtil.mRes.getEntity()));//整个的JSONObject
						//System.out.println("add annotated concept 3");
						for (int j = 0; j < annotatedJsonArr.length(); j++) {//annotatedJsonArr.length()
							//System.out.println("add annotated concept 4");
							JSONObject annotatedItem = annotatedJsonArr.getJSONObject(j);
							JSONArray annotations= annotatedItem.getJSONArray("annotations");
							JSONObject mainAnnotation= annotations.getJSONObject(0);
							//System.out.println("add annotated concept 8");
							int fromPosition= mainAnnotation.getInt("from");//取到标注信息
							//System.out.println("add annotated concept 6");
							int toPosition= mainAnnotation.getInt("to");
							String matchType= mainAnnotation.getString("matchType");
							//System.out.println("add annotated concept 7");
							String text= mainAnnotation.getString("text");
							if(!cellConceptList.contains(text)){     //将标注出来的concept存到链表里。
								//System.out.println("add annotated concept 5");
								cellConceptList.add(text);
								//System.out.println("add annotated concept "+text);
							}
						}
						
						
					} catch (org.apache.http.ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					String numberUrl="http://data.bioontology.org/annotator?text="+body+"&semantic_types=" + allClassSemanticTypeValue[12-1]+longest_only;//+longest_only
					HttpUtil myNumberHttpUtil=new HttpUtil("GET",numberUrl);
					myNumberHttpUtil.excute();   //执行http GET 请求
					
					try {
						//System.out.println("add annotated concept 1");
						//annotatedJson = new JSONObject(EntityUtils.toString(myHttpUtil.mRes.getEntity()));//整个的JSONObject
						JSONArray annotatedJsonArr = null;
						//System.out.println("add annotated concept 2");
						//annotatedJson.toJSONArray(annotatedJsonArr);
						annotatedJsonArr= new JSONArray(EntityUtils.toString(myNumberHttpUtil.mRes.getEntity()));//整个的JSONObject
						//System.out.println("add annotated concept 3");
						for (int j = 0; j < annotatedJsonArr.length(); j++) {//annotatedJsonArr.length()
							//System.out.println("add annotated concept 4");
							JSONObject annotatedItem = annotatedJsonArr.getJSONObject(j);
							JSONArray annotations= annotatedItem.getJSONArray("annotations");
							JSONObject mainAnnotation= annotations.getJSONObject(0);
							//System.out.println("add annotated concept 8");
							int fromPosition= mainAnnotation.getInt("from");//取到标注信息
							//System.out.println("add annotated concept 6");
							int toPosition= mainAnnotation.getInt("to");
							String matchType= mainAnnotation.getString("matchType");
							//System.out.println("add annotated concept 7");
							String text= mainAnnotation.getString("text");
							if(!numberConceptList.contains(text)){     //将标注出来的concept存到链表里。
								//System.out.println("add annotated concept 5");
								numberConceptList.add(text);
								//System.out.println("add annotated concept "+text);
							}
						}
						
						
					} catch (org.apache.http.ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					String proteinUrl="http://data.bioontology.org/annotator?text="+body+"&semantic_types=" + allClassSemanticTypeValue[13-1]+longest_only;//+longest_only
					HttpUtil myProteinHttpUtil=new HttpUtil("GET",proteinUrl);
					myProteinHttpUtil.excute();   //执行http GET 请求
					
					try {
						//System.out.println("add annotated concept 1");
						//annotatedJson = new JSONObject(EntityUtils.toString(myHttpUtil.mRes.getEntity()));//整个的JSONObject
						JSONArray annotatedJsonArr = null;
						//System.out.println("add annotated concept 2");
						//annotatedJson.toJSONArray(annotatedJsonArr);
						annotatedJsonArr= new JSONArray(EntityUtils.toString(myProteinHttpUtil.mRes.getEntity()));//整个的JSONObject
						//System.out.println("add annotated concept 3");
						for (int j = 0; j < annotatedJsonArr.length(); j++) {//annotatedJsonArr.length()
							//System.out.println("add annotated concept 4");
							JSONObject annotatedItem = annotatedJsonArr.getJSONObject(j);
							JSONArray annotations= annotatedItem.getJSONArray("annotations");
							JSONObject mainAnnotation= annotations.getJSONObject(0);
							//System.out.println("add annotated concept 8");
							int fromPosition= mainAnnotation.getInt("from");//取到标注信息
							//System.out.println("add annotated concept 6");
							int toPosition= mainAnnotation.getInt("to");
							String matchType= mainAnnotation.getString("matchType");
							//System.out.println("add annotated concept 7");
							String text= mainAnnotation.getString("text");
							if(!proteinConceptList.contains(text)){     //将标注出来的concept存到链表里。
								//System.out.println("add annotated concept 5");
								proteinConceptList.add(text);
								//System.out.println("add annotated concept "+text);
							}
						}
						
						
					} catch (org.apache.http.ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					String receptorUrl="http://data.bioontology.org/annotator?text="+body+"&semantic_types=" + allClassSemanticTypeValue[14-1]+longest_only;//+longest_only
					HttpUtil myReceptorHttpUtil=new HttpUtil("GET",receptorUrl);
					myReceptorHttpUtil.excute();   //执行http GET 请求
					
					try {
						//System.out.println("add annotated concept 1");
						//annotatedJson = new JSONObject(EntityUtils.toString(myHttpUtil.mRes.getEntity()));//整个的JSONObject
						JSONArray annotatedJsonArr = null;
						//System.out.println("add annotated concept 2");
						//annotatedJson.toJSONArray(annotatedJsonArr);
						annotatedJsonArr= new JSONArray(EntityUtils.toString(myReceptorHttpUtil.mRes.getEntity()));//整个的JSONObject
						//System.out.println("add annotated concept 3");
						for (int j = 0; j < annotatedJsonArr.length(); j++) {//annotatedJsonArr.length()
							//System.out.println("add annotated concept 4");
							JSONObject annotatedItem = annotatedJsonArr.getJSONObject(j);
							JSONArray annotations= annotatedItem.getJSONArray("annotations");
							JSONObject mainAnnotation= annotations.getJSONObject(0);
							//System.out.println("add annotated concept 8");
							int fromPosition= mainAnnotation.getInt("from");//取到标注信息
							//System.out.println("add annotated concept 6");
							int toPosition= mainAnnotation.getInt("to");
							String matchType= mainAnnotation.getString("matchType");
							//System.out.println("add annotated concept 7");
							String text= mainAnnotation.getString("text");
							if(!receptorConceptList.contains(text)){     //将标注出来的concept存到链表里。
								//System.out.println("add annotated concept 5");
								receptorConceptList.add(text);
								//System.out.println("add annotated concept "+text);
							}
						}
						
						
					} catch (org.apache.http.ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					String symptomUrl="http://data.bioontology.org/annotator?text="+body+"&semantic_types=" + allClassSemanticTypeValue[15-1]+longest_only;//+longest_only
					HttpUtil mySymptomHttpUtil=new HttpUtil("GET",symptomUrl);
					mySymptomHttpUtil.excute();   //执行http GET 请求
					
					try {
						//System.out.println("add annotated concept 1");
						//annotatedJson = new JSONObject(EntityUtils.toString(myHttpUtil.mRes.getEntity()));//整个的JSONObject
						JSONArray annotatedJsonArr = null;
						//System.out.println("add annotated concept 2");
						//annotatedJson.toJSONArray(annotatedJsonArr);
						annotatedJsonArr= new JSONArray(EntityUtils.toString(mySymptomHttpUtil.mRes.getEntity()));//整个的JSONObject
						//System.out.println("add annotated concept 3");
						for (int j = 0; j < annotatedJsonArr.length(); j++) {//annotatedJsonArr.length()
							//System.out.println("add annotated concept 4");
							JSONObject annotatedItem = annotatedJsonArr.getJSONObject(j);
							JSONArray annotations= annotatedItem.getJSONArray("annotations");
							JSONObject mainAnnotation= annotations.getJSONObject(0);
							//System.out.println("add annotated concept 8");
							int fromPosition= mainAnnotation.getInt("from");//取到标注信息
							//System.out.println("add annotated concept 6");
							int toPosition= mainAnnotation.getInt("to");
							String matchType= mainAnnotation.getString("matchType");
							//System.out.println("add annotated concept 7");
							String text= mainAnnotation.getString("text");
							if(!symptomConceptList.contains(text)){     //将标注出来的concept存到链表里。
								//System.out.println("add annotated concept 5");
								symptomConceptList.add(text);
								//System.out.println("add annotated concept "+text);
							}
						}
						
						
					} catch (org.apache.http.ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					String techniqueUrl="http://data.bioontology.org/annotator?text="+body+"&semantic_types=" + allClassSemanticTypeValue[16-1]+longest_only;//+longest_only
					HttpUtil myTechniqueHttpUtil=new HttpUtil("GET",techniqueUrl);
					myTechniqueHttpUtil.excute();   //执行http GET 请求
					
					try {
						//System.out.println("add annotated concept 1");
						//annotatedJson = new JSONObject(EntityUtils.toString(myHttpUtil.mRes.getEntity()));//整个的JSONObject
						JSONArray annotatedJsonArr = null;
						//System.out.println("add annotated concept 2");
						//annotatedJson.toJSONArray(annotatedJsonArr);
						annotatedJsonArr= new JSONArray(EntityUtils.toString(myTechniqueHttpUtil.mRes.getEntity()));//整个的JSONObject
						//System.out.println("add annotated concept 3");
						for (int j = 0; j < annotatedJsonArr.length(); j++) {//annotatedJsonArr.length()
							//System.out.println("add annotated concept 4");
							JSONObject annotatedItem = annotatedJsonArr.getJSONObject(j);
							JSONArray annotations= annotatedItem.getJSONArray("annotations");
							JSONObject mainAnnotation= annotations.getJSONObject(0);
							//System.out.println("add annotated concept 8");
							int fromPosition= mainAnnotation.getInt("from");//取到标注信息
							//System.out.println("add annotated concept 6");
							int toPosition= mainAnnotation.getInt("to");
							String matchType= mainAnnotation.getString("matchType");
							//System.out.println("add annotated concept 7");
							String text= mainAnnotation.getString("text");
							if(!techniqueConceptList.contains(text)){     //将标注出来的concept存到链表里。
								//System.out.println("add annotated concept 5");
								techniqueConceptList.add(text);
								//System.out.println("add annotated concept "+text);
							}
						}
						
						
					} catch (org.apache.http.ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					String virusUrl="http://data.bioontology.org/annotator?text="+body+"&semantic_types=" + allClassSemanticTypeValue[18-1]+longest_only;//+longest_only
					HttpUtil myVirusHttpUtil=new HttpUtil("GET",virusUrl);
					myVirusHttpUtil.excute();   //执行http GET 请求
					
					try {
						//System.out.println("add annotated concept 1");
						//annotatedJson = new JSONObject(EntityUtils.toString(myHttpUtil.mRes.getEntity()));//整个的JSONObject
						JSONArray annotatedJsonArr = null;
						//System.out.println("add annotated concept 2");
						//annotatedJson.toJSONArray(annotatedJsonArr);
						annotatedJsonArr= new JSONArray(EntityUtils.toString(myVirusHttpUtil.mRes.getEntity()));//整个的JSONObject
						//System.out.println("add annotated concept 3");
						for (int j = 0; j < annotatedJsonArr.length(); j++) {//annotatedJsonArr.length()
							//System.out.println("add annotated concept 4");
							JSONObject annotatedItem = annotatedJsonArr.getJSONObject(j);
							JSONArray annotations= annotatedItem.getJSONArray("annotations");
							JSONObject mainAnnotation= annotations.getJSONObject(0);
							//System.out.println("add annotated concept 8");
							int fromPosition= mainAnnotation.getInt("from");//取到标注信息
							//System.out.println("add annotated concept 6");
							int toPosition= mainAnnotation.getInt("to");
							String matchType= mainAnnotation.getString("matchType");
							//System.out.println("add annotated concept 7");
							String text= mainAnnotation.getString("text");
							if(!virusConceptList.contains(text)){     //将标注出来的concept存到链表里。
								//System.out.println("add annotated concept 5");
								virusConceptList.add(text);
								//System.out.println("add annotated concept "+text);
							}
						}
						
						
					} catch (org.apache.http.ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					String RNAUrl="http://data.bioontology.org/annotator?text="+body+"&semantic_types=" + allClassSemanticTypeValue[21-1]+longest_only;//+longest_only
					HttpUtil myRNAHttpUtil=new HttpUtil("GET",RNAUrl);
					myRNAHttpUtil.excute();   //执行http GET 请求
					
					try {
						//System.out.println("add annotated concept 1");
						//annotatedJson = new JSONObject(EntityUtils.toString(myHttpUtil.mRes.getEntity()));//整个的JSONObject
						JSONArray annotatedJsonArr = null;
						//System.out.println("add annotated concept 2");
						//annotatedJson.toJSONArray(annotatedJsonArr);
						annotatedJsonArr= new JSONArray(EntityUtils.toString(myRNAHttpUtil.mRes.getEntity()));//整个的JSONObject
						//System.out.println("add annotated concept 3");
						for (int j = 0; j < annotatedJsonArr.length(); j++) {//annotatedJsonArr.length()
							//System.out.println("add annotated concept 4");
							JSONObject annotatedItem = annotatedJsonArr.getJSONObject(j);
							JSONArray annotations= annotatedItem.getJSONArray("annotations");
							JSONObject mainAnnotation= annotations.getJSONObject(0);
							//System.out.println("add annotated concept 8");
							int fromPosition= mainAnnotation.getInt("from");//取到标注信息
							//System.out.println("add annotated concept 6");
							int toPosition= mainAnnotation.getInt("to");
							String matchType= mainAnnotation.getString("matchType");
							//System.out.println("add annotated concept 7");
							String text= mainAnnotation.getString("text");
							if(!RNAConceptList.contains(text)){     //将标注出来的concept存到链表里。
								//System.out.println("add annotated concept 5");
								RNAConceptList.add(text);
								//System.out.println("add annotated concept "+text);
							}
						}
						
						
					} catch (org.apache.http.ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					String familyUrl="http://data.bioontology.org/annotator?text="+body+"&semantic_types=" + allClassSemanticTypeValue[23-1]+longest_only;//+longest_only
					HttpUtil myFamilyHttpUtil=new HttpUtil("GET",familyUrl);
					myFamilyHttpUtil.excute();   //执行http GET 请求
					
					try {
						//System.out.println("add annotated concept 1");
						//annotatedJson = new JSONObject(EntityUtils.toString(myHttpUtil.mRes.getEntity()));//整个的JSONObject
						JSONArray annotatedJsonArr = null;
						//System.out.println("add annotated concept 2");
						//annotatedJson.toJSONArray(annotatedJsonArr);
						annotatedJsonArr= new JSONArray(EntityUtils.toString(myFamilyHttpUtil.mRes.getEntity()));//整个的JSONObject
						//System.out.println("add annotated concept 3");
						for (int j = 0; j < annotatedJsonArr.length(); j++) {//annotatedJsonArr.length()
							//System.out.println("add annotated concept 4");
							JSONObject annotatedItem = annotatedJsonArr.getJSONObject(j);
							JSONArray annotations= annotatedItem.getJSONArray("annotations");
							JSONObject mainAnnotation= annotations.getJSONObject(0);
							//System.out.println("add annotated concept 8");
							int fromPosition= mainAnnotation.getInt("from");//取到标注信息
							//System.out.println("add annotated concept 6");
							int toPosition= mainAnnotation.getInt("to");
							String matchType= mainAnnotation.getString("matchType");
							//System.out.println("add annotated concept 7");
							String text= mainAnnotation.getString("text");
							if(!familyConceptList.contains(text)){     //将标注出来的concept存到链表里。
								//System.out.println("add annotated concept 5");
								familyConceptList.add(text);
								//System.out.println("add annotated concept "+text);
							}
						}
						
						
					} catch (org.apache.http.ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					String bacteriumUrl="http://data.bioontology.org/annotator?text="+body+"&semantic_types=" + allClassSemanticTypeValue[25-1]+longest_only;//+longest_only
					HttpUtil myBacteriumHttpUtil=new HttpUtil("GET",bacteriumUrl);
					myBacteriumHttpUtil.excute();   //执行http GET 请求
					
					try {
						//System.out.println("add annotated concept 1");
						//annotatedJson = new JSONObject(EntityUtils.toString(myHttpUtil.mRes.getEntity()));//整个的JSONObject
						JSONArray annotatedJsonArr = null;
						//System.out.println("add annotated concept 2");
						//annotatedJson.toJSONArray(annotatedJsonArr);
						annotatedJsonArr= new JSONArray(EntityUtils.toString(myBacteriumHttpUtil.mRes.getEntity()));//整个的JSONObject
						//System.out.println("add annotated concept 3");
						for (int j = 0; j < annotatedJsonArr.length(); j++) {//annotatedJsonArr.length()
							//System.out.println("add annotated concept 4");
							JSONObject annotatedItem = annotatedJsonArr.getJSONObject(j);
							JSONArray annotations= annotatedItem.getJSONArray("annotations");
							JSONObject mainAnnotation= annotations.getJSONObject(0);
							//System.out.println("add annotated concept 8");
							int fromPosition= mainAnnotation.getInt("from");//取到标注信息
							//System.out.println("add annotated concept 6");
							int toPosition= mainAnnotation.getInt("to");
							String matchType= mainAnnotation.getString("matchType");
							//System.out.println("add annotated concept 7");
							String text= mainAnnotation.getString("text");
							if(!bacteriumConceptList.contains(text)){     //将标注出来的concept存到链表里。
								//System.out.println("add annotated concept 5");
								bacteriumConceptList.add(text);
								//System.out.println("add annotated concept "+text);
							}
						}
						
						
					} catch (org.apache.http.ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					String componentUrl="http://data.bioontology.org/annotator?text="+body+"&semantic_types=" + allClassSemanticTypeValue[27-1]+longest_only;//+longest_only
					HttpUtil myComponentHttpUtil=new HttpUtil("GET",componentUrl);
					myComponentHttpUtil.excute();   //执行http GET 请求
					
					try {
						//System.out.println("add annotated concept 1");
						//annotatedJson = new JSONObject(EntityUtils.toString(myHttpUtil.mRes.getEntity()));//整个的JSONObject
						JSONArray annotatedJsonArr = null;
						//System.out.println("add annotated concept 2");
						//annotatedJson.toJSONArray(annotatedJsonArr);
						annotatedJsonArr= new JSONArray(EntityUtils.toString(myComponentHttpUtil.mRes.getEntity()));//整个的JSONObject
						//System.out.println("add annotated concept 3");
						for (int j = 0; j < annotatedJsonArr.length(); j++) {//annotatedJsonArr.length()
							//System.out.println("add annotated concept 4");
							JSONObject annotatedItem = annotatedJsonArr.getJSONObject(j);
							JSONArray annotations= annotatedItem.getJSONArray("annotations");
							JSONObject mainAnnotation= annotations.getJSONObject(0);
							//System.out.println("add annotated concept 8");
							int fromPosition= mainAnnotation.getInt("from");//取到标注信息
							//System.out.println("add annotated concept 6");
							int toPosition= mainAnnotation.getInt("to");
							String matchType= mainAnnotation.getString("matchType");
							//System.out.println("add annotated concept 7");
							String text= mainAnnotation.getString("text");
							if(!componentConceptList.contains(text)){     //将标注出来的concept存到链表里。
								//System.out.println("add annotated concept 5");
								componentConceptList.add(text);
								//System.out.println("add annotated concept "+text);
							}
						}
						
						
					} catch (org.apache.http.ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					String sequenceUrl="http://data.bioontology.org/annotator?text="+body+"&semantic_types=" + allClassSemanticTypeValue[32-1]+longest_only;//+longest_only
					HttpUtil mySequenceHttpUtil=new HttpUtil("GET",sequenceUrl);
					mySequenceHttpUtil.excute();   //执行http GET 请求
					
					try {
						//System.out.println("add annotated concept 1");
						//annotatedJson = new JSONObject(EntityUtils.toString(myHttpUtil.mRes.getEntity()));//整个的JSONObject
						JSONArray annotatedJsonArr = null;
						//System.out.println("add annotated concept 2");
						//annotatedJson.toJSONArray(annotatedJsonArr);
						annotatedJsonArr= new JSONArray(EntityUtils.toString(mySequenceHttpUtil.mRes.getEntity()));//整个的JSONObject
						//System.out.println("add annotated concept 3");
						for (int j = 0; j < annotatedJsonArr.length(); j++) {//annotatedJsonArr.length()
							//System.out.println("add annotated concept 4");
							JSONObject annotatedItem = annotatedJsonArr.getJSONObject(j);
							JSONArray annotations= annotatedItem.getJSONArray("annotations");
							JSONObject mainAnnotation= annotations.getJSONObject(0);
							//System.out.println("add annotated concept 8");
							int fromPosition= mainAnnotation.getInt("from");//取到标注信息
							//System.out.println("add annotated concept 6");
							int toPosition= mainAnnotation.getInt("to");
							String matchType= mainAnnotation.getString("matchType");
							//System.out.println("add annotated concept 7");
							String text= mainAnnotation.getString("text");
							if(!sequenceConceptList.contains(text)){     //将标注出来的concept存到链表里。
								//System.out.println("add annotated concept 5");
								sequenceConceptList.add(text);
								//System.out.println("add annotated concept "+text);
							}
						}
						
						
					} catch (org.apache.http.ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					String enzymatic_activityUrl="http://data.bioontology.org/annotator?text="+body+"&semantic_types=" + allClassSemanticTypeValue[33-1]+longest_only;//+longest_only
					HttpUtil myEnzymatic_activityHttpUtil=new HttpUtil("GET",enzymatic_activityUrl);
					myEnzymatic_activityHttpUtil.excute();   //执行http GET 请求
					
					try {
						//System.out.println("add annotated concept 1");
						//annotatedJson = new JSONObject(EntityUtils.toString(myHttpUtil.mRes.getEntity()));//整个的JSONObject
						JSONArray annotatedJsonArr = null;
						//System.out.println("add annotated concept 2");
						//annotatedJson.toJSONArray(annotatedJsonArr);
						annotatedJsonArr= new JSONArray(EntityUtils.toString(myEnzymatic_activityHttpUtil.mRes.getEntity()));//整个的JSONObject
						//System.out.println("add annotated concept 3");
						for (int j = 0; j < annotatedJsonArr.length(); j++) {//annotatedJsonArr.length()
							//System.out.println("add annotated concept 4");
							JSONObject annotatedItem = annotatedJsonArr.getJSONObject(j);
							JSONArray annotations= annotatedItem.getJSONArray("annotations");
							JSONObject mainAnnotation= annotations.getJSONObject(0);
							//System.out.println("add annotated concept 8");
							int fromPosition= mainAnnotation.getInt("from");//取到标注信息
							//System.out.println("add annotated concept 6");
							int toPosition= mainAnnotation.getInt("to");
							String matchType= mainAnnotation.getString("matchType");
							//System.out.println("add annotated concept 7");
							String text= mainAnnotation.getString("text");
							if(!enzymatic_activityConceptList.contains(text)){     //将标注出来的concept存到链表里。
								//System.out.println("add annotated concept 5");
								enzymatic_activityConceptList.add(text);
								//System.out.println("add annotated concept "+text);
							}
						}
						
						
					} catch (org.apache.http.ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					String hormoneUrl="http://data.bioontology.org/annotator?text="+body+"&semantic_types=" + allClassSemanticTypeValue[34-1]+longest_only;//+longest_only
					HttpUtil myHormoneHttpUtil=new HttpUtil("GET",hormoneUrl);
					myHormoneHttpUtil.excute();   //执行http GET 请求
					
					try {
						//System.out.println("add annotated concept 1");
						//annotatedJson = new JSONObject(EntityUtils.toString(myHttpUtil.mRes.getEntity()));//整个的JSONObject
						JSONArray annotatedJsonArr = null;
						//System.out.println("add annotated concept 2");
						//annotatedJson.toJSONArray(annotatedJsonArr);
						annotatedJsonArr= new JSONArray(EntityUtils.toString(myHormoneHttpUtil.mRes.getEntity()));//整个的JSONObject
						//System.out.println("add annotated concept 3");
						for (int j = 0; j < annotatedJsonArr.length(); j++) {//annotatedJsonArr.length()
							//System.out.println("add annotated concept 4");
							JSONObject annotatedItem = annotatedJsonArr.getJSONObject(j);
							JSONArray annotations= annotatedItem.getJSONArray("annotations");
							JSONObject mainAnnotation= annotations.getJSONObject(0);
							//System.out.println("add annotated concept 8");
							int fromPosition= mainAnnotation.getInt("from");//取到标注信息
							//System.out.println("add annotated concept 6");
							int toPosition= mainAnnotation.getInt("to");
							String matchType= mainAnnotation.getString("matchType");
							//System.out.println("add annotated concept 7");
							String text= mainAnnotation.getString("text");
							if(!hormoneConceptList.contains(text)){     //将标注出来的concept存到链表里。
								//System.out.println("add annotated concept 5");
								hormoneConceptList.add(text);
								//System.out.println("add annotated concept "+text);
							}
						}
						
						
					} catch (org.apache.http.ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					String tissueUrl="http://data.bioontology.org/annotator?text="+body+"&semantic_types=" + allClassSemanticTypeValue[35-1]+longest_only;//+longest_only
					HttpUtil myTissueHttpUtil=new HttpUtil("GET",tissueUrl);
					myTissueHttpUtil.excute();   //执行http GET 请求
					
					try {
						//System.out.println("add annotated concept 1");
						//annotatedJson = new JSONObject(EntityUtils.toString(myHttpUtil.mRes.getEntity()));//整个的JSONObject
						JSONArray annotatedJsonArr = null;
						//System.out.println("add annotated concept 2");
						//annotatedJson.toJSONArray(annotatedJsonArr);
						annotatedJsonArr= new JSONArray(EntityUtils.toString(myTissueHttpUtil.mRes.getEntity()));//整个的JSONObject
						//System.out.println("add annotated concept 3");
						for (int j = 0; j < annotatedJsonArr.length(); j++) {//annotatedJsonArr.length()
							//System.out.println("add annotated concept 4");
							JSONObject annotatedItem = annotatedJsonArr.getJSONObject(j);
							JSONArray annotations= annotatedItem.getJSONArray("annotations");
							JSONObject mainAnnotation= annotations.getJSONObject(0);
							//System.out.println("add annotated concept 8");
							int fromPosition= mainAnnotation.getInt("from");//取到标注信息
							//System.out.println("add annotated concept 6");
							int toPosition= mainAnnotation.getInt("to");
							String matchType= mainAnnotation.getString("matchType");
							//System.out.println("add annotated concept 7");
							String text= mainAnnotation.getString("text");
							if(!tissueConceptList.contains(text)){     //将标注出来的concept存到链表里。
								//System.out.println("add annotated concept 5");
								tissueConceptList.add(text);
								//System.out.println("add annotated concept "+text);
							}
						}
						
						
					} catch (org.apache.http.ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					String tumorUrl="http://data.bioontology.org/annotator?text="+body+"&semantic_types=" + allClassSemanticTypeValue[36-1]+longest_only;//+longest_only
					HttpUtil myTumorHttpUtil=new HttpUtil("GET",tumorUrl);
					myTumorHttpUtil.excute();   //执行http GET 请求
					
					try {
						//System.out.println("add annotated concept 1");
						//annotatedJson = new JSONObject(EntityUtils.toString(myHttpUtil.mRes.getEntity()));//整个的JSONObject
						JSONArray annotatedJsonArr = null;
						//System.out.println("add annotated concept 2");
						//annotatedJson.toJSONArray(annotatedJsonArr);
						annotatedJsonArr= new JSONArray(EntityUtils.toString( myTumorHttpUtil.mRes.getEntity()));//整个的JSONObject
						//System.out.println("add annotated concept 3");
						for (int j = 0; j < annotatedJsonArr.length(); j++) {//annotatedJsonArr.length()
							//System.out.println("add annotated concept 4");
							JSONObject annotatedItem = annotatedJsonArr.getJSONObject(j);
							JSONArray annotations= annotatedItem.getJSONArray("annotations");
							JSONObject mainAnnotation= annotations.getJSONObject(0);
							//System.out.println("add annotated concept 8");
							int fromPosition= mainAnnotation.getInt("from");//取到标注信息
							//System.out.println("add annotated concept 6");
							int toPosition= mainAnnotation.getInt("to");
							String matchType= mainAnnotation.getString("matchType");
							//System.out.println("add annotated concept 7");
							String text= mainAnnotation.getString("text");
							if(!tumorConceptList.contains(text)){     //将标注出来的concept存到链表里。
								//System.out.println("add annotated concept 5");
								tumorConceptList.add(text);
								//System.out.println("add annotated concept "+text);
							}
						}
						
						
					} catch (org.apache.http.ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					try {
						if (questionType.equals("factoid")|| questionType.equals("list")) {
							outFour.write(String.valueOf(chemicalConceptList.size())+" "+String.valueOf(0)+" "+String.valueOf(diseaseConceptList.size())+" "+String.valueOf(drugConceptList.size())+" "+String.valueOf(enzymeConceptList.size())+" "+String.valueOf(functionConceptList.size())+" "+String.valueOf(geneConceptList.size())+" "+String.valueOf(0)+" "+String.valueOf(locationConceptList.size())+" "+String.valueOf(0)+" "+String.valueOf(cellConceptList.size())+" "+String.valueOf(numberConceptList.size())+" "+String.valueOf(proteinConceptList.size())+" "+String.valueOf(receptorConceptList.size())+" "+String.valueOf(symptomConceptList.size())+" "+String.valueOf(techniqueConceptList.size())+" "+String.valueOf(0)+" "+String.valueOf(virusConceptList.size())+" "+String.valueOf(0)+" "+String.valueOf(0)+" "+String.valueOf(RNAConceptList.size())+" "+String.valueOf(0)+" "+String.valueOf(familyConceptList.size())+" "+String.valueOf(0)+" "+String.valueOf(bacteriumConceptList.size())+" "+String.valueOf(0)+" "+String.valueOf(componentConceptList.size())+" "+String.valueOf(0)+" "+String.valueOf(0)+" "+String.valueOf(0)+" "+String.valueOf(0)+" "+String.valueOf(sequenceConceptList.size())+" "+String.valueOf(enzymatic_activityConceptList.size())+" "+String.valueOf(hormoneConceptList.size())+" "+String.valueOf(tissueConceptList.size())+" "+String.valueOf(tumorConceptList.size())+" "+String.valueOf(0));
							//System.out.println("come on!!");
							outFour.newLine();
						}
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					
					JSONArray snippetsJsonArr=jsonObjQues.getJSONArray("snippets");  //得到某个问题的snippets
					if (snippetsJsonArr.length()>0) {
						int relatedGrade[] = new int[snippetsJsonArr.length()];
						String allSnippetText[] = new String[snippetsJsonArr.length()];
						LinkedList<String> allSnippetConceptList = new LinkedList<String>(); //用一个all concept List
						int[] conceptCount = new int[800];
						int[] inGoldAnswerSet = new int[800];
						int[] numOfTokensEachCAO = new int[800];   //每个CAO中有多少个token
						float[] tokenOverlapCountPercentage = new float[800];
						float[] conceptOverlapCountPercentage = new float[800];
						int[] conceptProximityOfEachCAO = new int[800];   
						int[] tokenProximityOfEachCAO = new int[800];
						float averageNumOfTokensEachCAO=0.0f;
						int allSnippetsTotalNumOfTokensEachCAO=0;
						for (int a = 0; a < 800; a++) {
							conceptCount[a] = 0;
							inGoldAnswerSet[a]=0;
							numOfTokensEachCAO[a]=0;
							tokenOverlapCountPercentage[a]=0.0f;
							conceptOverlapCountPercentage[a]=0.0f;
							conceptProximityOfEachCAO[a]=20;
							tokenProximityOfEachCAO[a]=20;
						}
						for (int k = 0; k < snippetsJsonArr.length(); k++) {
							//System.out.println("add annotated concept 5");
							JSONObject jsonObjSnippet = snippetsJsonArr.getJSONObject(k); //得到每一个snippet
							String snippetText = jsonObjSnippet.getString("text"); //得到每一个snippet text
							String [] wordsofsnippetText=snippetText.split(regex);
							relatedGrade[k] = 0;//相关性系数
							allSnippetText[k] = snippetText;
							String snippetTextBody = null;
							try {
								snippetTextBody = URLEncoder.encode(snippetText, "utf-8");
							} catch (UnsupportedEncodingException e2) {
								// TODO Auto-generated catch block
								e2.printStackTrace();
							}
							String semantic_types = null;
							if (!(semanticType == null)) {
								semantic_types = "&semantic_types=" + semanticType;
							}
							String expand_semantic_types = "&expand_semantic_types_hierarchy=true";
							String snippetUrl = "http://data.bioontology.org/annotator?text=" + snippetTextBody
									+ semantic_types ;//+longest_only+ longest_only
							HttpUtil snippetHttpUtil = new HttpUtil("GET", snippetUrl);
							//System.out.println("semantic_types is "+semantic_types);
							snippetHttpUtil.excute(); //执行http GET 请求
							//System.out.println("add annotated snippet concept 1");
							LinkedList<String> snippetConceptList = new LinkedList<String>(); //每一个snippet用一个concept List存储该snippet中标出来的concept
							int[] snippetConceptWordPositionArray = new int[500];
							
							LinkedList<String> snippetConceptInAlsoQuestionList = new LinkedList<String>();
							int[] snippetConceptInAlsoQuestionWordPositionArray = new int[500];
							int[] snippetTokenInAlsoQuestionWordPositionArray = new int[500];
							int currentPointerOfSnippetTokenInAlsoQuestion=0;    //当做一个下标指针
							for(int d=0;d<words.length;d++){
								for(int sd=0;sd<wordsofsnippetText.length;sd++){
									if(words[d].equals(wordsofsnippetText[sd])){
										snippetTokenInAlsoQuestionWordPositionArray[currentPointerOfSnippetTokenInAlsoQuestion]=sd;
										currentPointerOfSnippetTokenInAlsoQuestion++;
									}
								}
							}
							for (int a = 0; a < 500; a++) {
								snippetConceptWordPositionArray[a]=0;
								snippetConceptInAlsoQuestionWordPositionArray[a]=0;
							}
							JSONArray annotatedSnippetJsonArr = null;
							try {
								System.out.println("add annotated snippet concept 2");
								//System.out.println(" result is "+EntityUtils.toString(snippetHttpUtil.mRes.getEntity()));
								annotatedSnippetJsonArr = new JSONArray(
										EntityUtils.toString(snippetHttpUtil.mRes.getEntity()));
								System.out.println(" result number is "+String.valueOf(annotatedSnippetJsonArr.length()));
								for (int j = 0; j < annotatedSnippetJsonArr.length(); j++) {
									//System.out.println("add annotated snippet concept 3");
									JSONObject annotatedItem = annotatedSnippetJsonArr.getJSONObject(j);
									JSONArray annotations = annotatedItem.getJSONArray("annotations");
									JSONObject mainAnnotation = annotations.getJSONObject(0);
									//System.out.println(" mainAnnotation is "+mainAnnotation.toString());
									String text = mainAnnotation.getString("text");
									System.out.println("annotated text is "+text);
									int wordPosition=0;
									for(int p=0;p<wordsofsnippetText.length;p++){
										if(text.equals(wordsofsnippetText[p])){
											wordPosition=p;
										}
									}
									int fromPosition= mainAnnotation.getInt("from");//取到标注信息
									//System.out.println("add annotated concept 6");
									int toPosition= mainAnnotation.getInt("to");
									int midPosition=(fromPosition+toPosition)/2;
									if ((!snippetConceptList.contains(text)) && (!conceptList.contains(text))
											&& (!text.equals("NULL")) && (!text.equals("here"))
											&& (!text.equals("efficient")) && (!text.equals(semanticType))
											&& (!text.equals("large")) && (!text.equals("have"))
											&& (!text.equals("evaluate")) ) { //将标注出来的concept存到链表里。
										System.out.println("add annotated snippet concept 5");
										snippetConceptList.add(text);
										snippetConceptWordPositionArray[snippetConceptList.indexOf(text)]=wordPosition;
										System.out.println("add annotated concept 7");
										System.out.println("add annotated snippet concept "+text);
									}
									else if(conceptList.contains(text)){
										System.out.println("add annotated concept 8");
										snippetConceptInAlsoQuestionList.add(text);
										snippetConceptInAlsoQuestionWordPositionArray[snippetConceptInAlsoQuestionList.indexOf(text)]=wordPosition;
									}
								}

							} catch (org.apache.http.ParseException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} 
							String regexOfCAO="[\\s\\p{Punct}]+";
							System.out.println("add annotated concept 16");
							if (questionType.equals("factoid")||questionType.equals("list")) {
								//jsonQuesExactAnswer = jsonObjQues.getJSONArray("exact_answer");
								System.out.println("add annotated concept 17");
							}
							System.out.println("snippetConceptList.size is "+snippetConceptList.size());
								for (int n = 0; n < snippetConceptList.size(); n++) {
									String[] wordsofCAO = snippetConceptList.get(n).split(regexOfCAO);
									if (allSnippetConceptList.contains(snippetConceptList.get(n))) {
										//System.out.println("add annotated snippet concept 6");
										System.out.println("add annotated concept 13");
										conceptCount[allSnippetConceptList.indexOf(snippetConceptList.get(n))]++;

									} else {
										allSnippetConceptList.add(snippetConceptList.get(n));
										conceptCount[allSnippetConceptList.indexOf(snippetConceptList.get(n))]++;
										if (questionType.equals("factoid")||questionType.equals("list")) {
											//System.out.println(
											//		"jsonQuesExactAnswer.length:" + jsonQuesExactAnswer.length());
											
//											if (jsonQuesExactAnswer.length()>1) {
//												for (int k1 = 0; k1 < jsonQuesExactAnswer.length(); k1++) {
//													JSONArray jsonExactAnswerSynonItem = jsonQuesExactAnswer
//															.getJSONArray(k1);
//													System.out.println("add annotated concept 9");
//													for (int k2 = 0; k2 < jsonExactAnswerSynonItem.length(); k2++) {
//														System.out.println("add annotated concept 10");
//														String exactAnswerItem = jsonExactAnswerSynonItem
//																.getString(k2);
//														System.out.println("exactAnswerItem is " + exactAnswerItem);
//														if (snippetConceptList.get(n).equals(exactAnswerItem)) {
//															inGoldAnswerSet[allSnippetConceptList
//																	.indexOf(snippetConceptList.get(n))] = 1;
//															break;
//														}
//													}
//													if (inGoldAnswerSet[allSnippetConceptList
//															.indexOf(snippetConceptList.get(n))] == 1) {
//														break;
//													}
//												} 
//											}
//											else if (jsonQuesExactAnswer.length()==1){
//												//JSONArray jsonExactAnswerSynonItem = jsonQuesExactAnswer
//														//.getJSONArray(0);
//												for (int k2 = 0; k2 < jsonQuesExactAnswer.length(); k2++) {
//													System.out.println("add annotated concept 10");
//													String exactAnswerItem = jsonQuesExactAnswer
//															.getString(k2);
//													System.out.println("exactAnswerItem is " + exactAnswerItem);
//													if (snippetConceptList.get(n).equals(exactAnswerItem)) {
//														inGoldAnswerSet[allSnippetConceptList
//																.indexOf(snippetConceptList.get(n))] = 1;
//														break;
//													}
//												}
//												if (inGoldAnswerSet[allSnippetConceptList
//														.indexOf(snippetConceptList.get(n))] == 1) {
//													break;
//												}
//											}
											System.out.println("add annotated concept 11");
											numOfTokensEachCAO[allSnippetConceptList
													.indexOf(snippetConceptList.get(n))] = wordsofCAO.length;
											int tokenOverlapCount = 0;
											int conceptOverlapCount = 0;
											for (int m = 0; m < wordsofCAO.length; m++) {
												for (int q = 0; q < words.length; q++) {
													if (wordsofCAO[m].equals(words[q])) {
														tokenOverlapCount++;
													}
												}

											}
											//for(int m=0;m<wordsofCAO.length;m++){
											for (int q = 0; q < conceptList.size(); q++) {
												//System.out.println("add annotated concept 12");
												if (snippetConceptList.get(n).equals(conceptList.get(q))) {
													conceptOverlapCount++;
												}
											}
											//}
											tokenOverlapCountPercentage[allSnippetConceptList
													.indexOf(snippetConceptList.get(n))] = tokenOverlapCount
															/ wordsofCAO.length;
											conceptOverlapCountPercentage[allSnippetConceptList
													.indexOf(snippetConceptList.get(n))] = conceptOverlapCount;
											int minConceptDistance = 20;
											for (int p = 0; p < snippetConceptInAlsoQuestionWordPositionArray.length; p++) {
												if (Math.abs(snippetConceptInAlsoQuestionWordPositionArray[p]
														- snippetConceptWordPositionArray[n]) < 10) {
													minConceptDistance = Math
															.abs(snippetConceptInAlsoQuestionWordPositionArray[p]
																	- snippetConceptWordPositionArray[n]);
												}
											}
											conceptProximityOfEachCAO[allSnippetConceptList
													.indexOf(snippetConceptList.get(n))] = minConceptDistance;
											int minTokenDistance = 20;
											for (int p = 0; p < snippetTokenInAlsoQuestionWordPositionArray.length; p++) {
												if (Math.abs(snippetTokenInAlsoQuestionWordPositionArray[p]
														- snippetConceptWordPositionArray[n]) < minTokenDistance) {
													minTokenDistance = Math
															.abs(snippetTokenInAlsoQuestionWordPositionArray[p]
																	- snippetConceptWordPositionArray[n]);
												}
											}
											tokenProximityOfEachCAO[allSnippetConceptList
													.indexOf(snippetConceptList.get(n))] = minTokenDistance;
										}

									}
								}
								for (int n = 0; n < conceptList.size(); n++) {
									if (snippetConceptList.contains(conceptList.get(n))) {
										relatedGrade[k]++;
									}
								} 
							}

						
						int maxTag = 0;//选出allSnippetConceptList中最重要的concept
						for (int a = 0; a < allSnippetConceptList.size(); a++) {
							allSnippetsTotalNumOfTokensEachCAO+=numOfTokensEachCAO[a];
							if (conceptCount[maxTag] < conceptCount[a]) {
								System.out.println("add annotated snippet concept conceptCount[maxTag] is "
										+ conceptCount[maxTag] + "maxTag is " + a);
								maxTag = a;
							}
						}
						averageNumOfTokensEachCAO=0;//allSnippetsTotalNumOfTokensEachCAO/allSnippetConceptList.size()
						if (questionType.equals("factoid")||questionType.equals("list")) {
							for (int a = 0; a < allSnippetConceptList.size(); a++) {
								try {
									if (questionType.equals("factoid") || questionType.equals("list")) { //String.valueOf(inGoldAnswerSet[a]) +
										String scoringData = null;
										if (inGoldAnswerSet[a]==1) {
											scoringData = String.valueOf(1) + " " + String.valueOf(1) + ":"
													+ String.valueOf(conceptCount[a]) + " " + String.valueOf(2)
													+ ":" + String.valueOf(allSnippetConceptList.size()) + " "
													+ String.valueOf(3) + ":"
													+ String.valueOf(averageNumOfTokensEachCAO) + " "
													+ String.valueOf(4) + ":"
													+ String.valueOf(tokenOverlapCountPercentage[a]) + " "
													+ String.valueOf(5) + ":"
													+ String.valueOf(conceptOverlapCountPercentage[a]) + " "
													+ String.valueOf(6) + ":"
													+ String.valueOf(tokenProximityOfEachCAO[a]) + " "
													+ String.valueOf(7) + ":"
													+ String.valueOf(conceptProximityOfEachCAO[a]);
										}
										else if(inGoldAnswerSet[a]==0){
											scoringData = String.valueOf(0) + " " + String.valueOf(1) + ":"
													+ String.valueOf(conceptCount[a]) + " " + String.valueOf(2)
													+ ":" + String.valueOf(allSnippetConceptList.size()) + " "
													+ String.valueOf(3) + ":"
													+ String.valueOf(averageNumOfTokensEachCAO) + " "
													+ String.valueOf(4) + ":"
													+ String.valueOf(tokenOverlapCountPercentage[a]) + " "
													+ String.valueOf(5) + ":"
													+ String.valueOf(conceptOverlapCountPercentage[a]) + " "
													+ String.valueOf(6) + ":"
													+ String.valueOf(tokenProximityOfEachCAO[a]) + " "
													+ String.valueOf(7) + ":"
													+ String.valueOf(conceptProximityOfEachCAO[a]);
										}
										//outTwo.write(scoringData);
										System.out.println(scoringData);  //allSnippetConceptList.get(a)+" "+  
										//System.out.println("come on!!");  
										//outTwo.newLine();
									}
								} catch (Exception e1) {
									// TODO Auto-generated catch block
									e1.printStackTrace();
								}
							} 
							try {
								System.out.println();
								//outTwo.write(questionBody);
								//outTwo.newLine();
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
						//outTwo.close();
						//outOne.close();
						int questionNum = i + 1;
						if (questionType.equals("factoid")) {//||questionType.equals("list")
							JSONArray factoidAnswerList = new JSONArray();
							JSONArray factoidAnswerListItem1 = new JSONArray();
							JSONArray factoidAnswerListItem2 = new JSONArray();
							JSONArray factoidAnswerListItem3 = new JSONArray();
							JSONArray factoidAnswerListItem4 = new JSONArray();
							JSONArray factoidAnswerListItem5 = new JSONArray();
							if (isChoiceQuestion == false && isRANKLQuestion == false && isQuantityQuestion == false) {
							  if (allSnippetConceptList.size()>0) {
								System.out.println("the exact_answer of " + questionNum + " question is"
										+ allSnippetConceptList.get(maxTag));
								if (isInherModeQuestion == false) {
									//System.out.println("add annotated snippet concept 8");
									factoidAnswerListItem1.put(allSnippetConceptList.get(maxTag));
									factoidAnswerList.put(factoidAnswerListItem1);
									//factoidAnswerList.put(allSnippetConceptList.get(maxTag));
									conceptCount[maxTag] = 0;
									maxTag = 0;
								} else {
									factoidAnswerListItem1.put("autosomal dominant");
									factoidAnswerList.put(factoidAnswerListItem1);
									//factoidAnswerList.put("autosomal dominant");
								}
								System.out.println("add annotated snippet concept 9");
								for (int a = 0; a < allSnippetConceptList.size(); a++) {
									if (conceptCount[maxTag] < conceptCount[a]) {
										//System.out.println("add annotated snippet concept 8");
										maxTag = a;
									}
								}
								System.out.println("the 2nd most import concept of " + questionNum + " question is"
										+ allSnippetConceptList.get(maxTag));
								factoidAnswerListItem2.put(allSnippetConceptList.get(maxTag));
								factoidAnswerList.put(factoidAnswerListItem2);
								//factoidAnswerList.put(allSnippetConceptList.get(maxTag));
								conceptCount[maxTag] = 0;
								maxTag = 0;
								for (int a = 0; a < allSnippetConceptList.size(); a++) {
									if (conceptCount[maxTag] < conceptCount[a]) {
										//System.out.println("add annotated snippet concept 8");
										maxTag = a;
									}
								}
								System.out.println("the 3rd most import concept of " + questionNum + " question is"
										+ allSnippetConceptList.get(maxTag));
								factoidAnswerListItem3.put(allSnippetConceptList.get(maxTag));
								factoidAnswerList.put(factoidAnswerListItem3);
								//factoidAnswerList.put(allSnippetConceptList.get(maxTag));
								conceptCount[maxTag] = 0;
								maxTag = 0;
								for (int a = 0; a < allSnippetConceptList.size(); a++) {
									if (conceptCount[maxTag] < conceptCount[a]) {
										//System.out.println("add annotated snippet concept 8");
										maxTag = a;
									}
								}
								System.out.println("the 4th most import concept of " + questionNum + " question is"
										+ allSnippetConceptList.get(maxTag));
								factoidAnswerListItem4.put(allSnippetConceptList.get(maxTag));
								factoidAnswerList.put(factoidAnswerListItem4);
								//factoidAnswerList.put(allSnippetConceptList.get(maxTag));
								conceptCount[maxTag] = 0;
								maxTag = 0;
								for (int a = 0; a < allSnippetConceptList.size(); a++) {
									if (conceptCount[maxTag] < conceptCount[a]) {
										//System.out.println("add annotated snippet concept 8");
										maxTag = a;
									}
								}
								System.out.println("the 5th most import concept of " + questionNum + " question is"
										+ allSnippetConceptList.get(maxTag));
								factoidAnswerListItem5.put(allSnippetConceptList.get(maxTag));
								factoidAnswerList.put(factoidAnswerListItem5);
								//factoidAnswerList.put(allSnippetConceptList.get(maxTag));
								jsonObjQues.put("exact_answer", factoidAnswerList);
							  }
							  else{
									System.out.println("the exact_answer of " + questionNum
											+ " question's annotated concepts in snippets are null, so choose its answers as default" );
																factoidAnswerListItem1.put("developmental delay");
																factoidAnswerList.put(factoidAnswerListItem1);
																factoidAnswerListItem2.put("epilepsy");
																factoidAnswerList.put(factoidAnswerListItem2);
																factoidAnswerListItem3.put("myelodysplastic syndrome (MDS)");
																factoidAnswerList.put(factoidAnswerListItem3);
																factoidAnswerListItem4.put("trifluridine");
																factoidAnswerList.put(factoidAnswerListItem4);
																factoidAnswerListItem5.put("glioblastoma");
																factoidAnswerList.put(factoidAnswerListItem5);
//									factoidAnswerList.put("one");
//									factoidAnswerList.put("three");
//									factoidAnswerList.put("10~20");
//									factoidAnswerList.put("1:5000");
//									factoidAnswerList.put("160");
									jsonObjQues.put("exact_answer", factoidAnswerList);
								}
								
							}
							/*else if(isRANKLQuestion){
								factoidAnswerListItem1.put("the ligand of RANKL");
								factoidAnswerList.put(factoidAnswerListItem1);
								factoidAnswerListItem2.put("RANKL");
								factoidAnswerList.put(factoidAnswerListItem2);
								factoidAnswerListItem3.put("nuclear factor kappa-B ligand");
								factoidAnswerList.put(factoidAnswerListItem3);
								factoidAnswerListItem4.put("the RANKL");
								factoidAnswerList.put(factoidAnswerListItem4);
								factoidAnswerListItem5.put("ligand");
								factoidAnswerList.put(factoidAnswerListItem5);
								jsonObjQues.put("exact_answer",factoidAnswerList);
							}*/
							else if (isQuantityQuestion) {
								//isQuantityQuestion=true;
								//answerOfQuantityQuestion="one";
								System.out.println("the exact_answer of " + questionNum
										+ " question is a quantity, it is" + answerOfQuantityQuestion);
															factoidAnswerListItem1.put("one");
															factoidAnswerList.put(factoidAnswerListItem1);
															factoidAnswerListItem2.put("three");
															factoidAnswerList.put(factoidAnswerListItem2);
															factoidAnswerListItem3.put("10~20");
															factoidAnswerList.put(factoidAnswerListItem3);
															factoidAnswerListItem4.put("1:5000");
															factoidAnswerList.put(factoidAnswerListItem4);
															factoidAnswerListItem5.put("160");
															factoidAnswerList.put(factoidAnswerListItem5);
//								factoidAnswerList.put("one");
//								factoidAnswerList.put("three");
//								factoidAnswerList.put("10~20");
//								factoidAnswerList.put("1:5000");
//								factoidAnswerList.put("160");
								jsonObjQues.put("exact_answer", factoidAnswerList);
							} else {
								System.out.println(
										"the exact_answer of " + questionNum + " question is" + answerOfChoiceQuestion);
															factoidAnswerListItem1.put(answerOfChoiceQuestion);
															factoidAnswerList.put(factoidAnswerListItem1);
															factoidAnswerListItem2.put(anotherAnswerOfChoiceQuestion);
															factoidAnswerList.put(factoidAnswerListItem2);
								//factoidAnswerList.put(answerOfChoiceQuestion);
								//factoidAnswerList.put(anotherAnswerOfChoiceQuestion);
								jsonObjQues.put("exact_answer", factoidAnswerList);
								//jsonObjQues.put("exact_answer",answerOfChoiceQuestion);
							}
						}
						if (questionType.equals("list")) {
							JSONArray listQuesAnswer = new JSONArray();
							if (!isHandQuestion) {
							  if (allSnippetConceptList.size()>0) {
								//allSnippetConceptList.remove(maxTag);

								System.out.println("the 1st most import concept of " + questionNum + " question is"
										+ allSnippetConceptList.get(maxTag));
								JSONArray firstListQuesAnswer = new JSONArray();
								firstListQuesAnswer.put(allSnippetConceptList.get(maxTag));
								listQuesAnswer.put(firstListQuesAnswer);
								System.out.println("add annotated snippet concept 8");
								for (int m = 1; (m < allSnippetConceptList.size()) && (m < 18); m++) {
									conceptCount[maxTag] = 0;
									maxTag = 0;
									System.out.println("add annotated snippet concept 9");
									for (int a = 0; a < allSnippetConceptList.size(); a++) {
										if (conceptCount[maxTag] < conceptCount[a]) {
											//System.out.println("add annotated snippet concept 8");
											maxTag = a;
										}
									}
									int answerNumtag = m + 1;
									JSONArray thisListQuesAnswer = new JSONArray();
									System.out.println("the " + answerNumtag + " most import concept of " + questionNum
											+ " question is" + allSnippetConceptList.get(maxTag));
									thisListQuesAnswer.put(allSnippetConceptList.get(maxTag));
									listQuesAnswer.put(thisListQuesAnswer);
								}
								jsonObjQues.put("exact_answer", listQuesAnswer);
								/*conceptCount[maxTag] = 0;
								maxTag = 0;
								for (int a = 0; a < allSnippetConceptList.size(); a++) {
									if (conceptCount[maxTag] < conceptCount[a]) {
										//System.out.println("add annotated snippet concept 8");
										maxTag = a;
									}
								}
								System.out.println("the 3rd most import concept of " + questionNum + " question is"
										+ allSnippetConceptList.get(maxTag));
								listQuesAnswer.put(allSnippetConceptList.get(maxTag));*/
							  }
							  else{
								  System.out.println("the exact_answer of " + questionNum
											+ " question's annotated concepts in snippets are null, so choose its answers as default" );
								  JSONArray thisListQuesAnswer = new JSONArray();
								  thisListQuesAnswer.put("non-small cell lung cancer");
								  listQuesAnswer.put(thisListQuesAnswer);
								  JSONArray thisListQuesAnswer1 = new JSONArray();
								  thisListQuesAnswer1.put("glioblastoma");
								  listQuesAnswer.put(thisListQuesAnswer1);
								  JSONArray thisListQuesAnswer2 = new JSONArray();
								  thisListQuesAnswer2.put("interleukin-6");
								  listQuesAnswer.put(thisListQuesAnswer2);
								  JSONArray thisListQuesAnswer3 = new JSONArray();
								  thisListQuesAnswer3.put("lectin complement pathway");
								  listQuesAnswer.put(thisListQuesAnswer3);
								  JSONArray thisListQuesAnswer4 = new JSONArray();
								  thisListQuesAnswer4.put("Robinow syndrome");
								  listQuesAnswer.put(thisListQuesAnswer4);
							  }

							} else {
								listQuesAnswer.put("complex syndactyly with bony fusion involving the index");
								listQuesAnswer.put("polydactyly");
								listQuesAnswer.put("short thumb with radial clinodactyly");
								listQuesAnswer.put("long and ring fingers");
								listQuesAnswer.put("symphalangism");
								listQuesAnswer.put("simple syndactyly of the fourth web space");
								listQuesAnswer.put("intrinsic muscle anomalies");
								listQuesAnswer.put("extrinsic tendon insertions");
								listQuesAnswer.put("neurovascular bundles");
								jsonObjQues.put("exact_answer", listQuesAnswer);
							}
						}
						if (questionType.equals("yesno")) {
							String yesnoAnswer = "yes";
							String noAnswer = "no";
							System.out.println("The exact_answer of " + questionNum + " question is : " + yesnoAnswer);
							jsonObjQues.put("exact_answer", yesnoAnswer);
							//jsonObjQues.put("exact_answer",noAnswer);
						}
						int maxLabel = 0;
						for (int k = 0; k < snippetsJsonArr.length(); k++) {
							if (relatedGrade[k] > relatedGrade[maxLabel]) {
								maxLabel = k;
							}
						}
						if (questionType.equals("summary")) {
							System.out.println("The ideal_answer of " + questionNum + " question is : "
									+ allSnippetText[maxLabel]);
							jsonObjQues.put("ideal_answer", allSnippetText[maxLabel]);
						} else {
							System.out.println("The ideal_answer of " + questionNum + " question is : "
									+ allSnippetText[maxLabel]);
							jsonObjQues.put("ideal_answer", allSnippetText[maxLabel]);
						}
						//System.out.println("The most revelant snippet is : "+allSnippetText[maxLabel]);
						/*int maxLabel[]={0,0,0};//可以把它变成数组，把concept个数同为最多的snippet的序号都存进去！！！！！！！
						int used=0;//当前正在使用的maxLabel项
						
						for(int k=0;k<snippetsJsonArr.length();k++){
							
							if(relatedGrade[k]>relatedGrade[maxLabel[used]]){
								maxLabel[used]=k;
							}
							else if(relatedGrade[k]==relatedGrade[maxLabel[used]]){
								used=(used+1)%3;
								maxLabel[used]=k;
							}
						}
						System.out.println("The most revelant snippet is : ");
						for(int m=0;m<used;m++){
							System.out.println(allSnippetText[maxLabel[m]]);
						}*/
					}
					else{
						JSONArray listQuesAnswer=new JSONArray();
						listQuesAnswer.put("short thumb with radial clinodactyly");
						jsonObjQues.put("exact_answer", listQuesAnswer);
						jsonObjQues.put("ideal_answer", listQuesAnswer);
					}
					
					
				}
				jsonArrAnswerQues.put(jsonObjQues);
				
				
				String modelType  = "medical-en";	// "general-en" or "medical-en"
				//String inputFile  = questionBody;
				String outputParsingQuestion = null;
				
				try
				{
					//DemoNLPDecode A=new DemoNLPDecode(modelType, questionBody,outputParsingQuestion);
					//outputParsingQuestion=A.process(A.tokenizer, A.components, questionBody);
					//System.out.println("outputParsingQuestion is \n"+outputParsingQuestion);
				}
				catch (Exception e) {e.printStackTrace();}
//				String longest_only="&longest_only=true"; //满足最长匹配
//				String url="http://data.bioontology.org/annotator?text="+body+longest_only;
//				HttpUtil myHttpUtil=new HttpUtil("GET",url);
//				myHttpUtil.excute();   //执行http GET 请求
//				/*try {
//					System.out.println("NER result is "+EntityUtils.toString(myHttpUtil.mRes.getEntity()));
//				} catch (org.apache.http.ParseException e1) {
//					// TODO Auto-generated catch block
//					e1.printStackTrace();
//				} catch (IOException e1) {
//					// TODO Auto-generated catch block
//					e1.printStackTrace();
//				}*/
//				LinkedList<String> conceptList=new LinkedList<String>();  //用一个concept List
//				try {
//					//System.out.println("add annotated concept 1");
//					//annotatedJson = new JSONObject(EntityUtils.toString(myHttpUtil.mRes.getEntity()));//整个的JSONObject
//					JSONArray annotatedJsonArr = null;
//					//System.out.println("add annotated concept 2");
//					//annotatedJson.toJSONArray(annotatedJsonArr);
//					annotatedJsonArr= new JSONArray(EntityUtils.toString(myHttpUtil.mRes.getEntity()));//整个的JSONObject
//					//System.out.println("add annotated concept 3");
//					for (int j = 0; j < annotatedJsonArr.length(); j++) {//annotatedJsonArr.length()
//						//System.out.println("add annotated concept 4");
//						JSONObject annotatedItem = annotatedJsonArr.getJSONObject(j);
//						JSONArray annotations= annotatedItem.getJSONArray("annotations");
//						JSONObject mainAnnotation= annotations.getJSONObject(0);
//						//System.out.println("add annotated concept 8");
//						int fromPosition= mainAnnotation.getInt("from");//取到标注信息
//						//System.out.println("add annotated concept 6");
//						int toPosition= mainAnnotation.getInt("to");
//						String matchType= mainAnnotation.getString("matchType");
//						//System.out.println("add annotated concept 7");
//						String text= mainAnnotation.getString("text");
//						if(!conceptList.contains(text)){     //将标注出来的concept存到链表里。
//							System.out.println("add annotated concept 5");
//							conceptList.add(text);
//							System.out.println("add annotated concept "+text);
//						}
//					}
//					
//					
//				} catch (org.apache.http.ParseException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				} catch (IOException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				} 
				/*
				JSONArray snippetsJsonArr=jsonObjQues.getJSONArray("snippets");  //得到某个问题的snippets
				int relatedGrade[]=new int[snippetsJsonArr.length()];
				String allSnippetText[]=new String[snippetsJsonArr.length()];
				for(int k=0;k<snippetsJsonArr.length();k++){
					JSONObject jsonObjSnippet = snippetsJsonArr.getJSONObject(k); //得到每一个snippet
					String snippetText=jsonObjSnippet.getString("text");  //得到每一个snippet text
					relatedGrade[k]=0;//相关性系数
					allSnippetText[k]=snippetText;
					String snippetTextBody=null;
					try {
						snippetTextBody = URLEncoder.encode(snippetText, "utf-8");
					} catch (UnsupportedEncodingException e2) {
						// TODO Auto-generated catch block
						e2.printStackTrace();
					}
					String snippetUrl="http://data.bioontology.org/annotator?text="+snippetTextBody+longest_only;
					HttpUtil snippetHttpUtil=new HttpUtil("GET",snippetUrl);
					snippetHttpUtil.excute();   //执行http GET 请求
					LinkedList<String> snippetConceptList=new LinkedList<String>();  //用一个concept List
					
					JSONArray annotatedSnippetJsonArr = null;
					try {
						annotatedSnippetJsonArr=new JSONArray(EntityUtils.toString(snippetHttpUtil.mRes.getEntity()));
						for (int j = 0; j < annotatedSnippetJsonArr.length(); j++) {
							JSONObject annotatedItem = annotatedSnippetJsonArr.getJSONObject(j);
							JSONArray annotations= annotatedItem.getJSONArray("annotations");
							JSONObject mainAnnotation= annotations.getJSONObject(0);
							String text= mainAnnotation.getString("text");
							if(!snippetConceptList.contains(text)){     //将标注出来的concept存到链表里。
								//System.out.println("add annotated snippet concept 5");
								snippetConceptList.add(text);
								//System.out.println("add annotated snippet concept "+text);
							}
						}
							
					} catch (org.apache.http.ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					for(int n=0;n<conceptList.size();n++){
						if(snippetConceptList.contains(conceptList.get(n))){
							relatedGrade[k]++;
						}
					}
					
					
				}
				int maxLabel[]={0,0,0};//可以把它变成数组，把concept个数同为最多的snippet的序号都存进去！！！！！！！
				int used=0;//当前正在使用的maxLabel项
				
				for(int k=0;k<snippetsJsonArr.length();k++){
					
					if(relatedGrade[k]>relatedGrade[maxLabel[used]]){
						maxLabel[used]=k;
					}
					else if(relatedGrade[k]==relatedGrade[maxLabel[used]]){
						used=(used+1)%3;
						maxLabel[used]=k;
					}
				}
				System.out.println("The most revelant snippet is : ");
				for(int m=0;m<used;m++){
					//System.out.println(allSnippetText[maxLabel[m]]);
				}
				//System.out.println("The most revelant snippet is "+allSnippetText[maxLabel]);
				//System.out.println("  After parsing is"+outputQuestionBody);

				/*try {
					ConfidenceChunker chunker
					= (ConfidenceChunker) AbstractExternalizable.readObject(inQuesFile);
					Iterator<Chunk> it
				      = chunker.nBestChunks(cs,0,cs.length,MAX_N_BEST_CHUNKS);
					for (int n = 0; it.hasNext(); ++n) {
				        Chunk chunk = it.next();
				        double conf = Math.pow(2.0,chunk.score());
				        int start = chunk.start();
				        int end = chunk.end();
				        String phrase = args[i].substring(start,end);
				        System.out.println(n + " "
				                           + Strings.decimalFormat(conf,"0.0000",12)
				                           + "       (" + start
				                           + ", " + end
				                           + ")       " + chunk.type()
				                           + "         " + phrase);
				     }
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}*/
	        }
				
				
	        JSONObject jsonObjAllQuesAnswer=new JSONObject();
	        jsonObjAllQuesAnswer.put("questions", jsonArrAnswerQues);
	        String filePath="E:/myphaseB_5b_04_generateSemanticTypes_rule-based_notCompleted_18_0-100.json";
	        FileWriter fw;
			try {
				fw = new FileWriter(filePath);
				PrintWriter out1= new PrintWriter(new OutputStreamWriter(new FileOutputStream(filePath), "utf-8"));
				//PrintWriter out = new PrintWriter(fw);
				//out.code
				out1.write(jsonObjAllQuesAnswer.toString());  
		        out1.println();  
		        fw.close();  
		        out1.close(); 
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}  
	    
	        
		
	        try {
	        	//System.out.println("come on!!");
				outTwo.close();
				outOne.close();
				outFour.close();
				outThree.close();
				inTwo.close();
				inOne.close();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}/*catch (FileNotFoundException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}*/

}
