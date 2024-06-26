package biblioteca;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface GestorBibliotecaIntf extends Remote {
    //Sevicios de Conexión
    int Conexion(String pPasswd) throws RemoteException;
    boolean Desconexion(int pIda) throws RemoteException;
    
    //Servicios de Repositorio
    int NRepositorios(int pIda) throws RemoteException;
    TDatosRepositorio DatosRepositorio(int pIda, int pPosRepo) throws RemoteException;
    int AbrirRepositorio(int pIda, String pNomFichero) throws RemoteException;
    int GuardarRepositorio(int pIda, int pRepo) throws RemoteException;
    
    
    //Gestión de cada repositorio por el administrador
    int NuevoLibro(int pIda, TLibro L, int pRepo) throws RemoteException;
    int Comprar(int pIda, String pIsbn, int pNoLibros) throws RemoteException;
    int Retirar(int pIda, String pIsbn, int pNoLibros) throws RemoteException;
    boolean Ordenar(int pIda, int pCampo) throws RemoteException;
    
    //Gestión de libros de todos los repositorios al mismo tiempo
    int NLibros(int pRepo) throws RemoteException;
    int Buscar(int pIda, String pIsbn) throws RemoteException;
    TLibro Descargar(int pIda, int pRepo, int pPos) throws RemoteException;
    int Prestar(int pPos) throws RemoteException;
    int Devolver(int pPos) throws RemoteException; 

    public List<TDatosRepositorio> DevolverRepositorios()throws RemoteException;
    public List<TLibro> DevolverBiblioteca()throws RemoteException;
   
}
