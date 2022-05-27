package mcdata.api.http;

import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.glassfish.jersey.server.ResourceConfig;

public class HTTPServiceConfig extends ResourceConfig {
	public HTTPServiceConfig() {
		packages("mcdata.api.httpcontroller.impl");
		register(ObjectMapperContextResolver.class);
		register(JacksonFeature.class);
		register(HTTPDefaultHeader.class);
		register(MultiPartFeature.class);
	}
}
