package graph.grader;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import graph.CapGraph;
import util.GraphLoader;

/*
 * (non-Javadoc)
 * Class to test the project. Uses the graph given in one of the videos for testing.
 */
public class ProjectTester {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		CapGraph graph = new CapGraph();
		GraphLoader.loadGraph(graph, "data/test_data.txt");
		graph.initializePreferenceMap("Console");
		System.out.println(graph);
		System.out.println(graph.getPreferenceMap());
		
		Map<Integer, String> changes = new HashMap<Integer, String>();
		changes.put(25, "PC");
		changes.put(18, "PC");
		
		Map<Integer, String> changedPreferenceMap = graph.findNetworkCascades(changes, 3);
		System.out.println(changedPreferenceMap);
		List<Integer> stronglyConnectedPeople = graph.stronglyConnectedPeopleSubset();
		System.out.println(stronglyConnectedPeople);
	}

}
