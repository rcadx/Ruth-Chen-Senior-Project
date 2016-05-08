package query;
import twitter4j.*;
import twitter4j.auth.AccessToken;
import twitter4j.conf.ConfigurationBuilder;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.Date;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.TreeSet;

import cse131.ArgsProcessor;

/**
 * Creates a text file with tweets queried from Twitter based on keywords. 
 * 
 * Queries for however long the user wants, as specified in the input box that appears.
 * 
 * The output text file, saved in the output folder, consists of one line per tweet in the following format: [User ID]	[Tweet ID]	[Text of tweet]
 * The name of the output text file is based on date and time of querying.
 * 
 * Example input: 10 seconds
 * Example query: "long, complicated"
 * Example name of output text file: 'Thu_May_05_16:30:46_CDT_2016.txt'
 * Example line in the output text file: "5392522	728294564839067648	A long, complicated battle over 9,000-year-old bones is finally over. [photo] [photo]"
 * 
 * (After running this class, run UserTimeline.java)
 * 
 * @authors Ruth Chen (ruthchen@wustl.edu)
 * 			Ron Cytron (cytron@wustl.edu)
 * 			Yusuke Yamamoto (yusuke@mac.com)
 * 
 * @resources Twitter4J (http://twitter4j.org/en/index.html)
 * 
 * @since Twitter4J 2.1.7
 */

public class SearchTweets {

	public static void main(String[] args) throws IOException {

		ArgsProcessor ap = new ArgsProcessor(args);
		int numSecs = ap.nextInt("How many seconds?"); //how long do you want to run this program for?

		Calendar localCalendar = Calendar.getInstance(); //imported Calendar class
		Date currentTime = localCalendar.getTime(); //gets the time from the Calendar class
		String currentTimeString = currentTime.toString(); //converts the time to a String that we can read 
		String dateTime = currentTimeString.replaceAll("\\s","_"); //replacing all spaces from the date to be used as filename

		String keyword = "";

		FileInputStream keywordsStream = new FileInputStream("customization/keywords.txt"); //opens the keywords text file
		BufferedReader keywordsBR = new BufferedReader(new InputStreamReader(keywordsStream));
		String currentLine = "";
		while((currentLine = keywordsBR.readLine()) != null){
			keyword += currentLine + " OR "; //concatenates each line of the keywords text file for one long query
		}

		keyword = keyword.substring(0, keyword.length() - 3); //trims the " OR " from the last line

		keywordsBR.close();

		String filename = "tweets/"+dateTime+".txt"; //naming a new file by keyword and date

		FileOutputStream fos = new FileOutputStream(new File(filename)); //creates a new file
		PrintStream ps = new PrintStream(fos); 

		Twitter twitter = Auth.auth(); 

		try {

			Query query = new Query(keyword + "-filter:retweets -filter:links"); //runs a query using our keywords and EXCLUDES retweets and links
			QueryResult result; 
			Set<Long> allUsers = new HashSet<Long>(); //long bc of twitter user ID, hashset bc no duplicate users
			Map<Long, Set<String>> messages = new HashMap<Long, Set<String>>(); //each user has a set of tweets
			List<RankingFilter> filters = new LinkedList<RankingFilter>(); //creates a list of words we want to filter for
			TreeSet<RankedString> queue = new TreeSet<RankedString>(); //stores all the tweets in order of ranking

			//adds to positive ranking filter
			FileInputStream posStream = new FileInputStream("customization/positive_words_for_filter.txt"); //opens the file with positive words
			BufferedReader posBR = new BufferedReader(new InputStreamReader(posStream));
			String posLine = "";
			int posLineNum = 0;
			while((posLine = posBR.readLine()) != null){
				if(posLineNum > 2) { //skipping instructions
					StringTokenizer st = new StringTokenizer(posLine, "\t"); //each line's format is word	ranking
					String word = st.nextToken();
					int posRating = Integer.parseInt(st.nextToken());
					filters.add(new HasWord(word, posRating));
				}
				posLineNum++;
			}
			posBR.close();
			
			//adds to negative ranking filter
			FileInputStream negStream = new FileInputStream("customization/positive_words_for_filter.txt"); //opens the file with negative words
			BufferedReader negBR = new BufferedReader(new InputStreamReader(negStream));
			String negLine = "";
			int negLineNum = 0;
			while((negLine = negBR.readLine()) != null){
				if(negLineNum > 2) { //skipping instructions
					StringTokenizer st = new StringTokenizer(negLine, "\t");	//each line's format is word	ranking
					String word = st.nextToken();
					int negRating = Integer.parseInt(st.nextToken());
					filters.add(new AvoidsWord(word, negRating));
				}
				negLineNum++;
			}
			negBR.close();

			filters.add(new AvoidsWord("donation", 100)); //adds a NEGATIVE ranking filter to our list
			filters.add(new AvoidsWord("donate", 100));
			filters.add(new AvoidsWord("fundraiser", 100));
			filters.add(new AvoidsWord("https", 100));
			filters.add(new AvoidsWord("horoscope", 100));
			filters.add(new AvoidsWord("aries", 100));
			filters.add(new AvoidsWord("taurus", 100));
			filters.add(new AvoidsWord("gemini", 100));
			filters.add(new AvoidsWord("leo", 100));
			filters.add(new AvoidsWord("virgo", 100));
			filters.add(new AvoidsWord("libra", 100));
			filters.add(new AvoidsWord("scorpio", 100));
			filters.add(new AvoidsWord("sagittarius", 100));
			filters.add(new AvoidsWord("capricorn", 100));
			filters.add(new AvoidsWord("aquarius", 100));
			filters.add(new AvoidsWord("pisces", 100));
			filters.add(new AvoidsWord("cat", 30));
			filters.add(new AvoidsWord("dog", 30));
			filters.add(new AvoidsWord("mom", 30));
			filters.add(new AvoidsWord("dad", 30));
			filters.add(new AvoidsWord("aunt", 30));
			filters.add(new AvoidsWord("nephew", 30));
			filters.add(new AvoidsWord("sign", 5));

			do { //this is where we're actually filtering now
				result = twitter.search(query); //running query
				List<Status> tweets = result.getTweets(); //list of tweets from the query

				for (Status tweet : tweets) { //loop through our list of tweets
					long userID = tweet.getUser().getId();
					long tweetID = tweet.getId();
					CleanedTweet message = new CleanedTweet(tweet);

					allUsers.add(userID); //add a new user to our list of users
					if (!messages.containsKey(userID)) { //this checks to make sure that the user isn't already in our list
						messages.put(userID, new HashSet<String>()); //makes a new pairing if the user doesn't exist
					}
					messages.get(userID).add(message.toString()); //associates the user with his or her set of tweets

					System.out.println(message); //this is just for viewing entertainment! all tweets will be saved in a file

					RankedString rs = new RankedString(userID + "	" + tweetID + "	" + message); //creates a version of our string with userID, tweetID, and a rank

					for (RankingFilter f : filters) {  
						rs = f.filter(rs); //runs our tweet through each filter
					}
					queue.add(rs); //adds this tweet to our ordered list of ranked tweets
				}
				sleep(6000); //bypass the twitter query rate limit
				numSecs = numSecs-6;
			} while ((query = result.nextQuery()) != null && numSecs > 0); //stop running if there are no other queries or we run out of time

			for (RankedString s : queue){ 
				ps.println(s);
			}

			System.out.println("FINISHED RUNNING! Refresh the 'tweets' folder to view tweets.");

		} catch (TwitterException te) {
			te.printStackTrace();
			System.out.println("Failed to search tweets: " + te.getMessage());
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