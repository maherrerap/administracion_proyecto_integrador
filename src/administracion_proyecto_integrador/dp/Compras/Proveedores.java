package administracion_proyecto_integrador.dp.Compras;

import java.util.List;
import java.util.ArrayList;

import administracion_proyecto_integrador.md.Compras.ProveedoresMD;
import javax.swing.JComboBox;

public class Proveedores {
    
    // Atributos de la clase Proveedores
    
    private String idProveedor;
    private String prvNombre;
    private String prvRucCed;
    private String prvTelefono;
    private String prvMail;
    private String prvCelular;
    private String prvDireccion;
    private String estadoPrv;
    private String idCiudad;
    private String prvPais;
    
    // CONSTRUCTORES
    
    // Constructor Vacio
    public Proveedores() {}

    // Constructor Completo
    public Proveedores(String idProveedor, String prvNombre, String prvRucCed, String prvTelefono, String prvMail, String prvCelular, String prvDireccion, String estadoPrv, String idCiudad, String prvPais) {
        this.idProveedor = idProveedor;
        this.prvNombre = prvNombre;
        this.prvRucCed = prvRucCed;
        this.prvTelefono = prvTelefono;
        this.prvMail = prvMail;
        this.prvCelular = prvCelular;
        this.prvDireccion = prvDireccion;
        this.estadoPrv = estadoPrv;
        this.idCiudad = idCiudad;
        this.prvPais = prvPais;
    }

    // GETTERS
    public String getIdProveedor() {
        return idProveedor;
    }

    public String getPrvNombre() {
        return prvNombre;
    }

    public String getPrvRucCed() {
        return prvRucCed;
    }

    public String getPrvTelefono() {
        return prvTelefono;
    }

    public String getPrvMail() {
        return prvMail;
    }

    public String getPrvCelular() {
        return prvCelular;
    }

    public String getPrvDireccion() {
        return prvDireccion;
    }

    public String getEstadoPrv() {
        return estadoPrv;
    }

    public String getIdCiudad() {
        return idCiudad;
    }

    public String getPrvPais() {
        return prvPais;
    }
    
    // SETTERS
    public void setIdProveedor(String idProveedor) {
        this.idProveedor = idProveedor;
    }

    public void setPrvNombre(String prvNombre) {
        this.prvNombre = prvNombre;
    }

    public void setPrvRucCed(String prvRucCed) {
        this.prvRucCed = prvRucCed;
    }

    public void setPrvTelefono(String prvTelefono) {
        this.prvTelefono = prvTelefono;
    }

    public void setPrvMail(String prvMail) {
        this.prvMail = prvMail;
    }

    public void setPrvCelular(String prvCelular) {
        this.prvCelular = prvCelular;
    }

    public void setPrvDireccion(String prvDireccion) {
        this.prvDireccion = prvDireccion;
    }

    public void setEstadoPrv(String estadoPrv) {
        this.estadoPrv = estadoPrv;
    }

    public void setIdCiudad(String idCiudad) {
        this.idCiudad = idCiudad;
    }

    public void setPrvPais(String prvPais) {
        this.prvPais = prvPais;
    }
    
    // Métodos de Conexión con DB
    
    /**
     * RF1.4.1: Consulta General de Proveedores
     */
    public static List<Proveedores> obtenerProveedores() throws Exception {
        return ProveedoresMD.obtenerListadoProveedores();
    }
    
    /**
     * Obtener solamente los proveedores activos
     */
    public static List<Proveedores> obtenerProveedoresActivos() throws Exception {
        return ProveedoresMD.obtenerListadoNombresProveedores();
    }
    
    /**
     * Obtener el nombre del proveedor por su ID
     */
    public static String obtenerNombreProveedor(String idProveedor) {
        return ProveedoresMD.obtenerNombreProveedor(idProveedor);
    }
    
    /**
     * RF1.1: Creación de Proveedor
     */
    public static boolean grabarProveedor(Proveedores proveedor) {
        ProveedoresMD proveedorMD = new ProveedoresMD();
        return proveedorMD.crearProveedor(proveedor);
    }

    /**
     * RF1.2: Modificación de Proveedor
     */
    public static boolean modificarProveedor(Proveedores proveedor) {
        ProveedoresMD proveedorMD = new ProveedoresMD();
        return proveedorMD.modificarProveedor(proveedor);
    }
    
    /**
     * RF1.3: Inhabilitación de Proveedor
     */
    public static boolean eliminarProveedor(String idProveedor) {
        ProveedoresMD proveedorMD = new ProveedoresMD();
        return proveedorMD.eliminarProveedor(idProveedor);
    }
    
    /**
     * RF1.4.2: Consulta por Parámetro de Proveedor
     */
    public static List<Proveedores> obtenerProveedoresPorParametro(
            String prvRucCed,
            String prvNombre,
            String prvTelefono,
            String prvMail,
            String prvCelular,
            String prvPais) {
        ProveedoresMD proveedorMD = new ProveedoresMD();
        return proveedorMD.obtenerProveedoresPorParametro(prvRucCed, prvNombre, prvTelefono, prvMail, prvCelular, prvPais);
    } 
    // ===================== MÉTODOS AUXILIARES PARA GUI =====================

    // OBTENER LISTADO DE CIUDADES PARA COMBOBOX
    public static List<Proveedores.ComboItem> obtenerCiudades() {
        List<ProveedoresMD.ComboItem> ciudadesMD = ProveedoresMD.obtenerListadoCiudades();
        List<Proveedores.ComboItem> ciudadesDP = new ArrayList<>();

        for (ProveedoresMD.ComboItem c : ciudadesMD) {
            ciudadesDP.add(new Proveedores.ComboItem(c.getId(), c.getDescripcion()));
        }

        return ciudadesDP;
    }

    // CLASE AUXILIAR PARA COMBOBOX
    public static class ComboItem {
        private final String id;
        private final String descripcion;

        public ComboItem(String id, String descripcion) {
            this.id = id;
            this.descripcion = descripcion;
        }

        public String getId() {
            return id;
        }

        public String getDescripcion() {
            return descripcion;
        }

        @Override
        public String toString() {
            return descripcion;
        }
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
     * Verifica los datos del proveedor antes de guardarlo/modificarlo en BD.
     * Retorna lista de errores (vacía si todo está bien).
     */
    public List<ErrorValidacion> verificarPrv() {
        List<ErrorValidacion> errores = new ArrayList<>();

        final int MAX_ID_PROVEEDOR = 7;
        final int MAX_NOMBRE       = 100;
        final int MAX_RUC_CED      = 13;   
        final int MAX_TELEFONO     = 10;
        final int MAX_MAIL         = 60;
        final int MAX_CELULAR      = 10;
        final int MAX_DIRECCION    = 100;
        final int MAX_ESTADO       = 3;    
        final int MAX_ID_CIUDAD    = 3;
        final int MAX_PAIS         = 30;

        String idProveedor = norm(this.idProveedor);
        String nombre      = norm(this.prvNombre);
        String rucCed      = norm(this.prvRucCed);
        String telefono    = norm(this.prvTelefono);
        String mail        = norm(this.prvMail);
        String celular     = norm(this.prvCelular);
        String direccion   = norm(this.prvDireccion);
        String estado      = norm(this.estadoPrv);
        String idCiudad    = norm(this.idCiudad);
        String pais        = norm(this.prvPais);

        // ---------------- V1: Campo obligatorio vacío ----------------
        if (isBlank(nombre))   errores.add(err("V1", "El campo proveedor es obligatorio."));
        if (isBlank(rucCed))   errores.add(err("V1", "El campo ruc/cedula es obligatorio."));
        if (isBlank(telefono)) errores.add(err("V1", "El campo telefono es obligatorio."));
        if (isBlank(mail))     errores.add(err("V1", "El campo correo es obligatorio."));
        if (isBlank(estado))   errores.add(err("V1", "El campo estado es obligatorio."));
        if (isBlank(idCiudad)) errores.add(err("V1", "El campo ciudad es obligatorio."));
        if (isBlank(pais))     errores.add(err("V1", "El campo pais es obligatorio."));

        // ---------------- V16: Dirección Incompleta ----------------
        if (isBlank(direccion)) {
            errores.add(err("V16", "Complete los campos obligatorios de la direccion."));
        }
        
        // ---------------- V2: Longitud máxima excedida ----------------
        if (!isBlank(idProveedor) && idProveedor.length() > MAX_ID_PROVEEDOR) 
            errores.add(err("V2", "El campo idProveedor excede la longitud permitida."));
        if (!isBlank(nombre) && nombre.length() > MAX_NOMBRE) 
            errores.add(err("V2", "El campo proveedor excede la longitud permitida."));
        if (!isBlank(rucCed) && rucCed.length() > MAX_RUC_CED) 
            errores.add(err("V2", "El campo ruc/cedula excede la longitud permitida."));
        if (!isBlank(telefono) && telefono.length() > MAX_TELEFONO) 
            errores.add(err("V2", "El campo telefono excede la longitud permitida."));
        if (!isBlank(mail) && mail.length() > MAX_MAIL) 
            errores.add(err("V2", "El campo correo excede la longitud permitida."));
        if (!isBlank(celular) && celular.length() > MAX_CELULAR) 
            errores.add(err("V2", "El campo celular excede la longitud permitida."));
        if (!isBlank(direccion) && direccion.length() > MAX_DIRECCION) 
            errores.add(err("V2", "El campo direccion excede la longitud permitida."));
        if (!isBlank(estado) && estado.length() > MAX_ESTADO) 
            errores.add(err("V2", "El campo estado excede la longitud permitida."));
        if (!isBlank(idCiudad) && idCiudad.length() > MAX_ID_CIUDAD) 
            errores.add(err("V2", "El campo ciudad excede la longitud permitida."));
        if (!isBlank(pais) && pais.length() > MAX_PAIS) 
            errores.add(err("V2", "El campo pais excede la longitud permitida."));
        
        // ---------------- V4: Solo letras permitido ----------------
        if (!isBlank(nombre) && !nombreValido(nombre)) {
            errores.add(err("V4", "El campo proveedor contiene caracteres no permitidos."));
        }
        if (!isBlank(pais) && !soloLetrasYEspacios(pais)) {
            errores.add(err("V4", "El campo pais solo debe contener letras."));
        }
        
        // ---------------- V5: Solo números permitido ----------------
        if (!isBlank(telefono) && !soloNumeros(telefono)) 
            errores.add(err("V5", "El campo telefono solo debe contener numeros."));
        if (!isBlank(celular) && !soloNumeros(celular)) 
            errores.add(err("V5", "El campo celular solo debe contener numeros."));
        if (!isBlank(rucCed) && !soloNumeros(rucCed)) 
            errores.add(err("V5", "El campo ruc/cedula solo debe contener numeros."));
        
        // ---------------- VE: Exactitud de Dígitos ----------------
        if (!isBlank(telefono) && soloNumeros(telefono)) {
            if (telefono.length() != 9) {
                errores.add(err("VE", "El telefono debe tener exactamente 9 digitos."));
            }
        }
        
        if (!isBlank(celular) && soloNumeros(celular)) {
            if (celular.length() != 10) {
                errores.add(err("VE", "El celular debe tener exactamente 10 digitos."));
            } else if (!celular.startsWith("09")) {
                errores.add(err("VE", "El celular debe iniciar con 09."));
            }
        }
            
        if (!isBlank(rucCed) && soloNumeros(rucCed)) {
            int longitud = rucCed.length();
            if (longitud != 10 && longitud != 13) {
                errores.add(err("VE", "El RUC/Cedula debe tener exactamente 10 digitos (cedula) o 13 digitos (RUC)."));
            }
        }
        
        // ---------------- V14: Formato Inválido de Correo ----------------
        if (!isBlank(mail) && !correoValido(mail)) {
            errores.add(err("V14", "El correo ingresado no tiene un formato valido."));
        }
        
        // ---------------- V12: Estado inválido para operación (ACT/INA) ----------------
        if (!isBlank(estado) && !(estado.equalsIgnoreCase("ACT") || estado.equalsIgnoreCase("INA"))) {
            errores.add(err("V12", "No se puede realizar la accion: el registro esta inactivo."));
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

    private static boolean soloNumeros(String s) {
        return s != null && s.matches("\\d+");
    }

    private static boolean soloLetrasYEspacios(String s) {
        return s != null && s.matches("^[\\p{L} ]+$");
    }

    private static boolean nombreValido(String s) {
        return s != null && s.matches("^[\\p{L}0-9 .,\\-()&]+$");
    }

    private static boolean correoValido(String correo) {
        return correo != null && correo.matches("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
    }

    public static String obtenerSiguienteIdProveedor() {
        return ProveedoresMD.obtenerSiguienteIdProveedor();
    }
}