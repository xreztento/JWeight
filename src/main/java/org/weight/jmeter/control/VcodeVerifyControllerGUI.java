package org.weight.jmeter.control;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.apache.jmeter.control.gui.AbstractControllerGui;
import org.apache.jmeter.gui.util.FocusRequester;
import org.apache.jmeter.testelement.TestElement;
import org.apache.jmeter.util.JMeterUtils;
import org.weight.jmeter.JMeterPluginUtils;

public class VcodeVerifyControllerGUI extends AbstractControllerGui implements ActionListener{

	private JCheckBox infinite;

    /**
     * A field allowing the user to specify the number of times the controller
     * should loop.
     */
    private JTextField loops;

    /**
     * Boolean indicating whether or not this component should display its name.
     * If true, this is a standalone component. If false, this component is
     * intended to be used as a subpanel for another component.
     */
    private boolean displayName = true;

    /** The name of the infinite checkbox component. */
    private static final String INFINITE = "Infinite Field"; // $NON-NLS-1$

    /** The name of the loops field component. */
    private static final String LOOPS = "Loops Field"; // $NON-NLS-1$

    /**
     * Create a new LoopControlPanel as a standalone component.
     */
    public VcodeVerifyControllerGUI() {
        this(true);
    }

    /**
     * Create a new LoopControlPanel as either a standalone or an embedded
     * component.
     *
     * @param displayName
     *            indicates whether or not this component should display its
     *            name. If true, this is a standalone component. If false, this
     *            component is intended to be used as a subpanel for another
     *            component.
     */
    public VcodeVerifyControllerGUI(boolean displayName) {
        this.displayName = displayName;
        init();
        setState(1);
    }

    /**
     * A newly created component can be initialized with the contents of a Test
     * Element object by calling this method. The component is responsible for
     * querying the Test Element object for the relevant information to display
     * in its GUI.
     *
     * @param element
     *            the TestElement to configure
     */
    @Override
    public void configure(TestElement element) {
        super.configure(element);
        if (element instanceof VcodeVerifyController) {
            setState(((VcodeVerifyController) element).getLoopString());
        } else {
            setState(1);
        }
    }

    /* Implements JMeterGUIComponent.createTestElement() */
    @Override
    public TestElement createTestElement() {
    	VcodeVerifyController vvc = new VcodeVerifyController();
        modifyTestElement(vvc);
        return vvc;
    }

    /* Implements JMeterGUIComponent.modifyTestElement(TestElement) */
    @Override
    public void modifyTestElement(TestElement vvc) {
        configureTestElement(vvc);
        if (vvc instanceof VcodeVerifyController) {
            if (loops.getText().length() > 0) {
                ((VcodeVerifyController) vvc).setLoops(loops.getText());
            } else {
                ((VcodeVerifyController) vvc).setLoops(VcodeVerifyController.INFINITE_LOOP_COUNT);
            }
        }
    }

    /**
     * Implements JMeterGUIComponent.clearGui
     */
    @Override
    public void clearGui() {
        super.clearGui();

        loops.setText("1"); // $NON-NLS-1$
        infinite.setSelected(false);
    }

    /**
     * Invoked when an action occurs. This implementation assumes that the
     * target component is the infinite loops checkbox.
     *
     * @param event
     *            the event that has occurred
     */
    @Override
    public void actionPerformed(ActionEvent event) {
        if (infinite.isSelected()) {
            loops.setText(""); // $NON-NLS-1$
            loops.setEnabled(false);
        } else {
            loops.setEnabled(true);
            FocusRequester.requestFocus(loops);
        }
    }

    @Override
	public String getLabelResource() {
		// TODO Auto-generated method stub
		return this.getClass().getName();
	}
	
	@Override
	public String getStaticLabel() {
		// TODO Auto-generated method stub
		//return JMeterPluginUtils.prefixLabel("VcodeVerifyController");
		return "VcodeVerifyController";
	}

    /**
     * Initialize the GUI components and layout for this component.
     */
    private void init() {
        // The Loop Controller panel can be displayed standalone or inside
        // another panel. For standalone, we want to display the TITLE, NAME,
        // etc. (everything). However, if we want to display it within another
        // panel, we just display the Loop Count fields (not the TITLE and
        // NAME).

        // Standalone
        if (displayName) {
            setLayout(new BorderLayout(0, 5));
            setBorder(makeBorder());
            add(makeTitlePanel(), BorderLayout.NORTH);

            JPanel mainPanel = new JPanel(new BorderLayout());
            mainPanel.add(createLoopCountPanel(), BorderLayout.NORTH);
            add(mainPanel, BorderLayout.CENTER);
        } else {
            // Embedded
            setLayout(new BorderLayout());
            add(createLoopCountPanel(), BorderLayout.NORTH);
        }
    }

    /**
     * Create a GUI panel containing the components related to the number of
     * loops which should be executed.
     *
     * @return a GUI panel containing the loop count components
     */
    private JPanel createLoopCountPanel() {
        JPanel loopPanel = new JPanel(new BorderLayout(5, 0));

        // LOOP LABEL
        JLabel loopsLabel = new JLabel(JMeterUtils.getResString("iterator_num")); // $NON-NLS-1$
        loopPanel.add(loopsLabel, BorderLayout.WEST);

        JPanel loopSubPanel = new JPanel(new BorderLayout(5, 0));

        // TEXT FIELD
        loops = new JTextField("1", 5); // $NON-NLS-1$
        loops.setName(LOOPS);
        loopsLabel.setLabelFor(loops);
        loopSubPanel.add(loops, BorderLayout.CENTER);

        // FOREVER CHECKBOX
        infinite = new JCheckBox(JMeterUtils.getResString("infinite")); // $NON-NLS-1$
        infinite.setActionCommand(INFINITE);
        infinite.addActionListener(this);
        loopSubPanel.add(infinite, BorderLayout.WEST);

        loopPanel.add(loopSubPanel, BorderLayout.CENTER);

        loopPanel.add(Box.createHorizontalStrut(loopsLabel.getPreferredSize().width + loops.getPreferredSize().width
                + infinite.getPreferredSize().width), BorderLayout.NORTH);

        return loopPanel;
    }

    /**
     * Set the number of loops which should be reflected in the GUI. The
     * loopCount parameter should contain the String representation of an
     * integer. This integer will be treated as the number of loops. If this
     * integer is less than 0, the number of loops will be assumed to be
     * infinity.
     *
     * @param loopCount
     *            the String representation of the number of loops
     */
    private void setState(String loopCount) {
        if (loopCount.startsWith("-")) { // $NON-NLS-1$
            setState(VcodeVerifyController.INFINITE_LOOP_COUNT);
        } else {
            loops.setText(loopCount);
            infinite.setSelected(false);
            loops.setEnabled(true);
        }
    }

    /**
     * Set the number of loops which should be reflected in the GUI. If the
     * loopCount is less than 0, the number of loops will be assumed to be
     * infinity.
     *
     * @param loopCount
     *            the number of loops
     */
    private void setState(int loopCount) {
        if (loopCount <= VcodeVerifyController.INFINITE_LOOP_COUNT) {
            infinite.setSelected(true);
            loops.setEnabled(false);
            loops.setText(""); // $NON-NLS-1$
        } else {
            infinite.setSelected(false);
            loops.setEnabled(true);
            loops.setText(Integer.toString(loopCount));
        }
    }
}
