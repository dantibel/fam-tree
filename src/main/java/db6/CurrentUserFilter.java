package db6;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import db6.domain.AppUser;
import jakarta.servlet.*;


@Component
public class CurrentUserFilter implements Filter {

    @Autowired
    private DataSource dataSource;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            Object principal = authentication.getPrincipal();
            if (principal instanceof db6.domain.AppUser) {
                AppUser user = (AppUser) principal;
                try (Connection connection = dataSource.getConnection()) {
                    PreparedStatement stmt = connection.prepareStatement("SET app.logged_user_id = ?");
                    stmt.setLong(1, user.getId());
                    stmt.execute();
                } catch (Exception e) {
                    throw new ServletException("Failed to set app.logged_user_id", e);
                }
            }
        }
        chain.doFilter(request, response);
    }

}
