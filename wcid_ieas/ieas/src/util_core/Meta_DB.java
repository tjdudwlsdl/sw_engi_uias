package util_core;

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
	public static final String col_mbAuto = "mb_auto";
	
	public static final String tb_device = "ieas_device";
	public static final String col_dvKey = "dv_no";
	public static final String col_dvID = "dv_id";
	public static final String col_dvType = "dv_type";
	
	public static final String tb_login = "ieas_login";
	
	public static final String tb_SensorData = "ieas_sensor_data";
	
	public static final String tb_ControllerData = "ieas_controller_data";
	
	public static final String tb_weather = "ieas_weather";
	public static final String col_wePty = "we_pty";
	public static final String col_weSky = "we_sky";
	public static final String col_weLocalcode = "we_localcode";
	
	public static final String tb_schedule = "ieas_schedule";
	public static final String col_scAct = "sc_act";
	
	public static final String tb_reservation = "ieas_reservation";
	public static final String col_rsID = "rs_id";
	public static final String col_rsActDate = "rs_act_date";
	public static final String col_rsActTime = "rs_act_time";
	public static final String col_rsAct = "rs_act";
}
