package patrones.prototype;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Informe que actúa como prototipo. {@link #copiar()} hace copia profunda:
 * las secciones y los estilos de la copia son objetos nuevos, así que
 * modificar la copia nunca altera el prototipo (problema de la pregunta 10).
 */
public class Informe implements Prototipo<Informe> {

    private String titulo;
    private String destinatario;
    private final List<Seccion> secciones;
    private final Map<String, String> estilos;

    public Informe(String titulo) {
        this.titulo = Objects.requireNonNull(titulo, "titulo");
        this.destinatario = "";
        this.secciones = new ArrayList<>();
        this.estilos = new LinkedHashMap<>();
    }

    /** Constructor de copia profunda. */
    private Informe(Informe otro) {
        this.titulo = otro.titulo;
        this.destinatario = otro.destinatario;
        this.secciones = new ArrayList<>(otro.secciones.size());
        for (Seccion s : otro.secciones) {
            this.secciones.add(s.copiar());
        }
        this.estilos = new LinkedHashMap<>(otro.estilos); // String es inmutable
    }

    @Override
    public Informe copiar() {
        return new Informe(this);
    }

    public Informe agregarSeccion(Seccion seccion) {
        secciones.add(Objects.requireNonNull(seccion, "seccion"));
        return this;
    }

    public Informe definirEstilo(String clave, String valor) {
        estilos.put(Objects.requireNonNull(clave), Objects.requireNonNull(valor));
        return this;
    }

    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = Objects.requireNonNull(titulo); }
    public String getDestinatario() { return destinatario; }
    public void setDestinatario(String destinatario) { this.destinatario = Objects.requireNonNull(destinatario); }

    /** Vista de solo lectura; para editar una sección se usa {@link #seccion(int)}. */
    public List<Seccion> getSecciones() { return Collections.unmodifiableList(secciones); }

    public Seccion seccion(int indice) { return secciones.get(indice); }

    public Map<String, String> getEstilos() { return Collections.unmodifiableMap(estilos); }

    @Override
    public String toString() {
        return "Informe{" + titulo + (destinatario.isEmpty() ? "" : " para " + destinatario)
                + ", " + secciones.size() + " secciones, estilos=" + estilos + "}";
    }
}
