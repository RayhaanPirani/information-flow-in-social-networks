# Overview
This project focuses on how information flows through a social network and how people react to such information. It also checks how many people are (at least) required to know a vital piece of information such that all people in the network know that information through a connection to the former set of people.

# How to use the project
1. Clone the project into your system.
```
git clone https://github.com/RayhaanPirani/information-flow-in-social-networks.git
```
2. For Eclipse, create a Java Project in your workspace.
3. Import the project files by going to File > Import > select _File System_ > Next > Browse and select the 'SocialNetworks' folder. Alternatively, you can use any other Java IDE or text editor by opening the folder 'SocialNetworks' as a Java project.
4. Run the `main()` method in the `ProjectTester` class to run the code. Modify the number of variables if necessary. Inputs can be found in the **data** folder.

# Code and design overview
## Graph (interface)
Provides an interface for a graph. Describes methods to add vertices and edges and a way to export the graph to be tested. Also describes methods to get the egonet and the strongly connected components of a graph.
## CapGraph (class)
A concrete implementation of the Graph interface using the adjacency list method. Contains all the methods that implement the solution for the problem described in the project as well. This is our major class in this project.
## MapUtils (class)
Contains a method to sort a given Map in the descending order depending on their value. Useful to sort the nodes in the graph by the number of their neighbors.
## ProjectTester (class)
A class that contains a main() method to test the project.
## Overall Design Justification
We do most of the work in the CapGraph class. It is because whatever we want to perform is directly related to the graph and placing the methods that are related to the graph inside a concrete implementation of the graph (i.e., CapGraph) would increase cohesion of our program. We use a separate class called ProjectTester only for testing our project and MapUtils class to perform an operation related to Maps, which is not directly related to the graph. Plus, having less classes for this project makes it simpler and easier to understand. The main goal of such a design was to increase cohesion within each class and reduce coupling among all the classes.


# Data
The data that will be used in this project would be the data provided for the warmup exercises in Week 1 of the UC San Diego online course _Capstone: Analyzing (Social) Network Data_.
## Data format
The data has the following simple text format.
```
0 1
0 2
1 2
```
It denotes the connection between nodes on a graph. The above example signifies that there is a connection from node 0 to 1, node 0 to 2, and node 1 to 2.

# Questions
The system answers the following questions.
## Easier Question
How do people make decisions to play video games on a PC or a console based on the decisions of people they are connected to? For example, if all of Sarah’s friends prefer a console, she would prefer to get one too. Or else, if most of Jason’s acquaintances play on PCs, and Jason anyway requires a PC for his graphics design classes, Jason would get a gaming PC instead of a regular netbook and a console. A PC will have a greater advantage as it could be used for multiple tasks, such as designing and development than a console, which can only be used for entertainment.
## Harder Question
Finding the lowest possible number of people in the social network to know about a new game release. Suppose a new game or a game console releases into the market. We need to figure out the minimum or at least the lowest possible number of people that need to know about the release so that they would tell about it to all their connections. This information is vital to many companies so that they could prepare their marketing strategy and target the most relevant people in the least possible cost. We also require the solution to be very optimal, as there are many such communities around the globe where we need to market these products and there are different products released frequently, and we would need to analyze the communities quickly multiple times in a short span of time. So, we are ready to compromise a little bit on the optimal solution, meaning that we are okay if we do not always obtain the minimum amount of people, but we require the amount of people to be as low as possible and the algorithm to be as fast as possible.

# Algorithms and Data Structures
Adjacency lists would be used to represent the main social network graph in both the problems.
## Easier Question
We would use a network cascade algorithm for the problem of checking if people would switch between Eclipse and NetBeans depending on their connections’ preference. We would provide more weight for a PC switch than a console switch to reflect on the earlier discussion in section 3 of this document.
## Harder Question
We would use a very trivial algorithm to find the lowest possible number of people that would cover all the people in the social network. This problem is equivalent to the problem of finding the minimum connected dominating set of a graph as discussed in a few videos of the course. The algorithm we will use would first map every person to its number of connections and then sort them by the number of connections in decreasing order. We then start checking the person having the most connections to check if that person covers all the people or not. If no, we continue performing this check on the person with the second highest connections and so on until the graph is fully covered. This is basically the greedy approach to this problem. To improve this algorithm, in event of two or more people having an equal number of connections, we would consider the one with the least number of connections overlapping the set of already visited people.

# Algorithm Analysis, Limitations, Risk
## Easier Question
For every switch, we would calculate the potential switch by all the connections of the person that made the switch from a PC to a console or vice versa. For each switch, we would take a worst-case time of some O(V), because each person would have at most all connections, which is equal to the number of vertices in the social network graph represented as V, and there can be a maximum of V switches. To calculate the potential switch for all the connections of V nodes, which can be at most V, we would require at worst O(V2) time. However, one issue that we would have to deal with is the equilibrium deadlock between the people. In such a case, we will take some constant number of iterations for each equilibrium calculation and stop after that to avoid entering an infinite loop.
## Harder Question
To count the number of edges of all the people in the network, it would take us linear time O(V). For sorting all the vertices in the order of their decreasing number of connections, it would take us at the most O(V * log V) time. To iterate over all the vertices again to check if covering is done, it would take us O(V2) since every person can have some amount of connections that is equal to V – k, for some k such that 0 <= k <= V and hence to iterate over V vertices having V – k friends would take us O(V * (V – k)) time which is asymptotically equivalent to O(V2). We will store the covered and non-covered people in some data structure for persistence so that we don’t have to iterate all over again to check for covering. This algorithm would be fast, but we would compromise on the optimal solution as we would not obtain the least subset in all cases. But, for our problem statement, this should suffice.

# Correctness Verification
I used the graph stored in a file called test_data.txt. Then I called the functions for the above two problems and printed their outputs and checked if they were what I would expect.

# Reflection
The output for the greedy algorithm discussed earlier in the test graph was unsatisfactory. It returned 4 nodes and the total number of nodes in the graph was 7. The output was literally more than half of the size of the graph. So, I changed my strategy and created another algorithm that was nearly the same as the one discussed above, but instead of sorting it by the descending number of neighbors, I shuffled it randomly. I converted the greedy algorithm into a randomized algorithm. And then, I ran this algorithm for either a given number of k iterations or V2 iterations if the number was not provided by the caller method, and returned the smallest subset I could find in these iterations. I also called the greedy method before performing all this so that I could check if I can find something lower than that and in the worst-case of not being able to find something better than the greedy solution, I could simply return the greedy solution. I found that I got an optimal solution by doing this for the test graph every time I did this (which is 2 nodes for the test graph)! Though I shouldn’t expect an optimal solution every time I do this. Though the complexity increased to O(kV2) or O(V4) depending on the caller method’s way of calling, the method is polynomial and is fast enough for our requirement, and is expected to provide a way better solution than the greedy solution in practice most of the time.