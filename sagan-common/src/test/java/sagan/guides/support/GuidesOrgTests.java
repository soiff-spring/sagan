package sagan.guides.support;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.CoreMatchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.social.github.api.GitHubRepo;
import sagan.git.GitClient;
import sagan.support.Fixtures;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.anyVararg;

@RunWith(MockitoJUnitRunner.class)
public class GuidesOrgTests {

    private static final String OWNER_TYPE = "orgs";
    private static final String OWNER_NAME = "my-org";

    @Mock
    private GitClient ghClient;
    private GuideOrganization service;

    @Before
    public void setUp() throws Exception {
        service = new GuideOrganization(OWNER_NAME, OWNER_TYPE, ghClient, new ObjectMapper(), null);
    }

    @Test
    public void getRawFileAsHtml_fetchesRenderedHtmlFromGitHub() throws Exception {
        given(ghClient.sendRequestForHtml("/path/to/html")).willReturn("<h1>Something</h1>");

        assertThat(service.getMarkdownFileAsHtml("/path/to/html"), equalTo("<h1>Something</h1>"));
    }

    @Test
    public void getRepoInfo_fetchesGitHubRepos() {
        given(ghClient.sendRequestForJson(anyString(), anyVararg())).willReturn(Fixtures.githubRepoJson());

        GitHubRepo repoInfo = service.getRepoInfo("repo");
        assertThat(repoInfo.getName(), equalTo("spring-boot"));
    }

    @Test
    public void getGitHubRepos_fetchesGuideReposGitHub() {
        given(ghClient.sendRequestForJson(anyString(), anyVararg())).willReturn(Fixtures.githubRepoListJson());

        GitHubRepo[] repos = service.findAllRepositories();
        assertThat(repos[0].getName(), equalTo("gs-rest-service"));
    }

    @Test
    public void shouldFindByPrefix() throws Exception {
        given(ghClient.sendRequestForJson(anyString(), anyVararg())).willReturn(Fixtures.githubRepoListJson());

        List<GitHubRepo> matches = service.findRepositoriesByPrefix(GettingStartedGuides.REPO_PREFIX);
        assertThat(matches.size(), greaterThan(0));
        for (GitHubRepo match : matches) {
            assertThat(match.getName(), CoreMatchers.startsWith(GettingStartedGuides.REPO_PREFIX));
        }
    }

}
