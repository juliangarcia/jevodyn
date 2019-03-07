package com.evolutionandgames.jevodyn.assymetric.impl;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.supercsv.cellprocessor.constraint.NotNull;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.io.CsvListWriter;
import org.supercsv.io.ICsvListWriter;
import org.supercsv.prefs.CsvPreference;

import com.evolutionandgames.jevodyn.assymetric.AsymetricEvolutionaryProcess;
import com.evolutionandgames.jevodyn.assymetric.AsymmetricPopulation;
import com.evolutionandgames.jevodyn.utils.Random;


public class AsymmetricSimulation {

	AsymetricEvolutionaryProcess process;

	public AsymmetricSimulation(AsymetricEvolutionaryProcess process) {
		super();
		this.process = process;
	}
	
	private static int[] getStartingArray(int indexOfTheMutant,
			int indexOfTheIncumbent, int numberOfTypes, int populationSize) {
		int[] array = new int[numberOfTypes];
		array[indexOfTheIncumbent] = populationSize - 1;
		array[indexOfTheMutant] = 1;
		return array;
	}
	

	public double estimateFixationProbabilityPopulation1(int indexOfTheMutant, int indexOfTheIncumbent, int numberOfSamples,
			Long seed) {
		
		int numberOfTypes = this.process.getPopulation().getNumberOfTypes(); 
		int populationSize = this.process.getPopulation().getSize1();
		Random.seed(seed);
		int positives = 0;
		int[] startingPopulationArray = getStartingArray(indexOfTheMutant,
				indexOfTheIncumbent, numberOfTypes, populationSize);
		
		int[] otherPop = new int[numberOfTypes];
		otherPop[indexOfTheIncumbent] = this.process.getPopulation().getSize2();
		
		
		
		for (int i = 0; i < numberOfSamples; i++) {
			this.process
					.reset(new AsymmetricPopulationImpl(startingPopulationArray, otherPop));
			int fixatedOn = -1;
			while (fixatedOn == -1) {
				this.process.stepWithoutMutation();
				fixatedOn = this.process.getPopulation().getFixatedType1();
			}
			if (fixatedOn == indexOfTheMutant)
				positives++;
		}
		return ((double) positives) / numberOfSamples;
	}
	
	
	public double estimateFixationProbabilityPopulation2(int indexOfTheMutant, int indexOfTheIncumbent, int numberOfSamples,
			Long seed) {
		
		int numberOfTypes = this.process.getPopulation().getNumberOfTypes(); 
		int populationSize = this.process.getPopulation().getSize2();
		Random.seed(seed);
		int positives = 0;
		int[] startingPopulationArray = getStartingArray(indexOfTheMutant,
				indexOfTheIncumbent, numberOfTypes, populationSize);
		
		int[] otherPop = new int[numberOfTypes];
		otherPop[indexOfTheIncumbent] = this.process.getPopulation().getSize1();
		
		
		
		for (int i = 0; i < numberOfSamples; i++) {
			this.process
					.reset(new AsymmetricPopulationImpl(otherPop, startingPopulationArray));
			int fixatedOn = -1;
			while (fixatedOn == -1) {
				this.process.stepWithoutMutation();
				fixatedOn = this.process.getPopulation().getFixatedType2();
			}
			if (fixatedOn == indexOfTheMutant)
				positives++;
		}
		return ((double) positives) / numberOfSamples;
	}
	
	
	


	
	

	public void simulateTimeSeries(int numberOfTimeSteps,
			int reportEveryTimeSteps, Long seed, boolean includeTotalPayoff, String fileName)
			throws IOException {
		Random.seed(seed);
		process.setKeepTrackTotalPayoff(includeTotalPayoff);
		ICsvListWriter listWriter = null;
		String[] header = this.buildHeader(process.getPopulation(),
				includeTotalPayoff);
		CellProcessor[] processors = this.getProcessors(process.getPopulation(),
				includeTotalPayoff);
		try {
			listWriter = new CsvListWriter(new FileWriter(fileName),
					CsvPreference.STANDARD_PREFERENCE);
			// write the header
			listWriter.writeHeader(header);
			// write the initial zero step content
			listWriter.write(this.currentStateRow(process, includeTotalPayoff),
					processors);
			// repeat for as many steps as requested
			for (int i = 0; i < numberOfTimeSteps; i++) {
				// step
				process.step();
				// if time to repor, report
				if (process.getTimeStep() % reportEveryTimeSteps == 0) {
					listWriter.write(
							this.currentStateRow(process, includeTotalPayoff),
							processors);
				}
			}

		} finally {
			// close files no matter what
			if (listWriter != null) {
				listWriter.close();
			}
		}

	}

	private List<Object> currentStateRow(AsymetricEvolutionaryProcess process,
			boolean includeTotalPayoff) {
		ArrayList<Object> ans = new ArrayList<Object>();
		ans.add(process.getTimeStep());
		for (int i = 0; i < process.getPopulation().getNumberOfTypes(); i++) {
			ans.add(process.getPopulation().getNumberOfCopies1(i));
		}
		for (int i = 0; i < process.getPopulation().getNumberOfTypes(); i++) {
			ans.add(process.getPopulation().getNumberOfCopies2(i));
		}
		if (includeTotalPayoff)
			ans.add(process.getTotalPopulationPayoff());
		return ans;
	}

	private String[] buildHeader(AsymmetricPopulation startingPopulation,
			boolean includeTotalPayoff) {
		// build the header
		int headerSize = getHeaderSize(startingPopulation, includeTotalPayoff);
		final String[] header = new String[headerSize];
		header[0] = "timeStep";
		for (int i = 0; i < startingPopulation.getNumberOfTypes(); i++) {
			header[i + 1] = "P1_" + String.valueOf(i);
		}
		for (int i = 0; i < startingPopulation.getNumberOfTypes(); i++) {
			header[i + 1] = "P2_" + String.valueOf(i);
		}
		if (includeTotalPayoff) {
			header[headerSize - 1] = "totalPayoff";
		}
		return header;
	}

	private CellProcessor[] getProcessors(AsymmetricPopulation startingPopulation,
			boolean includeTotalPayoff) {
		int headerSize = getHeaderSize(startingPopulation, includeTotalPayoff);
		final CellProcessor[] processors = new CellProcessor[headerSize];
		processors[0] = new NotNull();
		for (int i = 0; i < (2*startingPopulation.getNumberOfTypes()); i++) {
			processors[i + 1] = new NotNull();
		}
		if (includeTotalPayoff) {
			processors[headerSize - 1] = new NotNull();
		}
		return processors;
	}

	private int getHeaderSize(AsymmetricPopulation startingPopulation,
			boolean includeTotalPayoff) {
		int headerSize = (2*startingPopulation.getNumberOfTypes()) + 1;
		if (includeTotalPayoff) {
			headerSize++;
		}
		return headerSize;
	}

	

}
