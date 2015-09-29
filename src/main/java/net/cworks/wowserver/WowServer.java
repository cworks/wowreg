/**
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * 
 * Baked with love by comartin
 * Package: net.cworks.wowserver
 * User: comartin
 * Created: 8/13/2014 2:18 PM
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 */
package net.cworks.wowserver;

import net.cworks.wowserver.util.RandomHelper;
import spark.Spark;

import java.util.ArrayList;
import java.util.List;

public class WowServer {

    /**
     * list of exposed RESTful apis
     */
    private List<CoreApi> apis = null;
    private String hostname;
    private Integer port;

    public WowServer() {
        this("localhost", 4040);
    }

    public WowServer(String hostname, Integer port) {
        this.hostname = hostname;
        this.port = port;
        this.apis = new ArrayList<CoreApi>();
        // set secretKey valid for length of JVM process, used for simple encryption
        System.setProperty("wow.instanceId", RandomHelper.randomFirstName());
    }

    WowServer deploy(CoreApi api) {
        apis.add(api);
        return this;
    }

    public void start() {
        Spark.setIpAddress(hostname);
        Spark.setPort(port);
        for(CoreApi api : apis) {
            api.start();
        }
    }

    /**
     * Main entry point for wow registration service
     * @param args
     */
    public static void main(String[] args) {
        WowServer server = new WowServer();
        server.deploy(new AttendeesApi())
            .deploy(new RegistrationApi())
            .deploy(new EventsApi())
            .deploy(new HealthCheckApi())
            .deploy(new PaymentApi())
            .start();
    }

}
