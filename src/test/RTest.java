package test;

import processing.Lexer;
import processing.Syntax;
import tree.DrawSlowly;
import tree.Node;

import java.io.IOException;

/**
 * Created by Roy Gao on 12/21/2015.
 */
public class RTest {

	public static void main(String[] args) throws IOException, InterruptedException {

		Node root = new Node("root");
		Node child = new Node("child");
		Node child2 = new Node("child2");
		Node grandChild = new Node("grandChild");

		child.add(grandChild);
		root.add(child);
		root.add(child2);

		Node rootCopy = new Node(root);
		root.pop();

//		DFS(root);
//		System.out.println("===================");
//		DFS(rootCopy);

//		DrawTree(rootCopy);

		root = new Node("program");
		Syntax syntax = new Syntax(root, new Lexer("test.txt"));
		DrawSlowly rTest = new DrawSlowly();
		rTest.drawStepByStep(root, 1000);
	}

//	public void depthFirstDraw() throws InterruptedException {
//		dfs(root);
//	}
//
//	private void dfs(Node node) throws InterruptedException {
//		frame.depthFirstDraw(root);
//
//		if (node.hasChild())
//			for (Node child : node.getChilds())
//				child.setVisited(true);
//
//		if (node.hasChild())
//			for (Node child : node.getChilds())
//				dfs(child);
//
//		node.setVisited(false);
//		frame.depthFirstDraw(root);
//	}
}
