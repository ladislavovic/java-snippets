package cz.kul.snippets.hibernatesearch6.example05_embedded_with_routing_bridge;

import org.hibernate.search.mapper.pojo.bridge.RoutingBridge;
import org.hibernate.search.mapper.pojo.bridge.binding.RoutingBindingContext;
import org.hibernate.search.mapper.pojo.bridge.mapping.programmatic.RoutingBinder;
import org.hibernate.search.mapper.pojo.bridge.runtime.RoutingBridgeRouteContext;
import org.hibernate.search.mapper.pojo.route.DocumentRoutes;

public class AttributeRoutingBinder implements RoutingBinder {

	@Override
	public void bind(RoutingBindingContext context) {
		context.dependencies().use("name");
		context.bridge(Attribute.class, new Bridge());
	}

	public static class Bridge implements RoutingBridge<Attribute> {

		@Override
		public void route(
				DocumentRoutes routes,
				Object entityIdentifier,
				Attribute indexedEntity,
				RoutingBridgeRouteContext context) {
			if (indexedEntity.getName().startsWith("attr")) {
				routes.addRoute();
			} else {
				routes.notIndexed();
			}
		}

		@Override
		public void previousRoutes(
				DocumentRoutes routes,
				Object entityIdentifier,
				Attribute indexedEntity,
				RoutingBridgeRouteContext context) {
			routes.addRoute();
		}
	}

}
