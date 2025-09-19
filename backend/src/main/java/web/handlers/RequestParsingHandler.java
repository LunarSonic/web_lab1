package web.handlers;
import web.abstractions.BaseHandler;
import web.models.RequestContext;
import java.util.HashMap;
import java.util.Map;

public class RequestParsingHandler extends BaseHandler {

    private static Map<String, String> parseRequest(String request) {
        String[] keysAndValues = request.split("&");
        Map<String, String> result = new HashMap<>();
        for (String keyAndValue: keysAndValues) {
            String[] divideKeyAndValue = keyAndValue.split("=");
            if (divideKeyAndValue.length == 2) {
                result.put(divideKeyAndValue[0], divideKeyAndValue[1]);
            } else {
                result.put(divideKeyAndValue[0], "");
            }
        }
        return result;
    }

    @Override
    protected void process(RequestContext context) {
        String queryString = context.getRawQuery();
        Map<String, String> parsedParameters = parseRequest(queryString);
        context.setParameters(parsedParameters);
    }
}
