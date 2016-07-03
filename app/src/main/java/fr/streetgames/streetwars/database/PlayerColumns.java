package fr.streetgames.streetwars.database;

public interface PlayerColumns {

    String ID = Tables.PLAYER + "_id";

    String ALIAS = Tables.PLAYER + "_alias";

    String FIRST_NAME = Tables.PLAYER + "_first_name";

    String LAST_NAME = Tables.PLAYER + "_last_name";

    String IS_ME = Tables.PLAYER + "_is_me";

    String PHOTO = Tables.PLAYER + "_photo";

    String WATER_CODE = Tables.PLAYER + "_water_code";

    String KILL_COUNT = Tables.PLAYER + "_kill_count";

    String TEAM_ID = Tables.PLAYER + "_team_id";

    String HOME = Tables.PLAYER + "_home";

    String HOME_LATITUDE = Tables.PLAYER + "_home_latitude";

    String HOME_LONGITUDE = Tables.PLAYER + "_home_longitude";

    String WORK = Tables.PLAYER + "_work";

    String WORK_LATITUDE = Tables.PLAYER + "_work_latitude";

    String WORK_LONGITUDE = Tables.PLAYER + "_work_longitude";

    String JOB_CATEGORY = Tables.PLAYER + "_job_category";

    String EXTRA = Tables.PLAYER + "_extra";
}
