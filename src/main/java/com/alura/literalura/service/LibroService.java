package com.alura.literalura.service;

import com.alura.literalura.model.Autor;
import com.alura.literalura.model.DatosLibro;
import com.alura.literalura.model.Libro;
import com.alura.literalura.repository.AutorRepository;
import com.alura.literalura.repository.LibroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class LibroService {

    @Autowired
    private LibroRepository libroRepository;

    @Autowired
    private AutorRepository autorRepository;

    public void guardarLibro(DatosLibro datosLibro) {
        Libro libro = new Libro(datosLibro);

        List<Autor> autores = datosLibro.autores().stream()
                .map(Autor::new)
                .collect(Collectors.toList());

        autores.forEach(a -> a.setLibro(libro));
        libro.setAutores(autores);

        libroRepository.save(libro);
    }

    public Optional<Libro> buscarPorTitulo(String titulo) {
        return libroRepository.findByTituloContainsIgnoreCase(titulo);
    }

    public List<Libro> listarTodosLosLibros() {
        return libroRepository.findAll();
    }

    public List<Autor> listarAutores() {
        return autorRepository.findAll();
    }

    public List<Autor> autoresVivosEnAnio(Integer anio) {
        return autorRepository.findAutoresVivosEnAnio(anio);
    }

    public List<Libro> librosPorIdioma(String idioma) {
        return libroRepository.findByIdiomaContaining(idioma);
    }
}