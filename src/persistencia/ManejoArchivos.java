package persistencia;
import chat.Cliente;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.util.ArrayList;

public class ManejoArchivos {

    static ArrayList<Cliente> usuariosArray = new ArrayList<>();
    static File xmlChat = new File("src/xml/HistorialChat.xml");

    public static void crearArchivos(String nombreArchivo){

        File archivo = new File(nombreArchivo);
        try (PrintWriter salida = new PrintWriter(archivo)) {
            salida.close();
            System.out.println("Se creo el archivo");
        } catch (FileNotFoundException e) {
            e.printStackTrace(System.out);
        }

    }
    
    public static void leerArchivo(String nombreArchivo) {

        File archivo = new File(nombreArchivo);
        try (BufferedReader entrada = new BufferedReader(new FileReader(archivo))) {
        	String lectura = entrada.readLine();
        	while(lectura != null) {
        		System.out.println(lectura);
        		lectura = entrada.readLine();
        	}
        	entrada.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace(System.out);
        } catch (IOException e) {
            e.printStackTrace(System.out);
        }

    }

    public static void escribirArchivo(String nombreArchivo, String texto) {

        File archivo = new File(nombreArchivo);
        try (PrintWriter salida = new PrintWriter(new FileWriter(archivo, true))) {
        	salida.println(texto);
            salida.close();
            System.out.println("Se escribio el archivo");
        } catch (IOException e) {
            e.printStackTrace(System.out);
        }

    }

    public static void escribirXml(String contenidoChat, String receptorChat, String fechaHoraChat, String estadoChat){

        try {

            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(new File("src/xml/HistorialChat.xml"));

            Element rootElement = doc.createElement("historialChats");
            doc.appendChild(rootElement);

            //Chat
            Element chat = doc.createElement("chat");
            rootElement.appendChild(chat);
            Element contenido = doc.createElement("contenido");
            contenido.setTextContent(contenidoChat);
            chat.appendChild(contenido);
            Element receptor = doc.createElement("receptor");
            receptor.setTextContent(receptorChat);
            chat.appendChild(receptor);
            Element fechaHora = doc.createElement("fechaHora");
            fechaHora.setTextContent(fechaHoraChat);
            chat.appendChild(fechaHora);
            Element estado = doc.createElement("estado");
            estado.setTextContent(estadoChat);
            chat.appendChild(estado);


            //Se escribe el contenido del XML en un archivo
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(xmlChat);
            transformer.transform(source, result);

        } catch (ParserConfigurationException | TransformerException | IOException | SAXException pce) {
            pce.printStackTrace();
        }

    }

    public static String leerXml(){

        String clientes = "$";
        String nombre;
        String ip;
        String id;
        String estado;
        String contrasenia;

        try {
            File file = new File("src/xml/ListaUsuarios.xml");
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document document = db.parse(file);
            document.getDocumentElement().normalize();
            NodeList nList = document.getElementsByTagName("usuario");
            for (int temp = 0; temp < nList.getLength(); temp++) {
                Node nNode = nList.item(temp);
                if (nNode.getNodeType() == Node.ELEMENT_NODE) {

                    Element eElement = (Element) nNode;

                    nombre = eElement.getElementsByTagName("nombre").item(0).getTextContent();
                    ip = eElement.getElementsByTagName("ip").item(0).getTextContent();
                    id = eElement.getElementsByTagName("id").item(0).getTextContent();
                    estado = eElement.getElementsByTagName("estado").item(0).getTextContent();
                    contrasenia = eElement.getElementsByTagName("contrasenia").item(0).getTextContent();

                    clientes +=  ":" + nombre + ";" + ip + ";" + id + ";" + estado + ";" + contrasenia;

                    usuariosArray.add(new Cliente(nombre, ip, id, estado, contrasenia));

                }
            }
        }
        catch(IOException e) {
            System.out.println(e);
        } catch (ParserConfigurationException | SAXException e) {
            e.printStackTrace();
        }

        System.out.println(clientes);

        return clientes;

    }

    public static ArrayList<Cliente> getUsuariosArray() {
        return usuariosArray;
    }
}
