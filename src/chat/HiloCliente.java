package chat;

import persistencia.ManejoArchivos;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;

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

//        Obtener fecha
        LocalDate hoy = LocalDate.now();
        LocalTime ahora = LocalTime.now();
        LocalDateTime fecha = LocalDateTime.of(hoy, ahora);

        try {

            output = new ObjectOutputStream(conexion.getOutputStream());
            input  = new ObjectInputStream(conexion.getInputStream());

            String mensaje = "";

            while (!mensaje.equals("stop")) {

                output.flush();
                Object client = input.readObject();
                mensaje = client.toString();

                switch (mensaje.charAt(0)){

                    case '@':
//                        Login
                        break;

                    case '#':
//                        Recibir mensaje
                        String[] parts = mensaje.split(";");
                        ManejoArchivos.escribirXml(parts[1], parts[2], " " + fecha, "true");
                        break;

                    case '$':
//                        Enviar usuarios
                        output.writeObject(usuarios);
                        break;

                    case '%':
//                        Enviar historial de chats

                        break;

                    case '*':
//                        Parar
                        mensaje = "stop";
                        break;

                }

//                output.close();
//                input.close();

            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        System.out.println(hilo.getName()+" terminado.");
    }

}
