package edu.fer.project.questionnaire.security;

import com.vaadin.flow.server.HandlerHelper.RequestType;
import com.vaadin.flow.shared.ApplicationConstants;
import java.util.stream.Stream;
import javax.servlet.http.HttpServletRequest;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public final class SecurityUtils {

  private SecurityUtils() {
  }

  static boolean isFrameworkInternalRequest(HttpServletRequest request) {
    final String parameterValue = request.getParameter(ApplicationConstants.REQUEST_TYPE_PARAMETER);
    return parameterValue != null
        && Stream.of(RequestType.values())
        .anyMatch(r -> r.getIdentifier().equals(parameterValue));
  }

  static boolean isUserLoggedIn() {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    return auth != null
        && !(auth instanceof AnonymousAuthenticationToken)
        && auth.isAuthenticated();
  }
}
