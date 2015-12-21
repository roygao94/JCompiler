/**
 * 2010-11-8
 * John
 */
package tree;

import io.Position;

import java.util.ArrayList;
import java.util.List;

/**
 * 树的结构
 *
 * @author John
 */
public class Node {
	private String name;    //该结点名字
	private int layer = 0;    //该结点层级
	private String val = "";      //该结点的值
	private String type = "";      //该结点的类型
	private int tokenline = 1;

	private List<Node> childs = null;    //保存该结点的孩子

	private Node parent = null;

	private Position position;

	public Node(String name) {
		this.name = name;
	}


	/**
	 * 增加一个孩子
	 *
	 * @param n 要作为孩子增加的结点
	 */
	public void add(Node n) {
		if (childs == null)
			childs = new ArrayList<>();
		n.setLayer(layer + 1);
		setChildLayout(n);
		childs.add(n);
		n.parent = this;
	}


	/**
	 * 递归设置孩子的层级
	 *
	 * @param n
	 */
	private void setChildLayout(Node n) {
		if (n.hasChild()) {
			List<Node> c = n.getChilds();
			for (Node node : c) {
				node.setLayer(node.getLayer() + 1);
				setChildLayout(node);
			}
		}
	}

	/**
	 * 获取结点名
	 *
	 * @return 结点名
	 */
	public String getName() {
		return name;
	}

	public String getVal() {
		return val;
	}

	public String getType() {
		return type;
	}

	public int getLine() {
		return tokenline;
	}

	/**
	 * 设置结点名
	 *
	 * @param name 结点名
	 */
	public void setName(String name) {
		this.name = name;
	}

	public void setVal(String val) {
		this.val = val;
	}

	public void setType(String type) {
		this.type = type;
	}

	/**
	 * 获取该结点的层级
	 *
	 * @return 该结点的层级
	 */
	public int getLayer() {
		return layer;
	}

	/**
	 * 设置该结点的层级
	 *
	 * @param layer 该结点的层级
	 */
	public void setLayer(int layer) {
		this.layer = layer;
	}

	/**
	 * 获取该结点的孩子
	 *
	 * @return 所有孩子结点
	 */
	public List<Node> getChilds() {	return childs; }

	/**
	 * 检查是否存在祖先关系
	 *
	 * @return 是则返回true，否则返回false
	 */
	public boolean hasChild() {
		return childs == null ? false : true;
	}

	/**
	 * 判断是否为祖先关系
	 *
	 * @param node 可能的子孙节点
	 */
	public boolean isAncestorOf(Node node) {
		if (node.getLayer() <= this.getLayer())
			return false;
		for (Node child : this.getChilds())
			if (child == node || child.isAncestorOf(node))
				return true;
		return false;
	}

	/**
	 * 递归打印所有的结点（包括子结点）
	 *
	 * @param n 要打印的根结点
	 */
	public void printAllNode(Node n) {
		System.out.println(n.toString());
		if (n.hasChild()) {
			List<Node> c = n.getChilds();
			for (Node node : c) {
				printAllNode(node);
			}
		}
	}

	public String getAllNodeName(Node n) {
		String s = n.toString() + "/n";
		if (n.hasChild()) {
			List<Node> c = n.getChilds();
			for (Node node : c) {
				s += getAllNodeName(node) + "/n";
			}
		}
		return s;
	}

	public String toString() {
		return name + "@" + val + "@" + type;
	}

	/**
	 * 设置坐标
	 *
	 * @param x 横坐标X
	 * @param y 纵坐标Y
	 */
	public void setPosition(int x, int y) {
		position = new Position(x, y);
	}

	/**
	 * 获取横坐标X
	 *
	 * @return 横坐标X
	 */
	public int getX() {
		return position.getX();
	}

	/**
	 * 获取纵坐标Y
	 *
	 * @return 纵坐标Y
	 */
	public int getY() {
		return position.getY();
	}

	/**
	 * 获取父节点
	 *
	 * @return 父节点
	 */
	public Node getParent() {
		return parent;
	}
}


