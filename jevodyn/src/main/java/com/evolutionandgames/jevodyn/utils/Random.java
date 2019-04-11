package com.evolutionandgames.jevodyn.utils;

import java.util.ArrayList;

import org.apache.commons.math3.distribution.BinomialDistribution;
import org.apache.commons.math3.distribution.HypergeometricDistribution;
import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.distribution.PoissonDistribution;
import org.apache.commons.math3.random.Well19937c;
import org.apache.commons.math3.util.MathArrays;

/**
 * This class uses the Mersene Twister to generate pseudo random numbers. It is
 * a wrapper for the Mersene twister implementation of Commons math. It ensures
 * that the generator is shared in a program, by making the seed and function
 * static.
 * 
 * @author garcia
 * 
 */
public class Random {

	protected static Long seed = null;
	private static Well19937c generator = null;

	/**
	 * Sets the random generator seed
	 * 
	 * @param seed
	 */
	public static void seed(Long seed) {

		if (seed != null) {
			Random.seed = new Long(seed);
			generator = new Well19937c(seed);
		} else {
			Random.seed = System.currentTimeMillis();
			generator = new Well19937c(Random.seed);
		}
	}

	/**
	 * Seed the generator with a default seed value.
	 */
	public static void seed() {
		Random.seed(null);
	}

	private Random() {
		super();
		generator = new Well19937c();
	}

	public static int nextInt(int maximumExclusive)
			throws IllegalArgumentException {
		if (maximumExclusive <= 0) {
			throw new IllegalArgumentException(
					"limit for random integer must be positive");
		}
		return generator.nextInt(maximumExclusive);
	}

	public static double nextDouble() {
		return generator.nextDouble();
	}

	public static boolean nextBoolean() {
		return generator.nextBoolean();
	}

	public static boolean bernoulliTrial(double successProbability) {
		return (Random.nextDouble() <= successProbability) ? true : false;
	}

	public static int simulateDiscreteDistribution(double[] distribution) {
		double subtotal = 0.0;
		double r = Random.nextDouble();
		int j = 0;
		try {
			while (subtotal <= r) {
				subtotal = subtotal + distribution[j];
				j++;
			}
			return j - 1;
		} catch (IndexOutOfBoundsException e) {
			// it can go over the index in case2 the comparisons are really
			// small, when r is close to 1.0.
			// This usually means that the last element should be returned.
			return distribution.length - 1;
		}
	}

	/***
	 * Simulates a poisson distributed random variable
	 * 
	 * @param mean
	 * @return
	 */
	public static int simulatePoissonDistribution(double mean) {
		return new PoissonDistribution(Random.generator, mean,
				PoissonDistribution.DEFAULT_EPSILON,
				PoissonDistribution.DEFAULT_MAX_ITERATIONS).sample();
	}

	/***
	 * Simulates a Normally distributed random variable
	 * 
	 * @param mean
	 * @param standardDeviation
	 * @return
	 */
	public static double simulateNormalDistribution(double mean,
			double standardDeviation) {
		return new NormalDistribution(Random.generator, mean,
				standardDeviation,
				NormalDistribution.DEFAULT_INVERSE_ABSOLUTE_ACCURACY).sample();
	}

	/***
	 * Simulates a Geometric distributed random variable. Number of trials until
	 * the first success.
	 * 
	 * @param successProbability
	 * @return
	 */
	public static int simulateGeometricDistribution(double succesProbability) {
		double u = Random.nextDouble();
		if (succesProbability >= 1.0)
			return 0;
		if (u <= succesProbability)
			return 0;
		if (u >= 1.0 || succesProbability <= 0.0)
			return Integer.MAX_VALUE;
		return (int) Math
				.floor(Math.log1p(-u) / Math.log1p(-succesProbability));

	}

	/**
	 * Simulates a hypergeometric distribution for two types
	 * 
	 * @param populationSize
	 * @param numberOfSuccesses
	 * @param sampleSize
	 * @return
	 */
	public static int simulateHypergeometricDistribution(int populationSize,
			int numberOfSuccesses, int sampleSize) {
		return new HypergeometricDistribution(Random.generator, populationSize,
				numberOfSuccesses, sampleSize).sample();
	}
	
	
	/**
	 * Simulates a Binomial given number of trials and probability of success
	 * 
	 * @param trials
	 * @param p
	 * @return
	 */
	public static int simulateBinomialDistribution(int trials, double p) {
		return new BinomialDistribution(Random.generator, trials, p).sample();
	}

	public static int[] simulateMultivariateHypergeometricDistribution(
			int[] source, int sampleSize, int populationSize) {
		int colors = source.length;
		int[] destination = new int[colors];
		int n = sampleSize;

		int sum = populationSize;
		int x;
		int y;

		int i;
		if (n < 0 || colors < 0)
			throw new IllegalArgumentException(
					"Parameter negative in multihypergeo function");
		if (colors == 0)
			return destination;
		if (n > sum)
			throw new IllegalArgumentException(
					"n > sum in multihypergeo function");
		
		for (i = 0; i < colors - 1; i++) {
			// generate output by calling hypergeometric colors-1 times
				if (n==0){
					//sampling complete
					return destination;
				}
				y = source[i];
				x = simulateHypergeometricDistribution(sum, y, n);
				n -= x;
				sum -= y;
				destination[i] = x;
		}
		// get the last one
		destination[i] = n;
		return destination;
	}
	
	public static <E> ArrayList<E> shuffle(ArrayList<E> original){
		int size = original.size();
		int[] indexes = randomizeIndices(size);
		ArrayList<E> ans = new ArrayList<E>();
		for (int i = 0; i < indexes.length; i++) {
			ans.add(original.get(indexes[i]));
		}
		return ans;
	}
	
	
	/**
	 * Returns a randomize array of integers from 0 to size -1. 
	 * @param size
	 * @return
	 */
	public static int[] randomizeIndices(int size){
		int[] indexes = new int[size];
		for (int i = 0; i < indexes.length; i++) {
			indexes[i] = i;
		}
		MathArrays.shuffle(indexes, generator);
		return indexes;
	}

}
