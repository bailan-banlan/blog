<template>
    <div class="oauth-background">
    </div>
</template>

<script>
/* eslint-disable no-undef */
import { qqLogin, gitEELogin, weiboLogin } from '@/api'
import { getUrl } from '@/utils/auth'
export default {
    created() {
        const that = this;
        //关闭登录框
        that.$store.state.loginFlag = false;
        //通过路径判断是微博登录还是qq登录
        if (that.$route.path === "/callback/qq") {
            // 拿到openId，accessToken传入后台
            if (QC.Login.check()) {
                QC.Login.getMe(function (openId, accessToken) {
                    qqLogin({
                        openId: openId,
                        accessToken: accessToken
                    }).then(res => {
                        //保存登录状态
                        that.$store.commit("login", res.data);
                        if (res.data.email == null) {
                            that.$toast({
                                type: "warnning",
                                message: "请绑定邮箱以便及时收到回复"
                            });
                        } else {
                            that.$toast({ type: "success", message: res.message });
                        }
                    }).catch(err => {
                        that.$toast({ type: "error", message: err.message });
                    });
                });
            } else {
                that.$toast({ type: "error", message: data.message });
            }
        } else if (that.$route.path === "/callback/gitee") {
            gitEELogin(this.$route.query.code).then(res => {
                //保存登录状态
                that.$store.commit("login", res.data);
                if (res.data.email == null) {
                    that.$toast({
                        type: "warnning",
                        message: "请绑定邮箱以便及时收到回复"
                    });
                } else {
                    that.$toast({ type: "success", message: res.message });
                }
            }).catch(err => {
                that.$toast({ type: "error", message: err.message });
            });
        } else {
            weiboLogin(this.$route.query.code).then(res => {
                //保存登录状态
                that.$store.commit("login", res.data);

                if (res.data.email == null) {
                    that.$toast({
                        type: "warnning",
                        message: "请绑定邮箱以便及时收到回复"
                    });
                } else {
                    that.$toast({ type: "success", message: res.message });
                }
            }).catch(err => {
                that.$toast({ type: "error", message: err.message });
            });
        }
        // 跳转回原页面
        const loginUrl = getUrl();
        if (loginUrl != null && loginUrl != "") {
            if (loginUrl.indexOf("articleId") != -1) {
                let arr = loginUrl.split("=")
                this.$router.push({ path: '/articleInfo', query: { articleId: arr[1] } })
            } else {
                that.$router.push({ path: loginUrl });
            }
        } else {
            that.$router.push({ path: "/" });
        }
    }
};
</script>


