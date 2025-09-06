package web;
import java.util.Locale;

public class ResponseCreator {

    public static String createJson(DataFromRequest data, boolean wasThereHit, long scriptTime, String currentTime) {
        return String.format(Locale.US,
                "{\"x\": %d, \"y\": %.3f, \"r\": %.1f, \"hit\": %b, \"scriptTime\": %d, \"serverTime\": \"%s\"}", data.x(), data.y(), data.r(), wasThereHit, scriptTime, currentTime);
    }

    public static String createErrorJson(String message) {
        return String.format("{\"error\": \"%s\"}", message);
    }
}
