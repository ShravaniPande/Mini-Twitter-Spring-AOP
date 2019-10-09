package edu.sjsu.cmpe275.aop.tweet.aspect;

import java.io.IOException;
import java.security.AccessControlException;
import java.util.ArrayList;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;

import edu.sjsu.cmpe275.aop.tweet.TweetStatsServiceImpl;

@Aspect
@Order(0)
public class PermissionAspect {
    /***
     * Following is a dummy implementation of this aspect.
     * You are expected to provide an actual implementation based on the requirements, including adding/removing advices as needed.
     */
	@Autowired TweetStatsServiceImpl stats;
	@Before("execution(public int edu.sjsu.cmpe275.aop.tweet.TweetService.tweet(..))")
	public void dummyBeforeAdvice(JoinPoint joinPoint) {
		Object[] args = joinPoint.getArgs();
		String user = (String) args[0];
		String message = (String) args[1];
		if (message == null || message.length() > 140 || user == null || user.isEmpty()) {
			throw new IllegalArgumentException();
		} 
		
		System.out.printf("Permission check before the executuion of the metohd %s\n", joinPoint.getSignature().getName());
	}
	@Before("execution(public int edu.sjsu.cmpe275.aop.tweet.TweetService.retweet(..))")
	public void BeforeRetweet(JoinPoint joinPoint) {
		Object[] args = joinPoint.getArgs();
		String user = (String) args[0];
		int messageId = (Integer) args[1];
		boolean isBlocked = false;
		boolean isFollowing = false;
		int newMessageId = 0;
		String owner = stats.getMessageOwner(messageId);
		System.out.println("owner of the msg: " + owner);

		//check if message ID exists
		boolean messageExists = stats.messageExists(messageId);
		if(messageExists == false) {
			throw new AccessControlException("The message being retweeted does not exist.");
		}
		
		ArrayList<String> followingUsers = (ArrayList) stats.getFollowers().get(owner);
		System.out.println("Followers: " + followingUsers);
		if (followingUsers != null) {
			isFollowing = followingUsers.contains(user);
			System.out.println("Is Following: " + isFollowing);
			if(!isFollowing) {
				throw new AccessControlException(user+" does not follow the author: "+owner);
			}
		}
		else {
			throw new AccessControlException(owner+" does not have a follower. So his messages cannot be retweeted.");
		}
		// check if the user is blocked by the msg owner
		ArrayList<String> blockedUsers = (ArrayList) stats.getBlockList().get(owner);
		// System.out.println("blocked users: "+blockedUsers);
		if (blockedUsers != null) {
			isBlocked = blockedUsers.contains(user);
			System.out.println("Blocked: " + isBlocked);
			if(isBlocked) {
				throw new AccessControlException(user+" has been blocked by the author: "+owner);
			}
		}
		

		System.out.printf("Permission check before the executuion of the metohd %s\n", joinPoint.getSignature().getName());
	}
	
	
	@Before("execution(public * edu.sjsu.cmpe275.aop.tweet.TweetServiceImpl.follow(..))")
	public void beforeFollow(JoinPoint joinPoint) {
		System.out.printf("Before the execution of the method %s\n", joinPoint.getSignature().getName());
		Object[] args = joinPoint.getArgs();
		String follower = (String) args[0];
		String followee = (String) args[1];
		if(follower == null || follower.isEmpty() || followee == null || followee.isEmpty() || follower.equals(followee) ) {
			throw new IllegalArgumentException();
		}
			
	}
	

	

	
	
	
}
