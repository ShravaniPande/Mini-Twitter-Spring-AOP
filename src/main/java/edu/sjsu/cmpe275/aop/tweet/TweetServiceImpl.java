package edu.sjsu.cmpe275.aop.tweet;

import java.io.IOException;
import java.security.AccessControlException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

public class TweetServiceImpl implements TweetService {

    /***
     * Following is a dummy implementation.
     * You can tweak the implementation to suit your need, but this file is NOT part of the submission.
     */
	
	private ArrayList <String> msgList;								//for list of all the tweets
	private HashMap<Integer, String> messages;						//for storing tweets with their IDs
	private HashMap<String, HashMap<Integer,String>> userMessages;	//Storing Users and their tweets
	private HashMap<String, ArrayList<String>> followers;
	private HashMap<String, ArrayList<String>> blockList;
	int msgID;
	
	public TweetServiceImpl() {
		msgList = new ArrayList <String>();
		messages = new HashMap <Integer, String>();
		userMessages = new HashMap<String, HashMap<Integer,String>>();
		//msgID = 0;
		followers = new HashMap<String, ArrayList<String>>();
		blockList = new HashMap<String, ArrayList<String>>();
		//System.out.println("Constructor created");
	}
	
	@Override
    public int tweet(String user, String message) throws IllegalArgumentException, IOException {
		if(message.length() > 140 || message == "" || message == null) {
			throw new IllegalArgumentException();
		}
		else {
				msgList.add(message);		//this will create a simple list of messages
				msgID = msgList.size();		//to get message ID
				//System.out.println("No. of msgs: "+msgID);
				messages.put(msgID, message);	//creating a map of message ID and message
				if(userMessages.containsKey(user)) {		//to append the new message to the alread existing list if a user has already tweeted before
				HashMap <Integer,String> identifyUserMsgList = userMessages.get(user);
				identifyUserMsgList.put(msgID, message);
				userMessages.put(user, identifyUserMsgList);
				}
				else {
					HashMap <Integer,String> m = new HashMap<Integer, String>();
					m.put(msgID,message);
					userMessages.put(user, m);
				}
				System.out.println("message map: "+messages);
				System.out.println("user message list: "+userMessages);
		    	System.out.printf("User %s tweeted message: %s\n", user, message);
	    	return msgID;
		}
    }

	@Override
    public void follow(String follower, String followee) throws IOException {
		
		if (followers.containsKey(followee)) {
			ArrayList <String> followersList = followers.get(followee);			//followers of that user
			followersList.add(follower);
			followers.put(followee, followersList);								//update the list for that user
			/*if(followersList.contains(follower)) {
				System.out.println("Already following");
			}*/
			System.out.printf("User %s followed user %s \n", follower, followee);
			System.out.println("Followers1: "+followers);
		}
		else {																	//if the user has not been followed yet, i.e., this is his first follower
			ArrayList < String> followersList = new ArrayList <String>();
			followersList.add(follower);
			followers.put(followee, followersList);
			System.out.printf("User %s followed user %s \n", follower, followee);
			System.out.println("Followers2: "+followers);
			
		}
		
       	
    }

	@Override
	public void block(String user, String follower) throws IllegalArgumentException,IOException {
		if(user.equals(follower)) {					//user cannot block himself
			throw new IllegalArgumentException();
		}
		if (blockList.containsKey(user)) {
			ArrayList <String> blockedUsers = blockList.get(user);
			blockedUsers.add(follower);
			/*if(blockedUsers.contains(follower)) {
				System.out.println("User already blocked");
			}*/
			System.out.printf("User %s blocked user %s \n", user, follower);	
		}
		else {
			ArrayList <String> blockedUsers = new ArrayList<String>();
			blockedUsers.add(follower);
			blockList.put(user, blockedUsers);
			System.out.printf("User %s blocked user %s \n", user, follower);	
		}
       		
	}

	@Override
	public int retweet(String user, int messageId)
			throws AccessControlException, IllegalArgumentException, IOException {
		// TODO Auto-generated method stub

		
		return 0;
	}

}
