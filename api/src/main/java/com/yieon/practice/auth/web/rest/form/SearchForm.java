package com.yieon.practice.auth.web.rest.form;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Pageable;

/**
 * @author yieon
 * @version default
 * @email parrotbill@naver.com
 * @since 2023-04-04
 * <PRE>
 * ------------------------
 * summary : 사용자 조회 FORM
 * ------------------------
 * Revision history
 * 2023-04-04. yieon : Initial creation
 * </PRE>
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SearchForm {

    @ApiModelProperty(position = 1,required = false, value = "사용자 이메일")
    private String email;

//    @ApiModelProperty(position = 2,required = false, value = "사용자 이름")
//    private String name;

    @ApiModelProperty(position = 3,required = false, value = "권한그룹 코드")
    private String roleCd;

    @ApiModelProperty(position = 4,required = false, value = "클라이언트 ID")
    private String clientInfo;

    @ApiModelProperty(position = 5,required = true, value = "page")
    private Integer page;

    @ApiModelProperty(position = 6,required = true, value = "size")
    private Integer size;

    @ApiModelProperty(position = 7,required = true, value = "sort")
    private String sort;

}
