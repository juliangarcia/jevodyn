package de.mpg.jevodyn;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import de.mpg.jevodyn.agentbased.impl.AgentBasedPopulationImplTest;
import de.mpg.jevodyn.agentbased.impl.AgentBasedWrightFisherProcessWithAssortmentTest;
import de.mpg.jevodyn.stochastic.SimulationFixationProbabilityTest;
import de.mpg.jevodyn.stochastic.SimulationStationaryDistributionTest;
import de.mpg.jevodyn.stochastic.SimulationTimeSeriesTest;
import de.mpg.jevodyn.stochastic.SimulationTotalPayoffTest;
import de.mpg.jevodyn.stochastic.impl.GamePayoffCalculatorTest;
import de.mpg.jevodyn.stochastic.impl.MoranProcessTest;
import de.mpg.jevodyn.stochastic.impl.SimplePopulationImplTest;
import de.mpg.jevodyn.utils.RandomTest;

@RunWith(Suite.class)
@SuiteClasses({GamePayoffCalculatorTest.class,
			   SimplePopulationImplTest.class,
			   MoranProcessTest.class,
			   SimulationFixationProbabilityTest.class,
			   RandomTest.class, 
			   SimulationStationaryDistributionTest.class,
			   SimulationTimeSeriesTest.class,
			   SimulationTotalPayoffTest.class,
			   AgentBasedPopulationImplTest.class,
			   AgentBasedWrightFisherProcessWithAssortmentTest.class})

public class AllTests {

}
