<template>
    <div class="footer_container">
        <div class="content">
            <div class="site">
                <p class="run_time">
                    <svg-icon icon-class="shandian"></svg-icon>
                    <span>已运行
                        <strong>{{ day }}</strong> 天
                        <strong>{{ hours }}</strong> 时
                        <strong>{{ minute }}</strong> 分
                        <strong>{{ second }}</strong> 秒
                    </span>
                </p>
                <p>
                    2023 ©拾壹
                    <a href="https://beian.miit.gov.cn/" target="_blank" style="margin-left: 10px">{{
                        $store.state.webSiteInfo.recordNum
                    }}</a>
                </p>

            </div>
            <div class="site-number">
                <span class="site_pv">
                    <i class="iconfont icon-fangwenliang"></i>
                    访问量
                    <em class="pv_num">
                        {{ this.$store.state.siteAccess }}
                    </em>
                </span>

                <span class="site_uv">
                    <i class="iconfont icon-zongfangkeliang"></i>
                    访客量
                    <em class="uv_num">
                        {{ this.$store.state.visitorAccess }}
                    </em>
                </span>


                <!-- <span class="online_user">
                    <i class="iconfont icon-zaixian"></i>
                    在线人数
                    <em class="online_num">
                        1234
                    </em>
                </span> -->
            </div>
        </div>
    </div>
</template>
<script>
export default {
    data() {
        return {
            day: "",
            hours: "",
            minute: "",
            second: "",
            url: "https://nimg.ws.126.net/?url=http%3A%2F%2Fdingyue.ws.126.net%2F2020%2F0601%2Fba14f84ep00qb7neh000xd200jx00bog00gn009q.png&thumbnail=650x2147483647&quality=80&type=jpg"
        }
    },
    created() {
        this.timer = setInterval(this.runTime, 1000);
    },
    methods: {
        runTime() {
            var timeold =
                new Date().getTime() -
                new Date(1627747200000).getTime();
            var msPerDay = 24 * 60 * 60 * 1000;
            var daysold = Math.floor(timeold / msPerDay);
            var day = new Date();
            this.day = daysold;
            this.hours = day.getHours();
            this.minute = day.getMinutes();
            this.second = day.getSeconds();
        },
    }
}
</script >
<style lang="scss" scoped >
@keyframes bgSize {
    0% {
        background-size: 300% 100%;
    }

    100% {
        background-size: 100% 100%;
    }
}

@keyframes yb {
    10% {
        transform: rotate(15deg);
    }

    20% {
        transform: rotate(-10deg);
    }

    30% {
        transform: rotate(5deg);
    }

    40% {
        transform: rotate(-5deg);
    }

    50%,
    100% {
        transform: rotate(0deg);
    }
}

.footer_container {
    padding: 15px 20px;
    position: relative;
    bottom: 0;
    display: flex;
    align-items: center;
    justify-content: space-between;
    background: var(--footer-back-color);
    background-position: 100% 0;
    animation: bgSize 5s infinite ease-in-out alternate;
    margin-top: 50px;

    .content {
        margin: 0 auto;
        position: relative;

        .site {
            font-size: 14px;
            color: var(--text-color);
            margin-left: 10px;

            p {
                margin: 10px 0;
            }

            a {
                text-decoration: none;
                color: var(--theme-color);
            }

            .run_time {
                height: 20px;
                align-items: center;

                svg {
                    width: 19px;
                    height: 19px;
                    vertical-align: -4px;
                    animation: yb 1s linear infinite;
                }

                span {
                    line-height: 20px;
                    font-size: 14px;
                    padding-left: 5px;

                    strong {
                        color: var(--theme-color);
                    }
                }
            }
        }

        .site-number {

            i {
                display: inline-block;
                font-size: 12px;
            }

            .site_pv,
            .site_uv,
            .online_user {
                position: relative;
                display: inline-block;
                margin-right: 4px;
                height: 24px;
                padding-left: 10px;
                line-height: 24px;
                overflow: hidden;
                font-size: 12px;
                font-weight: 700;
                color: var(--site-text-color);
                background: var(--site-color);
                border-radius: 5px;
            }

            .pv_num,
            .uv_num,
            .online_num {
                display: inline-block;
                font-size: 12px;
                padding: 0 15px;
            }

            .pv_num {
                background: linear-gradient(0deg, #d0339b, #d03336);
            }

            .uv_num {
                background: linear-gradient(0deg, #e2c63a, #f58044);
            }

            .online_num {
                background: linear-gradient(0deg, #367eda, #964fdb);

            }


        }

    }
}
</style>