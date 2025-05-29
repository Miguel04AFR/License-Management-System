package services;

import java.util.List;

public interface EntityService<T> {
    List<T> getAll();
    boolean create(T entity);
    boolean update(T entity);
    boolean delete(String id);
    T getById(String id);
}