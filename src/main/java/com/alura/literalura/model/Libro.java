package com.alura.literalura.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "libros")
public class Libro {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;
    @Column(unique = true)
    private String titulo;
//    @ManyToMany(mappedBy = "libros", cascade = CascadeType.PERSIST)
//    private List<Autor> autores = new ArrayList<>();
    private String idioma;
    private Integer numeroDeDescargas;
    @ManyToMany(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @JoinTable(
            name = "libro_autor",
            joinColumns = @JoinColumn(name = "libro_id"),
            inverseJoinColumns = @JoinColumn(name = "autor_id")
    )
    private List<Autor> autores = new ArrayList<>();


    public Libro(){}

    public Libro(DatosLibro datosLibro) {
        this.titulo = datosLibro.titulo();
        this.idioma = datosLibro.idiomas() != null
                ? String.join(", ", datosLibro.idiomas())
                : "Idioma no disponible";
        this.numeroDeDescargas = datosLibro.numeroDeDescargas();

        this.autores = datosLibro.autores().stream()
                .map(datosAutor -> {
                    Autor autor = new Autor(datosAutor);
                    autor.getLibros().add(this);
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

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public List<Autor> getAutores() {
        return autores;
    }

    public void setAutores(List<Autor> autores) { this.autores = autores;
    }

    public String getIdioma() {
        return idioma;
    }

    public void setIdioma(String idioma) {
        this.idioma = idioma;
    }

    public Integer getNumeroDeDescargas() {
        return numeroDeDescargas;
    }

    public void setNumeroDeDescargas(Integer numeroDeDescargas) {
        this.numeroDeDescargas = numeroDeDescargas;
    }

    public void addAutor(Autor autor) {
        this.autores.add(autor);
        autor.getLibros().add(this);
    }
}