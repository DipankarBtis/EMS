package com.etrm.fms.filters;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.regex.Pattern;

public class MaliciousQueryBlockerFilter implements Filter {
    private static final Pattern MALICIOUS_PATTERN = Pattern.compile(
        "(<script>|</script>|%3Cscript%3E|%3C/style%3E|-->)",
        Pattern.CASE_INSENSITIVE
    );

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        if (request instanceof HttpServletRequest) {
            HttpServletRequest req = (HttpServletRequest) request;
            String queryString = req.getQueryString();

            if (queryString != null && MALICIOUS_PATTERN.matcher(queryString).find()) {
                // Log it, optionally send notification
                throw new ServletException("Malicious query detected");
            }
        }

        chain.doFilter(request, response);
    }

    public void init(FilterConfig filterConfig) {}
    public void destroy() {}
}