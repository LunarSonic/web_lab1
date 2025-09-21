package web.handlers;
import web.*;
import web.abstractions.BaseHandler;
import web.core.ResultHistory;
import web.models.DataFromRequest;
import web.models.RequestContext;
import web.models.Result;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.logging.Logger;

public class RequestHandler extends BaseHandler {
    private static final Logger logger = Logger.getLogger(RequestHandler.class.getName());

    @Override
    protected void process(RequestContext context) {
        String fullResponse;
        String jsonBody;
        List<String> errors = context.getErrorMessages();
        if (!errors.isEmpty()) {
            jsonBody = ResponseCreator.createErrorJsonBody(errors);
            fullResponse = ResponseCreator.createFullResponse(jsonBody);
            logger.warning(String.join(" ", errors));
        } else {
            DataFromRequest data = context.getData();
            long scriptTime = System.nanoTime() - context.getStartTime();
            ZonedDateTime currentTime = ZonedDateTime.now(ZoneId.of("Europe/Moscow"));
            String formattedTime = currentTime.format(DateTimeFormatter.ofPattern("dd.MM.yyyy H:mm:ss"));
            String sessionId = context.getSessionId();
            boolean wasThereHit = context.wasThereHit();

            Result result = new Result(data.x(), data.y(), data.r(), wasThereHit, scriptTime, formattedTime);
            ResultHistory.getInstance().addResult(sessionId, result);
            jsonBody = ResponseCreator.createSuccessJsonBody(result);
            String newSessionId = context.getSessionId();
            if (newSessionId != null) {
                fullResponse = ResponseCreator.createResponseWithCookie(jsonBody, newSessionId);
            } else {
                fullResponse = ResponseCreator.createFullResponse(jsonBody);
            }
            logger.info("Успешная обработка запроса: x=" + data.x() + ", y=" + data.y() + ", r=" + data.r());
        }
        System.out.print(fullResponse);
    }
}
