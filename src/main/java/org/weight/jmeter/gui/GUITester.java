package org.weight.jmeter.gui;

import java.awt.BorderLayout;
import java.awt.Graphics;
import java.util.Date;
import java.util.Random;

import javax.swing.JFrame;
import javax.swing.SwingWorker;
import javax.swing.JTabbedPane;

import org.jfree.data.time.Millisecond;
import org.jfree.data.time.TimeSeries;

public class GUITester extends JFrame{
	
	public static void main(String[] args){
		GUITester tester = new GUITester();
		tester.setVisible(true);
	}
	
	@Override
	public void paint(Graphics g){
		System.out.println("paint");
		System.out.println(getWidth());
		System.out.println(getHeight());
		super.paint(g);
	}
	
	@Override
	public void repaint(){
		System.out.println("repaint");
		super.repaint();
	}
	
	public GUITester(){
		setSize(700,600);
		setBounds(40,40,750,400);
		BorderLayout layout = new BorderLayout();
		layout.setHgap(0);
		layout.setVgap(0);
		setLayout(layout);
		/*
		TimeSeries series = new TimeSeries("Memory");
		series.add(new Millisecond(), 0);
		TimeSeriesChartPanel chart = new TimeSeriesChartPanel("Memory", series, null, "Time", "Usage(MB)");
		//chart.init();
		
		//chart.setBounds(80, 80, chart.WIDTH, chart.HEIGHT);
		add(chart);
		new ChartWorker(series).execute();
		*/
		AbstractReporterPanel panel = new AbstractReporterPanel(0,100,600,250);
		
		add(panel, BorderLayout.SOUTH);
		setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
	}
	
	class ChartWorker extends SwingWorker<Void,Void>
	{
		TimeSeries series = null;
		ChartWorker(TimeSeries series){
			this.series = series;
		}
		@Override
		public Void doInBackground(){
				try {
					new Thread(new Runnable(){

						@Override
						public void run() {
							// TODO Auto-generated method stub
							while(true){
								try {
									Thread.sleep(5000);
								} catch (InterruptedException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								Random rand = new Random();
								
								series.add(new Millisecond(), rand.nextInt(400));
								
							}
							
						}
						
					}).start();
					
				} catch(Exception e) {
					e.printStackTrace();
				}
				return null;
			
		    }
	}
}
