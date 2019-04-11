package com.evolutionandgames.jevodyn.utils;

import java.util.ArrayList;

import org.junit.Test;

import junit.framework.TestCase;

public class RandomTestGenerateExcluding extends TestCase {

	private static final double DELTA = 0.005;
	
	@Test
	public void testGenerateExcluding() {
		Random.seed(null);
		int numberOfSamples = 10000000;
		ArrayList<Integer> exclude = new ArrayList<Integer>();
		exclude.add(1);
		exclude.add(2);
		double[] distribution = {0.5, 0, 0, 0.5};
		
		
		int samples0 = 0;
		int samples1 = 0;
		int samples2 = 0;
		int samples3 = 0;
		
		for (int i = 0; i < numberOfSamples; i++) {
			int result = Random.generateRandomExcluding(4, exclude);
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
			case 3:
				samples3++;
				break;
			default:
				assertTrue(false);
			}
		}
		System.out.println(samples0/(double)numberOfSamples);
		System.out.println(samples1/(double)numberOfSamples);
		System.out.println(samples2/(double)numberOfSamples);
		System.out.println(samples3/(double)numberOfSamples);
		assertEquals(distribution[0], samples0/(double)numberOfSamples, DELTA);
		assertEquals(distribution[1], samples1/(double)numberOfSamples, DELTA);
		assertEquals(distribution[2], samples2/(double)numberOfSamples, DELTA);
		assertEquals(distribution[3], samples3/(double)numberOfSamples, DELTA);
	}
	
	

	
	

}
