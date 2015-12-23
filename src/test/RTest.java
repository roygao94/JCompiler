package test;

import io.Pair;
import processing.Lexer;
import processing.Syntax;
import tree.DrawSlowly;
import tree.Node;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Roy Gao on 12/21/2015.
 */
public class RTest {

	public static void main(String[] args) throws IOException, InterruptedException {

		Lexer lexer = new Lexer("source.txt");
		List<List<Pair<String, String>>> source = lexer.getOriginalCode();
		String input = "";
		for (List<Pair<String, String>> line : source) {
			for (Pair<String, String> token : line)
				input += token.getFirst() + " ";
			input += "\n";
		}

		System.out.println(input);

		String[] source2 = input.split("\n");
		List<String> list = new ArrayList<>();
		for (String line : source2)
			list.add(line);

		Lexer lexer1 = new Lexer(list);

		Runtime runtime = Runtime.getRuntime();
		runtime.exec("java -jar RDraw.jar");
//
//		Node root = new Node("root");
//		Node child = new Node("child");
//		Node child2 = new Node("child2");
//		Node grandChild = new Node("grandChild");
//
//		child.add(grandChild);
//		root.add(child);
//		root.add(child2);
//
//		Node rootCopy = new Node(root);
//		root.pop();
//
////		DFS(root);
////		System.out.println("===================");
////		DFS(rootCopy);
//
////		DrawTree(rootCopy);
//
//		root = new Node("program");
//		Syntax syntax = new Syntax(root, new Lexer("test.txt"));
//		DrawSlowly rTest = new DrawSlowly();
//		rTest.drawStepByStep(root, 1000);
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
