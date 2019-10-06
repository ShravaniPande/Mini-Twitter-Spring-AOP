package edu.sjsu.cmpe275.aop.tweet;

import org.springframework.context.support.ClassPathXmlApplicationContext;

public class App {
    public static void main(String[] args) {
        /***
         * Following is a dummy implementation of App to demonstrate bean creation with Application context.
         * You may make changes to suit your need, but this file is NOT part of the submission.
         */

    	ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("context.xml");
        TweetService tweeter = (TweetService) ctx.getBean("tweetService");
        TweetStatsService stats = (TweetStatsService) ctx.getBean("tweetStatsService");

        try {

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
            tweeter.block("alex", "bob");
            tweeter.retweet("bob", msg1);
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("Most productive user: " + stats.getMostProductiveUser());
        System.out.println("Most popular user: " + stats.getMostFollowedUser());
        System.out.println("Length of the longest tweet: " + stats.getLengthOfLongestTweet());
        System.out.println("Most popular message: " + stats.getMostPopularMessage());
        ctx.close();
    }
}
