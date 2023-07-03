<template>
    <div class="article-info">
        <el-card class="main">

            <el-tag @click="handleClike(article.category, '/category')" effect="dark" class="category">
                {{ article.category.name }}
            </el-tag>
            <h1 class="article-title">{{ article.title }}</h1>

            <div class="article-desc">
                <div class="article-item">
                    <img :src="article.avatar" @error="img" alt="">
                    <div class="meta">
                        <div class="author">
                            <a class="link" href="#" :title="article.username">{{ article.username }}</a>
                        </div>
                        <div class="item">
                            <span class="">{{ formatDate(article.createTime) }}</span>
                            <span class="line">/</span>
                            <span class="text">{{ article.commentCount }} 评论</span>
                            <span class="line">/</span>
                            <span class="text">{{ article.likeCount == null ? 0 : article.likeCount }} 点赞</span>
                            <span class="line">/</span>
                            <span class="text">{{ article.quantity }} 阅读</span>
                            <span class="line">/</span>
                            <span class="text">{{ fontNumber }} 字</span>
                        </div>
                    </div>
                </div>
                <time class="time">
                    {{ formatDate(article.createTime, "MM/dd") }}
                </time>
            </div>
            <div class="tips">
                <div class="tipsName">
                    <i class="iconfont icon-tongzhi1"></i>
                    <span>温馨提示：</span>
                </div>
                <p>本文最后更新于 {{ formatDate(article.updateTime == null ? article.createTime : article.updateTime)
                }}，若内容或图片失效，请留言反馈。部分素材来自网络，若不小心影响到您的利益，请联系我们删除</p>
            </div>
            <!-- 文章内容 -->
            <div style="height: 100%;" class="box-article">
                <v-md-preview :style="style" class="content" :text="this.article.contentMd" ref="preview" />
                <div v-if="article.readType != 0 && !serceShow" class="warpper">
                    <div class="item-title">
                        <i class="el-icon-lock"></i> 该文章部分内容已隐藏
                    </div>
                    <div class="item-box">
                        <span>
                            {{ readTypeList[article.readType] }}
                        </span>
                        <div class="neirong">
                            以下内容已隐藏，请{{ readDescList[article.readType] }}后查看
                        </div>
                        <el-button v-if="article.readType == 1" @click="checkLikeAndCoomment('请到文章内容下方完成评论')" class="btn"
                            type="primary" size="small">立即评论</el-button>
                        <el-button v-if="article.readType == 2" @click="checkLikeAndCoomment('请到文章内容左侧完成点赞')" class="btn"
                            type="primary" size="small">立即点赞</el-button>
                        <el-button v-if="article.readType == 3" @click="dialogVisible = true" class="btn" type="primary"
                            size="small">立即验证</el-button>

                    </div>
                </div>
            </div>

            <!-- 点赞和打赏 -->
            <div class="left-sidbarnav">
                <el-tooltip class="item" effect="dark" content="点赞" placement="left">
                    <div class="left-item" title="点赞" @click="like(article.id)">
                        <el-badge :value="article.likeCount" class="item">
                            <span>
                                <i v-if="article.isLike" class="iconfont icon-dianzan4"></i>
                                <i v-else class="iconfont icon-dianzan1"></i>
                            </span>
                        </el-badge>
                    </div>
                </el-tooltip>

                <el-tooltip class="item" effect="dark" content="收藏" placement="left">
                    <div class="left-item" title="收藏" @click="handleCollect">
                        <el-badge :value="article.collectCount" class="item">
                            <span style="font-size: 20px;">
                                <i v-if="article.isCollect" class="el-icon-star-on"></i>
                                <i v-else class="el-icon-star-off"></i>
                            </span>
                        </el-badge>
                    </div>
                </el-tooltip>

                <el-tooltip class="item" effect="dark" content="评论" placement="left">
                    <div class="left-item" title="评论" @click="handleGoPinglun">
                        <el-badge :value="article.commentCount" class="item">
                            <span>
                                <i class="iconfont icon-pinglun"></i>
                            </span>
                        </el-badge>
                    </div>
                </el-tooltip>

                <el-tooltip class="item" effect="dark" content="打赏" placement="left">
                    <div class="left-item rewardMain" title="打赏">
                        <span class="reward-btn">
                            <i class="iconfont icon-jineqiandaiyueshangjin"></i>
                        </span>
                        <!-- 二维码 -->
                        <div class="rewardItem">
                            <span>
                                <img class="reward-img" :src="$store.state.webSiteInfo.aliPay" />
                            </span>
                            <span>
                                <img class="reward-img" :src="$store.state.webSiteInfo.weixinPay" />
                            </span>
                        </div>
                    </div>
                </el-tooltip>

            </div>
            <!-- 移动端点赞 -->
            <div class="mobile-sidbarnav">
                <div class="dianzan-item" @click="like(article.id)">
                    <span>
                        <svg-icon v-if="article.isLike" icon-class="axsxdianzan"></svg-icon>
                        <svg-icon v-else icon-class="axdianzan"></svg-icon>
                    </span>
                    <span v-if="article.likeCount" class="like-count">{{ article.likeCount }}</span>
                </div>
            </div>

            <!-- 文章标签和分享 -->
            <div class="tag-share">
                <div>
                    <a class="tagBtn" v-for="item in article.tagList" :key="item.id" @click="handleClike(item, '/tags')">
                        <el-tag type="success">
                            {{ item.name }}
                        </el-tag>
                    </a>

                </div>
                <!-- 分享 -->
                <div class="social-share">
                    <a href="" class="social-share-icon icon-qzone">
                        <i class="iconfont icon-qqkongjian"></i>
                    </a>
                    <a href="" class="social-share-icon icon-qq">
                        <i class="iconfont icon-QQ"></i>
                    </a>
                    <a href="" class="social-share-icon icon-wechat">
                        <i class="iconfont icon-weixin"></i>
                    </a>
                    <a href="" class="social-share-icon icon-weibo">
                        <i class="iconfont icon-shejiaotubiao-06"></i>
                    </a>
                </div>
            </div>

            <!-- 版权 -->
            <div class="copyright">
                <div class="copyrightItem">
                    <svg-icon icon-class="copyright"></svg-icon>
                    <span class="text name">版权归属:</span>
                    <span class="text"> {{ article.username }}</span>
                </div>
                <div class="copyrightItem">
                    <svg-icon icon-class="link"></svg-icon>
                    <span class="text name">本文链接:</span>
                    <a href="" class="text"> {{ appUrl }}articleInfo?articleId={{ articleId }} </a>
                </div>
                <div class="copyrightItem">
                    <svg-icon icon-class="xkxy"></svg-icon>
                    <span class="text name">许可协议:</span>
                    <span class="text">
                        本博客所有文章除特别声明外，均采用
                        <a class="text" href="https://creativecommons.org/licenses/by-nc-sa/4.0/">
                            CC BY-NC-SA 4.0
                        </a>
                        许可协议。转载请注明文章出处。
                    </span>
                </div>
                <svg-icon class="yuan" icon-class="yuan"></svg-icon>
            </div>

            <!-- 评论 -->
            <div class="comment-mian" id="comment">
                <div class="title">
                    <i class="iconfont icon-pinglun"></i>
                    评论
                </div>
                <Comment :comment-list="commentList" :total="commentPages" @reloadComment="getCommens"
                    :articleUserId="article.userId" />
            </div>
        </el-card>
        <!-- 文章目录 -->
        <div class="sidebar">
            <div style="position: sticky;top:70px;">
                <el-card class="box-card articleUser">
                    <div style="margin-bottom: 15px;margin-top: 10px;">
                        <a href="javascript:;" style="display: flex;">
                            <el-avatar style="border: 1px solid var(--border-line);"
                                :src="article.userInfo.avatar"></el-avatar>
                            <div class="userInfo">
                                <p class="nickname">
                                    {{ article.userInfo.nickname }}
                                    <el-tag v-if="article.userId != 1" size="mini" type="warning">博主</el-tag>
                                    <el-tag v-else size="mini" type="danger">官方</el-tag>
                                </p>
                                <p class="intor">{{ article.userInfo.intro ? article.userInfo.intro : '这个博主很懒，什么都没有留下' }}
                                </p>
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
                        <span class="myComment">
                            <div>
                                0
                            </div>
                            <div class="name">
                                粉丝
                            </div>
                        </span>
                        <span class="myComment">
                            <div>
                                0
                            </div>
                            <div class="name">
                                关注
                            </div>
                        </span>
                    </div>
                </el-card>
                <div class="directory">
                    <el-card class="directory-item">
                        <div slot="header" class="title">
                            <span>目录</span>
                        </div>

                        <ul>
                            <li :class="active == index ? 'structure active' : 'structure'"
                                v-for="(anchor, index) in titles" @click="handleAnchorClick(anchor)" :key="index">
                                {{ anchor.title }}
                            </li>
                        </ul>
                    </el-card>
                </div>
            </div>

        </div>

        <!-- 公众号扫码验证框 -->
        <el-dialog title="关注公众号验证" :visible.sync="dialogVisible">
            <div style="text-align: center;">
                <div>扫码关注公众号<span style="color: red;">【 拾壹学编程 】</span></div>
                <div>回复<span style="color: red;">【 验证码 】</span>获取验证码</div>
            </div>
            <el-image class="wxImg" src="http://img.shiyit.com/wechatQr.jpg">
                <div slot="error" class="image-slot">
                    加载中<span class="dot">...</span>
                </div>
            </el-image>

            <el-input wi v-model="code" placeholder="请输入验证码"></el-input>
            <span slot="footer" class="dialog-footer">
                <el-button @click="dialogVisible = false">取 消</el-button>
                <el-button type="primary" @click="checkCode">确 定</el-button>
            </span>
        </el-dialog>
    </div>
</template>
<script>
import { articleInfo, featchComments, articleLike, checkCode, collect, cancelCollect } from '@/api'
import SiteInfo from '@/components/site/index.vue'
import Comment from '@/components/comment/index.vue'
export default {
    components: {
        SiteInfo,
        Comment
    },
    data() {
        return {
            article: {
                category: {},
                comments: [],
                tagList: [],
                userInfo: {}
            },
            code: null,
            style: '',
            titles: [],
            imgList: [],
            readTypeList: ['', '评论阅读', '点赞阅读', '扫码阅读'],
            readDescList: ['', '评论', '点赞', '扫码回复验证码'],
            dialogVisible: false,
            active: 0,
            fontNumber: 0,
            img: "https://foruda.gitee.com/avatar/1677050610632357168/5407895_quequnlong_1646130774.png",
            appUrl: process.env.VUE_APP_URL,
            commentList: [],
            tempList: null,
            articleId: this.$route.query.articleId,
            commentQuery: {
                pageNo: 1,
                pageSize: 5,
                articleId: this.$route.query.articleId,
            },
            commentPages: 0,
            user: {},
            // 加载层信息
            loading: [],
            serceShow: 0,
        }
    },

    mounted() {
        window.setTimeout(() => {
            if (this.$refs.preview) {
                const anchors = this.$refs.preview.$el.querySelectorAll('h1,h2,h3,h4,h5,h6');
                if (anchors) {
                    const titles = Array.from(anchors).filter((title) => !!title.innerText.trim());
                    if (!this.titles.length) {
                        this.titles = [];
                    }
                    const hTags = Array.from(new Set(titles.map((title) => title.tagName))).sort();
                    this.titles = titles.map((el) => ({
                        title: el.innerText,
                        lineIndex: el.getAttribute('data-v-md-line'),
                        indent: hTags.indexOf(el.tagName),
                    }));
                    this.tempList = anchors
                }

                // 添加图片预览功能
                const imgList = this.$refs.preview.$el.getElementsByTagName("img");
                let that = this
                for (var i = 0; i < imgList.length; i++) {
                    that.imgList.push(imgList[i].src);
                    imgList[i].addEventListener("click", function (e) {
                        that.previewImg(e.target.currentSrc);
                    });
                }
            }
        }, 500)
        // 监听滚动事件
        window.addEventListener('scroll', this.onScroll, false)
    },
    computed: {
        isCommentFlag() {
            return this.$store.state.isCommentFlag
        }
    },
    watch: {
        isCommentFlag: function (newVal, oldVal) {
            //插入需要在仓库数据变化时做的逻辑处理
            if (newVal) {
                this.checkAfter()
            }
        }
    },

    created() {
        this.openLoading()
        articleInfo(this.articleId).then(res => {
            this.article = res.data
            this.serceShow = this.article.activeReadType
            if (this.article.readType != 0 && !this.serceShow) {
                this.style = "max-height: 1500px;overflow: hidden;"
            }
            //修改标题
            document.title = this.article.title
            // 计算文章字数
            this.fontNumber = this.deleteHTMLTag(this.article.content).length
            this.getCommens();
        }).catch(err => {
            this.$message.error(err.message);
        });
        this.loading.close()
    },
    methods: {
        checkLikeAndCoomment(desc) {
            this.$message.info(desc)
        },
        checkCode() {
            checkCode(this.code).then(res => {
                this.$message.success("验证成功")
                this.checkAfter()
            }).catch(err => {
                this.$message.error("验证失败")
            })

        },
        checkAfter() {
            this.dialogVisible = false
            this.style = ''
            this.serceShow = true
        },
        previewImg(img) {
            this.$imagePreview({
                images: this.imgList,
                index: this.imgList.indexOf(img)
            });
        },
        onScroll() {
            // 所有锚点元素的 offsetTop
            const offsetTopArr = []
            if (this.tempList) {
                this.tempList.forEach(item => {
                    offsetTopArr.push(item.offsetTop)
                })
                // 获取当前文档流的 scrollTop
                const scrollTop = document.documentElement.scrollTop || document.body.scrollTop
                // 定义当前点亮的导航下标
                let navIndex = 0
                for (let n = 0; n < offsetTopArr.length; n++) {
                    // 如果 scrollTop 大于等于第n个元素的 offsetTop 则说明 n-1 的内容已经完全不可见
                    // 那么此时导航索引就应该是n了
                    if (scrollTop >= offsetTopArr[n]) {
                        navIndex = n
                    }
                }
                this.active = navIndex
            }
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
        handleCollect() {
            this.openLoading()
            let id = this.article.id;
            if (this.article.isCollect) {
                cancelCollect(id).then(res => {
                    this.article.collectCount--
                    this.article.isCollect = 0
                    this.$message.success("取消收藏成功")
                    this.loading.close()
                }).catch(err => {
                    this.$message.error("取消收藏失败")
                })
            } else {
                collect(id).then(res => {
                    this.article.collectCount++
                    this.article.isCollect = 1
                    this.$message.success("收藏成功")
                    this.loading.close()
                }).catch(err => {
                    this.$message.error("收藏失败")
                })
            }
        },
        handleGoPinglun() {
            document.getElementById("comment").scrollIntoView({ behavior: 'smooth' })
        },
        handleClike(item, path) {
            this.$router.push({ name: path, query: { id: item.id, name: item.name } })
        },
        getCommens() {
            featchComments(this.commentQuery).then(res => {
                this.commentList = res.data != null ? res.data.records : [];
                this.commentPages = res.data != null ? res.data.pages : 0;
            })
        },

        formatDate: function (value, args) {
            var dt = new Date(value);
            let year = dt.getFullYear();
            let month = (dt.getMonth() + 1).toString().padStart(2, '0');
            let date = dt.getDate().toString().padStart(2, '0');
            if (args === "MM/dd") {
                return `${month}/${date}`;
            }
            return `${year}-${month}-${date}`;
        },
        deleteHTMLTag(content) {
            return content ? content
                .replace(/<\/?[^>]*>/g, "")
                .replace(/[|]*\n/, "")
                .replace(/&npsp;/gi, "") : 0;
        },
        handleAnchorClick(anchor) {
            const { preview } = this.$refs;
            const { lineIndex } = anchor;
            const heading = preview.$el.querySelector(`[data-v-md-line="${lineIndex}"]`);

            if (heading) {
                preview.scrollToTarget({
                    target: heading,
                    scrollContainer: window,
                    top: 60,
                });
            }
        },
        like(articleId) {
            this.openLoading()
            articleLike(articleId).then(res => {
                if (this.article.isLike) {
                    this.article.likeCount--;
                    this.article.isLike = false
                    this.$message.success("取消成功");
                } else {
                    this.article.likeCount++;
                    this.article.isLike = true
                    if (this.article.readType == 2) {
                        this.checkAfter()
                    }
                    this.$message.success("点赞成功");
                }
            }).catch(err => {
                this.$message.error(err.message);
            })
            this.loading.close()
        },

    },
}
</script>
<style lang="scss" scoped>
/deep/ .vuepress-markdown-body {
    background-color: var(--background-color);
    color: var(--article-color);
}


@media screen and (max-width: 1118px) {
    /deep/ .el-dialog {
        width: 100%;
    }

    .article-info {
        display: flex;
        justify-content: center;
        position: relative;

        .main {
            height: 100%;
            width: 100%;
            margin-top: 60px;
            background-color: var(--background-color);


            .category {
                margin-right: 10px;
                border-radius: 5px;
                cursor: pointer;
            }

            .article-title {
                font-size: 1.2rem;
                color: var(--article-color);
                text-align: center;
                padding-top: 20px;
                margin-bottom: 15px;
                word-break: break-word;
                text-shadow: 0 1px 2px rgba(0, 0, 0, 0.25);
                font-weight: 500;
            }

            .article-desc {
                height: 50px;
                border-bottom: 1px solid var(--border-line);
                margin-bottom: 15px;
                position: relative;
                display: flex;
                align-items: center;
                padding-bottom: 15px;

                &::after {
                    content: '';
                    position: absolute;
                    bottom: -1.5px;
                    left: 0;
                    width: 80px;
                    height: 3px;
                    border-radius: 1.5px;
                    background: var(--theme-color);
                }

                .article-item {
                    display: flex;

                    img {
                        width: 30px;
                        height: 30px;
                        border-radius: 50%;
                        margin-right: 10px;
                        padding: 3px;
                        -o-object-fit: cover;
                        object-fit: cover;
                        background: var(--border-line);
                        border: 1px solid #dcdfe6;
                    }

                    .meta {
                        display: flex;
                        flex-direction: column;
                        height: 100%;
                        justify-content: space-between;
                        font-size: 12px;

                        .link {
                            font-weight: 500;
                            color: var(--theme-color);
                            text-decoration: none;
                        }

                        .item {
                            color: #909399;
                            line-height: 16px;
                            display: flex;
                            align-items: center;
                            width: 100%;

                            .line,
                            .text {
                                margin: 0 5px
                            }
                        }
                    }
                }


                .time {
                    display: none;
                }

            }

            .tips {
                color: #db7c22;
                background-color: var(--article-tips-color);
                border: 1px solid var(--article-tips-border);
                border-radius: 5px;
                margin-bottom: 20px;

                .tipsName {
                    margin-left: 20px;
                    margin-top: 10px;

                    span {
                        padding-left: 5px;
                    }
                }

                p {
                    margin-left: 40px;
                    padding-right: 10px;
                    padding-bottom: 10px;
                }
            }

            .box-article {
                .warpper {
                    background-color: gray;
                    position: relative;
                    height: 210px;
                    padding: 5px;

                    &::before {
                        content: "";
                        position: absolute;
                        top: -80px;
                        left: 0;
                        width: 100%;
                        height: 80px;
                        z-index: 2;
                        background: linear-gradient(180deg, rgba(55, 55, 55, 0), #ccc);
                    }

                    .item-box {
                        border-radius: 10px;
                        background-color: var(--background-color);
                        height: 150px;
                        margin-left: 10px;
                        margin-right: 10px;
                        margin-top: 10px;
                        margin-bottom: 10px;
                        overflow: hidden;

                        span {
                            background: linear-gradient(135deg, #ff74cd 10%, #ec7d0b);
                            border-top-left-radius: 10px;
                            border-bottom-right-radius: 10px;
                            padding: 5px;
                            font-size: 0.9rem;
                            color: #fff;
                        }

                        .neirong {
                            text-align: center;
                            margin-top: 15px;
                            color: var(--text-color);
                            font-size: 0.9rem;
                        }

                        .btn {
                            margin: 0 auto;
                            display: block;
                            margin-top: 20px;
                        }
                    }
                }
            }

            .content {
                color: var(--article-content-color);
            }

            .tag-share {
                display: flex;
                overflow-x: auto;
                margin-right: 5px;
                align-items: center;
                padding-bottom: 15px;
                margin-bottom: 20px;
                position: relative;
                border-bottom: 1px solid var(--border-line);
                margin-top: 20px;
                cursor: pointer;



                .social-share {
                    position: absolute;
                    right: 0;
                    top: 0;

                    .social-share-icon {
                        margin-left: 10px;
                        display: inline-block;
                        width: 32px;
                        height: 32px;
                        font-size: 20px;
                        border-radius: 50%;
                        line-height: 30px;
                        border: 1px solid #666;
                        color: #666;
                        text-align: center;
                        vertical-align: middle;
                        text-decoration: none;

                        i {
                            font-size: 25px;
                        }
                    }

                    .icon-qzone {
                        border-color: #fdbe3d;
                        color: #fdbe3d;

                    }

                    .icon-wechat {
                        color: #7bc549 !important;
                        border-color: #7bc549;
                    }

                    .icon-weibo {
                        color: #ff763b !important;
                        border-color: #ff763b;
                    }

                    .icon-qq {
                        color: #56b6e7 !important;
                        border-color: #56b6e7;
                    }
                }


            }

            .left-sidbarnav {
                display: none;
            }

            .mobile-sidbarnav {
                text-align: center;


                .dianzan-item {
                    width: 40px;
                    height: 40px;
                    line-height: 40px;
                    margin: 0 auto;
                    position: relative;

                    .like-count {
                        position: absolute;
                        bottom: -26px;
                        left: 17px;
                        color: var(--text-color);
                        font-size: 12px;
                    }

                    svg {
                        width: 30px;
                        height: 30px;
                        vertical-align: -10px;
                    }
                }
            }

            .copyright {
                background-color: var(--article-copyr--color);
                border-radius: 5px;
                width: 100%;
                min-height: 130px;
                position: relative;

                .copyrightItem {
                    padding: 10px 10px;
                    height: 100%;
                    line-height: 26px;
                    margin-bottom: 10px;

                    svg {
                        width: 18px;
                        height: 18px;
                        margin-right: 3px;
                        vertical-align: -4px;
                    }

                    .text {
                        color: #909399;
                        font-size: 14px;
                        margin-left: 8px;
                        text-decoration: none;
                    }

                    .name {
                        color: var(--theme-color);
                        font-weight: 700;
                    }

                    a:hover {
                        color: var(--theme-color);
                    }
                }

                .yuan {
                    width: 20px;
                    height: 20px;
                    position: absolute;
                    top: 10px;
                    right: 10px;
                }


            }

            .comment-mian {
                .title {
                    font-weight: 700;
                    font-size: 20px;
                    margin-top: 20px;
                    color: var(--theme-color);
                }
            }
        }

        .sidebar {
            display: none;
        }


    }
}

@media screen and (min-width: 1119px) {

    /deep/ .el-dialog {
        width: 30%;
    }

    .article-info {
        display: flex;
        justify-content: center;
        position: relative;

        .main {
            background-color: var(--background-color);
            padding: 20px;
            height: 100%;
            width: 50%;
            margin-top: 80px;

            &:hover {
                box-shadow: 5px 4px 8px 6px rgba(7, 17, 27, .06);
                transition: all .3s;
            }

            .category {
                margin-right: 10px;
                border-radius: 5px;
                cursor: pointer;
                transition: transform .5s;

                &:hover {
                    transform: scale(1.1);
                }
            }

            .article-title {
                font-size: 24px;
                color: var(--article-color);
                text-align: center;
                padding-top: 20px;
                margin-bottom: 15px;
                word-break: break-word;
                text-shadow: 0 1px 2px rgba(0, 0, 0, 0.25);
                font-weight: 500;
            }

            .article-desc {
                height: 50px;
                border-bottom: 1px solid var(--border-line);
                margin-bottom: 15px;
                position: relative;
                display: flex;
                align-items: center;
                padding-bottom: 15px;

                &::after {
                    content: '';
                    position: absolute;
                    bottom: -1.5px;
                    left: 0;
                    width: 80px;
                    height: 3px;
                    border-radius: 1.5px;
                    background: var(--theme-color);
                }

                .article-item {
                    display: flex;

                    img {
                        width: 30px;
                        height: 30px;
                        border-radius: 50%;
                        margin-right: 10px;
                        padding: 3px;
                        -o-object-fit: cover;
                        object-fit: cover;
                        background: var(--border-line);
                        border: 1px solid #dcdfe6;
                    }

                    .meta {
                        display: flex;
                        flex-direction: column;
                        height: 35px;
                        justify-content: space-between;
                        font-size: 12px;

                        .link {
                            font-weight: 500;
                            color: var(--theme-color);
                            text-decoration: none;
                        }

                        .item {
                            color: #909399;
                            line-height: 16px;
                            display: flex;
                            align-items: center;

                            .line,
                            .text {
                                margin: 0 5px
                            }
                        }
                    }
                }


                .time {
                    font-size: 32px;
                    line-height: 42px;
                    color: #606266;
                    text-shadow: 0 1px 2px rgba(0, 0, 0, 0.25);
                    position: absolute;
                    right: 0;
                }

            }

            .tips {
                color: #db7c22;
                background-color: var(--article-tips-color);
                border: 1px solid var(--article-tips-border);
                border-radius: 5px;
                margin-bottom: 20px;

                .tipsName {
                    margin-left: 20px;
                    margin-top: 10px;

                    span {
                        padding-left: 5px;
                    }
                }

                p {
                    margin-left: 40px;
                    padding-right: 10px;
                    padding-bottom: 10px;
                }
            }

            .box-article {

                .warpper {
                    background-color: gray;
                    position: relative;
                    height: 210px;
                    padding: 5px;

                    &::before {
                        content: "";
                        position: absolute;
                        top: -80px;
                        left: 0;
                        width: 100%;
                        height: 80px;
                        z-index: 2;
                        background: linear-gradient(180deg, rgba(55, 55, 55, 0), #ccc);
                    }

                    .item-title {
                        color: #fff;
                    }

                    .item-box {
                        border-radius: 10px;
                        background-color: var(--background-color);
                        height: 150px;
                        margin-left: 10px;
                        margin-right: 10px;
                        margin-top: 10px;
                        margin-bottom: 10px;
                        overflow: hidden;

                        span {
                            background: linear-gradient(135deg, #ff74cd 10%, #ec7d0b);
                            border-top-left-radius: 10px;
                            border-bottom-right-radius: 10px;
                            padding: 5px;
                            font-size: 0.9rem;
                            color: #fff;
                        }

                        .neirong {
                            text-align: center;
                            margin-top: 15px;
                            color: var(--text-color);
                            font-size: 0.9rem;
                        }

                        .btn {
                            margin: 0 auto;
                            display: block;
                            margin-top: 20px;
                        }
                    }
                }
            }

            .content {
                color: var(--article-content-color);

                /deep/ .vuepress-markdown-body img {

                    border-radius: 5px !important;
                    margin-top: 10px;
                    transition: box-shadow .35s, transform .35s;
                    cursor: pointer;

                    &:hover {
                        box-shadow: 5px 10px 5px rgba(0, 0, 0, 0.2);
                        transform: translateY(-10px)
                    }
                }
            }

            .tag-share {
                display: flex;
                overflow-x: auto;
                margin-right: 5px;
                align-items: center;
                padding-bottom: 15px;
                margin-bottom: 20px;
                position: relative;
                border-bottom: 1px solid var(--border-line);
                margin-top: 20px;

                .tagBtn {
                    cursor: pointer;
                    margin-right: 5px;
                }

                .social-share {
                    position: absolute;
                    right: 0;
                    top: 0;

                    .social-share-icon {
                        margin-left: 10px;
                        display: inline-block;
                        width: 32px;
                        height: 32px;
                        font-size: 20px;
                        border-radius: 50%;
                        line-height: 30px;
                        border: 1px solid #666;
                        color: #666;
                        text-align: center;
                        vertical-align: middle;
                        text-decoration: none;

                        i {
                            font-size: 25px;
                        }
                    }

                    .icon-qzone {
                        border-color: #fdbe3d;
                        color: #fdbe3d;

                        &:hover {
                            background-color: #fdbe3d;

                            i {
                                color: white;
                            }
                        }
                    }

                    .icon-wechat {
                        color: #7bc549 !important;
                        border-color: #7bc549;

                        &:hover {
                            background-color: #7bc549;

                            i {
                                color: white;
                            }
                        }
                    }

                    .icon-weibo {
                        color: #ff763b !important;
                        border-color: #ff763b;

                        &:hover {
                            background-color: #ff763b;

                            i {
                                color: white;
                            }
                        }
                    }

                    .icon-qq {
                        color: #56b6e7 !important;
                        border-color: #56b6e7;

                        &:hover {
                            background-color: #56b6e7;

                            i {
                                color: white;
                            }
                        }
                    }
                }


            }

            .copyright {
                background-color: var(--article-copyr--color);
                border-radius: 5px;
                width: 100%;
                min-height: 130px;
                position: relative;

                .copyrightItem {
                    padding: 10px 10px;
                    height: 100%;
                    line-height: 26px;
                    margin-bottom: 10px;

                    svg {
                        width: 18px;
                        height: 18px;
                        margin-right: 3px;
                        vertical-align: -4px;
                    }

                    .text {
                        color: #909399;
                        font-size: 14px;
                        margin-left: 8px;
                        text-decoration: none;
                    }

                    .name {
                        color: var(--theme-color);
                        font-weight: 700;
                    }

                    a:hover {
                        color: var(--theme-color);
                    }
                }

                .yuan {
                    width: 20px;
                    height: 20px;
                    position: absolute;
                    top: 10px;
                    right: 10px;
                }
            }

            .mobile-sidbarnav {
                display: none;
            }

            .left-sidbarnav {
                position: fixed;
                left: 8%;
                top: 25%;
                z-index: 999;

                .left-item {
                    margin-bottom: 20px;
                    width: 50px;
                    height: 50px;
                    border-radius: 50%;
                    text-align: center;
                    line-height: 50px;
                    background-color: var(--background-color);
                    cursor: pointer;
                    position: relative;
                    color: var(--text-color);

                    &:hover span {
                        color: var(--theme-color);
                    }

                    .icon-dianzan4 {
                        color: var(--theme-color);
                    }

                    .like-count {
                        color: #fff;
                        display: inline-block;
                        width: 20px;
                        height: 20px;
                        border-radius: 50%;
                        position: absolute;
                        line-height: 20px;
                        background-color: var(--theme-color);
                        top: -8px;

                    }

                    .rewardItem {
                        position: absolute;
                        bottom: -150px;
                        left: 52px;
                        margin: 0;
                        padding: 0 0 15px;
                        width: 480px;
                        background-color: #5956563e;
                        height: 310px;
                        border-radius: 5px;
                        display: none;

                        .reward-img {
                            margin-left: 5px;
                            margin-right: 5px;
                            width: 220px;
                            height: 300px;
                            margin-top: 12px;
                        }
                    }

                }

                .rewardMain {
                    &:hover {
                        .rewardItem {
                            display: block;
                        }
                    }
                }
            }

            .comment-mian {
                .title {
                    font-weight: 700;
                    font-size: 20px;
                    margin-top: 20px;
                    color: var(--theme-color);
                }
            }
        }

        .sidebar {
            margin-left: 20px;
            width: 20%;

            .box-card {
                margin-top: 80px;
            }

            .articleUser {
                padding: 10px;
                background-color: var(--background-color);

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
                    margin-left: 10px;
                    margin-right: 20px;
                    color: var(--text-color);

                    .name {
                        margin-top: 2px;
                    }
                }
            }

            .directory {
                margin-top: 10px;

                .directory-item {
                    background-color: var(--background-color);

                    /deep/ .el-card__header {
                        padding: 18px 20px;
                    }

                    .title {

                        span {
                            color: var(--article-color);
                        }
                    }

                    .structure {
                        cursor: pointer;
                        color: var(--article-color);
                        padding: 5px 0;
                        margin-left: 30px;
                        margin-bottom: 10px;


                    }

                    .active,
                    .structure:hover {
                        color: var(--theme-color);
                    }
                }



            }
        }


    }
}


.wxImg {
    display: block;
    margin: 0 auto;
    width: 50%;
    height: 50%;
}
</style>