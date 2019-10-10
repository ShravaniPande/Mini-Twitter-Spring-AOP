package edu.sjsu.cmpe275.aop.tweet.aspect;

import java.security.AccessControlException;
import java.util.ArrayList;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;

import edu.sjsu.cmpe275.aop.tweet.TweetStatsServiceImpl;

@Aspect
@Order(0)
public class PermissionAspect {

	@Autowired
	TweetStatsServiceImpl stats;

	@Before("execution(public int edu.sjsu.cmpe275.aop.tweet.TweetService.tweet(..))")
	public void beforeTweetAdvice(JoinPoint joinPoint) {
		System.out.printf("###PermissionAspect@Before Permission check before the executuion of the method %s\n",
				joinPoint.getSignature().getName());
		Object[] args = joinPoint.getArgs();
		String user = (String) args[0];
		String message = (String) args[1];
		if (message == null || message.length() > 140 || user == null || user.isEmpty()) {
			throw new IllegalArgumentException();
		}

	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Before("execution(public int edu.sjsu.cmpe275.aop.tweet.TweetService.retweet(..))")
	public void beforeRetweet(JoinPoint joinPoint) {
		Object[] args = joinPoint.getArgs();
		String user = (String) args[0];
		int messageId = (Integer) args[1];
		boolean isBlocked = false;
		boolean isFollowing = false;

		

		// check if message ID exists
		boolean messageExists = stats.messageExists(messageId);
		if (!messageExists) {
			throw new AccessControlException("The message being retweeted does not exist.");
		}
		
		String owner = stats.getMessageOwner(messageId);
		System.out.println("owner of the msg: " + owner);

		ArrayList<String> followingUsers = (ArrayList) stats.getFollowers().get(owner);
		System.out.println("Followers: " + followingUsers);
		if (followingUsers != null) {
			isFollowing = followingUsers.contains(user);
			System.out.println("Is Following: " + isFollowing);
			if (!isFollowing) {
				throw new AccessControlException(user + " does not follow the author: " + owner);
			}
		} else {
			throw new AccessControlException(owner + " does not have a follower. So his messages cannot be retweeted.");
		}
		
		// check if the user is blocked by the msg owner
		ArrayList<String> blockedUsers = (ArrayList) stats.getBlockList().get(owner);
		// System.out.println("blocked users: "+blockedUsers);
		if (blockedUsers != null) {
			isBlocked = blockedUsers.contains(user);
			System.out.println("Blocked: " + isBlocked);
			if (isBlocked) {
				throw new AccessControlException(user + " has been blocked by the author: " + owner);
			}
		}

		System.out.printf("Permission check before the executuion of the metohd %s\n",
				joinPoint.getSignature().getName());
	}

	@Before("execution(public * edu.sjsu.cmpe275.aop.tweet.TweetService.follow(..))")
	public void beforeFollow(JoinPoint joinPoint) {
		System.out.printf("####PermissionAspect@Before the execution of the method %s\n",
				joinPoint.getSignature().getName());
		Object[] args = joinPoint.getArgs();
		String follower = (String) args[0];
		String followee = (String) args[1];
		if (follower == null || follower.isEmpty() || followee == null || followee.isEmpty()
				|| follower.equals(followee)) {
			throw new IllegalArgumentException();
		}

	}

	@Before("execution(public * edu.sjsu.cmpe275.aop.tweet.TweetService.block(..))")
	public void beforeBlock(JoinPoint joinPoint) {
		System.out.printf("####PermissionAspect@Before the execution of the method %s\n",
				joinPoint.getSignature().getName());
		Object[] args = joinPoint.getArgs();
		String user = (String) args[0];
		String followee = (String) args[1];
		if (user == null || user.isEmpty() || followee == null || followee.isEmpty() || user.equals(followee)) {
			throw new IllegalArgumentException();
		}

	}

}
