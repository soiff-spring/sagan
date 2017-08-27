package sagan.blog.support;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sagan.util.GithubService;

/**
 * A {@link MarkdownService} based on the GitHub Markdown rendering API.
 */
@Service
class DefaultMarkdownService implements MarkdownService {

    private final GithubService service;

    @Autowired
    public DefaultMarkdownService(GithubService service) {
        this.service = service;
    }

    /**
     * Process the given markdown through GitHub's Markdown rendering API. See
     * http://developer.github.com/v3/markdown
     */
    @Override
    public String renderToHtml(String markdownSource) {
        return service.renderToHtml(markdownSource);
    }

}
