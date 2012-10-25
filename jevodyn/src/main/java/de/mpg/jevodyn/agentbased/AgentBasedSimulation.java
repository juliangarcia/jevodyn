package de.mpg.jevodyn.agentbased;


import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.google.common.collect.HashMultiset;
import com.google.common.collect.Multiset;
import com.google.common.collect.Multisets;

import de.mpg.jevodyn.agentbased.impl.AgentBasedPopulationImpl;
import de.mpg.jevodyn.utils.Random;

public class AgentBasedSimulation {
	
	
	private AgentBasedEvolutionaryProcess process;

	public AgentBasedSimulation(AgentBasedEvolutionaryProcess process) {
		super();
		this.process = process;
	}
	
	public double estimateFixationProbability(Agent mutant, Agent incumbent, int numberOfSamples,Long seed) {
		Random.seed(seed);
		int populationSize = this.process.getPopulation().getSize();
		int positives = 0;
		Agent[] startingPopulationArray = getStartingArray(mutant,incumbent,populationSize);
		for (int i = 0; i < numberOfSamples; i++) {
			this.process.reset(new AgentBasedPopulationImpl(startingPopulationArray));
			boolean fixated = false;
			while (!fixated) {
				this.process.stepWithoutMutation();
				fixated = this.process.getPopulation().getSetOfAgents().size()==1;
			}
			if (this.process.getPopulation().getAgent(0).equals(mutant))
				positives++;
		}
		return ((double) positives) / numberOfSamples;
	}

	private Agent[] getStartingArray(Agent mutant, Agent incumbent,int populationSize) {
		Agent[] ans = new Agent[populationSize];
		ans[0] = mutant;
		for (int i = 1; i < ans.length; i++) {
			ans[i]= incumbent;
		}
		return ans;
	}
	
	
	
	public Map<Agent, Double> estimateStationaryDistribution(int burningTimePerEstimate, int samplesPerEstimate, int numberOfEstimates, Long seed, int maximumResultSize, AgentBasedPopulationFactory factory) {
		Random.seed(seed);
		Multiset<Agent> multiset = HashMultiset.create();
		for (int estimate = 0; estimate < numberOfEstimates; estimate++) {
			process.reset(factory.createPopulation());
			for (int burningStep = 0; burningStep < burningTimePerEstimate; burningStep++) {
				process.step();
			}
			for (int sample = 0; sample < samplesPerEstimate; sample++) {
				process.step();
				for (int i = 0; i < this.process.getPopulation().getSize(); i++) {
					multiset.add(this.process.getPopulation().getAgent(i));
				}
			}
		}
		//build the answer
		double size = (double)multiset.size();
		Map<Agent, Double> ans = new HashMap<Agent, Double>();
		//create a view ordered by count
		Multiset<Agent> ordererdView = Multisets.copyHighestCountFirst(multiset);
		int i =0;
		for (Iterator<Agent> iterator = ordererdView.iterator(); iterator.hasNext();) {
			Agent agent = (Agent) iterator.next();
			ans.put(agent, ordererdView.count(agent)/size);
			if (i>=maximumResultSize) break;
		}
		return ans;
	}
	

}
