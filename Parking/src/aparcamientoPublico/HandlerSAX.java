package aparcamientoPublico;

import java.util.HashMap;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class HandlerSAX extends DefaultHandler {

  private StringBuilder currentValue = new StringBuilder();
  HashMap<String, Vehiculo> resultado;
  Vehiculo v;

  //para recuperar el HashMap de Vehiculos
  public HashMap<String, Vehiculo> devuelveResultado(){
	  return resultado;
  }
  
  @Override
  public void startDocument() {
      resultado = new HashMap<String, Vehiculo>();
  }

  @Override
  public void startElement(String uri, String localName, String qName, Attributes attributes) {

      //resetea el valor de la etiqueta
      currentValue.setLength(0);
      
      //crea un vehiculo del tipo que le indique la etiqueta
      if (qName.equalsIgnoreCase("vehiculo_oficial")) {
    	  v = new VehiculoOficial();
      }
      else if (qName.equalsIgnoreCase("vehiculo_residente")) {
    	  v = new VehiculoResidente();
      }
      else if (qName.equalsIgnoreCase("vehiculo_no_residente")) {
    	  v = new VehiculoNoResidente();
      }

  }

  @Override
  public void endElement(String uri, String localName, String qName) {
	  //si el elemento es <matricula> añade el vehiculo al resultado
      if (qName.equalsIgnoreCase("matricula")) {
    	  v.setMatricula(currentValue.toString());
    	  resultado.put(currentValue.toString(),v);
      }
  }

  @Override
  public void characters(char ch[], int start, int length) {
      currentValue.append(ch, start, length);
  }
}
