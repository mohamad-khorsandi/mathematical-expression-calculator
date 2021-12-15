package com.company.dateStructurs;

public class tree<T> {
    public tree(T value) {
        this.setRoot(value);
    }
    public tree() {
    }

    private int size = 0;
    private Node root = null;

    public Node setRoot(T value) throws IllegalStateException{
        if (! isEmpty()) throw new IllegalStateException("tree is not empty");

        this.root = new Node(value, null, null ,null);
        size++;
        return this.root;
    }

    public Node getRoot() {
        return root;
    }

    public int getSize() {
        return size;
    }

    boolean isEmpty(){
        return size == 0;
    }

    public class Node{
        T value;
        private Node parent;
        private Node left;
        private Node right;

        public Node(T value, Node parent, Node left, Node right) {
            this.value = value;
            this.parent = parent;
            this.left = left;
            this.right = right;
        }

        public T setValue(T newValue) {
            T prevVal = this.value;
            this.value = newValue;
            return prevVal;
        }

        public T getValue() {
            return value;
        }

        public Node setLeft(T value) throws IllegalStateException{
            if (this.left != null)
                throw new IllegalStateException("this node already has a left child");

            size++;
            this.left = new Node(value, this, null, null);
            return this.left;
        }

        public Node setRight(T value) throws IllegalStateException{
            if (this.right != null)
                throw new IllegalStateException("this node already has a right child");

            size++;
            this.right = new Node(value, this, null, null);
            return this.right;
        }

        public void setLeft(Node left) {
            if (this.left != null)
                throw new IllegalStateException("this node already has a left child");

            this.left = left;
        }

        public void setRight(Node right) {
            if (this.right != null)
                throw new IllegalStateException("this node already has a right child");

            this.right = right;
        }

        public Node getParent() {
            return parent;
        }

        public Node getLeft() {
            return left;
        }

        public Node getRight() {
            return right;
        }
    }

    public void print(){
        innerPrint(this.root, 0);
    }

    /**
     * print Nodes with following order :
     * LVR
     */
    private void innerPrint(Node root, int numOfTabs){
        if (root == null)
            return;
        
        innerPrint(root.left, numOfTabs + 1);
        System.out.println("    ".repeat(numOfTabs)+root.getValue());
        innerPrint(root.right, numOfTabs + 1);
    }
}
