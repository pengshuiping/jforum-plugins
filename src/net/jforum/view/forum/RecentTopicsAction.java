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
 * Created on Oct 19, 2004
 * The JForum Project
 * http://www.jforum.net
 */
package net.jforum.view.forum;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.jforum.Command;
import net.jforum.JForum;
import net.jforum.entities.Forum;
import net.jforum.entities.Topic;
import net.jforum.entities.User;
import net.jforum.repository.ForumRepository;
import net.jforum.repository.TopicRepository;
import net.jforum.util.preferences.ConfigKeys;
import net.jforum.util.preferences.SystemGlobals;
import net.jforum.util.preferences.TemplateKeys;
import net.jforum.util.I18n;
import net.jforum.view.forum.common.TopicsCommon;
import net.jforum.view.forum.common.ViewCommon;
import net.jforum.view.forum.common.ForumCommon;
import net.jforum.dao.UserDAO;
import net.jforum.dao.DataAccessDriver;

/**
 * Display a list of recent Topics
 * 
 * @author James Yong
 * @author Rafael Steil
 * @version $Id: RecentTopicsAction.java,v 1.12 2005/09/12 17:12:46 vmal Exp $
 */
public class RecentTopicsAction extends Command 
{
	private List forums;

	public void list() throws Exception
	{
		int postsPerPage = SystemGlobals.getIntValue(ConfigKeys.POST_PER_PAGE);

		this.setTemplateName(TemplateKeys.RECENT_LIST);
		
		this.context.put("postsPerPage", new Integer(postsPerPage));
		this.context.put("topics", this.topics());
		this.context.put("forums", this.forums);
		
		TopicsCommon.topicListingBase();
		JForum.getRequest().setAttribute("template", null);
	}
	
	List topics() throws Exception
	{
		int postsPerPage = SystemGlobals.getIntValue(ConfigKeys.POST_PER_PAGE);
		List tmpTopics = TopicRepository.getRecentTopics();
		
		this.forums = new ArrayList(postsPerPage);

		for (Iterator iter = tmpTopics.iterator(); iter.hasNext(); ) {
			Topic t = (Topic)iter.next();
			if (TopicsCommon.isTopicAccessible(t.getForumId())) {
				// Get name of forum that the topic refers to
				Forum f = ForumRepository.getForum(t.getForumId());
				forums.add(f);
			}
			else {
				iter.remove();
			}
		}
		
		return TopicsCommon.prepareTopics(tmpTopics);
	}

	public void showTopicsByUser() throws Exception
	{
	    DataAccessDriver da = DataAccessDriver.getInstance();
	    UserDAO udao = da.newUserDAO();
	    User u = udao.selectById(this.request.getIntParameter("user_id"));
		
	    if (u.getId() == 0) {
		this.context.put("message", I18n.getMessage("User.notFound"));
		this.setTemplateName(TemplateKeys.USER_NOT_FOUND);
	    } else {

		TopicsCommon.topicListingBase();
		int start = ViewCommon.getStartPage();
		int topicsPerPage = SystemGlobals.getIntValue(ConfigKeys.TOPICS_PER_PAGE);
		int postsPerPage = SystemGlobals.getIntValue(ConfigKeys.POST_PER_PAGE);

		int totalTopics=da.newTopicDAO().getUserTopics(u.getId());
		this.setTemplateName(TemplateKeys.RECENT_USER_TOPICS_SHOW);
		this.context.put("u",u);
		this.context.put("pageTitle", I18n.getMessage("ForumListing.userTopics")+" "+u.getUsername());
					
		ViewCommon.contextToPagination(start, totalTopics, topicsPerPage);
		this.context.put("postsPerPage", new Integer(postsPerPage));
		List l=TopicsCommon.prepareTopics(da.newTopicDAO().selectByUserByLimit(u.getId(),start,topicsPerPage));
		List fl = new ArrayList();
		for (Iterator iter = l.iterator(); iter.hasNext(); ) {
		    // they all are accessible
		    // Get name of forum that the topic refers to
		    fl.add(ForumRepository.getForum(((Topic)iter.next()).getForumId()));
		}
		this.context.put("topics", l);
		this.context.put("forums", fl);
	    }
	}
}
