package aiec.br.ehc.converter;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.ColorMatrixColorFilter;
import android.net.Uri;
import android.provider.MediaStore;
import android.widget.ImageView;

/**
 * Funcionalidades para utilização de images
 */

public class ImageConverter {
    private final Context context;

    private static final float[] NEGATIVE = {
            -1.0f,     0,     0,    0, 255, // red
            0, -1.0f,     0,    0, 255, // green
            0,     0, -1.0f,    0, 255, // blue
            0,     0,     0, 1.0f,   0  // alpha
    };

    public ImageConverter(Context context) {
        this.context = context;
    }

    public String getRealPathFromURI (Uri contentUri) {
        String res = null;
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = context.getContentResolver().query(contentUri, projection, null, null, null);
        if (cursor.moveToFirst()) {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            res = cursor.getString(column_index);
        }

        cursor.close();
        return res;
    }

    public static ImageConverter create(Context context) {
        return new ImageConverter(context);
    }

    public static Bitmap resizeBitmap(final Bitmap temp, final int size) {
        if (size > 0) {
            int width = temp.getWidth();
            int height = temp.getHeight();
            float ratioBitmap = (float) width / (float) height;
            int finalWidth = size;
            int finalHeight = size;
            if (ratioBitmap < 1) {
                finalWidth = (int) ((float) size * ratioBitmap);
            } else {
                finalHeight = (int) ((float) size / ratioBitmap);
            }
            return Bitmap.createScaledBitmap(temp, finalWidth, finalHeight, true);
        } else {
            return temp;
        }
    }

    /**
     * Aplica um filtro do tipo Negativo (inverte as cores) de uma ImageView
     *
     * @param image Imagem a ser aplicado o filtro
     */
    public static void toNegativeColor(ImageView image) {
        image.setColorFilter(new ColorMatrixColorFilter(NEGATIVE));
    }
}
