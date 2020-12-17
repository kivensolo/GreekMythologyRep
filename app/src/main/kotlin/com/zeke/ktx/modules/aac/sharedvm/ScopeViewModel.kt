package com.zeke.ktx.modules.aac.sharedvm

import androidx.lifecycle.ViewModel

/**
 * author: King.Z <br>
 * date:  2020/12/17 23:32 <br>
 * description:  <br>
 */
const val DEFAULT_SCOPE = "default_vm_scope"

@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FIELD)
annotation class VMScope(val name:String)

class ScopeViewModel: ViewModel()