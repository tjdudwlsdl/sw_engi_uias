package com.team8;

public class Meta_DB {
	public static final String driver = "org.gjt.mm.mysql.Driver";
	public static final String db_url = "jdbc:mysql://54.172.53.44/ieas_db";
	public static final String db_name = "ieas_db";
	public static final String db_user = "ieas_user";
	public static final String db_password = "softwareteam8";
	
	public static final String tb_member = "ieas_member";
	public static final String col_mbKey = "mb_no";
	public static final String col_mbID = "mb_id";
	public static final String col_mbHashPasswd = "mb_password";
	public static final String col_mbSalt = "mb_salt";
	public static final String col_mbLocation = "mb_location";
	public static final String col_mbLocalcode = "mb_localcode";
	
	public static final String tb_login = "ieas_login";
	
	public static final String tb_SensorData = "ieas_sensor_data";
}
