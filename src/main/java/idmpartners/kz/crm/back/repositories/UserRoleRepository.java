package idmpartners.kz.crm.back.repositories;

import idmpartners.kz.crm.back.entities.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface UserRoleRepository extends JpaRepository<UserRole, UUID> {
    List<UserRole> findByUserId(UUID userId);

    List<UserRole> findByUserIdAndActiveTrue(UUID userId);
}
