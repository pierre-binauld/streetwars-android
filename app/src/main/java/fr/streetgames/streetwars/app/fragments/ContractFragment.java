package fr.streetgames.streetwars.app.fragments;

import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import fr.streetgames.streetwars.R;
import fr.streetgames.streetwars.content.contract.StreetWarsContract;
import fr.streetgames.streetwars.widget.TargetAdapter;

/**
 * A placeholder fragment containing a simple view.
 */
public class ContractFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    public static final String TAG = "ContractFragment";

    private RecyclerView mRecyclerView;

    private TargetAdapter mAdapter;

    public ContractFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_contract, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view_contract);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mAdapter = new TargetAdapter(getContext());
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(mAdapter);

        LoaderManager loaderManager = getLoaderManager();
        if(loaderManager.getLoader(R.id.loader_query_targets) == null) {
            queryTargets();
        }
        else {
            loaderManager.initLoader(R.id.loader_query_player, null, this);
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        switch (id) {
            case R.id.loader_query_targets:
                return onCreateQueryTargetsLoader(args);
            default:
                throw new IllegalArgumentException("Unknown loader id: " + id);
        }
    }

    private Loader<Cursor> onCreateQueryTargetsLoader(@NonNull Bundle args) {
        return new CursorLoader(
                getContext(),
                StreetWarsContract.Target.CONTENT_URI,
                TargetAdapter.TargetProjection.PROJECTION,
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        int id = loader.getId();
        switch (id) {
            case R.id.loader_query_targets:
                mAdapter.swapCursor(cursor);
                break;
            default:
                throw new IllegalArgumentException("Unknown loader id: " + id);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        int id = loader.getId();
        switch (id) {
            case R.id.loader_query_targets:
                mAdapter.swapCursor(null);
                break;
            default:
                throw new IllegalArgumentException("Unknown loader id: " + id);
        }
    }

    private void queryTargets() {
        LoaderManager loaderManager = getLoaderManager();
        loaderManager.initLoader(R.id.loader_query_targets, null, this);
    }

}
