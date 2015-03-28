package tests;

import ca.syncron.network.tcp.client.ClientController;
import com.google.inject.Inject;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.event.*;

import static java.lang.System.out;

public class TestGuiDialog extends JDialog {
	private JPanel    contentPane;
	private JButton   buttonOK;
	private JButton   buttonCancel;
	private JCheckBox cbOut1;
	private JCheckBox cbOut2;
	@Inject
	ClientController mController;

	@Inject
	public TestGuiDialog() {
		setContentPane(contentPane);
		setModal(false);
		getRootPane().setDefaultButton(buttonOK);

		buttonOK.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {onOK();}
		});

		buttonCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {onCancel();}
		});

// call onCancel() when cross is clicked
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				onCancel();
			}
		});

// call onCancel() on ESCAPE
		contentPane.registerKeyboardAction(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				onCancel();
			}
		}, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);


		cbOut1.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				if (cbOut1.isSelected()) {
					ClientController.digitalVals[0] = 1;
				} else mController.digitalVals[0] = 0;
				out.println("Digital 0 value: " + mController.digitalVals[0]);

				mController.invalidateDigital();
			}
		});
		cbOut2.addActionListener(e -> {
			if (cbOut1.isSelected()) {
				ClientController.digitalVals[1] = 1;
			} else mController.digitalVals[1] = 0;
			out.println("Digital 1 value: " + mController.digitalVals[1]);
			mController.invalidateDigital();
		});

//	java.awt.EventQueue.invokeLater(new Runnable() {
//		public void run() {
//			//new  ArduinoGui().setVisible(true);
//			TestGuiDialog dialog = new TestGuiDialog();
//			dialog.pack();
//			dialog.setVisible(true);
//		}
//	});
	}

	private void onOK() {
// add your code here
		dispose();
	}

	private void onCancel() {
// add your code here if necessary
		dispose();
	}

	public static void main(String[] args) {
		TestGuiDialog dialog = new TestGuiDialog();
		dialog.pack();
		dialog.setVisible(true);
		System.exit(0);
	}
}
