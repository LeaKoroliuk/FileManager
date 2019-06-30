package com.koroliuk;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class UI extends JFrame {

	private JPanel catalogPanel = new JPanel();
	private JPanel btnPanel = new JPanel();

	private JButton backBtn = new JButton("BACK");
	private JButton addBtn = new JButton("ADD NEW");
	private JButton renameBtn = new JButton("RENAME");
	private JButton moveBtn = new JButton("MOVE");
	private JButton delBtn = new JButton("DELETE");

	private JList list = new JList();
	private JScrollPane scrollPane = new JScrollPane(list);

	private String path = "";

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					UI frame = new UI();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public UI() {
		super("My File Manager");

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(700, 400);

		btnPanel.add(backBtn);
		btnPanel.add(addBtn);
		btnPanel.add(renameBtn);
		btnPanel.add(moveBtn);
		btnPanel.add(delBtn);
		btnPanel.setLayout(new GridLayout(5, 5, 25, 25));

		catalogPanel.setLayout(new BorderLayout(5, 5));
		catalogPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		catalogPanel.add(btnPanel, BorderLayout.WEST);
		getContentPane().add(catalogPanel);

		scrollPane.setPreferredSize(new Dimension(400, 500));
		catalogPanel.add(scrollPane, BorderLayout.CENTER);

		setResizable(true);
		setLocationRelativeTo(null);

		File[] discs = File.listRoots();
		list.setListData(discs);
		list.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

		// LIST
		list.addMouseListener(new MouseListener() {
			@Override
			public void mouseReleased(MouseEvent e) {
			}

			@Override
			public void mousePressed(MouseEvent e) {
			}

			@Override
			public void mouseExited(MouseEvent e) {
			}

			@Override
			public void mouseEntered(MouseEvent e) {
			}

			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2) {

					DefaultListModel model = new DefaultListModel();

					if (!path.isEmpty()) {
						path += "\\" + list.getSelectedValue().toString();
					} else {
						path += list.getSelectedValue().toString();
					}

					File selectedFile = new File(path);

					if (selectedFile.isDirectory()) {
						String[] rootArr = selectedFile.list();

						for (String s : rootArr) {
							File checkObj = new File(s);
							if (!checkObj.isHidden()) {
								model.addElement(checkObj);
							}
						}
					}
					list.setModel(model);
				}
			}
		});
		
		// BACK button
		backBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (path.length() > 3) {

					path = path.substring(0, path.lastIndexOf("\\"));

					String[] objects = new File(path).list();
					DefaultListModel backRootModel = new DefaultListModel();

					for (String s : objects) {
						File checkObj = new File(s);
						if (!checkObj.isHidden()) {
							backRootModel.addElement(checkObj);
						}
					}
					list.setModel(backRootModel);
				} else {
					list.setListData(discs);
					path = "";
				}
			}
		});
		
		// ADD NEW button
		addBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (!path.isEmpty()) {

					CreateNewFolderJDialog newFolderJDialog = new CreateNewFolderJDialog(UI.this);

					if (newFolderJDialog.getReady()) {
						String nameOfNewFolder = newFolderJDialog.getNewName();
						File newFolder = new File(path, nameOfNewFolder);
						if (!newFolder.exists()) {
							newFolder.mkdir();
						}
						updateList();
					}
				}
			}
		});
		
		// RENAME button
		renameBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (!path.isEmpty() & list.getSelectedValue() != null) {

					RenameJDialog renameJDialog = new RenameJDialog(UI.this);

					if (renameJDialog.getReady()) {
						String selectedObj = list.getSelectedValue().toString();
						String newNameOfFolder = renameJDialog.getNewName();
						File renameFile = new File(path, selectedObj);
						renameFile.renameTo(new File(path, newNameOfFolder));
						updateList();
					}
				}
			}
		});
		
		// MOVE button
		moveBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (!path.isEmpty() & list.getSelectedValue() != null) {
					String selectedObj = list.getSelectedValue().toString();

					if (new File(path, selectedObj).list().length == 0) {

						MoveJDialog moveJDialog = new MoveJDialog(UI.this);

						if (moveJDialog.getReady()) {
							String newFolderPath = moveJDialog.getNewName();
							if (!newFolderPath.isEmpty()) {
								File createFile = new File(newFolderPath, selectedObj);
								if (!createFile.exists()) {
									createFile.mkdir();
									File delFile = new File(path, selectedObj);
									delFile.delete();
									updateList();
								} else {
									JOptionPane.showMessageDialog(btnPanel,
											"There is already such a folder along the specified path!", "Warning",
											JOptionPane.ERROR_MESSAGE);
								}
							}
						}
					} else {
						JOptionPane.showMessageDialog(btnPanel, "Cannot move non-empty folder!", "Warning",
								JOptionPane.ERROR_MESSAGE);
					}
				}
			}

		});
		
		// DELETE button
		delBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (!path.isEmpty() & list.getSelectedValue() != null) {
					String selectedObj = list.getSelectedValue().toString();
					if (new File(path, selectedObj).list().length == 0) {
						DeleteJDialog deleteJDialog = new DeleteJDialog(UI.this);
						if (deleteJDialog.getReady()) {
							File delFile = new File(path, selectedObj);
							delFile.delete();
						}
						updateList();
					} else {
						JOptionPane.showMessageDialog(btnPanel, "Cannot delete non-empty folder!", "Warning",
								JOptionPane.ERROR_MESSAGE);
					}
				}
			}
		});

	}

	private void updateList() {
		File updateDir = new File(path);
		String[] updateMas = updateDir.list();
		DefaultListModel updateModel = new DefaultListModel();

		for (String str : updateMas) {
			File check = new File(updateDir.getPath(), str);
			if (!check.isHidden()) {
				updateModel.addElement(str);
			}
		}
		list.setModel(updateModel);
	}

}