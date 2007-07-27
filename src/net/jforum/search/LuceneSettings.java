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
 * Created on 23/07/2007 15:58:30
 * 
 * The JForum Project
 * http://www.jforum.net
 */
package net.jforum.search;

import java.io.File;
import java.io.IOException;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.store.RAMDirectory;

/**
 * @author Rafael Steil
 * @version $Id: LuceneSettings.java,v 1.4 2007/07/27 01:32:29 rafaelsteil Exp $
 */
public class LuceneSettings
{
	private Analyzer analyzer;
	private Directory directory;
	private int fragments;
	
	public LuceneSettings(Analyzer analyzer, int fragments)
	{
		this.analyzer = analyzer;
		this.fragments = fragments;
	}
	
	public void useRAMDirectory() throws Exception
	{
		this.directory = new RAMDirectory();
		IndexWriter writer = new IndexWriter(this.directory, this.analyzer, true);
		writer.close();
	}
	
	public void useFSDirectory(String indexWritePath) throws Exception
	{
		File indexDirectory = new File(indexWritePath);
		
		if (!indexDirectory.exists()) {
			this.createIndexDirectory(indexDirectory);
		}
		
		this.directory = FSDirectory.getDirectory(indexDirectory);
	}
	
	public void createIndexDirectory(File directory) throws IOException 
	{
		FSDirectory fsDir = FSDirectory.getDirectory(directory);
		IndexWriter writer = new IndexWriter(fsDir, this.analyzer, true);
		writer.close();
	}
	
	public Directory directory()
	{
		return this.directory;
	}
	
	public Analyzer analyzer()
	{
		return this.analyzer;
	}
	
	public int numberOfFragments()
	{
		return this.fragments;
	}
}