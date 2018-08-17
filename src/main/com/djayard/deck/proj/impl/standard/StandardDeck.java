package com.djayard.deck.proj.impl.standard;

import static com.djayard.deck.proj.impl.standard.StandardCard.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import com.djayard.deck.proj.Deck;


/**
 * Concrete class for managing a standard 52-card deck.
 */
public class StandardDeck implements Deck<StandardCard>{
	
	private List<StandardCard> cards;
	
	/**
	 * Default constructor. The deck is initialized with the 52 cards, and the cards
	 * are shuffled.
	 */
	public StandardDeck(){
		this(true,true);
	}
	
	/**
	 * Constructor that allows a partial deck to be created.
	 */
	public StandardDeck(Collection<StandardCard> cards){
		this.cards = new ArrayList<>(cards);
	}
	
	/**
	 * Creates a deck instances and allows the caller to control whether or not
	 * the deck is initialized with 52 cards or shuffled.
	 * @param doInitialize If true, the deck is filled with the standard 52 cards. Otherwise, the deck is empty.
	 * @param doShuffle If doInitialized is true and doShuffle is true, the deck's contents are shuffled.
	 */
	public StandardDeck(Boolean doInitialize, Boolean doShuffle){
		if(doInitialize){
			initialize(this);
			if(doShuffle){
				shuffle();
			}
		} else {
			cards = new ArrayList<>();
		}
		
	}
	
	/**
	 * Sets the deck's content to be that of a 52-card deck.
	 * @param deck The deck to modify.
	 */
	private static void initialize(StandardDeck deck){
		SUIT[] suits = SUIT.values();
		VALUE[] values = VALUE.values();
		deck.cards = new ArrayList<>(suits.length * values.length);
		for(SUIT suit:suits){
			for(VALUE value:values){
				deck.cards.add(new StandardCard(suit,value));
			}
		}
	}
	
	/**
	 * Creates two partial decks by cutting this deck in half.
	 * @return A List of 2 partial decks created from splitting this deck.
	 */
	public List<StandardDeck> splitDeck(){
		return splitDeck(size()/2);
	}
	
	/**
	 * Method for cutting the deck.
	 * @param breakpoints Indexes (not inclusive) at which to split the deck.
	 * @return A List of partial decks created from splitting this deck.
	 */
	public List<StandardDeck> splitDeck(int... breakpoints){
		List<StandardDeck> newDecks = new ArrayList<>(breakpoints.length + 1);
		
		int startPoint = 0;
		for(int i = 0; i < breakpoints.length; ++i){
			int breakpoint = breakpoints[i];
			newDecks.add(new StandardDeck(cards.subList(startPoint, breakpoint)));
			startPoint = breakpoint;
		}
		
		if(startPoint < size() - 1){
			newDecks.add(new StandardDeck(cards.subList(startPoint, size())));
		}
		
		return newDecks;
	}

	@Override
	public List<StandardCard> getCards() {
		return cards;
	}
	
	/**
	 * Shuffle implementation following the Fisher-Yates algorithm.
	 */
	@Override
	public void shuffle() {
		final int size = size();
		final List<StandardCard> startingPermutation = this.cards;
		StandardCard[] newPermutation = new StandardCard[size];
		
		for(int i = 0; i < size; ++i){
			int j = (int)Math.round(Math.random() * i);
			if(j != i){
				newPermutation[i] = newPermutation[j];
			}
			newPermutation[j] = startingPermutation.get(i);
		}
		
		cards = Arrays.asList(newPermutation);
	}
	
	
}