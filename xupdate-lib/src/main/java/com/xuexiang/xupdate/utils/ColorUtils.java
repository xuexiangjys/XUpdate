/*
 * Copyright (C) 2018 xuexiangjys(xuexiangjys@163.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.xuexiang.xupdate.utils;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import androidx.annotation.ColorInt;
import androidx.annotation.ColorRes;

import java.util.Random;

/**
 * 颜色工具类
 *
 * @author xuexiang
 * @since 2018/7/2 下午3:12
 */
public final class ColorUtils {

    private ColorUtils() {
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    /**
     * 颜色选择器
     *
     * @param pressedColor 按下的颜色
     * @param normalColor  正常的颜色
     * @return 颜色选择器
     */
    public static ColorStateList getColorStateList(int pressedColor, int normalColor) {
        //其他状态默认为白色
        return new ColorStateList(
                new int[][]{{android.R.attr.state_enabled, android.R.attr.state_pressed}, {android.R.attr.state_enabled}, {}},
                new int[]{pressedColor, normalColor, Color.WHITE});
    }

    /**
     * 加深颜色
     *
     * @param color 原色
     * @return 加深后的
     */
    public static int colorDeep(int color) {
        int alpha = Color.alpha(color);
        int red = Color.red(color);
        int green = Color.green(color);
        int blue = Color.blue(color);
        float ratio = 0.8F;
        red = (int) (red * ratio);
        green = (int) (green * ratio);
        blue = (int) (blue * ratio);
        return Color.argb(alpha, red, green, blue);
    }

    /**
     * 是否是深色的颜色
     *
     * @param color
     * @return
     */
    public static boolean isColorDark(@ColorInt int color) {
        double darkness =
                1
                        - (0.299 * Color.red(color) + 0.587 * Color.green(color) + 0.114 * Color.blue(color))
                        / 255;
        return darkness >= 0.5;
    }


    /**
     * 按条件的到随机颜色
     *
     * @param alpha 透明
     * @param lower 下边界
     * @param upper 上边界
     * @return 颜色值
     */

    public static int getRandomColor(int alpha, int lower, int upper) {
        return new RandomColor(alpha, lower, upper).getColor();
    }

    /**
     * @return 获取随机色
     */
    public static int getRandomColor() {
        return new RandomColor(255, 80, 200).getColor();
    }

    /**
     * 获取Color值
     *
     * @param resId
     * @return
     */
    public static int getColor(Context context, @ColorRes int resId) {
        return context.getResources().getColor(resId);
    }

    /**
     * 随机颜色
     */
    public static class RandomColor {
        int alpha;
        int lower;
        int upper;

        RandomColor(int alpha, int lower, int upper) {
            if (upper <= lower) {
                throw new IllegalArgumentException("must be lower < upper");
            }
            setAlpha(alpha);
            setLower(lower);
            setUpper(upper);
        }

        public int getColor() {
            //随机数是前闭  后开
            int red = getLower() + new Random().nextInt(getUpper() - getLower() + 1);
            int green = getLower() + new Random().nextInt(getUpper() - getLower() + 1);
            int blue = getLower() + new Random().nextInt(getUpper() - getLower() + 1);
            return Color.argb(getAlpha(), red, green, blue);
        }

        public int getAlpha() {
            return alpha;
        }

        public void setAlpha(int alpha) {
            if (alpha > 255) alpha = 255;
            if (alpha < 0) alpha = 0;
            this.alpha = alpha;
        }

        int getLower() {
            return lower;
        }

        void setLower(int lower) {
            if (lower < 0) lower = 0;
            this.lower = lower;
        }

        int getUpper() {
            return upper;
        }

        void setUpper(int upper) {
            if (upper > 255) upper = 255;
            this.upper = upper;
        }
    }
}
