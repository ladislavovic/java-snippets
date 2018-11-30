package cz.kul.snippets.java.example18_jaas;

import java.security.Principal;

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
