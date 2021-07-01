package com.example.final_wallpaper;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.WindowMetrics;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.final_wallpaper.databinding.ActivityMainBinding;
//import com.example.final_wallpaper.grid_img.Muti_img_fragment;
import com.example.final_wallpaper.ui.middle.BackgroundTask;
import com.example.final_wallpaper.ui.middle.MyListener;
import com.example.final_wallpaper.ui.setting.SettingFragment;
import com.example.final_wallpaper.ui.home.HomeFragment;
import com.example.final_wallpaper.ui.middle.MiddleFragment;
import com.example.final_wallpaper.util.FileUtil;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.hjq.permissions.OnPermissionCallback;
import com.hjq.permissions.Permission;
import com.hjq.permissions.XXPermissions;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.lifecycle.SavedStateViewModelFactory;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager.widget.ViewPager;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;

import static com.example.final_wallpaper.util.FileUtil.readPath;
import static java.util.concurrent.TimeUnit.SECONDS;

/*
 * description:
 * @param null
 * @return null
 * @author zjy
 * @createTime 2021-06-30 16:20
 */
public class MainActivity extends AppCompatActivity implements MyListener {

    //    设置销毁时候保存状态的常量,这里保存的是背景
    public final static String BACKGROUND_KEY_NUM = "my_key";
    public final static String MUTI_FILE_NUM = "muti_img_list";


    //    保存app背景图片的路径
    private String picturePath;

    //分页
    private ViewPager mViewPager;

    //    viewModel和数据绑定
    MainViewModel mainViewModel;
    ActivityMainBinding activityMainBinding;
    //   给子fragment组件传值
    private ToFragmentListener mFragmentListener;
    private HomeFragment homeFragment;


    //2.实现MiddleFragment的接口，这个是activity接受传值
    public void middleFragmentMsg(String msg) {
        mainViewModel.saveImgUrl(msg);
//                activity给fragment传值，让它更新背景
//        mFragmentListener.onTypeClick(mainViewModel.getSavedImageUrl().getValue());
//                更新activity的背景
        activityMainBinding.setImageUrl(mainViewModel.getSavedImageUrl().getValue());
        Log.e("TAG", "Fragment传回的数据：" + msg);
    }

    /*
     * @author zjy
     * @param activity
     * @param bits
     * @param on
     * @return void
     * @date 16:49 2021-06-30
     * @description 为了改变状态栏和背景
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

    MiddleFragment middleFragment;


    int PREVIOUS_OFF_SET = 0;
    int screenWidth = 0;
    int PREVEIOUS_MARGIN = 0;
    int MIN_SIZE = 10;
    int TOTAL_OFFSET = 0;

    int ONE_PAGE_OFF = 25;
    int onePageNum = 0;
    Boolean isPush = false;
    int pos = 0;
    int START = 0;
    int END = 0;


    private final ScheduledExecutorService scheduler =

            Executors.newScheduledThreadPool(1);

//    public void beepForAnHour() {
//        final Runnable beeper = new Runnable() {
//            public void run() { System.out.println("beep"); }
//
//        };
//
//        final ScheduledFuture beeperHandle =
//
//                scheduler.scheduleAtFixedRate(beeper, 1, 1, SECONDS);
//
//        scheduler.schedule(new Runnable() {
//            public void run() { beeperHandle.cancel(true); }
//
//        }, 60 * 60, SECONDS);
//
//    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        final WindowMetrics metrics = getWindowManager().getCurrentWindowMetrics();
        Rect r = metrics.getBounds();
        screenWidth = r.width();
//         System.out.println(r.width() + "高度  " + r.height());

//        设置透明状态栏
        supportRequestWindowFeature(Window.FEATURE_ACTION_BAR_OVERLAY);
        if (Build.VERSION.SDK_INT >= 19 && Build.VERSION.SDK_INT < 21) {
            setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, true);
        }
        if (Build.VERSION.SDK_INT >= 19) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }

//        设置透明状态栏
        if (Build.VERSION.SDK_INT >= 21) {
            setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }

//        创建主layout
        super.onCreate(savedInstanceState);

//        数据绑定
        activityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);

//        设置底边栏的颜色为透明
        BottomNavigationView navView = findViewById(R.id.nav_view);
        navView.setItemIconTintList(null);//java;
        navView.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), android.R.color.transparent));

        mViewPager = (ViewPager) findViewById(R.id.vpager);//获取到ViewPager
        //ViewPager的监听

//        下面这个代码比较复杂，主要想实现的就是壁纸的滚动
        ImageView backGroundImg = findViewById(R.id.activity_background);
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) backGroundImg.getLayoutParams();
        Stack<Integer> offs = new Stack<>();
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

//                System.out.println( "   (PREVIOUS_OFF_SET -  positionOffsetPixels ) : " + (PREVIOUS_OFF_SET -  positionOffsetPixels ) );

                pos = position;
//                System.out.println( positionOffset +  "       pix: " +  (PREVIOUS_OFF_SET - positionOffsetPixels )+"         pre:" + PREVIOUS_OFF_SET);
                if ((PREVIOUS_OFF_SET - positionOffsetPixels) > MIN_SIZE) {

                    PREVIOUS_OFF_SET = positionOffsetPixels;
//                    System.out.println( onePageNum + "   ONE_PAGE_OFF : " + ONE_PAGE_OFF + "      >MIN_SIZE " );
                    if (offs.size() >= (position) * ONE_PAGE_OFF && !offs.empty()) {
                        offs.pop();
                        isPush = false;
                        params.leftMargin = (PREVEIOUS_MARGIN);

                        PREVEIOUS_MARGIN = (int) ((PREVEIOUS_MARGIN + MIN_SIZE));
                        TOTAL_OFFSET += MIN_SIZE;
                        System.out.println(TOTAL_OFFSET + "   pop : " + offs.size());
                    }

                }
                if ((PREVIOUS_OFF_SET - positionOffsetPixels) < -MIN_SIZE) {

//                     System.out.println( TOTAL_OFFSET + "   push outside : " + offs.size()  + "  pos::" + (position+1)  );

                    PREVIOUS_OFF_SET = positionOffsetPixels;

                    if (offs.size() < (position + 1) * ONE_PAGE_OFF) {
                        offs.push(MIN_SIZE);
                        isPush = true;
                        params.leftMargin = (PREVEIOUS_MARGIN);
                        PREVEIOUS_MARGIN = (int) ((PREVEIOUS_MARGIN - MIN_SIZE));
                        TOTAL_OFFSET -= MIN_SIZE;
                        System.out.println(TOTAL_OFFSET + "   push : " + offs.size() + "  pos::" + (position + 1));
                    }

//
                }

                backGroundImg.setLayoutParams(params);
            }

            @Override
            public void onPageSelected(int position) {
                navView.getMenu().getItem(position).setChecked(true);
                //写滑动页面后做的事，使每一个fragmen与一个page相对应
            }

            @Override
            public void onPageScrollStateChanged(int state) {

                switch (state) {
                    case ViewPager.SCROLL_STATE_IDLE:
                        //无动作、初始状态
//                        END = PREVEIOUS_MARGIN;
                        System.out.println("   TOTAL_OFFSET:  " + (TOTAL_OFFSET));
                        onePageNum = 0;
//                        Log.i("TAG", "---->onPageScrollStateChanged无动作");
                        break;
                    case ViewPager.SCROLL_STATE_DRAGGING:
                        //点击、滑屏
//                        START = PREVEIOUS_MARGIN;

//                        Log.i("TAG", "---->onPageScrollStateChanged点击、滑屏");
                        break;
                    case ViewPager.SCROLL_STATE_SETTLING:
                        onePageNum = 0;
                        final long timeInterval = 10;// 两分钟运行一次
                        Runnable runnable = new Runnable() {
                            public void run() {
                                while (offs.size() > pos * ONE_PAGE_OFF && !isPush) {
                                    // ------- code for task to run
                                    try {
                                        if (!offs.empty()) {
                                            offs.pop();
                                            params.leftMargin = (PREVEIOUS_MARGIN);
//                            PREVIOUS_OFF_SET = positionOffsetPixels;
                                            PREVEIOUS_MARGIN = (int) ((PREVEIOUS_MARGIN + MIN_SIZE));
                                            TOTAL_OFFSET += MIN_SIZE;
                                            System.out.println(TOTAL_OFFSET + "   pop : " + offs.size() + "pos:: " + pos);
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                        System.out.println("多线程问题");
                                    }
                                    // ------- ends here
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
        });

//        底边栏点击按钮的监听事件
        BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
                = item -> {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    mViewPager.setCurrentItem(0);
                    //                        System.out.println("home");
                    return true;
                case R.id.navigation_dashboard:
                    mViewPager.setCurrentItem(1);
                    //                        System.out.println("navigation_dashboard");
                    return true;
                case R.id.navigation_notifications:
                    mViewPager.setCurrentItem(2);
                    //                        System.out.println("navigation_notifications");
                    return true;
            }
            return false;
        };

        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
//      设置监听
        mainViewModel =
                new ViewModelProvider(this,
                        new SavedStateViewModelFactory(getApplication(), this))
                        .get(MainViewModel.class);

        activityMainBinding.setData(mainViewModel);
        activityMainBinding.setLifecycleOwner(this);

//        从保存的状态那里读入图片路径
        activityMainBinding.setImageUrl(mainViewModel.getSavedImageUrl().getValue());
        Log.e("main_oncreate out : ", mainViewModel.getSavedImageUrl().getValue());

        //Fragment列表，将fragment放入列表中，放入mPagerAdapter
        final ArrayList<Fragment> fgLists = new ArrayList<>(4);
        homeFragment = new HomeFragment();
//       初始化要传值的子组件

//        homeFragment.onPrimaryNavigationFragmentChanged();
        Bundle sendBundle = new Bundle();
        Log.e("main_oncreate: in ", mainViewModel.getSavedImageUrl().getValue());
        sendBundle.putString("imgUrl", mainViewModel.getSavedImageUrl().getValue());
        homeFragment.setArguments(sendBundle);
//      MainActivity 传值到HomeFragment，记得顺序

//        添加三个fragment
        fgLists.add(homeFragment);
        middleFragment = new MiddleFragment();
        fgLists.add(new MiddleFragment());

//        mFragmentListener = middleFragment;
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
//        设置adapter
        mViewPager.setAdapter(mPagerAdapter);
        mViewPager.setOffscreenPageLimit(3);

    }


    private int PICKFILE_RESULT_CODE = 0x111;

    /*
     * @author zjy
     * @param view
     * @return void
     * @date 16:51 2021-06-30
     * @description 选择单个图片作为背景，同时获得权限
     */
    public void chooseImgs(View view) {
//        requestPermission();
        XXPermissions.with(this)
                // 不适配 Android 11 可以这样写
                //.permission(Permission.Group.STORAGE)
                // 适配 Android 11 需要这样写，这里无需再写 Permission.Group.STORAGE
//                .permission(Permission.READ_EXTERNAL_STORAGE)
                .permission(Permission.MANAGE_EXTERNAL_STORAGE)
                .request(new OnPermissionCallback() {

                    @Override
                    public void onGranted(List<String> permissions, boolean all) {
                        if (all) {
                            makeToast("获取存储权限成功");
                        }
                    }

                    @Override
                    public void onDenied(List<String> permissions, boolean never) {
                        if (never) {
                            makeToast("被永久拒绝授权，请手动授予存储权限");
                            // 如果是被永久拒绝就跳转到应用权限系统设置页面
                            XXPermissions.startPermissionActivity(MainActivity.this, permissions);
                        } else {
                            makeToast("获取存储权限失败");
                        }
                    }
                });

        Intent i = new Intent(
                Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(i, PICKFILE_RESULT_CODE);

    }

    public void makeToast(String str) {
        Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICKFILE_RESULT_CODE && resultCode == RESULT_OK && null != data) {
            final Uri selectedImage = data.getData();
            if (!TextUtils.isEmpty(selectedImage.getAuthority())) {
                Cursor cursor = getContentResolver().query(selectedImage,
                        new String[]{MediaStore.Images.Media.DATA}, null, null, null);
                if (null == cursor) {
                    makeToast("图片没找到");
                    return;
                }
                cursor.moveToFirst();

                picturePath = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
                cursor.close();

//                mainViewModel.singleImgs.setValue();
                mainViewModel.saveImgUrl("file://" + picturePath);
//                activity给fragment传值，让它更新背景
//                mFragmentListener.onTypeClick(mainViewModel.getSavedImageUrl().getValue());
//                更新activity的背景
                activityMainBinding.setImageUrl(mainViewModel.getSavedImageUrl().getValue());
                Log.e(" onActivityResult ", "onActivityResult    " + mainViewModel.getSavedImageUrl().getValue());
//                 设置背景图片和锁屏壁纸
                FileUtil.setWallPaperAndBackground(this, picturePath);
            }
        }
    }

}