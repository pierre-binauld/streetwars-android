package fr.streetgames.streetwars.app.fragments;


import android.content.Context;
import android.support.annotation.CallSuper;
import android.support.v4.app.Fragment;
import android.view.View;

public abstract class FabFragment extends Fragment {

    protected SetupFabButtonListener mSetupFabButtonListener;

    public interface SetupFabButtonListener {
        void setOnFabClickListener(View.OnClickListener listener);
    }

    @CallSuper
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mSetupFabButtonListener = (SetupFabButtonListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement SetupFabButtonListener");
        }
    }
}
