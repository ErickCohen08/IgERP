package service;

import java.util.List;

public interface ProveedorService<T> {

    List<T> readTablaBuscar(String valor) throws Exception;    
}
