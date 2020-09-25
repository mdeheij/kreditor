package nl.kreditor.security;

import nl.kreditor.model.User;
import nl.kreditor.security.services.KreditorUserDetails;
import nl.kreditor.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationFacade implements Authenticatable {
    @Autowired
    private UserService userService;

    @Override
    public Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    public KreditorUserDetails getUserDetails() {
        return (KreditorUserDetails) this.getAuthentication().getPrincipal();
    }

    public User getUser() {
        return userService.findById(this.getUserDetails().getId());
    }
}