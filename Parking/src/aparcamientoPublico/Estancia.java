package aparcamientoPublico;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class Estancia {
	private Date fecha_entrada;
	private Date fecha_salida;
	
	SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
	
	
	//CREAR ENTRADA / FECHA ENTRADA
	public Estancia() {
		fecha_entrada = new Date();
		fecha_salida = null;
	}
	
	public Estancia(Date entrada,Date salida) {
		fecha_entrada = entrada;
		fecha_salida = salida;
	}
	
	//REGISTRAR SALIDA
	public void registraSalida() {
		fecha_salida = new Date();
	}
	
	//TIEMPO DE LA ESTANCIA
	public float tiempoEstancia() {
		return (TimeUnit.MILLISECONDS.toSeconds(this.fecha_salida.getTime() - this.fecha_entrada.getTime()));
	}
	
	
//----------------------------------TO STRING----------------------------------
	
	public String getFecha_entrada() {
		return sdf.format(fecha_entrada);
	}

	public void setFecha_entrada(Date fecha_entrada) {
		this.fecha_entrada = fecha_entrada;
	}

	public String getFecha_salida() {
		if(fecha_salida!=null) {
			return sdf.format(fecha_salida);
		}
		else return "-";
	}

	public void setFecha_salida(Date fecha_salida) {
		this.fecha_salida = fecha_salida;
	}

	@Override
	public String toString() {
		return "Entrada->" + sdf.format(fecha_entrada) + " Salida->" + sdf.format(fecha_salida);
	}
	
	
	public boolean haTerminado() {
		if(fecha_salida == null) {
			return false;
		}
		else return true;
	}
	
	
	
}
