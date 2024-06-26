package biblioteca;

import java.io.Serializable;

public class TLibro implements Serializable{

    String Idioma, Isbn;
    String Pais;
    String Titulo;
    String Autor;
    int NoLibros, NoPrestados, NoListaEspera, Anio;
    String nombreRepositorio;

    public TLibro(String Idioma, String Isbn, String Pais, String Titulo, String Autor, int NoLibros, int NoPrestados, int NoListaEspera, int Anio) {
        this.Idioma = Idioma;
        this.Isbn = Isbn;
        this.Pais = Pais;
        this.Titulo = Titulo;
        this.Autor = Autor;
        this.NoLibros = NoLibros;
        this.NoPrestados = NoPrestados;
        this.NoListaEspera = NoListaEspera;
        this.Anio = Anio;
        nombreRepositorio = null;
    }
    
    public void setNombreRepositorio(String n) {
        nombreRepositorio = n;
    }
    
    public String getNombreRepositorio() {
        return this.nombreRepositorio;
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
            System.out.println(String.format("%-5s%-58s%-18s%-4s%-4s%-4s", "POS", "TITULO", "ISBN", "DIS", "PRE", "RES"));
            System.out.println(String.format("     %-30s%-28s%-12s", "AUTOR", "PAIS (IDIOMA)", "AÑO"));
            for (int i = 0; i < 93; i++) {
                System.out.print("*");
            }
            System.out.print("\n");
        }

        String T = Ajustar(String.format("%-58s", Titulo), 58);

        String A = Ajustar(String.format("%-30s", Autor), 30);
        String PI = Ajustar(String.format("%-28s", Pais + " (" + Idioma + ")"), 28);

        System.out.println(String.format("%-5d%s%-18s%-4d%-4d%-4d", Pos + 1, T, Isbn, NoLibros, NoPrestados, NoListaEspera));
        System.out.println(String.format("     %s%s%-12d", A, PI, Anio));
    }

    public String getIdioma() {
        return Idioma;
    }

    public void setIdioma(String Idioma) {
        this.Idioma = Idioma;
    }

    public String getIsbn() {
        return Isbn;
    }

    public void setIsbn(String Isbn) {
        this.Isbn = Isbn;
    }

    public String getPais() {
        return Pais;
    }

    public void setPais(String Pais) {
        this.Pais = Pais;
    }

    public String getTitulo() {
        return Titulo;
    }

    public void setTitulo(String Titulo) {
        this.Titulo = Titulo;
    }

    public String getAutor() {
        return Autor;
    }

    public void setAutor(String Autor) {
        this.Autor = Autor;
    }

    public int getNoLibros() {
        return NoLibros;
    }

    public void setNoLibros(int NoLibros) {
        this.NoLibros = NoLibros;
    }

    public int getNoPrestados() {
        return NoPrestados;
    }

    public void setNoPrestados(int NoPrestados) {
        this.NoPrestados = NoPrestados;
    }

    public int getNoListaEspera() {
        return NoListaEspera;
    }

    public void setNoListaEspera(int NoListaEspera) {
        this.NoListaEspera = NoListaEspera;
    }

    public int getAnio() {
        return Anio;
    }

    public void setAnio(int Anio) {
        this.Anio = Anio;
    }
    
    public void aumentarDisponibles() {
        NoLibros++;
    }
    
    public void aumentarPrestados(){
        this.NoPrestados++;
    }
    public void aumentarLibroEspera(){
        this.NoListaEspera++;
    }
    
    public void disminuirDisponibles() {
        NoLibros--;
    }
    
    public void disminuirPrestados() {
        NoPrestados--;
    }
    
    public void disminuirReservados() {
        NoListaEspera--;
    }
    
}
