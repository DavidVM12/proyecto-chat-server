import chat.HiloCliente;
import persistencia.ManejoArchivos;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server {

    static final int PUERTO = 5000;
    static String usuarios;
    static ArrayList<HiloCliente> listaHilos;

    public static void main(String[] args) {

        listaHilos = new ArrayList<>();
        usuarios = ManejoArchivos.leerXmlUsuarios();
        new Server();

    }

     public Server(){
        try{

            ServerSocket servidor = new ServerSocket(PUERTO, 2);
            System.out.println("escucho el puerto");

            int cont = 0;

            while(true){

                Socket conexion = servidor.accept();
                listaHilos.add(HiloCliente.crearYComenzar("" + cont, conexion));
                cont++;

                for (HiloCliente c : listaHilos){
                    c.setListaHilos(listaHilos);
                }

            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
     }

}