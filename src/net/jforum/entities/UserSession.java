/*
 * Copyright (c) 2003, Rafael Steil
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
 * This file creation date: 30/12/2003 / 21:40:54
 * The JForum Project
 * http://www.jforum.net
 */
package net.jforum.entities;

import com.octo.captcha.image.ImageCaptcha;

import java.awt.image.BufferedImage;
import java.io.Serializable;
import java.util.Date;

import net.jforum.SessionFacade;
import net.jforum.repository.SecurityRepository;
import net.jforum.security.SecurityConstants;
import net.jforum.util.Captcha;
import net.jforum.util.preferences.ConfigKeys;
import net.jforum.util.preferences.SystemGlobals;

/**
 * Stores information about user's session.
 * 
 * @author Rafael Steil
 * @version $Id: UserSession.java,v 1.13 2004/12/28 04:18:13 rafaelsteil Exp $
 */
public class UserSession implements Serializable
{
	private Date startTime;
	private Date lastVisit;
	private long sessionTime;
	private int userId;
	private transient String sessionId;
	private String username;
	private boolean autoLogin;
	private String lang;
	private int privateMessages;
	private ImageCaptcha imageCaptcha = null;
	static final long serialVersionUID = 0;

	public UserSession() {}

	public UserSession(UserSession us)
	{
		this.startTime = us.getStartTime();
		this.lastVisit = us.getLastVisit();
		this.sessionTime = us.getSessionTime();
		this.userId = us.getUserId();
		this.sessionId = us.getSessionId();
		this.username = us.getUsername();
		this.autoLogin = us.getAutoLogin();
		this.lang = us.getLang();
		this.privateMessages = us.getPrivateMessages();
	}

	/**
	 * Set session's start time.
	 * 
	 * @param startTime  Start time in miliseconds
	 */
	public void setStartTime(Date startTime)
	{
		this.startTime = startTime;
	}

	/**
	 * @return Returns the privateMessages.
	 */
	public int getPrivateMessages()
	{
		return this.privateMessages;
	}

	/**
	 * @param privateMessages The privateMessages to set.
	 */
	public void setPrivateMessages(int privateMessages)
	{
		this.privateMessages = privateMessages;
	}

	/**
	 * Set session last visit time.
	 * 
	 * @param lastVisit Time in miliseconds
	 */
	public void setLastVisit(Date lastVisit)
	{
		this.lastVisit = lastVisit;
	}

	/**
	 * Set user's id
	 * 
	 * @param userId The user id
	 */
	public void setUserId(int userId)
	{
		this.userId = userId;
	}

	/**
	 * Set user's name
	 * 
	 * @param username The username
	 */
	public void setUsername(String username)
	{
		this.username = username;
	}

	public void setSessionId(String sessionId)
	{
		this.sessionId = sessionId;
	}

	public void setSessionTime(long sessionTime)
	{
		this.sessionTime = sessionTime;
	}

	public void setLang(String lang)
	{
		this.lang = lang;
	}

	/**
	 * Update the session time.
	 */
	public void updateSessionTime()
	{
		this.sessionTime = System.currentTimeMillis() - this.startTime.getTime();
	}

	/**
	 * Enable or disable auto-login.
	 * 
	 * @param autoLogin  <code>true</code> or <code>false</code> to represent auto-login status
	 */
	public void setAutoLogin(boolean autoLogin)
	{
		this.autoLogin = autoLogin;
	}

	/**
	 * Gets user's session start time
	 * 
	 * @return Start time in miliseconds
	 */
	public Date getStartTime()
	{
		return this.startTime;
	}

	public String getLang()
	{
		return this.lang;
	}

	/**
	 * Gets user's last visit time
	 * 
	 * @return Time in miliseconds
	 */
	public Date getLastVisit()
	{
		return this.lastVisit;
	}

	/**
	 * Gets the session time.
	 * 
	 * @return The session time
	 */
	public long getSessionTime()
	{
		return this.sessionTime;
	}

	/**
	 * Gets user's id
	 * 
	 * @return The user id
	 */
	public int getUserId()
	{
		return this.userId;
	}

	/**
	 * Gets the username
	 * 
	 * @return The username
	 */
	public String getUsername()
	{
		return this.username;
	}

	/**
	 * Gets auto-login status
	 * 
	 * @return <code>true</code> if auto-login is enabled, or <code>false</code> if disabled.
	 */
	public boolean getAutoLogin()
	{
		return this.autoLogin;
	}

	/**
	 * Gets the session id related to this user session
	 * 
	 * @return A string with the session id
	 */
	public String getSessionId()
	{
		return this.sessionId;
	}

	/**
	 * Checks if the user is an administrator
	 * 
	 * @return <code>true</code> if the user is an administrator
	 */
	public boolean isAdmin() throws Exception
	{
		return SecurityRepository.canAccess(this.userId, SecurityConstants.PERM_ADMINISTRATION);
	}

	/**
	 * Checks if the user is a moderator
	 * 
	 * @return <code>true</code> if the user has moderations rights
	 */
	public boolean isModerator() throws Exception
	{
		return SecurityRepository.canAccess(this.userId, SecurityConstants.PERM_MODERATION);
	}

	/**
	 * Makes the user's session "anoymous" - eg, the user. This method sets the session's start and
	 * last visit time to the current datetime, the user id to the return of a call to
	 * <code>SystemGlobals.getIntValue(ConfigKeys.ANONYMOUS_USER_ID)</code> and finally sets
	 * session attribute named "logged" to "0" will be considered a non-authenticated / anonymous
	 * user
	 */
	public void makeAnonymous()
	{
		this.setStartTime(new Date(System.currentTimeMillis()));
		this.setLastVisit(new Date(System.currentTimeMillis()));
		this.setUserId(SystemGlobals.getIntValue(ConfigKeys.ANONYMOUS_USER_ID));

		SessionFacade.setAttribute("logged", "0");
	}

	/**
	 * Sets a new user session information using information from an <code>User</code> instance.
	 * This method sets the user id, username, the number of private messages, the session's start
	 * time ( set to the current date and time ) and the language.
	 * 
	 * @param user The <code>User</code> instance to get data from
	 */
	public void dataToUser(User user)
	{
		this.setUserId(user.getId());
		this.setUsername(user.getUsername());
		this.setPrivateMessages(user.getPrivateMessagesCount());
		this.setStartTime(new Date(System.currentTimeMillis()));
		this.setLang(user.getLang());
	}

	/**
	 * Get the captcha image to challenge the user
	 * 
	 * @return BufferedImage the captcha image to challenge the user
	 */
	public BufferedImage getCaptchaImage()
	{
		if (this.imageCaptcha == null) {
			return null;
		}
		
		return (BufferedImage)this.imageCaptcha.getChallenge();
	}

	/**
	 * Validate the captcha response of user
	 * 
	 * @param anwser String the captcha response from user
	 * @return boolean true if the answer is valid, otherwise return false
	 */
	public boolean validateCaptchaResponse(String userResponse)
	{
		if (this.imageCaptcha != null) {
			return (this.imageCaptcha.validateResponse(userResponse)).booleanValue();
		}
		else {
			return false;
		}
	}

	/**
	 * create a new image captcha
	 * 
	 * @return void
	 */
	public void createNewCaptcha()
	{
		this.destroyCaptcha();
		this.imageCaptcha = Captcha.getInstance().getNextImageCaptcha();
	}

	/**
	 * Destroy the current captcha validation is done
	 * 
	 * @return void
	 */
	public void destroyCaptcha()
	{
		this.imageCaptcha = null;
	}
}
