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

	public DrawTree(Node n) {
		super("Draw tree");
		initComponents(n);
	}

	public static void main(String[] args) {
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
