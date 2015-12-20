package processing;

import io.Pair;
import io.Token;
import tree.Node;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by He on 2015/12/18.
 */
public class Semantics {

	static int i = 1;

	public static void main(String[] args) throws IOException {
		Node n = new Node("program");
		List<Pair<String, String>> stack_info = new ArrayList<>();
		List<String> input_info = new ArrayList<>();
		List<String> output_info = new ArrayList<>();

		Lexer lexer = new Lexer("test.txt");
		List<Node> treeNode = Syntax.scan(n, stack_info, input_info, output_info, lexer);
		for (Node node : treeNode)
			Syntax.DrawTree(node);

		List<Token> tokens = lexer.getTokenList();

		transiton(treeNode, tokens);

//		System.out.println("Token list size :\t" + tokens.size());
//		for (Token token : tokens)
//			System.out.println(token);
	}

	public static void transiton(List<Node> treeNode, List<Token> tokens) {
		String stmt;

		for (Node node : treeNode) {
			List<Node> tmpnode = node.getChilds();
			stmt = node.getName();
			switch (stmt) {
				case ("assgstmt"):
					transiton(tmpnode, tokens);
					if (node.getChilds().get(0).getType().equals("int") && node.getChilds().get(2).getVal().indexOf(".") != -1)
						System.out.println("real类型值无法赋值给int");
					else {
						for (Token token : tokens)
							if (token.getKey().equals(node.getChilds().get(0).getVal()) && token.isDecl())
								System.out.println("mov " + node.getChilds().get(0).getVal() + ",," + node.getChilds().get(2).getVal());
					}
					break;
				case ("arithexpr"):
					transiton(tmpnode, tokens);
					if (node.getChilds().get(1).getChilds().size() != 1) {
						if ((node.getChilds().get(1).getChilds().get(0).getVal().equals("+")))
							System.out.println("add t" + i + "," + node.getChilds().get(0).getVal() + "," + node.getChilds().get(1).getVal());
						else
							System.out.println("sub t" + i + "," + node.getChilds().get(0).getVal() + "," + node.getChilds().get(1).getVal());
						node.setVal("t" + i);
						i++;
					} else
						node.setVal(node.getChilds().get(0).getVal());
					break;
				case ("multexpr"):
					transiton(tmpnode, tokens);
					if (node.getChilds().get(1).getChilds().size() != 1) {
						if ((node.getChilds().get(1).getChilds().get(0).getVal().equals("*")))
							System.out.println("mul t" + i + "," + node.getChilds().get(0).getVal() + "," + node.getChilds().get(1).getVal());
						else
							System.out.println("div t" + i + "," + node.getChilds().get(0).getVal() + "," + node.getChilds().get(1).getVal());
						node.setVal("t" + i);
						i++;
					} else
						node.setVal(node.getChilds().get(0).getVal());
					break;
				case ("simpleexpr"):
					if (node.getChilds().size() != 1) {
						transiton(tmpnode, tokens);
						node.setVal(node.getChilds().get(1).getVal());
					} else
						node.setVal(node.getChilds().get(0).getVal());
					break;
				case ("multexprprime"):
					if (node.getChilds().size() != 1) {
						transiton(tmpnode, tokens);
						if (node.getChilds().get(2).getChilds().size() != 1) {
							if (node.getChilds().get(2).getChilds().get(0).getVal().equals("*"))
								System.out.println("mul t" + i + "," + node.getChilds().get(1).getVal() + "," + node.getChilds().get(2).getVal());
							else
								System.out.println("div t" + i + "," + node.getChilds().get(1).getVal() + "," + node.getChilds().get(2).getVal());
							node.setVal("t" + i);
							i++;
						} else
							node.setVal(node.getChilds().get(1).getVal());
					}
					break;
				case ("arithexprprime"):
					if (node.getChilds().size() != 1) {
						transiton(tmpnode, tokens);
						if (node.getChilds().get(2).getChilds().size() != 1) {
							if (node.getChilds().get(2).getChilds().get(0).getVal().equals("+"))
								System.out.println("add t" + i + "," + node.getChilds().get(1).getVal() + "," + node.getChilds().get(2).getVal());
							else
								System.out.println("sub t" + i + "," + node.getChilds().get(1).getVal() + "," + node.getChilds().get(2).getVal());
							node.setVal("t" + i);
							i++;
						} else
							node.setVal(node.getChilds().get(1).getVal());
					}
					break;
				case ("decl"):
					node.setType(node.getChilds().get(0).getChilds().get(0).getVal());
					transiton(tmpnode, tokens);
					break;
				case ("type"):
					node.setType(node.getChilds().get(0).getVal());
					break;
				case ("list"):
					node.setType(treeNode.get(0).getType());
					node.getChilds().get(0).setType(treeNode.get(0).getType());
					transiton(tmpnode, tokens);
					break;
				case ("list1"):
					if (node.getChilds().size() != 1) {
						node.setType(treeNode.get(0).getType());
						node.getChilds().get(0).setType(treeNode.get(0).getType());
					}
					transiton(tmpnode, tokens);
					break;
				case ("ID"):
					for (Token token : tokens)
						if (token.getKey().equals(node.getVal()))
							if (node.getType() != "" && token.getValue() == "") {
								token.setValue(node.getType());
								token.setDecl(true);
							} else if (node.getType() != "" && token.getValue() != "") {
								System.out.println("重复申明变量" + node.getVal());
								token.setDecl(false);
							} else if (token.getValue() == "") {
								System.out.println("未申明变量" + node.getVal());
								token.setDecl(false);
							} else
								node.setType(token.getValue());
					break;
			}
		}
	}
}

