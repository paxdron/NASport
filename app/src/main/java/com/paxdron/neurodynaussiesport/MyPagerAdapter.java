package com.paxdron.neurodynaussiesport;

/**
 * Created by Antonio on 27/03/2016.
 */
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

public class MyPagerAdapter extends FragmentPagerAdapter implements
        ViewPager.OnPageChangeListener {

    private MyLinearLayout cur = null;
    private MyLinearLayout next = null;
    private Control context;
    private FragmentManager fm;
    private float scale;

    public MyPagerAdapter(Control context, FragmentManager fm) {
        super(fm);
        this.fm = fm;
        this.context = context;
    }

    @Override
    public Fragment getItem(int position)
    {
        // make the first pager bigger than others
        if (position == Control.FIRST_PAGE)
            scale = Control.BIG_SCALE;
        else
            scale = Control.SMALL_SCALE;

        position = position % Control.PAGES;
        return MyFragment.newInstance(context, position, scale);
    }

    @Override
    public int getCount()
    {
        return Control.PAGES * Control.LOOPS;
    }

    @Override
    public void onPageScrolled(int position, float positionOffset,
                               int positionOffsetPixels)
    {
        if (positionOffset >= 0f && positionOffset <= 1f)
        {
            cur = getRootView(position);
            cur.setScaleBoth(Control.BIG_SCALE - Control.DIFF_SCALE * positionOffset);

            if (position < Control.PAGES-1) {
                next = getRootView(position +1);
                next.setScaleBoth(Control.SMALL_SCALE + Control.DIFF_SCALE * positionOffset);
            }
        }
    }

    @Override
    public void onPageSelected(int position) {}

    @Override
    public void onPageScrollStateChanged(int state) {}

    private MyLinearLayout getRootView(int position)
    {
        return (MyLinearLayout)
                fm.findFragmentByTag(this.getFragmentTag(position))
                        .getView().findViewById(R.id.root);
    }

    private String getFragmentTag(int position)
    {
        return "android:switcher:" + context.pager.getId() + ":" + position;
    }
}