package cliente;

import persistencia.ManejoArchivos;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

public class HiloCliente implements Runnable{

    Thread hilo;
    Socket conexion;
    ObjectOutputStream output;
    ObjectInputStream input;
    static String usuarios;

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
        usuarios = ManejoArchivos.leerXml();

        try {

            output = new ObjectOutputStream(conexion.getOutputStream());
            input  = new ObjectInputStream(conexion.getInputStream());

            String mensaje = "";

            while (!mensaje.equals("stop")) {

                output.flush();
                Object client = input.readObject();

                mensaje = client.toString();
                System.out.println(mensaje);

                switch (mensaje.charAt(0)){

                    case '@':



                    case '#':

                    case '$':

                    case '%':

                    case '*':


                }

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
