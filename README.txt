Author: Andrew Lewis
Date: 09-10-2018
Program: Digraphv.java

Compilation: javac Digraphv.java
Execution: Java Digraphv 
Dependencies: Java LinkedList, Java ArrayList, Java HashMap, Java List.

This implementation of a directed graph involves the use of a Node class, an Edge class, and a Digraphv Class.

Node Class: This class stores data about the current node, i.e node ID, as well as the adj list of neighbors.
It contains helper functions for modifying the node. i.e node.setLastVisited() As well as functions for node creation and edge creation
This class is dependent on the Java ArrayList data structure for storing neighbors. 

Edge Class: This class is a simple data structure of a pair of two ints, the first represting a computer ID, second represting the time of 
connection between host node and the computer ID. This class is dependent on the Java List data structure for storing edges. 

Digraphv class: This class encapsulates the other two classes and controls the logic for solving the 'virus spread' problem.
It contains a BFS -esque trasversal function with modified logic to fit the problem. This logic is proven in the handed-in write-up. 
It uses a Java ArrayList to store all nodes in the graph, and a HashMap to ID each element in the graph to a specific location in the list.
This allows for inputing the data in any order of triples and maintaining correctness.
i.e Every node in the graph can be accessed by an ID which is the simply the computer number as inputted from triples. 

The check_spread function in the Digraphv class simply returns a path from one node to another node if intermediary paths between them fall within the specific time range.
If a path is found, then node a could succefully have sent a message to node b through those valid channels.