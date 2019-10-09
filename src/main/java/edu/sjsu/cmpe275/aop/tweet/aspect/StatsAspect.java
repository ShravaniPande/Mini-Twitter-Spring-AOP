package edu.sjsu.cmpe275.aop.tweet.aspect;

import java.io.IOException;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;

import edu.sjsu.cmpe275.aop.tweet.TweetServiceImpl;
import edu.sjsu.cmpe275.aop.tweet.TweetStatsServiceImpl;

@Aspect
@Order(2)
public class StatsAspect {
    /***
     * Following is a dummy implementation of this aspect.
     * You are expected to provide an actual implementation based on the requirements, including adding/removing advices as needed.
     */

	@Autowired TweetStatsServiceImpl stats;
	//@Autowired TweetServiceImpl service;
	
	//@After("execution(public * edu.sjsu.cmpe275.aop.tweet.TweetService.*(..))")
	@AfterReturning(pointcut = "execution(public * edu.sjsu.cmpe275.aop.tweet.TweetService.tweet(..))", returning ="messageId")
	public void dummyAfterAdvice(JoinPoint joinPoint, Object messageId) {
		System.out.printf("After the executuion of the metohd %s\n", joinPoint.getSignature().getName());
		Object[] args = joinPoint.getArgs();
		String user = (String) args[0];
		String tweetMessage = (String) args[1];
		stats.addNewTweetEntry(user, tweetMessage, (Integer) messageId);
		
	}
	

	
	@After("execution(public * edu.sjsu.cmpe275.aop.tweet.TweetServiceImpl.block(..))")
	public void newBlockList(JoinPoint joinPoint) {
		System.out.printf("After the executuion of the metohd %s\n", joinPoint.getSignature().getName());
		Object[] args = joinPoint.getArgs();
		String follower = (String) args[1];
		String blocker = (String) args[0];
		stats.addNewBlockEntry(follower, blocker);
		
		
	}

	@After("execution(public * edu.sjsu.cmpe275.aop.tweet.TweetService.follow(..))")
	public void newFollowersList(JoinPoint joinPoint) {
		System.out.printf("After the executuion of the metohd %s\n", joinPoint.getSignature().getName());
		Object[] args = joinPoint.getArgs();
		String follower = (String) args[0];
		String followee = (String) args[1];
		stats.addNewFollowEntry(follower, followee);
		
		
	}
	
	
}
