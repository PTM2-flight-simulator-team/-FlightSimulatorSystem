package Model.dataHolder;

import java.util.HashMap;

public class TeleoperationsData {
    public HashMap<String,String> code = new HashMap<>();
    public TeleoperationsData(){
        code = new HashMap<>();
    }

    public HashMap<String,String> GetMap() {
        return code;
    }
}
