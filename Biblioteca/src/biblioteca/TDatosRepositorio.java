package biblioteca;

import java.io.Serializable;

public class TDatosRepositorio implements Serializable{
    
    
    private String nombre, direccion;
    private int nLibros;
    private String nomFichero;

    public TDatosRepositorio() {
    }

    
    public TDatosRepositorio(String nombre, String direccion, int nLibros) {
        this.nombre = nombre;
        this.direccion = direccion;
        this.nLibros = nLibros;
        nomFichero = null;
    }

    public String getNomFichero() {
        return nomFichero;
    }

    public void setNomFichero(String nomFichero) {
        this.nomFichero = nomFichero;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public int getnLibros() {
        return nLibros;
    }

    public void setnLibros(int nLibros) {
        this.nLibros = nLibros;
    }
    
    public void aumentarNLibros(){
        nLibros++;
    }
    
    private String Ajustar(String S, int Ancho) {
        byte v[] = S.getBytes();
        int c = 0;
        int len = 0;
        int uin;
        for (int i = 0; i < v.length; i++) {
            uin = Byte.toUnsignedInt(v[i]);
            if (uin > 128) {
                c++;
            }
        }

        len = c / 2;

        for (int i = 0; i < len; i++) {
            S = S + " ";
        }

        return S;
    }

    public void Mostrar(int Pos, boolean Cabecera) {
        if (Cabecera) {
            System.out.println(String.format("%-5s%-30s%-30s%-10s", "POS", "NOMBRE", "DIRECCION", "Nº DE LIBROS"));
            for (int i = 0; i < 75; i++) {
                System.out.print("*");
            }
            System.out.print("\n");
        }

        // Ajusta las cadenas a los tamaños apropiados
        String N = Ajustar(String.format("%-30s", nombre), 30);
        String D = Ajustar(String.format("%-30s", direccion), 30);
        String NL = Ajustar(String.format("%-10s", nLibros), 10);

        // Imprime los datos ajustados en el formato correcto
        System.out.println(String.format("%-5d%-30s%-30s%-10s", Pos + 1, N, D, NL));
    }
    
    
}
