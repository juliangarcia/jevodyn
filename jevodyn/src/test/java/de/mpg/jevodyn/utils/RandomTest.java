package de.mpg.jevodyn.utils;

import junit.framework.TestCase;

import org.junit.Test;

public class RandomTest extends TestCase {

	private static final double DELTA = 0.001;
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

}
