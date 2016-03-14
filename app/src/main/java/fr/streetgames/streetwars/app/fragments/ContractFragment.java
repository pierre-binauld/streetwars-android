package fr.streetgames.streetwars.app.fragments;

import android.accounts.Account;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import fr.streetgames.streetwars.R;
import fr.streetgames.streetwars.accounts.Authenticator;

/**
 * A placeholder fragment containing a simple view.
 */
public class ContractFragment extends Fragment {

    public static final String TAG = "ContractFragment";

    private Account mAccount;

    public ContractFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mAccount = Authenticator.CreateSyncAccount(getContext());
    }
}
