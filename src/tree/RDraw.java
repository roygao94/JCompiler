package tree;

import processing.Lexer;
import processing.Syntax;

import java.io.IOException;

/**
 * Created by Roy Gao on 12/23/2015.
 */
public class RDraw {

	public static void main(String[] args) throws IOException, InterruptedException {
		Node root = new Node("program");
		Syntax syntax = new Syntax(root, new Lexer("test.txt"));
		DrawSlowly rTest = new DrawSlowly();
		rTest.drawStepByStep(root, 1000);
	}
}
