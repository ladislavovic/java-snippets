package cz.kul.snippets.java._21_jaas;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.security.Principal;

import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.NameCallback;
import javax.security.auth.callback.PasswordCallback;
import javax.security.auth.callback.UnsupportedCallbackException;
import javax.security.auth.login.LoginContext;
import javax.security.auth.login.LoginException;

/**
 * * JAAS was added for Java 1.3 a optional package and with 1.4 was integrated into SDK
 * 
 * @author kulhalad
 * @since 7.4.2
 */
public class Main_JAAS {

    public static void main(String[] args) throws Exception {
        authentication();
    }

    /**
     * LoginContext - Client application works with this interface. It hides real
     * immplementation mechanism. It is created according to configuration. Configuration
     * can be in text file or whenever else. When you change environment, you change login
     * context configuration and aplication can work without any change.
     * 
     * LoginModule - An interface for particular authentication servide. You can often use
     * existing ones for particular services.
     * 
     * CallbackHandler - když LoginModule potøebuje komunikovat s uživatelem, napø zjistit
     * jméno a heslo, nedìlá to pøímo, ale pomocí CallbackHandleru. Pošle mu callback a
     * ten už vše zaøídí. Dùvod tohoto øešení je, že jeden LoginModule mùže komunikovat se
     * Subjektem rùznými zpùsoby - jednou pøes cmd, jednou pøes web atd.
     * 
     * Subject - In a security context, a subject is any entity that requests access to an
     * object. These are generic terms used to denote the thing requesting access and the
     * thing the request is made against. When you log onto an application you are the
     * subject and the application is the object. When someone knocks on your door the
     * visitor is the subject requesting access and your home is the object access is
     * requested of.
     * 
     * Principal - A subset of subject that is represented by an account, role or other
     * unique identifier. When we get to the level of implementation details, principals
     * are the unique keys we use in access control lists. They may represent human users,
     * automation, applications, connections, etc.
     * 
     * Credentials - Jsou to objekty, který jsou verifikovány pøi autentizaèní transakci.
     * Typicky jsou to jméno a heslo, certifikát, otisk prstu, token, ...
     * 
     * @throws Exception
     */
    private static void authentication() throws Exception {
        LoginContext lc = new LoginContext("Sample", new MyCallbackHandler());

        try {
            lc.login();
            fail("First login should failed because of hardcoded behaviour in callback handler");
        } catch (LoginException e) {
            ;
        }
        lc.login();
        assertEquals("ladislav", lc.getSubject().getPrincipals().iterator().next().getName());
    }

}

class MyCallbackHandler implements CallbackHandler {

    private boolean incorrectPasswordReturned = false;

    public void handle(Callback[] callbacks)
            throws IOException, UnsupportedCallbackException {

        for (int i = 0; i < callbacks.length; i++) {
            if (callbacks[i] instanceof NameCallback) {
                NameCallback nc = (NameCallback) callbacks[i];
                nc.setName("ladislav");
            } else if (callbacks[i] instanceof PasswordCallback) {
                PasswordCallback pc = (PasswordCallback) callbacks[i];
                if (incorrectPasswordReturned) {
                    pc.setPassword("butter".toCharArray());
                } else {
                    pc.setPassword("incorrectPassword".toCharArray());
                    incorrectPasswordReturned = true;
                }
            }
            // NOTE: there are also other callbacks but they are not supported by this handler
        }
    }
}

class SamplePrincipal implements Principal, java.io.Serializable {

    private static final long serialVersionUID = 1L;

    private String name;

    public SamplePrincipal(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String toString() {
        return ("SamplePrincipal:  " + name);
    }

    public boolean equals(Object o) {
        if (o == null)
            return false;

        if (this == o)
            return true;

        if (!(o instanceof SamplePrincipal))
            return false;
        SamplePrincipal that = (SamplePrincipal) o;

        if (this.getName().equals(that.getName()))
            return true;
        return false;
    }

    public int hashCode() {
        return name.hashCode();
    }
}


