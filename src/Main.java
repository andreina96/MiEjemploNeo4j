/*
 * Creado por: Andreína Alvarado González 
 * Fecha: 23 de septiembre del 2016
 * 
 * Clase que contiene métodos para crear un base de datos en Neo4j y, consultar y eliminar datos
 */

import java.io.File;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.neo4j.graphdb.Label;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.RelationshipType;
import org.neo4j.graphdb.Transaction;
import org.neo4j.jdbc.ResultSet;

public class Main {
	
	/*
	 * enum para nombrar las etiquetas que tendrán los nodos
	 */
	public enum nodos implements Label{
		VIDEOJUEGOS, PERSONAJES;
	}
	
	/*
	 * enum para nombrar los tipos que tendrán las relaciones
	 */
	public enum relaciones implements RelationshipType{
		CUENTA_CON, ES_FAMILIAR;
	}
	
	public void crearDb(){
		/*
		 * Se crea la base de datos en la ruta especificada
		 */
		GraphDatabaseFactory dbFactory = new GraphDatabaseFactory();
		GraphDatabaseService gdb =dbFactory.newEmbeddedDatabase(
							new File("C:/Users/Andreína Alvarado/eclipse/"
								   + "workspace/MiEjemploNeo4j/dB"));
	
		/*
		 * Se crea una transacción de base de datos
		 */
		try(Transaction tx = gdb.beginTx()){
			/*
			 * Se crea un nodo con etiqueta videojuego
			 */
			Node gow1 = gdb.createNode(nodos.VIDEOJUEGOS);
			gow1.setProperty("Nombre", "God of War I");
			gow1.setProperty("Desarrolladora", "Santa Monica Studios");
			gow1.setProperty("Distribuidora", "Sony");
			
			/*
			 * Se crean nodos con etiqueta personajes
			 */
			Node kratos = gdb.createNode(nodos.PERSONAJES);
			kratos.setProperty("Nombre", "Kratos");
			
			Node zeus = gdb.createNode(nodos.PERSONAJES);
			zeus.setProperty("Nombre", "Zeus");
			
			Node ares = gdb.createNode(nodos.PERSONAJES);
			ares.setProperty("Nombre", "Ares");
			
			/*
			 * Se crean relaciones de tipo cuenta_con entre nodo videojuego y los nodos personaje
			 */
			Relationship gow1tokratos = gow1.createRelationshipTo(kratos, relaciones.CUENTA_CON); 
			gow1tokratos.setProperty("Como", "Personaje principal");
			
			Relationship gow1tozeus = gow1.createRelationshipTo(zeus, relaciones.CUENTA_CON); 
			gow1tozeus.setProperty("Como", "Personaje secundario");
			
			Relationship gow1toares = gow1.createRelationshipTo(ares, relaciones.CUENTA_CON); 
			gow1toares.setProperty("Como", "Personaje recurrente");
			
			/*
			 * Se crean relaciones de tipo es_familiar entre nodos personaje
			 */
			Relationship kratostozeus = kratos.createRelationshipTo(zeus, relaciones.ES_FAMILIAR);
			kratostozeus.setProperty("Parentesco", "Hijo");
			
			Relationship kratostoares = kratos.createRelationshipTo(ares, relaciones.ES_FAMILIAR);
			kratostoares.setProperty("Parentesco", "Hermano");
			
			Relationship arestozeus = ares.createRelationshipTo(zeus, relaciones.ES_FAMILIAR);
			arestozeus.setProperty("Parentesco", "Hijo");
			
			tx.success();
		}
		catch(Exception ex)
		{
		    System.out.println(ex);
		}
	}
	
	public static void main(String[] args) throws SQLException{
		/*
		 * Descomentar cuando se ocupe crear la base de datos
		 * Main m = new Main();
		 * m.crearDb();
		 *
		*/
		
		/*
		 * Se establece una conexión con la base de datos
		 */
		/*
		 * Objeto para imprimir gráficamente la consulta
		 */
		dibujarGraphDb d = new dibujarGraphDb();
		
		/*
		 * Se iguala la variable query a una consulta
		 */
		//String query = "MATCH (a)-[r:CUENTA_CON]->(b) RETURN a.Nombre,r.Como,b.Nombre";
		
		/* Se establece una conexión con la base de datos */
		Connection con = DriverManager.getConnection("jdbc:neo4j:http://localhost:7474", "neo4j", "1234");
		
		/* Se crean nodos para la base de datos */
		String gow1 = "CREATE (a:VIDEOJUEGO { Nombre : \"God of War I\"})";
		String kratos = "CREATE (a:VIDEOJUEGO { Nombre : \"God of War I\"})";
		String zeus = "CREATE (a:VIDEOJUEGO { Nombre : \"God of War I\"})";
		
		/* Se crean relaciones para la base de datos */
		String gowtokratos = "CREATE (a:VIDEOJUEGO { Nombre : \"God of War I\" })-" +
									"[r:CUENTA_CON { Como : \"Personaje principal\" }]->" +
									"(b:PERSONAJE { Nombre : \"Kratos })\"";
		
		String kratostozeus = "CREATE (a:PERSONAJE { Nombre : \"Kratos\" })-" +
				 					"[r:ES_FAMILIAR { Parentesco : \"hijo\" }]->" +
				 					"(b:PERSONAJE { Nombre : \"Zeus })\"";
		
		/*
		 * Se ejecutan agregan los nodos y relaciones en la base de datos
		 */
		PreparedStatement stmtgow1 = con.prepareStatement(gow1);
		stmtgow1.executeQuery();
		PreparedStatement stmtkratos = con.prepareStatement(kratos);
		stmtkratos.executeQuery();
		PreparedStatement stmtzeus = con.prepareStatement(zeus);
		stmtzeus.executeQuery();
		PreparedStatement stmtgowtokratos = con.prepareStatement(gowtokratos);
		stmtgowtokratos.executeQuery();
		PreparedStatement stmtkratostozeus = con.prepareStatement(kratostozeus);
		stmtkratostozeus.executeQuery();
		
		/*
		 * Se muestra el resultado de las inserciones en forma visual 
		 */
		d.mostrar();
		
		/* Se establece una consulta para la base de datos */
		String consulta = "MATCH (a)-[r:CUENTA_CON]->(b) " +
						  "RETURN a.Nombre,r.Como,b.Nombre";
		
		/* Se ejecuta la consulta */
		try(PreparedStatement stmt = con.prepareStatement(consulta)){
			try(ResultSet rs = (ResultSet)stmt.executeQuery()){
				
				/* Se itera por el resultado de la consulta, imprimiendo los valores retornados*/
				while(rs.next()){
					System.out.println(rs.getString("a.Nombre") + 
									   " cuenta con " + 
									   rs.getString("b.Nombre") +
									   " como " +
									   rs.getString("r.Como"));
				}
			}
			catch(Exception ex)
			{
			    System.out.println(ex);
			}
		}
		catch(Exception ex)
		{
		    System.out.println(ex);
		}		
	}
}

/*d.dibujarRelacion(rs.getString("a.Nombre") + rs.getString("b.Nombre"), 
									  "es " + rs.getString("r.Como") + " en",
									  rs.getString("a.Nombre"),
									  rs.getString("b.Nombre"));*/