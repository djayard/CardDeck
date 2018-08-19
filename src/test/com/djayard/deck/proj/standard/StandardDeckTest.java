package com.djayard.deck.proj.standard;

import static com.djayard.deck.proj.standard.StandardCard.*;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.function.Predicate;

import org.junit.Test;

import com.djayard.deck.proj.Deck;

public class StandardDeckTest {

	@Test
	public void defaultDeck(){
		final int size = SUIT.values().length * VALUE.values().length;
		StandardDeck deck = new StandardDeck();
		assertEquals(deck.size(), size);
		
		Set<StandardCard> cardSet = new HashSet<>(deck.size(), 1f);
		List<StandardCard> drawnCards = deck.draw(size);
		
		for(StandardCard card:drawnCards){
			if(cardSet.contains(card)){
				fail("The default had a non-unique card!");
			}
			cardSet.add(card);
		}
	}
	
	@Test
	public void noTwoDecksSame(){
		confirmMutation(new StandardDeck().getCards(), new StandardDeck().getCards());
		
	}
	
	@Test(expected=NoSuchElementException.class)
	public void failOnEmptySimpleDraw(){
		StandardDeck deck = new StandardDeck(shortCardList());
		
		final int size = deck.size();
		for(int i = 0; i <= size; ++i){
			deck.draw();
		}
		
	}
	
	@Test
	public void sizeDecreaseOnDraw(){
		StandardDeck deck = new StandardDeck(shortCardList());
		final int size = deck.size();
		for(int i = 1; i <= size; ++i){
			deck.draw();
			assertEquals(deck.size(), size-i);
		}
	}
	
	@Test
	public void drawMultiple() {
		StandardDeck deck = new StandardDeck();
		final int initialSize = deck.size();
		final int numberToDraw = 14;
		List<StandardCard> cards = deck.draw(numberToDraw);
		
		assertEquals(cards.size(), numberToDraw);
		assertEquals(deck.size(), initialSize-numberToDraw);
	}
	
	@Test
	public void splitDeck(){
		StandardDeck deck = new StandardDeck(shortCardList());
		List<Deck<StandardCard>> subs = deck.splitDeck();
		assertEquals(subs.get(0).size(), subs.get(1).size());
		
		final int size = deck.size();
		final int breakpoint = size/2;
		
		//verifying that the split is simple; the cards follow the order from the parent deck
		for(int i = 0; i < size; ++i){
			Deck<StandardCard> sub = subs.get(i < breakpoint ? 0 : 1);
			assertEquals(deck.peek(i), sub.peek(i % breakpoint));
		}
	}
	
	@Test
	public void shuffleMutatesTheDeck(){
		StandardDeck deck = new StandardDeck();
		
		List<StandardCard> originalPermuation = new ArrayList<>(deck.getCards());
		
		deck.shuffle();
		
		confirmMutation(deck.getCards(), originalPermuation);
	}
	
	@Test
	public void find(){
		StandardDeck deck = new StandardDeck();
		StandardCard ace = new StandardCard(SUIT.SPADE, VALUE.ACE);
		List<StandardCard> addToDeck = new ArrayList<>(1);
		addToDeck.add(ace);
		
		deck.insertCards(0, addToDeck);
		
		assertEquals(0, deck.findCard(ace::equals));
	}
	
	@Test
	public void findAndRemove(){
		StandardDeck deck = new StandardDeck();
		final int initialSize = deck.size();
		StandardCard queenOfHearts = new StandardCard(SUIT.HEART, VALUE.QUEEN);
		int queenPos = deck.findCard(queenOfHearts::equals);
		assertTrue(  queenPos > -1 );
		
		StandardCard removedCard = deck.removeCard(queenPos);
		assertEquals(queenOfHearts, removedCard);
		assertEquals(deck.size(), initialSize-1);
	}
	
	@Test
	public void removeAll(){
		StandardDeck deck = new StandardDeck();
		final int initialSize = deck.size();
		final int suitSize = VALUE.values().length;
		
		Predicate<StandardCard> isHeart = card -> SUIT.HEART.equals(card.suit);
		List<StandardCard> hearts = deck.removeAll(isHeart);
		
		assertEquals(hearts.size(), suitSize);
		assertEquals(deck.size(), initialSize-suitSize);
		
	}
	
	private static List<StandardCard> shortCardList(){
		List<StandardCard> someCards = new ArrayList<>(4);
		for(SUIT suit: SUIT.values()){
			someCards.add(new StandardCard(suit, VALUE.ACE));
		}
		
		return someCards;
	}
	
	private static void confirmMutation(List<StandardCard> a, List<StandardCard> b){
		assertEquals(a.size(), b.size());
		
		Iterator<StandardCard> i = a.iterator();
		Iterator<StandardCard> j = b.iterator();
		
		while(i.hasNext()){
			if(i.next().code() != j.next().code()){
				return;
			}
		}
		
		fail("Excepted the decks to be different, but instead they were identical.");
	}
}
