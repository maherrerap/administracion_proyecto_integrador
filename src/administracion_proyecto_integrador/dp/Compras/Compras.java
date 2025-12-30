package administracion_proyecto_integrador.dp.Compras;

import java.util.List;
import java.util.ArrayList;
import java.time.LocalDate;

import administracion_proyecto_integrador.md.Compras.ComprasMD;

public class Compras {

    // ===================== ATRIBUTOS =====================

    private String idCompra;
    private String idProveedor;
    private double ocSubtotal;
    private double ocIva;
    private String estadoOc;
    private LocalDate ocFechaHora;
    private LocalDate ocFechaVenc;
    private LocalDate ocFechaPronto;
    private double ocPorDescPronto;
    private double ocSaldo;
    private double ocTotal;

    // ===================== CONSTRUCTORES =====================

    public Compras() {}

    public Compras(String idCompra, String idProveedor, double ocSubtotal, double ocIva,
                   String estadoOc, LocalDate ocFechaHora, LocalDate ocFechaVenc,
                   LocalDate ocFechaPronto, double ocPorDescPronto,
                   double ocSaldo, double ocTotal) {

        this.idCompra = idCompra;
        this.idProveedor = idProveedor;
        this.ocSubtotal = ocSubtotal;
        this.ocIva = ocIva;
        this.estadoOc = estadoOc;
        this.ocFechaHora = ocFechaHora;
        this.ocFechaVenc = ocFechaVenc;
        this.ocFechaPronto = ocFechaPronto;
        this.ocPorDescPronto = ocPorDescPronto;
        this.ocSaldo = ocSaldo;
        this.ocTotal = ocTotal;
    }

    // ===================== GETTERS =====================

    public String getIdCompra() { return idCompra; }
    public String getIdProveedor() { return idProveedor; }
    public double getOcSubtotal() { return ocSubtotal; }
    public double getOcIva() { return ocIva; }
    public String getEstadoOc() { return estadoOc; }
    public LocalDate getOcFechaHora() { return ocFechaHora; }
    public LocalDate getOcFechaVenc() { return ocFechaVenc; }
    public LocalDate getOcFechaPronto() { return ocFechaPronto; }
    public double getOcPorDescPronto() { return ocPorDescPronto; }
    public double getOcSaldo() { return ocSaldo; }
    public double getOcTotal() { return ocTotal; }

    // ===================== SETTERS =====================

    public void setIdCompra(String idCompra) { this.idCompra = idCompra; }
    public void setIdProveedor(String idProveedor) { this.idProveedor = idProveedor; }
    public void setOcSubtotal(double ocSubtotal) { this.ocSubtotal = ocSubtotal; }
    public void setOcIva(double ocIva) { this.ocIva = ocIva; }
    public void setEstadoOc(String estadoOc) { this.estadoOc = estadoOc; }
    public void setOcFechaHora(LocalDate ocFechaHora) { this.ocFechaHora = ocFechaHora; }
    public void setOcFechaVenc(LocalDate ocFechaVenc) { this.ocFechaVenc = ocFechaVenc; }
    public void setOcFechaPronto(LocalDate ocFechaPronto) { this.ocFechaPronto = ocFechaPronto; }
    public void setOcPorDescPronto(double ocPorDescPronto) { this.ocPorDescPronto = ocPorDescPronto; }
    public void setOcSaldo(double ocSaldo) { this.ocSaldo = ocSaldo; }
    public void setOcTotal(double ocTotal) { this.ocTotal = ocTotal; }

    // ===================== MÉTODOS DP =====================

    /** RF2.4.1: Consulta General */
    public static List<Compras> obtenerCompras() throws Exception {
        return ComprasMD.obtenerListadoCompras();
    }

    /** RF2.1: Registrar Orden de Compra */
    public static boolean grabarCompra(Compras compra) {
        ComprasMD md = new ComprasMD();
        return md.crearCompra(compra);
    }

    /** RF2.2: Modificar Orden de Compra */
    public static boolean modificarCompra(Compras compra) {
        ComprasMD md = new ComprasMD();
        return md.modificarCompra(compra);
    }

    /** RF2.3: Inhabilitar Orden de Compra */
    public static boolean eliminarCompra(String idCompra) {
        ComprasMD md = new ComprasMD();
        return md.eliminarCompra(idCompra);
    }

    /** RF2.4.2: Consulta por Parámetros */
    public static List<Compras> obtenerComprasPorParametro(
            String idCompra,
            String idProveedor,
            String estadoOc) {

        ComprasMD md = new ComprasMD();
        return md.obtenerComprasPorParametro(idCompra, idProveedor, estadoOc);
    }

    /**
     * Obtiene el siguiente ID de compra disponible
     * MISMO patrón que Facturas
     */
    public static String obtenerSiguienteIdCompra() {
        return ComprasMD.generarSiguienteIdCompra();
    }

    // ===================== VALIDACIONES =====================

    /** Representa un error de validación (Código + Mensaje) */
    public static class ErrorValidacion {
        private final String codigo;
        private final String mensaje;

        public ErrorValidacion(String codigo, String mensaje) {
            this.codigo = codigo;
            this.mensaje = mensaje;
        }

        public String getCodigo() { return codigo; }
        public String getMensaje() { return mensaje; }

        @Override
        public String toString() {
            return codigo + " - " + mensaje;
        }
    }

    /**
     * Verifica los datos de la orden de compra antes de guardarla en BD.
     */
    public List<ErrorValidacion> verificarOc() {
        List<ErrorValidacion> errores = new ArrayList<>();

        final int MAX_ID_COMPRA    = 7;
        final int MAX_ID_PROV      = 7;
        final int MAX_ESTADO       = 3;

        String idCompra   = norm(this.idCompra);
        String idProveedor = norm(this.idProveedor);
        String estado     = norm(this.estadoOc);

        // V1: obligatorios
        if (isBlank(idCompra))
            errores.add(err("V1", msgObligatorio("idCompra")));

        if (isBlank(idProveedor))
            errores.add(err("V1", msgObligatorio("proveedor")));

        if (isBlank(estado))
            errores.add(err("V1", msgObligatorio("estado")));

        // V2: longitud
        if (!isBlank(idCompra) && idCompra.length() != MAX_ID_COMPRA)
            errores.add(err("V2", msgLongitud("idCompra")));

        if (!isBlank(idProveedor) && idProveedor.length() != MAX_ID_PROV)
            errores.add(err("V2", msgLongitud("proveedor")));

        if (!isBlank(estado) && estado.length() > MAX_ESTADO)
            errores.add(err("V2", msgLongitud("estado")));

        // V7: fechas
        if (this.ocFechaHora != null && this.ocFechaVenc != null
                && this.ocFechaVenc.isBefore(this.ocFechaHora)) {
            errores.add(err("V7", "La fecha de vencimiento no es válida."));
        }

        return errores;
    }

    // ===================== HELPERS =====================

    private static ErrorValidacion err(String codigo, String mensaje) {
        return new ErrorValidacion(codigo, mensaje);
    }

    private static String msgObligatorio(String campo) {
        return "El campo " + campo + " es obligatorio.";
    }

    private static String msgLongitud(String campo) {
        return "El campo " + campo + " no tiene la longitud permitida.";
    }

    private static String norm(String s) {
        return (s == null) ? null : s.trim();
    }

    private static boolean isBlank(String s) {
        return s == null || s.trim().isEmpty();
    }
}