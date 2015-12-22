package tree;

/**
 * Created by Roy Gao on 12/22/2015.
 */
public class DrawSlowly {

	private DrawTree frame;
	private Node root;

	public DrawSlowly(Node root) throws InterruptedException {
		this.root = new Node(root);
		this.root.setVisited(true);
		frame = new DrawTree();
//		drawStepByStep();
	}

	public void drawStepByStep() throws InterruptedException {
		dfs(root);
	}

	private void dfs(Node node) throws InterruptedException {
		frame.draw(root);

		if (node.hasChild())
			for (Node child : node.getChilds())
				child.setVisited(true);

		if (node.hasChild())
			for (Node child : node.getChilds())
				dfs(child);

		node.setVisited(false);
		frame.draw(root);
	}
}
