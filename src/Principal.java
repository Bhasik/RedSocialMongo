import java.net.UnknownHostException;
import java.util.Scanner;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.Mongo;
import com.mongodb.MongoException;


public class Principal {
	
	
	private static Mongo mongoClient;
	private static DB db; 
	private static Scanner leer = new Scanner(System.in);
	
	public static void main(String[] args){
	
	try{
		
		mongoClient = new Mongo( "localhost" , 27017 );
		
		db = mongoClient.getDB("redsocial");

		
		int menu=-1;
			
		do{
			
			try{
				
			
			System.out.println("====MENU====");
			System.out.println("1.-Iniciar Sesion");
			System.out.println("2.-Crear nuevo usuario");
			System.out.println("0.-Salir");
			System.out.println("Introduzca una opcion");
			menu=Integer.parseInt(leer.nextLine());
			
			switch(menu){
			
			case 1:
				
				logear();
				
				break;
				
			case 2:
				
				crearUsuario();
				
				break;
				
			case 0:
				
				System.out.println("Saliendo de la aplicacion");
			
				break;
				
			default:
				
				System.out.println("La opcion no es valida \n");
				
				break;
			}
			
			}catch(NumberFormatException e){
				
				System.out.println("Ha introducido un caracter invalido \n");
			}
			
		}while(menu!=0);
		
	
			
	}catch(UnknownHostException e){
			
		e.printStackTrace();
			
	}catch(MongoException m){
			
			
		m.printStackTrace();
		
	}
	
}
	
		

	public static void crearUsuario(){
		
		System.out.println("Nombre");
		String nombre = leer.nextLine();
		
		System.out.println("Apellidos");
		String apellidos = leer.nextLine();
		
		System.out.println("Correo electronico:");
		String correo_electronico = leer.nextLine();
		
		System.out.println("Contraseña");
		String pass = leer.nextLine();
		
		System.out.println("Direccion:");
		String direccion = leer.nextLine();
		
		
		Usuario u = new Usuario(nombre,apellidos,correo_electronico,pass,direccion,db);
		menuUsuario(db, u);
		
	}
	
	public static void logear(){
		
		boolean validar;
		
		System.out.println("Usuario:(Correo electronico)");
		String correo = leer.nextLine();
		
		System.out.println("Contraseña:");
		String pass = leer.nextLine();
		
		Usuario u = new Usuario();
		
		validar=u.logearse(correo, pass,db);
		
		if(validar){
			
			System.out.println("Se ha logeado con existo");
			menuUsuario(db, u);
			
			
		}else{
			
			System.out.println("No conincide el correo o la password");
			
		}
	}
	
	public void elimnarUsuario(){
		
		
		Usuario u = new Usuario();
		
		u.getNombre();
		
		
		
		
	}
	
	public static void menuUsuario(DB db,Usuario u){
		
		

		int menu=-1;
		
		do{
			
			try{
				
			System.out.println("====USUARIO===");
			System.out.println("Menu Grupos");
			System.out.println("Darse de baja");
			System.out.println("Cerrar sesion");
			System.out.println("Introduzca una opcion:");
			menu=Integer.parseInt(leer.nextLine());
			
		switch(menu){
		
		case 1:
			
			menuGrupos(db);
			
			break;
			
		case 2:
			
			
			break;
			
		case 0:
			
			System.out.println("Cerrando sesion del usuario");
			
			break;
			
		default:
			
			System.out.println("La opcion no es valida");
			
			break;
		
		}
			}catch(NumberFormatException e){
				
				System.out.println("No has introducido un caracter valido");
			
			}
			
		}while(menu!=0);
		
		
		
	}
	
	public static void menuGrupos(DB db){
		
		int menu=-1;
		
		do{
			try{
		
		System.out.println("===GESTION GRUPOS===");
		System.out.println("1.-Crear grupo");
		System.out.println("2.-Unirse a grupo");
		System.out.println("3.-Salir del grupo");
		System.out.println("4.-Crear comentario");
		System.out.println("5.-Visualizar comentarios");
		System.out.println("6.-Listar usuarios localidad");
		System.out.println("0.-Volver al menu usuario");
		System.out.println("Seleccione una opcion valida");
		menu=Integer.parseInt(leer.nextLine());
		
		switch(menu){
		
		case 1:
			
			break;
			
		case 2:
			
			break;
			
		case 3:
			
			break;
			
		case 4:
			
			break;
			
		case 5:
			
			break;
			
		case 6:
			
			break;
			
		case 0:
			
			System.out.println("Volviendo al menu usuario \n");
			
			break;
			
		default:
			
			System.out.println("Opcion seleccionada invalida \n");
			
			break;
		
		}
			}catch(NumberFormatException e){
			
			System.out.println("No ha introducido un caracter valido");
			
		}
		
	}while(menu!=0);
		
	}
	

}
