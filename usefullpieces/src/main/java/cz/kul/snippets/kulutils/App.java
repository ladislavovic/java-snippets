package cz.kul.snippets.kulutils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import cz.kul.snippets.date.FirstDayOfWeek;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        
        Calendar c = Calendar.getInstance();
        c.set(Calendar.MONTH, 11);
        c.set(Calendar.DAY_OF_MONTH, 20);
        
        SimpleDateFormat sdf = new SimpleDateFormat("d/M");        
        String str = String.format("w%1$d (%2$s - %2$s)", 1, sdf.format(c.getTime()));
        
        System.out.println(str);
        
    }
    
    
    
    
}
