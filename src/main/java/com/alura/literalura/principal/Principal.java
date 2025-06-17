package com.alura.literalura.principal;

import com.alura.literalura.model.*;
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

    private void imprimirLibro(Libro libro) {
        System.out.println("Título: " + libro.getTitulo());
        System.out.println("Autor: " + libro.getAutores().get(0).getNombre());
        System.out.println("Idioma: " + libro.getIdioma());
        System.out.println("Descargas: " + libro.getNumeroDeDescargas());
    }

    private void buscarLibroPorTitulo() {
        System.out.println("Escribe el nombre del libro que deseas buscar:");
        String nombreBuscado = teclado.nextLine();

        //Buscar en la base de datos primero
        Optional<Libro> libroEnBD = libroRepository.findByTituloContainsIgnoreCaseFetchAutores(nombreBuscado);

        if(libroEnBD.isPresent()) {
            System.out.println("\n--------------------------------------\n");
            imprimirLibro(libroEnBD.get());
            System.out.println("\n--------------------------------------\n");
            return;
        }

        //Si no está en la base de datos, buscar en la API
        String url = URL_BASE + "/?search=" + URLEncoder.encode(nombreBuscado, StandardCharsets.UTF_8);
        String json = consumoAPI.obtenerDatos(url);

        ResultadoBusqueda resultado = conversor.obtenerDatos(json, ResultadoBusqueda.class);

        if (resultado.results() == null || resultado.results().isEmpty()) {
            System.out.println("Libro no encontrado.");
            return;
        }

        //Tomar el primer resultado de la API
        DatosLibro datosLibro = resultado.results().get(0);

        //Convertir DatosLibro - Libro
        Libro nuevoLibro = new Libro(datosLibro);

        //Guardar en la base de datos
        libroRepository.save(nuevoLibro);

        //Mostrar la información
        System.out.println("Libro encontrado en la API y guardado en la base de datos");
        imprimirLibro(nuevoLibro);
    }

    private void listarLibrosRegistrados() {
        System.out.println("Función en desarrollo");
//        libros = libroRepository.findAll();
//        if (libros.isEmpty()) {
//            System.out.println("No hay libros registrados.");
//            return;
//        }
//        libros.forEach(libro -> {
//            System.out.println("Título: " + libro.getTitulo());
//            System.out.println("Autor: " + libro.getAutores().get(0).getNombre());
//            System.out.println("Idioma: " + libro.getIdioma());
//            System.out.println("Descargas: " + libro.getNumeroDeDescargas());
//            System.out.println("-----");
//        });
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