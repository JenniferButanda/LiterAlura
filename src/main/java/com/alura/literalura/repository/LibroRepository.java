package com.alura.literalura.repository;

import com.alura.literalura.model.Libro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface LibroRepository extends JpaRepository<Libro, Long> {
    @Query("SELECT l FROM Libro l JOIN FETCH l.autores WHERE LOWER(l.titulo) LIKE LOWER(CONCAT('%', :titulo, '%'))")
    Optional<Libro> findByTituloContainsIgnoreCaseFetchAutores(@Param("titulo") String titulo);

    @Query("SELECT DISTINCT l FROM Libro l JOIN FETCH l.autores WHERE l.idioma = :idioma")
    List<Libro> findByIdioma(@Param("idioma") String idioma);

    List<Libro> findAll();

    @Query("SELECT DISTINCT l FROM Libro l JOIN FETCH l.autores")
    List<Libro> findAllWithAutores();

}