package web.handlers;
import web.abstractions.BaseHandler;
import web.models.DataFromRequest;
import web.core.HitCheck;
import web.models.RequestContext;

public class HitCheckHandler extends BaseHandler {

    @Override
    protected void process(RequestContext context) {
        DataFromRequest data = context.getData();
        HitCheck hitCheck = new HitCheck(data.x(), data.y(), data.r());
        context.setWasThereHit(hitCheck.wasThereHit());
    }
}
