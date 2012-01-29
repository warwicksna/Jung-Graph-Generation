import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.SparseMultigraph;
import edu.uci.ics.jung.graph.util.EdgeType;
import edu.uci.ics.jung.algorithms.generators.random.BarabasiAlbertGenerator;
import org.apache.commons.collections15.functors.ConstantFactory;
import edu.uci.ics.jung.graph.SparseGraph;
import java.util.HashSet;
import org.apache.commons.collections15.functors.InstantiateFactory;
import edu.uci.ics.jung.io.GraphMLWriter;
import java.io.*;

public class BasicGraphCreation {        
    public static void main(String[] args) throws Exception{
	SparseGraph<MyVertex, MyEdge> cake = new SparseGraph<MyVertex, MyEdge>();
	ConstantFactory<Graph<MyVertex, MyEdge>> graphFactory = new ConstantFactory<Graph<MyVertex,MyEdge>>(cake);
	InstantiateFactory<MyVertex> vertexFactory = new InstantiateFactory<MyVertex>(MyVertex.class);
	InstantiateFactory<MyEdge> edgeFactory = new InstantiateFactory<MyEdge>(MyEdge.class);
	HashSet<MyVertex> seedVertices = new HashSet<MyVertex>();
	BarabasiAlbertGenerator<MyVertex,MyEdge> gen = new BarabasiAlbertGenerator<MyVertex,MyEdge>(graphFactory, vertexFactory, edgeFactory, 10, 2, seedVertices);
	gen.evolveGraph(5);
	Graph g = gen.create();
	
        GraphMLWriter<MyVertex, MyEdge> graphWriter = new GraphMLWriter<MyVertex, MyEdge>(); 
        PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter("foo.graphml")));
        graphWriter.save(g, out);
        System.out.println("The graph g = " + g.toString());    
    }
}

