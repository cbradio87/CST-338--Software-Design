/*
 * Open Sourcerers
 * Nathan Jobes, Amy Gonzales, Jerome Ortega, Chris Bray
 * 
 * CST 338: Week 4
 * Optical Barcode Reader
 * 
 */

public class Assig4
{

   public static void main(String[] args)
   {
      
      DataMatrix alphabet = new DataMatrix();
      
      System.out.println("Reading in:\"\" ");
      alphabet.readText("");
      alphabet.generateImageFromText();
      alphabet.displayImageToConsole();
      alphabet.translateImageToText();
      alphabet.displayTextToConsole();
      
      System.out.println("Reading in: ABCDEFGHIJKLMNOPQRSTUVWXYZ"
            + "abcdefghijklmnopqrstuvwxyz");
      alphabet.readText("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz");
      alphabet.generateImageFromText();
      alphabet.displayImageToConsole();
      alphabet.translateImageToText();
      alphabet.displayTextToConsole();

      System.out.println("End of alphabet. \n\n");
      
      String[] sImageIn =
      {
         "                                               ",
         "                                               ",
         "                                               ",
         "     * * * * * * * * * * * * * * * * * * * * * ",
         "     *                                       * ",
         "     ****** **** ****** ******* ** *** *****   ",
         "     *     *    ****************************** ",
         "     * **    * *        **  *    * * *   *     ",
         "     *   *    *  *****    *   * *   *  **  *** ",
         "     *  **     * *** **   **  *    **  ***  *  ",
         "     ***  * **   **  *   ****    *  *  ** * ** ",
         "     *****  ***  *  * *   ** ** **  *   * *    ",
         "     ***************************************** ",  
         "                                               ",
         "                                               ",
         "                                               "

      };      
            
         
      
      String[] sImageIn_2 =
      {
            "                                          ",
            "                                          ",
            "* * * * * * * * * * * * * * * * * * *     ",
            "*                                    *    ",
            "**** *** **   ***** ****   *********      ",
            "* ************ ************ **********    ",
            "** *      *    *  * * *         * *       ",
            "***   *  *           * **    *      **    ",
            "* ** * *  *   * * * **  *   ***   ***     ",
            "* *           **    *****  *   **   **    ",
            "****  *  * *  * **  ** *   ** *  * *      ",
            "**************************************    ",
            "                                          ",
            "                                          ",
            "                                          ",
            "                                          "

      };

      BarcodeImage bc = new BarcodeImage(sImageIn);
      DataMatrix dm = new DataMatrix(bc);

      // First secret message
      dm.translateImageToText(); 
      dm.displayTextToConsole();
      dm.displayImageToConsole();

      // second secret message
      bc = new BarcodeImage(sImageIn_2); 
      dm.scan(bc);
      dm.translateImageToText();
      dm.displayTextToConsole();
      dm.displayImageToConsole();

      // create your own message
      dm.readText("What a great resume builder this is!");
      dm.generateImageFromText();
      dm.displayTextToConsole();
      dm.displayImageToConsole();

   }

}

interface BarcodeIO
{
   public boolean scan(BarcodeImage bc);

   public boolean readText(String text);

   public boolean generateImageFromText();

   public boolean translateImageToText();

   public void displayTextToConsole();

   public void displayImageToConsole();
}

class BarcodeImage implements Cloneable
{
   public static final int MAX_WIDTH = 65;
   public static final int MAX_HEIGHT = 30;

   private boolean[][] imageData;

   // Default Constructor - instantiates a
   // 2D array (MAX_HEIGHT x MAX_WIDTH) and stuffs
   // it all with blanks (false).
   BarcodeImage()
   {
      // creates an matrix of all false values
      imageData = new boolean[MAX_HEIGHT][MAX_WIDTH];
   }

   /**
    * Constructor that takes in an array of strings and fills the imageData array
    * with true if a '*' character is found, or false if a blank space is found.
    * This does not trim the input strings.
    * 
    * @param String[] strData
    */
   BarcodeImage(String[] strData)
   {
      // Creates default array
      imageData = new boolean[MAX_HEIGHT][MAX_WIDTH];

      if (checkSize(strData)) // If strData is a valid size
      {
         // Traverse starting from the back of the imageData array
         // to cut down on iterations.
         for (int i = MAX_HEIGHT - 1; i >= MAX_HEIGHT - strData.length; i--)
         {
            // If we get out of range on the strData array, break the loop.
            if (i - (MAX_HEIGHT - strData.length) < 0)
               break;

            // Grab the current string from strData, traverse and fill in
            // imageData appropriately.
            String currString = strData[i - (MAX_HEIGHT - strData.length)];
            for (int j = 0; j < MAX_WIDTH; j++)
            {
               if (i - (MAX_HEIGHT - strData.length) < 0)
                  break;

               if (j >= currString.length())
               {
                  break;
               } else if (currString.charAt(j) == '*')
               {
                  imageData[i][j] = true;
               } else
               {
                  imageData[i][j] = false;
               }
            }
         }
      }
   }

   /*
    * //Returns boolean value for specific pixel //returns false if null or out of
    * bounds
    */
   public boolean getPixel(int row, int col)
   {
      if (imageData != null && row < MAX_HEIGHT && col < MAX_WIDTH)
         return imageData[row][col];
      return false;
   }

   /*
    * Sets the boolean value for a valid location in imageData, returns true if a
    * value is successfully set, false otherwise
    */
   public boolean setPixel(int row, int col, boolean value)
   {
      // Make sure the field requested in valid
      // No null check as the constructors should not allow a null imageData[]
      if ((row >= 0 && col >= 0) && (row < MAX_HEIGHT && col < MAX_WIDTH))
      {
         imageData[row][col] = value;
         return true;
      } else
         return false;

   }

   // A private utility method
   // does the job of checking the incoming data for
   // every conceivable size or null error. Smaller is okay. Bigger
   // or null is not.
   private boolean checkSize(String[] data)
   {
      int size = data.length;
      boolean validData = false;
      // If the string[] does not contain at least the borderlines
      // and if it is within the max allowed height perform next check
      if (size >= 2 && size <= 30)
      {
         validData = true;
         for (int i = 0; i < size; i++)
         {
            if (data[i] == null)
            {
               validData = false;
               break;
            }
            // A row cannot have more than MAX_WIDTH elements
            else if (data[i].length() > MAX_WIDTH)
            {
               validData = false;
               break;
            }
         }
      }
      return validData;
   }

   /**
    * Traverses the imageData array and prints out a * or blank space based on the
    * boolean value at each position.
    */
   public void displayToConsole()
   {
      // Create a string for top and bottom of border
      String topBorder = "";
      for (int i = 0; i < MAX_WIDTH + 2; i++)
      {
         topBorder += "-";
      }

      System.out.println(topBorder); // Print top border
      for (int i = 0; i < MAX_HEIGHT; i++)
      {
         System.out.print("|"); // Add a pipe for left border
         for (int j = 0; j < MAX_WIDTH; j++)
         {
            // Print the corresponding character
            if (imageData[i][j] == true)
            {
               System.out.print("*");
            } else
            {
               System.out.print(" ");
            }
         }
         System.out.print("|"); // Add pipe to build right border
         System.out.printf("%n");
      }
      System.out.println(topBorder); // Add bottom border
   }

   /*
    * Amy Overrides method of this name in Cloneable interface returns copy
    */
   public BarcodeImage clone() throws CloneNotSupportedException
   {
      try
      {
         BarcodeImage copy = (BarcodeImage) super.clone();
         copy.imageData = imageData.clone();
         return copy;
      } catch (CloneNotSupportedException e)
      {
         return null; // do nothing
      }
   }
}

class DataMatrix implements BarcodeIO
{

   public static final char BLACK_CHAR = '*';
   public static final char WHITE_CHAR = ' ';

   private BarcodeImage image;
   private String text;
   private int actualWidth;
   private int actualHeight;

   /**
    * Default constructor. Sets blank image, empty text, and actual height and
    * width to 0.
    */
   DataMatrix()
   {
      image = new BarcodeImage();
      text = "";
      actualWidth = 0;
      actualHeight = 0;
   }

   /*
    * Sets the image but leaves the text at its default value. Call scan() and
    * avoid duplication of code here.
    */
   DataMatrix(BarcodeImage image)
   {
      this.image = new BarcodeImage();
      text = "";
      actualWidth = 0;
      actualHeight = 0;
      if (image != null)
         scan(image);
   }

   /*
    * sets the text but leaves image as default value
    */
   DataMatrix(String text)
   {
      this.text = "";
      if (text != null)
         readText(text);
      this.image = new BarcodeImage();
      this.actualHeight = 0;
      this.actualWidth = 0;
   }

   /*
    * Sets the text string, Couldn't find any restrictions on valid text input So
    * no input screening on top of what String already does
    */
   public boolean readText(String text)
   {

      if (text != null) // Make sure text exists
      {
         // Do not allow more than MAX_WIDTH -2 characters as must have room
         // for borderlines
         if (text.length() <= BarcodeImage.MAX_WIDTH - 2)
         {
            this.text = text;
            return true;
         }
         return false;
      }
      return false;
   }

   /*
    * Makes a clone of the input and sets it as the current image, then calls
    * cleans the image and computes its width
    */
   public boolean scan(BarcodeImage image)
   {
      boolean success = false;
      // attempt to get a copy of image
      try
      {
         this.image = image.clone();
         success = true;
      } catch (CloneNotSupportedException e)
      {
         success = false; // Technically redundant but safe
      }
      if (success)
      {
         cleanImage();
         actualWidth = computeSignalWidth();
         actualHeight = computeSignalHeight();
      }
      return success;
   }

   // returns actualWidth
   public int getActualWidth()
   {
      return this.actualWidth;
   }

   // Returns actualHeight
   public int getActualHeight()
   {

      return this.actualHeight;
   }

   /**
    * Computes the width of the barcode image.
    * 
    * @return int signalWidth
    */
   private int computeSignalWidth()
   {
      int signalWidth = 0;
      for (int i = 0; i < BarcodeImage.MAX_WIDTH; i++)
      {
         if (image.getPixel(BarcodeImage.MAX_HEIGHT - 1, i) == true)
            signalWidth++;
         else
            break;
      }

      return signalWidth;
   }

   /*
    * Assuming that the image is correctly situated in the lower-left corner of the
    * larger boolean array, these methods use the "spine" of the array (left and
    * bottom BLACK) to determine the actual size. Implementation of all BarcodeIO
    * methods.
    */
   private int computeSignalHeight()
   {
      int signalHeight = 0;
      for (int i = BarcodeImage.MAX_HEIGHT - 1; i >= 0; i--)
      {
         if (image.getPixel(i, 0) == true)
            signalHeight++;
         else
            break;
      }
      return signalHeight;
   }

   /*
    * Finds the corner of the spine and then shifts the image to be left and bottom
    * aligned
    */
   private void cleanImage()
   {
      // Variables to store the determined offset from standard
      int spacesFromBottomAligned = 0;
      int spacesFromLeftAligned = 0;

      // Start from the bottom row to search for the corner of the spine
      // Count to find how far from the bottom the barcode is
      for (int row = BarcodeImage.MAX_HEIGHT - 1; row >= 0; row--)
      {
         // SearchRowForSpine returns the value away from the left boundary
         spacesFromLeftAligned = searchRowForSpine(row);
         if (spacesFromLeftAligned != -1)// If the spine was found
         {
            shiftImageDown(spacesFromBottomAligned);
            shiftImageLeft(spacesFromLeftAligned);
            break;
         } else
         {
            spacesFromBottomAligned++;
         }
      }
   }

   /*
    * Writes every row of the image offset above the bottom, offset indices down
    *
    */
   private void shiftImageDown(int offset)
   {
      // Start bottom left
      for (int i = BarcodeImage.MAX_HEIGHT - 1; i >= offset; i--)
      {
         for (int j = 0; j < BarcodeImage.MAX_WIDTH; j++)
         {

            image.setPixel(i, j, image.getPixel(i - offset, j));
         }
      }

   }

   /*
    * Writes every column of the image offset to the left
    */
   private void shiftImageLeft(int offset)
   {
      for (int i = BarcodeImage.MAX_HEIGHT - 1; i >= offset; i--)
      {
         for (int j = 0; j < BarcodeImage.MAX_WIDTH; j++)
         {

            image.setPixel(i, j, image.getPixel(i, j + offset));
         }
      }
   }

   /*
    * TBD searches a given row for the spine returns the index of the column where
    * the spine is found or -1 if the spine is not in the row
    */
   private int searchRowForSpine(int row)
   {

      for (int j = 0; j < BarcodeImage.MAX_WIDTH; j++)
      {
         // First true we find from the left return its column index
         if (image.getPixel(row, j) == true)
            return j;

      }

      return -1;
   }

   /*
    * Generates the barcode representation of the current text string then scans
    * that image into dataMatrix
    */
   public boolean generateImageFromText()
   {
      clearImage();
      buildSpineForText();
      writeImage();
      return scan(image);
   }

   /**
    * Builds the vertical and horizontal spine as well as the top and right
    * borders. Assuming an 8-Bit ASCII to be the input
    */
   private void buildSpineForText()
   {
      // Loop through full Barcode space and build the top and bottom
      // Border lines
      for (int i = 0; i < text.length() + 2; i++)
      {
         // Build lower spine
         image.setPixel(BarcodeImage.MAX_HEIGHT - 1, i, true);

         // Build top open borderline
         // -10 results from assumption of 8-bit columns plus border
         if (i % 2 == 0)
            image.setPixel(BarcodeImage.MAX_HEIGHT - 10, i, true);
      }
      // Now build the left and right borderlines
      // -10 results from assumption of 8-bit columns plus border
      for (int i = BarcodeImage.MAX_HEIGHT - 1; i >= BarcodeImage.MAX_HEIGHT - 10; i--)
      {
         // Build left spine
         image.setPixel(i, 0, true);

         // Build right open borderline
         if (i % 2 != 0)
            image.setPixel(i, text.length() + 1, true);
      }
   }

   /**
    * Writes each character of a given string into the binary column representation
    * to build a BarcodeImage.
    */
   private void writeImage()
   {
      for (int j = 0; j < text.length(); j++) // For every character in text
      {
         // Lets convert the char into its binary representation
         int currentCharValue = (int) (text.charAt(j));
         String binary = Integer.toBinaryString(currentCharValue);

         int counter = 0;
         // Every time we see a 1 in the binary place a true in its position
         // in the image starting from the bottom going up
         for (int i = binary.length() - 1; i >= 0; i--)
         {
            if (binary.charAt(i) == '1')
            {
               image.setPixel(BarcodeImage.MAX_HEIGHT - 2 - counter, j + 1, true);
            }
            counter++;
         }
      }

   }

   /*
    * Reads the binary data from image and converts it to ASCII Characters returns
    * false if a column exceeds ASCII range
    */
   public boolean translateImageToText()
   {
      char[] temp = new char[actualWidth - 2];
      for (int j = 1; j < actualWidth - 1; j++) // Read each column
      {
         int sum = 0;
         int numberOfBools = actualHeight - 2;
         int firstDataRow = BarcodeImage.MAX_HEIGHT - 2;

         // compute the base 10 value of the column from base 2
         for (int i = firstDataRow; i > firstDataRow - numberOfBools; i--)
         {
            if (image.getPixel(i, j))
               sum += (int) Math.pow(2, firstDataRow - i);
         }
         if (sum < 256 && sum >= 0)
            temp[j - 1] = (char) sum;
         else // If a column exceeds ASCII range abort
         {
            return false;
         }

      }
      text = new String(temp);
      return true;
   }

   // Prints the value of text
   public void displayTextToConsole()
   {
      System.out.println(text);
   }

   /*
    * Displays a trimmed version of image, displaying only populated areas of
    * imageData[][]
    */
   public void displayImageToConsole()
   {
      // Building the correct length border for trimmed output
      String topBorder = "";
      for (int i = 0; i < actualWidth + 2; i++)
      {
         topBorder += "-";
      }
      // Print top border
      System.out.println(topBorder);
      int startPos = BarcodeImage.MAX_HEIGHT - actualHeight;
      for (int i = startPos; i < BarcodeImage.MAX_HEIGHT; i++)
      {
         System.out.print("|"); // Build left border
         for (int j = 0; j < actualWidth; j++)
         {
            if (image.getPixel(i, j) == true) // Print corresponding char
            {
               System.out.print(BLACK_CHAR);
            } else
            {
               System.out.print(WHITE_CHAR);
            }
         }
         System.out.print("|"); // build right border
         System.out.printf("%n");
      }
      System.out.println(topBorder); // Print bottom Border
   }

   /*
    * Displays the image without trimming
    */
   public void displayRawImage()
   {
      image.displayToConsole();
   }

   /*
    * Clears the image and sets actualWidth and actualHeight to 0
    */
   private void clearImage()
   {
      actualWidth = 0;
      actualHeight = 0;
      image = new BarcodeImage();
   }
}
/*
*********************************Output*****************************************

Reading in:"" 
----
|* |
|**|
|* |
|**|
|* |
|**|
|* |
|**|
|* |
|**|
----

Reading in: ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz
--------------------------------------------------------
|* * * * * * * * * * * * * * * * * * * * * * * * * * * |
|*                                                    *|
|***************************************************** |
|*                          ***************************|
|*               ***********               *********** |
|*       ********        ***       ********        ****|
|*   ****    ****    ****      ****    ****    ****    |
|* **  **  **  **  **  **  * **  **  **  **  **  **  **|
|** * * * * * * * * * * * * * * * * * * * * * * * * *  |
|******************************************************|
--------------------------------------------------------
ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz
End of alphabet. 


CSUMB CSIT online program is top notch.
-------------------------------------------
|* * * * * * * * * * * * * * * * * * * * *|
|*                                       *|
|****** **** ****** ******* ** *** *****  |
|*     *    ******************************|
|* **    * *        **  *    * * *   *    |
|*   *    *  *****    *   * *   *  **  ***|
|*  **     * *** **   **  *    **  ***  * |
|***  * **   **  *   ****    *  *  ** * **|
|*****  ***  *  * *   ** ** **  *   * *   |
|*****************************************|
-------------------------------------------
You did it!  Great work.  Celebrate.
----------------------------------------
|* * * * * * * * * * * * * * * * * * * |
|*                                    *|
|**** *** **   ***** ****   *********  |
|* ************ ************ **********|
|** *      *    *  * * *         * *   |
|***   *  *           * **    *      **|
|* ** * *  *   * * * **  *   ***   *** |
|* *           **    *****  *   **   **|
|****  *  * *  * **  ** *   ** *  * *  |
|**************************************|
----------------------------------------
What a great resume builder this is!
----------------------------------------
|* * * * * * * * * * * * * * * * * * * |
|*                                    *|
|***** * ***** ****** ******* **** **  |
|* ************************************|
|**  *    *  * * **    *    * *  *  *  |
|* *               *    **     **  *  *|
|**  *   * * *  * ***  * ***  *        |
|**      **    * *    *     *    *  * *|
|** *  * * **   *****  **  *    ** *** |
|**************************************|
----------------------------------------


********************************************************************************
*/


