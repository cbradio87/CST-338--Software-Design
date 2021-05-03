/*
 * Open Sourcerers
 * Nathan Jobes, Jerome Ortega, Amy Gonzales, Chris Bray
 * CST 338 Week 6
 * Build Game
 * 4/13/21
 */

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

public class Assig6
{
   public static void main(String[] args)
   {
      
      int numPacksPerDeck = 1;
      int numJokersPerPack = 2;
      int numUnusedCardsPerPack = 0;
      Card[] unusedCardsPerPack = null;
      
      BuildGameController me = new BuildGameController(numPacksPerDeck, numJokersPerPack,
            numUnusedCardsPerPack, unusedCardsPerPack,
            2, 7);
   
   }
}

//---------------------------------------------------

class BuildGameController implements ActionListener
{

   static final int NUM_CARD_IMAGES = 57; // Assig 6
   static int NUM_CARDS_PER_HAND = 7; // Assig6
   static int NUM_PLAYERS = 2; // Assig6
   static GridBagConstraints[] playAreaConstraints;

   private BuildGameView myView;
   private BuildGameModel myModel;
   private JButton confirmButton;
   private boolean difficultySet = false;
   private JButton timeButton;
   private Timer timer = new Timer();

   private ComputerPlayer PC = new ComputerPlayer(1);

   public BuildGameController(int numPacks, int numJokersPerPack, 
         int numUnusedCardsPerPack, Card[] unusedCardsPerPack,
         int numPlayers, int numCardsPerHand)
   {
      myView = new BuildGameView(NUM_CARDS_PER_HAND, NUM_PLAYERS);
      myModel = new BuildGameModel(numPacks, numJokersPerPack, 
            numUnusedCardsPerPack, unusedCardsPerPack, numPlayers,
            numCardsPerHand);

      confirmButton = new JButton();
      confirmButton.addActionListener(this);
      timeButton = new JButton();
      timeButton.addActionListener(this);

      initConstraints();
      executeComputerTurn();
      updateView();
      //timer = new Timer(this);
      timer.addActionListener(this);
      timer.run();

   }

   private static void initConstraints()
   {
      if (playAreaConstraints == null)
      {
         playAreaConstraints = new GridBagConstraints[9];
         // timer clock
         playAreaConstraints[0] = new GridBagConstraints();
         playAreaConstraints[0].gridx = 0;
         playAreaConstraints[0].gridy = 0;
         playAreaConstraints[0].gridheight = 2;
         playAreaConstraints[0].gridwidth = 1;
         playAreaConstraints[0].weightx = 1;
         playAreaConstraints[0].ipadx = 50;
         playAreaConstraints[0].ipady = 40;
         playAreaConstraints[0].anchor = GridBagConstraints.NORTHWEST;

         playAreaConstraints[1] = new GridBagConstraints();
         // timer button
         playAreaConstraints[1].gridx = 0;
         playAreaConstraints[1].gridy = 3;
         playAreaConstraints[1].weightx = 1;
         playAreaConstraints[1].gridwidth = 1;
         playAreaConstraints[1].gridheight = 1;
         playAreaConstraints[1].fill = GridBagConstraints.HORIZONTAL;
        // playAreaConstraints[1].ipadx = 20;
         playAreaConstraints[1].ipady = 20;
         //playAreaConstraints[1].anchor = GridBagConstraints.SOUTHWEST;

         playAreaConstraints[2] = new GridBagConstraints();
         // first stack
         playAreaConstraints[2].gridx = 1;
         playAreaConstraints[2].gridy = 1;
         playAreaConstraints[2].ipadx = 37;
         playAreaConstraints[2].ipady = 97;
         playAreaConstraints[2].weightx = 1;

         playAreaConstraints[3] = new GridBagConstraints();
         // second stack
         playAreaConstraints[3].gridx = 2;
         playAreaConstraints[3].gridy = 1;
         playAreaConstraints[3].ipadx = 37;
         playAreaConstraints[3].ipady = 97;
         playAreaConstraints[3].weightx = 1;

         playAreaConstraints[4] = new GridBagConstraints();
         // third stack
         playAreaConstraints[4].gridx = 3;
         playAreaConstraints[4].gridy = 1;
         playAreaConstraints[4].ipadx = 37;
         playAreaConstraints[4].ipady = 97;
         playAreaConstraints[4].weightx = 1;

         playAreaConstraints[5] = new GridBagConstraints();
         // first name tag
         playAreaConstraints[5].gridx = 1;
         playAreaConstraints[5].gridy = 0;
         playAreaConstraints[5].gridwidth = 3;
         playAreaConstraints[5].gridheight = 1;
         playAreaConstraints[5].weightx = 1;

         playAreaConstraints[6] = new GridBagConstraints();
         // second name tag
         playAreaConstraints[6].gridx = 1;
         playAreaConstraints[6].gridy = 3;
         playAreaConstraints[6].weightx = 1;
         playAreaConstraints[6].gridwidth = 3;
         playAreaConstraints[6].gridheight = 1;

         playAreaConstraints[7] = new GridBagConstraints();
         // deck
         playAreaConstraints[7].gridx = 4;
         playAreaConstraints[7].gridy = 0;
         playAreaConstraints[7].weightx = 1;
         playAreaConstraints[7].gridheight = 2;
         // playAreaConstraints[7].gridwidth = 3;

         playAreaConstraints[8] = new GridBagConstraints();
         // I cannot play button
         playAreaConstraints[8].gridx = 4;
         playAreaConstraints[8].gridy = 3;
         playAreaConstraints[8].weightx = 1;
         // playAreaConstraints[8].ipady = 50;
         playAreaConstraints[8].gridheight = 2;
         // playAreaConstraints[8].gridwidth = 3;

      }
   }

   public void actionPerformed(ActionEvent event)
   {
      Object source = event.getSource();
      if (source.getClass().equals(CardButton.class))
      {
         ifCardButtonPressed(source);
      }
      else if (source.getClass().equals(Timer.class))
      {
         Timer timer = (Timer) source;
         if (timer.getActive()) myView.updateTimer(timer.getTimeString());
      }
      else
      {
         JButton me = (JButton)source;
         if (me.getText().substring(0, 5).equals("Timer"))
         {
            timeButtonPressed();
            if (!myModel.getGameOver()) updateView();
         }
         else
         {
            resetButtonPressed();
            updateView();
         }
         
      }

   }

   private void ifCardButtonPressed(Object source)
   {
      CardButton me = (CardButton) source;
      int playedPosition = me.getPosition();
      if (me.getPlayerCard() && difficultySet)
      {
         if (myModel.getPlayerTurn() == 1)
         {
            executePlayerSelect(playedPosition);
            if (playedPosition == -1)
            {
               executePlayerTurn(playedPosition);
               if (myModel.getDeck().getNumCards() == 0) updateView();
               executeComputerTurn();
            }
            updateView();
         }
      }
      else
      {
         if (difficultySet)
         {
           // System.out.print("in else statement");
            executePlayerTurn(playedPosition);
            if (myModel.getDeck().getNumCards() == 0) updateView();
            executeComputerTurn();
            updateView();
         }
         else
         {
            if (!me.getPlayerCard() && me.getPosition() > -1)
            {
               difficultySet = true;
               PC.setDifficulty(me.getPosition());
               timeButtonPressed();
               executeComputerTurn();
               updateView();
            }
         }
      }
   }
   
   private void resetButtonPressed()
   {
         resetGame();
   }

   public void timeButtonPressed()
   {
         if (difficultySet && !myModel.getGameOver()) timer.toggleActive();
   }

   public boolean resetTimer()
   {
      this.timer.setSeconds(0);
      return true;
   }


   private JComponent[][] generateViewData()
   {
      JComponent[][] allLabels = new JComponent[3][];
      allLabels[0] = generateComputerLabels();
      allLabels[1] = generatePlayAreaLabels();
      allLabels[2] = generatePlayerLabels();

      return allLabels;
   }

   public void updateView()
   {
      myView.buildTable(generateViewData(), new GridBagLayout(), playAreaConstraints);
   }

   private JLabel[] generateComputerLabels()
   {
      Hand compHand = myModel.getHand(0);
      JLabel[] labels = new JLabel[compHand.getNumCards()];

      for (int i = 0; i < labels.length; i++)
      {
         labels[i] = new JLabel(GUICard.getBackCardIcon());
      }

      return labels;
   }

   private JComponent[] generatePlayAreaLabels()
   {

      JComponent[] cardLabels = generatePlayAreaCardLabels();
      if (!difficultySet && !myModel.getGameOver()) 
      {
         CardButton easyButton = buildCardButton(0);
         easyButton.setText("vs Randal");
         easyButton.setOpaque(true);
         easyButton.setBorderPainted(true);
         easyButton.setContentAreaFilled(true);
         cardLabels[0] = easyButton;
         
         CardButton normalButton = buildCardButton(1);
         normalButton.setText("vs Winston");
         normalButton.setOpaque(true);
         normalButton.setBorderPainted(true);
         normalButton.setContentAreaFilled(true);
         cardLabels[1] = normalButton;
         
         CardButton hardButton = buildCardButton(2);
         hardButton.setText("vs Albert");
         hardButton.setOpaque(true);
         hardButton.setBorderPainted(true);
         hardButton.setContentAreaFilled(true);
         cardLabels[2] = hardButton;

      }
      else if (myModel.getGameOver()) 
      {
         timer.stopThread();
         difficultySet = false;
         String oponent = "Winston";
         String cont = "            Click to continue";
         if (PC.getDifficulty() == 0) oponent = "Randal";
         else if(PC.getDifficulty() == 2) oponent = "Albert";
         String victory = "You beat " + oponent + "!!!";
         if (myModel.getWinner() == 0) victory = "Sorry, you lost to " + oponent;
         
         confirmButton.removeAll();
         confirmButton.setText(victory);
         confirmButton.setLayout(new BorderLayout());
         confirmButton.add(BorderLayout.SOUTH, new JLabel(cont));
         
         cardLabels[1] = confirmButton;
      }
      

      JComponent[] nameTagLabels = generatePlayAreaNameTagLabels();

      JComponent[] timerColumn = generateTimerLabels();
      JComponent[] deckColumn = generateDeckLabels();
      int totalLabels = cardLabels.length + nameTagLabels.length 
            + timerColumn.length + deckColumn.length;
      
      JComponent[] playAreaLabels = new JComponent[totalLabels];
      playAreaLabels[0] = timerColumn[0];
      playAreaLabels[1] = timerColumn[1];
      for (int i = 2; i < playAreaLabels.length - 2; i++)
      {
         if (i - 2 < cardLabels.length)
            playAreaLabels[i] = cardLabels[i - 2];
         else
            playAreaLabels[i] = nameTagLabels[i - cardLabels.length - 2];
      }
      playAreaLabels[playAreaLabels.length - 2] = deckColumn[0];
      playAreaLabels[playAreaLabels.length - 1] = deckColumn[1];
      return playAreaLabels;

   }

   private JComponent[] generatePlayAreaCardLabels()
   {
      Card[] cards = myModel.getPlayAreaCards();
      JComponent[] cardLabels = new JComponent[3];
      boolean[] playability = myModel.getPlayablePlayAreaCards();

      for (int i = 0; i < cardLabels.length; i++)
      {
         Card currCard = cards[i];
         cardLabels[i] = new JLabel(GUICard.getIcon(currCard));
         cardLabels[i].setLayout(new GridBagLayout());
         GridBagConstraints cardButtonConstraints = new GridBagConstraints();
         cardButtonConstraints.ipadx = 37;
         cardButtonConstraints.ipady = 97;
         CardButton temp = buildCardButton(i);
         temp.setEnabled(playability[i]);
         cardLabels[i].add(temp, cardButtonConstraints);
      }
      return cardLabels;
   }

   private JComponent[] generatePlayAreaNameTagLabels()
   {
      String[] nameTags = generateNameTags();
      JComponent[] nameTagLabels = new JLabel[2];

      for (int i = 0; i < nameTagLabels.length; i++)
      {
         nameTagLabels[i] = new JLabel(nameTags[i], JLabel.CENTER);
      }
      return nameTagLabels;
   }

   private JComponent[] generateTimerLabels()
   {
      JComponent[] timerLabels = new JComponent[2];
      JButton timeButton = new JButton();
      timeButton.setText("Timer: "+ timer.getTimeString());
      timeButton.addActionListener(this);
      timeButton.setLayout(new BorderLayout());
      
      String timerState = "       Locked";
      if (timer.getActive()) timerState = " Stop";
      else if (difficultySet) timerState = "  Start";
      
      timeButton.add(BorderLayout.SOUTH, new JLabel(timerState));
      
      timerLabels[0] = timeButton;

      JButton tempButton = new JButton();

      tempButton.setText("Restart");
      tempButton.setEnabled(true);
      tempButton.setOpaque(true);
      tempButton.setBorderPainted(true);
      tempButton.setContentAreaFilled(true);
      tempButton.addActionListener(this);

      timerLabels[1] = tempButton;

      return timerLabels;
   }

   private JComponent[] generateDeckLabels()
   {
      JComponent[] deckLabels = new JComponent[2];
      JLabel deck = new JLabel(Integer.toString(myModel.getDeck().getNumCards()) + " Left");
      deck.setIcon(GUICard.getBackCardIcon());
      deck.setVerticalTextPosition(JLabel.BOTTOM);
      deck.setHorizontalTextPosition(JLabel.CENTER);
      deckLabels[0] = deck;

      CardButton tempButton = buildCardButton(-1);

      tempButton.setText("Can't Play");
      tempButton.setPlayerCard(true);
      tempButton.setEnabled(true);
      tempButton.setOpaque(true);
      tempButton.setBorderPainted(true);
      tempButton.setContentAreaFilled(true);

      deckLabels[1] = tempButton;

      return deckLabels;
   }


   private JLabel[] generatePlayerLabels()
   {
      Hand playerHand = myModel.getHand(1);
      JLabel[] labels = new JLabel[playerHand.getNumCards()];

      for (int i = 0; i < labels.length; i++)
      {
         Card currCard = playerHand.inspectCard(i);
         labels[i] = new JLabel(GUICard.getIcon(currCard));
         labels[i].setLayout(new GridBagLayout());
         GridBagConstraints cardButtonConstraints = new GridBagConstraints();
         cardButtonConstraints.ipadx = 37;
         cardButtonConstraints.ipady = 97;
         CardButton temp = buildCardButton(i);
         temp.setPlayerCard(true);
         temp.setEnabled(true);
         labels[i].add(temp, cardButtonConstraints);
      }

      return labels;

   }

   private String[] generateNameTags()
   {

      int[] scores = myModel.getScores();
      // int playerRound = myModel.getPlayerRound();
      int playerTurn = myModel.getPlayerTurn();
      // Re-determine turn/round labels
      String computerName = "Randal";
      if (PC.getDifficulty() == 1) computerName = "Winston";
      else if (PC.getDifficulty() ==2) computerName = "Albert";
      
      
      String computerLabel = computerName + ": " + scores[0] + "pts";
      String playerLabel = "You: " + scores[1] + "pts";
      if (playerTurn == 0)
      {
         computerLabel = computerName + "'s Turn: " + scores[0] + "pts";
      } else
      {
         playerLabel = "Your Turn: " + scores[1] + "pts";
      }
      String[] nameTags =
      { computerLabel, playerLabel };
      return nameTags;
   }

   private CardButton buildCardButton(int position)
   {
      CardButton cardButton = new CardButton(position);
      cardButton.addActionListener(this);
      return cardButton;
   }

   public void resetGame()
   {
      myModel.resetGame();
      timer.stopThread();
      difficultySet = false;
      executeComputerTurn();
      updateView();
      resetTimer();
   }

   private void executePlayerTurn(int position)
   {
     if (!myModel.getGameOver()) myModel.playerPlayCard(1, position);
   }

   private void executePlayerSelect(int position)
   {
      if (difficultySet && myModel.getPlayerTurn() == 1) 
         myModel.playerSelectCard(position, 1);
   }

   private void executeComputerTurn()
   {
      // Have the computerPlayer decide it's move
      if (difficultySet && !myModel.getGameOver() && myModel.getPlayerTurn() == 0)
      {
         Hand computerHand = myModel.getHand(0);
         Card[] stacks = myModel.getPlayAreaCards();
         int[] decision = PC.calculateMove(computerHand, stacks);
         myModel.playerSelectCard(decision[0], 0);
         myModel.playerPlayCard(0, decision[1]);

      }
   }

}

//------------------------------------------------------------------------------

class BuildGameModel
{

   private Random rand = new Random();
   private CardGameOutline buildGame;
   private int playerTurn;
   private boolean waitForConfirm = false;
   private boolean gameOver = false;
   private boolean oneHasFailed = false;
   private int[] scores;
   private int winner = -1;
   private Card[] playAreaCards = new Card[3];
   private int selectedCard = -1;
   private boolean [] playablePlayAreaCards = new boolean [3];

   public BuildGameModel(int numPacks, int numJokersPerPack, 
         int numUnusedCardsPerPack, Card[] unusedCardsPerPack,
         int numPlayers, int numCardsPerHand)
   {

      this.buildGame = new CardGameOutline(numPacks, numJokersPerPack,
            numUnusedCardsPerPack, unusedCardsPerPack,
            numPlayers, numCardsPerHand);
      scores = new int[numPlayers];
      playerTurn  = rand.nextInt(numPlayers);
      resetGame();

   }


   public int getPlayerTurn()
   {
      return playerTurn;
   }

   public int getWinner()
   {
      return winner;
   }

   public boolean getWaitForConfirm()
   {
      return waitForConfirm;
   }

   public void setWaitForConfirm(boolean input)
   {
      waitForConfirm = input;
   }

   public boolean getGameOver()
   {
      return gameOver;
   }

   public int[] getScores()
   {
      return scores.clone();
   }

   public Card[] getPlayAreaCards()
   {
      Card[] copy = new Card[3];
      for (int i = 0; i < 3; i++)
      {
         copy[i] = new Card(playAreaCards[i].getValue(), playAreaCards[i].getSuit());
      }
      return copy;
   }

   public void compareCardToPlayAreaCards(int player)
   {
      Card cardToCompare = buildGame.getHand(player).inspectCard(selectedCard);
      //System.out.println(cardToCompare.toString());
      for (int i = 0; i < playAreaCards.length; i++)
      {
         playablePlayAreaCards[i] = Math.abs(Card.lexographicCompareCards
               (cardToCompare, playAreaCards[i])) == 1;
      }
   }

   public void playerSelectCard(int position, int player)
   {
      selectedCard = position;
      compareCardToPlayAreaCards(player);
   }

   public boolean[] getPlayablePlayAreaCards()
   {
      return playablePlayAreaCards.clone();
   }

   public Hand getHand(int k)
   {
      return buildGame.getHand(k);
   }

   /**
    * Given player plays card at given card position.
    * 
    * @param player
    * @param cardPosition
    */
   public void playerPlayCard(int player, int stackPosition)
   {
      //If player cannot play
      if (selectedCard == -1 || stackPosition == -1) 
      {
         scores[player]++;
         if (oneHasFailed)
         {
            dealToPlayArea();
         }
         else oneHasFailed = true;
      }
      else
      {
         oneHasFailed = false;
         if (!gameOver)
         {
            playAreaCards[stackPosition] = buildGame.getHand(player).playCard(selectedCard);
            if (buildGame.getNumCardsRemainingInDeck() > 0) 
               buildGame.getHand(player).takeCard(buildGame.getCardFromDeck()); 
            else gameOver = true;
            if (buildGame.getNumCardsRemainingInDeck() <= 0)
            {
               gameOver = true;
               computeWinner();
            }
         }
      }
      nextTurn();
   }

   private void nextTurn()
   {
      playerTurn++;
      playerTurn %= 2;
   }


   public void resetGame()
   {

      // Computer is always 0;
      gameOver = false;
      playerTurn = rand.nextInt(scores.length);
      waitForConfirm = false;
      oneHasFailed = false;

      // Reset scores
      for (int i = 0; i < scores.length; i++)
      {
         scores[i] = 0;
      }

      resetTable();



      buildGame.newGame();
      buildGame.deal();
      buildGame.sortHands();
      fillPlayAreaCardsFromDeck();
   }

   private void dealToPlayArea()
   {
      for (int i = 0; i < playAreaCards.length; i++)
      {
        if (buildGame.getNumCardsRemainingInDeck() > 0) 
           playAreaCards[i] = buildGame.getCardFromDeck();
        else gameOver = true;
      }
      if (buildGame.getNumCardsRemainingInDeck() <= 0)
      {
         gameOver = true;
         computeWinner();
      }
   }
   private void fillPlayAreaCardsFromDeck()
   {
      for (int i = 0; i < playAreaCards.length; i++)
      {
         playAreaCards[i] = buildGame.getCardFromDeck();
      }
   }

   public void nextRound()
   {
      waitForConfirm = false;
      nextTurn();
      resetTable();
   }

   public void resetTable()
   {
      playAreaCards = new Card[3];
   }
   
   public Deck getDeck()
   {
      return buildGame.getDeck();
   }
   
   private void computeWinner ()
   {
      if (scores[0] < scores[1])winner = 0;
      else if (scores[1] < scores[0]) winner = 1;
      else winner = -1;
   }
   
}

//------------------------------------------------------------------------------

class BuildGameView
{
   private CardTable myCardTable;

   public BuildGameView(int numCards, int numPlayers)
   {
      myCardTable = new CardTable("CardTable", numCards, numPlayers);
      myCardTable.setSize(800, 600);
      myCardTable.setLocationRelativeTo(null);
      myCardTable.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
   }

   public void setHandAreas(JLabel[] compHand, JLabel[] playerHand)
   {
      myCardTable.pnlComputerHand.removeAll();
      myCardTable.pnlHumanHand.removeAll();

      for (int k = 0; k < compHand.length; k++)
      {
         myCardTable.pnlComputerHand.add(compHand[k]);
      }

      for (int k = 0; k < playerHand.length; k++)
      {
         myCardTable.pnlHumanHand.add(playerHand[k]);
      }

   }

   public void setPlayArea(JLabel[] playAreaHand)
   {
      myCardTable.pnlPlayArea.removeAll();
      for (int k = 0; k < playAreaHand.length; k++)
      {
         myCardTable.pnlPlayArea.add(playAreaHand[k]);
      }
   }

   public void setPlayArea(JComponent[] playAreaHand, GridBagLayout layout, GridBagConstraints[] constraints)
   {
      myCardTable.pnlPlayArea.removeAll();
      myCardTable.pnlPlayArea.setLayout(layout);
      for (int k = 0; k < playAreaHand.length; k++)
      {
         if (playAreaHand[k].getClass().equals(CardButton.class))
         {
            myCardTable.pnlPlayArea.add((CardButton) playAreaHand[k], constraints[k]);
         } 
         else if (playAreaHand[k].getClass().equals(JButton.class))
         {
            GridBagConstraints resetButton = new GridBagConstraints();
            // second stack
            resetButton.gridx = 2;
            resetButton.gridy = 1;
            resetButton.ipadx = 37;
            resetButton.ipady = 97;
            resetButton.weightx = 1;
            JButton me = (JButton)playAreaHand[k];
            if (me.getText().equals("Restart")) resetButton = constraints[k];
            if (me.getText().substring(0,5).equals("Timer")) resetButton = constraints[k];
            
            myCardTable.pnlPlayArea.add((JButton) playAreaHand[k], resetButton);
         }
         else
            myCardTable.pnlPlayArea.add((JLabel) playAreaHand[k], constraints[k]);
      }
   }

   public void buildTable(JLabel[][] allLabels)
   {
      setHandAreas(allLabels[0], allLabels[2]);
      setPlayArea(allLabels[1]);
      drawTable();
   }

   public void buildTable(JComponent[][] allLabels, GridBagLayout layout, GridBagConstraints[] constraints)
   {
      JLabel[] PCHand = new JLabel[allLabels[0].length];
      for (int i = 0; i < allLabels[0].length; i++)
      {
         PCHand[i] = (JLabel) allLabels[0][i];
      }

      JLabel[] playerHand = new JLabel[allLabels[2].length];
      for (int i = 0; i < allLabels[2].length; i++)
      {
         playerHand[i] = (JLabel) allLabels[2][i];
      }

      setHandAreas(PCHand, playerHand);
      setPlayArea(allLabels[1], layout, constraints);
      drawTable();
   }

   public void drawTable()
   {
      myCardTable.pack();
      myCardTable.setVisible(true);
      myCardTable.repaint();
   }

   public void resetTable()
   {
      // Removes the cards currently on the table and replaces them with
      // Card backs to indicate no card has been played
      myCardTable.pnlPlayArea.remove(0);
      myCardTable.pnlPlayArea.remove(0);
      myCardTable.pnlPlayArea.add(new JLabel(GUICard.getBackCardIcon()), 0);
      myCardTable.pnlPlayArea.add(new JLabel(GUICard.getBackCardIcon()), 0);

   }

   public void updateTimer(String time)
   {
      if (myCardTable.pnlPlayArea.getComponentCount() > 0)
      {
         JButton currentComponent = (JButton) myCardTable.pnlPlayArea.getComponent(0);
         currentComponent.setText("Timer: " + time);
      }
   }
   
}

//------------------------------------------------------------------------------

class ComputerPlayer
{
   private int difficulty = 0;
   
   
   public ComputerPlayer(int diff)
   {
      difficulty = diff;
   }
   
   //Returns an array of the positions of the card to play and the stack to play
   //it on - Note: if No cards to play index 0 = -1;
   public int [] calculateMove(Hand myHand, Card[] stackCards)
   {
      int [] decision = {-1, -1};
      
      int [][][] temp = computeGameState(myHand, stackCards);
      
      int [][] playableCards = temp[0];
      int [][] numberOfOptions = temp[1];
      int [][] playableOptions = temp[2];
        
      if (playableCards[0].length > 0)
      {
         if (difficulty == 0) 
            decision = decideRandomly(playableCards[0], numberOfOptions[0], playableOptions );
         else if (difficulty == 2) 
            decision = decideSmartly(myHand, stackCards, playableCards[0],
                  numberOfOptions[0], playableOptions);
         else
         {
            double threshold = .5;
            double roll = Math.random();
            if (roll < threshold)
            {
               decision = decideRandomly(playableCards[0], numberOfOptions[0], playableOptions);
            }
            else
            {
               decision = decideSmartly(myHand, stackCards, playableCards[0],
                     numberOfOptions[0], playableOptions);
            }
         }
      }
      
      return decision;
   }
   
   private int [][][] computeGameState(Hand myHand, Card[] stackCards) 
   {
      int [][] playableCards = new int [1][myHand.getNumCards()];
      int [][] numberOfOptions = new int [1][myHand.getNumCards()];
      int [][] playableOptions = new int [myHand.getNumCards()][3];
      
      //Compute if a card has a move, what moves it has and the total moves a card has
      //store unplayable cards as a bad index to filter out later
      for (int i = 0; i < myHand.getNumCards(); i++)
      {
          
         Card currCard = myHand.inspectCard(i);
         boolean playable = false;
         int numOfOp = 0;
         for (int stack = 0; stack< stackCards.length; stack++)
         {
            if (Math.abs(Card.lexographicCompareCards(currCard, stackCards[stack])) == 1 )
            {
               playableCards[0][i] = i;
               playableOptions[i][stack] = 1;
               playable = true;
               numOfOp++;
            }
            else
            {
               if (!playable) playableCards[0][i] = -1;
            }
         }
         numberOfOptions[0][i] = numOfOp;
           
      }
       
      //Now filter the unplayable cards from the set
      int [][][] filtered = filterBadMoves(myHand, playableCards, numberOfOptions, playableOptions);    
      playableCards = filtered[0];
      numberOfOptions = filtered[1];
      playableOptions = filtered[2];
      
      
      int [][][] result = {playableCards, numberOfOptions, playableOptions};
      return result;
      
   }
   
   
   private int [][][] filterBadMoves(Hand myHand, int [][] playableCards, int [][] numberOfOptions, int [][] playableOptions)
   {
    int [][] temp1 = new int [1][0];
    int [][] temp2 = new int [1][0];
    int [][] temp3 = new int [0][3];
    for (int i = 0; i < myHand.getNumCards(); i++)
    {
       if (playableCards[0][i] != -1)
       {
          int [][] resize1 = new int [1][temp1[0].length+1];
          int [][] resize2 = new int [1][temp2[0].length+1];
          int [][] resize3 = new int [temp3.length+1][3];
          
          for (int k = 0; k < temp1[0].length; k++)
          {
             resize1[0][k] = temp1[0][k];
             resize2[0][k] = temp2[0][k];
             resize3[k] = temp3[k];
          }
                  
          int lastIndex = resize1[0].length -1;
          resize1[0][lastIndex] = playableCards[0][i];
          resize2[0][lastIndex] = numberOfOptions[0][i];
          resize3[lastIndex] = playableOptions[i];
          
          temp1 = resize1;
          temp2 = resize2;
          temp3 = resize3;
       }

    }
    
    int [][][] output = {temp1, temp2, temp3};
    return output;
    
   }
   
   
   private int [] decideRandomly(int [] playableCards, int [] numberOfOptions, int [][] playableOptions)
   {
      int cardPos =-1;
      int relativePos = 0;
      int stackPos = -1;
      int [] result = {-1,-1};
      
      int [] basicRules = applyBasicRules(playableCards, numberOfOptions, playableOptions);
      
      if (basicRules[0] == 1)
      {
         result [0] = basicRules[1];
         result[1] = basicRules[2];
         return result;
      }
      else cardPos = basicRules[1];
      
      Random rand = new Random(); 
      //If we have more than 1 card to play choose a random card
      if (cardPos == -1)
      {
         relativePos = rand.nextInt(playableCards.length);
         cardPos = playableCards[relativePos];
      }
      
      int [] choices = new int [0];
      //Build an array containing the index of the stacks that can be
      // played on by the selected card
      for (int i = 0; i < playableOptions[relativePos].length; i++)
      {
         if (playableOptions[relativePos][i] == 1)
         {
            int [] resize = new int [choices.length+1];
            
            for (int k = 0; k < choices.length; k++)
            {
               resize[k] = choices[k];
            }
                    
            int lastIndex = resize.length -1;
            resize[lastIndex] = i;
            
            choices = resize;
         }
      }
      
      //Now pick a random valid stack to play
      if (choices.length > 0) stackPos = choices[rand.nextInt(choices.length)];
      
      //Set and return the decision
      result[0] = cardPos;
      result[1] = stackPos;
      return result;

   }
   
   
   private int [] applyBasicRules(int [] playableCards, int [] numberOfOptions, int [][] playableOptions)
   {
      int shouldReturn = 0;
      int cardPos =-1;
      int stackPos = -1; 
      int [] result = {0,-1,-1};
      
      //If I have no playable cards return result;
      if (playableCards.length <= 0)
      {
         shouldReturn = 1;
      }
      //If I have only one playable card then play that card
      else if (playableCards.length == 1)
      {
         cardPos = playableCards[0];
         //If that card has only one option > play that stack
         if (numberOfOptions[0] == 1)
         {
            for (int i = 0; i < playableOptions[0].length; i++)
            {
               if (playableOptions[0][i] == 1) stackPos = i;
            }

              shouldReturn = 1;
         }
         //Otherwise proceeded to random
      }
      result[0] = shouldReturn;
      result[1] = cardPos;
      result[2] = stackPos; 
      return result;
   }
   
   
   private int [] decideSmartly(Hand myHand, Card[] stackCards, int [] playableCards, int [] numberOfOptions, int [][] playableOptions)
   {
      //Apply some basic Rules to filter choices / minimize workload
      int cardPos =-1;
      int stackPos = -1;
      int [] result = {-1,-1};
      
      //Lets not consider playing jokers unless we have to play one to play
      int [][][] jokerRemoved = filterJokers(myHand, playableCards,
            numberOfOptions, playableOptions);
      playableCards = jokerRemoved[0][0];
      numberOfOptions = jokerRemoved[1][0];
      playableOptions = jokerRemoved[2];
      
      //Now apply some basic rules / heuristics for trivial cases
      //i.e. No card to play, or only one card to play with only one possible
      //stack to play it on
      int [] basicRules = applyBasicRules(playableCards, numberOfOptions, playableOptions);
      
      if (basicRules[0] == 1)
      {
         result [0] = basicRules[1];
         result[1] = basicRules[2];
         return result;
      }
      //If we have only one card but it has more
      //than one stack to be placed then we don't need to determine the card
      //to play
      else cardPos = basicRules[1]; 

      //Evaluate remaining options to Maximize future options
      
      //Sort the data by NumOfOptions
      int [][][] sorted = sortByNumberOfOptions(playableCards, numberOfOptions, playableOptions);
      playableCards = sorted[0][0];
      numberOfOptions = sorted[1][0];
      playableOptions = sorted[2];
      
      int lowestOptions = numberOfOptions[0];
      int [] choices = new int [1];
      choices[0] = 0;
      if (cardPos == -1)
      {
         //if there could be others
         //Add all other cards with that number of options to choices
         choices = populateChoices(playableCards, numberOfOptions, choices, lowestOptions);
         choices = shuffleChoices(choices);
      }

      //For every card in choices compute the number of options after playing a card
      int [][] numberOfOptionsAfter = 
            computeMyFutureOptions(choices, myHand, stackCards, playableCards, playableOptions);
   

      //Find the biggest numberOfOptions move and choose the best move
      int [] decision = chooseOptionsMaximalMove(numberOfOptionsAfter);
      int maxCardPos = decision[0];
      int maxStackPos = decision[1];
      
      cardPos = playableCards[choices[maxCardPos]];
      stackPos = maxStackPos;
      
      result[0] = cardPos;
      result[1] = stackPos;

      //Return
      return result;
   }
   
   private int [][][] filterJokers(Hand myHand, int [] playableCards, int [] numberOfOptions, int [][] playableOptions)
   {
      while(playableCards.length > 1)
      {
         boolean jokerSeen = false;
         for (int i = 0; i < playableCards.length; i++)
         {
            //If we see a joker
            if (myHand.inspectCard(playableCards[i]).getValue() == 'X')
            {
               int [] temp1 = new int [playableCards.length -1];
               int [] temp2 = new int [playableCards.length -1];
               int [][] temp3 = new int [playableCards.length -1][];
               
               //Copy the preceding cards
               for (int j = 0; j < i; j++)
               {
                  temp1[j] = playableCards[j];
                  temp2[j] = numberOfOptions[j];
                  temp3[j] = playableOptions[j];
               }
               //Copy all the cards following the joker
               for (int j = i; j < playableCards.length-1; j++)
               {
                  temp1[j] = playableCards[j+1];
                  temp2[j] = numberOfOptions[j+1];
                  temp3[j] = playableOptions[j+1];
               }
               playableCards = temp1;
               numberOfOptions = temp2;
               playableOptions = temp3;
               jokerSeen = true;
               i--;
               //We break early to make sure we leave a joker if its the only
               //move i.e. we only have jokers
               break;
            }
         }
         
         
         if (!jokerSeen) break; 
      }
      int [][] wrapPlayableCards = {playableCards};
      int [][] wrapNumberOfOptions = {numberOfOptions};
      int [][][] output = {wrapPlayableCards, wrapNumberOfOptions, playableOptions};
      return output;
   }
   
   private int [][][] sortByNumberOfOptions(int [] playableCards, int [] numberOfOptions, int [][] playableOptions)
   {
      //Sort the data by NumOfOptions as list will always be less than 10
      //use insertion sort to sort low-high optionCount
      for (int i = 0; i < numberOfOptions.length-1; i++)
      {
         int minPos = i;
         int min = numberOfOptions[minPos];
         for (int j = i+1; j < numberOfOptions.length; j++)
         {
            if (numberOfOptions[j] < min)
            {
               minPos = j;
               min = numberOfOptions[j];
            }
         }
         
         numberOfOptions[minPos] = numberOfOptions[i];
         numberOfOptions[i] = min;
         
         int temp = playableCards[i];
         playableCards[i] = playableCards[minPos];
         playableCards[minPos] = temp;
         
         int [] temp2 = playableOptions[i];
         playableOptions[i] = playableOptions[minPos];
         playableOptions[minPos] = temp2;
         
      }
      
      int [][] wrapPlayableCards = {playableCards};
      int [][] wrapNumberOfOptions = {numberOfOptions};
      int [][][] output = {wrapPlayableCards, wrapNumberOfOptions, playableOptions};
      return output;
   }
   
   private int [] populateChoices(int [] playableCards, int [] numberOfOptions, int [] choices, int lowestOptions)
   {
      //Add all cards with that number of options to choices
      for (int i = 1; i < playableCards.length; i++)
      {
         if (numberOfOptions[i] == lowestOptions) 
         {
            int [] temp = choices;
            choices = new int [temp.length+1];
            for (int k = 0; k < temp.length; k++ )
            {
               choices[k] = temp[k];
            }
            choices[choices.length -1] = i;
         }
      }
      return choices;
   }
   
   private int [] shuffleChoices(int [] choices)
   {
      for (int pass = 0; pass < 10; pass++)
      {
         Random rand = new Random();
         for (int i = 0; i < choices.length; i++)
         {
            int roll = rand.nextInt(choices.length);
            int temp = choices[roll];
            choices[roll] = choices[i];
            choices[i] = temp;
         }
      }
      return choices;
   }

   private int [][] computeMyFutureOptions(int [] choices, Hand myHand, Card[] stackCards, int [] playableCards, int [][] playableOptions )
   {
      int [][] numberOfOptionsAfter = new int [choices.length][3];
      for (int i = 0; i < choices.length; i++)
      {
         Hand nextHand = new Hand();
         int possibleCard = playableCards[choices[i]];
         //Fill hand with all cards but current card
         for (int k = 0; k < myHand.getNumCards(); k++)
         {
            if (k != possibleCard) nextHand.takeCard(myHand.inspectCard(k));
         }
         
         //For every stack to play on for this card compute the sum of its options
        for (int stack = 0; stack < playableOptions[choices[i]].length; stack++)
        {
           int sum = -10;
           if (playableOptions[choices[i]][stack] == 1)
           {
              //Build new card stacks
              Card [] nextStacks = new Card[stackCards.length];
              for (int k = 0; k < nextStacks.length; k++)
              {
                 if (k == stack) nextStacks[k] = myHand.inspectCard(possibleCard);
                 else nextStacks[k] = stackCards[k];
              }
              
              
              int [][][] nextGame = computeGameState(nextHand, nextStacks);
              
              int [] nextNumOfOptions = nextGame[1][0];
              
              for (int LL = 0; LL < nextNumOfOptions.length; LL++)
              {
              //   System.out.println(nextNumOfOptions[LL]);
              }
              
              sum=0;
              for(int k = 0; k < nextNumOfOptions.length; k++)
              {
                 sum += nextNumOfOptions[k];
              }  
           }
           numberOfOptionsAfter[i][stack] = sum;
        }    
      }
      
      return numberOfOptionsAfter;
   }
   
   private int [] chooseOptionsMaximalMove(int [][] numberOfOptionsAfter)
   {
      int max = numberOfOptionsAfter[0][0];
      int maxCardPos = 0;
      int maxStackPos = 0;
      
      //Incase there are equivalent move choices
      int [][] ties = new int [0][2];
      
      for (int i = 0; i < numberOfOptionsAfter.length; i++ )
      {
         for (int j = 0; j < numberOfOptionsAfter[i].length; j++) 
         {
            if (numberOfOptionsAfter[i][j] > max)
            {
               maxCardPos = i;
               maxStackPos = j;
               max = numberOfOptionsAfter[i][j];
               //Erase ties as something higher was found
               ties = new int [0][2];
            }
            else if (numberOfOptionsAfter[i][j] == max)
            {
               
               int [][] temp = ties;
               ties = new int [temp.length+1][2];
               for (int pos = 0; pos < temp.length; pos++)
                  ties[pos] = temp[pos];
               
               ties[ties.length -1][0] = i;
               ties[ties.length -1][1] = j;
            }
         }
      }
      
      //If there were tied choices, then randomly pick between those moves
      if (ties.length > 0)
      {
         
         Random rand = new Random();
         int roll = rand.nextInt(ties.length);
         maxCardPos = ties[roll][0];
         maxStackPos = ties[roll][1];
      }
      
      
      int [] output = {maxCardPos, maxStackPos};
      return output;
      
   }
   
   public int getDifficulty()
   {
      return difficulty;
   }
   
   public void setDifficulty(int diff)
   {
      difficulty = diff;
   }
   
}

//-------------------------------------------------------------

class Timer extends Thread
{
   private int seconds;
   private boolean active = false;
   public String timeString;
   private ActionListener listener;

   /* default constructor that calls the constructor of the thread class */
   public Timer()
   {
      super();
      
   }

   public Timer(int timeStartInt)
   {
      // starts thread with a time, creating a "paused" timer
      // subtract 1 to prevent timer from incrementing
      // at every start/stop
      this.seconds = timeStartInt - 1;
   }

   public void run() // updates time
   {
      while (true)
      {
         if (active)
         {
            doNothing(1000);
            if (active)
            {
               if (this.seconds < 3600)
               {
                  this.seconds += 1;
               } else
               {
                  this.seconds = 0;
               }
            }
         }
         //Lets the thread reduce utilization while inactive
         else doNothing(5);
         if (listener != null) 
            listener.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, null));
         
      }
   }

   /* Set seconds of timer */
   public boolean setSeconds(int seconds)
   {
      this.seconds = seconds;
      return true;
   }

   public void toggleActive ()
   {
      active = !active;
   }
   
   /* stops run() */
   public boolean stopThread()
   {
      this.active = false;
      return true;
   }

   public boolean getActive()
   {
      return this.active;
   }

   public int getSecondsSinceStart()
   {
      return this.seconds;
   }

   /** doNothing method allows thread to sleep and is crucial for keeping time */

   public void doNothing(int milliseconds)
   {
      try
      {
         Thread.sleep(milliseconds);
      } catch (InterruptedException e)
      {
         System.out.println("Unexpected interrupt.");
         System.exit(0);
      }
   }
   /* Formats and returns a String of time formated in minutes:seconds. */

   public String getTimeString()
   {
      int mins = this.seconds / 60;
      int secs = this.seconds - (mins * 60);
      String timeString = String.format("%02d", mins) + ":" + String.format("%02d", secs);

      return timeString;
   }

   public void addActionListener(ActionListener listener)
   {
      this.listener = listener;
   }
}

//------------------------------------------------------------------

class CardButton extends JButton
{
   int myPos;
   boolean playerCard = false;
   
   public CardButton (int position)
   {
      super();
      myPos = position;
      
     setOpaque(false);
     setBorderPainted(false);
     setContentAreaFilled(false);
      
   }
    
   public int getPosition()
   {
      return myPos;
   }
   
   public boolean getPlayerCard()
   {
      return playerCard;
   }
   
   public void setPlayerCard(boolean in)
   {
      playerCard = in;
   }
   
   
}

//-------------------------------------------------------------------------

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
   
   public Deck getDeck()
   {
      return deck;
   }

}

//------------------------------------------------------------------------------

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

      // Set panel layouts and constraints
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

      pnlPlayArea = new JPanel(new GridLayout(2, 3));
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

//------------------------------------------------------------------------------

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
               String cardImagePath = pathToImagesFolder.concat(turnIntIntoCardValue(i % 14))
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
      if (card != null && !card.getErrorFlag())
         return iconCards[turnCardValueIntoInt(card.getValue())][turnSuitIntoInt(card.getSuit())];
      else
         return getBackCardIcon();
   }

   // Returns the iconBack icon
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

//----------------------------------------------------------------

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
      numCards = 0;
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

//-----------------------------------------------------------------

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

      if(card1 == null || card2 == null)
         return false;
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

   /**
    * Returns the difference in value of cards.
    * 
    * @param card1
    * @param card2
    * @return int result
    */
   public static int lexographicCompareCards(Card card1, Card card2)
   {
      int otherValue = 0;
      int myValue = 0;

      for (int i = 0; i < valueRanks.length; i++)
      {
         if (card1.getValue() == valueRanks[i])
            myValue = i;
         if (card2.getValue() == valueRanks[i])
            otherValue = i;
      }
      int result = myValue - otherValue;
      if (myValue == 0 && otherValue == 12)
      {
         result = 1;
      } else if (myValue == 12 && otherValue == 0)
      {
         result = -1;
      } else if (myValue == 13)
      {
         result = 1;
      } else if (myValue != 13 && otherValue == 13)
      {
         result = -2;
      }
      return result;

   }

}



