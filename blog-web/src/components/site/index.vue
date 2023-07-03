<template>
    <el-card class="box">
        <img src="http://img.shiyit.com/beijing.jpg" alt="">
        <div class="user">
            <div class="avatar_wrapper">
                <img :src="$store.state.webSiteInfo.authorAvatar" alt="">
            </div>
            <a class="username">{{ $store.state.webSiteInfo.author }}</a>
            <span class="desc" :title="$store.state.webSiteInfo.authorInfo">
                {{ $store.state.webSiteInfo.authorInfo }}
            </span>

            <div class="lianxi">
                <a v-show="isShow(3)" :href="$store.state.webSiteInfo.github">
                    <svg-icon icon-class="github" />
                </a>
                <a v-show="isShow(4)" class="gitee" :href="$store.state.webSiteInfo.gitee" target="_blank" title="Gitee"
                    rel="noopener noreferrer nofollow">
                    <svg-icon icon-class="gitee" />
                </a>
                <a v-show="isShow(2)" class="qq"
                    :href="'//wpa.qq.com/msgrd?v=3&amp;uin=' + $store.state.webSiteInfo.qqNumber + '&amp;site=qq&amp;menu=yes'"
                    target="_blank" title="QQ" rel="noopener noreferrer nofollow">
                    <svg-icon icon-class="qq" />
                </a>
                <a v-show="isShow(1)" class="email" :href="'mailto:' + $store.state.webSiteInfo.email" target="_blank"
                    title="邮箱" rel="noopener noreferrer nofollow">
                    <svg-icon icon-class="email" />
                </a>
                <a class="weibo" href="https://weibo.com/u/5747542477" target="_blank" title="微博"
                    rel="noopener noreferrer nofollow">
                    <svg-icon icon-class="weibo" />
                </a>
                <a class="zhihu" href="https://www.zhihu.com/people/he-he-85-83-34" target="_blank" title="知乎"
                    rel="noopener noreferrer nofollow">
                    <svg-icon icon-class="zhihu" />
                </a>
            </div>
            <!-- 收藏本站 -->
            <div class="collect">
                <el-button class="btn" @click="handleCollect">加入书签</el-button>
            </div>
        </div>
    </el-card>
</template>
<script>
export default {
    data() {
        return {
        }
    },

    methods: {
        isShow(type) {
            return this.$store.state.webSiteInfo.showList.indexOf(type) != -1
        },
        handleClike(val) {
            window.location.href = val
        },
        handleCollect() {
            this.$message({
                message: '按CTRL+D 键将本页加入书签',
                type: 'success'
            });
        }
    }
}
</script>
<style lang="scss" scoped>
.box {

    height: auto;
    background-color: var(--background-color);
    position: relative;

    &:hover {
        box-shadow: 5px 4px 8px 6px rgba(7, 17, 27, .06);
        transition: all .3s;
    }

    &::before {
        content: "";
        position: absolute;
        top: 95px;
        left: 0;
        width: 100%;
        height: 30px;
        z-index: 2;
        background: linear-gradient(to bottom, rgba(255, 255, 255, 0), #fff);
    }

    img {
        width: 100%;
        height: 120px;
        object-fit: cover;
        position: absolute;
        top: 0;
    }

    .user {
        padding: 45px 15px 15px;
        display: flex;
        align-items: center;
        position: relative;
        flex-direction: column;

        .avatar_wrapper {
            position: relative;
            width: 75px;
            height: 75px;
            margin-bottom: 12px;

            img {
                width: 75px;
                height: 75px;
                border-radius: 50%;
                display: block;
                overflow: hidden;
                padding: 5px;
                -o-object-fit: cover;
                object-fit: cover;
                transition: transform .5s;

                &:hover {
                    transform: rotate(360deg);
                }
            }
        }

        .username {
            color: var(--theme-color);
            display: block;
            font-size: 16px;
            font-weight: 500;
            margin-bottom: 10px;
        }

        .desc {
            font-size: 14px;
            color: var(--text-color);
            white-space: nowrap;
            overflow: hidden;
            text-overflow: ellipsis;
            width: 100%;
            text-align: center;
        }

        .collect {
            margin-top: 8px;
            width: 100%;

            .btn {
                background-color: transparent;
                border-radius: 5px;
                border: 2px solid var(--theme-color);
                color: var(--theme-color);
                padding: 10px 20px;
                font-weight: 700;
                position: relative;
                transition: all 1s;
                z-index: 1;
                overflow: hidden;
                height: 30px;
                width: 100%;
                line-height: 8px;

                &:hover {
                    color: white;

                    &::before {
                        width: 180%;

                    }
                }

                &::before {
                    content: '';
                    height: 100%;
                    position: absolute;
                    left: -30px;
                    top: 0;
                    background-color: var(--theme-color);
                    transform: skewX(45deg);
                    width: 0%;
                    transition: all 1s;
                    z-index: -1;
                }
            }
        }

        .lianxi {
            border-top: 1px solid var(--border-line);
            justify-content: space-around;
            padding-top: 10px;
            text-align: center;
            align-items: center;
            font-size: 14px;
            display: flex;
            margin-top: 6px;

            a {
                display: inline-block;
                margin: 0 10px;
                transition: all 0.5s;

                &:hover {
                    transform: scale(1.2);
                }

                svg {
                    width: 18px;
                    height: 18px;
                }
            }
        }
    }

}


@keyframes img {
    0% {
        -webkit-transform: rotate(0deg);
    }

    50% {
        -webkit-transform: rotate(180deg);
    }

    100% {
        -webkit-transform: rotate(360deg);
    }

}
</style>