//
// Created by kviensolo on 2019/12/16.
//

#include <jni.h>
#include <string>
#include "common.h"

#ifdef __cplusplus


static const unsigned int matrix[] = {
	0, 0, 2, 0, 0,
	0, 1, 2, 1, 0,
	2, 2, 4, 2, 2,
	0, 1, 2, 1, 0,
	0, 0, 2, 0, 0,
};

static const unsigned int factor[] = {
	0, 0x1000 / 1, 0x1000 / 2, 0x1000 / 3, 0x1000 / 4, 0x1000 / 5, 0x1000 / 6, 0x1000 / 7,
	0x1000 / 8, 0x1000 / 9, 0x1000 / 10, 0x1000 / 11, 0x1000 / 12, 0x1000 / 13, 0x1000 / 14, 0x1000 / 15,
	0x1000 / 16, 0x1000 / 17, 0x1000 / 18, 0x1000 / 19, 0x1000 / 20, 0x1000 / 21, 0x1000 / 22, 0x1000 / 23,
	0x1000 / 24, 0x1000 / 25, 0x1000 / 26, 0x1000 / 27, 0x1000 / 28, 0x1000 / 29, 0x1000 / 30, 0x1000 / 31,
	0x1000 / 32, 0x1000 / 33, 0x1000 / 34, 0x1000 / 35, 0x1000 / 36, 0x1000 / 37, 0x1000 / 38, 0x1000 / 39,
	0x1000 / 40, 0x1000 / 41, 0x1000 / 42, 0x1000 / 43, 0x1000 / 44, 0x1000 / 45, 0x1000 / 46, 0x1000 / 47,
	0x1000 / 48, 0x1000 / 49, 0x1000 / 50, 0x1000 / 51, 0x1000 / 52, 0x1000 / 53, 0x1000 / 54, 0x1000 / 55,
	0x1000 / 56, 0x1000 / 57, 0x1000 / 58, 0x1000 / 59, 0x1000 / 60, 0x1000 / 61, 0x1000 / 62, 0x1000 / 63,
	0x1000 / 64, 0x1000 / 65, 0x1000 / 66, 0x1000 / 67, 0x1000 / 68, 0x1000 / 69, 0x1000 / 70, 0x1000 / 71,
};

const int DX = 2;
const int DY = 2;
const int M_SIZE = DY * 2 + 1;

/**
 * 进行模糊算法
 * @param buf
 * @param width  像素宽度
 * @param height 像素高度
 */
void doBlur(ubyte* buf, int width, int height) {
    //每一行的字节总数
	int rowBytes = width * 4;
	int lineHead = 0;
	for (int y = 0; y < DY; ++y, lineHead += rowBytes) {
		for (int x = 0, curLineHead = lineHead; x < width; ++x, curLineHead += 4) {
			int count = 0;
			unsigned int r = 0, g = 0, b = 0;
			int startDX = std::max(0, x - DX), endDX = std::min((int) width, x + DX);
			int startDY = std::max(0, y - DY), endDY = std::min((int) height, y + DY);

			int xRange = (endDX - startDX) * 4;
			int startPxOffset = rowBytes * startDY + startDX * 4;
			int scalarXPos = startDX - x + DX;
			int scalarYPos = startDY - y + DY;
			while (startDY < endDY) {
				int endXRange = startPxOffset + xRange;
				for (int pxBase = startPxOffset, j = scalarYPos * M_SIZE + scalarXPos; pxBase < endXRange; pxBase += 4, ++j) {
					int scalar = matrix[j];
					if (scalar > 0) {
						count += scalar;
						r += buf[pxBase + 0] * scalar;
						g += buf[pxBase + 1] * scalar;
						b += buf[pxBase + 2] * scalar;
					}
				}
				++scalarYPos;
				++startDY;
				startPxOffset += rowBytes;
			}
			count = factor[count];
			buf[curLineHead + 0] = r * count / 0x1000;
			buf[curLineHead + 1] = g * count / 0x1000;
			buf[curLineHead + 2] = b * count / 0x1000;
		}
	}

	lineHead = DY*rowBytes;
	for (int y = DY; y < height - DY; ++y, lineHead += rowBytes) {
		for (int x = 0, curLineHead = lineHead; x < DX; ++x, curLineHead += 4) {
			int count = 0;
			unsigned int r = 0, g = 0, b = 0;
			int startDX = std::max(0, x - DX), endDX = std::min((int) width, x + DX);
			int startDY = std::max(0, y - DY), endDY = std::min((int) height, y + DY);

			int xRange = (endDX - startDX) * 4;
			int startPxOffset = rowBytes * startDY + startDX * 4;
			int scalarXPos = startDX - x + DX;
			int scalarYPos = startDY - y + DY;
			while (startDY < endDY) {
				int endXRange = startPxOffset + xRange;
				for (int pxBase = startPxOffset, j = scalarYPos * M_SIZE + scalarXPos; pxBase < endXRange; pxBase += 4, ++j) {
					int scalar = matrix[j];
					if (scalar > 0) {
						count += scalar;
						r += buf[pxBase + 0] * scalar;
						g += buf[pxBase + 1] * scalar;
						b += buf[pxBase + 2] * scalar;
					}
				}
				++scalarYPos;
				++startDY;
				startPxOffset += rowBytes;
			}
			count = factor[count];
			buf[curLineHead + 0] = r * count / 0x1000;
			buf[curLineHead + 1] = g * count / 0x1000;
			buf[curLineHead + 2] = b * count / 0x1000;
		}

		for (int x = DX, curLineHead = lineHead + DX * 4; x < width - DX; ++x, curLineHead += 4) {
			int count = 0;
			unsigned int r = 0, g = 0, b = 0;
			int startDX = x - DX, endDX = x + DX;
			int startDY = y - DY, endDY = y + DY;

			int xRange = (endDX - startDX) * 4;
			int startPxOffset = rowBytes * startDY + startDX * 4;
			int scalarXPos = 0;
			int scalarYPos = 0;
			while (startDY < endDY) {
				int endXRange = startPxOffset + xRange;
				for (int pxBase = startPxOffset, j = scalarYPos * M_SIZE + scalarXPos; pxBase < endXRange; pxBase += 4, ++j) {
					int scalar = matrix[j];
					if (scalar > 0) {
						count += scalar;
						r += buf[pxBase + 0] * scalar;
						g += buf[pxBase + 1] * scalar;
						b += buf[pxBase + 2] * scalar;
					}
				}
				++scalarYPos;
				++startDY;
				startPxOffset += rowBytes;
			}
			count = factor[count];
			buf[curLineHead + 0] = r * count / 0x1000;
			buf[curLineHead + 1] = g * count / 0x1000;
			buf[curLineHead + 2] = b * count / 0x1000;
		}

		for (int x = width - DX, curLineHead = lineHead + (width - DX) * 4; x < width; ++x, curLineHead += 4) {
			int count = 0;
			unsigned int r = 0, g = 0, b = 0;
			int startDX = std::max(0, x - DX), endDX = std::min((int) width, x + DX);
			int startDY = std::max(0, y - DY), endDY = std::min((int) height, y + DY);

			int xRange = (endDX - startDX) * 4;
			int startPxOffset = rowBytes * startDY + startDX * 4;
			int scalarXPos = startDX - x + DX;
			int scalarYPos = startDY - y + DY;
			while (startDY < endDY) {
				int endXRange = startPxOffset + xRange;
				for (int pxBase = startPxOffset, j = scalarYPos * M_SIZE + scalarXPos; pxBase < endXRange; pxBase += 4, ++j) {
					int scalar = matrix[j];
					if (scalar > 0) {
						count += scalar;
						r += buf[pxBase + 0] * scalar;
						g += buf[pxBase + 1] * scalar;
						b += buf[pxBase + 2] * scalar;
					}
				}
				++scalarYPos;
				++startDY;
				startPxOffset += rowBytes;
			}
			count = factor[count];
			buf[curLineHead + 0] = r * count / 0x1000;
			buf[curLineHead + 1] = g * count / 0x1000;
			buf[curLineHead + 2] = b * count / 0x1000;
		}
	}

	lineHead = (height - DY)*rowBytes;
	for (int y = height - DY; y < height; ++y, lineHead += rowBytes) {
		for (int x = 0, curLineHead = lineHead; x < width; ++x, curLineHead += 4) {
			int count = 0;
			unsigned int r = 0, g = 0, b = 0;
			int startDX = std::max(0, x - DX), endDX = std::min((int) width, x + DX);
			int startDY = std::max(0, y - DY), endDY = std::min((int) height, y + DY);

			int xRange = (endDX - startDX) * 4;
			int startPxOffset = rowBytes * startDY + startDX * 4;
			int scalarXPos = startDX - x + DX;
			int scalarYPos = startDY - y + DY;
			while (startDY < endDY) {
				int endXRange = startPxOffset + xRange;
				for (int pxBase = startPxOffset, j = scalarYPos * M_SIZE + scalarXPos; pxBase < endXRange; pxBase += 4, ++j) {
					int scalar = matrix[j];
					if (scalar > 0) {
						count += scalar;
						r += buf[pxBase + 0] * scalar;
						g += buf[pxBase + 1] * scalar;
						b += buf[pxBase + 2] * scalar;
					}
				}
				++scalarYPos;
				++startDY;
				startPxOffset += rowBytes;
			}

			count = factor[count];
			buf[curLineHead + 0] = r * count / 0x1000;
			buf[curLineHead + 1] = g * count / 0x1000;
			buf[curLineHead + 2] = b * count / 0x1000;
		}
	}
}

extern "C" {
#endif
/**
 * 进行Bitmap的模糊操作
 * @param env       JNIEnv接口指针
 * @param clazz
 * @param bufObject Java Buffer对象
 * @param width
 * @param height
 * @param pass
 * @return
 */
JNIEXPORT jboolean JNICALL
Java_com_zeke_utils_MyNativeUtils_doBlur(JNIEnv *env, jobject clazz, jobject bufObject,
										 jint width, jint height, jint pass) {
    // 获取原生数组的内存地址
    ubyte *buf = (ubyte *) env->GetDirectBufferAddress(bufObject);
    if (buf == 0) {
        return JNI_FALSE;
    }
    // 进行模糊操作
    while (pass-- > 0) {
		doBlur(buf, width, height);
	}
    return JNI_TRUE;
}

#ifdef __cplusplus
}
#endif
