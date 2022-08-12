package s3.ai;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import s3.base.S3;
import s3.entities.S3PhysicalEntity;
import s3.util.Pair;


public class AStar {
	private ArrayList<Node> CLOSED;
	private ArrayList<Node> OPEN;


	//computes the length of the path by returning the size of compute path
	public static int pathDistance(double start_x, double start_y, double goal_x, double goal_y,
			S3PhysicalEntity i_entity, S3 the_game) {

		AStar a = new AStar(start_x,start_y,goal_x,goal_y,i_entity,the_game);
		List<Pair<Double, Double>> path = a.computePath();
		if (path!=null) return path.size();
		return -1;

	}


	// Heuristic from slides is f(x) = g(x) + h(x)
	// Where g(x) = distance from start, and h(x) = manhattan distance
	public AStar(double start_x, double start_y, double goal_x, double goal_y, S3PhysicalEntity i_entity, S3 the_game) {
		this.CLOSED = new ArrayList<Node>();
		this.OPEN = new ArrayList<Node>();

		ArrayList<Node> children;
		S3PhysicalEntity i_entity_clone = i_entity;

		//declare new node for start node
		Node start = new Node();
		start.setXY(start_x, start_y);

		//add start to open list

		OPEN.add(start);

		while(!OPEN.isEmpty()){
			Node N = removeLowestHeuristicNode();
			//System.out.println("N.x = " + N.get_x() + " N.y = " + N.get_y());

			//check if N is goal
			if(isGoal(N,goal_x, goal_y)){
				CLOSED.add(N);
				System.out.println("reached goal");
				break;
			}
			else{
				CLOSED.add(N);
				children = N.getChildrenOf();
				for(Node M : children){
					//set clone coordinates equal to new child node
					i_entity_clone.setX((int)M.get_x());
					i_entity_clone.setY((int)M.get_y());

					if(!existsInOpenOrClosed(M) && the_game.anyLevelCollision(i_entity_clone) == null){
						M.setParent(N);
						M.setg(N.get_g() + 1.0);
						M.setHeuristic(getManhattan(M.get_x(), M.get_y(), goal_x, goal_y));
						OPEN.add(0, M);
					}
				}
				children.clear();
			}
		}
	}


	private boolean existsInOpenOrClosed(Node node){
		boolean existsInOpen = false;
		boolean existsInClosed = false;

		//check if exists in open
		for(int i = 0; i < OPEN.size(); i++){
			//see if node exists in OPEN
			if(node.get_x() == OPEN.get(i).get_x() && node.get_y() == OPEN.get(i).get_y()) {
				existsInOpen = true;
			}
		}

		//check if exists in closed
		for(int i = 0; i < CLOSED.size(); i++){
			//see if node exists in OPEN
			if(node.get_x() == CLOSED.get(i).get_x() && node.get_y() == CLOSED.get(i).get_y()) {
				existsInClosed = true;
			}
		}


		return (existsInClosed && existsInOpen);
	}

	private double getManhattan(double start_x, double start_y, double goal_x, double goal_y) {
		double x = Math.abs(goal_x - start_x);
		double y = Math.abs(goal_y - start_y);

		return (x + y);
	}

	private boolean isGoal(Node node, double goal_x, double goal_y){
		return node.get_x() == goal_x && node.get_y() == goal_y;
	}

	private Node removeLowestHeuristicNode(){
		Node low = OPEN.get(0);
		int lowest_h_index = 0;


		for(int i = 0; i < OPEN.size(); i++){
			if(OPEN.get(i).getHeuristic() < low.getHeuristic()){
				low = OPEN.get(i);
				lowest_h_index = i;
			}
		}
		System.out.println("Lowest index h: " + lowest_h_index);
		OPEN.remove(lowest_h_index);
		return low;
	}

	public ArrayList<Pair<Double, Double>> computePath() {

		ArrayList<Pair<Double, Double>> path = new ArrayList<Pair<Double,Double>>();

		if(!CLOSED.isEmpty()) {
			//start from end of CLOSED, last node in CLOSED is the goal node
			Node currentNode = CLOSED.get(CLOSED.size() - 1);

			while (currentNode.getParent() != null) {
				path.add(new Pair(currentNode.get_x(), currentNode.get_y()));
				currentNode = currentNode.getParent();
			}

			Collections.reverse(path);
			return path;
		}
		else
			return null;
	}

}

