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
 * Created on 24/05/2004 01:07:39
 * The JForum Project
 * http://www.jforum.net
 */
package net.jforum.drivers.oracle;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;

import net.jforum.JForum;
import net.jforum.drivers.generic.UserModel;
import net.jforum.entities.Post;
import net.jforum.entities.PrivateMessage;
import net.jforum.entities.PrivateMessageType;
import net.jforum.util.preferences.ConfigKeys;
import net.jforum.util.preferences.SystemGlobals;

/**
 * @author Rafael Steil
 * @version $Id: PrivateMessageModel.java,v 1.1 2005/01/13 23:30:07 rafaelsteil Exp $
 */
public class PrivateMessageModel extends net.jforum.drivers.generic.PrivateMessageModel
{
	protected PrivateMessage getPm(ResultSet rs, boolean full) throws Exception
	{
		PrivateMessage pm = new PrivateMessage();
		Post p = new Post();

		pm.setId(rs.getInt("privmsgs_id"));
		pm.setType(rs.getInt("privmsgs_type"));
		p.setTime(rs.getTimestamp("privmsgs_date"));
		p.setSubject(rs.getString("privmsgs_subject"));

		SimpleDateFormat df = new SimpleDateFormat(SystemGlobals.getValue(ConfigKeys.DATE_TIME_FORMAT));
		pm.setFormatedDate(df.format(p.getTime()));

		if (full) {
			net.jforum.drivers.generic.UserModel um = new UserModel();
			pm.setFromUser(um.selectById(rs.getInt("privmsgs_from_userid")));
			pm.setToUser(um.selectById(rs.getInt("privmsgs_to_userid")));

			p.setBbCodeEnabled(rs.getInt("privmsgs_enable_bbcode") == 1);
			p.setSignatureEnabled(rs.getInt("privmsgs_attach_sig") == 1);
			p.setHtmlEnabled(rs.getInt("privmsgs_enable_html") == 1);
			p.setSmiliesEnabled(rs.getInt("privmsgs_enable_smilies") == 1);
			String post_text = net.jforum.drivers.oracle.DataAccessDriver
					.readBlobUTF16BinaryStream(rs, "privmsgs_text");
			p.setText(post_text);
		}

		pm.setPost(p);

		return pm;
	}

	/**
	 * @see net.jforum.drivers.generic.PrivateMessageModel#send(net.jforum.entities.PrivateMessage)
	 */
	public void send(PrivateMessage pm) throws Exception
	{
		this.setSupportAutoGeneratedKey(false);
		this.setAutoGeneratedKeysQuery(SystemGlobals.getSql("PrivateMessagesModel.lastGeneratedPmId"));

		// We should store 2 copies: one for the sendee's sent box
		// and another for the target user's inbox.
		// Sendee's sent box
		PreparedStatement p = this.getStatementForAutoKeys("PrivateMessageModel.add");
		p.setInt(1, PrivateMessageType.SENT);
		p.setString(2, pm.getPost().getSubject());
		p.setInt(3, pm.getFromUser().getId());
		p.setInt(4, pm.getToUser().getId());
		p.setTimestamp(5, new Timestamp(pm.getPost().getTime().getTime()));
		p.setString(6, pm.getPost().isBbCodeEnabled() ? "1" : "0");
		p.setString(7, pm.getPost().isHtmlEnabled() ? "1" : "0");
		p.setString(8, pm.getPost().isSmiliesEnabled() ? "1" : "0");
		p.setString(9, pm.getPost().isSignatureEnabled() ? "1" : "0");

		int fromPmId = this.executeAutoKeysQuery(p);

		p.setInt(1, PrivateMessageType.NEW);

		int toPmId = this.executeAutoKeysQuery(p);

		p.close();

		// From fromStatement

		PreparedStatement fromStatement = JForum.getConnection().prepareStatement(
				SystemGlobals.getSql("PrivateMessagesModel.addText"));
		fromStatement.setInt(1, fromPmId);
		fromStatement.executeUpdate();
		fromStatement.close();

		net.jforum.drivers.oracle.DataAccessDriver.writeBlobUTF16BinaryStream(SystemGlobals
				.getSql("PrivateMessagesModel.addTextField"), fromPmId, pm.getPost().getText());

		fromStatement.close();

		// From fromStatement

		PreparedStatement toStatement = JForum.getConnection().prepareStatement(
				SystemGlobals.getSql("PrivateMessagesModel.addText"));
		toStatement.setInt(1, toPmId);
		toStatement.executeUpdate();
		toStatement.close();

		net.jforum.drivers.oracle.DataAccessDriver.writeBlobUTF16BinaryStream(SystemGlobals
				.getSql("PrivateMessagesModel.addTextField"), toPmId, pm.getPost().getText());

		toStatement.close();
	}
}
