package sagan.git.github;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.social.github.api.GitHub;
import org.springframework.social.github.api.impl.GitHubTemplate;
import org.springframework.social.github.connect.GitHubConnectionFactory;
import org.springframework.util.StringUtils;
import sagan.git.DownloadConverter;
import sagan.git.GitClient;
import sagan.git.JsonStringConverter;
import sagan.git.MarkdownHtmlConverter;
import sagan.util.CachedRestClient;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * Configuration for authentication against and access to GitHub's API. See
 * application.yml for details on each of the @Value-annotated fields.
 */
@Slf4j
@Configuration
public class GithubAutoConfiguration {

    @Value("${github.client.id}")
    private String githubClientId;

    @Value("${github.client.secret}")
    private String githubClientSecret;

    @Value("${github.access.token}")
    private String githubAccessToken;

    @Bean
    public GitHubConnectionFactory gitHubConnectionFactory() {
        GitHubConnectionFactory factory = new GitHubConnectionFactory(githubClientId, githubClientSecret);
        factory.setScope("user");
        return factory;
    }

    @Bean
    public GitHub gitHubTemplate() {
        if (StringUtils.isEmpty(githubAccessToken)) {
            return new GuideGitHubTemplate();
        }
        return new GuideGitHubTemplate(githubAccessToken);
    }

    @Bean
    public GitClient gitClient(final GitHub gitHub, final CachedRestClient cachedRestClient) {
        return new GithubClient(gitHub, cachedRestClient);
    }

    private static class GuideGitHubTemplate extends GitHubTemplate {

        private GuideGitHubTemplate() {
            super();
            log.warn("GitHub API access will be rate-limited at 60 req/hour");
        }

        private GuideGitHubTemplate(String githubAccessToken) {
            super(githubAccessToken);
        }

        @Override
        protected List<HttpMessageConverter<?>> getMessageConverters() {
            List<HttpMessageConverter<?>> converters = new ArrayList<>();
            converters.add(new JsonStringConverter());
            converters.add(new MarkdownHtmlConverter());
            converters.add(new DownloadConverter());
            converters.add(new StringHttpMessageConverter(Charset.forName("UTF-8")));
            return converters;
        }
    }
}
