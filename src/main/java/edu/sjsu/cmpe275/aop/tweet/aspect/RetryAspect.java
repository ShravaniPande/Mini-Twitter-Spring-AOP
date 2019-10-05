package edu.sjsu.cmpe275.aop.tweet.aspect;

import java.io.IOException;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.core.annotation.Order;

@Aspect
@Order(1)
public class RetryAspect {
	/***
	 * Following is a dummy implementation of this aspect. You are expected to
	 * provide an actual implementation based on the requirements, including
	 * adding/removing advices as needed.
	 * 
	 * @throws Throwable
	 */

	/*-
	@Autowired
	private TweetService tweetService;
*/
	// @Around("execution(public int
	// edu.sjsu.cmpe275.aop.tweet.TweetService.*tweet(..))")
	// @AfterThrowing(pointcut = "execution(public int
	// edu.sjsu.cmpe275.aop.tweet.TweetService.tweet(..))", throwing =
	// "ioException")
	@Around("execution(public * edu.sjsu.cmpe275.aop.tweet.TweetService.*(..))")
	public void aroundTtweetAdvice(ProceedingJoinPoint joinPoint) throws Throwable {

		try {
			// before business logic
			joinPoint.proceed(); // Default execution of business logic
			// after business logic
		} catch (Throwable e) {
			e.printStackTrace();
			// tryFirstTime(joinPoint);
			tryNTimes(joinPoint, 3, 1);
		}
	}

	private void tryNTimes(ProceedingJoinPoint joinPoint, int maxTriesCount, int currentTryCount) throws Throwable {
		if (currentTryCount <= maxTriesCount) {
			try {
				joinPoint.proceed();
			} catch (IOException e) {
				if (currentTryCount == maxTriesCount) {
					throw e;
				}
				tryNTimes(joinPoint, maxTriesCount, currentTryCount + 1);
			} catch (Throwable e) {
				throw e;
			}
		}
	}

	/*-
		private void tryFirstTime(ProceedingJoinPoint joinPoint) throws Throwable {
			try {
				joinPoint.proceed(); // trying for 1st time
			} catch (Throwable e) {
				try {
					joinPoint.proceed(); // trying for 2nd time
				} catch (Throwable e1) {
					joinPoint.proceed(); // trying for 3rd time and if still error re-throw same error
	
				}
			}
		}
	*/
	// @Around("execution(public int
	// edu.sjsu.cmpe275.aop.tweet.TweetService.*tweet(..))")
	/*-
	public int dummyAdviceOne(ProceedingJoinPoint joinPoint) throws Throwable {
		System.out.printf("Prior to the executuion of the metohd %s\n", joinPoint.getSignature().getName());
		Integer result = null;
		try {
			result = (Integer) joinPoint.proceed();
			System.out.printf("Finished the executuion of the metohd %s with result %s\n",
					joinPoint.getSignature().getName(), result);
		} catch (Throwable e) {
			e.printStackTrace();
			System.out.printf("Aborted the executuion of the metohd %s\n", joinPoint.getSignature().getName());
			throw e;
		}
		return result.intValue();
	}
	/*-
	System.out.println("io Exception occured in "+ joinPoint.getSignature().getName());
	System.err.println("exception details " + ioException.getMessage());
	
	System.out.println("*********retrying ************* Count: 1");
	Object[] args = joinPoint.getArgs();
	String user = (String) args[0];
	String tweetMessage = (String) args[1];
	try {
		tweetService.tweet(user, tweetMessage);
	} catch (IllegalArgumentException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
		throw e;
	}
	*/
}
