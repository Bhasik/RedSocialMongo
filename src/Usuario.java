import java.util.ArrayList;
import java.util.Arrays;

import org.bson.types.ObjectId;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.Mongo;

public class Usuario {

	private String nombre, apellidos, correo, pass, direccion;
	private ObjectId id;
	private ArrayList<String> grupos = new ArrayList<String>();
	private int i;
	
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

				this.nombre = usuario.get("nombre").toString();
				this.apellidos = usuario.get("apellidos").toString();
				this.correo = usuario.get("correo").toString();
				this.pass = usuario.get("pass").toString();
				this.direccion = usuario.get("direccion").toString();

				return true;
			}

			return false;

		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;

	}

	public void eliminarUsuario(DB db) {

		BasicDBObject query = new BasicDBObject("_id", this.id);

		this.collection = db.getCollection("usuario");
		this.collection.remove(query);

	}
	
	public void insertarGrupo(DB db,String nombre){
		
		grupos.add(nombre);
		
		DBCollection collection = db.getCollection("usuario");

		BasicDBObject query = new BasicDBObject().append("_id", id);
		
		BasicDBObject insertar = new BasicDBObject();
		insertar.put("$push", new BasicDBObject(
				"Grupo", new BasicDBObject("Nombre", nombre)));
						



		collection.update(query, insertar);
		
		
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

	public String getDireccion() {
		return direccion;
	}

	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}

	public DBCollection getCollection() {
		return collection;
	}

	public void setCollection(DBCollection collection) {
		this.collection = collection;
	}

}
