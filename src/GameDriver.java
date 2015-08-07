/**
//a user plays a personalized game of cards against a computer intelligence
*/

import java.util.*;
import java.util.concurrent.TimeUnit;

public class GameDriver {

	public static void main(String[] args) {
		ArrayList<Card> cards = new ArrayList<Card>(52);
		int num;
		String game;
		String name;

		Scanner scan = new Scanner(System.in);
		Scanner scan2 = new Scanner(System.in);
		Scanner scan3 = new Scanner(System.in);

		System.out.print("Welcome to cards. Enter the game you would like to play. I know poker.\n>>> ");
		game = scan.nextLine();			
		System.out.print("How many opponents would you like?\n>>> ");
		num = scan2.nextInt();
		System.out.print("Enter your name\n>>> ");
		name = scan3.nextLine();
		System.out.println("\nLaunching your game...");

		try{
			TimeUnit.MILLISECONDS.sleep(500);
		} catch(InterruptedException e){
			System.out.println();
		}

		Deck deck = new Deck(cards);
		Dealer dealer = new Dealer(game, num, deck, name);
		dealer.greet();

		//play until user runs out of chips or quits
		for(;;)
			dealer.newHand();
	}
}