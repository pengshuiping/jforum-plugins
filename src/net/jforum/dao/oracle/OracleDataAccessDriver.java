/*
 * Copyright (c) 2003, 2004, Rafael Steil
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, 
 * with or without modification, are permitted provided 
 * that the following conditions are met:
 * 
 * 1) Redistributions of source code must retain the above 
 * copyright notice, this list of conditions and the 
 * following  disclaimer.
 * 2)  Redistributions in binary form must reproduce the 
 * above copyright notice, this list of conditions and 
 * the following disclaimer in the documentation and/or 
 * other materials provided with the distribution.
 * 3) Neither the name of "Rafael Steil" nor 
 * the names of its contributors may be used to endorse 
 * or promote products derived from this software without 
 * specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT 
 * HOLDERS AND CONTRIBUTORS "AS IS" AND ANY 
 * EXPRESS OR IMPLIED WARRANTIES, INCLUDING, 
 * BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF 
 * MERCHANTABILITY AND FITNESS FOR A PARTICULAR 
 * PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL 
 * THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE 
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, 
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES 
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF 
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, 
 * OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER 
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER 
 * IN CONTRACT, STRICT LIABILITY, OR TORT 
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN 
 * ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF 
 * ADVISED OF THE POSSIBILITY OF SUCH DAMAGE
 * 
 * This file creation date: 24/05/2004 / 12:01 PM
 * The JForum Project
 * http://www.jforum.net
 */
package net.jforum.dao.oracle;

/**
 * @author Rafael Steil
 * @version $Id: OracleDataAccessDriver.java,v 1.2 2005/03/26 04:11:01 rafaelsteil Exp $
 */
public class OracleDataAccessDriver extends net.jforum.dao.generic.DataAccessDriver
{
	private static OraclePostDAO postModel = new OraclePostDAO();
	private static OracleTopicDAO topicModel = new OracleTopicDAO();
	private static OracleUserDAO userModel = new OracleUserDAO();
	private static OraclePrivateMessageDAO pmModel = new OraclePrivateMessageDAO();
	private static OracleScheduledSearchIndexerDAO ssim = new OracleScheduledSearchIndexerDAO();
	
	/**
	 * @see net.jforum.dao.DataAccessDriver#newPostDAO()
	 */
	public net.jforum.dao.PostDAO newPostDAO()
	{
		return postModel;
	}

	/** 
	 * @see net.jforum.dao.DataAccessDriver#newTopicDAO()
	 */
	public net.jforum.dao.TopicDAO newTopicDAO()
	{
		return topicModel;
	}
	
	/** 
	 * @see net.jforum.dao.DataAccessDriver#newUserDAO()
	 */
	public net.jforum.dao.UserDAO newUserDAO()
	{
		return userModel;
	}
	
	/**
	 * @see net.jforum.dao.DataAccessDriver#newPrivateMessageDAO()
	 */
	public net.jforum.dao.PrivateMessageDAO newPrivateMessageDAO()
	{
		return pmModel;
	}
	
	/**
	 * @see net.jforum.dao.DataAccessDriver#newScheduledSearchIndexerDAO()
	 */
	public net.jforum.dao.ScheduledSearchIndexerDAO newScheduledSearchIndexerDAO()
	{
		return ssim;
	}
}
