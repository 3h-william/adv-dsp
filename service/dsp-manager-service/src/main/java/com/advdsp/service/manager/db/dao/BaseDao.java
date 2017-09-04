package com.advdsp.service.manager.db.dao;

import org.springframework.stereotype.Repository;

import java.util.List;

/**
 */
@Repository
public interface BaseDao<T> {

    /**
     * save an entity
     *
     * @param obj
     */
    void saveObject(T obj);

    T getObjectByID(Object id);

    void updateObjectById(Object dto);

    List<T> getAll();

    void deleteById(Object id);

    void saveObjects(List<T> obj);

}
