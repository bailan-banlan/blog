<template>
    <div class="main-container">
        <div class="main">
            <div class="main-box">
                <div class="bannerBox">
                    <!-- 轮播图 -->
                    <el-carousel class="banner" :interval="5000" arrow="always">
                        <el-carousel-item v-for="(item, index) in bannerList" :key="index">
                            <a href="javascript:;" @click="handleClick(item.id)">
                                <el-image class="bannerImg" :src="item.avatar"></el-image>
                                <h3 class="title">{{ item.title }}</h3>
                            </a>
                        </el-carousel-item>
                    </el-carousel>
                </div>

                <div class="tuijian" style="">
                    <SiteInfo />
                    <Notcie />
                </div>
            </div>

            <!-- 热门分类 -->
            <div class="hot_category">
                <el-tabs v-model="activeName">
                    <el-tab-pane class="categoryItem" v-for="item in categoryList" :key="item.id">
                        <span @click="handleTabClick(item.id)" slot="label">
                            <i class="el-icon-folder-opened"></i>
                            {{ item.name }}
                        </span>
                    </el-tab-pane>
                </el-tabs>
            </div>

            <!-- 文章列表 -->
            <div class="content">
                <div class="articleBox" v-if="articleList.length > 0">
                    <el-card class="articleItem" v-for="item in articleList" :key="item.id">
                        <div class="articleInfo">
                            <div class="articleInfo-item">
                                <el-tooltip class="item" effect="dark" content="原创文章" placement="top-start">
                                    <span v-if="item.isOriginal" class="original">
                                        <i class="el-icon-tickets"></i>
                                    </span>
                                </el-tooltip>

                                <span v-if="item.isStick" class="top">置顶</span>
                                <h3 @click="handleClick(item.id)">{{ item.title }}</h3>
                                <p>
                                    {{ item.summary }}
                                </p>
                            </div>
                            <el-image class="articleImg" :src="item.avatar" fit="scale - down"></el-image>
                        </div>
                        <div class="bottumItem">
                            <div class="articleUser">
                                <el-avatar class="userAvatar" :src="item.userAvatar"></el-avatar>
                                <span>{{ item.username }}</span>
                            </div>

                            <div class="tag">
                                <el-tag style="margin-right: 8px; cursor: pointer;"
                                    :type="tagStyle[Math.round(Math.random() * 4)]" size="small" v-for="tag in item.tagList"
                                    :key="tag.id" @click="handleTagClike(tag)">{{ tag.name
                                    }}</el-tag>

                            </div>

                            <div class="articleOhter">

                                <span class="item">
                                    <i class="el-icon-view"></i>{{ item.quantity }}
                                </span>
                                <span class="item">
                                    <i class="el-icon-chat-dot-round"></i>{{ item.commentCount }}
                                </span>
                                <span class="item">
                                    <span v-if="item.isCollect">
                                        <i style="font-size: 1rem;" class="el-icon-star-on"></i>{{
                                            item.collectCount }}
                                    </span>
                                    <span v-else>
                                        <i style="font-size: 1rem;" class="el-icon-star-off"></i>{{ item.collectCount
                                        }}
                                    </span>
                                </span>
                                <span class="item">
                                    <i style="font-size: 0.8rem;" class="iconfont icon-dianzan1"></i>{{ item.likeCount }}
                                </span>
                                <span class="item">
                                    <i class="el-icon-time"></i>{{ item.createTime }}
                                </span>
                            </div>
                        </div>
                    </el-card>
                    <!-- 分页按钮 -->
                    <div class="page" v-if="pageData.pageNo < pageTotal" @click="onPage">
                        加载更多
                    </div>
                    <div style="text-align: center;color: var(--text-color);" v-else>
                        我也是有底线的--
                    </div>
                </div>
                <el-empty style=" width: 100%;" v-else description="很抱歉，暂无文章"></el-empty>
                <div class="rightBox">
                    <!-- 登录提示框 -->
                    <el-card v-if="!$store.state.userInfo" class="box-card">
                        <div style="margin-bottom: 15px;margin-top: 10px;">
                            登录网站，开启你的创作之旅：
                        </div>
                        <div style="margin-bottom: 15px;margin-top: 30px;">
                            <span>
                                <i class="el-icon-cloudy"></i>参与互动评论
                            </span>
                            <span style="margin-left: 20px;">
                                <i class="el-icon-cloudy"></i>发表优质文章
                            </span>

                            <el-button style="margin-top: 20px;width: 100%;" size="small" type="success"
                                @click="handleLogin">立即登录</el-button>
                        </div>
                    </el-card>
                    <!-- 个人信息 -->
                    <el-card v-else class="box-card myUserInfo">
                        <div style="margin-bottom: 15px;margin-top: 10px;">
                            <a href="javascript:;" @click="$store.state.userInfoDrawer = true" style="display: flex;">
                                <el-avatar style="border: 1px solid var(--border-line);"
                                    :src="$store.state.userInfo.avatar"></el-avatar>
                                <div class="userInfo">
                                    <p class="nickname">{{ $store.state.userInfo.nickname }}</p>
                                    <p class="intor">{{ $store.state.userInfo.intro ? $store.state.userInfo.intro
                                        : '这个博主很懒，什么都没有留下' }}</p>
                                </div>
                            </a>
                        </div>
                        <div style="margin-bottom: 15px;margin-top: 30px;display: flex;">
                            <span class="myArticle">
                                <div>
                                    0
                                </div>
                                <div class="name">
                                    文章
                                </div>
                            </span>
                            <span class="myComment">
                                <div>
                                    0
                                </div>
                                <div class="name">
                                    评论
                                </div>
                            </span>
                        </div>
                    </el-card>
                    <!-- 关注我 -->
                    <el-card class="box-card guanzhu">
                        <div slot="header" class="clearfix">
                            <span>关注我</span>
                        </div>
                        <ul class="guanzhuList">
                            <li v-show="isShow(2)">
                                <div class="item qq">
                                    <svg-icon icon-class="qq" />
                                    <a :href="'//wpa.qq.com/msgrd?v=3&amp;uin=' + $store.state.webSiteInfo.qqNumber + '&amp;site=qq&amp;menu=yes'"
                                        target="_blank">
                                        {{ $store.state.webSiteInfo.qqNumber }}
                                    </a>
                                    <span title="点击复制" @click="copy($store.state.webSiteInfo.qqNumber)"
                                        class="copyBtn name">
                                        QQ号
                                    </span>
                                </div>
                            </li>
                            <li v-show="isShow(3)">
                                <div class="item github">
                                    <svg-icon icon-class="github" />
                                    <a :href="$store.state.webSiteInfo.github" target="_blank">
                                        {{ $store.state.webSiteInfo.github }}
                                    </a>
                                    <span title="点击复制" @click="copy($store.state.webSiteInfo.github)" class="copyBtn name">
                                        github
                                    </span>
                                </div>
                            </li>
                            <li v-show="isShow(4)">
                                <div class="item gitee">
                                    <svg-icon icon-class="gitee" />
                                    <a :href="$store.state.webSiteInfo.gitee" target="_blank">
                                        {{ $store.state.webSiteInfo.gitee }}
                                    </a>
                                    <span title="点击复制" @click="copy($store.state.webSiteInfo.gitee)" class="copyBtn name">
                                        gitee
                                    </span>
                                </div>
                            </li>
                            <li v-show="isShow(1)">
                                <div class="item email">
                                    <svg-icon icon-class="email" />
                                    <a :href="'mailto:' + $store.state.webSiteInfo.email" target="_blank" title="邮箱">
                                        {{ $store.state.webSiteInfo.email }}
                                    </a>
                                    <span title="点击复制" @click="copy($store.state.webSiteInfo.email)" class="copyBtn name">
                                        邮箱
                                    </span>
                                </div>
                            </li>
                            <li v-show="isShow(5)">
                                <div class="item wechat">
                                    <svg-icon icon-class="wechat" /> {{ $store.state.webSiteInfo.wechat }}
                                    <span title="点击复制" @click="copy($store.state.webSiteInfo.wechat)" class="copyBtn name">
                                        微信
                                    </span>
                                </div>
                            </li>
                        </ul>
                    </el-card>
                    <!-- 推荐文章 -->
                    <el-card class="box-card recomArticle">
                        <div slot="header" class="clearfix">
                            <span>推荐文章</span>
                        </div>
                        <ul class="recomArticleUl">
                            <li v-for="(item, index) in newArticleList">
                                <div class="item">
                                    <el-image style="float: left;" :src="item.avatar" fit="fit" />
                                    <p class="info">
                                        <a href="javascript:;" @click="handleClick(item.id)">{{ item.title }}</a>
                                    </p>
                                    <p class="time">{{ item.createTime }}</p>
                                </div>
                            </li>
                        </ul>
                    </el-card>
                    <!-- 标签墙 -->
                    <el-card class="box-card tag_container">
                        <div slot="header" class="clearfix">
                            <span>标签墙</span>
                            <a href="/tag" class="more">更多</a>
                        </div>
                        <div class="tag">
                            <span @click="handleTagClike(item)" :style="{ backgroundColor: `${randomColor()}` }"
                                class="item" v-for="(item, index) in tagList" :key="index">
                                {{ item.name }}
                            </span>
                        </div>
                    </el-card>
                </div>
            </div>
        </div>
    </div>
</template>

<script>
import { fetchArticleList, featchHomeData, featchCategory } from '@/api'
import SiteInfo from '@/components/site/index.vue'
import Notcie from '@/components/notice/index.vue'
export default {
    components: {
        SiteInfo,
        Notcie
    },
    name: 'Home',
    data() {
        return {
            emojis: [],
            pageData: {
                pageNo: 1,
                pageSize: 8,
            },
            // 加载层信息
            loading: [],
            activeName: null,
            bannerList: [],
            categoryList: [
                {
                    id: null,
                    name: "最新"
                }
            ],
            articleList: [],
            pageTotal: 0,
            tagList: [],
            newArticleList: [],
            tagStyle: ['', 'success', 'info', 'warning', 'danger']

        };
    },
    // require引用：
    mounted() {
        this.fetchArticleList()
        this.getHomeData()
        this.fetchCategoryList()
    },
    methods: {
        handleTagClike(item) {
            this.$router.push({ name: "/tags", query: { id: item.id, name: item.name } })
        },
        randomColor() {
            var letters = '0123456789ABCDEF';
            var color = '#';
            do {
                for (var i = 0; i < 6; i++) {
                    color += letters[Math.floor(Math.random() * 16)];
                }
            } while (color === '#FFFFFF' || color === '#000000');
            return color;
        },
        isShow(type) {
            return this.$store.state.webSiteInfo.showList.indexOf(type) != -1
        },
        copy(value) {
            const clipboard = new this.Clipboard('.copyBtn', {
                text: () => value
            })
            clipboard.on('success', () => {
                this.$message.success("复制成功");
                clipboard.destroy()
            })
            clipboard.on('error', () => {
                this.$message.error('复制失败');
                clipboard.destroy()
            })
        },
        handleTabClick(id) {
            this.openLoading()
            this.pageData.pageNo = 1
            this.pageData.categoryId = id
            fetchArticleList(this.pageData).then(res => {
                this.articleList = res.data.records;
                this.pageTotal = res.data.pages
            })
            this.loading.close()
        },
        handleLogin() {
            this.$store.state.loginFlag = true
        },
        // 分页
        onPage() {

            this.pageData.pageNo++;
            this.fetchArticleList()

        },
        fetchArticleList() {
            this.openLoading()
            fetchArticleList(this.pageData).then(res => {
                this.articleList.push(...res.data.records);
                this.pageTotal = res.data.pages
                this.loading.close()
            })
        },
        fetchCategoryList() {
            featchCategory().then(res => {
                this.categoryList.push(...res.data)
            })
        },
        getHomeData() {
            featchHomeData().then(res => {
                //this.categoryList = res.extra.categories
                this.bannerList = res.extra.articles
                this.tagList = res.extra.tagCloud
                this.newArticleList = res.extra.newArticleList
            })
        },
        handleClick(id) {
            if (id == null) {
                this.$message.info("待开发！")
                return;
            }
            this.$router.push({ path: '/articleInfo', query: { articleId: id } })
        },
        handleCategoryClike(item) {
            this.$router.push({ name: "/category", query: { id: item.id, name: item.name } })
        },
        // 打开加载层
        openLoading: function () {
            this.loading = this.$loading({
                lock: true,
                text: "正在加载中~",
                spinner: "el-icon-loading",
                background: "rgba(0, 0, 0, 0.7)"
            });
        },
    },
}
</script>

<style lang="scss" scoped >
.page {
    text-align: center;
    background-color: rgba(0, 0, 0, .8);
    width: 120px;
    height: 30px;
    line-height: 30px;
    border-radius: 50px;
    margin: 0 auto;
    margin-top: 20px;
    cursor: pointer;
    color: #fff;
}

/deep/ .el-tabs__item {
    color: var(--article-color);
}

/deep/ .is-active {
    color: #409EFF;
}

/deep/ .el-tabs__nav-wrap::after {
    background-color: var(--background-color);
}

/deep/ .el-tabs__active-bar {
    background-color: var(--theme-color);
}

.banner {
    position: relative;
    height: 420px;

    /deep/ .el-carousel__container {
        height: 100%;
    }

    .title {
        position: absolute;
        height: 50px;
        line-height: 50px;
        text-align: center;
        width: 100%;
        bottom: 50px;
        color: #fff;
        background: rgba(0, 0, 0, .3);
        z-index: 1;
    }

    .bannerImg {
        width: 100%;
        height: 100%;
    }
}

@media screen and (max-width: 1118px) {
    .main-container {
        display: flex;
        justify-content: center;
        padding-left: 10px;
        padding-right: 10px;

        .main {
            width: 100%;
            margin-top: 70px;

            .main-box {
                width: 100%;
                display: flex;

                .bannerBox {
                    width: 100%;

                    .banner {
                        height: 200px;
                    }
                }

                .tuijian {
                    display: none;
                }
            }


            .hot_category {
                margin-top: 30px;
            }

            .content {
                display: flex;

                .articleBox {
                    font-size: 1rem;
                    width: 100%;

                    .articleItem {

                        position: relative;
                        padding: 10px 5px 10px 15px;
                        background-color: var(--background-color);
                        margin-bottom: 10px;
                        transition: box-shadow .35s, transform .35s;

                        &:hover {
                            box-shadow: 3px 3px 3px rgba(0, 0, 0, 0.2);
                            transform: translateY(-2px)
                        }


                        .articleInfo {
                            display: flex;
                            color: var(--article-color);

                            .original {
                                content: '';
                                position: absolute;
                                right: 0;
                                top: 0;
                                width: 0;
                                height: 0;
                                border-color: transparent #40c9c6;
                                /*上下颜色 左右颜色*/
                                border-width: 0 40px 40px 0;
                                border-style: solid;

                                i {
                                    position: absolute;
                                    top: 4px;
                                    right: -37px;
                                    font-size: 16px;
                                    color: aquamarine;
                                }
                            }

                            .articleInfo-item {
                                width: 100%;
                                height: 100px;

                                .top {
                                    background-image: -webkit-linear-gradient(0deg, #3ca5f6 0, #a86af9 100%);
                                    padding-left: 5px;
                                    padding-right: 5px;
                                    display: inline-block;
                                    border-top-right-radius: 5px;
                                    border-bottom-left-radius: 5px;
                                    font-size: 0.9rem;
                                    margin-right: 5px;
                                    color: #fff;
                                }

                                h3 {
                                    cursor: pointer;
                                    font-size: 16px;
                                    display: inline-block;

                                    &:hover {
                                        color: var(--theme-color);
                                    }
                                }

                                p {
                                    margin-top: 10px;
                                    overflow: hidden;
                                    text-overflow: ellipsis;
                                    display: -webkit-box;
                                    -webkit-box-orient: vertical;
                                    -webkit-line-clamp: 4;
                                }
                            }

                            .articleImg {
                                display: none;
                            }
                        }

                        .bottumItem {
                            align-items: center;
                            display: flex;
                            margin-top: 20px;
                            flex-wrap: wrap;


                            .articleUser {
                                font-size: 0.8rem;
                                width: 50%;

                                span {
                                    color: var(--theme-color);
                                    margin-left: 3px;
                                    line-height: 40px;
                                }

                                .userAvatar {
                                    vertical-align: top;
                                    border: 1px solid var(--border-line);

                                }
                            }

                            .tag {
                                display: inline-block;
                                width: 50%
                            }

                            .articleOhter {
                                font-size: 0.8rem;
                                color: var(--text-color);
                                display: block;

                                .item {
                                    margin-right: 8px;

                                    i {
                                        margin-right: 3px;
                                    }
                                }
                            }
                        }

                    }

                }

                .rightBox {
                    display: none;
                }
            }
        }

    }
}

@media screen and (min-width: 1119px) {

    .main-container {
        display: flex;
        justify-content: center;

        .main {
            width: 70%;
            margin-top: 80px;

            .main-box {
                width: 100%;
                display: flex;

                .bannerBox {
                    width: 70%;
                }

                .tuijian {
                    display: inline-block;
                    width: 30%;
                    height: 420px;
                    margin-left: 20px;

                    /deep/ .el-image {
                        width: 100%;
                        height: 47%;
                        cursor: pointer;

                        &:last-child {
                            margin-top: 20px;
                        }
                    }
                }
            }


            .hot_category {
                margin-top: 30px;

                /deep/ .el-tabs__item:hover span {
                    color: #409EFF;
                }
            }

            .content {
                display: flex;

                .articleBox {
                    font-size: 1rem;
                    width: 100%;

                    .articleItem {

                        position: relative;
                        padding: 10px 5px 10px 15px;
                        background-color: var(--background-color);
                        margin-bottom: 20px;
                        transition: box-shadow .35s, transform .35s;

                        &:hover {
                            box-shadow: 3px 3px 3px rgba(0, 0, 0, 0.2);
                            transform: translateY(-2px)
                        }


                        .articleInfo {
                            display: flex;
                            color: var(--article-color);

                            .original {
                                content: '';
                                position: absolute;
                                right: 0;
                                top: 0;
                                width: 0;
                                height: 0;
                                border-color: transparent #40c9c6;
                                /*上下颜色 左右颜色*/
                                border-width: 0 40px 40px 0;
                                border-style: solid;

                                i {
                                    position: absolute;
                                    top: 4px;
                                    right: -37px;
                                    font-size: 16px;
                                    color: aquamarine;
                                }
                            }

                            .articleInfo-item {
                                width: 60%;
                                height: 100px;

                                .top {
                                    background-image: -webkit-linear-gradient(0deg, #3ca5f6 0, #a86af9 100%);
                                    padding-left: 5px;
                                    padding-right: 5px;
                                    display: inline-block;
                                    border-top-right-radius: 5px;
                                    border-bottom-left-radius: 5px;
                                    font-size: 0.9rem;
                                    margin-right: 5px;
                                    color: #fff;
                                }

                                h3 {
                                    cursor: pointer;
                                    position: relative;
                                    display: inline-block;

                                    &::after {
                                        content: "";
                                        height: 2px;
                                        overflow: hidden;
                                        display: block;
                                        left: 0;
                                        bottom: -5px;
                                        position: absolute;
                                        width: 100%;
                                        background: var(--theme-color);
                                        transform: scaleX(0);
                                        transition: all 0.5s;
                                    }

                                    &:hover {
                                        &::after {
                                            transform: scaleX(1);
                                        }

                                        color: var(--theme-color);
                                    }

                                }

                                p {
                                    margin-top: 10px;
                                    overflow: hidden;
                                    text-overflow: ellipsis;
                                    display: -webkit-box;
                                    -webkit-box-orient: vertical;
                                    -webkit-line-clamp: 3;
                                }
                            }

                            .articleImg {
                                width: 20%;
                                height: 100px;
                                position: absolute;
                                right: 20px;
                                top: 20px;
                                border-radius: 3px;

                                /deep/ .el-image__inner {
                                    transition: all 0.5s;
                                }

                                &:hover {
                                    /deep/ .el-image__inner {
                                        transform: scale(1.1);

                                    }
                                }
                            }
                        }

                        .bottumItem {
                            align-items: center;
                            height: 50px;
                            display: flex;
                            margin-top: 20px;

                            .articleUser {
                                line-height: 50px;
                                font-size: 0.8rem;

                                span {
                                    color: var(--theme-color);
                                    margin-left: 3px;
                                }

                                .userAvatar {
                                    vertical-align: top;
                                    border: 1px solid var(--border-line);
                                    transition: transform .5s;

                                    &:hover {
                                        transform: rotate(360deg);
                                    }
                                }
                            }

                            .tag {
                                display: inline-block;
                                margin-left: 20px;
                            }

                            .articleOhter {
                                margin-left: 20px;
                                font-size: 0.8rem;
                                color: var(--text-color);

                                .item {
                                    margin-right: 8px;

                                    .item-img {
                                        overflow: hidden;
                                        width: 100px;
                                        height: 75px;
                                        display: inline-block;
                                    }

                                    i {
                                        margin-right: 3px;
                                    }
                                }
                            }
                        }

                    }

                }

                .rightBox {
                    width: 43%;
                    margin-left: 20px;

                    .box-card {
                        font-size: 16px;
                        padding: 12px;
                        background-color: var(--background-color);
                        color: var(--article-color);

                        &:hover {
                            .clearfix {
                                &::before {
                                    content: '';
                                    width: 70px;
                                }
                            }
                        }

                        .clearfix {
                            position: relative;

                            &::before {
                                content: '';
                                width: 25px;
                                border: 2px solid #333;
                                position: absolute;
                                bottom: -10px;
                                transition: width .5s;
                            }
                        }

                    }

                    .myUserInfo {
                        a {
                            text-decoration: none;

                        }

                        .userInfo {
                            margin-left: 10px;

                            .nickname {
                                color: var(--article-color);
                                font-weight: 700;
                            }

                            .intor {
                                font-size: 0.9rem;
                                color: var(--text-color);
                                margin-top: 2px;
                            }
                        }

                        .myArticle,
                        .myComment {
                            text-align: center;
                            margin-left: 20px;

                            .name {
                                margin-top: 2px;
                            }
                        }
                    }

                    .recomArticle {
                        margin-top: 20px;
                        color: var(--article-color);

                        .recomArticleUl {
                            display: flex;
                            padding: 10px;
                            list-style: none;
                            flex-wrap: wrap;

                            li {
                                width: 100%;
                                margin-bottom: 20px;

                                &:last-child {
                                    margin-bottom: 10px;
                                }

                                &:hover {
                                    /deep/ .el-image__inner {
                                        transform: scale(1.1);
                                    }
                                }


                            }

                            .item {
                                height: 100%;

                                /deep/ .el-image {
                                    width: 100px;
                                    height: 75px;
                                    margin-right: 10px;
                                }

                                /deep/ .el-image__inner {
                                    transition: all 0.5s;
                                    margin-right: 10px;
                                }

                                .info a {
                                    text-decoration: none;
                                    color: var(--article-color);
                                    overflow: hidden;
                                    text-overflow: ellipsis;
                                    display: -webkit-box;
                                    -webkit-box-orient: vertical;
                                    -webkit-line-clamp: 2;

                                    &:hover {
                                        color: var(--theme-color);
                                    }
                                }


                                .time {
                                    font-size: 0.8rem;
                                    color: var(--text-color);
                                    margin-top: 10px;
                                }
                            }



                        }
                    }

                    .guanzhu {
                        margin-top: 20px;

                        .guanzhuList {
                            padding: 15px;
                            list-style: none;

                            .item {
                                margin-bottom: 20px;
                                height: 40px;
                                line-height: 40px;
                                font-size: 12px;
                                display: flex;
                                position: relative;

                                a {
                                    text-decoration: none;
                                    color: var(--text-color);
                                    overflow: hidden;
                                    text-overflow: ellipsis;
                                    white-space: nowrap;
                                    width: 100%;
                                    flex: 1;

                                    &:hover {
                                        color: var(--theme-color);
                                    }
                                }

                                .name {
                                    position: absolute;
                                    right: 0;
                                    width: 60px;
                                    text-align: center;
                                    cursor: pointer;
                                    color: #fff;
                                    transition: width .35s;
                                }

                                &:hover .name {
                                    width: 80px;
                                }

                                svg {
                                    width: 20px;
                                    height: 20px;
                                    margin-left: 5px;
                                    position: relative;
                                    bottom: -10px;
                                    margin-right: 10px;
                                }
                            }

                            .github {
                                border: 1px solid #606266;

                                .name {
                                    background-color: #606266;
                                }
                            }

                            .gitee {
                                border: 1px solid red;

                                .name {
                                    background-color: red;
                                }
                            }

                            .email {
                                border: 1px solid #F56C87;

                                .name {
                                    background-color: #F56C87;
                                }
                            }

                            .wechat {
                                border: 1px solid #67C23A;

                                .name {
                                    background-color: #67C23A;
                                }
                            }

                            .qq {
                                border: 1px solid #409EFF;

                                .name {
                                    background-color: #409EFF;
                                }
                            }


                        }
                    }

                    .tag_container {
                        margin-top: 20px;
                        font-size: 0.9rem;
                        background-color: var(--background-color);
                        color: var(--text-color);

                        &:hover {
                            transition: all .3s;
                        }

                        .clearfix {
                            font-size: 16px;
                            font-weight: 700;

                            .more {
                                float: right;
                                margin-right: 10px;
                                font-size: 13px;
                                color: var(--text-color);
                                text-decoration: none;
                            }
                        }

                        .tag {
                            padding-left: 10px;
                            padding-bottom: 10px;
                            height: auto;
                            margin-top: 5px;

                            span {
                                text-decoration: none;
                                margin-left: 15px;
                                padding: 5px;
                                display: inline-block;
                                margin-bottom: 10px;
                                border-radius: 8px;
                                color: #fff;
                                cursor: pointer;
                                transition: all .3s;

                                &:hover {
                                    border-radius: 0;
                                }
                            }
                        }

                    }
                }
            }
        }

    }

}
</style>