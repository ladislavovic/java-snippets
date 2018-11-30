package cz.kul.snippets.java.example18_jaas;

import javax.security.auth.callback.*;
import java.io.IOException;

class SampleCallbackHandler implements CallbackHandler {

    private String name;
    private String password;

    public SampleCallbackHandler(String name, String password) {
        this.name = name;
        this.password = password;
    }

    public void handle(Callback[] callbacks) {
        for (int i = 0; i < callbacks.length; i++) {
            if (callbacks[i] instanceof NameCallback) {
                NameCallback nc = (NameCallback) callbacks[i];
                nc.setName(name);
            } else if (callbacks[i] instanceof PasswordCallback) {
                PasswordCallback pc = (PasswordCallback) callbacks[i];
                pc.setPassword(password.toCharArray());
            }
            // NOTE: there are also other callbacks but they are not supported by this handler
        }
    }
}
