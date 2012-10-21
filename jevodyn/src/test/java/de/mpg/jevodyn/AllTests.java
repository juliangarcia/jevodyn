package de.mpg.jevodyn;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import de.mpg.jevodyn.stochastic.impl.GamePayoffCalculatorTest;
import de.mpg.jevodyn.stochastic.impl.MoranProcessTest;
import de.mpg.jevodyn.stochastic.impl.SimplePopulationImplTest;
import de.mpg.jevodyn.stochastic.simulation.SimulationFixationProbabilityTest;
import de.mpg.jevodyn.utils.RandomTest;

@RunWith(Suite.class)
@SuiteClasses({GamePayoffCalculatorTest.class,
			   SimplePopulationImplTest.class,
			   MoranProcessTest.class,
			   SimulationFixationProbabilityTest.class,
			   RandomTest.class})

public class AllTests {

}
