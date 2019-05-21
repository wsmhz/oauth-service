package com.wsmhz.security.oauth.service.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Created By TangBiJing On 2019/5/20
 * Description:
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdminInfoVo {

    private Long id;

    private String username;

    private boolean status;

    private List<Long> roleIdList;

    private List<Long> resourceIdList;

    private String email;

    private String phone;
}
