package com.djayard.deck.proj.impl.standard;

import static com.djayard.deck.proj.impl.standard.StandardCard.*;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

import org.junit.Test;


public class StandardDeckTest {

	@Test
	public void defaultDeckSize(){
		StandardDeck deck = new StandardDeck();
		assertEquals(deck.size(), SUIT.values().length * VALUE.values().length);
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
		List<StandardDeck> subs = deck.splitDeck();
		assertEquals(subs.get(0).size(), subs.get(1).size());
		
		final int size = deck.size();
		final int breakpoint = size/2;
		
		//verifying that the split is simple; the cards follow the order from the parent deck
		for(int i = 0; i < size; ++i){
			StandardDeck sub = subs.get(i < breakpoint ? 0 : 1);
			assertEquals(deck.getCards().get(i), sub.getCards().get(i % breakpoint));
		}
	}
	
	@Test
	public void shuffleMutatesTheDeck(){
		StandardDeck deck = new StandardDeck();
		
		List<StandardCard> originalPermuation = new ArrayList<>(deck.getCards());
		
		deck.shuffle();
		
		confirmMutation(deck.getCards(), originalPermuation);
	}
	
	private static List<StandardCard> shortCardList(){
		List<StandardCard> someCards = new ArrayList<>(4);
		someCards.add(new StandardCard(SUIT.HEART, VALUE.ACE));
		someCards.add(new StandardCard(SUIT.SPADE, VALUE.ACE));
		someCards.add(new StandardCard(SUIT.CLUB, VALUE.ACE));
		someCards.add(new StandardCard(SUIT.DIAMOND, VALUE.ACE));
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
