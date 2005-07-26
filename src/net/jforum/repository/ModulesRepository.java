/*
 * Copyright (c) 2003, 2004 Rafael Steil
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
 * Created on 29/11/2004 22:53:28
 * The JForum Project
 * http://www.jforum.net
 */
package net.jforum.repository;

import java.io.IOException;
import java.util.Properties;

import net.jforum.ConfigLoader;
import net.jforum.JForum;
import net.jforum.cache.CacheEngine;
import net.jforum.cache.Cacheable;

import org.apache.log4j.Logger;

/**
 * @author Rafael Steil
 * @version $Id: ModulesRepository.java,v 1.4 2005/07/15 04:31:06 rafaelsteil
 *          Exp $
 */
public class ModulesRepository implements Cacheable {
	private static final Logger logger = Logger
			.getLogger(ModulesRepository.class);

	private static CacheEngine cache;

	private static final String FQN = "modulesMapping";

	private static final String ENTRIES = "entries";

	/**
	 * @see net.jforum.cache.Cacheable#setCacheEngine(net.jforum.cache.CacheEngine)
	 */
	public void setCacheEngine(CacheEngine engine) {
		cache = engine;
	}

	/**
	 * Loads all modules mapping.
	 * 
	 * @param baseDir
	 *            The directory where the file "modulesMapping.properties" is
	 *            placed.
	 * @throws IOException
	 */
	public static void init(String baseDir) throws IOException {
		cache.add(FQN, ENTRIES, ConfigLoader.loadModulesMapping(baseDir));
	}

	public static int size() {
		return ((Properties) cache.get(FQN, ENTRIES)).size();
	}

	/**
	 * Gets the fully qualified name of some given module name.
	 * 
	 * @param moduleName
	 *            The module's name to get its class name
	 * @return The class name associated to the module name passed as argument,
	 *         or <code>null</code> if not found.
	 */
	public static String getModuleClass(String moduleName) {
		Properties p = (Properties) cache.get(FQN, ENTRIES);

		if (p == null) {
			logger.error("Null modules. Askes moduleName: " + moduleName
					+ ", url=" + JForum.getRequest().getQueryString());
			return null;
		}

		return p.getProperty(moduleName);
	}
}
