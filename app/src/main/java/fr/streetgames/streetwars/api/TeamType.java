package fr.streetgames.streetwars.api;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class TeamType {

    public static final int TEAM_MATE = 0;

    public static final int TARGET = 1;

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({
            TEAM_MATE,
            TARGET
    })
    public @interface Type {
    }
}
