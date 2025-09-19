package web.handlers;
import web.*;
import web.abstractions.BaseHandler;
import web.models.DataFromRequest;
import web.models.RequestContext;
import java.nio.charset.StandardCharsets;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.logging.Logger;

public class RequestHandler extends BaseHandler {
    private static final Logger logger = Logger.getLogger(RequestHandler.class.getName());

    private static final String HTTP_RESPONSE = """
            Connection: keep-alive
            Content-Type: application/json
            Content-Length: %d
            
            %s
            """;

    @Override
    protected void process(RequestContext context) {
        String jsonResponse;
        List<String> errors = context.getErrorMessages();
        if (!errors.isEmpty()) {
            jsonResponse = ResponseCreator.createErrorJson(context.getErrorMessages());
            logger.warning(String.join(", ", errors));
        } else {
            DataFromRequest data = context.getData();
            long scriptTime = System.nanoTime() - context.getStartTime();
            ZonedDateTime currentTime = ZonedDateTime.now(ZoneId.of("Europe/Moscow"));
            String formattedTime = currentTime.format(DateTimeFormatter.ofPattern("dd.MM.yyyy H:mm:ss"));
            jsonResponse = ResponseCreator.createJson(data, context.wasThereHit(), scriptTime, formattedTime);
            logger.info("Успешная обработка запроса: x=" + data.x() + ", y=" + data.y() + ", r=" + data.r());
        }
        String fullResponse = String.format(HTTP_RESPONSE, jsonResponse.getBytes(StandardCharsets.UTF_8).length, jsonResponse);
        System.out.print(fullResponse);
    }
}
