package servidor;

import biblioteca.GestorBibliotecaIntf;
import biblioteca.TDatosRepositorio;
import biblioteca.TLibro;
import java.io.DataInputStream;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GestorBibliotecaImpl implements GestorBibliotecaIntf {

    public int IdAdmin = -1;
    private List<String> repositoriosCargados = new ArrayList<>();
    private List<TLibro> Biblioteca = new ArrayList<>();
    private int totalLibros = 0;
    private List<TDatosRepositorio> Repositorios = new ArrayList<>();

    @Override
    public int Conexion(String pPasswd) throws RemoteException {

        int result = Integer.parseInt(pPasswd);

        if (-1 != IdAdmin) {
            result = -1;
        } else if (result != 1234) {
            result = -2;
        } else {
            Random r = new Random();
            result = r.nextInt(1000000) + 1;
            IdAdmin = result;
        }

        return result;
    }

    @Override
    public boolean Desconexion(int pIda) throws RemoteException {
        boolean desconectar = false;
        if (IdAdmin != -1 && pIda == IdAdmin) {
            IdAdmin = -1;
            desconectar = true;
        }
        return desconectar;
    }

    @Override
    public int NRepositorios(int pIda) throws RemoteException {
        int result = -1;
        if (pIda == IdAdmin && IdAdmin != -1) {
            result = Repositorios.size();
        }
        return result;
    }

    @Override
    public TDatosRepositorio DatosRepositorio(int pIda, int pPosRepo) throws RemoteException {

        TDatosRepositorio Repositorio = new TDatosRepositorio();
        if (pIda == IdAdmin) {
            Repositorio = Repositorios.get(pPosRepo);
        } else {
            System.err.println("ERROR. El id de administrador no coincide");
        }
        return Repositorio;
    }

    @Override
    public int AbrirRepositorio(int pIda, String pNomFichero) throws RemoteException {

        int result = -2;

        if (IdAdmin == -1 || pIda != IdAdmin) {
            result = -1;
        } else if (repositoriosCargados.contains(pNomFichero)) {
            result = -2;
        } else {
            try (DataInputStream dis = new DataInputStream(new FileInputStream(pNomFichero))) {
                int nLibros = dis.readInt();

                System.out.println("TOTAL LIBROS: " + nLibros);
                totalLibros += nLibros;

                String nombreRepositorio = dis.readUTF();
                String direccionRepositorio = dis.readUTF();

                TDatosRepositorio nuevoRepo = new TDatosRepositorio(nombreRepositorio, direccionRepositorio, nLibros);
                nuevoRepo.setNomFichero(pNomFichero);
                Repositorios.add(nuevoRepo);

                for (int i = 0; i < nLibros; i++) {
                    int j = i + 1;
                    System.out.println("");
                    System.out.println("*****LIBRO " + j + " *****");
                    String isbn = dis.readUTF();
                    System.out.println("isbn: " + isbn);
                    String titulo = dis.readUTF();
                    System.out.println("titulo: " + titulo);
                    String autor = dis.readUTF();
                    System.out.println("autor: " + autor);
                    int anio = dis.readInt();
                    System.out.println("anio: " + anio);
                    String pais = dis.readUTF();
                    System.out.println("pais: " + pais);
                    String idioma = dis.readUTF();
                    System.out.println("Idioma: " + idioma);
                    int noLibros = dis.readInt();
                    System.out.println("noLibros: " + noLibros);
                    int noPrestados = dis.readInt();
                    System.out.println("noPrestados: " + noPrestados);
                    int noListaEspera = dis.readInt();
                    System.out.println("noListaEspera: " + noListaEspera);

                    TLibro libro = new TLibro(idioma, isbn, pais, titulo, autor, noLibros, noPrestados, noListaEspera, anio);
                    libro.setNombreRepositorio(nuevoRepo.getNombre());
                    Biblioteca.add(libro); // Agregar el libro a la lista
                    
                }

                dis.close();
                repositoriosCargados.add(pNomFichero);
                result = 1;

            } catch (FileNotFoundException fnf) {
                return 0; // Archivo no encontrado
            } catch (IOException e) {
                throw new RemoteException("Error al cargar el repositorio: " + e.getMessage());
            }
        }
        return result;
    }

    @Override
    public int GuardarRepositorio(int pIda, int pRepo) throws RemoteException {
        int result = -3;
        if(IdAdmin != pIda) {
            result = -1;
        } else if (pRepo != -1 && pRepo > this.Repositorios.size()) {
            result = -2;            
        } else if(pRepo >= 0 && pRepo < Repositorios.size()){
            
            try {
                TDatosRepositorio repo = Repositorios.get(pRepo);
                String nomFichero = repo.getNomFichero();
                int libros = repo.getnLibros();
                DataOutputStream dos = new DataOutputStream(new FileOutputStream(nomFichero));
                
                dos.writeInt(libros);
                dos.writeUTF(repo.getNombre());
                dos.writeUTF(repo.getDireccion());
                
                for(TLibro libro: Biblioteca) {
                    if(libro.getNombreRepositorio().equals(repo.getNombre())) {
                        dos.writeUTF(libro.getIsbn());
                        dos.writeUTF(libro.getTitulo());
                        dos.writeUTF(libro.getAutor());
                        dos.writeInt(libro.getAnio());
                        dos.writeUTF(libro.getPais());
                        dos.writeUTF(libro.getIdioma());
                        dos.writeInt(libro.getNoLibros());
                        dos.writeInt(libro.getNoPrestados());
                        dos.writeInt(libro.getNoPrestados());
                    }
                }
                result = 1;
            } catch(FileNotFoundException fex) {
                result = - 2;
            } catch (IOException ex) {
                System.err.println("Error al guardar los datos en el fichero.");
            }
            
        } else if(pRepo == -1) {
            
            for(int i = 0; i < Repositorios.size(); i++) {
                try {
                    TDatosRepositorio repo = Repositorios.get(i);
                    String nomFichero = repo.getNomFichero();
                    int libros = repo.getnLibros();
                    DataOutputStream dos = new DataOutputStream(new FileOutputStream(nomFichero));

                    dos.writeInt(libros);
                    dos.writeUTF(repo.getNombre());
                    dos.writeUTF(repo.getDireccion());

                    for(TLibro libro: Biblioteca) {
                        if(libro.getNombreRepositorio().equals(repo.getNombre())) {
                            dos.writeUTF(libro.getIsbn());
                            dos.writeUTF(libro.getTitulo());
                            dos.writeUTF(libro.getAutor());
                            dos.writeInt(libro.getAnio());
                            dos.writeUTF(libro.getPais());
                            dos.writeUTF(libro.getIdioma());
                            dos.writeInt(libro.getNoLibros());
                            dos.writeInt(libro.getNoPrestados());
                            dos.writeInt(libro.getNoPrestados());
                        }
                    }
                    
                } catch(FileNotFoundException fex) {
                    result = - 2;
                } catch (IOException ex) {
                    System.err.println("Error al guardar los datos en el fichero.");
                }
            }
            result = 1;
        }
        
        return result;
    }

    @Override
    public int NuevoLibro(int pIda, TLibro L, int pRepo) throws RemoteException {

        int result = 3;

        if (IdAdmin == -1 || pIda != IdAdmin) {
            result = -1;
        } else if (pRepo > repositoriosCargados.size() || pRepo < 0) {
            result = -2;
        } else if (compararIsbn(L.getIsbn())) {
            result = 0;
        } else {
            L.setNombreRepositorio(Repositorios.get(pRepo).getNombre());
            Biblioteca.add(L);
            Repositorios.get(pRepo).aumentarNLibros();
            result = 1;
        }
        return result;
    }

    @Override
    public int Comprar(int pIda, String pIsbn, int pNoLibros) throws RemoteException {
        int result = -2;
        int pos = Buscar(pIda, pIsbn);
        if (pIda != IdAdmin) {
            result = -1;
        } else if (pos == -1) {
            result = 0;
        } else {
            int librosN = Biblioteca.get(pos).getNoLibros();
            Biblioteca.get(pos).setNoLibros(pNoLibros + librosN);
            result = 1;
        }

        return pos;
    }

    @Override
    public int Retirar(int pIda, String pIsbn, int pNoLibros) throws RemoteException {
        int result = -2;

        int pos = Buscar(pIda, pIsbn);
        int librosN = Biblioteca.get(pos).getNoLibros();

        if (pIda != IdAdmin) {
            result = -1;
        } else if (pos == -1) {
            result = 0;
        } else if (librosN - pNoLibros < 0) {
            result = 2;
        } else {
            Biblioteca.get(pos).setNoLibros(librosN - pNoLibros);
            result = 1;
        }

        return pos;
    }

    @Override
    public boolean Ordenar(int pIda, int pCampo) throws RemoteException {
        boolean ordenado = false;
        
        if(Biblioteca == null || pIda != IdAdmin) {
		ordenado = false;
	} else {
		mergeSort(Biblioteca, 0, totalLibros - 1, pCampo);
                ordenado = true;
	}
        return ordenado;
    }

    @Override
    public int NLibros(int pRepo) throws RemoteException {
        return Repositorios.get(pRepo).getnLibros();
    }

    @Override
    public int Buscar(int pIda, String pIsbn) throws RemoteException {

        int result = -3;
        boolean encontrado = false;
        int i = 0;
        if (pIda != IdAdmin) {

            result = -2;

        } else {

            do {
                if (pIsbn.equals(Biblioteca.get(i).getIsbn())) {
                    encontrado = true;
                } else {
                    i++;
                }

            } while (!encontrado && i < Biblioteca.size());
        }

        if (encontrado) {
            result = i;
        } else {
            result = -1;
        }

        return result;
    }

    @Override
    public TLibro Descargar(int pIda, int pRepo, int pPos) throws RemoteException {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public int Prestar(int pPos) throws RemoteException {

        int result = 8;

        if (pPos < 0 || pPos > Biblioteca.size()) {
            result = -1;
        } else if (Biblioteca.get(pPos).getNoLibros() > 0) {
            Biblioteca.get(pPos).disminuirDisponibles();
            Biblioteca.get(pPos).aumentarPrestados();
            result = 1;
        } else {
            
            Biblioteca.get(pPos).aumentarLibroEspera();
            result = 0;
        }

        return result;
    }

    @Override
    public int Devolver(int pPos) throws RemoteException {
        int result = 8;

        if (pPos < 0 || pPos > Biblioteca.size()) {
            result = -1;
        } else if(Biblioteca.get(pPos).getNoListaEspera() == 0 && Biblioteca.get(pPos).getNoPrestados() == 0) {
            result = 2;
        } else if (Biblioteca.get(pPos).getNoListaEspera()> 0) {
            Biblioteca.get(pPos).disminuirReservados();
            result = 0;
        } else if(Biblioteca.get(pPos).getNoListaEspera()==0 && Biblioteca.get(pPos).getNoPrestados()>0){
            Biblioteca.get(pPos).disminuirPrestados();
            Biblioteca.get(pPos).aumentarDisponibles();
            result = 1;
        }

        return result;
    }

    @Override
    public List<TDatosRepositorio> DevolverRepositorios() throws RemoteException {
        return Repositorios;
    }

    @Override
    public List<TLibro> DevolverBiblioteca() throws RemoteException {
        return Biblioteca;
    }

    private boolean compararIsbn(String isbn) {

        boolean encontrado = false;

        int i = 0;
        do {
            if (isbn.equals(Biblioteca.get(i))) {
                encontrado = true;
            } else {
                i++;
            }

        } while (!encontrado && i < totalLibros);

        return encontrado;
    }
    
    public void merge(List<TLibro> Biblioteca, int izq, int m, int dcha, int campo) {
        int i, j, k;
        int n1 = m - izq + 1;
        int n2 = dcha - m;

        List<TLibro> L = new ArrayList<>(Biblioteca.subList(izq, m + 1));
        List<TLibro> R = new ArrayList<>(Biblioteca.subList(m + 1, dcha + 1));

        i = 0;
        j = 0;
        k = izq;
        while (i < n1 && j < n2) {
            if (compararLibros(L.get(i), R.get(j), campo) <= 0) {
                Biblioteca.set(k, L.get(i));
                i++;
            } else {
                Biblioteca.set(k, R.get(j));
                j++;
            }
            k++;
        }

        while (i < n1) {
            Biblioteca.set(k, L.get(i));
            i++;
            k++;
        }

        while (j < n2) {
            Biblioteca.set(k, R.get(j));
            j++;
            k++;
        }
    }

    public void mergeSort(List<TLibro> Biblioteca, int izq, int dcha, int campo) {
        if (izq < dcha) {
            int m = izq + (dcha - izq) / 2;

            mergeSort(Biblioteca, izq, m, campo);
            mergeSort(Biblioteca, m + 1, dcha, campo);

            merge(Biblioteca, izq, m, dcha, campo);
        }
    }

    public int compararLibros(TLibro o1, TLibro o2, int campo) {
        switch (campo) {
            case 0:
                return o1.getIsbn().compareTo(o2.getIsbn());
            case 1:
                return o1.getTitulo().compareTo(o2.getTitulo());
            case 2:
                return o1.getAutor().compareTo(o2.getAutor());
            case 3:
                return Integer.compare(o1.getAnio(), o2.getAnio());
            case 4:
                return o1.getPais().compareTo(o2.getPais());
            case 5:
                return o1.getIdioma().compareTo(o2.getIdioma());
            case 6:
                return Integer.compare(o1.getNoLibros(), o2.getNoLibros());
            case 7:
                return Integer.compare(o1.getNoPrestados(), o2.getNoPrestados());
            case 8:
                return Integer.compare(o1.getNoListaEspera(), o2.getNoListaEspera());
            default:
                // Si el campo de comparación no es válido, devuelve 0
                return 0;
        }
    }

}
