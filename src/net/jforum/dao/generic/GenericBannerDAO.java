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
 * This file creation date: Mar 6, 2003 / 11:09:34 PM
 * The JForum Project
 * http://www.jforum.net
 */
package net.jforum.dao.generic;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import net.jforum.JForum;
import net.jforum.entities.Banner;
import net.jforum.util.preferences.SystemGlobals;

/**
 * @author Samuel Yung
 * @version $Id: GenericBannerDAO.java,v 1.4 2005/07/26 02:45:16 diegopires Exp $
 */
public class GenericBannerDAO extends AutoKeys implements
		net.jforum.dao.BannerDAO {
	public Banner selectById(int bannerId) throws Exception {
		PreparedStatement p = JForum.getConnection().prepareStatement(
				SystemGlobals.getSql("BannerDAO.selectById"));
		p.setInt(1, bannerId);

		ResultSet rs = p.executeQuery();

		Banner b = new Banner();
		if (rs.next()) {
			b = this.getBanner(rs);
		}

		rs.close();
		p.close();

		return b;
	}

	public List selectAll() throws Exception {
		PreparedStatement p = JForum.getConnection().prepareStatement(
				SystemGlobals.getSql("BannerDAO.selectAll"));
		List l = new ArrayList();

		ResultSet rs = p.executeQuery();
		while (rs.next()) {
			l.add(this.getBanner(rs));
		}

		rs.close();
		p.close();

		return l;
	}

	protected Banner getBanner(ResultSet rs) throws Exception {
		Banner b = new Banner();

		b.setId(rs.getInt("banner_id"));
		b.setName(rs.getString("banner_name"));
		b.setPlacement(rs.getInt("banner_placement"));
		b.setDescription(rs.getString("banner_description"));
		b.setClicks(rs.getInt("banner_clicks"));
		b.setViews(rs.getInt("banner_views"));
		b.setUrl(rs.getString("banner_url"));
		b.setWeight(rs.getInt("banner_weight"));
		b.setActive(rs.getInt("banner_active") == 1);
		b.setComment(rs.getString("banner_comment"));
		b.setType(rs.getInt("banner_type"));
		b.setWidth(rs.getInt("banner_width"));
		b.setHeight(rs.getInt("banner_height"));

		return b;
	}

	public boolean canDelete(int bannerId) throws Exception {
		boolean result = true;
		PreparedStatement p = JForum.getConnection().prepareStatement(
				SystemGlobals.getSql("BannerDAO.canDelete"));
		p.setInt(1, bannerId);

		ResultSet rs = p.executeQuery();
		if (!rs.next() || rs.getInt("total") < 1) {
			result = false;
		}

		rs.close();
		p.close();

		return result;
	}

	public void delete(int bannerId) throws Exception {
		PreparedStatement p = JForum.getConnection().prepareStatement(
				SystemGlobals.getSql("BannerDAO.delete"));
		p.setInt(1, bannerId);
		p.executeUpdate();

		p.close();
	}

	public void update(Banner banner) throws Exception {
		PreparedStatement p = JForum.getConnection().prepareStatement(
				SystemGlobals.getSql("BannerDAO.update"));
		setBannerParam(p, banner);
		p.setInt(13, banner.getId());
		p.executeUpdate();
		p.close();
	}

	public int addNew(Banner banner) throws Exception {
		PreparedStatement p = this.getStatementForAutoKeys("BannerDAO.addNew");
		setBannerParam(p, banner);
		int id = this.executeAutoKeysQuery(p);
		p.close();

		banner.setId(id);
		return id;
	}

	protected void setBannerParam(PreparedStatement p, Banner b)
			throws Exception {
		p.setString(1, b.getName());
		p.setInt(2, b.getPlacement());
		p.setString(3, b.getDescription());
		p.setInt(4, b.getClicks());
		p.setInt(5, b.getViews());
		p.setString(6, b.getUrl());
		p.setInt(7, b.getWeight());
		p.setInt(8, b.isActive() ? 1 : 0);
		p.setString(9, b.getComment());
		p.setInt(10, b.getType());
		p.setInt(11, b.getWidth());
		p.setInt(12, b.getHeight());
	}

	public List selectActiveBannerByPlacement(int placement) throws Exception {
		PreparedStatement p = JForum
				.getConnection()
				.prepareStatement(
						SystemGlobals
								.getSql("BannerDAO.selectActiveBannerByPlacement"));
		p.setInt(1, placement);

		List l = new ArrayList();

		ResultSet rs = p.executeQuery();
		while (rs.next()) {
			l.add(this.getBanner(rs));
		}

		rs.close();
		p.close();

		return l;
	}
}
