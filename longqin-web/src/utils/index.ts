/**
 * Check if an element has a class
 * @param {HTMLElement} ele
 * @param {string} cls
 * @returns {boolean}
 */
export function hasClass(ele: HTMLElement, cls: string) {
  return !!ele.className.match(new RegExp("(\\s|^)" + cls + "(\\s|$)"));
}

/**
 * Add class to element
 * @param {HTMLElement} ele
 * @param {string} cls
 */
export function addClass(ele: HTMLElement, cls: string) {
  if (!hasClass(ele, cls)) ele.className += " " + cls;
}

/**
 * Remove class from element
 * @param {HTMLElement} ele
 * @param {string} cls
 */
export function removeClass(ele: HTMLElement, cls: string) {
  if (hasClass(ele, cls)) {
    const reg = new RegExp("(\\s|^)" + cls + "(\\s|$)");
    ele.className = ele.className.replace(reg, " ");
  }
}

/**
 * 判断是否是外部链接
 *
 * @param {string} path
 * @returns {Boolean}
 */
export function isExternal(path: string) {
  const isExternal = /^(https?:|http?:|mailto:|tel:)/.test(path);
  return isExternal;
}

/**
 * 更改自定义列表中显示内容
 *
 * @param {[]} tableDataList 表格数据
 * @param {[]]} widgetList 表单组件
 */
 export function changeTableData(tableDataList: [], widgetList: []) {
  for(let i = 0; i < tableDataList.length; i++){
    let row = tableDataList[i];
    for(let key in row){
      const widget = widgetList.find((item) => item.options.name === key);
      if (widget){
        if (widget.type == "select" || widget.type == "radio"){
          let optionItems = widget.options.optionItems;
          for(let j = 0; j < optionItems.length; j++){
            let optionItem = optionItems[j];
            if (optionItem.value == row[key]){
              row[key] = optionItem.label;
            }
          }
        }
        else if (widget.type == "checkbox"){
          let optionItems = widget.options.optionItems;
          let val = [];
          for(let j = 0; j < optionItems.length; j++){
            let optionItem = optionItems[j];
            if (row[key] && row[key].includes(optionItem.value)){
              val.push(optionItem.label);
            }
          }
          row[key] = val.join();
        }
      }
    }
  }
}
