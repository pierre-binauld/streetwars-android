package fr.streetgames.streetwars.app.fragments;

import android.content.res.Configuration;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import fr.streetgames.streetwars.BuildConfig;
import fr.streetgames.streetwars.R;
import fr.streetgames.streetwars.app.activities.MainActivity;
import fr.streetgames.streetwars.content.contract.StreetWarsContract;
import fr.streetgames.streetwars.widget.CardTargetAdapter;
import fr.streetgames.streetwars.widget.LineTargetAdapter;
import fr.streetgames.streetwars.widget.TargetAdapter;

/**
 * A placeholder fragment containing a simple view.
 */
public class ContractFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    public static final String TAG = "ContractFragment";

    private static final String ARG_GAME_MODE = "arg:game_mode";

    private Toolbar mToolbar;

    private RecyclerView mRecyclerView;

    private TargetAdapter mAdapter;

    @MainActivity.GameMode
    private int mGameMode;

    public static ContractFragment newInstance(@MainActivity.GameMode int gameMode) {
        Bundle args = new Bundle(1);
        args.putInt(ARG_GAME_MODE, gameMode);
        ContractFragment fragment = new ContractFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public ContractFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setHasOptionsMenu(true);
        //noinspection WrongConstant
        mGameMode = getArguments().getInt(ARG_GAME_MODE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO 16:9 aspect ratio
        return inflater.inflate(R.layout.fragment_contract, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view_contract);
        mToolbar = (Toolbar) view.findViewById(R.id.toolbar);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getActivity().setTitle(R.string.contract_title);

        if (BuildConfig.LOOP == mGameMode) {
            mAdapter = new CardTargetAdapter(getContext());
        }
        else {
            mAdapter = new LineTargetAdapter(getContext());
        }
        StaggeredGridLayoutManager layoutManager;
        switch (getResources().getConfiguration().orientation) {
            case Configuration.ORIENTATION_LANDSCAPE:
                layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
                break;
            case Configuration.ORIENTATION_PORTRAIT:
            default:
                layoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
                break;
        }
        layoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(mAdapter);

        ((MainActivity) getActivity()).setupToolbar(mToolbar);

        LoaderManager loaderManager = getLoaderManager();
        if(loaderManager.getLoader(R.id.loader_query_targets) == null) {
            queryTargets();
        }
        else {
            loaderManager.initLoader(R.id.loader_query_targets, null, this);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_fragment_contract, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        switch (itemId) {
            case R.id.menu_target_down:
                return true;
            default:
                return super.onOptionsItemSelected(item);
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
                CardTargetAdapter.TargetProjection.PROJECTION,
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
