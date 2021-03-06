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
 * Created on Jan 13, 2005 5:58:36 PM
 * The JForum Project
 * http://www.jforum.net
 */
package com.ekfans.plugin.cache.engine;

/**
 * 缓存引擎接口
 * 
 * @author
 * @version $Id: CacheEngine.java,v 1.1 2011/04/12 07:52:54 zhj Exp $
 */
public interface CacheEngine {
	public static final String DUMMY_FQN = "";

	public static final String NOTIFICATION = "notification";

	public static final String defaultFqn = "default";

	/**
	 * Inits the cache engine.
	 */
	public void init();

	/**
	 * Stops the cache engine
	 */
	public void stop();

	/**
	 * Adds a new object to the cache. The fqn will be set as the value of
	 * {@link #DUMMY_FQN}
	 * 
	 * @param key
	 *            The key to associate with the object.
	 * @param value
	 *            The object to cache
	 */
	public void add(String key, Object value);

	/**
	 * Set a new object to the cache. The fqn will be set as the value of
	 * {@link #DUMMY_FQN}
	 * 
	 * @param key
	 *            The key to associate with the object.
	 * @param value
	 *            The object to cache
	 */
	public void set(String key, Object value);

	/**
	 * Gets some object from the cache.
	 * 
	 * @param fqn
	 *            The fully qualified name associated with the key
	 * @param key
	 *            The key to get
	 * @return The cached object, or <code>null</code> if no entry was found
	 */
	public Object get(String key);

	/**
	 * Removes an entry from the cache.
	 * 
	 * @param fqn
	 *            The fully qualified name associated with the key
	 * @param key
	 *            The key to remove
	 */
	public void remove(String key);

	/**
	 * Removes a default cache
	 * 
	 * @param key
	 *            The fqn to remove
	 */
	public void removeAll();

	/**
	 * Removes a complete note from the cache
	 * 
	 * @param key
	 *            The fqn to remove
	 */
	public int size(String key);

	/**
	 * Removes a default cache
	 * 
	 * @param key
	 *            The fqn to remove
	 */
	public int size();

	/**
	 * 
	 */
	public String[] getKeys();

}
