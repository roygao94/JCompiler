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

	public static void main(String[] args) throws IOException {
		Node n = new Node("Program");
		List<String> stack_info = new ArrayList<>();
		List<String> input_info = new ArrayList<>();
		List<String> output_info = new ArrayList<>();

		scan(n, stack_info, input_info, output_info);
		for(int i =0;i<stack_info.size();i++)
		{
			System.out.print(stack_info.get(i)+"\t\t\t\t\t\t\t\t");
			System.out.print(input_info.get(i)+"\t\t\t\t\t\t\t\t");
			System.out.println(output_info.get(i));
		}
		DrawTree(n);
	}

	public static void scan(Node n, List<String> stack_info, List<String> input_info, List<String> output_info) throws IOException {
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

		//判断token是否读到末尾
		boolean tokenflag = true;
		String top;
		String tmp_stack_info = "";
		String tmp_input_info = "";
		String tmp_output_info = "";

		//读取文件
		List<Pair<String, String>> list = Lexer.scan("test.txt");
		List<List<String>> codeTable = CodeParser.parse(list);
		codeTable.get(codeTable.size() - 1).add("$");

		//输入信息
		for (List<String> line : codeTable) {
			for (String token : line) {
				tmp_input_info += token;
			}
		}

		while (!stack.isEmpty() && tokenflag) {
			for (List<String> line : codeTable) {
				for (String token : line) {

					//输出当前栈信息
					tmpstack = (Stack<String>) stack.clone();
					while (!tmpstack.isEmpty()) {
						tmp_stack_info = tmpstack.pop() + tmp_stack_info;
					}
					stack_info.add(tmp_stack_info);
					tmp_stack_info = "";

					//输出当前输入信息
					input_info.add(tmp_input_info);
					if (tmp_output_info != "")
						output_info.add(tmp_output_info);

					if (token.equals("$"))
						tokenflag = false;

					while (true) {
						top = stack.pop();
						//栈顶与token相同，则跳出读下一个token
						if (top.equals(token)) {
							tmp_input_info = tmp_input_info.substring(token.length());
							tmp_output_info = "匹配";
							treeStack.pop();
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
								output_info.add("error token " + token);
								break;
							}
						}

						//输出分析后栈信息
						tmpstack = (Stack<String>) stack.clone();
						while (!tmpstack.isEmpty()) {
							tmp_stack_info = tmpstack.pop() + tmp_stack_info;
						}
						stack_info.add(tmp_stack_info);
						tmp_stack_info = "";
						input_info.add(tmp_input_info);
						output_info.add(tmp_output_info);
					}
				}
			}
		}

		if (stack.isEmpty())
			output_info.add("分析成功");
		else
			output_info.add("分析失败");
	}

	public static void DrawTree(Node n) {
		DrawTree frame = new DrawTree(n);
		frame.setSize(800, 1000);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

}