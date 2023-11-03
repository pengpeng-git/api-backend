package com.springBoot.controller;

import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONUtil;
import com.apiinterface.client.ApiClient;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.gson.Gson;
import com.springBoot.annotation.AuthCheck;
import com.springBoot.common.BaseResponse;
import com.springBoot.common.ErrorCode;
import com.springBoot.common.IdRequest;
import com.springBoot.common.ResultUtils;
import com.springBoot.constant.UserConstant;
import com.springBoot.exception.BusinessException;
import com.springBoot.exception.ThrowUtils;
import com.springBoot.model.dto.interfaceInfo.InterfaceInfoAddRequest;
import com.springBoot.model.dto.interfaceInfo.InterfaceInfoInvokeRequest;
import com.springBoot.model.dto.interfaceInfo.InterfaceInfoQueryRequest;
import com.springBoot.model.dto.interfaceInfo.InterfaceInfoUpdateRequest;
import com.springBoot.model.entity.CallRecord;
import com.springBoot.model.entity.InterfaceInfo;
import com.springBoot.model.entity.User;
import com.springBoot.model.enums.InterfaceInfoEnum;
import com.springBoot.model.vo.CallRecordVO;
import com.springBoot.model.vo.InterfaceInfoVO;
import com.springBoot.service.CallRecordService;
import com.springBoot.service.InterfaceInfoService;
import com.springBoot.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * 接口调用信息
 */
@RestController
@RequestMapping("/callRecord")
@Slf4j
public class CallRecordController {

    @Resource
    private InterfaceInfoService interfaceInfoService;
    @Resource
    private UserService userService;
    @Resource
    private CallRecordService callRecordService;


    /**
     * 删除接口调用信息
     *
     * @param request   是否成功
     * @return
     */
    @PostMapping("/delete")
    public BaseResponse<Boolean> deleteAllCallRecord(HttpServletRequest request) {
        if (request == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User loginUser = userService.getLoginUser(request);
        QueryWrapper<CallRecord> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id",loginUser.getId());
        boolean remove = callRecordService.remove(wrapper);
        ThrowUtils.throwIf(!remove,ErrorCode.OPERATION_ERROR);
        return ResultUtils.success(true);
    }

    /**
     * 获取接口调用信息列表
     *
     * @param request
     * @return
     */
    @PostMapping("/list")
    public BaseResponse<List<CallRecordVO>> getCallRecordList(HttpServletRequest request) {
        if (request == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User loginUser = userService.getLoginUser(request);
        List<CallRecordVO> callRecordList = callRecordService.getCallRecordList(loginUser.getId());
        return ResultUtils.success(callRecordList);
    }

}
