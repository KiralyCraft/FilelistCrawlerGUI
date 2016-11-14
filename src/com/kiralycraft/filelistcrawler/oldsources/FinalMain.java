package com.kiralycraft.filelistcrawler.oldsources;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.kiralycraft.filelistcrawler.newsources.SaveManager;

public class FinalMain 
{
	
	public static void main(String[] args) 
	{
		RunThread rt = new RunThread();
		
	}

}
class RunThread
{
	SaveManager saveman;
	String cfduid="",phpsessid="",uid="",pass="",fl="";
	String username="",password="";
	String downloadLocation="";
	boolean onlyFreeleech=false;
	double seedLeechRatio=0.6;
	File downloadFolder;
	///////////////////////////////////////////////////
	ArrayList<TorrentData> torrentDataList = new ArrayList<TorrentData>();
	class TorrentData
	{
		String torrentName;
		boolean freeleech=false;
		String downloadLink;
		double downloadSize = 0;
		int leechers = 0;
		int seeders = 0;
		float leechseedratio=9999;
		
		public TorrentData(String namex,boolean freelechx, String downLinkx, double sizeGBx,int leechersx,int seedersx)
		{
			torrentName = namex;
			freeleech = freelechx;
			downloadLink = downLinkx;
			downloadSize = sizeGBx;
			leechers = leechersx;
			seeders = seedersx;
			if (leechers>0)
			{
				leechseedratio = seeders / leechers;
			}
		}
	}
	///////////////////////////////////////////////////
	
	public RunThread()
	{
		System.setProperty("http.proxyHost", "127.0.0.1");
	    System.setProperty("https.proxyHost", "127.0.0.1");
	    System.setProperty("http.proxyPort", "8888");
	    System.setProperty("https.proxyPort", "8888");
	    
		log("Starting up..");
		saveman = new SaveManager();
		while (!saveman.loadFinished)
		{
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {}
		}
		
		if (saveman.getKey("enabled").equals("true"))
		{
			username = saveman.getKey("username");
			password = saveman.getKey("password");
			downloadLocation = saveman.getKey("downLocation");
			downloadFolder = new File(downloadLocation);
			if (downloadFolder.exists() && downloadFolder.isDirectory())
			{
				File testFile = new File(downloadFolder.getAbsolutePath()+File.separator+"writeTest.txt");
				boolean writePerms = true;
				try 
				{
					if (testFile.createNewFile()) 
					{
						testFile.delete();
					} 
				} catch (Exception e) 
				{
					writePerms = false;
				}
				
				if (!writePerms)
				{
					log("================================");
					log("You don't have write permissions at: "+downloadLocation+"! Aborting!");
					log("================================");
					return;
				}
			}
			seedLeechRatio = Double.parseDouble(saveman.getKey("seedLeechRatio"));
			onlyFreeleech = Boolean.parseBoolean(saveman.getKey("downloadFreeleechOnly"));
			
			while(true)
			{
				try 
				{
					log("Clearing Torrent Cache ...");
					torrentDataList.clear();
					
					log("Acquiring CFDUID ...");
					List<String> tmpLoginData = getCFDUID();
					updateData(tmpLoginData);
					log("Logging in ...");
					updateData(getLoginData(cfduid,username,password));
					log("Getting the 1st torrent page ...");
					String browsePage = getBrowsePage(cfduid,pass,phpsessid,uid,fl);
					log("Grabbing torrent data ...");
					getTorrentData(browsePage);
					log("Analyzing results ...");
					parseTorrentData();
					log("Analysis complete.");
//					Thread.sleep(10000);
					Thread.sleep(1800000);
				} catch (Exception e) 
				{
					log("ERROR OCCURED! "+e.getMessage());
					e.printStackTrace();
					try 
					{
						Thread.sleep(600000);
					} catch (InterruptedException e1) {}
				}
				
			}
		}
		else
		{
			saveman.setKey("enabled", "false");
			saveman.setKey("username", "enterUsernameHere");
			saveman.setKey("password", "enterPasswordHere");
			saveman.setKey("downLocation", "enter.Download.Location.Here(For.The.Torrents)");
			saveman.setKey("seedLeechRatio", "0.6");
			saveman.setKey("downloadFreeleechOnly", "true");
			
			log("A new config file has been created. Please edit your settings, then change 'enabled' to 'true' to continue");
		}
	}
	////////////////////////////////////
	///////UTILITIES////////////////////
	////////////////////////////////////
	public String getHour()
	{
		Date date=new Date();    
		return "["+new SimpleDateFormat("HH:mm").format(date)+"]";
	}
	public void log(String str)
	{
		System.out.println(getHour()+" "+str);
	}
	public String getTorrentFilename(String str)
	{
		try 
		{
			return URLDecoder.decode(str.substring(str.lastIndexOf("=")+1,str.length()),"UTF-8").replace("\"", "");
		} catch (UnsupportedEncodingException e) {
			return str.substring(str.lastIndexOf("=")+1,str.length()).replace("\"", "");
		}
	}
	///////////////////////////////////
	///////////////////////////////////
	///////////////////////////////////
	
	/////////////////////////////////////
	///////////WEB INTERFACE/////////////
	/////////////////////////////////////
	public List<String> getCFDUID() throws Exception
	{
		String request        = "http://filelist.ro/login.php";
		URL    url            = new URL( request );
		HttpURLConnection conn= (HttpURLConnection) url.openConnection();           
		conn.setDoOutput( true );
		conn.setInstanceFollowRedirects( false );
		conn.setRequestMethod( "GET" );
		conn.setUseCaches( false );
		List<String> cookies = conn.getHeaderFields().get("Set-Cookie");
		return cookies;
	}
	private void updateData(List<String> tmpCookies) 
	{
		for (String s:tmpCookies)
		{
			if (!s.contains("deleted"))
			{
				if (s.contains("__cfduid"))
				{
					cfduid = s.substring(s.indexOf("=")+1, s.indexOf(";"));
					log("CFDUID: "+cfduid);
				}
				else if (s.contains("PHPSESSID"))
				{
					phpsessid = s.substring(s.indexOf("=")+1, s.indexOf(";"));
					log("PHPSESSID: "+phpsessid);
				}
				else if (s.contains("pass"))
				{
					pass = s.substring(s.indexOf("=")+1, s.indexOf(";"));
					log("PASS: "+pass);
				}
				else if (s.contains("uid"))
				{
					uid = s.substring(s.indexOf("=")+1, s.indexOf(";"));
					log("UID: "+uid);
				}
				else if (s.contains("fl"))
				{
					fl = s.substring(s.indexOf("=")+1, s.indexOf(";"));
					log("FL: "+fl);
				}
			}
		}
	}
	public List<String> getLoginData(String cfduidtmp,String user,String password) throws Exception
	{
		String urlParameters  = "username="+user+"&password="+password;
		byte[] postData       = urlParameters.getBytes( StandardCharsets.UTF_8 );
		int    postDataLength = postData.length;
		String request        = "http://filelist.ro/takelogin.php";
		URL    url            = new URL( request );
		HttpURLConnection conn= (HttpURLConnection) url.openConnection();    
		conn.setDoOutput( true );
		conn.setInstanceFollowRedirects( false );
		conn.setRequestMethod( "POST" );

		conn.setRequestProperty( "Cookie", "__cfduid="+cfduidtmp);
		conn.setRequestProperty( "Content-Length", Integer.toString( postDataLength ));
		conn.connect();
		try( DataOutputStream wr = new DataOutputStream( conn.getOutputStream())) 
		{
		   wr.write( postData );
		}
		
		List<String> cookies = conn.getHeaderFields().get("Set-Cookie");
		return cookies;
	}
	public String getBrowsePage(String cfduidtmp,String passtmp,String phpsessidtmp,String uidtmp, String fl2) throws Exception
	{
		String request        = "http://filelist.ro/browse.php";
		URL    url            = new URL( request );
		HttpURLConnection conn= (HttpURLConnection) url.openConnection();    
		conn.setDoOutput( true );
		conn.setInstanceFollowRedirects( false );
		conn.setRequestMethod( "POST" );
		conn.setRequestProperty( "Cookie", "__cfduid="+cfduidtmp+"; PHPSESSID="+phpsessidtmp+"; uid="+uidtmp+"; pass="+passtmp+"; fl="+fl2);
		conn.connect();
		
		int responseCode = conn.getResponseCode();
		log("Response Code : " + responseCode);
		BufferedReader in = new BufferedReader(
		        new InputStreamReader(conn.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();

		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine+"\n");
		}
		in.close();
		
		return response.toString();
	}
	public void downloadTorrent(String cfduidtmp,String passtmp,String phpsessidtmp,String uidtmp,String downloadLink,File downLoc) throws Exception
	{
		String request        = "http://filelist.ro/"+downloadLink;
		URL    url            = new URL( request );
		HttpURLConnection conn= (HttpURLConnection) url.openConnection();    
		conn.setDoOutput( true );
		conn.setInstanceFollowRedirects( false );
		conn.setRequestMethod( "GET" );
		conn.setRequestProperty( "Cookie", "__cfduid="+cfduidtmp+"; PHPSESSID="+phpsessidtmp+"; uid="+uidtmp+"; pass="+passtmp);
		conn.connect();
		
		int responseCode = conn.getResponseCode();
		log("Response Code : " + responseCode);
		
		String contentDisposition = conn.getHeaderField("Content-Disposition");
		String fileName = contentDisposition.substring(contentDisposition.indexOf("=")+1, contentDisposition.length()).replace("\"", "");
		
		BufferedInputStream in = new BufferedInputStream(conn.getInputStream());
		FileOutputStream fos = new FileOutputStream(downLoc.getAbsolutePath()+File.separator+fileName);
		byte buf[] = new byte[1024];
		int len;
		while ((len = in.read(buf)) > 0)
        {
			fos.write(buf, 0, len);
		}
		in.close();
		fos.close();
	}
	//////////////////////////////////////
	//////////////////////////////////////
	//////////////////////////////////////
	
	//////////////////////////////////////
	////////PROCESSING DATA///////////////
	//////////////////////////////////////
	private void getTorrentData(String str) 
	{
		Document doc = Jsoup.parse(str);
		
//		Element content = doc.getElementById("content");
		Elements torrents = doc.select("div.torrentrow");
		for (Element link : torrents)
		{
			  String torrentName;
			  boolean freeleech=false;
			  String downloadLink;
			  double downloadSize = 0;
			  int leechers = 0;
			  int seeders = 0;
			  ////////////////////////////
	          Elements torrentData = link.select("div.torrenttable");
	          
	          torrentName = torrentData.get(1).select("a[href]").get(0).text();
	          
	          for (Element e:torrentData.get(1).getElementsByTag("img"))
	          {
	        	  if (e.attr("src").contains("freeleech"))
	        	  {
	        		  freeleech = true;
	        		  break;
	        	  }
	          }
	          
	          downloadLink = torrentData.get(2).select("a[href]").get(0).attr("href");

	          
	          Element torrentSizeDiv = torrentData.get(6);
	          String torrentSizeData[] = torrentSizeDiv.text().split("\\ ");

	          if (torrentSizeData[1].equalsIgnoreCase("GB"))
	          {
	        	  downloadSize = Double.parseDouble(torrentSizeData[0]);
	          }
	          else if (torrentSizeData[1].equalsIgnoreCase("MB"))
	          {
	        	  downloadSize = Double.parseDouble(torrentSizeData[0])/1024;
	          }
	          Element seedersDiv = torrentData.get(8);
	          seeders = Integer.parseInt(seedersDiv.text().replaceAll("\\D", ""));
	          Element leechersDiv = torrentData.get(9);
	          leechers = Integer.parseInt(leechersDiv.text().replaceAll("\\D", ""));
	          ////////////////////////////////////////////////
	          torrentDataList.add(new TorrentData(torrentName,freeleech,downloadLink,downloadSize,leechers,seeders));
	    }
	}

	private void parseTorrentData() 
	{
		int totalSize=0;
		for (TorrentData td:torrentDataList)
		{
			
			boolean freelechCheck = onlyFreeleech && td.freeleech;
			boolean ratioCheck = td.leechseedratio<=seedLeechRatio;
			if (freelechCheck && ratioCheck)
			{
				log("==========================================");
				log(td.torrentName+" - Checking requirements ...");
				if (downloadFolder.getFreeSpace()/1024/1024/1024>=(totalSize+td.downloadSize))
				{
					if (!new File(downloadFolder.getAbsolutePath()+File.separator+getTorrentFilename(td.downloadLink)).exists())
					{
						totalSize+=td.downloadSize;
						log("Okay! Downloading torrent: "+td.torrentName);
						log("Total size: "+td.downloadSize+" GB, seeders: "+td.seeders+", leechers: "+td.leechers+", ratio: "+td.leechseedratio);
						log("Free space remaining after the download: "+(downloadFolder.getFreeSpace()/1024/1024/1024-totalSize)+" GB");
						try 
						{
							downloadTorrent(cfduid,pass,phpsessid,uid,td.downloadLink,downloadFolder);
							log("Success!");
						} 
						catch (Exception e) 
						{
							log("ERROR! "+e.getMessage());
						}
					}
					else
					{
						log(td.torrentName+" already exists.");
					}
				}
				else
				{
					log("Not enough space to download "+td.torrentName);
				}
				log("==========================================");
				log("");
			}
		}
	}
	//////////////////////////////////////
	//////////////////////////////////////
	//////////////////////////////////////
}
