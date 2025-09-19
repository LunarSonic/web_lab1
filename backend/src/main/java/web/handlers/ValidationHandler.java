package web.handlers;
import web.abstractions.BaseHandler;
import web.models.DataFromRequest;
import web.models.RequestContext;
import web.core.Validation;
import java.util.Map;

public class ValidationHandler extends BaseHandler {
    @Override
    protected void process(RequestContext context) {
        Map<String, String> parameters = context.getParameters();
        Validation validation = new Validation(parameters);
        validation.validateXYR();
        if (validation.hasErrors()) {
            context.setErrorMessages(validation.getErrors());
        } else {
            int x = validation.getX();
            float y = validation.getY();
            float r = validation.getR();
            context.setData(new DataFromRequest(x, y, r));
        }
    }
}
