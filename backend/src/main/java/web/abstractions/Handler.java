package web.abstractions;
import web.models.RequestContext;

public interface Handler {
    void setNextHandler(Handler next);
    void handle(RequestContext context);
}
