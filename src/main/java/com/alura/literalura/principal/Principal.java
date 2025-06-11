package com.alura.literalura.principal;

import com.alura.literalura.model.Autor;
import com.alura.literalura.model.DatosAutor;
import com.alura.literalura.model.DatosLibro;
import com.alura.literalura.model.Libro;
import com.alura.literalura.repository.AutorRepository;
import com.alura.literalura.repository.LibroRepository;
import com.alura.literalura.service.AutorService;
import com.alura.literalura.service.ConsumoAPI;
import com.alura.literalura.service.ConvierteDatos;
import com.alura.literalura.service.LibroService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

@Component
public class Principal {
    private Scanner teclado = new Scanner(System.in);
    private ConsumoAPI consumoAPI = new ConsumoAPI();
    private ConvierteDatos conversor = new ConvierteDatos();
    private final String URL_BASE = "https://gutendex.com/books/?search=";

    private final LibroService libroService;
    private final AutorService autorService;

    @Autowired
    public Principal(AutorService autorService) {
        this.autorService = autorService;
    }

    private LibroRepository repositorio;
    private LibroService libroService;

    public Principal(LibroRepository repositorio) {
        this.repositorio = repositorio;
        this.libroService = new LibroService(repositorio);
    }

    public void muestraElMenu() {
        int opcion = -1;
        while (opcion != 0) {
            var menu = ("""
                    
                    === BIBLIOTECA DIGITAL ===
                    1 - Buscar libro por título
                    2 - Listar libros registrados
                    3 - Listar autores registrados
                    4 - Listar autores vivos en un determinado año
                    5 - Listar libros por idioma
                    
                    0 - Salir
                    """);

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

    private void buscarLibroPorTitulo() {
        System.out.print("Escribe el título del libro que deseas buscar: ");
        String titulo = teclado.nextLine();

        String json = consumoAPI.obtenerDatos(URL_BASE + titulo.replace(" ", "+"));
        DatosLibro resultado = conversor.obtenerDatos(json, DatosLibro.class);

        if (resultado != null && !resultado.resultados().isEmpty()) {
            DatosLibro datosLibro = resultado.resultados().get(0);  // Solo el primero
            Libro libro = new Libro(datosLibro);
            libroService.guardarLibro(libro);
            System.out.println("Libro guardado: \n" + libro);
        } else {
            System.out.println("No se encontró ningún libro con ese título.");
        }
    }

    private void listarLibrosRegistrados() {
        List<Libro> libros = libroService.listarLibros();
        libros.forEach(System.out::println);
    }

    private void listarAutoresRegistrados() {
        List<Autor> autores = libroService.listarAutores();
        autores.forEach(System.out::println);
    }

    private void listarAutoresVivosEnAnio() {
        System.out.print("Introduce el año para buscar autores vivos en ese periodo: ");
        int anio = teclado.nextInt();
        teclado.nextLine();

        List<Autor> autoresVivos = libroService.autoresVivosEn(anio);
        if (autoresVivos.isEmpty()) {
            System.out.println("No se encontraron autores vivos en ese año.");
        } else {
            autoresVivos.forEach(System.out::println);
        }
    }

    private void listarLibrosPorIdioma() {
        System.out.print("Introduce el código de idioma (por ejemplo: 'en' para inglés, 'es' para español): ");
        String idioma = teclado.nextLine();

        List<Libro> librosPorIdioma = libroService.librosPorIdioma(idioma);
        if (librosPorIdioma.isEmpty()) {
            System.out.println("No hay libros en ese idioma.");
        } else {
            librosPorIdioma.forEach(System.out::println);
        }
    }
}