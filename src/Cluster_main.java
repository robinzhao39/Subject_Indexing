import java.util.ArrayList;

public class Cluster_main {
	
	public static void main(String[] args) {
		
		Preprocessing obj=new Preprocessing();
		double[][] matrix=obj.process();
	/*	
		ArrayList<String> feat=obj.features;
		obj.keyword(matrix, feat);
		double[][] matrix_pca=new double[24][obj.index_pca.size()];
		for(int i=0;i<24;i++) {
			for(int j=0;j<obj.index_pca.size();j++) {
				matrix_pca[i][j]=matrix[i][obj.index_pca.get(j)];
			}
		}
		double[][] processed_matrix=Visualization.principalComponentAnalysis(Matrix.transpose(matrix_pca), 2);
	*/
		Clustering cluster=new Clustering();
		int[] label=new int[matrix.length];
		label=cluster.kmeans_plus(matrix, 3, 200, "cosine");
		Evaluation e=new Evaluation();
		int[] truth= {0,0,0,0,0,0,0,0,1,1,1,1,1,1,1,1,2,2,2,2,2,2,2,2};
		int[] trans_label=e.map_result(truth, label, 3);
		
		System.out.println("K-means evaluation");
		int[][] confusion_matrix=e.confusion_matrix(truth, trans_label, 3);
		System.out.println("Confusion Matrix:");
		for(int i=0;i<confusion_matrix.length;i++) {
			for(int j=0;j<confusion_matrix[i].length;j++) {
				System.out.print(confusion_matrix[i][j]+" ");
			}
			System.out.println();
		}
		double precision=e.precision(confusion_matrix);
		double recall=e.recall(confusion_matrix);
		System.out.println("Precision: "+precision);
		System.out.println("Recall: "+recall);
		System.out.println(e.f_score(precision, recall));
		System.out.println();
		
		System.out.println("Predict new document");
		fuzzy_knn classifier=new fuzzy_knn();
		String path="unknown/unknown06.txt";
		int[] in=classifier.neighbors(7, matrix, "cosine", path);
		int predicted_label=classifier.classify(in, trans_label, 3,true);
		System.out.println("The new document is predicted in group "+predicted_label+":");
		if(predicted_label==0) {
			System.out.println("airline safety");
		}
		if(predicted_label==1) {
			System.out.println("hoof and mouth");
		}
		if(predicted_label==2) {
			System.out.println("mortgage rates");
		}
		
		System.out.println();
		System.out.println("Prediction Evaluation");
		String[] names={"01","02","03","04","05","06","07","08","09","10"};
		int[] pred_truth= {0,0,0,0,1,1,2,2,1,0};
		int[] pred_label=new int[pred_truth.length];
		for(int i=0;i<names.length;i++) {
			path="unknown/unknown"+names[i]+".txt";
			in=classifier.neighbors(7, matrix, "cosine", path);
			pred_label[i]=classifier.classify(in, truth, 3,false);
		}
		
		confusion_matrix=e.confusion_matrix(pred_truth, pred_label, 3);
		System.out.println("Confusion Matrix:");
		for(int i=0;i<confusion_matrix.length;i++) {
			for(int j=0;j<confusion_matrix[i].length;j++) {
				System.out.print(confusion_matrix[i][j]+" ");
			}
			System.out.println();
		}
		precision=e.precision(confusion_matrix);
		recall=e.recall(confusion_matrix);
		System.out.println("Precision: "+precision);
		System.out.println("Recall: "+recall);
		System.out.println(e.f_score(precision, recall));
		System.out.println();
		
		
		/*
		Visualization viz=new Visualization("graph","original");
		viz.graph();
	*/

}
}
