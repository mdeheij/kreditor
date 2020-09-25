package nl.kreditor.security;

import nl.kreditor.model.User;

public interface Authenticatable {
    User getUser();
}