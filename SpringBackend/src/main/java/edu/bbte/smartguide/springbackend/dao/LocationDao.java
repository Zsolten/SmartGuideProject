package edu.bbte.smartguide.springbackend.dao;

import edu.bbte.smartguide.springbackend.model.Location;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
public interface LocationDao extends Dao<Location>, JpaRepository<Location, Long> {
    Collection<Location> findByName(String name);
}
