package cz.kul.snippets.commonscollections._01_multikeymap;

import org.apache.commons.collections4.map.MultiKeyMap;

public class _01_MultiKeyMap {

    /**
     * With this class you can map values according to combinations of keys.
     * 
     * The limitation is all keys have to have the same type.
     * 
     * @param args
     */
    public static void main(String args[]) {
        MultiKeyMap<String, Integer> m = new MultiKeyMap<String, Integer>();
        
        // Basic usage        
        m.put("A", "B", 1);
        System.out.println("1:" + m.get("A", "B"));
        
        // You can use null as a key
        m.put(null, "B", 2);
        System.out.println("2:" + m.get(null, "B"));
        
        // Or more null instances
        m.put(null, null, 3);
        System.out.println("3:" + m.get(null, null));
        
        // For getting the value you can use all keys, not only some of them.
        // This example will not returns anything
        m.put("C", "D", 4);
        System.out.println("4:" + m.get("C", null));
    
    }

}
