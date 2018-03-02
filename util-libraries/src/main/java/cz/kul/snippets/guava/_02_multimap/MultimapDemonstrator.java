package cz.kul.snippets.guava._02_multimap;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

/**
 * Multimap is a map, which can have more values for one key. Effectively it is
 * more handy version of Map<K, Collection<V>>.
 * 
 * @author kulhalad
 * @since 7.4
 *
 */
public class MultimapDemonstrator {

    public static void main(String[] args) {
        
        {
            System.out.println("\nBasic usage");
            Multimap<String, Integer> mm = HashMultimap.create();
            mm.put("a", 1);
            mm.put("a", 2);
            mm.put("a", 3);
            System.out.println("Multimap contains these values for key \"a\": " + mm.get("a"));
        }
        
    }

}
