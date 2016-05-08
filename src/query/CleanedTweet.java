package query;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import twitter4j.MediaEntity;
import twitter4j.Status;
import twitter4j.SymbolEntity;
import twitter4j.URLEntity;
import twitter4j.UserMentionEntity;

/**
 * Cleans a tweet from a Twitter query.
 * 
 * By "cleaning" a tweet (Status), we replace...
 * 	1. New lines with spaces
 * 	2. Links with '[links]'
 * 	3. Photos with '[photo]'
 * 	4. Screennames with 'sn'
 * 	5. Symbols with '[symbol]'
 * 
 * Example input: "Hello @twitteruser check out this website: https://www.wustl.edu"
 * Example output: "Hello @sn check out this website: [link]"
 * 
 * @author Ruth Chen (ruthchen@wustl.edu)
 */

public class CleanedTweet {

	private String string;

	public String toString(){
		return string;
	}
	
	//cleans tweet of new lines, links, photos, screennames, and symbols
	public CleanedTweet(Status s) {

		StringBuffer cleaned = new StringBuffer(s.getText()); //creates a stringbuffer of our tweet so we can edit it as we go

		//GETS RID OF NEW LINES
		Pattern newline = Pattern.compile("[\n]"); //turn newline into a pattern for matching
		Matcher match = newline.matcher(cleaned); //match the tweet with newline
		while(match.find()){
			cleaned.replace(match.start(), match.end(), " "); //replace newlines with spaces
		}

		//REPLACES LINKS
		URLEntity[] links = s.getURLEntities(); //stores all links in the tweet in an array
		if(links.length != 0){ //check to see that there's even a link before analyzing it
			for(int i = 0; i < links.length; i++){ //replace all links
				String link = links[i].getURL(); //get the link
				Pattern p = Pattern.compile(link); //turn link into a pattern for matching
				Matcher m = p.matcher(cleaned); //match the tweet with the link
				while(m.find()) { 
					cleaned.replace(m.start(), m.end(), "[link]"); //replace the link with '[link]'
					break; //need to break so we can move on to next link
				}
			}
		}

		//REPLACES PHOTOS
		MediaEntity[] photos = s.getMediaEntities();
		if(photos.length != 0) {
			for(int i = 0; i < photos.length; i++){ //replace all photos
				String photo = photos[i].getURL(); //get the photo
				Pattern p = Pattern.compile(photo); //turn link into a pattern for matching
				Matcher m = p.matcher(cleaned); //match the tweet with the photo
				while(m.find()) { 
					cleaned.replace(m.start(), m.end(), "[photo]"); //replace the photo with '[photo]'
					break; //need to break so we can move on to next photo
				}
			}
		}

		//REPLACES SCREENNAMES
		UserMentionEntity[] screennames = s.getUserMentionEntities();
		if(screennames.length != 0) {
			for(int i = 0; i < screennames.length; i++){ //replace all screennames
				String screenname = screennames[i].getScreenName(); //get the screenname
				Pattern p = Pattern.compile(screenname); //turn link into a pattern for matching
				Matcher m = p.matcher(cleaned); //match the tweet with the screenname
				while(m.find()) { 
					cleaned.replace(m.start(), m.end(), "sn"); //replace the screenname with '@sn'
					break; //need to break so we can move on to next screenname
				}
			}
		}

		//REPLACES SYMBOLS
		SymbolEntity[] symbols= s.getSymbolEntities();
		if(symbols.length != 0) {
			for(int i = 0; i < symbols.length; i++){ //replace all symbols
				String symbol = symbols[i].getText(); //get the symbol
				Pattern p = Pattern.compile(symbol); //turn link into a pattern for matching
				Matcher m = p.matcher(cleaned); //match the tweet with the symbol
				while(m.find()) { 
					cleaned.replace(m.start(), m.end(), "[symbol]"); //replace the symbol with '@sn'
					break; //need to break so we can move on to next symbol
				}
			}
		}
		
		string = cleaned.toString();
	}
}

