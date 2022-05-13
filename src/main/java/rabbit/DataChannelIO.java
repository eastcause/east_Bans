package rabbit;

import java.util.Map;

@FunctionalInterface
public interface DataChannelIO {

    void execute(Map<String, String> data);

}
