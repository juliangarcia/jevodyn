package de.mpg.jevodyn.agentbased;

/**
 * Interface for an agent. Any object is already an agent, because all the methods here inherint from Object.
 * @author garcia
 *
 */
public interface Agent {

	 public boolean equals(Object other);
	 public int hashCode();
	 public String toString();
	
}
