package com.example.common.util;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

/**
 * 类描述：
 * @Package com.example.common.util
 * @ClassName: Utils
 * @author 尤洋
 * @mail youyang@ucweb.com
 * @date 2015-3-31 下午2:53:52
 */
public class Utils {

    /**
     * 构造方法描述：
     * @Title: Utils
     * @date 2015-3-31 下午2:53:52
     */
    public Utils() {
        // TODO Auto-generated constructor stub
    }
    
    /**
     * google文档上提供的计算图片 宽高最大是你要想的宽高的 2的几次冥
     *  Calculate the largest inSampleSize value that is a power of 2 and keeps both
        height and width larger than the requested height and width.
     * @author 尤洋
     * @Title: calculateInSampleSize
     * @param options
     * @param reqWidth
     * @param reqHeight
     * @return
     * @return int
     * @date 2015-3-30 上午2:14:50
     */
    public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;
        if (height > reqHeight || width > reqWidth) {
            final int halfHeight = height / 2;
            final int halfWidth = width / 2;
            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }
        return inSampleSize;
    }

    /**
     * 
     * 方法描述： 保持不失真的情况下 压缩图片
     * @author 尤洋
     * @Title: decodeSampledBitmapFromResource
     * @param res    资源对象
     * @param resId    资源id
     * @param reqWidth  需要的图片的宽度
     * @param reqHeight 需要的图片的高度
     * @return
     * @return Bitmap
     * @date 2015-3-30 上午2:16:44
     */
    public static Bitmap decodeSampledBitmapFromResource(Resources res, int resId,
            int reqWidth, int reqHeight) {
        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, options);
        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        return BitmapFactory.decodeResource(res, resId, options);
    }

}
