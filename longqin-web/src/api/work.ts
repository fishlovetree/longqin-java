import request from "@/utils/request";
import { FormData } from "@/api/desform";

const WORK_BASE_URL = "/bus/wfWork";

class WorkAPI {
  /**
   * 获取待办工作分页列表
   *
   * @param queryParams 查询参数
   */
  static getProcessPage(queryParams: WorkPageQuery) {
    return request<any, PageResult<WorkPageVO[]>>({
      url: `${WORK_BASE_URL}/getProcessPage`,
      method: "get",
      params: queryParams,
    });
  }

  /**
   * 获取流程开始节点表单
   *
   * @param flowId 流程id
   */
  static getFlowBeginNodeForm(flowId: number) {
    return request<any, FormData>({
      url: `${WORK_BASE_URL}/getFlowBeginNodeForm?flowId=${flowId}`,
      method: "get",
    });
  }

  /**
   * 获取流程进程表单
   *
   * @param processId 流程进程id
   */
   static getFlowProcessForm(processId: number) {
    return request<any>({
      url: `${WORK_BASE_URL}/getFlowProcessForm?processId=${processId}`,
      method: "get",
    });
  }

  /**
   * 获取工作实例所有表单和数据
   *
   * @param workId 工作实例id
   */
   static getWorkFlowProcessList(workId: number) {
    return request<any>({
      url: `${WORK_BASE_URL}/getWorkFlowProcessList?workId=${workId}`,
      method: "get",
    });
  }

  /**
   * 获取工作实例创建人
   *
   * @param workId 工作实例id
   */
   static getCreator(workId: number) {
    return request<any>({
      url: `${WORK_BASE_URL}/getCreator?workId=${workId}`,
      method: "get",
    });
  }

  /**
   * 流程处理
   *
   * @param formData 表单数据
   */
   static dealWork(formData) {
    return request({
      url: `${WORK_BASE_URL}/dealWork`,
      method: "post",
      data: formData,
    });
  }

  /**
   * 流程转办
   *
   * @param processId 流程进程id
   * @param transferUser 转办人id
   */
   static workTransfer(processId: number, transferUser: string) {
    return request({
      url: `${WORK_BASE_URL}/workTransfer?processId=${processId}&transferUser=${transferUser}`,
      method: "post",
    });
  }

  /**
   * 流程作废
   *
   * @param workId 工作实例id
   */
   static disableWork(workId: number) {
    return request({
      url: `${WORK_BASE_URL}/disableWork?workId=${workId}`,
      method: "post",
    });
  }

  /**
   * 获取历史记录分页列表
   *
   * @param queryParams 查询参数
   */
   static getWorkSteps(queryParams: StepPageQuery) {
    return request<any, PageResult<StepPageVO[]>>({
      url: `${WORK_BASE_URL}/getWorkSteps`,
      method: "get",
      params: queryParams,
    });
  }
}

export default WorkAPI;

/**
 * 工作分页查询对象
 */
export interface WorkPageQuery extends PageQuery {
  /** 开始时间 */
  beginDate?: string;
  /** 结束时间 */
  endDate?: string;
  /** 1-待办，0-已办 */
  status: number;
}

/**
 * 工作分页VO
 */
export interface WorkPageVO {
  /** 主键 */
  processId: number;
  /** 流程名称 */
  flowName: string;
  /** 节点名称 */
  nodeName: string;
  /** 发起人 */
  creatorName: string;
  /** 发起时间 */
  createTime: Date;
  /** 提交时间 */
  submitTime: Date;
  /** 发起人部门 */
  departmentName: string;
}

/**
 * 历史记录分页查询对象
 */
 export interface StepPageQuery extends PageQuery {
  /** 工作实例id */
  workId?: number;
}

/**
 * 历史记录分页VO
 */
 export interface StepPageVO {
   /** 主键 */
  stepId: number;
  /** 工作实例id */
  workId: number;
  /** 节点id */
  nodeId: number;
  /** 工作进程id */
  processId: number;
  /** 节点名称 */
  nodeName: string;
  /** 处理人 */
  submitterName: string;
  /** 处理时间 */
  submitTime: Date;
  /** 停留时长 */
  stayTime: string;
}