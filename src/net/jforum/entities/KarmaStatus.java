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
 * Created on Jan 11, 2005 11:05:57 PM
 * The JForum Project
 * http://www.jforum.net
 */
package net.jforum.entities;

/**
 * @author Rafael Steil
 * @version $Id: KarmaStatus.java,v 1.2 2005/01/14 21:11:48 rafaelsteil Exp $
 */
public class KarmaStatus
{
	private int id;
	private double karmaPoints;
	
	public KarmaStatus() {}
	
	public KarmaStatus(KarmaStatus karma)
	{
		this.id = karma.getId();
		this.karmaPoints = karma.getKarmaPoints();
	}
	
	public KarmaStatus(int id, double points)
	{
		this.id = id;
		this.karmaPoints = points;
	}
	
	/**
	 * @return Returns the karmaPoints.
	 */
	public double getKarmaPoints()
	{
		return this.karmaPoints;
	}
	
	public void setKarmaPoints(double points)
	{
		this.karmaPoints = points;
	}
	
	/**
	 * @return Returns the userId.
	 */
	public int getId()
	{
		return this.id;
	}
	
	/**
	 * @param userId The userId to set.
	 */
	public void setId(int userId)
	{
		this.id = userId;
	}
}
