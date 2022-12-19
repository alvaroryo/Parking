package aparcamientoPublico;

import java.util.ArrayList;

public class VehiculoOficial extends Vehiculo{
	private static float precio = 0f;
	
	
	public VehiculoOficial(String mat) {
		super(mat,precio);
	}
	
	public VehiculoOficial() {
		super();
	}

	public float registraSalida() {
		Estancia e = estancias.get(estancias.size()-1);
		e.registraSalida();
		return -1f;
	}

	public String finalizaMes() {
		String listadoEstancias="";
		ArrayList <Estancia> estanciasAEliminar = new ArrayList<Estancia>();
		for(Estancia e : estancias) {
			if (e.haTerminado()) {
				listadoEstancias+=e.toString()+"\n";
				estanciasAEliminar.add(e);
			}
		}
		//recorre las estancias que hay que eliminar por acabar el mes
		for(Estancia e: estanciasAEliminar) {
			estancias.remove(e);
		}
		return listadoEstancias;
	}	
}
