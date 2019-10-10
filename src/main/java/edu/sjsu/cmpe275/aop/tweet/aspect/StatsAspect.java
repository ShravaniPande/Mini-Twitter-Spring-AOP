package edu.sjsu.cmpe275.aop.tweet.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;

import edu.sjsu.cmpe275.aop.tweet.TweetStatsServiceImpl;

@Aspect
@Order(2)
public class StatsAspect {
	/***
	 * Following is a dummy implementation of this aspect. You are expected to
	 * provide an actual implementation based on the requirements, including
	 * adding/removing advices as needed.
	 */

	@Autowired
	TweetStatsServiceImpl stats;

	@AfterReturning(pointcut = "execution(public * edu.sjsu.cmpe275.aop.tweet.TweetService.tweet(..))", returning = "messageId")
	public void afterReturningTweetAdvice(JoinPoint joinPoint, Object messageId) {
		System.out.printf("$$$StatsAspect@AfterReturning the executuion of the METHOD %s\n",
				joinPoint.getSignature().getName());
		Object[] args = joinPoint.getArgs();
		String user = (String) args[0];
		String tweetMessage = (String) args[1];
		
		stats.addNewTweetEntry(user, tweetMessage, (Integer) messageId);
		stats.updateMessageShareDetails(user, tweetMessage);
	}

	@AfterReturning(pointcut = "execution(public * edu.sjsu.cmpe275.aop.tweet.TweetService.retweet(..))", returning = "reTweetMessageId")
	public void afterReturningReTweetAdvice(JoinPoint joinPoint, Object reTweetMessageId) {
		System.out.printf("$$$StatsAspect@AfterReturning the executuion of the METHOD %s\n",
				joinPoint.getSignature().getName());
		Object[] args = joinPoint.getArgs();
		String user = (String) args[0];
		int tweetMessgeId = (Integer) args[1];
		String tweetMessage = stats.getMessageById(tweetMessgeId);
		stats.getMessageOwner(tweetMessgeId);
		stats.updateMessageShareDetails(user, tweetMessage);

	}

	@AfterReturning("execution(public * edu.sjsu.cmpe275.aop.tweet.TweetServiceImpl.block(..))")
	public void newBlockList(JoinPoint joinPoint) {
		System.out.printf("$$$StatsAspect@AfterReturning the executuion of the method BLOCK");
		Object[] args = joinPoint.getArgs();
		String follower = (String) args[1];
		String blocker = (String) args[0];
		stats.addNewBlockEntry(follower, blocker);
	}

	@AfterReturning("execution(public * edu.sjsu.cmpe275.aop.tweet.TweetService.follow(..))")
	public void newFollowersList(JoinPoint joinPoint) {
		System.out.printf("$$$StatsAspect@AfterReturning the executuion of the method %s\n",
				joinPoint.getSignature().getName());
		Object[] args = joinPoint.getArgs();
		String follower = (String) args[0];
		String followee = (String) args[1];
		stats.addNewFollowEntry(follower, followee);
	}
}
