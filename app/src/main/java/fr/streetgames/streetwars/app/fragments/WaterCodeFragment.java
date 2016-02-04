package fr.streetgames.streetwars.app.fragments;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ShareCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.streetgames.streetwars.R;

import fr.streetgames.streetwars.utils.HTTP;

/**
 * A simple {@link Fragment} subclass.
 */
public class WaterCodeFragment extends Fragment {

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
        View view =  inflater.inflate(R.layout.fragment_water_code, container, false);

        mWaterCodeTextView = (TextView) view.findViewById(R.id.water_code);

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        getActivity().setTitle(R.string.title_water_code);
    }

    private void onShareClick() {
        Resources res = getResources();
        Intent intent = ShareCompat.IntentBuilder.from(getActivity())
                .setType(HTTP.TYPE_PLAIN_TEXT)
                .setSubject(res.getString(R.string.title_water_code))
                .setText(mWaterCodeTextView.getText())
                .getIntent();

        startActivity(Intent.createChooser(intent, getString(R.string.share_with)));

    }
}
