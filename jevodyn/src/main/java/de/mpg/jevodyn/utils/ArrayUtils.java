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

}
