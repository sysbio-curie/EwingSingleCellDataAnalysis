package getools.scripts;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.Vector;

import com.meterware.httpunit.*;

import javax.net.ssl.HttpsURLConnection;

import vdaoengine.utils.Utils;

public class UseTOPPgeneSuiteForEnrichment {

	private final String USER_AGENT = "Mozilla/5.0";

	
	public static void main(String[] args) {
		
		try{
		// Test example
			
		Vector<String> glist = Utils.loadStringListFromFile("C:/Datas/ToppGene/test_set");
		produceToppGeneReportInFile(glist,"Enrichment results for IC1","C:/Datas/ToppGene/IC1.html");
		System.exit(0);
			
		UseTOPPgeneSuiteForEnrichment http = new UseTOPPgeneSuiteForEnrichment();
		
		System.out.println("\nTesting 2 - Send Http POST request");
		//http.sendPostExample();
		Vector<String> list = Utils.loadStringListFromFile("C:/Datas/ToppGene/test_set");
		String out = http.sendPostToppGene(list);
		String userid = extractUserID(out);
		System.out.println("User_id="+userid);
		
		String categories[] = {"GeneOntologyMolecularFunction","GeneOntologyBiologicalProcess","GeneOntologyCellularComponent","HumanPheno","MousePheno","Domain","Pathway","Pubmed","Interaction","Cytoband","TFBS","GeneFamily","Coexpression","CoexpressionAtlas","Computational","MicroRNA","Drug","Disease"};
		//String categories[] = {"GeneOntologyMolecularFunction","GeneOntologyBiologicalProcess"};
		//String categories[] = {"GeneOntologyMolecularFunction"};
		
		String out1 = http.sendPostToppGeneSubmitAction(userid,categories);
		Thread.sleep(20000);
		//System.out.println("Done.");
		String out2 = vdaoengine.utils.VDownloader.downloadURL("https://toppgene.cchmc.org/output.jsp?userdata_id="+userid);
		
		out2 = Utils.replaceString(out2, "href=\"styles.css", "href=\"toppgene/styles.css");
		out2 = Utils.replaceString(out2, "src=\"output.js", "src=\"toppgene/output.js");
		out2 = Utils.replaceString(out2, "href='/download.jsp", "href='https://toppgene.cchmc.org/download.jsp");
		out2 = Utils.replaceString(out2, "href='/display.jsp", "href='https://toppgene.cchmc.org/display.jsp");
		out2 = Utils.replaceString(out2, "href=\"ResultsMatrix", "href=\"https://toppgene.cchmc.org/ResultsMatrix");
		out2 = Utils.replaceString(out2, "href=\"/showTermDetail.jsp", "href=\"https://toppgene.cchmc.org/showTermDetail.jsp");
		out2 = Utils.replaceString(out2, "href='/showQueryTerms.jsp", "href='https://toppgene.cchmc.org/showQueryTerms.jsp");
		
		
		
		
		
		
		FileWriter fw = new FileWriter("C:/Datas/ToppGene/test.html");
		fw.write(out);
		fw.close();

		fw = new FileWriter("C:/Datas/ToppGene/test_temp.html");
		fw.write(out1);
		fw.close();
		
		fw = new FileWriter("C:/Datas/ToppGene/test_res.html");
		fw.write(out2);
		fw.close();
		
		
		
		//example(null);
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}
	
	public static void produceToppGeneReportInFile(Vector<String> geneList, String analysisName, String fileName) throws Exception{
		String out = produceToppGeneReport(geneList,analysisName);
		FileWriter fw = new FileWriter(fileName);
		fw.write(out);
		fw.close();
	}
	
	public static String produceToppGeneReport(Vector<String> geneList, String analysisName) throws Exception{
		String res = "";

		UseTOPPgeneSuiteForEnrichment http = new UseTOPPgeneSuiteForEnrichment();
		
		String out = http.sendPostToppGene(geneList);
		String userid = extractUserID(out);
		System.out.println("User_id="+userid);
		
		String categories[] = {"GeneOntologyMolecularFunction","GeneOntologyBiologicalProcess","GeneOntologyCellularComponent","HumanPheno","MousePheno","Domain","Pathway","Pubmed","Interaction","Cytoband","TFBS","GeneFamily","Coexpression","CoexpressionAtlas","Computational","MicroRNA","Drug","Disease"};
		//String categories[] = {"GeneOntologyMolecularFunction","GeneOntologyBiologicalProcess"};
		//String categories[] = {"GeneOntologyMolecularFunction"};
		
		String out1 = http.sendPostToppGeneSubmitAction(userid,categories);
		Thread.sleep(20000);
		String out2 = "";
		while(true){
			out2 = vdaoengine.utils.VDownloader.downloadURL("https://toppgene.cchmc.org/output.jsp?userdata_id="+userid);
			if(out2.length()>3000) break;
			Thread.sleep(5000);
		}
		
		//System.out.println("Done.");
		
		
		out2 = Utils.replaceString(out2, "href=\"styles.css", "href=\"toppgene/styles.css");
		out2 = Utils.replaceString(out2, "src=\"output.js", "src=\"toppgene/output.js");
		out2 = Utils.replaceString(out2, "href='/download.jsp", "href='https://toppgene.cchmc.org/download.jsp");
		out2 = Utils.replaceString(out2, "href='/display.jsp", "href='https://toppgene.cchmc.org/display.jsp");
		out2 = Utils.replaceString(out2, "href=\"ResultsMatrix", "href=\"https://toppgene.cchmc.org/ResultsMatrix");
		out2 = Utils.replaceString(out2, "href=\"/showTermDetail.jsp", "href=\"https://toppgene.cchmc.org/showTermDetail.jsp");
		out2 = Utils.replaceString(out2, "href='/showQueryTerms.jsp", "href='https://toppgene.cchmc.org/showQueryTerms.jsp");
		
		out2 = Utils.replaceString(out2, ">Results<", ">"+analysisName+"<");
		
		
		res = out2;
		
		return res;
	}
	
	
	private String sendPostToppGene(Vector<String> geneList) throws Exception {

		//String url = "https://toppgene.cchmc.org/enrichment.jsp";
		String url = "https://toppgene.cchmc.org/CheckInput.action";
		URL obj = new URL(url);
		HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();

		//add reuqest header
		con.setRequestMethod("POST");
		con.setRequestProperty("User-Agent", USER_AGENT);
		con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
		
		con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

		
		HashMap<String,String> params = new HashMap<String,String>();
		
		String list = "";
		for(String s:geneList) list+=s+"\n";
		
		params.put("training_set", list);
		params.put("query", "TOPPFUN");
		params.put("type", "HGNC");
		
		String urlParameters = getQuery(params);
		
		//System.out.println(urlParameters);
		
		// Send post request
		con.setDoOutput(true);
		DataOutputStream wr = new DataOutputStream(con.getOutputStream());
		wr.writeBytes(urlParameters);
		wr.flush();
		wr.close();

		int responseCode = con.getResponseCode();
		//System.out.println("\nSending 'POST' request to URL : " + url);
		//System.out.println("Post parameters : " + urlParameters);
		//System.out.println("Response Code : " + responseCode);

		BufferedReader in = new BufferedReader(
		        new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();

		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();
		
		String res = response.toString();
		return res;
		//print result
		//System.out.println();

	}
	
	private String sendPostToppGeneSubmitAction(String userid, String categories[]) throws Exception {

		//String url = "https://toppgene.cchmc.org/enrichment.jsp";
		String url = "https://toppgene.cchmc.org/Submit.action";
		URL obj = new URL(url);
		HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();

		//add reuqest header
		con.setRequestMethod("POST");
		con.setRequestProperty("User-Agent", USER_AGENT);
		con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
		
		con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

		
		HashMap<String,String> params = new HashMap<String,String>();
		
		Vector<String> names = new Vector<String>();
		Vector<String> values = new Vector<String>();
		
		//params.put("userdata_id", userid);
		names.add("userdata_id"); values.add(userid);
		
		params.put("all_min", "1");
		names.add("all_min"); values.add("1");
		params.put("all_max", "500");
		names.add("all_max"); values.add("500");
		params.put("all_correction", "FDR");
		names.add("all_correction"); values.add("FDR");
		params.put("all_p_value", "0.05");
		names.add("all_p_value"); values.add("0.05");
		
		params.put("sample_size", "0");
		names.add("sample_size"); values.add("0");
		params.put("min_feature_count", "2");
		names.add("min_feature_count"); values.add("2");
		
		for(String cat:categories){
			//System.out.println(cat);
		//params.put("category", cat);
			names.add("category"); values.add(cat);
		//params.put(cat+"_min", "1");
			names.add(cat+"_min"); values.add("1");
		//params.put(cat+"_max", "500");
			names.add(cat+"_max"); values.add("500");
		//params.put(cat+"_correction", "FDR");
			names.add(cat+"_correction"); values.add("FDR");
		//params.put(cat+"_p_value", "0.05");
			names.add(cat+"_p_value"); values.add("0.05");
		
		}
		//params.put("query", "TOPPFUN");
		//params.put("type", "HGNC");
		
		String urlParameters = getQueryV(names,values);
		
		//System.out.println(urlParameters);
		
		// Send post request
		con.setDoOutput(true);
		DataOutputStream wr = new DataOutputStream(con.getOutputStream());
		wr.writeBytes(urlParameters);
		wr.flush();
		wr.close();

		int responseCode = con.getResponseCode();
		//System.out.println("\nSending 'POST' request to URL : " + url);
		//System.out.println("Post parameters : " + urlParameters);
		//System.out.println("Response Code : " + responseCode);

		BufferedReader in = new BufferedReader(
		        new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();

		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();
		
		String res = response.toString();
		return res;
		//print result
		//System.out.println();

	}
	
	
	
	private void sendPostExample() throws Exception {

		String url = "https://selfsolve.apple.com/wcResults.do";
		URL obj = new URL(url);
		HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();

		//add reuqest header
		con.setRequestMethod("POST");
		con.setRequestProperty("User-Agent", USER_AGENT);
		con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");

		String urlParameters = "sn=C02G8416DRJM&cn=&locale=&caller=&num=12345";
		
		// Send post request
		con.setDoOutput(true);
		DataOutputStream wr = new DataOutputStream(con.getOutputStream());
		wr.writeBytes(urlParameters);
		wr.flush();
		wr.close();

		int responseCode = con.getResponseCode();
		System.out.println("\nSending 'POST' request to URL : " + url);
		System.out.println("Post parameters : " + urlParameters);
		System.out.println("Response Code : " + responseCode);

		BufferedReader in = new BufferedReader(
		        new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();

		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();
		
		//print result
		System.out.println(response.toString());

	}
	
	private String getQuery(HashMap<String,String> params) throws UnsupportedEncodingException
	{
	    StringBuilder result = new StringBuilder();
	    boolean first = true;
	    
	    Set<String> keys = params.keySet();

	    for (String key : keys)
	    {
	        if (first)
	            first = false;
	        else
	            result.append("&");

	        result.append(URLEncoder.encode(key, "UTF-8"));
	        result.append("=");
	        result.append(URLEncoder.encode(params.get(key), "UTF-8"));
	    }

	    return result.toString();
	}
	
	private String getQueryV(Vector<String> names,Vector<String> values) throws UnsupportedEncodingException
	{
	    StringBuilder result = new StringBuilder();
	    boolean first = true;
	    
	    for (int i=0;i<names.size();i++)
	    {
	        if (first)
	            first = false;
	        else
	            result.append("&");

	        result.append(URLEncoder.encode(names.get(i), "UTF-8"));
	        result.append("=");
	        result.append(URLEncoder.encode(values.get(i), "UTF-8"));
	    }

	    return result.toString();
	}
	
	
	public static String extractUserID(String s){
		String id = "none";
		StringTokenizer st = new StringTokenizer(s,"\" ");
		while(st.hasMoreTokens()){
			String tok = st.nextToken();
			if(tok.equals("userdata_id")){
				st.nextToken();
				id = st.nextToken();
			}
		}
		return id;
	}
	
	
	/** This is a simple example of using HttpUnit to read and understand web pages. **/
	  public static void example( String[] params ) {
	    try {
	      // create the conversation object which will maintain state for us
	      WebConversation wc = new WebConversation();

	      // Obtain the main page on the meterware web site
	      String url="http://www.ihes.fr/~zinovyev";
	      
	      WebRequest request = new GetMethodWebRequest( url );
	      WebResponse response = wc.getResponse( request );
	      
	      // find the link which contains the string "HttpUnit" and click it
	      WebLink httpunitLink = response.getFirstMatchingLink( WebLink.MATCH_CONTAINED_TEXT, "HttpUnit" );
	      response = httpunitLink.click();

	      // print out the number of links on the HttpUnit main page
	      System.out.println( "The HttpUnit main page '"+url+"' contains " + response.getLinks().length + " links" );

	    } catch (Exception e) {
	       System.err.println( "Exception: " + e );
	    }
	  }


	

}
