package com.yhl.demoother.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bigkoo.pickerview.OptionsPickerView;
import com.yancy.imageselector.ImageConfig;
import com.yancy.imageselector.ImageSelector;
import com.yancy.imageselector.ImageSelectorActivity;
import com.yhl.demoother.R;
import com.yhl.demoother.bean.Account;
import com.yhl.demoother.bean.ProvinceBean;
import com.yhl.demoother.common.BaseActivity;
import com.yhl.demoother.common.Common;
import com.yhl.demoother.manager.PreferencesManager;
import com.yhl.demoother.utils.CityDataUtil;
import com.yhl.demoother.utils.GlideLoader;
import com.yhl.demoother.utils.ImageLoader;
import com.yhl.demoother.utils.ToastUtils;

import java.io.File;
import java.io.OptionalDataException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadFileListener;

public class PerfectActivity extends BaseActivity {

    @BindView(R.id.img_photo)
    ImageView imgPhoto;
    @BindView(R.id.tv_sex)
    TextView tvSex;
    @BindView(R.id.tv_age)
    TextView tvAge;
    @BindView(R.id.tv_address)
    TextView tvAddress;
    @BindView(R.id.btn_submit)
    Button btnSubmit;

    private ArrayList<ProvinceBean> options1Items = new ArrayList<>();
    private ArrayList<ArrayList<String>> options2Items = new ArrayList<>();
    private ArrayList<ArrayList<ArrayList<String>>> options3Items = new ArrayList<>();
    private ArrayList<String> ageItems = new ArrayList<>();
    private ArrayList<String> sexItems = new ArrayList<>();

    private String photoUrl, sex, age, address;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfect);
        ButterKnife.bind(this);

        setRightVisibility(View.GONE);
        setTitle("完善个人信息");
    }

    @Override
    protected void onResume() {
        super.onResume();
        options1Items = CityDataUtil.getProvinceData();
        options2Items = CityDataUtil.getCityData();
        options3Items = CityDataUtil.getAreData();
    }

    @OnClick({R.id.img_photo, R.id.tv_sex, R.id.tv_age, R.id.tv_address, R.id.btn_submit})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_photo:
                //TODO 选择头像
                selectPicture();
                break;
            case R.id.tv_sex:
                //TODO 选择性别
                showSex();
                break;
            case R.id.tv_age:
                //TODO 选择年龄
                showAge();
                break;
            case R.id.tv_address:
                //TODO 城市地址
                showAddress();
                break;
            case R.id.btn_submit:
                //TODO 提交信息
                updateUserInfo();
                break;
        }
    }

    //TODO 需要先登录才能使用此功能
    private void updateUserInfo() {
        sex = tvSex.getText().toString();
        age = tvAge.getText().toString();
        address = tvAddress.getText().toString();
        if (!TextUtils.isEmpty(photoUrl)
                && !TextUtils.isEmpty(sex)
                && !TextUtils.isEmpty(age)
                && !TextUtils.isEmpty(address)) {
            Account newUser = new Account();
            newUser.setPhoto(photoUrl);
            newUser.setSex("男".equals(sex) ? true : false);
            newUser.setAge(Integer.valueOf(age));
            newUser.setAddress(address);
            Account bmobUser = BmobUser.getCurrentUser(PerfectActivity.this, Account.class);
            newUser.update(PerfectActivity.this, bmobUser.getObjectId(), new UpdateListener() {
                @Override
                public void onSuccess() {
                    // TODO Auto-generated method stub
                    ToastUtils.shortToast(PerfectActivity.this, getString(R.string.update_userinfo_success));
                    PreferencesManager.getInstance(PerfectActivity.this).put(Common.USER_PHOTO, photoUrl);
                    //关闭当前页面
                    PerfectActivity.this.finish();
                }

                @Override
                public void onFailure(int code, String msg) {
                    // TODO Auto-generated method stub
                    ToastUtils.shortToast(PerfectActivity.this, getString(R.string.update_userinfo_failed) + msg);
                }
            });

        } else {
            ToastUtils.shortToast(PerfectActivity.this, getString(R.string.checkinfo));
        }
    }


    //  TODO 年龄选择器
    private void showAge() {
        for (int i = 1; i < 100; i++) {
            ageItems.add(String.valueOf(i));
        }
        OptionsPickerView opv = new OptionsPickerView.Builder(this, new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                tvAge.setText(ageItems.get(options1));
            }
        })
                .setTitleText(getString(R.string.select_age))
                .setDividerColor(Color.BLACK)
                .setTextColorCenter(Color.BLACK) //设置选中项文字颜色
                .setContentTextSize(20)
                .setOutSideCancelable(false)// default is true
                .build();
        opv.setPicker(ageItems);//一级选择器
        opv.show();
    }

    //TODO 性别选择器
    private void showSex() {
        sexItems.add("男");
        sexItems.add("女");
        OptionsPickerView opv = new OptionsPickerView.Builder(this, new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                tvSex.setText(sexItems.get(options1));
            }
        })
                .setTitleText(getString(R.string.select_sex))
                .setDividerColor(Color.BLACK)
                .setTextColorCenter(Color.BLACK) //设置选中项文字颜色
                .setContentTextSize(20)
                .setOutSideCancelable(false)// default is true
                .build();
        opv.setPicker(sexItems);//一级选择器
        opv.show();
    }

    //TODO 头像选择器
    private void selectPicture() {
        ImageConfig imageConfig
                = new ImageConfig.Builder(new GlideLoader())
                .steepToolBarColor(getResources().getColor(R.color.colorPrimary))
                .titleSubmitTextColor(getResources().getColor(R.color.white))
                .titleTextColor(getResources().getColor(R.color.white))
                // (截图默认配置：关闭    比例 1：1    输出分辨率  500*500)
                .crop(1, 1, 300, 300)
                // 开启单选   （默认为多选）
                .singleSelect()
                // 开启拍照功能 （默认关闭）
                .showCamera()
                // 拍照后存放的图片路径（默认 /temp/picture） （会自动创建）
                .filePath("/six/Pictures")
                .build();
        ImageSelector.open(PerfectActivity.this, imageConfig);   // 开启图片选择器

    }

    //TODO 城市地址选择器
    private void showAddress() {
        OptionsPickerView opv = new OptionsPickerView.Builder(this, new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                //返回的分别是三个级别的选中位置
                String tx = options1Items.get(options1).getPickerViewText() +
                        options2Items.get(options1).get(options2) +
                        options3Items.get(options1).get(options2).get(options3);
                tvAddress.setText(tx);
            }
        })
                .setTitleText(getString(R.string.select_city))
                .setDividerColor(Color.BLACK)
                .setTextColorCenter(Color.BLACK) //设置选中项文字颜色
                .setContentTextSize(20)
                .setOutSideCancelable(false)// default is true
                .build();
        //pvOptions.setPicker(options1Items);//一级选择器
        //opv.setPicker(options1Items, options2Items);//二级选择器
        opv.setPicker(options1Items, options2Items, options3Items);//三级选择器
        opv.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ImageSelector.IMAGE_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            // Get Image Path List
            List<String> pathList = data.getStringArrayListExtra(ImageSelectorActivity.EXTRA_RESULT);
            if (pathList.size() > 0){
                //TODO 由于单选只需要回去第一个数据就好,获取图片URL并上传
                uploadPhotoForURL(pathList.get(0));
            }else {
                ToastUtils.shortToast(PerfectActivity.this,getString(R.string.select_pic_failed));
            }
        }
    }
    private void uploadPhotoForURL(String path){
        final BmobFile bmobFile = new BmobFile(new File(path));
        bmobFile.uploadblock(PerfectActivity.this, new UploadFileListener() {

            @Override
            public void onSuccess() {
                //TODO bmobFile.getFileUrl(context)--返回的上传文件的完整地址
                photoUrl = bmobFile.getFileUrl(PerfectActivity.this);
                ImageLoader.getInstance().displayImageTarget(imgPhoto, photoUrl);
                ToastUtils.shortToast(PerfectActivity.this, getString(R.string.upload_photo_success) + photoUrl);
            }

            @Override
            public void onProgress(Integer value) {
                // TODO 返回的上传进度（百分比）
            }

            @Override
            public void onFailure(int code, String msg) {
                ToastUtils.shortToast(PerfectActivity.this, getString(R.string.upload_failed) + msg);
            }
        });
    }
}
