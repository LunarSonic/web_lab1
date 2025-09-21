package web;
import web.models.HttpResponseTemplates;
import web.models.Result;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Locale;

public class ResponseCreator {

    public static String createSuccessJsonBody(Result result) {
        return String.format(Locale.US,
                "{\"x\": %d, \"y\": %f, \"r\": %.1f, \"hit\": %b, \"scriptTime\": %d, \"serverTime\": \"%s\"}", result.x(), result.y(), result.r(), result.hit(), result.scriptTime(), result.serverTime());
    }

    public static String createErrorJsonBody(List<String> errors) {
        String errorsAsJsonArray = String.format("[\"%s\"]", String.join("\", \"", errors));
        return String.format("{\"errors\": %s}", errorsAsJsonArray);
    }

    public static String createResponseWithCookie(String jsonBody, String sessionId) {
        int contentLength = jsonBody.getBytes(StandardCharsets.UTF_8).length;
        return String.format(HttpResponseTemplates.HTTP_RESPONSE_WITH_COOKIE.getTemplate(), sessionId, contentLength, jsonBody);
    }

    public static String createFullResponse(String jsonBody) {
        int contentLength = jsonBody.getBytes(StandardCharsets.UTF_8).length;
        return String.format(HttpResponseTemplates.HTTP_RESPONSE.getTemplate(), contentLength, jsonBody);
    }

    public static String createNotFoundResponse() {
        return HttpResponseTemplates.NOT_FOUND_RESPONSE.getTemplate();
    }
}
