package lu.llemaire.xchange_transfer.configuration;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import org.apache.logging.log4j.ThreadContext;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.UUID;

@Component
public class RequestIdFilter implements Filter {
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        try {
           String requestId = UUID.randomUUID().toString();
            ThreadContext.put("requestId", requestId);
            filterChain.doFilter(servletRequest, servletResponse);
        } finally {
            ThreadContext.remove("requestId");
        }
    }
}
