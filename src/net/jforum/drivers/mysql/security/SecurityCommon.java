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
 * This file creation date: 19/03/2004 - 18:45:54
 * net.jforum.drivers.mysql.security.SecurityCommon.java
 * The JForum Project
 * http://www.jforum.net
 */
package net.jforum.drivers.mysql.security;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Iterator;

import net.jforum.JForum;
import net.jforum.security.Role;
import net.jforum.security.RoleCollection;
import net.jforum.security.RoleValue;
import net.jforum.security.RoleValueCollection;
import net.jforum.util.SystemGlobals;

/**
 * @author Rafael Steil
 */
class SecurityCommon 
{
	/**
	 * Execute the <i>add role</i> thing.
	 * As the SQL statement to insert user and group roles are diferent, they cannot be
	 * manipuled with a 'generic' statement, and is for this reason that <code>addRole</code>
	 * method is marked abstract. <br>
	 * The only job the <code>addRole</code> method should do is to get the correct SQL
	 * statement for each case - user or group - and the repass it to this method, who
	 * then do the job for us.
	 * 
	 * @param sql The SQL statement to be executed.
	 * @param id The ID do insert. May be user's or group's id, depending of the situation ( the caller ) 
	 * @param roleName The role name to insert
	 * @param roleValues A <code>RoleValueCollection</code> collection containing the role values to 
	 * insert. If none is wanted, just pass null as argument.
	 * @throws Exception
	 */
	public static void executeAddRole(String sql, int id, Role role, RoleValueCollection roleValues) throws Exception
	{
		PreparedStatement p = JForum.getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
		p.setInt(1, id);
		p.setString(2, role.getName());
		p.setInt(3, role.getType());
		
		p.executeUpdate();
		
		if (roleValues != null) {
			ResultSet rs = p.getGeneratedKeys();
			rs.next();
			
			int roleId = rs.getInt(1);
			
			rs.close();
			
			p = JForum.getConnection().prepareStatement(SystemGlobals.getSql("PermissionControl.addRoleValues"));
			
			for (Iterator iter = roleValues.iterator(); iter.hasNext(); ) {
				RoleValue rv = (RoleValue)iter.next();
				
				p.setInt(1, roleId);
				p.setString(2, rv.getValue());
				p.setInt(3, rv.getType());
				
				p.executeUpdate();
			}
		}
		
		p.close();	
	}
	
	/**
	 * See {@link PermissionControl#executeAddRole(String, int, String, RoleValueCollection)} for explanation
	 * about this method. The working way is the same. 
	 * 
	 * @param sql The SQL statement to execute
	 * @param id The ID do insert. May be user's or group's id, depending of the situation ( the caller )
	 * @return A <code>RoleCollection</code> collection with the roles processed.
	 * @throws Exception
	 */
	public static RoleCollection processLoadRoles(String sql, int id) throws Exception
	{
		RoleCollection rc = new RoleCollection();
		
		PreparedStatement p = JForum.getConnection().prepareStatement(sql);
		p.setInt(1, id);
		
		Role r = null;
		int lastId = -1;
		
		ResultSet rs = p.executeQuery();
		while (rs.next()) {
			if (rs.getInt("role_id") != lastId) {
				if (r != null) {
					rc.add(r);
				}
				r = new Role();
				
				r.setGroupId(id);
				r.setName(rs.getString("name"));
				r.setType(rs.getInt("type"));
				r.setId(rs.getInt("role_id"));
				
				lastId = r.getId();
			}
			
			if (rs.getString("value") != null) {
				RoleValue rv = new RoleValue();
				rv.setRoleId(r.getId());
				rv.setType(rs.getInt("rv_type"));
				rv.setValue(rs.getString("value"));
				
				r.getValues().add(rv);
			}
		}
		
		if (r != null) {
			rc.add(r);
		}
		
		rs.close();
		p.close();
		
		return rc;
	}
}
