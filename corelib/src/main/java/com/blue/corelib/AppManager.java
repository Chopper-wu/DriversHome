package com.blue.corelib;

import android.app.Activity;
import android.content.Context;

import com.blue.corelib.utils.CacheDemo;
import com.blue.xrouter.XRouter;
import com.newcw.component.manager.SystemManager;
import com.newcw.component.utils.ConfigManager;

import java.lang.ref.WeakReference;
import java.util.Stack;


/**
 * activity堆栈式管理
 */
public class AppManager {
    private static Stack<Activity> activityStack;
    private WeakReference<Activity> sCurrentActivityWeakRef;

    public AppManager() {

    }

    /**
     * 单一实例
     */
    public static AppManager getInstance() {
        return SingleApp.INSTANCE;
    }

    public static class SingleApp {
        public static  AppManager INSTANCE = new AppManager();
    }


    /**
     * 获取指定的Activity
     */
    public static Activity getActivity(Class<?> cls) {
        if (activityStack != null)
            for (Activity activity : activityStack) {
                if (activity.getClass().equals(cls)) {
                    return activity;
                }
            }
        return null;
    }

    /**
     * 获取当前显示Activity（堆栈中最后一个传入的activity）
     */
    public Activity getLastActivity() {
        Activity activity = activityStack.lastElement();
        return activity;
    }

    /**
     * 获取所有Activity
     */
    public Stack<Activity> getAllActivityStacks() {
        return activityStack;
    }

    /**
     * 结束指定的Activity
     */
    public void finishActivity(Activity activity) {
        if (activity != null) {
            if (!activity.isFinishing()) {
                activity.finish();
                activityStack.remove(activity);
            }
        }
    }

    /**
     * 结束指定类名的Activity
     */
    public void finishActivity(Class<?> cls) {
        for (Activity activity : activityStack) {
            if (activity.getClass().equals(cls)) {
                finishActivity(activity);
                break;
            }
        }
    }

    /**
     * 结束除当前传入以外所有Activity
     */
    public void finishOthersActivity(Class<?> cls) {
        if (activityStack != null && activityStack.size() > 0)
            for (Activity activity : activityStack) {
                if (!activity.getClass().equals(cls)) {
                    activity.finish();
                }
            }
    }

    /**
     * 结束所有Activity
     */
    public void finishAllActivity() {
        if (activityStack != null && activityStack.size() > 0) {
            while (!activityStack.empty()) {
                Activity activity = activityStack.pop();
                if (activity != null) {
                    activity.finish();
                }
            }

            activityStack.clear();
            activityStack = null;
        }
    }


    public Activity getCurrentActivity() {
        Activity currentActivity = null;
        if (sCurrentActivityWeakRef != null) {
            currentActivity = sCurrentActivityWeakRef.get();
        }

        return currentActivity;
    }

    public void setCurrentActivity(Activity activity) {
        sCurrentActivityWeakRef = new WeakReference<Activity>(activity);
    }

    /**
     * 添加Activity到堆栈
     */
    public void addActivity(Activity activity) {
        if (activityStack == null) {
            activityStack = new Stack<Activity>();
        }
        if (activityStack.search(activity) == -1) {
            activityStack.push(activity);
        }
    }

    public void removeActivity(Activity activity) {
        if (activityStack != null && activityStack.size() > 0) {
            activityStack.remove(activity);
        }
    }

    public void setTopActivity(Activity activity) {
        if (activityStack != null && activityStack.size() > 0) {
            if (activityStack.search(activity) == -1) {
                activityStack.push(activity);
                return;
            }

            int location = activityStack.search(activity);
            if (location != 1) {
                activityStack.remove(activity);
                activityStack.push(activity);
            }
        }
    }

    public Activity getTopActivity() {
        if (activityStack != null && activityStack.size() > 0) {
            return activityStack.peek();
        }

        return null;
    }

    public boolean isTopActivity(Activity activity) {
        return activity.equals(activityStack.peek());
    }

    public void finishTopActivity() {
        if (activityStack != null && activityStack.size() > 0) {
            Activity activity = activityStack.pop();
            if (activity != null) {
                activity.finish();
            }
        }
    }


    public Stack<Activity> getActivityStack() {
        return activityStack;
    }


    /**
     * 调整到登录页，结束所有Activity
     */
    public void exitUser(Context context) {
        //监管退出
        CacheDemo.INSTANCE.clearLoginUser();
        ConfigManager.setBooleanValue("agreementFlag", false);
        AppManager.getInstance().finishAllActivity();
        //XRouter.with(context).target(DRIVER_LOGIN).jump();
    }

    /**
     * 单点登录弹窗，调整到登录页。
     */
    public void singleLoginDialog(String msg) {
        if (AppManager.getInstance().getLastActivity() != null) {
            SystemManager.Companion.getINSTANCES().showSingleLogin(AppManager.getInstance().getLastActivity(), msg);
        }
    }

}