package fr.streetgames.streetwars.app.fragments;


import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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
import fr.streetgames.streetwars.widget.TeamMateAdapter;

public class ContractEliminationFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    public static final String TAG = "ContractEliminationFragment";

    private RecyclerView mRecyclerView;

    private TeamMateAdapter mAdapter;

    public ContractEliminationFragment() {
    }

    public static Fragment newInstance() {
        Bundle args = null;
        Fragment fragment = new ContractEliminationFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_contract_elimination, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        mRecyclerView = (RecyclerView) view.findViewById(R.id.elimination_killer_list);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mAdapter = new TeamMateAdapter();

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);

        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(mAdapter);

        LoaderManager loaderManager = getLoaderManager();
        if (loaderManager.getLoader(R.id.loader_query_team_mates) == null) {
            queryTeamMates();
        }
        else {
            loaderManager.initLoader(R.id.loader_query_team_mates, null, this);
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        switch (id) {
            case R.id.loader_query_team_mates:
                return onCreateQueryTeamMatesLoader(args);
            default:
                throw new IllegalArgumentException("Unknown loader id: " + id);
        }
    }

    private Loader<Cursor> onCreateQueryTeamMatesLoader(Bundle args) {
        return new CursorLoader(
                getContext(),
                StreetWarsContract.TeamMate.CONTENT_URI,
                TeamMateAdapter.TeamMateProjection.PROJECTION,
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        int id = loader.getId();
        switch (id) {
            case R.id.loader_query_team_mates:
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
            case R.id.loader_query_team_mates:
                mAdapter.swapCursor(null);
                break;
            default:
                throw new IllegalArgumentException("Unknown loader id: " + id);
        }
    }

    private void queryTeamMates() {
        LoaderManager loaderManager = getLoaderManager();
        loaderManager.initLoader(R.id.loader_query_team_mates, null, this);
    }

}
