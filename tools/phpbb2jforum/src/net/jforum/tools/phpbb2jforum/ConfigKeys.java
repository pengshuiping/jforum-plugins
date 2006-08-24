package net.jforum.tools.phpbb2jforum;

/**
* Encapsulate all configuration keys in constants. This is more typesafe and provides
* a nice overview of all configuration keys. Last but not least this lets us autocomplete
* configuration keys under eclipse ;-)
* 
* @author Rafael Steil
* @version $Id: ConfigKeys.java,v 1.1 2006/08/20 22:47:41 rafaelsteil Exp $
*/

public class ConfigKeys {
	private ConfigKeys() { }

	public static final String INSTALLATION = "installation";

	public static final String INSTALLATION_CONFIG = "installation.config";
	public static final String DEFAULT_CONFIG = "default.config";

	public static final String RESOURCE_DIR = "resource.dir";
	public static final String CONFIG_DIR = "config.dir";
	
	public static final String DATABASE_DRIVER = "database.driver";
	public static final String DATABASE_JFORUM_URL = "database.jforum.url";
	public static final String DATABASE_QUERIES = "database.queries";
	public static final String DBNAME = "dbname";
	public static final String DBUSER = "dbuser";
	public static final String DBPASSWD = "dbpasswd";
	public static final String DBHOST = "dbhost";
	
	public static final String B_REGEX = "b.regex";
	public static final String U_REGEX = "u.regex";
	public static final String I_REGEX = "i.regex";
	public static final String QUOTE_REGEX = "quote.regex";
	public static final String QUOTE_USERNAME_OPEN_REGEX = "quote.username.open.regex";
	public static final String QUOTE_USERNAME_CLOSE_REGEX = "quote.username.close.regex";
	public static final String LIST_REGEX = "list.regex";
	public static final String COLOR_REGEX = "color.regex";
	public static final String SIZE_REGEX = "size.regex";
	public static final String IMG_REGEX = "img.regex";
	public static final String CODE_REGEX = "code.regex";
	
	public static final String B_REPLACE = "b.replace";
	public static final String I_REPLACE = "i.replace";
	public static final String U_REPLACE = "u.replace";
	public static final String QUOTE_REPLACE = "quote.replace";
	public static final String QUOTE_USERNAME_OPEN_REPLACE = "quote.username.open.replace";
	public static final String QUOTE_USERNAME_CLOSE_REPLACE = "quote.username.close.replace";
	public static final String LIST_REPLACE = "list.replace";
	public static final String COLOR_REPLACE = "color.replace";
	public static final String SIZE_REPLACE = "size.replace";
	public static final String IMG_REPLACE = "img.replace";
	public static final String CODE_REPLACE = "code.replace";
	
	public static final String QUERY_PRIVMSGS = "query.privmsgs";
	public static final String QUERY_PRIVMSGS_TEXT = "query.privmsgs.text";
	public static final String QUERY_CATEGORIES = "query.categories";
	public static final String QUERY_FORUMS = "query.forums";
	public static final String QUERY_POSTS = "query.posts";
	public static final String QUERY_POSTS_TEXT = "query.posts.text";
	public static final String QUERY_RANKS = "query.ranks";
	public static final String QUERY_SEARCH_WORDS = "query.search.words";
	public static final String QUERY_SEARCH_WORDMATCH = "query.search.wordmatch";
	public static final String QUERY_TOPICS = "query.topics";
	public static final String QUERY_TOPICS_WATCH = "query.topics.watch";
	public static final String QUERY_USERS = "query.users";
	public static final String QUERY_WORDS = "query.words";

	public static final String QUERY_TOTAL_POSTS = "query.totalposts";
	public static final String QUERY_SELECT_POSTS_TEXT = "query.select.poststext";
	public static final String QUERY_SELECT_PM = "query.select.pm";
	public static final String QUERY_SELECT_USERS = "query.select.users";
	
	public static final String QUERY_UPDATE_ANONYMOUS = "query.update.anonymous";
	
	public static final String QUERY_CLEAN_CATEGORIES = "query.clean.categories";
	public static final String QUERY_CLEAN_FORUMS = "query.clean.forums";
	public static final String QUERY_CLEAN_POSTS = "query.clean.posts";
	public static final String QUERY_CLEAN_POSTS_TEXT = "query.clean.posts.text";
	public static final String QUERY_CLEAN_PRIVMSGS = "query.clean.privmsgs";
	public static final String QUERY_CLEAN_PRIVMSGS_TEXT = "query.clean.privmsgs.text";
	public static final String QUERY_CLEAN_RANKS = "query.clean.ranks";
	public static final String QUERY_CLEAN_SEARCH_WORDS = "query.clean.search.words";
	public static final String QUERY_CLEAN_SEARCH_WORDMATCH = "query.clean.search.wordmatch";
	public static final String QUERY_CLEAN_TOPICS = "query.clean.topics";
	public static final String QUERY_CLEAN_TOPICS_WATCH = "query.clean.topicswatch";
	public static final String QUERY_CLEAN_USERS = "query.clean.users";
	public static final String QUERY_CLEAN_WORDS = "query.clean.words";
}