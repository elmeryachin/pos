package bo.clync.pos.arquetipo.dto;

public class DatosUsuario {

    private static final Long id = 1000l;

    private Integer idCredencial;
    private Long idUsuario;
    private String nombreUsuario;
    private String codigoAmbiente;
    private String nombreAmbiente;
    private String tipoAmbiente;
    private String contrasenia;
    private String token;

    public DatosUsuario(Integer idCredencial, Long idUsuario, String nombreUsuario, String codigoAmbiente, String nombreAmbiente, String tipoAmbiente, String contrasenia, String token) {
        this.idCredencial = idCredencial;
        this.idUsuario = idUsuario;
        this.nombreUsuario = nombreUsuario;
        this.codigoAmbiente = codigoAmbiente;
        this.nombreAmbiente = nombreAmbiente;
        this.tipoAmbiente = tipoAmbiente;
        this.contrasenia = contrasenia;
        this.token = token;
    }

    public Integer getIdCredencial() {
        return idCredencial;
    }

    public void setIdCredencial(Integer idCredencial) {
        this.idCredencial = idCredencial;
    }

    public Long getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Long idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public String getCodigoAmbiente() {
        return codigoAmbiente;
    }

    public void setCodigoAmbiente(String codigoAmbiente) {
        this.codigoAmbiente = codigoAmbiente;
    }

    public String getNombreAmbiente() {
        return nombreAmbiente;
    }

    public void setNombreAmbiente(String nombreAmbiente) {
        this.nombreAmbiente = nombreAmbiente;
    }

    public String getTipoAmbiente() {
        return tipoAmbiente;
    }

    public void setTipoAmbiente(String tipoAmbiente) {
        this.tipoAmbiente = tipoAmbiente;
    }

    public String getContrasenia() {
        return contrasenia;
    }

    public void setContrasenia(String contrasenia) {
        this.contrasenia = contrasenia;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
