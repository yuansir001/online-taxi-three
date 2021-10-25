package com.mashibing.cloudzuul.filter;

import com.google.common.util.concurrent.RateLimiter;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * 网关限流
 */
@Component
public class LimitFilter implements Filter {

    // 令牌桶 每秒生成两个
    private static final RateLimiter RATE_LIMITER = RateLimiter.create(2);

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        if (RATE_LIMITER.tryAcquire()){
            filterChain.doFilter(servletRequest,servletResponse);
        }else {
            servletResponse.setCharacterEncoding("utf-8");
            servletResponse.setContentType("text/html; charset=utf-8");
            PrintWriter writer = servletResponse.getWriter();
            writer.write("限流了zuul");
        }
    }

    @Override
    public void destroy() {

    }
}
