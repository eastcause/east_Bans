package rabbit;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class DataChannelManager {

    private static Map<String, DataChannelIO> dataChannelIOList = new HashMap<>();

    public DataChannelManager(){

    }

    public static void invoke(String channel, String tag, String data){
        DataChannelIO dataChannelIO = dataChannelIOList.getOrDefault(channel + "#" + tag.toLowerCase(), null);
        if(dataChannelIO == null){
            System.out.println(" [-] DataChannel `"+channel+"#"+tag+"` not exists!");
            return;
        }
        Map<String, String> map = new LinkedHashMap<>();
        String[] parts = data.split("%&%");
        for(String part : parts){
            String[] kv = part.split("%=%");
            map.put(kv[0], kv[1]);
        }
        dataChannelIO.execute(map);
    }

    public DataChannelManager addDataChannel(String channel, String tag, DataChannelIO dataChannelIO){
        dataChannelIOList.put(channel + "#" + tag.toLowerCase(), dataChannelIO);
        return this;
    }

}
