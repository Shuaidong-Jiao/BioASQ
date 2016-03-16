package myBioASQ;

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
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
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
public class DealWithInputQuestions {

	public static void main(String[] args) {
		
		String fullFileName = "E:/BioASQ-trainingDataset4b.json";
		//File file = new File(fullFileName);
        File testset = new File(fullFileName);
        
        JSONObject jsonObjQuery;
		try {
			jsonObjQuery = new JSONObject(new JSONTokener(new FileReader(testset)));
			JSONArray jsonArrQues = jsonObjQuery.getJSONArray("questions");
	        for (int i = 0; i < 2; i++) {//jsonArrQues.length()
				JSONObject jsonObjQues = jsonArrQues.getJSONObject(i);
				JSONObject annotatedJson;
				String id = jsonObjQues.getString("id").trim();
				String questionBody=jsonObjQues.getString("body");
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
				
				String questionType=jsonObjQues.getString("type");
				System.out.println(questionBody+"is"+questionType);
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
				String longest_only="&longest_only=true"; //满足最长匹配
				String url="http://data.bioontology.org/annotator?text="+body+longest_only;
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
				try {
					//System.out.println("add annotated concept 1");
					//annotatedJson = new JSONObject(EntityUtils.toString(myHttpUtil.mRes.getEntity()));//整个的JSONObject
					JSONArray annotatedJsonArr = null;
					//System.out.println("add annotated concept 2");
					//annotatedJson.toJSONArray(annotatedJsonArr);
					annotatedJsonArr= new JSONArray(EntityUtils.toString(myHttpUtil.mRes.getEntity()));//整个的JSONObject
					//System.out.println("add annotated concept 3");
					for (int j = 0; j < 2; j++) {//annotatedJsonArr.length()
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
							System.out.println("add annotated concept 5");
							conceptList.add(text);
							System.out.println("add annotated concept "+text);
						}
					}
					
					
				} catch (org.apache.http.ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} 
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
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}

class HttpUtil {  
    private HttpClient mClient;  
    HttpResponse mRes = null;  
    private String method, url;  
    private JSONObject json,data;  
    private static final String errorMessage = "Something wrong!";  
    private static final String invalidKeyMessage = "no such key exists!";    
    public HttpUtil(String method, String url){  
        this.method = method;  
        this.url = url;  
    } 
    public static String convertStreamToString(InputStream is) {      
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));      
        StringBuilder sb = new StringBuilder();      
       
        String line = null;      
        try {      
            while ((line = reader.readLine()) != null) {  
                sb.append(line + "\n");      
            }      
        } catch (IOException e) {      
            e.printStackTrace();      
        } finally {      
            try {      
                is.close();      
            } catch (IOException e) {      
               e.printStackTrace();      
            }      
        }      
        return sb.toString();      
    }
    public void excute(){  
        mClient = HttpClientBuilder.create().build();  
        try {  
            switch(method){  
            case "GET":  
            	HttpGet A=new HttpGet(url);
            	A.addHeader("Authorization", "apikey token=a7480201-153b-4dd9-9d39-8a94fa30ce61");
                mRes = mClient.execute(A);  
                break;  
            case "POST":  
                mRes = mClient.execute(new HttpPost(url));  
            }  
            /*HttpEntity entity = mRes.getEntity();
            if (entity != null) {    
                InputStream instreams = entity.getContent();    
                String str = convertStreamToString(instreams);  
                System.out.println("Do something");   
                System.out.println("NER result is "+str);
                //System.out.println(str);  
                // Do not need the rest    
                //httpgets.abort();    
            } */
            //json = new JSONObject(EntityUtils.toString(mRes.getEntity())); 
            //System.out.println("NER result is "+EntityUtils.toString(mRes.getEntity()));
            //data = json.getJSONObject("semantic_types");  
            /*String ontology=json.getString("ontologies");
            String semanticType=json.getString("semantic_types");
            System.out.println("NER result is "+ontology);*/
        } catch (ClientProtocolException e) {  
            // TODO Auto-generated catch block  
            e.printStackTrace();  
        } catch (IOException e) {  
            // TODO Auto-generated catch block  
            e.printStackTrace();  
        } catch (Exception e) {  
            // TODO Auto-generated catch block  
            e.printStackTrace();  
        } 
        /*catch (JSONException e) {  
            // TODO Auto-generated catch block  
            e.printStackTrace();  
        }  */
          
    } 
}

class DemoNLPDecode
{
	final String language = AbstractReader.LANG_EN;
	AbstractTokenizer tokenizer;
	AbstractComponent[] components=new AbstractComponent[5];
	public DemoNLPDecode(String modelType, String sentence,String outPutSentence) throws Exception
	{
		tokenizer  = NLPGetter.getTokenizer(language);
		AbstractComponent tagger     = NLPGetter.getComponent(modelType, language, NLPMode.MODE_POS);
		AbstractComponent parser     = NLPGetter.getComponent(modelType, language, NLPMode.MODE_DEP);
		AbstractComponent identifier = NLPGetter.getComponent(modelType, language, NLPMode.MODE_PRED);
		AbstractComponent classifier = NLPGetter.getComponent(modelType, language, NLPMode.MODE_ROLE);
		AbstractComponent labeler    = NLPGetter.getComponent(modelType, language, NLPMode.MODE_SRL);
		
		components[0]=tagger;
		components[1]=parser;
		components[2]=identifier;
		components[3]=classifier;
		components[4]=labeler;
		//components[4] = {tagger, parser, identifier, classifier, labeler};
		//outPutSentence=process(tokenizer, components, sentence);
		
	}
	
	
	
	/*public DemoNLPDecode(String modelType, String inputFile, String outputFile) throws Exception
	{
		AbstractTokenizer tokenizer  = NLPGetter.getTokenizer(language);
		AbstractComponent tagger     = NLPGetter.getComponent(modelType, language, NLPMode.MODE_POS);
		AbstractComponent parser     = NLPGetter.getComponent(modelType, language, NLPMode.MODE_DEP);
		AbstractComponent identifier = NLPGetter.getComponent(modelType, language, NLPMode.MODE_PRED);
		AbstractComponent classifier = NLPGetter.getComponent(modelType, language, NLPMode.MODE_ROLE);
		AbstractComponent labeler    = NLPGetter.getComponent(modelType, language, NLPMode.MODE_SRL);
		
		AbstractComponent[] components = {tagger, parser, identifier, classifier, labeler};
		
		String sentence = "I'd like to meet Dr. Choi.";
		process(tokenizer, components, sentence);
		process(tokenizer, components, UTInput.createBufferedFileReader(inputFile), UTOutput.createPrintBufferedFileStream(outputFile));
	}*/
	
	public String process(AbstractTokenizer tokenizer, AbstractComponent[] components, String sentence)
	{
		DEPTree tree = NLPGetter.toDEPTree(tokenizer.getTokens(sentence));
		
		for (AbstractComponent component : components)
			component.process(tree);

		System.out.println(tree.toStringSRL()+"\n");
		//outPutSentence=new String(tree.toStringSRL());
		return tree.toStringSRL();
	}
	
	public void process(AbstractTokenizer tokenizer, AbstractComponent[] components, BufferedReader reader, PrintStream fout)
	{
		AbstractSegmenter segmenter = NLPGetter.getSegmenter(language, tokenizer);
		DEPTree tree;
		
		for (List<String> tokens : segmenter.getSentences(reader))
		{
			tree = NLPGetter.toDEPTree(tokens);
			
			for (AbstractComponent component : components)
				component.process(tree);
			
			fout.println(tree.toStringSRL()+"\n");
		}
		
		fout.close();
	}

	/*public static void main(String[] args)
	{
		String modelType  = args[0];	// "general-en" or "medical-en"
		String inputFile  = args[1];
		String outputFile = args[2];
		
		try
		{
			new DemoNLPDecode(modelType, inputFile, outputFile);
		}
		catch (Exception e) {e.printStackTrace();}
	}*/
}        


        
       /* Scanner scanner = null;
        StringBuilder buffer = new StringBuilder();
        try {
            scanner = new Scanner(file, "utf-8");
            while (scanner.hasNextLine()) {
                buffer.append(scanner.nextLine());
            }
 
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block  
 
        } finally {
            if (scanner != null) {
                scanner.close();
            }
        }
         
        System.out.println(buffer.toString());
        
        try {
			//JSONObject jsonObject = new JSONObject(buffer.toString());
			
			ListOrderedMap map = new ListOrderedMap();
	        //最外层解析
	        //JSONObject json = JSONObject.fromObject(buffer.toString());
			JSONObject json = new JSONObject(buffer.toString());
	        //for(Object k : json.keys()){
	            Object v = json.get("questions"); 
	            //如果内层还是数组的话，继续解析
	            if(v instanceof JSONArray){
	                List<Map<String, String>> list = new ArrayList<Map<String,String>>();
	                Iterator<JSONObject> it = ((List<Map<String, String>>)v).iterator();
	                while(it.hasNext()){
	                    JSONObject json2 = it.next();
	                    list.add(parseJSON2Map(json2.toString()));
	                }
	                map.put(k.toString(), list);
	            } else {
	                map.put(k.toString(), v);
	            }
	        }
			
			
			JSONArray questionsArray=new JSONArray(jsonObject[questions]);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		*/
		// TODO Auto-generated method stub

