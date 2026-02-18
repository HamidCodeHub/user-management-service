package com.hamid.usermanagement.event;

import com.hamid.usermanagement.entity.User;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

import java.time.LocalDateTime;

@Getter
public class UserCreatedEvent extends ApplicationEvent {

    private final User user;
    private final String createdBy;
    private final LocalDateTime timestamp;

    public UserCreatedEvent(Object source, User user, String createdBy) {
        super(source);
        this.user = user;
        this.createdBy = createdBy;
        this.timestamp = LocalDateTime.now();
    }
}