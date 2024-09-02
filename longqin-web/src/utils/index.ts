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
  let map = {};
  let arr = [];
  transferWidget(widgetList, map, arr);
  for(let i = 0; i < tableDataList.length; i++){
    let row = tableDataList[i];
    for(let key in row){
      const widget = arr.find((item) => item.options.name === key);
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
        else if (widget.type == "file-upload"){
          let html = "";
          let fileList = JSON.parse(row[key]);
          for(let k = 0; k < fileList.length; k++){
            let file = fileList[k];
            html += "<p v-html><a href='" + file.response.data[0].url + "' target='_blank' style='text-decoration:underline;'>" + file.name + "</a></p>";
          }
          html += "";
          row[key] = html;
        }
      }
    }
  }
}

/**
 * 判断是否是数字
 *
 * @param {[]} value 数据
 */
 export function isNumber(value: string): boolean {
  return !isNaN(parseFloat(value)) && !isNaN(Number(value));
}

/**
 * 处理自定义表单中显示数据
 *
 * @param {[]} formData 表单原始数据
 * @param {[]} widgetList 表单组件列表
 */
 export function transferFormData(formData: [], widgetList: []) {
   let map = {};
   let arr = [];
   transferWidget(widgetList, map, arr);
  for(let key in formData){
    let type = map[key];
    if (!type) continue;
    switch (type)
    {
      case "file-upload":
        if (formData[key]){
          let json = JSON.parse(formData[key]);
          for (let i = 0; i < json.length; i++){
            json[i].url = json[i].response.data[0].url;
          }
          formData[key] = json;
        }
        break;
      case "number":
      case "radio":
      case "rate":
      case "slider":
        if (isNumber(formData[key])){
          formData[key] = Number(formData[key])
        }
        break;
      case "switch":
        if (formData[key] === "true"){
          formData[key] = true;
        }
        else if (formData[key] === "false"){
          formData[key] = false;
        }
        break;
      case "checkbox":
      case "select":
      case "time-range":
      case "date-range":
      case "cascader":
        if (formData[key].includes('[') && formData[key].includes(']')){
          let str = formData[key].substring(1, formData[key].length - 1);
          formData[key] = str.split(',');
          for(let i = 0; i < formData[key].length; i++){
            formData[key][i] = formData[key][i].trim();
            if (isNumber(formData[key][i])){
              formData[key][i] = Number(formData[key][i]);
            }
          }
        }
        else if (isNumber(formData[key])){
          formData[key] = Number(formData[key])
        }
        break;
      default:
          break;
    }
  }
}

/**
 * 转换组件清单为键值对和数组
 *
 * @param {[]} widgetList 表单组件列表
 * @param {[]} map 表单组件键值对结果
 * @param {[]} arr 表单组件数组
 */
function transferWidget(widgetList: [], map: any, arr: []){
  for(let i = 0; i < widgetList.length; i++){
    let widget = widgetList[i];
    let type = widget.type;
    switch (type)
    {
      case "grid":
        let cols = widget.cols;
        for (let j = 0 ; j < cols.length; j++){
          let col = cols[j];
          transferWidget(col.widgetList, map, arr);
        }
        break;
      case "table":
        let rows = widget.rows;
        for (let j = 0 ; j < rows.length; j++){
          let row = rows[j];
          let rowCols = row.cols;
          for (let k = 0; k < rowCols.length; k++){
            let rowCol = rowCols[k];
            transferWidget(rowCol.widgetList, map, arr);
          }
        }
        break;
      case "tab":
        let tabs = widget.tabs;
        for (let j = 0 ; j < tabs.length; j++){
          let tab = tabs[j];
          transferWidget(tab.widgetList, map, arr);
        }
        break;
      case "card":
        transferWidget(widget.widgetList, map, arr);
        break;
      default:
        if (widget.options.name){
          map[widget.options.name] = widget.type;
        }
        arr.push(widget);
        break;
    }
  }
}
