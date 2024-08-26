import request from "@/utils/request";

const NOTICE_BASE_URL = "/notice";

class NoticeAPI {
  /**
   * 获取公告分页列表
   *
   * @param queryParams 查询参数
   */
  static getPage(queryParams: NoticePageQuery) {
    return request<any, PageResult<NoticePageVO[]>>({
      url: `${NOTICE_BASE_URL}/getNoticePage`,
      method: "get",
      params: queryParams,
    });
  }

  /**
   * 添加公告
   *
   * @param data 公告表单数据
   */
  static add(data: NoticeForm) {
    return request({
      url: `${NOTICE_BASE_URL}/create`,
      method: "post",
      data: data,
    });
  }

  /**
   * 修改公告
   *
   * @param data 公告表单数据
   */
  static update(data: NoticeForm) {
    return request({
      url: `${NOTICE_BASE_URL}/update`,
      method: "post",
      data: data,
    });
  }

  /**
   * 删除公告
   *
   * @param id 公告ID
   */
  static deleteById(id: number) {
    return request({
      url: `${NOTICE_BASE_URL}/delete?noticeId=${id}`,
      method: "post",
    });
  }
}

export default NoticeAPI;

/**
 * 公告分页查询对象
 */
export interface NoticePageQuery extends PageQuery {
  /** 标题 */
  title?: string;
  /** 开始时间 */
  beginDate?: string;
  /** 结束时间 */
  endDate?: string;
}

/** 公告分页对象 */
export interface NoticePageVO {
  /** 标题 */
  title?: string;
  /** 创建时间 */
  createTime?: Date;
  /** 紧急程度：1-普通，2-紧急，3-加急 */
  noticeLevel?: number;
  /** 保密程度：1-公开，2-内部公开，3-机密 */
  security?: number;
  /** 发文人 */
  creatorName?: string;
  /** 发文部门 */
  departmentName?: string;
  /** 公告内容 */
  content?: string;
  /** 附件 */
  attachments?: string;
}

/** 公告表单类型 */
export interface NoticeForm {
  /** ID */
  noticeId?: string;
  /** 标题 */
  title?: string;
  /** 紧急程度：1-普通，2-紧急，3-加急 */
  noticeLevel?: number;
  /** 保密程度：1-公开，2-内部公开，3-机密 */
  security?: number;
  /** 公告内容 */
  content?: string;
  /** 附件 */
  attachments?: string;
}
