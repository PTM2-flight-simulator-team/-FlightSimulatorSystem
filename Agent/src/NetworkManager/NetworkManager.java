package NetworkManager;

import java.util.Observable;

public class NetworkManager extends Observable {
    private SSHhandler ssHhandler;
    private HttpHandler httpHandler;
    private SocketHandler socketHandler;
}
