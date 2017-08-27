package sagan.util;

import org.springframework.web.client.RestTemplate;

/**
 * Created by soiff on 27/08/2017.
 *
 * @author : soiff
 * @version : 1.0
 * @since : 1.8
 */
public class GithubService {
    public static final String API_URL_BASE = "https://api.github.com";

    private final RestTemplate template;
    private final CachedRestClient client;

    public GithubService(RestTemplate template, CachedRestClient client) {
        this.template = template;
        this.client = client;
    }

    /**
     * Process the given markdown through GitHub's Markdown rendering API. See
     * http://developer.github.com/v3/markdown
     */
    public String renderToHtml(String markdownSource) {
        return client.post(template, API_URL_BASE + "/markdown/raw", String.class, markdownSource);
    }
}
