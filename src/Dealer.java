/**
//this class implements a dealer for a user specified game of cards
//the dealer greets, deals, and controls the flow of the game
*/

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Dealer{
	List<String> games = Arrays.asList("Poker", "poker");
	String game;
	ArrayList<Player> players = new ArrayList<Player>();
	ArrayList<Card> blank = new ArrayList<Card>(3);	
	ArrayList<Card> table = new ArrayList<Card>(5);
	ArrayList<Card> starter = new ArrayList<Card>(3);
	ArrayList<Player> playersIn;
	Deck deck;
	int blinder = 0;
	double pot;
	double tableBet;
	String userName;
	
	//constructor
	public Dealer(String game, int numPlayers, Deck deck, String userName){
		this.game = game;
		this.deck = deck;
		this.userName = userName;
		
		//fills arraylist of players at the table
		Player user = new Player(this.userName, starter);
		this.players.add(user);
		for (int i=1; i<=numPlayers; i++){
			Player a = new Player("Computer "+Integer.toString(i), starter);
			this.players.add(a);
		}
		
		checkValid();
	}
	
	//checks to make sure the dealer knows the game, and there are not too many players
	private void checkValid(){
		if (!games.contains(this.game)){
			System.out.print("The game you entered is invalid. Right now I only know ");
			for (int i = 0; i <games.size(); i+=2)
				System.out.print(""+games.get(i)+"  ");
			System.exit(0);
		}
		
		if (this.players.size() > 8){
			System.out.println("Sorry, you can only have a maximum of 7 opponents");
			System.exit(0);
		}
	}
	
	//deals out two cards to each player
	public void deal(){
		if (this.game.equals("Poker") || this.game.equals("poker")){
			System.out.print("\nDealing...");
			
			for(Player p : players){
				p.addToHand(deck.get(0), deck.get(players.size()));
				deck.remove(players.size());
				deck.remove(0);
			}
		}
	}
	
	//dealer launches the game
	public void greet(){
		System.out.println("\n+--------------------------------------------------------------------"+
				"-------------------------------------------------------+");
		System.out.println("+--------------------------------------------------------------------"+
				"-------------------------------------------------------+");	
		System.out.println("+--------------------------------------------------------------------"+
				"-------------------------------------------------------+");
		System.out.print("\nHi "+this.userName+", I am your "+game+" dealer. There are "+this.players.size()
				+" players at the table. You start with 50 chips. You cannot bet fractional amounts.\n");
	}
	
	//all players with chips start fresh and user has option to play or quit before hand
	public void newHand(){
		recap();
		System.out.println("\n+--------------------------------------------------------------------"+
				"-------------------------------------------------------+\n");
		reset();
		
		if (players.size() == 1){
			System.out.println("\n"+players.get(0).name + " wins!!!!\nGame over. Goodbye.");
			System.exit(0);
		}
		
		Scanner scan = new Scanner(System.in);
		String play = new String();
		System.out.println("Ready for the next hand. Enter to deal. Type 'quit' to cash out.");
		play = scan.nextLine();
		
		if(play.equals("quit")||play.equals("Quit"))
			players.get(0).quit();
		
		else
			playHand();
	}
	
	//burns a card, and flops 3 from the top of the deck
	public void flop (){
		table.clear();
		System.out.println("Flop...");
		deck.remove(0);
		for(int i=0; i<3; i++){
			table.add(deck.get(0));
			deck.remove(0);
		}
		printTable();
	}
	
	//burns a card, and shows the turn from the top of the deck
	public void turn (){
		System.out.println("Turn...");
		deck.remove(0);
		table.add(deck.get(0));
		deck.remove(0);
		printTable();
	}
	
	//burns a card, and shows the river from the top of the deck
	public void river (){
		System.out.println("River...");
		deck.remove(0);
		table.add(deck.get(0));
		deck.remove(0);
		printTable();
	}
	
	//blind of one chip goes to the next player in line, first tablebet of 1.0 is established
	public void blind(int anti){
		Scanner scan = new Scanner(System.in);
		String ok;
		if (anti %(players.size()) == 0){
				System.out.print("Your blind (1 javadollar) --- Type 'ok' to start the betting.\n>>> ");
				ok = scan.nextLine();
				if(!ok.equals("ok"))
					blind(0);
				double bet = players.get(0).bet(1, tableBet);
				pot += bet;
		}
		else{
			System.out.println("Blind is to Computer "+anti%(players.size()));
			double bet = players.get(anti%(players.size())).bet(1, tableBet);
			pot += bet;
		}
		blinder ++;
		tableBet = 1;
	}
	
	//shows cards on the table
	public void printTable(){
		System.out.print("TABLE: [");
		int i = 0;
		while(i<table.size()-1){
			Card c = table.get(i);
			c.printCard();
			System.out.print("    ");
			i++;
		}
		Card c = table.get(i);
		c.printCard();
		System.out.print("]\n");
	}
	
	//searches to see if only one player is left and wins
	private boolean lookForWinner(ArrayList<Player> playersIn){
		if(playersIn.size() == 1)
			return true;
		return false;
	}
	
	//updates the winner's pot
	private void getWinner(ArrayList<Player> playersIn, ArrayList<Card> table){
		Player winner;
		winner = playersIn.get(0);
		winner.winPot(pot);
	}
	
	//creates a new, shuffled deck
	private void refreshDeck(){
		ArrayList<Card> cards = new ArrayList<Card>(52);
		Deck newDeck = new Deck(cards);
		this.deck = newDeck;
	}
	
	//prints out all remaining players in a hand
	private void printRemaining(){
		int s = 0;
		ArrayList<Player> stillIn = playersLeft();
		System.out.print("in: [");
		while (s<stillIn.size()-1){
			System.out.print(stillIn.get(s).name+", ");
			s++;
		}
		System.out.println(stillIn.get(s).name+"]");
	}
	
	//checks to see that all players who are still in have paid the table bet
	private boolean allPaid(ArrayList<Player> oppsLeft){
		for(Player p : oppsLeft){
			if (p.allIn)
				p.paid = tableBet;
			else if (p.paid <tableBet)
				return false;
		}
		return true;
	}
	
	private boolean allInButOne(){
		int count = 0;
		for (Player p : playersLeft()){
			if (p.allIn)
				count ++;
		}
		if (count >= playersLeft().size() - 1)
			return true;
		return false;
	}
	
	//round of betting
	private void round(int startPlayerIndex, int roundNum){
		int index = startPlayerIndex%(players.size());
		int i = 0;
		System.out.println();
		
		//everyone gets at least one turn -- allows for a round of all checks
		while(i<players.size()){
			if (allInButOne())
				break;
			if (index >= players.size() || index < 0)
				index = 0;
			Player p = players.get(index);
			if(p.in){
				if(p.name.equals(this.userName)){
					double bet = p.userTurn(tableBet, pot);
					if (bet >= tableBet)
						tableBet = bet;
					pot += bet;
				}
				else{
					double bet = p.makeTurn(table, tableBet, roundNum);
					if (bet >= tableBet)
						tableBet = bet;
					pot+=bet;
				}
			}
			i++;
			index ++;
		}
		
		//following rounds (if necessary -- if not everyone has paid that is in)
		index = startPlayerIndex%(players.size());
		while(tableBet>0 && !allPaid(playersLeft())){
			if (index >= players.size() || index < 0)
				index = 0;
			
			Player p = players.get(index);
			if(p.in && p.paid < tableBet){
				if(p.name.equals(this.userName)){
					double bet = p.userTurn(tableBet, pot);
					if(bet >= tableBet)
						tableBet = bet;
					pot+=bet;
				}
				else{
					double bet = p.makeTurn(table, tableBet, roundNum);
					if (bet >= tableBet)
						tableBet = bet;
					pot+=bet;
				}
			}
			index++;

			if (allPaid(playersLeft()))
				break;
		}
		System.out.print("ROUND FINISHED -- ");
		System.out.print("POT: "+pot+" -- Players ");
		printRemaining();
		System.out.println();
		tableBet = 0;
		clearPay();
	}
	
	//puts all players with a chipCount > 0 back into play for a new hand
	private void bringPlayersBack(){
		ArrayList<Player> out = new ArrayList<Player>();
		for (Player p : players){
			if(p.chipCount <= 0){
				System.out.println(p.name+ " has been eliminated and left the table.");
				out.add(p);
				p.in = false;
			}
			else
				p.in = true;
		}
		for (Player p : out){
			if (p.name.equals(this.userName))
				p.quit();
			players.remove(p);
		}
	}
	
	//resets the amount that each player has paid to the pot
	private void clearPay(){
		for (Player p : players)
			p.paid = 0;
	}
	
	private void clearAllIn(){
		for (Player p : players)
			p.allIn = false;
	}
	
	//makes sure that at the start of a hand, no player is continuously bluffing
	private void clearBluffs(){
		for (Player p : players)
			p.bluffing = false;
	}
	
	//clears the best cards for each player
	private void clearBest(){
		for (Player p : players)
			p.high = 0;
	}
	
	//resets all players and cards for a new hand
	private void reset(){
		playersIn = players;
		refreshDeck();
		this.deck = this.deck.shuffle();
		table.clear();
		pot = 0;
		bringPlayersBack();
		clearPay();
		clearBest();
		clearBluffs();
		clearAllIn();
	}
	
	//returns an arrayList of the remaining players in a hand
	private ArrayList<Player> playersLeft(){
		ArrayList<Player> rem = new ArrayList<Player>();
		for(Player p : players){
			if(p.in || p.allIn)
				rem.add(p);
		}
		return rem;
	}
	
	//compares best hand for each player left -- uses BestHand class
	private void compareHands(){
		printTable();
		ArrayList<Player> playerSet = playersLeft();
		double max = 0;
		int count = 0;
		ArrayList<Player> tied = new ArrayList<Player>();
		BestHand pBest;
		
		for(Player p : playerSet){
			if (p.name.equals(this.userName))
				System.out.print("\n\nYour Hand: [" +p.hand.get(0).cardString()+", "
					+p.hand.get(1).cardString()+"]");
			else
				p.showCompHand();
			System.out.print(" ===> ");
			pBest = new BestHand(p.hand, table);
			p.high = pBest.findBest();
			if(p.high >= max)
				max = p.high;
		}
		
		//checks to see if any players are tied
		for(Player p : playerSet){
			if(p.high == max){
				tied.add(p);
				count++;
			}
		}
		
		if(tied.size()>1)
			tieBreak(tied);
		else
			tied.get(0).winPot(pot);
	}
	
	//breaks a tie if two player have an even best hand -- splits the pot if necessary
	private void tieBreak(ArrayList<Player> tied){
		System.out.println("Tie breaker");
		ArrayList<Player> doubleTie = new ArrayList<Player>();
		double max = 0;
		for(Player p : tied){
			if (p.highCardInHand()>max)
				max = p.highCardInHand();
		}
		
		for (Player p : tied){
			if (p.highCardInHand() == max)
				doubleTie.add(p);			
		}
		
		double eachWin = pot/doubleTie.size();
		for (Player p : doubleTie)
			p.winPot(eachWin);
	}
	
	//goes through an entire hand, looking for winner as it goes
	private void playHand(){
		blind(blinder%players.size());
		int index = (blinder)%(players.size());
		deal();
		
		//opening bets
		round(index, 1);
		ArrayList<Player> rem = playersLeft();
		if(lookForWinner(rem)){
			getWinner(rem, null);
			return;
		}
		
		//flop and round of betting
		flop();
		if(index - 1 < 0)
			index = 1;
		round(blinder%players.size() - 1, 2);
		rem = playersLeft();
		if(lookForWinner(rem)){
			getWinner(rem, table);
			return;
		}

		//turn and round of betting
		turn();
		if(index - 1 < 0)
			index = 1;
		round(blinder%players.size()-1, 3);
		
		rem = playersLeft();
		if(lookForWinner(rem)){
			getWinner(rem, table);
			return;
		}
		
		//river and last round of betting
		river();
		if(index - 1 < 0)
			index = 1;
		round(blinder%players.size()-1, 4);
		System.out.println();
		compareHands();
	}
	
	//prints out total chip count for each player
	public void recap(){
		System.out.println("\nCHIP COUNT\n----------");
		for(Player p : players){
			System.out.println(p.name+": "+p.chipCount);
		}
	}
}