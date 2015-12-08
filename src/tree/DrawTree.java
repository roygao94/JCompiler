/**
 * 2010-11-9
 * John
 */
package tree;

import java.awt.BorderLayout;
import java.awt.GridLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;

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
		TreePanel panel1 = new TreePanel(TreePanel.CHILD_ALIGN_RELATIVE);
		panel1.setTree(n);

		JPanel contentPane = new JPanel();
		contentPane.setLayout(new GridLayout(1, 1));
		contentPane.add(panel1);
		add(contentPane, BorderLayout.CENTER);
	}

}
