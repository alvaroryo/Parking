package aparcamientoPublico;

import java.io.File;
import java.text.ParseException;
import java.util.Scanner;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

public class Main {
	static Scanner entrada = new Scanner(System.in);
	
	public static void main(String[] args) throws ParserConfigurationException, TransformerException, ParseException {
		Parking parking = new Parking();
		
		File f = new File("vehiculosXML.txt");
		//si el fichero de vehiculos existe, recupera los vehiculos y las estancis
		if(f.exists()) { 
			//si no hay fichero de  vehiculos no se puede recuperar tampoco las estancias
			try {
				parking.recuperaVehiculos();
				parking.recuperaEstancias();
			}catch(Exception e) {
				System.err.println("Ha habido un problema al recuperar los datos.");
			}	
		}
		else{
			System.out.println("Bienvenido a la aplicacion de Aparcamiento");
		}
		
		
		boolean finalizar = false;
		while (!finalizar) {
			System.out.println("\t  ____________________________");
			System.out.println("\t |Estadisticas                |");
			System.out.println("\t |------------                |");
			System.out.println("\t |1. Registrar entrada        |");
			System.out.println("\t |2. Registrar salida         |");
			System.out.println("\t |3. Alta a vehiculo oficial  |");
			System.out.println("\t |4. Alta a vehiculo residente|");
			System.out.println("\t |5. Comenzar el mes          |");
			System.out.println("\t |6. Pagos residentes         |");
//			System.out.println("\t |7. Recuperar Vehiculos      |");
//			System.out.println("\t |8. Recuperar Estancias      |");
			System.out.println("\t |0. Salir                    |");
			System.out.println("\t |____________________________|");
			int opcion = Integer.parseInt(entrada.nextLine());

			// REGISTRAR ENTRADA
			if (opcion == 1) {
				parking.registraEntrada(pideMatricula());
				parking.escribeVehiculos();
				parking.escribeEstancias();
			}
			
			// REGISTRAR SALIDA
			else if (opcion == 2) {
				float e = parking.registraSalida(pideMatricula());
				
				if(e>0) {
					System.out.println("Coste de la estancia: " + e+"€");
				}
				parking.escribeEstancias();
				parking.escribeVehiculos();
			}	
			
			//ALTA A VEHICULO OFICIAL
			else if (opcion == 3) {
				String mat = pideMatricula();
				if(parking.existeVehiculo(mat)) {
					System.err.println("ERROR: El vehiculo ya estaba registrado.");
				}
				else{
					parking.anyadeVehiculo(pideMatricula(),0);
					parking.escribeVehiculos();
				}
				
				
			}
			
			//ALTA A VEHICULO RESIDENTE
			else if (opcion == 4) {
				String mat = pideMatricula();
				if(parking.existeVehiculo(mat)) {
					System.err.println("ERROR: El vehiculo ya estaba registrado.");
				}
				else{
					parking.anyadeVehiculo(pideMatricula(),1);
					parking.escribeVehiculos();
				}
			}			
			
			//COMENZAR MES
			else if (opcion == 5) {
				System.out.println(parking.finalizaMes());
				parking.escribeEstancias();
			}	
			
			//FICHERO CON PAGOS RESIDENTES
			else if (opcion == 6) {
				System.out.println("Introduce un nombre para el fichero. (ejemplo.txt)");
				parking.pagoResidentes(entrada.nextLine());
			}	
			
			//GUARDA LOS VEHICULOS EN EL XML
			else if (opcion == 7) {
				parking.escribeVehiculos();
			}	
			
			//GUARDA LAS ESTANCIAS EN EL XML
			else if (opcion == 8) {
				parking.escribeEstancias();
			}
			
//			//RECUPERA LAS ESTANCIAS DEL XML
//			else if (opcion == 9) {	
//				parking.recuperaEstancias();
//			}
//			
//			//RECUPERA LOS VEHICULOS DEL XML
//			else if (opcion == 10) {
//				parking.recuperaVehiculos();
//			}
			
			//SALIR DEL PROGRAMA
			else if (opcion == 0) finalizar = true;
			
			//OPCION NO VALIDA
			else System.out.println("Opcion no valida");			
		}
	}
	
	public static String pideMatricula() {
		System.out.print("Matricula: ");
		String mat = entrada.nextLine();
		return mat;
	}
}