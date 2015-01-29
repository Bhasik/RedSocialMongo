import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

import org.bson.types.ObjectId;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.Mongo;

public class Usuario {

	public ObjectId getId() {
		return id;
	}

	public void setId(ObjectId id) {
		this.id = id;
	}

	private String nombre, apellidos, correo, pass;
	private ObjectId id;
	private int i;
	private String[] direccion;
	private DBCollection collection;

	public Usuario() {

	}

	public Usuario(String nombre, String apellidos, String correo, String pass,
			String[] direccion, DB db) {

		BasicDBObject doc = new BasicDBObject();
		doc.put("nombre", nombre);
		doc.put("apellidos", apellidos);
		doc.put("correo", correo);
		doc.put("pass", pass);
		doc.put("direccion",
				new BasicDBObject("calle", direccion[0])
						.append("numero", direccion[1])
						.append("localidad", direccion[2])
						.append("codigo postal", direccion[3]));

		DBCollection collection = db.getCollection("usuario");
		
		collection.save(doc);

	}

	public boolean logearse(String correo, String pass, DB db) {

		try {

			DBCollection collection = db.getCollection("usuario");

			BasicDBObject query = new BasicDBObject();
			query.put("correo", correo);
			query.put("pass", pass);
			DBCursor cursor = collection.find(query);

			for (DBObject usuario : cursor) {

				this.id = (ObjectId) usuario.get("_id");
				this.nombre = usuario.get("nombre").toString();
				this.apellidos = usuario.get("apellidos").toString();
				this.correo = usuario.get("correo").toString();
				this.pass = usuario.get("pass").toString();
				
				/*DBObject direccion = (DBObject) usuario.get("direccion");
				
				this.direccion[0] = (String)direccion.get("calle");
				this.direccion[1] = (String)direccion.get("numero");
				this.direccion[2] = (String)direccion.get("localidad");
				this.direccion[3] = (String)direccion.get("codigo postal");*/
				
				return true;
			}

			return false;

		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;

	}

	public int eliminarUsuario(DB db) {

		BasicDBObject query = new BasicDBObject("_id", this.id);

		this.collection = db.getCollection("usuario");
		this.collection.remove(query);

		return 0;

	}

	public void insertarGrupo(DB db, String nombre) {
		
		DBCollection collection = db.getCollection("usuario");

		BasicDBObject query = new BasicDBObject().append("_id", this.id);

		BasicDBObject insertar = new BasicDBObject();

		insertar.put("$push", new BasicDBObject("Grupo", new BasicDBObject(
				"Nombre", nombre)));

		collection.update(query, insertar);

	}

	public void salirGrupo(DB db, String nombre) {
		
		DBCollection collection = db.getCollection("usuario");

		BasicDBObject query = new BasicDBObject().append("_id",this.id);

		BasicDBObject insertar = new BasicDBObject();

		insertar.put("$pull", new BasicDBObject("Grupo", new BasicDBObject(
				"Nombre", nombre)));
		
		collection.update(query, insertar);

	}
	
	
	public void sacarGrupo(DB db){
		
		
		this.collection = db.getCollection("usuario");
		
		BasicDBObject query = new BasicDBObject().append("_id",this.id);
		
		DBCursor cursor = collection.find(query);
		
		String nombre;
		ArrayList<String> gruposUsu = new ArrayList<String>();
		
	
		for(DBObject usuario : cursor){
			
			System.out.println("Grupos a los que perteneces:");
			ArrayList<DBObject> grupos = (ArrayList<DBObject>) usuario.get("Grupo");
			
			
			
			
		
		for(int i=0;i< grupos.size();i++){
			
			nombre = (String) grupos.get(i).get("Nombre");
			
			System.out.println(nombre);
			
		}
		
		}
		
		
	}
	
	
	public void buscarUsuario(ObjectId id){
		
		System.out.println("Nombre: " + this.nombre);
		System.out.println("Correo: " + this.correo);
		System.out.println("Direccion: ");
		System.out.println("Calle: " + this.direccion[0]);
		System.out.println("Numero: " + this.direccion[1]);
		System.out.println("Localidad: " + this.direccion[2]);
		System.out.println("Codigo Postal: " + this.direccion[3]);
	
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getApellidos() {
		return apellidos;
	}

	public void setApellidos(String apellidos) {
		this.apellidos = apellidos;
	}

	public String getCorreo() {
		return correo;
	}

	public void setCorreo(String correo) {
		this.correo = correo;
	}

	public String getPass() {
		return pass;
	}

	public void setPass(String pass) {
		this.pass = pass;
	}

	public DBCollection getCollection() {
		return collection;
	}

	public void setCollection(DBCollection collection) {
		this.collection = collection;
	}

}
