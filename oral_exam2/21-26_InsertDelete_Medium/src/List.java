// This code was modified by Zach Swanson from Fig. 21.3: List.java
// ListNode and List class declarations.

/**
 * Implements a single node in a linked list
 * 
 * @author Zach Swanson
 * @author Deitel & Associates, Inc. and Pearson Education, Inc.
 * @param <T>	type of data
 */
class ListNode<T> 
{
	/**
	 * Data contained in node
	 */
	T data; // data for this node
	/**
	 * Reference to the next node in the list
	 */
	ListNode<T> nextNode;

	/**
	 * Creates node with empty nextNode
	 * 
	 * @param object data for this node
	 */
	ListNode(T object) 
	{ 
		this(object, null); 
	}  

	// constructor creates ListNode that refers to the specified
	// object and to the next ListNode
	/**
	 * Creates node containing object and pointing to next node in List
	 * 
	 * @param object	data for this node
	 * @param node		reference to next node in List
	 */
	ListNode(T object, ListNode<T> node)
	{
		data = object;    
		nextNode = node;  
	} 

	/**
	 * @return data in node
	 */
	T getData() 
	{ 
		return data; 
	} 

	// return reference to next node in list
	/**
	 * @return next node in List
	 */
	ListNode<T> getNext() 
	{ 
		return nextNode; 
	} 
} // end class ListNode<T>

// class List definition
/**
 * Implements a linked list
 * 
 * @author Invicti
 * @author Deitel & Associates, Inc. and Pearson Education, Inc.
 * @param <T>	type of data
 */
public class List<T>
{
	/**
	 * Reference to first node in list
	 */
	private ListNode<T> firstNode;
	/**
	 * Reference to last node in list
	 */
	private ListNode<T> lastNode;
	/**
	 * Name of list
	 */
	private String name; // string like "list" used in printing

	// constructor creates empty List with "list" as the name
	/**
	 * Creates new linked list with default name "list"
	 */
	public List() 
	{ 
		this("list"); 
	} 

	// constructor creates an empty List with a name
	/**
	 * Creates new linked list with given name
	 * 
	 * @param listName 	name of this List
	 */
	public List(String listName)
	{
		name = listName;
		firstNode = lastNode = null;
	} 

	/**
	 * Inserts a node containing insertItem at location index of List
	 * 
	 * @param insertItem	data for new node
	 * @param index			index of new node
	 */
	public void insert(T insertItem, int index){
		if (isEmpty())  // firstNode and lastNode refer to same object
			firstNode = lastNode = new ListNode<T>(insertItem);	
		else{												
			if(index==0){													//if inserting at beginning of list
				firstNode = new ListNode<T>(insertItem, firstNode);			//implement given insertAtFront method
			}
			else{	
				int i=0;
				ListNode<T> node=firstNode;
				while(i<index-1&&node.nextNode!=null){						//otherwise increment to intended node, or end of List, whichever is first
					i++;
					node=node.getNext();
				}	
				node.nextNode = new ListNode<T>(insertItem,node.nextNode);	//insert new node at current location and pointing at next location
				if(i<index){
					lastNode=node.nextNode;									//if this new node is the last node in the List, update lastNode
				}
			}
		}
	}



	/*// insert item at front of List
   public void insertAtFront(T insertItem)
   {
      if (isEmpty()) // firstNode and lastNode refer to same object
         firstNode = lastNode = new ListNode<T>(insertItem);
      else // firstNode refers to new node
         firstNode = new ListNode<T>(insertItem, firstNode);
   } */

	// insert item at end of List
	/**
	 * Inserts a node containing insertItem at the end of the linked list
	 * 
	 * @param insertItem		data for new node
	 */
	public void insertAtBack(T insertItem)
	{
		if (isEmpty()) // firstNode and lastNode refer to same object
			firstNode = lastNode = new ListNode<T>(insertItem);
		else // lastNode's nextNode refers to new node
			lastNode = lastNode.nextNode = new ListNode<T>(insertItem);
	} 

	/**
	 * Removes a node from the given index
	 * 
	 * @param index		location of node to remove
	 * @return			value held in node
	 * @throws EmptyListException
	 */
	public T remove(int index) throws EmptyListException{
		if (isEmpty()){ // throw exception if List is empty
			throw new EmptyListException(name);
		}
		if(index==0){											//implement given removeFromFront
			T removedItem = firstNode.data; 
			if (firstNode == lastNode)
				firstNode = lastNode = null;
			else
				firstNode = firstNode.nextNode;
			return removedItem; 
		}
		int i=0;
		ListNode<T> node=firstNode;
		while(i<index-1&&node.nextNode.nextNode!=null){			//otherwise increment to node before intended node or second to last node in array, whichever's first
			i++;
			node=node.nextNode;
		}

		T removedItem = node.nextNode.data;						//store data in target node

		if(node.nextNode==lastNode){							//if second to last node
			lastNode = node; 									//update lastNode
			node.nextNode = null;								//remove target node
		}
		else{													//otherwise, make node before intended node point to node after intended node, deleting it
			node.nextNode=node.nextNode.nextNode;
		}
		return removedItem;										//return data from node
	}
	// remove first node from List
	/*public T removeFromFront() throws EmptyListException
   {
      if (isEmpty()) // throw exception if List is empty
         throw new EmptyListException(name);

      T removedItem = firstNode.data; // retrieve data being removed

      // update references firstNode and lastNode 
      if (firstNode == lastNode)
         firstNode = lastNode = null;
      else
         firstNode = firstNode.nextNode;

      return removedItem; // return removed node data
   } // end method removeFromFront*/

	// remove last node from List
	/**
	 * Removes last node in linked list
	 * 
	 * @return		data in node
	 * @throws EmptyListException
	 */
	public T removeFromBack() throws EmptyListException
	{
		if (isEmpty()) // throw exception if List is empty
			throw new EmptyListException(name);

		T removedItem = lastNode.data; // retrieve data being removed

		// update references firstNode and lastNode
		if (firstNode == lastNode)
			firstNode = lastNode = null;
		else // locate new last node
		{ 
			ListNode<T> current = firstNode;

			// loop while current node does not refer to lastNode
			while (current.nextNode != lastNode)
				current = current.nextNode;

			lastNode = current; // current is new lastNode
			current.nextNode = null;
		} 

		return removedItem; // return removed node data
	} 

	// determine whether list is empty
	/**
	 * @return true if this List is empty, false otherwise
	 */
	public boolean isEmpty()
	{ 
		return firstNode == null; // return true if list is empty
	} 

	// output list contents
	/**
	 * Prints contents of this List to System.out
	 */
	public void print()
	{
		if (isEmpty()) 
		{
			System.out.printf("Empty %s%n", name);
			return;
		}

		System.out.printf("The %s is: ", name);
		ListNode<T> current = firstNode;

		// while not at end of list, output current node's data
		while (current != null) 
		{
			System.out.printf("%s ", current.data);
			current = current.nextNode;
		}

		System.out.println();
	} 
} // end class List<T>

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
