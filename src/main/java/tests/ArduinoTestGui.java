package tests;

import ca.syncron.network.tcp.client.ClientController;
import com.google.inject.Inject;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by Dawson on 3/26/2015.
 */
public class ArduinoTestGui {
	static              String           nameId = ArduinoTestGui.class.getSimpleName();
	public final static org.slf4j.Logger log    = LoggerFactory.getLogger(nameId);
	private JPanel           mPanel1;
	private JButton          mButton1;
	private JTextArea        m1TextArea;
	private JButton          mButton3;
	private JTextField       mTextField1;
	private JCheckBox        cbOut1;
	private JCheckBox        cbOut2;
	@Inject
	        ClientController mController;

	public ArduinoTestGui() {
		cbOut1.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (cbOut1.isSelected()) {
					ClientController.digitalVals[0] = 1;
				} else ClientController.digitalVals[0] = 0;
				mController.invalidateDigital();
			}
		});
	}

}
