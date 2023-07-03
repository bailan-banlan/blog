<template>
    <div class="artcile_main">
        <div class="box">
            <div class="title">
                <span>
                    <i style="color: var(--theme-color);font-weight: 700;margin-right: 5px;"
                        class="iconfont icon-search"></i>
                    以下是
                    <span style="color: var(--theme-color);font-weight: 700;">{{ pageData.keyword }}</span>
                    相关的文章：
                </span>
            </div>
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
                            <h3 @click="handleClick(item.id)" v-html="item.title"></h3>
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
                                <i style="font-size: 0.8rem;" class="iconfont icon-dianzan1"></i>{{ item.likeCount }}
                            </span>
                            <span class="item">
                                <i class="el-icon-time"></i>{{ item.createTime }}
                            </span>
                        </div>
                    </div>
                </el-card>
                <!-- 分页按钮 -->
                <div class="page" v-if="pageData.pageNo < pages" @click="onPage">
                    加载更多
                </div>
                <div style="text-align: center;color: var(--text-color);" v-else>
                    我也是有底线的--
                </div>
            </div>
            <div class="empty-box" v-else>
                <el-empty description="哎呀，文章丢失啦..."></el-empty>
            </div>
        </div>

    </div>
</template>
<script>
import { searchArticle } from '@/api'

export default {

    data() {
        return {
            pageData: {
                pageNo: 1,
                pageSize: 6,
                keyword: this.$route.query.keyword,
            },
            pages: 0,
            total: 0,
            tagStyle: ['', 'success', 'info', 'warning', 'danger'],
            articleList: [],
            loading: [],
        }
    },

    created() {
        this.fetchArticleList()
    },
    methods: {
        handleTagClike(item) {
            this.$router.push({ name: "/tags", query: { id: item.id, name: item.name } })
        },
        // 分页
        onPage() {
            this.pageData.pageNo++;
            this.fetchArticleList()
        },
        handleClick(id) {
            this.$router.push({ path: '/articleInfo', query: { articleId: id } })
        },
        fetchArticleList() {
            this.openLoading()
            searchArticle(this.pageData).then(res => {
                this.articleList.push(...res.data.records);
                this.pages = res.data.pages
                this.total = res.data.total
            })
            this.loading.close()
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
    }
}
</script>
<style lang="scss" scoped>
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

@media screen and (max-width: 1118px) {
    .artcile_main {
        min-height: calc(100vh - 167px);
        display: flex;
        justify-content: center;
        position: relative;

        .box {
            width: 100%;
            height: 100%;
            padding: 10px;
            margin-top: 60px;
        }

        .title {
            height: 45px;
            position: relative;
            border-bottom: 1px solid var(--border-line);
            margin-left: 15px;
            margin-bottom: 20px;

            span {
                display: inline-block;
                font-size: 14px;
                line-height: 45px;
                font-weight: 700;
                color: var(--text-color);
            }
        }

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

        .empty-box {
            text-align: center;
            margin-bottom: 50px;
            color: var(--text-color);
        }

    }
}

@media screen and (min-width: 1119px) {
    .artcile_main {
        min-height: calc(100vh - 167px);
        display: flex;
        justify-content: center;
        position: relative;

        .box {
            width: 70%;
            height: 100%;
            padding: 10px;
            margin-top: 80px;
        }

        .title {
            height: 45px;
            position: relative;
            border-bottom: 1px solid var(--border-line);
            margin-left: 15px;
            margin-bottom: 20px;

            span {
                display: inline-block;
                font-size: 1rem;
                line-height: 45px;
                font-weight: 700;
                color: var(--text-color);
            }
        }

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
                                width: 80%;
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

        .empty-box {
            text-align: center;
            margin-bottom: 50px;
            color: var(--text-color);
        }

    }
}
</style>