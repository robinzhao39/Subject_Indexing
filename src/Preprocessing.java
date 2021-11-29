import java.io.File;  // Import the File class
import java.io.FileNotFoundException;  // Import this class to handle errors
import java.util.Scanner; // Import the Scanner class to read text files
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Properties;
import java.util.Collections;

import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.*;

import edu.stanford.nlp.simple.*;
import java.lang.Math;
import java.io.IOException;  
import java.io.FileWriter;


public class Preprocessing{
	
	 public static ArrayList<String> features=new ArrayList<String>();
	 ArrayList<Integer> index_pca=new ArrayList<Integer>();
	 public static ArrayList<String> newword=new ArrayList<String>();
	 public static double[] idf_vector;
	
public Preprocessing() {
	
}

	 public double[][] process() {
		 ArrayList<String> stoplist=new ArrayList<String>();
		 ArrayList<ArrayList<String>> finaltoken=new ArrayList<ArrayList<String>>();
		 ArrayList<ArrayList<String>> this_token=new ArrayList<ArrayList<String>>();
		 features=new ArrayList<String>();
		 
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
	
	        

		for(int a=1;a<8;a+=3) {
			for(int b=1;b<9;b++) {
				String currentfile="C"+Integer.toString(a)+"/article0"+Integer.toString(b)+".txt";
				ArrayList<String> tokens=new ArrayList<String>();
				ArrayList<String> temp_token=new ArrayList<String>();
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
			         	 temp_token.add(tokens.get(i));	  
			         	  }
			          else {
			         	 stopyes=false;
			           }
			       }
				 
				 
				 finaltoken.add(temp_token);

			}
			
		}

	   
	   int n_gram=2;
	   int threshold=3;
	   
	   //identify n grams
	   ArrayList<String> combinedlist=new ArrayList<String>();
		for(int a=0;a<24;a++) {
			ArrayList<String> currentdoc=finaltoken.get(a);
			for(int i=0;i<currentdoc.size()-(n_gram-1);i++) {
				int count=0;
				String combined="";
				while(count!=(n_gram)) {
					combined+=currentdoc.get(i+count)+" ";
					count++;
				}
				combined=combined.substring(0, combined.length()-1);
				combinedlist.add(combined);
			}
			
		}
		
		
		for(int a=0;a<combinedlist.size()-1;a++) {
			int level=0;
			for(int b=a+1;b<combinedlist.size();b++) {
				if(combinedlist.get(a).equals(combinedlist.get(b))) {
					level++;
				}
			}
			if(level>threshold) {
				newword.add(combinedlist.get(a));
			}
		}
		
		for(int a=0;a<24;a++) {
			ArrayList<String> currentdoc=finaltoken.get(a);
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
					for(String word: newword) {
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
			
				this_token.add(temp_token);
	
		}
			
	   

		//get unique elements across all documents
			
			boolean addyes;
			for(int a=0;a<24;a++) {
				for(String word: this_token.get(a)) {
					addyes=false;
					for(String cur: features) {
						if(cur.equals(word)) {
							addyes=true;
							break;
						}
					}
					if(addyes==false) {
						features.add(word);
					}
					
				}
			}
			
			
			int[][] matrix=new int[24][features.size()];
			
			//Generate Matrix
			
			for(int a=0;a<24;a++) {
				for(int b=0;b<features.size();b++) {
					int count=0;
					for(String word: this_token.get(a)) {
						if(features.get(b).equals(word)) {
							count++;
						}
					}
					matrix[a][b]=count;
				}
			}
			
		
			//transform matrix
			double[][] trans_matrix=new double[24][features.size()];
		       for (int i = 0; i < 24; i++) {
		    	   double total=0;
		    	   for(int z=0;z<features.size();z++) {
		    		   total+=matrix[i][z];
		    	   }
		    	
		    	   	idf_vector=new double[features.size()];
		            for (int j = 0; j < features.size(); j++) {
		               double tf=matrix[i][j]/total;
		               double numwitht=0;
		               for(int a=0;a<24;a++) {
		            	   if(matrix[a][j]!=0) {
		            		   numwitht++;
		            	   }
		               }
		               double idf=Math.log(24/numwitht);
		               idf_vector[j]=idf;
		               trans_matrix[i][j]=tf*idf;
		            }
		  
		            
		        }
				
		       return trans_matrix;

	    }
	 
	 public void keyword(double[][] matrix,ArrayList<String> feat) {
		 
		    try {
		        File myObj = new File("topics.txt");
		        if (myObj.createNewFile()) {
		          System.out.println("File created: " + myObj.getName());
		        } else {
		          System.out.println("File already exists.");
		        }
		      } catch (IOException e) {
		        System.out.println("An error occurred.");
		        e.printStackTrace();
		      }
		        
		 String[][] keyword=new String[3][10];
		 for(int folder=0;folder<3;folder++) {
		 
		 Double[] vector=new Double[matrix[0].length];
		
		 for(int j=0;j<vector.length;j++) {
			 vector[j]=0.0;
		 }
		 for(int i=folder*8;i<folder*8+8;i++) {
			 for(int j=0;j<vector.length;j++) {
				 vector[j]+=matrix[i][j];
			 } 
		 }

		 Double[] oldvector=vector.clone();
		 
		 Arrays.sort(vector, Collections.reverseOrder());
		 for(int i=0;i<keyword[0].length;i++) {
			 for(int j=0;j<vector.length;j++) {
				 if(oldvector[j]==vector[i]) {
					 keyword[folder][i]=feat.get(j);
					 index_pca.add(j);
				 }
			 }
		 }	

	 } 
		 try {
		      FileWriter myWriter = new FileWriter("topics.txt");
		      for(int i=0;i<keyword.length;i++) {
		    	  for(int j=0;j<keyword[0].length;j++) {
		    	  myWriter.write(keyword[i][j]+"| ");
		    	  }
		    	  myWriter.write("\n");  
		      }
		      myWriter.close();
		      System.out.println("Successfully wrote to the file.");
		    } catch (IOException e) {
		      System.out.println("An error occurred.");
		      e.printStackTrace();   
		    }
	 }

	  }
	