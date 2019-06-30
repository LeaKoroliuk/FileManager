package com.koroliuk;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

public class RenameJDialog  extends JDialog {

	private JButton okBtn = new JButton("OK");
	private JButton cancelBtn = new JButton("Cancel");
	private JLabel addNewNameLbl = new JLabel("New name of folder:");
	private JTextField newNameOfFolder = new JTextField(20);
	private boolean ready = false;
	private String newFolderName;
	
	public RenameJDialog(JFrame jFrame) {
		
		super(jFrame, "Rename folder", true);
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		
		getContentPane().setLayout(null);
		
		addNewNameLbl.setBounds(10, 25, 149, 33);
		okBtn.setBounds(165, 87, 89, 23);
		cancelBtn.setBounds(284, 87, 89, 23);
		newNameOfFolder.setBounds(165, 25, 219, 33);

		setSize(400, 150);
		setResizable(false);
		
		
		okBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				newFolderName = newNameOfFolder.getText();
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
	
		getContentPane().add(addNewNameLbl);
		getContentPane().add(newNameOfFolder);
		getContentPane().add(okBtn);
		getContentPane().add(cancelBtn);

		setLocationRelativeTo(null);
		setVisible(true);
	}

	public boolean getReady() {
		return ready;
	}
	
	public String getNewName() {
		return newFolderName;
	}
}
