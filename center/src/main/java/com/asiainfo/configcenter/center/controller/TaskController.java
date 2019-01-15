package com.asiainfo.configcenter.center.controller;

import com.asiainfo.configcenter.center.common.PageRequestContainer;
import com.asiainfo.configcenter.center.common.PageResultContainer;
import com.asiainfo.configcenter.center.common.Result;
import com.asiainfo.configcenter.center.entity.CcTaskDetailConfigEntity;
import com.asiainfo.configcenter.center.entity.CcTaskDetailPushEntity;
import com.asiainfo.configcenter.center.service.interfaces.ITaskSV;
import com.asiainfo.configcenter.center.vo.task.*;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 任务操作控制层
 * Created by bawy on 2018/8/1 20:01.
 */
@Controller
@RequestMapping(value = "/center/task")
public class TaskController extends BaseController {

    @Resource
    private ITaskSV taskSV;

    @ApiOperation(value = "提交任务", notes = "将待提交任务提交，任务标识、任务名称必填，任务描述选填，任务扩展信息放在extInfo集合中")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(paramType = "body", name = "submitTaskReqVO", value = "提交任务请求参数信息", required = true, dataType = "SubmitTaskReqVO"),
    })
    @RequestMapping(value = "/submitTask",method = RequestMethod.POST)
    @ResponseBody
    public Result<String> submitTask(@RequestBody SubmitTaskReqVO submitTaskReqVO){
        Result<String> result = new Result<>();
        String response = taskSV.submitTask(submitTaskReqVO, getCurrentUser().getId());
        result.setResult(response);
        return result;
    }

    @ApiOperation(value = "获取配置变更任务详情", notes = "获取用户当前环境配置变更待提交任务，任务状态必填, 如果是待审核任务需要填任务id")
    @RequestMapping(value = "/getConfigTaskDetail",method = RequestMethod.POST)
    @ResponseBody
    public Result<PageResultContainer<CcTaskDetailConfigEntity>> getConfigTaskDetail(@RequestBody PageRequestContainer<QueryTaskDetailReqVO> requestContainer){
        Result<PageResultContainer<CcTaskDetailConfigEntity>> result = new Result<>();
        result.setResult(taskSV.getConfigTaskDetail(requestContainer, getCurrentUser().getId()));
        return result;
    }

    @ApiOperation(value = "获取配置推送任务详情", notes = "获取用户当前环境配置推送待提交任务，任务状态必填, 如果是待审核任务需要填任务id")
    @RequestMapping(value = "/getPushTaskDetail",method = RequestMethod.POST)
    @ResponseBody
    public Result<PageResultContainer<CcTaskDetailPushEntity>> getPushTaskDetail(@RequestBody PageRequestContainer<QueryTaskDetailReqVO> requestContainer){
        Result<PageResultContainer<CcTaskDetailPushEntity>> result = new Result<>();
        result.setResult(taskSV.getPushTaskDetail(requestContainer, getCurrentUser().getId()));
        return result;
    }

    @ApiOperation(value = "任务审核", notes = "审核任务，处理状态包括审核通过与审核不通过")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(paramType = "body", name = "taskDealReq", value = "任务审核请求参数,任务标识、步骤、操作类型（2：审核通过，3：审核不通过）必填，操作标记（审核不通过时必填5-200字）、扩展信息选填", required = true, dataType = "TaskDealReqVO"),
    })
    @RequestMapping(value = "/dealTask",method = RequestMethod.POST)
    @ResponseBody
    public Result<String> dealTask(@RequestBody TaskDealReqVO taskDealReq){
        Result<String> result = new Result<>();
        result.setResult(taskSV.dealTask(taskDealReq, getCurrentUser().getId()));
        return result;
    }

    @ApiOperation(value = "撤销待提交任务", notes = "撤销待提交任务，将任务置失效，并将任务详情一并置失效")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(paramType = "body", name = "taskBaseReq", value = "待提交任务撤销请求参数,任务标识必填", required = true, dataType = "TaskBaseReqVO"),
    })
    @RequestMapping(value = "/rollbackTask",method = RequestMethod.POST)
    @ResponseBody
    public Result<String> rollbackTask(@RequestBody TaskBaseReqVO taskBaseReq){
        Result<String> result = new Result<>();
        result.setResult(taskSV.rollbackTask(taskBaseReq, getCurrentUser().getId()));
        return result;
    }

    @ApiOperation(value = "撤销待任务详情", notes = "撤销待提交任务中的一条详情，将任务详情置失效")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(paramType = "body", name = "taskDetailBaseReq", value = "任务详情撤销请求参数，任务标识、任务详情标识必填", required = true, dataType = "TaskDetailBaseReqVO"),
    })
    @RequestMapping(value = "/rollbackTaskDetail",method = RequestMethod.POST)
    @ResponseBody
    public Result<String> rollbackTaskDetail(@RequestBody TaskDetailBaseReqVO taskDetailBaseReq){
        Result<String> result = new Result<>();
        result.setResult(taskSV.rollbackTaskDetail(taskDetailBaseReq, getCurrentUser().getId()));
        return result;
    }

    @ApiOperation(value = "获取已提交任务", notes = "获取当前用户所有已提交的任务，所有查询条件选填")
    @RequestMapping(value = "/getSubmitTask",method = RequestMethod.POST)
    @ResponseBody
    public Result<PageResultContainer<MySubmitTaskVO>> getSubmitTask(@RequestBody PageRequestContainer<MySubmitTaskReqVO> requestContainer){
        Result<PageResultContainer<MySubmitTaskVO>> result = new Result<>();
        result.setResult(taskSV.getSubmitTask(requestContainer, getCurrentUser().getId()));
        return result;
    }

    @ApiOperation(value = "获取待审核任务", notes = "获取当前用户所有待审核的任务，所有查询条件选填")
    @RequestMapping(value = "/getInReviewTask",method = RequestMethod.POST)
    @ResponseBody
    public Result<PageResultContainer<MyInReviewTaskVO>> getInReviewTask(@RequestBody PageRequestContainer<MyInReviewTaskReqVO> requestContainer){
        Result<PageResultContainer<MyInReviewTaskVO>> result = new Result<>();
        result.setResult(taskSV.getInReviewTask(requestContainer, getCurrentUser().getId()));
        return result;
    }

    @ApiOperation(value = "获取已审核任务", notes = "获取当前用户所有已审核的任务，所有查询条件选填")
    @RequestMapping(value = "/getReviewedTask",method = RequestMethod.POST)
    @ResponseBody
    public Result<PageResultContainer<MyReviewedTaskVO>> getReviewedTask(@RequestBody PageRequestContainer<MyReviewedTaskReqVO> requestContainer){
        Result<PageResultContainer<MyReviewedTaskVO>> result = new Result<>();
        result.setResult(taskSV.getReviewedTask(requestContainer, getCurrentUser().getId()));
        return result;
    }

    @ApiOperation(value = "获取任务流程", notes = "根据任务编码获取任务流程")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(paramType = "query", name = "taskId", value = "任务编码", required = true, dataType = "int"),
    })
    @RequestMapping(value = "/getTaskFlow",method = RequestMethod.GET)
    @ResponseBody
    public Result<List<TaskStepVO>> getTaskFlow(@RequestParam("taskId") int taskId){
        Result<List<TaskStepVO>> result = new Result<>();
        result.setResult(taskSV.getTaskFlow(taskId, getCurrentUser().getId()));
        return result;
    }

    @ApiOperation(value = "获取任务下一操作节点的人员信息", notes = "获取任务下一操作节点人员信息，组织信息，人员信息")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(paramType = "body", name = "nextOperatorInfoReq", value = "请求参数", required = true, dataType = "NextOperatorInfoReqVO"),
    })
    @RequestMapping(value = "/getNextOperatorInfo",method = RequestMethod.POST)
    @ResponseBody
    public Result<TaskNextOperatorInfoVO> getNextOperatorInfo(@RequestBody NextOperatorInfoReqVO nextOperatorInfoReq){
        Result<TaskNextOperatorInfoVO> result = new Result<>();
        result.setResult(taskSV.getNextOperatorInfo(nextOperatorInfoReq.getTaskId(), nextOperatorInfoReq.getStep(), getCurrentUser().getId()));
        return result;
    }

    @ApiOperation(value = "获取配置变更任务中刷新策略变更详情", notes = "获取配置变更任务中配置策略变更详情")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(paramType = "query", name = "taskDetailId", value = "请求参数，任务详情标识", required = true, dataType = "int"),
    })
    @RequestMapping(value = "/getStrategyChangeDetail",method = RequestMethod.GET)
    @ResponseBody
    public Result<StrategyChangeDetailVO> getStategyChangeDetail(@RequestParam int taskDetailId){
        Result<StrategyChangeDetailVO> result = new Result<>();
        result.setResult(taskSV.getStrategyChangeDetail(taskDetailId, getCurrentUser().getId()));
        return result;
    }

    @ApiOperation(value = "获取配置变更任务中配置变更详情", notes = "获取配置变更任务中配置变更详情")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(paramType = "query", name = "taskDetailId", value = "请求参数，任务详情标识", required = true, dataType = "int"),
    })
    @RequestMapping(value = "/getConfigChangeDetail",method = RequestMethod.GET)
    @ResponseBody
    public Result<ConfigChangeDetailVO> getConfigChangeDetail(@RequestParam int taskDetailId){
        Result<ConfigChangeDetailVO> result = new Result<>();
        result.setResult(taskSV.getConfigChangeDetail(taskDetailId, getCurrentUser().getId()));
        return result;
    }

    @ApiOperation(value = "任务推送日期和推送实例查询", notes = "获取推送任务的时间和实例信息")
    @RequestMapping(value = "/getTaskTimeAndIns", method = RequestMethod.POST)
    @ResponseBody
    public Result<TaskTimeAndInsVO> getTaskTimeAndIns(@RequestBody TaskInfo taskInfo){
        Result<TaskTimeAndInsVO> result = new Result<>();
        result.setResult(taskSV.getTaskTimeAndIns(taskInfo.getTaskId()));
        return result;
    }

}
