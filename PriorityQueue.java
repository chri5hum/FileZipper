class PriorityQueue<T> {
  
  private PriorityNode<T> front;
  private PriorityNode<T> back;
  
  public boolean isEmpty() {
    if (back == null && front == null) {
      return true;
    } else {
      return false;
    }
  }
  public int size() {
    PriorityNode<T> tempNode = back;
    int i = 1;
    if (tempNode == null) {
      return 0;
    }
    while(tempNode.getAhead() != null) {
      tempNode = tempNode.getAhead();
      i++;
    }
    return i;
  }
  
  public void insert(T item, int priority) {
    PriorityNode<T> tempNode = new PriorityNode<T>(item, priority);
    PriorityNode<T> checkNode = back;
    if (front == null && back == null) { //if there is nothing, makes the first
      back = tempNode;
      front = tempNode;
      return;
    } else if (back.getAhead() == null && front.getBehind() == null) {//only one
//      System.out.println("two");
      if (tempNode.getPriority() >= back.getPriority()) { //greater priority than back
        back.setAhead(tempNode);
        tempNode.setBehind(back);
        front = tempNode;
//        System.out.println("inserted: " + tempNode.getItem() + " at front");
        return;
      } else if (tempNode.getPriority() < back.getPriority()) {//lesser priority than back
        back.setBehind(tempNode);
        tempNode.setAhead(back);
        back = tempNode;
//        System.out.println("inserted: " + tempNode.getItem() + " at back");
        return;
      }
    }
    //checkNode is back
    //tempNode is the one you want to place
    while (checkNode.getPriority() <= tempNode.getPriority() && checkNode.getAhead() != null) {
      checkNode = checkNode.getAhead();
    }
    //now checkNode should be higher priority than tempNode
    if (checkNode.getAhead() == null) { //if at the front of the queue
      if (checkNode.getPriority() <= tempNode.getPriority()) {
        checkNode.setAhead(tempNode);
        tempNode.setBehind(checkNode);
        front = tempNode;
      } else if (checkNode.getPriority() > tempNode.getPriority()) {
        checkNode.getBehind().setAhead(tempNode);
        tempNode.setBehind(checkNode.getBehind());
        tempNode.setAhead(checkNode);
        checkNode.setBehind(tempNode);
      }
    } else if (checkNode.getBehind() == null) { //if at the back of the queue
      checkNode.setBehind(tempNode);
      tempNode.setAhead(checkNode);
      back = tempNode;
    } else { //if neither
      checkNode.getBehind().setAhead(tempNode);
      tempNode.setBehind(checkNode.getBehind());
      tempNode.setAhead(checkNode);
      checkNode.setBehind(tempNode);
    }
    
  }
  
  public PriorityNode<T> dequeue() {
    if (front == null) {
      return null;
    }
    if (front.getBehind() != null) { //more than one
      PriorityNode<T> tempNode = front;
      front = front.getBehind();
      front.setAhead(null);
      return tempNode;
    } else { //only one
      PriorityNode<T> tempNode = front;
      clear();
      return tempNode;
    }
  }

  
  public PriorityNode<T> dequeueReverse() { //this is me cheating, so i can use frequencies for priority and not screw myself
    if (back == null) {
      return null;
    }
    if (back.getAhead() != null) { //more than one
      PriorityNode<T> tempNode = back;
      back = back.getAhead();
      back.setBehind(null);
      return tempNode;
    } else {
      PriorityNode<T> tempNode = back;
      clear();
      return tempNode;
    }
  }
  
  
  public void clear() {
    front = null;
    back = null;
  }
  
  //peek methods
  //do this if u have time, not like its gonna be used in the program anyways
  
}


class PriorityNode<T> {
  private T item;
  private int priority;
  private PriorityNode<T> ahead;
  private PriorityNode<T> behind;
  
  
  public PriorityNode(T item, int priority) {
    this.item=item;
    this.priority = priority;
    this.ahead=null;
    this.behind=null;
  }
  
  public PriorityNode(T item, int priority, PriorityNode<T> ahead, PriorityNode<T> behind) {
    this.item=item;
    this.priority = priority;
    this.ahead=ahead;
    this.behind=behind;
  }
  
  public PriorityNode<T> getBehind(){
    return this.behind;
  }
  
  public PriorityNode<T> getAhead() {
    return this.ahead;
  }
  
  public int getPriority() {
    return priority;
  }
  
  public void setBehind(PriorityNode<T> behind){
    this.behind = behind;
  }
  
  public void setAhead(PriorityNode<T> ahead) {
    this.ahead = ahead;
  }
  
  public T getItem(){
    return this.item;
  }
}