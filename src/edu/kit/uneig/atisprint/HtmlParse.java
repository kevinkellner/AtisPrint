package edu.kit.uneig.atisprint;
import java.io.IOException;
import java.util.NoSuchElementException;
import java.util.StringTokenizer;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.os.AsyncTask;

public class HtmlParse extends AsyncTask<Void, Void, String>{

    private Document pStatusPage;
    public AsyncResponse delegate = null;

    public HtmlParse() {
        
    }

    /**
     * Parses https://atis.informatik.kit.edu/1194.php for the printer with the lowest amount of items in its queue and
     * returns it.
     * 
     * @return Printer which should print the fastest. (Has the lowest elements in queue.) null if no such printer was
     *         found. (i.e ATIS is down, no printer there etc)
     * @throws IOException 
     */
    public String getBestPrinter() throws IOException {
        pStatusPage = Jsoup.connect("https://atis.informatik.kit.edu/1194.php").get();        
        
        int min = Integer.MAX_VALUE;
        String best = null;
        String regex = "<td>|<\\/td>";
        
        //get first and only table from html page
        Element table = pStatusPage.select("table").first();
        for (Element row : table.select("tr:gt(1)")) { // skip 2 tr elements
            Elements tds = row.select("td");
            
            //replace -raw in name, as we don't need it later
            String pName = tds.get(1).text().replaceAll("-raw", "");
            String pStatus = tds.get(2).text();
            
            int pQueue = parseStatus(pStatus);
            
            //check for correct syntax of printer name in case the page changes. 
            if (pName.matches("pool-sw[1-3]")) {                
                if (pQueue == 0) {
                    //no need to go further if queue is empty
                    best = pName.replaceAll(regex, "");
                    break;
                    
                } else if (pQueue < min){
                    //Check if printer queue is less than current queue minimum
                    min = pQueue;
                    best = pName.replaceAll(regex, "");
                }
            }
        }
        return best;
    }

    private int parseStatus(String pStatus) {
        StringTokenizer tok = new StringTokenizer(pStatus);
        String stat = tok.nextToken();
        int pNumber = 0;
        if (stat.equals("idle")) {
            System.out.println("IDLE");
            pNumber = 0;
        } else if (stat.equals("printing")) {
            tok.nextToken();
            try {
                pNumber = Integer.parseInt(tok.nextToken()); // get number of elements in print queue
            } catch (NoSuchElementException e) {
                // happens if only one item is in the queue and it hasn't refreshed yet. stupid.
                return 1;
            }
        }
        return pNumber;
    }

    @Override
    protected String doInBackground(Void... params) {
        try {
            return getBestPrinter();
        } catch (IOException e) {
            return "pool-sw1";
        }
        
    }
    
    @Override
    protected void onPostExecute(String result) {
        delegate.processFinish(result); // callback to activity that started
                                        // this async task.
    }

}
