/*
 * Copyright (c) 2003, Rafael Steil
 * All rights reserved.

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
 * Created on 24/05/2004 22:36:07
 * The JForum Project
 * http://www.jforum.net
 */
package net.jforum.drivers.postgresql;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import net.jforum.JForum;
import net.jforum.entities.User;
import net.jforum.util.preferences.SystemGlobals;

/**
 * @author Rafael Steil
 * @version $Id: UserModel.java,v 1.3 2004/06/01 19:47:18 pieter2 Exp $
 */
public class UserModel extends net.jforum.drivers.generic.UserModel
{
	/** 
	 * @see net.jforum.model.UserModel#addNew(net.jforum.entities.User)
	 */
	public int addNew(User user) throws Exception
	{
		this.setSupportAutoGeneratedKey(false);
		this.setAutoGeneratedKeysQuery(SystemGlobals.getSql("UserModel.lastGeneratedUserId"));
		
		return super.addNew(user);
	}

	/** 
	 * @see net.jforum.model.UserModel#selectAll(int, int)
	 */
	public ArrayList selectAll(int startFrom, int count) throws Exception
	{
		PreparedStatement p;
		ArrayList list = new ArrayList();
		
		if (count > 0) {
			p = JForum.getConnection().prepareStatement(SystemGlobals.getSql("UserModel.selectAllByLimit"));
			p.setInt(1, count);
			p.setInt(2, startFrom);
		}
		else {
			p = JForum.getConnection().prepareStatement(SystemGlobals.getSql("UserModel.selectAll"));
		}
		
		ResultSet rs = p.executeQuery();
		while (rs.next()) {
			User u = new User();
			
			u.setEmail(rs.getString("user_email"));
			u.setId(rs.getInt("user_id"));
			u.setTotalPosts(rs.getInt("user_posts"));
			u.setRegistrationDate(rs.getString("user_regdate"));
			u.setUsername(rs.getString("username"));
			
			list.add(u);
		}

		rs.close();
		p.close();
		
		return list;
	}
}
