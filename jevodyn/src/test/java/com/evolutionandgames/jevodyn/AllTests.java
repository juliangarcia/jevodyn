package com.evolutionandgames.jevodyn;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import com.evolutionandgames.jevodyn.impl.GamePayoffCalculatorTest;
import com.evolutionandgames.jevodyn.impl.MoranProcessTest;
import com.evolutionandgames.jevodyn.impl.SimplePopulationImplTest;
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
