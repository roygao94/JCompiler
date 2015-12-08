package processing;

import io.Pair;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

/**
 * Created by Roy Gao on 11/18/2015.
 */
public class Lexer {

	private static final String COMM = "comment";
	private static final String KEY = "keyword";
	private static final String ID = "identifier";
	private static final String OP = "operator";
	private static final String DEL = "delimeter";
	private static final String NUM = "number";

	private static final Set<String> keywords = new HashSet<String>() {{
		add("int");
		add("real");
		add("if");
		add("then");
		add("else");
		add("while");
	}};

	private static final Set<Character> operators = new HashSet<Character>() {{
		add('+');
		add('-');
		add('/');
		add('*');
		add('=');
		add('<');
		add('>');
		add('!');
	}};

	private static final Set<Character> delimeters = new HashSet<Character>() {{
		add('(');
		add(')');
		add('{');
		add('}');
		add(';');
	}};

	public static void main(String[] args) throws IOException {
		List<Pair<String, String>> list = scan("source.txt");
		for (Pair<String, String> token : list)
			System.out.println(token);
	}

	public static List<Pair<String, String>> scan(String sourceFile) throws IOException {
		// tokens 是一个list，记录所有出现的token，可以重复
		List<String> tokens = new ArrayList<>();
		// map记录<token, type>的对应关系
		Map<String, String> map = new HashMap<>();

		BufferedReader reader = new BufferedReader(new FileReader(sourceFile));

		String line;
		while ((line = reader.readLine()) != null) {
			for (int i = 0; i < line.length(); ) {
				// 扫空格
				for (; i < line.length() && (line.charAt(i) == ' ' || line.charAt(i) == '\t'); ++i) ;

				if (line.charAt(i) == '/' && i + 1 < line.length() && line.charAt(i + 1) == '/') {
					// comment
					String comment = line.substring(i);
					tokens.add(comment);
					map.put(comment, COMM);
					i = line.length();

				} else if (Character.isLetter(line.charAt(i))) {
					// keyword or identifiers
					String word = "";
					boolean hasDigit = false;

					while (i < line.length() && Character.isLetter(line.charAt(i)))
						word += line.charAt(i++);
					if (i < line.length() && Character.isDigit(line.charAt(i))) {
						hasDigit = true;
						while (i < line.length() && Character.isLetterOrDigit(line.charAt(i)))
							word += line.charAt(i++);
					}

					tokens.add(word);
					if (!hasDigit && keywords.contains(word))
						// word is a keyword
						map.put(word, KEY);
					else
						// word is an identifier
						map.put(word, ID);

				} else if (operators.contains(line.charAt(i))) {
					// operators
					String operator = "" + line.charAt(i++);
					if (i < line.length() && line.charAt(i) == '=')
						operator += line.charAt(i++);
					tokens.add(operator);
					map.put(operator, OP);

				} else if (delimeters.contains(line.charAt(i))) {
					// delimeters
					tokens.add("" + line.charAt(i));
					map.put("" + line.charAt(i++), DEL);

				} else if (Character.isDigit(line.charAt(i))) {
					// numbers
					String number = "";
					// digit+
					while (i < line.length() && Character.isDigit(line.charAt(i)))
						number += line.charAt(i++);
					if (i < line.length() && line.charAt(i) == '.') {
						// digit+ fraction
						number += line.charAt(i++);
						while (i < line.length() && Character.isDigit(line.charAt(i)))
							number += line.charAt(i++);
						if (i < line.length() && (line.charAt(i) == 'E' || line.charAt(i) == 'e')) {
							// digit+ fraction exponent
							number += line.charAt(i++);
							if (i < line.length() && (line.charAt(i) == '+' || line.charAt(i) == '-'))
								number += line.charAt(i++);
							while (i < line.length() && Character.isDigit(line.charAt(i)))
								number += line.charAt(i++);
						}
					} else if (i < line.length() && (line.charAt(i) == 'E' || line.charAt(i) == 'e')) {
						// digit+ exponent
						number += line.charAt(i++);
						if (i < line.length() && (line.charAt(i) == '+' || line.charAt(i) == '-'))
							number += line.charAt(i++);
						while (i < line.length() && Character.isDigit(line.charAt(i)))
							number += line.charAt(i++);
					}

					tokens.add(number);
					map.put(number, NUM);
				} else {
					System.out.print(line + "," + i);
					System.out.println("error");
					i = line.length();
				}
			}
		}

		reader.close();

		List<Pair<String, String>> returnList = new ArrayList<>();
		for (String token : tokens)
			returnList.add(new Pair<>(token, map.get(token)));

		return returnList;
	}

}
