package com.evolutionandgames.jevodyn.stochastic;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.evolutionandgames.jevodyn.stochastic.Simulation;
import com.evolutionandgames.jevodyn.stochastic.impl.GamePayoffCalculator;
import com.evolutionandgames.jevodyn.stochastic.impl.MoranProcess;
import com.evolutionandgames.jevodyn.stochastic.impl.SimplePopulationImpl;
import com.evolutionandgames.jevodyn.utils.Games;


public class SimulationTotalPayoffTest {

	private static final double DELTA = 1.0;

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
