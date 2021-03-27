package org.beer30.leaf.repository;

import org.beer30.leaf.domain.Cardholder;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CardholderRepository extends JpaRepository<Cardholder, Long> {
    Cardholder findByExternalIdAndEnvId(Long externalId, Integer envId);
}
