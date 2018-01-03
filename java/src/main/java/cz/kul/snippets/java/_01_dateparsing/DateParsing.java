package cz.kul.snippets.java._01_dateparsing;

import java.io.File;
import java.text.SimpleDateFormat;

public class DateParsing {

    public static void main(String[] args) throws Exception {
        parseISO8601();
        System.out.println(String.class.isAssignableFrom(Object.class));
        System.out.println("ahoj" instanceof Object);
        System.out.println(Object.class.isAssignableFrom(String.class));
        
        String path = "c:/ca/file/repo-customfieldsurl-test/0/0/1";
        String file = "02.jpg";
        
        File dir = new File(path);
        dir.mkdirs();
        dir.mkdirs();
        
    }
    
    /**
     * This can parse dateTime string in ISO8601 format.
     * 
     * But not always, because SimpleDateFormat is not ISO8601 compliant. There are differences in time
     * zone formats.
     */
    private static void parseISO8601() throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSX");
        System.out.println(sdf.parse("2014-12-16T23:00:00.000Z"));
    }

}
