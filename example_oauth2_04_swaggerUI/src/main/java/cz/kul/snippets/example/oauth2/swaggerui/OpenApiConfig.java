package cz.kul.snippets.example.oauth2.swaggerui;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.OAuthFlow;
import io.swagger.v3.oas.annotations.security.OAuthFlows;
import io.swagger.v3.oas.annotations.security.OAuthScope;
import io.swagger.v3.oas.annotations.security.SecurityScheme;

@OpenAPIDefinition(info = @Info(title = "My App",
		description = "Some long and useful description", version = "v1"))
@SecurityScheme(
		name = "security_auth",
		type = SecuritySchemeType.OAUTH2,
		flows = @OAuthFlows(
				authorizationCode = @OAuthFlow(
					authorizationUrl = "${springdoc.oAuthFlow.authorizationUrl}",
					tokenUrl = "${springdoc.oAuthFlow.tokenUrl}"
//					scopes = {
//						@OAuthScope(name = "read", description = "read scope"),
//						@OAuthScope(name = "write", description = "write scope") }
				)
		)
)
public class OpenApiConfig {}

// TODO what is this class good for? Probably you can configure openabi by this annotations or by
// bean definitions.