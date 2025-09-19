package web.abstractions;
import web.models.RequestContext;

abstract public class BaseHandler implements Handler {
    private Handler nextHandler;

    @Override
    public void setNextHandler(Handler nextHandler) {
        this.nextHandler = nextHandler;
    }

    @Override
    public void handle(RequestContext context) {
        process(context);
        if (nextHandler != null && (context.getErrorMessages() == null || context.getErrorMessages().isEmpty())) {
            nextHandler.handle(context);
        }
    }
    protected abstract void process(RequestContext context);
}
