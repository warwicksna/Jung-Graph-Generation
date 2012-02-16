// Jung types
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.SparseGraph;
import edu.uci.ics.jung.graph.UndirectedGraph;
import edu.uci.ics.jung.graph.UndirectedSparseGraph;

// Jung generators
import edu.uci.ics.jung.algorithms.generators.random.ErdosRenyiGenerator;
import edu.uci.ics.jung.algorithms.generators.random.BarabasiAlbertGenerator;
import edu.uci.ics.jung.algorithms.generators.random.KleinbergSmallWorldGenerator;

// Collection factories
import org.apache.commons.collections15.functors.ConstantFactory;
import org.apache.commons.collections15.functors.InstantiateFactory;

// Java imports
import java.util.Set;
import java.util.HashSet;

public class GraphFactory
{
	protected static InstantiateFactory undirectedGraphFactory = new InstantiateFactory(UndirectedSparseGraph.class);
		
	protected static InstantiateFactory<MyVertex> vertexFactory = new InstantiateFactory<MyVertex>(MyVertex.class);
	protected static InstantiateFactory<MyEdge> edgeFactory = new InstantiateFactory<MyEdge>(MyEdge.class);
	
	protected static int[] targetPopulationSizes = {100, 200, 300, 400, 500};
	protected static float[] targetVertexDegrees = {1, 2, 3, 4, 5, 10, 15, 20, 25};//, 50, 100};
	
	public static void main(String[] args)
	{
		Graph graph;
		
		// System.out.println("Wanted\t\t\t\tFound\t\t\t\tDifference\n============================================================================");
		// 
		// for (int targetPopulationSizeIndex = 0; targetPopulationSizeIndex < targetPopulationSizes.length; targetPopulationSizeIndex++)
		// {
		// 	for (int targetVertexDegreeIndex = 0; targetVertexDegreeIndex < targetVertexDegrees.length; targetVertexDegreeIndex++)
		// 	{
		// 		int targetPopulationSize = targetPopulationSizes[targetPopulationSizeIndex];
		// 		float targetVertexDegree = targetVertexDegrees[targetVertexDegreeIndex];
		// 		
		// 		graph = getRandomGraph(targetPopulationSize, targetVertexDegree);
		// 		
		// 		System.out.format("(|V| = %-4d, |E| = %-6.0f)\t(|V| = %-4d, |E| = %-6d)\t%.0f\t%-3.1f\n", targetPopulationSize, targetVertexDegree * targetPopulationSize, graph.getVertexCount(), graph.getEdgeCount(), Math.abs(targetVertexDegree * targetPopulationSize - graph.getEdgeCount()), Math.abs(targetVertexDegree * targetPopulationSize - graph.getEdgeCount()) / (targetVertexDegree * targetPopulationSize) * 100);
		// 	}
		// }
		
		// for (int targetPopulationSizeIndex = 0; targetPopulationSizeIndex < targetPopulationSizes.length; targetPopulationSizeIndex++)
		// {
		// 	for (int targetVertexDegreeIndex = 0; targetVertexDegreeIndex < targetVertexDegrees.length; targetVertexDegreeIndex++)
		// 	{
		// 		int targetPopulationSize = targetPopulationSizes[targetPopulationSizeIndex];
		// 		int targetVertexDegree = (int) targetVertexDegrees[targetVertexDegreeIndex];
		// 		
		// 		graph = getScaleFreeGraph(targetPopulationSize, targetVertexDegree * targetPopulationSize);
		// 		
		// 		System.out.format("|V| = %d, |E| = %d\n", graph.getVertexCount(), graph.getEdgeCount());				
		// 	}
		// }
		
		for (int targetPopulationSizeIndex = 0; targetPopulationSizeIndex < targetPopulationSizes.length; targetPopulationSizeIndex++)
		{
			for (int targetVertexDegreeIndex = 0; targetVertexDegreeIndex < targetVertexDegrees.length; targetVertexDegreeIndex++)
			{
				int targetPopulationSize = targetPopulationSizes[targetPopulationSizeIndex];
				int targetVertexDegree = (int) targetVertexDegrees[targetVertexDegreeIndex];
				
				graph = getSmallWorldGraph(targetPopulationSize, targetVertexDegree * targetPopulationSize);
				
				System.out.format("|V| = %d, |E| = %d\n", graph.getVertexCount(), graph.getEdgeCount());				
			}
		}
	}

	public static Graph getRandomGraph(int targetPopulationSize, float targetVertexDegree)
	{
		// work out probability of a connection
		double connectionProbability = targetVertexDegree / ((float) targetPopulationSize) * 2.0;
		
		// clamp the probability to the range [0, 1]
		connectionProbability = (connectionProbability > 1) ? 1 : connectionProbability;
		connectionProbability = (connectionProbability < 0) ? 0 : connectionProbability;
		
		// create a graph generator
		ErdosRenyiGenerator<MyVertex, MyEdge> generator = new ErdosRenyiGenerator<MyVertex, MyEdge>(undirectedGraphFactory, vertexFactory, edgeFactory, targetPopulationSize, connectionProbability);
		
		// create the graph
		return generator.create();
	}
	
	// formulas:
	// |V| = i + t
	// |E| = et
	// where |V| = num vertices, |E| = num edges, i = initial vertices, t = timesteps, e = edges to add per timestep
	//
	// if we assume:
	// i = 0.5|V|
	// t = 0.5|V|
	// then:
	// e = 2|E| / |V|
	public static Graph getScaleFreeGraph(int targetPopulationSize, int targetEdgeCount)
	{
		int initialVertexCount = (int) Math.round(((float) targetPopulationSize) * 0.5);
		int timesteps = targetPopulationSize - initialVertexCount;
		
		int edgesToAddPerTimestep = (int) Math.round((2.0 * ((float) targetEdgeCount)) / ((float) targetPopulationSize));
		
		// create a graph generator
		SparseGraph<MyVertex, MyEdge> graph = new SparseGraph<MyVertex, MyEdge>();
		ConstantFactory graphFactory = new ConstantFactory(graph);
		Set seedVertices = new HashSet<MyVertex>();
		BarabasiAlbertGenerator<MyVertex, MyEdge> generator = new BarabasiAlbertGenerator<MyVertex, MyEdge>(graphFactory, vertexFactory, edgeFactory, initialVertexCount, edgesToAddPerTimestep, seedVertices);
		
		// create the graph using evolution
		generator.evolveGraph(timesteps);
		
		return generator.create();
	}
	
	// a = 2 (if graph is undirected)
	// a = 4 (if graph is directed)
	//
	// |E| = a|V| + c|V|
	// where:
	// c = number of long distance connections
	// c = (|E| - a|V|) / |V|
	//
	// |V| = l * l
	// where:
	// l = lattice size
	// l = sqrt(|V|);
	public static Graph getSmallWorldGraph(int targetPopulationSize, int targetEdgeCount)
	{
		int numberOfLongDistanceConnections = (int) Math.round((targetEdgeCount - targetPopulationSize) / targetPopulationSize);
		numberOfLongDistanceConnections = (numberOfLongDistanceConnections < 1) ? 1 : numberOfLongDistanceConnections;
		
		int latticeSize = (int) Math.round(Math.sqrt(targetPopulationSize));
		
		System.out.format("SmallWorldGraph targets: (p = %d, e = %d) latticeSize: %d connections: %d\n", targetPopulationSize, targetEdgeCount, latticeSize, numberOfLongDistanceConnections);
		
		KleinbergSmallWorldGenerator<MyVertex, MyEdge> generator = new KleinbergSmallWorldGenerator<MyVertex, MyEdge>(undirectedGraphFactory, vertexFactory, edgeFactory, latticeSize, 2.0);
		generator.setConnectionCount(numberOfLongDistanceConnections);
		
		return generator.create();
	}
}