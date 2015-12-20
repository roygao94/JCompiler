/**
 * 2010-11-9
 * John
 */
package tree;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

import javax.swing.JPanel;

/**
 * TODO 同一层结点过多有BUG，应该对每一层的所有结点都进行个数统计，之后才绘制。
 *
 * @author John
 */
public class TreePanel extends JPanel {

	private Node tree;                //保存整棵树
	private int gridWidth = 100;        //每个结点的宽度
	private int gridHeight = 20;    //每个结点的高度
	private int vGap = 80;            //每2个结点的垂直距离
	private int hGap = 30;            //每2个结点的水平距离

	private int startY = 10;        //根结点的Y，默认距离顶部10像素
	private int startX = 10;            //根结点的X，默认水平居中对齐

	private int childAlign;                        //孩子对齐方式
	public static int CHILD_ALIGN_ABSOLUTE = 0;    //相对Panel居中
	public static int CHILD_ALIGN_RELATIVE = 1;    //相对父结点居中

	private Font font = new Font("", Font.BOLD, 14);    //描述结点的字体

	private Color backgroundColor = Color.darkGray;
	private Color gridColor = Color.gray;        //结点背景颜色
	private Color linkLineColor = Color.white;    //结点连线颜色
	private Color stringColor = Color.black;    //结点描述文字的颜色

	/**
	 * 默认构造
	 */
	public TreePanel() {
		this(null, CHILD_ALIGN_ABSOLUTE);
	}

	/**
	 * 根据传入的Node绘制树，以绝对居中的方式绘制
	 *
	 * @param n 要绘制的树
	 */
	public TreePanel(Node n) {
		this(n, CHILD_ALIGN_ABSOLUTE);
	}

	/**
	 * 设置要绘制时候的对齐策略
	 *
	 * @param childAlign 对齐策略
	 * @see tree.TreePanel#CHILD_ALIGN_RELATIVE
	 * @see tree.TreePanel#CHILD_ALIGN_ABSOLUTE
	 */
	public TreePanel(int childAlign) {
		this(null, childAlign);
	}

	/**
	 * 根据孩子对齐策略childAlign绘制的树的根结点n
	 *
	 * @param n          要绘制的树的根结点
	 * @param childAlign 对齐策略
	 */
	public TreePanel(Node n, int childAlign) {
		super();
		setTree(n);
		this.childAlign = childAlign;
	}

	/**
	 * 设置用于绘制的树
	 *
	 * @param n 用于绘制的树的
	 */
	public void setTree(Node n) {
		tree = n;
	}

	//重写而已，调用自己的绘制方法
	public void paintComponent(Graphics g) {
		startX = (getWidth() - gridWidth) / 2;
		super.paintComponent(g);
		g.setFont(font);
		g.setColor(backgroundColor);
		g.fillRect(0, 0, getWidth(), getHeight());
//		drawAllNode(tree, startX, g);
		drawAllNodeBFS(tree, startX, g);
	}

	/**
	 * 递归绘制整棵树
	 *
	 * @param n    被绘制的Node
	 * @param x 根节点的绘制X位置
	 * @param g    绘图上下文环境
	 */
	public void drawAllNode(Node n, int x, Graphics g) {
		int y = n.getLayer() * (vGap + gridHeight) + startY;
		int fontY = y + gridHeight - 5;        //5为测试得出的值，你可以通过FM计算更精确的，但会影响速度

		g.setColor(gridColor);
		g.fillRect(x, y, gridWidth, gridHeight);    //画结点的格子

		g.setColor(stringColor);
		g.drawString(n.getName(), x, fontY);        //画结点的名字

		if (n.hasChild()) {
			List<Node> c = n.getChilds();
			int size = n.getChilds().size();
			int tempPosx = childAlign == CHILD_ALIGN_RELATIVE
					? x + gridWidth / 2 - (size * (gridWidth + hGap) - hGap) / 2
					: (getWidth() - size * (gridWidth + hGap) + hGap) / 2;

			int i = 0;
			for (Node node : c) {
				int newX = tempPosx + (gridWidth + hGap) * i;    //孩子结点起始X
				g.setColor(linkLineColor);
				g.drawLine(x + gridWidth / 2, y + gridHeight, newX + gridWidth / 2, y + gridHeight + vGap);    //画连接结点的线
				drawAllNode(node, newX, g);
				i++;
			}
		}
	}

	public void drawAllNodeBFS(Node n, int x, Graphics g) {
		Queue<Node> queue = new LinkedBlockingQueue<>();
		List<Node> currList = new ArrayList<>();
		List<Node> fatherList = new ArrayList<>();
		int rootLyyer = n.getLayer();
		int currLayer = rootLyyer;
		queue.offer(n);

		while (!queue.isEmpty()) {
			Node curr = queue.poll();
			int layer = curr.getLayer() - rootLyyer;
			if (layer != currLayer) {
				int y = currLayer * (vGap + gridHeight) + startY;
				int fontY = y + gridHeight - 5;

				int xGap = getWidth() / (currList.size() + 1);
				x = xGap - gridWidth / 2;

				for (Node node : currList) {
					node.setPosition(x + gridWidth / 2, y + gridHeight);
					g.setColor(gridColor);
					g.fillRect(x, y, gridWidth, gridHeight);

					g.setColor(stringColor);
					g.drawString(node.toString(), x, fontY);

					x += xGap;
				}

				if (!fatherList.isEmpty()) {
					for (Node father : fatherList) {
						int fatherX = father.getX();
						int fatherY = father.getY();

						if (father.hasChild())
							for (Node child : father.getChilds()) {
								g.setColor(linkLineColor);
								g.drawLine(fatherX, fatherY, child.getX(), child.getY() - gridHeight);
							}
					}
				}

				fatherList.clear();
				fatherList.addAll(currList);
				currList.clear();
				currLayer = layer;
			}
			currList.add(curr);

			if (curr.hasChild())
				for (Node child : curr.getChilds())
					queue.offer(child);
		}

		int y = currLayer * (vGap + gridHeight) + startY;
		int fontY = y + gridHeight - 5;

		if (!fatherList.isEmpty()) {
			for (Node father : fatherList) {
				int fatherX = father.getX();
				int fatherY = father.getY();

				if (father.hasChild())
					for (Node child : father.getChilds()) {
						g.setColor(linkLineColor);
						g.drawLine(fatherX, fatherY, fatherX, y);

						g.setColor(gridColor);
						g.fillRect(fatherX - gridWidth / 2, y, gridWidth, gridHeight);

						g.setColor(stringColor);
						g.drawString(child.toString(), fatherX - gridWidth / 2, fontY);
					}
			}
		}
	}

	public Color getGridColor() {
		return gridColor;
	}

	/**
	 * 设置结点背景颜色
	 *
	 * @param gridColor 结点背景颜色
	 */
	public void setGridColor(Color gridColor) {
		this.gridColor = gridColor;
	}

	public Color getLinkLineColor() {
		return linkLineColor;
	}

	/**
	 * 设置结点连接线的颜色
	 *
	 * @param gridLinkLine 结点连接线的颜色
	 */
	public void setLinkLineColor(Color gridLinkLine) {
		this.linkLineColor = gridLinkLine;
	}

	public Color getStringColor() {
		return stringColor;
	}

	/**
	 * 设置结点描述的颜色
	 *
	 * @param stringColor 结点描述的颜色
	 */
	public void setStringColor(Color stringColor) {
		this.stringColor = stringColor;
	}

	public int getStartY() {
		return startY;
	}

	/**
	 * 设置根结点的Y位置
	 *
	 * @param startY 根结点的Y位置
	 */
	public void setStartY(int startY) {
		this.startY = startY;
	}

	public int getStartX() {
		return startX;
	}

	/**
	 * 设置根结点的X位置
	 *
	 * @param startX 根结点的X位置
	 */
	public void setStartX(int startX) {
		this.startX = startX;
	}

}
