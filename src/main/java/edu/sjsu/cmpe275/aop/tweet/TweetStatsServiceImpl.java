package edu.sjsu.cmpe275.aop.tweet;

import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Set;

public class TweetStatsServiceImpl implements TweetStatsService {
	/***
	 * Following is a dummy implementation. You are expected to provide an actual
	 * implementation based on the requirements.
	 */
	private HashMap<Integer, String> messageIdToMessageMap = new HashMap<Integer, String>(); // Map<messageId, message>
	private HashMap<String, HashMap<Integer, String>> userMessages = new HashMap<String, HashMap<Integer, String>>(); // Storing
																														// Users
																														// and
																														// their
																														// tweets

	@Override
	public void resetStatsAndSystem() {
		messageIdToMessageMap = new HashMap<Integer, String>();
		userMessages = new HashMap<String, HashMap<Integer, String>>();

	}

	@Override
	public int getLengthOfLongestTweet() {
		Set<Entry<Integer, String>> entrySet = messageIdToMessageMap.entrySet();
		int lengthOfLongestTweet = 0;
		for (Entry<Integer, String> entry : entrySet) {
			String message = entry.getValue();
			if (message.length() > lengthOfLongestTweet) {
				lengthOfLongestTweet = message.length();
			}
		}
		return lengthOfLongestTweet;
	}

	@Override
	public String getMostFollowedUser() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getMostPopularMessage() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getMostProductiveUser() {
		// TODO Auto-generated method stub
		return null;
	}

	public void addNewTweetEntry(String user, String message, int messageId) {
		messageIdToMessageMap.put(messageId, message);
		if (userMessages.containsKey(user)) {
			HashMap<Integer, String> identifyUserMsgList = userMessages.get(user);
			identifyUserMsgList.put(messageId, message);
			// userMessages.put(user, identifyUserMsgList); this is not needed. since obj is
			// updated by reference
		} else {
			HashMap<Integer, String> m = new HashMap<Integer, String>();
			m.put(messageId, message);
			userMessages.put(user, m);
		}
	}
}
