<template>
    <div>
        <el-dialog :lock-scroll="false" class="dialog" title="账号密码登录" :visible.sync="dialogFormVisible">
            <el-form :model="form" :rules="rules" ref="ruleForm">
                <el-form-item label="账号" :label-width="formLabelWidth" prop="email">
                    <el-input placeholder="请输入账号或邮箱号" v-model="form.email" autocomplete="off"></el-input>
                </el-form-item>
                <el-form-item label="密码" :label-width="formLabelWidth" prop="password">
                    <el-input placeholder="请输入密码" v-model="form.password" autocomplete="off" show-password></el-input>
                </el-form-item>
            </el-form>
            <el-button type="success" class="loginBtn" @click="login" round>登录</el-button>

            <div>
                <div class="social-login-title">社交账号登录</div>
                <div class="social-login-wrapper">
                    <a v-show="isShow(2)" @click="qqLogin">
                        <svg-icon icon-class="qq" />
                    </a>

                    <a v-show="isShow(3)" @click="openAuthLogin('gitee')">
                        <svg-icon icon-class="gitee" />
                    </a>
                    <a v-show="isShow(4)" @click="openAuthLogin('weibo')">
                        <svg-icon icon-class="weibo" />
                    </a>
                    <a v-show="isShow(5)" @click="openWechat">
                        <svg-icon icon-class="wechat" />
                    </a>
                    <a v-show="isShow(6)" @click="openAuthLogin('github')">
                        <svg-icon icon-class="github" />
                    </a>
                </div>
            </div>
        </el-dialog>

        <!-- 微信登录 -->
        <el-dialog :lock-scroll="false" class="dialog" title="微信扫码登录" :visible.sync="wechatLoginFlag" :before-close="close">
            <el-image class="wxImg" src="http://img.shiyit.com/wechatQr.jpg">
                <div slot="error" class="image-slot">
                    加载中<span class="dot">...</span>
                </div>
            </el-image>
            <div style="text-align: center;">
                <p>
                    扫码关注公众号，回复验证码完成登录
                </p>
                <p>
                    登录验证码： <span style="color: red;margin-right: 5px;">{{ this.wechatLoginCode }}</span>
                    <i style="cursor: pointer;" @click="getWecahtLoginCode()" class="el-icon-refresh"></i>
                </p>
            </div>
            <div slot="footer" class="dialog-footer">
                <el-button @click="bacKLogin">返回登录</el-button>
            </div>
        </el-dialog>
    </div>
</template>

<script>
import { emailLogin, wxIsLogin, openAuthUrl, getWechatLoginCode } from "@/api";
import { setUrl, setToken } from '@/utils/auth'

export default {
    data: function () {
        return {
            form: {
                email: null,
                password: null,
            },
            code: null,
            timer: null,
            wechatLoginFlag: false,
            formLabelWidth: '80px',
            wechatLoginCode: null,
            countdown: 60, // 倒计时初始值为 60 秒
            rules: {
                email: [
                    { required: true, message: '请输入邮箱地址', trigger: 'blur' },
                ],
                password: [
                    { required: true, message: '请输入密码', trigger: 'blur' },
                ],
            }
        };
    },

    computed: {
        dialogFormVisible: {
            set(value) {
                this.$store.state.loginFlag = value;
            },
            get() {
                return this.$store.state.loginFlag;
            }
        },
    },
    methods: {
        close() {
            clearInterval(this.timer);
            this.$store.state.loginFlag = false;
            this.wechatLoginFlag = false
        },
        openWechat() {
            this.getWecahtLoginCode()
            this.wechatLoginFlag = true
            this.$store.state.loginFlag = false;
        },
        getWecahtLoginCode() {
            getWechatLoginCode().then(res => {
                this.wechatLoginCode = res.data
                this.$message.success("验证码获取成功");
                this.countdown = 60
                this.timer = setInterval(() => {
                    if (this.countdown > 0) {
                        this.countdown--
                        // 轮询判断用户是否已经登录
                        wxIsLogin(this.wechatLoginCode).then(res => {
                            setToken(res.data.token)
                            this.close()
                            location.reload()
                            if (res.data.email == null) {
                                this.$message({
                                    message: '请绑定邮箱以便及时收到回复',
                                    type: 'warning'
                                });
                            } else {
                                this.$message({
                                    message: '登录成功',
                                    type: 'success'
                                });
                            }
                        })
                    } else {
                        // 倒计时结束，处理逻辑
                        clearInterval(this.timer);
                        this.wechatLoginCode = "验证码失效，请刷新获取"
                    }
                }, 1000);
            })
        },
        bacKLogin() {
            clearInterval(this.timer);
            this.$store.state.loginFlag = true
            this.wechatLoginFlag = false
        },
        isShow(type) {
            return this.$store.state.webSiteInfo.loginTypeList.indexOf(type) != -1
        },
        login() {
            this.$refs['ruleForm'].validate((valid) => {
                if (valid) {
                    //发送登录请求
                    emailLogin(this.form).then(res => {
                        setToken(res.data.token)
                        this.close()
                        location.reload()
                        this.$message({
                            message: '登录成功',
                            type: 'success'
                        });
                        location.reload()
                    }).catch(err => {
                        this.$message.error(err.message);
                    });
                } else {
                    console.log('error submit!!');
                    return false;
                }
            });

        },
        qqLogin() {
            //保留当前路径
            this.settingUrl()
            if (
                navigator.userAgent.match(
                    /(iPhone|iPod|Android|ios|iOS|iPad|Backerry|WebOS|Symbian|Windows Phone|Phone)/i
                )
            ) {
                // eslint-disable-next-line no-undef
                QC.Login.showPopup({
                    appId: this.config.QQ_CLIENT_ID,
                    redirectURI: this.config.QQ_REDIRECT_URL
                });
            } else {
                openAuthUrl("qq").then(res => {
                    window.open(res.data);
                });
            }
        },
        openAuthLogin(source) {
            //保留当前路径
            this.settingUrl()
            openAuthUrl(source).then(res => {
                window.open(res.data);
            });
        },
        giteeLogin() {

        },
        weiboLogin() {
            //保留当前路径
            this.settingUrl()
            openAuthUrl("weibo").then(res => {
                window.open(res.data);
            });
        },
        settingUrl() {
            if (this.$route.path == "/articleInfo") {
                setUrl("articleId=" + this.$route.query.articleId)
            } else {
                setUrl(this.$route.path)
            }
        }
    }
};
</script>

<style lang="scss" scoped>
.dialog {
    @media screen and (max-width: 1118px) {
        /deep/ .el-dialog {
            width: 100%;
        }
    }

    @media screen and (min-width: 1119px) {
        /deep/ .el-dialog {
            width: 20%;
            border-radius: 10px;
        }
    }



    .wxImg {
        width: 50%;
        display: block;
        margin: 0 auto;
    }


    .loginBtn {
        margin-top: 30px;
        text-align: center;
        display: block;
        margin: 0 auto;
        width: 50%;
    }

    .social-login-title {
        margin-top: 1.5rem;
        color: #b5b5b5;
        font-size: 0.75rem;
        text-align: center;

        &::before {
            content: "";
            display: inline-block;
            background-color: #d8d8d8;
            width: 60px;
            height: 1px;
            margin: 0 12px;
            vertical-align: middle;
        }

        &::after {
            content: "";
            display: inline-block;
            background-color: #d8d8d8;
            width: 60px;
            height: 1px;
            margin: 0 12px;
            vertical-align: middle;
        }
    }

    .social-login-wrapper {
        margin-top: 1rem;
        font-size: 2rem;
        text-align: center;

        a {
            text-decoration: none;
            margin-left: 20px;
            cursor: pointer;

            svg {
                width: 30px;
                height: 30px;
            }
        }
    }
}
</style>
