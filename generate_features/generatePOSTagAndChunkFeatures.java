package myBioASQ;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.lang.String;
import edu.stanford.nlp.tagger.maxent.MaxentTagger;

public class generatePOSTagAndChunkFeatures {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		MaxentTagger tagger = new MaxentTagger("taggers/english-bidirectional-distsim.tagger");
		// The sample string
		String sample = "This is a sample text";
		File inputQuestions = new File("E:/BioASQ_question_classfication/BioASQ-trainingDataset_1b-4b.json");  //要读取的文件
		JSONObject jsonObjQuery = null;
		String regex ="[\\s\\p{Punct}]+"; //used to split tagged question body.
		String PennTagSet[] ={"CC","CD","DT","EX","FW","IN","JJ","JJR","JJS","LS","MD","NN","NNS","NNP","NNPS","PDT","POS","PRP","PRP$","RB","RBR","RBS","RP","SYM","TO","UH","VB","VBD","VBG","VBN","VBP","VBZ","WDT","WP","WP$","WRB"};
		System.out.println("PennTagSet's length is "+String.valueOf(PennTagSet.length));
		int MaxWordNumber = 50;
		int POSTagNumber = 36;
		FileWriter outOne = null;
		BufferedWriter outTwo = null;
		
		try {
			//FileReader inOne = new FileReader(inputQuestions);
			//BufferedReader inTwo = new BufferedReader(inOne);
			try {
				outOne = new FileWriter("E:/BioASQ_question_classfication/BioASQ-trainingDataset_1b-4b_POSTagFeatures_addQuestionAndNumber.txt");
				outTwo = new BufferedWriter(outOne);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			jsonObjQuery = new JSONObject(new JSONTokener(new FileReader(inputQuestions)));
			JSONArray jsonArrQues = jsonObjQuery.getJSONArray("questions");//得到question的JSONArray
			int factoidOrListCount =0;
			float factoidOrListCountQuestionNumber[] = new float[903];
			for(int f=0;f<903;f++){
				 factoidOrListCountQuestionNumber[f] = 0;
			}
			for(int i=0;i<jsonArrQues.length();i++){//jsonArrQues.length()
				JSONObject jsonObjQues = jsonArrQues.getJSONObject(i);
				String questionBody = jsonObjQues.getString("body");
				System.out.println("question is "+questionBody);
				String questionType = jsonObjQues.getString("type");
				if (questionType.equals("factoid")||questionType.equals("list")) {
					factoidOrListCountQuestionNumber[factoidOrListCount]=i+1;
					
					String taggedQuestion = tagger.tagString(questionBody);
					System.out.println("Tagged question is " + taggedQuestion);
					String words[] = taggedQuestion.split(regex);
					String tags[] = new String[words.length / 2];
					for (int j = 0; j < words.length / 2; j++) {
						tags[j] = words[2 * j + 1];
						System.out.println("tags[" + String.valueOf(j) + "] is  " + tags[j]);
					}
					int questionPOSTagFeatures[] = new int[MaxWordNumber * POSTagNumber]; //total 1800 
					for (int k = 0; k < questionPOSTagFeatures.length; k++) {
						questionPOSTagFeatures[k] = 0;
					}
					for (int j = 0; j < tags.length; j++) {
						for (int k = 0; k < PennTagSet.length; k++) {
							if (tags[j].equals(PennTagSet[k])) {
								questionPOSTagFeatures[j * POSTagNumber + k] = 1; //当tags[j]与PennTagSet[k]相同时，则对应的这个特征设为1
								break;
							}
						}
					}
					try {
						outTwo.write(String.valueOf(factoidOrListCountQuestionNumber[factoidOrListCount]) + " ");
						System.out.print(String.valueOf(factoidOrListCountQuestionNumber[factoidOrListCount]) + " ");
					} catch (IOException e) {
						e.printStackTrace();
					}
					for (int k = 0; k < questionPOSTagFeatures.length; k++) {
						try {
							outTwo.write(String.valueOf(questionPOSTagFeatures[k]) + " ");
							System.out.print(String.valueOf(questionPOSTagFeatures[k]) + " ");
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
					try {
						outTwo.newLine();
						System.out.println();
					} catch (IOException e) {
						e.printStackTrace();
					} 
					factoidOrListCount++;
				}
				
			}
			for(int f=0;f<903;f++){
				 System.out.println(String.valueOf(factoidOrListCountQuestionNumber[f]));
			}
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		// The tagged string
		String tagged = tagger.tagString(sample);

		// Output the result
		System.out.println(tagged);
		

	}

}
