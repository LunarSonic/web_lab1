package web.core;
import web.ResponseCreator;
import web.abstractions.BaseHandler;
import web.handlers.*;
import web.models.RequestContext;

public class RequestChainExecutor {

    public void execute(String requestUri, String queryString, String cookies) {
        RequestContext context = new RequestContext(queryString);
        context.getParameters().put("cookies", cookies);
        BaseHandler startOfChain;
        String path = requestUri;
        int index = path.indexOf("?");
        if (index != -1) {
            path = path.substring(0, index);
        }
        switch (path) {
            case "/api/history":
                startOfChain = new SessionHandler();
                startOfChain.setNextHandler(new HistoryHandler());
                break;
            case "/api/clear":
                startOfChain = new SessionHandler();
                startOfChain.setNextHandler(new ClearHistoryHandler());
                break;
            case "/api/points":
                SessionHandler sessionHandler = new SessionHandler();
                RequestParsingHandler parser = new RequestParsingHandler();
                ValidationHandler validation = new ValidationHandler();
                HitCheckHandler hitCheck = new HitCheckHandler();
                RequestHandler requestHandler = new RequestHandler();

                sessionHandler.setNextHandler(parser);
                parser.setNextHandler(validation);
                validation.setNextHandler(hitCheck);
                hitCheck.setNextHandler(requestHandler);
                startOfChain = sessionHandler;
                break;
            default:
                String notFoundResponse = ResponseCreator.createNotFoundResponse();
                System.out.print(notFoundResponse);
                return;
        }
        startOfChain.handle(context);
    }
}
