package cz.kul.snippets.spring._12_scheduling;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

// TODO refactor. This was created only as quick test if something works.
public class Main_Scheduling {

    public static void main(String [] args) throws InterruptedException {

        try (AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext()) {
            ctx.register(Config.class);
            ctx.refresh();

            Thread.sleep(10L * 1000);
        }



    }

}
