/**
//this class makes all analyses and comparisons among a player's hand to 
//determine the correct bet and method of betting
*/

import java.util.*;

public class BestHand {
	ArrayList<Card> cards = new ArrayList<Card>(52);
	Deck deck = new Deck(cards);
	ArrayList<Card> meta = new ArrayList<Card>();
	
	//creates an arrayList, "meta", of the player's hand and the cards on the table to analyze
	public BestHand(ArrayList<Card> hand, ArrayList<Card> table){
		this.meta.add(hand.get(0));
		this.meta.add(hand.get(1));
		for(Card c : table)
			meta.add(c);
	}

	//checks for royal flush
	private boolean royalFlush(){
		int high = 0;
		Card next;
		for (Card c : meta){
			ArrayList<Card> rem = meta;
			if(c.val == 13)
				next = new Card(1, c.suit);
			else
				next = new Card(c.val+1, c.suit); 
			int count = 0;
			while(rem.contains(next)){
				high = next.val;
				if (high == 13)
					next = new Card(1, next.suit);
				else
					next = new Card(next.val+1, next.suit);
				count ++;
				if(count == 4&&high == 1)
					return true;
			}

		}
		return false;
	}
	
	//checks for straight flush and returns highest card in straight if true
	private int straightFlush(){
		int high = 0;
		Card next;
		for (Card c : meta){
			ArrayList<Card> rem = meta;
			if(c.val == 13)
				next = new Card(1, c.suit);
			else
				next = new Card(c.val+1, c.suit); 
			int count = 0;
			while(rem.contains(next)){
				high = next.val;
				if (high == 13)
					next = new Card(1, next.suit);
				else
					next = new Card(next.val+1, next.suit);
				count ++;
				if(count == 4){
					if(high == 1)
						return 14;
					else if (high > 4)
						return high;
				}
			}
		}
		return 0;
	}
	
	//checks for four of a kind and returns card value if true
	private int fourOfAKind(){
		int card = 0;
		for (Card c: meta){
			int count = 0;
			for (Card d: meta){
				if (d.val == c.val){
					count ++;
					if (count == 4){
						if(c.val == 1)
							return 14;
						return c.val;
					}
				}
			}
		}	
		return card;
	}
	
	//checks for fullHouse
	private int fullHouse(){
		if(threeOfAKind()!=0 && twoPair()!=0){
			if (threeOfAKind() > twoPair())
				return threeOfAKind();
			else
				return twoPair();
		}
		return 0;
	}
	
	//checks for flush
	private int flush(){
		for (Card c : meta){
			int count=0;
			int high=0;
			for(Card d : meta){
				if(d.suit == c.suit){
					count++;
					if (c.val == 1 || (c.val > high && high!=1))
						high = c.val;
					if (count==5){
						if (high == 1)
							return 14;
						return high;
					}
				}
			}
		}		
		return 0;
	}
	
	
	//checks for a straight and returns highest card in straight if true
	private int straight(){
		int high = 0;
		ArrayList<Integer> values = new ArrayList<Integer>();
		int next;
		for(Card a : meta)
			values.add(a.val);
			
		for (Card c : meta){
			if(c.val == 13)
				next = 1;
			else
				next = c.val+1; 
			int count = 0;
			while(values.contains(next)){
				high = next;
				if (high == 13)
					next = 1;
				else
					next = next+1;
				count ++;
				if(count == 4){
					if(high == 1)
						return 14;
					else if (high > 4)
						return high;
				}
			}
		}
		return 0;
	}
	
	//checks for 3 of a kind and returns the card's value if true
	private int threeOfAKind(){
		int card = 0;
		for (Card c: meta){
			int count = 0;
			for (Card d: meta){
				if (d.val == c.val){
					count ++;
					if (count == 3){
						if(c.val==1)
							return 14;
						return c.val;
					}
				}
			}
		}	
		return card;
	}
	
	//checks for two pairs and returns the value of the higher pair if true
	private int twoPair(){
		int max = 0;
		int pc = 0;
		for(Card c : meta){
			int count = 0;
			for(Card d : meta){
				if(d.val == c.val){
					count ++;
					if (count >= 2){
						pc++;
						count = 0;
						if(c.val > max && max!=1||c.val == 1)
							max = c.val;
					}
				}
			}
		}
		if(pc >= 4){
			if(max == 1)
				return 14;
			else
				return max;
		}
		return 0;
	}
	
	
	//checks for a pair and returns the value if true
	public int pair(){
		int max = 0;
		for(Card c : meta){
			int count = 0;
			for(Card d : meta){
				if(d.val == c.val){
					count ++;
					if (count % 2 == 0 && c.val > max)
						max = c.val;
				}
			}
		}
		if (max == 1)
			return 14;
		return max;
	}
	
	//returns the highest Card in the user's hand
	private int highCard(){
		int a = meta.get(0).val;
		int b = meta.get(1).val;
		
		if (a == 1 || b == 1)
			return 14;
		else if(a > b)
			return a;
		else
			return b;
	}
	
	//checks in anticipation of a royal flush -- only missing one card
	private int almostRoyal(){
		int high = 0;
		Card next;
		for (Card c : meta){
			ArrayList<Card> rem = meta;
			if(c.val == 13)
				next = new Card(1, c.suit);
			else
				next = new Card(c.val+1, c.suit); 
			int count = 0;
			while(rem.contains(next)){
				high = next.val;
				if (high == 13)
					next = new Card(1, next.suit);
				else
					next = new Card(next.val+1, next.suit);
				count ++;
				if(count == 3&&high == 1)
					return count;
			}

		}
		return 0;
	}
	
	//checks in anticipation of a straight flush -- only missing one card
	private int almostStraightFlush(){
		int high = 0;
		Card next;
		for (Card c : meta){
			ArrayList<Card> rem = meta;
			if(c.val == 13)
				next = new Card(1, c.suit);
			else
				next = new Card(c.val+1, c.suit); 
			int count = 0;
			while(rem.contains(next)){
				high = next.val;
				if (high == 13)
					next = new Card(1, next.suit);
				else
					next = new Card(next.val+1, next.suit);
				count ++;
				if(count == 3){
					return count;
				}
			}
		}
		return 0;
	}
	
	//checks in anticipation of a flush -- only missing one card
	private boolean almostFlush(){
		for (Card c : meta){
			int count=0;
			for(Card d : meta){
				if(d.suit == c.suit){
					count++;
					if (count==4)
						return true;
				}
			}
		}		
		return false;
	}
	
	//checks in anticipation of a straight -- only missing one card
	private boolean almostStraight(){
		int high = 0;
		ArrayList<Integer> values = new ArrayList<Integer>();
		int next;
		for(Card a : meta)
			values.add(a.val);
			
		for (Card c : meta){
			if(c.val == 13)
				next = 1;
			else
				next = c.val+1; 
			int count = 0;
			while(values.contains(next)){
				high = next;
				if (high == 13)
					next = 1;
				else
					next = next+1;
				count ++;
				if(count == 3){
					return true;
				}
			}
		}
		return false;
	}
	
	
	//checks for all hands in order of hierarchy and returns a value assigned to the best hand it finds
	public double findBest(){
		if (royalFlush())
			return 180;
		else if (straightFlush()!=0){
			String name = transformNames(straightFlush());
			System.out.println("Straight Flush "+name+" High");
			return 160+straightFlush();
		}
		else if (fourOfAKind()!=0){
			String name = transformNames(fourOfAKind());
			System.out.println("Four "+name+"'s");
			return 140+fourOfAKind();
		}
		else if (fullHouse()!=0){
			String name = transformNames(fullHouse());
			System.out.println("Full House -- "+name+" High");
			return 120+fullHouse();
		}
		else if (flush()!=0){
			String name = transformNames(flush());
			System.out.println("Flush -- "+name+" High");
			return 100+flush();
		}
		else if (straight()!=0){
			String name = transformNames(straight());
			System.out.println("Straight -- "+name+" High");
			return 80+straight();
		}
		else if (threeOfAKind()!=0){
			String name = transformNames(threeOfAKind());
			System.out.println("Three "+name+"'s");
			return 60+threeOfAKind();
		}
		else if (twoPair() != 0){
			String name = transformNames(twoPair());
			System.out.println("Two Pair -- High "+ name+"'s");
			return 40+twoPair();
		}
		else if (pair ()!= 0){
			String name = transformNames(pair());
			System.out.println("Pair of "+name+"'s");
			return 15+pair();
		}
		else{
			String name = transformNames(highCard());
			System.out.println(name+" High");
			return highCard();
		}
	}
	
	//toString for card values
	private String transformNames(int value){
		String name;
		if (value == 11)
			name = "Jack";
		else if (value == 12)
			name = "Queen";
		else if (value == 13)
			name = "King";
		else if (value == 14)
			name = "Ace";
		else
			name = Integer.toString(value);
		return name;
	}
	
	//checks "meta" to determine an amount to bet
	public double analyzeHand(int roundNum){
		if(royalFlush())
			return 0.9;
		else if(straightFlush()!=0){
			double chg = bigOrSmall(straightFlush());
			return 0.8+chg;			
		}
		else if(fourOfAKind()!=0){
			double chg = bigOrSmall(fourOfAKind());
			return 0.5+chg;
		}
		else if(fullHouse()!=0)
			return 0.2;
		else if(flush()!=0)
			return 0.15;
		else if(straight()!=0){
			double chg = bigOrSmall(straight());
			return 0.13+chg;			
		}
		else if(threeOfAKind()!=0){
			double chg = bigOrSmall(threeOfAKind());
			return 0.08+chg;
		}
		else if(twoPair()!=0){
			double chg = bigOrSmall(twoPair());
			return 0.06+chg;
		}
		else if (almostRoyal()!=0 && roundNum < 4){
			return 0.01*almostRoyal();
		}
		else if (almostStraightFlush()!=0 && roundNum < 4){
			return 0.01*almostStraightFlush();
		}
		else if (almostFlush() && roundNum < 4){
			return 0.03;
		}
		else if (almostStraight() && roundNum < 4){
			return 0.025;
		}
		else if(pair()!=0){
			double chg = bigOrSmall(pair());
			return 0.04+chg;
		}
		else
			return 0;
	}

	//randomly decides to increment the bet by a small or large amount
	private double bigOrSmall(int func){
		Random r = new Random();
		if (func > 9){
			int num = r.nextInt(2);
			return 0.01*num;
		}
		else{
			int num = r.nextInt(2)+3;
			return 0.01*num;
		}
	}
}