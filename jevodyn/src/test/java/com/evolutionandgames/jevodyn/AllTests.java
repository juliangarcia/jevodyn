package com.evolutionandgames.jevodyn;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import com.evolutionandgames.jevodyn.stochastic.SimulationFixationProbabilityTest;
import com.evolutionandgames.jevodyn.stochastic.SimulationStationaryDistributionTest;
import com.evolutionandgames.jevodyn.stochastic.SimulationTimeSeriesTest;
import com.evolutionandgames.jevodyn.stochastic.SimulationTotalPayoffTest;
import com.evolutionandgames.jevodyn.stochastic.impl.GamePayoffCalculatorTest;
import com.evolutionandgames.jevodyn.stochastic.impl.MoranProcessTest;
import com.evolutionandgames.jevodyn.stochastic.impl.SimplePopulationImplTest;
import com.evolutionandgames.jevodyn.utils.RandomTest;


@RunWith(Suite.class)
@SuiteClasses({GamePayoffCalculatorTest.class,
			   SimplePopulationImplTest.class,
			   MoranProcessTest.class,
			   SimulationFixationProbabilityTest.class,
			   RandomTest.class, 
			   SimulationStationaryDistributionTest.class,
			   SimulationTimeSeriesTest.class,
			   SimulationTotalPayoffTest.class})

public class AllTests {

}
