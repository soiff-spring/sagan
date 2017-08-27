package sagan.git;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import sagan.util.CachedRestClient;
import sagan.util.GithubService;

/**
 * Created by soiff on 27/08/2017.
 *
 * @author : soiff
 * @version : 1.0
 * @since : 1.8
 */
@Configuration
public class GitAutoConfiguration {
    @Bean
    @ConditionalOnMissingBean
    public RestTemplate restTemplate(final RestTemplateBuilder builder) {
        return builder.build();
    }

    @Bean
    public GithubService githubService(final RestTemplate template,
                                       final CachedRestClient client) {
        return new GithubService(template, client);
    }
}
