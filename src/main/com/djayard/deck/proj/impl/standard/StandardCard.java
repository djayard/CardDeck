package com.djayard.deck.proj.impl.standard;

import com.djayard.deck.proj.Card;

/**
 * A card that belongs to a normal 4-suit 52-card deck. A card's suit and value
 * are immutable once the card is created.
 *
 */
public class StandardCard implements Card {
	
	/**
	 * Enum representing the 4-suits possible for a card in a standard deck.
	 */
	public enum SUIT {
		HEART, DIAMOND, CLUB, SPADE;
	}

	/**
	 * Enum representing the 13 face-values possible for a card in a standard deck.
	 */
	public enum VALUE {
		ACE(1), TWO(2), THREE(3), FOUR(4), FIVE(5), SIX(6), SEVEN(7), EIGHT(8), NINE(9), TEN(10), JACK(10), QUEEN(10), KING(10);
		
		final public int intValue;
		
		private VALUE(int value){
			intValue = value;
		}
	}
	
	
	private final SUIT suit;
	private final VALUE value;
	
	/**
	 * Constructor for setting the required fields.
	 * @param suit An instance of {@link SUIT}.
	 * @param value An instance of {@link VALUE}.
	 */
	public StandardCard(SUIT suit, VALUE value){
		this.suit = suit;
		this.value = value;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof StandardCard){
			StandardCard other = (StandardCard)obj;
			return this.code().equals(other.code());
		}
		
		return false;
	}
	
	@Override
	public int hashCode() {
		return code().hashCode();
	}
	
	@Override
	public String code() {
		return suit.name() + " _ " + value.name();
	}
	
	public SUIT getSuit() {
		return suit;
	}
	
	public VALUE getValue() {
		return value;
	}
	
}
