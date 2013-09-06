package com.evolutionandgames.jevodyn;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.evolutionandgames.jevodyn.dimorphic.DimorphicMoranProcess;
import com.evolutionandgames.jevodyn.dimorphic.DimorphicPopulation;
import com.evolutionandgames.jevodyn.impl.GamePayoffCalculator;
import com.evolutionandgames.jevodyn.utils.Games;
import com.evolutionandgames.jevodyn.utils.PayoffToFitnessMapping;


public class StationaryDistributionMoranProcessSmallMutationTest {

	private static final double DELTA = 0.01;

	@Test
	public void testSimulateStationaryDistributionNeutral() {
		Long seed = System.currentTimeMillis();
		DimorphicPopulation population = new DimorphicPopulation(10,0,3);
		DimorphicMoranProcess mp  = new DimorphicMoranProcess(population, PayoffToFitnessMapping.EXPONENTIAL, 0.0, 0.01, new GamePayoffCalculator(Games.allcTftAlld()));
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
		Long seed = System.currentTimeMillis();
		DimorphicPopulation population = new DimorphicPopulation(10,0,3);
		DimorphicMoranProcess mp  = new DimorphicMoranProcess(population, PayoffToFitnessMapping.EXPONENTIAL, 1.0, 0.001, new GamePayoffCalculator(Games.allcTftAlld()));
		int burningTimePerEstimate = 1000;
		int samplesPerEstimate = 5000000;
		int numberOfEstimates = 100;
		double[] ans = mp.estimateStationaryDistributionSmallMutation(burningTimePerEstimate, samplesPerEstimate, numberOfEstimates, seed);
		assertEquals(0.0799139, ans[0], DELTA);
		assertEquals(0.66839172, ans[1], DELTA);
		assertEquals(0.25169438, ans[2], DELTA);
	}
	

}
