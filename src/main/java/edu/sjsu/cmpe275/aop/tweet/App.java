package edu.sjsu.cmpe275.aop.tweet;

import java.io.IOException;
import java.util.SortedMap;
import java.util.TreeMap;

import org.springframework.context.support.ClassPathXmlApplicationContext;

public class App {

	public TweetService tweeter;
	public TweetStatsService stats;

	public static void main(String[] args) {
		/***
		 * Following is a dummy implementation of App to demonstrate bean creation with
		 * Application context. You may make changes to suit your need, but this file is
		 * NOT part of the submission.
		 */
		ClassPathXmlApplicationContext ctx;
		ctx = new ClassPathXmlApplicationContext("context.xml");
		App app = new App();
		app.setStats((TweetStatsService) ctx.getBean("tweetStatsService"));
		app.setTweeter((TweetService) ctx.getBean("tweetService"));

		// app.test_1_block_nullvalues_throwsIllegalArgumentException();
		// app.test_2_test_tweetMethod();
		// app.test_3_tweet();
		// app.test_4_retweetTest();
		// app.test_5_followTest();
		// app.test_6_block_IOException_retires();
		// app.test_7_sortedMapTest();
		app.testOne();

		ctx.close();
	}

	public void test_7_sortedMapTest() {
		SortedMap<String, Integer> sortedMap = new TreeMap<String, Integer>();
		sortedMap.put("zz", 1);
		sortedMap.put("dd", 4);
		sortedMap.put("aa", 4);
		System.out.println(sortedMap);

	}

	public void test_6_block_IOException_retires() {
		try {
			tweeter.block("bob", "alex");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void test_1_block_nullvalues_throwsIllegalArgumentException() {
		try {
			tweeter.block(null, null);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void test_2_test_tweetMethod() {
		try {
			tweeter.tweet("alex", "first tweet");

		} catch (IllegalArgumentException e) {

			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void test_3_tweet() {
		try {
			tweeter.tweet(null, null);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		printStatusAsLog();
	}

	public void test_4_retweetTest() {
		int msgId;
		try {
			msgId = tweeter.tweet("alex", "first tweet");
			tweeter.follow("bob", "alex");
			tweeter.block("alex", "bob");
			tweeter.retweet("bob", msgId);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public void test_5_followTest() {
		try {
			tweeter.follow("bob", "alex");
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void testOne() {
		int msg;
		try {

			int msg1 = tweeter.tweet("alex", "second message");
			tweeter.follow("alex", "bob");
			tweeter.tweet("bob", "bob's first message");
			tweeter.tweet("alex", "third message");
			tweeter.tweet("bob", "bobs second message");
			tweeter.tweet("bob", "fourth message");
			tweeter.tweet("cathy", "third message");
			
			tweeter.follow("cathy", "bob");
			//tweeter.follow("alex", "bob");
			tweeter.follow("bob", "alex");
			tweeter.follow("cathy", "alex");

			// tweeter.follow("dorothy", "bob");
			msg = tweeter.tweet("alex", "first tweet");
			tweeter.retweet("bob", msg);
			// tweeter.block("alex", "bob");
			tweeter.retweet("bob", msg1);
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		printStatusAsLog();
	}

	public void printStatusAsLog() {
		System.out.println("Most productive user: " + stats.getMostProductiveUser());
		System.out.println("Most popular user: " + stats.getMostFollowedUser());
		System.out.println("Length of the longest tweet: " + stats.getLengthOfLongestTweet());
		System.out.println("Most popular message: " + stats.getMostPopularMessage());
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
}
