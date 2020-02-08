package com.hrodberaht.inject.extensions.transaction.web;

import com.hrodberaht.inject.extensions.transaction.TransactionManager;
import com.hrodberaht.inject.extensions.transaction.manager.TransactionManagerResolver;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.IOException;

/**
 * Injection Transaction Extension
 *
 * @author Robert Alexandersson
 * 2010-aug-25 23:05:59
 * @version 1.0
 * @since 1.0
 */
public class OpenTransactionInViewFilter implements Filter {

    private TransactionManager transactionManager;

    public void doFilter(ServletRequest request,
                         ServletResponse response,
                         FilterChain chain)
            throws IOException, ServletException {

        try {
            transactionManager.begin();

            // Call the next filter (continue request processing)
            chain.doFilter(request, response);

            transactionManager.commit();

        } catch (Throwable ex) {
            // Rollback only
            ex.printStackTrace(System.err);
            try {
                if (transactionManager.isActive()) {
                    transactionManager.rollback();
                }
            } catch (Throwable e) {
                e.printStackTrace(System.err);
            }

            // Let others handle it... maybe another interceptor for exceptions?
            throw new ServletException(ex);
        }
    }

    public void init(FilterConfig filterConfig) throws ServletException {
        String transactionResolver = filterConfig.getInitParameter("transactionManagerResolver");
        try {
            Class<TransactionManagerResolver> aClass =
                    (Class<TransactionManagerResolver>) Class.forName(transactionResolver);

            TransactionManagerResolver transactionManagerResolver = aClass.newInstance();

            transactionManager = transactionManagerResolver.getTransactionManager();
        } catch (ClassNotFoundException e) {
            e.printStackTrace(System.err);
        } catch (InstantiationException e) {
            e.printStackTrace(System.err);
        } catch (IllegalAccessException e) {
            e.printStackTrace(System.err);
        }

    }

    public void destroy() {
    }
}
