package com.kingz.animation;
import android.graphics.Camera;
import android.graphics.Matrix;
import android.view.animation.Animation;
import android.view.animation.Transformation;

public class Rotate3dAnimation extends Animation {
	private final float mFromDegrees;
	private final float mToDegrees;
	private final float mCenterX;
	private final float mCenterY;
	private Camera mCamera;

	public Rotate3dAnimation(float fromDegrees, float toDegrees, float centerX, float centerY) {
		this.mFromDegrees = fromDegrees;
		this.mToDegrees = toDegrees;
		this.mCenterX = centerX;
		this.mCenterY = centerY;
	}

	@Override
	public void initialize(int width, int height, int parentWidth, int parentHeight) {
		super.initialize(width, height, parentWidth, parentHeight);
		mCamera = new Camera();
	}

	@Override
	protected void applyTransformation(float interpolatedTime, Transformation t) {
		final float fromDegrees = mFromDegrees;
		float degrees = fromDegrees + ((mToDegrees - fromDegrees) * interpolatedTime);

		final float centerX = mCenterX;
		final float centerY = mCenterY;
		final Camera camera = mCamera;

		final Matrix matrix = t.getMatrix();

		if (degrees >= 76.0f) {
			degrees = 90.0f;
			camera.save();
			camera.rotateY(degrees);
			camera.getMatrix(matrix);
			camera.restore();
		} else if (degrees <= -76.0f) {
			degrees = -90.0f;
			camera.save();
			camera.rotateY(degrees);
			camera.getMatrix(matrix);
			camera.restore();
		} else {
			camera.save();
			camera.translate(0, 0, centerX);
			camera.rotateY(degrees);
			camera.translate(0, 0, -centerX);
			camera.getMatrix(matrix);
			camera.restore();
		}

		matrix.preTranslate(-centerX, -centerY);
		matrix.postTranslate(centerX, centerY);
	}
}
