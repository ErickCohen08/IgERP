package service;

import java.util.List;

public interface DatoComunService<T> {

    List<T> readDetalle(int CodigoTabla) throws Exception;
    List<T> readTabla(T o) throws Exception;
    List<T> readAll(T o) throws Exception;
    T readId(int Id) throws Exception;    
}
