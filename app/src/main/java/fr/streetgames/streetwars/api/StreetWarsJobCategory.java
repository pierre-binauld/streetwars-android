package fr.streetgames.streetwars.api;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public interface StreetWarsJobCategory {

    int WORKER = 0;

    int STUDENT = 1;

    int NO_jOB = 2;

    @IntDef({
            WORKER,
            STUDENT,
            NO_jOB
    })
    @Retention(RetentionPolicy.SOURCE)
    @interface JobCategory {

    }
}
