package fr.streetgames.streetwars.app.fragments;

import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.v4.app.LoaderManager;
import android.support.v4.app.ShareCompat;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.streetgames.streetwars.R;

import fr.streetgames.streetwars.utils.HTTP;

import static fr.streetgames.streetwars.content.contract.StreetWarsContract.*;


public class WaterCodeFragment extends FabFragment implements View.OnClickListener, LoaderManager.LoaderCallbacks<Cursor> {

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

        LoaderManager loaderManager = getLoaderManager();
        if (null != loaderManager.getLoader(R.id.loader_query_water_code)) {
            loaderManager.initLoader(R.id.loader_query_water_code, null, this);
        }
        else {
            loaderManager.initLoader(R.id.loader_query_water_code, null, this);
        }
    }

    private void onShareClick() {
        Resources res = getResources();
        Intent intent = ShareCompat.IntentBuilder.from(getActivity())
                .setType(HTTP.TYPE_PLAIN_TEXT)
                .setSubject(res.getString(R.string.water_code_title))
                .setText(mWaterCodeTextView.getText().toString())
                .getIntent();

        startActivity(Intent.createChooser(intent, getString(R.string.share_water_code)));
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

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        switch (id) {
            case R.id.loader_query_water_code:
                return new CursorLoader(
                        getContext(),
                        Player.CONTENT_URI,
                        new String[] {Player.WATER_CODE},
                        null,
                        null,
                        null
                );
            default:
                throw new IllegalArgumentException(String.format("Unknown loader id: %s", id));
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        @IdRes int id = loader.getId();
        switch (id) {
            case R.id.loader_query_water_code:
                onWaterCodeLoadFinished(cursor);
                break;
            default:
                throw new IllegalArgumentException(String.format("Unknown loader id: %s", id));
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        @IdRes int id = loader.getId();
        switch (id) {
            case R.id.loader_query_water_code:
                // Tu bluffes, Martoni !
                break;
            default:
                throw new IllegalArgumentException(String.format("Unknown loader id: %s", id));
        }
    }

    private void onWaterCodeLoadFinished(@NonNull Cursor cursor) {
        if (cursor.moveToFirst()) {
            String waterCode = cursor.getString(0);
            mWaterCodeTextView.setText(waterCode);
        }
    }

}
