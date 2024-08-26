import request from "@/utils/request";

const LOG_BASE_URL = "/log";

class LogAPI {
  /**
   * 获取日志分页列表
   *
   * @param queryParams 查询参数
   */
  static getPage(queryParams: LogPageQuery) {
    return request<any, PageResult<LogPageVO[]>>({
      url: `${LOG_BASE_URL}/getLogPage`,
      method: "get",
      params: queryParams,
    });
  }
}

export default LogAPI;

/**
 * 日志分页查询对象
 */
export interface LogPageQuery extends PageQuery {
  /** 开始时间 */
  beginDate?: string;
  /** 结束时间 */
  endDate?: string;
}

/**
 * 系统日志分页VO
 */
export interface LogPageVO {
  /** 主键 */
  logId: number;
  /** 标题 */
  title: string;
  /** 日志内容 */
  remark: string;
  /** 操作人 */
  creatorName: string;
  /** 操作时间 */
  createTime: Date;
  /** IP 地址 */
  ip: string;
  /** 操作类型 */
  operateType: number;
}