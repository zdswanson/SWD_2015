// This code was modified by Zach Swanson from Fig. 21.5: ListTest.java
// ListTest class to demonstrate List capabilities.

/**
 * Tests List class
 * 
 * @author Zach Swanson
 * @author Deitel & Associates, Inc. and Pearson Education, Inc.
 * @version 1.0
 */
public class ListTest 
{
   /**
    * Tests List class
    * 
    * @param args no args expected
    */
public static void main(String[] args)
   {
      List<Integer> list = new List<>(); 

      // insert integers in list
      list.insert(-1,0);
      list.print();
      list.insert(0,0);
      list.print();
      list.insert(2, 1);
      list.print();
      list.insert(7, 100);
      list.print();
      list.insertAtBack(1);
      list.print();
      list.insertAtBack(5);
      list.print();

      // remove objects from list; print after each removal
      try 
      { 
         int removedItem = list.remove(0);
         System.out.printf("%n%d removed%n", removedItem);
         list.print();

         removedItem = list.remove(1);
         System.out.printf("%n%d removed%n", removedItem);
         list.print();
         
         removedItem = list.remove(100);
         System.out.printf("%n%d removed%n", removedItem);
         list.print();
         
         removedItem = list.remove(0);
         System.out.printf("%n%d removed%n", removedItem);
         list.print();

         removedItem = list.removeFromBack();
         System.out.printf("%n%d removed%n", removedItem);
         list.print();

         removedItem = list.removeFromBack();
         System.out.printf("%n%d removed%n", removedItem);
         list.print();
      } 
      catch (EmptyListException emptyListException) 
      {
         emptyListException.printStackTrace();
      } 
   } 
} // end class ListTest


/**************************************************************************
 * (C) Copyright 1992-2014 by Deitel & Associates, Inc. and               *
 * Pearson Education, Inc. All Rights Reserved.                           *
 *                                                                        *
 * DISCLAIMER: The authors and publisher of this book have used their     *
 * best efforts in preparing the book. These efforts include the          *
 * development, research, and testing of the theories and programs        *
 * to determine their effectiveness. The authors and publisher make       *
 * no warranty of any kind, expressed or implied, with regard to these    *
 * programs or to the documentation contained in these books. The authors *
 * and publisher shall not be liable in any event for incidental or       *
 * consequential damages in connection with, or arising out of, the       *
 * furnishing, performance, or use of these programs.                     *
 *************************************************************************/
