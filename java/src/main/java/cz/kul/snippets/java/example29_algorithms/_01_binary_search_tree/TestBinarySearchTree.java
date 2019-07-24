package cz.kul.snippets.java.example29_algorithms._01_binary_search_tree;

import org.junit.Assert;
import org.junit.Test;

public class TestBinarySearchTree {
    
    @Test
    public void testBasicOperations() {
        BinarySearchTree bst = new BinarySearchTree();

        Assert.assertFalse(bst.search(5));
        
        bst.add(5);
        bst.add(2);
        bst.add(10);
        bst.add(11);
        Assert.assertTrue(bst.search(2));
        Assert.assertTrue(bst.search(5));
        Assert.assertTrue(bst.search(10));
        Assert.assertTrue(bst.search(11));
    }
    
}
