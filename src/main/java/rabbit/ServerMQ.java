package rabbit;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

import java.nio.charset.StandardCharsets;

public class ServerMQ {

    private String queue;
    private ConnectionFactory factory;
    private Connection connection;
    private Channel channel;
    private DeliverCallback deliverCallback;

    public ServerMQ(Server server) throws Exception{
        this.queue = server.getQueue();
        factory = new ConnectionFactory();
        factory.setHost(server.getHost());
        factory.setPort(server.getPort());
        factory.setPassword("123456789");
        connection = factory.newConnection();
        channel = connection.createChannel();
        channel.queueDeclare(queue,false,false,false,null);
        System.out.println("");
        System.out.println(" [*] RabbitMQ received channel '"+queue+"' has been started!");
        System.out.println(" [*] Listening on /"+server.getPort()+":" + queue);
        System.out.println("");
        setDeliverCallback();
    }

    public void setDeliverCallback() throws Exception{
        deliverCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), StandardCharsets.UTF_8);
            String[] args = message.split("%#%");
            String channel = args[0];
            String tag = args[1];
            String data = args[2];
            DataChannelManager.invoke(channel, tag, data);
        };
        channel.basicConsume(queue, true, deliverCallback, consumerTag -> {});
    }
}
