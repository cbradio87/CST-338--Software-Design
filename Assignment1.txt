/*
 * Header:
 * This is Assignment 1 "Write a Java program: String Manipulation"
 * for CST338-40_SP21: Software Design Spring 2021
 * Copyright Christopher Bray 2021
 */


import java.util.Scanner;
import java.text.DecimalFormat;

public class Assign1 
{
   
   public static final int MIN_HOURS = 12; 
   //The minimum hours that should be spent on this class
   public static final int MAX_HOURS = 20;
   //The maximum hours that should be spent on this class
   public static void main(String[] args) 
   {
      Scanner kbd = new Scanner(System.in);
      //create keyboard input
      System.out.println("Please enter your first name with the "
            + "first letter capital: ");
      String firstName = kbd.next();
      //collect first name from user input
      System.out.println("Please enter your last name with"
            + " the first letter capital: ");
      String lastName = kbd.next();
      //collect last name from user input
      String fullName = firstName + " " + lastName;
      //concatinate first and last name with a space in the middle
      int lengthFullName = fullName.length() - 1;
      //collect the length of the name minus the space
      System.out.println("Your full name is; " + fullName + " and it is "
            + lengthFullName + " characters long.");
      System.out.println("Here it is in all uppercase " 
            + fullName.toUpperCase() + ".");
      System.out.println("Here it is in all lowercase "
            + fullName.toLowerCase() + ".");
      System.out.println("The minimum hours you should spend on this "
            + "class each week is " + MIN_HOURS + ".");
      System.out.println("The maximum hours you should spend on this "
            + "class each week is " + MAX_HOURS + ".");
      System.out.println("Please enter how many hours you have spent "
            + "this week on this class to 3 decimal places.");
      float hours = kbd.nextFloat();
      //collect time spent in class up to 3 decimals
      System.out.print("Your entry hours rounded to one decimal place: ");
      DecimalFormat formattedDecimal = new DecimalFormat("0.0");
      //tells how the decimal will be formatted
      System.out.println(formattedDecimal.format(hours));
      //rounds inputed time to hold only one decimal place
   }
}


/*********Output************

Run 1: 

Please enter your first name with the first letter capital: 
Christopher
Please enter your last name with the first letter capital: 
Bray
Your full name is; Christopher Bray and it is 15 characters long.
Here it is in all uppercase CHRISTOPHER BRAY.
Here it is in all lowercase christopher bray.
The minimum hours you should spend on this class each week is 12.
The maximum hours you should spend on this class each week is 20.
Please enter how many hours you have spent this week on this class to 3 decimal places.
12.754
Your entry hours rounded to one decimal place: 12.8

Run 2:

Please enter your first name with the first letter capital: 
Joe
Please enter your last name with the first letter capital: 
Smith
Your full name is; Joe Smith and it is 8 characters long.
Here it is in all uppercase JOE SMITH.
Here it is in all lowercase joe smith.
The minimum hours you should spend on this class each week is 12.
The maximum hours you should spend on this class each week is 20.
Please enter how many hours you have spent this week on this class to 3 decimal places.
12.343
Your entry hours rounded to one decimal place: 12.3

 ****************************/