package club.sgen.network;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.CornerPathEffect;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;

public class BitmapDrawRingOnOuter implements BitmapHandler {
	private int color;
	private int strokeWidth;
	private int diff;

	public BitmapDrawRingOnOuter(int ringColor, int strokeWidth, int diff) {
		this.color = ringColor;
		this.strokeWidth = strokeWidth;
		this.diff = diff;
	}

	@Override
	public Bitmap handleBitmap(Bitmap org) {
		Bitmap output = Bitmap.createBitmap(org.getWidth(), org.getHeight(),
				Config.ARGB_8888);
		Canvas canvas = new Canvas(output);

		final int c = 0xff424242;
		final Paint paint = new Paint();
		final Rect rect = new Rect(0, 0, org.getWidth(), org.getHeight());

		paint.setAntiAlias(true);
		paint.setDither(true); // set the dither to true

		paint.setStrokeJoin(Paint.Join.ROUND);
		paint.setStrokeCap(Paint.Cap.ROUND);
		paint.setPathEffect(new CornerPathEffect(100));

		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(c);
		canvas.drawCircle(org.getWidth() / 2, org.getHeight() / 2,
				org.getWidth() / 2, paint);
		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(org, rect, rect, paint);

		Paint tempPaint = new Paint();
		tempPaint.setAntiAlias(true);
		tempPaint.setColor(color); // set the color
		tempPaint.setStrokeWidth(strokeWidth); // set the size
		tempPaint.setStyle(Paint.Style.STROKE);
		canvas.drawCircle(org.getWidth() / 2, org.getHeight() / 2,
				org.getWidth() / 2 - diff, tempPaint);
		return output;
	}

}
