package com.alura.literalura.repository;

import com.alura.literalura.model.Autor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AutorRepository extends JpaRepository<Autor, Long> {
    List<Autor> findAll();

    @Query("SELECT DISTINCT a FROM Autor a LEFT JOIN FETCH a.libros")
    List<Autor> findAllWithLibros();

    @Query("SELECT DISTINCT a FROM Autor a LEFT JOIN FETCH a.libros WHERE a.anioNacimiento <= :anio AND (a.anioMuerte IS NULL OR a.anioMuerte >= :anio)")
    List<Autor> findAutoresVivosEnAnio(@Param("anio") Integer anio);
}