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
		
		System.out.println("Random Graph\n============\n");
		
		System.out.println("Target\t\t\t\t| Actual\t\t\t| Difference");
		System.out.println("|V|\t\t|E|\t\t| |V|\t\t|E|\t\t| #\t\t%");
		System.out.println("--------------------------------+-------------------------------+--------------------------------");
		
		for (int targetPopulationSizeIndex = 0; targetPopulationSizeIndex < targetPopulationSizes.length; targetPopulationSizeIndex++)
		{
			for (int targetVertexDegreeIndex = 0; targetVertexDegreeIndex < targetVertexDegrees.length; targetVertexDegreeIndex++)
			{
				int targetPopulationSize = targetPopulationSizes[targetPopulationSizeIndex];
				int targetVertexDegree = (int) targetVertexDegrees[targetVertexDegreeIndex];
				int targetEdgeCount = targetVertexDegree * targetPopulationSize;
				
				graph = getRandomGraph(targetPopulationSize, targetEdgeCount);
								
				int actualPopulationSize = graph.getVertexCount();
				int actualEdgeCount = graph.getEdgeCount();
				
				int edgeCountDifference = (int) Math.round(Math.abs(targetEdgeCount - actualEdgeCount));
				int edgeCountPercentageDifference = (int) Math.round((((float) edgeCountDifference) / ((float) targetEdgeCount)) * 100.0);
				System.out.format("%8d\t%8d\t| %8d\t%8d\t| %8d\t%8d\n", targetPopulationSize, targetEdgeCount, actualPopulationSize, actualEdgeCount, edgeCountDifference, edgeCountPercentageDifference);				
			}
			
			if (targetPopulationSizeIndex == targetPopulationSizes.length - 1)
			{
				System.out.println("=================================================================================================\n");
			}
			else
			{
				System.out.println("--------------------------------+-------------------------------+--------------------------------");
			}
		}
		
		System.out.println("Scale-Free Graph\n================\n");
		System.out.println(" + initial number of vertices = 0.5 * |V|");
		System.out.println(" +        number of timesteps = 0.5 * |V|");
		System.out.println(" +  edges to add per timestep = (2 * |E|) / |V|\n");
		
		System.out.println("Target\t\t\t\t| Actual\t\t\t| Difference");
		System.out.println("|V|\t\t|E|\t\t| |V|\t\t|E|\t\t| #\t\t%");
		System.out.println("--------------------------------+-------------------------------+--------------------------------");
		
		for (int targetPopulationSizeIndex = 0; targetPopulationSizeIndex < targetPopulationSizes.length; targetPopulationSizeIndex++)
		{
			for (int targetVertexDegreeIndex = 0; targetVertexDegreeIndex < targetVertexDegrees.length; targetVertexDegreeIndex++)
			{
				int targetPopulationSize = targetPopulationSizes[targetPopulationSizeIndex];
				int targetVertexDegree = (int) targetVertexDegrees[targetVertexDegreeIndex];
				int targetEdgeCount = targetVertexDegree * targetPopulationSize;
				
				graph = getScaleFreeGraph(targetPopulationSize, targetVertexDegree * targetPopulationSize);
				
				int actualPopulationSize = graph.getVertexCount();
				int actualEdgeCount = graph.getEdgeCount();
				
				int edgeCountDifference = (int) Math.round(Math.abs(targetEdgeCount - actualEdgeCount));
				int edgeCountPercentageDifference = (int) Math.round((((float) edgeCountDifference) / ((float) targetEdgeCount)) * 100.0);
				
				System.out.format("%8d\t%8d\t| %8d\t%8d\t| %8d\t%8d\n", targetPopulationSize, targetEdgeCount, actualPopulationSize, actualEdgeCount, edgeCountDifference, edgeCountPercentageDifference);
			}
			
			if (targetPopulationSizeIndex == targetPopulationSizes.length - 1)
			{
				System.out.println("=================================================================================================\n");
			}
			else
			{
				System.out.println("--------------------------------+-------------------------------+--------------------------------");
			}
		}
		
		System.out.println("Small-World Graph\n=================\n");
		System.out.println(" + number of long distance connections = max(1, (|E| - 2 * |V|) / |V|)");
		System.out.println(" +                    cluster exponent = 2");
		System.out.println(" +                        lattice size = sqrt(|V|)\n");
		
		System.out.println("Target\t\t\t\t| Actual\t\t\t| Difference");
		System.out.println("|V|\t\t|E|\t\t| |V|\t\t|E|\t\t| #\t\t%");
		System.out.println("--------------------------------+-------------------------------+--------------------------------");
		
		for (int targetPopulationSizeIndex = 0; targetPopulationSizeIndex < targetPopulationSizes.length; targetPopulationSizeIndex++)
		{
			for (int targetVertexDegreeIndex = 0; targetVertexDegreeIndex < targetVertexDegrees.length; targetVertexDegreeIndex++)
			{
				int targetPopulationSize = targetPopulationSizes[targetPopulationSizeIndex];
				int targetVertexDegree = (int) targetVertexDegrees[targetVertexDegreeIndex];
				int targetEdgeCount = targetVertexDegree * targetPopulationSize;
				
				graph = getSmallWorldGraph(targetPopulationSize, targetVertexDegree * targetPopulationSize);
				
				int actualPopulationSize = graph.getVertexCount();
				int actualEdgeCount = graph.getEdgeCount();
				
				int edgeCountDifference = (int) Math.round(Math.abs(targetEdgeCount - actualEdgeCount));
				int edgeCountPercentageDifference = (int) Math.round((((float) edgeCountDifference) / ((float) targetEdgeCount)) * 100.0);
				
				System.out.format("%8d\t%8d\t| %8d\t%8d\t| %8d\t%8d\n", targetPopulationSize, targetEdgeCount, actualPopulationSize, actualEdgeCount, edgeCountDifference, edgeCountPercentageDifference);
			}
			
			if (targetPopulationSizeIndex == targetPopulationSizes.length - 1)
			{
				System.out.println("=================================================================================================\n");
			}
			else
			{
				System.out.println("--------------------------------+-------------------------------+--------------------------------");
			}
		}
	}
	
	// p = (2 * |E|) / |V|^2
	public static Graph getRandomGraph(int targetPopulationSize, int targetEdgeCount)
	{
		double connectionProbability = 2.0 * (((float) targetEdgeCount) / Math.pow(targetPopulationSize, 2));
		connectionProbability = (connectionProbability > 1) ? 1 : connectionProbability;
		connectionProbability = (connectionProbability < 0) ? 0 : connectionProbability;
		
		ErdosRenyiGenerator<MyVertex, MyEdge> generator = new ErdosRenyiGenerator<MyVertex, MyEdge>(undirectedGraphFactory, vertexFactory, edgeFactory, targetPopulationSize, connectionProbability);
		
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
		
		// System.out.format("SmallWorldGraph targets: (p = %d, e = %d) latticeSize: %d connections: %d\n", targetPopulationSize, targetEdgeCount, latticeSize, numberOfLongDistanceConnections);
		
		KleinbergSmallWorldGenerator<MyVertex, MyEdge> generator = new KleinbergSmallWorldGenerator<MyVertex, MyEdge>(undirectedGraphFactory, vertexFactory, edgeFactory, latticeSize, 2.0);
		generator.setConnectionCount(numberOfLongDistanceConnections);
		
		return generator.create();
	}
}