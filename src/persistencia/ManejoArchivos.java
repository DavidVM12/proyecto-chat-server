package persistencia;
import chat.Usuario;
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

    static ArrayList<Usuario> usuariosArray = new ArrayList<>();
    static File xmlChat = new File("src/xml/HistorialChat.xml");

//    Generar el archivo xml correctamente
    static DocumentBuilderFactory factory;
    static DocumentBuilder builder;
    static Document doc;
    static Element rootElement;

//    Constructor para inicializar una primera instancia de las etiquetas del xml
    public ManejoArchivos() throws ParserConfigurationException, IOException, SAXException {
        factory = DocumentBuilderFactory.newInstance();
        builder = factory.newDocumentBuilder();
        doc = builder.newDocument();
        rootElement = doc.createElement("historialChats");
        doc.appendChild(rootElement);
    }

    public void escribirXml(String contenidoChat, String receptorChat, String fechaHoraChat, String estadoChat){

        try {

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

        } catch (TransformerException pce) {
            pce.printStackTrace();
        }

    }

    public static String leerXmlChats(){

        String chat = "%" + ":";
        String contenido;
        String receptor;
        String fechaHora;
        String estado;

        try {
            File file = new File("src/xml/HistorialChat.xml");
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document document = db.parse(file);
            document.getDocumentElement().normalize();
            NodeList nList = document.getElementsByTagName("chat");
            for (int temp = 0; temp < nList.getLength(); temp++) {
                Node nNode = nList.item(temp);
                if (nNode.getNodeType() == Node.ELEMENT_NODE) {

                    Element eElement = (Element) nNode;

                    contenido = eElement.getElementsByTagName("contenido").item(0).getTextContent();
                    receptor = eElement.getElementsByTagName("receptor").item(0).getTextContent();
                    fechaHora = eElement.getElementsByTagName("fechaHora").item(0).getTextContent();
                    estado = eElement.getElementsByTagName("estado").item(0).getTextContent();

                    chat += contenido + ";" + receptor + ";" + fechaHora + ";" + estado + ";";

                }
            }
        }
        catch(IOException e) {
            System.out.println(e);
        } catch (ParserConfigurationException | SAXException e) {
            e.printStackTrace();
        }

        return chat;

    }


    public static String leerXmlUsuarios(){

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

                    clientes +=  ":" + nombre + ";" + ip + ";" + id + ";" + estado + ";" + contrasenia + ";";

                    usuariosArray.add(new Usuario(nombre, ip, id, estado, contrasenia));
                }
            }
        }
        catch(IOException e) {
            System.out.println(e);
        } catch (ParserConfigurationException | SAXException e) {
            e.printStackTrace();
        }

        return clientes;

    }

    public static ArrayList<Usuario> getUsuariosArray() {
        return usuariosArray;
    }

}
