package edu.bbte.smartguide.springbackend.dao;

import edu.bbte.smartguide.springbackend.model.BaseEntity;

import java.util.Collection;

public interface Dao<T extends BaseEntity> {
//    T getById(Long id);

//    T saveAndFlush(T entity);

//    void deleteById(Long id);

    Collection<T> findAll();

}
