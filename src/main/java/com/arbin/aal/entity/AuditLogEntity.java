package com.arbin.aal.entity;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;
import org.springframework.data.cassandra.core.mapping.Table;

import java.sql.Timestamp;
import java.util.UUID;

@Data
@Table(value = "audit_log")
public class AuditLogEntity {

    @PrimaryKeyColumn(name = "app_name", ordinal = 0, type = PrimaryKeyType.PARTITIONED)
    private String appName;

    @PrimaryKeyColumn(name = "tenant_id", ordinal = 1, type = PrimaryKeyType.PARTITIONED)
    private UUID tenantId;

    @PrimaryKeyColumn(name = "created_time", ordinal = 2, type = PrimaryKeyType.CLUSTERED)
    private Timestamp createdTime;

    @Column("customer_id")
    private UUID customerId;

    @Column("entity_type")
    private String entityType;

    @Column("entity_id")
    private UUID entityId;

    @Column("entity_name")
    private String entityName;

    @Column("user_id")
    private UUID userId;

    @Column("user_name")
    private String userName;

    @Column("action_type")
    private String actionType;

    @Column("action_data")
    private String actionData;

    @Column("action_status")
    private String actionStatus;

    @Column("action_failure_details")
    private String actionFailureDetails;
}
