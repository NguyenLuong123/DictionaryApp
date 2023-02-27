package com.example.myapplication;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.Notification.ReminderBroadcast;
import com.example.myapplication.adapters.ViewPagerAdapter;
import com.example.myapplication.database.DatabaseOpenHelper;
import com.example.myapplication.databinding.ActivityMainBinding;
import com.example.myapplication.listener.AdapterListener;
import com.example.myapplication.listener.HomeListener;
import com.example.myapplication.listener.NotifyListener;
import com.example.myapplication.model.Word;
import com.example.myapplication.view.FavouriteFragment;
import com.example.myapplication.view.HistoryFragment;
import com.example.myapplication.view.HomeFragment;
import com.example.myapplication.view.NotifyFragment;
import com.example.myapplication.view.ReviewFragment;
import com.example.myapplication.view.TranslateFragment;
import com.google.android.material.navigation.NavigationView;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity implements HomeListener, AdapterListener, NotifyListener {
    //fragment
    private HomeFragment mHomeFragment = new HomeFragment();
    private FavouriteFragment mFavouriteFragment = new FavouriteFragment();
    private ReviewFragment mReviewFragment = new ReviewFragment();
    private TranslateFragment mTranslateFragment = new TranslateFragment();
    private DatabaseOpenHelper mDatabaseOpenHelper;
    //Viewpager and Tab layout
    private ViewPager2 mViewPager;
    private ViewPagerAdapter mViewPagerAdapter;
    private TabLayout mTabLayout;
    private String[] mTitleList = {"Home", "Favourite", "Review", "Translate"};
    private Integer[] mIconList = {R.drawable.ic_menu_home, R.drawable.ic_menu_favorite, R.drawable.ic_menu_review,
            R.drawable.ic_baseline_g_translate_24};

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;
    private View mHeaderLayout;
    private TextView notifyTv, historyTv;
    private HistoryFragment historyFragment = new HistoryFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDatabaseOpenHelper = new DatabaseOpenHelper(this, "saved_word_table", null, 1);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarMain.toolbar);
        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_favourite, R.id.nav_review, R.id.nav_setting)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
        getSupportActionBar().setTitle("My Dictionary");
        mHeaderLayout = navigationView.getHeaderView(0);
        setUpTab();
        notifyTv = mHeaderLayout.findViewById(R.id.frg_notify);
        historyTv = mHeaderLayout.findViewById(R.id.frg_history);
        NotifyFragment notifyFragment = new NotifyFragment(this);
        notifyTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getSupportFragmentManager().beginTransaction().replace(R.id.content_main, notifyFragment)
                        .addToBackStack(null).commit();
            }
        });
        historyTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getSupportFragmentManager().beginTransaction().replace(R.id.content_main, historyFragment)
                        .addToBackStack(null).commit();
            }
        });
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.main, menu);
//        return true;
//    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    private void setUpTab() {
        mHomeFragment.setmHomeListener(this);
        mHomeFragment.setmDatabaseOpenHelper(mDatabaseOpenHelper);
        mFavouriteFragment.setmDataOpenHelper(mDatabaseOpenHelper);
        mReviewFragment.setmDataOpenHelper(mDatabaseOpenHelper);
        mFavouriteFragment.setAdapterListener(this);
        mViewPager = findViewById(R.id.view_pager);
        mViewPagerAdapter = new ViewPagerAdapter(this);
        mViewPagerAdapter.addFragment(mHomeFragment);
        mViewPagerAdapter.addFragment(mFavouriteFragment);
        mViewPagerAdapter.addFragment(mReviewFragment);
        mViewPagerAdapter.addFragment(mTranslateFragment);
        mViewPager.setAdapter(mViewPagerAdapter);
        mViewPager.setOffscreenPageLimit(4);
        mTabLayout = findViewById(R.id.tab_layout);
        new TabLayoutMediator(mTabLayout, mViewPager,
                (tab, position) ->
                {
                    tab.setText(mTitleList[position]);
                    tab.setIcon(mIconList[position]);
                }
        ).attach();
    }


    @Override
    public void onUpdateFromHome(Word word) {
        mFavouriteFragment.updateSavedList(word);
    }

    @Override
    public void onUpdateFromSearch(String text) {
        historyFragment.updateData(text);

    }

    @Override
    public void onUpdateFromAdapter(String word) {
        mHomeFragment.onUpdateSaveBt(word);
    }

    private void createNotificationChannel(){
        if(Build.VERSION.SDK_INT > Build.VERSION_CODES.O) {
            CharSequence name = "LemubitReminderChannel";
            String description = "Channel for Lemubit Reminder";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("notifyLemubit", name, importance);
            channel.setDescription(description);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    @Override
    public void onUpdateFromNotify() {
        createNotificationChannel();
        Intent intent = new Intent(MainActivity.this, ReminderBroadcast.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(MainActivity.this, 0, intent, 0);
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        Calendar calendar = Calendar.getInstance();
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,
                calendar.getTimeInMillis(),NotifyFragment.minuteInMilis,
                pendingIntent);
    }

    @Override
    public void onStopNotify() {
        Intent intent = new Intent(this, ReminderBroadcast.class);
        PendingIntent sender = PendingIntent.getBroadcast(this, 0, intent, 0);
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmManager.cancel(sender);
    }
}