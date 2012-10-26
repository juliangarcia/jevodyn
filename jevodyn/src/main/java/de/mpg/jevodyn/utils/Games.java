package de.mpg.jevodyn.utils;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;

/***
 * Games provides implementations of well-known matrix games.
 * @author garcia
 *
 */
public class Games {
	
	/**
	 * Prisoners dilemma with custom values. 
	 * @param reward
	 * @param sucker
	 * @param temptation
	 * @param punishment
	 * @return RealMatrix
	 */

	public static RealMatrix prionersDilemma(double reward, double sucker, double temptation, double punishment){
		double[][] game = {{reward, sucker},{temptation, punishment}}; 
		return new Array2DRowRealMatrix(game);
	}
	
	
	/***
	 * Standard prisoners dilemma reward 3, sucker 0, temptation 4, punishment 1.
	 * @return RealMatrix
	 */
	public static RealMatrix prionersDilemma(){
		return prionersDilemma(3.0, 0.0, 4.0, 1.0);
	}
	
	/***
	 * PD with equal gains from switching
	 * @param benefit
	 * @param cost
	 * @return RealMatrix
	 */
	public static RealMatrix prionersDilemmaEqualGainsFromSwitching(double benefit, double cost){
		return prionersDilemma(benefit-cost,-cost,benefit,0.0);
	}
	
	
	/**
	 * PD with equal gains from switching, b = 2, c=1.
	 * @return RealMatrix
	 */
	public static RealMatrix prionersDilemmaEqualGainsFromSwitching(){
		return prionersDilemmaEqualGainsFromSwitching(2.0, 1.0);
	}
	
	/**
	 * a rock paper scissors game
	 * @param win
	 * @param lose
	 * @param tie
	 * @return RealMatrix
	 */
	public static RealMatrix rockPaperSccisors(double win,double lose,double tie){
		double[][] game = {{tie,lose, win},{win,tie, lose},{lose,win, tie}}; 
		return new Array2DRowRealMatrix(game);
	}
	
	/**
	 * Standard rock paper scissors game, win 1, lose -1, tie 0.
	 * @return RealMatrix
	 */
	public static RealMatrix rockPaperSccisors(){
		return rockPaperSccisors(1.0, -1.0, 0.0);
	}
	    

	/**
	 * Hawk dove game
	 * @param resourceValue
	 * @param costOfFighting
	 * @return RealMatrix
	 */
	public static RealMatrix hawkDoveGame(double resourceValue, double costOfFighting){
		double[][] game = {{(resourceValue-costOfFighting)/2.0,resourceValue},{0.0,resourceValue}};
		return new Array2DRowRealMatrix(game);
	}
	
	/***
	 * Standard haw dove, resourse value 2, cost of fighting 1. 
	 * @return RealMatrix
	 */
	public static RealMatrix hawkDoveGame(){
		return hawkDoveGame(2.0, 1.0);
	}
	
	/**
	 * Stag hunt game
	 * @param stag
	 * @param hare
	 * @return RealMatrix
	 */
	public static RealMatrix stagHunt(double stag, double hare){
		double[][] game = {{stag,0.0},{hare,hare}};
		return new Array2DRowRealMatrix(game);
	}
	
	/**
	 * Stag hunt game, stag value 2, hare value 1.
	 * @return RealMatrix
	 */
	public static RealMatrix stagHunt(){
		return stagHunt(2.0, 1.0);
	}
	
	
	/***
	 * Simple repeated game. 
	 * @param reward
	 * @param sucker
	 * @param temptation
	 * @param punishment
	 * @param continuationProbability
	 * @return RealMatrix
	 */
	public static RealMatrix allcTftAlld(double reward,double sucker,double temptation,double punishment,double continuationProbability){
		double[][] game = {{reward,reward,sucker},{reward,reward, sucker*(1.0-continuationProbability)+punishment*continuationProbability},{temptation,temptation*(1.0-continuationProbability)+punishment*continuationProbability, punishment}};
		return new Array2DRowRealMatrix(game);
	}
	
	
	/***
	 * R,T,P,S as in the standard PD, continuation probability = 0.95
	 * @return RealMatrix
	 */
	public static RealMatrix allcTftAlld(){
		return allcTftAlld(3.0, 0.0, 4.0, 1.0, 0.95);
	}
	
	    

	/***
	 * Repeated game with standard stage game with equal gains from switching. 
	 * @param benefit
	 * @param cost
	 * @param continuationProbability
	 * @return RealMatrix
	 */
	public static RealMatrix allcTftAlldEqualGains(double benefit, double cost,double  continuationProbability){
		double reward=benefit-cost;
		double sucker=-cost;
		double temptation=benefit;
		double punishment=0.0;
		double game[][] = {{reward,reward,sucker},{reward,reward, sucker*(1.0-continuationProbability)+punishment*continuationProbability},{temptation,temptation*(1.0-continuationProbability)+punishment*continuationProbability, punishment}};
		return new Array2DRowRealMatrix(game);
	}
	
	
	/***
	 * Repeated game with standard stage game with default values for equal gains form switching, continuation probability = 0.95
	 * @return RealMatrix
	 */
	public static RealMatrix allcTftAlldEqualGains(){
		return allcTftAlldEqualGains(2.0, 1.0, 0.95);
	}
	    

	
	/**
	 * A two player symmetric game where everybody gets 1.0. 
	 * @param numberOfStrategies
	 * @return RealMatrix
	 */
	public static RealMatrix neutralGame(int numberOfStrategies){
		double[][] rawMatrix = new double[numberOfStrategies][numberOfStrategies];
		for (int i = 0; i < numberOfStrategies; i++) {
			for (int j = 0; j < numberOfStrategies; j++) {
				rawMatrix[i][j] = 1.0;
			}
		}
		return new Array2DRowRealMatrix(rawMatrix);
	}
	
	/***
	 * A 2x2 symmetric game, with matrix [[a,b],[c,d]]
	 * @param a
	 * @param b
	 * @param c
	 * @param d
	 * @return RealMatrix
	 */
	public static RealMatrix two_times_two(double a,double b,double c,double d){
		double game[][]= {{a,b},{c,d}};
		return new Array2DRowRealMatrix(game);
	}
	    

	/**
	 * Constant selection game, player one gets payoffA and player 2 gets payoffB.
	 * @param payoffA
	 * @param payoffB
	 * @return RealMatrix
	 */
	public static RealMatrix constantSelection(double payoffA,double payoffB){
		double game[][] = {{payoffA,payoffA},{payoffB,payoffB}};
		return new Array2DRowRealMatrix(game);
	}
	    
}
