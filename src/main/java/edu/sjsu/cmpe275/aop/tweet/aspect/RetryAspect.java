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

	@Around("execution(public * edu.sjsu.cmpe275.aop.tweet.TweetService.*(..))")
	public Object aroundTtweetAdvice(ProceedingJoinPoint joinPoint) throws Throwable {
		System.out.println("***TryAspect@Around:  before " + joinPoint.getSignature().getName());
		Object returnValue = null;
		try {
			// before business logic
			returnValue = joinPoint.proceed(); // Default execution of business logic
			// after business logic
		} catch (IOException e) {
			e.printStackTrace();
			tryNTimes(joinPoint, 3, 1);
		}
		System.out.println("***TryAspect@Around:  after" + joinPoint.getSignature().getName());
		return returnValue;
	}

	private void tryNTimes(ProceedingJoinPoint joinPoint, int maxTriesCount, int currentTryCount) throws Throwable {
		System.out.println("Trying execution of :" + joinPoint.getSignature().getName() + " max count = "
				+ maxTriesCount + " currentTryCount = " + currentTryCount);
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
}
