/**
//this class creates a deck of cards by converting 52 integers to an arrayList of cards
//each with a value, name, and suit
*/

import java.util.*;

public class Deck {
	ArrayList<Card> finalDeck = new ArrayList<Card>(52);
	
	public Deck(ArrayList<Card> deck){
		this.finalDeck = deck;
		this.fill();
	}
	
	public ArrayList<Card> fill(){
		ArrayList<Integer> numDeck = new ArrayList<Integer>(52);
		ArrayList<String> stringDeck = new ArrayList<String>(52);
		Card myCard;
		
		for (int i = 0; i<52; i++)
			numDeck.add(i);
		
		for (int i = 0; i < 52; i ++){
			int cardVal = i%13;
			stringDeck.add(i, Integer.toString(cardVal));
		}
		
		for (int i=0; i<stringDeck.size(); i ++){
			String cardVal = stringDeck.get(i);
			if (i%4 == 1){
				myCard = new Card(i%13, "Clubs");
				finalDeck.add(i,myCard);
				stringDeck.set(i, cardVal+ " of Clubs");
			}
			else if (i%4 == 2){
				myCard = new Card(i%13, "Diamonds");
				finalDeck.add(i, myCard);
				stringDeck.set(i, cardVal+" of Diamonds");
			}
			else if (i%4 == 3){
				myCard = new Card(i%13, "Hearts");
				finalDeck.add(i, myCard);
				stringDeck.set(i, cardVal+" of Hearts");
			}
			else{
				myCard = new Card(i%13, "Spades");
				finalDeck.add(i, myCard);
				stringDeck.set(i, cardVal+" of Spades");
			}
		}
		return finalDeck;
	}
	
	public Deck shuffle(){
		double shuffCount = (Math.random()*25)+15;
		for(double i=0; i<shuffCount; i++)
			Collections.shuffle(this.finalDeck);
		return this;
	}
	public ArrayList<Card> getDeck(){
		return finalDeck;
	}
	
	public void print(){
		for (Card card : finalDeck)
			card.printCard();	
	}
	
	public void remove(int index){
		this.finalDeck.remove(index);
	}
	
	public Card get(int index){
		return this.finalDeck.get(index);
	}
}
