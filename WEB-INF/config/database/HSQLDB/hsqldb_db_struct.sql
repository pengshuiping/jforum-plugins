-- MySQL dump 9.07
--
-- Host: localhost    Database: jforum
---------------------------------------------------------
-- Server version	4.0.12-nt

--
-- Table structure for table 'jforum_banlist'
--

DROP TABLE IF EXISTS jforum_banlist;
CREATE TABLE jforum_banlist (
  banlist_id int NOT NULL IDENTITY,
  user_id int default '0' NOT NULL,
  banlist_ip varchar(8) default '' NOT NULL,
  banlist_email varchar(255) default NULL,
  PRIMARY KEY  (banlist_id)
) ;

CREATE INDEX idx_banlist_user ON jforum_banlist(user_id);

--
-- Table structure for table 'jforum_categories'
--

DROP TABLE IF EXISTS jforum_categories;
CREATE TABLE jforum_categories (
  categories_id int NOT NULL identity,
  title varchar(100) default '' NOT NULL ,
  display_order int default 0 NOT NULL,
  PRIMARY KEY  (categories_id)
) ;

--
-- Table structure for table 'jforum_config'
--

DROP TABLE IF EXISTS jforum_config;
CREATE TABLE jforum_config (
  config_name varchar(255) default '' NOT NULL,
  config_value varchar(255) default '' NOT NULL,
  config_id int not null identity,
  PRIMARY KEY(config_id)
) ;

--
-- Table structure for table 'jforum_forums'
--

DROP TABLE IF EXISTS jforum_forums;
CREATE TABLE jforum_forums (
  forum_id int NOT NULL identity,
  categories_id int default '1' NOT NULL,
  forum_name varchar(150) default '' NOT NULL,
  forum_desc varchar(255) default NULL,
  forum_order int default '1',
  forum_topics int default '0' NOT NULL,
  forum_last_post_id int default '0' NOT NULL,
  moderated int default '0',
  PRIMARY KEY  (forum_id)
) ;
CREATE INDEX idx_forums_categories_id ON jforum_forums(categories_id);

--
-- Table structure for table 'jforum_groups'
--

DROP TABLE IF EXISTS jforum_groups;
CREATE TABLE jforum_groups (
  group_id int NOT NULL identity,
  group_name varchar(40) default '' NOT NULL,
  group_description varchar(255) default NULL,
  parent_id int default '0',
  PRIMARY KEY  (group_id)
) ;


DROP TABLE IF EXISTS jforum_user_groups;
CREATE TABLE jforum_user_groups (
	group_id INT NOT NULL,
	user_id INT NOT NULL
) ;
CREATE INDEX idx_ug_group ON jforum_user_groups(group_id);
CREATE INDEX idx_ug_user ON jforum_user_groups(user_id);

--
-- Table structure for table 'jforum_roles'
--

DROP TABLE IF EXISTS jforum_roles;
CREATE TABLE jforum_roles (
  role_id INT NOT NULL identity,
  group_id int default '0',
  user_id int default '0',
  name varchar(255) NOT NULL,
  type int DEFAULT 1,
  PRIMARY KEY (role_id)
) ;
CREATE INDEX idx_roles_group ON jforum_roles(group_id);
CREATE INDEX idx_roles_user ON jforum_roles(user_id);
CREATE INDEX idx_roles_name ON jforum_roles(name);

--
-- Table structure for table 'jforum_role_values'
--
DROP TABLE IF EXISTS jforum_role_values;
CREATE TABLE jforum_role_values (
  role_id INT NOT NULL,
  value VARCHAR(255),
  type int DEFAULT 1
) ;
CREATE INDEX idx_rv_role ON jforum_role_values(role_id);

--
-- Table structure for table 'jforum_posts'
--

DROP TABLE IF EXISTS jforum_posts;
CREATE TABLE jforum_posts (
  post_id int NOT NULL identity,
  topic_id int default '0' NOT NULL,
  forum_id int default '0' NOT NULL,
  user_id int default NULL,
  post_time varchar(13) default NULL,
  poster_ip varchar(15) default NULL,
  enable_bbcode int default '1' NOT NULL,
  enable_html int default '1' NOT NULL,
  enable_smilies int default '1' NOT NULL,
  enable_sig int default '1' NOT NULL,
  post_edit_time varchar(13) default NULL,
  post_edit_count int default '0' NOT NULL,
  status int default '1',
  PRIMARY KEY  (post_id)
) ;
CREATE INDEX idx_posts_user ON jforum_posts(user_id);
CREATE INDEX idx_posts_topic ON jforum_posts(topic_id);
CREATE INDEX idx_posts_forum ON jforum_posts(forum_id);


--
-- Table structure for table 'jforum_posts_text'
--
DROP TABLE IF EXISTS jforum_posts_text;
CREATE TABLE jforum_posts_text (
	post_id int NOT NULL PRIMARY KEY,
	post_text LONGVARCHAR,
	post_subject VARCHAR(100)
) ;

--
-- Table structure for table 'jforum_privmsgs'
--

DROP TABLE IF EXISTS jforum_privmsgs;
CREATE TABLE jforum_privmsgs (
  privmsgs_id int NOT NULL identity,
  privmsgs_type int default '0' NOT NULL,
  privmsgs_subject varchar(255) default '' NOT NULL,
  privmsgs_from_userid int default '0' NOT NULL,
  privmsgs_to_userid int default '0' NOT NULL,
  privmsgs_date varchar(13) default '0' NOT NULL,
  privmsgs_ip varchar(8) default '' NOT NULL,
  privmsgs_enable_bbcode int default '1' NOT NULL,
  privmsgs_enable_html int default '0' NOT NULL,
  privmsgs_enable_smilies int default '1' NOT NULL,
  privmsgs_attach_sig int default '1' NOT NULL,
  PRIMARY KEY  (privmsgs_id)
) ;

DROP TABLE IF EXISTS jforum_privmsgs_text;
CREATE TABLE jforum_privmsgs_text (
	privmsgs_id int NOT NULL,
	privmsgs_text LONGVARCHAR,
	PRIMARY KEY ( privmsgs_id )
) ;

--
-- Table structure for table 'jforum_ranks'
--

DROP TABLE IF EXISTS jforum_ranks;
CREATE TABLE jforum_ranks (
  rank_id int NOT NULL identity,
  rank_title varchar(50) default '' NOT NULL,
  rank_min int default '0' NOT NULL,
  rank_special int default NULL,
  rank_image varchar(255) default NULL,
  PRIMARY KEY  (rank_id)
) ;

--
-- Table structure for table 'jforum_sessions'
--

DROP TABLE IF EXISTS jforum_sessions;
CREATE TABLE jforum_sessions (
  session_id varchar(50) default '' NOT NULL,
  session_user_id int default '0' NOT NULL,
  session_start varchar(13) default '0' NOT NULL,
  session_time varchar(13) default '0' NOT NULL,
  session_ip varchar(8) default '' NOT NULL,
  session_page int default '0' NOT NULL,
  session_logged_int int default NULL
) ;

--
-- Table structure for table 'jforum_smilies'
--

DROP TABLE IF EXISTS jforum_smilies;
CREATE TABLE jforum_smilies (
  smilie_id int NOT NULL identity,
  code varchar(50)default '' NOT NULL ,
  url varchar(100) default NULL,
  disk_name varchar(255),
  PRIMARY KEY  (smilie_id)
) ;

--
-- Table structure for table 'jforum_themes'
--

DROP TABLE IF EXISTS jforum_themes;
CREATE TABLE jforum_themes (
  themes_id int NOT NULL identity,
  template_name varchar(30) default '' NOT NULL,
  style_name varchar(30) default '' NOT NULL,
  PRIMARY KEY  (themes_id)
) ;

--
-- Table structure for table 'jforum_topics'
--

DROP TABLE IF EXISTS jforum_topics;
CREATE TABLE jforum_topics (
  topic_id int NOT NULL identity,
  forum_id int default '0' NOT NULL,
  topic_title varchar(100) default '' NOT NULL,
  user_id int default '0' NOT NULL,
  topic_time varchar(13) default '0' NOT NULL,
  topic_views int default '1',
  topic_replies int default '0',
  topic_status int default '0',
  topic_vote int default '0',
  topic_type int default '0',
  topic_first_post_id int default '0',
  topic_last_post_id int default '0' NOT NULL,
  moderated int default '0',
  PRIMARY KEY  (topic_id)
) ;
CREATE INDEX idx_topics_forum ON jforum_topics(forum_id);
CREATE INDEX idx_topics_user ON jforum_topics(user_id);
CREATE INDEX idx_topics_fp ON jforum_topics(topic_first_post_id);
CREATE INDEX idx_topics_lp ON jforum_topics(topic_last_post_id);

--
-- Table structure for table 'jforum_topics_watch'
--

DROP TABLE IF EXISTS jforum_topics_watch;
CREATE TABLE jforum_topics_watch (
  topic_id int default '0' NOT NULL,
  user_id int default '0' NOT NULL,
  is_read int default '0' NOT NULL
) ;
CREATE INDEX idx_tw_topic ON jforum_topics_watch(topic_id);
CREATE INDEX idx_tw_user ON jforum_topics_watch(user_id);

--
-- Table structure for table 'jforum_users'
--

DROP TABLE IF EXISTS jforum_users;
CREATE TABLE jforum_users (
  user_id int NOT NULL identity,
  user_active int default NULL,
  username varchar(50) default '' NOT NULL,
  user_password varchar(32) default '' NOT NULL,
  user_session_time varchar(13) default '0' NOT NULL,
  user_session_page int default '0' NOT NULL,
  user_lastvisit int default '0' NOT NULL,
  user_regdate varchar(13) default '0' NOT NULL,
  user_level int default NULL,
  user_posts int default '0' NOT NULL,
  user_timezone varchar(5) default '' NOT NULL,
  user_style int default NULL,
  user_lang varchar(255) default '' NOT NULL,
  user_dateformat varchar(20) default '%d/%M/%Y %H:%i' NOT NULL,
  user_new_privmsg int default '0' NOT NULL,
  user_unread_privmsg int default '0' NOT NULL,
  user_last_privmsg int default '0' NOT NULL,
  user_emailtime int default NULL,
  user_viewemail int default '0',
  user_attachsig int default '1',
  user_allowhtml int default '0',
  user_allowbbcode int default '1',
  user_allowsmilies int default '1',
  user_allowavatar int default '1',
  user_allow_pm int default '1',
  user_allow_viewonline int default '1',
  user_notify int default '1',
  user_notify_pm int default '1',
  user_popup_pm int default '1',
  rank_id int default '1',
  user_avatar varchar(100) default NULL,
  user_avatar_type int default '0' NOT NULL,
  user_email varchar(255) default '' NOT NULL,
  user_icq varchar(15) default NULL,
  user_website varchar(100) default NULL,
  user_from varchar(100) default NULL,
  user_sig longvarchar,
  user_sig_bbcode_uid varchar(10) default NULL,
  user_aim varchar(255) default NULL,
  user_yim varchar(255) default NULL,
  user_msnm varchar(255) default NULL,
  user_occ varchar(100) default NULL,
  user_interests varchar(255) default NULL,
  user_actkey varchar(32) default NULL,
  gender char(1) default NULL,
  themes_id int default NULL,
  deleted int default NULL,
  user_viewonline int default '1',
  security_hash varchar(32),
  PRIMARY KEY  (user_id)
) ;


--
-- Table structure for table 'jforum_vote_desc'
--

DROP TABLE IF EXISTS jforum_vote_desc;
CREATE TABLE jforum_vote_desc (
  vote_id int NOT NULL identity,
  topic_id int default '0' NOT NULL,
  vote_text longvarchar NOT NULL,
  vote_start int default '0' NOT NULL,
  vote_length int default '0' NOT NULL,
  PRIMARY KEY  (vote_id)
) ;

--
-- Table structure for table 'jforum_vote_results'
--

DROP TABLE IF EXISTS jforum_vote_results;
CREATE TABLE jforum_vote_results (
  vote_id int default '0' NOT NULL,
  vote_option_id int default '0' NOT NULL,
  vote_option_text varchar(255) default '' NOT NULL,
  vote_result int default '0' NOT NULL
) ;

--
-- Table structure for table 'jforum_vote_voters'
--

DROP TABLE IF EXISTS jforum_vote_voters;
CREATE TABLE jforum_vote_voters (
  vote_id int default '0' NOT NULL,
  vote_user_id int default '0' NOT NULL,
  vote_user_ip char(8) default '' NOT NULL
) ;

--
-- Table structure for table 'jforum_words'
--

DROP TABLE IF EXISTS jforum_words;
CREATE TABLE jforum_words (
  word_id int NOT NULL identity,
  word varchar(100) default '' NOT NULL,
  replacement varchar(100) default '' NOT NULL,
  PRIMARY KEY  (word_id)
) ;

--
-- Table structure for table 'jforum_search_words'
--
DROP TABLE IF EXISTS jforum_search_words;
CREATE TABLE jforum_search_words (
  word_id INT NOT NULL identity,
  word VARCHAR(100) NOT NULL,
  word_hash INT
) ;
CREATE INDEX idx_sw_word ON jforum_search_words(word);
CREATE INDEX idx_sw_hash ON jforum_search_words(word_hash);

-- 
-- Table structure for table 'jforum_search_wordmatch'
--
DROP TABLE IF EXISTS jforum_search_wordmatch;
CREATE TABLE jforum_search_wordmatch (
  post_id INT NOT NULL,
  word_id INT NOT NULL,
  title_match int DEFAULT '0'
) ;
CREATE INDEX idx_swm_post ON jforum_search_wordmatch(post_id);
CREATE INDEX idx_swm_word ON jforum_search_wordmatch(word_id);
CREATE INDEX idx_swm_title ON jforum_search_wordmatch(title_match);

--
-- Table structure for table 'jforum_search_results'
--
DROP TABLE IF EXISTS jforum_search_results;
CREATE TABLE jforum_search_results (
  topic_id INT NOT NULL,
  session VARCHAR(50),
  time DATETIME
) ;
CREATE INDEX idx_sr_topic ON jforum_search_results(topic_id);


DROP TABLE IF EXISTS jforum_search_topics;
CREATE TABLE jforum_search_topics (
  topic_id int NOT NULL,
  forum_id int default '0' NOT NULL,
  topic_title varchar(60) default '' NOT NULL,
  user_id int default '0' NOT NULL,
  topic_time varchar(13) default '0' NOT NULL,
  topic_views int default '1',
  topic_replies int default '0',
  topic_status int default '0',
  topic_vote int default '0',
  topic_type int default '0',
  topic_first_post_id int default '0',
  topic_last_post_id int default '0' NOT NULL,
  moderated int default '0',
  session varchar(50),
  time datetime
) ;
CREATE INDEX idx_st_topic ON jforum_search_topics(topic_id);
CREATE INDEX idx_st_forum ON jforum_search_topics(forum_id);
CREATE INDEX idx_st_user ON jforum_search_topics(user_id);
CREATE INDEX idx_st_fp ON jforum_search_topics(topic_first_post_id);
CREATE INDEX idx_st_lp ON jforum_search_topics(topic_last_post_id);