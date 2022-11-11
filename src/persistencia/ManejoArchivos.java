package persistencia;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.util.ArrayList;

public class ManejoArchivos {



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
        } catch (FileNotFoundException e) {
            e.printStackTrace(System.out);
        } catch (IOException e) {
            e.printStackTrace(System.out);
        }

    }

    public static String leerXml(){

        String clientes = "$";

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
                    clientes +=  ":" + eElement.getElementsByTagName("nombre").item(0).getTextContent() +
                                 ";" + eElement.getElementsByTagName("ip").item(0).getTextContent() +
                                 ";" + eElement.getElementsByTagName("id").item(0).getTextContent() +
                                 ";" + eElement.getElementsByTagName("ip").item(0).getTextContent() +
                                 ";" + eElement.getElementsByTagName("estado").item(0).getTextContent() +
                                 ";" + eElement.getElementsByTagName("contrasenia").item(0).getTextContent();
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
    
}
