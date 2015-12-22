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

	public DrawTree(Node n) {
		super("Draw Tree");
		initComponents(n);
	}

	public void draw(Node n) throws InterruptedException {
		initComponents(n);
		setSize(800, 1000);
		setVisible(true);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		Thread.sleep(2000);
	}

	public void initComponents(Node n) {
		TreePanelForDFS panel1 = new TreePanelForDFS(TreePanelForDFS.CHILD_ALIGN_RELATIVE);
		panel1.setTree(n);

		JPanel contentPane = new JPanel();
		contentPane.setLayout(new GridLayout(1, 1));
		contentPane.add(panel1);
		add(contentPane, BorderLayout.CENTER);
	}

}
