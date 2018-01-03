package cz.kul.snippets.springmvc._05.securityservices;

public interface FooService {
	
	int adminMethod(int a, int b);
	
	int superAdminMethod(int a, int b);

}
