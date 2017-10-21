import java.util.ArrayList;

public class WaterJugs
{
	public ArrayList<Node> frontier;
	public ArrayList<int[]> explored;

	// X is 4 gallon jug
	// Y is 3 gallon jug
	public Node solve()
	{
		// Initial problem state
		int[] initial_state = {0, 0};

		// Current node under consideration
		Node curr_node = new Node(initial_state, null, null);

		// List of frontier nodes at each iteration
		// Manipulated as a queue
		this.frontier = new ArrayList<Node>();

		// List of nodes already explored
		// They are excluded from expanded frontier nodes at each iteration
		this.explored = new ArrayList<int[]>();

		frontier.add(curr_node);

		while (true) {
			// If we have exhausted our frontier 
			// we have no solution
			if (frontier.size() == 0) {
				return null;
			}
            
            // Pop new node from frontier to consider
			curr_node = frontier.remove(0);
			System.out.print("Current Node:");
			System.out.print("[" + curr_node.state[0] + "," + curr_node.state[1] + "]");
            
		    // Add chosen node to explored list if not already there
			if(!this.inExplored(curr_node.state)) {
				explored.add(curr_node.state);				
			}

            // List of actions that can be applied based on the state
            // and the description of the problem
			Action[] actions = this.getFeasibleActions(curr_node.state);

            // Apply our transition model to generate child nodes 
            // from current node
			System.out.print("Children");
			for (Action action : actions) {
				Node child = this.getChildNode(curr_node, action);		
				
				if (!this.inFrontier(child) && !this.inExplored(child.state)) {
					System.out.print(child.state[0] + "," + child.state[1] + "| ");
					if (this.isGoal(child.state)) {
						return child;
					}
					frontier.add(child);
				}

			}
			System.out.println();
		}
	}

    /**
     * Get the list of actions that should be applied 
     * when given a state
     * 
     * @param  state A problem state
     * @return actions Array of actions 
     */
	public Action[] getFeasibleActions(int[] state)
	{
	    int x = state[0];
	    int y = state[1];
		ArrayList<Action> actionsList = new ArrayList<Action>();

		if ( x == 0) {
			// Action for filling x from tap
		    actionsList.add(new Action("tap", "x", 4));
		}

		if (y == 0) {
			// Action for filling y from tap
		    actionsList.add(new Action("tap", "y", 3));
		}

		if (x == 4) {
			// Action for emptying x
			actionsList.add(new Action("x", "ground", x));
		}

		if (y == 3) {
			// Action for emptying y
			actionsList.add(new Action("y", "ground", y));
		}

        // Add action for filling one jug with another
        // if only it is possible to and there is the need for it
        if ((x > 0) && ((3-y) > 0)) {
        	// if (x >= (3 - y)) fill with ( 3 - y) else fill with x
			actionsList.add(new Action("x", "y", Math.min((3-y), x)));
        }

        if ((y > 0) && ((4 -x) > 0)) {
        	// If (y >= (4-x) fill with (4-x)) else fill with y
	        actionsList.add(new Action("y", "x", Math.min((4-x), y)));	                
	    }

        Action[] actionsArray = new Action[actionsList.size()];
		return actionsList.toArray(actionsArray);
	}

    /**
     * Get a child node when given a node and action
     * 
     * @param  node  The node which becomes the parent
     * @param  action The action to apply
     * @return  child Child node
     */
	public Node getChildNode(Node node, Action action) {
		int[] new_state = new int[2];

        new_state[0] = node.state[0];
		new_state[1] = node.state[1];

		if (action != null) {
			// Implementing the transition model
			if (action.to == "x") {
				new_state[0] = node.state[0] + action.quantity;
			} 

			if (action.to == "y") {
				new_state[1] = node.state[1] + action.quantity;
			}

			if (action.from == "x") {
				new_state[0] = node.state[0] - action.quantity;
			}

	        if (action.from == "y") {
				new_state[1] = node.state[1] - action.quantity;

			}

		}

		Node new_node = new Node(new_state, action, node);

		return new_node;
	}

    /**
     * Check whether a node is in frontier
     * 
     * @param  node The node
     * @return  Truth value of test
     */
	public boolean inFrontier(Node node)
	{
		for (Node curr : this.frontier) {
			if ((curr.state[0] == node.state[0]) && (curr.state[1] == node.state[1])) {
				return true;
			}
		}

		return false;
	}

    /**
     * Check whether a state is in explored state
     * 
     * @param  state State to consider
     * @return  Truth value of test
     */
	public boolean inExplored(int[] state)
	{
		for (int i = 0; i < this.explored.size(); i++) {
			int[] curr = this.explored.get(i);

			if ((curr[0] == state[0]) && (curr[1] == state[1])) {
				return true;
			}
		}

		return false;
	}

    /**
     * Check whether given state is goal state
     * 
     * @param  state State under consideration
     * @return  Truth value of test
     */
	public boolean isGoal(int[] state)
	{
		return (state[0] == 2);
	}

    /**
     * Trace out the history of steps that generated this solution
     * and print it
     * 
     * @param  solution_node Node The solution node
     * @return null
     */
	public void printSolution(Node solution_node)
	{
		ArrayList<String> steps = new ArrayList<String>();

		while (solution_node.parent != null) {
			steps.add("Fill " + solution_node.action.to + " with water from  " + 
					solution_node.action.from + " (ie. " + solution_node.action.quantity + " jugs)" + 
					" New state: " + solution_node.state [0] + "," + solution_node.state[1]);

			solution_node = solution_node.parent;
		} 

		System.out.print("\n\nSteps:\n\n");

		steps.add("\n4 gallon jug has " + solution_node.state[0] + " gallons and 3 gallon jug has " + 
					solution_node.state[1] + " gallons");

		while (steps.size() > 0) {
			String curr = steps.remove(steps.size() - 1);
			System.out.println(curr);
		}
	}
}