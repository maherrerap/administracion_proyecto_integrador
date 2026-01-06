package administracion_proyecto_integrador.dp.Compras;

import java.util.List;
import java.util.ArrayList;
import java.time.LocalDate;

import administracion_proyecto_integrador.md.Compras.ComprasMD;

public class Compras {

    // Atributos de la clase Compras
    
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

    // CONSTRUCTORES
    
    // Constructor Vacio
    public Compras() {}
    
    // Constructor Completo
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

    // GETTERS
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

    // SETTERS
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

    // Métodos de Conexión con DB
    
    /**
     *  RF2.4.1: Consulta General de Órdenes de Compra
     */
    
    public static List<Compras> obtenerCompras() {
        return ComprasMD.obtenerListadoCompras();
    }

    /**
     * Obtener una orden de compra específica por su ID
     */
    public static Compras obtenerOrdenCompraPorId(String idCompra) {
        return ComprasMD.obtenerCompraPorId(idCompra);
    }

    /**
     * RF2.1: Registrar una Orden de Compra
     */
    public static boolean grabarCompra(Compras compra) {
        return ComprasMD.crearCompra(compra);
    }

    /**
     * RF2.2: Modificación de Orden de Compra
     */
    public static boolean modificarCompra(Compras compra) {
        return ComprasMD.modificarCompra(compra);
    }

    /**
     * RF2.3: Inhabilitación de Orden de Compra
     */
    public static boolean eliminarOrdenCompra(String idCompra) {
        return ComprasMD.inhabilitarOrdenCompra(idCompra);
    }
    
    public static boolean inhabilitarOrdenCompraCompleta(String idCompra) {
        return ComprasMD.inhabilitarOrdenCompraCompleta(idCompra);
    }

    /**
     * RF2.4.2: Consulta por Parámetro de Órdenes de Compra
     */
    public static List<Compras> obtenerOrdenCompraPorParametro(
            String idCompra,
            String idProveedor,
            String fechaEmision) {

        return ComprasMD.obtenerOrdenCompraPorParametros(
            idCompra,
            idProveedor,
            fechaEmision
        );
    }

    // ===================== MÉTODOS PARA LA CREACIÓN DE ORDENES DE COMRPA =====================
    
    // GENERAR ID AUTOMÁTICO
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
     * Verifica los datos de la orden de compras antes de guardarlo/modificarlo en BD.
     * Retorna lista de errores (vacía si todo está bien).
     */
    public List<ErrorValidacion> verificarOc() {
        List<ErrorValidacion> errores = new ArrayList<>();

        final int MAX_ID_COMPRA = 7;
        final int MAX_ID_PROVEEDOR = 7;
        final int MAX_ESTADO = 3;

        // Normalización (trim)
        String idCompra = norm(this.idCompra);
        String idProveedor = norm(this.idProveedor);
        String estado = norm(this.estadoOc);
        
        // ---------------- V1: Campo obligatorio vacío ----------------
        if (isBlank(idCompra))
            errores.add(err("V1", "El campo idCompra es obligatorio."));

        if (isBlank(idProveedor))
            errores.add(err("V1", "El campo proveedor es obligatorio."));

        if (isBlank(estado))
            errores.add(err("V1", "El campo estado es obligatorio."));

        // ---------------- V2: Longitud máxima excedida ----------------
        if (!isBlank(idCompra) && idCompra.length() != MAX_ID_COMPRA)
            errores.add(err("V2", "El campo idCompra no tiene la longitud permitida."));

        if (!isBlank(idProveedor) && idProveedor.length() != MAX_ID_PROVEEDOR)
            errores.add(err("V2", "El campo idProveedor no tiene la longitud permitida."));
        
        // ---------------- V7: Formato no Válido de Fecha ----------------
        if (this.ocFechaHora != null && this.ocFechaVenc != null &&
            this.ocFechaVenc.isBefore(this.ocFechaHora)) {
            errores.add(err("V7", "La fecha ingresada no es válida."));
        }
        
        // ---------------- V10: Precio menor o igual a cero ----------------
        if (this.ocTotal <= 0) {
            errores.add(err("V10", "El total debe ser mayor a 0."));
        }
        
        return errores;
    }
    
    // ===================== HELPERS =====================
    private static ErrorValidacion err(String codigo, String mensaje) {
        return new ErrorValidacion(codigo, mensaje);
    }

    private static String norm(String s) {
        return (s == null) ? null : s.trim();
    }

    private static boolean isBlank(String s) {
        return s == null || s.trim().isEmpty();
    }
}