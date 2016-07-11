package com.utils.ui;

/**
 * Created by KingZ on 2016/1/5.
 * Discription:
 */
public class ScreenTools {
    public static final int DESIGN_WIDTH = 1280;
	public static final int DESIGN_HEIGHT = 720;

    public static int SCREEN_WIDTH = 1280;
	public static int SCREEN_HEIGHT = 720;

    public static int OperationHeight(int Original) {
		return Operation(Original);
	}

    public static int OperationHeight(float Original) {
		return Operation((int) Original);
		// return (int) (Original * mainScale + 0.5f);
	}

    public static int OperationWidth(int Original) {
		return Operation(Original);
		// return (int) (SCREEN_WIDTH * (Original * 1.0f / DESIGN_WIDTH) +
		// 0.5f);
	}
    public static int Operation(int Original) {
		return (int) (SCREEN_HEIGHT * (Original * 1.0f / DESIGN_HEIGHT) + 0.5f);
	}
}
