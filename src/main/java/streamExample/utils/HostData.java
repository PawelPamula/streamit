package streamExample.utils;

import java.io.Serializable;

public class HostData implements Serializable {
    String address;
    int port;
    String type;

    public HostData(String address, int port, String type) {
        this.address = address;
        this.port = port;
        this.type = type;
    }

    public String getAddress() {
        return address;
    }

    public int getPort() {
        return port;
    }

    public String getType() {
        return type;
    }
}
