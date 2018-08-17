package com.djayard.deck.proj.impl.standard;

import static com.djayard.deck.proj.impl.standard.StandardCard.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import com.djayard.deck.proj.Deck;


/**
 * Concrete class for managing a standard 52-card deck.
 */
public class StandardDeck extends Deck<StandardCard>{
	
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
	
	@Override
	public List<Deck<StandardCard>> splitDeck() {
		return splitDeck(StandardDeck::new, size()/2);
	}
	
	@Override
	public List<Deck<StandardCard>> splitDeck(int... breakpoints) {
		return splitDeck(StandardDeck::new, breakpoints);
	}
	
	/**
	 * If shuffler is non-null, shuffler is called with this deck's cards. Otherwise, this deck's cards are shuffled according
	 * to the Fisher-Yates algorithm.
	 */
	@Override
	public void shuffle() {
		Optional.ofNullable(shuffler).orElse(StandardDeck::fisherYatesShuffle).accept(cards);
	}
	
	private static void fisherYatesShuffle(List<StandardCard> cards){
		final int size = cards.size();
		StandardCard[] newPermutation = new StandardCard[size];
		
		for(int i = 0; i < size; ++i){
			int j = (int)Math.round(Math.random() * i);
			if(j != i){
				newPermutation[i] = newPermutation[j];
			}
			newPermutation[j] = cards.get(i);
		}
		
		cards.clear();
		Arrays.stream(newPermutation).forEach(cards::add);
	}
	
	@Override
	protected List<StandardCard> getCards() {
		return super.getCards();
	}
	
	
	@Override
	public String toString() {
		return cards.toString();
	}
	
	/**
	 * Simple main for toying with the class.
	 */
	public static void main(String... args){
		System.out.println(new StandardDeck());
	}
	
	
}
