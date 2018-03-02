package cz.kul.snippets.guava._01_range;

import com.google.common.collect.Range;

/**
 * Ranges represents mathematical intervals:
 *   (1, 5)
 *   <0, 1000)
 *   <0, inf)
 *   
 * You can use them on many places.
 */
public class Ranges 
{
    public static void main( String[] args )
    {
        // Opened interval
        {
            System.out.println("Opened interval");
            Range<Integer> r = Range.open(0, 10);
            System.out.println("(0, 10) contains 5: " + r.contains(5));
            System.out.println("(0, 10) contains 0: " + r.contains(0));
        }
        
        // Closed interval
        {
            System.out.println("\nClosed interval");
            Range<Integer> r = Range.closed(0, 10);
            System.out.println("<0, 10> contains 5: " + r.contains(5));
            System.out.println("<0, 10> contains 0: " + r.contains(0));
        }
        
        // Intersection
        {
            System.out.println("\nClosed interval");
            Range<Integer> r1 = Range.open(5, 10);
            Range<Integer> r2 = Range.closed(7, 12);
            Range<Integer> intersection = r1.intersection(r2);
            System.out.println("The intersection of (5, 10) and <7, 12> is " + intersection);
        }
    }
}
