import request from '@/utils/request'

export function fetchArticleList(params) {
    return request({
        url: '/v1/article/',
        method: 'get',
        params: params
    })
}
export function getMyArticle(params) {
    return request({
        url: '/v1/article/selectMyArticle',
        method: 'get',
        params: params
    })
}
export function deleteMyArticle(id) {
    return request({
        url: '/v1/article/',
        method: 'delete',
        params: {
            id: id
        }
    })
}
export function getMyArticleInfo(id) {
    return request({
        url: '/v1/article/selectMyArticleInfo',
        method: 'get',
        params: {
            id: id
        }
    })
}
export function insertArticle(data) {
    return request({
        url: '/v1/article/',
        method: 'post',
        data
    })
}
export function searchArticle(params) {
    return request({
        url: '/v1/article/search',
        method: 'get',
        params: params
    })
}

export function articleInfo(id) {
    return request({
        url: '/v1/article/info',
        method: 'get',
        params: {
            id: id
        }
    })
}
export function checkCode(code) {
    return request({
        url: '/v1/article/checkCode',
        method: 'get',
        params: {
            code: code
        }
    })
}
export function articleLike(id) {
    return request({
        url: '/v1/article/like',
        method: 'get',
        params: {
            articleId: id
        }
    })
}
export function archive() {
    return request({
        url: '/v1/article/archive',
        method: 'get',
    })
}

export function fetchTagList() {
    return request({
        url: '/v1/tag/',
        method: 'get',

    })
}

export function featchComments(params) {
    return request({
        url: '/v1/comment/',
        method: 'get',
        params: params
    })
}
export function newArticle() {
    return request({
        url: '/v1/upToDateArticle',
        method: 'get',
    })
}
export function featchLinks() {
    return request({
        url: '/v1/link/',
        method: 'get',
    })
}
export function addLink(data) {
    return request({
        url: '/v1/link/',
        method: 'post',
        data
    })
}
export function postComment(data) {
    return request({
        url: '/v1/comment/',
        method: 'post',
        data
    })
}

export function featchHomeData() {
    return request({
        url: '/v1/',
        method: 'get',
    })
}
export function getHot(type) {
    return request({
        url: '/v1/hot',
        method: 'get',
        params: {
            type: type
        }
    })
}
export function report() {
    return request({
        url: '/v1/report',
        method: 'get',
    })
}
export function getWebSiteInfo() {
    return request({
        url: '/v1/webSiteInfo',
        method: 'get',
    })
}
export function listMessage() {
    return request({
        url: '/v1/message/',
        method: 'get',
    })
}
export function addMessage(data) {
    return request({
        url: '/v1/message/',
        method: 'post',
        data
    })
}

export function emailLogin(data) {
    return request({
        url: '/oauth/emailLogin',
        method: 'post',
        data
    })
}
export function logout() {
    return request({
        url: '/logout',
        method: 'get'
    })
}
export function openAuthUrl(source) {
    return request({
        url: '/oauth/render/' + source,
        method: 'get'
    })
}
export function wxIsLogin(loginCode) {
    return request({
        url: '/oauth/wechat/is_login',
        method: 'get',
        params: {
            loginCode: loginCode
        }
    })
}
export function getWechatLoginCode() {
    return request({
        url: '/oauth/wechatLoginCode',
        method: 'get',

    })
}
export function updateUserInfo(data) {
    return request({
        url: '/v1/user/',
        method: 'put',
        data
    })
}
export function getUserInfo() {
    return request({
        url: '/v1/user/info',
        method: 'get'
    })
}
export function selectUserInfoByToken(token) {
    return request({
        url: '/v1/user/selectUserInfoByToken',
        method: 'get',
        params: {
            token: token
        }
    })
}
export function upload(data) {
    return request({
        url: '/file/upload',
        method: 'POST',
        headers: { 'Content-Type': 'multipart/articles-data' },
        data
    })
}
export function updatePassword(data) {
    return request({
        url: '/system/user/updatePassword',
        method: 'post',
        data
    })
}

export function featchCategory() {
    return request({
        url: '/v1/category/',
        method: 'get'
    })
}

export function featchPhotoAlbum() {
    return request({
        url: '/v1/photoAlbum/',
        method: 'get'
    })
}

export function featchPhoto(params) {
    return request({
        url: '/v1/photo/',
        method: 'get',
        params: params
    })
}
export function getPayUrl(params) {
    return request({
        url: '/v1/sponsor/createOrder',
        method: 'get',
        params: params
    })
}
// 聊天室接口
export function getImHistory(params) {
    return request({
        url: '/v1/im/',
        method: 'get',
        params: params
    })
}
export function getUserImHistoryList(params) {
    return request({
        url: '/v1/im/selectUserImHistory',
        method: 'get',
        params: params
    })
}
export function addFeedback(data) {
    return request({
        url: '/v1/feedback/',
        method: 'post',
        data
    })
}
export function getCollect() {
    return request({
        url: '/v1/collect/',
        method: 'get'
    })
}
export function collect(id) {
    return request({
        url: '/v1/collect/collect',
        method: 'get',
        params: {
            articleId: id
        }
    })
}
export function cancelCollect(id) {
    return request({
        url: '/v1/collect/',
        method: 'delete',
        params: {
            articleId: id
        }
    })
}