import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class HttpFetcher implements Fetcher {
    private String url;

    public HttpFetcher(String url) {
        this.url = url;
    }

    @Override
    public String fetch() {
        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .GET()
                    .build();
            HttpResponse<String> response =
                    client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() != 200) {
                throw new RuntimeException("NBP HTTP " + response.statusCode());
            }
            return response.body();
        } catch (Exception e) {
            throw new RuntimeException("Nie udało się pobrać kursów z NBP", e);
        }
    }
}
