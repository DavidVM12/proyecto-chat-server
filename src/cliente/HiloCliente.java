package cliente;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class HiloCliente implements Runnable{

    Thread hilo;
    Socket conexion;
    ObjectOutputStream output;
    ObjectInputStream input;

    //Construye un nuevo hilo.
    HiloCliente(String nombre, Socket conexionServer){
        hilo= new Thread(this,nombre);
        this.conexion = conexionServer;
    }

    //Un método de fábrica que crea e inicia un hilo.
    public static HiloCliente crearYComenzar (String nombre, Socket conexionServer){
        HiloCliente miHilo = new HiloCliente(nombre, conexionServer);
        miHilo.hilo.start(); //Inicia el hilo
        return miHilo;
    }

    //Punto de entrada de hilo.
    public void run(){

        System.out.println(hilo.getName()+" iniciando.");

        try {

            output = new ObjectOutputStream(conexion.getOutputStream());
            input  = new ObjectInputStream(conexion.getInputStream());

            String mensaje = "";

            while (!mensaje.equals("cerrar")) {

                output.flush();
                Object client = input.readObject();

                mensaje = client.toString();
                System.out.println(mensaje);
//                Devolver el mensaje
                output.writeObject(mensaje);

//                Continuar escuchando
                System.out.println("Continuidad");

//                aux.close();
//                aux2.close();

            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        System.out.println(hilo.getName()+" terminado.");
    }

}
