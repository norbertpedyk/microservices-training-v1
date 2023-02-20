package pl.pedyk.resource.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.pedyk.resource.model.ResourceTracking;

@Repository
public interface ResourceTrackingRepository extends JpaRepository<ResourceTracking, Long> {
}
