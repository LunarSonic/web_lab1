package web;
import com.fastcgi.FCGIInterface;

public class App {
    public static void main(String[] args) {
        RequestHandler requestHandler = new RequestHandler();
        FCGIInterface fcgiInterface = new FCGIInterface();
        while (fcgiInterface.FCGIaccept() >= 0) {
            requestHandler.handleRequest();
        }
    }
}
