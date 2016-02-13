package fr.streetgames.streetwars.app.fragments;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.ShareCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.streetgames.streetwars.R;

import fr.streetgames.streetwars.utils.HTTP;


public class WaterCodeFragment extends FabFragment implements View.OnClickListener {

    public static final String TAG = "WaterCodeFragment";

    private TextView mWaterCodeTextView;

    public static WaterCodeFragment newInstance() {
        WaterCodeFragment fragment = new WaterCodeFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_water_code, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        mWaterCodeTextView = (TextView) view.findViewById(R.id.water_code);


    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getActivity().setTitle(R.string.water_code_title);
        mSetupFabButtonListener.setOnFabClickListener(this);

    }

    private void onShareClick() {
        Resources res = getResources();
        Intent intent = ShareCompat.IntentBuilder.from(getActivity())
                .setType(HTTP.TYPE_PLAIN_TEXT)
                .setSubject(res.getString(R.string.water_code_title))
                .setText(mWaterCodeTextView.getText())
                .getIntent();

        startActivity(Intent.createChooser(intent, getString(R.string.share_with)));

    }

    @Override
    public void onClick(View v) {
        @IdRes int id = v.getId();
        switch (id) {
            case R.id.fab:
                onShareClick();
                break;
            default:
                throw new IllegalArgumentException(String.format("%s is not a valid id", id));
        }
    }
}
