package streamExample.coserver;


public class CoordinationServer {

    public final static String SERVER_CONNECTING = "Server connecting";
    public final static String CLIENT_CONNECTING = "Client connecting";
    public final static int COORDINATION_SERVER_PORT = 20002;
    public final static String COORDINATION_SERVER_HOST = "localhost";

    public static void main(String[] args) {
        CoordinationServerAgent agent = new CoordinationServerAgent();
    }
}
