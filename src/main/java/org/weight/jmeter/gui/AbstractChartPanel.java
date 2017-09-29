package org.weight.jmeter.gui;


import java.awt.FlowLayout;

import javax.swing.JPanel;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.general.Series;

public abstract class AbstractChartPanel extends JPanel{
	protected String title = null;
	protected Series series = null;
	protected String chartTitle = null;
	protected String xTitle = null;
	protected String yTitle = null;
	
	
	public AbstractChartPanel(String title, Series series, String chartTitle, String xTitle, String yTitle){
		super();
		this.chartTitle = chartTitle;
		this.series = series;
		this.title = title;
		this.xTitle = xTitle;
		this.yTitle = yTitle;
	}
	
	public void init(){
		
		ChartPanel chartPanel = new ChartPanel(createChart(),true);
		chartPanel.setPreferredSize(new java.awt.Dimension(460,200));
		chartPanel.setMouseZoomable(true, false);
		chartPanel.setLayout(new FlowLayout(FlowLayout.CENTER,10,10));
		chartPanel.setBorder(createTitledBorder());
		add(chartPanel);
	}
	
	protected TitledBorder createTitledBorder(){
		TitledBorder tb = new TitledBorder(new EtchedBorder(), this.title);
		return tb;
	}
	
	protected abstract JFreeChart createChart();
}
