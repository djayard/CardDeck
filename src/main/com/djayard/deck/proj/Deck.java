package com.djayard.deck.proj;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.function.Consumer;

/**
 * API for interacting with a deck of cards.
 */
public interface Deck<E extends Card> {
	
	/**
	 * Getter method for the deck's contents.
	 * @return List of cards.
	 */
	List<E> getCards();
	
	/**
	 * Getter method for the deck's current shuffler.
	 * @return A method that mutates a list of cards
	 */
	Consumer<List<E>> getShuffler();
	
	/**
	 * Specify how the deck should shuffle itself. Enter an argument of null to reset the shuffle back to the default.
	 * @param shuffler Function that mutates a list of cards
	 */
	void setShuffleFunction(Consumer<List<E>> shuffler);
	
	/**
	 * Operation that shuffles the deck's contents.
	 */
	default void shuffle(){
		Optional.ofNullable(getShuffler()).orElse(Collections::shuffle).accept(getCards());
	}
	
	
	/**
	 * Attempts to return a card from the deck.
	 * @return A card from the top of the deck.
	 * @throws NoSuchElementException Thrown if the deck is empty when this method is called.
	 */
	default E draw() throws NoSuchElementException {
		if(isEmpty()){
			throw new NoSuchElementException("The deck has no card to offer.");
		}
		return getCards().remove(0);
	}
	
	/**
	 * Attempts to retrieve a number of cards from the top of the deck.
	 * @param numCards The number of cards desired.
	 * @return A List of cards whose length is equal or less than the value of numCards.
	 * @throws IllegalArgumentException Thrown if the argument is not a positive integer.
	 */
	default List<E> draw(int numCards) throws IllegalArgumentException {
		return draw(numCards, true);
	}
	
	/**
	 * Attempts to retrieve a number of cards from the top of the deck.
	 * @param numRequested The number of cards desired.
	 * @param permissive If false, this method will throw IllegalArgumentException if the deck can't provide the number of cards requested.
	 * @return A List of cards whose length is equal or less than the value of numCards.
	 * @throws IllegalArgumentException Thrown if numRequested is not a positive integer, or permissive is false and
	 *   the deck's size is less than numRequested.
	 */
	default List<E> draw(int numRequested, Boolean permissive) throws IllegalArgumentException {
		if( !permissive && numRequested > size()){
			throw new IllegalArgumentException(String.format("Not enough cards in the deck to draw %d.%n", numRequested));
		}
		
		if(numRequested < 1){
			throw new IllegalArgumentException(String.format(
					"Positive integer expected for the number of cards to draw. Received: %d%n", numRequested));
		}
		
		int numberToDraw = Math.min(numRequested, size());
		
		List<E> leadingCards = getCards().subList(0, numberToDraw);
		final List<E> drawnCards = new ArrayList<>(leadingCards);
		
		leadingCards.clear();
		
		return drawnCards;
		
	}
	
	/**
	 * The current size of the deck.
	 * @return Integer reflecting the current number of cards in the deck.
	 */
	default int size(){
		return getCards().size();
	}
	
	/**
	 * @return True if and only if the deck has no more cards to offer.
	 */
	default boolean isEmpty(){
		return getCards().isEmpty();
	}
	
	/**
	 * @return True if and only if the deck has more cards to offer.
	 */
	default boolean isNotEmpty(){
		return !isEmpty();
	}
	
	/**
	 * Inject cards into the deck.
	 * @param returningCards Cards to be added.
	 * @param start The insertion point for the cards. The element at that position (and all subsequent elements) will be shifted
	 * over so that they come after the newly inserted cards.
	 * @return true if the operations succeeds
	 */
	default boolean insertCards(List<E> returningCards, int start) {
		return getCards().addAll(start, returningCards);
	}	
}
