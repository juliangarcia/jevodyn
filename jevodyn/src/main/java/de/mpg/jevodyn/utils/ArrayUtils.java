package de.mpg.jevodyn.utils;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.stat.StatUtils;

/**
 * This class contains functions that deal with arrays and matrices. 
 * @author garcia
 *
 */
public class ArrayUtils {

	/***
	 * This function takes an array of doubles, and returns another array such that each entry is divided by the sum 
	 * of elements in the original array. It normalizes it so that the result's elements sum up to one.  This is useful 
	 * when creating a probability distribution based on fitness or payoff values.
	 * @param fitness an array of doubles
	 * @return normalized array, such that array[i] = fitness[i]/sum(fitness[i]) 
	 */
	public static double[] normalize(double[] fitness) {
		double[] ans = new double[fitness.length];
		double sum = StatUtils.sum(fitness);
		for (int i = 0; i < ans.length; i++) {
			ans[i] = fitness[i]/sum;
		}
		return ans;
	}
	
	/***
	 * This function takes an array of doubles, and returns another array such that each entry is divided by the sum 
	 * of elements in the original array. It normalizes it so that the result's elements sum up to one.  This is useful 
	 * when creating a probability distribution based on fitness or payoff values.
	 * @param fitness an array of integers
	 * @return normalized array, such that array[i] = fitness[i]/sum(fitness[i]) 
	 */
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
	
	/***
	 * This function takes an array of doubles, and returns another array such that each entry is divided by the sum 
	 * of elements in the original array. It normalizes it so that the result's elements sum up to one.  This is useful 
	 * when creating a probability distribution based on fitness or payoff values.
	 * @param fitness an array of long values
	 * @return normalized array, such that array[i] = fitness[i]/sum(fitness[i]) 
	 */
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
	
	/***
	 * Computes a standard mutation kernel, where all other strategies have the same probability to be chosen.
	 * Kernels are defined as in García and Traulsen (2012), PLoS ONE. The function returns a RealMatrix. Entries on 
	 * non-diagonal positions will have the quantiy u/(n-1), where u is the mutation probability and n is the number of strategies.
	 * The shape of the result is n times n. 
	 * @param mutationProbability double
	 * @param numberOfTypes int
	 * @return
	 */
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
	
	/**
	 * Creates an array of integers, where every position has been sampled from a uniform distribution 
	 * on (0, maximumNumberOfCopiesPerType). The array has numberOfTypes positions
	 * @param numberOfTypes, integer
	 * @param maximumNumberOfCopiesPerType, integer
	 * @return
	 */
	public static int[] randomArray(int numberOfTypes, int maximumNumberOfCopiesPerType){
		int [] ans = new int[numberOfTypes];
		for (int i = 0; i < ans.length; i++) {
			ans[i] = Random.nextInt(maximumNumberOfCopiesPerType+1);
		}
		return ans;
	}

	/***
	 * Computes the dot product, or the linear combination between two vectors, expressed as arrays.
	 * @param payoffVector array of doubles
	 * @param asArrayOfTypes array of integers.
	 * @return
	 */
	public static double dot(double[] payoffVector, int[] asArrayOfTypes) {
		if (payoffVector.length != asArrayOfTypes.length) throw new IllegalArgumentException("Arrays should have the same size");
		double ans = 0.0; 
		for (int i = 0; i < asArrayOfTypes.length; i++) {
			ans = ans + asArrayOfTypes[i]*payoffVector[i];
		}
		return ans;
	}

	
	/***
	 * Returns an array of numberOfTypes integers with exactly at most two non-zero positions, such that the sum equals populationSize.
	 * @param numberOfTypes size of the returned array
	 * @param populationSize sum of elements goes up to this number.
	 * @return
	 */
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
