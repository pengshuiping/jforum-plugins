ALTER TABLE jforum_users ADD COLUMN user_authhash VARCHAR(32);
ALTER TABLE jforum_roles DROP COLUMN role_type;
ALTER TABLE jforum_role_values DROP COLUMN role_type;

CREATE TABLE jforum_mail_integration (
	forum_id INT NOT NULL,
	pop_username VARCHAR(100) NOT NULL,
	pop_password VARCHAR(100) NOT NULL,
	pop_host VARCHAR(100) NOT NULL,
	pop_port INT DEFAULT 110,
	KEY(forum_id)
) TYPE=InnoDB;