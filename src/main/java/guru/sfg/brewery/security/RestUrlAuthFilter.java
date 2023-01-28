package guru.sfg.brewery.security;

import org.springframework.security.web.util.matcher.RequestMatcher;

import javax.servlet.http.HttpServletRequest;

public class RestUrlAuthFilter extends AbstractRestAuthFilter{

    public RestUrlAuthFilter(RequestMatcher requiresAuthenticationRequestMatcher) {
        super(requiresAuthenticationRequestMatcher);
    }

    protected String getPassword(HttpServletRequest httpServletRequest) {
        return httpServletRequest.getParameter("Api-Secret");
    }

    protected String getUserName(HttpServletRequest httpServletRequest) {
        return httpServletRequest.getParameter("Api-Key");
    }
}
