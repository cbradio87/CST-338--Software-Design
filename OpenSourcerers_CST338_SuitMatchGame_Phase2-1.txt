
/*
Open Sourcerers
Nathan Jobes, Amy Gonzales, Chris Bray, Jerome Ortega
CST 338 Week 5
Assignment 5: Suit Match
3/31/2021
*/



import javax.swing.*;
import javax.swing.border.*;

import java.awt.*;
import java.util.Random;
import javax.swing.border.*;

public class Assig5
{
   /**
    * 52 + 4 jokers + 1 back-of-card image
    */
   static final int NUM_CARD_IMAGES = 57;
   static int NUM_CARDS_PER_HAND = 7;
   static int NUM_PLAYERS = 2;
   static JLabel[] computerLabels = new JLabel[NUM_CARDS_PER_HAND];
   static JLabel[] humanLabels = new JLabel[NUM_CARDS_PER_HAND];
   static JLabel[] playedCardLabels = new JLabel[NUM_PLAYERS];
   static JLabel[] playLabelText = new JLabel[NUM_PLAYERS];

   /**
    * Array that holds all the image icons of the cards.
    */
   static Icon[] icon = new ImageIcon[NUM_CARD_IMAGES];

   /**
    * Creates the card icons and loads them into the icon array.
    */
   static void loadCardIcons()
   {
      String pathToImagesFolder = "images/";

      for (int i = 0; i < NUM_CARD_IMAGES - 1; i++)
      {
         // Construct the path using helper methods.
         String cardImagePath = 
               pathToImagesFolder.concat(turnIntIntoCardValue(i % 14))
               .concat(turnIntIntoCardSuit(i / 14)).concat(".gif");

         // Add image to icon array.
         icon[i] = new ImageIcon(cardImagePath);
      }

      // Manually add in the image for the back of the cards.
      icon[NUM_CARD_IMAGES - 1] = new ImageIcon(pathToImagesFolder + "BK.gif");
   }

   /**
    * Takes an integer, converts to card value.
    * 
    * @param int k
    * @return String cardValue
    */
   static String turnIntIntoCardValue(int k)
   {
      // Use String as an array of Char
      String charValue = "A23456789TJQKX";
      String result = "";
      if (k < charValue.length())
         result = charValue.substring(k, k + 1);
      return result;
   }

   /**
    * Takes an integer, converts to card suit.
    * 
    * @param int j
    * @return String suit
    */
   static String turnIntIntoCardSuit(int j)
   {
      // Use String as an array of Char
      String suits = "CDHS";
      String result = "";
      if (j < suits.length())
         result = suits.substring(j, j + 1);
      return result;
   }

   public static void main(String[] args)
   {

      Icon tempIcon;

      // establish main frame in which program will run
      CardTable myCardTable = 
            new CardTable("CardTable", NUM_CARDS_PER_HAND, NUM_PLAYERS);
      
      myCardTable.setSize(800, 600);
      myCardTable.setLocationRelativeTo(null);
      myCardTable.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

      // show everything to the user
      myCardTable.setVisible(true);

      // CREATE LABELS ----------------------------------------------------
      playLabelText[0] = new JLabel("Computer", JLabel.CENTER);
      playLabelText[1] = new JLabel("You", JLabel.CENTER);

      // creates computer hand array of back card icons
      for (int k = 0; k < NUM_CARDS_PER_HAND; k++)
      {
         // creates computer hand array of back card icons
         computerLabels[k] = new JLabel(GUICard.getBackCardIcon());
         // creates human hand array
         tempIcon = GUICard.getIcon(randomCardGen()); // is this correct?
         humanLabels[k] = new JLabel(tempIcon);
      }

      // ADD LABELS TO PANELS -----------------------------------------
      for (int k = 0; k < NUM_CARDS_PER_HAND; k++)
      {
         // add indexed label to computer panel
         myCardTable.pnlComputerHand.add(computerLabels[k]);
         // add indexed labels to Human panel
         myCardTable.pnlHumanHand.add(humanLabels[k]);
      }

      // and two random cards in the play region (simulating a computer/hum ply)
      tempIcon = GUICard.getIcon(randomCardGen());
      // assigning labels to played cards
      playedCardLabels[0] = new JLabel(tempIcon); 
      // playedCardLabels[0].setVisible(false);
      tempIcon = GUICard.getIcon(randomCardGen());
      playedCardLabels[1] = new JLabel(tempIcon);
      // playedCardLabels[1].setVisible(false);

      // add cards and text to play area
      myCardTable.pnlPlayArea.add(playedCardLabels[0]);
      myCardTable.pnlPlayArea.add(playedCardLabels[1]);
      myCardTable.pnlPlayArea.add(playLabelText[0]);
      myCardTable.pnlPlayArea.add(playLabelText[1]);

      // show everything to the user
      myCardTable.pack();
      myCardTable.setVisible(true);
   }

   static Card randomCardGen()
   {
      // Picks a random card for testing purposes
      Deck deck = new Deck();
      Random randomGen = new Random();
      return deck.inspectCard(randomGen.nextInt(deck.getNumCards()));
   }

}

//-----------------------------------------------------------------------------
class CardTable extends JFrame
{
   static int MAX_CARDS_PER_HAND = 56;
   static int MAX_PLAYERS = 2; // for now, we only allow 2 person games

   private int numCardsPerHand;
   private int numPlayers;

   public JPanel pnlComputerHand, pnlHumanHand, pnlPlayArea;

   CardTable(String title, int numCardsPerHand, int numPlayers)
   {
      this.setMinimumSize(new Dimension(400, 700));
      if (title.length() == 0)
         title = "Default";

      if (numCardsPerHand <= 0)
         numCardsPerHand = 1;
      else if (numCardsPerHand > MAX_CARDS_PER_HAND)
         numCardsPerHand = MAX_CARDS_PER_HAND;

      if (numPlayers <= 0)
         numPlayers = 1;
      else if (numPlayers > MAX_PLAYERS)
         numPlayers = MAX_PLAYERS;

      GridLayout panelLayout = new GridLayout(3, 1);
      this.setLayout(panelLayout);
      FlowLayout handLayout = new FlowLayout(FlowLayout.CENTER, 5, 5);
      // handLayout.setVgap(0);

      pnlComputerHand = new JPanel(handLayout);
      TitledBorder compBorder = BorderFactory.createTitledBorder("Computer Hand");
      pnlComputerHand.setBorder(compBorder);
      // pnlComputerHand.setMaximumSize(new Dimension(800, 10));

      pnlHumanHand = new JPanel(handLayout);
      TitledBorder playerBorder = BorderFactory.createTitledBorder("Your Hand");
      pnlHumanHand.setBorder(playerBorder);

      pnlPlayArea = new JPanel(new GridLayout(2, 2));
      TitledBorder playAreaBorder = BorderFactory.createTitledBorder("Playing Area");
      pnlPlayArea.setBorder(playAreaBorder);

      this.setTitle(title);
      this.add(pnlComputerHand);
      this.add(pnlPlayArea);
      this.add(pnlHumanHand);

   }

   // accessors for instance members
   public int getnumCardsPerHand()
   {
      return numCardsPerHand;
   }

   //
   public int getnumPlayers()
   {
      return numPlayers;
   }

}

//-----------------------------------------------------------------------------

/*
 * Chris- static void loadCardIcons() - Store the Icons in a 2-D array. Don't
 * require the client to call this method.
 */
class GUICard
{
   private static Icon[][] iconCards = new ImageIcon[14][4];
   private static Icon iconBack;
   static boolean iconsLoaded = false;

   static final int NUM_CARD_IMAGES = 57;
   static final int CARD_NUMBER = 14;

   static void loadCardIcons()
   {
      if (iconBack == null)
      {
         String pathToImagesFolder = "images/";

         for (int j = 0; j < 4; j++)
         {
            for (int i = 0; i < 14; i++)
            {
               // Construct the path using helper methods.
               String cardImagePath = 
                     pathToImagesFolder.concat(turnIntIntoCardValue(i % 14))
                     .concat(turnIntIntoCardSuit(j)).concat(".gif");

               // Add image to icon array.
               iconCards[i][j] = new ImageIcon(cardImagePath);
            }
         }
         // Manually add in the image for the back of the cards.
         iconBack = new ImageIcon(pathToImagesFolder + "BK.gif");

      }
   }

   /*
    * -Chris This method takes a Card object from the client, and returns the Icon
    * for that card.
    */
   static public Icon getIcon(Card card)
   {
      return iconCards[turnCardValueIntoInt(card.getValue())][turnSuitIntoInt(card.getSuit())];
   }

   public static Icon getBackCardIcon()
   {
      loadCardIcons();

      return iconBack;
   }

   private static int turnCardValueIntoInt(char value)
   {
      String charValue = "A23456789TJQKX";

      int result = charValue.indexOf(value);
      return result;

   }

   private static int turnSuitIntoInt(Card.Suit suit)
   {
      int result = 3;

      if (suit == Card.Suit.CLUBS)
         result = 0;
      else if (suit == Card.Suit.DIAMONDS)
         result = 1;
      else if (suit == Card.Suit.HEARTS)
         result = 2;

      return result;
   }

   private static String turnIntIntoCardValue(int k)
   {
      //Use String as Char array
      String charValue = "A23456789TJQKX";
      String result = "";
      if (k < charValue.length()) result = charValue.substring(k, k+1);
      return result;
   }

   /**
    * Takes an integer, converts to card suit.
    * 
    * @param int j
    * @return String suit
    */
   private static String turnIntIntoCardSuit(int j)
   {
      //Use String as Char array
      String suits = "CDHS";
      String result = "";
      if (j < suits.length()) result = suits.substring(j, j+1);
      return result;
   }

}

//------------------------------------------------------------------------------

class Card
{
   // Chris - added suit for joker to be used in 2d arrays.
   public enum Suit
   {
      CLUBS, DIAMONDS, HEARTS, SPADES
   }

   private char value;
   private Suit suit;
   private boolean errorFlag;

   public static char[] valueRanks =
   { '2', '3', '4', '5', '6', '7', '8', '9', 'T', 'J', 'Q', 'K', 'A', 'X' };

   // Default
   // Amy
   public Card()
   {
      char defaultValue = 'A';
      Suit defaultSuit = Suit.SPADES;
      set(defaultValue, defaultSuit);
   }

   // Overload
   // Amy
   public Card(char value, Suit suit)
   {
      set(value, suit);
   }

   // Chris -Returns a string representation of the Card,
   // returns invalid if (errorFlag)
   public String toString()
   {
      if (errorFlag == true)
      {
         return "[invalid]";
      }
      return (value + " " + suit);
   }

   // Chris -Sets the card's representation and
   // modifies errorFlag based on inputs
   public boolean set(char value, Suit suit)
   {
      if (isValid(value, suit))
      {
         this.value = value;
         this.suit = suit;
         errorFlag = false;

      } else
      {
         errorFlag = true;

      }
      return !errorFlag;
   }

   // Accessors
   public Suit getSuit()
   {
      return suit;
   }

   public char getValue()
   {
      return value;
   }

   // Nathan - Returns value of error Flag
   public boolean getErrorFlag()
   {
      return errorFlag;
   }

   // Returns true if the cards are the same value and suit
   // Nathan
   public boolean equals(Card card)
   {
      // If all instance variables are equal return true, otherwise false
      if (value == card.getValue() && suit == card.getSuit() 
            && errorFlag == card.getErrorFlag())
         return true;
      else
         return false;
   }

   // Returns true if entered value is a real card value -
   // Suit should always be valid
   // Amy

   private boolean isValid(char value, Suit suit)
   {
      // checks each character in string for bad value
      String charValue = "23456789TJQKAX";
      if (charValue.indexOf(value) != -1)
         return true;
      else
         return false;
   }

   /*
    * Chris- static void arraySort(Card[], int arraySize) - will sort the incoming
    * array of cards using a bubble sort routine. You can break this up into
    * smaller methods if it gets over 20 lines or so.
    */

   static void arraySort(Card[] cards, int arraySize)
   {
      for (int j = 0; j < arraySize; j++)
      {
         for (int i = 0; i < arraySize - 1; i++)
         {
            if (compareCards(cards[i], cards[i + 1]))
            {
               Card temp = cards[i];
               cards[i] = cards[i + 1];
               cards[i + 1] = temp;
            }
         }
      }
   }

   /**
    * Returns true if card1 is greater than card2
    * 
    * @param card
    * @return isGreater
    */
   private static boolean compareCards(Card card1, Card card2)
   {
      boolean isGreater = true;
      int otherValue = 0;
      int myValue = 0;

      for (int i = 0; i < valueRanks.length; i++)
      {
         if (card1.value == valueRanks[i])
            myValue = i;
         if (card2.getValue() == valueRanks[i])
            otherValue = i;
      }

      if (myValue < otherValue)
         isGreater = false;
      return isGreater;

   }

}

//-----------------------------------------------------------------------------

class Hand
{

   public static final int MAX_CARDS = 56; // one deck

   private Card[] myCards;
   private int numCards;

   // Default
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

   // Chris - Adds a copy of a card to the hand
   public boolean takeCard(Card card)
   {
      if (numCards < MAX_CARDS)
      {
         myCards[numCards] = new Card(card.getValue(), card.getSuit());
         numCards++;
         return true;
      } else
         return false;
   }

   // Returns and removes the card at the tail of myCards[]
   // Nathan
   public Card playCard()
   {
      if (numCards > 0 && myCards[numCards - 1] != null) // If we have a card
      {
         Card playedCard = myCards[numCards - 1]; // get card to play
         myCards[numCards - 1] = null; // remove from myCards[]
         numCards--; // adjust numCards

         // Because hand contains its own unique references which were removed
         // a copy isn't needed to be returned
         return playedCard;
      } else // return an invalid card
      {
         Card errorCard = new Card('M', Card.Suit.SPADES);
         return errorCard;
      }
   }

   public Card playCard(int cardIndex)
   {
      if (numCards == 0) // error
      {
         // Creates a card that does not work
         return new Card('M', Card.Suit.SPADES);
      }
      // Decreases numCards.
      Card card = myCards[cardIndex];

      numCards--;
      for (int i = cardIndex; i < numCards; i++)
      {
         myCards[i] = myCards[i + 1];
      }

      myCards[numCards] = null;

      return card;
   }

   public String toString()
   {
      String oneString = "";
      for (int i = 0; i < numCards; i++)
      {
         oneString = oneString + myCards[i].toString() + " ";
      }
      return oneString;
      // Returns a string representation of all cards in myCards[]
   }

   // Accessors
   public int getNumCards()
   {
      return numCards;
   }

   // Returns card at position k
   // if position k does not exists returns a card with errorFlag == true
   // Amy
   public Card inspectCard(int k)
   {
      if (k >= 0 && k < numCards && k < MAX_CARDS) // Is k valid?
      {
         if (myCards[k] != null) // If a card exists return a copy
         {
            Card copy = new Card(myCards[k].getValue(), myCards[k].getSuit());
            return copy;
         } else
         {
            Card errorCard = new Card('Z', Card.Suit.SPADES);
            return errorCard;
         }
      } else
      {
         Card errorCard = new Card('Z', Card.Suit.SPADES);
         return errorCard;
      }
   }

}

//------------------------------------------------------------------------------

class Deck
{

   public static final int MAX_PACKS = 6;

   private static Card[] masterPack;
   private static Card[] cards;
   private int topCard;

   // Default assumes 1 pack
   // Amy
   public Deck()
   {
      allocateMasterPack();
      init(1);
   }

   // Creates a deck with numPacks worth of standard decks
   // Amy
   public Deck(int numPacks)
   {

      // defaults to either MAX_PACKS or numPacks
      if (numPacks > MAX_PACKS)
         numPacks = MAX_PACKS;
      else if (numPacks < 1)
         numPacks = 1;
      allocateMasterPack();
      init(numPacks);
   }

   // Reset cards[] with numPacks number of decks ordered
   // Nathan
   public void init(int numPacks)
   {
      // Check & constrain numPacks
      if (numPacks > MAX_PACKS)
         numPacks = MAX_PACKS;
      else if (numPacks < 1)
         numPacks = 1;

      // Create a deck containing numpacks worth of cards.
      cards = new Card[56 * numPacks];
      topCard = 56 * numPacks;

      // Loop through masterPack adding pointers until cards[] is full
      for (int i = 0; i < topCard; i++)
      {
         cards[i] = masterPack[i % 52];
      }
   }

   // Chris -Shuffles the deck
   public void shuffle()
   {
      Random randomGenerator = new Random();
      // Repeat shuffle loop 5 times to increase randomness
      for (int passes = 0; passes < 5; passes++)
      {
         // Loop and swap each element with a random location
         for (int i = 0; i < (topCard); i++)
         {
            int thisSwitch = randomGenerator.nextInt(topCard);
            Card temp = Deck.cards[i];
            Deck.cards[i] = Deck.cards[thisSwitch];
            Deck.cards[thisSwitch] = temp;
            // In order to shuffle every index of the deck
            // array's contents are swapped with a random index of the deck.
         }
      }
   }

   // Returns the top card of the deck if it exists and removes it.
   // Amy
   public Card dealCard()
   {
      if (topCard <= 0) // If there is no card
      {
         return null;
      }
      Card temp = cards[topCard - 1];
      cards[topCard - 1] = null;
      topCard--;
      // Return a copy of the card
      Card copy = new Card(temp.getValue(), temp.getSuit());
      return copy;
   }

   // Adds a card to deck while making sure there aren't too many instances
   // Returns false if there are too many
   public boolean addCard(Card card)
   {
      int cardInstances = 0;
      for (int i = 0; i < topCard; i++)
      {
         if (card.equals(cards[i]))
         {
            cardInstances++;
         }
         if (cardInstances >= MAX_PACKS)
         {
            return false;
         } else
         {
            cards[topCard] = card;
            topCard++;
         }
      }
      return true;

   }

   /**
    * If the deck has a copy of the given card, it removes it and slides the cards
    * above it down a place and returns true, if the card is not in the deck,
    * returns false
    * 
    * @param card
    * @return success
    */
   public boolean removeCard(Card card)
   {
      int cardIndex = deckContains(card);
      if (cardIndex != -1)
      {
         cards[cardIndex] = null;

         // Shift all card above down a slot

         for (int i = cardIndex; i < topCard; i++)
         {
            cards[i] = cards[i + 1];
         }

         topCard--;
         return true;
      } else
         return false;
   }

   /**
    * Returns the position of the first card from the bottom of the deck that
    * matches the input card, returns -1 otherwise.
    * 
    * @param card
    * @return position
    */
   private int deckContains(Card card)
   {
      if (card != null)
         for (int i = 0; i < topCard; i++)
         {
            if (cards[i] != null)
               if (card.equals(cards[i]))
                  return i;
         }

      return -1;
   }

   /**
    * returns the field representing the number of cards in the deck
    * 
    * @return topCard
    */
   public int getNumCards()
   {
      return topCard;
   }

   // Returns the value of topCard
   public int getTopCard()
   {
      return topCard;
   }

   // Returns the card at position k,
   // if a card does not exist there return an illegal card
   // Amy
   public Card inspectCard(int k)
   {
      if (k >= 0 && k < topCard && k < cards.length) // Is k valid?
      {
         if (cards[k] != null) // does the card exist?
         {
            Card copy = new Card(cards[k].getValue(), cards[k].getSuit());
            return copy;
         } else
         {
            Card errorCard = new Card('Z', Card.Suit.SPADES);
            return errorCard;
         }
      } else
      {
         Card errorCard = new Card('Z', Card.Suit.SPADES);
         return errorCard;
      }
   }

   // If masterPack has not been populated, then populate otherwise do nothing
   // Nathan
   private static void allocateMasterPack()
   {
      // Check if masterPack has not been populated yet
      if (masterPack == null)
      {
         masterPack = new Card[54];
         for (int i = 0; i < 54; i++)
         {
            // Default to a bad value so card will throw errorFlag
            char value = 'Z';
            // Rotate through all 13 card values
            switch (i % 13)
            {
            case 0:
               value = 'A';
               break;
            case 1:
               value = '2';
               break;
            case 2:
               value = '3';
               break;
            case 3:
               value = '4';
               break;
            case 4:
               value = '5';
               break;
            case 5:
               value = '6';
               break;
            case 6:
               value = '7';
               break;
            case 7:
               value = '8';
               break;
            case 8:
               value = '9';
               break;
            case 9:
               value = 'T';
               break;
            case 10:
               value = 'J';
               break;
            case 11:
               value = 'Q';
               break;
            case 12:
               value = 'K';
               break;
            default:
               break;
            }

            Card.Suit suit = Card.Suit.CLUBS; // Default to Clubs
            // Rotate through suits every 4th of a deck
            switch ((i / 13))
            {
            case 0:
               suit = Card.Suit.CLUBS;
               break;
            case 1:
               suit = Card.Suit.DIAMONDS;
               break;
            case 2:
               suit = Card.Suit.HEARTS;
               break;
            case 3:
               suit = Card.Suit.SPADES;
               break;
            default:
               break;
            }

            masterPack[i] = new Card(value, suit);
         }
      }
      /*
       * CHris - void sort() - put all of the cards in the deck back into the right
       * order according to their values.
       */

   }

   void sort()
   {
      Card.arraySort(cards, cards.length);
   }
}

