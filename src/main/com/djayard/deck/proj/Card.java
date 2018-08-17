package com.djayard.deck.proj;

/**
 * Simple interface that requires cards in a deck to report an identifier.
 */
public interface Card {
	
	/**
	 * Returns an identifier for this card instance.
	 * @return The card's identifier.
	 */
	String code();
	
}
