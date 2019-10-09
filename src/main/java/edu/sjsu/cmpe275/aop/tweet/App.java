package edu.sjsu.cmpe275.aop.tweet;

import java.io.IOException;
import java.security.AccessControlException;

import org.springframework.context.support.ClassPathXmlApplicationContext;

public class App {
	
	public TweetService tweeter;
	public TweetStatsService stats;
	
    public static void main(String[] args) {
        /***
         * Following is a dummy implementation of App to demonstrate bean creation with Application context.
         * You may make changes to suit your need, but this file is NOT part of the submission.
         */
    	 ClassPathXmlApplicationContext ctx;
    	 ctx = new ClassPathXmlApplicationContext("context.xml");
    	 App app = new App();
    	 app.setStats((TweetStatsService) ctx.getBean("tweetStatsService"));
    	 app.setTweeter((TweetService) ctx.getBean("tweetService"));
    	 
         /*tweeter = (TweetService) ctx.getBean("tweetService");
         stats = (TweetStatsService) ctx.getBean("tweetStatsService");*/
    	 
        try {
        	//app.testOne();
        	//app.testTwo();
        	//app.retweetTest();
        	app.followTest();
        	

        } catch (Exception e) {
            e.printStackTrace();
        }

        
        ctx.close();
    }
    public TweetStatsService getStats() {
		return stats;
	}
    public TweetService getTweeter() {
		return tweeter;
	}
    public void setStats(TweetStatsService stats) {
		this.stats = stats;
	}
    public void setTweeter(TweetService tweeter) {
		this.tweeter = tweeter;
	}
    
    public void printStatusAsLog() {
    	System.out.println("Most productive user: " + stats.getMostProductiveUser());
        System.out.println("Most popular user: " + stats.getMostFollowedUser());
        System.out.println("Length of the longest tweet: " + stats.getLengthOfLongestTweet());
        System.out.println("Most popular message: " + stats.getMostPopularMessage());
    }
    
    public void testTwo() throws IllegalArgumentException, IOException {
    	int msg = tweeter.tweet(null, null);
    	printStatusAsLog();
    	
    }
    public void retweetTest() throws IllegalArgumentException, IOException, AccessControlException {
        int msgId = tweeter.tweet("alex", "first tweet");
        tweeter.follow("bob", "alex"); 
        tweeter.block("alex", "bob");
        tweeter.retweet("bob", msgId);
    	
    }
    
    public void followTest() throws IllegalArgumentException, IOException {
    	tweeter.follow("bob", "alex");
    }
    public void testOne() throws IllegalArgumentException, IOException {
        int msg = tweeter.tweet("alex", "first tweet");
        int msg1 = tweeter.tweet("alex", "second message");
        int msg2 = tweeter.tweet("bob", "bob's first message");
        int msg3 = tweeter.tweet("alex", "third message");
    	int msg4 = tweeter.tweet("bob", "third message");
        int msg5 = tweeter.tweet("bob", "third message");
        int msg6 = tweeter.tweet("cathy", "third message");
        tweeter.follow("cathy", "bob");
        tweeter.follow("alex", "bob");
        tweeter.follow("bob", "alex");            
        tweeter.follow("cathy", "alex");
       
        
        //tweeter.follow("dorothy", "bob");
        tweeter.retweet("bob", msg);
        //tweeter.block("alex", "bob");
        tweeter.retweet("bob", msg1);
        
        printStatusAsLog();
    }
}
