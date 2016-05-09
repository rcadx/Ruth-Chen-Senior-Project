package query;

import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import twitter4j.conf.ConfigurationBuilder;

public class Auth {

	public static Twitter auth() {
		try {
			ConfigurationBuilder cb = new ConfigurationBuilder();
			cb.setOAuthConsumerKey("Il6uBzWLAKFpfzpifxoi9NGTw");
			cb.setOAuthConsumerSecret("ahio4tdUkOzaQmN21jn3DsEiKOlJuQMxqZWEfIya7WOE7nsw66");
			//cb.setUseSSL(true);
			cb.setStreamBaseURL("https://stream.twitter.com/1.1/");
			cb.setDebugEnabled(true);

			twitter4j.conf.Configuration config = cb.build();
			TwitterFactory factorystream = new TwitterFactory(config);
			AccessToken acToken = new AccessToken(
					"727906661977997312-s9QRUcEM24xBhtrRrELr3ACA0EYwRdJ", //authKey
					"wIqfxAFwmJQRH5FZpPR2d97adsFLUZoQW4HgdwD41phCn" //authSecret
					);
			Twitter twitterStream = factorystream.getInstance(acToken);
			TwitterFactory factory = new TwitterFactory(config);
			Twitter twitter = factory.getInstance(acToken);
			return twitter;
		} catch(Throwable t) {
			System.out.println("error " + t);
			return null;
		}
	}
}
