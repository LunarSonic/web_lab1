package web.handlers;
import web.ResponseCreator;
import web.abstractions.BaseHandler;
import web.core.ResultHistory;
import web.models.RequestContext;
import com.google.gson.Gson;
import web.models.Result;
import java.util.List;

public class HistoryHandler extends BaseHandler {

    private static final Gson gson = new Gson();
    @Override
    protected void process(RequestContext context) {
        String sessionId = context.getSessionId();
        List<Result> history = ResultHistory.getInstance().getHistory(sessionId);
        String jsonResponse = gson.toJson(history);
        String fullResponse = ResponseCreator.createFullResponse(jsonResponse);

        System.out.print(fullResponse);
    }
}
