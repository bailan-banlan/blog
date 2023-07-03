package com.shiyi.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.shiyi.utils.DateUtil;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.models.auth.In;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ApiCollectListVO {

    @ApiModelProperty(value = "id")
    private Integer id;

    @ApiModelProperty(value = "文章id")
    private Integer articleId;

    @ApiModelProperty(value = "文章名")
    private String title;

    @ApiModelProperty(value = "作者")
    private String nickname;

    @ApiModelProperty(value = "头像")
    private String avatar;


    @ApiModelProperty(value = "收藏时间")
    @JsonFormat(pattern = DateUtil.YYYY_MM_DD_HH_MM_SS,timezone="GMT+8")
    private Date createTime;
}
