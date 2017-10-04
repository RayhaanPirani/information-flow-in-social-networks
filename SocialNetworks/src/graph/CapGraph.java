/**
 * 
 */
package graph;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

import util.MapUtils;

//import com.sun.org.apache.xml.internal.serializer.ToStream;

/**
 * @author Your name here.
 * 
 * For the warm up assignment, you must implement your Graph in a class
 * named CapGraph.  Here is the stub file.
 *
 */
public class CapGraph implements Graph {
	
	private HashMap<Integer,HashSet<Integer>> adjListsMap;
	private Map<Integer,String> preferenceMap;
	
	public CapGraph() {
		adjListsMap = new HashMap<Integer, HashSet<Integer>>();
		preferenceMap = new HashMap<Integer, String>();
	}

	/* (non-Javadoc)
	 * @see graph.Graph#addVertex(int)
	 */
	@Override
	public void addVertex(int num) {
		if(!adjListsMap.containsKey(num))
			adjListsMap.put(num, new HashSet<Integer>());
	}

	/* (non-Javadoc)
	 * @see graph.Graph#addEdge(int, int)
	 */
	@Override
	public void addEdge(int from, int to) {
		adjListsMap.get(from).add(to);
	}
	
	/*
	 * (non-Javadoc)
	 * Used to perform the network cascades (easier question) given the changes and the number of generations.
	 */
	public Map<Integer, String> findNetworkCascades(Map<Integer, String> changes, int generations) {
		for(int i = 1; i <= generations; i++)
			this.findNetworkCascades(changes);
		return preferenceMap;
	}
	
	/*
	 * (non-Javadoc)
	 * Used to perform the network cascades (easier question) given the changes for a single generation.
	 */
	public Map<Integer, String> findNetworkCascades(Map<Integer, String> changes) {
		for(int changedNode : changes.keySet())
			this.preferenceMap.put(changedNode, changes.get(changedNode));
		
		for(int node : adjListsMap.keySet()) {
			if(changes.keySet().contains(node)) continue;
			
			Set<Integer> nodeNeighbors = this.getNeighbors(node);
			int neighborCount = nodeNeighbors.size();
			int computerUserCount = 0;
			
			for(int neighbor : nodeNeighbors)
				if(preferenceMap.get(neighbor).equals("PC")) computerUserCount++;
			
			double rewardForComputer = 7, rewardForConsole = 4;
			
			double preferenceForComputer = ((double)computerUserCount) / neighborCount;
			
			if(preferenceForComputer > (rewardForConsole / (rewardForComputer + rewardForConsole))) {
				this.preferenceMap.put(node, "PC");
			}
			else this.preferenceMap.put(node, "Console");
		}
		
		return preferenceMap;
	}
	
	/*
	 * (non-Javadoc)
	 * Given some k iterations, we find the strongly connected people subset in the
	 * graph whose size is always less than or equal to than the one that
	 * would be returned by the greedy method to find the smallest
	 * possible subset of connected people. Uses a randomized algorithm on top
	 * of the greedy algorithm to find the required subset.
	 */
	public List<Integer> stronglyConnectedPeopleSubset(int k) {
		List<Integer> stronglyConnectedPeople = greedyStronglyConnectedPeopleSubset();
		
		for(int i = 1; i <= k; i++) {
			List<Integer> randomizedStronglyConnectedPeople = randomizedStronglyConnectedPeopleSubset();
			if(randomizedStronglyConnectedPeople.size() < stronglyConnectedPeople.size())
				stronglyConnectedPeople = randomizedStronglyConnectedPeople;
		}
		
		return stronglyConnectedPeople;
	}
	
	/*
	 * (non-Javadoc)
	 * Finds the strongly connected people subset in the
	 * graph whose size is always less than or equal to than the one that
	 * would be returned by the greedy method to find the smallest
	 * possible subset of connected people. Runs the randomized algorithm
	 * for V*V iterations.
	 */
	public List<Integer> stronglyConnectedPeopleSubset() {
		return stronglyConnectedPeopleSubset((int)Math.pow(verticesCount(), 2));
	}
	
	/*
	 * (non-Javadoc)
	 * Finds the strongly connected people subset in the graph using the
	 * greedy strategy. In most of the cases, this algorithm does not return
	 * the optimal solution, but always returns a subset that is less than
	 * or equal to the number of people in the social network.
	 */
	private List<Integer> greedyStronglyConnectedPeopleSubset() {
		List<Integer> stronglyConnectedNodes = new LinkedList<Integer>();
		Map<Integer, Integer> neighborCountMap = new HashMap<Integer, Integer>();
		
		for(int node : adjListsMap.keySet())
			neighborCountMap.put(node, getNeighbors(node).size());
		
		Map<Integer, Integer> sortedByNeighborCountMap = MapUtils.sortByValue(neighborCountMap);
		
		Map<Integer, Boolean> coveredNodesMap = new HashMap<Integer, Boolean>();
		for(int node : adjListsMap.keySet())
			coveredNodesMap.put(node, false);
		
		for(int node : sortedByNeighborCountMap.keySet()) {
			Set<Integer> nodeNeighbors = getNeighbors(node);
			
			for(int neighbor : nodeNeighbors)
				coveredNodesMap.put(neighbor, true);
			
			stronglyConnectedNodes.add(node);
			if(areAllNodesCovered(coveredNodesMap)) break;
		}
		
		return stronglyConnectedNodes;
	}
	
	/*
	 * (non-Javadoc)
	 * Shuffles the node order randomly to calculate the possible minimum
	 * size of the connected people subset in the social network. In practice,
	 * running this method for some amount of iterations proportional to the
	 * number of vertices returns a solution that is very close to the optimal
	 * solution.  
	 */
	private List<Integer> randomizedStronglyConnectedPeopleSubset() {
		List<Integer> stronglyConnectedNodes = new LinkedList<Integer>();
		Map<Integer, Integer> neighborCountMap = new HashMap<Integer, Integer>();
		
		for(int node : adjListsMap.keySet())
			neighborCountMap.put(node, getNeighbors(node).size());
		
		List<Integer> shuffledNodes = new ArrayList<Integer>(adjListsMap.keySet());
		Collections.shuffle(shuffledNodes);
		
		Map<Integer, Boolean> coveredNodesMap = new HashMap<Integer, Boolean>();
		for(int node : adjListsMap.keySet())
			coveredNodesMap.put(node, false);
		
		for(int node : shuffledNodes) {
			Set<Integer> nodeNeighbors = getNeighbors(node);
			
			for(int neighbor : nodeNeighbors)
				coveredNodesMap.put(neighbor, true);
			
			stronglyConnectedNodes.add(node);
			if(areAllNodesCovered(coveredNodesMap)) break;
		}
		
		return stronglyConnectedNodes;
	}
	
	/*
	 * (non-Javadoc)
	 * Helper function to check if all the nodes in the given map are visited or not.
	 */
	private boolean areAllNodesCovered(Map<Integer, Boolean> coveredNodesMap) {
		for(int node : coveredNodesMap.keySet())
			if(!coveredNodesMap.get(node)) return false;
		return true;
	}

	/* (non-Javadoc)
	 * @see graph.Graph#getEgonet(int)
	 */
	@Override
	public Graph getEgonet(int center) {
		// TODO Auto-generated method stub
		Graph egonet = new CapGraph();
		
		egonet.addVertex(center);
		
		Set<Integer> centerConnections = adjListsMap.get(center);
		for(Integer connection : centerConnections) {
			egonet.addVertex(connection);
			egonet.addEdge(center, connection);
			egonet.addEdge(connection, center);
		}
		
		for(Integer connection : centerConnections) {
			for(Integer otherConnection : centerConnections)
				if(connection != otherConnection && adjListsMap.get(connection).contains(otherConnection))
					egonet.addEdge(connection, otherConnection);
		}
			
		
		return egonet;
	}

	/* (non-Javadoc)
	 * @see graph.Graph#getSCCs()
	 */
	@Override
	public List<Graph> getSCCs() {
		List<Graph> connectedComponents = new ArrayList<Graph>();
		Stack<Integer> vertices = new Stack<Integer>();
		vertices.addAll(adjListsMap.keySet());
		Stack<Integer> finished = DFS(this, vertices, false, connectedComponents);
		
		Graph transpose = getTranspose();
		
		finished = DFS(transpose, finished, true, connectedComponents);
		return connectedComponents;
	}
	
	private Stack<Integer> DFS(Graph graph, Stack<Integer> vertices, boolean createGraph, List<Graph> connectedComponents) {
		Set<Integer> visited = new HashSet<Integer>();
		Stack<Integer> finished = new Stack<Integer>();
		
		while(!vertices.isEmpty()) {
			int vertex = vertices.pop();
			if(!visited.contains(vertex)) {
				DFSVisit(graph, vertex, visited, finished, createGraph, connectedComponents);

				if(createGraph) {
					Graph connectedComponent = new CapGraph();
					List<Integer> nodeList = new ArrayList<Integer>();
					
					while(!finished.isEmpty())
						nodeList.add(finished.pop());
					
					for(int node : nodeList) {
						connectedComponent.addVertex(node);
						Set<Integer> nodeNeighbors = ((CapGraph)graph).getNeighbors(node);
						for(int neighbor : nodeNeighbors)
							if(nodeList.contains(neighbor))
								connectedComponent.addEdge(node, neighbor);
					}
					
					connectedComponents.add(connectedComponent);
				}
				
			}
		}
		return finished;
	}
	
	private void DFSVisit(Graph graph, int vertex, Set<Integer> visited, Stack<Integer> finished, boolean createGraph, List<Graph> connectedComponents) {
		visited.add(vertex);
		
		for(int node : ((CapGraph)graph).getNeighbors(vertex))
			if(!visited.contains(node)) 
				DFSVisit(graph, node, visited, finished, createGraph, connectedComponents);

		finished.push(vertex);
	}
	
	public Map<Integer, String> getPreferenceMap() {
		return this.preferenceMap;
	}
	
	public void initializePreferenceMap(String preference) {
		for(int node : adjListsMap.keySet())
			preferenceMap.put(node, preference);
	}
	
	private Graph getTranspose() {
		Graph transpose = new CapGraph();
		
		for(int vertex : adjListsMap.keySet())
			for(int node : adjListsMap.get(vertex)) {
				transpose.addVertex(node);
				transpose.addEdge(node, vertex);
			}
		return transpose;
	}
	
	public Set<Integer> getNeighbors(int vertex) {
		HashSet<Integer> neighbors = adjListsMap.get(vertex);
		if(neighbors == null) return new HashSet<Integer>();
		return new HashSet<Integer>(neighbors);
	}
	
	public int verticesCount() {
		return adjListsMap.keySet().size();
	}
	
	public String toString() {
		String string = "";
		for(int node : adjListsMap.keySet())
			string += node + "::" + adjListsMap.get(node) + "\n";
		return string;
	}

	/* (non-Javadoc)
	 * @see graph.Graph#exportGraph()
	 */
	@Override
	public HashMap<Integer, HashSet<Integer>> exportGraph() {
		// TODO Auto-generated method stub
		return adjListsMap;
	}

}
