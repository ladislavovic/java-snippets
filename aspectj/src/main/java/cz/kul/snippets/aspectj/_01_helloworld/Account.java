package cz.kul.snippets.aspectj._01_xmlconfig_hw;

public class Account {

    private int balance = 20;
 
    public boolean withdraw(int amount) {
        if (balance < amount) {
            return false;
        } 
        balance = balance - amount;
        return true;
    }


}
