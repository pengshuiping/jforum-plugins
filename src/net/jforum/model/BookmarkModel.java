/*
 * Copyright (c) 2003, Rafael Steil
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, 
 * with or without modification, are permitted provided 
 * that the following conditions are met:
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
 * Created on Jan 16, 2005 12:30:52 PM
 * The JForum Project
 * http://www.jforum.net
 */
package net.jforum.model;

import java.util.List;

import net.jforum.entities.Bookmark;

/**
 * @author Rafael Steil
 * @version $Id: BookmarkModel.java,v 1.2 2005/01/26 12:57:13 rafaelsteil Exp $
 */
public interface BookmarkModel
{
	/**
	 * Adds a new bookmark.
	 * 
	 * @param b The bookmark to add
	 * @throws Exception
	 */
	public void add(Bookmark b) throws Exception;
	
	/**
	 * Updates a bookmark.
	 * Only the fields <i>publicVisible</i>, <i>title</i>
	 * and <i>description</i> are changed.
	 * All other fields remain with the same value.
	 * 
	 * @param b The bookmark to update
	 * @throws Exception
	 */
	public void update(Bookmark b) throws Exception;
	
	/**
	 * Removes a bookmark.
	 * 
	 * @param bookmarkId The bookmark's id to remove
	 * @throws Exception
	 */
	public void remove(int bookmarkId) throws Exception;
	
	/**
	 * Gets all bookmarks of a given type.
	 * 
	 * @param userId The bookmark's owner
	 * @param relationType Any valid type declared in
	 * <code>net.jforum.entities.BookmarkType</code>
	 * @return A list with all results found. Each entry is
	 * a {@link net.jforum.entities.Bookmark} instance.
	 * @throws Exception
	 */
	public List selectByUser(int userId, int relationType) throws Exception;
	
	/**
	 * Gets all bookmarks from some user.
	 * 
	 * @param userId The bookmark's owner
	 * <code>net.jforum.entities.BookmarkType</code>
	 * @return A list with all results found. Each entry is
	 * a {@link net.jforum.entities.Bookmark} instance.
	 * @throws Exception
	 */
	public List selectByUser(int userId) throws Exception;
	
	/**
	 * Gets a bookmark.
	 * 
	 * @param bookmarkId The bookmark id
	 * @return A Bookmark instance or null if no entry found
	 * @throws Exception
	 */
	public Bookmark selectById(int bookmarkId) throws Exception;
	
	/**
	 * Gets a bookmark for edition.
	 * 
	 * @param relationId The relation's id
	 * @param relationType The relation type.
	 * @param userId The bookmark's owner
	 * @return A bookmark instance of <code>null</code> if 
	 * the record cannot be found
	 * @throws Exception
	 */
	public Bookmark selectForUpdate(int relationId, int relationType, int userId) throws Exception;
}
