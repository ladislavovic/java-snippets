package cz.kul.snippets.aspectj.commons;

public class AccountHandler {

    public void handleInsert(Account account, int amount) {
        account.insert(amount);
    }

}
