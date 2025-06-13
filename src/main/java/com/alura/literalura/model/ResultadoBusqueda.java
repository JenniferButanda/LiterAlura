package com.alura.literalura.model;

import java.util.List;

public record ResultadoBusqueda(
        int count,
        String next,
        String previous,
        List<DatosLibro> results
) {}