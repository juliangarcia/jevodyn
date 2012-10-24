package de.mpg.jevodyn.stochastic;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import de.mpg.jevodyn.stochastic.Simulation;
import de.mpg.jevodyn.stochastic.impl.GamePayoffCalculator;
import de.mpg.jevodyn.stochastic.impl.MoranProcess;
import de.mpg.jevodyn.stochastic.impl.SimplePopulationImpl;
import de.mpg.jevodyn.utils.Games;

public class SimulationTotalPayoffTest {

	private static final double DELTA = 0.5;

	@Test
	public void testSimulateStationaryDistribution() {
		int[] array = { 2, 2, 6 };
		Long seed = System.currentTimeMillis();
		// System.out.println(seed);
		SimplePopulationImpl population = new SimplePopulationImpl(array);
		MoranProcess mp = new MoranProcess(population,
				new GamePayoffCalculator(Games.allcTftAlld()), 0.001, 1.0);
		Simulation simulation = new Simulation(mp);
		int burningTimePerEstimate = 100;
		int samplesPerEstimate = 5000000;
		int numberOfEstimates = 5;
		int reportEveryTimeSteps =10;
		double total = simulation.estimateTotalPayoff(burningTimePerEstimate, samplesPerEstimate, numberOfEstimates, reportEveryTimeSteps, seed);
		assertEquals((0.0799139*3.0 + 3*0.66839172+0.25169438)*10, total, DELTA);
		
		
	}

}
