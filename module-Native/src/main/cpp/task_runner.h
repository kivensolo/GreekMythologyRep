#ifndef __TASK_RUNNER__
#define __TASK_RUNNER__

class runner_base {
public:
	virtual void * run() = 0;
};

template <typename _Runner, typename _ParamType>
class task_runner : public runner_base {
	_Runner &_runner;
	_ParamType _param;

public:
	task_runner(_Runner &runner, _ParamType param)
		: _runner(runner)
		, _param(param) {}

	virtual void * run() {
		this->_runner(&(this->_param));
		return 0;
	}
};

class thread_worker {
	volatile bool _terminate;
	runner_base * _runners[16];
	int _runner_count;
protected:
	void * _run() {
		while (!_terminate) {
			if (_runner_count == 0) {
				this->wait_task();
				continue;
			}
			while (_runner_count > 0) {
				_runner_count--;
				_runners[_runner_count]->run();
			}
			this->signal_task_finished();
		}
		return 0;
	}
	virtual void wait_task() = 0;
	virtual void signal_task_finished() = 0;
	void terminate() {
		_terminate = true;
		this->signal();
	}
public:
	thread_worker() : _terminate(false), _runner_count(0) {}
	void add_runner(runner_base *runner) {
		_runners[_runner_count++] = runner;
	}
	virtual void wait() = 0;
	virtual void signal() = 0;
	virtual void join() = 0;
};

#if _WIN32
class thread_impl : public thread_worker {
	HANDLE _tid;
	HANDLE _sem_task;
	HANDLE _sem_thread;
	static void *_thread_proc(thread_impl *t) {
		t->_run();
		t->_stop();
		return 0;
	}
	void _stop() {
		CloseHandle(_tid);
		CloseHandle(_sem_task);
		CloseHandle(_sem_thread);
	}

	void wait_task() {
		WaitForSingleObject(_sem_task, INFINITE);
	}
	void signal_task_finished() {
		ReleaseSemaphore(_sem_thread, 1, 0);
	}
public:
	thread_impl() {}

	void start() {
		_sem_task = CreateSemaphore(0, 0, 100, 0);
		_sem_thread = CreateSemaphore(0, 0, 100, 0);
		_tid = CreateThread(0, 0, (LPTHREAD_START_ROUTINE) thread_impl::_thread_proc, (void*) this, 0, 0);
	}

	void wait() {
		WaitForSingleObject(_sem_thread, INFINITE);
	}

	void signal() {
		ReleaseSemaphore(_sem_task, 1, 0);
	}

	void join() {
		this->terminate();
		WaitForSingleObject(_tid, INFINITE);
	}
};
int GET_CPU_NUM() {
	int _CPU_NUM = 0;
	if (_CPU_NUM == 0) {
		SYSTEM_INFO si;
		GetSystemInfo(&si);
		_CPU_NUM = si.dwNumberOfProcessors;
	}
	return _CPU_NUM;
}
#else
class pthread_sem {
	sem_t _sem;
public:
	pthread_sem(int value = 0, int shared = 0) {
		sem_init(&_sem, shared, value);
	}
	~pthread_sem() {
		sem_destroy(&_sem);
	}
	int wait() {
		return sem_wait(&_sem);
	}
	int signal(int n = 1) {
#if _WIN32
		if (n == 1) {
			return sem_post(&_sem);
		}
		return sem_post_multiple(&_sem, n);
#else
		return sem_post(&_sem);
#endif
	}
	int value() {
		int val = 0;
		sem_getvalue(&_sem, &val);
		return val;
	}
};

class thread_impl : public thread_worker {
	pthread_t _tid;
	pthread_sem _sem_task;
	pthread_sem _sem_thread;

	static void *_thread_proc(thread_impl *t) {
		t->_run();
		t->_stop();
		return 0;
	}
	void _stop() {
		_sem_task.~pthread_sem();
		_sem_thread.~pthread_sem();
	}
	void wait_task() {
		_sem_task.wait();
	}
	void signal_task_finished() {
		_sem_thread.signal();
	}
public:
	thread_impl() {}

	void start() {
		pthread_create(&_tid, 0, (void *(*)(void *))thread_impl::_thread_proc, (void*) this);
	}

	void wait() {
		_sem_thread.wait();
	}

	void signal() {
		_sem_task.signal();
	}

	void join() {
		this->terminate();
		pthread_join(_tid, 0);
	}
};

int GET_CPU_NUM() {
	static int _CPU_NUM = sysconf(_SC_NPROCESSORS_ONLN);
	return _CPU_NUM;
}
#endif

#endif // __TASK_RUNNER__
