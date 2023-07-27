//
// Created by kviensolo on 2019/12/16.
//

#include <jni.h>
#include <string>
#include "common.h"
#include "task_runner.h"

#ifndef LOG_TAG
#define LOG_TAG "JNI-Blur"
#endif

#ifdef __cplusplus
extern "C"{

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
 * 进行常规模糊算法
 * 执行次数越多，图片越暗
 * 效率低，一张 2000x1200的图，在Pixel XL设备上处理一次需要900ms左右
 *
 * @param buf    分配在内存的图片数据buffer数据
 * @param width  像素宽度
 * @param height 像素高度
 */
void doBlur(ubyte* buf, int width, int height) {
    //每一行的字节总数
	int rowBytes = width * 4;
	int lineHead = 0;

    //上边界处理
	for (int y = 0; y < DY; ++y, lineHead += rowBytes) {
        //对当前行的每个像素进行循环处理
        for (int x = 0, curLineHead = lineHead; x < width; ++x, curLineHead += 4) {
			int count = 0;
			unsigned int r = 0, g = 0, b = 0; //记录像素模糊后的颜色值。
			//计算水平和垂直方向像素模糊的范围
			int startDX = std::max(0, x - DX), endDX = std::min((int) width, x + DX);
			int startDY = std::max(0, y - DY), endDY = std::min((int) height, y + DY);

			//水平方向上需要模糊的数据字节范围(即偏移量)
			int xBytesRange = (endDX - startDX) * 4;
            //计算起始像素在图像数据中的字节偏移量: 前面行的字节数+当前行水平方向字节数的偏移量
			int startPxOffsetOfBytes = rowBytes * startDY + startDX * 4;

            // 计算方形模糊核中当前像素的位置偏移量
			int scalarXPos = startDX - x + DX;
			int scalarYPos = startDY - y + DY;

			//在垂直方向上循环处理模糊核内的像素。
			while (startDY < endDY) {
				int endXPosOfBytes = startPxOffsetOfBytes + xBytesRange;
				//对水平方向每个像素进行循环处理，j是根据模糊核的索引关系来确定当前像素在模糊核中的位置。
				for (int xPos = startPxOffsetOfBytes,
						j = scalarYPos * M_SIZE + scalarXPos; //垂直方向上的像素偏移量乘以模糊核的宽度，再加上水平方向上的偏移量，得到当前像素在模糊核中的索引值。
						xPos < endXPosOfBytes;
						xPos += 4, ++j) {

				    //获取模糊核中偏移量j的权重值，如果权重值大于0，则进行计算。
					int scalar = matrix[j];
					if (scalar > 0) {
						count += scalar; //权重值累加
						r += buf[xPos + 0] * scalar;
						g += buf[xPos + 1] * scalar;
						b += buf[xPos + 2] * scalar;
					}
				}
				//更新垂直方向上的位置偏移量
				++scalarYPos;
				++startDY;
				startPxOffsetOfBytes += rowBytes;
			}
			//根据累加的颜色通道值计算平均值，并将结果写回原图像数据中。
			count = factor[count];
			buf[curLineHead + 0] = r * count / 0x1000;
			buf[curLineHead + 1] = g * count / 0x1000;
			buf[curLineHead + 2] = b * count / 0x1000;
		}
	}

//	//核心模糊区域内部
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
				for (int pxBase = startPxOffset,
				        j = scalarYPos * M_SIZE + scalarXPos;
				        pxBase < endXRange;
				        pxBase += 4, ++j) {
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
				for (int pxBase = startPxOffset,
				        j = scalarYPos * M_SIZE + scalarXPos;
				        pxBase < endXRange;
				        pxBase += 4, ++j) {
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
				for (int pxBase = startPxOffset,
				        j = scalarYPos * M_SIZE + scalarXPos;
				        pxBase < endXRange; pxBase += 4, ++j) {
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

//	//下边界
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

/**
 * 进行Bitmap数据进行高斯模糊操作
 * @param env       JNIEnv接口指针
 * @param clazz
 * @param bufObject Java Buffer对象
 * @param width
 * @param height
 * @param pass
 * @return
 */
JNIEXPORT jboolean JNICALL
Java_com_zeke_utils_WildFireUtils_doBlur(JNIEnv *env, jobject clazz, jobject bufObject,
                                         jint width, jint height, jint pass) {
    ubyte *buf = (ubyte *) env->GetDirectBufferAddress(bufObject);

    if (buf == nullptr) {
        return JNI_FALSE;
    }
    while (pass-- > 0) {
		doBlur(buf, width, height);
	}
    return JNI_TRUE;
}

/**
 * 并发的快速高斯模糊
 *
 * 效率比doFastBlur高，一张 2000x1200的图，在Pixel XL设备上，模糊半径越大，越能体现性能。
 * 20个像素范围，需要1386ms左右
 *
 * FIXME radius为1时，图片会花
 *
 * @param bufObject Java Buffer对象
 * @param width		图片宽度像素
 * @param height	图片高度像素
 * @param radius	模糊半径
 * @return
 */
void doFastBlurParallel(ubyte* buf, int width, int height, int radius) {
	if (radius <= 0 || width <= radius * 2 || height < radius * 2) {
		return;
	}
	int rowBytes = width * 4;
	ubyte * tmpBuf = (ubyte*) malloc(sizeof(ubyte)*rowBytes*height);
	if (tmpBuf == nullptr) {
		return;
	}

	const int COUNT_SCALAR = 64;
	const int SHIFT_BITS = 6 + 12;
	const long CPU_NUM = GET_CPU_NUM();

	uint32_t * scalar = (uint32_t*) alloca((radius + 1) * 3 * 2 * sizeof(uint32_t));
	{
		float factor = 1.0f / 2.8f; //方差数据  0.357142866
		float d1 = 0.39894f / (radius*factor);
		float d2 = (2 * radius*radius * factor*factor); //0.255102038  //0.274348408
		for (uint32_t i = 0; i < 2 * radius + 1; i++) {
			int d3 = i - radius; //-1: 90.7802048  0:4575.36279  1:90.7802048
			scalar[i] = d1*exp(-(d3 * d3) / d2) * 64 * COUNT_SCALAR;
			__android_log_print(ANDROID_LOG_ERROR, LOG_TAG, "JiSuan scalar: %f", scalar[i]);
		}
	}

	thread_impl *THREADS = (thread_impl*) alloca(sizeof(thread_impl)*CPU_NUM);
	for (int i = 0; i < CPU_NUM; ++i) {
		new (&THREADS[i]) thread_impl();
		THREADS[i].start();
	}

	// horizontal
	{
		int horz_block_size = (height + CPU_NUM - 1) / CPU_NUM;
		auto _cal_horz = [&](void *yBegin) -> void * {
			int yStart = *(int*) yBegin;
			int yEnd = yStart + horz_block_size;
			if (yEnd > height) {
				yEnd = height;
			}
			int lineHead = yStart*rowBytes;
			for (int y = yStart; y < yEnd; ++y, lineHead += rowBytes) {
				for (int x = radius, curLineHead = lineHead + 4 * radius; x < width - radius; ++x, curLineHead += 4) {
					int s = scalar[radius];
					int count = s + COUNT_SCALAR - 1;
					unsigned int r = buf[curLineHead + 0] * s;
					unsigned int g = buf[curLineHead + 1] * s;
					unsigned int b = buf[curLineHead + 2] * s;

					for (int i = 1, xDelta = 4; i < radius; ++i, xDelta += 4) {
						int s = scalar[radius + i];
						count += s * 2;
						r += (buf[curLineHead + 0 - xDelta] + buf[curLineHead + 0 + xDelta])* s;
						g += (buf[curLineHead + 1 - xDelta] + buf[curLineHead + 1 + xDelta])* s;
						b += (buf[curLineHead + 2 - xDelta] + buf[curLineHead + 2 + xDelta])* s;
					}
					count = factor[count / COUNT_SCALAR];
					tmpBuf[curLineHead + 0] = (r * count) >> SHIFT_BITS;
					tmpBuf[curLineHead + 1] = (g * count) >> SHIFT_BITS;
					tmpBuf[curLineHead + 2] = (b * count) >> SHIFT_BITS;
				}
			}
			return 0;
		};

		auto _cal_horz_left = [&](void *) -> void * {
			int lineHead = 0;
			for (int y = 0; y < height; ++y, lineHead += rowBytes) {
				for (int x = 0, curLineHead = lineHead; x < radius; ++x, curLineHead += 4) {
					int count = COUNT_SCALAR - 1;
					unsigned int r = 0, g = 0, b = 0;
					int startDX = 0, endDX = std::min((int) width, x + radius);

					int xRange = (endDX - startDX) * 4;
					int startPxOffset = lineHead + startDX * 4;
					int scalarXPos = startDX - x + radius;
					int endXRange = startPxOffset + xRange;
					for (int pxBase = startPxOffset, j = scalarXPos; pxBase < endXRange; pxBase += 4, ++j) {
						int s = scalar[j];
						count += s;
						r += buf[pxBase + 0] * s;
						g += buf[pxBase + 1] * s;
						b += buf[pxBase + 2] * s;
					}
					count = factor[count / COUNT_SCALAR];
					tmpBuf[curLineHead + 0] = (r * count) >> SHIFT_BITS;
					tmpBuf[curLineHead + 1] = (g * count) >> SHIFT_BITS;
					tmpBuf[curLineHead + 2] = (b * count) >> SHIFT_BITS;
				}
			}
			return 0;
		};

		auto _cal_horz_right = [&](void *) -> void * {
			int lineHead = 0;
			for (int y = 0; y < height; ++y, lineHead += rowBytes) {
				for (int x = width - radius, curLineHead = lineHead + 4 * (width - radius); x < width; ++x, curLineHead += 4) {
					int count = COUNT_SCALAR - 1;
					unsigned int r = 0, g = 0, b = 0;
					int startDX = std::max(0, x - radius), endDX = width;

					int xRange = (endDX - startDX) * 4;
					int startPxOffset = lineHead + startDX * 4;
					int scalarXPos = startDX - x + radius;
					int endXRange = startPxOffset + xRange;
					for (int pxBase = startPxOffset, j = scalarXPos; pxBase < endXRange; pxBase += 4, ++j) {
						int s = scalar[j];
						count += s;
						r += buf[pxBase + 0] * s;
						g += buf[pxBase + 1] * s;
						b += buf[pxBase + 2] * s;
					}
					count = factor[count / COUNT_SCALAR];
					tmpBuf[curLineHead + 0] = (r * count) >> SHIFT_BITS;
					tmpBuf[curLineHead + 1] = (g * count) >> SHIFT_BITS;
					tmpBuf[curLineHead + 2] = (b * count) >> SHIFT_BITS;
				}
			}
			return 0;
		};

		typedef task_runner<decltype(_cal_horz), int> _horz_runner_t;
		_horz_runner_t *runners = (_horz_runner_t*) alloca(sizeof(_horz_runner_t)*CPU_NUM);

		task_runner<decltype(_cal_horz_left), int> _horz_left_runner(_cal_horz_left, 0);
		task_runner<decltype(_cal_horz_right), int> _horz_right_runner(_cal_horz_right, 0);
		runner_base *runners_ex[2] = {
				&_horz_left_runner, &_horz_right_runner
		};

		for (int i = 0; i < CPU_NUM; ++i) {
			int yBegin = i*horz_block_size;
			new(runners + i) _horz_runner_t(_cal_horz, yBegin);
			THREADS[i].add_runner(runners + i);
		}

		for (int i = 0; i < 2; ++i) {
			THREADS[i%CPU_NUM].add_runner(runners_ex[i]);
		}

		for (int i = 0; i < CPU_NUM; ++i) {
			THREADS[i].signal();
		}

		for (int i = 0; i < CPU_NUM; ++i) {
			THREADS[i].wait();
		}
	}

	// vertical
	{
		int vert_range_end = height - radius;
		int vert_block_size = (vert_range_end - radius + CPU_NUM - 1) / CPU_NUM;
		auto _cal_vert = [&](void *yBegin) -> void * {
			int yStart = *(int*) yBegin;
			int yEnd = yStart + vert_block_size;
			if (yEnd > vert_range_end) {
				yEnd = vert_range_end;
			}
			int lineHead = yStart*rowBytes;
			for (int y = yStart; y < yEnd; ++y, lineHead += rowBytes) {
				for (int x = 0, curLineHead = lineHead; x < width; ++x, curLineHead += 4) {
					int s = scalar[radius];
					int count = s + COUNT_SCALAR - 1;
					unsigned int r = tmpBuf[curLineHead + 0] * s;
					unsigned int g = tmpBuf[curLineHead + 1] * s;
					unsigned int b = tmpBuf[curLineHead + 2] * s;

					for (int i = 1, yDelta = rowBytes; i < radius; ++i, yDelta += rowBytes) {
						int s = scalar[radius + i];
						count += s * 2;
						r += (tmpBuf[curLineHead + 0 - yDelta] + tmpBuf[curLineHead + 0 + yDelta]) * s;
						g += (tmpBuf[curLineHead + 1 - yDelta] + tmpBuf[curLineHead + 1 + yDelta]) * s;
						b += (tmpBuf[curLineHead + 2 - yDelta] + tmpBuf[curLineHead + 2 + yDelta]) * s;

					}
					count = factor[count / COUNT_SCALAR];
					buf[curLineHead + 0] = (r * count) >> SHIFT_BITS;
					buf[curLineHead + 1] = (g * count) >> SHIFT_BITS;
					buf[curLineHead + 2] = (b * count) >> SHIFT_BITS;
				}
			}
			return 0;
		};
		auto _cal_vert_top = [&](void *) -> void * {
			int lineHead = 0;
			for (int y = 0; y < radius; ++y, lineHead += rowBytes) {
				for (int x = 0, curLineHead = lineHead; x < width; ++x, curLineHead += 4) {
					int count = COUNT_SCALAR - 1;
					unsigned int r = 0, g = 0, b = 0;
					int startDY = 0, endDY = std::min((int) height, y + radius);
					int scalarYPos = startDY - y + radius;

					for (int pxBase = rowBytes * startDY + x * 4, j = scalarYPos; startDY < endDY; pxBase += rowBytes, ++j, ++startDY) {
						int s = scalar[j];
						count += s;
						r += tmpBuf[pxBase + 0] * s;
						g += tmpBuf[pxBase + 1] * s;
						b += tmpBuf[pxBase + 2] * s;
					}

					count = factor[count / COUNT_SCALAR];
					buf[curLineHead + 0] = (r * count) >> SHIFT_BITS;
					buf[curLineHead + 1] = (g * count) >> SHIFT_BITS;
					buf[curLineHead + 2] = (b * count) >> SHIFT_BITS;
				}
			}
			return 0;
		};
		auto _cal_vert_bottom = [&](void *) -> void * {
			int lineHead = (height - radius)*rowBytes;
			for (int y = height - radius; y < height; ++y, lineHead += rowBytes) {
				for (int x = 0, curLineHead = lineHead; x < width; ++x, curLineHead += 4) {
					int count = COUNT_SCALAR - 1;
					unsigned int r = 0, g = 0, b = 0;
					int startDY = std::max(0, y - radius), endDY = height;
					int scalarYPos = startDY - y + radius;

					for (int pxBase = rowBytes * startDY + x * 4, j = scalarYPos; startDY < endDY; pxBase += rowBytes, ++j, ++startDY) {
						int s = scalar[j];
						count += s;
						r += tmpBuf[pxBase + 0] * s;
						g += tmpBuf[pxBase + 1] * s;
						b += tmpBuf[pxBase + 2] * s;
					}

					count = factor[count / COUNT_SCALAR];
					buf[curLineHead + 0] = (r * count) >> SHIFT_BITS;
					buf[curLineHead + 1] = (g * count) >> SHIFT_BITS;
					buf[curLineHead + 2] = (b * count) >> SHIFT_BITS;
				}
			}
			return 0;
		};

		typedef task_runner<decltype(_cal_vert), int> _vert_runner_t;
		_vert_runner_t *runners = (_vert_runner_t*) alloca(sizeof(_vert_runner_t)*CPU_NUM);

		task_runner<decltype(_cal_vert_top), int> _vert_top_runner(_cal_vert_top, 0);
		task_runner<decltype(_cal_vert_bottom), int> _vert_bottom_runner(_cal_vert_bottom, 0);
		runner_base *runners_ex[2] = {
				&_vert_top_runner, &_vert_bottom_runner
		};

		for (int i = 0; i < CPU_NUM; ++i) {
			int yBegin = i*vert_block_size + radius;
			new(runners + i) _vert_runner_t(_cal_vert, yBegin);
			THREADS[i].add_runner(runners + i);
		}

		for (int i = 0; i < 2; ++i) {
			THREADS[i%CPU_NUM].add_runner(runners_ex[i]);
		}

		for (int i = 0; i < CPU_NUM; ++i) {
			THREADS[i].signal();
		}

		for (int i = 0; i < CPU_NUM; ++i) {
			THREADS[i].wait();
		}

		for (int i = 0; i < CPU_NUM; ++i) {
			THREADS[i].join();
		}
	}

	free(tmpBuf);
}

/**
 * 快速高斯模糊
 *
 * 效率比doBlur高，一张 2000x1200的图，在Pixel XL设备上处理1个radius,需要100ms左右
 *
 * FIXME radius为1时，图片会花
 *
 * @param bufObject Java Buffer对象
 * @param width		图片宽度像素
 * @param height	图片高度像素
 * @param radius	模糊半径
 * @return
 */
void doFastBlur(ubyte* buf, int width, int height, int radius) {
	if (radius <= 0 || width <= radius * 2 || height < radius * 2) {
		__android_log_print(ANDROID_LOG_ERROR, LOG_TAG, "radius params is illegal !!! ");
		return;
	}
	int rowBytes = width * 4;
	ubyte * tmpBuf = (ubyte*) malloc(sizeof(ubyte)*rowBytes*height);
	if (tmpBuf == nullptr) {
		return;
	}

	const int COUNT_SCALAR = 64;
	const int SHIFT_BITS = 6+12;

	uint32_t * scalar = (uint32_t*) alloca((radius + 1) * 3 * 2 * sizeof(uint32_t));
	{
		float factor = 1.0f / 2.8f;
		float d1 = 0.39894f / (radius*factor);
		float d2 = (2 * radius*radius * factor*factor);
		for (uint32_t i = 0; i < 2 * radius + 1; i++) {
			int d3 = i - radius;
			scalar[i] = d1*exp(-(d3 * d3) / d2) * 64 * COUNT_SCALAR;
		}
	}

	// horizontal
	int lineHead = 0;
	for (int y = 0; y < height; ++y, lineHead += rowBytes) {
		for (int x = 0, curLineHead = lineHead; x < radius; ++x, curLineHead += 4) {
			int count = COUNT_SCALAR-1;
			unsigned int r = 0, g = 0, b = 0;
			int startDX = 0, endDX = std::min((int) width, x + radius);

			int xRange = (endDX - startDX) * 4;
			int startPxOffset = lineHead + startDX * 4;
			int scalarXPos = startDX - x + radius;
			int endXRange = startPxOffset + xRange;
			for (int pxBase = startPxOffset, j = scalarXPos; pxBase < endXRange; pxBase += 4, ++j) {
				int s = scalar[j];
				count += s;
				r += buf[pxBase + 0] * s;
				g += buf[pxBase + 1] * s;
				b += buf[pxBase + 2] * s;
			}
			count = factor[count / COUNT_SCALAR];
			tmpBuf[curLineHead + 0] = (r * count) >> SHIFT_BITS;
			tmpBuf[curLineHead + 1] = (g * count) >> SHIFT_BITS;
			tmpBuf[curLineHead + 2] = (b * count) >> SHIFT_BITS;
		}
	}

	lineHead = 0;
	for (int y = 0; y < height; ++y, lineHead += rowBytes) {
		for (int x = radius, curLineHead = lineHead + 4 * radius; x < width - radius; ++x, curLineHead += 4) {
			int s = scalar[radius];
			int count = s + COUNT_SCALAR - 1;
			unsigned int r = buf[curLineHead + 0] * s;
			unsigned int g = buf[curLineHead + 1] * s;
			unsigned int b = buf[curLineHead + 2] * s;

			for (int i = 1, xDelta = 4; i < radius; ++i, xDelta += 4) {
				int s = scalar[radius + i];
				count += s * 2;
				r += (buf[curLineHead + 0 - xDelta] + buf[curLineHead + 0 + xDelta])* s;
				g += (buf[curLineHead + 1 - xDelta] + buf[curLineHead + 1 + xDelta])* s;
				b += (buf[curLineHead + 2 - xDelta] + buf[curLineHead + 2 + xDelta])* s;
			}
			count = factor[count / COUNT_SCALAR];
			tmpBuf[curLineHead + 0] = (r * count) >> SHIFT_BITS;
			tmpBuf[curLineHead + 1] = (g * count) >> SHIFT_BITS;
			tmpBuf[curLineHead + 2] = (b * count) >> SHIFT_BITS;
		}
	}


	lineHead = 0;
	for (int y = 0; y < height; ++y, lineHead += rowBytes) {
		for (int x = width - radius, curLineHead = lineHead + 4 * (width - radius); x < width; ++x, curLineHead += 4) {
			int count = COUNT_SCALAR - 1;
			unsigned int r = 0, g = 0, b = 0;
			int startDX = std::max(0, x - radius), endDX = width;

			int xRange = (endDX - startDX) * 4;
			int startPxOffset = lineHead + startDX * 4;
			int scalarXPos = startDX - x + radius;
			int endXRange = startPxOffset + xRange;
			for (int pxBase = startPxOffset, j = scalarXPos; pxBase < endXRange; pxBase += 4, ++j) {
				int s = scalar[j];
				count += s;
				r += buf[pxBase + 0] * s;
				g += buf[pxBase + 1] * s;
				b += buf[pxBase + 2] * s;
			}
			count = factor[count / COUNT_SCALAR];
			tmpBuf[curLineHead + 0] = (r * count) >> SHIFT_BITS;
			tmpBuf[curLineHead + 1] = (g * count) >> SHIFT_BITS;
			tmpBuf[curLineHead + 2] = (b * count) >> SHIFT_BITS;
		}
	}


	// vertical
	lineHead = 0;
	for (int y = 0; y < radius; ++y, lineHead += rowBytes) {
		for (int x = 0, curLineHead = lineHead; x < width; ++x, curLineHead += 4) {
			int count = COUNT_SCALAR - 1;
			unsigned int r = 0, g = 0, b = 0;
			int startDY = 0, endDY = std::min((int) height, y + radius);
			int scalarYPos = startDY - y + radius;

			for (int pxBase = rowBytes * startDY + x * 4, j = scalarYPos; startDY < endDY; pxBase += rowBytes, ++j, ++startDY) {
				int s = scalar[j];
				count += s;
				r += tmpBuf[pxBase + 0] * s;
				g += tmpBuf[pxBase + 1] * s;
				b += tmpBuf[pxBase + 2] * s;
			}

			count = factor[count / COUNT_SCALAR];
			buf[curLineHead + 0] = (r * count) >> SHIFT_BITS;
			buf[curLineHead + 1] = (g * count) >> SHIFT_BITS;
			buf[curLineHead + 2] = (b * count) >> SHIFT_BITS;
		}
	}


	lineHead = radius*rowBytes;
	for (int y = radius; y < height - radius; ++y, lineHead += rowBytes) {
		for (int x = 0, curLineHead = lineHead; x < width; ++x, curLineHead += 4) {
			int s = scalar[radius];
			int count = s + COUNT_SCALAR - 1;
			unsigned int r = tmpBuf[curLineHead + 0] * s;
			unsigned int g = tmpBuf[curLineHead + 1] * s;
			unsigned int b = tmpBuf[curLineHead + 2] * s;

			for (int i = 1, yDelta = rowBytes; i < radius; ++i, yDelta += rowBytes) {
				int s = scalar[radius+i];
				count += s*2;
				r += (tmpBuf[curLineHead + 0 - yDelta] + tmpBuf[curLineHead + 0 + yDelta]) * s;
				g += (tmpBuf[curLineHead + 1 - yDelta] + tmpBuf[curLineHead + 1 + yDelta]) * s;
				b += (tmpBuf[curLineHead + 2 - yDelta] + tmpBuf[curLineHead + 2 + yDelta]) * s;

			}
			count = factor[count / COUNT_SCALAR];
			buf[curLineHead + 0] = (r * count) >> SHIFT_BITS;
			buf[curLineHead + 1] = (g * count) >> SHIFT_BITS;
			buf[curLineHead + 2] = (b * count) >> SHIFT_BITS;
		}
	}


	lineHead = (height - radius)*rowBytes;
	for (int y = height - radius; y < height; ++y, lineHead += rowBytes) {
		for (int x = 0, curLineHead = lineHead; x < width; ++x, curLineHead += 4) {
			int count = COUNT_SCALAR - 1;
			unsigned int r = 0, g = 0, b = 0;
			int startDY = std::max(0, y - radius), endDY = height;
			int scalarYPos = startDY - y + radius;

			for (int pxBase = rowBytes * startDY + x * 4, j = scalarYPos; startDY < endDY; pxBase += rowBytes, ++j, ++startDY) {
				int s = scalar[j];
				count += s;
				r += tmpBuf[pxBase + 0] * s;
				g += tmpBuf[pxBase + 1] * s;
				b += tmpBuf[pxBase + 2] * s;
			}

			count = factor[count / COUNT_SCALAR];
			buf[curLineHead + 0] = (r * count) >> SHIFT_BITS;
			buf[curLineHead + 1] = (g * count) >> SHIFT_BITS;
			buf[curLineHead + 2] = (b * count) >> SHIFT_BITS;
		}
	}
	free(tmpBuf);
}


/**
 *
 * @param env       JNIEnv接口指针
 * @param clazz
 * @param bufObject Java Buffer对象
 * @param width
 * @param height
 * @param pass
 * @return
 */
JNIEXPORT jboolean JNICALL
Java_com_zeke_utils_WildFireUtils_doFastBlur(JNIEnv *env, jobject clazz, jobject bufObject,
                                             jint width, jint height, jint radius) {
	// 获取原生数组的内存地址
	ubyte *buf = (ubyte *) env->GetDirectBufferAddress(bufObject);
	if (buf == nullptr) {
		return JNI_FALSE;
	}
	if (GET_CPU_NUM() > 1) {
		__android_log_print(ANDROID_LOG_DEBUG, LOG_TAG, "do fast blur in parallel. radius: %d",radius);
		doFastBlurParallel(buf, width, height, radius);
	} else {
		__android_log_print(ANDROID_LOG_DEBUG, LOG_TAG, "do fast blur. radius: %d",radius);
		doFastBlur(buf, width, height, radius);
	}
	return JNI_TRUE;
}

}
#endif
