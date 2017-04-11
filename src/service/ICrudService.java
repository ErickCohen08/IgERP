package service;

import java.util.List;

public interface ICrudService<T> {

    //definir las firmas
    int create(T o) throws Exception;

    int update(T o) throws Exception;

    int delete(T o) throws Exception;

    List<T> read(T o) throws Exception;
    
    T readId(int o) throws Exception;
}
