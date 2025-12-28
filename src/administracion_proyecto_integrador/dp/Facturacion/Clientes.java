
package administracion_proyecto_integrador.dp.Facturacion;


import java.util.List;
import java.util.ArrayList;

import administracion_proyecto_integrador.md.Facturacion.ClienteMD;


public class Clientes {
    
    // Atributos de la clase Clientes
    
    private String idCliente;
    private String cliNombre;
    private String cliRucCed;
    private String cliTelefono;
    private String cliMail;
    private String cliCelular;
    private String cliDireccion;
    private String estadoCli;
    private String idCiudad;
    
    // Constuctores
    
    // Constructor Vacio
    public Clientes () {}
    
    // Constructor Completo

    public Clientes(String idCliente, String cliNombre, String cliRucCed, String cliTelefono, String cliMail, String cliCelular, String cliDireccion, String estadoCli, String idCiudad) {
        this.idCliente = idCliente;
        this.cliNombre = cliNombre;
        this.cliRucCed = cliRucCed;
        this.cliTelefono = cliTelefono;
        this.cliMail = cliMail;
        this.cliCelular = cliCelular;
        this.cliDireccion = cliDireccion;
        this.estadoCli = estadoCli;
        this.idCiudad = idCiudad;
    }
    
    
    // Getters

    public String getIdCliente() {
        return idCliente;
    }

    public String getCliNombre() {
        return cliNombre;
    }

    public String getCliRucCed() {
        return cliRucCed;
    }

    public String getCliTelefono() {
        return cliTelefono;
    }

    public String getCliMail() {
        return cliMail;
    }

    public String getCliCelular() {
        return cliCelular;
    }

    public String getCliDireccion() {
        return cliDireccion;
    }

    public String getEstadoCli() {
        return estadoCli;
    }

    public String getIdCiudad() {
        return idCiudad;
    }
    
    
    // Setters

    public void setIdCliente(String idCliente) {
        this.idCliente = idCliente;
    }

    public void setCliNombre(String cliNombre) {
        this.cliNombre = cliNombre;
    }

    public void setCliRucCed(String cliRucCed) {
        this.cliRucCed = cliRucCed;
    }

    public void setCliTelefono(String cliTelefono) {
        this.cliTelefono = cliTelefono;
    }

    public void setCliMail(String cliMail) {
        this.cliMail = cliMail;
    }

    public void setCliCelular(String cliCelular) {
        this.cliCelular = cliCelular;
    }

    public void setCliDireccion(String cliDireccion) {
        this.cliDireccion = cliDireccion;
    }

    public void setEstadoCli(String estadoCli) {
        this.estadoCli = estadoCli;
    }

    public void setIdCiudad(String idCiudad) {
        this.idCiudad = idCiudad;
    }
    
    
    // Métodos de Conexión con DB
    
    /**
     * RF6.4.1: Consulta General de Clientes
     * Obtiene el listado completo de clientes desde el MD
     */
    
    public static List<Clientes> obtenerClientes() throws Exception {
        return ClienteMD.obtenerListadoClientes();
    }
    
    // PARA COMBOBOX
    
    public static List<Clientes> obtenerClientesActivos() throws Exception {
        return ClienteMD.obtenerListadoNombresClientes();
    }
    
    /**
     * RF6.1: Creación Cliente
     * Recibe un objeto Cliente con los datos cargados desde la GUI
     * y lo envia a la capa MD para que se inserte en la BD.
     */
    
    public static boolean grabarCliente(Clientes cliente) {
        ClienteMD clienteMD = new ClienteMD();
        return clienteMD.crearCliente(cliente);
    }
    
    /**
     * RF6.2: Modificación Cliente
     */
    
    public static boolean modificarCliente(Clientes cliente) {
        ClienteMD clienteMD = new ClienteMD();
        return clienteMD.modificarCliente(cliente);
    }
    
    /**
     * RF6.3: Inhabilitación Cliente
     */
    
    public static boolean eliminarCliente (String idCliente) {
        ClienteMD clienteMD = new ClienteMD();
        return clienteMD.eliminarCliente(idCliente);
    }

    /**
     * RF6.4.2: Consulta Por Parámetros de Cliente
     */
    
    public static List<Clientes> obtenerClientesPorParametro (
            String cliRucCed,
            String cliNombre,
            String cliCelular,
            String cliTelefono) {
        ClienteMD clienteMD = new ClienteMD();
        return clienteMD.obtenerClientesPorParametro(cliRucCed, cliNombre, cliCelular, cliTelefono);
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
     * Verifica los datos del cliente antes de guardarlo en BD.
     * Retorna lista de errores (vacía si todo está bien).
     */
    public List<ErrorValidacion> verificarCli() {
        List<ErrorValidacion> errores = new ArrayList<>();

        
        final int MAX_ID_CLIENTE  = 7;
        final int MAX_NOMBRE      = 40;
        final int MAX_RUC_CED     = 13;   
        final int MAX_TELEFONO    = 10;   
        final int MAX_MAIL        = 60;
        final int MAX_CELULAR     = 10;
        final int MAX_DIRECCION   = 60;
        final int MAX_ESTADO      = 3;    
        final int MAX_ID_CIUDAD   = 3;

        // Normaliza / Hacer trim 
        String idCliente = norm(this.idCliente);
        String nombre    = norm(this.cliNombre);
        String rucCed    = norm(this.cliRucCed);
        String telefono  = norm(this.cliTelefono);
        String mail      = norm(this.cliMail);
        String celular   = norm(this.cliCelular);
        String direccion = norm(this.cliDireccion);
        String estado    = norm(this.estadoCli);
        String idCiudad  = norm(this.idCiudad);

        // -------- V1: Obligatorios --------
        if (isBlank(nombre))   errores.add(err("V1", msgObligatorio("cliente")));
        if (isBlank(rucCed))   errores.add(err("V1", msgObligatorio("ruc/cedula")));
        if (isBlank(telefono)) errores.add(err("V1", msgObligatorio("telefono")));
        if (isBlank(mail))     errores.add(err("V1", msgObligatorio("correo")));
        if (isBlank(estado))   errores.add(err("V1", msgObligatorio("estado")));
        if (isBlank(idCiudad)) errores.add(err("V1", msgObligatorio("ciudad")));

        // Validación de Dirección 
        if (isBlank(direccion)) {
            errores.add(err("V16", "Complete los campos obligatorios de la dirección."));
        }

        // -------- V2: Longitud máxima --------
        if (!isBlank(idCliente) && idCliente.length() > MAX_ID_CLIENTE) errores.add(err("V2", msgLongitud("idCliente")));
        if (!isBlank(nombre)    && nombre.length()    > MAX_NOMBRE)     errores.add(err("V2", msgLongitud("cliente")));
        if (!isBlank(rucCed)    && rucCed.length()    > MAX_RUC_CED)    errores.add(err("V2", msgLongitud("ruc/cedula")));
        if (!isBlank(telefono)  && telefono.length()  > MAX_TELEFONO)   errores.add(err("V2", msgLongitud("telefono")));
        if (!isBlank(mail)      && mail.length()      > MAX_MAIL)       errores.add(err("V2", msgLongitud("correo")));
        if (!isBlank(celular)   && celular.length()   > MAX_CELULAR)    errores.add(err("V2", msgLongitud("celular")));
        if (!isBlank(direccion) && direccion.length() > MAX_DIRECCION)  errores.add(err("V2", msgLongitud("direccion")));
        if (!isBlank(estado)    && estado.length()    > MAX_ESTADO)     errores.add(err("V2", msgLongitud("estado")));
        if (!isBlank(idCiudad)  && idCiudad.length()  > MAX_ID_CIUDAD)  errores.add(err("V2", msgLongitud("ciudad")));

        // -------- V4: Solo letras (nombre/cliente) --------
        if (!isBlank(nombre) && !soloLetrasYEspacios(nombre)) {
            errores.add(err("V4", "El campo cliente solo debe contener letras."));
        }

        // -------- V5: Solo números (teléfono / celular / rucCed) --------
        if (!isBlank(telefono) && !soloNumeros(telefono)) errores.add(err("V5", "El campo teléfono solo debe contener números."));
        if (!isBlank(celular)  && !soloNumeros(celular))  errores.add(err("V5", "El campo celular solo debe contener números."));
        if (!isBlank(rucCed)   && !soloNumeros(rucCed))   errores.add(err("V5", "El campo ruc/cedula solo debe contener números."));

        // -------- V6: Longitud inválida (10 dígitos) --------
        if (!isBlank(telefono) && soloNumeros(telefono) && telefono.length() != 10) {
            errores.add(err("V6", "El teléfono debe tener 10 dígitos."));
        }
        if (!isBlank(celular) && soloNumeros(celular) && celular.length() != 10) {
            errores.add(err("V6", "El celular debe tener 10 dígitos."));
        }

        // -------- V14: Correo inválido --------
        if (!isBlank(mail) && !correoValido(mail)) {
            errores.add(err("V14", "El correo ingresado no tiene un formato válido."));
        }

        // -------- V12: Estado inválido (ACT/INA) --------
        if (!isBlank(estado) && !(estado.equalsIgnoreCase("ACT") || estado.equalsIgnoreCase("INA"))) {
            errores.add(err("V12", "Estado inválido para operación. Use ACT o INA."));
        }

        return errores;
    }

    // HELPERS PARA VALIDACION

    private static ErrorValidacion err(String codigo, String mensaje) {
        return new ErrorValidacion(codigo, mensaje);
    }

    private static String msgObligatorio(String campo) {
        return "El campo " + campo + " es obligatorio.";
    }

    private static String msgLongitud(String campo) {
        return "El campo " + campo + " excede la longitud permitida.";
    }

    private static String norm(String s) {
        return (s == null) ? null : s.trim();
    }

    private static boolean isBlank(String s) {
        return s == null || s.trim().isEmpty();
    }

    private static boolean soloNumeros(String s) {
        return s != null && s.matches("\\d+");
    }

    private static boolean soloLetrasYEspacios(String s) {
        return s != null && s.matches("^[\\p{L} ]+$");
    }

    private static boolean correoValido(String correo) {
        return correo != null && correo.matches("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
    }
    
    
}
