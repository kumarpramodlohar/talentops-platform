package com.talentops.audit;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "audit_logs")
@Access(AccessType.FIELD)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AuditLogEntity {

    @Id
    @GeneratedValue
    @Column(columnDefinition = "uuid")
    private UUID id;

    private UUID userId;
    private String action;
    private String entity;
    private UUID entityId;
    private String ipAddress;

    public static AuditLogEntity record(
            UUID userId, String action, String entity, UUID entityId, String ip
    ) {
        AuditLogEntity auditlog = new AuditLogEntity();
        auditlog.userId = userId;
        auditlog.action = action;
        auditlog.entity = entity;
        auditlog.entityId = entityId;
        auditlog.ipAddress = ip;
        return auditlog;
    }

    public UUID getId() {
        return id;
    }

    public UUID getUserId() {
        return userId;
    }

    public String getAction() {
        return action;
    }

    public String getEntity() {
        return entity;
    }

    public UUID getEntityId() {
        return entityId;
    }

    public String getIpAddress() {
        return ipAddress;
    }
}

