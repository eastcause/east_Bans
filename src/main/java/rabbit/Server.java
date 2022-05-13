package rabbit;

import lombok.Data;

@Data
public class Server {

    private String server;
    private String host;
    private int port;
    private String queue;

    public Server(String server, String host, int port, String queue) {
        this.server = server;
        this.host = host;
        this.port = port;
        this.queue = queue;
    }
}
