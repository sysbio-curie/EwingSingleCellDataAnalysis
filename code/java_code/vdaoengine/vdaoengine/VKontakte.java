package vdaoengine;

import java.io.*;
import java.util.*;

public class VKontakte {

	private class Message{
		String text;
		String date;
		String author;
		public Message(){
			text = "";
		}
	}
	
	/**
	 * @param args
	 */
	
	public static String delimiters = " ,.;)(:!?\n><-";
	
	public static void main(String[] args) {
		try{
			String author1 = null;
			String author2 = null;
			LineNumberReader lr = new LineNumberReader(new FileReader("c:/docs/natasha/kontakt3.txt"));
			String s = null;
			Message m = null;
			VKontakte vk = new VKontakte();
			Vector<Message> messages = new Vector<Message>();
			
			author1 = lr.readLine(); author1 = author1.substring(1, author1.length()); System.out.println("Author1 = "+author1+" "+author1.length()); //for(int i=0;i<author1.length();i++) System.out.println(author1.charAt(i));
			author2 = lr.readLine(); System.out.println("Author2 = "+author2+" "+author2.length());
			
			while((s=lr.readLine())!=null){
				//System.out.println(s);
				s = s.trim();
				if(s.length()!=0){
				if(s.equals(author1)||s.equals(author2)){
					m = vk.new Message();
					m.author = s;
					//System.out.println(m.author);
				}else{
					s = s.toLowerCase();
					if((s.length()==8)&&(s.startsWith("0")||s.startsWith("1")||s.startsWith("2")||s.startsWith("3"))&&(!s.contains(":")&&(!s.contains(" ")))){
						m.date = s;
						//System.out.println(m.date);
						messages.add(m);
					}else{
						m.text+=s+"\n";
					}
				}}
			}
			
			CountWordFrequency(messages, author1);
			//CountNumberPerDate(messages, null);
			
		}catch(Exception e){
			e.printStackTrace();
		}

	}
	
	public static void CountWordFrequency(Vector<Message> messages, String author){
		HashMap<String,Integer> words = new HashMap<String,Integer>();
		System.out.println(messages.size()+" messages total");
		int mcount = 0;
		for(int i=0;i<messages.size();i++){
			Message m = messages.get(i);
			//System.out.println(m.text);
			if(m.author.equals(author)){
				mcount++;
				StringTokenizer st = new StringTokenizer(m.text,delimiters);
				while(st.hasMoreTokens()){
					String word = st.nextToken();
					if(words.containsKey(word)){
						int count = words.get(word);
						words.put(word, ++count);
					}else{
						words.put(word, 1);
					}
				}
			}
		}
		System.out.println(mcount+" messages from "+author);
		Iterator<String> ws = words.keySet().iterator();
		while(ws.hasNext()){
			String word = ws.next();
			System.out.println(word+"\t"+words.get(word));
		}
	}
	
	public static void CountNumberPerDate(Vector<Message> messages, String author){
		HashMap<String,Integer> dates = new HashMap<String,Integer>();
		for(int i=0;i<messages.size();i++){
			Message m = messages.get(i);
			//System.out.println(m.text);
			if(m.author.equals(author)||(author==null)){
				String date = m.date;
				StringTokenizer st = new StringTokenizer(date,".");
				String day = st.nextToken();
				String month = st.nextToken();
				String year = st.nextToken();
				date = day+"/"+month+"/"+year;
				if(dates.containsKey(date)){
					int count = dates.get(date);
					dates.put(date, ++count);
				}else{
					dates.put(date, 1);
				}
			}
		}
		Iterator<String> ws = dates.keySet().iterator();
		while(ws.hasNext()){
			String date = ws.next();
			if(date.length()==5) date = "0"+date;
			System.out.println(date+"\t"+dates.get(date));
		}
	}

}
