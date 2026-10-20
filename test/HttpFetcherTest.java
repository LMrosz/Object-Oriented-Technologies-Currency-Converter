import com.sun.net.httpserver.HttpServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testuje HttpFetcher na lokalnym serwerze HTTP, bez łączenia się z NBP.
 */
class HttpFetcherTest {

    private HttpServer server;
    private String baseUrl;

    @BeforeEach
    void startServer() throws IOException {
        server = HttpServer.create(new InetSocketAddress("127.0.0.1", 0), 0);
        server.createContext("/ok", exchange -> respond(exchange, 200, "<tabela_kursow/>"));
        server.createContext("/error", exchange -> respond(exchange, 500, "error"));
        server.start();
        baseUrl = "http://127.0.0.1:" + server.getAddress().getPort();
    }

    @AfterEach
    void stopServer() {
        server.stop(0);
    }

    private static void respond(com.sun.net.httpserver.HttpExchange exchange, int status, String body)
            throws IOException {
        byte[] bytes = body.getBytes(StandardCharsets.UTF_8);
        exchange.sendResponseHeaders(status, bytes.length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(bytes);
        }
    }

    @Test
    void returnsBodyWhenStatusIs200() {
        HttpFetcher fetcher = new HttpFetcher(baseUrl + "/ok");

        assertEquals("<tabela_kursow/>", fetcher.fetch());
    }

    @Test
    void throwsWhenStatusIsNot200() {
        HttpFetcher fetcher = new HttpFetcher(baseUrl + "/error");

        RuntimeException e = assertThrows(RuntimeException.class, fetcher::fetch);
        assertNotNull(e.getCause());
        assertTrue(e.getCause().getMessage().contains("500"));
    }

    @Test
    void throwsWhenServerIsUnreachable() {
        server.stop(0);
        HttpFetcher fetcher = new HttpFetcher(baseUrl + "/ok");

        assertThrows(RuntimeException.class, fetcher::fetch);
    }
}
