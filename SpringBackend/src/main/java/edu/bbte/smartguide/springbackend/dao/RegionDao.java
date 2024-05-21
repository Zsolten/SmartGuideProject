package edu.bbte.smartguide.springbackend.dao;

import edu.bbte.smartguide.springbackend.model.Region;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;

public interface RegionDao  extends Dao<Region>, JpaRepository<Region, Long> {
    Collection<Region> findByName(String name);
}
