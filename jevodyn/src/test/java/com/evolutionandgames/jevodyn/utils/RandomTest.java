package com.evolutionandgames.jevodyn.utils;

import junit.framework.TestCase;

import org.apache.commons.math3.util.ArithmeticUtils;
import org.junit.Test;

import com.google.common.collect.HashMultiset;
import com.google.common.collect.Multiset;

public class RandomTest extends TestCase {

	private static final double DELTA = 0.005;
	private static final int MAX = 10;

	@Test
	public void testSimulateDiscreteDistribution() {
		Random.seed(null);
		double[] distribution = { Random.nextInt(MAX), Random.nextInt(MAX),
				Random.nextInt(MAX) };
		distribution = ArrayUtils.normalize(distribution);
		int samples0= 0;
		int samples1= 0;
		int samples2= 0;
		int numberOfSamples = 10000000;
		for (int i = 0; i < numberOfSamples; i++) {
			int result = Random.simulateDiscreteDistribution(distribution);
			switch (result) {
			case 0:
				samples0++;
				break;
			case 1:
				samples1++;
				break;
			case 2:
				samples2++;
				break;
			default:
				assertTrue(false);
			}
		}
		assertEquals(distribution[0], samples0/(double)numberOfSamples, DELTA);
		assertEquals(distribution[1], samples1/(double)numberOfSamples, DELTA);
		assertEquals(distribution[2], samples2/(double)numberOfSamples, DELTA);
	}
	
	
	@Test
	public void testSimulateGeometricDistribution() {
		Random.seed(null);
		double p = 0.1;
		int numberOfSamples = 10000000;
		Multiset<Integer> multiset = HashMultiset.create();
		for (int i = 0; i < numberOfSamples; i++) {
			int result = Random.simulateGeometricDistribution(p);
			multiset.add(result);
		}
		for (int k = 0; k < 10; k++) {
			assertEquals(Math.pow(1.0-p, k)*p, multiset.count(k)/(double)(numberOfSamples), DELTA);
		}
		
	}
	
	
	@Test
	public void testSimulateHyperGeometricDistribution() {
		Random.seed(null);
		int populationSize = 10;
		int numberOfSuccesses = 3;
		int sampleSize = 7;
		int numberOfSamples = 5000000;
		Multiset<Integer> multiset = HashMultiset.create();
		for (int i = 0; i < numberOfSamples; i++) {
			int result = Random.simulateHypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
			multiset.add(result);
		}
		for (int k = 0; k < 10; k++) {
			if (multiset.count(k) >0){
				double theory = (ArithmeticUtils.binomialCoefficientDouble(numberOfSuccesses, k)*
						ArithmeticUtils.binomialCoefficientDouble(populationSize-numberOfSuccesses, sampleSize-k))/ArithmeticUtils.binomialCoefficientDouble(populationSize, sampleSize);
				assertEquals(theory, multiset.count(k)/(double)(numberOfSamples), DELTA);	
			}
			
		}
		
	}
	
	
	@Test
	public void testMultivarateSimulateHyperGeometricDistribution() {
		Random.seed(null);
		int numberOfSamples = 5000000;
		
		int[] population = {5, 3, 4};
		int count = 0;
		int[] target = {3, 2, 2};
		
		for (int i = 0; i < numberOfSamples; i++) {
			int[] result = Random.simulateMultivariateHypergeometricDistribution(population, target[0]+target[1]+target[2], 
					population[0]+population[1]+population[2]);
			if (result[0] == target[0] && result[1] == target[1] && result[2] == target[2]){
				count++;
			}
		}
		double theory = (ArithmeticUtils.binomialCoefficientDouble(population[0], target[0])*
						(ArithmeticUtils.binomialCoefficientDouble(population[1], target[1]))*
						(ArithmeticUtils.binomialCoefficientDouble(population[2], target[2])))/
						ArithmeticUtils.binomialCoefficientDouble(population[0]+population[1]+population[2], 
								target[0]+target[1]+target[2]);
						
		assertEquals(theory, new Double(count)/new Double(numberOfSamples), DELTA);	
			}
			
		
		
	
	
	

}
