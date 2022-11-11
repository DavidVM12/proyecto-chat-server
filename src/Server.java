import cliente.HiloCliente;
import persistencia.ManejoArchivos;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server {

    static final int PUERTO = 5000;
    static ArrayList<String> usuariosArray = new ArrayList<>();
    static String usuarios;

    public static void main(String[] args) {

        usuarios = ManejoArchivos.leerXml();
        new Server();

    }

     public Server(){
        try{

            ServerSocket servidor = new ServerSocket(PUERTO, 2);
            System.out.println("escucho el puerto");

            int cont = 0;

            while(true){
                Socket conexion = servidor.accept();
                HiloCliente cliente = HiloCliente.crearYComenzar("hilo"+cont, conexion);
                cont++;
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
     }
}