package com.kingz.library.player.render

import com.kingz.library.player.IPlayer


/**
 * 渲染View
 */
interface IRender {

    var renderCallback: RenderCallback?

    val iPlayer: IPlayer

}