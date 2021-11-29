import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Properties;
import java.util.Scanner;

import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.CoreDocument;
import edu.stanford.nlp.pipeline.CoreEntityMention;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;

public class Preprocessing_knn {
	
	public Preprocessing_knn() {
		
	}

	 public double[] process(String path) {
		 ArrayList<String> stoplist=new ArrayList<String>();
		 ArrayList<String> finaltoken=new ArrayList<String>();
		
		 //get stopwords
		  try {
		        File stop=new File("stopwords.txt");
		        Scanner Reader=new Scanner(stop);
		        while (Reader.hasNextLine()) {
		          String word = Reader.nextLine();
		          stoplist.add(word);
		        }
		        }
		        catch (FileNotFoundException e) {
		            System.out.println("An error occurred.");
		            e.printStackTrace();
		          }
		  
		  Properties props = new Properties();
	        // set the list of annotators to run
	        props.setProperty("annotators", "tokenize,ssplit,pos,lemma,ner");
	        // build pipeline
	        StanfordCoreNLP pipeline = new StanfordCoreNLP(props);
	
	        
				String currentfile=path;
				ArrayList<String> tokens=new ArrayList<String>();
			
				 try {
				     File myObj = new File(currentfile);
				      Scanner myReader = new Scanner(myObj);
				     
				      while (myReader.hasNextLine()) {
					       // set up pipeline properties
				   
				        // create a document object
				        String data=myReader.nextLine();
				        data = data.replaceAll("\\p{Punct}", "");
				        data = data.toLowerCase();
				        CoreDocument doc = new CoreDocument(data);
				        // annotate
				        pipeline.annotate(doc);
				        // display tokens
				        boolean ans = doc.entityMentions()!=null && !doc.entityMentions().isEmpty();
				        
				        if(ans==true) {
				        for (CoreEntityMention em : doc.entityMentions()) 
				        	tokens.add(em.text());	       
				        }
				             
				        for (CoreLabel tok : doc.tokens()) {    
				        	if(tok.ner().equals("O")) {
				        		tokens.add(tok.word());
				        	}
				        }
				           
				        
				      }
				      myReader.close();
				    } catch (FileNotFoundException e) {
				      System.out.println("An error occurred.");
				      e.printStackTrace();
				    }
				 
				 
				 for(int i=0;i<tokens.size();i++){
			     	  boolean stopyes=false;
			           for(String element: stoplist) {
			         	  if (element.equals(tokens.get(i))){
			         		  stopyes=true;
			         		  break;
			         	  }
			           }
			          if(stopyes==false) {
			         	 finaltoken.add(tokens.get(i));	  
			         	  }
			          else {
			         	 stopyes=false;
			           }
			       }
				 
	   
	   int n_gram=2;
	   int threshold=3;
	   
		
	   ArrayList<String> new_word=Preprocessing.newword;
	   
	
			ArrayList<String> currentdoc=finaltoken;
			ArrayList<String> temp_token=new ArrayList<String>();
			int iter=0;
			while(iter<currentdoc.size()) {
				if(iter!=currentdoc.size()-1) {
					int count=0;
					String combined="";
					while(count!=(n_gram)) {
						combined+=currentdoc.get(iter+count)+" ";
						count++;
					}
					combined=combined.substring(0, combined.length()-1);
					boolean addword=false;
					for(String word: new_word) {
						if(combined.equals(word)) {
							temp_token.add(combined);
							addword=true;
							iter++;
							break;
						}	
					}
					if(addword==false) {
						temp_token.add(currentdoc.get(iter));
					}	
				}
				else {
					temp_token.add(currentdoc.get(iter));
				}
					iter++;
				}
			

			ArrayList<String> columns=Preprocessing.features;
			double[] final_vector=new double[columns.size()];
			for(int i=0;i<columns.size();i++) {
				int count=0;
				for(String word: temp_token) {
					if(columns.get(i).equals(word)) {
						count++;
					}
				}
				final_vector[i]=count;
			}
			

		    	   double total=0;
		    	   for(int z=0;z<columns.size();z++) {
		    		   total+=final_vector[z];
		    	   }
		    	   double []idf_v=Preprocessing.idf_vector;
		            for (int j = 0; j < columns.size(); j++) {
		               double tf=final_vector[j]/total;
		               double idf=idf_v[j];
		               final_vector[j]=tf*idf;
		            }
	
		       return final_vector;

	    }

}
