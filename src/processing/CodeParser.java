package processing;

import io.Pair;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Roy Gao on 11/26/2015.
 */
public class CodeParser {

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
}
