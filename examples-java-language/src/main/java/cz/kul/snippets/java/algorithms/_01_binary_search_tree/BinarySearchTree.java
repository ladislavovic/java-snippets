package cz.kul.snippets.java.algorithms._01_binary_search_tree;

public class BinarySearchTree {
    
    private Node root;

    public BinarySearchTree() {
    }
    
    public void add(int value) {
        Node newNode = new Node(value);
        if (root == null) {
            root = newNode;
            return;
        }
        Node node = root;
        while (true) {
            if (value < node.payload) {
                if (node.left != null) {
                    node = node.left;
                    continue;
                } else {
                    node.left = newNode;
                }
            } else if (value > node.payload) {
                if (node.right != null) {
                    node = node.right;
                    continue;
                } else {
                    node.right = newNode;
                }
            }
            break;
        }
    }
    
    public boolean search(int value) {
        Node node = root;
        while (node != null) {
            if (value == node.payload) {
                return true;
            } else if (value < node.payload) {
                node = node.left;
            } else if (value > node.payload) {
                node = node.right;
            }
        }
        return false;
    }

    private static class Node {
        int payload;
        Node left;
        Node right;

        public Node(int payload) {
            this.payload = payload;
        }
    }
}
