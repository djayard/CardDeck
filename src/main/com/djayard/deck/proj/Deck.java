package com.djayard.deck.proj;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * Base class for interacting with a deck of cards.
 */
public class Deck<E extends Card> {
	
	protected List<E> cards;
	protected Consumer<List<E>> shuffler;
	
	/**
	 * Default constructor. The deck is initialized without any cards.
	 */
	public Deck(){
		this(Collections.emptyList());
	}
	
	/**
	 * The deck is initialized such that it can offer the cards passed in.
	 * @param cards The cards that should be available in the deck.
	 */
	public Deck(Collection<E> cards){
		this(cards, false);
	}
	
	/**
	 * The deck is initialized such that it can offer the cards passed in.
	 * @param cards The cards that should be available in the deck.
	 * @param doShuffle True if the cards should be shuffled
	 */
	public Deck(Collection<E> cards, boolean doShuffle) {
		this(cards, doShuffle, Collections::shuffle);
	}
	
	/**
	 * The deck is initialized such that it can offer the cards passed in.
	 * @param cards The cards that should be available in the deck.
	 * @param doShuffle True if the cards should be shuffled
	 * @param shuffler A function that mutates a list of cards.
	 */
	public Deck(Collection<E> cards, boolean doShuffle, Consumer<List<E>> shuffler) {
		//LinkedList used to reduce cost of remove and draw operations
		this.cards = new LinkedList<>(cards);
		this.shuffler = shuffler;
		if(doShuffle){
			shuffle();
		}
	}
	
	
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
		if( Boolean.FALSE.equals(permissive) && numRequested > size()){
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
	 * Removes the card at the specified index from the deck.
	 * @param index A non-negative integer.
	 * @return The card formerly at the specified index, or null if no such card is available.
	 */
	public E removeCard(int index) throws IndexOutOfBoundsException {
		return index > -1 && index < size() ? getCards().remove(index) : null;
	}
	
	/**
	 * Removes all cards that satisfy the specified condition.
	 * @param necessaryCondition The criteria for removing the card from the deck.
	 * @return A list of the cards removed. The list may be empty, but it is never null.
	 */
	public List<E> removeAll(Predicate<? super E> necessaryCondition){
		ListIterator<E> iterator = getCards().listIterator();
		List<E> removedCards = new ArrayList<>();
		
		while(iterator.hasNext()){
			E card = iterator.next();
			if(necessaryCondition.test(card)){
				removedCards.add(card);
				iterator.remove();
			}
		}
		
		return removedCards;
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
		ListIterator<E> iterator = getCards().listIterator(index);
		while(iterator.hasNext()){
			if( necessaryCondition.test(iterator.next()) ){
				return  iterator.previousIndex();
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
	 * @param start The insertion point for the cards. The element at that position (and all subsequent elements) will be shifted
	 * over so that they come after the newly inserted cards.
	 * @param returningCards Cards to be added.
	 * @return true if the operations succeeds
	 */
	public boolean insertCards(int start, Collection<E> returningCards) {
		return getCards().addAll(start, returningCards);
	}
	
	/**
	 * Creates two partial decks by cutting this deck in half. This method calls on splitDeck(int...); overwrite
	 * that method to change the concrete type of the created decks.
	 * @return A List of 2 partial decks created from splitting this deck.
	 */
	public List<Deck<E>> splitDeck(){
		return splitDeck(size()/2);
	}
	
	/**
	 * Method for cutting the deck.
	 * @param breakpoints Indexes (not inclusive) at which to split the deck.
	 * @return A List of partial decks created from splitting this deck.
	 */
	public List<Deck<E>> splitDeck(int...breakpoints){
		return splitDeck(Deck<E>::new, this, breakpoints);
	}
	
	/**
	 * Split a deck such that all the children are initialized in a way specified by you.
	 * @param deckConstructor A function that create a new deck instance from a list of cards.
	 * @param sourceDeck The deck to split.
	 * @param breakpoints Indexes (not inclusive) at which to split the deck.
	 * @return A list of decks created by splitting the passed in deck
	 */
	public static <T extends Card, U extends Deck<T>> List<U> splitDeck(
			Function<List<T>, U> deckConstructor, Deck<T> sourceDeck, int... breakpoints){
		List<U> newDecks = new ArrayList<>(breakpoints.length + 1);
		List<T> cards = sourceDeck.getCards();
		
		int startPoint = 0;
		for(int i = 0; i < breakpoints.length; ++i){
			int breakpoint = breakpoints[i];
			newDecks.add(deckConstructor.apply(cards.subList(startPoint, breakpoint)));
			startPoint = breakpoint;
		}
		
		if(startPoint < cards.size() - 1){
			newDecks.add(deckConstructor.apply(cards.subList(startPoint, cards.size())));
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
