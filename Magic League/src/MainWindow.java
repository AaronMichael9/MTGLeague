import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.BoxLayout;
import javax.swing.JFrame;

public class MainWindow extends JFrame {

	private String[] tabs = {"Standings","Game","Edit"};
	private Tab tab = null;
	private String tabName = "";
	//currently only supports one league, all file paths are hard-coded
	private final String statsPath = "League1/stats.txt";
	private final String historyPath = "League1/history.txt";
	
	
	public static void main(String[] args) {
		JFrame window = new MainWindow();
		window.setDefaultCloseOperation(EXIT_ON_CLOSE);
	}
	
	public MainWindow() {
		generateLayout();
		generateTopBar();
		this.setVisible(true);
		transitionTab(tabs[1]);
	}
	private void generateLayout() {
		BorderLayout mainLayout = new BorderLayout();
		this.setLayout(mainLayout);
		this.setVisible(true);
		this.setTitle("MTG League");
		try {
			Image image = ImageIO.read(new File("src/icon.jpg"));
			this.setIconImage(image);
		} catch (Exception e) {
		}
		this.setSize(500, 600);
		this.setLocation(500, 250);
	}
	private void generateTopBar() {
		Container top = new Container();
		this.add(top, BorderLayout.NORTH);
		top.setLayout(new BoxLayout(top, BoxLayout.LINE_AXIS));
		for (String s : tabs) {
			Button button = new Button(s);
			button.setFocusable(false);
			button.setBackground(Color.ORANGE);
			button.setFont(new Font("Serif", Font.BOLD, 16));
			top.add(button);
			button.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					transitionTab(s);
				}
			});
		}	
	}
	
	private void transitionTab(String name) {
		if (name.equals(tabName))
			return;
		if (tab != null) {
			tab.close();
			this.remove(tab);
		}
		Tab newTab = null;
		switch (name) {
		case "Standings":
			this.requestFocus();
			newTab = new StandingsTab(statsPath,this);
			break;
		case "Game":
			newTab = new GameTab(statsPath,historyPath);
			break;
		case "Edit":
			newTab = new EditTab(statsPath);
			break;
		default:
			return;
		}
		this.add(newTab, BorderLayout.CENTER);
		tab = newTab;
		tabName = name;
		this.revalidate();
	}
}
