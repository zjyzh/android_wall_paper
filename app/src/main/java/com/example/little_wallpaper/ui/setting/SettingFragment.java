package com.example.little_wallpaper.ui.setting;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import com.example.little_wallpaper.GlobalSetting;
import com.example.little_wallpaper.MainViewModel;
import com.example.little_wallpaper.R;
import static com.example.little_wallpaper.util.FileUtil.makeToast;

public class SettingFragment extends Fragment {
    private MainViewModel mainViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_setting, container, false);
        if (root == null) {
            root = inflater.inflate(R.layout.fragment_muti_img, null, false);
        }
//        这个用来保存数据
        mainViewModel = new ViewModelProvider(requireActivity()).get(MainViewModel.class);
        addRadioClickListener(root);
        addTimeRadioClickListener(root);
        return root;
    }
    public static final String updateSetting = "updateSetting";

    //    发数据，更新服务，因为设置变了
    void sendMessageToActivity() {
        Intent local = new Intent();
        local.putExtra(updateSetting, true);
        requireActivity().sendBroadcast(local);
    }

    void addTimeRadioClickListener(View view) {
        RadioGroup timeSelect = view.findViewById(R.id.timeSelect);
        timeSelect.check(R.id.oneMinute);
        timeSelect.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup Group, int Checkid) {
//判断是否被选中
                if (Checkid == R.id.oneMinute) {
                    mainViewModel.timeIntervalSetting.setValue(GlobalSetting.oneMinute);
                    makeToast("自动更换壁纸的间隔时间设置为1m", getContext());
//                    Log.e("onCheckedChanged ", "oneMinute  " +mainViewModel.timeIntervalSetting.getValue());

                } else if (Checkid == R.id.halfHour) {
                    mainViewModel.timeIntervalSetting.setValue(GlobalSetting.halfHour);
                    makeToast("自动更换壁纸的间隔时间设置为30m", getContext());
//                    Log.e("onCheckedChanged", "halfHour  " + mainViewModel.timeIntervalSetting.getValue());

                } else if (Checkid == R.id.oneHour) {
                    mainViewModel.timeIntervalSetting.setValue(GlobalSetting.oneHour);
                    makeToast("自动更换壁纸的间隔时间设置为1h", getContext());
//                    Log.e("onCheckedChanged", "oneHour " + mainViewModel.timeIntervalSetting.getValue());
                }
                ml.sendMsg(mainViewModel.timeIntervalSetting.getValue() + "");

            }
        });

    }

    //1.定义和Activity通信的接口
    public interface SettingListener {
        public void sendMsg(String msg);
    }

    //3.声明一个引用
    private SettingListener ml;

    //4.为引用赋值
    public void onAttach(Context context) {
        super.onAttach(context);
        ml = (SettingListener) getActivity();
    }


    void addRadioClickListener(View view) {
        RadioGroup modelGroup = view.findViewById(R.id.modelGroup);
        modelGroup.check(R.id.mainScreen);
        modelGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup Group, int Checkid) {
//判断是否被选中

                if (Checkid == R.id.lock_mainScreen) {
                    mainViewModel.ScreenSetting.setValue(GlobalSetting.lock_mainScreen);
                    makeToast("壁纸用于锁定屏幕和主屏幕", getContext());

                } else if (Checkid == R.id.lockScreen) {
                    mainViewModel.ScreenSetting.setValue(GlobalSetting.lockScreen);
                    makeToast("壁纸仅用于锁定屏幕", getContext());

                } else if (Checkid == R.id.mainScreen) {
                    mainViewModel.ScreenSetting.setValue(GlobalSetting.mainScreen);
                    makeToast("壁纸仅用于主屏幕", getContext());
                }
                //传递消息，我更新了设置，然后activity也要更新对应的服务
                ml.sendMsg(mainViewModel.ScreenSetting.getValue() + "");
            }
        });
    }
}