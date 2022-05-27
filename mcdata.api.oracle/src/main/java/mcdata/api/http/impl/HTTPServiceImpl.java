/**
 * 
 */
package mcdata.api.http.impl;

import java.net.URI;

import javax.ws.rs.core.UriBuilder;

import org.eclipse.jetty.server.Server;
import org.glassfish.jersey.jetty.JettyHttpContainerFactory;

import mcdata.api.common.Utils;
import mcdata.api.http.HTTPService;
import mcdata.api.http.HTTPServiceConfig;

/**
 * @author ThangDQ
 *
 */
public class HTTPServiceImpl implements HTTPService {
	private Server server = null;

	public HTTPServiceImpl() {
		URI baseUri = UriBuilder.fromUri("http://localhost/").port(Utils.getServerProperties().getListening_port())
				.build();
		HTTPServiceConfig app = new HTTPServiceConfig();
		this.server = JettyHttpContainerFactory.createServer(baseUri, app);
	}

	public void startHTTPService() throws Exception {
		// TODO Auto-generated method stub
		server.start();
//        server.join();
	}

	public void stopHTTPService() throws Exception {
		// TODO Auto-generated method stub
		server.stop();
	}

}
