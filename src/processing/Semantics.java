package processing;

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
	static int j = 1;

	private List<String> threeAddressInfo = new ArrayList<>();
	private List<String> errorInfo = new ArrayList<>();

	public List<String> getThreeAddressInfo() {
		return threeAddressInfo;
	}

	public List<String> getErrorInfo() {
		return errorInfo;
	}

	public Semantics(List<Node> treeNode, List<Token> tokens) {
		transition(treeNode, tokens);
	}

//	public static void main(String[] args) throws IOException{
//		Node n = new Node("program");
//
//		Lexer lexer = new Lexer("test.txt");
//		List<Token> _tokens = lexer.getTokenList();
//
//		Syntax syntax = new Syntax(n, lexer);
//		List<Node> _treeNode = syntax.getTreeNode();
//		List<String> _syntaxError = syntax.getErrorInfo();
//
//		Semantics semantics;
//
//		if (_syntaxError.size() == 0) {
//			semantics = new Semantics(_treeNode, _tokens);
//			List<String> _threeAddressInfo = semantics.getThreeAddressInfo();
//			List<String> _semanticsErrorInfo = semantics.getErrorInfo();
//			if (_semanticsErrorInfo.size() == 0)
//				for (String string : _threeAddressInfo)
//					System.out.print(string);
//			else
//				for (String string : _semanticsErrorInfo)
//					System.out.print(string);
//		} else
//			System.out.println("语法分析出错！");
//
//		System.out.println("Token list size :\t" + _tokens.size());
//		for (Token token : _tokens)
//			System.out.println(token);
//	}

	private void transition(List<Node> treeNode, List<Token> tokens) {
		String stmt;

		for (Node node : treeNode) {
			List<Node> tmpnode = node.getChilds();
			stmt = node.getName();
			switch (stmt) {
				case ("compoundstmt"):
					transition(tmpnode, tokens);
					break;
				case ("stmt"):
					transition(tmpnode, tokens);
					if (treeNode.get(0).getName().equals("while")) {
						//System.out.println("jump ,," + treeNode.get(0).getVal());
						threeAddressInfo.add("jump ,," + treeNode.get(0).getVal() + "\n");
						//System.out.print(node.getVal() + " ");
						threeAddressInfo.add(node.getVal() + " ");
					}
					break;
				case ("stmts"):
					transition(tmpnode, tokens);
					break;
				case ("assgstmt"):
					transition(tmpnode, tokens);
					if (node.getChilds().get(0).getType().equals("int") && node.getChilds().get(2).getVal().indexOf(".") != -1)
						//System.out.println("real类型值无法赋值给int");
						errorInfo.add("Real value can't assign to type int" + "\n");
					else {
						for (Token token : tokens)
							if (token.getKey().equals(node.getChilds().get(0).getVal()) && token.isDecl())
								//System.out.println("mov " + node.getChilds().get(0).getVal() + ",," + node.getChilds().get(2).getVal());
								threeAddressInfo.add("mov " + node.getChilds().get(0).getVal() + ",," + node.getChilds().get(2).getVal() + "\n");
					}
					node.setName("assgstmt_finish");
					break;
				case ("arithexpr"):
					transition(tmpnode, tokens);
					if (node.getChilds().get(1).getChilds().size() != 1) {
						if ((node.getChilds().get(1).getChilds().get(0).getVal().equals("+")))
							//System.out.println("add t" + i + "," + node.getChilds().get(0).getVal() + "," + node.getChilds().get(1).getVal());
							threeAddressInfo.add("add t" + i + "," + node.getChilds().get(0).getVal() + "," + node.getChilds().get(1).getVal() + "\n");
						else
							//System.out.println("sub t" + i + "," + node.getChilds().get(0).getVal() + "," + node.getChilds().get(1).getVal());
							threeAddressInfo.add("sub t" + i + "," + node.getChilds().get(0).getVal() + "," + node.getChilds().get(1).getVal() + "\n");
						node.setVal("t" + i);
						i++;
					} else
						node.setVal(node.getChilds().get(0).getVal());
					break;
				case ("multexpr"):
					transition(tmpnode, tokens);
					if (node.getChilds().get(1).getChilds().size() != 1) {
						if ((node.getChilds().get(1).getChilds().get(0).getVal().equals("*")))
							//System.out.println("mul t" + i + "," + node.getChilds().get(0).getVal() + "," + node.getChilds().get(1).getVal());
							threeAddressInfo.add("mul t" + i + "," + node.getChilds().get(0).getVal() + "," + node.getChilds().get(1).getVal() + "\n");
						else
							//System.out.println("div t" + i + "," + node.getChilds().get(0).getVal() + "," + node.getChilds().get(1).getVal());
							threeAddressInfo.add("div t" + i + "," + node.getChilds().get(0).getVal() + "," + node.getChilds().get(1).getVal() + "\n");
						node.setVal("t" + i);
						i++;
					} else
						node.setVal(node.getChilds().get(0).getVal());
					break;
				case ("simpleexpr"):
					if (node.getChilds().size() != 1) {
						transition(tmpnode, tokens);
						node.setVal(node.getChilds().get(1).getVal());
					} else
						node.setVal(node.getChilds().get(0).getVal());
					break;
				case ("multexprprime"):
					if (node.getChilds().size() != 1) {
						transition(tmpnode, tokens);
						if (node.getChilds().get(2).getChilds().size() != 1) {
							if (node.getChilds().get(2).getChilds().get(0).getVal().equals("*"))
								//System.out.println("mul t" + i + "," + node.getChilds().get(1).getVal() + "," + node.getChilds().get(2).getVal());
								threeAddressInfo.add("mul t" + i + "," + node.getChilds().get(1).getVal() + "," + node.getChilds().get(2).getVal() + "\n");
							else
								//System.out.println("div t" + i + "," + node.getChilds().get(1).getVal() + "," + node.getChilds().get(2).getVal());
								threeAddressInfo.add("div t" + i + "," + node.getChilds().get(1).getVal() + "," + node.getChilds().get(2).getVal() + "\n");
							node.setVal("t" + i);
							i++;
						} else
							node.setVal(node.getChilds().get(1).getVal());
					}
					break;
				case ("arithexprprime"):
					if (node.getChilds().size() != 1) {
						transition(tmpnode, tokens);
						if (node.getChilds().get(2).getChilds().size() != 1) {
							if (node.getChilds().get(2).getChilds().get(0).getVal().equals("+"))
								//System.out.println("add t" + i + "," + node.getChilds().get(1).getVal() + "," + node.getChilds().get(2).getVal());
								threeAddressInfo.add("add t" + i + "," + node.getChilds().get(1).getVal() + "," + node.getChilds().get(2).getVal() + "\n");
							else
								//System.out.println("sub t" + i + "," + node.getChilds().get(1).getVal() + "," + node.getChilds().get(2).getVal());
								threeAddressInfo.add("sub t" + i + "," + node.getChilds().get(1).getVal() + "," + node.getChilds().get(2).getVal() + "\n");
							node.setVal("t" + i);
							i++;
						} else
							node.setVal(node.getChilds().get(1).getVal());
					}
					break;
				case ("decl"):
					node.setType(node.getChilds().get(0).getChilds().get(0).getVal());
					transition(tmpnode, tokens);
					node.setName("decl_finish");
					break;
				case ("type"):
					node.setType(node.getChilds().get(0).getVal());
					break;
				case ("list"):
					node.setType(treeNode.get(0).getType());
					node.getChilds().get(0).setType(treeNode.get(0).getType());
					transition(tmpnode, tokens);
					break;
				case ("list1"):
					if (node.getChilds().size() != 1) {
						node.setType(treeNode.get(0).getType());
						node.getChilds().get(0).setType(treeNode.get(0).getType());
					}
					transition(tmpnode, tokens);
					break;
				case ("ID"):
					for (Token token : tokens)
						if (token.getKey().equals(node.getVal()))
							if (node.getType() != "" && token.getValue() == "") {
								token.setValue(node.getType());
								token.setDecl(true);
							} else if (node.getType() != "" && token.getValue() != "") {
								//System.out.println("重复申明变量" + node.getVal());
								errorInfo.add("Repeated declaration variable: " + node.getVal() + "\n");
								token.setDecl(false);
							} else if (node.getType() == "" && token.getValue() == "") {
								//System.out.println("未申明变量" + node.getVal());
								//errorInfo.add("Not declare variable: " + node.getVal() + "\n");
								token.setDecl(false);
							} else
								node.setType(token.getValue());
					break;
				case ("ifstmt"):
					transition(tmpnode, tokens);
					//System.out.print(node.getChilds().get(6).getVal() + " ");
					threeAddressInfo.add(node.getChilds().get(6).getVal() + " ");
					node.setName("ifstmt_finish");
					break;
				case ("boolexpr"):
					transition(tmpnode, tokens);
					String op = "";
					switch (node.getChilds().get(1).getChilds().get(0).getVal()) {
						case ("=="):
							op = "eq";
							break;
						case (">"):
							op = "gt";
							break;
						case ("<"):
							op = "lt";
							break;
						case (">="):
							op = "ge";
							break;
						case ("<="):
							op = "le";
							break;
					}
					//System.out.println(op + " t" + i + "," + node.getChilds().get(0).getVal() + "," + node.getChilds().get(2).getVal());
					threeAddressInfo.add(op + " t" + i + "," + node.getChilds().get(0).getVal() + "," + node.getChilds().get(2).getVal() + "\n");
					node.setVal("t" + i);
					i++;
					break;
				case ("then"):
					//System.out.println("jumpf " + treeNode.get(2).getVal() + ",,b" + j);
					threeAddressInfo.add("jumpf " + treeNode.get(2).getVal() + ",,b" + j + "\n");
					node.setVal("b" + j);
					j++;
					break;
				case ("else"):
					//System.out.println("jumpf ,,b" + j);
					threeAddressInfo.add("jumpf ,,b" + j + "\n");
					//System.out.print(treeNode.get(4).getVal() + " ");
					threeAddressInfo.add(treeNode.get(4).getVal() + " ");
					node.setVal("b" + j);
					j++;
					break;
				case ("whilestmt"):
					//System.out.print("b" + j + " ");
					threeAddressInfo.add("b" + j + " ");
					node.getChilds().get(0).setVal("b" + j);
					j++;
					transition(tmpnode, tokens);
					node.setName("whilestmt_finish");
					break;
				case (")"):
					if (treeNode.get(0).getName().equals("while")) {
						//System.out.println("jumpf " + treeNode.get(2).getVal() + ",," + "b" + j);
						threeAddressInfo.add("jumpf " + treeNode.get(2).getVal() + ",," + "b" + j + "\n");
						treeNode.get(4).setVal("b" + j);
						j++;
					}
					break;
			}
		}
	}
}