package com.kiralycraft.filelistcrawler.newsources;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;

public class GUI extends JFrame {

	private JPanel contentPane;
	private JTextPane helpTextPane;
	private JScrollPane helpScrollPane;
	protected JTextField txtSettingsusername;
	protected JTextField txtSettingspassword;
	protected JTextField txtTxtsettingscfduid;
	protected JTextField txtSettingsphpsessid;
	protected JTextField txtTxtsettingsuid;
	protected JTextField txtTxtsettingspass;
	protected JTextField txtTxtsettingsfl;
	static GUI thisInstance;
	protected JRadioButton rdbtnLoginCuUser;
	protected JRadioButton rdbtnLoginCuCookies;
	private JButton btnCeSaAleg;
	private JButton btnSave;
	SaveManager saveman;
	protected JTextField txtSettingsdownlocation;
	protected JTextField txtSettingsSeedLeechRatio;
	protected JCheckBox freelechonly;
	private JButton btnBrowse;
	private JTextPane runtablog;
	private JButton btnStart;
	private JButton btnStop;
	private RunThread runThread;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
//		System.setProperty("http.proxyHost", "127.0.0.1");
//	    System.setProperty("https.proxyHost", "127.0.0.1");
//	    System.setProperty("http.proxyPort", "8888");
//	    System.setProperty("https.proxyPort", "8888");
		try {
			UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
		} catch (Throwable e) {
			e.printStackTrace();
		}
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					GUI frame = new GUI();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	
	public GUI() {
		saveman = new SaveManager();
		thisInstance = this;
		setResizable(false);
		setTitle("FilelistCrawler by KiralyCraft V1.1");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 386);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setBounds(10, 11, 424, 334);
		contentPane.add(tabbedPane);
		
		JPanel helppane = new JPanel();
		tabbedPane.addTab("Help", null, helppane, null);
		helppane.setLayout(null);
		
		helpScrollPane = new JScrollPane();
		helpScrollPane.setBounds(10, 11, 399, 283);
		helppane.add(helpScrollPane);
		
		helpTextPane = new JTextPane();
		helpTextPane.setEditable(false);
		helpScrollPane.setViewportView(helpTextPane);
		helpTextPane.setText("fasd");
		
		JPanel settingspane = new JPanel();
		tabbedPane.addTab("Settings", null, settingspane, null);
		settingspane.setLayout(null);
		
		txtSettingsusername = new JTextField();
		txtSettingsusername.setText("None");
		txtSettingsusername.setBounds(120, 12, 114, 20);
		settingspane.add(txtSettingsusername);
		txtSettingsusername.setColumns(10);
		
		JLabel lblUsernameFilelist = new JLabel("Filelist User:");
		lblUsernameFilelist.setBounds(12, 14, 101, 16);
		settingspane.add(lblUsernameFilelist);
		
		JLabel lblParolaFilelist = new JLabel("Filelist Password:");
		lblParolaFilelist.setBounds(12, 42, 100, 16);
		settingspane.add(lblParolaFilelist);
		
		txtSettingspassword = new JPasswordField();
		txtSettingspassword.setText("None");
		txtSettingspassword.setColumns(10);
		txtSettingspassword.setBounds(120, 40, 114, 20);
		settingspane.add(txtSettingspassword);
		
		JLabel lblCfduid = new JLabel("CFDUID:");
		lblCfduid.setBounds(12, 80, 55, 16);
		settingspane.add(lblCfduid);
		
		txtTxtsettingscfduid = new JTextField();
		txtTxtsettingscfduid.setText("None");
		txtTxtsettingscfduid.setBounds(120, 78, 114, 20);
		settingspane.add(txtTxtsettingscfduid);
		txtTxtsettingscfduid.setColumns(10);
		
		JLabel lblPhpsessionid = new JLabel("PHPSessionID:");
		lblPhpsessionid.setBounds(12, 104, 84, 16);
		settingspane.add(lblPhpsessionid);
		
		txtSettingsphpsessid = new JTextField();
		txtSettingsphpsessid.setText("None");
		txtSettingsphpsessid.setColumns(10);
		txtSettingsphpsessid.setBounds(120, 101, 114, 20);
		settingspane.add(txtSettingsphpsessid);
		
		JLabel lblUid = new JLabel("UID:");
		lblUid.setBounds(12, 127, 55, 16);
		settingspane.add(lblUid);
		
		txtTxtsettingsuid = new JTextField();
		txtTxtsettingsuid.setText("None");
		txtTxtsettingsuid.setBounds(120, 125, 114, 20);
		settingspane.add(txtTxtsettingsuid);
		txtTxtsettingsuid.setColumns(10);
		
		JLabel lblPass = new JLabel("Pass:");
		lblPass.setBounds(12, 147, 55, 16);
		settingspane.add(lblPass);
		
		txtTxtsettingspass = new JTextField();
		txtTxtsettingspass.setText("None");
		txtTxtsettingspass.setBounds(120, 145, 114, 20);
		settingspane.add(txtTxtsettingspass);
		txtTxtsettingspass.setColumns(10);
		
		JLabel lblFl = new JLabel("Fl:");
		lblFl.setBounds(12, 167, 55, 16);
		settingspane.add(lblFl);
		
		txtTxtsettingsfl = new JTextField();
		txtTxtsettingsfl.setText("None");
		txtTxtsettingsfl.setBounds(120, 165, 114, 20);
		settingspane.add(txtTxtsettingsfl);
		txtTxtsettingsfl.setColumns(10);
		
		rdbtnLoginCuUser = new JRadioButton("Login with User and Pass");
		rdbtnLoginCuUser.setSelected(true);
		rdbtnLoginCuUser.setBounds(248, 22, 198, 24);
		settingspane.add(rdbtnLoginCuUser);
		
		rdbtnLoginCuCookies = new JRadioButton("Login with Cookies");
		rdbtnLoginCuCookies.setBounds(248, 123, 132, 24);
		settingspane.add(rdbtnLoginCuCookies);
		
		ButtonGroup group = new ButtonGroup();
	    group.add(rdbtnLoginCuUser);
	    group.add(rdbtnLoginCuCookies);
		
		btnCeSaAleg = new JButton("Help me choose");
		btnCeSaAleg.setBounds(246, 75, 158, 26);
		settingspane.add(btnCeSaAleg);
		
		btnSave = new JButton("Save");
		btnSave.setBounds(248, 273, 158, 26);
		settingspane.add(btnSave);
		
		JLabel lblDownloadLocation = new JLabel("Download Location:");
		lblDownloadLocation.setBounds(12, 222, 111, 16);
		settingspane.add(lblDownloadLocation);
		
		JLabel lblSeedLeech = new JLabel("Seed / Leech Ratio:");
		lblSeedLeech.setBounds(12, 250, 108, 16);
		settingspane.add(lblSeedLeech);
		
		JLabel lblDownloadFreelechOnly = new JLabel("Freelech Only:");
		lblDownloadFreelechOnly.setBounds(12, 278, 80, 16);
		settingspane.add(lblDownloadFreelechOnly);
		
		txtSettingsdownlocation = new JTextField();
		txtSettingsdownlocation.setText("None");
		txtSettingsdownlocation.setBounds(131, 220, 163, 20);
		settingspane.add(txtSettingsdownlocation);
		txtSettingsdownlocation.setColumns(10);
		
		txtSettingsSeedLeechRatio = new JTextField();
		txtSettingsSeedLeechRatio.setText("0.6");
		txtSettingsSeedLeechRatio.setBounds(131, 248, 114, 20);
		settingspane.add(txtSettingsSeedLeechRatio);
		txtSettingsSeedLeechRatio.setColumns(10);
		
		freelechonly = new JCheckBox("");
		freelechonly.setSelected(true);
		freelechonly.setBounds(131, 278, 21, 21);
		settingspane.add(freelechonly);
		
		btnBrowse = new JButton("Browse");
		btnBrowse.setBounds(306, 217, 98, 26);
		settingspane.add(btnBrowse);
		
		JPanel runtab = new JPanel();
		tabbedPane.addTab("Run Tab", null, runtab, null);
		runtab.setLayout(null);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(12, 12, 395, 241);
		runtab.add(scrollPane);
		
		runtablog = new JTextPane();
		scrollPane.setViewportView(runtablog);
		
		btnStart = new JButton("Start");
		btnStart.setBounds(199, 265, 98, 26);
		runtab.add(btnStart);
		
		btnStop = new JButton("Stop");
		btnStop.setEnabled(false);
		btnStop.setBounds(309, 265, 98, 26);
		runtab.add(btnStop);
		
		init();
		run();
		
		if (!Utils.readSaveData(thisInstance,saveman))
		{
			log("Save file is corrupted or incomplete. Attempting to repair");
			Utils.saveData(thisInstance, saveman);
		}
		else
		{
			log("Settings were read succesfully.");
		}
	}
	public String getHour()
	{
		Date date=new Date();    
		return "["+new SimpleDateFormat("HH:mm").format(date)+"]";
	}
	public void log(String str)
	{
		if (runtablog.getText().length()>0)
		{
			runtablog.setText(runtablog.getText()+"\n"+getHour()+" "+str);
			
		}
		else
		{
			runtablog.setText(getHour()+" "+str);
		}
		runtablog.setCaretPosition(runtablog.getText().length());
	}
	private void init()
	{
		Utils.setEnableLC(thisInstance, false);
		rdbtnLoginCuUser.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) 
			{
				Utils.setEnableLC(thisInstance, false);
				Utils.setEnableLUP(thisInstance, true);
			}
		});
		rdbtnLoginCuCookies.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) 
			{
				Utils.setEnableLC(thisInstance, true);
				Utils.setEnableLUP(thisInstance, false);
			}
		});
		btnCeSaAleg.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) 
			{
				Utils.dialog("If you want to login with your Filelist.ro username and password, select the first option. This is the recommanded option.");
				Utils.dialog("If you want to use an already logged in session, select the second option.\nThis is not recommanded because you might have to re-enter your login data\nevery time you login on Filelist.ro on another location.");
			}
		});
		btnSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) 
			{
				Utils.saveData(thisInstance,saveman);
			}
		});
		btnBrowse.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) 
			{
				File fil = Utils.browseFolder("Selecteaza destinatia pentru fisierele .torrent", "Folder");
				if (fil!=null)
				{
					if (fil.isDirectory())
					{
						txtSettingsdownlocation.setText(fil.getAbsolutePath());
					}
					else
					{
						Utils.dialog("Trebuie sa selectezi un folder, nu un fisier.");
					}
				}
			}
		});
		btnStart.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) 
			{
				btnStart.setEnabled(false);
				btnStop.setEnabled(true);
				runtablog.setText("");
				if (runThread==null)
				{
					runThread = new RunThread(rdbtnLoginCuUser.isSelected(),txtSettingsusername.getText(),txtSettingspassword.getText(),txtTxtsettingscfduid.getText(),txtSettingsphpsessid.getText(),txtTxtsettingspass.getText(),txtTxtsettingsuid.getText(),txtTxtsettingsfl.getText(),txtSettingsdownlocation.getText(),freelechonly.isSelected()+"",txtSettingsSeedLeechRatio.getText(),saveman,thisInstance);
					runThread.start();
					log("Starting up");
				}
			}
		});
		btnStop.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) 
			{
				btnStart.setEnabled(true);
				btnStop.setEnabled(false);
				runThread.interupt();
				runThread = null;
				log("Killing the worker. Wait for confirmation.");
			}
		});
	}
	private void run()  
	{
		helpTextPane.setText(Utils.getHelpString());
		helpTextPane.setCaretPosition(0);
	}
}
