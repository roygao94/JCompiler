package tree;

/**
 * Created by Roy Gao on 12/22/2015.
 */
public class DrawSlowly {

	private DrawTree frame;
	private Node root;
	private long sleepTime = 2000;

	public DrawSlowly() throws InterruptedException {
		frame = new DrawTree();
//		drawStepByStep();
	}

	public void drawStepByStep(Node root) throws InterruptedException {
		this.root = new Node(root);
		this.root.setVisited(true);
//		dfs(root);
		dfsEvenMoreSlowly(root);
	}

	public void drawStepByStep(Node root, long sleepTime) throws InterruptedException {
		this.root = new Node(root);
		this.root.setVisited(true);
		this.sleepTime = sleepTime;
//		dfs(root);
		dfsEvenMoreSlowly(this.root);
	}

	private void dfs(Node node) throws InterruptedException {
		frame.depthFirstDraw(root, sleepTime);

		if (node.hasChild())
			for (Node child : node.getChilds())
				child.setVisited(true);

		if (node.hasChild())
			for (Node child : node.getChilds())
				dfs(child);

		node.setVisited(false);
		frame.depthFirstDraw(this.root, sleepTime);
	}

	private void dfsEvenMoreSlowly(Node node) throws InterruptedException {
		if (node.hasChild())
			for (Node child : node.getChilds()) {
				child.setVisited(true);
				frame.depthFirstDraw(root, sleepTime);
			}

		if (node.hasChild())
			for (Node child : node.getChilds())
				dfsEvenMoreSlowly(child);

		node.setVisited(false);
		frame.depthFirstDraw(root, sleepTime);
	}
}
