package com.alura.literalura.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "libros")
public class Libro {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;
    @Column(unique = true)
    private String titulo;
    @OneToMany(mappedBy = "libro", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Autor> autores;
    private List<String> idioma;
    private Integer numeroDeDescargas;

    public Libro(){}

    public Libro(DatosLibro datosLibro) {
        this.titulo = datosLibro.titulo();
        this.idioma = datosLibro.idiomas();
        this.numeroDeDescargas = datosLibro.numeroDeDescargas();

        this.autores = datosLibro.autores().stream()
                .map(datosAutor -> {
                    Autor autor = new Autor(datosAutor);
                    autor.setLibro(this);
                    return autor;
                })
                .toList();
    }

    @Override
    public String toString() {
        return "titulo: " + titulo + '\'' +
                ", autor: " + autores + '\'' +
                ", idioma: " + idioma + '\'' +
                ", descargas" + numeroDeDescargas + '\'';
    }

    public List<Autor> getAutores() {
        return null;
    }

    public void setAutores(List<Autor> autores) {
    }
}