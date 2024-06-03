/*
  Open Sourcerers
  Nathan Jobes, Amy Gonzales, Chris Bray, Jerome Ortega
  CST 338 Week 3
  Assignment 3: Decks of Cards
  3/17/2021
 */

import java.util.Scanner;
import java.util.Random;

public class Assig3

{
   public static void main(String[] args)
   {
     //Phase 3 
      Deck myDeck = new Deck();
      System.out.println("Test with 2 pack.\n");
      //loops to repeat test for 2 pack and 1 pack version
      for (int numPacks = 2; numPacks > 0; numPacks --)
      {
         //incrementor to add line breaks to print as a paragraph
         int lineBreak = 0;
         //a loop to switch shuffle off and on
         for (int i = 1; i <= 2; i++)
         {
            myDeck = new Deck(numPacks);
            lineBreak = 0;
            if (i%2 == 0)
            {
               myDeck.shuffle();
               System.out.println("Now with shuffle:");
            }
            while (myDeck.getTopCard() > 0)
            {
               lineBreak++;
               System.out.print(myDeck.dealCard() + " / ");
               if (lineBreak%5 == 0) System.out.print("\n"); // for readability
            }
            System.out.println("\n"); // add space between prints
         }
       //Add space between first and second test
       if (numPacks == 2) System.out.println("\n\nTest with 1 pack.\n\n");
       //Mark end of test
       else System.out.println("\nEnd of Phase 3 Test\n\n");
      }
      
      //Phase 4
      Scanner keyboard = new Scanner(System.in);
      boolean validInput = false;
      int numHands = 0;
      String errorMessage = "";
      
      while (!validInput)
      {
         
         System.out.println(errorMessage + "How many hands? (1-10, please): ");
         //checks for bad values
         if (keyboard.hasNextInt()) numHands = keyboard.nextInt();
         keyboard.nextLine();
         
         if (numHands > 0 && numHands <= 10)
         {
            validInput = true;
         }
         else
         {
            errorMessage = "Invalid input, retry - ";
            continue;
         }
         
         Deck newDeck = new Deck(); //new deck
         Hand hands[] = new Hand[numHands]; //create array 
         int firstCard = newDeck.getTopCard(); //get first card 
         
         //Populate Hands
         for (int currentHand = 0; currentHand < numHands; ++currentHand)
         {
            hands[currentHand] = new Hand(); //populates array w/ hand objects
         }
         
         //Place cards into hands
         while (firstCard > 0)
         {
            
            for (int currentHand = 0; currentHand < numHands; ++currentHand)
            { 
               if (newDeck.getTopCard() <= 0)
                  break; //stop dealing cards
               hands[currentHand].takeCard(newDeck.dealCard());
               --firstCard;
            }
         }
         
         System.out.println("Unshuffled hands: ");
         for (int currentHand = 0; currentHand < numHands; ++currentHand)
         {
            System.out.println(" Player " + (currentHand + 1) 
                  + " = ( "+ hands[currentHand].toString() + ")");//prints cards
            hands[currentHand].resetHand();
         }
         
         newDeck.init(1);
         newDeck.shuffle();
         
         firstCard = newDeck.getTopCard(); //get first card 
         for (int currentHand = 0; currentHand < numHands; ++currentHand)
         {
            hands[currentHand] = new Hand(); //populates array w/ hand objects
         }
         
         while (firstCard > 0)
         {
            for (int currentHand = 0; currentHand < numHands; ++currentHand)
            { 
               if (newDeck.getTopCard() <= 0)
                  break; //stop dealing cards
               hands[currentHand].takeCard(newDeck.dealCard());
               -- firstCard;
            }

         }
         System.out.println("Shuffled hands: ");
         for (int currentHand = 0; currentHand < numHands; ++currentHand)
         {
            System.out.println(" Player " + (currentHand + 1) 
                  + " = ( " + hands[currentHand].toString()+")");//prints cards
         }
         System.out.println("Thanks for playing!");
      
         
         keyboard.close();
         
      }
      
      
   }
 
}


class Card
{
   
   public enum Suit {CLUBS, DIAMONDS, HEARTS, SPADES}
   
   private char value;
   private Suit suit;
   private boolean errorFlag;
   
   //Default
   //Amy
   public Card()
   {
      char defaultValue = 'A';
      Suit defaultSuit = Suit.SPADES;
      set(defaultValue, defaultSuit);
   }
   
   //Overload
   //Amy
   public Card(char value, Suit suit)
   {
      set(value, suit);
   }
   
   
   //Chris -Returns a string representation of the Card,
   //returns invalid if (errorFlag)
   public String toString()
   {
      if (errorFlag == true)
      {
         return "[invalid]";
      }
      return (value + " " + suit);
   }
   
 
   //Chris -Sets the card's representation and
   //modifies errorFlag based on inputs
   public boolean set(char value, Suit suit)
   {
      if (isValid(value, suit))
      {
         this.value = value;
         this.suit = suit;
         errorFlag = false;
         
      }
      else
      {
         errorFlag = true;
         
      }
      return !errorFlag;
   }
   
   //Accessors
   public Suit getSuit()
   {
      return suit;
   }
   
   public char getValue()
   {
      return value;
   }
   
   //Nathan - Returns value of error Flag
   public boolean getErrorFlag()
   {
      return errorFlag;
   }
   
   //Returns true if the cards are the same value and suit
   //Nathan
   public boolean equals(Card card)
   {
      //If all instance variables are equal return true, otherwise false
      if (value == card.getValue() && suit == card.getSuit() &&
            errorFlag == card.getErrorFlag()) return true;
      else return false;
   }
   
   //Returns true if entered value is a real card value -
   //Suit should always be valid
   //Amy
   private boolean isValid(char value, Suit suit)
   {
      //checks each character in string for bad value
      String charValue = "23456789TJQKA";
      if (charValue.indexOf(value) != -1)
         return true;
      else
         return false;  
   }
      
}


class Hand
{
   
   public static final int MAX_CARDS = 52; // one deck
   
   private Card [] myCards;
   private int numCards;
   
   //Default
   public Hand()
   {
      numCards = 0;
      myCards = new Card[MAX_CARDS];
   }
   
   // Creates a new cards array.
   public void resetHand()
   { 
      myCards = new Card[MAX_CARDS];
   }
   
   
   //Chris - Adds a copy of a card to the hand
   public boolean takeCard(Card card)
   {
      if (numCards < MAX_CARDS)
      {
         myCards[numCards] = new Card(card.getValue(), card.getSuit());
         numCards++;
         return true;
      }
      else return false;
   }
   
   
   //Returns and removes the card at the tail of myCards[]
   //Nathan
   public Card playCard()
   {
      if (numCards > 0) //If we have a card
      {
         Card playedCard = myCards[numCards -1]; //get card to play
         myCards[numCards-1] = null; // remove from myCards[]
         numCards--; // adjust numCards
         
         //Because hand contains its own unique references which were removed
         //a copy isn't needed to be returned
         return playedCard;
      }
      else // return an invalid card
      {
         Card errorCard = new Card('Z', Card.Suit.SPADES);
         return errorCard;
      }
   }
   
   
   public String toString()
   {
      String oneString = "";
      for (int i = 0; i < numCards; i++)
      {
         oneString = oneString + myCards[i].toString() + " ";
      }
      return oneString;
    //Returns a string representation of all cards in myCards[]
   }
   
   
   //Accessors
   public int getNumCards()
   {
      return numCards;
   }
   
   //Returns card at position k
   //if position k does not exists returns a card with errorFlag == true
   //Amy
   public Card inspectCard(int k)
   {
      if (k >= 0 && k < numCards && k < MAX_CARDS) //Is k valid?
      {
         if (myCards[k] != null) //If a card exists return a copy
         {
            Card copy = new Card(myCards[k].getValue(), myCards[k].getSuit());
            return copy;
         }
         else
         {
            Card errorCard = new Card('X', Card.Suit.SPADES);
            return errorCard;
         }
      }
      else
      {
         Card errorCard = new Card('X', Card.Suit.SPADES);
         return errorCard;
      }
   }
   
}


class Deck
{
   
   public static final int MAX_PACKS = 6;

   
   private static Card [] masterPack;
   private static Card[] cards;
   private int topCard;
   
   
   //Default assumes 1 pack
   //Amy
   public Deck()
   {
      allocateMasterPack();
      init(1);
   }
   
   //Creates a deck with numPacks worth of standard decks
   //Amy
   public Deck(int numPacks)
   {
      //defaults to either MAX_PACKS or numPacks
      if (numPacks > MAX_PACKS) numPacks = MAX_PACKS;
      else if (numPacks < 1) numPacks = 1;
      allocateMasterPack();
      init(numPacks);
   }
   
   //Reset cards[] with numPacks number of decks ordered
   //Nathan
   public void init(int numPacks)
   {
      //Check & constrain numPacks
      if (numPacks > MAX_PACKS) numPacks = MAX_PACKS;
      else if (numPacks < 1) numPacks = 1;
      
      //Create a deck containing numpacks worth of cards.
      cards = new Card [52*numPacks];
      topCard = 52*numPacks;
      
      //Loop through masterPack adding pointers until cards[] is full
      for (int i = 0; i < topCard; i++)
      {
         cards[i] = masterPack[i%52];
      }
   }
   
  
   //Chris -Shuffles the deck
   public void shuffle()
   {
      Random randomGenerator = new Random();
      //Repeat shuffle loop 5 times to increase randomness
      for (int passes = 0; passes < 5; passes++)
      {
         //Loop and swap each element with a random location
         for (int i = 0; i < (topCard); i++)
         {
            int thisSwitch = randomGenerator.nextInt(topCard);
            Card temp = Deck.cards[i];
            Deck.cards[i] = Deck.cards[thisSwitch];
            Deck.cards[thisSwitch] = temp;
            //In order to shuffle every index of the deck 
            //array's contents are swapped with a random index of the deck.
         }
      }
   }
   
   //Returns the top card of the deck if it exists and removes it.
   //Amy
   public Card dealCard()
   {
         if (topCard <= 0) //If there is no card
         {
            return null;
         }
         Card temp = cards[topCard-1];
         cards[topCard-1] = null;
         topCard--;
         //Return a copy of the card
         Card copy = new Card(temp.getValue(), temp.getSuit());
         return copy;  
   }
   
   //Returns the value of topCard
   public int getTopCard()
   {
      return topCard;
   }
   
   //Returns the card at position k, 
   //if a card does not exist there return an illegal card
   //Amy
   public Card inspectCard(int k)
   {
      if (k >= 0 && k < topCard && k < cards.length) //Is k valid?
      {
         if (cards[k] != null) //does the card exist?
         {
            Card copy = new Card(cards[k].getValue(), cards[k].getSuit());
            return copy;
         }
         else
         {
            Card errorCard = new Card('X', Card.Suit.SPADES);
            return errorCard;
         }
      }
      else
      {
         Card errorCard = new Card('X', Card.Suit.SPADES);
         return errorCard;
      }
   }
   
   //If masterPack has not been populated, then populate otherwise do nothing
   //Nathan
   private static void allocateMasterPack()
   {
      //Check if masterPack has not been populated yet
      if (masterPack == null)
      {
         masterPack = new Card [52];
         for (int i = 0; i < 52; i++)
         {
            //Default to a bad value so card will throw errorFlag
            char value = 'X';
            //Rotate through all 13 card values
            switch(i%13)
            {
               case 0: value =  'A'; break;
               case 1: value =  '2'; break;
               case 2: value =  '3'; break;
               case 3: value =  '4'; break;
               case 4: value =  '5'; break;
               case 5: value =  '6'; break;
               case 6: value =  '7'; break;
               case 7: value =  '8'; break;
               case 8: value =  '9'; break;
               case 9: value =  'T'; break;
               case 10: value = 'J'; break;
               case 11: value = 'Q'; break;
               case 12: value = 'K'; break;
               default: break;
            }
            
            Card.Suit suit = Card.Suit.CLUBS; //Default to Clubs
            //Rotate through suits every 4th of a deck
            switch((i/13))
            {
               case 0: suit = Card.Suit.CLUBS; break;
               case 1: suit = Card.Suit.DIAMONDS; break;
               case 2: suit = Card.Suit.HEARTS; break;
               case 3: suit = Card.Suit.SPADES; break;
               default: break;
            }
            
            masterPack[i] = new Card(value, suit);
         }
      }
   }
   
}
/*
************************************Output**************************************

Begin Deck Test (double Deal)
Test with 2 pack.

K SPADES / Q SPADES / J SPADES / T SPADES / 9 SPADES / 
8 SPADES / 7 SPADES / 6 SPADES / 5 SPADES / 4 SPADES / 
3 SPADES / 2 SPADES / A SPADES / K HEARTS / Q HEARTS / 
J HEARTS / T HEARTS / 9 HEARTS / 8 HEARTS / 7 HEARTS / 
6 HEARTS / 5 HEARTS / 4 HEARTS / 3 HEARTS / 2 HEARTS / 
A HEARTS / K DIAMONDS / Q DIAMONDS / J DIAMONDS / T DIAMONDS / 
9 DIAMONDS / 8 DIAMONDS / 7 DIAMONDS / 6 DIAMONDS / 5 DIAMONDS / 
4 DIAMONDS / 3 DIAMONDS / 2 DIAMONDS / A DIAMONDS / K CLUBS / 
Q CLUBS / J CLUBS / T CLUBS / 9 CLUBS / 8 CLUBS / 
7 CLUBS / 6 CLUBS / 5 CLUBS / 4 CLUBS / 3 CLUBS / 
2 CLUBS / A CLUBS / K SPADES / Q SPADES / J SPADES / 
T SPADES / 9 SPADES / 8 SPADES / 7 SPADES / 6 SPADES / 
5 SPADES / 4 SPADES / 3 SPADES / 2 SPADES / A SPADES / 
K HEARTS / Q HEARTS / J HEARTS / T HEARTS / 9 HEARTS / 
8 HEARTS / 7 HEARTS / 6 HEARTS / 5 HEARTS / 4 HEARTS / 
3 HEARTS / 2 HEARTS / A HEARTS / K DIAMONDS / Q DIAMONDS / 
J DIAMONDS / T DIAMONDS / 9 DIAMONDS / 8 DIAMONDS / 7 DIAMONDS / 
6 DIAMONDS / 5 DIAMONDS / 4 DIAMONDS / 3 DIAMONDS / 2 DIAMONDS / 
A DIAMONDS / K CLUBS / Q CLUBS / J CLUBS / T CLUBS / 
9 CLUBS / 8 CLUBS / 7 CLUBS / 6 CLUBS / 5 CLUBS / 
4 CLUBS / 3 CLUBS / 2 CLUBS / A CLUBS / 

Now with shuffle:
5 SPADES / K CLUBS / 3 HEARTS / Q CLUBS / J DIAMONDS / 
6 HEARTS / 4 CLUBS / T CLUBS / A SPADES / T SPADES / 
2 CLUBS / K DIAMONDS / 9 CLUBS / 8 DIAMONDS / Q DIAMONDS / 
9 SPADES / 5 SPADES / T SPADES / K DIAMONDS / 5 DIAMONDS / 
4 CLUBS / 6 CLUBS / 6 DIAMONDS / 2 SPADES / K SPADES / 
3 SPADES / 5 CLUBS / 9 HEARTS / A SPADES / T DIAMONDS / 
9 HEARTS / Q HEARTS / 5 DIAMONDS / A HEARTS / 2 SPADES / 
4 SPADES / Q DIAMONDS / A CLUBS / K SPADES / A HEARTS / 
6 CLUBS / 2 HEARTS / 6 SPADES / 5 CLUBS / J DIAMONDS / 
4 DIAMONDS / 3 CLUBS / J HEARTS / 2 DIAMONDS / 9 DIAMONDS / 
J CLUBS / T HEARTS / 6 SPADES / 6 DIAMONDS / 4 HEARTS / 
7 HEARTS / 7 SPADES / A DIAMONDS / 7 DIAMONDS / T CLUBS / 
8 DIAMONDS / 4 SPADES / 3 SPADES / 5 HEARTS / 2 DIAMONDS / 
4 DIAMONDS / Q SPADES / 7 SPADES / K HEARTS / 9 DIAMONDS / 
8 CLUBS / A CLUBS / 3 DIAMONDS / 6 HEARTS / J CLUBS / 
2 HEARTS / 7 HEARTS / T DIAMONDS / J SPADES / 8 HEARTS / 
Q CLUBS / 3 DIAMONDS / 8 CLUBS / 7 CLUBS / 3 CLUBS / 
8 SPADES / A DIAMONDS / K CLUBS / 9 SPADES / 5 HEARTS / 
4 HEARTS / 9 CLUBS / J HEARTS / K HEARTS / 8 SPADES / 
3 HEARTS / J SPADES / Q HEARTS / 8 HEARTS / T HEARTS / 
7 CLUBS / 7 DIAMONDS / Q SPADES / 2 CLUBS / 



Test with 1 pack.


K SPADES / Q SPADES / J SPADES / T SPADES / 9 SPADES / 
8 SPADES / 7 SPADES / 6 SPADES / 5 SPADES / 4 SPADES / 
3 SPADES / 2 SPADES / A SPADES / K HEARTS / Q HEARTS / 
J HEARTS / T HEARTS / 9 HEARTS / 8 HEARTS / 7 HEARTS / 
6 HEARTS / 5 HEARTS / 4 HEARTS / 3 HEARTS / 2 HEARTS / 
A HEARTS / K DIAMONDS / Q DIAMONDS / J DIAMONDS / T DIAMONDS / 
9 DIAMONDS / 8 DIAMONDS / 7 DIAMONDS / 6 DIAMONDS / 5 DIAMONDS / 
4 DIAMONDS / 3 DIAMONDS / 2 DIAMONDS / A DIAMONDS / K CLUBS / 
Q CLUBS / J CLUBS / T CLUBS / 9 CLUBS / 8 CLUBS / 
7 CLUBS / 6 CLUBS / 5 CLUBS / 4 CLUBS / 3 CLUBS / 
2 CLUBS / A CLUBS / 

Now with shuffle:
K DIAMONDS / J SPADES / K SPADES / Q HEARTS / T HEARTS / 
3 HEARTS / 5 SPADES / A HEARTS / 4 HEARTS / 8 CLUBS / 
4 DIAMONDS / Q SPADES / J CLUBS / 7 DIAMONDS / 6 HEARTS / 
3 SPADES / A DIAMONDS / 5 CLUBS / 2 SPADES / T CLUBS / 
A CLUBS / 9 SPADES / 2 DIAMONDS / 5 DIAMONDS / J DIAMONDS / 
Q CLUBS / 3 DIAMONDS / T DIAMONDS / 6 DIAMONDS / 7 CLUBS / 
9 CLUBS / Q DIAMONDS / 7 HEARTS / 4 SPADES / 9 HEARTS / 
4 CLUBS / T SPADES / 7 SPADES / A SPADES / K CLUBS / 
8 DIAMONDS / 8 SPADES / 2 HEARTS / K HEARTS / 9 DIAMONDS / 
J HEARTS / 3 CLUBS / 8 HEARTS / 2 CLUBS / 6 CLUBS / 
5 HEARTS / 6 SPADES / 


End of Phase 3 Test


How many hands? (1-10, please): 
22
Invalid input, retry - How many hands? (1-10, please): 
-10
Invalid input, retry - How many hands? (1-10, please): 
g
Invalid input, retry - How many hands? (1-10, please): 
7
Unshuffled hands: 
 Player 1 = ( K SPADES 6 SPADES Q HEARTS 5 HEARTS J DIAMONDS 4 DIAMONDS T CLUBS 3 CLUBS )
 Player 2 = ( Q SPADES 5 SPADES J HEARTS 4 HEARTS T DIAMONDS 3 DIAMONDS 9 CLUBS 2 CLUBS )
 Player 3 = ( J SPADES 4 SPADES T HEARTS 3 HEARTS 9 DIAMONDS 2 DIAMONDS 8 CLUBS A CLUBS )
 Player 4 = ( T SPADES 3 SPADES 9 HEARTS 2 HEARTS 8 DIAMONDS A DIAMONDS 7 CLUBS )
 Player 5 = ( 9 SPADES 2 SPADES 8 HEARTS A HEARTS 7 DIAMONDS K CLUBS 6 CLUBS )
 Player 6 = ( 8 SPADES A SPADES 7 HEARTS K DIAMONDS 6 DIAMONDS Q CLUBS 5 CLUBS )
 Player 7 = ( 7 SPADES K HEARTS 6 HEARTS Q DIAMONDS 5 DIAMONDS J CLUBS 4 CLUBS )
Shuffled hands: 
 Player 1 = ( 2 DIAMONDS T CLUBS 3 CLUBS 5 CLUBS Q HEARTS T SPADES A DIAMONDS J CLUBS )
 Player 2 = ( 8 DIAMONDS 3 HEARTS J DIAMONDS 9 CLUBS 6 HEARTS 2 SPADES K HEARTS 2 HEARTS )
 Player 3 = ( K DIAMONDS J HEARTS 3 DIAMONDS A CLUBS 6 DIAMONDS K SPADES J SPADES 3 SPADES )
 Player 4 = ( A SPADES 6 SPADES Q SPADES 5 SPADES T DIAMONDS 4 HEARTS 9 DIAMONDS )
 Player 5 = ( Q DIAMONDS 5 DIAMONDS 9 SPADES 4 DIAMONDS K CLUBS A HEARTS 8 CLUBS )
 Player 6 = ( 8 SPADES 7 SPADES 4 SPADES 7 HEARTS 6 CLUBS T HEARTS 7 DIAMONDS )
 Player 7 = ( 9 HEARTS 4 CLUBS 2 CLUBS 8 HEARTS Q CLUBS 7 CLUBS 5 HEARTS )
Thanks for playing!


********************************************************************************
*/









