package com.android.rivchat.util;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.util.Base64;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;


public class ImageUtils {

    public static final int AVATAR_WIDTH = 128;
    public static final int AVATAR_HEIGHT = 128;

    /**
     * Bo tròn ảnh avatar
     * @param context
     * @param src ảnh dạng bitmap
     * @return RoundedBitmapDrawable là đầu vào cho hàm setImageDrawable()
     */
    public static RoundedBitmapDrawable roundedImage(Context context, Bitmap src){
        /*Bo tròn avatar*/
        Resources res = context.getResources();
        RoundedBitmapDrawable dr =
                RoundedBitmapDrawableFactory.create(res, src);
        dr.setCornerRadius(Math.max(src.getWidth(), src.getHeight()) / 2.0f);

        return dr;
    }

    /**
     * Đối với ảnh hình chữ nhật thì cần cắt ảnh theo hình vuông và lấy phần tâm
     * ảnh để khi đặt làm avatar sẽ không bị méo
     * @param srcBmp
     * @return
     */
    public static Bitmap cropToSquare(Bitmap srcBmp){
        Bitmap dstBmp = null;
        if (srcBmp.getWidth() >= srcBmp.getHeight()){

            dstBmp = Bitmap.createBitmap(
                    srcBmp,
                    srcBmp.getWidth()/2 - srcBmp.getHeight()/2,
                    0,
                    srcBmp.getHeight(),
                    srcBmp.getHeight()
            );

        }else{
            dstBmp = Bitmap.createBitmap(
                    srcBmp,
                    0,
                    srcBmp.getHeight()/2 - srcBmp.getWidth()/2,
                    srcBmp.getWidth(),
                    srcBmp.getWidth()
            );
        }

        return dstBmp;
    }

    /**
     * Convert ảnh dạng bitmap ra String base64
     * @param imgBitmap
     * @return
     */
    public static String encodeBase64(Bitmap imgBitmap){
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        imgBitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(byteArray, Base64.DEFAULT);
    }

    /**
     * Làm giảm số điểm ảnh xuống để tránh lỗi Firebase Database OutOfMemory
     * @param is anh dau vao
     * @param reqWidth kích thước chiều rộng sau khi giảm
     * @param reqHeight kích thước chiều cao sau khi giảm
     * @return
     */
    public static Bitmap makeImageLite(InputStream is, int width, int height,
                                       int reqWidth, int reqHeight) {
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) >= reqHeight
                    && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }
        }

        // Calculate inSampleSize
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = inSampleSize;

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeStream(is, null, options);
    }


    public static InputStream convertBitmapToInputStream(Bitmap bitmap){
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0 /*ignored for PNG*/, bos);
        byte[] bitmapdata = bos.toByteArray();
        ByteArrayInputStream bs = new ByteArrayInputStream(bitmapdata);
        return bs;
    }

}
