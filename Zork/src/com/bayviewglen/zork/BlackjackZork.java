package com.bayviewglen.zork;

import java.util.Scanner;

//This class holds the methods required to play Blackjack, made compatible with Zork
public class BlackjackZork {

	private static Scanner keyboard = new Scanner(System.in);

	// starting values
	private static final int DEFAULT_STARTING_WALLET = 500;
	
	private static final int NUM_SUITS = 4;
	private static final int NUM_RANKS = 13;
	// assigning integer values to represent suits
	private static final int DIAMONDS = 0;
	private static final int SPADES = 1;
	private static final int HEARTS = 2;
	private static final int CLUBS = 3; // variable never used because of else statement
	private static final int RANK_OF_ACE = 1; // variable never used because of else statement
	private static final int RANK_OF_JACK = 11;
	private static final int RANK_OF_QUEEN = 12;
	private static final int RANK_OF_KING = 13;
	private static final int LOW_VALUE_OF_ACE = 1;
	private static final int VALUE_OF_FACE_CARDS_AND_TEN = 10;
	private static final int HIGH_VALUE_OF_ACE = 11;
	private static final int LIMIT_FOR_DEALER = 17; // dealer cannot hit if score is 17 or greater
	private static final int BLACKJACK = 21;
	// win conditions
	private static final int LOSS = 0;
	private static final int LOSS_WITH_DOUBLE_DOWN = 1;
	private static final int WIN = 2;
	private static final int WIN_WITH_DOUBLE_DOWN = 3;
	private static final int TIE = 4;
	
	private static final int TARGET_WINS = 5;

	public static int play(String name) {		
		System.out.println("\n-----------------------------------------------------------\n\nHello " + name
				+ "! Let's play some Blackjack!\n\nYou have to win 5 times without losing. Double down gives you 2 wins!");

		System.out.println("\n-----------------------------------------------------------\n");
		int winCondition = playHand();
		return winCondition;
	} // method main

	// if it is a draw, the player doesn't lose or gain anything
	private static int playHand() {
		String playerHand = getCard();
		String dealerHand = getCard();

		boolean handDone = false;
		int doubleDown = 0; // double down has not occurred
		boolean ddAvailable = true;
	
		int playerScore = 0;
		int dealerScore = calculateScore(dealerHand);
		// player
		while (!handDone) {
			playerHand += getCard();
			playerScore = calculateScore(playerHand);
			System.out.println("\n-----------------------------------------------------------\n\nDealer's Hand:\t"
					+ dealerHand + "XX\nDealer's Score:\t" + dealerScore + "\nYour Hand:\t" + playerHand + "\nYour Score:\t" + playerScore);
			if (playerScore == BLACKJACK) {
				return WIN + doubleDown; // player gets 21
			} else if (playerScore > BLACKJACK) {
				return LOSS + doubleDown; // player goes over 21
			}
			if (doubleDown == 0) {
				boolean valid = false;
				while (!valid) {
					if (ddAvailable) {
						System.out.print("\nHit, Stay, or Double Down? (h/s/dd) ");
					} else {
						System.out.print("\nHit, or Stay? (h/s) ");
					}
					String choice = keyboard.nextLine().toLowerCase();
					if (choice.equals("hit") || choice.equals("h")) {
						valid = true;
						ddAvailable = false;
					} else if (choice.equals("stay") || choice.equals("s")) {
						handDone = true;
						valid = true;
					} else if (ddAvailable && (choice.equals("double down") || choice.equals("dd"))) {
						valid = true;
						doubleDown = 1; // adds one to doubleDown, which will affect winCondition in main
					} else {
						System.out.println("Invalid Choice!");
					}
				} // while
			} else {
				handDone = true;
			}
		} // while

		// dealer
		while (dealerScore < LIMIT_FOR_DEALER) {
			dealerHand += getCard();
			dealerScore = calculateScore(dealerHand);
			System.out.println("\n-----------------------------------------------------------\n\nDealer's Hand:\t" + dealerHand
					+ "\nDealer's Score:\t" + dealerScore + "\nYour Hand:\t" + playerHand + "\nYour Score:\t" + playerScore);
			if (dealerScore > BLACKJACK) {
				return WIN + doubleDown; // dealer goes over 21, player not over 21
			}
		} // while
		if (playerScore > dealerScore) {
			return WIN + doubleDown; // player's score is greater, but not over 21
		}
		else if (playerScore == dealerScore) {
			return TIE; // player's score is the same as dealer's score
		}
		return LOSS + doubleDown; // dealer's score is greater, but not over 21
	} // method playHand

	private static String getCard() {
		String card = "";

		int rank = (int) (Math.random() * NUM_RANKS + 1); // randomizes rank
		if (rank >= 2 && rank <= 10) { // card values 2 to 10
			card += "" + rank;
		} else if (rank == RANK_OF_JACK) {
			card += "J";
		} else if (rank == RANK_OF_QUEEN) {
			card += "Q";
		} else if (rank == RANK_OF_KING) {
			card += "K";
		} else {
			card += "A";
		}

		int suit = (int) (Math.random() * NUM_SUITS); // randomizes suit
		if (suit == DIAMONDS) {
			card += "D";
		} else if (suit == SPADES) {
			card += "S";
		} else if (suit == HEARTS) {
			card += "H";
		} else {
			card += "C";
		}

		return card + " ";
	} // method getCard

	private static int calculateScore(String hand) {
		int score = 0;
		int numberOfAces = 0;
		for (int i = 0; i < hand.length() - 1;) {
			if (hand.charAt(i) == 'A') { // ace
				numberOfAces++;
				score += HIGH_VALUE_OF_ACE;
			} else if (hand.charAt(i) == '1' || hand.charAt(i) == 'J' || hand.charAt(i) == 'Q' ||
					hand.charAt(i) == 'K'){ // ten to king
				score += VALUE_OF_FACE_CARDS_AND_TEN;
			} else { // two to nine
				score += Integer.parseInt(hand.substring(i, i + 1));
			}
			
			if (hand.charAt(i + 2) == ' ') { // two character card
				i += 3;
			} else { // three character card
				i += 4;
			}
		} // for
		while (numberOfAces > 0 && score > BLACKJACK) {
			// if the score is greater than 21, subtract 10 until the score is under 21 or there are no more aces left
			score -= (HIGH_VALUE_OF_ACE - LOW_VALUE_OF_ACE);
			numberOfAces --;
		} // while
		return score;
	} // method calculateScore

	private static boolean playAgain() {
		String answer = "";
		System.out.println();
		while (!(answer.equals("y") || answer.equals("yes") || answer.equals("n") || answer.equals("no"))) {
			System.out.print("Would you like to play again? (y/n) ");
			answer = keyboard.nextLine().toLowerCase();
			if (answer.equals("n") || answer.equals("no")) {
				return false;
			} else if (answer.equals("y") || answer.equals("yes")) {
				return true;
			}
		} // while
		return false; // unreachable code
	} // method playAgain
} // class