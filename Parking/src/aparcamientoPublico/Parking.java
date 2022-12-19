package aparcamientoPublico;

import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.text.ParseException;

public class Parking {
	private HashMap<String, Vehiculo> vehiculos;

	public Parking() {
		vehiculos = new HashMap<String, Vehiculo>();
	}

//-----------------REGISTRAR ENTRADA------------------------------------------------------
	public void registraEntrada(String mat) {
		// si el vehiculo con esa matricula esta en la lista, registra su entrada
		if (vehiculos.containsKey(mat)) {
			Vehiculo v = vehiculos.get(mat);
			v.registraEstancia();
		}
		// si el vehiculo no esta en la lista es un vehiculo no residente
		else {
			Vehiculo v = new VehiculoNoResidente(mat);
			vehiculos.put(mat, v);
			v.registraEstancia();
		}
	}

//-----------------REGISTRAR SALIDA-------------------------------------------------------
	public float registraSalida(String mat) {
		Vehiculo v = vehiculos.get(mat);
		float s = v.registraSalida();
		
		//si el vehiculo es no residente, lo elimina de la lista  de vehiculos
		if (v instanceof VehiculoNoResidente)
			vehiculos.remove(mat);
		return s;
	}

//-----------------AÑADIR VEHICULO A LA LISTA--------------------------------------------
	public void anyadeVehiculo(String mat, int i) {
		//Añadir vehiculo oficial
		if (i == 0) { 
			vehiculos.put(mat, new VehiculoOficial(mat));
		//Añadir vehiculo residente
		} else if (i == 1) {
			vehiculos.put(mat, new VehiculoResidente(mat));
		} else {

		}
	}

//-----------------FINALIZA MES---------------------------------------------------------
	public String finalizaMes() {
		String salida = "";
		for (Map.Entry<String, Vehiculo> objeto : vehiculos.entrySet()) {
			Vehiculo v = vehiculos.get(objeto.getKey());
			
			//Si es vehiculo oficial o residente muestra sus estancias y elimina los resitros de estas
			if (v instanceof VehiculoOficial || v instanceof VehiculoResidente) {
				salida += "_______________________________________________________________________\n" + "Matricula-> "
						+objeto.getKey() + "\n" +
						  "-----------------------------------------------------------------------\n"
						+ v.finalizaMes() + "\n";
			}
		}
		return salida;
	}
	
	
//------------ESCRIBIR LOS PAGOS DE LOS RESIDENTES EN UN FICHERO---------------------------
	public void pagoResidentes(String nombreFichero) {
		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter(nombreFichero));
			bw.write("Matricula\tTiempo estacionado(min)\t\tCantidad a pagar\n");
			for (Map.Entry<String, Vehiculo> objeto : vehiculos.entrySet()) {
				Vehiculo v = vehiculos.get(objeto.getKey());
				if (v instanceof VehiculoResidente) {
					String resultado = ((VehiculoResidente) v).resumenPagar();
					bw.write(resultado);
				}
			}
			bw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

//------------------------VEHICULOS A XML-------------------------------------------------------------------------
	public void escribeVehiculos() throws ParserConfigurationException, TransformerException {
		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

		Document doc = docBuilder.newDocument();
		Element rootElement = doc.createElement("Vehiculos");
		doc.appendChild(rootElement);
		for (Map.Entry<String, Vehiculo> objeto : vehiculos.entrySet()) {
			Vehiculo v = vehiculos.get(objeto.getKey());
			if (v instanceof VehiculoResidente) {
				Element vehiculoRes = doc.createElement("vehiculo_residente");
				rootElement.appendChild(vehiculoRes);
				Element matricula = doc.createElement("matricula");
				matricula.setTextContent(v.getMatricula());
				vehiculoRes.appendChild(matricula);
				
			} else if (v instanceof VehiculoOficial) {
				Element vehiculoOfi = doc.createElement("vehiculo_oficial");
				rootElement.appendChild(vehiculoOfi);
				Element matricula = doc.createElement("matricula");
				matricula.setTextContent(v.getMatricula());
				vehiculoOfi.appendChild(matricula);
				
			} else if (v instanceof VehiculoNoResidente) {
				Element vehiculoNoRes = doc.createElement("vehiculo_no_residente");
				rootElement.appendChild(vehiculoNoRes);
				Element matricula = doc.createElement("matricula");
				matricula.setTextContent(v.getMatricula());
				vehiculoNoRes.appendChild(matricula);
			}

		}
		escribeXML(doc, new File("vehiculosXML.txt"));
	}

	
//------------------------ESTANCIAS A XML-----------------------------------------------------------------
	public void escribeEstancias() throws ParserConfigurationException, TransformerException {
		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

		Document doc = docBuilder.newDocument();
		Element rootElement = doc.createElement("Entradas-salidas");
		doc.appendChild(rootElement);
		for (Map.Entry<String, Vehiculo> objeto : vehiculos.entrySet()) {
			Vehiculo v = vehiculos.get(objeto.getKey());
			Element vehiculo = doc.createElement("vehiculo");
			vehiculo.setAttribute("matricula", v.getMatricula());
			Element estancias = doc.createElement("estancias");
			v.estanciasXML(estancias, doc);
			vehiculo.appendChild(estancias);
			rootElement.appendChild(vehiculo);
		}
		escribeXML(doc, new File("estanciasXML.txt"));
	}

	
	
//------------------PARA ESCRIBIR CUALQUIER FICHEROS-------------------------------------------------	
	public void escribeXML(Document doc, File ficheroSalida) throws TransformerException {
		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		Transformer transformer = transformerFactory.newTransformer();

		// pretty print
		transformer.setOutputProperty(OutputKeys.INDENT, "yes");

		DOMSource source = new DOMSource(doc);
		StreamResult result = new StreamResult(ficheroSalida);

		transformer.transform(source, result);

	}
	
	
//-----------------------RECUPERAR ESTANCIAS CON DOM------------------------------------------------------------------https://javiergarciaescobedo.es/programacion-en-java/53-xml/247-ejemplo-de-lectura-de-fichero-xml-usando-dom
	public void recuperaEstancias() throws ParseException {
		try {
			DocumentBuilderFactory fábricaCreadorDocumento = DocumentBuilderFactory.newInstance();
			DocumentBuilder creadorDocumento = fábricaCreadorDocumento.newDocumentBuilder();
			Document documento = creadorDocumento.parse("estanciasXML.txt");
			// Obtener el elemento raíz del documento
			Element raiz = documento.getDocumentElement();
			NodeList listaVehiculos = raiz.getElementsByTagName("vehiculo");
		
			for (int i = 0; i < listaVehiculos.getLength(); i++) {
				
				//para la etiqueta <vehiculo>
				Node nodoVehiculo = listaVehiculos.item(i);
				Element vehiculo = (Element) nodoVehiculo;
				String matricula = vehiculo.getAttribute("matricula");
				Vehiculo v = vehiculos.get(matricula);
				
				//para la etiqueta <estancias> que solo hay una 
				NodeList etiquetaEstancias = vehiculo.getElementsByTagName("estancias");
				Node nodoEstancias = etiquetaEstancias.item(0);
				Element estancias = (Element)nodoEstancias;
				
				//lsita de <estancia> dentro de <estancias>
				NodeList listaEstancias = estancias.getElementsByTagName("estancia");

				for (int j = 0; j < listaEstancias.getLength(); j++) {
					
					//para la etiqueta <estancia>
					Node nodoEstancia = listaEstancias.item(j);
					Element estancia = (Element) nodoEstancia;
					
					//para las etiquetas <entrada> y <salida>
					NodeList registroEntrada = estancia.getElementsByTagName("entrada");
					NodeList registroSalida = estancia.getElementsByTagName("salida");
					
					String fecha_Entrada = registroEntrada.item(0).getTextContent();
					String fecha_Salida = registroSalida.item(0).getTextContent();
					
					v.anyadeEstancia(fecha_Entrada, fecha_Salida);
					
				}
				if(v instanceof VehiculoResidente) ((VehiculoResidente) v).actualizaTiempoEstancias();

			}

		} catch (SAXException ex) {
			System.out.println("ERROR: El formato XML del fichero no es correcto\n" + ex.getMessage());
			ex.printStackTrace();
		} catch (IOException ex) {
			System.out.println("ERROR: Se ha producido un error el leer el fichero\n" + ex.getMessage());
			ex.printStackTrace();
		} catch (ParserConfigurationException ex) {
			System.out.println("ERROR: No se ha podido crear el generador de documentos XML\n" + ex.getMessage());
			ex.printStackTrace();
		}
	}
	
	
//--------------------------------RECUPERAR VEHICULOS CON SAX--------------------------------------------------------------
	public void recuperaVehiculos() {
		SAXParserFactory factory = SAXParserFactory.newInstance();

        try {
            SAXParser saxParser = factory.newSAXParser();
            HandlerSAX handler = new HandlerSAX();
            saxParser.parse("vehiculosXML.txt", handler);
            vehiculos = handler.devuelveResultado();

        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }
	}
	
//----------------------PARA VER SI YA HAY UN VEHICULO EN EL HASHMAP---------------------------------------------
	public boolean existeVehiculo(String mat) {
		return vehiculos.containsKey(mat);
	}
}
