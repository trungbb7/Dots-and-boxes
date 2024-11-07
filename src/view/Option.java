package view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Scanner;

import javax.swing.AbstractButton;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.border.TitledBorder;

import controller.ApplyButtonController;

public class Option extends JPanel {

	private Color textColor = new Color(203, 207, 212);

	private JButton applyBtn;
	private ButtonGroup levelBG;
	private ButtonGroup typeBG;

	public Option() {
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		JPanel levelP = new JPanel(new FlowLayout(FlowLayout.CENTER));
		levelBG = new ButtonGroup();

		JRadioButton easyBtn = new JRadioButton("Easy");
		easyBtn.setFont(new Font("roboto", Font.PLAIN, 14));
		easyBtn.setActionCommand("easy");

		JRadioButton mediumBtn = new JRadioButton("Medium");
		mediumBtn.setFont(new Font("roboto", Font.PLAIN, 14));
		mediumBtn.setActionCommand("medium");

		JRadioButton hardBtn = new JRadioButton("Hard");
		hardBtn.setFont(new Font("roboto", Font.PLAIN, 14));
		hardBtn.setActionCommand("hard");

		levelP.add(easyBtn);
		levelP.add(mediumBtn);
		levelP.add(hardBtn);

		levelBG.add(easyBtn);
		levelBG.add(mediumBtn);
		levelBG.add(hardBtn);

		levelP.setBorder(new TitledBorder(getBorder(), "Level", TitledBorder.DEFAULT_JUSTIFICATION,
				TitledBorder.DEFAULT_POSITION, new Font("roboto", Font.BOLD, 16), textColor));

		JPanel typeP = new JPanel(new FlowLayout(FlowLayout.CENTER));
		typeBG = new ButtonGroup();
		JRadioButton type3Btn = new JRadioButton("3x3");
		type3Btn.setFont(new Font("roboto", Font.PLAIN, 14));
		type3Btn.setActionCommand("3");
		JRadioButton type5Btn = new JRadioButton("5x5");
		type5Btn.setFont(new Font("roboto", Font.PLAIN, 14));
		type5Btn.setActionCommand("5");
		JRadioButton type6Btn = new JRadioButton("6x6");
		type6Btn.setFont(new Font("roboto", Font.PLAIN, 14));
		type6Btn.setActionCommand("6");
		typeP.add(type3Btn);
		typeP.add(type5Btn);
		typeP.add(type6Btn);
		typeBG.add(type3Btn);
		typeBG.add(type5Btn);
		typeBG.add(type6Btn);
		typeP.setBorder(new TitledBorder(getBorder(), "Board type", TitledBorder.DEFAULT_JUSTIFICATION,
				TitledBorder.DEFAULT_POSITION, new Font("roboto", Font.BOLD, 16), textColor));

		JPanel wrapPanel = new JPanel(new GridLayout(3, 1, 10, 10));
		wrapPanel.add(levelP);
		wrapPanel.add(typeP);
		wrapPanel.setPreferredSize(new Dimension(620, 200));
		wrapPanel.setMaximumSize(new Dimension(620, 200));
		this.add(wrapPanel);
		applyBtn = new JButton("Apply");
		ApplyButtonController abc = new ApplyButtonController(this);
		applyBtn.addMouseListener(abc);
		this.add(applyBtn);
		this.add(Box.createVerticalGlue());
		updateSelected();

	}

	private void updateSelected() {
		try {
			String levelConfig = "";
			String typeConfig = "";
			File file = new File("src\\config");
			Scanner sc = new Scanner(file);
			int count = 1;
			while (sc.hasNextLine()) {
				if (count == 1) {
					levelConfig = sc.nextLine().split(":")[1];
				} else if (count == 2) {
					typeConfig = sc.nextLine().split(":")[1];
				}
				count++;
			}
			Enumeration<AbstractButton> levelBtns = levelBG.getElements();
			while (levelBtns.hasMoreElements()) {
				JRadioButton btn = (JRadioButton) levelBtns.nextElement();
				if (btn.getActionCommand().equals(levelConfig)) {
					btn.setSelected(true);
				} else {
					btn.setSelected(false);
				}
			}

			Enumeration<AbstractButton> typeBtns = typeBG.getElements();
			while (typeBtns.hasMoreElements()) {
				JRadioButton btn = (JRadioButton) typeBtns.nextElement();
				if (btn.getActionCommand().equals(typeConfig)) {
					btn.setSelected(true);
				} else {
					btn.setSelected(false);
				}
			}
			sc.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	public ButtonGroup getLevelBG() {
		return levelBG;
	}

	public ButtonGroup getTypeBG() {
		return typeBG;
	}

}
