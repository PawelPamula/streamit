package streamExample.coserver;


public class CoordinationServer {

    public final static String SERVER_CONNECTING = "Server connecting";
    public final static String CLIENT_CONNECTING = "Client connecting";

    public static void main(String[] args) {
        CoordinationServerAgent agent = new CoordinationServerAgent(20002);
    }
}
