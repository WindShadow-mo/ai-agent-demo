package ws.ai.demo.log;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * @author WindShadow
 * @version 2026-10-05
 */
@Slf4j
@WebFilter(urlPatterns = "/*", asyncSupported = true)
public class WebAccessLoggerFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String uri = request.getRequestURI();
        String requestContentType = request.getContentType();
        filterChain.doFilter(request, response);
        int status = response.getStatus();
        String responseContentType = response.getContentType();
        log.info("uri:{} status:[{}] ContentType:[{}] => [{}]", uri, status, requestContentType, responseContentType);
    }
}
