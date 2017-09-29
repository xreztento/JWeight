package org.weight.jmeter.gui;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.text.SimpleDateFormat;

import javax.swing.JPanel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.AxisLocation;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.ui.RectangleInsets;

public class TimeSeriesChartPanel extends AbstractChartPanel{
	private String formatPattern = "HH:mm:ss";
	

	public TimeSeriesChartPanel(String title, TimeSeries series, String chartTitle, String xTitle, String yTitle){
		super(title, series, chartTitle, xTitle, yTitle);
		super.init();
		
	}
	

	public TimeSeriesChartPanel(String title, TimeSeries series, String chartTitle, String xTitle, String yTitle, String formatPattern){
		super(title, series, chartTitle, xTitle, yTitle);
		this.formatPattern = formatPattern;
		super.init();
	}
	
	@Override
	public void paint(Graphics g){
		
		super.paint(g);
	}
	
	@Override
	protected JFreeChart createChart() {
		 TimeSeries series = (TimeSeries)this.series;
		 TimeSeriesCollection dataset = new TimeSeriesCollection();
		 dataset.addSeries(series);
		 
		 JFreeChart chart = ChartFactory.createTimeSeriesChart(
	            this.chartTitle, 
	            this.xTitle,
	            this.yTitle,
	            dataset,
	            false,
	            true,
	            false );
	        
		 chart.setBackgroundPaint(Color.LIGHT_GRAY);
	     chart.setNotify(true);
	    
	     XYPlot plot = (XYPlot) chart.getPlot();
	     plot.setBackgroundPaint(Color.WHITE);
	     plot.setDomainGridlinePaint(Color.DARK_GRAY);
	     plot.setRangeGridlinePaint(Color.DARK_GRAY);
	       
	     plot.setAxisOffset(new RectangleInsets(10.0, 10.0, 10.0, 10.0));
	     plot.setDomainCrosshairVisible(true);
	     plot.setRangeCrosshairVisible(true);
	     
	     plot.setDomainAxisLocation(AxisLocation.BOTTOM_OR_RIGHT);
	     DateAxis dateaxis = (DateAxis)plot.getDomainAxis();
	     SimpleDateFormat sfd = new SimpleDateFormat(this.formatPattern);
	     dateaxis.setDateFormatOverride(sfd);
	     plot.setDomainAxis(dateaxis);
	       
	     Font font = new Font("1",2,20);
	     font.deriveFont(Font.BOLD);
	       
	     XYItemRenderer r = plot.getRenderer();
	     if (r instanceof XYLineAndShapeRenderer) {
	         XYLineAndShapeRenderer renderer = (XYLineAndShapeRenderer) r;
	         renderer.setBaseShapesVisible(false);
	         renderer.setBaseShapesFilled(false);
	         renderer.setSeriesPaint(0, Color.blue);
	     }
	        
	     return chart;
	 }
}

