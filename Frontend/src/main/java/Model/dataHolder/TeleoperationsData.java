package Model.dataHolder;

import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class TeleoperationsData {
    public JsonObject code;
    public TeleoperationsData(){
        code = new JsonObject();
    }

    public JsonObject GetCode() {
        return code;
    }
}
