package be.cegeka.retrobox.newretro;

import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.inputmethod.InputMethodManager;

import be.cegeka.retrobox.R;
import be.cegeka.retrobox.util.DepthPageTransformer;

public class NewRetroActivity extends FragmentActivity implements ActionBar.TabListener {

    private NewRetroPagerAdapter newRetroPagerAdapter;
    private ViewPager viewPager;
    private InputMethodManager inputMethodManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_retro);

        setupTabs();
        inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
    }

    private void setupTabs() {
        final ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        newRetroPagerAdapter = new NewRetroPagerAdapter(getFragmentManager(), getApplicationContext());

        viewPager = (ViewPager) findViewById(R.id.new_retro_pager);
        viewPager.setAdapter(newRetroPagerAdapter);
        viewPager.setPageTransformer(true, new DepthPageTransformer());

        viewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                actionBar.setSelectedNavigationItem(position);
                inputMethodManager.hideSoftInputFromWindow(viewPager.getWindowToken(), 0);
            }
        });

        for (int i = 0; i < newRetroPagerAdapter.getCount(); i++) {
            actionBar.addTab(
                    actionBar.newTab()
                            .setText(newRetroPagerAdapter.getPageTitle(i))
                            .setTabListener(this));
        }
    }

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
        viewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {

    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {

    }
}