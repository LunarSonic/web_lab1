package web;
import web.models.DataFromRequest;
import java.util.List;
import java.util.Locale;

public class ResponseCreator {

    public static String createJson(DataFromRequest data, boolean wasThereHit, long scriptTime, String currentTime) {
        return String.format(Locale.US,
                "{\"x\": %d, \"y\": %f, \"r\": %.1f, \"hit\": %b, \"scriptTime\": %d, \"serverTime\": \"%s\"}", data.x(), data.y(), data.r(), wasThereHit, scriptTime, currentTime);
    }

    public static String createErrorJson(List<String> errors) {
        String errorsAsJsonArray = String.format("[\"%s\"]", String.join("\", \"", errors));
        return String.format("{\"errors\": %s}", errorsAsJsonArray);
    }
}
