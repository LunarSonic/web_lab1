package web.abstractions;
import web.handlers.RequestHandler;
import web.models.RequestContext;

abstract public class BaseHandler implements Handler {
    private Handler nextHandler;

    @Override
    public void setNextHandler(Handler nextHandler) {
        this.nextHandler = nextHandler;
    }

    @Override
    public Handler getNextHandler() {
        return nextHandler;
    }

    @Override
    public void handle(RequestContext context) {
        process(context);
        if (context.getErrorMessages().isEmpty()) {
            if (nextHandler != null) {
                nextHandler.handle(context);
            }
        } else {
            Handler last = nextHandler;
            while (last != null && !(last instanceof RequestHandler)) {
                last = last.getNextHandler();
            }
            if (last != null) {
                last.handle(context);
            }
        }
    }

    protected abstract void process(RequestContext context);
}
