package patrones.builder;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.Optional;

/**
 * Solicitud de crédito inmutable construida con el patrón Builder.
 *
 * <ul>
 *   <li>Obligatorios: solicitante, documento, monto y plazo en meses.</li>
 *   <li>Opcionales: codeudor, garantía, descripción y tasa preferencial.</li>
 * </ul>
 *
 * <p>Reglas de negocio (se validan en {@link Builder#build()} antes de crear
 * el objeto, de modo que nunca existe una solicitud inválida):</p>
 * <ol>
 *   <li>El documento solo tiene dígitos (6 a 12).</li>
 *   <li>El monto está entre {@link Builder#MONTO_MINIMO} y {@link Builder#MONTO_MAXIMO}.</li>
 *   <li>El plazo está entre 6 y 120 meses.</li>
 *   <li>Un monto mayor que {@link Builder#UMBRAL_RESPALDO} exige codeudor o garantía.</li>
 *   <li>La tasa preferencial solo aplica si hay garantía.</li>
 * </ol>
 */
public final class SolicitudCredito {

    private final String solicitante;
    private final String documento;
    private final BigDecimal monto;
    private final int plazoMeses;
    private final String codeudor;
    private final String garantia;
    private final String descripcion;
    private final boolean tasaPreferencial;

    private SolicitudCredito(Builder b) {
        this.solicitante = b.solicitante;
        this.documento = b.documento;
        this.monto = b.monto;
        this.plazoMeses = b.plazoMeses;
        this.codeudor = b.codeudor;
        this.garantia = b.garantia;
        this.descripcion = b.descripcion;
        this.tasaPreferencial = b.tasaPreferencial;
    }

    /**
     * Inicia la construcción. Los obligatorios se piden aquí para que no se
     * puedan olvidar.
     */
    public static Builder builder(String solicitante, String documento, BigDecimal monto, int plazoMeses) {
        return new Builder(solicitante, documento, monto, plazoMeses);
    }

    public String getSolicitante() { return solicitante; }
    public String getDocumento() { return documento; }
    public BigDecimal getMonto() { return monto; }
    public int getPlazoMeses() { return plazoMeses; }
    public Optional<String> getCodeudor() { return Optional.ofNullable(codeudor); }
    public Optional<String> getGarantia() { return Optional.ofNullable(garantia); }
    public Optional<String> getDescripcion() { return Optional.ofNullable(descripcion); }
    public boolean isTasaPreferencial() { return tasaPreferencial; }

    /** Indica si la solicitud tiene algún respaldo (codeudor o garantía). */
    public boolean tieneRespaldo() {
        return codeudor != null || garantia != null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SolicitudCredito)) return false;
        SolicitudCredito s = (SolicitudCredito) o;
        return plazoMeses == s.plazoMeses && tasaPreferencial == s.tasaPreferencial
                && solicitante.equals(s.solicitante) && documento.equals(s.documento)
                && monto.compareTo(s.monto) == 0 && Objects.equals(codeudor, s.codeudor)
                && Objects.equals(garantia, s.garantia) && Objects.equals(descripcion, s.descripcion);
    }

    @Override
    public int hashCode() {
        return Objects.hash(solicitante, documento, monto.stripTrailingZeros(), plazoMeses,
                codeudor, garantia, descripcion, tasaPreferencial);
    }

    @Override
    public String toString() {
        return "SolicitudCredito{solicitante=" + solicitante + ", documento=" + documento
                + ", monto=" + monto.toPlainString() + ", plazo=" + plazoMeses + " meses"
                + (codeudor != null ? ", codeudor=" + codeudor : "")
                + (garantia != null ? ", garantia=" + garantia : "")
                + (tasaPreferencial ? ", tasa preferencial" : "") + "}";
    }

    /** Builder de {@link SolicitudCredito}. No es reutilizable entre hilos. */
    public static final class Builder {

        public static final BigDecimal MONTO_MINIMO = new BigDecimal("500000");
        public static final BigDecimal MONTO_MAXIMO = new BigDecimal("500000000");
        public static final BigDecimal UMBRAL_RESPALDO = new BigDecimal("50000000");
        public static final int PLAZO_MINIMO = 6;
        public static final int PLAZO_MAXIMO = 120;

        private final String solicitante;
        private final String documento;
        private final BigDecimal monto;
        private final int plazoMeses;
        private String codeudor;
        private String garantia;
        private String descripcion;
        private boolean tasaPreferencial;

        private Builder(String solicitante, String documento, BigDecimal monto, int plazoMeses) {
            this.solicitante = limpiar(Objects.requireNonNull(solicitante, "El solicitante es obligatorio"));
            this.documento = limpiar(Objects.requireNonNull(documento, "El documento es obligatorio"));
            this.monto = Objects.requireNonNull(monto, "El monto es obligatorio");
            this.plazoMeses = plazoMeses;
        }

        public Builder codeudor(String codeudor) {
            this.codeudor = limpiar(codeudor);
            return this;
        }

        public Builder garantia(String garantia) {
            this.garantia = limpiar(garantia);
            return this;
        }

        public Builder descripcion(String descripcion) {
            this.descripcion = limpiar(descripcion);
            return this;
        }

        public Builder tasaPreferencial(boolean tasaPreferencial) {
            this.tasaPreferencial = tasaPreferencial;
            return this;
        }

        /**
         * Valida todas las reglas y crea la solicitud.
         *
         * @throws IllegalStateException con un mensaje claro si alguna regla no se cumple
         */
        public SolicitudCredito build() {
            if (solicitante == null) {
                throw new IllegalStateException("El solicitante es obligatorio");
            }
            if (documento == null || !documento.matches("\\d{6,12}")) {
                throw new IllegalStateException("El documento debe tener entre 6 y 12 dígitos");
            }
            if (monto.compareTo(MONTO_MINIMO) < 0 || monto.compareTo(MONTO_MAXIMO) > 0) {
                throw new IllegalStateException("El monto debe estar entre " + MONTO_MINIMO.toPlainString()
                        + " y " + MONTO_MAXIMO.toPlainString());
            }
            if (plazoMeses < PLAZO_MINIMO || plazoMeses > PLAZO_MAXIMO) {
                throw new IllegalStateException("El plazo debe estar entre " + PLAZO_MINIMO
                        + " y " + PLAZO_MAXIMO + " meses");
            }
            if (monto.compareTo(UMBRAL_RESPALDO) > 0 && codeudor == null && garantia == null) {
                throw new IllegalStateException("Montos mayores a " + UMBRAL_RESPALDO.toPlainString()
                        + " requieren codeudor o garantía");
            }
            if (tasaPreferencial && garantia == null) {
                throw new IllegalStateException("La tasa preferencial solo aplica con garantía");
            }
            return new SolicitudCredito(this);
        }

        /** Convierte cadenas vacías en null y quita espacios sobrantes. */
        private static String limpiar(String valor) {
            return (valor == null || valor.isBlank()) ? null : valor.strip();
        }
    }
}
