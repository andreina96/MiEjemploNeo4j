import java.util.Random;

import org.graphstream.graph.*;
import org.graphstream.graph.implementations.SingleGraph;

public class dibujarGraphDb {

	Graph graph;

	/* Constructor de la clase, que le da un nombre a la ventana en 
	 * la que se mostrar� el grafo */
	public dibujarGraphDb(){
		graph = new SingleGraph("Consulta");
	}
	
	/* M�todo para agregar un nodo al grafo con un nombre espec�fico */
	public void dibujarNodo(String nombre){
		graph.addNode(nombre);
	}
	
	/* M�todo para agregar una relaci�n al grafo con un nombre espec�fico */
	public void dibujarRelacion(String relacion, String nombreRelacion, String nodo1, String nodo2){
		graph.setStrict(false);
        graph.setAutoCreate(true);
		graph.addEdge(relacion, nodo1, nodo2, true);
		graph.getEdge(relacion).addAttribute("ui.label", nombreRelacion);
	}
	
	
	/* M�todo que muestra el grafo creado */
	public void mostrar(){
		graph.addAttribute("ui.stylesheet", "node {text-style : bold; size-mode: dyn-size; shape: box;fill-color: blue, green, red; text-mode: normal; text-background-mode: rounded-box; fill-mode: dyn-plain;}");
		
		for(Node node : graph) {
			Random rnd = new Random();
			/* Le asigna un nombre a cada nodo en el grafo */
	        node.addAttribute("ui.label", node.getId());
	        /* Le asigna un color aleatorio a cada nodo en el grafo */
	        node.addAttribute("ui.color",  rnd.nextFloat());
	        /* Le asgina un tama�o de 15 a cada nodo en el grafo */
	        node.addAttribute("ui.size", 15);
	    }
		
		graph.display();
	}
}
