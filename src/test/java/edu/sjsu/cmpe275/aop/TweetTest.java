package edu.sjsu.cmpe275.aop;

import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import edu.sjsu.cmpe275.aop.tweet.TweetService;
import edu.sjsu.cmpe275.aop.tweet.TweetServiceImpl;

public class TweetTest {

    /***
     * These are dummy test cases. You may add test cases based on your own need.
     */

	private TweetService tweeetService = new TweetServiceImpl();

	@Before
	public void setUp() {

	}

	@Test(expected = IllegalArgumentException.class)
	public void newTweet_nullArguments_throwsIllegalArgumentException() throws IllegalArgumentException, IOException {
		tweeetService.tweet(null, null);

	}

	@Test(expected = IllegalArgumentException.class)
	public void newTweet_nullUserName_throwsIllegalArgumentException() throws IllegalArgumentException, IOException {
		tweeetService.tweet(null, "message1");
	}

	@Test(expected = IllegalArgumentException.class)
	public void newTweet_nullMessage_throwsIllegalArgumentException() throws IllegalArgumentException, IOException {
		tweeetService.tweet("bob", null);
	}

	@Test(expected = IllegalArgumentException.class)
	public void newTweet_longMessage_throwsIllegalArgumentException() throws IllegalArgumentException, IOException {
		String messageWith140Char = "K1cauo0734lBoKJdWZr7jnYVBNKFiyfdXkXfB9Jz8UqOt2MNqprZEaMCfyGA8XTjUfLtfic0t7R9QdtijweSC7mewOKGoN794dlAnKBzwpMirrbsqLMcTtfej8NYNGMwezh7UYVau8R\n";
		System.err.println(messageWith140Char.length());
		
		tweeetService.tweet("bob", messageWith140Char+"extra chars");
	}

	@Test
	public void newTweet_zeroLengthMessage_returnsMessageId() throws IllegalArgumentException, IOException {
		int messageId = tweeetService.tweet("alice", "");
		assertTrue(messageId == 1);
	}

	@Test
	public void newTweet_returnsMessageId() throws IllegalArgumentException, IOException {
		int messageId = tweeetService.tweet("alice", "message1");
		assertTrue(messageId == 1);
	}
	
	@Test
	public void newTweet_multiTweet_generatesUniqueMessageId() throws IllegalArgumentException, IOException {
		int messageId = tweeetService.tweet("alice", "message1");
		assertTrue(messageId == 1);
		messageId = tweeetService.tweet("alice", "message2");
		assertTrue(messageId == 2);
		messageId = tweeetService.tweet("alice", "message3");
		assertTrue(messageId == 3);
		messageId = tweeetService.tweet("alice", "message4");
		assertTrue(messageId == 4);
	}

}