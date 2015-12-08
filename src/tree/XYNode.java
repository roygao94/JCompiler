package tree;

import io.Position;

import java.util.List;

/**
 * Created by Roy Gao on 12/7/2015.
 */
public class XYNode {
	private Position position;
	private List<XYNode> childs = null;

	public XYNode() {
		position = new Position(0, 0);
	}

	public XYNode(int x, int y) {
		position = new Position(x, y);
	}

	public void add(XYNode child) {
		childs.add(child);
	}
}
