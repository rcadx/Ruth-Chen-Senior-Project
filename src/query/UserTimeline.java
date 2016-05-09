package query;

import twitter4j.MediaEntity;
import twitter4j.Paging;
import twitter4j.Status;
import twitter4j.SymbolEntity;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.URLEntity;
import twitter4j.UserMentionEntity;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cse131.ArgsProcessor;

/**
 * (Before running this class, run SearchTweets.java)
 * 
 * Creates a text file for every user whose tweet is in the output text file of SearchTweets.java. 
 * This text file contains each user's timeline before and after the tweet in the output text file of SearchTweets.java.
 * 
 * Queries for however long the user wants, as specified in the input box that appears.
 * 
 * The user chooses the text file from the output folder.
 * 
 * Example time input: 10 seconds
 * Example text file input: 'Thu_May_05_16:30:46_CDT_2016.txt'
 * If this input text file has 5 tweets (1 on each line), then this program would generate 5 new text files. 
 * Each text file will contain the Twitter timeline of a user from the input text file. 
 * 
 * The name of each user's text file is based on their Twitter user ID and time/date of querying.
 * Example name of output text file: '5392522_Thu_May_05_16:44:00_CDT_2016.txt'
 * 	
 * The number of tweets from each user's timeline can be changed by editing the number of pages, on line 87 in the 'for' loop.
 * For example: To get 30 pages from a user's timeline...
 * 		Change the line		for(int pageNum = 1; pageNum < 21; pageNum++)
 * 				TO			for(int pageNum = 1; pageNum < 31; pageNum++)
 *  
 * @authors Ruth Chen (ruthchen@wustl.edu)
 * 			Ron Cytron (cytron@wustl.edu)
 * 			Yusuke Yamamoto (yusuke@mac.com)
 * 
 * @resources Twitter4J (http://twitter4j.org/en/index.html)
 * 
 * @since Twitter4J 2.1.7
 */

public class UserTimeline {
	public static void main(String[] args) throws IOException {

		try {
			FileOpener f = new FileOpener(); //choose a file from the output folder (files from SearchTweets.java)
			Iterator<String> i = f.iter();
			do {
				String[] words = i.next().split("\t"); //converts words to an array for grabbing user and tweet IDs

				long userID = Long.parseLong(words[0]); //the first word in the file is always the userID
				long tweetID = Long.parseLong(words[1]); //the second word in the file is always the tweetID

				System.out.println("Now grabbing tweets from user " + userID);

				Calendar localCalendar = Calendar.getInstance(); //imported Calendar class
				Date currentTime = localCalendar.getTime(); //gets the time from the Calendar class
				String currentTimeString = currentTime.toString(); //converts the time to a String that we can read 
				String dateTime = currentTimeString.replaceAll("\\s","_"); //replacing all spaces from the date to be used as filename

				String outputFilename = "timelines/"+userID+"_"+dateTime+".txt"; //naming a new file by keyword and date

				FileOutputStream fos = new FileOutputStream(new File(outputFilename)); //creates a new file
				PrintStream ps = new PrintStream(fos); 

				Twitter twitter = Auth.auth(); //authentication

				int tweetNumber = 1; //counter 

				for(int pageNum = 1; pageNum < 31; pageNum++){
					//gets tweets AFTER specified tweet and prints them to the file
					Paging afterPage = new Paging(pageNum, 500).sinceId(tweetID); //gets tweets after the specified tweet
					List<Status> afterTweets = twitter.getUserTimeline(userID, afterPage); //list of tweets

					for (Status tweet : afterTweets) {

						if(!tweet.isRetweet()){
							CleanedTweet message = new CleanedTweet(tweet);
							ps.println(tweetNumber + " " + message ); //writes to text file
						}
						tweetNumber++; 
					}

					//gets tweets BEFORE specified tweet and prints them to the file
					Paging beforePage = new Paging(pageNum, 500).maxId(tweetID); //gets tweets before the specified tweet
					List<Status> beforeTweets = twitter.getUserTimeline(userID, beforePage); //list of tweets

					for (Status tweet : beforeTweets) {
						if(!tweet.isRetweet()){
							CleanedTweet message = new CleanedTweet(tweet);
							ps.println(tweetNumber + " " + message); //writes to text file
						}
						tweetNumber++;
					}
					
					sleep(6000);
				}

				ps.close(); //closes the file
			}
			while (i.hasNext()); //stop running if there are no other queries or we run out of time
			System.out.println("FINISHED RUNNING! Refresh the 'timelines' folder to view tweets.");
		} catch (TwitterException te) {
			te.printStackTrace();
			System.out.println("Failed to get timeline: " + te.getMessage());
			System.exit(-1);
		}
	}

	private static void sleep(int msecs) {
		try {
			Thread.sleep(msecs);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}