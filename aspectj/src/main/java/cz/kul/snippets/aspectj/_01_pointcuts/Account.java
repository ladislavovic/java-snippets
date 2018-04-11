package cz.kul.snippets.aspectj._01_pointcuts;

public class Account {

    int balance = 20;

    public void withdraw(int amount) {
        if (balance < amount) {
            throw new IllegalStateException("You do not have sufficient credit balance.");
        }
        balance = balance - amount;
    }

    public void insert(int amount) {
       balance += amount;
    }


}
