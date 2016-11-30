-- -------------------------------------------------
--
-- Table structure for table 'ieas_login'
--

DROP TABLE IF EXISTS 'ieas_login';
CREATE TABLE IF NOT EXISTS 'ieas_login' (
	'mb_no' int(11) NOT NULL,
	'lo_date' date NOT NULL default '0000-00-00',
	'lo_time' date NOT NULL default '00:00:00',
	PRIMARY KEY ('mb_no')
) DEFAULT CHARSET=utf8;

-- -------------------------------------------------
--
-- Table structure for table 'ieas_member'
--

DROP TABLE IF EXISTS 'ieas_member';
CREATE TABLE IF NOT EXISTS 'ieas_member' (
	'mb_no' int(11) NOT NULL auto_increment,
	'mb_id' varchar(20) NOT NULL default '',
	'mb_password' varchar(255) NOT NULL default '',
	'mb_salt' varchar(255) NOT NULL default '',
	'mb_location' text NOT NULL,
	'mb_location_code' varchar(20) NOT NULL default '0000000000',
	PRIMARY KEY ('mb_no'),
	UNIQUE KEY 'mb_id' ('mb_id')
) DEFAULT CHARSET=utf8;

-- --------------------------------------------------
--
-- Table structure for table 'ieas_device'
--

DROP TABLE IF EXISTS 'ieas_device';
CREATE TABLE IF NOT EXISTS 'ieas_device' (
	'dv_no' int(11) NOT NULL auto_increment,
	'dv_mac' varchar(255) NOT NULL default '',
	'mb_no' int(11) NOT NULL,
	'dv_type' varchar(20) NOT NULL default '',
	PRIMARY KEY ('dv_no'),
	UNIQUE KEY 'dv_mac' ('dv_mac')
) DEFAULT CHARSET=utf8;

-- --------------------------------------------------
--
-- Table structure for table 'ieas_indoor_data'
--

DROP TABLE IF EXISTS 'ieas_indoor_data';
CREATE TABLE IF NOT EXISTS 'ieas_indoor_data' (
	'in_date' date NOT NULL default '0000-00-00',
	'in_time' time NOT NULL default '00:00:00',
	'dv_no' int(11) NOT NULL default -1,
	'mb_no' int(11) NOT NULL default -1,
	'in_temperature' double NOT NULL default 0,
	'in_humidity' double NOT NULL default 0,
	'in_co2' double NOT NULL default 0
) DEFAULT CHARSET=utf8;

-- --------------------------------------------------
--
-- Table structure for table 'ieas_schedule'
--

DROP TABLE IF EXISTS 'ieas_schedule';
CREATE TABLE IF NOT EXISTS 'ieas_schedule' (
	'sc_act_date' date NOT NULL default '0000-00-00',
	'sc_act_time' time NOT NULL default '00:00:00',
	'mb_no' int(11) NOT NULL default -1,
	'dv_no' int(11) NOT NULL default -1,
	'sc_act' tinyint(4) NOT NULL default 0
) DEFAULT CHARSET=utf8;

-- --------------------------------------------------
--
-- Table structure for table 'ieas_reservation'
--

DROP TABLE IF EXISTS 'ieas_reservation';
CREATE TABLE IF NOT EXISTS 'ieas_reservation' (
	'rs_no', int NOT NULL auto_increment,
	'rs_act_date' date NOT NULL default '0000-00-00',
	'rs_act_time' time NOT NULL default '00:00:00',
	'mb_no' int(11) NOT NULL default -1,
	'rs_act' tinyint(4) NOT NULL default 0,
	PRIMARY KEY ('rs_no')
) DEFAULT CHARSET=utf8;

-- --------------------------------------------------
--
-- Table structure for table 'ieas_weather'
--

DROP TABLE IF EXISTS 'ieas_weather';
CREATE TABLE IF NOT EXISTS 'ieas_weather' (
	'we_date' DATE NOT NULL default '0000-00-00',
	'we_location_code' VARCHAR(20) NOT NULL default '0000000000',
	'we_hour' TIME NOT NULL default '00:00:00',
	'we_temperature' DOUBLE NOT NULL,
	'we_humidity' DOUBLE NOT NULL,
	'we_pty' tinyint(4) NOT NULL,
	'we_sky' tinyint(4) NOT NULL,
	'we_pop' tinyint(4) NOT NULL
) DEFAULT CHARSET=utf8;
