package club.sgen.crop;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import club.sgen.network.R;

public class PictureSelector {

	private static final String TAG = "TestImageCropActivity";

	public static final int PICK_FROM_CAMERA = 100;
	public static final int PICK_FROM_ALBUM = 101;
	public static final int CROP_FROM_CAMERA = 102;

	private Uri mImageCaptureUri;
	private Activity activity;
	private Bitmap photo = null;
	private int width = 0;
	private int height = 0;

	public PictureSelector(Activity activity) {
		this.activity = activity;
	}

	public Bitmap getBitmap() {
		return photo;
	}

	public String getFileName() {
		return mImageCaptureUri.getPath();
	}

	public void setImageSize(int width, int height) {
		this.width = width;
		this.height = height;
	}

	public AlertDialog createDialog() {
		final View innerView = activity.getLayoutInflater().inflate(
				R.layout.image_crop_row, null);

		Button camera = (Button) innerView.findViewById(R.id.btn_camera_crop);
		Button gellary = (Button) innerView.findViewById(R.id.btn_gellary_crop);

		AlertDialog.Builder ab = new AlertDialog.Builder(activity);
		ab.setTitle("�̹��� ����");
		ab.setView(innerView);
		final AlertDialog dialog = ab.create();

		camera.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				doTakePhotoAction();
				setDismiss(dialog);
			}
		});

		gellary.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				doTakeAlbumAction();
				setDismiss(dialog);
			}
		});

		return dialog;
	}

	/**
	 * ī�޶� ȣ�� �ϱ�
	 */
	private void doTakePhotoAction() {
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		// Crop�� �̹����� ������ ������ ��θ� ����
		mImageCaptureUri = createSaveCropFile();
		intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT,
				mImageCaptureUri);
		activity.startActivityForResult(intent, PICK_FROM_CAMERA);
	}

	private Uri createSaveCropFile() {
		Uri uri = null;
		deleteTempFile();
		String url = "tmp_" + String.valueOf(System.currentTimeMillis())
				+ ".jpg";
		File file = new File(Environment.getExternalStorageDirectory(), url);

		if (!file.exists())
			try {
				file.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		uri = Uri.fromFile(file);
		return uri;
	}

	/**
	 * �ٹ� ȣ�� �ϱ�
	 */
	private void doTakeAlbumAction() {
		// �ٹ� ȣ��
		Intent intent = new Intent(Intent.ACTION_PICK);
		intent.setType(android.provider.MediaStore.Images.Media.CONTENT_TYPE);
		activity.startActivityForResult(intent, PICK_FROM_ALBUM);
	}

	private void setDismiss(AlertDialog dialog) {
		if (dialog != null && dialog.isShowing()) {
			dialog.dismiss();
		}
	}

	public boolean copyFile(File srcFile, File destFile) {
		boolean result = false;
		try {
			InputStream in = new FileInputStream(srcFile);
			try {
				result = copyToFile(in, destFile);
			} finally {
				in.close();
			}
		} catch (IOException e) {
			result = false;
		}
		return result;
	}

	private File getImageFile(Uri uri) {
		return new File(uri.getPath());
	}

	/**
	 * Copy data from a source stream to destFile. Return true if succeed,
	 * return false if failed.
	 */
	private boolean copyToFile(InputStream inputStream, File destFile) {
		try {
			OutputStream out = new FileOutputStream(destFile);
			try {
				byte[] buffer = new byte[4096];
				int bytesRead;
				while ((bytesRead = inputStream.read(buffer)) >= 0) {
					out.write(buffer, 0, bytesRead);
				}
			} finally {
				out.close();
			}
			return true;
		} catch (IOException e) {
			return false;
		}
	}

	public void deleteTempFile() {
		if (mImageCaptureUri != null) {
			File file = getImageFile(mImageCaptureUri);
			if (file != null)
				if (file.exists())
					file.delete();
			mImageCaptureUri = null;
		}
		if (photo != null) {
			photo.recycle();
			photo = null;
		}
	}

	private File getImageFileFromAlbum(Uri uri) {
		String[] projection = { MediaStore.Images.Media.DATA };
		if (uri == null) {
			uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
		}

		Cursor mCursor = activity.getContentResolver().query(uri, projection,
				null, null, MediaStore.Images.Media.DATE_MODIFIED + " desc");
		if (mCursor == null || mCursor.getCount() < 1) {
			return null; // no cursor or no record
		}
		int column_index = mCursor
				.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
		mCursor.moveToFirst();

		String path = mCursor.getString(column_index);

		if (mCursor != null) {
			mCursor.close();
			mCursor = null;
		}

		return new File(path);
	}

	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode != Activity.RESULT_OK) {
			return;
		}

		switch (requestCode) {
		case PICK_FROM_ALBUM: {
			Log.d(TAG, "PICK_FROM_ALBUM");

			// ������ ó���� ī�޶�� �����Ƿ� �ϴ� break���� �����մϴ�.
			// ���� �ڵ忡���� ���� �ո����� ����� �����Ͻñ� �ٶ��ϴ�.
			File original_file = getImageFileFromAlbum(data.getData());

			mImageCaptureUri = createSaveCropFile();
			File cpoy_file = new File(mImageCaptureUri.getPath());

			// SDī�忡 ����� ������ �̹��� Crop�� ���� �����Ѵ�.
			copyFile(original_file, cpoy_file);
		}

		case PICK_FROM_CAMERA: {
			Log.d(TAG, "PICK_FROM_CAMERA");

			// �̹����� ������ ������ ���������� �̹��� ũ�⸦ �����մϴ�.
			// ���Ŀ� �̹��� ũ�� ���ø����̼��� ȣ���ϰ� �˴ϴ�.

			Intent intent = new Intent("com.android.camera.action.CROP");
			intent.setDataAndType(mImageCaptureUri, "image/*");

			// Crop�� �̹����� ������ Path
			intent.putExtra("output", mImageCaptureUri);

			// Return Data�� ����ϸ� ���� �뷮 �������� ũ�Ⱑ ū �̹�����
			// �Ѱ� �� �� ����.
			// intent.putExtra("return-data", true);
			activity.startActivityForResult(intent, CROP_FROM_CAMERA);

			break;
		}

		case CROP_FROM_CAMERA: {
			Log.w(TAG, "CROP_FROM_CAMERA");

			// Crop �� �̹����� �Ѱ� �޽��ϴ�.
			Log.w(TAG, "mImageCaptureUri = " + mImageCaptureUri);

			String full_path = mImageCaptureUri.getPath();
			BitmapFactory.Options bof = new BitmapFactory.Options();
			bof.inSampleSize = 2;
			photo = BitmapFactory.decodeFile(full_path, bof);
			if (height > 0 && width > 0) {
				Bitmap temp = photo;
				photo = Bitmap.createScaledBitmap(temp, width, height, false);
				temp.recycle();
			}

			break;
		}
		}
	}
}
