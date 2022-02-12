package com.zeke.reactivehttp.viewmodel

import kotlinx.coroutines.Job

/**
 * 定义ViewMode ===> View 的UI行为事件
 */
open class BaseActionEvent

class ShowLoadingEvent(val job: Job?) : BaseActionEvent()

object DismissLoadingEvent : BaseActionEvent()

object FinishViewEvent : BaseActionEvent()

class ShowToastEvent(val message: String) : BaseActionEvent()