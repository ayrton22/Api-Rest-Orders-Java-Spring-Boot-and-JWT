package com.codmain.orderapi.converters;

import java.util.List;
import java.util.stream.Collectors;

// funcion convertir de entidad a DTO y de DTO a entidad, pero también que me permita convertir listas 
public abstract class AbstractConverter<E, D> {
    public abstract D fromEntity(E entity);

    public abstract E fromDTO(D dto);

    // esta no es abstracta porque si ya tenemos la logica para convertir un objetom
    // individual la podemos reutilizar para convertir una lista

    public List<D> fromEntity(List<E> entitys) {
        // aca lo que estamos haciendo es iterar en cada uno de los productos y por cada
        // producto de tipo entidad estamos retornando un nuevo producto de tipo DTO y
        // eso lo pasamos a un lista
        return entitys.stream().map(e -> fromEntity(e)).collect(Collectors.toList());
    }

    public List<E> fromDTO(List<D> dtos) {
        return dtos.stream().map(d -> fromDTO(d)).collect(Collectors.toList());
    }
}
