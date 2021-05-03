/*
Open Sourcerers
Nathan Jobes, Amy Gonzales, Chris Bray, Jerome Ortega
CST 338 Week 5
Assignment 5: Suit Match
3/31/2021
*/
package suitMatch;

import javax.swing.*;
import java.awt.*;

public class Assig5
{
   /**
    * 52 + 4 jokers + 1 back-of-card image
    */
   static final int NUM_CARD_IMAGES = 57;

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
      String suits = "CDHS";
      String result = "";
      if (j < suits.length())
         result = suits.substring(j, j + 1);
      return result;
   }

   // a simple main to throw all the JLabels out there for the world to see
   public static void main(String[] args)
   {
      int k;

      // prepare the image icon array
      loadCardIcons();

      // establish main frame in which program will run
      JFrame frmMyWindow = new JFrame("Card Room");
      frmMyWindow.setSize(1150, 650);
      frmMyWindow.setLocationRelativeTo(null);
      frmMyWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

      // set up layout which will control placement of buttons, etc.
      FlowLayout layout = new FlowLayout(FlowLayout.CENTER, 5, 20);
      frmMyWindow.setLayout(layout);

      // prepare the image label array
      JLabel[] labels = new JLabel[NUM_CARD_IMAGES];
      for (k = 0; k < NUM_CARD_IMAGES; k++)
         labels[k] = new JLabel(icon[k]);

      // place your 3 controls into frame
      for (k = 0; k < NUM_CARD_IMAGES; k++)
         frmMyWindow.add(labels[k]);

      // show everything to the user
      frmMyWindow.setVisible(true);
   }

}
