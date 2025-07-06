package idmpartners.kz.crm.back.repositories;

import idmpartners.kz.crm.back.entities.Permission;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PermissionRepository extends JpaRepository<Permission, UUID> {
}
