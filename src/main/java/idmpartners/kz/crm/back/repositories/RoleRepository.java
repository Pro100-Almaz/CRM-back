package idmpartners.kz.crm.back.repositories;

import idmpartners.kz.crm.back.entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface RoleRepository extends JpaRepository<Role, UUID> {
}
