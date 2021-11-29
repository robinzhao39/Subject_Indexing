
public class fuzzy_knn {

	
	public fuzzy_knn() {
		
	}
	
	public int[] neighbors(int k, double[][] matrix, String metrics, String path) {
		Similarity simi=new Similarity();
		Preprocessing_knn knn_pre=new Preprocessing_knn();
		double[] target_vector=knn_pre.process(path);
		double[] distance=new double[matrix.length];
		int[] index=new int[k];
		
		for(int i=0;i<matrix.length;i++) {
			if(metrics.equals("euclidean")) {
				distance[i]=simi.euclidean(target_vector, matrix[i]);
			}

			if(metrics.equals("cosine")) {
				distance[i]=simi.euclidean(target_vector, matrix[i]);
			}
		}
		
	
		
		for(int a=0;a<k;a++) {
		double min=Double.POSITIVE_INFINITY;
		for(int i=0;i<distance.length;i++) {
			if(distance[i]<min) {
				min=distance[i];
				index[a]=i;
			}
		}
		distance[index[a]]=Double.POSITIVE_INFINITY;
		}
	
		return index;
	}
	
	public int classify(int[] index, int[] labels,int cluster_num, boolean fuzzy) {
		int[] cluster=new int[cluster_num];
		for(int i=0;i<cluster_num;i++) {
			cluster[i]=0;
		}
		for(int i=0;i<index.length;i++) {
			cluster[labels[index[i]]]++;
		}
		int max=0;
		int output=0;
		for(int i=0;i<cluster_num;i++) {
			if (cluster[i]>max){
				max=cluster[i];
				output=i;
			}
		}
		if(fuzzy==true) {
		for(int i=0;i<cluster_num;i++) {
			double percent=cluster[i]*1.0/index.length;
			System.out.print(percent*100+"% Cluster"+i+"   ");
		}
		System.out.println();
		}
	
		return output;
		
	}

}

