package fr.streetgames.streetwars.app.fragments;

import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.app.ShareCompat;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import fr.streetgames.streetwars.BuildConfig;
import fr.streetgames.streetwars.R;
import fr.streetgames.streetwars.app.activities.MainActivity;
import fr.streetgames.streetwars.utils.HTTP;
import fr.streetgames.streetwars.widget.RuleAdapter;

import static fr.streetgames.streetwars.content.contract.StreetWarsContract.*;
import static fr.streetgames.streetwars.content.contract.StreetWarsContract.Player;


public class WaterCodeFragment extends Fragment implements View.OnClickListener, LoaderManager.LoaderCallbacks<Cursor> {

    public static final String TAG = "WaterCodeFragment";

    private CoordinatorLayout mCoordinatorLayout;
    private AppBarLayout mAppBarLayout;
    private CollapsingToolbarLayout mCollapsingToolbar;
    private Toolbar mToolbar;
    private ImageView mHeaderImageView;
    private RecyclerView mRuleRecycleView;
    private FloatingActionButton mFab;
    private LinearLayoutManager mLayoutManager;

    private RuleAdapter mAdapter;

    private DecelerateInterpolator mInterpolator;
    private boolean mDoWaterCodeAnimation;
    private boolean mDoRulesAnimation;

    public static WaterCodeFragment newInstance() {
        WaterCodeFragment fragment = new WaterCodeFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setHasOptionsMenu(true);
        mDoWaterCodeAnimation = true;
        mDoRulesAnimation = true;
    }

    @Override
    public View onCreateView(
            LayoutInflater inflater,
            ViewGroup container,
            Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_water_code, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        mHeaderImageView = (ImageView) view.findViewById(R.id.header);
        mToolbar = (Toolbar) view.findViewById(R.id.toolbar);
        mCollapsingToolbar = (CollapsingToolbarLayout) view.findViewById(R.id.collapsing_toolbar);
        mAppBarLayout = (AppBarLayout) view.findViewById(R.id.app_bar_layout);
        mCoordinatorLayout = (CoordinatorLayout) view.findViewById(R.id.coordinator_layout);
        mFab = (FloatingActionButton) view.findViewById(R.id.fab);
        mRuleRecycleView = (RecyclerView) getActivity().findViewById(R.id.recycler_view_rule);

        prepareWaterCodeAnimation();
        prepareRulesAnimation();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getActivity().setTitle(R.string.water_code_title);

        mAdapter = new RuleAdapter();
        mLayoutManager = new LinearLayoutManager(getContext());
        mRuleRecycleView.setLayoutManager(mLayoutManager);
        mRuleRecycleView.setAdapter(mAdapter);

        mFab.setOnClickListener(this);

        ((MainActivity) getActivity()).setupToolbar(mToolbar);

        onReattachLoader();

        queryWaterCode();
    }

    @Override
    public void onResume() {
        super.onResume();
        Picasso.with(getContext())
                .load(BuildConfig.LOCATION_HEADER_WATER_CODE)
                .placeholder(R.color.colorPrimary)
                .into(mHeaderImageView);
    }

    @CallSuper
    protected void onReattachLoader() {
        LoaderManager loaderManager = getLoaderManager();
        if (loaderManager.getLoader(R.id.loader_query_player) != null) {
            loaderManager.initLoader(R.id.loader_query_player, null, WaterCodeFragment.this);
        }
        // TODO Put this in adapter
        if (loaderManager.getLoader(R.id.loader_query_rules) != null) {
            loaderManager.initLoader(R.id.loader_query_rules, null, WaterCodeFragment.this);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_share:
                onShareClick();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void onShareClick() {
        Resources res = getResources();
        Intent intent = ShareCompat.IntentBuilder.from(getActivity())
                .setType(HTTP.TYPE_PLAIN_TEXT)
                .setSubject(res.getString(R.string.water_code_title))
                .setText(String.valueOf(mCollapsingToolbar.getTitle()))
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
            case R.id.loader_query_rules:
                return new CursorLoader(
                        getContext(),
                        Rule.CONTENT_URI,
                        new String[]{Rule.RULE},
                        null,
                        null,
                        null
                );
            case R.id.loader_query_water_code:
                return new CursorLoader(
                        getContext(),
                        Player.CONTENT_URI,
                        new String[]{Player.WATER_CODE},
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
            case R.id.loader_query_rules:
                onRulesLoadFinished(cursor);
                break;
            case R.id.loader_query_water_code:
                onWaterCodeLoadFinished(cursor);
                break;
            default:
                throw new IllegalArgumentException(String.format("Unknown loader id: %s", id));
        }
    }

    private void onRulesLoadFinished(@Nullable Cursor cursor) {
        mAdapter.swapCursor(cursor);
        if (cursor != null) {
            startRulesAnimation();
        }
    }

    private void onWaterCodeLoadFinished(@Nullable Cursor cursor) {
        if (cursor != null && cursor.moveToFirst()) {
            String waterCode = cursor.getString(0);
            mCollapsingToolbar.setTitle(waterCode);
            queryRules();
            startWaterCodeAnimation();
        } else {
            Log.w(TAG, "onWaterCodeLoadFinished: No water code found in database !");
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        @IdRes int id = loader.getId();
        switch (id) {
            case R.id.loader_query_rules:
            case R.id.loader_query_water_code:
                // Tu bluffes, Martoni !
                break;
            default:
                throw new IllegalArgumentException(String.format("Unknown loader id: %s", id));
        }
    }

    private void queryRules() {

        LoaderManager loaderManager = getLoaderManager();
        if (null != loaderManager.getLoader(R.id.loader_query_rules)) {
            loaderManager.initLoader(R.id.loader_query_rules, null, this);
        } else {
            loaderManager.initLoader(R.id.loader_query_rules, null, this);
        }
    }

    private void queryWaterCode() {

        LoaderManager loaderManager = getLoaderManager();
        if (null != loaderManager.getLoader(R.id.loader_query_water_code)) {
            loaderManager.initLoader(R.id.loader_query_water_code, null, this);
        } else {
            loaderManager.initLoader(R.id.loader_query_water_code, null, this);
        }
    }

    private void prepareWaterCodeAnimation() {
        if (mDoWaterCodeAnimation) {
            ViewCompat.setAlpha(mAppBarLayout, 0);
            ViewCompat.setTranslationY(mAppBarLayout, -100);

            ViewCompat.setScaleX(mFab, 0);
            ViewCompat.setScaleY(mFab, 0);
        }
    }

    private void prepareRulesAnimation() {
        if(mDoRulesAnimation) {
            mRuleRecycleView.setAlpha(0F);
        }
    }

    private void startWaterCodeAnimation() {
        if(mDoWaterCodeAnimation) {
            mDoWaterCodeAnimation = false;
            mCoordinatorLayout.post(new Runnable() {
                @Override
                public void run() {
                    mInterpolator = new DecelerateInterpolator();

                    ViewCompat.animate(mAppBarLayout)
                            .alpha(1)
                            .translationY(0)
                            .setInterpolator(mInterpolator)
                            .setStartDelay(100)
                            .start();

                    ViewCompat.animate(mFab)
                            .scaleX(1)
                            .scaleY(1)
                            .setInterpolator(mInterpolator)
                            .setStartDelay(300)
                            .start();
                }
            });
        }
    }

    private void startRulesAnimation() {
        if(!mDoWaterCodeAnimation && mDoRulesAnimation) {
            mDoRulesAnimation = false;
            mCoordinatorLayout.post(new Runnable() {
                @Override
                public void run() {
                    int rowIndex = 0;
                    View rowView;
                    do {
                        rowView = mRuleRecycleView.getChildAt(rowIndex);
                        if (rowView != null) {
                            ViewCompat.setAlpha(rowView, 0);
                            ViewCompat.setTranslationY(rowView, 50);
                        }
                        rowIndex++;
                    } while (rowView != null);
                    mRuleRecycleView.setAlpha(1F);

                    rowIndex = 0;
                    int startDelay = 500;
                    do {
                        rowView = mRuleRecycleView.getChildAt(rowIndex);
                        if (rowView != null) {
                            ViewCompat.setAlpha(rowView, 0);
                            ViewCompat.setTranslationY(rowView, 50);
                            ViewCompat.animate(rowView)
                                    .alpha(1)
                                    .translationY(0)
                                    .setInterpolator(mInterpolator)
                                    .setStartDelay(startDelay)
                                    .start();
                        }
                        rowIndex++;
                        startDelay += 50;
                    } while (rowView != null);
                }
            });
        }
    }
}
