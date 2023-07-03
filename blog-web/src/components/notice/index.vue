<template>
    <el-card class="notice-main">
        <div class="title">
            <i class="el-icon-bell"></i>
            <span class="titleName">公告信息</span>
        </div>
        <div class="info">
            <span>{{ $store.state.webSiteInfo.bulletin }}</span>
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

    }
}
</script>
<style lang="scss" scoped>
.notice-main {
    margin-top: 20px;
    background-color: var(--background-color);

    &:hover {
        box-shadow: 5px 4px 8px 6px rgba(7, 17, 27, .06);
        transition: all .3s;
    }

    .title {
        width: 300px;
        height: 45px;

        i {
            color: red;
            font-size: 16px;
            line-height: 45px;
            padding-left: 20px;
            font-weight: 700;
            animation: light1 1s ease-in-out infinite alternate;
        }

        @keyframes light1 {
            from {
                transform: scale(0.8);
            }

            to {
                transform: 1;
            }
        }

        .titleName {
            color: var(--box-title_color);
            line-height: 45px;
            font-size: 16px;
            padding-left: 10px;
            font-weight: 600;
        }
    }

    .info {

        border-top: 1px solid var(--border-line);
        background: var(--notice-background);
        // overflow: hidden;
        // text-overflow: ellipsis;
        // white-space: nowrap;
        animation: light 2s ease-in-out infinite alternate;
        min-height: 80px;

        span {
            padding: 5px;
            font-size: 14px;
            color: var(--text-color);
        }
    }
}

@keyframes light {
    from {
        box-shadow: inset 0 0 4px var(--theme-color);
    }

    to {
        box-shadow: inset 0 0 15px var(--theme-color);
    }
}
</style>