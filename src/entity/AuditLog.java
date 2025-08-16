package entity;

import java.time.LocalDateTime;

public record AuditLog(String userId, String action, LocalDateTime timestamp) {
    
    public AuditLog {
        if (userId == null || userId.isBlank()) {
            throw new IllegalArgumentException("User ID cannot be null or blank");
        }
        if (action == null || action.isBlank()) {
            throw new IllegalArgumentException("Action cannot be null or blank");
        }
        if (timestamp == null) {
            timestamp = LocalDateTime.now();
        }
    }
    
    public AuditLog(String userId, String action) {
        this(userId, action, LocalDateTime.now());
    }
    
    public String toFormattedString() {
        return String.format("[%s] User %s: %s", 
            timestamp.format(java.time.format.DateTimeFormatter.ISO_LOCAL_DATE_TIME),
            userId,
            action);
    }
}
