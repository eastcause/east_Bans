package rabbit;

import lombok.Data;

@Data
public class DataMQ {
    private String key;
    private String value;

    public DataMQ(String key, String value){
        this.key = key;
        this.value = value;
    }
}
