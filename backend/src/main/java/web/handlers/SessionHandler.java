package web.handlers;
import web.abstractions.BaseHandler;
import web.models.RequestContext;
import java.util.UUID;

public class SessionHandler extends BaseHandler {

    @Override
    protected void process(RequestContext context) {
        String cookies = context.getParameters().get("cookies");
        String sessionId = parseSessionId(cookies);
        if (sessionId == null) {
            sessionId = UUID.randomUUID().toString();
            context.getParameters().put("sessionId", sessionId);
        }
        context.setSessionId(sessionId);
    }

    private String parseSessionId(String cookies) {
        if (cookies == null) {
            return null;
        }
        String[] pairs = cookies.split(";");
        for (String pair: pairs) {
            String[] parts = pair.split("=");
            if (parts.length == 2 && parts[0].trim().equals("sessionId")) {
                return parts[1];
            }
        }
        return null;
    }
}
