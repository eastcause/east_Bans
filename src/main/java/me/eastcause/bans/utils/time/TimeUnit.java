package me.eastcause.bans.utils.time;

import lombok.Getter;

import java.util.LinkedHashMap;
import java.util.Map;

public enum TimeUnit {
    SECOND("s"), MINUTE("m"), HOUR("h"), DAY("d"), /*WEEK("w"),*/ MONTH("months"), YEAR("y");

    @Getter private static Map<Integer, TimeUnit> UNITS = new LinkedHashMap<>(6);

    static {
        getUNITS().put(31104000, TimeUnit.YEAR);
        getUNITS().put(2592000, TimeUnit.MONTH);
        //getUNITS().put(604800, TimeUnit.WEEK);
        getUNITS().put(86400, TimeUnit.DAY);
        getUNITS().put(3600, TimeUnit.HOUR);
        getUNITS().put(60,TimeUnit.MINUTE);
        getUNITS().put(1,TimeUnit.SECOND);
    }

    private String id;

    TimeUnit(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }
}
