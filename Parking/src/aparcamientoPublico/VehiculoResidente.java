package aparcamientoPublico;

import java.util.ArrayList;

public class VehiculoResidente extends Vehiculo{
	private static float precio= 0.002f;
	private float tiempoMes;
	
	
	public VehiculoResidente(String mat) {
		super(mat,precio);
		tiempoMes=0;
	}
	
	public VehiculoResidente() {
		super();
		tiempoMes=0;
	}
	
	public float registraSalida(){
		Estancia e = estancias.get(estancias.size()-1);
		e.registraSalida();
		tiempoMes += e.tiempoEstancia();
		return -1f;
	}
	
	public String finalizaMes() {
		String listadoEstancias="";
		ArrayList <Estancia> estanciasAEliminar = new ArrayList<Estancia>();
		for(Estancia e : estancias) {
			if (e.haTerminado()) {
				listadoEstancias+=e.toString()+" Tiempo->"+e.tiempoEstancia()+"\n";
				estanciasAEliminar.add(e);
			}
		}
		
		//recorre las estancias que hay que eliminar por acabar el mes
		for(Estancia e: estanciasAEliminar) {
			estancias.remove(e);
		}		
		listadoEstancias+="-----------------------------------------------------------------------\n"
						+ "\t\t\t\t\t\t   Tiempo total: " +tiempoMes;
		this.tiempoMes=0;
		return listadoEstancias;
	}
	
	
	
	public String resumenPagar() {
		return (getMatricula() + "\t\t\t" +tiempoMes + "\t\t\t\t\t\t\t" + precio*tiempoMes+"\n");
	}
	
	public void actualizaTiempoEstancias() {
		for(Estancia e : estancias) {
			if (e.haTerminado()) {
				tiempoMes+= e.tiempoEstancia();
			}
		}
	}
}
