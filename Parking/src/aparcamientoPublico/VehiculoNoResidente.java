package aparcamientoPublico;

public class VehiculoNoResidente extends Vehiculo{
	private static float precio= 0.02f;
	
	public VehiculoNoResidente(String mat) {
		super(mat,precio);	
	}
	
	public VehiculoNoResidente() {
		super();	
	}
	
	//registra la salida y devuelve cuanto debe pagar
	public float registraSalida(){
		Estancia e = estancias.get(estancias.size()-1);
		e.registraSalida();
		float factura = e.tiempoEstancia()*precio;
		return factura;
	}

	public String finalizaMes() {
		return null;
	}
}
