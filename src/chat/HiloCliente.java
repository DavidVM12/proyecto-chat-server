package chat;

import org.xml.sax.SAXException;
import persistencia.ManejoArchivos;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;

public class HiloCliente implements Runnable{

    Thread hilo;
    Socket conexion;
    ObjectOutputStream output;
    ObjectInputStream input;
    static String usuarios;
    static ArrayList<Usuario> usuariosArray;

    //Construye un nuevo hilo.
    HiloCliente(String nombre, Socket conexionServer){
        hilo= new Thread(this,nombre);
        this.conexion = conexionServer;
        usuariosArray = ManejoArchivos.getUsuariosArray();
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
        usuarios = ManejoArchivos.leerXmlUsuarios();

//        Obtener fecha
        LocalDate hoy = LocalDate.now();
        LocalTime ahora = LocalTime.now();
        LocalDateTime fecha = LocalDateTime.of(hoy, ahora);

        try {

            ManejoArchivos archivos = new ManejoArchivos();
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
                        if(estaRegistrado(mensaje)) {
                            output.writeObject("true");
                        } else {
                            output.writeObject("false");
                        }

                        break;

                    case '#':
//                        Recibir mensaje
                        String[] mensajeDividido = mensaje.split(";");
                        archivos.escribirXml(mensajeDividido[1], mensajeDividido[2], " " + fecha, "true");
                        break;

                    case '$':
//                        Enviar usuarios
                        output.writeObject(usuarios);
                        break;

                    case '%':
//                        Enviar historial de chats
                        output.writeObject(ManejoArchivos.leerXmlChats());
                        break;

                    case '*':
//                        Parar
                        mensaje = "stop";
                        break;

                }

//                output.close();
//                input.close();

            }
        } catch (IOException | ClassNotFoundException | ParserConfigurationException | SAXException e) {
            e.printStackTrace();
        }

        System.out.println(hilo.getName()+" terminado.");
    }

    public boolean estaRegistrado(String mensaje){

        boolean usuarioRegistrado = false;

        String[] user = mensaje.split(";");
        user[0] = user[0].replace("@", "");

        for (Usuario c : usuariosArray){
            if (user[0].equals(c.getNombre())){
                if(user[1].equals(c.getContrasenia())){
                    usuarioRegistrado = true;
                }
            }
        }

        return usuarioRegistrado;
    }

}
