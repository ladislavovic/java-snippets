package cz.kul.snippets.concurrency._02_monitor;

public class Main {

    public static void main(String[] args) {
        new Thread(new Foo("alfa")).start();
        new Thread(new Foo("beta")).start();
    }
    
    public static class Foo implements Runnable {
        
        private String name;
        
        public Foo(String name) {
            this.name = name;
        }

        public void run() {
            while (true) {
                System.out.println(name);
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
        
    }

}
