////**********A template  for a simple linked list ********
//import java.util.ArrayList;
//
//class linkTemplate {
//  
//  private static final int MAX = 10000;
//  private static final boolean canSet = true;
//  
//}

// ********************** Simple Linked List class in the linked list *********
class BinaryTree<E> {
  private tNode<E> head;
  
  private BinaryTree(tNode<E> forRoot) {
  head = forRoot;
 }

  public BinaryTree() {
 }
  
  public tNode<E> getHead() {
    return this.head;
  }
  public tNode<E> getRoot() {
    return this.head;
  }
  
  public String printTree(){
    if(head == null){return "";}
    if(head.getLeft() == null){return head.getItem().toString();}
    if(head.getLeft().getLeft() == null && head.getRight().getLeft() == null)
      return "(" + subTree(head.getLeft()).printTree() + " " + subTree(head.getRight()).printTree() + ")";
    return "(" + subTree(head.getLeft()).printTree() + subTree(head.getRight()).printTree() + ")";
  }
  public BinaryTree<E> subTree(tNode<E> forRoot){
    return new BinaryTree<E>(forRoot);
  }
  
  public void buildTree(tNode<E> only) {
    head = only;
  }
    public void buildTree(BinaryTree<E> only) {
    head = only.getHead();
  }
  public void buildTree(tNode<E> left, tNode<E> right){
    tNode<E> tempNode = new tNode<E>(left, right);
    head = tempNode;
  }
  
  public void buildTree(BinaryTree<E> left, BinaryTree<E> right) {
    tNode<E> tempNode = new tNode<E>(left.getHead(), right.getHead());
    head = tempNode;
  }
  
//  public addRightBranch
  /*
  public void makeTree(int[] frequencies) {
    int[] asciiCodes = new int[256];
    for (int i = 0; i < 256; i++) {
      asciiCodes[i] = i;
    }
    
    //sort method
    //using two parallel arrays, sort from highest frequency to lowest frequency, then mirror in other
    //sort using frequencies[] and mirror in asciiCodes[]
    boolean sorted = false;
    while (sorted = false) {
      sorted = true;
      
      //loop through frequencies[] and find the lowest
      for (int j = 0; j < frequencies.length - 2; j++) { //originally "frequencies.length - 2" was just 254
        if (frequencies[j] < frequencies[j+1]) {
          //switch
          int tempFrequencies = frequencies[j];
          frequencies[j] = frequencies[j+1];
          frequencies[j+1] = tempFrequencies;
          
          int tempAsciiCodes = asciiCodes[j];
          asciiCodes[j] = asciiCodes[j+1];
          asciiCodes[j+1] = tempAsciiCodes;
          
          sorted = false;
        }
      }
      
    }
    
    //truncate the array where everything after is a zero and reverse it
    //mirror that to the asciiCodes array
    boolean truncated = false;
    while (truncated = false) {
      int j = 0;
      
      if (frequencies[j] == 0 || j == frequencies.length-1) {
        int[] tempFrequencies = new int[j];
        int[] tempAsciiCodes = new int[j];
        
        for (int i = 0; i < j; i++) {
          tempFrequencies[i] = frequencies[j-(i+1)];
          tempAsciiCodes[i] = asciiCodes[j-(i+1)];
        }
        frequencies = tempFrequencies;
        asciiCodes = tempAsciiCodes;
        truncated = true;
      }
      
      j++;
    }
    
    //build that tree crap
    
    //make nodes, store in array from lowest to highest in sum of frequencies
    //leaves first
    SimpleLinkedList<tNode> nodeList1 = new SimpleLinkedList<tNode>();
    SimpleLinkedList<tNode> nodeList2 = new SimpleLinkedList<tNode>();
    for (int i = 0; i < frequencies.length; i++) {
      nodeList1.add(new tNode(asciiCodes[i], frequencies[i]));
    }
    
    //attach nodes to each other until 1 remaining, use 2 SimpleLinkedLists
    while(nodeList1.size() != 1 && nodeList2.size() != 1) {
      if (nodeList1.size() > 1) {
        //take the two smallest nodes (by frequency out), link them, put them into nodeList2

        while (nodeList1.size() > 0) {
          int smallOne = 0;
          int smallTwo;
          //run through the list and pick out the minimum frequency
          for(int i = 0; i < nodeList1.size(); i++) {
            int k = nodeList1.get(i).getFrequency();
            if (k < nodeList1.get(smallOne).getFrequency()) {
              smallOne = i;
            }
          }
          //start at 0 if smallOne is not the first
          if (smallOne == 0) {
            smallTwo = 1;
          } else {
            smallTwo = 0;
          }
          //get the second minimum
          for(int i = smallTwo; i < nodeList1.size(); i++) {
            int k = nodeList1.get(i).getFrequency();
//            System.out.println(smallTwo);
            if (k < nodeList1.get(smallTwo).getFrequency() && i != smallOne) {
              smallTwo = i;
            }
            
          }
          //make the node
          int totalFrequency = nodeList1.get(smallOne).getFrequency() + nodeList1.get(smallTwo).getFrequency();
          nodeList2.add(new tNode(totalFrequency, nodeList1.get(smallOne), nodeList1.get(smallTwo)));
          
          if (smallOne > smallTwo) {
            nodeList1.remove(smallOne);
            nodeList1.remove(smallTwo);
          } else {
            nodeList1.remove(smallTwo);
            nodeList1.remove(smallOne);
          }
          
          //if there is one node remaining in the nodeList, move it to the other nodeList
          if (nodeList1.size() == 1) {
            nodeList2.add(nodeList1.get(0));
            nodeList1.remove(0);
          }
        }

        nodeList1.clear(); //obligatory nodeList clear
      }
      if (nodeList2.size() > 1) {
        //take the two smallest nodes (by frequency out), link them, put them into nodeList1

        while (nodeList2.size() > 0) {
          int smallOne = 0;
          int smallTwo;
          //run through the list and pick out the minimum frequency
          for(int i = 0; i < nodeList2.size(); i++) {
            int k = nodeList2.get(i).getFrequency();
            if (k < nodeList2.get(smallOne).getFrequency()) {
              smallOne = i;
            }
          }
          //start at 0 if smallOne is not the first
          if (smallOne == 0) {
            smallTwo = 1;
          } else {
            smallTwo = 0;
          }
          //get the second minimum
          for(int i = smallTwo; i < nodeList2.size(); i++) {
            int k = nodeList2.get(i).getFrequency();
//            System.out.println(smallTwo);
            if (k < nodeList2.get(smallTwo).getFrequency() && i != smallOne) {
              smallTwo = i;
            }
            
          }
          int totalFrequency = nodeList2.get(smallOne).getFrequency() + nodeList2.get(smallTwo).getFrequency();
          nodeList1.add(new tNode(totalFrequency, nodeList2.get(smallOne), nodeList2.get(smallTwo)));
          if (smallOne > smallTwo) {
            nodeList2.remove(smallOne);
            nodeList2.remove(smallTwo);
          } else {
            nodeList2.remove(smallTwo);
            nodeList2.remove(smallOne);
          }
          //if there is one node remaining in the nodeList, move it to the other nodeList
          if (nodeList2.size() == 1) {
            nodeList1.add(nodeList2.get(0));
            nodeList2.remove(0);
          }
        }
        nodeList2.clear();
      }
      
    }
    
    if (nodeList1.size() == 1) {
      head = nodeList1.get(0);
    } else if (nodeList2.size() == 1) {
      head = nodeList2.get(0);
    }
    //okay now i have a tree, what u want me to do with it

    
  }
  */
  
  
  //might need this later but for now no***********************************************
//  public E get(int index) {
//    tNode<E> temptNode = head;
//    if (temptNode == null || index > size()) {
//      return null;
//    }
//    for (int i = 0; i < index; i++) {
//      temptNode = temptNode.getNext();
//    }
//    
//    
//    return temptNode.getItem();
//  }

//  public void getTree() {
//    //output the tree
//  }
//  
//  public void getTreeRespresentation() {
//    //output the tree, but in that weird representation thing
//  }
  
  
  public void clear() { //done
    head = null;
  }
  
  
}

//class tNode<E> {
//  private int asciiCode;
//  private int frequency;
//  private tNode<E> left;
//  private tNode<E> right;
//  
//  public tNode() {
//    this.asciiCode = -1;
//    this.frequency = 0;
//    this.left = null;
//    this.right = null;
//  }
//  public tNode(int asciiCode, int frequency) { //leaves on the tree
//    this.asciiCode = asciiCode;
////    this.asciiCodeList = new SimpleLinkedList<E>();
//    this.frequency = frequency;
//    this.left = null;
//    this.right = null;
//  }
//  
//  public tNode(int frequency, tNode<E> left, tNode<E> right) { //branches
//    this.asciiCode = -1;
//    this.frequency = frequency;
//    this.left = left;
//    this.right = right;
//  }
//  public tNode(int asciiCode, int frequency, tNode<E> left, tNode<E> right) {
//    this.asciiCode = asciiCode;
//    this.frequency = frequency;
//    this.left = left;    this.right = right;
//  }
//  
//  public tNode<E> getLeft(){
//    return this.left;
//  }
//  
//  public tNode<E> getRight() {
//    return this.right;
//  }
//  
//  public void setLeft(tNode<E> left){
//    this.left = left;
//  }
//  
//  public void setRight(tNode<E> right) {
//    this.right = right;
//  }
//  
//  public int getAsciiCode(){
//    return this.asciiCode;
//  }
//  
//  public int getFrequency() {
//    return this.frequency;
//  }
//  
//}