class tNode<T> {
  private T item;
  private tNode<T> left;
  private tNode<T> right;
  
  public tNode() {
    this.item = null;
    this.left = null;
    this.right = null;
  }
  public tNode(T item) { //leaves on the tree
    this.item = item;
    this.left = null;
    this.right = null;
  }
  
  public tNode(tNode<T> left, tNode<T> right) { //branches
    this.item = null;
    this.left = left;
    this.right = right;
  }
      /** isLeaf
     * Checks if the node is a leaf (does not have a left or a right child)
     * @return A boolean value representing whether or not the node is a leaf (true means it is a leaf)
     */
  public boolean isLeaf () {
    
    // Check if the left and right children are null
    if (this.left == null && this.right == null) {
      
      // Since they're null, return true
      return true;
    }
    
    // Since either the left or right variables point to another node, return false
    return false;
  } // End of method
  
  public tNode<T> getLeft(){
    return this.left;
  }
  
  public tNode<T> getRight() {
    return this.right;
  }
  
  public void setLeft(tNode<T> left){
    this.left = left;
  }
  
  public void setRight(tNode<T> right) {
    this.right = right;
  }
  
  public T getItem() {
    return this.item;
  }
  
  
}