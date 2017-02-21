package fr.streetgames.streetwars.app.delegate.base;

import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;

public abstract class CursorLoaderFragmentDelegate implements FragmentDelegate, LoaderManager.LoaderCallbacks<Cursor> {

    @CallSuper
    @Override
    public Loader<Cursor> onCreateLoader(int id, @NonNull Bundle data) {
        throw new IllegalArgumentException("Unknown loader id: " + id);
    }

    @CallSuper
    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        throw new IllegalArgumentException("Unknown loader id: " + loader.getId());
    }

    @CallSuper
    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        throw new IllegalArgumentException("Unknown loader id: " + loader.getId());
    }
}
