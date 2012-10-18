package de.mpg.jevodyn.utils;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;

/***
 * Games provides implementations of famous games, as a real matrix.
 * @author garcia
 *
 */
public class Games {

	public static RealMatrix prionersDilemma(double reward, double sucker, double temptation, double punishment){
		double[][] game = {{reward, sucker},{temptation, punishment}}; 
		return new Array2DRowRealMatrix(game);
	}
	
	public static RealMatrix prionersDilemma(){
		return prionersDilemma(3.0, 0.0, 4.0, 1.0);
	}
	
	public static RealMatrix prionersDilemmaEqualGainsFromSwitching(double benefit, double cost){
		return prionersDilemma(benefit-cost,-cost,benefit,0.0);
	}
	
	public static RealMatrix prionersDilemmaEqualGainsFromSwitching(){
		return prionersDilemmaEqualGainsFromSwitching(2.0, 1.0);
	}
	
	public static RealMatrix rockPaperSccisors(double win,double lose,double tie){
		double[][] game = {{tie,lose, win},{win,tie, lose},{lose,win, tie}}; 
		return new Array2DRowRealMatrix(game);
	}
	
	public static RealMatrix rockPaperSccisors(){
		return rockPaperSccisors(1.0, -1.0, 0.0);
	}
	    

	public static RealMatrix hawkDoveGame(double resourceValue, double costOfFighting){
		double[][] game = {{(resourceValue-costOfFighting)/2.0,resourceValue},{0.0,resourceValue}};
		return new Array2DRowRealMatrix(game);
	}
	
	public static RealMatrix hawkDoveGame(){
		return hawkDoveGame(2.0, 1.0);
	}
	
	public static RealMatrix stagHunt(double stag, double hare){
		double[][] game = {{stag,0.0},{hare,hare}};
		return new Array2DRowRealMatrix(game);
	}
	
	public static RealMatrix stagHunt(){
		return stagHunt(2.0, 1.0);
	}
	
	public static RealMatrix allcTftAlld(double reward,double sucker,double temptation,double punishment,double continuationProbability){
		double[][] game = {{reward,reward,sucker},{reward,reward, sucker*(1.0-continuationProbability)+punishment*continuationProbability},{temptation,temptation*(1.0-continuationProbability)+punishment*continuationProbability, punishment}};
		return new Array2DRowRealMatrix(game);
	}
	
	public static RealMatrix allcTftAlld(){
		return allcTftAlld(3.0, 0.0, 4.0, 1.0, 0.95);
	}
	
	    

	public static RealMatrix allcTftAlldEqualGains(double benefit, double cost,double  continuationProbability){
		double reward=benefit-cost;
		double sucker=-cost;
		double temptation=benefit;
		double punishment=0.0;
		double game[][] = {{reward,reward,sucker},{reward,reward, sucker*(1.0-continuationProbability)+punishment*continuationProbability},{temptation,temptation*(1.0-continuationProbability)+punishment*continuationProbability, punishment}};
		return new Array2DRowRealMatrix(game);
	}
	
	
	public static RealMatrix allcTftAlldEqualGains(){
		return allcTftAlldEqualGains(2.0, 1.0, 0.95);
	}
	    

	public static RealMatrix neutralGame(int numberOfStrategies){
		double[][] rawMatrix = new double[numberOfStrategies][numberOfStrategies];
		for (int i = 0; i < numberOfStrategies; i++) {
			for (int j = 0; j < numberOfStrategies; j++) {
				rawMatrix[i][j] = 1.0;
			}
		}
		return new Array2DRowRealMatrix(rawMatrix);
	}
	
	public static RealMatrix two_times_two(double a,double b,double c,double d){
		double game[][]= {{a,b},{c,d}};
		return new Array2DRowRealMatrix(game);
	}
	    

	public static RealMatrix constantSelection(double payoffA,double payoffB){
		double game[][] = {{payoffA,payoffA},{payoffB,payoffB}};
		return new Array2DRowRealMatrix(game);
	}
	    
}
