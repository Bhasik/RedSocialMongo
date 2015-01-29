import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

import org.bson.types.ObjectId;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;

public class Grupo {



	private ObjectId id;
	private String nombre;
	private int comentarios=0, usuarios = 1;
	private ArrayList<String> grupos;
	
	public Grupo() {

	}

	public Grupo(String nombre) {

		this.nombre = nombre;

	}

	public void crearGrupo(DB db,Usuario u,String nombre) {
		

		DBCollection collection = db.getCollection("grupo");
	
		Date fecha = new Date();

		BasicDBObject doc = new BasicDBObject();
		doc.put("Nombre", nombre);
		doc.put("Fecha Creacion", fecha);
		doc.put("Total Usuarios", usuarios);
		doc.put("Total Comentarios",comentarios);

		u.insertarGrupo(db,nombre);
		insertarUsuario(db, u, nombre);
		
		collection.save(doc);
		
		
		
			
	}

	public void unirseGrupo(DB db, ObjectId id,Usuario u) {

		DBCollection collection = db.getCollection("grupo");

		this.usuarios++;

		System.out.println("Añadido al grupo \n");
		
		BasicDBObject query = new BasicDBObject().append("_id", id);
		
		BasicDBObject insertar = new BasicDBObject();
		insertar.append("$set",
				new BasicDBObject().append("Total Usuarios", usuarios));

		collection.update(query, insertar);
		
		u.insertarGrupo(db,this.nombre);

	}

	public ObjectId buscarGrupo(DB db, String nombre,Usuario u) {

		ObjectId id;
		
		try {

			DBCollection collection = db.getCollection("grupo");

			BasicDBObject query = new BasicDBObject();
			query.put("Nombre", nombre);
			DBCursor cursor = collection.find(query);

			for (DBObject grupo : cursor) {

				this.nombre = grupo.get("Nombre").toString();
		

				if (!this.nombre.equalsIgnoreCase(nombre)) {

					System.out.println("No se ha encontrado el grupo");
					
					
				} else {
					
					id = (ObjectId) grupo.get("_id");
					return id;
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}
	
	public boolean buscarGrupoNombre(DB db,String nombre){
		
		try{
		
		DBCollection collection = db.getCollection("grupo");

		BasicDBObject query = new BasicDBObject();
		query.put("Nombre", nombre);
		DBCursor cursor = collection.find(query);

		for (DBObject grupo : cursor) {

			this.nombre = grupo.get("Nombre").toString();
	

			if (this.nombre.equalsIgnoreCase(nombre)) {

				return true;
				
			}else{
			
				return false;
				
		}
			
		}

	} catch (Exception e) {
		e.printStackTrace();
	}
		return false;
				
	}
	
	
	public void insertarComentario(DB db,String comentario,String nombre,Usuario u){
				
		
		
		
		ObjectId id;
		Date fecha = new Date();
		this.comentarios++;
		
		DBCollection collection = db.getCollection("grupo");
		
		id=buscarGrupo(db, nombre,u);
		
		BasicDBObject query = new BasicDBObject().append("_id", id);
		
		BasicDBObject insertar = new BasicDBObject();
		
		insertar.put("$push", new BasicDBObject(
				"Comentario", new BasicDBObject("Texto", comentario).append(
						"Id", u.getId()).append("Nombre:",u.getNombre()).append("fecha", fecha)));

		

		collection.update(query,insertar);	
		aumentarComentarios(db, id);
		
	}
	
	public void visualizarComentarios(DB db,ObjectId id){
		
		DBCollection collection = db.getCollection("grupo");
		
		String comentario,nombre;
		Date fecha = new Date();
		
		
		try{
			
			
		BasicDBObject query = new BasicDBObject().append("_id", id);
		
		DBCursor cursor = collection.find(query);

		for (DBObject grupo : cursor) {

			ArrayList<DBObject> comentarios = (ArrayList<DBObject>)grupo.get("comentario");
	
			for (int i=0;i<comentarios.size();i++){
				
				comentario = (String) comentarios.get(i).get("Texto");
				nombre = (String) comentarios.get(i).get("usuario");
				fecha = (Date) comentarios.get(i).get("fecha");
				
				System.out.println("Usuario: " + nombre  + "\n Comentario: " + comentario + "\n Fecha: " + fecha );
				
			}
			
		}

	} catch (Exception e) {
		e.printStackTrace();
	}
	
	
		
	}
	
	
	public void borrarGrupo(DB db,ObjectId id){
		
		

			DBCollection collection = db.getCollection("grupo");

			BasicDBObject query = new BasicDBObject();
			query.put("_id", id);

			collection.remove(query);
		
	}
	
	public void disminuirUsuarios(DB db,ObjectId id){
		
		DBCollection collection = db.getCollection("grupo");
		BasicDBObject query = new BasicDBObject().append("_id", id);
		
		this.usuarios--;
		
		BasicDBObject insertar = new BasicDBObject();
		insertar.append("$set",
				new BasicDBObject().append("Total Usuarios", usuarios));	
		
		collection.update(query, insertar);
		
		
	}
	
	public void aumentarComentarios(DB db , ObjectId id){
		
		
		DBCollection collection = db.getCollection("grupo");
		BasicDBObject query = new BasicDBObject().append("_id", id);
	
		BasicDBObject insertar = new BasicDBObject();
		insertar.append("$set",
				new BasicDBObject().append("Total Comentarios", comentarios));	
		
		collection.update(query, insertar);
		
	}
	
	public void insertarUsuario(DB db,Usuario u,String nombre){
		
		ObjectId id;
		
		DBCollection collection = db.getCollection("grupo");
		
		id=buscarGrupo(db,nombre,u);
		
		BasicDBObject query = new BasicDBObject().append("_id", id);
		
		BasicDBObject insertar = new BasicDBObject();
		
		insertar.put("$push", new BasicDBObject("Usuarios", new BasicDBObject(
				"Usuario", u.getNombre()).append("ID",u.getId())));

		

		collection.update(query,insertar);	
		
		
		
	}
	
	/*public void disminuirComentarios(DB db,ObjectId id){
		
		
		DBCollection collection = db.getCollection("grupo");
		BasicDBObject query = new BasicDBObject().append("_id", id);
		
		comentarios++;
		
		BasicDBObject insertar = new BasicDBObject();
		insertar.append("$set",
				new BasicDBObject().append("Total Comentarios", comentarios));	
		
		collection.update(query, insertar);
		
		
	}*/
	
	public void buscarUsuarios(DB db,Usuario u,String nombre){
		
		ObjectId id;
		
		id = buscarGrupo(db,nombre,u);
		
		DBCollection collection = db.getCollection("grupo");
		
		BasicDBObject query = new BasicDBObject().append("_id", id);
		
		DBCursor cursor = collection.find(query);
		
		for (DBObject grupo : cursor) {

			ArrayList<DBObject> usuarios = (ArrayList<DBObject>)grupo.get("Usuarios");
	
			for (int i=0;i<usuarios.size();i++){
				
				ObjectId idUsuario = (ObjectId) usuarios.get(i).get("ID");
				
				Usuario us = new Usuario();
				us.buscarUsuario(db,idUsuario);
				
				if(u.getDireccion()[2].equals(us.getDireccion()[2])){
					
					System.out.println("Usuario: " + us.getNombre() + " - Correo: "
							+ us.getCorreo());
					
				}

				
			}
			

			
		}


		
		
	}
	
	
	public ObjectId getId() {
		return id;
	}

	public void setId(ObjectId id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public int getComentarios() {
		return comentarios;
	}

	public void setComentarios(int comentarios) {
		
		this.comentarios = comentarios;
	}

	public int getUsuarios() {
		
		return usuarios;
		
	}

	public void setUsuarios(int usuarios) {
		
		this.usuarios = usuarios;
		
	}
	
}
