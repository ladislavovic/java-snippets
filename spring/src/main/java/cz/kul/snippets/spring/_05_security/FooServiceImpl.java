package cz.kul.snippets.spring._05_security;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

@Service
public class FooServiceImpl implements FooService {

    @Override
    @PreAuthorize("hasRole('ROLE_USER')")
    public String getMessage() {
        return "FooService msg";
    }

}
