/**
//this class creates a player type, containing all information necessary for a player to 
//analyze his own hand and make conclusions about his opponents hand to determine
//the best moves to make while playing
*/

import java.util.*;

public class Player {
	ArrayList<Card> hand;
	String name;
	Double chipCount;
	boolean in;
	double paid;
	double high;
	boolean bluffing;
	boolean allIn;
	
	//constructor
	public Player (String name, ArrayList<Card> hand){
		if(name.contains("Computer 0"))
			this.name = "You";
		else	
			this.name = name;
		
		this.hand = hand;
		this.chipCount = 50.0;
		this.in = true;
		this.paid = 0.0;
		this.high = 0;
		this.bluffing = false;
		this.allIn = false;
	}
	
	//returns value of the highest card in the hand
	public int highCardInHand(){
		int a = this.hand.get(0).val;
		int b = this.hand.get(1).val;
		
		if (a == 1 || b == 1)
			return 14;
		else if(a > b)
			return a;
		else
			return b;
	}
	
	//chooses if it will bluff bet, small bet, or big bet
	private double getBetAmount(ArrayList<Card> table, double tableBet, int roundNum){
		if (roundNum == 1)
			return getRoundOneBet(table, tableBet);
		else
			return getRoundTwoBet(table, tableBet, roundNum);
	}
	
	//choose to play or pass in the first round
	private double getRoundOneBet(ArrayList<Card> myTable, double myTableBet){
		BestHand best = new BestHand(hand, myTable);
		if(best.pair()!=0){
			Random r = new Random();
			double val = r.nextInt(3);
			if (val%2 ==0)
				return 2;
			else return 1;
		}
		else if((highCardInHand()>=9 && this.chipCount > 10) || this.highCardInHand() >= 12){
			Random r = new Random(15);
			double val = r.nextInt(7);
			if (val == 3)
				return 2;
			return 1;
		}
		else{
			Random r = new Random();
			double amt = r.nextInt(2);
			if (amt == 1 && this.chipCount > 10){
				double val = r.nextInt(20);
				if (val == 3)
					return 2;
				return amt;
			}
			else
				return 0;
		}
	}
	
	//get amount to bet after the first round
	private double getRoundTwoBet(ArrayList<Card> myTable, double myTableBet, int roundNum){
		BestHand best = new BestHand(hand, myTable);
		double betAmount = best.analyzeHand(roundNum);
		if(betAmount > 0){
			if (checkForCheck(myTableBet)){
				if(betAmount > 60){
					Random b = new Random();
					int v = b.nextInt(5);
					if (v == 3)
						return 0;
				}
			}
			int a = (int) Math.round(betAmount*this.chipCount);
			return a;
		}
		else{
			if(this.bluffing == true){
				Random r = new Random();
				double amt = r.nextInt(2)-1;
				return amt+myTableBet;
			}
			else
				return 0;
		}
	}
	
	private boolean checkForCheck(double tableBet){
		if (tableBet == 0)
			return true;
		return false;
	}
	//takes player out of the hand
	public void fold(){
		this.in = false;
		if (this.allIn)
			System.out.println(this.name+" remains all in");
		else
			System.out.println(this.name+" folded");
	}
	
	//takes the pot and adds to chips count
	public void winPot(double pot){
		this.chipCount += pot;
		System.out.println("\nThe pot ("+pot+") goes to "+this.name+"!");
	}
	
	//sets up teh user's hand
	public void addToHand(Card card1, Card card2){
		ArrayList<Card> myHand = new ArrayList<Card>();
		myHand.add(card1);
		myHand.add(card2);
		this.hand = myHand;
	}
	
	public void addCard(Card card){
		ArrayList<Card> myHand = new ArrayList<Card>();
		myHand.add(card);
		this.hand = myHand;
	}
	
	//toString for user's hand
	public void showUserHand(){
		if (!this.name.contains("Computer"))
			System.out.print(this.name+"'s Hand: [" +this.hand.get(0).cardString()+", "
				+this.hand.get(1).cardString()+"]");
		
	}
	
	//toString for computer hand
	public void showCompHand(){
		if (this.name.contains("Computer"))
			System.out.print(this.name+"'s Hand: [" +this.hand.get(0).cardString()+", "
				+this.hand.get(1).cardString()+"]");
	}
	
	//bet updates how much the player has paid
	public double bet(double amount, double tableBet){
		if(amount == 0)
			System.out.println(this.name+" checked");
		else if (amount == tableBet)
			System.out.println(this.name+" called");
		else
			System.out.println(this.name+" bet " +amount);
		this.chipCount -= amount;
		if (this.chipCount == 0){
			System.out.println(this.name+" is all in!");
			this.allIn = true;
		}
		this.paid += amount;
		return amount;
	}
	
	//user selects move from short list of options
	public double userTurn(double recentBet, double pot){
		if(this.chipCount == 0)
			return 0;

		Scanner scan = new Scanner(System.in);
		int move;
		System.out.println();
		this.showUserHand();
		
		if(recentBet == 0)
			System.out.print(" --- (ChipCount: "+this.chipCount+")" +
					"\n1. Check\n2. Bet\n3. Fold\n>>> ");
		
		else{
			if(recentBet - this.paid == 0)
				System.out.print(" --- (ChipCount: "+this.chipCount+")" +
				"\n1. Check\n2. Bet\n3. Fold\n>>> ");
			else	
				System.out.print(" --- (ChipCount: "+this.chipCount+") --- "+(recentBet-this.paid)+
						" to stay"+" \n1. Call\n2. Raise\n3. Fold\n>>> ");
		}
		move = scan.nextInt();
		
		switch (move){
			case 1:
				double bet;
				if (recentBet-this.paid >= this.chipCount){
					Scanner scana = new Scanner(System.in);
					String in;
					System.out.print("GO ALL IN? (y or n)\n>>> ");
						in = scana.nextLine();
						if (in.equals("y")||in.equals("Y"))
							bet = bet(this.chipCount,recentBet);
						else
							return userTurn(recentBet, pot);
				}
				else
					bet = bet(recentBet-this.paid,recentBet);
				return bet;
			case 2:
				Scanner scan2 = new Scanner(System.in);
				double raise;
				System.out.print("Add how much?\n>>> ");
				raise = scan2.nextInt()/1;
				if(this.chipCount - ((recentBet+raise)-this.paid) > 0)
					bet((recentBet+raise)-this.paid, recentBet);
				else if (this.chipCount - ((recentBet+raise)-this.paid) == 0){
					Scanner scana = new Scanner(System.in);
					String in;
					System.out.print("GO ALL IN? (y or n)\n>>> ");
						in = scana.nextLine();
						if (in.equals("y")||in.equals("Y"))
							bet = bet(this.chipCount,recentBet);
						else
							return userTurn(recentBet, pot);
				}
				else{
					System.out.println("\nYOU ONLY HAVE "+this.chipCount+" CHIPS. TRY AGAIN");
					return userTurn(recentBet, pot);
				}
				this.paid = recentBet+raise;
				return recentBet+raise;
			case 3:
				fold();
				return 0;
			default:
				return userTurn(recentBet, pot);
		}
	}
	
	//analyzes hand, and chooses whether to call or raise, check, fold -- or play the hand bluffing
	public double makeTurn(ArrayList<Card> table, double tableBet, int roundNum){
		if (this.chipCount == 0)
			return 0;
		
		Random bluffer = new Random();
		int b = bluffer.nextInt(9);
		if (b == 3)
			this.bluffing = true;

		double bet = 0;
		double choice = getBetAmount(table, tableBet, roundNum);
		
		if (choice >= this.chipCount){
			choice = this.chipCount;
		}
		//computer's choice is more than tableBet or close to it
		if (choice >= tableBet){
			if (choice == tableBet || choice - tableBet < 1)
				bet = bet(tableBet, tableBet);
			
			else
				bet = bet(choice, tableBet);
		}
		
		//tableBet is too steep -- randomly call if valid
		else if (choice < tableBet && choice !=0){
			if (this.chipCount - choice <= 0 || this.chipCount - tableBet <= 0){
				if (detectBluff(choice, tableBet)){
					Random r = new Random();
					int c = r.nextInt(4);
					if (c == 1)
						bet = bet(this.chipCount, tableBet);
				}
				else if (this.high > 60 )
					bet = bet(this.chipCount, tableBet);
				else 
					fold();
			}
			else if (tableBet - choice < 3){
				Random a = new Random();
				double play = a.nextInt(2);
				if(play==1)
					bet = bet(tableBet, tableBet);
				
				//looks to see if opponent is likely bluffing with overly large bet
				else if(detectBluff(choice, tableBet)){
						Random r = new Random();
						double val = r.nextInt(4);
						if(val == 2)
							bet = bet(tableBet+val, tableBet);
						else if (val == 3)
							bet = bet(tableBet, tableBet);
						else
							fold();
				}
				else{
					fold();
					return 0;
				}
			}
			else{
				fold();
				return 0;
			}
		}

		else{
			if (tableBet == 0)
				bet = bet(0, tableBet);
			else
				fold();
		}
		return bet;
	}
	
	//looks to see if other player made unusually high bet
	private boolean detectBluff(double playerChoice, double tableBet){
		if(playerChoice >=3){
			if(tableBet - playerChoice >=4)
				return true;
		}
		else if (playerChoice >= 2 &&tableBet - playerChoice >= 3){
				Random r = new Random();
				double val = r.nextInt(3);
				if(val != 2)
					return true;
		}
		else if (tableBet - playerChoice >= 10){
			Random r = new Random();
			double val = r.nextInt(5);
			if(val != 2)
				return true;
		}
		return false;
	}
	
	//allows user to exit the game and prints out final stats
	public void quit(){
		System.out.println("\n+--------------------------------------------------------------------"+
					"-------------------------------------------------------+");
		System.out.println("+--------------------------------------------------------------------"+
					"-------------------------------------------------------+");
		System.out.println("+--------------------------------------------------------------------"+
					"-------------------------------------------------------+");
		System.out.print("\nThanks for playing. You leave the table with "+this.chipCount+" javadollars. ");
		if(this.chipCount > 50)
			System.out.println("Your winnings are "+(this.chipCount-50)+" javadollars! Nice Job.");
		else if (this.chipCount < 50)
			System.out.println("You lost "+(50-this.chipCount)+" javadollars. Better luck next time.");
		System.out.println("Goodbye.");
		System.exit(0);
	}
}