package rabbit;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import lombok.Data;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.concurrent.TimeoutException;

@Data
public class ChannelMQ {

    private Server server;
    private ConnectionFactory factory;

    public ChannelMQ(Server server){
        this.server = server;
        factory = new ConnectionFactory();
        factory.setHost(server.getHost());
        factory.setPort(server.getPort());
    }

    public Connection newConnection(){
        try {
            return factory.newConnection();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void sendData(String channel, String tag, String data){
        Connection connection = newConnection();
        try {
            Channel c = connection.createChannel();
            c.queueDeclare(server.getQueue(), false, false, false, null);
            String message = channel + "%#%" + tag + "%#%" + data;
            c.basicPublish("", server.getQueue(), null, message.getBytes(StandardCharsets.UTF_8));
            System.out.println("Send "+message+" to: " + server.getHost() + " " + server.getPort());
            if(connection.isOpen()){
                connection.close();
            }
            if(c.isOpen()){
                c.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }
    }

    public void sendData(String channel, String tag, DataMQ... dataMQS){
        Connection connection = newConnection();
        System.out.println(connection.isOpen());
        try {
            Channel c = connection.createChannel();
            c.queueDeclare(getServer().getQueue(), false, false, false, null);
            String data = "";
            for(DataMQ dataObject : dataMQS){
                data += dataObject.getKey() + "%=%" + dataObject.getValue() + "%&%";
            }
            data = data.substring(0, data.length() - 3);
            String message = channel + "%#%" + tag + "%#%" + data;
            c.basicPublish("", getServer().getQueue(), null, message.getBytes(StandardCharsets.UTF_8));
            System.out.println("Send "+message+" to: " + getServer().getHost() + " " + getServer().getPort() + " " + getServer().getQueue());
            if(connection.isOpen()){
                connection.close();
            }
            if(c.isOpen()){
                c.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }
    }

    public void sendData(String channel, String tag, Map<String, String> map){
        Connection connection = newConnection();
        try {
            Channel c = connection.createChannel();
            c.queueDeclare(getServer().getQueue(), false, false, false, null);
            String data = "";
            for(Map.Entry<String, String> entry : map.entrySet()){
                data += entry.getKey() + "%=%" + entry.getValue() + "%&%";
            }
            data = data.substring(0, data.length() - 3);
            String message = channel + "%#%" + tag + "%#%" + data;
            c.basicPublish("", getServer().getQueue(), null, message.getBytes(StandardCharsets.UTF_8));
            System.out.println("Send "+message+" to: " + getServer().getHost() + " " + getServer().getPort());
            if(connection.isOpen()){
                connection.close();
            }
            if(c.isOpen()){
                c.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }
    }

}
