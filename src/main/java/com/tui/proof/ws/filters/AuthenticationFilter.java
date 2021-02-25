package com.tui.proof.ws.filters;


import javax.servlet.Filter;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Log4j2
@Component
public class AuthenticationFilter implements Filter {

    @Autowired
    private Environment env;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        String givenApiKey = req.getHeader("apiKey");
        String givenSecret = req.getHeader("secret");
        String apiKey = env.getProperty("apiKey");
        String secret = env.getProperty("secret");
        if (givenApiKey == null || givenSecret == null || givenApiKey.isEmpty() || givenSecret.isEmpty() || apiKey == null
                || secret == null || !apiKey.equals(givenApiKey) || !secret.equals(givenSecret)) {
            ((HttpServletResponse) response).sendError(401, "Unauthorized!");
        } else {
            log.debug("User "+ givenApiKey+" authenticated");
            chain.doFilter(request, response);
        }
    }
}
