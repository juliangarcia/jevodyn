package de.mpg.jevodyn.utils;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.stat.StatUtils;

public class ArrayUtils {

	public static double[] normalize(double[] fitness) {
		double[] ans = new double[fitness.length];
		double sum = StatUtils.sum(fitness);
		for (int i = 0; i < ans.length; i++) {
			ans[i] = fitness[i]/sum;
		}
		return ans;
	}
	
	
	public static double[] normalize(int[] fitness) {
		double[] ans = new double[fitness.length];
		double sum = 0;
		for (int i = 0; i < fitness.length; i++) {
			sum = sum + fitness[i];
		}
		for (int i = 0; i < ans.length; i++) {
			ans[i] = ((double)fitness[i])/sum;
		}
		return ans;
	}
	
	
	public static double[] normalize(long[] fitness) {
		double[] ans = new double[fitness.length];
		double sum = 0;
		for (int i = 0; i < fitness.length; i++) {
			sum = sum + fitness[i];
		}
		for (int i = 0; i < ans.length; i++) {
			ans[i] = ((double)fitness[i])/sum;
		}
		return ans;
	}
	

	public static RealMatrix uniformMutationKernel(double mutationProbability, int numberOfTypes) {
		double[][] raw = new double[numberOfTypes][numberOfTypes];
		for (int i = 0; i < raw.length; i++) {
			for (int j = 0; j < raw.length; j++) {
				if (i!=j) {
					raw[i][j] = mutationProbability/(numberOfTypes -1);
				}else{
					raw[i][j] = 1.0-mutationProbability;
				}
			}
		}
		return new Array2DRowRealMatrix(raw);
	}
	
	
	public static int[] randomArray(int numberOfTypes, int maximumNumberOfCopiesPerType){
		int [] ans = new int[numberOfTypes];
		for (int i = 0; i < ans.length; i++) {
			ans[i] = Random.nextInt(maximumNumberOfCopiesPerType+1);
		}
		return ans;
	}

	public static double dot(double[] payoffVector, int[] asArrayOfTypes) {
		if (payoffVector.length != asArrayOfTypes.length) throw new IllegalArgumentException("Arrays should have the same size");
		double ans = 0.0; 
		for (int i = 0; i < asArrayOfTypes.length; i++) {
			ans = ans + asArrayOfTypes[i]*payoffVector[i];
		}
		return ans;
	}


	public static int[] randomEdge(int numberOfTypes, int populationSize) {
		int[] ans = new int[numberOfTypes];
		int edgeOne = 0;
		int edgeTwo = 0;
		while(edgeOne == edgeTwo){
			edgeOne = Random.nextInt(numberOfTypes);
			edgeOne = Random.nextInt(numberOfTypes);
		}
		ans[edgeOne] = Random.nextInt(populationSize);
		ans[edgeTwo] = populationSize-ans[edgeOne];
		return ans;
	}

}
