package edu.sjsu.cmpe275.aop.tweet;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class TweetStatsServiceImpl implements TweetStatsService {
	/***
	 * Following is a dummy implementation. You are expected to provide an actual
	 * implementation based on the requirements.
	 */
	private HashMap<Integer, String> messageIdToMessageMap = new HashMap<Integer, String>(); // Map<messageId, message>
	private HashMap<String, HashMap<Integer, String>> userMessages = new HashMap<String, HashMap<Integer, String>>(); // Storing Users and their tweets
	private HashMap<String, ArrayList<String>> followers = new HashMap<String, ArrayList<String>>();

	@Override
	public void resetStatsAndSystem() {
		//messageIdToMessageMap = new HashMap<Integer, String>();
		//userMessages = new HashMap<String, HashMap<Integer, String>>();
		messageIdToMessageMap.clear();
		userMessages.clear();

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
		int maxFollowers = 0;
		int thisFollowers = 0;
		String mostFollowed = null;
		
		for (Map.Entry<String, ArrayList<String>> f  : followers.entrySet()) {
			ArrayList currentFollowers = f.getValue();
			thisFollowers = currentFollowers.size();
			if(thisFollowers > maxFollowers) {
				maxFollowers  = thisFollowers;
				mostFollowed = f.getKey();
			}
		}
		//System.out.println("*******************Most followed: "+mostFollowed);
		return mostFollowed;
	}

	@Override
	public String getMostPopularMessage() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getMostProductiveUser() {
		int thisCount = 0;
		int maxLength = 0;
		String mostProductive = null;
		int lengthOfMessage = 0;
		for (Map.Entry<String, HashMap<Integer, String>> um : userMessages.entrySet()) {
			lengthOfMessage = 0;
			HashMap<Integer, String>msg = um.getValue();
			//System.out.println("user: "+um.getKey());
			for(Map.Entry<Integer, String> len : msg.entrySet()) {
				//System.out.println("message: "+len.getValue());
				lengthOfMessage = lengthOfMessage + len.getValue().length();
				//System.out.println("lengthOfMessage: "+lengthOfMessage);
			}
			if(lengthOfMessage > maxLength) {
				maxLength = lengthOfMessage;
				//System.out.println("maxLength: "+maxLength);
				mostProductive = um.getKey();
				//System.out.println("Most productive: "+mostProductive);
			}
			
		}
		return mostProductive;
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
	
	public void addNewFollowEntry(String follower, String followee) {
		if (followers.containsKey(followee)) {
			ArrayList<String> followersList = followers.get(followee); // followers of that user
			followersList.add(follower);
			//followers.put(followee, followersList); // update the list for that user
			/*
			 * if(followersList.contains(follower)) {
			 * System.out.println("Already following"); }
			 */
			System.out.printf("User %s followed user %s \n", follower, followee);
			System.out.println("Followers1: " + followers);
		} else { // if the user has not been followed yet, i.e., this is his first follower
			ArrayList<String> followersList = new ArrayList<String>();
			followersList.add(follower);
			//followers.put(followee, followersList);
			System.out.printf("User %s followed user %s \n", follower, followee);
			System.out.println("Followers2: " + followers);

		}
	}
}
