package processing;

import io.Pair;
import io.Token;

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

	public static final Set<String> keywords = new HashSet<String>() {{
		add("int");
		add("real");
		add("if");
		add("then");
		add("else");
		add("while");
	}};

	public static final Set<Character> operators = new HashSet<Character>() {{
		add('+');
		add('-');
		add('/');
		add('*');
		add('=');
		add('<');
		add('>');
		add('!');
	}};

	public static final Set<Character> delimeters = new HashSet<Character>() {{
		add(',');
		add('(');
		add(')');
		add('{');
		add('}');
		add(';');
	}};

	// tokens 是一个list，记录所有出现的token，可以重复
	private List<String> tokens = new ArrayList<>();
	// map记录<token, type>的对应关系
	private Map<String, String> map = new HashMap<>();

	private List<Pair<String, String>> codeList = new ArrayList<>();

	private List<List<Pair<String, String>>> originalCode = new ArrayList<>();

	private List<List<String>> formatedCode = new ArrayList<>();

	/**
	 * 通过指定代码文件路径构造词法分析
	 *
	 * @param path 指定代码文件路径
	 * @throws IOException
	 */
	public Lexer(String path) throws IOException {
		scan(readCode(path));
	}

	/**
	 * 通过代码构造词法分析
	 *
	 * @param code 代码
	 */
	public Lexer(List<String> code) {
		scan(code);
	}

	/**
	 * 根据制定代码文件路径读取代码
	 *
	 * @param path 指定代码文件路径
	 * @return 读取的代码
	 * @throws IOException
	 */
	public static List<String> readCode(String path) throws IOException {
		BufferedReader reader = new BufferedReader(new FileReader(path));
		List<String> code = new ArrayList<>();
		String line;
		while ((line = reader.readLine()) != null)
			code.add(line);

		return code;
	}

	/**
	 * 词法分析
	 *
	 * @param code 代码
	 */
	private void scan(List<String> code) {

		List<Pair<String, String>> originalLine = new ArrayList<>();
		List<String> formatedLine = new ArrayList<>();
		for (String line : code) {

			for (int i = 0; i < line.length(); ) {
				// 扫空格
				for (; i < line.length() && (line.charAt(i) == ' ' || line.charAt(i) == '\t'); ++i) ;
				if (i >= line.length()) break;

				if (line.charAt(i) == '/' && i + 1 < line.length() && line.charAt(i + 1) == '/') {
					// comment
					String comment = line.substring(i);
					tokens.add(comment);
					map.put(comment, COMM);
					i = line.length();

					originalLine.add(new Pair<>(comment, comment));
					formatedLine.add(comment);

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
					if (!hasDigit && keywords.contains(word)) {
						// word is a keyword
						map.put(word, KEY);
						originalLine.add(new Pair<>(word, word));
						formatedLine.add(word);
					} else {
						// word is an identifier
						map.put(word, ID);
						originalLine.add(new Pair<>(word, "ID"));
						formatedLine.add("ID");
					}

				} else if (operators.contains(line.charAt(i))) {
					// operators
					String operator = "" + line.charAt(i++);
					if (i < line.length() && line.charAt(i) == '=')
						operator += line.charAt(i++);
					tokens.add(operator);
					map.put(operator, OP);

					originalLine.add(new Pair<>(operator, operator));
					formatedLine.add(operator);

				} else if (delimeters.contains(line.charAt(i))) {
					// delimeters
					tokens.add("" + line.charAt(i));
					map.put("" + line.charAt(i), DEL);
					if (line.charAt(i) == '{') {
						originalLine.add(new Pair<>("{", "{"));
						formatedLine.add("{");
						originalCode.add(originalLine);
						originalLine = new ArrayList<>();
						formatedCode.add(formatedLine);
						formatedLine = new ArrayList<>();

					} else if (line.charAt(i) == '}') {
						if (!formatedLine.isEmpty()) {
							originalCode.add(originalLine);
							originalLine = new ArrayList<>();
							formatedCode.add(formatedLine);
							formatedLine = new ArrayList<>();
						}
						originalLine.add(new Pair<>("}", "}"));
						formatedLine.add("}");
						originalCode.add(originalLine);
						originalLine = new ArrayList<>();
						formatedCode.add(formatedLine);
						formatedLine = new ArrayList<>();

					} else {
						originalLine.add(new Pair<>("" + line.charAt(i), "" + line.charAt(i)));
						formatedLine.add("" + line.charAt(i));
						if (line.charAt(i) == ';') {
							originalCode.add(originalLine);
							originalLine = new ArrayList<>();
							formatedCode.add(formatedLine);
							formatedLine = new ArrayList<>();
						}
					}
					i++;

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

					originalLine.add(new Pair<>(number, "NUM"));
					formatedLine.add("NUM");

				} else {
					System.out.print(line + "," + i);
					System.out.println("error");
					i = line.length();
				}
			}

//			if (!formatedLine.isEmpty()) {
//				originalCode.add(originalLine);
//				formatedCode.add(formatedLine);
//			}
		}


		for (String token : tokens)
			codeList.add(new Pair<>(token, map.get(token)));
	}

	/**
	 * 获取所有出现的token（有重复）
	 *
	 * @return 所有出现的token
	 */
	public List<String> getTokens() {
		return tokens;
	}

	/**
	 * 获取记录token和终止符对应关系的map
	 *
	 * @return 记录token和终止符对应关系的map
	 */
	public Map<String, String> getMap() {
		return map;
	}

	/**
	 * 获取没有分行的代码（token和对应终结符）
	 *
	 * @return 没有分行的代码
	 */
	public List<Pair<String, String>> getCodeList() {
		return codeList;
	}

	/**
	 * 获取又分行的代码（token和对应终结符）
	 *
	 * @return 有分行的代码
	 */
	public List<List<Pair<String, String>>> getOriginalCode() {
		return originalCode;
	}

	/**
	 * 获取又分行的代码
	 *
	 * @return 有分行的代码
	 */
	public List<List<String>> getFormatedCode() {
		return formatedCode;
	}

	/**
	 * 对代码分行
	 *
	 * @param list 未分行的代码
	 * @return 分好行的代码
	 */
	public static List<List<String>> parse(List<Pair<String, String>> list) {
		List<List<String>> codeTable = new ArrayList<>();
		List<String> currLine = new ArrayList<>();

		for (Pair<String, String> token : list) {
			if (token.getSecond().equals("identifier"))
				currLine.add("ID");
			else if (token.getSecond().equals("number"))
				currLine.add("NUM");
			else currLine.add(token.getFirst());
			if (token.getFirst().equals(";") || token.getFirst().equals("{") || token.getFirst().equals("}")
					|| token.getSecond().equals("comment")) {
				codeTable.add(currLine);
				currLine = new ArrayList<>();
			}
		}

		return codeTable;
	}

	/**
	 * 获取符号表
	 *
	 * @return 符号表
	 */
	public List<Token> getTokenList() {
		List<Token> list = new ArrayList<>();
		Set<String> set = new HashSet<>();
		for (String token : tokens)
			if (map.get(token).equals(ID) && !set.contains(token)) {
				list.add(new Token(token, map.get(token)));
				set.add(token);
			}

		return list;
	}

//	public List<String> beautifulCode(List<List<Pair<String, String>>> originalCode) {}
}
