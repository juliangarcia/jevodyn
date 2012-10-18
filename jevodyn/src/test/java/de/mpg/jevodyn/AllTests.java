package de.mpg.jevodyn;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ de.mpg.jevodyn.stochastic.impl.GamePayoffCalculatorTest.class, 
	de.mpg.jevodyn.stochastic.impl.SimplePopulationImplTest.class })
public class AllTests {

}
