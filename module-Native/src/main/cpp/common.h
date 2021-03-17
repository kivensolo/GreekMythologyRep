#if _WIN32
#	define NOMINMAX
#	include <windows.h>
#endif

#include <stdio.h>
#include <stdlib.h>
#include <string.h>

#include <string>
#include <map>
#include <iterator>
#include <algorithm>

#if _WIN32
#	define ANDROID_LOG_DEBUG	0
#	define __android_log_write	(void)
#else
#	include <android/log.h>
#	include <pthread.h>
#	include <semaphore.h>
#	include <unistd.h>
#endif

typedef unsigned char ubyte;
typedef unsigned int uint32_t;

extern "C" void doBlur(ubyte* buf, int width, int height);
extern "C" void doFastBlur(ubyte* buf, int width, int height, int radius);
extern "C" void doFastBlurParallel(ubyte* buf, int width, int height, int radius);
