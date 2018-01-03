package cz.kul.snippets.springmvc._05.securityservices;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

@Service
public class FooServiceImpl implements FooService {

	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@Override
	public int adminMethod(int a, int b) {
		return a + b;
	}
	
	@PreAuthorize("hasRole('ROLE_SUPERADMIN')")
	@Override
	public int superAdminMethod(int a, int b) {
		return a + b;
	}
	
	

}
