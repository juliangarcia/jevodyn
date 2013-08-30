package com.evolutionandgames.jevodyn;

import junit.framework.TestCase;

import org.junit.Test;

import com.evolutionandgames.jevodyn.Simulation;
import com.evolutionandgames.jevodyn.impl.GamePayoffCalculator;
import com.evolutionandgames.jevodyn.impl.MoranProcess;
import com.evolutionandgames.jevodyn.impl.SimplePopulationImpl;
import com.evolutionandgames.jevodyn.utils.Games;


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
		assertEquals(0.7364040545619178, estimate, DELTA);	
	}

}
