
package administracion_proyecto_integrador.dp.Facturacion;

import java.util.List;
import java.util.ArrayList;
import java.time.LocalDate;

import administracion_proyecto_integrador.md.Facturacion.FacturasMD;
import administracion_proyecto_integrador.md.Facturacion.ClienteMD;
import administracion_proyecto_integrador.dp.Facturacion.Clientes;

public class Facturas {
    
    // Atributos de la clase Facturas
    
    private String idFactura;
    private String idCliente;
    private double facSubtotal;
    private double facIva;
    private String estadoFac;
    private String facDescripcion;
    private LocalDate facFechaHora;
    private LocalDate facFechaPago;
    private double facTotal;
    
    
    // CONSTRUCTORES
    
    // CONSTRUCTOR VACIO
    public Facturas (){}
    
    // CONSTRUCTOR COMPLETO

    public Facturas(String idFactura, String idCliente, double facSubtotal, double facIva, String estadoFac, String facDescripcion, LocalDate facFechaHora, LocalDate facFechaPago, double facTotal) {
        this.idFactura = idFactura;
        this.idCliente = idCliente;
        this.facSubtotal = facSubtotal;
        this.facIva = facIva;
        this.estadoFac = estadoFac;
        this.facDescripcion = facDescripcion;
        this.facFechaHora = facFechaHora;
        this.facFechaPago = facFechaPago;
        this.facTotal = facTotal;
    }
    
    // GETTERS

    public String getIdFactura() {
        return idFactura;
    }

    public String getIdCliente() {
        return idCliente;
    }

    public double getFacSubtotal() {
        return facSubtotal;
    }

    public double getFacIva() {
        return facIva;
    }

    public String getEstadoFac() {
        return estadoFac;
    }

    public String getFacDescripcion() {
        return facDescripcion;
    }

    public LocalDate getFacFechaHora() {
        return facFechaHora;
    }

    public LocalDate getFacFechaPago() {
        return facFechaPago;
    }

    public double getFacTotal() {
        return facTotal;
    }
    
    // SETTERS

    public void setIdFactura(String idFactura) {
        this.idFactura = idFactura;
    }

    public void setIdCliente(String idCliente) {
        this.idCliente = idCliente;
    }

    public void setFacSubtotal(double facSubtotal) {
        this.facSubtotal = facSubtotal;
    }

    public void setFacIva(double facIva) {
        this.facIva = facIva;
    }

    public void setEstadoFac(String estadoFac) {
        this.estadoFac = estadoFac;
    }

    public void setFacDescripcion(String facDescripcion) {
        this.facDescripcion = facDescripcion;
    }

    public void setFacFechaHora(LocalDate facFechaHora) {
        this.facFechaHora = facFechaHora;
    }

    public void setFacFechaPago(LocalDate facFechaPago) {
        this.facFechaPago = facFechaPago;
    }

    public void setFacTotal(double facTotal) {
        this.facTotal = facTotal;
    }
    
    // METODOS DE CONEXION A BD
    
    /**
     * RF5.4.1: Consulta General de Facturas
     * Obtiene el listado completo de peliculas desde MD
     */
    
    public static List<Facturas> obtenerFacturas() throws Exception{
        return FacturasMD.obtenerListadoFacturas();
    }
    
    /**
     * 
     * METODO PARA GUI PARA OBTENER LOS DATOS DE UNA FACTURA EN ESPECIFICO (CABECERA)
     */

    public static Facturas obtenerFacturaPorId(String idFactura) throws Exception {
        return FacturasMD.obtenerFacturaPorId(idFactura);
    }
    
    
    /**
     * RF5.1: Grabar Factura
     * Recibe un objeto Factura con los datos cargados desde la GUI
     * y lo envia a la capa MD para que se inserte en la BD
     */
    
    public static boolean grabarFactura(Facturas factura) {
        FacturasMD facturaMD = new FacturasMD();
        return facturaMD.crearFactura(factura);
    }
    
    /**
     * RF5.2: Modificar Factura
     */
    
    public static boolean modificarFactura(Facturas factura) {
        FacturasMD facturaMD = new FacturasMD();
        return facturaMD.modificarFactura(factura);
    }
    
    /**
     * RF5.3: Inhabilitación Facturas
     */
    
    public static boolean eliminarFactura (String idFactura) {
        FacturasMD facturaMD = new FacturasMD();
        return facturaMD.eliminarFactura(idFactura);
    }
    
    /**
     * RF5.4.2: Consulta Por Parametros de Factura
     */
    
    public static List<Facturas> obtenerFacturasPorParametro (
            String idFactura,
            String idCliente,
            String facDescripcion) {
        FacturasMD facturaMD = new FacturasMD();
        return facturaMD.obtenerFacturasPorParametro(idFactura, idCliente, facDescripcion);
    }
    
    
    /**
     * METODO PARA COMBOBO DE FACTURACION
     */
    
    public static List<Clientes> obtenerClientesActivos () throws Exception {
        return ClienteMD.obtenerListadoClientes(); 
    }
    
    /**
     * Obtiene el siguiente ID de factura disponible
     */
    public static String obtenerSiguienteIdFactura() {
        return FacturasMD.generarSiguienteIdFactura();
    }
    
    // VALIDACIONES

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
     * Verifica los datos de la factura antes de guardarla en BD.
     * Retorna lista de errores (vacía si todo está bien).
     */
    public List<ErrorValidacion> verificarFac() {
        List<ErrorValidacion> errores = new ArrayList<>();

        
        final int MAX_ID_FACTURA   = 7;    
        final int MAX_ID_CLIENTE   = 7;    
        final int MAX_ESTADO       = 3;    
        final int MAX_DESCRIPCION  = 100;   

        // Normalización
        String idFactura   = norm(this.idFactura);
        String idCliente   = norm(this.idCliente);
        String estado      = norm(this.estadoFac);
        String descripcion = norm(this.facDescripcion);

        LocalDate fechaHora = this.facFechaHora;
        LocalDate fechaPago = this.facFechaPago;

        // ---------------- V1: Campo obligatorio vacío ----------------

        if (isBlank(idFactura)) 
            errores.add(err("V1", msgObligatorio("idFactura")));
        
        if (isBlank(idCliente))
            errores.add(err("V1", msgObligatorio("cliente")));

        if (isBlank(descripcion))
            errores.add(err("V1", msgObligatorio("descripcion")));

        if (isBlank(estado))
            errores.add(err("V1", msgObligatorio("estado")));

        if (fechaHora == null)
            errores.add(err("V1", msgObligatorio("fecha")));

        // ---------------- V2: Longitud máxima excedida ----------------
        if (!isBlank(idFactura) && idFactura.length() != MAX_ID_FACTURA)
            errores.add(err("V2", msgLongitud("idFactura")));

        if (!isBlank(idCliente) && idCliente.length() != MAX_ID_CLIENTE)
            errores.add(err("V2", msgLongitud("idCliente")));

        if (!isBlank(estado) && estado.length() > MAX_ESTADO)
            errores.add(err("V2", msgLongitud("estado")));

        if (!isBlank(descripcion) && descripcion.length() > MAX_DESCRIPCION)
            errores.add(err("V2", msgLongitud("descripcion")));

        // ---------------- V7: Fecha inválida ----------------
        if (fechaHora != null && fechaPago != null && fechaPago.isBefore(fechaHora)) {
            errores.add(err("V7", "La fecha ingresada no es válida."));
        }
        // ---------------- V10: Precio/total menor o igual a cero ----------------
        if (this.facTotal <= 0) {
            errores.add(err("V10", "El precio debe ser mayor a 0."));
        }

        // ---------------- V12: Estado inválido para operación (ACT/INA) ----------------
        if (!isBlank(estado) && !(estado.equalsIgnoreCase("ACT") || estado.equalsIgnoreCase("INA"))) {
            errores.add(err("V12", "No se puede realizar la acción: el registro está inactivo."));
        }
        return errores;
    }

    // ===================== HELPERS =====================

    private static ErrorValidacion err(String codigo, String mensaje) {
        return new ErrorValidacion(codigo, mensaje);
    }

    private static String msgObligatorio(String nombreCampo) {
        return "El campo " + nombreCampo + " es obligatorio.";
    }

    private static String msgLongitud(String nombreCampo) {
        return "El campo " + nombreCampo + " no tiene la longitud permitida.";
    }

    private static String norm(String s) {
        return (s == null) ? null : s.trim();
    }

    private static boolean isBlank(String s) {
        return s == null || s.trim().isEmpty();
    }


}
