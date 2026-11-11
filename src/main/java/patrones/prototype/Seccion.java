package patrones.prototype;

import java.util.Objects;

/** Sección de un informe. Es mutable, por eso el informe debe copiarla en profundidad. */
public class Seccion implements Prototipo<Seccion> {

    private String titulo;
    private String contenido;

    public Seccion(String titulo, String contenido) {
        this.titulo = Objects.requireNonNull(titulo, "titulo");
        this.contenido = Objects.requireNonNull(contenido, "contenido");
    }

    /** Constructor de copia. */
    private Seccion(Seccion otra) {
        this(otra.titulo, otra.contenido);
    }

    @Override
    public Seccion copiar() {
        return new Seccion(this);
    }

    public String getTitulo() { return titulo; }
    public String getContenido() { return contenido; }
    public void setTitulo(String titulo) { this.titulo = Objects.requireNonNull(titulo); }
    public void setContenido(String contenido) { this.contenido = Objects.requireNonNull(contenido); }

    @Override
    public String toString() {
        return titulo + ": " + contenido;
    }
}
