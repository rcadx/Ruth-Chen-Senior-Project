package query;

import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import twitter4j.conf.ConfigurationBuilder;

public class Auth {

	public static Twitter auth() {
		try {
			ConfigurationBuilder cb = new ConfigurationBuilder();
			cb.setOAuthConsumerKey("fbt4Tgx8FhmuoWSxXUlZOSVvd");
			cb.setOAuthConsumerSecret("xCRTCxu2DEmfBvweFK7VTAYgmY6MS0HsJLc7WEq5AA1yuFjv88");
			//cb.setUseSSL(true);
			cb.setStreamBaseURL("https://stream.twitter.com/1.1/");
			cb.setDebugEnabled(true);

			twitter4j.conf.Configuration config = cb.build();
			TwitterFactory factorystream = new TwitterFactory(config);
			AccessToken acToken = new AccessToken(
					"97778182-tkrksJYuRxu7P2FbGzgZkroCZgOq6bwp3nyU47Ysf", //authKey
					"1PBaiVFGpx9vkZX7Rux4r6Ml9CgYTL2vqBaBGCfmCEt5W" //authSecret
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
