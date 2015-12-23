/**
 * 2010-11-9
 * John
 */
package tree;

import javax.swing.*;
import java.awt.*;

/**
 * @author John
 */
public class DrawTree extends JFrame {

	public DrawTree() {
		super("Draw Tree");
	}

//	public DrawTree(Node n) {
//		super("Draw Tree");
//		initComponents(n, TreePanel.BROAD_FIRST);
//	}

	public void depthFirstDraw(Node n) throws InterruptedException {
		initComponents(n, TreePanel.DEPTH_FIRST);
		setSize(800, 1000);
		setVisible(true);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		Thread.sleep(2000);
	}

	public void depthFirstDraw(Node n, long sleep) throws InterruptedException {
		initComponents(n, TreePanel.DEPTH_FIRST);
		setSize(1500, 1200);
		setVisible(true);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		Thread.sleep(sleep);
	}

	public void broadFirstDraw(Node n) {
		initComponents(n, TreePanel.BROAD_FIRST);
		setSize(1500, 1200);
		setVisible(true);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	}

	public void initComponents(Node n, int depthOrBroad) {
		TreePanel panel1 = new TreePanel(TreePanel.CHILD_ALIGN_RELATIVE, depthOrBroad);
		panel1.setTree(n);

		JPanel contentPane = new JPanel();
		contentPane.setLayout(new GridLayout(1, 1));
		contentPane.add(panel1);
		add(contentPane, BorderLayout.CENTER);
	}

}
