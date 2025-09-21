package web.handlers;
import web.ResponseCreator;
import web.abstractions.BaseHandler;
import web.core.ResultHistory;
import web.models.RequestContext;

public class ClearHistoryHandler extends BaseHandler {
    private static final String successJson = "{\"status\": \"success\"}";

    @Override
    protected void process(RequestContext context) {
        String sessionId = context.getSessionId();
        ResultHistory.getInstance().clearHistory(sessionId);
        String fullResponse = ResponseCreator.createFullResponse(successJson);
        System.out.print(fullResponse);
    }
}
