package com.wuxianedu.oschina.ui.fragment;

import android.content.Intent;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wuxianedu.corelib.image.ImageLoaderManager;
import com.wuxianedu.corelib.util.IntentUtils;
import com.wuxianedu.corelib.util.JSONParseUtils;
import com.wuxianedu.oschina.R;
import com.wuxianedu.oschina.base.BaseActivity;
import com.wuxianedu.oschina.base.BaseFragment;
import com.wuxianedu.oschina.bean.Featured;
import com.wuxianedu.oschina.bean.Owner;
import com.wuxianedu.oschina.network.RequestAPI;
import com.wuxianedu.oschina.network.RequestClient;
import com.wuxianedu.oschina.network.RequestConfig;
import com.wuxianedu.oschina.ui.ProjectDetailActivity;
import com.wuxianedu.oschina.util.IntentConstants;
import com.wuxianedu.oschina.util.ShakeManager;
import com.wuxianedu.oschina.util.TypefaceUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 摇一摇Fragment
 */
public class ShakeFragment extends BaseFragment {

    private RelativeLayout upImg;
    private RelativeLayout downImg;
    private LinearLayout mLoading;
    private RelativeLayout mShakeProject;
    private ImageView mProjectFace;
    private TextView mProjectTitle;
    private TextView mProjectDescription;
    private TextView mProjectLanguage;
    private TextView mProjectWatchNums;
    private TextView mProjectStarNums;
    private TextView mProjectForkNums;
    private SoundPool soundPool;
    private final int DURATION_TIME = 600;
    private ShakeManager shakeManager;
    private Map<Integer,Integer> soundPoolMap = new HashMap<>();
    private Featured project;
    private RelativeLayout mainBody;

    @Override
    public View getRootView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_shake,container,false);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if(hidden){
            shakeManager.stop();  //停止监听
        }else{
            shakeManager.start();  //启动监听
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        shakeManager.stop();  //停止监听
    }

    @Override
    public void onResume() {
        super.onResume();
        shakeManager.start();  //启动监听
    }

    @Override
    public void init(View rootView) {
        mainBody = (RelativeLayout) rootView.findViewById(R.id.mainBody_shake);
        upImg = (RelativeLayout) rootView.findViewById(R.id.shakeImgUp);
        downImg = (RelativeLayout) rootView.findViewById(R.id.shakeImgDown);
        mLoading = (LinearLayout) rootView.findViewById(R.id.shake_loading);
        mShakeProject = (RelativeLayout) rootView.findViewById(R.id.shake_project);

        mProjectFace = (ImageView) rootView.findViewById(R.id.iv_portrait);
        mProjectTitle = (TextView) rootView.findViewById(R.id.tv_title);
        mProjectDescription = (TextView) rootView.findViewById(R.id.tv_description);
        mProjectLanguage = (TextView) rootView.findViewById(R.id.tv_watch);
        mProjectWatchNums = (TextView) rootView.findViewById(R.id.tv_star);
        mProjectStarNums = (TextView) rootView.findViewById(R.id.tv_fork);
        mProjectForkNums = (TextView) rootView.findViewById(R.id.tv_language);

        mShakeProject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(project!=null){
                    Intent intent=new Intent(getActivity(), ProjectDetailActivity.class);
                    Bundle bundle=new Bundle();
                    bundle.putParcelable(IntentConstants.PROJECT_DETAIL,project);
                    intent.putExtras(bundle);
                    IntentUtils.startActivity(getActivity(), intent);
                }
            }
        });

        loadSound();
        shakeManager = new ShakeManager(getActivity());
        shakeManager.setShakeListener(new ShakeManager.OnShakeListener() {
            @Override
            public void onShake() {
                startAnim();
            }
        });

    }

    /**
     * 加载声音
     */
    private void loadSound() {
        soundPool = new SoundPool(2, AudioManager.STREAM_SYSTEM, 5);
        new Thread() {
            public void run() {
                try {
                    soundPoolMap.put(0, soundPool.load(
                            getActivity().getAssets().openFd("sound/shake_sound_male.mp3"), 1));
                    soundPoolMap.put(1, soundPool.load(
                            getActivity().getAssets().openFd("sound/shake_match.mp3"), 1));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    /**
     * 执行动画
     */
    public void startAnim() {
        //上班部分动画
        AnimationSet upAnim = new AnimationSet(true);
        TranslateAnimation upTranslateAnim0 = new TranslateAnimation(
                Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, 0f,
                Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF,
                -0.5f);
        upTranslateAnim0.setDuration(DURATION_TIME);
        TranslateAnimation upTranslateAnim1 = new TranslateAnimation(
                Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, 0f,
                Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF,
                +0.5f);
        upTranslateAnim1.setDuration(DURATION_TIME);
        upTranslateAnim1.setStartOffset(DURATION_TIME);
        upAnim.addAnimation(upTranslateAnim0);
        upAnim.addAnimation(upTranslateAnim1);
        upImg.startAnimation(upAnim);
        //下半部分动画
        AnimationSet downAnim = new AnimationSet(true);
        TranslateAnimation downTranslateAnim0 = new TranslateAnimation(
                Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, 0f,
                Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF,
                +0.5f);
        downTranslateAnim0.setDuration(DURATION_TIME);
        TranslateAnimation downTranslateAnim1 = new TranslateAnimation(
                Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, 0f,
                Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF,
                -0.5f);
        downTranslateAnim1.setDuration(DURATION_TIME);
        downTranslateAnim1.setStartOffset(DURATION_TIME);
        downAnim.addAnimation(downTranslateAnim0);
        downAnim.addAnimation(downTranslateAnim1);
        downImg.startAnimation(downAnim);

        upTranslateAnim0.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                mShakeProject.setVisibility(View.GONE);
                shakeManager.stop();  //停止监听
                int temp = soundPoolMap.get(0);
                soundPool.play(temp, (float) 0.2, (float) 0.2, 0, 0, (float) 0.6);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                loadProject();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
    }

    /**
     * 加载项目
     */
    private void loadProject(){
        mLoading.setVisibility(View.VISIBLE);
        mShakeProject.setVisibility(View.GONE);


        final BaseActivity activity = ((BaseActivity)getActivity());
        RequestConfig config = new RequestConfig();
        config.setTipInfoLayout(activity.getTipInfoLayout())
                .setMainBody(mainBody)
                .setIsCover(false)
                .setIsLoading(false);
        new RequestClient(activity,config){
            @Override
            public void loadSuccess(String result) {
                project = JSONParseUtils.parseObject(result, Featured.class);
                mLoading.setVisibility(View.GONE);
                soundPool.play(soundPoolMap.get(1), (float) 0.2, (float) 0.2, 0, 0, (float) 0.6);
                shakeManager.start();  //启动监听

                ShakeFragment.this.project = project;
                if(project!=null){
                    showProject();
                }
            }

            @Override
            public void loadFail() {}

        }.get(RequestAPI.SHAKE);
    }

    /**
     * 显示推荐的项目
     */
    private void showProject(){
        //标题
        Owner ownerVo = project.getOwner();
        String titleName ="";
        if(ownerVo!=null){
            String userName = ownerVo.getUsername();
            titleName += userName;
        }
        if(!TextUtils.isEmpty(titleName)){
            titleName +="/";
        }
        titleName+=project.getName();
        mProjectTitle.setText(titleName);

        String description = project.getDescription();
        if (TextUtils.isEmpty(description)) {
            description = getString(R.string.no_description_hint);
        }
        mProjectDescription.setText(description);

        TypefaceUtils.setIconText(mProjectWatchNums, getString(R.string.sem_watch) + " " + project.getWatches_count());
        TypefaceUtils.setIconText(mProjectStarNums, getString(R.string.sem_star) + " " + project.getStars_count());
        TypefaceUtils.setIconText(mProjectForkNums, getString(R.string.sem_fork) + " " + project.getForks_count());

        String language = project.getLanguage();
        if (TextUtils.isEmpty(language)) {
            mProjectLanguage.setVisibility(View.GONE);
        } else {
            TypefaceUtils.setIconText(mProjectLanguage, getString(R.string.sem_tag) + " " + project.getLanguage());
        }

        String portraitURL = project.getOwner().getNew_portrait();
        if (portraitURL.endsWith("portrait.gif")) {
            mProjectFace.setImageResource(R.mipmap.mini_avatar);
        } else {
            ImageLoaderManager.getInstance().loadImage(portraitURL,mProjectFace);
        }
        mProjectFace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                Bundle bundle = new Bundle();
                bundle.putInt(IntentConstants.USER_ID, project.getOwner().getId());
                bundle.putString(IntentConstants.USER_NAME, project.getOwner().getName());
                intent.putExtras(bundle);
//                IntentUtils.startActivityNetWork(getActivity(), UserActivity.class, intent);
            }
        });
        mShakeProject.setVisibility(View.VISIBLE);
    }

}
