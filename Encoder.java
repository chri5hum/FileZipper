/**
 * Encoder.java
 * By: Chris Hum
 * Date: April 19 2018
 * Takes in a file and encodes it, using a binary tree based on the frequency of characters
 * Outputs encoded into a ".MZIP" file called "Encoded"
 * Layout of "Encoded.MZIP"
 * original file name.FILE EXTENSION IN ALL CAPS
 * string representation of the tree
 * number of extra bits
 * encoded part + extra bits
 */

//important java inputs
import java.io.BufferedOutputStream;
import javax.swing.JOptionPane;
import java.util.ArrayList;
import java.util.Scanner;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.util.HashMap;

public class Encoder {
  
  //static variables to be used later
  static int[] frequencies; //stores frequencies of each character (in terms of int value)
  static  HashMap<Integer, String> directions; //stores directions to each leaf of the tree
  static String fileName; //stores the file name
  static int index; //stores the number of characters in a file
  
  public static void main(String[] args) throws Exception {
    
    //creates a new binary tree and an int array to store frequencies
    BinaryTree<Integer> realTree = new BinaryTree<Integer>();
    frequencies = new int[256];
    
    //when the I get the file and convert the characters to ints, it gets stored here
    int[] buffer = readData(); //gets the data using a method

    PriorityQueue<tNode<Integer>> nodeQueue = new PriorityQueue<tNode<Integer>>(); //i use this queue to make nodes
    PriorityQueue<BinaryTree<Integer>> queueOne = new PriorityQueue<BinaryTree<Integer>>(); //i use this queue to make trees

    //dump in everything that doesnt have 0 frequency
    for (int i = 0; i < frequencies.length; i++) {
      if (frequencies[i] > 0) {
        nodeQueue.insert(new tNode<Integer>(i), frequencies[i]); //makes the initial leaves
      }
    }
    //make mini trees (that are literally just nodes)
    while (nodeQueue.size() > 1) { //make BinaryTrees and store in queueOne, taking two nodes at a time
      PriorityNode<tNode<Integer>> itemOne = nodeQueue.dequeueReverse();
      PriorityNode<tNode<Integer>> itemTwo = nodeQueue.dequeueReverse();
      int sumFrequency = itemOne.getPriority() + itemTwo.getPriority(); //i used frequency for the priority, then just had a dequeueReverse method in my pirority list
      BinaryTree<Integer> tempTree = new BinaryTree<Integer>();
      tempTree.buildTree(itemOne.getItem(), itemTwo.getItem());
      queueOne.insert(tempTree, sumFrequency);
    }
    
    //at this point there should either be one or zero nodes
    if (nodeQueue.size() > 0) { //take the last node (if it exists)
      PriorityNode<tNode<Integer>> itemOne = nodeQueue.dequeueReverse();
      BinaryTree<Integer> tempTree = new BinaryTree<Integer>(); //put the node into its own tree
      tempTree.buildTree(itemOne.getItem());
      queueOne.insert(tempTree, itemOne.getPriority()); //put tree into queueOne
    }

    
    //makes the actual tree out of mini-trees
    //take the top two (lowest priority) and combine the tree
    //then put the new tree back into the queue
    //until only one tree left
      while (queueOne.size() > 1) {
        PriorityNode<BinaryTree<Integer>> itemOne = queueOne.dequeueReverse(); //already stated up there, but I used frquency = priority, then made a dequeueReverse method
        PriorityNode<BinaryTree<Integer>> itemTwo = queueOne.dequeueReverse();
        int sumFrequency = itemOne.getPriority() + itemTwo.getPriority();
        BinaryTree<Integer> tempTree = new BinaryTree<Integer>();
        tempTree.buildTree(itemOne.getItem().getHead(), itemTwo.getItem().getHead());
        queueOne.insert(tempTree, sumFrequency);
        
      }
   
      realTree.buildTree(queueOne.dequeueReverse().getItem().getHead()); //make realTree the tree, empty the queues
    
      //this is used to store directions from the root(head) of the tree to each leaf
      directions = new HashMap<Integer, String>();
    
      //recursive method to get strings that correspond to directions on the tree
      getDirections(realTree.getHead(), "");
      
      //outputs the tree
      output(buffer, realTree);   
    
  }
  
  /** output
    * method to output the encoded file to a new file
    * @param buffer byte[] the byte array that stores the original text, used when converting to tree directions
    * @param realTree takes in a binary tree so that you can call the tree to string conversion method
    */
  private static void output(int[] buffer, BinaryTree<Integer> realTree) throws Exception{
    File file = new File("Encoded.MZIP");
    try {
      if (!file.exists()) {
        file.createNewFile();
      }
      
      //use this to determine extra bits
      StringBuilder entireThing = new StringBuilder ();
      
      for (int i = 0; i < index; i++) { //does not catch the last 2 characters of buffer but buffer should be big enough anyways that this doesnt matter anyways
        entireThing.append(directions.get((int)(buffer[i]))); //put everything (in terms of tree directions) into a string
      }
      
      int extraBits = 8 - (entireThing.length()%8); //find remainder when divided by 8
      
      //output file name and extension in all caps
      int i = fileName.lastIndexOf(".");
      
      //output the string
      FileOutputStream outputStream = new FileOutputStream(file);

      String printOne = fileName.substring(0, i) + "" + fileName.substring(i).toUpperCase(); //print the original file name, but with extension in all caps
      byte[] stuffByte = printOne.getBytes(); //the actual content
      byte[] enterByte = {13,10}; //a carriage-return and new line
      outputStream.write(stuffByte); //print the content
      outputStream.write(enterByte); //print the carriage return and new line
      
      String stringTree = encodeTreeStart(realTree); //get tree (this is shane's code)
      stuffByte = stringTree.getBytes(); //turn it into a byte array so it can be printed
      outputStream.write(stuffByte); //print the string tree
      outputStream.write(enterByte); //print a carriage-return and new line
      
      //prints the number of extra bits, and then a carriage return and new line
      String extraLength = "" + extraBits;
      stuffByte = extraLength.getBytes();
      outputStream.write(stuffByte);
      outputStream.write(enterByte);
      
      //close the FileOutputStream can't print all any ascii characters with decimals 129 to 159
      outputStream.flush();
      outputStream.close();
      
      //new outputStream
      BufferedOutputStream outputStream2 = new BufferedOutputStream(new FileOutputStream(file, true));
      
      StringBuilder printThing = new StringBuilder ();
      
      for (int k = 0; k < index; k++) { //does not catch the last 2 characters of buffer but buffer should be big enough anyways that this doesnt matter anyways
        
        //add to the stringBuilder
        printThing.append(directions.get((int)(buffer[k])));
        
        
        while (printThing.length() >= 8) { //if more than or equal to 8 characters, convert to ascii character and write to file
          String firstEight = printThing.substring(0,8);
          
          int intPrint = binaryToDecimal(firstEight); //the decimal of the ascii character you want to print
          outputStream2.write(intPrint);          //print it
          
          //delete from string after converting to ascii and outputing to file
          printThing.delete(0,8);
        }
       
      }
      
      //append 0s to the end (i think there is one extra here)
      for (int k = printThing.length(); k <= 8; k++) {
        printThing.append("0");
      }

      //cut off the extra zero while turning to string
      String firstEight = printThing.substring(0,8);
      outputStream2.write(binaryToDecimal(firstEight));
      
      //close the outputStream
      outputStream2.close();
    }
    catch (FileNotFoundException e) {
      System.out.println("File not found");
    } catch (IOException e) {
      System.out.println("Error IO");
      e.printStackTrace();
    }
  }
  
  /** getDirections
    * recursive method to find directions to each ascii code character
    * @param current the head of the tree
    * @param s just a string of "", because you don't want to declare a new String s every time
    */ 
  private static void getDirections(tNode<Integer> current, String s) { //take in head
    
    //if left has something, then go the left, add 0 to the front of the string
    if (current.getLeft() != null) {
      getDirections(current.getLeft(), s + "0");
    }
    //if right has something, then go the left, add 1 to the front of the string
    if (current.getRight() != null) {
      getDirections(current.getRight(), s + "1");
    }
    
    //when reaching a node, get the key, then put the corresponding string in the hash map
    if (current.getRight() == null && current.getLeft() == null) {
      directions.put(current.getItem(), s);
      return;
    }
   }
   
  /** readData
    * opens an inputDialog so obtain filename from user
    * reads the data in the file and stores in a byte array
    * counts frequency of character while reading
    */
  private static int[] readData() {
    
    //get the file name
    fileName = JOptionPane.showInputDialog("Input the file name (capitalization and extension is required):");

    // Use this for reading the data.
    int[] buffer = new int[100000000]; //this must be longer than the data
    try {
      //uses FileInputStream to get data from file
      FileInputStream inputStream = new FileInputStream(new File(fileName));
      int next;//this temporarily stores the int value of the read character
      index = 0; //sets the number of characters counted to zero
      
      //counts frequencies
      while((next = inputStream.read()) != -1 && index < buffer.length-1) {
        
        //writes the character (as an int) to the int array buffer
        buffer[index] = next;
        
        (frequencies[next])++; //count frequency of characters
        index++; //increases index by one
        
      }

      //close the inputStream
      inputStream.close();
      
    } catch (FileNotFoundException e) {
      System.out.println("File not found");
    } catch (IOException e) {
      System.out.println("Error IO");
      e.printStackTrace();
    }
    return buffer;
  }

  
  /** binaryToDecimal
    * converts an 8 bit binary string to an int
    * so I can get ascii codes
    * @param binaryString The string to be converted into the int
    * @return The int value of the string
    */
  private static int binaryToDecimal (String binaryString) {
    
    // a variable to store the decimal value
    int decimal = 0;
    
    // loops 8 times
    for (int i = 7;i>=0;i--) {
      
      // checks if the number at position 0 of the string is a 1
      if (binaryString.substring(0,1).equals("1")) {
        
        // adds 2^i to the decimal value
        decimal += Math.pow (2,i);
      }
      
      // sets binaryString to go from position 1 and on
      binaryString = binaryString.substring(1);
    }
    
    return decimal;
  }
    
  //note that this is either william's or shane's code (NOT MINE)
  //THE CODE BELOW IS NOT MY CODE
  /** encodeTree
    * This method will recursively read through the nodes of the binary tree and translate their location into string form
    * @param node binary node that is currently being checked for children, data and whether they are a leaf
    * @return A completed string representing the enitre binary tree
    */
  
  
    private static String encodeTreeStart(BinaryTree <Integer> fullTree){
      //check if root/head is actually a leaf that points to some data item
        if (fullTree.getRoot().getItem()==null){
            return encodeTree(fullTree.getRoot());
        }else{
          //this case is where there is only 1 data item that is contained in the root/head of the tree
            return "("+fullTree.getRoot().getItem()+")";
        }
    }
    private static String encodeTree(tNode <Integer> node){
      //create temporary string to store all string values below current level of the node
        String temp="";
        //check if there is a branch to the left, which indicates more nodes deeper in the tree, and therefore will 
        //create more items in the string to the left side of the space, as the space is a seperator between left and right nodes
        if (node.getLeft().getItem() == null){
            temp = encodeTree(node.getLeft())+temp+" ";
        }else if (node.getLeft().isLeaf()){ //if its a leaf, add data to the string and then continue to right side if statements
            temp = Integer.toString(node.getLeft().getItem())+temp+" ";
        }
        
        //  //check if there is a branch to the right
        if (node.getRight().getItem() ==null){
            temp = temp+encodeTree(node.getRight());
        }else if (node.getRight().isLeaf()){   //if its a leaf, add data to the string and then continue to return statement
            temp =  temp+Integer.toString(node.getRight().getItem());
        }
        //return the string value of the node and its children plus brackets around the string as brackets indicate seperate branches
        //if there are many children in one branch, the brackets will seperate each BRANCH or connecting nodes, leaves are represented by actual data values,
        //and the # of brackets around a leaf represents how many levels deep the leaf is in branches
        return "("+temp+")";
    }
  
}