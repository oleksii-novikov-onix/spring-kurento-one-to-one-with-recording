package com.onix.springkurento.websocket.service;

import com.onix.springkurento.websocket.UserSession;
import org.springframework.stereotype.Service;

import java.util.concurrent.ConcurrentHashMap;

@Service
public final class UserService {

    private ConcurrentHashMap<Integer, UserSession> usersById = new ConcurrentHashMap<>();
    private ConcurrentHashMap<String, UserSession> usersByName = new ConcurrentHashMap<>();

    public void register(final UserSession user) {
        this.usersByName.put(user.getName(), user);
        this.usersById.put(user.getId(), user);
    }

    public UserSession getByName(final String name) {
        return this.usersByName.get(name);
    }

    public UserSession getById(final Integer id) {
        return this.usersById.get(id);
    }

    public boolean exists(final String name) {
        return this.usersByName.keySet().contains(name);
    }

    public boolean existsById(final Integer id) {
        return this.usersById.keySet().contains(id);
    }

    public void removeById(final Integer id) {
        final UserSession user = this.getById(id);

        if (user != null) {
            this.usersByName.remove(user.getName());
            this.usersById.remove(id);
        }
    }

}
