/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ca.syncron.gui.netbeans;

import ca.syncron.network.message.Message.MessageType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;

/**
 * @author Dawson
 */
public class DigitalOutPanel extends javax.swing.JPanel {

	public ArrayList<DigitalPinStatusTester> pins = new ArrayList<DigitalPinStatusTester>();
	public  MessageListener  msgListener;
	private UserListListener msgUserListener;

//	public enum MessageType {
//
//		DIGITAL, ANALOG, ADMIN, UPDATE, REGISTER, LOGIN, STATUS, CHECKIN, USER, STREAM, CHAT, QUERY, ERROR, UNKNOWN, ACCESS, TARGET, SUBSCRIBE;
//		public static final MessageType            values[]  = values();
//		public static       ArrayList<MessageType> arrayList = new ArrayList<MessageType>(Arrays.asList(MessageType.values()));
//
//		public static ArrayList<MessageType> getTypes() {
//			return new ArrayList<MessageType>(Arrays.asList(MessageType.values()));
//		}
//
//		public static MessageType getByIndex(int i) {
//			return values[i];
//		}
//	}

	public enum UserType {

		NODE, SERVER, ANDROID, NODE_SERVER, NODE_CLIENT, UNKNOWN;
	}

	public enum Chat {

		REGISTER, UPDATE, LOGIN, USERS, DISCONNECT, UNKNOWN;
	}

	public int    msgPin      = -1;
	public String msgChat     = null;
	public String msgTargetId = null;

	List<MessageType> msgTypes = new ArrayList<MessageType>(Arrays.asList(MessageType.values()));

	/**
	 * Creates new form DigitalOutPanel
	 */
	public DigitalOutPanel() {
		pins = new ArrayList<DigitalPinStatusTester>();
		DigitalPinStatusTester.setRegistrar(pins);
		initComponents();
		ArrayList<MessageType> types = MessageType.getTypes() // cbMsgType.setModel( new ComboBoxModel (){});
				;
	}

	/**
	 * This method is called from within the constructor to initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is always
	 * regenerated by the Form Editor.
	 */
	@SuppressWarnings("unchecked")
	// <editor-fold defaultstate="collapsed" desc="Generated Code">
	private void initComponents() {
		java.awt.GridBagConstraints gridBagConstraints;

		jFrame1 = new javax.swing.JFrame();

		jTabbedPane1 = new javax.swing.JTabbedPane();
		connectionPanel1 = new org.zu.ardulink.gui.ConnectionPanel();
		tabDigital = new javax.swing.JPanel();
		jPanel1 = new javax.swing.JPanel();
		digitalPinStatusTester1 = new DigitalPinStatusTester();
		digitalPinStatusTester2 = new DigitalPinStatusTester();
		digitalPinStatusTester4 = new DigitalPinStatusTester();
		digitalPinStatusTester3 = new DigitalPinStatusTester();
		digitalPinStatusTester5 = new DigitalPinStatusTester();
		digitalPinStatusTester6 = new DigitalPinStatusTester();
		tabMessage = new javax.swing.JPanel();
		jPanel4 = new javax.swing.JPanel();
		cbMsgType = new javax.swing.JComboBox();
		jPanel2 = new javax.swing.JPanel();
		btnSend = new javax.swing.JButton();
		jTabbedPane2 = new javax.swing.JTabbedPane();
		jScrollPane3 = new javax.swing.JScrollPane();
		msgFields = new javax.swing.JTable();
		jPanel3 = new javax.swing.JPanel();
		jPanel7 = new javax.swing.JPanel();
		jPanel8 = new javax.swing.JPanel();
		jLabel1 = new javax.swing.JLabel();
		txtPin = new javax.swing.JTextField();
		jPanel10 = new javax.swing.JPanel();
		jLabel3 = new javax.swing.JLabel();
		txtValue = new javax.swing.JTextField();
		jSplitPane1 = new javax.swing.JSplitPane();
		jLabel2 = new javax.swing.JLabel();
		txtChat = new javax.swing.JTextField();
		userScrollPane = new javax.swing.JScrollPane();
		userList = new javax.swing.JList();
		consolScrollPane = new javax.swing.JScrollPane();
		txtConsol = new javax.swing.JTextArea();
		etConsolInput = new javax.swing.JTextField();
		jPanel5 = new javax.swing.JPanel();
		jPanel6 = new javax.swing.JPanel();
		connectionStatus1 = new org.zu.ardulink.gui.ConnectionStatus();
		jButton1 = new javax.swing.JButton();

		javax.swing.GroupLayout jFrame1Layout = new javax.swing.GroupLayout(jFrame1.getContentPane());
		jFrame1.getContentPane().setLayout(jFrame1Layout);
		jFrame1Layout.setHorizontalGroup(
				jFrame1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				             .addGap(0, 400, Short.MAX_VALUE)
		                                );
		jFrame1Layout.setVerticalGroup(
				jFrame1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				             .addGap(0, 300, Short.MAX_VALUE)
		                              );

		jTabbedPane1.addTab("Connection", connectionPanel1);

		javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
		jPanel1.setLayout(jPanel1Layout);
		jPanel1Layout.setHorizontalGroup(
				jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				             .addGroup(jPanel1Layout.createSequentialGroup()
				                                    .addContainerGap()
				                                    .addComponent(digitalPinStatusTester1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
				                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
				                                    .addComponent(digitalPinStatusTester2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
				                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
				                                    .addComponent(digitalPinStatusTester4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
				                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
				                                    .addComponent(digitalPinStatusTester3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
				                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
				                                    .addComponent(digitalPinStatusTester5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
				                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
				                                    .addComponent(digitalPinStatusTester6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
				                                    .addContainerGap(237, Short.MAX_VALUE))
		                                );
		jPanel1Layout.setVerticalGroup(
				jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				             .addGroup(jPanel1Layout.createSequentialGroup()
				                                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
				                                                           .addComponent(digitalPinStatusTester1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
				                                                           .addComponent(digitalPinStatusTester2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
				                                                           .addComponent(digitalPinStatusTester4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
				                                                           .addComponent(digitalPinStatusTester3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
				                                                           .addComponent(digitalPinStatusTester5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
				                                                           .addComponent(digitalPinStatusTester6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
				                                    .addGap(0, 21, Short.MAX_VALUE))
		                              );

		javax.swing.GroupLayout tabDigitalLayout = new javax.swing.GroupLayout(tabDigital);
		tabDigital.setLayout(tabDigitalLayout);
		tabDigitalLayout.setHorizontalGroup(
				tabDigitalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				                .addGap(0, 789, Short.MAX_VALUE)
				                .addGroup(tabDigitalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				                                          .addGroup(tabDigitalLayout.createSequentialGroup()
				                                                                    .addContainerGap()
				                                                                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
				                                                                    .addContainerGap()))
		                                   );
		tabDigitalLayout.setVerticalGroup(
				tabDigitalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				                .addGap(0, 186, Short.MAX_VALUE)
				                .addGroup(tabDigitalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				                                          .addGroup(tabDigitalLayout.createSequentialGroup()
				                                                                    .addContainerGap()
				                                                                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
				                                                                    .addContainerGap(51, Short.MAX_VALUE)))
		                                 );

		jTabbedPane1.addTab("Digital", tabDigital);

		cbMsgType.setEditable(true);
		cbMsgType.setModel(new javax.swing.DefaultComboBoxModel(new String[]{"DIGITAL", "ANALOG", "ADMIN", "UPDATE", "REGISTER", "LOGIN", "STATUS", "CHECKIN", "USER", "STREAM", "CHAT", "QUERY", "ERROR", "UNKNOWN", "ACCESS", "TARGET", "SUBSCRIBE"}));
		cbMsgType.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				cbMsgTypeActionPerformed(evt);
			}
		});

		btnSend.setText("Send Message");
		btnSend.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				btnSendActionPerformed(evt);
			}
		});

		javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
		jPanel2.setLayout(jPanel2Layout);
		jPanel2Layout.setHorizontalGroup(
				jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				             .addGroup(jPanel2Layout.createSequentialGroup()
				                                    .addContainerGap()
				                                    .addComponent(btnSend, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
				                                    .addContainerGap())
		                                );
		jPanel2Layout.setVerticalGroup(
				jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				             .addGroup(jPanel2Layout.createSequentialGroup()
				                                    .addComponent(btnSend)
				                                    .addGap(0, 0, Short.MAX_VALUE))
		                              );

		msgFields.setModel(new javax.swing.table.DefaultTableModel(
				new Object[][]{
						{"Pin", "2"},
						{"Value", "1"},
						{"Chat", "Hello, guy!"},
						{"isTargeted", "true"},
						{"Target ID", "dawson@jPanel"}
				},
				new String[]{
						"Field", "Value"
				}
		));
		msgFields.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
		msgFields.setFillsViewportHeight(true);
		jScrollPane3.setViewportView(msgFields);

		jTabbedPane2.addTab("Table", jScrollPane3);

		jPanel8.setLayout(new java.awt.GridLayout(1, 4, 15, 0));

		jLabel1.setText("Pin");
		jPanel8.add(jLabel1);

		txtPin.setText("2");
		txtPin.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				txtPinActionPerformed(evt);
			}
		});
		jPanel8.add(txtPin);

		java.awt.GridBagLayout jPanel10Layout = new java.awt.GridBagLayout();
		jPanel10Layout.columnWidths = new int[]{100};
		jPanel10.setLayout(jPanel10Layout);

		jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
		jLabel3.setLabelFor(txtValue);
		jLabel3.setText("Value");
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		jPanel10.add(jLabel3, gridBagConstraints);

		txtValue.setText("1");
		txtValue.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				txtValueActionPerformed(evt);
			}
		});
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		jPanel10.add(txtValue, gridBagConstraints);

		javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
		jPanel7.setLayout(jPanel7Layout);
		jPanel7Layout.setHorizontalGroup(
				jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				             .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createSequentialGroup()
				                                                                                .addContainerGap(25, Short.MAX_VALUE)
				                                                                                .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
				                                                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
				                                                                                .addComponent(jPanel10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
				                                                                                .addContainerGap())
		                                );
		jPanel7Layout.setVerticalGroup(
				jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				             .addGroup(jPanel7Layout.createSequentialGroup()
				                                    .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				                                                           .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
				                                                           .addComponent(jPanel10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
				                                    .addContainerGap(119, Short.MAX_VALUE))
		                              );

		jLabel2.setText("Chat");
		jSplitPane1.setLeftComponent(jLabel2);

		txtChat.setText("jTextField1");

		javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
		jPanel3.setLayout(jPanel3Layout);
		jPanel3Layout.setHorizontalGroup(
				jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				             .addGroup(jPanel3Layout.createSequentialGroup()
				                                    .addContainerGap()
				                                    .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
				                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
				                                    .addComponent(jSplitPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
				                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
				                                    .addComponent(txtChat, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
				                                    .addContainerGap(42, Short.MAX_VALUE))
		                                );
		jPanel3Layout.setVerticalGroup(
				jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				             .addGroup(jPanel3Layout.createSequentialGroup()
				                                    .addContainerGap()
				                                    .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
				                                    .addContainerGap())
				             .addGroup(jPanel3Layout.createSequentialGroup()
				                                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				                                                           .addComponent(jSplitPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
				                                                           .addComponent(txtChat, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
				                                    .addGap(0, 0, Short.MAX_VALUE))
		                              );

		jTabbedPane2.addTab("tab2", jPanel3);

		javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
		jPanel4.setLayout(jPanel4Layout);
		jPanel4Layout.setHorizontalGroup(
				jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				             .addGroup(jPanel4Layout.createSequentialGroup()
				                                    .addContainerGap()
				                                    .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
				                                                           .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
				                                                           .addComponent(cbMsgType, 0, 134, Short.MAX_VALUE))
				                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
				                                    .addComponent(jTabbedPane2)
				                                    .addContainerGap())
		                                );
		jPanel4Layout.setVerticalGroup(
				jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				             .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
				                                                                                .addComponent(cbMsgType, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
				                                                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
				                                                                                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
				                                                                                .addGap(20, 20, 20))
				             .addGroup(jPanel4Layout.createSequentialGroup()
				                                    .addComponent(jTabbedPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 189, javax.swing.GroupLayout.PREFERRED_SIZE)
				                                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
		                              );

		javax.swing.GroupLayout tabMessageLayout = new javax.swing.GroupLayout(tabMessage);
		tabMessage.setLayout(tabMessageLayout);
		tabMessageLayout.setHorizontalGroup(
				tabMessageLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				                .addGroup(tabMessageLayout.createSequentialGroup()
				                                          .addContainerGap()
				                                          .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
				                                          .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
		                                   );
		tabMessageLayout.setVerticalGroup(
				tabMessageLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				                .addGroup(tabMessageLayout.createSequentialGroup()
				                                          .addContainerGap()
				                                          .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, 174, Short.MAX_VALUE)
				                                          .addContainerGap())
		                                 );

		jTabbedPane1.addTab("Messages", tabMessage);

		userList.setModel(new javax.swing.AbstractListModel() {
			String[] strings = {"Item 1", "Item 2", "Item 3", "Item 4", "Item 5"};

			public int getSize() { return strings.length; }

			public Object getElementAt(int i) { return strings[i]; }
		});
		userList.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
			public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
				userListValueChanged(evt);
			}
		});
		userScrollPane.setViewportView(userList);

		txtConsol.setColumns(20);
		txtConsol.setRows(5);
		consolScrollPane.setViewportView(txtConsol);

		etConsolInput.setText("Type command");

		javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
		jPanel5.setLayout(jPanel5Layout);
		jPanel5Layout.setHorizontalGroup(
				jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				             .addGap(0, 100, Short.MAX_VALUE)
		                                );
		jPanel5Layout.setVerticalGroup(
				jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				             .addGap(0, 100, Short.MAX_VALUE)
		                              );

		javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
		jPanel6.setLayout(jPanel6Layout);
		jPanel6Layout.setHorizontalGroup(
				jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				             .addGap(0, 0, Short.MAX_VALUE)
		                                );
		jPanel6Layout.setVerticalGroup(
				jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				             .addGap(0, 0, Short.MAX_VALUE)
		                              );

		jButton1.setText("jButton1");
		jButton1.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				jButton1ActionPerformed(evt);
			}
		});

		javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
		this.setLayout(layout);
		layout.setHorizontalGroup(
				layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				      .addGroup(layout.createSequentialGroup()
				                      .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				                                      .addGroup(layout.createSequentialGroup()
				                                                      .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				                                                                      .addGroup(layout.createSequentialGroup()
				                                                                                      .addContainerGap()
				                                                                                      .addComponent(etConsolInput)
				                                                                                      .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
				                                                                                      .addComponent(jButton1))
				                                                                      .addComponent(consolScrollPane))
				                                                      .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
				                                                      .addComponent(userScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE))
				                                      .addGroup(layout.createSequentialGroup()
				                                                      .addContainerGap()
				                                                      .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				                                                                      .addComponent(jTabbedPane1)
				                                                                      .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
				                                                      .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
				                                                      .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
				                                      .addGroup(layout.createSequentialGroup()
				                                                      .addComponent(connectionStatus1, javax.swing.GroupLayout.PREFERRED_SIZE, 134, javax.swing.GroupLayout.PREFERRED_SIZE)
				                                                      .addGap(0, 0, Short.MAX_VALUE)))
				                      .addContainerGap())
		                         );
		layout.setVerticalGroup(
				layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				      .addGroup(layout.createSequentialGroup()
				                      .addComponent(connectionStatus1, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
				                      .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
				                      .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
				                                      .addComponent(userScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
				                                      .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
				                                                                                                  .addComponent(consolScrollPane)
				                                                                                                  .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
				                                                                                                  .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
				                                                                                                                  .addComponent(etConsolInput, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
				                                                                                                                  .addComponent(jButton1))
				                                                                                                  .addGap(12, 12, 12)))
				                      .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
				                      .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				                                      .addComponent(jPanel5, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
				                                      .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
				                                                                                                  .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 216, javax.swing.GroupLayout.PREFERRED_SIZE)
				                                                                                                  .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
				                                                                                                  .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
				                                                                                                  .addGap(5, 5, 5)))
				                      .addContainerGap())
		                       );
	}// </editor-fold>

	private void cbMsgTypeActionPerformed(java.awt.event.ActionEvent evt) {
		// TODO add your handling code here:
	}

	private void btnSendActionPerformed(java.awt.event.ActionEvent evt) {
		if (msgListener != null) {
			String type = (String) cbMsgType.getSelectedItem();
			int typeInt = cbMsgType.getSelectedIndex();
			//String pin = txtPin.getText();
			String pin = msgFields.getValueAt(0, 1).toString();
			String value = msgFields.getValueAt(1, 1).toString();
			String chat = msgFields.getValueAt(2, 1).toString();
			String isTargeted = msgFields.getValueAt(3, 1).toString();
			String targetId = txtValue.getText();
			msgListener.messageEvent(new MsgBundle());

		}

	}

	public void setUserListData(Vector<String> v) {
		userList.setListData(v);
	}

	private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {
		append("Pin 2 Toggled to:" + (pins.get(0).toggle() ? "True" : "False"));
	}

	private void txtPinActionPerformed(java.awt.event.ActionEvent evt) {
		// TODO add your handling code here:
	}

	private void txtValueActionPerformed(java.awt.event.ActionEvent evt) {
		// TODO add your handling code here:
	}

	private void userListValueChanged(javax.swing.event.ListSelectionEvent evt) {
		if (msgUserListener != null) msgUserListener.itemSelectedEvent(userList.getSelectedIndex());
		System.out.println("Selected User List Index " + userList.getSelectedIndex());
	}

	public void userListSelected(int index) {

	}


	public class MsgBundle {

		int typeInt = cbMsgType.getSelectedIndex();
		public String type       = (String) cbMsgType.getSelectedItem();
		//String pin = txtPin.getText();
		public String pin        = msgFields.getValueAt(0, 1).toString();
		public String value      = msgFields.getValueAt(1, 1).toString();
		public String chat       = msgFields.getValueAt(2, 1).toString();
		public String isTargeted = msgFields.getValueAt(3, 1).toString();
		public String targetId   = msgFields.getValueAt(4, 1).toString();

		public MsgBundle() {
			print();
		}

		public String print() {
			String x = "\n";
			String s = type + "/" + typeInt + x;
			s += pin + x;
			s += value + x;
			s += chat + x;
			s += isTargeted + x;
			s += targetId;
			System.out.println(s);
			return s;
		}
	}

	public void setMessageListener(MessageListener m) {
		msgListener = m;
	}

	public void setUserListListener(UserListListener m) {
		msgUserListener = m;
	}

	public interface MessageListener {
		void messageEvent(MsgBundle bundle);
	}

	public interface UserListListener {
		void itemSelectedEvent(int index);
	}

	public void append(String s) {
		txtConsol.append(s + "\n");
	}

	// Variables declaration - do not modify
	private javax.swing.JButton                  btnSend;
	private javax.swing.JComboBox                cbMsgType;
	private org.zu.ardulink.gui.ConnectionPanel  connectionPanel1;
	private org.zu.ardulink.gui.ConnectionStatus connectionStatus1;
	private javax.swing.JScrollPane              consolScrollPane;
	private DigitalPinStatusTester               digitalPinStatusTester1;
	private DigitalPinStatusTester               digitalPinStatusTester2;
	private DigitalPinStatusTester               digitalPinStatusTester3;
	private DigitalPinStatusTester               digitalPinStatusTester4;
	private DigitalPinStatusTester               digitalPinStatusTester5;
	private DigitalPinStatusTester               digitalPinStatusTester6;
	private javax.swing.JTextField               etConsolInput;
	private javax.swing.JButton                  jButton1;
	private javax.swing.JFrame                   jFrame1;
	private javax.swing.JLabel                   jLabel1;
	private javax.swing.JLabel                   jLabel2;
	private javax.swing.JLabel                   jLabel3;
	private javax.swing.JPanel                   jPanel1;
	private javax.swing.JPanel                   jPanel10;
	private javax.swing.JPanel                   jPanel2;
	private javax.swing.JPanel                   jPanel3;
	private javax.swing.JPanel                   jPanel4;
	private javax.swing.JPanel                   jPanel5;
	private javax.swing.JPanel                   jPanel6;
	private javax.swing.JPanel                   jPanel7;
	private javax.swing.JPanel                   jPanel8;
	private javax.swing.JScrollPane              jScrollPane3;
	private javax.swing.JSplitPane               jSplitPane1;
	private javax.swing.JTabbedPane              jTabbedPane1;
	private javax.swing.JTabbedPane              jTabbedPane2;
	public  javax.swing.JTable                   msgFields;
	private javax.swing.JPanel                   tabDigital;
	private javax.swing.JPanel                   tabMessage;
	private javax.swing.JTextField               txtChat;
	private javax.swing.JTextArea                txtConsol;
	private javax.swing.JTextField               txtPin;
	public  javax.swing.JTextField               txtValue;
	private javax.swing.JList                    userList;
	private javax.swing.JScrollPane              userScrollPane;
	// End of variables declaration
}
