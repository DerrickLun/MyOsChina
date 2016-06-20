package com.wuxianedu.oschina;

import com.wuxianedu.corelib.CoreApplication;
import com.wuxianedu.corelib.util.L;
import com.wuxianedu.corelib.util.file.SerializableUtils;
import com.wuxianedu.oschina.bean.Member;
import com.wuxianedu.oschina.util.Constant;

/**
 * 开源中国应用
 * Created by lungank on 2016/4/9.
 */
public class OsChinaApplication extends CoreApplication {

    public static OsChinaApplication instance;
    //语言布局是否改变
    public static boolean carLayout = false;
    //会员信息
    public static Member member;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }

    /**
     * 判断用户是否登录过,true已登录,false未登录
     * @return 是否登录
     */
    public boolean isMemberLogin(){
        if(member==null){
            member = SerializableUtils.getSerializable(Constant.MEMBER);
            if(member == null){
                return false;
            }
        }
        return true;
    }

    /**
     * 保存会员信息
     */
    public void saveMember(Member member){
        SerializableUtils.setSerializable(Constant.MEMBER, member);
    }

    /**
     * 获取会员信息
     * @return 会员信息
     */
    public Member getMember(){
        isMemberLogin();
        return member;
    }

    /**
     * 退出登录
     */
    public boolean logout(){
        member = null;
        boolean flag = SerializableUtils.deleteSerializable(Constant.MEMBER);
        L.e("退出登录成功===========" + flag);
        return flag;
    }

}
