package controllers;

import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Objects;
import javax.sql.DataSource;

/**
 * Handles the single-query database test using a SQL database.
 */
public final class DatabaseName implements HttpHandler {

    private final DataSource db;

    public DatabaseName(DataSource db) {
        this.db = Objects.requireNonNull(db);
    }

    @Override
    public void handleRequest(HttpServerExchange exchange) throws Exception {
        String response;
        try (
                Connection connection = db.getConnection();
                PreparedStatement statement = connection.prepareStatement(
                        "SELECT DATABASE() as name;"
                )) {
            try (ResultSet resultSet = statement.executeQuery()) {
                resultSet.next();
                response = resultSet.getString("name");
                System.out.println(response);
                exchange.setStatusCode(200);
            }
        }

        exchange.getResponseSender().send(response);
    }
}
