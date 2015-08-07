/**
//this class creates a card type, recognizing a value and suit for each card in a deck
*/

public class Card {
	String name;
	int val;
	String suit;
	
	//constructor
	public Card(int val, String suit){
		this.val = val+1;
		this.suit = suit;
		
		if (this.val == 11)
			this.name = "Jack";
		else if (this.val == 12)
			this.name = "Queen";
		else if (this.val == 13)
			this.name = "King";
		else if (this.val == 1)
			this.name = "Ace";
		else
			this.name = Integer.toString(this.val);
	}
	
	public void printCard(){
		System.out.print(this.name + " of " + this.suit);
	}
	
	public String cardString(){
		String c = this.name + " of " + this.suit;
		return c;
	}
	
}
