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
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
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

	private Path path = Paths.get("");
	private Path parent;

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
					String selectedObj = list.getSelectedValue().toString();

					if (path == null) {
						path = Paths.get(selectedObj);
					} else {
						path = Paths.get(path.toString(), selectedObj);
						parent = path.getParent();
					}

					if (Files.isDirectory(path)) {
						try (DirectoryStream<Path> rootArr = Files.newDirectoryStream(Paths.get(path.toString()))) {
							for (Path path1 : rootArr) {
								if (!Files.isHidden(path1)) {
									model.addElement(path1.getFileName());
								}
							}
						} catch (IOException e1) {
							e1.printStackTrace();
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

				if (parent != null) {
					path = parent;
					parent = path.getParent();

					DefaultListModel backRootModel = new DefaultListModel();

					try (DirectoryStream<Path> rootArr = Files.newDirectoryStream(Paths.get(path.toString()))) {
						for (Path path1 : rootArr) {
							if (!Files.isHidden(path1)) {
								backRootModel.addElement(path1.getFileName());
							}
						}
					} catch (IOException e1) {
						e1.printStackTrace();
					}
					list.setModel(backRootModel);
				} else {
					list.setListData(discs);
					path = Paths.get("");
				}
			}
		});

		// ADD NEW button
		addBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (!path.toString().isEmpty()) {

					CreateNewFolderJDialog newFolderJDialog = new CreateNewFolderJDialog(UI.this);

					if (newFolderJDialog.getReady()) {
						Path nameOfNewFolder = Paths.get(path.toString(), newFolderJDialog.getNewName());
						if (!Files.exists(nameOfNewFolder)) {
							try {
								Files.createDirectory(nameOfNewFolder);
							} catch (IOException e1) {
								e1.printStackTrace();
							}
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
				if (!path.toString().isEmpty() & list.getSelectedValue() != null) {

					RenameJDialog renameJDialog = new RenameJDialog(UI.this);

					if (renameJDialog.getReady()) {
						String selectedObj = list.getSelectedValue().toString();
						String newNameOfFolder = renameJDialog.getNewName();
						File renameFile = new File(path.toString(), selectedObj);
						renameFile.renameTo(new File(path.toString(), newNameOfFolder));

						updateList();

					}
				}
			}
		});

		// MOVE button
		moveBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (!path.toString().isEmpty() & list.getSelectedValue() != null) {
					String selectedObj = list.getSelectedValue().toString();

					if (new File(path.toString(), selectedObj).list().length == 0) {

						Path currentPathOfFile = Paths.get(path.toString(), selectedObj);
						MoveJDialog moveJDialog = new MoveJDialog(UI.this);

						if (moveJDialog.getReady()) {
							String newFolderPath = moveJDialog.getNewName();

							if (!newFolderPath.isEmpty()) {
								Path newPath = Paths.get(newFolderPath, selectedObj);

								if (Files.notExists(newPath)) {
									try {
										Files.move(currentPathOfFile, newPath);
									} catch (IOException e1) {
										e1.printStackTrace();
									}
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
				if (!path.toString().isEmpty() & list.getSelectedValue() != null) {
					String selectedObj = list.getSelectedValue().toString();

					if (new File(path.toString(), selectedObj).list().length == 0) {
						DeleteJDialog deleteJDialog = new DeleteJDialog(UI.this);

						if (deleteJDialog.getReady()) {

							Path newPath = Paths.get(path.toString(), selectedObj);
							try {
								Files.delete(newPath);
							} catch (IOException e1) {
								e1.printStackTrace();
							}
						}
						updateList();
					} else {
						JOptionPane.showMessageDialog(btnPanel, "Cannot delete non-empty folder!", "Warning",
								JOptionPane.ERROR_MESSAGE);
					}
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
	}

	private void updateList() {
		DefaultListModel updateModel = new DefaultListModel();
		try (DirectoryStream<Path> rootArr = Files.newDirectoryStream(Paths.get(path.toString()))) {
			for (Path path1 : rootArr) {
				if (!Files.isHidden(path1)) {
					updateModel.addElement(path1.getFileName());
				}
			}
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		list.setModel(updateModel);
	}

}