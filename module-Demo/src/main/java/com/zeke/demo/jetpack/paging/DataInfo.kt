package com.zeke.demo.jetpack.paging

/**
 * author: King.Z <br>
 * date:  2021/8/22 21:03 <br>
 * description:  <br>
 */
data class ReqresUserList(
    val `data`: List<Data>,
    val page: Int,
    val per_page: Int,
    val support: Support,
    val total: Int,
    val total_pages: Int
)

data class Data(
    val avatar: String,
    val email: String,
    val first_name: String,
    val id: Int,
    val last_name: String
)

data class Support(
    val text: String,
    val url: String
)