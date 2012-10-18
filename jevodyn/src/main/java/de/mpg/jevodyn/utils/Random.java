package de.mpg.jevodyn.utils;

import org.apache.commons.math3.random.MersenneTwister;

public class Random {
	
    private static Long seed = null;
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
	    Random.seed = null;
	    generator = new MersenneTwister();
	}
    }
	
	
    public static void seed() {
    	Random.seed(null);
    }

    private Random() {
	super();
	generator = new MersenneTwister();
    }

    public static int nextInt() {
	return generator.nextInt();
    }

    public static int nextInt (int maximumExclusive) throws IllegalArgumentException{
	if (maximumExclusive <=0) {
	    throw new IllegalArgumentException("limit for random integer must be positive");
	}
	return generator.nextInt(maximumExclusive);
    }
    
    public static int nextIntBetweenOneAndInclusive (int maximumInclusive) throws IllegalArgumentException{
	if (maximumInclusive <=0) {
	    throw new IllegalArgumentException("limit for random integer must be positive");
	}
	return generator.nextInt(maximumInclusive)+1;
    }

    public static double nextDouble() {
	return generator.nextDouble();
    }

    public static boolean nextBoolean() {
	return generator.nextBoolean();
    }

    public static MersenneTwister getRandom() {
        return generator;
    } 

    public static Long getSeed() {
        return seed;
    }
    
	public static int simulateDiscreteDistribution(double[] distribution) {
		double subtotal = 0.0;
		double r = Random.nextDouble();
		int j = 0;
		while (subtotal <= r) {
			subtotal = subtotal + distribution[j];
			j++;
		}
		return j - 1;
	}

}
