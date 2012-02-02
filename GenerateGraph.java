import peersim.graph.BitMatrixGraph;
import peersim.graph.GraphFactory;
import peersim.graph.GraphIO;
import java.util.Random;
import java.io.*;


public class GenerateGraph {       
    public static void main(String[] args) throws Exception{
	if(args.length != 4){
	  System.out.println("GenerateGraph <numberOfNodes> <neighbourhoodsize> <directed> <outputPath>");
	  return;
	}
	
	final int vertices = Integer.parseInt(args[0]);//100;
	final int degree = Integer.parseInt(args[1]);
	final boolean directed = Boolean.parseBoolean(args[2]);//~10% out when undirected
	final String outputPath = args[3];
	
	BitMatrixGraph g = new BitMatrixGraph(vertices, directed);
	GraphFactory.wireKOut(g, degree, new Random());
	int size = g.size();
	int e = 0;
	int edge = 0;
	String nodes = "";
	String edges = "";
	for(int i = 0; i<size; i++){
	  nodes+="<node id='n"+i+"'/>\n";
	  for(int k = 0; k<=size; k++){
	      if(g.isEdge(i,k) && (directed || i<k)){
		e++;
		edges+="<edge id='e"+e+"' source='n"+i+"' target='n"+k+"'/>\n";
	      }
	  }
	}
	String graphml = "<graphml edgedefault='undirected'>\n<graph>\n" + nodes + edges + "</graph>\n</graphml>";
	
     // int miss = (degree * vertices) - e;
     //  System.out.println(miss);
       
       BufferedWriter out = new BufferedWriter(new FileWriter(outputPath));
       out.write(graphml);
       out.close();
       String dir;
       if(directed){
	dir = "directed";
       }
       else {dir = "undirected";}
       System.out.println("Wrote "+dir+" graph with "+vertices+" nodes and "+e+" edges to "+outputPath);
       return;
    }
}
/*
10:50
5:15
1:0.5
*/
