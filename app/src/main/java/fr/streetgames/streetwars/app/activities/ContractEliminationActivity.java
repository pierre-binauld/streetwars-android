package fr.streetgames.streetwars.app.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import fr.streetgames.streetwars.R;
import fr.streetgames.streetwars.app.fragments.ContractEliminationFragment;

public class ContractEliminationActivity extends AppCompatActivity {

    private Fragment mFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contract_elimination);

        FragmentManager fragmentManager = getSupportFragmentManager();
        mFragment = fragmentManager.findFragmentByTag(ContractEliminationFragment.TAG);
        if (null == mFragment) {
            //noinspection WrongConstant
            mFragment = ContractEliminationFragment.newInstance();
        }
        fragmentManager.beginTransaction()
                .replace(R.id.content_fragment, mFragment, ContractEliminationFragment.TAG)
                .commit();
    }
}
