package de.mpg.jevodyn.stochastic.simulation;

import junit.framework.TestCase;

import org.junit.Test;

import de.mpg.jevodyn.stochastic.Simulation;
import de.mpg.jevodyn.stochastic.impl.GamePayoffCalculator;
import de.mpg.jevodyn.stochastic.impl.MoranProcess;
import de.mpg.jevodyn.stochastic.impl.SimplePopulationImpl;
import de.mpg.jevodyn.utils.Games;

public class SimulationFixationProbabilityTest extends TestCase {

	private static final double DELTA = 0.001;

	@Test
	public void testEstimateNeutralMutant() {
		int[] array = {5,5};
		int popSize =10;
		Long seed = System.currentTimeMillis();
		//System.out.println(seed);
		MoranProcess mp = new MoranProcess(new SimplePopulationImpl(array), new GamePayoffCalculator(Games.hawkDoveGame()), 0.1, 0.0);
		Simulation simulationFixationProbability = new Simulation(mp);
		double estimate = simulationFixationProbability.estimateFixationProbability(0, 1, 1000000, seed);
		assertEquals(1.0/popSize, estimate, DELTA);	
	}
	
	@Test
	public void testNonTrivialFixation() {
		int[] array = {5,5};
		Long seed = System.currentTimeMillis();
		MoranProcess mp = new MoranProcess(new SimplePopulationImpl(array), new GamePayoffCalculator(Games.prionersDilemma()), 0.1, 1.0);
		Simulation simulationFixationProbability = new Simulation(mp);
		double estimate = simulationFixationProbability.estimateFixationProbability(1, 0, 1000000, seed);
		System.out.println(estimate);
		assertEquals(0.7364040545619178, estimate, DELTA);	
	}

}
