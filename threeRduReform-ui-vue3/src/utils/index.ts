import { parseTime } from './ruoyi';

/**
 * 表格时间格式化
 */
export function formatDate(cellValue: any): string {
  if (cellValue == null || cellValue == '') return '';
  const date = new Date(cellValue);
  const year = date.getFullYear();
  const month = date.getMonth() + 1 < 10 ? '0' + (date.getMonth() + 1) : date.getMonth() + 1;
  const day = date.getDate() < 10 ? '0' + date.getDate() : date.getDate();
  const hours = date.getHours() < 10 ? '0' + date.getHours() : date.getHours();
  const minutes = date.getMinutes() < 10 ? '0' + date.getMinutes() : date.getMinutes();
  const seconds = date.getSeconds() < 10 ? '0' + date.getSeconds() : date.getSeconds();
  return year + '-' + month + '-' + day + ' ' + hours + ':' + minutes + ':' + seconds;
}

/**
 * @param time
 * @param option
 * @returns {string}
 */
export function formatTime(time: number | string, option?: string): string {
  if (('' + time).length === 10) {
    time = parseInt(time as string) * 1000;
  } else {
    time = +time;
  }
  const d = new Date(time);
  const now = Date.now();

  const diff = (now - d.getTime()) / 1000;

  if (diff < 30) {
    return '刚刚';
  } else if (diff < 3600) {
    // less 1 hour
    return Math.ceil(diff / 60) + '分钟前';
  } else if (diff < 3600 * 24) {
    return Math.ceil(diff / 3600) + '小时前';
  } else if (diff < 3600 * 24 * 2) {
    return '1天前';
  }
  if (option) {
    return parseTime(time, option) || '';
  } else {
    return d.getMonth() + 1 + '月' + d.getDate() + '日' + d.getHours() + '时' + d.getMinutes() + '分';
  }
}

/**
 * @param url
 * @returns {Object}
 */
export function getQueryObject(url?: string | null): Record<string, string> {
  url = url == null ? window.location.href : url;
  const search = url.substring(url.lastIndexOf('?') + 1);
  const obj: Record<string, string> = {};
  const reg = /([^?&=]+)=([^?&=]*)/g;
  search.replace(reg, (rs, $1: string, $2: string) => {
    const name = decodeURIComponent($1);
    let val = decodeURIComponent($2);
    val = String(val);
    obj[name] = val;
    return rs;
  });
  return obj;
}

/**
 * @param input value
 * @returns {number} output value
 */
export function byteLength(str: string): number {
  // returns the byte length of an utf8 string
  let s = str.length;
  for (let i = str.length - 1; i >= 0; i--) {
    const code = str.charCodeAt(i);
    if (code > 0x7f && code <= 0x7ff) s++;
    else if (code > 0x7ff && code <= 0xffff) s += 2;
    if (code >= 0xdc00 && code <= 0xdfff) i--;
  }
  return s;
}

/**
 * @param actual
 * @returns {Array}
 */
export function cleanArray(actual: any[]): any[] {
  const newArray: any[] = [];
  for (let i = 0; i < actual.length; i++) {
    if (actual[i]) {
      newArray.push(actual[i]);
    }
  }
  return newArray;
}

/**
 * @param json
 * @returns {Array}
 */
export function param(json: Record<string, any>): string {
  if (!json) return '';
  return cleanArray(
    Object.keys(json).map((key) => {
      if (json[key] === undefined) return '';
      return encodeURIComponent(key) + '=' + encodeURIComponent(json[key]);
    }),
  ).join('&');
}

/**
 * @param url
 * @returns {Object}
 */
export function param2Obj(url: string): Record<string, string> {
  const search = decodeURIComponent(url.split('?')[1]).replace(/\+/g, ' ');
  if (!search) {
    return {};
  }
  const obj: Record<string, string> = {};
  const searchArr = search.split('&');
  searchArr.forEach((v) => {
    const index = v.indexOf('=');
    if (index !== -1) {
      const name = v.substring(0, index);
      const val = v.substring(index + 1, v.length);
      obj[name] = val;
    }
  });
  return obj;
}

/**
 * @param val
 * @returns {string}
 */
export function html2Text(val: string): string {
  const div = document.createElement('div');
  div.innerHTML = val;
  return div.textContent || div.innerText || '';
}

/**
 * Merges two objects, giving the last one precedence
 * @param target
 * @param source
 * @returns {Object}
 */
export function objectMerge(target: any, source: any): any {
  if (typeof target !== 'object') {
    target = {};
  }
  if (Array.isArray(source)) {
    return source.slice();
  }
  Object.keys(source).forEach((property) => {
    const sourceProperty = source[property];
    if (typeof sourceProperty === 'object') {
      target[property] = objectMerge(target[property], sourceProperty);
    } else {
      target[property] = sourceProperty;
    }
  });
  return target;
}

/**
 * @param element
 * @param className
 */
export function toggleClass(element: HTMLElement, className: string): void {
  if (!element || !className) {
    return;
  }
  let classString = element.className;
  const nameIndex = classString.indexOf(className);
  if (nameIndex === -1) {
    classString += ' ' + className;
  } else {
    classString = classString.substr(0, nameIndex) + classString.substr(nameIndex + className.length);
  }
  element.className = classString;
}

/**
 * @param type
 * @returns {Date}
 */
export function getTime(type?: string): number {
  if (type === 'start') {
    return new Date().getTime() - 3600 * 1000 * 24 * 90;
  } else {
    return new Date(new Date().toDateString()).getTime();
  }
}

/**
 * 防抖函数
 * @param func 要执行的函数
 * @param wait 等待时间（毫秒）
 * @param immediate 是否立即执行（true: 先执行再等待，false: 先等待再执行）
 * @returns 包装后的防抖函数
 */
export function debounce<T extends (...args: any[]) => any>(func: T, wait: number, immediate: boolean = false): (...args: Parameters<T>) => ReturnType<T> | undefined {
  let timeout: NodeJS.Timeout | null = null;
  let args: Parameters<T> | null = null;
  let context: any = null;
  let timestamp: number = 0;
  let result: ReturnType<T> | undefined;

  const later = function (): void {
    // 计算距离上一次触发的时间间隔
    const last = Date.now() - timestamp;

    // 如果上次触发时间间隔小于设定的等待时间，并且大于0
    if (last < wait && last > 0) {
      timeout = setTimeout(later, wait - last);
    } else {
      timeout = null;
      // 如果设定为 immediate === true，因为开始边界已经调用过了，此处无需调用
      if (!immediate) {
        result = func.apply(context, args as Parameters<T>);
        if (!timeout) {
          context = null;
          args = null;
        }
      }
    }
  };

  return function (this: unknown, ...innerArgs: Parameters<T>): ReturnType<T> | undefined {
    context = this;
    timestamp = Date.now();
    args = innerArgs;
    const callNow = immediate && !timeout;

    // 如果延时不存在，重新设定延时
    if (!timeout) {
      timeout = setTimeout(later, wait);
    }

    if (callNow) {
      result = func.apply(context, args);
      context = null;
      args = null;
    }

    return result;
  };
}

/**
 * This is just a simple version of deep copy
 * Has a lot of edge cases bug
 * If you want to use a perfect deep copy, use lodash's _.cloneDeep
 * @param source
 * @returns {Object}
 */
export function deepClone<T>(source: T): T {
  if (!source && typeof source !== 'object') {
    throw new Error('error arguments');
  }
  const targetObj = (source as any).constructor === Array ? [] : {};
  Object.keys(source as any).forEach((keys) => {
    if ((source as any)[keys] && typeof (source as any)[keys] === 'object') {
      (targetObj as any)[keys] = deepClone((source as any)[keys]);
    } else {
      (targetObj as any)[keys] = (source as any)[keys];
    }
  });
  return targetObj as T;
}

/**
 * @param arr
 * @returns {Array}
 */
export function uniqueArr<T>(arr: T[]): T[] {
  return Array.from(new Set(arr));
}

/**
 * @returns {string}
 */
export function createUniqueString(): string {
  const timestamp = +new Date() + '';
  const randomNum = parseInt((1 + Math.random()) * 65536 + '') + '';
  return (+(randomNum + timestamp)).toString(32);
}

/**
 * Check if an element has a class
 * @param elm
 * @param cls
 * @returns {boolean}
 */
export function hasClass(ele: HTMLElement, cls: string): boolean {
  return !!ele.className.match(new RegExp('(\\s|^)' + cls + '(\\s|$)'));
}

/**
 * Add class to element
 * @param ele
 * @param cls
 */
export function addClass(ele: HTMLElement, cls: string): void {
  if (!hasClass(ele, cls)) ele.className += ' ' + cls;
}

/**
 * Remove class from element
 * @param ele
 * @param cls
 */
export function removeClass(ele: HTMLElement, cls: string): void {
  if (hasClass(ele, cls)) {
    const reg = new RegExp('(\\s|^)' + cls + '(\\s|$)');
    ele.className = ele.className.replace(reg, ' ');
  }
}

export function makeMap(str: string, expectsLowerCase?: boolean): (val: string) => boolean {
  const map = Object.create(null);
  const list = str.split(',');
  for (let i = 0; i < list.length; i++) {
    map[list[i]] = true;
  }
  return expectsLowerCase ? (val: string) => map[val.toLowerCase()] : (val: string) => map[val];
}

export const exportDefault = 'export default ';

export const beautifierConf = {
  html: {
    indent_size: '2',
    indent_char: ' ',
    max_preserve_newlines: '-1',
    preserve_newlines: false,
    keep_array_indentation: false,
    break_chained_methods: false,
    indent_scripts: 'separate',
    brace_style: 'end-expand',
    space_before_conditional: true,
    unescape_strings: false,
    jslint_happy: false,
    end_with_newline: true,
    wrap_line_length: '110',
    indent_inner_html: true,
    comma_first: false,
    e4x: true,
    indent_empty_lines: true,
  },
  js: {
    indent_size: '2',
    indent_char: ' ',
    max_preserve_newlines: '-1',
    preserve_newlines: false,
    keep_array_indentation: false,
    break_chained_methods: false,
    indent_scripts: 'normal',
    brace_style: 'end-expand',
    space_before_conditional: true,
    unescape_strings: false,
    jslint_happy: true,
    end_with_newline: true,
    wrap_line_length: '110',
    indent_inner_html: true,
    comma_first: false,
    e4x: true,
    indent_empty_lines: true,
  },
};

// 首字母大小
export function titleCase(str: string): string {
  return str.replace(/( |^)[a-z]/g, (L) => L.toUpperCase());
}

// 下划转驼峰
export function camelCase(str: string): string {
  return str.replace(/_[a-z]/g, (str1) => str1.substr(-1).toUpperCase());
}

export function isNumberStr(str: string): boolean {
  return /^[+-]?(0|([1-9]\d*))(\.\d+)?$/g.test(str);
}

//获取文件名后缀名
export function extension(filename) {
  if (!filename) return '';
  let suffix = '';
  let name = filename.toLowerCase();
  let i = name.lastIndexOf('.');
  if (i > -1) {
    suffix = name.substring(i + 1);
  }
  return suffix;
}

// 根据身份证号码获取
export function getIdNumberInfo(card_num) {
  if (card_num != null) {
    //获取出生日期
    let birth = `${card_num.substring(6, 10)}-${card_num.substring(10, 12)}-${card_num.substring(12, 14)}`;
    //获取性别 奇数表示男性,偶数表示女性
    let sex = '';
    if (parseInt(card_num.substr(16, 1)) % 2 == 1) {
      sex = '1'; // 男
    } else {
      sex = '0'; // 女
    }
    //获取年龄
    let myDate = new Date();
    let month = myDate.getMonth() + 1;
    let day = myDate.getDate();
    let age = myDate.getFullYear() - card_num.substring(6, 10) - 1;
    if (card_num.substring(10, 12) < month || (card_num.substring(10, 12) == month && card_num.substring(12, 14) <= day)) {
      age++;
    }
    return {
      age,
      sex,
      birth,
    };
  }
}
export function validateMail(val) {
  var isMail = /^[a-z0-9]+([._\\-]*[a-z0-9])*@([a-z0-9]+[-a-z0-9]*[a-z0-9]+.){1,63}[a-z0-9]+$/; //电子邮箱
  if (isMail.test(val)) {
    return true;
  } else {
    return false;
  }
}
export function validatePhone(val) {
  var isPhone = /^(13[0-9]|14[01456879]|15[0-35-9]|16[2567]|17[0-8]|18[0-9]|19[0-35-9])\d{8}$/; //手机号码
  var isMob = /^0\d{2,3}-?\d{7,8}$/; // 座机格式 区号之后用'-'隔开
  if (isMob.test(val) || isPhone.test(val)) {
    return true;
  } else {
    return false;
  }
}

// 获取第一条路径的地址
export function getFirstPath(firstMenuPath) {
  let path = '';
  parseTree([firstMenuPath]);

  function parseTree(treeNodes) {
    if (!treeNodes || !treeNodes.length) return;
    for (let i = 0; i < treeNodes.length; i++) {
      if (!path) {
        path = treeNodes[i].path;
      } else {
        if (path == '/' || treeNodes[i].path[0] == '/') {
          path += `${treeNodes[i].path}`;
        } else {
          path += `/${treeNodes[i].path}`;
        }
      }
      let _children = treeNodes[i].children;
      if (_children && _children.length > 0) {
        parseTree(_children);
      } else {
        return;
      }
    }
  }
  return path;
}
export function traverseTree(node, path = [], callback) {
  // 处理当前节点
  const nodePath = [...path, node.path]; // 当前路径加上当前节点值
  callback(nodePath);
  // 递归遍历子节点
  if (node.children) {
    node.children.forEach((child) => {
      traverseTree(child, nodePath, callback);
    });
  }
}

// 解析JSON
export function parseJSON(str) {
  if (typeof str !== 'string' || str.trim() === '') {
    return false;
  }
  try {
    return JSON.parse(str);
  } catch (error) {
    console.error('JSON parse error:', { error, str });
    return false;
  }
}
