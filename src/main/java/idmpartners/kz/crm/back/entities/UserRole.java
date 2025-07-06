package idmpartners.kz.crm.back.entities;

import idmpartners.kz.crm.back.enums.ModuleType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "user_roles")
@Getter
@Setter
public class UserRole {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role_id", nullable = false)
    private Role role;

    @Enumerated(EnumType.STRING)
    @Column(name = "module_type")
    private ModuleType moduleType;

    @Column(name = "assigned_by")
    private UUID assignedBy;

    @Column(name = "assigned_at")
    private LocalDateTime assignedAt;

    @Column(name = "expires_at")
    private LocalDateTime expiresAt;

    @Column(name = "is_active")
    private boolean active = true;

    @Column(name = "scope_department")
    private String scopeDepartment;

    @Column(name = "scope_team")
    private String scopeTeam;

    @Column(name = "scope_region")
    private String scopeRegion;

    @PrePersist
    protected void onCreate() {
        assignedAt = LocalDateTime.now();
    }
}
