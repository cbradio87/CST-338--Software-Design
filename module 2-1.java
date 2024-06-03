/*
 * Header:
 * This is Assignment 2 "Write a Java program: Casino"
 * for CST338-40_SP21: Software Design Spring 2021
 * Copyright Christopher Bray 2021
 */

import java.util.Scanner;
import java.lang.Math;

public class Assig2
{
   static Scanner kbd = new Scanner(System.in);
   public static void main(String[] args) 
   {  
      ThreeString myThreeString = new ThreeString();
      do 
      {
         ThreeString.bet = getBet();
         if (ThreeString.bet == 0)
            break;
         // leaves loop if the bet is entered as 0
         if (ThreeString.numPulls == 39)
            break;
         // leaves the loop if the number of pulls is = 39
         System.out.println("whirrrrrr .... and your pull is...");
         myThreeString = pull();
         //sets myThreeString to have the random number in its strings
         ThreeString.allString = toString(myThreeString);
         //concatenates 3 strings to 1
         ThreeString.multiplier = getPayMultiplier(ThreeString.allString);
         //gets value of slot pull to later be multiplied by bet
         ThreeString.winnings = ThreeString.bet * ThreeString.multiplier;
         //bet * winnings stored in winnings
         ThreeString.saveWinnings(ThreeString.winnings);
         //stores winnings in array
         display (ThreeString.allString, ThreeString.winnings);
         //tells whether you won or lost
      }
      while (ThreeString.bet != 0);
      //exit loop when 0 is entered
      System.out.print("Thanks for playing at Casino! \n" +
            "Your individual winnings were: \n");
      int[] array = new int[ThreeString.numPulls];
      for (int i = 0; i < ThreeString.numPulls; i++)
         array[i] = ThreeString.pullWinnings[i];
      //copies array
      for (int i = 0; i < ThreeString.numPulls; i++)
         System.out.print(ThreeString.pullWinnings[i] + " ");
      //prints array
      System.out.print("\n" + ThreeString.toStringWinnings()); 
      //returns total winnings in a string
   }
   public static int getBet()
   {
      boolean doLoop = true;
      int bet = 0;      
      while (doLoop == true) 
      { 
         System.out.print("How much would you like to bet "
               + "(1-100) or 0 to quit? ");
         bet = kbd.nextInt();
         if ((bet <= 100) && (bet > 0))
         {
         doLoop = false;
         }
         else if (bet == 0)
            break;
         else
            doLoop = true;
      } 
      return bet; 
   }
   public static ThreeString pull()
   {   
     ThreeString setPull = new ThreeString();
     ThreeString.mutString1(randString());
     //feeds random string to mutator String1
     ThreeString.mutString2(randString());
     //feeds random string to mutator String2
     ThreeString.mutString3(randString());
     //feeds random string to mutator String3
     return setPull;
   }
   private static String randString()
   {
     int outOfEight = (int)(Math.random() * 8) + 1;
     if (outOfEight < 4)
        return "(space)";
     else if (outOfEight == 4 || outOfEight == 5)
        return "cherries";
     else if (outOfEight == 7)
        return "BAR";
     else
        return "7";
   }
   //gives proper odds to each outcome
   static int getPayMultiplier(String pullString)
   {  
      String[] possibility = new String[4];
      possibility[0] = "(space)";
      possibility[1] = "7";
      possibility[2] = "BAR";
      possibility[3] = "cherries";
      int multiplier = 0;
      if ("cherries, cherries, cherries".equals(pullString))
         multiplier = 30;
      else if ("BAR, BAR, BAR".equals(pullString))
         multiplier = 50;
      else if ("7, 7, 7".equals(pullString))
         multiplier = 100;
      for (int i = 0; i < 3; i++)
      {
         String first1 = "cherries, cherries, ";
         if ((first1 + possibility[i]).equals(pullString))
            multiplier = 15;
      }
      String first2 = "cherries, ";
      for (int i = 0; i < 4; i++)
      {
         for (int j = 0; j < 3; j++)
         {
            if ((first2 + possibility[j]+ 
                  ", " + possibility[i]).equals(pullString))
               multiplier = 5;
         }
      }  
      return multiplier;
      //calculates the value won to later be multiplied with the bet
    }
   public static void display (String thePull, int winnings)
   {
      System.out.println(thePull);
      ThreeString.numPulls++;
      if (winnings > 0)
         System.out.println("congrats you win: " + (winnings) + "\n");
      else
         System.out.println("sorry, you lose. \n");
   }
   //displays if you've won or lost
   static String toString(ThreeString item)
   {
      ThreeString.allString = ThreeString.retString1() + ", " + 
            ThreeString.retString2() + ", " + ThreeString.retString3();
      return ThreeString.allString;
   }
   //concatenates the strings
}
class ThreeString
{
   private static String string1;
   private static String string2;
   private static String string3;
   public static final int MAX_LEN = 20;
   public static final int MAX_PULLS = 40; 
   static int[] pullWinnings = new int [MAX_PULLS];
   static int numPulls = 0;
   //above were prescribed in assignment
   static String allString = "";
   static int bet = 0;
   static int winnings = 0;
   static int multiplier = 0;
   static int totalWinnings = 0;
   //I put the above in the ThreeString Class and then couldn't get them out
   //once I realized the ThreeString Class is supposed to be more slim.
   public ThreeString()
   {
      string1 = "";
      string2 = "";
      string3 = "";
   }
   //ThreeString constructor
   private static boolean validString( String str )
   {
      if ((str != null) && (str.length() <= MAX_LEN))
         return true;
      else
         return false;
   }
   //validates string
   public static boolean mutString1 (String str)
   {
     if (validString(str) == true)
     {
        string1 = str;
        return true;
     } 
     else 
        return false;
   }
   //mutates first string
   public static boolean mutString2 (String str)
   {
     if (validString(str) == true)
     {
        string2 = str;
        return true;
     } 
     else 
        return false;
   }
   //mutates 2nd string
   public static boolean mutString3 (String str)
   {
     if (validString(str) == true)
     {
        string3 = str;
        return true;
     } 
     else 
        return false;
   }
   //mutates 3rd string
   public static String retString1()
   {
      return ThreeString.string1;
   }
   //returns private string 1
   public static String retString2 ()
   {
      return ThreeString.string2;
   }
   //returns private string 2
   public  static String retString3 ()
   {
      return ThreeString.string3;
   }
   //returns private string 3
   static boolean saveWinnings(int winnings)
   {
     if (numPulls < MAX_PULLS)
     {
        pullWinnings[numPulls] = winnings;
        return true;
     }
     else
     {
        System.exit(0);
        return false;
     }
   }
   //records winnings
   public static String toStringWinnings()
   {
     for (int i = 0; i <= numPulls; i++)
     {
        ThreeString.totalWinnings += ThreeString.pullWinnings[i];
     }
     String totalString = "Total winnings are: $" + ThreeString.totalWinnings;
     return totalString;
   }
   //returns total winnings
}

/*-------------------Sample Runs 1 and 2--------------------*
 *                      Sample Run 1
How much would you like to bet (1-100) or 0 to quit? 4
whirrrrrr .... and your pull is...
BAR, 7, BAR
sorry, you lose. 

How much would you like to bet (1-100) or 0 to quit? 4
whirrrrrr .... and your pull is...
BAR, 7, cherries
sorry, you lose. 

How much would you like to bet (1-100) or 0 to quit? 4
whirrrrrr .... and your pull is...
cherries, cherries, (space)
congrats you win: 60

How much would you like to bet (1-100) or 0 to quit? 4
whirrrrrr .... and your pull is...
cherries, (space), (space)
congrats you win: 20

How much would you like to bet (1-100) or 0 to quit? 4
whirrrrrr .... and your pull is...
7, BAR, 7
sorry, you lose. 

How much would you like to bet (1-100) or 0 to quit? 4
whirrrrrr .... and your pull is...
(space), 7, cherries
sorry, you lose. 

How much would you like to bet (1-100) or 0 to quit? 4
whirrrrrr .... and your pull is...
(space), (space), BAR
sorry, you lose. 

How much would you like to bet (1-100) or 0 to quit? 4
whirrrrrr .... and your pull is...
7, (space), (space)
sorry, you lose. 

How much would you like to bet (1-100) or 0 to quit? 4
whirrrrrr .... and your pull is...
(space), cherries, cherries
sorry, you lose. 

How much would you like to bet (1-100) or 0 to quit? 4
whirrrrrr .... and your pull is...
(space), (space), (space)
sorry, you lose. 

How much would you like to bet (1-100) or 0 to quit? 4
whirrrrrr .... and your pull is...
7, cherries, (space)
sorry, you lose. 

How much would you like to bet (1-100) or 0 to quit? 4
whirrrrrr .... and your pull is...
(space), 7, (space)
sorry, you lose. 

How much would you like to bet (1-100) or 0 to quit? 4
whirrrrrr .... and your pull is...
BAR, (space), cherries
sorry, you lose. 

How much would you like to bet (1-100) or 0 to quit? 4
whirrrrrr .... and your pull is...
7, BAR, (space)
sorry, you lose. 

How much would you like to bet (1-100) or 0 to quit? 4
whirrrrrr .... and your pull is...
(space), cherries, 7
sorry, you lose. 

How much would you like to bet (1-100) or 0 to quit? 4
whirrrrrr .... and your pull is...
cherries, BAR, cherries
congrats you win: 20

How much would you like to bet (1-100) or 0 to quit? 4
whirrrrrr .... and your pull is...
7, 7, BAR
sorry, you lose. 

How much would you like to bet (1-100) or 0 to quit? 4
whirrrrrr .... and your pull is...
cherries, 7, (space)
congrats you win: 20

How much would you like to bet (1-100) or 0 to quit? 4
whirrrrrr .... and your pull is...
(space), 7, 7
sorry, you lose. 

How much would you like to bet (1-100) or 0 to quit? 4
whirrrrrr .... and your pull is...
BAR, cherries, (space)
sorry, you lose. 

How much would you like to bet (1-100) or 0 to quit? 4
whirrrrrr .... and your pull is...
(space), (space), cherries
sorry, you lose. 

How much would you like to bet (1-100) or 0 to quit? 4
whirrrrrr .... and your pull is...
(space), (space), (space)
sorry, you lose. 

How much would you like to bet (1-100) or 0 to quit? 4
whirrrrrr .... and your pull is...
(space), BAR, cherries
sorry, you lose. 

How much would you like to bet (1-100) or 0 to quit? 4
whirrrrrr .... and your pull is...
7, (space), cherries
sorry, you lose. 

How much would you like to bet (1-100) or 0 to quit? 4
whirrrrrr .... and your pull is...
(space), (space), BAR
sorry, you lose. 

How much would you like to bet (1-100) or 0 to quit? 4
whirrrrrr .... and your pull is...
(space), cherries, 7
sorry, you lose. 

How much would you like to bet (1-100) or 0 to quit? 4
whirrrrrr .... and your pull is...
(space), (space), 7
sorry, you lose. 

How much would you like to bet (1-100) or 0 to quit? 4
whirrrrrr .... and your pull is...
cherries, (space), (space)
congrats you win: 20

How much would you like to bet (1-100) or 0 to quit? 4
whirrrrrr .... and your pull is...
(space), 7, (space)
sorry, you lose. 

How much would you like to bet (1-100) or 0 to quit? 4
whirrrrrr .... and your pull is...
(space), (space), (space)
sorry, you lose. 

How much would you like to bet (1-100) or 0 to quit? 4
whirrrrrr .... and your pull is...
7, 7, BAR
sorry, you lose. 

How much would you like to bet (1-100) or 0 to quit? 4
whirrrrrr .... and your pull is...
cherries, (space), (space)
congrats you win: 20

How much would you like to bet (1-100) or 0 to quit? 4
whirrrrrr .... and your pull is...
(space), (space), cherries
sorry, you lose. 

How much would you like to bet (1-100) or 0 to quit? 4
whirrrrrr .... and your pull is...
cherries, (space), (space)
congrats you win: 20

How much would you like to bet (1-100) or 0 to quit? 4
whirrrrrr .... and your pull is...
(space), BAR, (space)
sorry, you lose. 

How much would you like to bet (1-100) or 0 to quit? 4
whirrrrrr .... and your pull is...
(space), 7, (space)
sorry, you lose. 

How much would you like to bet (1-100) or 0 to quit? 4
whirrrrrr .... and your pull is...
(space), BAR, (space)
sorry, you lose. 

How much would you like to bet (1-100) or 0 to quit? 4
whirrrrrr .... and your pull is...
(space), 7, 7
sorry, you lose. 

How much would you like to bet (1-100) or 0 to quit? 4
whirrrrrr .... and your pull is...
7, 7, (space)
sorry, you lose. 

How much would you like to bet (1-100) or 0 to quit? 4
Thanks for playing at Casino! 
Your individual winnings were: 
0 0 60 20 0 0 0 0 0 0 0 0 0 0 0 20 0 20 0 0 0 0 0 0 0 0 0 20 0 0 0 20 0 20 0 0 0 0 0 
Total winnings are: $180


              Run 2


 How much would you like to bet (1-100) or 0 to quit? 5
whirrrrrr .... and your pull is...
7, (space), (space)
sorry, you lose. 

How much would you like to bet (1-100) or 0 to quit? 6
whirrrrrr .... and your pull is...
cherries, (space), (space)
congrats you win: 30

How much would you like to bet (1-100) or 0 to quit? 7
whirrrrrr .... and your pull is...
7, (space), cherries
sorry, you lose. 

How much would you like to bet (1-100) or 0 to quit? 4
whirrrrrr .... and your pull is...
7, 7, (space)
sorry, you lose. 

How much would you like to bet (1-100) or 0 to quit? 3
whirrrrrr .... and your pull is...
(space), (space), (space)
sorry, you lose. 

How much would you like to bet (1-100) or 0 to quit? 5
whirrrrrr .... and your pull is...
7, (space), cherries
sorry, you lose. 

How much would you like to bet (1-100) or 0 to quit? 6
whirrrrrr .... and your pull is...
(space), (space), (space)
sorry, you lose. 

How much would you like to bet (1-100) or 0 to quit? 7
whirrrrrr .... and your pull is...
7, (space), 7
sorry, you lose. 

How much would you like to bet (1-100) or 0 to quit? 6
whirrrrrr .... and your pull is...
(space), (space), 7
sorry, you lose. 

How much would you like to bet (1-100) or 0 to quit? 5
whirrrrrr .... and your pull is...
BAR, (space), cherries
sorry, you lose. 

How much would you like to bet (1-100) or 0 to quit? 4
whirrrrrr .... and your pull is...
7, cherries, 7
sorry, you lose. 

How much would you like to bet (1-100) or 0 to quit? 5
whirrrrrr .... and your pull is...
7, BAR, cherries
sorry, you lose. 

How much would you like to bet (1-100) or 0 to quit? 6
whirrrrrr .... and your pull is...
cherries, (space), 7
congrats you win: 30

How much would you like to bet (1-100) or 0 to quit? 4
whirrrrrr .... and your pull is...
cherries, (space), (space)
congrats you win: 20

How much would you like to bet (1-100) or 0 to quit? 0
Thanks for playing at Casino! 
Your individual winnings were: 
0 30 0 0 0 0 0 0 0 0 0 0 30 20 
Total winnings are: $80
*----------------------------------------------*/

