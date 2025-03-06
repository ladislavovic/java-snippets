package cz.kul.snippets.springboot.security_01_default;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

@Service
public class MyService
{

    @PreAuthorize("@mySecurityService.canGetSecredData()")
    public String getMeSecredData() {
        return "foo";
    }

}
