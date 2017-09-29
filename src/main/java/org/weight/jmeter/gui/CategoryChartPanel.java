package org.weight.jmeter.gui;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;

import javax.swing.JPanel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.AxisLocation;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.category.CategoryItemRenderer;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.ui.RectangleInsets;

public class CategoryChartPanel extends JPanel{
	private DefaultCategoryDataset dataset = null;
	private String title = null;
	private String xTitle = null;
	private String yTitle = null;
	public CategoryChartPanel(DefaultCategoryDataset dataset, String title, String xTitle, String yTitle){
		super();
		this.dataset = dataset;
		this.title = title;
		this.xTitle = xTitle;
		this.yTitle = yTitle;
		ChartPanel chartPanel = new ChartPanel(createChart(),true);
		chartPanel.setPreferredSize(new java.awt.Dimension(460,200));
		chartPanel.setMouseZoomable(true, false);
		chartPanel.setLayout(new FlowLayout(FlowLayout.CENTER,10,10));
		add(chartPanel);
	}
	
	private JFreeChart createChart() {

		 JFreeChart chart = ChartFactory.createLineChart(
	            this.title, 
	            this.xTitle,
	            this.yTitle,
	            dataset,
	            PlotOrientation.VERTICAL,
	            false,
	            true,
	            false );
	        
		 chart.setBackgroundPaint(Color.LIGHT_GRAY);
	     chart.setNotify(true);
	    
	     CategoryPlot plot = (CategoryPlot) chart.getPlot();
	     plot.setBackgroundPaint(Color.WHITE);
	     plot.setDomainGridlinePaint(Color.DARK_GRAY);
	     plot.setRangeGridlinePaint(Color.DARK_GRAY);
	       
	     plot.setAxisOffset(new RectangleInsets(10.0, 10.0, 10.0, 10.0));
	     plot.setDomainCrosshairVisible(true);
	     plot.setRangeCrosshairVisible(true);
	     plot.setDomainAxisLocation(AxisLocation.BOTTOM_OR_RIGHT);
	       
	     Font font = new Font("1",2,20);
	     font.deriveFont(Font.BOLD);
	       
	     final NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();  
	     rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());  
	     rangeAxis.setAutoRangeIncludesZero(true); 
	     
	     CategoryItemRenderer r = plot.getRenderer();
	     if (r instanceof LineAndShapeRenderer) {
	         LineAndShapeRenderer renderer = (LineAndShapeRenderer) r;
	         renderer.setBaseShapesVisible(false);
	         renderer.setBaseShapesFilled(false);
	         renderer.setSeriesPaint(0, Color.blue);
	     }
	        
	     return chart;
	 }
}
