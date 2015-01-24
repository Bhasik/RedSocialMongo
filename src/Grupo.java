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
	private static int comentarios=0, usuarios = 1;

	public Grupo() {

	}

	public Grupo(String nombre) {

		this.nombre = nombre;

	}

	public void crearGrupo(DB db,Usuario u) {

	
		Date fecha = new Date();

		BasicDBObject doc = new BasicDBObject();
		doc.put("nombre", this.nombre);
		doc.put("Fecha Creacion", fecha);
		doc.put("Total Usuarios", usuarios);
		doc.put("Total Comentario",comentarios);

		DBCollection collection = db.getCollection("grupo");
		collection.save(doc);

		u.insertarGrupo(db,this.nombre);
	}

	public void unirseGrupo(DB db, ObjectId id,Usuario u) {

		DBCollection collection = db.getCollection("grupo");

		usuarios++;

		System.out.println("Añadido al grupo");
		BasicDBObject query = new BasicDBObject().append("_id", id);
		
		BasicDBObject insertar = new BasicDBObject();
		insertar.append("$set",
				new BasicDBObject().append("Total Usuarios", usuarios));

	
		u.insertarGrupo(db, nombre);
		collection.update(query, insertar);

	}

	public ObjectId buscarGrupo(DB db, String nombre,Usuario u) {

		ObjectId id;
		try {

			DBCollection collection = db.getCollection("grupo");

			BasicDBObject query = new BasicDBObject();
			query.put("nombre", nombre);
			DBCursor cursor = collection.find(query);

			for (DBObject grupo : cursor) {

				this.nombre = grupo.get("nombre").toString();
		

				if (!this.nombre.equalsIgnoreCase(nombre)) {

					System.out.println("No se ha encontrado el grupo");
					
					
				} else {
					
					id = (ObjectId) grupo.get("_id");
					u.insertarGrupo(db,this.nombre);
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
				"comentario", new BasicDBObject("Texto", comentario).append(
						"usuario", u.getNombre()).append("fecha", fecha)));

		

		collection.update(query,insertar);	
		
	}
}
