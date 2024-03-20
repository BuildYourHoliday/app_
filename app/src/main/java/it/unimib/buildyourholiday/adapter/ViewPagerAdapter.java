package it.unimib.buildyourholiday.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import it.unimib.buildyourholiday.ui.main.BookedFragment;
import it.unimib.buildyourholiday.ui.main.MyFragment;
import it.unimib.buildyourholiday.ui.main.SavedFragment;

public class ViewPagerAdapter extends FragmentStateAdapter {
    public ViewPagerAdapter(@NonNull MyFragment fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if(position == 0)
            return new BookedFragment();
        else
            return new SavedFragment();
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
