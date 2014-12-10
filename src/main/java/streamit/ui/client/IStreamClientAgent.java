package streamit.ui.client;

import java.net.SocketAddress;

public interface IStreamClientAgent {
    public void connect(SocketAddress streamServerAddress);

    public void stop();
}
