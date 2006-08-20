/*
 * Copyright (c) JForum Team
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
 * This file creation date: 20/05/2004 - 15:51:10
 * The JForum Project
 * http://www.jforum.net
 */
package net.jforum.dao.generic;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import net.jforum.JForumExecutionContext;
import net.jforum.dao.DataAccessDriver;
import net.jforum.dao.UserDAO;
import net.jforum.entities.Post;
import net.jforum.entities.PrivateMessage;
import net.jforum.entities.PrivateMessageType;
import net.jforum.entities.User;
import net.jforum.exceptions.DatabaseException;
import net.jforum.util.DbUtils;
import net.jforum.util.preferences.ConfigKeys;
import net.jforum.util.preferences.SystemGlobals;

import org.apache.log4j.Logger;

/**
 * @author Rafael Steil
 * @version $Id: GenericPrivateMessageDAO.java,v 1.9 2006/08/20 22:47:28 rafaelsteil Exp $
 */
public class GenericPrivateMessageDAO extends AutoKeys implements net.jforum.dao.PrivateMessageDAO
{
    private final static Logger log = Logger.getLogger(GenericPrivateMessageDAO.class);

    /**
	 * @see net.jforum.dao.PrivateMessageDAO#send(net.jforum.entities.PrivateMessage)
	 */
	public void send(PrivateMessage pm)
	{
		// We should store 2 copies: one for the sendee's sent box
		// and another for the target user's inbox.
		PreparedStatement p=null;
        try
        {
            p = this.getStatementForAutoKeys("PrivateMessageModel.add");

            // Sendee's sent box
            this.addPm(pm, p);
            this.addPmText(pm);

            // Target user's inbox
            p.setInt(1, PrivateMessageType.NEW);
            pm.setId(this.executeAutoKeysQuery(p));

            this.addPmText(pm);
        }
        catch (SQLException e) {
            String es = "Error send()";
            log.error(es, e);
            throw new DatabaseException(es, e);
        }
        finally {
            DbUtils.close(p);
        }
    }
	
	protected void addPmText(PrivateMessage pm) throws SQLException
	{
		PreparedStatement text = JForumExecutionContext.getConnection().prepareStatement(
				SystemGlobals.getSql("PrivateMessagesModel.addText"));
		
		text.setInt(1, pm.getId());
		text.setString(2, pm.getPost().getText());
		text.executeUpdate();
		
		text.close();
	}
	
	protected void addPm(PrivateMessage pm, PreparedStatement p) throws SQLException
	{
		p.setInt(1, PrivateMessageType.SENT);
		p.setString(2, pm.getPost().getSubject());
		p.setInt(3, pm.getFromUser().getId());
		p.setInt(4, pm.getToUser().getId());
		p.setTimestamp(5, new Timestamp(pm.getPost().getTime().getTime()));
		p.setInt(6, pm.getPost().isBbCodeEnabled() ? 1 : 0);
		p.setInt(7, pm.getPost().isHtmlEnabled() ? 1 : 0);
		p.setInt(8, pm.getPost().isSmiliesEnabled() ? 1 : 0);
		p.setInt(9, pm.getPost().isSignatureEnabled() ? 1 : 0);
		
		this.setAutoGeneratedKeysQuery(SystemGlobals.getSql("PrivateMessagesModel.lastGeneratedPmId"));
		pm.setId(this.executeAutoKeysQuery(p));
	}
	
	/** 
	 * @see net.jforum.dao.PrivateMessageDAO#delete(net.jforum.entities.PrivateMessage[])
	 */
	public void delete(PrivateMessage[] pm) 
	{
		PreparedStatement p=null;
        PreparedStatement deleteText=null;
        try
        {
            p = JForumExecutionContext.getConnection().prepareStatement(SystemGlobals.getSql("PrivateMessageModel.delete"));
            p.setInt(2, pm[0].getFromUser().getId());
            p.setInt(3, pm[0].getFromUser().getId());

            deleteText = JForumExecutionContext.getConnection().prepareStatement(SystemGlobals.getSql("PrivateMessagesModel.deleteText"));

            for (int i = 0; i < pm.length; i++) {
                deleteText.setInt(1, pm[i].getId());
                deleteText.executeUpdate();

                p.setInt(1, pm[i].getId());
                p.executeUpdate();
            }
        }
        catch (SQLException e) {
            String es = "Error delete()";
            log.error(es, e);
            throw new DatabaseException(es, e);
        }
        finally {
            DbUtils.close( p);
            DbUtils.close( deleteText);
        }
    }

	/**
	 * @see net.jforum.dao.PrivateMessageDAO#selectFromInbox(net.jforum.entities.User)
	 */
	public List selectFromInbox(User user)
	{
		String query = SystemGlobals.getSql("PrivateMessageModel.baseListing");
		query = query.replaceAll("#FILTER#", SystemGlobals.getSql("PrivateMessageModel.inbox"));
		
		PreparedStatement p=null;
        ResultSet rs=null;
        try
        {
            p = JForumExecutionContext.getConnection().prepareStatement(query);
            p.setInt(1, user.getId());

            List pmList = new ArrayList();

            rs = p.executeQuery();
            while (rs.next()) {
                PrivateMessage pm = this.getPm(rs, false);

                User fromUser = new User();
                fromUser.setId(rs.getInt("user_id"));
                fromUser.setUsername(rs.getString("username"));

                pm.setFromUser(fromUser);

                pmList.add(pm);
            }

            return pmList;
        }
        catch (SQLException e) {
            String es = "Error selectFromInbox()";
            log.error(es, e);
            throw new DatabaseException(es, e);
        }
        finally {
            DbUtils.close(rs, p);
        }
    }

	/** 
	 * @see net.jforum.dao.PrivateMessageDAO#selectFromSent(net.jforum.entities.User)
	 */
	public List selectFromSent(User user)
	{
		String query = SystemGlobals.getSql("PrivateMessageModel.baseListing");
		query = query.replaceAll("#FILTER#", SystemGlobals.getSql("PrivateMessageModel.sent"));
		
		PreparedStatement p=null;
        ResultSet rs=null;
        try
        {
            p = JForumExecutionContext.getConnection().prepareStatement(query);
            p.setInt(1, user.getId());

            List pmList = new ArrayList();

            rs = p.executeQuery();
            while (rs.next()) {
                PrivateMessage pm = this.getPm(rs, false);

                User toUser = new User();
                toUser.setId(rs.getInt("user_id"));
                toUser.setUsername(rs.getString("username"));

                pm.setToUser(toUser);

                pmList.add(pm);
            }
            return pmList;
        }
        catch (SQLException e) {
            String es = "Error selectFromSent()";
            log.error(es, e);
            throw new DatabaseException(es, e);
        }
        finally {
            DbUtils.close(rs, p);
        }
    }
	
	protected PrivateMessage getPm(ResultSet rs) throws SQLException
	{
		return this.getPm(rs, true);
	}
	
	protected PrivateMessage getPm(ResultSet rs, boolean full) throws SQLException
	{
		PrivateMessage pm = new PrivateMessage();
		Post p = new Post();

		pm.setId(rs.getInt("privmsgs_id"));
		pm.setType(rs.getInt("privmsgs_type"));
		p.setTime(new Date(rs.getTimestamp("privmsgs_date").getTime()));
		p.setSubject(rs.getString("privmsgs_subject"));
		
		SimpleDateFormat df = new SimpleDateFormat(SystemGlobals.getValue(ConfigKeys.DATE_TIME_FORMAT));
		pm.setFormatedDate(df.format(p.getTime()));
		
		if (full) {
			UserDAO um = DataAccessDriver.getInstance().newUserDAO();
			pm.setFromUser(um.selectById(rs.getInt("privmsgs_from_userid")));
			pm.setToUser(um.selectById(rs.getInt("privmsgs_to_userid")));
			
			p.setBbCodeEnabled(rs.getInt("privmsgs_enable_bbcode") == 1);
			p.setSignatureEnabled(rs.getInt("privmsgs_attach_sig") == 1);
			p.setHtmlEnabled(rs.getInt("privmsgs_enable_html") == 1);
			p.setSmiliesEnabled(rs.getInt("privmsgs_enable_smilies") == 1);
			p.setText(this.getPmText(rs));
		}
		
		pm.setPost(p);

		return pm;
	}
	
	protected String getPmText(ResultSet rs) throws SQLException
	{
		return rs.getString("privmsgs_text");
	}

	/** 
	 * @see net.jforum.dao.PrivateMessageDAO#selectById(net.jforum.entities.PrivateMessage)
	 */
	public PrivateMessage selectById(PrivateMessage pm)
	{
		PreparedStatement p=null;
        ResultSet rs=null;
        try
        {
            p = JForumExecutionContext.getConnection().prepareStatement(SystemGlobals.getSql("PrivateMessageModel.selectById"));
            p.setInt(1, pm.getId());

            rs = p.executeQuery();
            if (rs.next()) {
                pm = this.getPm(rs);
            }

            return pm;
        }
        catch (SQLException e) {
            String es = "Error selectById()";
            log.error(es, e);
            throw new DatabaseException(es, e);
        }
        finally {
            DbUtils.close(rs, p);
        }
    }

	/** 
	 * @see net.jforum.dao.PrivateMessageDAO#updateType(net.jforum.entities.PrivateMessage)
	 */
	public void updateType(PrivateMessage pm)
	{
		PreparedStatement p=null;
        try
        {
            p = JForumExecutionContext.getConnection().prepareStatement(SystemGlobals.getSql("PrivateMessageModel.updateType"));
            p.setInt(1,pm.getType());
            p.setInt(2, pm.getId());
            p.executeUpdate();
        }
        catch (SQLException e) {
            String es = "Error updateType()";
            log.error(es, e);
            throw new DatabaseException(es, e);
        }
        finally {
            DbUtils.close( p);
        }
	}
}
