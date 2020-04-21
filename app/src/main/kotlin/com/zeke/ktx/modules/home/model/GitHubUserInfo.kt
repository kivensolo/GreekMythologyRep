package com.zeke.ktx.modules.home.model

/**
 * author: King.Z <br></br>
 * date:  2020/3/4 21:39 <br></br>
 * description: GitHub用户信息类 <br></br>
 */
class GitHubUserInfo {
    /**
     * login : kivensolo
     * id : 15629155
     * node_id : MDQ6VXNlcjE1NjI5MTU1
     * avatar_url : https://avatars0.githubusercontent.com/u/15629155?v=4
     * gravatar_id :
     * url : https://api.github.com/users/kivensolo
     * html_url : https://github.com/kivensolo
     * followers_url : https://api.github.com/users/kivensolo/followers
     * following_url : https://api.github.com/users/kivensolo/following{/other_user}
     * gists_url : https://api.github.com/users/kivensolo/gists{/gist_id}
     * starred_url : https://api.github.com/users/kivensolo/starred{/owner}{/repo}
     * subscriptions_url : https://api.github.com/users/kivensolo/subscriptions
     * organizations_url : https://api.github.com/users/kivensolo/orgs
     * repos_url : https://api.github.com/users/kivensolo/repos
     * events_url : https://api.github.com/users/kivensolo/events{/privacy}
     * received_events_url : https://api.github.com/users/kivensolo/received_events
     * type : User
     * site_admin : false
     * name : KingZ
     * company : Starcor
     * blog : https://www.starcor.com
     * location : Chengdu,Sichuan, China
     * email : null
     * hireable : null
     * bio : null
     * public_repos : 33
     * public_gists : 0
     * followers : 5
     * following : 16
     * created_at : 2015-11-03T11:05:34Z
     * updated_at : 2020-03-04T08:57:59Z
     */
    var login: String? = null
    var id = 0
    var node_id: String? = null
    var avatar_url: String? = null
    var gravatar_id: String? = null
    var url: String? = null
    var html_url: String? = null
    var followers_url: String? = null
    var following_url: String? = null
    var gists_url: String? = null
    var starred_url: String? = null
    var subscriptions_url: String? = null
    var organizations_url: String? = null
    var repos_url: String? = null
    var events_url: String? = null
    var received_events_url: String? = null
    var type: String? = null
    var isSite_admin = false
    var name: String? = null
    var company: String? = null
    var blog: String? = null
    var location: String? = null
    var email: Any? = null
    var hireable: Any? = null
    var bio: Any? = null
    var public_repos = 0
    var public_gists = 0
    var followers = 0
    var following = 0
    var created_at: String? = null
    var updated_at: String? = null

    override fun toString(): String {
        return "GitHubUserInfo{" +
                "login='" + login + '\'' +
                ", id=" + id +
                ", node_id='" + node_id + '\'' +
                ", avatar_url='" + avatar_url + '\'' +
                ", gravatar_id='" + gravatar_id + '\'' +
                ", url='" + url + '\'' +
                ", html_url='" + html_url + '\'' +
                ", followers_url='" + followers_url + '\'' +
                ", following_url='" + following_url + '\'' +
                ", gists_url='" + gists_url + '\'' +
                ", starred_url='" + starred_url + '\'' +
                ", subscriptions_url='" + subscriptions_url + '\'' +
                ", organizations_url='" + organizations_url + '\'' +
                ", repos_url='" + repos_url + '\'' +
                ", events_url='" + events_url + '\'' +
                ", received_events_url='" + received_events_url + '\'' +
                ", type='" + type + '\'' +
                ", site_admin=" + isSite_admin +
                ", name='" + name + '\'' +
                ", company='" + company + '\'' +
                ", blog='" + blog + '\'' +
                ", location='" + location + '\'' +
                ", email=" + email +
                ", hireable=" + hireable +
                ", bio=" + bio +
                ", public_repos=" + public_repos +
                ", public_gists=" + public_gists +
                ", followers=" + followers +
                ", following=" + following +
                ", created_at='" + created_at + '\'' +
                ", updated_at='" + updated_at + '\'' +
                '}'
    }
}