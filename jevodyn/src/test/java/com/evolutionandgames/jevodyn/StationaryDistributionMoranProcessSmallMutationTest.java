package com.evolutionandgames.jevodyn;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.evolutionandgames.jevodyn.impl.GamePayoffCalculator;
import com.evolutionandgames.jevodyn.impl.MoranProcess;
import com.evolutionandgames.jevodyn.impl.SimplePopulationImpl;
import com.evolutionandgames.jevodyn.utils.Games;


public class StationaryDistributionMoranProcessSmallMutationTest {

	private static final double DELTA = 0.05;

	@Test
	public void testSimulateStationaryDistributionNeutral() {
		int[] array = { 2, 2, 6 };
		Long seed = System.currentTimeMillis();
		// System.out.println(seed);
		SimplePopulationImpl population = new SimplePopulationImpl(array);
		MoranProcess mp = new MoranProcess(population,
				new GamePayoffCalculator(Games.allcTftAlld()), 0.01, 0.0);
		int burningTimePerEstimate = 100;
		int samplesPerEstimate = 5000000;
		int numberOfEstimates = 5;
		double[] ans = mp.estimateStationaryDistributionSmallMutation(burningTimePerEstimate, samplesPerEstimate, numberOfEstimates, seed);
		for (int i = 0; i < ans.length; i++) {
			assertEquals(1.0 / population.getNumberOfTypes(), ans[i], DELTA);
		}
	}
	
	
	@Test
	public void testSimulateStationaryDistribution() {
		int[] array = { 2, 2, 6 };
		Long seed = System.currentTimeMillis();
		SimplePopulationImpl population = new SimplePopulationImpl(array);
		MoranProcess mp = new MoranProcess(population,
				new GamePayoffCalculator(Games.allcTftAlld()), 0.001, 1.0);
		int burningTimePerEstimate = 100;
		int samplesPerEstimate = 5000000;
		int numberOfEstimates = 5;
		double[] ans = mp.estimateStationaryDistributionSmallMutation(burningTimePerEstimate, samplesPerEstimate, numberOfEstimates, seed);
		assertEquals(0.0799139, ans[0], DELTA);
		assertEquals(0.66839172, ans[1], DELTA);
		assertEquals(0.25169438, ans[2], DELTA);
	}
	

}
