import request from "@/utils/request";

const MATTER_BASE_URL = "/matter";

class MatterAPI {

  /**
   * 获取事项列表
   *
   * @param queryParams 查询参数
   */
   static getList(date: string) {
    return request<any, MatterVO[]>({
      url: `${MATTER_BASE_URL}/getMatterList?date=${date}`,
      method: "get",
    });
  }

  /**
   * 添加事项
   *
   * @param data 事项表单数据
   */
  static add(data: MatterForm) {
    return request({
      url: `${MATTER_BASE_URL}/create`,
      method: "post",
      data: data,
    });
  }

  /**
   * 修改事项
   *
   * @param data 事项表单数据
   */
  static update(data: MatterForm) {
    return request({
      url: `${MATTER_BASE_URL}/update`,
      method: "post",
      data: data,
    });
  }

  /**
   * 更改事项状态
   *
   * @param id 事项ID
   */
   static updateStatus(id: number) {
    return request({
      url: `${MATTER_BASE_URL}/updateStatus?id=${id}`,
      method: "post",
    });
  }

  /**
   * 删除用户
   *
   * @param id 用户ID
   */
  static deleteById(id: number) {
    return request({
      url: `${MATTER_BASE_URL}/delete?id=${id}`,
      method: "post",
    });
  }
}

export default MatterAPI;

/** 事项对象 */
export interface MatterVO {
  /** 事项 */
  matter?: string;
  /** 事项日期 */
  matterDate?: Date;
  /** 事项时间 */
  matterTime?: string;
  /** 创建时间 */
  createTime?: Date;
  /** 事项ID */
  id?: number;
}

/** 事项表单类型 */
export interface MatterForm {
  /** 事项 */
  matter?: string;
  /** 事项日期 */
  matterDate?: Date;
  /** 事项时间 */
  matterTime?: string;
  /** 事项ID */
  id?: number;
}
