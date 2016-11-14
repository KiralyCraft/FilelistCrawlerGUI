package com.kiralycraft.filelistcrawler.oldsources;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Main 
{
	String cfduid="",phpsessid="",uid="",pass="";
	
	String username="";
	String password="";
	
	
	
	
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
			leechseedratio = seeders / leechers;
		}
	}
	///////////////////////////////////////////////////
	public Main()
	{
		System.setProperty("http.proxyHost", "localhost");
		System.setProperty("http.proxyPort", "8888");
		try 
		{
			List<String> tmpCookies = getCFDUID();
			actualizeData(tmpCookies);
			
			List<String> loginData = getLoginData(cfduid,username,password);
			actualizeData(loginData);
			String str = getBrowsePage(cfduid, pass, phpsessid, uid);
			getTorrentData(str);
			
			for (TorrentData td:torrentDataList)
			{
//				System.out.println("Torrent name: "+td.torrentName+", size: "+td.downloadSize+" GB"+", is freelech: "+td.freeleech+", leechers: "+td.leechers+", seeders: "+td.seeders+", ratio: "+td.leechseedratio);
				if (td.leechseedratio<=0.6 && td.freeleech)
				{
					downloadTorrent(cfduid, pass, phpsessid, uid,td.downloadLink);
				}
			}
		} catch (Exception e) 
		{
			e.printStackTrace();
		}
	}
	
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
//	        	  System.out.println(e.attr("src"));
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
	          seeders = Integer.parseInt(seedersDiv.text());
	          Element leechersDiv = torrentData.get(9);
	          leechers = Integer.parseInt(leechersDiv.text());
	          ////////////////////////////////////////////////
	          torrentDataList.add(new TorrentData(torrentName,freeleech,downloadLink,downloadSize,leechers,seeders));
	    }
	}

	private void actualizeData(List<String> tmpCookies) 
	{
		for (String s:tmpCookies)
		{
			if (!s.contains("deleted"))
			{
				if (s.startsWith("__cfduid"))
				{
					cfduid = s.substring(s.indexOf("=")+1, s.indexOf(";"));
					System.out.println("CFDUID: "+cfduid);
				}
				else if (s.startsWith("PHPSESSID"))
				{
					phpsessid = s.substring(s.indexOf("=")+1, s.indexOf(";"));
					System.out.println("PHPSESSID: "+phpsessid);
				}
				else if (s.startsWith("pass"))
				{
					pass = s.substring(s.indexOf("=")+1, s.indexOf(";"));
					System.out.println("PASS: "+pass);
				}
				else if (s.startsWith("uid"))
				{
					uid = s.substring(s.indexOf("=")+1, s.indexOf(";"));
					System.out.println("UID: "+uid);
				}
			}
		}
	}

	public void writeDebugFile(String s)
	{
		try 
		{
			PrintWriter out = new PrintWriter("test.html");
			out.print(s);
			out.close();
		} catch (Exception e) 
		{
			System.err.print("Whoops! "+e.getMessage());
		}
	}
	public static void main(String[] args) 
	{
		Main m = new Main();

	}

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
	public byte[] downloadTorrent(String cfduidtmp,String passtmp,String phpsessidtmp,String uidtmp,String downloadLink) throws Exception
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
		System.out.print("Response Code : " + responseCode);
		if (responseCode==200)
		{
			System.out.println(", this is the expected response!");
		}
		else
		{
			System.out.println(", NOT the expected response!");
		}
		String contentDisposition = conn.getHeaderField("Content-Disposition");
		String fileName = contentDisposition.substring(contentDisposition.indexOf("=")+1, contentDisposition.length());
		
		System.out.println("Downloading "+fileName+"...");
		BufferedInputStream in = new BufferedInputStream(conn.getInputStream());
		FileOutputStream fos = new FileOutputStream(fileName);
		byte buf[] = new byte[1024];
		int len;
		while ((len = in.read(buf)) > 0)
        {
			fos.write(buf, 0, len);
		}
		in.close();
		fos.close();
		System.out.println("Done downloading "+fileName+"!");
		return null;
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
	public String getBrowsePage(String cfduidtmp,String passtmp,String phpsessidtmp,String uidtmp) throws Exception
	{
		String request        = "http://filelist.ro/browse.php";
		URL    url            = new URL( request );
		HttpURLConnection conn= (HttpURLConnection) url.openConnection();    
		conn.setDoOutput( true );
		conn.setInstanceFollowRedirects( false );
		conn.setRequestMethod( "POST" );
		conn.setRequestProperty( "Cookie", "__cfduid="+cfduidtmp+"; PHPSESSID="+phpsessidtmp+"; uid="+uidtmp+"; pass="+passtmp);
		conn.connect();
		
		int responseCode = conn.getResponseCode();
		System.out.print("Response Code : " + responseCode);
		if (responseCode==200)
		{
			System.out.println(", this is the expected response!");
		}
		else
		{
			System.out.println(", NOT the expected response!");
		}
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
}
