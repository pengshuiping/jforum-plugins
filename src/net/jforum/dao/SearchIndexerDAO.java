/*
 * Copyright (c) 2005 Rafael Steil
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
 * Created on Feb 22, 2005 4:23:15 PM
 * The JForum Project
 * http://www.jforum.net
 */
package net.jforum.dao;

import java.sql.Connection;
import java.util.List;

import net.jforum.entities.Post;

/**
 * @author Rafael Steil
 * @version $Id: SearchIndexerDAO.java,v 1.2 2005/03/26 04:10:34 rafaelsteil Exp $
 */
public interface SearchIndexerDAO
{
	/**
	 * Sets a connection to the class.
	 * 
	 * @param conn The connection the class will use
	 */
	public void setConnection(Connection conn);
	
	/**
	 * Indexes a set of posts.
	 * 
	 * @param posts The posts to index
	 * @throws Exception
	 */
	public void insertSearchWords(List posts) throws Exception;
	
	/**
	 * Indexes a post
	 * 
	 * @param post The post to index
	 * @throws Exception
	 */
	public void insertSearchWords(Post post) throws Exception;
}