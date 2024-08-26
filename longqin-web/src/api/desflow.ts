import request from "@/utils/request";

const FLOW_BASE_URL = "/bus/wfFlow";

class DesflowAPI {
  /** 保存流程 */
  static saveFlow(data: FlowData) {
    return request({
      url: `${FLOW_BASE_URL}/save`,
      method: "post",
      data: data,
    });
  }

  /**
   * 获取流程分页列表
   *
   * @param queryParams 查询参数
   */
   static getPage(queryParams: FlowPageQuery) {
    return request<any, PageResult<FlowPageVO[]>>({
      url: `${FLOW_BASE_URL}/getFlowPage`,
      method: "get",
      params: queryParams,
    });
  }

  /**
   * 根据id获取流程信息
   *
   * @param id 流程ID
   */
   static getById(id: number) {
    return request<any, FlowData>({
      url: `${FLOW_BASE_URL}/getById?flowId=${id}`,
      method: "get",
    });
  }

  /**
   * 根据id获取流程节点和连线
   *
   * @param id 流程ID
   */
   static getFlowJson(id: number) {
    return request<any, FlowData>({
      url: `${FLOW_BASE_URL}/getFlowJson?flowId=${id}`,
      method: "get",
    });
  }

  /**
   * 删除流程
   *
   * @param id 流程ID
   */
   static deleteById(id: number) {
    return request({
      url: `${FLOW_BASE_URL}/delete?flowId=${id}`,
      method: "post",
    });
  }
}

export default DesflowAPI;

/** 保存流程请求参数 */
export interface FlowData {
  /** 流程名称 */
  flowName?: string;
  /** 流程类别 */
  flowSort?: number;
  /** 节点集 */
  nodes?: string;
  /** 连线集 */
  links?: string;
}

/**
 * 流程分页查询对象
 */
 export interface FlowPageQuery extends PageQuery {
  /** 搜索关键字 */
  flowName?: string;
}

/** 流程分页对象 */
export interface FlowPageVO {
  /** id */
  flowId?: number;
  /** 流程名称 */
  flowName?: string;
  /** 流程类别 */
  flowSort?: number;
  /** 节点集 */
  nodes?: string;
  /** 连线集 */
  links?: string;
}
