package patrones.prototype;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * Registro de prototipos: guarda una plantilla ya inicializada por nombre y
 * entrega siempre una copia, nunca el original.
 */
public class RegistroPlantillas {

    private final Map<String, Informe> prototipos = new HashMap<>();

    public void registrar(String nombre, Informe prototipo) {
        Objects.requireNonNull(nombre, "nombre");
        Objects.requireNonNull(prototipo, "prototipo");
        prototipos.put(nombre, prototipo.copiar()); // se guarda una copia defensiva
    }

    /**
     * @throws IllegalArgumentException si no existe una plantilla con ese nombre
     */
    public Informe crear(String nombre) {
        Informe prototipo = prototipos.get(nombre);
        if (prototipo == null) {
            throw new IllegalArgumentException("No existe la plantilla: " + nombre);
        }
        return prototipo.copiar();
    }

    public Set<String> nombres() {
        return Set.copyOf(prototipos.keySet());
    }
}
