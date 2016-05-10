package fr.streetgames.streetwars.app.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import fr.streetgames.streetwars.R;

public class ContractEliminationFragment extends Fragment {

    public static final String TAG = "ContractEliminationFragment";

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
}
