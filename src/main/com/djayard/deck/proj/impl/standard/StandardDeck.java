package com.djayard.deck.proj.impl.standard;

import static com.djayard.deck.proj.impl.standard.StandardCard.*;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

import com.djayard.deck.proj.Deck;


/**
 * Class for managing a standard 52-card deck.
 */
public class StandardDeck extends Deck<StandardCard>{
	
	//Overwrite of base class' default.
	{
		shuffler = StandardDeck::fisherYatesShuffle;
	}
	
	/**
	 * Default constructor. The deck is initialized with the 52 cards, and the cards
	 * are shuffled.
	 */
	public StandardDeck(){
		this(true);
		
	}
	
	/**
	 * Constructor that allows a partial deck to be created.
	 */
	public StandardDeck(Collection<StandardCard> cards){
		super(cards);
	}
	
	/**
	 * Creates a deck and allows the caller to control whether or not
	 * the deck is shuffled.
	 * @param doShuffle If true, the deck is shuffled are it's populated with cards.
	 */
	public StandardDeck(boolean doShuffle){
		this(doShuffle, StandardDeck::fisherYatesShuffle);
		
	}
	
	public StandardDeck(boolean doShuffle, Consumer<List<StandardCard>> shuffler){
		this.shuffler = shuffler;
		SUIT[] suits = SUIT.values();
		VALUE[] values = VALUE.values();
		for(SUIT suit:suits){
			for(VALUE value:values){
				cards.add(new StandardCard(suit,value));
			}
		}
		if(doShuffle){
			shuffle();
		}
	}
	
	@Override
	public List<Deck<StandardCard>> splitDeck(int... breakpoints) {
		return splitDeck(StandardDeck::new, this, breakpoints);
	}
	
	/**
	 * If shuffler is non-null, shuffler is called with this deck's cards. Otherwise, this deck's cards are shuffled according
	 * to the Fisher-Yates algorithm.
	 */
	@Override
	public void shuffle() {
		Optional.ofNullable(shuffler).orElse(StandardDeck::fisherYatesShuffle).accept(cards);
	}
	
	static void fisherYatesShuffle(List<StandardCard> cards){
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
