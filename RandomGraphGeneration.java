//download&extract http://sourceforge.net/projects/jung/files/latest/download?source=files
//javac -cp .:* RandomGraphGeneration.java

import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.SparseMultigraph;
import edu.uci.ics.jung.graph.util.EdgeType;
import edu.uci.ics.jung.algorithms.generators.random.*;
import org.apache.commons.collections15.functors.ConstantFactory;
import edu.uci.ics.jung.graph.SparseGraph;
import java.util.HashSet;
import org.apache.commons.collections15.functors.InstantiateFactory;
import edu.uci.ics.jung.io.GraphMLWriter;
import java.io.*;

public class RandomGraphGeneration {       
    public static void main(String[] args) throws Exception{
	SparseGraph<MyVertex, MyEdge> cake = new SparseGraph<MyVertex, MyEdge>();
	ConstantFactory<Graph<MyVertex, MyEdge>> graphFactory = new ConstantFactory<Graph<MyVertex,MyEdge>>(cake);
	InstantiateFactory<MyVertex> vertexFactory = new InstantiateFactory<MyVertex>(MyVertex.class);
	InstantiateFactory<MyEdge> edgeFactory = new InstantiateFactory<MyEdge>(MyEdge.class);
	HashSet<MyVertex> seedVertices = new HashSet<MyVertex>();
        int evolve = 1;
        int degree = 10;
        int agents = 100;

       //ignoring generateMixedRandomGraph because we don't want mixed graphs

       //ErdosRenyiGenerator<MyVertex,MyEdge> gen = new ErdosRenyiGenerator<MyVertex,MyEdge>(graphFactory, vertexFactory, edgeFactory, agents, 0.1);
       //cannot find symbol

       int latticeSize = 5;
       KleinbergSmallWorldGenerator<MyVertex,MyEdge> gen3 = new KleinbergSmallWorldGenerator<MyVertex,MyEdge>(graphFactory, vertexFactory, edgeFactory, latticeSize, 0.1);
       System.out.println(gen3.create().toString()); //beware of IndexOutOfBounds related to vertexFactory

       int iterations = 5;
       EppsteinPowerLawGenerator<MyVertex,MyEdge> gen1 = new EppsteinPowerLawGenerator<MyVertex,MyEdge>(graphFactory, vertexFactory, edgeFactory, agents, degree, iterations);
       System.out.println(gen1.create().toString()); 

       BarabasiAlbertGenerator<MyVertex,MyEdge> gen2 = new BarabasiAlbertGenerator<MyVertex,MyEdge>(graphFactory, vertexFactory, edgeFactory, agents, degree, seedVertices);
       gen2.evolveGraph(evolve);
       System.out.println(gen2.create().toString()); 

       
//         for(int agents = 100; agents <= 500; agents+=100){//500
//             BarabasiAlbertGenerator<MyVertex,MyEdge> gen = new BarabasiAlbertGenerator<MyVertex,MyEdge>(graphFactory, vertexFactory, edgeFactory, agents, degree, seedVertices);
//             gen.evolveGraph(evolve);
//             writeOut(gen.create(), Integer.toString(agents)+"agents.graphml");
// 	}
//         int agents = 100;
//         for(degree = 5; degree <= 50; degree+=5){
//             BarabasiAlbertGenerator<MyVertex,MyEdge> gen = new BarabasiAlbertGenerator<MyVertex,MyEdge>(graphFactory, vertexFactory, edgeFactory, agents, degree, seedVertices);
//             gen.evolveGraph(evolve);
//             writeOut(gen.create(), Integer.toString(degree)+"degree.graphml");
//         }
        
    }

    private static void writeOut(Graph g, String filepath) throws Exception{
        GraphMLWriter<MyVertex, MyEdge> graphWriter = new GraphMLWriter<MyVertex, MyEdge>(); 
        PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(filepath)));
        graphWriter.save(g, out);
        System.out.println(g.toString());// + g.toString());    
    }
}

/*
base: 100 agents, neighbourhood/degree 10

agents: 100,200,300,400,500

neighbourhood size: 5,10,25,50

Unless stated otherwise, the results presented here represent an
average of 10 runs using a population of 100 agents, a neigh-
bourhood size of n = 10 (using a regular random network
as the initial topology), 10 pairings per agent per generation
(P = 10), and a context influence of γ = 0.5. After repro-
duction, the probability of mutating the tag by selecting a
new random value is 0.001, and there is a 0.001 probability
of adding Gaussian noise to the tolerance (with mean 0 and
standard deviation of 0.01).






*/