package com.evolutionandgames.jevodyn.utils;

import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.distribution.PoissonDistribution;
import org.apache.commons.math3.random.MersenneTwister;

/**
 * This class uses the Mersene Twister to generate pseudo random numbers. It is a wrapper for the Mersene twister implementation of Commons math.
 *  It ensures that the generator is shared in a program, by making the seed and function static.
 * @author garcia
 *
 */
public class Random {
	
    protected static Long seed = null;
    private static MersenneTwister generator = null;

    /**
         * Sets the random generator seed
         * 
         * @param seed
         */
    public static void seed(Long seed) {

	if (seed != null) {
	    Random.seed = new Long(seed);
	    generator = new MersenneTwister(seed);
	} else {
	    Random.seed = System.currentTimeMillis();
	    generator = new MersenneTwister(Random.seed);
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
	generator = new MersenneTwister();
    }

    
    public static int nextInt (int maximumExclusive) throws IllegalArgumentException{
	if (maximumExclusive <=0) {
	    throw new IllegalArgumentException("limit for random integer must be positive");
	}
	return generator.nextInt(maximumExclusive);
    }
    
    public static double nextDouble() {
	return generator.nextDouble(); 
    }

    public static boolean nextBoolean() {
	return generator.nextBoolean();
    }
    
    public static boolean bernoulliTrial(double successProbability){
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
			//it can go over the index in case2 the comparisons are really small, when r is close to 1.0. 
			//This usually means that the last element should be returned.
			return distribution.length -1;
		}
	}
	
	/***
	 * Simulates a poisson distributed random variable
	 * @param mean
	 * @return
	 */
	public static int simulatePoissonDistribution(double mean){
		return new PoissonDistribution(Random.generator, mean, PoissonDistribution.DEFAULT_EPSILON, PoissonDistribution.DEFAULT_MAX_ITERATIONS).sample();
	}
	
	/***
	 * Simulates a Normally distributed random variable
	 * @param mean
	 * @param standardDeviation
	 * @return
	 */
	public static double simulateNormalDistribution(double mean, double standardDeviation){
		return new NormalDistribution(Random.generator, mean, standardDeviation, NormalDistribution.DEFAULT_INVERSE_ABSOLUTE_ACCURACY).sample();
	}
	

}