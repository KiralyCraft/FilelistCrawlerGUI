package com.kiralycraft.filelistcrawler.newsources;
import java.io.File;
import java.util.Base64;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;

public class Utils 
{
	public static String getHelpString()
	{
		return "Hi!\r\nWelcome to my Filelist.ro ratio maker. This is a brief tutorial on how to use the app and configure it properly. Unlike other ratio makers, this one does not send fake data to the tracker, so it's completely safe to use. This app will check the torrents page every 30 minutes and select the best torrents that will improve your ratio while also helping the comunity. \r\n\r\nThis app will ONLY download the .torrents file. You have to configure your torrent client (BitTorrent, uTorrent, etc) to automatically load the files and download them.\r\n\r\nThe efficiency of this app only depends on your upload speed.\r\n\r\nThere are two ways to authenticate yourself:\r\n- The recommanded one: Enter your username and password for Filelist.ro and let the app do the rest.\r\n- The more advanced one: Login on Filelist.ro with your browser. After that, go to the section where the cookies are stored in the Options menu of your browser, and complete the requested fields from the Settings tab with the data from your browser. This way, if you do not trust this application, you can always change your password because this method does not need your username and password to work. \r\n\r\nYou should NOT touch the SeedLeechRatio and FreeleechOnly fields unless you know what you're doing.\r\n\r\nTo configure this app:\r\n- Enter your login data using either method specified above\r\n- Select a download folder. This is the folder where the .torrent files will be downloaded\r\n- Click Save to confirm your entered data\r\n- Go to the Run Tab and click Start\r\n- Leave this app open (it works when it's minimised too). \r\n\r\nTo configure your torrent client:\r\n- Go to the Preferences menu\r\n- Find the Directories submenu\r\n- Select the \"automatically load .torrents from\" and select the folder you used in the configuration of this app\r\n- DO NOT tick the \"move torrents\" or \"Delete torrents\" checkboxes, if any\r\n- Tick the \"Put new downloads in\" checkbox, and select a folder on your hard disk where the torrent itself (NOT the .torrent files) will be downloaded.\n\nFor a video tutorial, check out my YouTube channel: https://www.youtube.com/user/ucuucu234/videos"; 
	}
	public static String encode(String s)
	{
		byte[]   bytesEncoded = Base64.getEncoder().encode(s.getBytes());
		return new String(bytesEncoded).replace("=", "");
	}
	public static String decode(String s)
	{
		byte[] valueDecoded= Base64.getDecoder().decode(s );
		return new String(valueDecoded);
	}
	public static String getAppdata()
	{
		return System.getenv("APPDATA");
	}
	public static void setEnableLC(GUI guiInstance,boolean enabled)
	{
		guiInstance.txtTxtsettingscfduid.setEnabled(enabled);
		guiInstance.txtSettingsphpsessid.setEnabled(enabled);
		guiInstance.txtTxtsettingsuid.setEnabled(enabled);
		guiInstance.txtTxtsettingsfl.setEnabled(enabled);
		guiInstance.txtTxtsettingspass.setEnabled(enabled);
	}
	public static void setEnableLUP(GUI guiInstance,boolean enabled)
	{
		guiInstance.txtSettingspassword.setEnabled(enabled);
		guiInstance.txtSettingsusername.setEnabled(enabled);
	}
	public static void dialog(String text)
	{
		JOptionPane.showMessageDialog(null,text);
	}
	public static void saveData(GUI thisInstance, SaveManager saveman) 
	{
		if (thisInstance.txtSettingsusername.getText().length()==0)
		{
			thisInstance.txtSettingsusername.setText("None");
		}
		if (thisInstance.txtSettingspassword.getText().length()==0)
		{
			thisInstance.txtSettingspassword.setText("None");
		}
		if (thisInstance.txtTxtsettingscfduid.getText().length()==0)
		{
			thisInstance.txtTxtsettingscfduid.setText("None");
		}
		if (thisInstance.txtSettingsphpsessid.getText().length()==0)
		{
			thisInstance.txtSettingsphpsessid.setText("None");
		}
		if (thisInstance.txtTxtsettingsuid.getText().length()==0)
		{
			thisInstance.txtTxtsettingsuid.setText("None");
		}
		if (thisInstance.txtTxtsettingspass.getText().length()==0)
		{
			thisInstance.txtTxtsettingspass.setText("None");
		}
		if (thisInstance.txtTxtsettingsfl.getText().length()==0)
		{
			thisInstance.txtTxtsettingsfl.setText("None");
		}
		if (thisInstance.txtSettingsdownlocation.getText().length()==0)
		{
			thisInstance.txtSettingsdownlocation.setText("None");
		}
		if (thisInstance.txtSettingsSeedLeechRatio.getText().length()==0)
		{
			thisInstance.txtSettingsSeedLeechRatio.setText("0.6");
		}
		saveman.setKey("loginup", thisInstance.rdbtnLoginCuUser.isSelected()+"");
		saveman.setKey("loginupusername", thisInstance.txtSettingsusername.getText());
		saveman.setKey("loginupparola", thisInstance.txtSettingspassword.getText());
		saveman.setKey("loginccfduid", thisInstance.txtTxtsettingscfduid.getText());
		saveman.setKey("logincsessid", thisInstance.txtSettingsphpsessid.getText());
		saveman.setKey("logincuid", thisInstance.txtTxtsettingsuid.getText());
		saveman.setKey("logincpass", thisInstance.txtTxtsettingspass.getText());
		saveman.setKey("logincfl", thisInstance.txtTxtsettingsfl.getText());
		saveman.setKey("downlocation", thisInstance.txtSettingsdownlocation.getText());
		saveman.setKey("seedleechratio", thisInstance.txtSettingsSeedLeechRatio.getText());
		saveman.setKey("freelechonly", thisInstance.freelechonly.isSelected()+"");
	}
	public static boolean readSaveData(GUI thisInstance, SaveManager saveman) 
	{
		String tmpRead;
		
		tmpRead = saveman.getKey("loginup");
		if (!tmpRead.equals("null"))
		{
			boolean tmpB = Boolean.parseBoolean(tmpRead);
			thisInstance.rdbtnLoginCuUser.setSelected(tmpB);
			thisInstance.rdbtnLoginCuCookies.setSelected(!tmpB);
			setEnableLUP(thisInstance,tmpB);
			setEnableLC(thisInstance,!tmpB);
		}
		else
		{
			return false;
		}
		
		tmpRead = saveman.getKey("loginupusername");
		if (!tmpRead.equals("null"))
		{
			thisInstance.txtSettingsusername.setText(tmpRead);
		}
		else
		{
			return false;
		}
		
		tmpRead = saveman.getKey("loginupparola");
		if (!tmpRead.equals("null"))
		{
			thisInstance.txtSettingspassword.setText(tmpRead);
		}
		else
		{
			return false;
		}
		
		tmpRead = saveman.getKey("loginccfduid");
		if (!tmpRead.equals("null"))
		{
			thisInstance.txtTxtsettingscfduid.setText(tmpRead);
		}
		else
		{
			return false;
		}
		
		tmpRead = saveman.getKey("logincsessid");
		if (!tmpRead.equals("null"))
		{
			thisInstance.txtSettingsphpsessid.setText(tmpRead);
		}
		else
		{
			return false;
		}
		
		tmpRead = saveman.getKey("logincuid");
		if (!tmpRead.equals("null"))
		{
			thisInstance.txtTxtsettingsuid.setText(tmpRead);
		}
		else
		{
			return false;
		}
		
		tmpRead = saveman.getKey("logincpass");
		if (!tmpRead.equals("null"))
		{
			thisInstance.txtTxtsettingspass.setText(tmpRead);
		}
		else
		{
			return false;
		}
		
		tmpRead = saveman.getKey("logincfl");
		if (!tmpRead.equals("null"))
		{
			thisInstance.txtTxtsettingsfl.setText(tmpRead);
		}
		else
		{
			return false;
		}
		
		tmpRead = saveman.getKey("downlocation");
		if (!tmpRead.equals("null"))
		{
			thisInstance.txtSettingsdownlocation.setText(tmpRead);
		}
		else
		{
			return false;
		}
		
		tmpRead = saveman.getKey("seedleechratio");
		if (!tmpRead.equals("null"))
		{
			thisInstance.txtSettingsSeedLeechRatio.setText(tmpRead);
		}
		else
		{
			return false;
		}
		
		tmpRead = saveman.getKey("freelechonly");
		if (!tmpRead.equals("null"))
		{
			boolean tmpB = Boolean.parseBoolean(tmpRead);
			thisInstance.freelechonly.setSelected(tmpB);
		}
		else
		{
			return false;
		}
		return true;
	}
	public static File browseFolder(String title, final String filetype)
    {
    	    JFileChooser chooser = new JFileChooser(); 
    	    chooser.setCurrentDirectory(new java.io.File("."));
    	    chooser.setDialogTitle(title);
    	    chooser.setApproveButtonText("Select Folder");
    	    chooser.setApproveButtonToolTipText("Confirms the selection");
    	    chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
    	    chooser.setFileFilter(new FileFilter(){

                @Override
                public boolean accept(File f) {
                   if (f.isDirectory())
                   {
                	   return true;
                   }
                   else
                   {
                	   return false;
                   }
                }

                public String getDescription() {
                    return filetype;
                }

            });
    	    //
    	    // disable the "All files" option.
    	    //
    	    chooser.setAcceptAllFileFilterUsed(false);
    	    //    
    	    if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) 
    	    return chooser.getSelectedFile();
    	    else 
    	    return null;
    	  
    }
	public static void sleep(int ms)
	{
		try {
			Thread.sleep(ms);
		} catch (InterruptedException e) {
		}
	}
}
