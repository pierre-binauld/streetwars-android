package fr.streetgames.streetwars.widget.behavior;

import android.content.Context;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class MapsFloatingActionButtonBehavior
        extends FloatingActionButton.Behavior {

    private static final String TAG = "MapsFABBehavior";

    public MapsFloatingActionButtonBehavior() {
        super();
    }

    public MapsFloatingActionButtonBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, FloatingActionButton child, View dependency) {
        Log.d(TAG, "layoutDependsOn: " + dependency);
        Log.d(TAG, "layoutDependsOn: " + (dependency instanceof TextView));
        return super.layoutDependsOn(parent, child, dependency);
    }
}
