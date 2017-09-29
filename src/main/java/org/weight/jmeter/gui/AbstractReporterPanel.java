package org.weight.jmeter.gui;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;

import javax.swing.JPanel;
import javax.swing.JTabbedPane;

public class AbstractReporterPanel extends JPanel{
	private JTabbedPane tab = new JTabbedPane(JTabbedPane.TOP); 
	public AbstractReporterPanel(int x, int y, int width, int height){
		setPreferredSize(new Dimension(width, height));
		init();
	}
	
	private void init(){
		
		setLayout(new BorderLayout());
		JPanel p1 = new JPanel();
		
		JPanel p2 = new JPanel();
		JPanel p3 = new JPanel();
		JPanel p4 = new JPanel();
		addPanel(p1, "SELECT");
		addPanel(p2, "UPDATE");
		addPanel(p3, "INSERT");
		addPanel(p4, "DELETE");
		
		add(tab);
	}
	
	public void addPanel(JPanel panel, String title){
		tab.add(panel, title);
	}
}	
