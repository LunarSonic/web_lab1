package web.core;
import web.handlers.HitCheckHandler;
import web.handlers.RequestHandler;
import web.handlers.RequestParsingHandler;
import web.handlers.ValidationHandler;
import web.models.RequestContext;

public class RequestChainExecutor {

    public void execute(String questyString) {
        RequestContext context = new RequestContext(questyString);
        RequestParsingHandler parser = new RequestParsingHandler();
        ValidationHandler validation = new ValidationHandler();
        HitCheckHandler hitCheck = new HitCheckHandler();
        parser.setNextHandler(validation);
        validation.setNextHandler(hitCheck);
        parser.handle(context);

        RequestHandler requestHandler = new RequestHandler();
        requestHandler.handle(context);
    }
}
