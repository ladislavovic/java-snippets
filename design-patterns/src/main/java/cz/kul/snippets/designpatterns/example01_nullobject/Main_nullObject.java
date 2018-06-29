package cz.kul.snippets.designpatterns.example01_nullobject;

import org.junit.Assert;

/**
 * <h1>Description</h1>
 * <p>
 * It replaces null values by object with dummy/default implementation. It allows to avoid
 * not-null checks.
 * </p>
 * 
 * <h1>When to use</h1>
 * <ul>
 * <li>for some calculations. When you calculate something and an item is missing you can
 * use NullObject and avoid not null checking. The code is more clear then.</li>
 * </ul>
 * 
 * <h1>When not to use</h1>
 * <ul>
 * <li>not good for "big" objects. For example it does not make a sense to have NullOrder
 * or NullUser usually</li>
 * </ul>
 * 
 * @author kulhalad
 * @since 7.4.2
 */
public class Main_nullObject {
    
    public static void main(String[] args) {
        NodeNormal n1 = new NodeNormal();
        n1.left = new NodeNormal();
        n1.right = new NodeNormal();
        Assert.assertEquals(3, n1.getTreeSize());
    }

}

interface Node {
    /**
     * Returns number of nodes in the tree.
     */
    int getTreeSize();
}

class NodeNormal implements Node
{
    public Node left = new NodeNull();
    public Node right = new NodeNull();

    public int getTreeSize() {
        return 1 + left.getTreeSize() + right.getTreeSize();
    }
}

class NodeNull implements Node {

    @Override
    public int getTreeSize() {
        return 0;
    }
}
