package aparcamientoPublico;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public abstract class Vehiculo {
	private String matricula;
	private float precio;
	protected ArrayList <Estancia> estancias;
	
	public Vehiculo(String m,float p) {
		this.matricula =m;
		this.precio=p;
		estancias = new ArrayList<Estancia>();
	}
	
	public Vehiculo() {
		estancias = new ArrayList<Estancia>();
	}
	
//---------------------------------------ESTANCIA--------------------------------------
	public void registraEstancia() {
		Estancia e = new Estancia();
		estancias.add(e);
	}
	
	//AÑADE UNA ESTANCIA TRAS RECUPERARLA DEL XML
	public void anyadeEstancia(String entrada, String salida) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		Date fechaEntrada = sdf.parse(entrada);
		Date fechaSalida = null;
		if(!(salida.equals("-"))) {
			fechaSalida = sdf.parse(salida);
		}
		
		Estancia e = new Estancia(fechaEntrada,fechaSalida);
		estancias.add(e);
	}
	
	//REGISTRA LA SALIDA DE UNA ESTANCIA
	public abstract float registraSalida();
	
	//FINALIZA MES Y SE MUESTRAN LAS ESTANCIAS
	public abstract String finalizaMes();
	
	
//----------------------------------SETTERS & GETTERS----------------------------------
	public String getMatricula() {
		return matricula;
	}

	public void setMatricula(String matricula) {
		this.matricula = matricula;
	}

	public float getPrecio() {
		return precio;
	}

	public void setPrecio(float precio) {
		this.precio = precio;
	}

	//CREA UNA ESTANCIA DENTRO DEL XML
	public void estanciasXML(Element estanciasXML,Document doc) {
		for(Estancia e : estancias) {
			Element estancia = doc.createElement("estancia");
			
			Element entrada = doc.createElement("entrada");
			entrada.setTextContent(e.getFecha_entrada());
			estancia.appendChild(entrada);
			
			Element salida = doc.createElement("salida");
			salida.setTextContent(e.getFecha_salida());
			estancia.appendChild(salida);
			
			estanciasXML.appendChild(estancia);
		}
		
	}
}
