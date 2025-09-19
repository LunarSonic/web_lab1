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
            logger.info("Поступил запрос: " + queryString);
            executor.execute(queryString);
        }
    }
}
