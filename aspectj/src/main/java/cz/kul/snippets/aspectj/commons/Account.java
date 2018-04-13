package cz.kul.snippets.aspectj.commons;

public class Account {

    int balance;

    public Account() {
    }

    public Account(int balance) {
        this.balance = balance;
    }

    public void withdraw(int amount) {
        if (balance < amount) {
            throw new IllegalStateException("You do not have sufficient credit balance.");
        }
        balance = balance - amount;
    }

    public void insert(int amount) {
       balance += amount;
    }

    public int getBalance() {
        return balance;
    }

}
