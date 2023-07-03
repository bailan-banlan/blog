<template>
    <div class="nav" v-show="drawer">
        <!-- 遮罩层 -->
        <div class=" mask" @click="close" @touchmove.stop.prevent></div>
        <!-- 内容 -->
        <div class="warpper" :style="{ width: drawer ? '240px' : '0px' }">
            <!-- 博主介绍 -->
            <div class="blogger-info">
                <el-avatar :src="$store.state.webSiteInfo.authorAvatar"></el-avatar>
            </div>
            <!-- 博客信息 -->
            <div class="blog-info-wrapper">
                <div class="blog-info-data">

                    <a @click="handleClike('/archive')">
                        <div style="font-size: 0.875rem">文章</div>
                        <div style="font-size: 1.125rem;">
                            {{ $store.state.siteCount.articleCount }}
                        </div>
                    </a>
                </div>
                <div class="blog-info-data">

                    <a @click="handleClike('/categorys')">
                        <div style="font-size: 0.875rem">分类</div>
                        <div style="font-size: 1.125rem">
                            {{ $store.state.siteCount.categoryCount }}
                        </div>
                    </a>
                </div>
                <div class="blog-info-data">
                    <a @click="handleClike('/tag')">
                        <div style="font-size: 0.875rem">标签</div>
                        <div style="font-size: 1.125rem">
                            {{ $store.state.siteCount.tagCount }}
                        </div>
                    </a>
                </div>
            </div>
            <hr />
            <!-- 页面导航 -->
            <div class="menu-container">
                <div class="menus-item">
                    <a @click="handleClike('/')">
                        <i class="el-icon-star-off" /> 首页
                    </a>
                </div>
                <div class="menus-item">
                    <a @click="handleClike('/archive')">
                        <i class="el-icon-s-order" /> 归档
                    </a>
                </div>

                <div class="menus-item">

                    <a @click="handleClike('/categorys')">
                        <i class="el-icon-menu" /> 分类
                    </a>
                </div>
                <div class="menus-item">
                    <a @click="handleClike('/tag')">
                        <i class="el-icon-discount" /> 标签
                    </a>
                </div>
                <div class="menus-item">
                    <a @click="handleClike('/photo')">
                        <i class="el-icon-picture" /> 画廊
                    </a>
                </div>

                <div class="menus-item">
                    <a @click="handleClike('/sponsor')">
                        <i class="iconfont icon-jineqiandaiyueshangjin" /> 打赏
                    </a>
                </div>
                <div class="menus-item">
                    <a @click="handleClike('/message')">
                        <i class="iconfont icon-pinglun" /> 留言
                    </a>
                </div>
                <div class="menus-item">
                    <a @click="handleClike('/links')">
                        <i class="el-icon-position" /> 友链
                    </a>
                </div>
                <div class="menus-item">
                    <a @click="handleClike('/about')">
                        <i class="el-icon-info" /> 关于
                    </a>
                </div>

                <div class="menus-item" v-if="!isLogin">
                    <a @click="openLogin"><i class="el-icon-user-solid" /> 登录 </a>
                </div>
                <div v-else>
                    <div class="menus-item">
                        <a @click="handleClike('/user')"><i class="el-icon-user" /> 个人中心 </a>
                    </div>
                    <div class="menus-item">
                        <a @click="logout"><i class="el-icon-switch-button" /> 退出</a>
                    </div>
                </div>
            </div>
        </div>

    </div>
</template>

<script>
import { logout } from '@/api'
import { removeToken } from '@/utils/auth'
export default {
    data: function () {
        return {
            img: process.env.VUE_APP_IMG_API,
        }
    },
    computed: {
        drawer: {
            set(value) {
                this.$store.state.drawer = value;
            },
            get() {
                return this.$store.state.drawer;
            }
        },
        isLogin() {
            let user = this.$store.state.userInfo
            return user != null;
        }
    },

    methods: {
        handleClike(val) {
            this.$store.state.drawer = false
            this.$router.push({ path: val })
        },
        close() {
            this.$store.state.drawer = false;
        },
        openLogin() {
            this.$store.state.loginFlag = true;
        },
        logout() {
            //如果在个人中心则跳回上一页
            if (this.$route.path === "/user") {
                this.$router.go(-1);
            }
            logout().then(res => {
                removeToken()
                location.reload()
                this.$message({ type: "success", message: "注销成功" });
            }).catch(err => {
                console.log(err)
            });;
        }
    }
};
</script>

<style lang="scss" scoped>
a {
    color: var(--text-color);
}

.nav {
    height: 100%;

    .mask {
        background: rgba(101, 101, 101, 0.8); //设置透明度 ，改最后一个数值，0-1
        width: 100%;
        height: 100%;
        z-index: 9;
        position: fixed;
        top: 0;
        bottom: 0;
        left: 0;
        right: 0;
    }

    .warpper {
        font-weight: 700;
        font-size: 1.2rem;
        background-color: var(--background-color);
        position: fixed;
        top: 0;
        z-index: 100;
        height: 100vh;
        animation: 0.8s ease 0s 1 normal none running sidebarItem;

        .blogger-info {
            padding: 26px 30px 0;
            text-align: center;
            z-index: 99;

            /deep/ .el-avatar {
                width: 80px;
                height: 80px;
            }
        }

        .blog-info-wrapper {
            display: flex;
            align-items: center;
            padding: 12px 10px 0;

            .blog-info-data {
                flex: 1;
                line-height: 2;
                text-align: center;

                a {
                    text-decoration: none;

                }
            }
        }

        hr {
            border: 2px dashed #d2ebfd;
            margin: 20px 0;
        }

        .menu-container {
            padding: 0 10px 40px;
            overflow: scroll;

            .menus-item {


                a {
                    padding: 6px 30px;
                    display: block;
                    line-height: 2;
                    text-decoration: none;
                }

                i {
                    margin-right: 1rem;
                }
            }
        }
    }
}

@keyframes sidebarItem {
    0% {
        transform: translateX(200px);
    }

    100% {
        transform: translateX(0);
    }
}
</style>
