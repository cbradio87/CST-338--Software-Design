
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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

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
   
   private static CardTable myCardTable;
   private static CardGameOutline suitMatchGame;
   private static int playerTurn;
   private static int playerRound;
   private static boolean waitForConfirm = false;
   private static int[] scores = new int[NUM_PLAYERS];
   private static Card[] playAreaCards = new Card[NUM_PLAYERS];

   public static void main(String[] args)
   {
      int numPacksPerDeck = 1;
      int numJokersPerPack = 2;
      int numUnusedCardsPerPack = 0;
      Card[] unusedCardsPerPack = null;

      // creates object
      suitMatchGame = new CardGameOutline(numPacksPerDeck, numJokersPerPack,
            numUnusedCardsPerPack, unusedCardsPerPack,
            NUM_PLAYERS, NUM_CARDS_PER_HAND);

      // establish main frame in which program will run
      myCardTable = new CardTable("CardTable", NUM_CARDS_PER_HAND, NUM_PLAYERS);
      myCardTable.setSize(800, 600);
      myCardTable.setLocationRelativeTo(null);
      myCardTable.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      
      //Initialize the game
      resetGame();

      // show everything to the user
      updatePlayAreaText();
      myCardTable.pack();
      myCardTable.setVisible(true);

      if (playerTurn == 0 && !waitForConfirm)
      {
         executeComputerTurn();
      }
   }

   static Card randomCardGen()
   {
      Deck deck = new Deck();
      Random randomGen = new Random();
      return deck.inspectCard(randomGen.nextInt(deck.getNumCards()));
   }

   public static void updatePlayAreaText()
   {
      //If game is not in-between rounds 
      if (!waitForConfirm)
      {
         //Remove turn/round labels in the panel
         myCardTable.pnlPlayArea.remove(3);
         myCardTable.pnlPlayArea.remove(2);

         //Re-determine turn/round labels
         String computerLabel = "PC: "+ scores[0] +"pts";
         String playerLabel = "You: "+ scores[1] +"pts";
         if (playerRound == 0)
         {
            computerLabel = "PC's Round: "+ scores[0] +"pts";
            if (playerTurn == 1)
               playerLabel = "Your Turn: "+ scores[1] +"pts";
         } else
         {
            playerLabel = "Your Round: "+ scores[1] +"pts";
            if (playerTurn == 0)
               computerLabel = "PC's Turn "+ scores[0] +"pts";
         }

         //Build the labels for the playArea
         playLabelText[0] = new JLabel(computerLabel, JLabel.CENTER);
         playLabelText[1] = new JLabel(playerLabel, JLabel.CENTER);

         //Add them to the panel in their correct spots
         myCardTable.pnlPlayArea.add(playLabelText[0], 2);
         myCardTable.pnlPlayArea.add(playLabelText[1], 3);
      }

   }

   //Returns a button with the ActionListener that
   //contains the code for the Player
   private static JButton buildCardButton()
   {
      JButton button = new JButton();
      button.addActionListener(new ActionListener()
      {
         public void actionPerformed(ActionEvent evt)
         {
            //If we are not in-between rounds execute player's action
            if (!waitForConfirm && playerTurn != 0)
            {
               // Get reference to source label
               JButton me = (JButton) evt.getSource();
               JLabel meSource = (JLabel) me.getParent();

               // Find the label's position in humanLabels
               int myPos = 0;
               for (int pos = 0; pos < humanLabels.length; pos++)
               {

                  if (meSource.getX() == humanLabels[pos].getX())
                     myPos = pos;
               }

               // Have SuitMatchGame play that card
               Card played = suitMatchGame.getHand(1).playCard(myPos);
               playAreaCards[playerTurn] = played;

               // Put card in playArea for player
               myCardTable.pnlPlayArea.remove(1);
               myCardTable.pnlPlayArea.add(new JLabel(GUICard.getIcon(played)), 1);

               // Create a temp label array
               JLabel[] temp = new JLabel[humanLabels.length - 1];

               // Populate temp with all labels before the one that was removed
               for (int i = 0; i < myPos; i++)
               {
                  temp[i] = humanLabels[i];
               }
               // Add all the labels after the removed label into temp
               for (int i = myPos; i < temp.length; i++)
               {
                  temp[i] = humanLabels[i + 1];
               }

               humanLabels = temp;
               // Update the player's hand panel
               myCardTable.pnlHumanHand.remove(myPos);
               for (int k = 0; k < humanLabels.length; k++)
               {
                  // add indexed labels to Human panel
                  myCardTable.pnlHumanHand.add(humanLabels[k]);
               }

               //If the round has ended check who won
               if (playerTurn != playerRound)
               {
                  compareSuit();
               }

               //Adjust the turn state
               playerTurn++;
               playerTurn %= NUM_PLAYERS;

               if (playerRound == playerTurn)
               {
                  playerRound++;
                  playerRound %= NUM_PLAYERS;

                  playerTurn = playerRound;
               }

               updatePlayAreaText();
               myCardTable.pack();
               myCardTable.setVisible(true);
               myCardTable.repaint();

               //If it is now the PC's turn have it play
               if (playerTurn == 0 && !waitForConfirm)
               {
                  executeComputerTurn();
               }

            }
         }
      });

      //Make the button itself invisible to sit on top of the GUICard Icons
      button.setOpaque(false);
      button.setBorderPainted(true);
      button.setContentAreaFilled(false);
      return button;
   }

   //Computes the computers turn and updates the cardTable
   private static void executeComputerTurn()
   {
      // Have Computer play a random card from its hand
      Random rand = new Random();
      Hand computerHand = suitMatchGame.getHand(0);
      if (computerHand.getNumCards() > 0)
      {
         //PC plays a random card from it's hand
         Card played = computerHand.playCard(rand.nextInt(computerHand.getNumCards()));
         playAreaCards[playerTurn] = played;

         // Put card in playArea for Computer
         myCardTable.pnlPlayArea.remove(0);
         myCardTable.pnlPlayArea.add(new JLabel(GUICard.getIcon(played)), 0);

         // Create a temp label array
         JLabel[] temp = new JLabel[computerLabels.length - 1];

         // Populate temp with the new number of cardBacks for computer
         for (int i = 0; i < temp.length; i++)
         {
            temp[i] = computerLabels[i + 1];
         }

         computerLabels = temp;
         // Update the Computer's hand panel
         myCardTable.pnlComputerHand.remove(0);
         for (int k = 0; k < computerLabels.length; k++)
         {
            // add indexed label to computer panel
            myCardTable.pnlComputerHand.add(computerLabels[k]);
         }

         //If round has ended check who won
         if (playerTurn != playerRound)
         {
            compareSuit();
         }

         //adjust who's turn it is
         playerTurn++;
         playerTurn %= NUM_PLAYERS;

         if (playerRound == playerTurn)
         {
            playerRound++;
            playerRound %= NUM_PLAYERS;

            playerTurn = playerRound;
         }

         //Draw changes
         updatePlayAreaText();
         myCardTable.pack();
         myCardTable.setVisible(true);
         myCardTable.repaint();

         //If its PC turn execute
         if (playerTurn == 0 && !waitForConfirm)
         {
            executeComputerTurn();
         }

      }
   }

   //Resets the playArea card icons for new round
   private static void resetTable()
   {
      //Removes the cards currently on the table and replaces them with 
      //Card backs to indicate no card has been played
      myCardTable.pnlPlayArea.remove(0);
      myCardTable.pnlPlayArea.remove(0);
      myCardTable.pnlPlayArea.add(new JLabel(GUICard.getBackCardIcon()), 0);
      myCardTable.pnlPlayArea.add(new JLabel(GUICard.getBackCardIcon()), 0);

   }

   //Checks who won the round and calls the relevant next function
   private static void compareSuit()
   {
      //Get the cards that were played and store them based on who
      //Owns the current round
      //Note: A round ends on the player's turn who does not own the round
      Card toMatch = playAreaCards[playerRound];
      Card played = playAreaCards[playerTurn];

      //Determine who won based on matching suits and add to scores
      boolean won = false;
      if (played.getSuit() == toMatch.getSuit())
      {
         scores[playerTurn]++;
         if (playerTurn == 1)
            won = true;
      } else
      {
         if (playerRound == 1)
            won = true;
         scores[playerRound]++;
      }

      //Update to display new score
      updatePlayAreaText();
      //Call displayWinner if the game has ended, otherwise pauseMatch
      if (suitMatchGame.getHand(0).getNumCards() <= 0)
         displayWinner();
      else
         pauseMatch(won);

   }

   /*
    *Displays a button on screen with the Match result for the game  based on
    *score total and the button also serves as a way to get new hands and 
    *play again when clicked
    * */
   private static void displayWinner()
   {
      // Helpful Debug
      // if (scores[0] > scores[1]) System.out.println("Computer wins!!");
      // else System.out.println("You win!!!!!");
      boolean won = true;
      if (scores[0] > scores[1])
         won = false;

      waitForConfirm = true;
      String cont = "         Click to play again.";
      String result = "You lost the game.";
      if (won)
         result = "You won the game!";
      JButton resetButton = new JButton(result);
      resetButton.setLayout(new BorderLayout());
      resetButton.add(BorderLayout.SOUTH, new JLabel(cont));
      resetButton.addActionListener(new ActionListener()
      {

         public void actionPerformed(ActionEvent evt)
         {

            Component[] stuff = new Component[myCardTable.pnlPlayArea.getComponentCount()];
            for (int i = 0; i < myCardTable.pnlPlayArea.getComponentCount(); i++)
            {
               stuff[i] = myCardTable.pnlPlayArea.getComponent(i);
            }

            myCardTable.pnlPlayArea.removeAll();
            myCardTable.pnlPlayArea.setLayout(new GridLayout(2, 2));
            int offset = 0;
            for (int i = 0; i < 4; i++)
            {
               if (i + offset == 1)
               {
                  offset++;
               } else if (i + offset == 4)
               {
                  offset++;
               }
               myCardTable.pnlPlayArea.add((JLabel) stuff[i + offset]);
            }

            waitForConfirm = false;
            resetGame();
         }

      });

      Component[] stuff = new Component[myCardTable.pnlPlayArea.getComponentCount()];
      for (int i = 0; i < myCardTable.pnlPlayArea.getComponentCount(); i++)
      {
         stuff[i] = myCardTable.pnlPlayArea.getComponent(i);
      }

      myCardTable.pnlPlayArea.removeAll();
      myCardTable.pnlPlayArea.setLayout(new GridLayout(2, 3));
      int offset = 0;
      for (int i = 0; i < 6; i++)
      {
         if (i == 1)
         {
            myCardTable.pnlPlayArea.add(new JLabel());
            offset++;
         } else if (i == 4)
         {
            myCardTable.pnlPlayArea.add(resetButton);
            offset++;
         } else
            myCardTable.pnlPlayArea.add((JLabel) stuff[i - offset]);
      }

      // myCardTable.pnlPlayArea.add(resetButton);
      myCardTable.pack();
      myCardTable.setVisible(true);
      myCardTable.repaint();
   }

   /*
    *Pauses the game from moving to the nextRound until the user
    *Acknowledges the outcome by clicking a button that presents the round's
    *outcome
    */
   private static void pauseMatch(boolean won)
   {
      waitForConfirm = true;
      //Determine result to present
      String cont = "         Click to Continue";
      String result = "You lost this round.";
      if (won)
         result = "You won this round!";
      JButton confirmButton = new JButton(result);
      //Add continue statement below result
      confirmButton.setLayout(new BorderLayout());
      confirmButton.add(BorderLayout.SOUTH, new JLabel(cont));
      confirmButton.addActionListener(new ActionListener()
      {

         public void actionPerformed(ActionEvent evt)
         {
            //Stores the components in a temp array
            Component[] temp = new Component[myCardTable.pnlPlayArea.getComponentCount()];
            for (int i = 0; i < myCardTable.pnlPlayArea.getComponentCount(); i++)
            {
               temp[i] = myCardTable.pnlPlayArea.getComponent(i);
            }

            //Reset the play area
            myCardTable.pnlPlayArea.removeAll();
            myCardTable.pnlPlayArea.setLayout(new GridLayout(2, 2));
            
            //Add back the cardIcons and turn/round labels
            int offset = 0;
            for (int i = 0; i < 4; i++)
            {
               if (i + offset == 1)
               {
                  offset++;
               } else if (i + offset == 4)
               {
                  offset++;
               }
               myCardTable.pnlPlayArea.add((JLabel) temp[i + offset]);
            }

            waitForConfirm = false;
            resetTable();

            updatePlayAreaText();
            myCardTable.pack();
            myCardTable.repaint();
            myCardTable.setVisible(true);

            if (playerTurn == 0 && !waitForConfirm)
            {
               executeComputerTurn();
            }

         }

      });

      //Store components for grid repack
      Component[] temp = new Component[myCardTable.pnlPlayArea.getComponentCount()];
      for (int i = 0; i < myCardTable.pnlPlayArea.getComponentCount(); i++)
      {
         temp[i] = myCardTable.pnlPlayArea.getComponent(i);
      }

      //Clear panel and set new grid for button in center
      myCardTable.pnlPlayArea.removeAll();
      myCardTable.pnlPlayArea.setLayout(new GridLayout(2, 3));
      //Add the components accounting for order for button
      int offset = 0;
      for (int i = 0; i < 6; i++)
      {
         if (i == 1)
         {
            myCardTable.pnlPlayArea.add(new JLabel());
            offset++;
         } else if (i == 4)
         {
            myCardTable.pnlPlayArea.add(confirmButton);
            offset++;
         } else
            myCardTable.pnlPlayArea.add((JLabel) temp[i - offset]);
      }

      myCardTable.pack();
      myCardTable.setVisible(true);

   }

   private static void resetGame()
   {
      Random rand = new Random();
      // Computer is always 0;
      playerRound = rand.nextInt(NUM_PLAYERS);
      playerTurn = playerRound;
      
      //Reset scores
      for (int i = 0; i < scores.length; i++) 
      {
         scores[i] = 0;
      }

      waitForConfirm = false;

      suitMatchGame.newGame();

      suitMatchGame.deal();
      suitMatchGame.sortHands();

      computerLabels = new JLabel[NUM_CARDS_PER_HAND];
      humanLabels = new JLabel[NUM_CARDS_PER_HAND];
      playedCardLabels = new JLabel[NUM_PLAYERS];

      for (int k = 0; k < NUM_CARDS_PER_HAND; k++)
      {
         // creates computer hand array of back card icons
         computerLabels[k] = new JLabel(GUICard.getBackCardIcon());
         // creates human hand array
         humanLabels[k] = new JLabel(GUICard.getIcon(suitMatchGame.getHand(1).inspectCard(k)));
         humanLabels[k].setLayout(new GridBagLayout());

         JButton button = buildCardButton();
         // Build layout constraints for labels
         GridBagConstraints buttonConstraints = new GridBagConstraints();
         buttonConstraints.ipadx = 37;
         buttonConstraints.ipady = 97;
         humanLabels[k].add(button, buttonConstraints);
      }

      // ADD LABELS TO PANELS -----------------------------------------
      for (int k = 0; k < NUM_CARDS_PER_HAND; k++)
      {
         // add indexed label to computer panel
         myCardTable.pnlComputerHand.add(computerLabels[k]);
         // add indexed labels to Human panel
         myCardTable.pnlHumanHand.add(humanLabels[k]);
      }
      myCardTable.pnlPlayArea.removeAll();
      myCardTable.pnlPlayArea.add(new JLabel());
      myCardTable.pnlPlayArea.add(new JLabel());
      myCardTable.pnlPlayArea.add(new JLabel());
      myCardTable.pnlPlayArea.add(new JLabel());

      resetTable();

      updatePlayAreaText();
      myCardTable.pack();
      myCardTable.setVisible(true);
      myCardTable.repaint();

      if (playerTurn == 0 && !waitForConfirm)
      {
         executeComputerTurn();
      }

   }
}

//-----------------------------------------------------------------------------

//class CardGameOutline  ----------------------------------------------------
class CardGameOutline
{
   private static final int MAX_PLAYERS = 50;

   private int numPlayers;
   private int numPacks; // # standard 52-card packs per deck
                         // ignoring jokers or unused cards
   private int numJokersPerPack; // if 2 per pack & 3 packs per deck, get 6
   private int numUnusedCardsPerPack; // # cards removed from each pack
   private int numCardsPerHand; // # cards to deal each player
   private Deck deck; // holds the initial full deck and gets
                      // smaller (usually) during play
   private Hand[] hand; // one Hand for each player
   private Card[] unusedCardsPerPack; // an array holding the cards not used
                                      // in the game. e.g. pinochle does not
                                      // use cards 2-8 of any suit

   public CardGameOutline(int numPacks, int numJokersPerPack,
         int numUnusedCardsPerPack, Card[] unusedCardsPerPack,int numPlayers,
         int numCardsPerHand)
   {
      int k;

      // filter bad values
      if (numPacks < 1 || numPacks > 6)
         numPacks = 1;
      if (numJokersPerPack < 0 || numJokersPerPack > 4)
         numJokersPerPack = 0;
      if (numUnusedCardsPerPack < 0 || numUnusedCardsPerPack > 50) // > 1 card
         numUnusedCardsPerPack = 0;
      if (numPlayers < 1 || numPlayers > MAX_PLAYERS)
         numPlayers = 4;
      // one of many ways to assure at least one full deal to all players
      if (numCardsPerHand < 1 || numCardsPerHand > numPacks * (52 - numUnusedCardsPerPack) / numPlayers)
         numCardsPerHand = numPacks * (52 - numUnusedCardsPerPack) / numPlayers;

      // allocate
      this.unusedCardsPerPack = new Card[numUnusedCardsPerPack];
      this.hand = new Hand[numPlayers];
      for (k = 0; k < numPlayers; k++)
         this.hand[k] = new Hand();
      deck = new Deck(numPacks);

      // assign to members
      this.numPacks = numPacks;
      this.numJokersPerPack = numJokersPerPack;
      this.numUnusedCardsPerPack = numUnusedCardsPerPack;
      this.numPlayers = numPlayers;
      this.numCardsPerHand = numCardsPerHand;
      for (k = 0; k < numUnusedCardsPerPack; k++)
         this.unusedCardsPerPack[k] = unusedCardsPerPack[k];

      // prepare deck and shuffle
      newGame();
   }

   // constructor overload/default for game like bridge
   public CardGameOutline()
   {
      this(1, 0, 0, null, 4, 13);
   }

   public Hand getHand(int k)
   {
      // hands start from 0 like arrays

      // on error return automatic empty hand
      if (k < 0 || k >= numPlayers)
         return new Hand();

      return hand[k];
   }

   public Card getCardFromDeck()
   {
      return deck.dealCard();
   }

   public int getNumCardsRemainingInDeck()
   {
      return deck.getNumCards();
   }

   public void newGame()
   {
      int k, j;

      // clear the hands
      for (k = 0; k < numPlayers; k++)
         hand[k].resetHand();

      // restock the deck
      deck.init(numPacks);

      // remove unused cards
      for (k = 0; k < numUnusedCardsPerPack; k++)
         deck.removeCard(unusedCardsPerPack[k]);

      // add jokers
      for (k = 0; k < numPacks; k++)
         for (j = 0; j < numJokersPerPack; j++)
            deck.addCard(new Card('X', Card.Suit.values()[j]));

      // shuffle the cards
      deck.shuffle();
   }

   public boolean deal()
   {
      // returns false if not enough cards, but deals what it can
      int k, j;
      boolean enoughCards;

      // clear all hands
      for (j = 0; j < numPlayers; j++)
         hand[j].resetHand();

      enoughCards = true;
      for (k = 0; k < numCardsPerHand && enoughCards; k++)
      {
         for (j = 0; j < numPlayers; j++)
            if (deck.getNumCards() > 0)
               hand[j].takeCard(deck.dealCard());
            else
            {
               enoughCards = false;
               break;
            }
      }

      return enoughCards;
   }

   void sortHands()
   {
      int k;

      for (k = 0; k < numPlayers; k++)
         hand[k].sort();
   }

   Card playCard(int playerIndex, int cardIndex)
   {
      // returns bad card if either argument is bad
      if (playerIndex < 0 || playerIndex > numPlayers - 1 || cardIndex < 0 ||
            cardIndex > numCardsPerHand - 1)
      {
         // Creates a card that does not work
         return new Card('M', Card.Suit.SPADES);
      }

      // return the card played
      return hand[playerIndex].playCard(cardIndex);

   }

   boolean takeCard(int playerIndex)
   {
      // returns false if either argument is bad
      if (playerIndex < 0 || playerIndex > numPlayers - 1)
         return false;

      // Are there enough Cards?
      if (deck.getNumCards() <= 0)
         return false;

      return hand[playerIndex].takeCard(deck.dealCard());
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
      this.setMinimumSize(new Dimension(600, 500));
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

      //Set panel layouts and constraints
      GridBagLayout panelLayout = new GridBagLayout();
      this.setLayout(panelLayout);
      FlowLayout handLayout = new FlowLayout(FlowLayout.CENTER, 5, 1);

      Dimension handSize = new Dimension(600, 125);

      pnlComputerHand = new JPanel(handLayout);
      TitledBorder compBorder = BorderFactory.createTitledBorder("Computer Hand");
      pnlComputerHand.setBorder(compBorder);
      pnlComputerHand.setPreferredSize(handSize);
      GridBagConstraints c = new GridBagConstraints();
      c.fill = GridBagConstraints.HORIZONTAL;
      c.gridx = 0;
      c.gridy = 0;


      pnlHumanHand = new JPanel(handLayout);
      TitledBorder playerBorder = BorderFactory.createTitledBorder("Your Hand");
      pnlHumanHand.setBorder(playerBorder);
      pnlHumanHand.setPreferredSize(handSize);
      GridBagConstraints c2 = new GridBagConstraints();
      c2.fill = GridBagConstraints.HORIZONTAL;
      c2.gridx = 0;
      c2.gridy = 2;

      pnlPlayArea = new JPanel(new GridLayout(2, 2));
      TitledBorder playAreaBorder = BorderFactory.createTitledBorder("Playing Area");
      pnlPlayArea.setBorder(playAreaBorder);
      GridBagConstraints c3 = new GridBagConstraints();
      c3.fill = GridBagConstraints.HORIZONTAL;
      c3.gridwidth = 1;
      c3.gridx = 0;
      c3.gridy = 1;

      this.setTitle(title);
      this.add(pnlComputerHand, c);
      this.add(pnlPlayArea, c3);
      this.add(pnlHumanHand, c2);

   }

   // accessors for instance members
   public int getnumCardsPerHand()
   {
      return numCardsPerHand;
   }

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
      loadCardIcons();
      return iconCards[turnCardValueIntoInt(card.getValue())][turnSuitIntoInt(card.getSuit())];
   }

   //Returns the iconBack icon
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
   private static String turnIntIntoCardSuit(int j)
   {
      String suits = "CDHS";
      String result = "";
      if (j < suits.length())
         result = suits.substring(j, j + 1);
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
      if (value == card.getValue() && suit == card.getSuit() && errorFlag == card.getErrorFlag())
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
    * @return
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

   public void sort()
   {
      Card.arraySort(myCards, numCards);
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
      topCard = 52 * numPacks;

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

      // Count the instances of card in deck
      int cardInstances = 0;
      for (int i = 0; i < topCard; i++)
      {
         if (card.equals(cards[i]))
         {
            cardInstances++;
         }
      }

      if (cardInstances > cards.length / 56)
      {
         return false;
      } else
      {
         cards[topCard] = card;
         topCard++;
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
       * CHris - void sort() - put all of the cards in the deck back into the 
       * right order according to their values.
       * 
       */

   }

   public void sort()
   {
      Card.arraySort(cards, topCard);
   }
}
