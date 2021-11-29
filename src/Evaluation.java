import java.util.ArrayList;
import java.util.List;

public class Evaluation {
	int[] mapped_result;
	int[][]  order;
	int count=0;
	
	public Evaluation() {
	}
	 
	public void permute(java.util.List<Integer> arr, int k){
        for(int i = k; i < arr.size(); i++){
            java.util.Collections.swap(arr, i, k);
            permute(arr, k+1);
            java.util.Collections.swap(arr, k, i);
        }

        if (k == arr.size() -1){
	        for(int a=0;a<arr.size();a++) {
				order[count][a]=arr.get(a);
			}
	        count++;
	     
        }
    }
	
	public int true_positives(int[] truth,int[] predicted) {
		int count=0;
		for(int i=0;i<truth.length;i++) {
			if(truth[i]==predicted[i]) {
				count++;
			}
		}
		return count;	
	}
	
	public int[] map_result(int[] truth, int[] predicted,int cluster_num) {
		int fact=1;
		for(int i=1;i<=cluster_num;i++){
			fact=fact*i;
			}
		order=new int[fact][cluster_num];

		int true_positive=0;
		List<Integer> input=new ArrayList<Integer>();
		for(int i=0;i<cluster_num;i++) {
			input.add(i);
		}
		this.permute(input, 0);
		
		for(int[]curr_order: order) {
			int[] transformed_label=new int[predicted.length];
			for(int i=0;i<predicted.length;i++) {
				for(int j=0;j<curr_order.length;j++) {
					if(predicted[i]==j) {
						transformed_label[i]=curr_order[j];
					}
				}
			}
	
			
			if(this.true_positives(transformed_label,truth)>true_positive) {
				true_positive=this.true_positives(transformed_label,truth);
				mapped_result=transformed_label;
			}
		}
		return mapped_result;
	}
	
	

	
	public double precision(int[][] confusion_matrix) {
			double[] precision=new double[confusion_matrix.length];
			for(int i=0;i<precision.length;i++) {
				double sum=0;
				for(int j=0;j<precision.length;j++) {
					sum+=confusion_matrix[i][j];
				}
				precision[i]=confusion_matrix[i][i]/sum;
			}
			double ave_precision=0;
			for(int i=0;i<precision.length;i++) {
				ave_precision+=precision[i];
			}
			return ave_precision/precision.length;
		}
	
	
	public double recall(int[][] confusion_matrix) {
		double[] recall=new double[confusion_matrix.length];
		for(int i=0;i<recall.length;i++) {
			double sum=0;
			for(int j=0;j<recall.length;j++) {
				sum+=confusion_matrix[j][i];
			}
			recall[i]=confusion_matrix[i][i]/sum;
		}
		double ave_recall=0;
		for(int i=0;i<recall.length;i++) {
			ave_recall+=recall[i];
		}
		return ave_recall/recall.length;
	}
	
	public double f_score(double precision,double recall) {
		return 2*(precision*recall)/(precision+recall);
	}
	
	
	public int[][] confusion_matrix(int[] truth,int[] predicted,int cluster_num) {
			int[][] confusion_matrix;
			confusion_matrix=new int[cluster_num][cluster_num];
		for(int i=0;i<predicted.length;i++) {
			int pred_label=predicted[i];
			int actual_label=truth[i];
			confusion_matrix[pred_label][actual_label]++;
		}
		return confusion_matrix;
	
	    }
	
	

	


}