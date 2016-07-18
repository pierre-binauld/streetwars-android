package fr.streetgames.streetwars.content.contract;

import fr.streetgames.streetwars.database.Tables;
import fr.streetgames.streetwars.database.TeamColumns;

public interface AddressColumns {

    String ADDRESS = Tables.PLAYER + "_address";

    String ADDRESS_LATITUDE = Tables.PLAYER + "_address_latitude";

    String ADDRESS_LONGITUDE = Tables.PLAYER + "_address_longitude";

    String TEAM_TYPE = TeamColumns.TYPE;

}
