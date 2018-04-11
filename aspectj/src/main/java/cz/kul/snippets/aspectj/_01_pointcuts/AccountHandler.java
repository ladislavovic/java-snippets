package cz.kul.snippets.aspectj._01_pointcuts;

public class AccountHandler {

    public void handleInsert(Account account, int amount) {
        account.insert(amount);
    }

}
