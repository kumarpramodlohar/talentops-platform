package com.talentops.notification;

import com.talentops.user.UserEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "notifications")
@Access(AccessType.FIELD)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class NotificationEntity {

    @Id
    @GeneratedValue
    @Column(columnDefinition = "uuid")
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserEntity user;

    private String eventType;
    private String title;
    private String message;
    private boolean isRead;

    public static NotificationEntity create(UserEntity user, String event, String title, String message) {
        NotificationEntity notification = new NotificationEntity();
        notification.user = user;
        notification.eventType = event;
        notification.title = title;
        notification.message = message;
        notification.isRead = false;
        return notification;
    }

    public void markRead() {
        this.isRead = true;
    }

    public UUID getId() {
        return id;
    }

    public UserEntity getUser() {
        return user;
    }

    public String getEventType() {
        return eventType;
    }

    public String getTitle() {
        return title;
    }

    public String getMessage() {
        return message;
    }

    public boolean isRead() {
        return isRead;
    }
}

