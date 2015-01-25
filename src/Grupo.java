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
	private static int comentarios=0, usuarios = 0;

	public Grupo() {

	}

	public Grupo(String nombre) {

		this.nombre = nombre;

	}

	public void crearGrupo(DB db,Usuario u) {
		

		DBCollection collection = db.getCollection("grupo");
	
		Date fecha = new Date();

		BasicDBObject doc = new BasicDBObject();
		doc.put("Nombre", this.nombre);
		doc.put("Fecha Creacion", fecha);
		
		doc.put("Total Usuarios", usuarios);
		doc.put("Total Comentarios",comentarios);

		collection.save(doc);

		u.insertarGrupo(db,this.nombre);
		
		
	}

	public void unirseGrupo(DB db, ObjectId id,Usuario u) {

		DBCollection collection = db.getCollection("grupo");

		usuarios++;

		System.out.println("Añadido al grupo \n");
		BasicDBObject query = new BasicDBObject().append("_id", id);
		
		BasicDBObject insertar = new BasicDBObject();
		insertar.append("$set",
				new BasicDBObject().append("Total Usuarios", usuarios));

	
		u.insertarGrupo(db,this.nombre);
		collection.update(query, insertar);
		

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
	
	
	public void insertarComentario(DB db,String comentario,String nombre,Usuario u){
				
		ObjectId id;
		Date fecha = new Date();
		comentarios++;
		
		DBCollection collection = db.getCollection("grupo");
		
		id=buscarGrupo(db, nombre,u);
		
		BasicDBObject query = new BasicDBObject().append("_id", id);
		
		BasicDBObject insertar = new BasicDBObject();
		
		insertar.put("$push", new BasicDBObject(
				"Comentario", new BasicDBObject("Texto", comentario).append(
						"Usuario", u.getCorreo()).append("fecha", fecha)));

		

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
		
			int numUsuarios;

			DBCollection collection = db.getCollection("grupo");

			BasicDBObject query = new BasicDBObject();
			query.put("_id", id);
			
			DBCursor cursor = collection.find(query);

			for (DBObject grupo : cursor) {

				numUsuarios = (int) grupo.get("Total Usuarios");
				
				if(numUsuarios == 0){
					
					collection.remove(query);
					
				}

			}

	}
	
	public void disminuirUsuarios(DB db,ObjectId id){
		
		DBCollection collection = db.getCollection("grupo");
		BasicDBObject query = new BasicDBObject().append("_id", id);
		
		usuarios--;
		
		BasicDBObject insertar = new BasicDBObject();
		insertar.append("$set",
				new BasicDBObject().append("Total Usuarios", usuarios));	
		
		collection.update(query, insertar);
		
		borrarGrupo(db,id);
		
	}
	
	public void aumentarComentarios(DB db , ObjectId id){
		
		
		DBCollection collection = db.getCollection("grupo");
		BasicDBObject query = new BasicDBObject().append("_id", id);
	
		BasicDBObject insertar = new BasicDBObject();
		insertar.append("$set",
				new BasicDBObject().append("Total Comentarios", comentarios));	
		
		collection.update(query, insertar);
		
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
	
}
