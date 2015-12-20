package processing;

import io.Pair;
import tree.*;

import javax.swing.*;
import java.io.IOException;
import java.util.*;

/**
 * Created by He on 2015/11/25.
 */
public class Syntax {
	private static Map<Pair<String, String>, String> map = new HashMap<Pair<String, String>, String>() {
		{
			put(new Pair<>("program", "{"), "compoundstmt");

			put(new Pair<>("stmt", "{"), "compoundstmt");
			put(new Pair<>("stmt", "if"), "ifstmt");
			put(new Pair<>("stmt", "while"), "whilestmt");
			put(new Pair<>("stmt", "int"), "decl");
			put(new Pair<>("stmt", "real"), "decl");
			put(new Pair<>("stmt", "ID"), "assgstmt");

			put(new Pair<>("compoundstmt", "{"), "{ stmts }");

			put(new Pair<>("stmts", "{"), "stmt stmts");
			put(new Pair<>("stmts", "}"), "");
			put(new Pair<>("stmts", "if"), "stmt stmts");
			put(new Pair<>("stmts", "while"), "stmt stmts");
			put(new Pair<>("stmts", "int"), "stmt stmts");
			put(new Pair<>("stmts", "real"), "stmt stmts");
			put(new Pair<>("stmts", "ID"), "stmt stmts");

			put(new Pair<>("ifstmt", "if"), "if ( boolexpr ) then stmt else stmt");

			put(new Pair<>("whilestmt", "while"), "while ( boolexpr ) stmt");

			put(new Pair<>("assgstmt", "ID"), "ID = arithexpr ;");

			put(new Pair<>("decl", "int"), "type list ;");
			put(new Pair<>("decl", "real"), "type list ;");

			put(new Pair<>("type", "int"), "int");
			put(new Pair<>("type", "real"), "real");

			put(new Pair<>("list", "ID"), "ID list1");

			put(new Pair<>("list1", ","), ", list");
			put(new Pair<>("list1", ";"), "");

			put(new Pair<>("boolexpr", "("), "arithexpr boolop arithexpr");
			put(new Pair<>("boolexpr", "ID"), "arithexpr boolop arithexpr");
			put(new Pair<>("boolexpr", "NUM"), "arithexpr boolop arithexpr");

			put(new Pair<>("boolop", "<"), "<");
			put(new Pair<>("boolop", ">"), ">");
			put(new Pair<>("boolop", "<="), "<=");
			put(new Pair<>("boolop", ">="), ">=");
			put(new Pair<>("boolop", "=="), "==");

			put(new Pair<>("arithexpr", "("), "multexpr arithexprprime");
			put(new Pair<>("arithexpr", "ID"), "multexpr arithexprprime");
			put(new Pair<>("arithexpr", "NUM"), "multexpr arithexprprime");

			put(new Pair<>("arithexprprime", ")"), "");
			put(new Pair<>("arithexprprime", ";"), "");
			put(new Pair<>("arithexprprime", "+"), "+ multexpr arithexprprime");
			put(new Pair<>("arithexprprime", "-"), "- multexpr arithexprprime");
			put(new Pair<>("arithexprprime", "<"), "");
			put(new Pair<>("arithexprprime", ">"), "");
			put(new Pair<>("arithexprprime", "<="), "");
			put(new Pair<>("arithexprprime", ">="), "");
			put(new Pair<>("arithexprprime", "=="), "");

			put(new Pair<>("multexpr", "("), "simpleexpr multexprprime");
			put(new Pair<>("multexpr", "ID"), "simpleexpr multexprprime");
			put(new Pair<>("multexpr", "NUM"), "simpleexpr multexprprime");

			put(new Pair<>("multexprprime", ")"), "");
			put(new Pair<>("multexprprime", ";"), "");
			put(new Pair<>("multexprprime", "+"), "");
			put(new Pair<>("multexprprime", "-"), "");
			put(new Pair<>("multexprprime", "*"), "* simpleexpr multexprprime");
			put(new Pair<>("multexprprime", "/"), "/ simpleexpr multexprprime");
			put(new Pair<>("multexprprime", ">"), "");
			put(new Pair<>("multexprprime", "<"), "");
			put(new Pair<>("multexprprime", ">="), "");
			put(new Pair<>("multexprprime", "<="), "");
			put(new Pair<>("multexprprime", "=="), "");

			put(new Pair<>("simpleexpr", "("), "( arithexpr )");
			put(new Pair<>("simpleexpr", "ID"), "ID");
			put(new Pair<>("simpleexpr", "NUM"), "NUM");
		}
	};

	public static final Set<String> stmt = new HashSet<String>() {{
		add("decl");
		add("ifstmt");
		add("whilestmt");
		add("assgstmt");
	}};

	public static void main(String[] args) throws IOException {
		Node n = new Node("program");
		List<Pair<String, String>> stack_info = new ArrayList<>();
		List<String> input_info = new ArrayList<>();
		List<String> output_info = new ArrayList<>();

		Lexer lexer = new Lexer("test.txt");
		scan(n, stack_info, input_info, output_info, lexer);

		for (int i = 0; i < stack_info.size(); i++) {
			System.out.print(stack_info.get(i) + "\t\t\t\t\t\t\t\t");
			System.out.print(input_info.get(i) + "\t\t\t\t\t\t\t\t");
			System.out.println(output_info.get(i));
		}
		DrawTree(n);
	}

	public static List<Node> scan(Node n, List<Pair<String, String>> stack_info, List<String> input_info, List<String> output_info, Lexer lexer) {

		//过程栈
		Stack<String> stack = new Stack<>();
		Stack<String> tmpstack;
		stack.push("$");
		stack.push("program");

		//树栈
		Stack<Node> treeStack = new Stack<>();
		treeStack.push(new Node("$"));
		treeStack.push(n);

		Node tmpnode;
		int nowline = 1;
		int i;

		List<Node> treeNode = new ArrayList<>();

		String top;
		String tmp_stack_info = "";
		String tmp_input_info = "";
		String tmp_output_info = "";

		//读取文件
		List<List<Pair<String, String>>> codeTable = lexer.getOriginalCode();
		codeTable.get(codeTable.size() - 1).add(new Pair<>("$", "$"));

		for (List<Pair<String, String>> line : codeTable) {

			//获得当前输入行信息
			for (Pair<String, String> token : line)
				tmp_input_info += token.getFirst();

			//读取每行的一个token
			for (i = 0; i < line.size(); i++) {

				String token = line.get(i).getSecond();

				boolean errorflag = true;
				int length = 0;

				//输出当前栈信息
				tmpstack = (Stack<String>) stack.clone();
				while (!tmpstack.isEmpty())
					tmp_stack_info = tmpstack.pop() + tmp_stack_info;
				stack_info.add(new Pair<>(nowline + "", tmp_stack_info));
				tmp_stack_info = "";

				//输出当前输入输出信息
				input_info.add(tmp_input_info);
				if (tmp_output_info != "")
					output_info.add(tmp_output_info);

				while (true) {

					top = stack.pop();
					//栈顶与token相同，则跳出读下一个token
					if (top.equals(token)) {
						tmp_input_info = tmp_input_info.substring(line.get(i).getFirst().length());
						tmp_output_info = "match";
						tmpnode = treeStack.pop();
						tmpnode.setVal(line.get(i).getFirst());
						break;
					} else {
						//查LL表
						String val = map.get(new Pair<>(top, token));

						//存在文法则压栈
						if (val != null) {

							if (val == "")
								val = "empty";

							//将文法spilt后按序压栈
							String[] split = val.split(" ");
							List<Node> tmplist = new ArrayList<>();
							tmpnode = treeStack.pop();

							//
							if (stmt.contains(tmpnode.getName())) {
								treeNode.add(tmpnode);
							}

							//将子节点加入到父亲节点后
							for (int j = 0; j <= split.length - 1; j++) {
								Node a = new Node(split[j]);
								tmplist.add(a);
								tmpnode.add(a);
							}

							for (int j = tmplist.size() - 1; j >= 0; j--) {
								if (!val.equals("empty")) {
									stack.push(tmplist.get(j).getName());
									treeStack.push(tmplist.get(j));
								}
							}
							tmp_output_info = top + "->" + val;
						}
						//不存在则相应报错
						else {
							//处理缺失"{"
							if (top.equals("program")) {
								treeStack.pop();
								stack.push("}");
								treeStack.push(new Node("}"));
								stack.push("stmts");
								treeStack.push(new Node("stmts"));
								tmp_output_info = "line " + nowline + " error token " + line.get(i).getFirst() + " ,may need {";
								output_info.add(tmp_output_info);
								tmp_output_info = "";
							} else {
								stack.push(top);

								tmp_output_info = "line " + nowline + " error token " + line.get(i).getFirst() + " ,may need ";
								String ptokenString = "";
								//输出可能的符号
								for (String ptoken : Lexer.keywords) {
									if (map.get(new Pair<>(top, ptoken)) != null)
										ptokenString += ptoken + " ";
								}
								for (char ptoken : Lexer.operators) {
									if (map.get(new Pair<>(top, ptoken + "")) != null)
										ptokenString += ptoken + " ";
								}
								for (char ptoken : Lexer.delimeters) {
									if (map.get(new Pair<>(top, ptoken + "")) != null)
										ptokenString += ptoken + " ";
								}

								if (map.get(new Pair<>(top, "ID")) != null)
									ptokenString += "ID ";
								if (map.get(new Pair<>(top, "NUM")) != null)
									ptokenString += "NUM ";

								if (ptokenString.equals(""))
									ptokenString += top;
								tmp_output_info += ptokenString;
								output_info.add(tmp_output_info);
								tmp_output_info = "";

								//回滚到上一次的stmts
								while (!stack.peek().equals("stmts"))
									stack.pop();

								//跳过此行
								for (int j = i; j < line.size(); j++)
									length += line.get(j).getFirst().length();
								tmp_input_info = tmp_input_info.substring(length);
								errorflag = false;
								break;
							}
						}
					}

					//输出成功分析后栈信息
					tmpstack = (Stack<String>) stack.clone();
					while (!tmpstack.isEmpty()) {
						tmp_stack_info = tmpstack.pop() + tmp_stack_info;
					}
					stack_info.add(new Pair<>(nowline + "", tmp_stack_info));
					tmp_stack_info = "";
					input_info.add(tmp_input_info);
					if (tmp_output_info != "")
						output_info.add(tmp_output_info);
				}

				//单独处理缺失";"
				if (i == line.size() - 1 && !stack.isEmpty() && !stack.peek().equals("stmts") && !stack.peek().equals("else")) {

					String ptoken = ";";
					if (stack.peek().equals("then"))
						ptoken = "then";
					if (stack.peek().equals("else"))
						ptoken = "else";

					tmpstack = (Stack<String>) stack.clone();
					while (!tmpstack.isEmpty())
						tmp_stack_info = tmpstack.pop() + tmp_stack_info;
					stack_info.add(new Pair<>(nowline + "", tmp_stack_info));
					tmp_stack_info = "";
					input_info.add(tmp_input_info);
					if (tmp_output_info != "")
						output_info.add(tmp_output_info);

					while (!stack.peek().equals("stmts"))
						stack.pop();
					tmp_output_info = "line " + nowline + " error line ,may need " + ptoken;
					output_info.add(tmp_output_info);
					tmp_output_info = "";
					break;
				}

				//本行出错，跳过本行
				if (!errorflag && i != line.size() - 1)
					break;
			}
			nowline++;
		}

		if (stack.isEmpty())
			output_info.add("success");
		else
			output_info.add("fail");


//		for (Node node : treeNode)
//			DrawTree(node);
		return treeNode;

	}

	public static void DrawTree(Node n) {
		DrawTree frame = new DrawTree(n);
		frame.setSize(800, 1000);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	}

}