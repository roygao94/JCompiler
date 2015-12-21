package test;

import processing.Lexer;
import processing.Syntax;
import tree.DrawTree;
import tree.Node;

import javax.swing.*;
import java.io.IOException;

/**
 * Created by Roy Gao on 12/21/2015.
 */
public class RTest {

	private static Node copyOfRoot;

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
		copyOfRoot = new Node(root);
		dfs(copyOfRoot);
	}

	private static boolean first = true;
	private static DrawTree frame;

	private static void dfs(Node node) throws InterruptedException {
		node.setVisited(true);
		if (first) {
			frame = new DrawTree(copyOfRoot);
			first = false;
		} else
			frame.initComponents(copyOfRoot);

		frame.setSize(800, 1000);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		Thread.sleep(1000);

		if (node.hasChild())
			for (Node child : node.getChilds())
				dfs(child);

		node.setVisited(false);
		frame.initComponents(copyOfRoot);
		frame.setSize(800, 1000);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		Thread.sleep(1000);
	}
}
