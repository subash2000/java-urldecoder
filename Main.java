import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.net.InetAddress;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.io.InputStreamReader;
import java.io.PrintWriter;

class IPDecoder
{
    InetAddress ip;

    IPDecoder(String hostname)
    {
        try
        {
            ip = InetAddress.getByName(hostname);
        }
        catch(Exception e)
        {
            e.printStackTrace();

        }
    }

    private static String yesNo(boolean val)
    {
        return val?"Yes":"No";
    }

    private  String ipVersion()
    {
        if(ip.getAddress().length == 4) return "IPv4";
        return "IPv6";
    }

    public void showDetails() throws IOException
    {
        if(ip != null)
        {
            System.out.println("\nIp address and Details : ");
            System.out.println("\tIp address : "+ip.getHostAddress());
            System.out.println("\tIp version : "+ipVersion());
            System.out.println("\tIs Local address ? "+yesNo(ip.isAnyLocalAddress()));
            System.out.println("\tIs Link Local Address ? "+yesNo(ip.isLinkLocalAddress()));
            System.out.println("\tIs Loop Back Address ? "+yesNo(ip.isLoopbackAddress()));
            System.out.println("\tIs Multicast Address ? "+yesNo(ip.isMulticastAddress()));
            System.out.println("\tIs Site local Address ? "+ yesNo(ip.isSiteLocalAddress()));
            System.out.println("\tIs Reachable ? "+ yesNo(ip.isReachable(5000)));
        }
    }
}

class URLDecoder
{
    URL url;
    IPDecoder ip;

    URLDecoder(String url)
    {
        try
        {
            this.url = new URL(url);
            ip = new IPDecoder(this.url.getHost());

        }
        catch(Exception e)
        {
            System.out.println("Please enter valid URL");
            System.out.println(e);

        }
        
    }

    public void printUrlParts()
    {
        System.out.println("\nURL Parts");
        System.out.println("\tProtocol : "+url.getProtocol());
        System.out.println("\tAuthority : "+url.getAuthority());
        System.out.println("\tPath : "+url.getPath());
        System.out.println("\tFragments : "+url.getRef());
        System.out.println("\tQuery : "+url.getQuery());
    }


    public void printContents() 
    {
        try
        {
            URLConnection uc = url.openConnection();
            printHeaderContents(uc);
            System.out.println("\nRaw Content : (This may take few seconds)");
            InputStream in = uc.getInputStream();
            in = new BufferedInputStream(in);
            Reader r = new InputStreamReader(in);
            try(PrintWriter fout = new PrintWriter("Content.txt"))
            {
                int b;
                while((b = r.read()) != -1)
                {
                    fout.write((char)b);
                }
                System.out.println("Open this file to see the contents of the URL : "+System.getProperty("user.dir")+"/Content.txt");
            }
        }
        catch(Exception e)
        {
            System.out.println(e);
        }
        
    }

    public void printHeaderContents(URLConnection uc)
    {
        System.out.println("\nHeader Details :");
        Map<String,List<String>> headerFields = uc.getHeaderFields();
        for(Map.Entry<String,List<String>> entry: headerFields.entrySet())
        {
            System.out.println("\t"+entry.getKey());
            for(String val:entry.getValue())
            {
                System.out.println("\t\t"+val);
            }

        }

    }

    void showDetails() throws IOException
    {
        
        if(url != null)
        {
            printUrlParts();
            ip.showDetails();
            printContents();
        }
    }
}



public class Main 
{
    public static void main(String[] args)  throws IOException
    {
        System.out.print("Enter URL (eg: http://api.plos.org/search?q=title:DNA) ");
        Scanner sc = new Scanner(System.in);
        String url = sc.nextLine();
        new URLDecoder(url).showDetails();
        sc.close();
    }
    
}
