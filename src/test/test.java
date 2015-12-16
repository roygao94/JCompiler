package test;

import io.Pair;
import processing.CodeParser;
import processing.Lexer;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

/**
 * Created by Roy Gao on 11/26/2015.
 */
public class test {

	// I'm another comment!
	// Hello, Kevin, this is a comment.

	public static void main(String[] args) throws IOException {
//		用文件构造
//		Lexer lexer = new Lexer("source.txt");
//		用List构造
		List<String> code = Lexer.readCode("source.txt");
		Lexer lexer = new Lexer(code);

		List<Pair<String, String>> list = lexer.getCodeList();
		System.out.println(list.size());
		List<List<String>> codeTable = Lexer.parse(list);
		System.out.println(codeTable.size());

		for (List<String> line : codeTable) {
			for (String token : line)
				System.out.print(token + " ");
			System.out.println();
		}

		Map<Pair<String, String>, String> map = new HashMap<Pair<String, String>, String>() {{
			put(new Pair<>("aa", "BB"), "cc");
			put(new Pair<>("AA", "bb"), "dd");
		}};

		System.out.println(map.get(new Pair<>("aa", "BB")));
	}
}
