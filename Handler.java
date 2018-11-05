package VCS;


import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.URLDecoder;
import java.util.Map;
import java.util.Random;

public class Handler implements HttpHandler {
    public  static  final String[] ACTIONS   = {"move", "eat", "load", "unload"};
    public  static  final String[] DIRECTIONS    = {"up", "down", "right", "left"};

    public static void main(String[] args) throws Exception {
        HttpServer server  = HttpServer.create(new InetSocketAddress(7070),0);
        server.createContext("/",new Handler());
        server.setExecutor(null);
        server.start();

    }

    public void _set_headers(HttpExchange ex){

        ex.getResponseHeaders().set("Content-type", "application/json");


    }
    @Override
    public void handle(HttpExchange exchange) throws IOException {

        if (exchange.getRequestMethod().equalsIgnoreCase("POST")){


            String response = "";
            Object obj = null;
            String requestString = "";
            InputStream is;
            try {
                StringBuilder requestBuffer = new StringBuilder();
                is = exchange.getRequestBody();
                int rByte;
                while ((rByte = is.read()) != -1) {
                    requestBuffer.append((char) rByte);
                }
                is.close();
                if (requestBuffer.length() > 0) {
                    requestString = URLDecoder.decode(requestBuffer.toString(), "UTF-8");
                } else {
                    requestString = null;
                }

                obj = new JSONParser().parse(requestString);
                JSONObject jo = (JSONObject) obj;
                JSONObject jout = new JSONObject();
                Map ants = ((Map)jo.get("ants"));
                System.out.println(ants.keySet());
                for (Object ant :ants.keySet() )
                {

                    JSONObject antAttr = new JSONObject();
                    antAttr.put("act", ACTIONS[(new Random()).nextInt(ACTIONS.length)]);
                    antAttr.put("dir",DIRECTIONS[(new Random()).nextInt(DIRECTIONS.length)]);
                    jout.put(ant,antAttr);

                }
                System.out.println(jout);
                response = jout.toString();
            } catch (ParseException e) {
                e.printStackTrace();
            }

            _set_headers(exchange);
            exchange.sendResponseHeaders(200, response.length());
            OutputStream os = exchange.getResponseBody();
            os.write(response.getBytes());
            os.close();

        }



    }
}
