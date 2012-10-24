package de.mpg.jevodyn.agentbased;

public interface Mutator {
	
	//in case of returning an agent different than the one being returned, a new copy must be created!!
	public Agent mutate(Agent agent);
}
