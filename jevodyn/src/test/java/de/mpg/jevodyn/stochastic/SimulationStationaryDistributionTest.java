package de.mpg.jevodyn.stochastic;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import de.mpg.jevodyn.stochastic.Simulation;
import de.mpg.jevodyn.stochastic.impl.GamePayoffCalculator;
import de.mpg.jevodyn.stochastic.impl.MoranProcess;
import de.mpg.jevodyn.stochastic.impl.SimplePopulationImpl;
import de.mpg.jevodyn.utils.Games;

public class SimulationStationaryDistributionTest {

	private static final double DELTA = 0.05;

	@Test
	public void testSimulateStationaryDistributionNeutral() {
		int[] array = { 2, 2, 6 };
		Long seed = System.currentTimeMillis();
		// System.out.println(seed);
		SimplePopulationImpl population = new SimplePopulationImpl(array);
		MoranProcess mp = new MoranProcess(population,
				new GamePayoffCalculator(Games.allcTftAlld()), 0.01, 0.0);
		Simulation simulation = new Simulation(
				mp);
		int burningTimePerEstimate = 100;
		int samplesPerEstimate = 5000000;
		int numberOfEstimates = 5;
		double[] ans = simulation.estimateStationaryDistribution(
				burningTimePerEstimate, samplesPerEstimate, numberOfEstimates,
				seed);
		for (int i = 0; i < ans.length; i++) {
			assertEquals(1.0 / population.getNumberOfTypes(), ans[i], DELTA);
		}
	}
	
	
	@Test
	public void testSimulateStationaryDistribution() {
		int[] array = { 2, 2, 6 };
		Long seed = System.currentTimeMillis();
		// System.out.println(seed);
		SimplePopulationImpl population = new SimplePopulationImpl(array);
		MoranProcess mp = new MoranProcess(population,
				new GamePayoffCalculator(Games.allcTftAlld()), 0.001, 1.0);
		Simulation simulation = new Simulation(
				mp);
		int burningTimePerEstimate = 100;
		int samplesPerEstimate = 5000000;
		int numberOfEstimates = 5;
		double[] ans = simulation.estimateStationaryDistribution(
				burningTimePerEstimate, samplesPerEstimate, numberOfEstimates,
				seed);
		assertEquals(0.0799139, ans[0], DELTA);
		assertEquals(0.66839172, ans[1], DELTA);
		assertEquals(0.25169438, ans[2], DELTA);
	}
	

}
