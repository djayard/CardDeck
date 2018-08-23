package com.djayard.deck.proj.tcg;

import com.djayard.deck.proj.Card;

public interface TradingCard<E extends Stats> extends Card {
	
	/**
	 * @return true if the card has a special effect; false otherwise.
	 */
	default boolean isSpecial() {
		return false;
	}

	/**
	 * Provide an entry point for executing a card's special effect (if any). This
	 * method defaults to being a no-op. 
	 * @param controller The game instance that will be applying the effect.
	 */
	default void specialEffect(GameController controller) {
		return;
	}
	
	/**
	 * @return The card's official name or title.
	 */
	String name();
	
	/**
	 * @return The card's description, a.k.a flavor-text.
	 */
	String description();
	
	/**
	 * @return An object modeling the card's normal combat capabilities.
	 */
	E stats();
	
}
