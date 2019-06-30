package com.koroliuk;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

public class MoveJDialog extends JDialog {
	
	private JButton okBtn = new JButton("OK");
	private JButton cancelBtn = new JButton("Cancel");
	private JLabel addNewPathLbl = new JLabel("New path of folder:");
	private JTextField newPathOfFolder = new JTextField(20);
	private boolean ready = false;
	
	private String newPath;

	public MoveJDialog(JFrame jFrame) {

		super(jFrame, "Rename folder", true);
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

		getContentPane().setLayout(null);

		addNewPathLbl.setBounds(10, 25, 149, 33);
		okBtn.setBounds(165, 87, 89, 23);
		cancelBtn.setBounds(284, 87, 89, 23);
		newPathOfFolder.setBounds(165, 25, 219, 33);

		setSize(400, 150);
		setResizable(false);

		okBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				newPath = newPathOfFolder.getText();
				setVisible(false);
				ready = true;
			}
		});

		cancelBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				setVisible(false);
				ready = false;
			}
		});

		getContentPane().add(addNewPathLbl);
		getContentPane().add(newPathOfFolder);
		getContentPane().add(okBtn);
		getContentPane().add(cancelBtn);

		setLocationRelativeTo(null);
		setVisible(true);
	}

	public boolean getReady() {
		return ready;
	}

	public String getNewName() {
		return newPath;
	}
}