public class Similarity {
	
	public Similarity() {
		
	}
	
	public double cosine(double[] v1, double[] v2) {
		double sum=0;
		for(int i=0;i<v1.length;i++) {
			sum+=v1[i]*v2[i];
		}
		
		double a=0;
		for(int i=0;i<v1.length;i++) {
			a+=v1[i]*v1[i];
		}
		a=Math.sqrt(a);
		
		double b=0;
		for(int i=0;i<v2.length;i++) {
			b+=v2[i]*v2[i];
		}
		b=Math.sqrt(b);
		
		return sum/(a*b);
		
	}
	
	public double euclidean(double[] v1, double[] v2) {
		double sum=0;
		for(int i=0;i<v1.length;i++) {
			sum+=(v1[i]-v2[i])*(v1[i]-v2[i]);
		}
		
		return Math.sqrt(sum);
	}
}
