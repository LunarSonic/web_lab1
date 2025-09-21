package web.abstractions;
import web.models.RequestContext;

public interface Handler {
    void setNextHandler(Handler next);
    Handler getNextHandler();
    void handle(RequestContext context);
}
