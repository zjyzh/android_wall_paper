package com.example.little_wallpaper;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.WallpaperManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.lifecycle.SavedStateViewModelFactory;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager.widget.ViewPager;

import com.example.little_wallpaper.databinding.ActivityMainBinding;
import com.example.little_wallpaper.ui.home.HomeFragment;
import com.example.little_wallpaper.ui.middle.MiddleFragment;
import com.example.little_wallpaper.ui.middle.MiddleViewModel;
import com.example.little_wallpaper.ui.middle.MyListener;
import com.example.little_wallpaper.ui.setting.SettingFragment;
import com.example.little_wallpaper.util.FileUtil;
import com.example.little_wallpaper.util.WallPaperService;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.Stack;

import static com.example.little_wallpaper.util.FileUtil.getFilePermission;
import static com.example.little_wallpaper.util.FileUtil.makeToast;


/*
 * description:
 * @param null
 * @return null
 * @author zjy
 * @createTime 2021-06-30 16:20
 */
public class MainActivity extends AppCompatActivity implements MyListener, ViewPager.OnPageChangeListener, SettingFragment.SettingListener {

    //    ???????????????????????????????????????,????????????????????????
    public final static String GlobalBackground = "my_background_key";
    //    ??????????????????
    public final static String MutiImageList = "muti_img_list";

    //    ??????app?????????????????????
    private String picturePath;

//   ??????????????????service???action??????
    public static final String UpdateAction = "com.wallpaper.action";

    @Override
    protected void onStart() {
        super.onStart();
    }

    //????????????
    private ViewPager mViewPager;

    //    viewModel???????????????
    MainViewModel mainViewModel;
    ActivityMainBinding activityMainBinding;
    //   ??????fragment????????????
    private HomeFragment homeFragment;
    private MiddleViewModel middleViewModel;
    private BroadcastReceiver updateUIReciver;

    //2.??????MiddleFragment?????????????????????activity????????????
    public void middleFragmentMsg(String msg) {
        mainViewModel.saveImgUrl(msg);
//                activity???fragment???????????????????????????
//                ??????activity?????????
        activityMainBinding.setImageUrl(mainViewModel.getSavedImageUrl().getValue());
    }

    /*
     * @author zjy
     * @param activity
     * @param bits
     * @param on
     * @return void
     * @date 16:49 2021-06-30
     * @description ??????????????????????????????
     */
    public static void setWindowFlag(Activity activity, final int bits, boolean on) {
        Window win = activity.getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }


//    ?????????????????????????????????????????????????????????????????????
    MiddleFragment middleFragment;
    FrameLayout.LayoutParams params;
    int PREVIOUS_OFF_SET = 0;
    int PREVEIOUS_MARGIN = 0;
    int MIN_SIZE = 10;
    int TOTAL_OFFSET = 0;
    int ONE_PAGE_OFF = 25;
    int onePageNum = 0;
    Boolean isPush = false;
    int pos = 0;
    BottomNavigationView navView;
    Stack<Integer> offs;
    ImageView backGroundImg;


    @RequiresApi(api = Build.VERSION_CODES.R)
    @Override
    protected void onCreate(Bundle savedInstanceState) {

//        ?????????????????????
        supportRequestWindowFeature(Window.FEATURE_ACTION_BAR_OVERLAY);
        if (Build.VERSION.SDK_INT >= 19 && Build.VERSION.SDK_INT < 21) {
            setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, true);
        }
        if (Build.VERSION.SDK_INT >= 19) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }

//        ?????????????????????
        if (Build.VERSION.SDK_INT >= 21) {
            setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }

//        ?????????layout
        super.onCreate(savedInstanceState);

//        ????????????
        activityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);

//        ?????????????????????????????????
        navView = findViewById(R.id.nav_view);
        navView.setItemIconTintList(null);//java;
        navView.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), android.R.color.transparent));

//        ??????viewPager
        mViewPager = findViewById(R.id.vpager);//?????????ViewPager
        mViewPager.setOffscreenPageLimit(0);
        //ViewPager?????????

//        ????????????????????????????????????????????????????????????????????????
        backGroundImg = findViewById(R.id.activity_background);
        params = (FrameLayout.LayoutParams) backGroundImg.getLayoutParams();
        offs = new Stack<>();
        mViewPager.addOnPageChangeListener(this);

//        ????????????????????????????????????
        BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
                = item -> {
            if (item.getItemId() == R.id.navigation_home) {
                mViewPager.setCurrentItem(0);
                return true;
            }
            if (item.getItemId() == R.id.navigation_dashboard) {
                mViewPager.setCurrentItem(1);
                return true;
            }
            if (item.getItemId() == R.id.navigation_notifications) {
                mViewPager.setCurrentItem(2);
                return true;
            }
            return false;
        };

        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
//      ????????????
        mainViewModel =
                new ViewModelProvider(this,
                        new SavedStateViewModelFactory(getApplication(), this))
                        .get(MainViewModel.class);

        activityMainBinding.setData(mainViewModel);
        activityMainBinding.setLifecycleOwner(this);

//        ??????????????????????????????????????????
        activityMainBinding.setImageUrl(mainViewModel.getSavedImageUrl().getValue());
        Log.e("first  ", "main_oncreate out :" + mainViewModel.getSavedImageUrl().getValue());

        mainViewModel.saveImgUrl(mainViewModel.getSavedImageUrl().getValue());

        //Fragment????????????fragment????????????????????????mPagerAdapter
        final ArrayList<Fragment> fgLists = new ArrayList<>(4);
        homeFragment = new HomeFragment();
//       ??????????????????????????????

//        homeFragment.onPrimaryNavigationFragmentChanged();

//      MainActivity ?????????HomeFragment???????????????

//        ????????????fragment
        fgLists.add(homeFragment);
        middleFragment = new MiddleFragment();

        Bundle sendBundle = new Bundle();
//        Log.e("main_oncreate: in ", "" + mainViewModel.getSavedImageUrl().getValue());
        sendBundle.putString("imgUrl", mainViewModel.getSavedImageUrl().getValue());
        middleFragment.setArguments(sendBundle);

        fgLists.add(middleFragment);
        fgLists.add(new SettingFragment());

        FragmentPagerAdapter mPagerAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return fgLists.get(position);
            }

            @Override
            public int getCount() {
                return fgLists.size();
            }
        };
//        ??????adapter
        mViewPager.setAdapter(mPagerAdapter);
        mViewPager.setOffscreenPageLimit(3);

        middleViewModel =
                new ViewModelProvider(this).get(MiddleViewModel.class);

//        middleViewModel.saveImgs();
        IntentFilter filter = new IntentFilter();
        filter.addAction(UpdateAction);
        updateUIReciver = new BroadcastReceiver() {

            @Override
            public void onReceive(Context context, Intent intent) {
                //UI update here
                Log.e("update", "activity?????????");
                String background = intent.getStringExtra(MainActivity.GlobalBackground);
                makeToast("?????????", MainActivity.this);
                setBackground(background);
                if (intent.hasExtra(SettingFragment.updateSetting)) {
                    makeToast("???????????????", MainActivity.this);
                    startService(findViewById(R.id.start_btn));
                }
            }
        };
        registerReceiver(updateUIReciver, filter);

    }


    public final static String workerKey = "wallpaperList";
    public final static String screenSetting = "screenSetting";

    private final int PickImagesFromAlbum = 0x111;



    /*
     * @author zjy
     * @param view
     * @return void
     * @date 16:51 2021-06-30
     * @description ???????????????????????????????????????????????????
     */
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    public void chooseImgs(View view) {

        getFilePermission(this);
        Intent i = new Intent(
                Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        i.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        startActivityForResult(i, PickImagesFromAlbum);

    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 10) {
            if (Build.VERSION.SDK_INT >= 23) {
                if (!Settings.canDrawOverlays(this)) {
                    // SYSTEM_ALERT_WINDOW permission not granted...
                    Toast.makeText(MainActivity.this, "not permission", Toast.LENGTH_SHORT).show();
                }
            }
        }


        try {
            if (requestCode == PickImagesFromAlbum && resultCode == RESULT_OK && null != data) {
                final Uri selectedImage = data.getData();
                if (!TextUtils.isEmpty(selectedImage.getAuthority())) {
                    Cursor cursor = getContentResolver().query(selectedImage,
                            new String[]{MediaStore.Images.Media.DATA}, null, null, null);
                    if (null == cursor) {
                        makeToast("???????????????", this);
                        return;
                    }
                    cursor.moveToFirst();
                    picturePath = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
                    cursor.close();
                    mainViewModel.saveImgUrl(picturePath);
//                activity???fragment???????????????????????????
//                mFragmentListener.onTypeClick(mainViewModel.getSavedImageUrl().getValue());
//                ??????activity?????????
                    activityMainBinding.setImageUrl(mainViewModel.getSavedImageUrl().getValue());
                    middleViewModel.mutiImgs.getValue().add(mainViewModel.getSavedImageUrl().getValue());
                    setBackground(mainViewModel.getSavedImageUrl().getValue());
                    middleViewModel.getRecycleAdapta().notifyDataSetChanged();
                    Log.e(" onActivityResult ", "onActivityResult    " + mainViewModel.getSavedImageUrl().getValue());
//                 ?????????????????????????????????

                }
            }
            middleViewModel.saveImgs();

        } catch (Exception e) {
            Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG)
                    .show();
        }


    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    void setBackground(String background) {
//        middleViewModel.singleImgs.setValue(background);
        mainViewModel.saveImgUrl(background);
        activityMainBinding.setImageUrl(mainViewModel.getSavedImageUrl().getValue());
        FileUtil.setWallPaperAndBackground(WallpaperManager.getInstance(this), mainViewModel.getSavedImageUrl().getValue(),
                mainViewModel.ScreenSetting.getValue(),getApplicationContext());
    }

    Intent intent;
    PendingIntent pi;
    AlarmManager alarmManager;

    //    ??????????????????
    public void stopService(View view) {
        if (isMyServiceRunning()) {
            if (intent != null) {
                stopService(intent);
            }
            if (alarmManager != null) {
                alarmManager.cancel(pi);
            }
        } else {
            makeToast("??????????????????", this);
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
//        ??????????????????
        unregisterReceiver(updateUIReciver);
    }

    //    ?????????????????????????????????
    private boolean isMyServiceRunning() {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (WallPaperService.class.getName().equals(service.service.getClassName())) {
//                Log.e("running", "servicerunning");
                return true;
            }
        }
        return false;
    }

    /*
     * @author zjy
     * @param view
     * @return void
     * @date 15:51 2021-07-04
     * @description ????????????????????????????????????????????????????????????
     */
    public void startService(View view) {

        if (isMyServiceRunning()) {
            stopService(findViewById(R.id.stop));
        }

        intent = new Intent(this, WallPaperService.class);
        intent.putExtra(workerKey, middleViewModel.mutiImgs.getValue());
        intent.putExtra(screenSetting, mainViewModel.ScreenSetting.getValue());
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        pi = PendingIntent.getService(this, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        alarmManager = (AlarmManager) this.getSystemService(Service.ALARM_SERVICE);
        alarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, 500,
                mainViewModel.timeIntervalSetting.getValue(), pi);

        makeToast("?????????????????????????????????, ????????????: " + mainViewModel.timeIntervalSetting.getValue() / 1000 / 60 + "min", getApplicationContext());

    }


    /*
     * @author zjy
     * @param position
     * @param positionOffset
     * @param positionOffsetPixels
     * @return void
     * @date 15:50 2021-07-04
     * @description ??????APP??????????????????????????????
     */
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        pos = position;
        if ((PREVIOUS_OFF_SET - positionOffsetPixels) > MIN_SIZE) {
            PREVIOUS_OFF_SET = positionOffsetPixels;
            if (offs.size() >= (position) * ONE_PAGE_OFF && !offs.empty()) {
                offs.pop();
                isPush = false;
                params.leftMargin = (PREVEIOUS_MARGIN);
                PREVEIOUS_MARGIN = (int) ((PREVEIOUS_MARGIN + MIN_SIZE));
                TOTAL_OFFSET += MIN_SIZE;
            }

        }
        if ((PREVIOUS_OFF_SET - positionOffsetPixels) < -MIN_SIZE) {
            PREVIOUS_OFF_SET = positionOffsetPixels;
            if (offs.size() < (position + 1) * ONE_PAGE_OFF) {
                offs.push(MIN_SIZE);
                isPush = true;
                params.leftMargin = (PREVEIOUS_MARGIN);
                PREVEIOUS_MARGIN = (int) ((PREVEIOUS_MARGIN - MIN_SIZE));
                TOTAL_OFFSET -= MIN_SIZE;
            }
        }

        backGroundImg.setLayoutParams(params);
    }

    @Override
//    ???????????????
    public void onPageSelected(int position) {
        navView.getMenu().getItem(position).setChecked(true);
    }

    @Override
//    ??????????????????
    public void onPageScrollStateChanged(int state) {
        switch (state) {
            case ViewPager.SCROLL_STATE_IDLE:
                onePageNum = 0;
                break;
            case ViewPager.SCROLL_STATE_DRAGGING:
                break;
            case ViewPager.SCROLL_STATE_SETTLING:
                onePageNum = 0;
                final long timeInterval = 10;
                Runnable runnable = new Runnable() {
                    public void run() {
                        while (offs.size() > pos * ONE_PAGE_OFF && !isPush) {
                            try {
                                if (!offs.empty()) {
                                    offs.pop();
                                    params.leftMargin = (PREVEIOUS_MARGIN);
                                    PREVEIOUS_MARGIN = (int) ((PREVEIOUS_MARGIN + MIN_SIZE));
                                    TOTAL_OFFSET += MIN_SIZE;
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            try {
                                Thread.sleep(timeInterval);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                };
                Thread thread = new Thread(runnable);
                thread.start();
                break;
        }
    }

    @Override
    public void sendMsg(String msg) {
        startService(findViewById(R.id.start_btn));
    }
}