package me.eastcause.bans.yaml;

public interface Wad {

    boolean checkFiles();
    void topUp();
    boolean saveData();
    boolean reloadData();
    boolean setIfThereIsNo(String path, Object o);

}
