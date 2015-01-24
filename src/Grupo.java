import java.util.ArrayList;
import java.util.Arrays;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;


public class Grupo {

	private Long id;
	private String nombre;
	private ArrayList<Usuario> usuarios = new ArrayList<Usuario>();
	private ArrayList<String> comentarios = new ArrayList<String>();
	
	public Grupo(){
		
	}

	public Grupo(String nombre,Usuario u,DB db){
		
		BasicDBObject doc = new BasicDBObject();
		doc.put("_id", new Long(1));
        doc.put("nombre", nombre);
        doc.put("usuarios", Arrays.asList(u.getId()));
        doc.put("comentarios", null);
        
        
		DBCollection collection = db.getCollection("");				
		collection.save(doc);
        
		
		
		
	}
	
	
}
