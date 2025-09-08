package web;
import com.fastcgi.FCGIInterface;
import java.nio.charset.StandardCharsets;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

public class RequestHandler {
    private static final String HTTP_RESPONSE = """
            Connection: keep-alive
            Content-Type: application/json
            Content-Length: %d
            
            %s
            """;

    private static Map<String, String> parseRequest(String request) {
        String[] keysAndValues = request.split("&");
        Map<String, String> resultMap = new HashMap<>();
        for (String keyAndValue: keysAndValues) {
            String[] dividedKeyAndValue = keyAndValue.split("=");
            if (dividedKeyAndValue.length == 2) {
                resultMap.put(dividedKeyAndValue[0], dividedKeyAndValue[1]);
            } else if (dividedKeyAndValue.length == 1) resultMap.put(dividedKeyAndValue[0], "");
        }
        return resultMap;
    }

    public void handleRequest() {
        String jsonResponse;
        try {
            long start = System.nanoTime();
            String request = FCGIInterface.request.params.getProperty("QUERY_STRING");
            Map<String, String> values = parseRequest(request);
            Validation validation = new Validation(values);
            if (!validation.validateXYR()) {
                jsonResponse = ResponseCreator.createErrorJson("Параметры не корректны");
            } else {
                int x = Integer.parseInt(values.get("x"));
                float y = Float.parseFloat(values.get("y"));
                float r = Float.parseFloat(values.get("r"));
                DataFromRequest data = new DataFromRequest(x, y, r);
                long end = System.nanoTime();
                long time = end - start;
                ZonedDateTime currentTime = ZonedDateTime.now(ZoneId.of("Europe/Moscow"));
                String formattedTime = currentTime.format(DateTimeFormatter.ofPattern("dd.MM.yyyy H:mm:ss"));
                jsonResponse = ResponseCreator.createJson(data, validation.wasThereHit(), time, formattedTime);
            }
        } catch (Exception e) {
            jsonResponse = ResponseCreator.createErrorJson(e.getMessage());
        }
        String fullResponse = String.format(HTTP_RESPONSE, jsonResponse.getBytes(StandardCharsets.UTF_8).length, jsonResponse);
        System.out.print(fullResponse);
    }
}
