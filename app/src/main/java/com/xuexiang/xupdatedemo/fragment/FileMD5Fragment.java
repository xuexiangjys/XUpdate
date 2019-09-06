package com.xuexiang.xupdatedemo.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.xuexiang.xaop.annotation.Permission;
import com.xuexiang.xaop.annotation.SingleClick;
import com.xuexiang.xaop.consts.PermissionConsts;
import com.xuexiang.xpage.annotation.Page;
import com.xuexiang.xpage.base.XPageFragment;
import com.xuexiang.xupdate._XUpdate;
import com.xuexiang.xupdatedemo.R;
import com.xuexiang.xutil.app.IntentUtils;
import com.xuexiang.xutil.app.PathUtils;
import com.xuexiang.xutil.app.SocialShareUtils;
import com.xuexiang.xutil.common.StringUtils;
import com.xuexiang.xutil.file.FileUtils;
import com.xuexiang.xutil.tip.ToastUtils;

import butterknife.BindView;
import butterknife.OnClick;

import static android.app.Activity.RESULT_OK;

/**
 * @author xuexiang
 * @since 2019-09-03 23:45
 */
@Page(name = "获取文件的MD5值")
public class FileMD5Fragment extends XPageFragment {

    private final static int REQUEST_CODE_SELECT_APK_FILE = 1000;

    @BindView(R.id.tv_path)
    TextView tvPath;
    @BindView(R.id.tv_md5)
    TextView tvMd5;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_file_md5;
    }

    @Override
    protected void initViews() {

    }

    @Override
    protected void initListeners() {

    }


    @SingleClick
    @OnClick({R.id.btn_select_file, R.id.btn_calculate_md5, R.id.btn_share_file, R.id.btn_share_md5})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_select_file:
                selectAPKFile();
                break;
            case R.id.btn_calculate_md5:
                calculateMd5();
                break;
            case R.id.btn_share_file:
                shareFile();
                break;
            case R.id.btn_share_md5:
                shareMd5();
                break;
            default:
                break;
        }
    }


    private void calculateMd5() {
        String filePath = tvPath.getText().toString();
        if (StringUtils.isEmpty(filePath)) {
            ToastUtils.toast("请先选择文件！");
            return;
        }

        tvMd5.setText(_XUpdate.encryptFile(FileUtils.getFileByPath(filePath)));
    }


    private void shareFile() {
        String filePath = tvPath.getText().toString();
        if (StringUtils.isEmpty(filePath)) {
            ToastUtils.toast("请先选择文件！");
            return;
        }

        SocialShareUtils.shareFile(getActivity(), PathUtils.getUriForFile(FileUtils.getFileByPath(filePath)));
    }

    private void shareMd5() {
        String md5 = tvMd5.getText().toString();
        if (StringUtils.isEmpty(md5)) {
            ToastUtils.toast("请先计算MD5值！");
            return;
        }

        shareText(md5);
    }


    /**
     * 分享文字
     *
     * @param content 文字
     */
    private void shareText(String content) {
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_TEXT, content);
        shareIntent.setType("text/plain");
        //设置分享列表的标题，并且每次都显示分享列表
        startActivity(Intent.createChooser(shareIntent, "分享到"));
    }


    @Permission(PermissionConsts.STORAGE)
    private void selectAPKFile() {
        startActivityForResult(IntentUtils.getDocumentPickerIntent(IntentUtils.DocumentType.ANY), REQUEST_CODE_SELECT_APK_FILE);
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_CODE_SELECT_APK_FILE) {
                tvPath.setText(PathUtils.getFilePathByUri(getContext(), data.getData()));
            }
        }
    }


}
