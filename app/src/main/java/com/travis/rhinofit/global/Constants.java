package com.travis.rhinofit.global;

/**
 * Created by Sutan Kasturi on 2/1/2015.
 */
public class Constants {


    // Request
    public static final String kRequestLogin = "login";
    public static final String kRequestGetClasses = "getclasses";
//public static final String kRequestGetUserInfo = "getuserinfo";
    public static final String kRequestMakeReservation = "makereservation";
    public static final String kRequestListReservations = "listreservations";
    public static final String kRequestDeleteReservation = "deletereservation";
    public static final String kRequestGetAttendance = "getattendance";
    public static final String kRequestMakeAttendance = "makeattendance";
    public static final String kRequestDeleteAttendance = "deleteattendance";
    public static final String kRequestMyBenchmarks = "listmybenchmarks";
    public static final String kRequestAvailableBenchmarks = "listavailablebenchmarks";
    public static final String kRequestNewBenchmark = "addbenchmark";
    public static final String kRequestMyMemberships = "listmemberships";
    public static final String kRequestMyBenchmarkData = "listmybenchmarkdata";
    public static final String kRequestDeleteBenchmarkData = "deletebenchmarkdata";
    public static final String kRequestGetWallPosts = "getwallposts";
    public static final String kRequestAddWallPost = "addwallpost";
    public static final String kRequestGetUserInfo = "getuserinfo";
    public static final String kRequestUpdateUserInfo = "updateuserinfo";
    public static final String kRequestGetWodInfo = "getwodinfo";
    public static final String kRequestTrackWod = "trackwod";
    public static final String kRequestGetMyWods = "getmywods";
    public static final String kRequestGetCountries = "getcountries";
    public static final String kRequestGetStates = "getstates";

// Messages
    public static final String kMessageInvalidLogin = "Invalid Login";
    public static final String kMessageMakeReservation = "Make a reservation for this class?";
    public static final String kMessageCancelReservation = "Are you sure you want to cancel this reservation?";
    public static final String kMessageMarkAttendance = "Are you sure you want to mark this class as attended?";
    public static final String kMessageCancelAttendance = "Are you sure you want to cancel this attendance?";
    public static final String kMessageNoClasses = "There are no classes scheduled for this day";
    public static final String kMessageIsReservation = "You are reserved in this class";
    public static final String kMessageIsAttended = "You have attended this class";
    public static final String kMessageIsReservationAndAttended = "You have reserved and attended this class";
    public static final String kMessageUnkownError = "Unkown Error";
    public static final String kMessageNoReservations = "You currently have no upcoming reservations.";
    public static final String kMessageNoAttendance = "You have not attended any classes in this month.";
    public static final String kMessageNoMyBenchmarks = "You currently have no benchmarks";
    public static final String kMessageDeleteBenchmark = "Are you sure you want to delete this entry?";
    public static final String kMessageNoMyMemberships = "You currently have no memberships";
    public static final String kMessageNoAvailableBenchmarks = "There are no available benchmarks";
    public static final String kMessageNoMyBenchmarkHistories = "There are no benchmark histories";
    public static final String kMessageNoWalls = "There are no wall posts at this time, post one today!";
    public static final String kMessageNoMyWods = "You have not yet tracked any WODs. To track a WOD, find a class you have attended in \"My Classes\" and click \"Track WOD\"";

// Button Names

    public static final String kButtonMakeReservation = "Make Reservation";
    public static final String kButtonCancelReservation = "Cancel Reservation";
    public static final String kButtonMakeAttendance = "Mark Attended";
    public static final String kButtonCancelAttendance = "Cancel Attendance";

// Globals
    public static final String kRhinoFitUserToken = "com.travis.rhinofit.token";
    public static final String kRhinoFitUserEmail = "com.travis.rhinofit.email";

// Request Params
    public static final String kParamAction = "action";
    public static final String kParamEmail = "email";
    public static final String kParamPassword = "password";
    public static final String kParamToken = "token";
    public static final String kParamStartDate = "startdate";
    public static final String kParamEndDate = "enddate";
    public static final String kParamClassTimeId = "classtimeid";
    public static final String kParamDate = "date";
    public static final String kParamResId = "resid";
    public static final String kParamAId = "aid";

    public static final String kParamStart = "start";
    public static final String kParamWod = "wod";
    public static final String kParamTitle = "title";
    public static final String kParamResults = "results";

    public static final String kParamBId = "bid";
    public static final String kParamBDesc = "bdesc";
    public static final String kParamBType = "btype";
    public static final String kParamBBestDate = "bbestdate";
    public static final String kParamBBestResults = "bbestresults";
    public static final String kParamBLastDate = "blastdate";
    public static final String kParamBLastResults = "blastresults";
    public static final String kParamBFormat = "bformat";
    public static final String kParamId = "id";
    public static final String kParamValue = "value";
    public static final String kParamDataId = "dataid";

    public static final String kParamMId = "mid";
    public static final String kParamMName = "mname";
    public static final String kParamMStartDate = "mstart";
    public static final String kParamMEndDate = "mend";
    public static final String kParamMRenewal = "mrenewal";
    public static final String kParamMAttended = "mattended";
    public static final String kParamMAttendedLimit = "mattendedlimit";
    public static final String kParamMLimit = "mlimit";

    public static final String kParamMSG = "msg";
    public static final String kParamFile = "file";

    public static final String kParamUserFirstName = "u_first";
    public static final String kParamUserLastName = "u_last";
    public static final String kParamUserAddress1 = "u_address1";
    public static final String kParamUserAddress2 = "u_address2";
    public static final String kParamUserCity = "u_city";
    public static final String kParamUserState = "u_state";
    public static final String kParamUserZip = "u_zip";
    public static final String kParamUserCountry = "u_country";
    public static final String kParamUserPhone1 = "u_phone1";
    public static final String kParamUserPhone2 = "u_phone2";
    public static final String kParamUserName = "u_username";
    public static final String kParamCountry = "country";

// CoreData Tables
    public static final String kCoreDataUserInfo = "UserInfo";
    public static final String kCoreDataClass = "RhinoFitClass";
    public static final String kCoreDataReservation = "Reservation";
    public static final String kCoreDataAttendance = "Attendance";

//////////////////////////////////////////////
//          Response Keys                   //
//////////////////////////////////////////////

// login
    public static final String kResponseKeyError = "error";
    public static final String kResponseKeyToken = "token";

// getclasses
    public static final String kResponseKeyStartDate = "start";
    public static final String kResponseKeyEndDate = "end";
    public static final String kResponseKeyTitle = "title";
    public static final String kResponseKeyAllDay = "allDay";
    public static final String kResponseKeyColor = "color";
    public static final String kResponseKeyOrigColor = "origcolor";
    public static final String kResponseKeyReservation = "reservation";
    public static final String kResponseKeyInstructorId = "instructorid";
    public static final String kResponseKeyInstructorName = "instructorname";
    public static final String kResponseKeyClassId = "id";
    public static final String kResponseKeyDay = "day";

// getwodinfo
    public static final String kResponseKeyWodCanEdit = "canedit";
    public static final String kResponseKeyWodId = "id";
    public static final String kResponseKeyWodName = "name";
    public static final String kResponseKeyWodResults = "results";
    public static final String kResponseKeyWodStart = "start";
    public static final String kResponseKeyWodTitle = "title";
    public static final String kResponseKeyWodWod = "wod";
    public static final String kResponseKeyWodWodId = "wodid";

// getuserinfo
    public static final String kResponseKeyUserAddress1 = "u_address1";
    public static final String kResponseKeyUserAddress2 = "u_address2";
    public static final String kResponseKeyUserCity = "u_city";
    public static final String kResponseKeyUserCountry = "u_country";
    public static final String kResponseKeyUserFirstName = "u_first";
    public static final String kResponseKeyUserLastName = "u_last";
    public static final String kResponseKeyUserPhone1 = "u_phone1";
    public static final String kResponseKeyUserPhone2 = "u_phone2";
    public static final String kResponseKeyUserState = "u_state";
    public static final String kResponseKeyUserZip = "u_zip";
    public static final String kResponseKeyUserEmail = "u_email";
    public static final String kResponseKeyUserName = "u_username";
    public static final String kResponseKeyUserPicture = "u_photo";

// reservation
    public static final String kResponseKeyReservationId = "resid";
    public static final String kResponseKeyReservationTitle = "title";
    public static final String kResponseKeyReservationWhen = "when";

// attendance
    public static final String kResponseKeyAttendanceId = "aid";
    public static final String kResponseKeyAttendanceTitle = "title";
    public static final String kResponseKeyAttendanceWhen = "when";

// wall
    public static final String kResponseKeyWallId = "id";
    public static final String kResponseKeyWallMessage = "msg";
    public static final String kResponseKeyWallUserName = "name";
    public static final String kResponseKeyWallPic = "pic";
    public static final String kResponseKeyWallUserPicture = "profilepic";
    public static final String kResponseKeyWallYours = "yours";

// result
    public static final String kResponseKeyResult = "result";

// Notification

    public static final String kNotificationGetUserInfo = "get_user_info";
    public static final String kNotificationUpdateProfile = "update_profile";
    public static final String kNotificationMyWods = "my_wods";
}
