import request from "@/utils/request";

const LOG_BASE_URL = "/errorlog";

class ErrorLogAPI {
  /**
   * 获取日志分页列表
   *
   * @param queryParams 查询参数
   */
  static getPage(queryParams: ErrorLogPageQuery) {
    return request<any, PageResult<ErrorLogPageVO[]>>({
      url: `${LOG_BASE_URL}/getLogPage`,
      method: "get",
      params: queryParams,
    });
  }
}

export default ErrorLogAPI;

/**
 * 日志分页查询对象
 */
export interface ErrorLogPageQuery extends PageQuery {
  /** 开始时间 */
  beginDate?: string;
  /** 结束时间 */
  endDate?: string;
}

/**
 * 错误日志分页VO
 */
export interface ErrorLogPageVO {
  /** 主键 */
  logId: number;
  /** 浏览器 */
  broswer: string;
  /** 异常信息 */
  message: string;
  /** 异常堆栈 */
  stacktrace: string;
  /** 操作人 */
  nickName: string;
  /** 异常时间 */
  createTime: Date;
  /** IP 地址 */
  ip: string;
  /** 异常类 */
  errorClass: string;
}