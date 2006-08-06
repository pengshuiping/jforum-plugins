/*
 * Copyright (c) Rafael Steil
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
 * This file creation date: 21/05/2004 - 15:33:36
 * The JForum Project
 * http://www.jforum.net
 */
package net.jforum.view.forum.common;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.jforum.ActionServletRequest;
import net.jforum.JForumExecutionContext;
import net.jforum.SessionFacade;
import net.jforum.dao.PostDAO;
import net.jforum.entities.Post;
import net.jforum.entities.Smilie;
import net.jforum.repository.BBCodeRepository;
import net.jforum.repository.PostRepository;
import net.jforum.repository.SecurityRepository;
import net.jforum.repository.SmiliesRepository;
import net.jforum.security.SecurityConstants;
import net.jforum.util.SafeHtml;
import net.jforum.util.bbcode.BBCode;
import net.jforum.util.preferences.ConfigKeys;
import net.jforum.util.preferences.SystemGlobals;

/**
 * @author Rafael Steil
 * @version $Id: PostCommon.java,v 1.31 2006/08/06 00:07:45 rafaelsteil Exp $
 */
public class PostCommon
{
	private static PostCommon instance = new PostCommon();
	
	/**
	 * Gets the instance.
	 * This method only exists to situations where an instance is 
	 * needed in the template context, so we don't  need to 
	 * create a new instance every time.
	 * @return
	 */
	public static PostCommon getInstance()
	{
		return instance;
	}
	
	public static Post preparePostForDisplay(Post p)
	{
		if (p.getText() == null) {
			return p;
		}
		
		StringBuffer text = new StringBuffer(p.getText());
		
		if (!p.isHtmlEnabled()) {
			ViewCommon.replaceAll(text, "<", "&lt;");
			ViewCommon.replaceAll(text, ">", "&gt;");
		}
		
		// DO NOT remove the trailing blank space
		ViewCommon.replaceAll(text, "\n", "<br/> ");
		
		p.setText(text.toString());
		p.setText(alwaysProcess(p.getText(), BBCodeRepository.getBBCollection().getAlwaysProcessList()));

		// Then, search for bb codes
		if (p.isBbCodeEnabled()) {
			p.setText(PostCommon.processText(p.getText()));
		}

		// Smilies...
		if (p.isSmiliesEnabled()) {
			p.setText(processSmilies(new StringBuffer(p.getText()), SmiliesRepository.getSmilies()));
		}
		
		p.setText(SafeHtml.avoidJavascript(p.getText()));

		return p;
	}
	
	public  static String alwaysProcess(String text, Collection bbList)
	{
		for (Iterator iter = bbList.iterator(); iter.hasNext(); ) {
			BBCode bb = (BBCode)iter.next();
			text = text.replaceAll(bb.getRegex(), bb.getReplace());
		}
		
		return text;
	}

	public static String processText(String text)
	{
		if (text == null) {
			return null;
		}
		
		if (text.indexOf('[') == -1 || text.indexOf(']') == -1) {
			return text;
		}

		Iterator tmpIter = BBCodeRepository.getBBCollection().getBbList().iterator();

		while (tmpIter.hasNext()) {
			BBCode bb = (BBCode) tmpIter.next();

			// Another hack for the quotes
			if (bb.getTagName().equals("openQuote") 
					|| bb.getTagName().equals("openSimpleQuote")
					|| bb.getTagName().equals("closeQuote")) {
				text = text.replaceAll(bb.getRegex(), bb.getReplace());
			}
			else if (bb.getTagName().equals("code")) {
				Matcher matcher = Pattern.compile(bb.getRegex()).matcher(text);
				StringBuffer sb = new StringBuffer(text);

				while (matcher.find()) {
					StringBuffer contents = new StringBuffer(matcher.group(1));

					ViewCommon.replaceAll(contents, "<br/>", "\n");

					// Do not allow other bb tags inside "code"
					ViewCommon.replaceAll(contents, "[", "&#91;");
					ViewCommon.replaceAll(contents, "]", "&#93;");

					// Try to bypass smilies interpretation
					ViewCommon.replaceAll(contents, "(", "&#40;");
					ViewCommon.replaceAll(contents, ")", "&#41;");

					// XML-like tags
					ViewCommon.replaceAll(contents, "<", "&lt;");
					ViewCommon.replaceAll(contents, ">", "&gt;");

					StringBuffer replace = new StringBuffer(bb.getReplace());
					int index = replace.indexOf("$1");
					
					if (index > -1) {
						replace.replace(index, index + 2, contents.toString());
					}

					index = sb.indexOf("[code]");
					int lastIndex = sb.indexOf("[/code]") + "[/code]".length();

					if (lastIndex > index) {
						sb.replace(index, lastIndex, replace.toString());
					}
				}
				
				text = sb.toString();
			}
			else {
				text = text.replaceAll(bb.getRegex(), bb.getReplace());
			}
		}

		return text;
	}

	/**
	 * Replace the smlies code by the respective URL.
	 * @param text The text to process
	 * @param smilies the relation of {@link Smilie} instances
	 * @return the parsed text. Note that the StringBuffer you pass as parameter
	 * will already have the right contents, as the replaces are done on the instance
	 */
	public static String processSmilies(StringBuffer text, List smilies)
	{
		for (Iterator iter = smilies.iterator(); iter.hasNext(); ) {
			Smilie s = (Smilie) iter.next();
			int pos = text.indexOf(s.getCode());
			
			if (pos > -1) {
				text.replace(pos, pos + s.getCode().length(), s.getUrl());
			}
		}

		return text.toString();
	}

	public static Post fillPostFromRequest() throws Exception
	{
		Post p = new Post();
		p.setTime(new Date());

		return fillPostFromRequest(p, false);
	}

	public static Post fillPostFromRequest(Post p, boolean isEdit) throws Exception
	{
		ActionServletRequest request = JForumExecutionContext.getRequest();
		
		p.setSubject(SafeHtml.makeSafe(request.getParameter("subject")));
		p.setBbCodeEnabled(request.getParameter("disable_bbcode") != null ? false : true);
		p.setSmiliesEnabled(request.getParameter("disable_smilies") != null ? false : true);
		p.setSignatureEnabled(request.getParameter("attach_sig") != null ? true : false);

		if (!isEdit) {
			p.setUserIp(request.getRemoteAddr());
			p.setUserId(SessionFacade.getUserSession().getUserId());
		}
		
		boolean htmlEnabled = SecurityRepository.canAccess(SecurityConstants.PERM_HTML_DISABLED, 
				request.getParameter("forum_id"));
		p.setHtmlEnabled(htmlEnabled && request.getParameter("disable_html") == null);

		if (p.isHtmlEnabled()) {
			p.setText(SafeHtml.makeSafe(request.getParameter("message")));
		}
		else {
			p.setText(request.getParameter("message"));
		}

		return p;
	}

	public static List topicPosts(PostDAO dao, boolean canEdit, int userId, int topicId, int start, int count) throws Exception
	{
		List posts = null;
		boolean needPrepare = true;
		
 		if (SystemGlobals.getBoolValue(ConfigKeys.POSTS_CACHE_ENABLED)) {
 			posts = PostRepository.selectAllByTopicByLimit(topicId, start, count);
 			needPrepare = false;
 		}
 		else {
 			posts = dao.selectAllByTopicByLimit(topicId, start, count);
 		}
 		
		List helperList = new ArrayList();

		int anonymousUser = SystemGlobals.getIntValue(ConfigKeys.ANONYMOUS_USER_ID);

		for (Iterator iter = posts.iterator(); iter.hasNext(); ) {
			Post p;
			
			if (needPrepare) {
				p = (Post)iter.next();
			}
			else {
				p = new Post((Post)iter.next());
			}
			
			if (canEdit || (p.getUserId() != anonymousUser && p.getUserId() == userId)) {
				p.setCanEdit(true);
			}

			helperList.add(needPrepare ? PostCommon.preparePostForDisplay(p) : p);
		}

		return helperList;
	}
}
