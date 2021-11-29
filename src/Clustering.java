import java.util.Random;
import java.util.ArrayList;
import java.util.*;

public class Clustering {
	
	public static int[] labels;
	
	public Clustering() {
		
	}
	
	
	public int[] kmeans_plus(double[][] matrix, int k,int iteration,String metrics){
		
		Similarity simi=new Similarity();
		Random rand=new Random();
		int random_num=rand.nextInt(matrix.length);
		double[][] centers=new double[k][matrix[0].length];
		centers[0]=matrix[random_num].clone();
		int[] center_ind=new int[k];
		center_ind[0]=random_num;
		
		
		if(metrics.equals("euclidean")) {
		int count=1;
		while(count<k) {
		double[] score=new double[matrix.length];
		for(int i=0; i<score.length;i++) {
			score[i]=Double.POSITIVE_INFINITY;
		}
		for(int i=0; i<score.length;i++) {
			boolean pass=false;
			for(int j=0;j<count;j++) {
				if(i==center_ind[j]) {
					pass=true;
					score[i]=0;
				}
			}
			if(pass==false) {
			for(int j=0;j<count;j++) {
					double curr_score=simi.euclidean(centers[j], matrix[i]);
					
					if(curr_score<score[i]) {
						score[i]=curr_score;					
					}		
			}	
			}
		}
		double summation=0;
		for(int i=0; i<score.length;i++) {
			summation+=score[i]*score[i];
		}
		for(int i=0; i<score.length;i++) {
			score[i]=score[i]*score[i]/summation;
		}
	    double p = rand.nextDouble();
        double sum = 0.0;
        int choose = 0;
        while(sum < p){
            sum += score[choose];
            choose++;
        }
        center_ind[count]=choose-1;
        centers[count]=matrix[choose-1].clone();
        count++;
        
		}
		}
		
		
		if(metrics.equals("cosine")) {
			int count=1;
			while(count<k) {
			double[] score=new double[matrix.length];
			for(int i=0; i<score.length;i++) {
				score[i]=-1;
			}
			for(int i=0; i<score.length;i++) {
				boolean pass=false;
				for(int j=0;j<count;j++) {
					if(i==center_ind[j]) {
						pass=true;
						score[i]=1;
					}
				}
				if(pass==false) {
				for(int j=0;j<count;j++) {
					
						double curr_score=simi.cosine(centers[j], matrix[i]);
						if(curr_score>score[i]) {
							score[i]=curr_score;
						}	
				}
				}
			}
			
			
			for(int i=0;i<score.length;i++) {
				score[i]=1-score[i];
			}
			double summation=0;
			for(int i=0; i<score.length;i++) {
				summation+=score[i]*score[i];
			}
			for(int i=0; i<score.length;i++) {
				score[i]=score[i]*score[i]/summation;
			}

		    double p = rand.nextDouble();
	        double sum = 0.0;
	        int choose = 0;
	        while(sum < p){
	            sum += score[choose];
	            choose++;
	        }
	      
	        center_ind[count]=choose-1;
	        centers[count]=matrix[choose-1].clone();
	        count++;
			}
			}
		
		

		//K-means
		int iter=0;
		boolean criterion=false;
		
		labels=new int[matrix.length];
		while(iter<iteration && criterion==false) {
		ArrayList<ArrayList<double[]>> vectors = new ArrayList<ArrayList<double[]>>();
		for (int i = 0; i <centers.length ; i++) {
		   vectors.add(new ArrayList<double[]>());
		}
	
		for(int i=0;i<matrix.length;i++) {

			boolean breakout=false;
			for(int y=0;y<center_ind.length;y++) {
				if(center_ind[y]==i) {
					breakout=true;
				}
			}
		
			if(breakout==false) {
				if(metrics.equals("cosine")) {
					double simi_scores=-1;	
					int index=0;
					for(int z=0;z<centers.length;z++) {
						if(simi.cosine(centers[z], matrix[i])>simi_scores) {
							simi_scores=simi.cosine(centers[z], matrix[i]);
							index=z;
						}		
					}
					vectors.get(index).add(matrix[i]);
					labels[i]=index;
				}
				else {
					double simi_scores=simi.euclidean(centers[0], matrix[i]);			
					int index=0;
					for(int z=0;z<centers.length;z++) {
						
						if(simi.euclidean(centers[z], matrix[i])<simi_scores) {
						simi_scores=simi.euclidean(centers[z], matrix[i]);
						index=z;
						}
						
					}
					vectors.get(index).add(matrix[i]);
					labels[i]=index;
				}
			}
		}

		double[][] newcenters=new double[k][matrix[0].length];
		
		for(int x=0;x<centers.length;x++) {	
			if(vectors.get(x).size()==0) {
				newcenters[x]=centers[x];
			}
			else {
			double[] finalvector=new double[matrix[0].length];
			for(int y=0;y<matrix[0].length;y++) {
				finalvector[y]=0;
			}
			for(int y=0;y<matrix[0].length;y++) {
				for(int z=0;z<vectors.get(x).size();z++) {
					finalvector[y]+=vectors.get(x).get(z)[y];		
			}	
			}
			for(int y=0;y<matrix[0].length;y++) {
			finalvector[y]=finalvector[y]/vectors.get(0).size();
			}
			newcenters[x]=finalvector.clone();
			center_ind[x]=-1;
		}
		}
			for(int x=0;x<centers.length;x++) {
				 if (!Arrays.equals(centers[x],newcenters[x]))
			            criterion=false;           
			}	
			if(criterion==false) {
				centers=newcenters.clone();
			}
			iter++;

		}
		 
		return labels;

	}
	

}
