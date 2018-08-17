package com.djayard.deck.proj;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * API for interacting with a deck of cards.
 */
public abstract class Deck<E extends Card> {
	
	protected List<E> cards;
	protected Consumer<List<E>> shuffler = Collections::shuffle;
	
	/**
	 * Operation that shuffles the deck's contents.
	 */
	public void shuffle(){
		Optional.ofNullable(getShuffler()).orElse(Collections::shuffle).accept(getCards());
	}
	
	/**
	 * Reports the deck's current top card without removing the card from the deck.
	 * @return The current top card or null.
	 */
	public E peek() {
		return peek(0);
	}
	
	/**
	 * Reports the card at a specific position of the deck. Lower indexes are closer to the top of the deck.
	 * @param index A non-negative integer.
	 * @return The card at the specified index or null if a card is not available at that position.
	 */
	public E peek(int index) {
		return index > -1 && index < size() ? getCards().get(index) : null;
	}
	
	
	/**
	 * Remove the top card from the deck.
	 * @return A card from the top of the deck.
	 * @throws NoSuchElementException Thrown if the deck is empty when this method is called.
	 */
	public E draw() throws NoSuchElementException {
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
	public List<E> draw(int numCards) throws IllegalArgumentException {
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
	public List<E> draw(int numRequested, Boolean permissive) throws IllegalArgumentException {
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
	 * Removes the card at the specificed index from the deck.
	 * @param index A non-negative integer.
	 * @return The card formerly at the specified index, or null if no such card is available.
	 */
	public E removeCard(int index) throws IndexOutOfBoundsException {
		return index > -1 && index < size() ? getCards().remove(index) : null;
	}
	
	/**
	 * Attempts to locate a card that satisfies the predicate.
	 * @param necessaryCondition Condition that an acceptable card must satisfy.
	 * @return The index of a card that satisfies the predicate, or -1.
	 */
	public int findCard(Predicate<? super E> necessaryCondition) {
		return findCard(0, necessaryCondition);
	}
	
	/**
	 * Attempts to locate a card that satisfies the predicate. The search begins at the passed in index.
	 * @param index The index to begin the search at.
	 * @param necessaryCondition Condition that an acceptable card must satisfy.
	 * @return The index of a card that satisfies the predicate, or -1.
	 */
	public int findCard(int index, Predicate<? super E> necessaryCondition) {
		for(int i = index; i < size(); ++i){
			if( necessaryCondition.test(cards.get(i)) ){
				return  i;
			}
		}
		
		return -1;
	}
	
	/**
	 * The current size of the deck.
	 * @return Integer reflecting the current number of cards in the deck.
	 */
	public int size(){
		return getCards().size();
	}
	
	/**
	 * @return True if and only if the deck has no more cards to offer.
	 */
	public boolean isEmpty(){
		return getCards().isEmpty();
	}
	
	/**
	 * @return True if and only if the deck has more cards to offer.
	 */
	public boolean isNotEmpty(){
		return !isEmpty();
	}
	
	/**
	 * Inject cards into the deck.
	 * @param returningCards Cards to be added.
	 * @param start The insertion point for the cards. The element at that position (and all subsequent elements) will be shifted
	 * over so that they come after the newly inserted cards.
	 * @return true if the operations succeeds
	 */
	public boolean insertCards(List<E> returningCards, int start) {
		return getCards().addAll(start, returningCards);
	}
	
	/**
	 * Creates two partial decks by cutting this deck in half.
	 * @return A List of 2 partial decks created from splitting this deck.
	 */
	public abstract List<Deck<E>> splitDeck();
	
	/**
	 * Method for cutting the deck.
	 * @param breakpoints Indexes (not inclusive) at which to split the deck.
	 * @return A List of partial decks created from splitting this deck.
	 */
	public abstract List<Deck<E>> splitDeck(int...breakpoints);
	
	/**
	 * Assister method to allow subclasses to easily implemented the public splitDeck methods.
	 * @param subDeckConstructor Function that accepts a list of cards and returns a new deck.
	 * @param breakpoints Indexes (not inclusive) at which to split the deck.
	 * @return A List of partial decks created from splitting this deck.
	 */
	protected List<Deck<E>> splitDeck(Function<List<E>, Deck<E>> subDeckConstructor, int... breakpoints){
		List<Deck<E>> newDecks = new ArrayList<>(breakpoints.length + 1);
		
		int startPoint = 0;
		for(int i = 0; i < breakpoints.length; ++i){
			int breakpoint = breakpoints[i];
			newDecks.add(subDeckConstructor.apply(cards.subList(startPoint, breakpoint)));
			startPoint = breakpoint;
		}
		
		if(startPoint < size() - 1){
			newDecks.add(subDeckConstructor.apply(cards.subList(startPoint, size())));
		}
		
		return newDecks;
	}
	
	/**
	 * Getter method for the deck's contents.
	 * @return List of cards.
	 */
	protected List<E> getCards(){
		return cards;
	}
	
	/**
	 * Specify the exact contents of the deck.
	 * @param cards The cards they deck should have.
	 * @return this
	 */
	protected Deck<E> setCards(List<E> cards){
		this.cards = cards;
		return this;
	}
	
	/**
	 * Getter method for the deck's current shuffler.
	 * @return A method that mutates a list of cards
	 */
	public Consumer<List<E>> getShuffler(){
		return shuffler;
	}
	
	/**
	 * Specify how the deck should shuffle itself. Enter an argument of null to reset the shuffle back to the default.
	 * @param shuffler Function that mutates a list of cards
	 * @return this
	 */
	public Deck<E> setShuffler(Consumer<List<E>> shuffler){
		this.shuffler = Optional.ofNullable(shuffler).orElse(Collections::shuffle);
		return this;
	}
}
