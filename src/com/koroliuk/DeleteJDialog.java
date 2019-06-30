package com.koroliuk;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class DeleteJDialog extends JDialog {	
	
	private JButton okBtn = new JButton("OK");
	private JButton cancelBtn = new JButton("Cancel");
	private JLabel questionLbl = new JLabel("Delete folder?");

	private boolean ready = false;

	public DeleteJDialog(JFrame jFrame) {

		super(jFrame, "Delete folder", true);
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		
		getContentPane().setLayout(null);
		
		questionLbl.setBounds(10, 25, 374, 33);
		okBtn.setBounds(165, 87, 89, 23);
		cancelBtn.setBounds(284, 87, 89, 23);
		
		setSize(400, 150);
		setResizable(false);

		
		okBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
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

		getContentPane().add(questionLbl);
		getContentPane().add(okBtn);
		getContentPane().add(cancelBtn);

		setLocationRelativeTo(null);
		setVisible(true);
	}

	public boolean getReady() {
		return ready;
	}

}