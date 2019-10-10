package edu.sjsu.cmpe275.aop.tweet;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.TreeSet;

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
	private HashMap<String, ArrayList<String>> followers = new HashMap<String, ArrayList<String>>();
	private HashMap<String, ArrayList<String>> blockList = new HashMap<String, ArrayList<String>>();
	private SortedMap<String, Set<String>> messageToSharedWithUsersMap = new TreeMap<String, Set<String>>();

	public HashMap<String, ArrayList<String>> getBlockList() {
		return blockList;
	}

	public HashMap<String, ArrayList<String>> getFollowers() {
		return followers;
	}

	public boolean messageExists(int messageId) {
		boolean exists = messageIdToMessageMap.containsKey(messageId);
		return exists;
	}

	@Override
	public void resetStatsAndSystem() {
		// messageIdToMessageMap = new HashMap<Integer, String>();
		// userMessages = new HashMap<String, HashMap<Integer, String>>();
		// messageIdToMessageMap.clear();
		// userMessages.clear();
		followers.clear();
		blockList.clear();
		messageToSharedWithUsersMap.clear();
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

		for (Map.Entry<String, ArrayList<String>> f : followers.entrySet()) {
			List<String> currentFollowers = f.getValue();
			System.out.println("current followers: " + currentFollowers + "of: " + f.getKey());
			thisFollowers = currentFollowers.size();
			if (thisFollowers > maxFollowers) {
				maxFollowers = thisFollowers;
				mostFollowed = f.getKey();
			}
		}
		System.out.println("*******************Most followed: " + mostFollowed);
		return mostFollowed;
	}

	/*****************************************************************************/

	@Override
	public String getMostPopularMessage() {
	
		Set<Entry<String, Set<String>>> entrySet = messageToSharedWithUsersMap.entrySet();
		int maxSharedCount = 0;
		String popularMessage = null;
		for (Entry<String, Set<String>> entry : entrySet) {
			String message = entry.getKey();
			Set<String> sharedWithUsers = entry.getValue();
			if (sharedWithUsers != null && sharedWithUsers.size() > maxSharedCount) {
				maxSharedCount = sharedWithUsers.size();
				popularMessage = message;
			}

		}
		return popularMessage;
	}

	@Override
	public String getMostProductiveUser() {
		int maxLength = 0;
		String mostProductive = null;
		int lengthOfMessage = 0;
		for (Map.Entry<String, HashMap<Integer, String>> um : userMessages.entrySet()) {
			lengthOfMessage = 0;
			HashMap<Integer, String> msg = um.getValue();
			// System.out.println("user: "+um.getKey());
			for (Map.Entry<Integer, String> len : msg.entrySet()) {
				// System.out.println("message: "+len.getValue());
				lengthOfMessage = lengthOfMessage + len.getValue().length();
				// System.out.println("lengthOfMessage: "+lengthOfMessage);
			}
			if (lengthOfMessage > maxLength) {
				maxLength = lengthOfMessage;
				// System.out.println("maxLength: "+maxLength);
				mostProductive = um.getKey();
				// System.out.println("Most productive: "+mostProductive);
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

	/**
	 * update share count on tweet or re-tweet
	 * 
	 * @param user
	 * @param message
	 */
	public void updateMessageShareDetails(String user, String message) {
		// if (messageToSharedWithUsersMap.containsKey(message)) {

		ArrayList<String> currentUserFollowers = followers.get(user);
		List<String> newShareList = new ArrayList<String>();
		if (currentUserFollowers != null && !currentUserFollowers.isEmpty()) {
			for (String follower : currentUserFollowers) {
				ArrayList<String> currentUsersBlockedList = blockList.get(user);
				if (currentUsersBlockedList != null && !currentUsersBlockedList.isEmpty()) {
					if (!currentUsersBlockedList.contains(follower)) {
						newShareList.add(follower);
					}
				}else {
					newShareList.add(follower);
				}
			}
		}
		Set<String> sharedWithUsers = messageToSharedWithUsersMap.get(message);
		if (sharedWithUsers == null) {
			TreeSet<String> treeSet = new TreeSet<String>();
			treeSet.addAll(newShareList);
			messageToSharedWithUsersMap.put(message, treeSet);
		} else {
			sharedWithUsers.addAll(newShareList);
		}

		// }
	}

	public void addNewFollowEntry(String follower, String followee) {
		if (followers.containsKey(followee)) {
			ArrayList<String> followersList = followers.get(followee); // followers of that user
			followersList.add(follower);
			followers.put(followee, followersList); // update the list for that user
			/*
			 * if(followersList.contains(follower)) {
			 * System.out.println("Already following"); }
			 */
			System.out.printf("User %s followed user %s \n", follower, followee);
			System.out.println("Followers1: " + followers);
		} else { // if the user has not been followed yet, i.e., this is his first follower
			ArrayList<String> followersList = new ArrayList<String>();
			followersList.add(follower);
			followers.put(followee, followersList);
			System.out.printf("User %s followed user %s \n", follower, followee);
			System.out.println("Followers2: " + followers);

		}
	}

	public void addNewBlockEntry(String follower, String blocker) {
		if (blockList.containsKey(blocker)) {
			ArrayList<String> block = blockList.get(blocker); // followers of that user
			block.add(follower);
			blockList.put(blocker, block); // update the list for that user
			/*
			 * if(followersList.contains(follower)) {
			 * System.out.println("Already following"); }
			 */
			System.out.printf("User %s followed user %s \n", follower, blocker);
			System.out.println("Followers1: " + blockList);
		} else { // if the user has not been followed yet, i.e., this is his first follower
			ArrayList<String> block = new ArrayList<String>();
			block.add(follower);
			blockList.put(blocker, block);
			System.out.printf("User %s followed user %s \n", follower, blocker);
			System.out.println("Followers2: " + blockList);

		}
	}

	public String getMessageOwner(int messageId) {
		String uName = "";
		boolean found = false;

		for (Map.Entry<String, HashMap<Integer, String>> um : userMessages.entrySet()) {
			uName = um.getKey();
			HashMap<Integer, String> mes = um.getValue();
			System.out.println("User: " + uName);
			System.out.println("messages: " + mes);

			for (Map.Entry<Integer, String> m : mes.entrySet()) {
				int mID = m.getKey();
				if (messageId == mID) {
					found = true;
					break;
				}
			}
			if (found == true) {
				break;
			}

		}
		return uName;

	}

	public String getMessageById(int msgId) {
		return messageIdToMessageMap.get(msgId);
	}
}
