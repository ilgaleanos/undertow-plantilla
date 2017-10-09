package router;

import controllers.DatabaseName;
import db.MyConnection;
import io.undertow.Undertow;
import io.undertow.UndertowOptions;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.server.handlers.PathHandler;
import io.undertow.server.handlers.SetHeaderHandler;
import java.io.InputStream;
import java.util.Properties;
import javax.sql.DataSource;


/**
 * Provides the {@link #main(String[])} method, which launches the application.
 */
public final class Server {

    private Server() {
        throw new AssertionError();
    }

    public static void main(String[] args) throws Exception {

        Properties props = new Properties();
        final DataSource source;
        
        try (InputStream in = Thread.currentThread()
                .getContextClassLoader()
                .getResourceAsStream("config/server.properties")) {
            props.load(in);
            source = new MyConnection(props).getDB(); 
        }

        int port = Integer.parseInt(props.getProperty("undertow.port"));
        String host = props.getProperty("undertow.host");

        HttpHandler rootHandler = new SetHeaderHandler(
            new PathHandler()
                    
                .addExactPath("/", new DatabaseName(source))
                
            ,"Server", "U-tow");

        Undertow.builder()
                .addHttpListener(port, host)
                .setServerOption(UndertowOptions.ALWAYS_SET_KEEP_ALIVE, false)
                .setHandler(rootHandler)
                .build()
                .start();
    }
}
