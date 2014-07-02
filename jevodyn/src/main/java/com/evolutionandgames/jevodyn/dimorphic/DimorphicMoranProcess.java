package com.evolutionandgames.jevodyn.dimorphic;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.math3.linear.RealMatrix;
import org.supercsv.cellprocessor.constraint.NotNull;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.io.CsvListWriter;
import org.supercsv.io.ICsvListWriter;
import org.supercsv.prefs.CsvPreference;

import com.evolutionandgames.jevodyn.utils.ArrayUtils;
import com.evolutionandgames.jevodyn.utils.PayoffToFitnessMapping;
import com.evolutionandgames.jevodyn.utils.Random;

public class DimorphicMoranProcess {

	protected int timeStep;
	protected DimorphicPopulation population;
	protected PayoffToFitnessMapping mapping;
	protected double intensityOfSelection;
	protected double mutationProbability;
	protected RealMatrix mutationKernel;
	protected DimorphicPayoffCalculator payoffCalculator;

	public void step() {
		double[] payoffs = this.payoffCalculator.payoff(this.population);
		double fitnessResident = this.population.getNumberOfResidents()
				* mapFitness(payoffs[0], this.mapping);
		double fitnessMutant = this.population.getNumberOfMutants()
				* mapFitness(payoffs[1], this.mapping);
		boolean mutantBirth = Random.bernoulliTrial(fitnessMutant
				/ (fitnessResident + fitnessMutant));
		boolean mutantDeath = Random.bernoulliTrial(this.population
				.getMutantFrequency());
		// (true true) and (false false ) cancel out and nothing happens
		if (mutantBirth && !mutantDeath) {
			// true false
			this.population.incrementNumberOfMutants();
		} else if (!mutantBirth && mutantDeath) {
			// false true
			this.population.decrementNumberOfMutants();
		}
		this.timeStep++;
	}
	
	
	
	

	private double mapFitness(double payoffValue,
			PayoffToFitnessMapping fitnessMapping) {
		switch (fitnessMapping) {
		case LINEAR:
			return 1.0 - this.intensityOfSelection + this.intensityOfSelection
					* payoffValue;
		case EXPONENTIAL:
			return Math.exp(intensityOfSelection * payoffValue);
		}
		throw new IllegalArgumentException("Inexistent payoff mapping");
	}

	public DimorphicMoranProcess(DimorphicPopulation population,
			PayoffToFitnessMapping mapping, double intensityOfSelection,
			double mutationProbability,
			DimorphicPayoffCalculator payoffCalculator) {
		super();
		this.timeStep = 0;
		this.population = population;
		this.mapping = mapping;
		this.intensityOfSelection = intensityOfSelection;
		this.mutationProbability = mutationProbability;
		this.mutationKernel = ArrayUtils.uniformMutationKernel(
				this.mutationProbability, this.population.getNumberOfTypes());
		this.payoffCalculator = payoffCalculator;

	}

	public DimorphicMoranProcess(DimorphicPopulation population,
			PayoffToFitnessMapping mapping, double intensityOfSelection,
			RealMatrix mutationKernel,
			DimorphicPayoffCalculator payoffCalculator) {
		super();
		this.timeStep = 0;
		this.population = population;
		this.mapping = mapping;
		this.intensityOfSelection = intensityOfSelection;
		this.mutationKernel = mutationKernel;
		this.payoffCalculator = payoffCalculator;
	}

	public double[] estimateStationaryDistributionSmallMutation(
			int burningTimePerEstimate, int samplesPerEstimate,
			int numberOfEstimates, Long seed) {
		Random.seed(seed);
		int numberOfTypes = this.population.getNumberOfTypes();
		int populationSize = this.population.getSize();
		long[] countPerStrategy = new long[numberOfTypes];
		for (int estimate = 0; estimate < numberOfEstimates; estimate++) {
			// for every new esimate start at a random type
			int typeOfResident = Random.nextInt(numberOfTypes);
			this.population = new DimorphicPopulation(populationSize,
					typeOfResident, numberOfTypes);
			this.timeStep = 0;
			// burning time
			this.burn(burningTimePerEstimate);
			int samples = 0;
			// run to fixation and count
			while (samples < samplesPerEstimate) {
				while (!this.population.isFixated()) {
					countPerStrategy[this.population.getTypeOfResident()] = countPerStrategy[this.population
							.getTypeOfResident()]
							+ this.population.getNumberOfResidents();
					countPerStrategy[this.population.getTypeOfMutant()] = countPerStrategy[this.population
							.getTypeOfMutant()]
							+ this.population.getNumberOfMutants();
					this.step();
					samples++;
				}
				if (samples > samplesPerEstimate)
					break;
				// here we are fixated
				int residentType = this.population.getTypeOfResident();
				double escapeProbability = 1.0 - this.mutationKernel.getEntry(
						residentType, residentType);
				int escapeTime = Random
						.simulateGeometricDistribution(escapeProbability);
				samples = samples + escapeTime;
				countPerStrategy[this.population.getTypeOfResident()] = countPerStrategy[this.population
						.getTypeOfResident()]
						+ escapeTime
						* this.population.getSize();
				// add the mutant
				double[] distibutionGivenThatIJumpedOut = transformToConditional(
						mutationKernel.getRow(residentType), residentType);
				int mutantType = Random
						.simulateDiscreteDistribution(distibutionGivenThatIJumpedOut);
				this.population.introduceNewMutant(mutantType);
			}
		}
		return ArrayUtils.normalize(countPerStrategy);
	}

	private void burn(int burningTimePerEstimate) {
		int burningStep = 0;
		if (!this.population.isFixated())
			throw new IllegalStateException(
					"This method assumes a fixated population");
		while (burningStep < burningTimePerEstimate) {
			// compute and add time to escape
			int residentType = this.population.getTypeOfResident();
			double escapeProbability = 1.0 - this.mutationKernel.getEntry(
					residentType, residentType);
			int escapeTime = Random
					.simulateGeometricDistribution(escapeProbability);
			burningStep = burningStep + escapeTime;
			// add the mutant
			double[] distibutionGivenThatIJumpedOut = transformToConditional(
					mutationKernel.getRow(residentType), residentType);
			int mutantType = Random
					.simulateDiscreteDistribution(distibutionGivenThatIJumpedOut);
			this.population.introduceNewMutant(mutantType);
			// if burning time is not over, run to fixation or until burning
			// time is over
			while (!this.population.isFixated()
					&& burningStep < burningTimePerEstimate) {
				this.step();
				burningStep++;
			}
		}
	}

	private double[] transformToConditional(double[] row, int focalIndex) {
		double weight = 1.0 - row[focalIndex];
		double ans[] = new double[row.length];
		ans[focalIndex] = 0.0;
		for (int i = 0; i < ans.length; i++) {
			if (i != focalIndex) {
				ans[i] = row[i] / weight;
			}
		}
		return ans;
	}
	
	
	public int stepFastForward() {
        double[] payoffs = this.payoffCalculator.payoff(this.population);
        double fitnessResident = this.population.getNumberOfResidents()
                * mapFitness(payoffs[0], this.mapping);
        double fitnessMutant = this.population.getNumberOfMutants()
                * mapFitness(payoffs[1], this.mapping);
        double p = fitnessMutant / (fitnessResident + fitnessMutant);// mutantSelectedProbability
        double q = this.population.getMutantFrequency();//mutantForDeathProbability
        
        double probabilityMutantIncrement = p*(1.0-q);
        double probabilityMutantDecrement = q*(1.0-p);
        double probabilitySomethingHappehs = probabilityMutantDecrement + probabilityMutantIncrement;
        
        int timeStepsUntilSomethingHappens = Random.simulateGeometricDistribution(probabilitySomethingHappehs);
        
        if (Random.bernoulliTrial(probabilityMutantIncrement/probabilitySomethingHappehs)) {
            this.population.incrementNumberOfMutants();
        }else{
            this.population.decrementNumberOfMutants();
        }
        this.timeStep= timeStep + timeStepsUntilSomethingHappens +1;
        return timeStepsUntilSomethingHappens +1;
}


private void burnFastForward(int burningTimePerEstimate) {
        int burningStep = 0;
        if (!this.population.isFixated())
            throw new IllegalStateException(
                    "This method assumes a fixated population");
        while (burningStep < burningTimePerEstimate) {
            // compute and add time to escape
            int residentType = this.population.getTypeOfResident();
            double escapeProbability = 1.0 - this.mutationKernel.getEntry(
                    residentType, residentType);
            int escapeTime = Random
                    .simulateGeometricDistribution(escapeProbability);
            burningStep = burningStep + escapeTime;
            // add the mutant
            double[] distibutionGivenThatIJumpedOut = transformToConditional(
                    mutationKernel.getRow(residentType), residentType);
            int mutantType = Random
                    .simulateDiscreteDistribution(distibutionGivenThatIJumpedOut);
            this.population.introduceNewMutant(mutantType);
            // if burning time is not over, run to fixation or until burning
            // time is over
            while (!this.population.isFixated()
                    && burningStep < burningTimePerEstimate) {
                int steps = this.stepFastForward();
                burningStep+=steps;
            }
        }
    }



private CellProcessor[] getProcessors() {
	int headerSize = 5;
	final CellProcessor[] processors = new CellProcessor[headerSize];
	processors[0] = new NotNull();
	processors[1] = new NotNull();
	processors[2] = new NotNull();
	processors[3] = new NotNull();
	processors[4] = new NotNull();
	return processors;
}


private List<Object> currentStateRow() {
	ArrayList<Object> ans = new ArrayList<Object>();
	ans.add(this.timeStep);
	ans.add(population.getTypeOfMutant());
	ans.add(population.getNumberOfMutants());
	ans.add(population.getTypeOfResident());
	ans.add(population.getNumberOfResidents());
	return ans;
}


public void simulateTimeSeries(int numberOfTimeSteps, int reportEveryTimeSteps, Long seed, String fileName)
		throws IOException {
	
	Random.seed(seed);
	//Fast printing
	ICsvListWriter listWriter = null;
	String[] header = {"timeStep", "typeOfMutant", "numberOfMutants" , "typeOfResident", "numberOfResidents"};
	CellProcessor[] processors = this.getProcessors();
	try {
		listWriter = new CsvListWriter(new FileWriter(fileName),
				CsvPreference.STANDARD_PREFERENCE);
		// write the header
		listWriter.writeHeader(header);
		// write the initial zero step content
		listWriter.write(this.currentStateRow(), processors);
		// repeat for as many steps as requested
		for (int i = 0; i < numberOfTimeSteps; i++) {
			// step
			this.step();
			boolean fixated = this.population.isFixated();
			// if time to report or fixated, report
			if (this.timeStep % reportEveryTimeSteps == 0 || fixated) {
				listWriter.write(
						this.currentStateRow(),processors);
			}
			if(fixated){
				//once fixated compute time to escape, add it to timeSteps and introduce new mutant 
				int residentType = this.population.getTypeOfResident();
				double escapeProbability = 1.0 - this.mutationKernel.getEntry(residentType, residentType);
				int escapeTime = Random.simulateGeometricDistribution(escapeProbability);
				timeStep = timeStep + escapeTime;
				double[] distibutionGivenThatIJumpedOut = transformToConditional(
						mutationKernel.getRow(residentType), residentType);
				int mutantType = Random
						.simulateDiscreteDistribution(distibutionGivenThatIJumpedOut);
				this.population.introduceNewMutant(mutantType);
			}
			
			
			
		}

	} finally {
		// close files no matter what
		if (listWriter != null) {
			listWriter.close();
		}
	}

}


public double[] estimateStationaryDistributionSmallMutationFastForward(
            int burningTimePerEstimate, int samplesPerEstimate,
            int numberOfEstimates, Long seed) {
        Random.seed(seed);
        int numberOfTypes = this.population.getNumberOfTypes();
        int populationSize = this.population.getSize();
        long[] countPerStrategy = new long[numberOfTypes];
        for (int estimate = 0; estimate < numberOfEstimates; estimate++) {
            // for every new esimate start at a random type
            int typeOfResident = Random.nextInt(numberOfTypes);
            this.population = new DimorphicPopulation(populationSize,
                    typeOfResident, numberOfTypes);
            this.timeStep = 0;
            // burning time
            this.burnFastForward(burningTimePerEstimate);
            int samples = 0;
            int timeSpent = 1;
            // run to fixation and count
            while (samples < samplesPerEstimate) {
                while (!this.population.isFixated()) {
                    countPerStrategy[this.population.getTypeOfResident()] = countPerStrategy[this.population
                            .getTypeOfResident()]
                            + timeSpent*this.population.getNumberOfResidents();
                    countPerStrategy[this.population.getTypeOfMutant()] = countPerStrategy[this.population
                            .getTypeOfMutant()]
                            + timeSpent*this.population.getNumberOfMutants();
                    timeSpent = this.stepFastForward();
                    samples+=timeSpent;
                }
                if (samples > samplesPerEstimate)
                    break;
                // here we are fixated
                int residentType = this.population.getTypeOfResident();
                double escapeProbability = 1.0 - this.mutationKernel.getEntry(
                        residentType, residentType);
                int escapeTime = Random
                        .simulateGeometricDistribution(escapeProbability);
                samples = samples + escapeTime;
                countPerStrategy[this.population.getTypeOfResident()] = countPerStrategy[this.population
                        .getTypeOfResident()]
                        + escapeTime
                        * this.population.getSize();
                // add the mutant
                double[] distibutionGivenThatIJumpedOut = transformToConditional(
                        mutationKernel.getRow(residentType), residentType);
                int mutantType = Random
                        .simulateDiscreteDistribution(distibutionGivenThatIJumpedOut);
                this.population.introduceNewMutant(mutantType);
            }
        }
        return ArrayUtils.normalize(countPerStrategy);
    }

}
