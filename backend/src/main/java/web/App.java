package web;
import com.fastcgi.FCGIInterface;
import web.core.RequestChainExecutor;
import java.util.logging.Logger;

public class App {
    private static final Logger logger = Logger.getLogger(App.class.getName());

    public static void main(String[] args) {
        RequestChainExecutor executor = new RequestChainExecutor();
        FCGIInterface fcgiInterface = new FCGIInterface();
        logger.info("Сервер начал работу...");
        while (fcgiInterface.FCGIaccept() >= 0) {
            String queryString = FCGIInterface.request.params.getProperty("QUERY_STRING");
            String cookies = FCGIInterface.request.params.getProperty("HTTP_COOKIE");
            String requestUri = FCGIInterface.request.params.getProperty("REQUEST_URI");
            logger.info("Поступил запрос: " + requestUri);
            executor.execute(requestUri, queryString, cookies);
        }
    }
}
