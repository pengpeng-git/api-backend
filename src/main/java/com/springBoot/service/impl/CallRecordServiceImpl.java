package com.springBoot.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.springBoot.common.ErrorCode;
import com.springBoot.exception.BusinessException;
import com.springBoot.exception.ThrowUtils;
import com.springBoot.model.entity.CallRecord;
import com.springBoot.model.entity.InterfaceInfo;
import com.springBoot.model.entity.User;
import com.springBoot.model.vo.CallRecordVO;
import com.springBoot.service.CallRecordService;
import com.springBoot.mapper.CallRecordMapper;
import com.springBoot.service.InterfaceInfoService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author 26550
 * @description 针对表【call_record】的数据库操作Service实现
 * @createDate 2023-10-07 19:15:36
 */
@Service
public class CallRecordServiceImpl extends ServiceImpl<CallRecordMapper, CallRecord>
        implements CallRecordService {

    @Resource
    private InterfaceInfoService interfaceInfoService;
    /**
     * 记录用户调用数据
     *
     * @param userId
     * @param interfaceId
     * @return
     */
    @Override
    public boolean updateUserCallRecord(Long userId, Long interfaceId) {
        if (userId == null && interfaceId == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "请求参数为空");
        }
        QueryWrapper<CallRecord> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id", userId);
        wrapper.eq("interface_id", interfaceId);
        CallRecord one = getOne(wrapper);
        if (one == null) {
            //创建
            CallRecord callRecord = new CallRecord();
            callRecord.setUserId(userId);
            callRecord.setInterfaceId(interfaceId);
            boolean save = save(callRecord);
            ThrowUtils.throwIf(!save, ErrorCode.SYSTEM_ERROR);
        }else {
            //修改
            UpdateWrapper<CallRecord> wrapper2 = new UpdateWrapper<>();
            wrapper2.eq("id", one.getId());
            wrapper2.setSql("call_count = call_count + 1");
            boolean update = update(wrapper2);
            ThrowUtils.throwIf(!update, ErrorCode.SYSTEM_ERROR);
        }
        return true;
    }

    @Override
    public List<CallRecordVO> getCallRecordList(Long userId) {
        if (userId == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "请求参数为空");
        }
        List<CallRecordVO> callRecordVOS = new ArrayList<>();
        QueryWrapper<CallRecord> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id",userId);
        List<CallRecord> list = list(wrapper);
        for (CallRecord callRecord : list) {
            CallRecordVO callRecordVO = new CallRecordVO();
            callRecordVO.setUserCallCount(callRecord.getCallCount());
            callRecordVO.setLastCallTime(callRecord.getUpdateTime());
            InterfaceInfo interfaceInfo = interfaceInfoService.getById(callRecord.getInterfaceId());
            BeanUtils.copyProperties(interfaceInfo,callRecordVO);
            callRecordVOS.add(callRecordVO);
        }
        return callRecordVOS;
    }
}




