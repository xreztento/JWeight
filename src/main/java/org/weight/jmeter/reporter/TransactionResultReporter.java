package org.weight.jmeter.reporter;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Map.Entry;
import java.util.Set;
import java.util.Vector;

import javax.swing.Box;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import org.apache.jmeter.samplers.Clearable;
import org.apache.jmeter.testelement.TestElement;
import org.apache.jmeter.testelement.TestStateListener;
import org.apache.jmeter.visualizers.gui.AbstractListenerGui;
import org.weight.jmeter.JMeterPluginUtils;


public class TransactionResultReporter extends AbstractListenerGui 
							implements Clearable, PropertyChangeListener, TestStateListener{
	private TransactionTimeComputer ttc = new TransactionTimeComputer();
	private final String[] COLUMNS = 
			new String[] { "Trans Name", "Avg RT", "90% RT","Min RT","Max RT","Latest RT"};
	private Object[][] rows = 
			new Object[][] {{"N/A","N/A","N/A","N/A","N/A","N/A"}};
	private DefaultTableModel model = new DefaultTableModel(rows, COLUMNS);
	private JTable table = new JTable(model);
	private int num = 1;
	
	public TransactionResultReporter(){
		super();
		init();
	}
	
	private void init() {
		this.setLayout(new BorderLayout());
		JPanel mainPanel = new JPanel();
		DefaultTableCellRenderer renderer = new  DefaultTableCellRenderer();
		table.setDefaultRenderer(Object.class, renderer);
		table.setAutoscrolls(true);
		JScrollPane pane = new JScrollPane();
		pane.setPreferredSize(new Dimension(500, 50));
		pane.setViewportView(table);
        mainPanel.add(pane);
        add(mainPanel);
	}
	
	@Override
    public void clearData() {
		model = new DefaultTableModel(rows, COLUMNS);
		table = new JTable(model);
		init();
		num = 1;
    }
	
	@Override
	public TestElement createTestElement() {
		ttc.addPropertyChangeListener(this);
		TransactionResultCollector collector = new TransactionResultCollector(ttc);
		modifyTestElement(collector);
		return collector;
	}

	@Override
	public String getLabelResource() {
		return this.getClass().getSimpleName();
	}
	
	@Override
	public String getStaticLabel() {//������ʾ����
		return JMeterPluginUtils.prefixLabel("TransactionReport");
	}

	@Override
	public void modifyTestElement(TestElement element) {
		super.configureTestElement(element);
		
	}
	
	@Override
    public void configure(TestElement element) {
        super.configure(element);
    }
	
	class TimeUpdate implements Runnable
	{
		@Override
		public void run()
		{
			int i = 0;
			
			Set<Entry<String, Vector<Long>>> set = ttc.getResult().entrySet();
			if(num == set.size()){
				num++;
			    Object[][] rows = new Object[set.size()][6];
			    for(Entry<String, Vector<Long>> entry : set){
			    	TransactionTimeResult ttr = ttc.compute(entry.getKey());
			    	rows[i][0] = entry.getKey();
			    	rows[i][1] = ttr.getAvg();
			    	rows[i][2] = ttr.getTime90();
			    	rows[i][3] = ttr.getMin();
			    	rows[i][4] = ttr.getMax();
			    	rows[i][5] = ttr.getLast();
			    	i++;
				}
			    model.setDataVector(rows, COLUMNS);
				
			} else {
				
				for(Entry<String, Vector<Long>> entry : set){
					TransactionTimeResult ttr = ttc.compute(entry.getKey());
					model.setValueAt(ttr.getAvg(), i, 1);
					model.setValueAt(ttr.getTime90(), i, 2);
					model.setValueAt(ttr.getMin(), i, 3);
					model.setValueAt(ttr.getMax(), i, 4);
					model.setValueAt(ttr.getLast(), i, 5);
			    	i++;
				}
			}
		}
	}
	

	@Override
	public void propertyChange(PropertyChangeEvent pce) {
		try {
			SwingUtilities.invokeLater(new TimeUpdate());
		} catch(Exception e) {
			e.printStackTrace();
		}
		
	}

	@Override
	public void testEnded() {
		num = 1;
	}

	@Override
	public void testEnded(String host) {
		num = 1;
	}

	@Override
	public void testStarted() {
		clearData();
	}

	@Override
	public void testStarted(String host) {
		clearData();
	}
	
	@Override
    public void clearGui() {
		num = 1;
    }

}
