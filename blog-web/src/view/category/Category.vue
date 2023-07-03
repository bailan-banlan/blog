<template>
    <div class="category">
        <div class="main">
            <div class="header">
                <span>目前共计{{ categoryList.length }}个分类,继续加油</span>
            </div>
            <div id="time-line">
                <div class="item" v-for="(item, index) in categoryList" :key="index" @click="handleClick(item.id, index)">
                    <div :class="index == 0 ? 'item-index active' : 'item-index'" ref="category">{{ item.name }}</div>
                </div>
            </div>
            <div class="article" v-infinite-scroll="load" infinite-scroll-immediate="false"
                :infinite-scroll-disabled="isInfiniteScroll" :infinite-scroll-distance="5">
                <el-timeline style="margin-left: 5px;" v-if="articleList.length">
                    <el-timeline-item :timestamp="item.createTime" placement="top" v-for="(item, index) in articleList"
                        :key="index">
                        <el-card
                            style="padding: 15px;background-color: var(--background-color);border: 1px solid var(--border-line);">
                            <h4 @click="handleArticleClike(item.id)" class="title">{{ item.title }}</h4>
                            <div class="bottumItem">
                                <div class="articleUser">
                                    <el-avatar class="userAvatar" :src="item.userAvatar"></el-avatar>
                                    <span>{{ item.username }}</span>
                                </div>

                                <div class="tag">
                                    <el-tag style="margin-right: 8px; cursor: pointer;"
                                        :type="tagStyle[Math.round(Math.random() * 4)]" size="small"
                                        v-for="tag in item.tagList" :key="tag.id">{{ tag.name
                                        }}</el-tag>

                                </div>
                            </div>

                        </el-card>
                    </el-timeline-item>
                </el-timeline>
                <el-empty v-else description="暂时没有该分类的文章"></el-empty>
            </div>
        </div>
    </div>
</template>


<script>
import { fetchArticleList, featchCategory } from '@/api'
export default {
    data() {
        return {
            categoryList: [],
            pageData: {
                pageNo: 1,
                pageSize: 5,
                categoryId: null
            },
            // 加载层信息
            loading: [],
            articleList: [],
            pageTotal: 0,
            isInfiniteScroll: false, // 控制滚动禁用
            tagStyle: ['', 'success', 'info', 'warning', 'danger']
        };
    },
    created() {
        document.title = "文章分类";
        this.featchCategory()
    },

    methods: {
        handleArticleClike(id) {
            this.$router.push({ path: '/articleInfo', query: { articleId: id } })
        },
        load() {

            if (this.pageData.pageNo < this.pageTotal) {
                this, this.pageData.pageNo++;
                this.fetchArticleList()
            }
        },
        handleClick(id, index) {
            for (var i = 0; i < this.$refs.category.length; i++) {
                this.$refs.category[i].className = "item-index"
            }
            this.$refs.category[index].className = "item-index active"
            this.openLoading()
            this.pageData.categoryId = id
            this.pageData.pageNo = 1
            this.articleList = []
            this.fetchArticleList()
            this.loading.close()
        },
        featchCategory() {
            this.openLoading()
            featchCategory().then(res => {
                this.categoryList = res.data
                this.pageData.categoryId = this.categoryList[0].id
                this.fetchArticleList()
            })
            this.loading.close()
        },
        fetchArticleList() {
            fetchArticleList(this.pageData).then(res => {
                this.articleList.push(...res.data.records)
                this.pageTotal = res.data.pages
            })
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
};
</script>
<style lang="scss" scoped>
@media screen and (max-width: 1118px) {
    .category {
        display: flex;
        justify-content: center;
        position: relative;

        .main {
            margin-top: 60px;
            width: 100%;
            background-color: var(--background-color);

            .header {
                text-align: center;
                margin-bottom: 20px;
                padding: 10px;
                color: var(--theme-color);
                font-size: 1rem;
                font-weight: bold;
                border-bottom: 2px dashed #569ffb;

                span {
                    margin-right: 20px;
                }
            }

            .article {
                width: 72%;
                float: left;
                height: 700px;
                overflow-y: scroll;

                .title {
                    color: var(--text-color);
                    cursor: pointer;

                    &:hover {
                        color: var(--theme-color);
                    }
                }

                /deep/ .el-timeline-item__timestamp {
                    color: var(--text-color);
                    font-weight: bold;
                    font-size: 0.8rem;
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
                }


                .tag-item {
                    margin-top: 20px;
                    margin-right: 5px;
                }
            }

            #time-line {
                float: left;
                width: 28%;

            }

            .item {
                margin-left: 20px;
                /*用左边margin显示时间线*/
                width: calc(100% - 20px);
                /*因为左边部分用于显示时间线，所以右边部分减去30px*/
                height: auto;
                /*高度由内容决定*/
                position: relative;
                margin-bottom: 30px;
                cursor: pointer;

                &::before {
                    content: "";
                    width: 11px;
                    height: 11px;
                    border-radius: 100%;
                    background-color: #91c2fc;
                    position: absolute;
                    left: -15px;
                }

                &:last-child::after {
                    display: none;
                }

                &::after {
                    content: "";
                    width: 3px;
                    height: calc(100% + 30px);
                    /*加上10px是item底部的margin*/
                    background-color: #91c2fc;
                    position: absolute;
                    top: 0;
                    left: -11px;
                }

                &:hover {
                    &::before {
                        transform: scale3d(1.2, 1.2, 1);
                        background-color: #569ffb;
                    }

                    &::after {
                        transform: scale3d(1.1, 1, 1);
                        background-color: #569ffb;
                    }

                    .item-index {
                        transform: scale3d(1.01, 1.01, 1);
                        color: var(--text-color);
                    }
                }


                .item-index {
                    line-height: 12px;
                    font-size: 14px;
                    position: relative;
                    color: var(--text-color);
                    margin-left: 10px;
                }

                .active {
                    color: var(--theme-color) !important;
                }

                .item-content {
                    width: 100%;
                    height: auto;
                    /*由内容决定*/
                    font-size: 16px;
                    position: relative;
                    white-space: pre-wrap;
                    /*保留空白符序列，但是正常地进行换行*/
                    word-wrap: break-word;
                    /*在长单词或 URL 地址内部进行换行*/
                }
            }
        }

    }
}

@media screen and (min-width: 1119px) {
    .category {
        display: flex;
        justify-content: center;
        position: relative;

        .main {
            margin-top: 80px;
            width: 70%;

            .header {
                text-align: center;
                margin-bottom: 20px;
                padding: 10px;
                color: var(--theme-color);
                font-size: 1rem;
                font-weight: bold;
                border-bottom: 2px solid #e4e7ed;

                span {
                    margin-right: 20px;
                }
            }

            .article {
                width: 80%;
                float: left;
                height: 800px;
                overflow-y: scroll;

                .title {
                    color: var(--text-color);
                    cursor: pointer;

                    &:hover {
                        color: var(--theme-color);
                    }
                }

                /deep/ .el-timeline-item__timestamp {
                    color: var(--text-color);
                    font-weight: bold;
                    font-size: 0.8rem;
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
                }

                .tag-item {
                    margin-top: 20px;
                    margin-right: 5px;
                }
            }

            #time-line {
                float: left;
                width: 17%;

            }

            .item {
                margin-left: 30px;
                /*用左边margin显示时间线*/
                width: calc(100% - 30px);
                /*因为左边部分用于显示时间线，所以右边部分减去30px*/
                height: auto;
                /*高度由内容决定*/
                position: relative;
                margin-bottom: 30px;
                cursor: pointer;

                &::before {
                    content: "";
                    width: 11px;
                    height: 11px;
                    border-radius: 100%;
                    background-color: #91c2fc;
                    position: absolute;
                    left: -15px;
                }

                &:last-child::after {
                    display: none;
                }

                &::after {
                    content: "";
                    width: 3px;
                    height: calc(100% + 30px);
                    /*加上10px是item底部的margin*/
                    background-color: #91c2fc;
                    position: absolute;
                    top: 0;
                    left: -11px;
                }

                &:hover {
                    &::before {
                        transform: scale3d(1.2, 1.2, 1);
                        background-color: #569ffb;
                    }

                    &::after {
                        transform: scale3d(1.1, 1, 1);
                        background-color: #569ffb;
                    }

                    .item-index {
                        transform: scale3d(1.01, 1.01, 1);
                        color: var(--text-color);
                    }
                }


                .item-index {
                    line-height: 12px;
                    font-size: 14px;
                    position: relative;
                    color: var(--text-color);
                    margin-left: 10px;
                }

                .active {
                    color: var(--theme-color);
                }

                .item-content {
                    width: 100%;
                    height: auto;
                    /*由内容决定*/
                    font-size: 16px;
                    position: relative;
                    white-space: pre-wrap;
                    /*保留空白符序列，但是正常地进行换行*/
                    word-wrap: break-word;
                    /*在长单词或 URL 地址内部进行换行*/
                }
            }
        }

    }

}
</style>