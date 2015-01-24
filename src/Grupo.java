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
	private static int comentarios, usuarios = 1;

	public Grupo() {

	}

	public Grupo(String nombre) {

		this.nombre = nombre;

	}

	public void crearGrupo(DB db) {

		Date fecha = new Date();

		BasicDBObject doc = new BasicDBObject();
		doc.put("nombre", this.nombre);
		doc.put("Fecha Creacion", fecha);
		doc.put("Total Usuarios", usuarios);

		DBCollection collection = db.getCollection("grupo");
		collection.save(doc);

	}

	public void unirseGrupo(DB db, ObjectId id) {

		DBCollection collection = db.getCollection("grupo");

		usuarios++;

		System.out.println("Añadido al grupo");

		BasicDBObject put = new BasicDBObject();
		put.append("$set",
				new BasicDBObject().append("Total Usuarios", usuarios));

		BasicDBObject query2 = new BasicDBObject().append("_id", id);

		collection.update(query2, put);

	}

	public ObjectId buscarGrupo(DB db, String nombre) {

		ObjectId id;

		try {

			DBCollection collection = db.getCollection("grupo");

			BasicDBObject query = new BasicDBObject();
			query.put("nombre", nombre);
			DBCursor cursor = collection.find(query);

			for (DBObject grupo : cursor) {

				this.nombre = grupo.get("nombre").toString();
				id = (ObjectId) grupo.get("_id");

				if (this.nombre.equalsIgnoreCase(nombre)) {

					return id;

				} else {

					System.out
							.println("No se ha encontrado ningun grupo con ese nombre");

					break;
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}
}
