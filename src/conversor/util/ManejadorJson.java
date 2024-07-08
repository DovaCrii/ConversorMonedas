package conversor.util;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class ManejadorJson {
    public static JsonObject parsearJson(String jsonString) {
        return JsonParser.parseString(jsonString).getAsJsonObject();
    }
}