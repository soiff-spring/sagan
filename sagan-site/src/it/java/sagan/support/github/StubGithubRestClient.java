package sagan.support.github;

import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.util.UriTemplate;
import sagan.git.github.GithubClient;

import java.util.HashMap;
import java.util.Map;

public class StubGithubRestClient extends GithubClient {

    private Map<String, String> responseMap = new HashMap<>();

    public StubGithubRestClient() {
        super(null, null);
    }

    public void putResponse(String url, String response) {
        responseMap.put(url, response);
    }

    public void clearResponses() {
        responseMap.clear();
    }

    @Override
    public String sendRequestForJson(String path, Object... uriVariables) {
        return handleRequest(path, uriVariables);
    }

    @Override
    public String sendRequestForHtml(String path, Object... uriVariables) {
        return handleRequest(path, uriVariables);
    }

    @Override
    public String sendPostRequestForHtml(String path, String body, Object... uriVariables) {
        return "<h1>HTML</h1>";
    }

    private String handleRequest(String path, Object[] uriVariables) {
        String url = new UriTemplate(path).expand(uriVariables).getPath();
        if (!responseMap.containsKey(url)) {
            throw new HttpClientErrorException(HttpStatus.NOT_FOUND, "no response for: " + url);
        }
        return responseMap.get(url);
    }
}
