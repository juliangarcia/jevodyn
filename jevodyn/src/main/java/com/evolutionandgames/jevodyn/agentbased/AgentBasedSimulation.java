package com.evolutionandgames.jevodyn.agentbased;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.SortedSet;
import java.util.TreeSet;

import org.supercsv.cellprocessor.constraint.NotNull;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.io.CsvListWriter;
import org.supercsv.io.ICsvListWriter;
import org.supercsv.prefs.CsvPreference;

import com.evolutionandgames.jevodyn.agentbased.impl.AgentBasedPopulationImpl;
import com.evolutionandgames.jevodyn.utils.Random;
import com.google.common.util.concurrent.AtomicLongMap;


/**
 * Provides methods for simulating a given AgentBased process.
 * 
 * @author garcia
 * 
 */
public class AgentBasedSimulation {

	/**
	 * Process to be simulated
	 */
	private AgentBasedEvolutionaryProcess process;

	/***
	 * Default constructor takes a process.
	 * 
	 * @param process
	 */
	public AgentBasedSimulation(AgentBasedEvolutionaryProcess process) {
		super();
		this.process = process;
	}

	/***
	 * Estimate the fixation probability of mutant in a population of incumbent.
	 * 
	 * @param mutant
	 *            Agent
	 * @param incumbent
	 *            Agent
	 * @param numberOfSamples
	 * @param seed
	 * @return double
	 */
	public double estimateFixationProbability(Agent mutant, Agent incumbent,
			int numberOfSamples, Long seed) {
		Random.seed(seed);
		int populationSize = this.process.getPopulation().getSize();
		int positives = 0;
		Agent[] startingPopulationArray = getStartingArray(mutant, incumbent,
				populationSize);
		for (int i = 0; i < numberOfSamples; i++) {
			this.process.reset(new AgentBasedPopulationImpl(
					startingPopulationArray));
			boolean fixated = false;
			// step until fixation is reached
			while (!fixated) {
				this.process.stepWithoutMutation();
				fixated = this.process.getPopulation().getSetOfAgents().size() == 1;
			}
			// increase positives if it fixated to the mutant type
			if (this.process.getPopulation().getAgent(0).equals(mutant))
				positives++;
		}
		return ((double) positives) / numberOfSamples;
	}

	/**
	 * Help method, to initialize fixation calculations
	 * 
	 * @param mutant
	 * @param incumbent
	 * @param populationSize
	 * @return an array of Agent.
	 */
	private Agent[] getStartingArray(Agent mutant, Agent incumbent,
			int populationSize) {
		Agent[] ans = new Agent[populationSize];
		ans[0] = mutant;
		for (int i = 1; i < ans.length; i++) {
			ans[i] = incumbent;
		}
		return ans;
	}

	private AtomicLongMap<Agent> buildMultiset(int reportEveryTimeSteps, int numberOfEstimates, int burningTimePerEstimate, int samplesPerEstimate, AgentBasedPopulationFactory factory) {
		AtomicLongMap<Agent> map = AtomicLongMap.create();
		for (int estimate = 0; estimate < numberOfEstimates; estimate++) {
			process.reset(factory.createPopulation());
			for (int burningStep = 0; burningStep < burningTimePerEstimate; burningStep++) {
				process.step();
			}
			for (int sample = 0; sample < samplesPerEstimate; sample++) {
				process.step();
				if (sample % reportEveryTimeSteps == 0) {
					for (int i = 0; i < this.process.getPopulation().getSize(); i++) {
						map.incrementAndGet(this.process.getPopulation().getAgent(i));
					}
				}
			}
		}
		return map;
	}

	/**
	 * Estimate stationary distribution
	 * 
	 * @param burningTimePerEstimate
	 *            for every estimate the chain is left running without taking
	 *            samples
	 * @param timeStepsPerEstimate
	 *            once burningTime is over, we start taking this number of
	 *            samples
	 * @param numberOfEstimates
	 *            the process is repeatedas many times as number of estimate
	 *            requires
	 *@param reportEveryTimeSteps
	 *     takes one sample every this number of steps.           
	 *            
	 * @param seed 
	 *            for reproduciblity.
	 * @param factory
	 *            a class that generates a new starting population for every
	 *            estimate.
	 * @return A Map of Agent to a double frequency.
	 */
	public Map<Agent, Double> estimateStationaryDistribution(
			int burningTimePerEstimate, 
			int timeStepsPerEstimate,
			int numberOfEstimates,
			int reportEveryTimeSteps, 
			Long seed,
			AgentBasedPopulationFactory factory) {
		Random.seed(seed);
		AtomicLongMap<Agent> multiset = buildMultiset(reportEveryTimeSteps, numberOfEstimates, burningTimePerEstimate, timeStepsPerEstimate, factory);
		// build the answer
		long size = multiset.sum();
		// create a view ordered by count
		SortedSet<Map.Entry<Agent, Long>> sortedset = new TreeSet<Map.Entry<Agent, Long>>(
	            new Comparator<Map.Entry<Agent, Long>>() {
	                //@Override
	                public int compare(Map.Entry<Agent, Long> e1,
	                        Map.Entry<Agent, Long> e2) {
	                    return -1*(e1.getValue().compareTo(e2.getValue()));
	                }
	            });
		sortedset.addAll(multiset.asMap().entrySet());
		Map<Agent, Double> ans = new HashMap<Agent, Double>();
		for (Iterator<Entry<Agent, Long>> iterator = sortedset.iterator(); iterator.hasNext();) {
			Entry<Agent, Long> entry = (Entry<Agent, Long>) iterator.next();
			ans.put(entry.getKey(),  ((double)entry.getValue()/(double)size));
		}
		return ans;
	}

	/**
	 * Simulates evolution writing the ouput to a file.
	 * 
	 * @param numberOfTimeSteps
	 * @param reportEveryTimeSteps
	 * @param seed
	 * @param fileName
	 * @throws IOException
	 */
	public void simulateTimeSeries(int numberOfTimeSteps,
			int reportEveryTimeSteps, Long seed, String fileName)
			throws IOException {
		Random.seed(seed);
		ICsvListWriter listWriter = null;
		String[] header = this.buildHeader();
		CellProcessor[] processors = this.getProcessors();
		try {
			listWriter = new CsvListWriter(new FileWriter(fileName),
					CsvPreference.STANDARD_PREFERENCE);
			// write the header
			listWriter.writeHeader(header);
			// write the initial zero step content
			listWriter.write(this.currentStateRow(process), processors);
			// repeat for as many steps as requested
			for (int i = 0; i < numberOfTimeSteps; i++) {
				// step
				process.step();
				// if time to repor, report
				if (process.getTimeStep() % reportEveryTimeSteps == 0) {
					listWriter.write(this.currentStateRow(process), processors);
				}
			}
		} finally {
			// close files no matter what
			if (listWriter != null) {
				listWriter.close();
			}
		}

	}

	/**
	 * Helper method to write the csv file
	 * 
	 * @return
	 */
	private CellProcessor[] getProcessors() {

		CellProcessor timeStepProcessor = new NotNull();
		CellProcessor populationProcessor = new NotNull();
		CellProcessor totalPayoffProcessor = new NotNull();
		final CellProcessor[] processors = { timeStepProcessor,
				totalPayoffProcessor, populationProcessor };
		return processors;
	}

	/**
	 * Helper method to write the csv file
	 * 
	 * @return
	 */
	private String[] buildHeader() {
		// build the header
		final String[] header = { "timeStep", "totalPayoff", "population" };
		return header;
	}

	/**
	 * Turns the current population into a list to be written in the csv file
	 * 
	 * @param process
	 * @return
	 */
	private List<Object> currentStateRow(AgentBasedEvolutionaryProcess process) {
		ArrayList<Object> ans = new ArrayList<Object>();
		ans.add(process.getTimeStep());
		ans.add(process.getTotalPopulationPayoff());
		ans.add(process.getPopulation().toString());
		return ans;
	}

	/**
	 * Estimate the total payoff, paramters are similar to those of estimate
	 * distribution. Instead of counting the agents, all we care about here is
	 * the population payoff
	 * 
	 * @param burningTimePerEstimate
	 * @param timeStepsPerEstimate
	 * @param numberOfEstimates
	 * @param reportEveryTimeSteps
	 * @param seed
	 * @param factory
	 * @return double
	 */
	public double estimateTotalPayoff(int burningTimePerEstimate,
			int timeStepsPerEstimate, int numberOfEstimates,
			int reportEveryTimeSteps, Long seed,
			AgentBasedPopulationFactory factory) {
		Random.seed(seed);
		double payoffSum = 0.0;
		int totalNumberOfSamples = 0;
		for (int estimate = 0; estimate < numberOfEstimates; estimate++) {
			process.reset(factory.createPopulation());
			for (int burningStep = 0; burningStep < burningTimePerEstimate; burningStep++) {
				process.step();
			}
			for (int sample = 0; sample < timeStepsPerEstimate; sample++) {
				// sample every time?
				process.step();
				if (sample % reportEveryTimeSteps == 0) {
					payoffSum = payoffSum + process.getTotalPopulationPayoff();
					totalNumberOfSamples++;
				}

			}
		}
		return payoffSum / totalNumberOfSamples;
	}
}
