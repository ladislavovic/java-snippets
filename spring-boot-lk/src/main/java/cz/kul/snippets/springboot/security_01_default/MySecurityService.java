package cz.kul.snippets.springboot.security_01_default;

import org.springframework.stereotype.Service;

@Service
public class MySecurityService
{

    public boolean canGetSecredData() {
        if (true) throw new IllegalStateException("My exception");
        return true;
    }

}
