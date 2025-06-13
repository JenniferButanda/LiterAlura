package com.alura.literalura.principal;

import com.alura.literalura.model.Autor;
import com.alura.literalura.model.DatosAutor;
import com.alura.literalura.model.DatosLibro;
import com.alura.literalura.model.Libro;
import com.alura.literalura.repository.AutorRepository;
import com.alura.literalura.repository.LibroRepository;
import com.alura.literalura.service.ConsumoAPI;
import com.alura.literalura.service.ConvierteDatos;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class Principal {
    private Scanner teclado = new Scanner(System.in);
    private ConsumoAPI consumoAPI = new ConsumoAPI();
    private ConvierteDatos conversor = new ConvierteDatos();
    private final String URL_BASE = "https://gutendex.com/books";
    private List<DatosLibro> datosLibros = new ArrayList<>();
    private LibroRepository libroRepository;
    private List<Libro> libros;
    private List<DatosAutor> datosAutores = new ArrayList<>();
    private AutorRepository autorRepository;
    private List<Autor> autores;

    public Principal(LibroRepository libroRepository, AutorRepository autorRepository) {
        this.libroRepository = libroRepository;
        this.autorRepository = autorRepository;
    }

    public void muestraElMenu() {
        int opcion = -1;
        while (opcion != 0) {
            var menu = """
                    *** BIBLIOTECA DIGITAL ***
                    1 - Buscar libro por título
                    2 - Listar libros registrados
                    3 - Listar autores registrados
                    4 - Listar autores vivos en un determinado año
                    5 - Listar libros por idioma
                    
                    0 - Salir
                    """;
            System.out.println(menu);
            opcion = teclado.nextInt();
            teclado.nextLine();

            switch (opcion) {
                case 1:
                    buscarLibroPorTitulo();
                    break;
                case 2:
                    listarLibrosRegistrados();
                    break;
                case 3:
                    listarAutoresRegistrados();
                    break;
                case 4:
                    listarAutoresVivosEnAnio();
                    break;
                case 5:
                    listarLibrosPorIdioma();
                    break;
                case 0:
                    System.out.println("Cerrando aplicación...");
                    break;
                default:
                    System.out.println("Opción inválida");
            }
        }
    }

    private DatosLibro getDatosLibro() {
        System.out.println("Escribe el nombre del libro que deseas buscar");
        var nombreLibro = teclado.nextLine();
        var json = consumoAPI.obtenerDatos(URL_BASE + "?search=" + URLEncoder.encode(nombreLibro, StandardCharsets.UTF_8));
        //var json = consumoAPI.obtenerDatos("https://gutendex.com/books/?search=gatsby"); //para prueba de la API
        if (json == null || json.isBlank()) {
            System.out.println("No se pudo obtener información del libro.");
            return null;
        }
        System.out.println("Respuesta de la API: " + json); //temporal mientras hago debugging
        DatosLibro datos = conversor.obtenerDatos(json, DatosLibro.class);
        return datos;
    }

    private void buscarLibroPorTitulo() {
        DatosLibro datos = getDatosLibro();
        if (datos == null) {
            System.out.println("Libro no encontrado");
            return;
        }

        //verificando si el libro ya existe en la base de datos:
        Optional<Libro> libroExistente = libroRepository.findByTituloContainsIgnoreCase(datos.titulo());
        if(libroExistente.isPresent()) {
            System.out.println("El libro ya está registrado: " + libroExistente.get());
            return;
        }

        //Guardar libro y autores:
        Libro libro = new Libro(datos);
        datos.autores().forEach(datosAutor -> {
            Autor autor = new Autor(datosAutor);
            autorRepository.save(autor);
            libro.addAutor(autor);
        });
        libroRepository.save(libro);

        //Mostrar datos:
        System.out.println("Título: " + datos.titulo());
        System.out.println("Autor: " + datos.autores().get(0).nombre());
        System.out.println("Idioma: " + datos.idiomas().get(0));
        System.out.println("Descargas: " + datos.numeroDeDescargas());
    }

    private void listarLibrosRegistrados() {
        libros = libroRepository.findAll();
        if (libros.isEmpty()) {
            System.out.println("No hay libros registrados.");
            return;
        }
        libros.forEach(libro -> {
            System.out.println("Título: " + libro.getTitulo());
            System.out.println("Autor: " + libro.getAutores().get(0).getNombre());
            System.out.println("Idioma: " + libro.getIdioma());
            System.out.println("Descargas: " + libro.getNumeroDeDescargas());
            System.out.println("-----");
        });
    }

    private void listarAutoresRegistrados() {
        System.out.println("Función en desarrollo");
    }

    private void listarAutoresVivosEnAnio() {
        System.out.println("Función en desarrollo");
    }

    private void listarLibrosPorIdioma() {
        System.out.println("Función en desarrollo");
    }
}