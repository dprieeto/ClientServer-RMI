package cliente;

import biblioteca.GestorBibliotecaIntf;
import biblioteca.TDatosRepositorio;
import biblioteca.TLibro;
import java.rmi.Naming;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class Cliente {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        List<TDatosRepositorio> Repositorios = new ArrayList<>();
        List<TLibro> Biblioteca = new ArrayList<>();
        int result_int = -5;
        boolean result_bool = false;
        int idAdmin = -1;
        boolean logueado = false;
        try {
            int Puerto = 0;
            String Host;
            Scanner Teclado = new Scanner(System.in);

            System.out.println("Introduce el nº de puerto: ");
            Puerto = Teclado.nextInt();
            Teclado.nextLine(); //limpiar buffer
            System.out.println("Introduce el nombre del host: ");
            Host = Teclado.next();
            Teclado.nextLine();
            Random rnd = new Random(System.nanoTime());

            GestorBibliotecaIntf biblio = (GestorBibliotecaIntf) Naming.lookup("rmi://" + Host + ":" + Puerto + "/Biblioteca");
            
            int opc;

            do {
                opc = MenuPrincipal();

                switch (opc) {
                    case 1:
                        int opc2;
                        System.out.println("Introduce la contraseña: ");
                        //Teclado.nextLine();
                        String pass = Teclado.nextLine();
                        
                        result_int = biblio.Conexion(pass);

                        if (result_int == -1) {
                            System.err.println("Ya hay un usuario identificado como administrador");
                        } else if (result_int == -2) {
                            System.err.println("La contraseña es erronea");
                        } else {
                            idAdmin = result_int;
                            logueado = true;
                        }

                        if (logueado) {
                            do {

                                opc2 = MenuAdministracion();

                                switch (opc2) {
                                    case 1:
                                        System.out.println("\n**CARGAR REPOSITORIO**");
                                        System.out.println("1.- Biblioteca.jdat_R1_");
                                        System.out.println("2.- Biblioteca.jdat_R2_");
                                        System.out.println("3.- Biblioteca.jdat_R3_");
                                        System.out.println("Elige opcion: ");
                                        int opc1 = Teclado.nextInt();
                                        Teclado.nextLine();
                                        String nomFich = "Biblioteca.jdat_R" + opc1 + "_";
                                        result_int = biblio.AbrirRepositorio(idAdmin, nomFich);

                                        if (result_int == -1) {
                                            System.out.println("Error: El administrador no está autorizado.");
                                        } else if (result_int == -2) {
                                            System.out.println("Error: El repositorio ya esta cargado.");
                                        } else if (result_int == 0) {
                                            System.out.println("Error: El archivo no se pudo encontrar o abrir.");
                                        } else if (result_int == 1) {
                                            Repositorios = biblio.DevolverRepositorios();
                                            System.out.println("El repositorio se cargo con exito.");
                                        }

                                        break;
                                    case 2:
                                        System.out.println("\n**Guardar Repositorio**");
                                        int totalLibros = 0;
                                        for (int i = 0; i < Repositorios.size(); i++) {
                                            if (i == 0) {
                                                Repositorios.get(i).Mostrar(i, true);
                                            } else {
                                                Repositorios.get(i).Mostrar(i, false);
                                            }
                                            totalLibros += Repositorios.get(i).getnLibros();
                                        }
                                        TDatosRepositorio todos = new TDatosRepositorio("Todos los repositorios", " ", totalLibros);
                                        todos.Mostrar(-1, false);
                                        Teclado = new Scanner(System.in);
                                        System.out.println("Elige una posicion para guardar los datos:");
                                        int rep = Teclado.nextInt();
                                        result_int = biblio.GuardarRepositorio(idAdmin, rep-1);
                                        if(result_int == 1) {
                                            System.out.println("\n*** El/los repositorios se han guardado correctamente.");
                                        } else if (result_int == 0) {
                                            System.err.println("No se ha podido guardar a fichero el/los repositorios.");
                                        } else if(result_int == -1) {
                                            System.err.println("Ya hay un usuario identificado como administrador.");
                                        } else if (result_int == -2) {
                                            System.err.println("La posicion del repositorio elegida no existe.");
                                        }
                                        break;
                                    case 3:
                                        System.out.println("\n**NUEVO LIBRO**");

                                        String isbn,
                                         autor,
                                         titulo,
                                         pais,
                                         idioma;
                                        int anio,
                                         nLibrosIni;

                                        System.out.println("Introduce el Isbn: ");
                                        Teclado = new Scanner(System.in);
                                        //Teclado.nextLine();
                                        isbn = Teclado.nextLine();
                                        System.out.println("Introduce el Autor: ");
                                        autor = Teclado.nextLine();
                                        System.out.println("Introduce el Titulo: ");
                                        titulo = Teclado.nextLine();
                                        System.out.println("Introduce el anio: ");
                                        anio = Teclado.nextInt();
                                        System.out.println("Introduce el Pais: ");
                                        Teclado.nextLine();
                                        pais = Teclado.nextLine();
                                        System.out.println("Introduce el Idioma: ");
                                        idioma = Teclado.nextLine();
                                        System.out.println("Introduce Numero de libros inicial: ");
                                        nLibrosIni = Teclado.nextInt();

                                        MostrarRepositorios(Repositorios);

                                        System.out.println("Elige repositorio: ");
                                        int repo = Teclado.nextInt();

                                        TLibro nuevoLibro = new TLibro(idioma, isbn, pais, titulo, autor, nLibrosIni, 0, 0, anio);
                                        result_int = biblio.NuevoLibro(idAdmin, nuevoLibro, repo - 1);

                                        switch (result_int) {
                                            case -1 ->
                                                System.err.println("Ya hay un usuario identificado como administrador o su idAdmin es incorrecto");
                                            case -2 ->
                                                System.err.println("El repositorio cuya posicion se indica no existe");
                                            case 0 ->
                                                System.err.println(" Hay un libro en algún repositorio de la biblioteca que tiene el mismo Isbn");
                                            case 1 ->
                                                System.out.println("**Se ha añadido el nuevo libro al repositorio indicado**");

                                        }

                                        break;

                                    case 4:
                                        System.out.println("\n**COMPRAR LIBROS**");

                                        System.out.println("Introduce Isbn a Buscar: ");
                                        String is = Teclado.nextLine();

                                        result_int = biblio.Buscar(idAdmin, is);

                                        if (result_int == -2) {
                                            System.err.println("ERROR. Ya hay un usuario identificado como administrador o el id no coincide con el almacenado en servidor");
                                        } else if (result_int == -1) {
                                            System.err.println("ERROR. No se ha encontrado ningun libro con el ISBN indicado por parametro");
                                        } else {
                                            Biblioteca.get(result_int).Mostrar(result_int, true);
                                            System.out.println("Es este el libro que deseas comprar mas unidades (s/n)?");
                                            String respuesta = Teclado.nextLine();

                                            if (respuesta.equals("s")) {
                                                System.out.println("Introduce Numero de libros comprados: ");
                                                int comp = Teclado.nextInt();
                                                result_int = biblio.Comprar(idAdmin, is, comp);

                                                switch (result_int) {
                                                    case -1 ->
                                                        System.err.println("ERROR. Ya hay un usuario identificado como administrador o el id no coincide con el alamcenado en el Servidor");
                                                    case 0 ->
                                                        System.err.println("ERROR. No se ha encontrado ningun libro con el isbn indicado por parametro");
                                                    case 1 ->
                                                        System.out.println("**COMPRA REALIZADA CON EXITO** Se han anadido " + comp + " libros a " + Biblioteca.get(result_int).getTitulo());
                                                }
                                            }
                                        }

                                        break;
                                    case 5:
                                        System.out.println("\n**RETIRAR LIBROS**");

                                        System.out.println("Introduce Isbn a Buscar: ");
                                        String isb = Teclado.nextLine();

                                        result_int = biblio.Buscar(idAdmin, isb);

                                        if (result_int == -2) {
                                            System.err.println("ERROR. Ya hay un usuario identificado como administrador o el id no coincide con el almacenado en servidor");
                                        } else if (result_int == -1) {
                                            System.err.println("ERROR. No se ha encontrado ningun libro con el ISBN indicado por parametro");
                                        } else {
                                            Biblioteca.get(result_int).Mostrar(result_int, true);
                                            System.out.println("Es este el libro que deseas comprar mas unidades (s/n)?");
                                            String respuesta = Teclado.nextLine();

                                            if (respuesta.equals("s")) {
                                                System.out.println("Introduce Numero de libros comprados: ");
                                                int comp = Teclado.nextInt();
                                                result_int = biblio.Retirar(idAdmin, isb, comp);

                                                switch (result_int) {
                                                    case -1 ->
                                                        System.err.println("ERROR. Ya hay un usuario identificado como administrador o el id no coincide con el alamcenado en el Servidor");
                                                    case 0 ->
                                                        System.err.println("ERROR. No se ha encontrado ningun libro con el isbn indicado por parametro");
                                                    case 1 ->
                                                        System.out.println("**RETIRO REALIZADO CON EXITO** Se han eliminado " + comp + " libros a " + Biblioteca.get(result_int).getTitulo());
                                                    case 2 ->
                                                        System.out.println("No hay suficientes ejemplares disponibles para ser retirados");
                                                }
                                            }
                                        }

                                        break;
                                    case 6:
                                        System.out.println("\n**Ordenar Libros**");
                                        System.out.println("Introduce el texto a Buscar: ");
                                        System.out.println("Codigo de consulta");
                                        System.out.println("0.-Por Isbn");
                                        System.out.println("1.-Por Titulo");
                                        System.out.println("2.-Por Autor");
                                        System.out.println("3.-Por Año");
                                        System.out.println("4.-Por Pais");
                                        System.out.println("5.-Por Idioma");
                                        System.out.println("6.-Por Nº de libros disponibles");
                                        System.out.println("7.-Por Nº de libros prestados");
                                        System.out.println("8.-Por Nº de libros en espera");

                                        System.out.println("Introduce Codigo");
                                        int codigo = Teclado.nextInt();

                                        boolean ordena = biblio.Ordenar(idAdmin, codigo);
                                        if (ordena) {
                                            System.out.println("\n**Libros ordenados correctamente.**");
                                        } else {
                                            System.err.println("Error al ordenar los libros");
                                        }

                                        break;
                                    case 7:
                                        System.out.println("\n**BUSCAR LIBROS**");
                                        System.out.println("Introduce el texto a Buscar: ");
                                        String campo = Teclado.nextLine();
                                        System.out.println("Codigo de consulta");
                                        System.out.println("I.-Por Isbn");
                                        System.out.println("T.-Por Titulo");
                                        System.out.println("A.-Por Autor");
                                        System.out.println("P.-Por Pais");
                                        System.out.println("D.-Por Idioma");
                                        System.out.println("*.-Por todos los campos");
                                        System.out.println("\nElige una opcion");
                                        String opcionBuscar = Teclado.nextLine();

                                        if (!Repositorios.isEmpty()) {
                                            int totalesLibros = 0;
                                            for (int i = 0; i < Repositorios.size(); i++) {
                                                if (i == 0) {
                                                    Repositorios.get(i).Mostrar(i, true);
                                                } else {
                                                    Repositorios.get(i).Mostrar(i, false);
                                                }
                                                //totalesLibros += Repositorios.get(i).getnLibros();
                                            }
                                            TDatosRepositorio tod = new TDatosRepositorio("Todos los repositorios", " ", totalesLibros);
                                            tod.Mostrar(-1, false);
                                            Teclado = new Scanner(System.in);
                                            System.out.println("Elige un repositorio: ");
                                            String eligeRepo = Teclado.nextLine();
                                            List<TLibro> buscados = new ArrayList<>();
                                            if(eligeRepo.equals("0")) {
                                                buscados = buscarLibro(Biblioteca, campo, opcionBuscar);
                                            } else {
                                                int posRepo = Integer.valueOf(eligeRepo) -1;
                                                TDatosRepositorio repo_result = biblio.DatosRepositorio(idAdmin, posRepo);
                                                buscados = buscarLibrosPorRepositorio(Biblioteca, campo, opcionBuscar, repo_result);
                                            }
                                                
                                            if (!buscados.isEmpty()) {
                                                for (int i = 0; i < buscados.size(); i++) {
                                                    if (i == 0) {
                                                        buscados.get(i).Mostrar(i, true);
                                                    } else {
                                                        buscados.get(i).Mostrar(i, false);
                                                    }
                                                }
                                            } else {
                                                System.out.println("\n*** No se han encontrado libros con esos parametros ***");
                                            }

                                        } else {
                                            System.out.println("\n*** No hay repositorios cargados en la biblioteca. ***");
                                        }

                                        break;
                                    case 8:
                                        System.out.println("\n**LISTAR LIBROS**");
                                        Biblioteca = biblio.DevolverBiblioteca();

                                        for (int i = 0; i < Biblioteca.size(); i++) {

                                            if (i == 0) {
                                                Biblioteca.get(i).Mostrar(i, true);
                                            } else {
                                                Biblioteca.get(i).Mostrar(i, false);
                                            }
                                        }
                                        break;
                                    case 0:
                                        biblio.Desconexion(idAdmin);
                                        break;
                                }
                            } while (opc2 != 0 && logueado);
                        }
                        logueado = false;
                        break;
                    case 2:
                        System.out.println("\n**Consulta de libros**");
                        System.out.println("Introduce el texto a Buscar: ");
                        Scanner entrada = new Scanner(System.in);
                        String campo = entrada.nextLine();
                        System.out.println("Codigo de consulta");
                        System.out.println("I.-Por Isbn");
                        System.out.println("T.-Por Titulo");
                        System.out.println("A.-Por Autor");
                        System.out.println("P.-Por Pais");
                        System.out.println("D.-Por Idioma");
                        System.out.println("*.-Por todos los campos");
                        System.out.println("\nElige una opcion");
                        Teclado = new Scanner(System.in);
                        String opcionBuscar = Teclado.nextLine();
                        Biblioteca = biblio.DevolverBiblioteca();
                        List<TLibro> buscados = buscarLibro(Biblioteca, campo, opcionBuscar);
                        if (!buscados.isEmpty()) {
                            for (int i = 0; i < buscados.size(); i++) {
                                if (i == 0) {
                                    buscados.get(i).Mostrar(i, true);
                                } else {
                                    buscados.get(i).Mostrar(i, false);
                                }
                            }
                        } else {
                            System.out.println("\n*** No se han encontrado libros con esos parametros ***");
                        }
                        break;
                    case 3:
                        System.out.println("\n**PRESTAMO DE LIBROS**");

                        System.out.println("Introduce el texto a Buscar: ");
                        Teclado = new Scanner(System.in);
                        String texto = Teclado.nextLine();
                        System.out.println("Codigo de consulta");
                        System.out.println("I.-Por Isbn");
                        System.out.println("T.-Por Titulo");
                        System.out.println("A.-Por Autor");
                        System.out.println("P.-Por Pais");
                        System.out.println("D.-Por Idioma");
                        System.out.println("*.-Por todos los campos");

                        System.out.println("Introduce Codigo");
                        Teclado = new Scanner(System.in);
                        String opcb = Teclado.nextLine();
                        Biblioteca = biblio.DevolverBiblioteca();
                        List<TLibro> librosPrestar = buscarLibro(Biblioteca, texto, opcb);
                        boolean cabecera = false;
                        int cab = 0;
                        for(int i = 0; i<Biblioteca.size(); i++) {
                            
                            for(int j = 0; j<librosPrestar.size(); j++) {
                                if(librosPrestar.get(j).getIsbn().equals(Biblioteca.get(i).getIsbn())) {
                                    
                                    if(cab == 0) {
                                        
                                        Biblioteca.get(i).Mostrar(i, true);
                                    }      
                                    else
                                        Biblioteca.get(i).Mostrar(i, false);
                                    cab++;
                                }
                            }                            
                        }

                        System.out.println("Quieres sacar algun libro de la biblioteca (s/n)?");
                        String respuesta = Teclado.nextLine();

                        if (respuesta.equals("s")) {
                            System.out.println("Introduce la posicion del libro a prestar: ");
                            Teclado = new Scanner(System.in);
                            int comp = Teclado.nextInt();
                            result_int = biblio.Prestar(comp-1);

                            switch (result_int) {
                                case -1 ->
                                    System.err.println("ERROR.  La posición indicada no está dentro de los límites del repositorio mezclado y ordenado");
                                case 0 ->
                                    System.out.println("Se ha puesto el usuario en la lista de espera");
                                case 1 ->
                                    System.out.println("** Se ha prestado el libro el libro correctamente**");

                            }
                        }
                        
                       
                        break;
                    case 4:
                        System.out.println("\n**DEVOLUCION DE LIBROS**");
                        
                        System.out.println("Introduce el texto a Buscar: ");
                        Teclado = new Scanner(System.in);
                        String textoDev = Teclado.nextLine();
                        System.out.println("Codigo de consulta");
                        System.out.println("I.-Por Isbn");
                        System.out.println("T.-Por Titulo");
                        System.out.println("A.-Por Autor");
                        System.out.println("P.-Por Pais");
                        System.out.println("D.-Por Idioma");
                        System.out.println("*.-Por todos los campos");

                        System.out.println("Introduce Codigo");
                        Teclado = new Scanner(System.in);
                        String opcbDev = Teclado.nextLine();
                        Biblioteca = biblio.DevolverBiblioteca();
                        List<TLibro> librosDevolver = buscarLibro(Biblioteca, textoDev, opcbDev);
                        int cabD = 0;
                        for(int i = 0; i<Biblioteca.size(); i++) {
                            
                            for(int j = 0; j<librosDevolver.size(); j++) {
                                if(librosDevolver.get(j).getIsbn().equals(Biblioteca.get(i).getIsbn())) {
                                    
                                    if(cabD == 0) {
                                        
                                        Biblioteca.get(i).Mostrar(i, true);
                                    }      
                                    else
                                        Biblioteca.get(i).Mostrar(i, false);
                                    cabD++;
                                }
                            }                            
                        }

                        System.out.println("Quieres devolver libro de la biblioteca (s/n)?");
                        String respuestaD = Teclado.nextLine();

                        if (respuestaD.equals("s")) {
                            System.out.println("Introduce la posicion del libro a devolver: ");
                            Teclado = new Scanner(System.in);
                            int compD = Teclado.nextInt();
                            result_int = biblio.Devolver(compD-1);

                            switch (result_int) {
                                case -1 ->
                                    System.err.println("ERROR.  La posición indicada no está dentro de los límites del repositorio mezclado y ordenado");
                                case 0 ->
                                    System.out.println("Se ha devuelto el libro reduciendo la lista de espera");
                                case 1 ->
                                    System.out.println("** Se ha devuelto el libro el libro correctamente**");
                                case 2 ->
                                    System.out.println("** No hay libros prestados ni usuarios en lista de espera **");

                            }
                        }
                        
                        break;
                    case 0:
                        break;
                }

            } while (opc != 0);

        } catch (Exception e) {
            System.out.println("Error: " + e);
        }

    }

    public static int MenuPrincipal() {
        Scanner Teclado = new Scanner(System.in);
        int Salida;
        boolean valido = false;
        do {
            System.out.println("\nGESTOR BIBLIOTECARIO 2.0 (M.PRINCIPAL) ");
            System.out.println("****************************************");
            System.out.println("** 1.- M.Administracion");
            System.out.println("** 2.- Consulta de libros");
            System.out.println("** 3.- Prestamo de libros");
            System.out.println("** 4.- Devolucion de libros");
            System.out.println("** 0.- Salir");
            System.out.print("** Elige Opcion: ");
            Salida = Teclado.nextInt();

            if (Salida >= 0 && Salida <= 4) {
                valido = true;
            } else {
                System.err.println("ERROR. Numero no valido");
            }
        } while (!valido);

        return Salida;
    }

    private static int MenuAdministracion() {
        Scanner Teclado = new Scanner(System.in);
        int Salida;
        boolean valido = false;
        do {
            System.out.println("\nGESTOR BIBLIOTECARIO 2.0 (M.ADMINISTRACION) ");
            System.out.println("****************************************");
            System.out.println("** 1.- Cargar Repositorio");
            System.out.println("** 2.- Guardar Repositorio");
            System.out.println("** 3.- Nuevo libro");
            System.out.println("** 4.- Comprar Libros");
            System.out.println("** 5.- Retirar Libros");
            System.out.println("** 6.- Ordenar Libros");
            System.out.println("** 7.- Buscar Libros");
            System.out.println("** 8.- Listar Libros");
            System.out.println("** 0.- Salir");
            System.out.print("** Elige Opcion: ");
            Salida = Teclado.nextInt();

            if (Salida >= 0 && Salida <= 8) {
                valido = true;
            } else {
                System.err.println("ERROR. Numero no valido");
            }
        } while (!valido);

        return Salida;
    }

    private static void MostrarRepositorios(List<TDatosRepositorio> rep) {

        System.out.println("POS\tNOMBRE\t\t\tDIRECCION\t\tNº LIBROS");
        System.out.println("**********************************************************************************************");
        int j = 0;
        for (int i = 0; i < rep.size(); i++) {
            j++;
            System.out.println(j + "\t" + rep.get(i).getNombre() + "\t\t\t" + rep.get(i).getDireccion() + "\t\t" + rep.get(i).getnLibros());
        }

    }

    
    private static List<TLibro> buscarLibro(List<TLibro> libros, String campo, String codigo) {
        List<TLibro> librosBuscados = new ArrayList<>();

        switch (codigo) {
            case "I" -> {
                for (TLibro libro : libros) {
                    if (libro.getIsbn().contains(campo)) {
                        librosBuscados.add(libro);
                    }
                }
            }
            case "T" -> {
                for (TLibro libro : libros) {
                    if (libro.getTitulo().contains(campo)) {
                        librosBuscados.add(libro);
                    }
                }
            }
            case "A" -> {
                for (TLibro libro : libros) {
                    if (libro.getAutor().contains(campo)) {
                        librosBuscados.add(libro);
                    }
                }
            }
            case "P" -> {
                for (TLibro libro : libros) {
                    if (libro.getPais().contains(campo)) {
                        librosBuscados.add(libro);
                    }
                }
            }
            case "D" -> {
                for (TLibro libro : libros) {
                    if (libro.getIdioma().contains(campo)) {
                        librosBuscados.add(libro);
                    }
                }

            }
            case "*" -> {
                for (TLibro libro : libros) {
                    if (libro.getIsbn().contains(campo) || libro.getTitulo().contains(campo) || libro.getAutor().contains(campo) || libro.getPais().contains(campo) || libro.getIdioma().contains(campo)) {
                        librosBuscados.add(libro);
                    }
                }
            }
        }
        return librosBuscados;
    }
    
    private static List<TLibro> buscarLibrosPorRepositorio(List<TLibro> libros, String campo,String codigo, TDatosRepositorio repo) {
        
        List<TLibro> librosRepo = new ArrayList<>();
        List<TLibro> librosBuscados = new ArrayList<>();
        
        for(TLibro it : libros) {
            if(it.getNombreRepositorio().equals(repo.getNombre()))
                librosRepo.add(it);
        }
        librosBuscados = buscarLibro(librosRepo, campo, codigo);
        return librosBuscados;
    }

}
