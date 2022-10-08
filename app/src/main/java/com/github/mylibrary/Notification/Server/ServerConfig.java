package com.github.mylibrary.Notification.Server;

public class ServerConfig {

	// your FCM Advance Push Admin Panel Url
	public static final String BASE_URL = "https://api2.s4apps.in/v1";
	public static final String BASE_URL1 = "https://api2.s4apps.in/cron/Ads_id/";
	public static final String BASE_URLTxt = "https://api2.s4apps.in/Ads/";
	//for Multiple App only
	public static final String APP_TYPE = "ADVANCE_PUSH";

	//=============== DO NOT CHANGE THIS
	public static final String REGISTRATION_URL = "/register"; // Do not change it if you have not customized web Admin panel.
	public static final String UPDATE_DATA_URL = "/updatefield";
	public static final String GET_RECOMMAND = "/recommended";
	public static final String GET_RTO_STATUS = "/get_rcstatus2";
	public static final String GET_AD_ID ="/get_ad_ids2";

	public static final String GET_VIDEOLIST ="/videos";
	public static final String GET_VIDEOTAGS ="/tags";
	public static final String TRENDING_VIDEO ="/trending";
	public static final String ADDLOG_VIDEO ="/user/addlog";
	public static final String GET_REFRESH_TOKEN ="/user/refreshtoken";


	public static final String Get_News_By_Cat_Id ="/news_by_cat_lang_id";


	public static final String NEW_BASE_URL = "http://www.api.s4apps.in/v1";

	public static final String GET_DATA_URL = "/getdata";
	public static final String GET_AADHAAR = "/getadhaar";


	public static final String IFSC_STATE_LIST = "/ifsc_getstates";
	public static final String IFSC_DIST_LIST = "/ifsc_getdistricts";
	public static final String IFSC_BRANCH_LIST = "/ifsc_getdetails";

	//===============

	public static final String  MUTUAL_FUNDS_LIST ="http://www.sensiblemobiles.com/mutual_funds/fundhouse.php";
	public static final String  MUTUAL_FUNDS_LIST_DETAILS ="http://www.sensiblemobiles.com/mutual_funds/fundjson.php?fhouse=";


}
