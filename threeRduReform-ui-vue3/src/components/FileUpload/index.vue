<template>
  <div class="upload-file">
    <el-upload
      multiple
      :action="uploadFileUrl"
      :before-upload="handleBeforeUpload"
      :data="data"
      :limit="limit"
      :drag="drag"
      :on-progress="handleProgress"
      :on-error="handleUploadError"
      :on-exceed="handleExceed"
      :on-success="handleUploadSuccess"
      :show-file-list="false"
      :headers="headers"
      :file-list="fileList"
      class="upload-file-uploader"
      :class="{ hide: limit === 1 && fileList.length >= limit }"
      ref="fileUpload"
      v-if="!disabled">
      <slot>
        <!-- 上传按钮 -->
        <el-button size="mini" type="primary" v-if="!disabled">选取文件</el-button>
        <!-- 上传提示 -->
        <div class="el-upload__tip" v-if="showTip && !disabled && drag">
          <slot name="tip">
            请上传
            <template v-if="fileSize">
              大小不超过
              <b style="color: #f56c6c">{{ fileSize }}MB</b>
            </template>
            <template v-if="fileType">
              格式为
              <b style="color: #f56c6c">{{ fileType.join('/') }}</b>
            </template>
            的文件
            <template v-if="extraStr">
              <br />
              {{ extraStr }}
            </template>
          </slot>
        </div>
      </slot>
    </el-upload>
    <!-- 上传提示 -->
    <div class="el-upload__tip" v-if="showTip && !disabled && !drag">
      <slot name="tip">
        请上传
        <template v-if="fileSize">
          大小不超过
          <b style="color: #f56c6c">{{ fileSize }}MB</b>
        </template>
        <template v-if="fileType">
          格式为
          <b style="color: #f56c6c">{{ fileType.join('/') }}</b>
        </template>
        的文件
        <template v-if="extraStr">
          <br />
          {{ extraStr }}
        </template>
      </slot>
    </div>

    <!-- 上传中的文件：只展示还没到 100% 的文件进度 -->
    <transition-group v-if="computUploadingFiles.length" class="upload-file-list el-upload-list el-upload-list--text is-uploading" name="el-fade-in-linear" tag="ul">
      <li :key="file.uid" class="el-upload-list__item ele-upload-list__item-content is-uploading-item" v-for="file in computUploadingFiles">
        <div class="upload-file-list__progress">
          <div class="upload-file-list__progress-header">
            <span class="el-icon-document">{{ getFileName(file.name) }}</span>
            <span class="upload-file-list__progress-text">{{ file.percentage }}%</span>
          </div>
          <el-progress :percentage="file.percentage" :stroke-width="4" :show-text="false"></el-progress>
        </div>
      </li>
    </transition-group>

    <!-- 已上传的文件列表 -->
    <transition-group v-if="showFileList" ref="uploadFileList" class="upload-file-list el-upload-list el-upload-list--text" name="el-fade-in-linear" tag="ul">
      <li :key="file.url" class="el-upload-list__item ele-upload-list__item-content" v-for="(file, index) in fileList">
        <el-link :href="getHref(file.url)" underline="never" target="_blank">
          <span class="el-icon-document">{{ getFileName(file.name) }}</span>
        </el-link>
        <div class="ele-upload-list__item-content-action" v-if="!disabled">
          <el-link underline="never" @click="handleDelete(index)" type="danger">&nbsp;删除</el-link>
        </div>
      </li>
    </transition-group>
  </div>
</template>

<script setup lang="ts" name="FileUpload">
  import { getToken } from '@/utils/auth';
  import Sortable from 'sortablejs';
  import type { UploadFileResult } from '@/types/api/common';

  interface UploadFileItem {
    uid?: number | string;
    name: string;
    url: string;
    percentage?: number;
  }

  interface UploadFile extends File {
    uid?: string;
    raw?: any;
  }

  const props = defineProps({
    modelValue: [String, Object, Array],
    // 模型值类型，array: 数组，string: 字符串
    valueType: {
      type: String,
      default: 'array',
    },
    // 上传接口地址
    action: {
      type: String,
      default: '/common/upload',
    },
    // 上传携带的参数
    data: {
      type: Object,
    },
    // 数量限制
    limit: {
      type: Number,
      default: 5,
    },
    // 大小限制(MB)
    fileSize: {
      type: Number,
      default: 5,
    },
    // 文件类型, 例如['png', 'jpg', 'jpeg']
    fileType: {
      type: Array as () => string[],
      default: () => ['doc', 'docx', 'xls', 'xlsx', 'ppt', 'pptx', 'txt', 'pdf'],
    },
    // 是否显示提示
    isShowTip: {
      type: Boolean,
      default: true,
    },
    // 额外提示
    extraStr: {
      type: String,
      default: '',
    },
    // 禁用组件（仅查看文件）
    disabled: {
      type: Boolean,
      default: false,
    },
    // 是否拖拽上传
    drag: {
      type: Boolean,
      default: false,
    },
    // 拖动排序
    dragSort: {
      type: Boolean,
      default: true,
    },
    // 是否显示文件列表
    showFileList: {
      type: [Boolean],
      default: true,
    },
    // 额外的移除逻辑
    extraRemove: {
      type: Boolean,
      default: false,
    },
  });

  const { proxy } = getCurrentInstance();
  const emit = defineEmits('update:modelValue', 'change');
  const number = ref(0);
  const baseUrl = import.meta.env.VITE_APP_BASE_API;
  const uploadFileUrl = ref(import.meta.env.VITE_APP_BASE_API + props.action); // 上传文件服务器地址
  const headers = ref({ Authorization: 'Bearer ' + getToken() });
  const fileList = ref<UploadFileItem[]>([]);
  const uploadingFiles = ref<UploadFileItem[]>([]);
  const showTip = computed(() => props.isShowTip && (props.fileType || props.fileSize));
  const getHref = computed(() => {
    return (url: string) => {
      if (!url) return '';
      let path = `${baseUrl}${url}`;
      return path;
    };
  });
  const computUploadingFiles = computed<UploadFileItem[]>(() =>
    uploadingFiles.value.filter((item: UploadFileItem) => {
      if (item.percentage == 100 && !fileList.value.some((file: UploadFileItem) => file.uid === item.uid)) {
        return true;
      }
      return item.percentage != 100;
    }),
  );

  watch(
    () => props.modelValue,
    (val: any) => {
      if (val) {
        let temp = 1;
        // 首先将值转为数组

        let list: UploadFileItem[];
        if (val instanceof Array) {
          const newVal = val.map((v) => {
            v.name = v.attachName || v.name;
            v.url = v.path || v.url;
            return v;
          });
          list = Array.isArray(newVal) ? newVal : props.modelValue.split(',');
        } else {
          list = Array.isArray(val) ? val : props.modelValue.split(',');
        }
        // 然后将数组转为对象数组
        fileList.value = list.map((item: any) => {
          if (typeof item === 'string') {
            item = { name: item, url: item, path: item };
          }
          item.uid = item.uid || new Date().getTime() + temp++;
          return item;
        });
      } else {
        fileList.value = [];
        return [];
      }
    },
    { deep: true, immediate: true },
  );

  // 上传前校检格式和大小
  function handleBeforeUpload(file: UploadFile): boolean {
    // 限制同一批次并行上传的文件数量，避免超过浏览器并发数
    const uploadingCount = uploadingFiles.value.filter((item: UploadFileItem) => item.percentage != 100).length;
    if (uploadingCount >= 6) {
      proxy.$modal.msgError('同一批上传的文件最多支持 6 个，请分批上传');
      return false;
    }

    // 校检文件类型
    if (props.fileType.length > 0) {
      const fileName = file.name.split('.');
      const fileExt = fileName[fileName.length - 1]?.toLowerCase();
      const isTypeOk = props.fileType.indexOf(fileExt) >= 0;
      if (!isTypeOk) {
        proxy.$modal.msgError(`文件格式不正确，请上传${props.fileType.join('/')}格式文件!`);
        return false;
      }
    }
    // 校检文件名是否包含特殊字符
    if (file.name.includes(',')) {
      proxy.$modal.msgError('文件名不正确，不能包含英文逗号!');
      return false;
    }
    // 校检文件大小
    if (props.fileSize) {
      const isLt = file.size / 1024 / 1024 < props.fileSize;
      if (!isLt) {
        proxy.$modal.msgError(`上传文件大小不能超过 ${props.fileSize} MB!`);
        return false;
      }
    }
    proxy.$modal.loading('正在上传文件，请稍候...');
    number.value++;
    uploadingFiles.value.push({
      uid: file.uid,
      name: file.name,
      percentage: 0,
      status: 'uploading',
    });
    return true;
  }

  // 文件个数超出
  function handleExceed(): void {
    proxy.$modal.msgError(`上传文件数量不能超过 ${props.limit} 个!`);
  }

  // 上传失败
  function handleUploadError(err: Error, file: UploadFile): void {
    // 从上传中的列表中移除失败的文件，并维护上传计数
    if (file && file.uid) {
      uploadingFiles.value = uploadingFiles.value.filter((item: UploadFileItem) => item.uid !== file.uid);
    }
    if (number.value > 0) {
      number.value--;
    }
    if (number.value <= 0) {
      number.value = 0;
      proxy.$modal.closeLoading();
    }
    proxy.$modal.msgError('上传文件失败，请重试');
  }

  // 上传成功回调
  function handleUploadSuccess(res: UploadFileResult, file: UploadFile): void {
    if (res.code === 200) {
      let data = {
        uid: file.uid,
        name: getFileName(res.originalFilename || res.fileName || file.name),
        path: res.fileName || res.url,
        url: res.fileName || res.url,
        size: file.size,
        fileType: file?.raw?.type,
        duration: null,
      };
      // 把单个成功文件加入已上传列表，并维护上传计数 / v-model
      const pushToListAndEmit = () => {
        fileList.value.push(data);

        if (file && file.uid) {
          // 从“上传中”列表移除，后续只在已上传列表中展示
          uploadingFiles.value = uploadingFiles.value.filter((item: UploadFileItem) => item.uid !== file.uid);
        }

        number.value--;
        if (number.value <= 0) {
          number.value = 0;
          proxy.$modal.closeLoading();
        }
        emitValue();
      };

      // 如果上传文件的类型为视频文件，则先获取视频的时长
      if (file?.raw?.type == 'video/mp4') {
        FileValue(file.raw).then((duration) => {
          data.duration = duration;
          pushToListAndEmit();
        });
      } else {
        pushToListAndEmit();
      }
    } else {
      number.value--;
      if (number.value <= 0) {
        number.value = 0;
        proxy.$modal.closeLoading();
      }
      proxy.$modal.msgError(res.msg);
      proxy.$refs.fileUpload.handleRemove(file);
    }
  }

  // 删除文件
  function handleDelete(index: number): void {
    if (props.extraRemove) {
      let data = {
        file: fileList.value[index],
        index: index,
      };
      emit('handleDelete', data);
    } else {
      const removed = fileList.value.splice(index, 1)[0];
      console.log(removed);
      // 同步移除 el-upload 内部列表，避免仍占用 limit
      if (proxy.$refs.fileUpload && removed && removed.uid) {
        const uploader = proxy.$refs.fileUpload;
        if (Array.isArray(uploader.uploadFiles)) {
          uploader.uploadFiles = uploader.uploadFiles.filter((item: UploadFileItem) => item.uid !== removed.uid);
        }
        if (uploader.handleRemove) {
          uploader.handleRemove({ uid: removed.uid });
        }
      }
      emitValue();
    }
  }

  // 获取文件名称
  function getFileName(name: string): string {
    // 如果是url那么取最后的名字 如果不是直接返回
    if (name.lastIndexOf('/') > -1) {
      return name.slice(name.lastIndexOf('/') + 1);
    } else {
      return name;
    }
  }

  // 触发值变更
  function emitValue() {
    let value = fileList.value;
    if (props.valueType === 'string') {
      value = listToString(fileList.value);
    }
    emit('update:modelValue', value);
    emit('change', value);
  }

  // 对象转成指定字符串分隔
  function listToString(list: UploadFileItem[], separator?: string): string {
    let strs = '';
    separator = separator || ',';
    for (let i in list) {
      if (list[i].url) {
        strs += list[i].url + separator;
      }
    }
    return strs != '' ? strs.substr(0, strs.length - 1) : '';
  }

  function FileValue(file: UploadFile): Promise<number> {
    let fileurl = URL.createObjectURL(file);
    let audioElement = new Audio(fileurl);
    // 当指定的音频/视频的元数据已加载时，会发生 loadedmetadata 事件。
    // 音频/视频的元数据包括：时长、尺寸（仅视频）以及文本轨道。
    let duration;
    return new Promise((resolve, reject) => {
      audioElement.addEventListener('loadedmetadata', function (_event) {
        duration = audioElement.duration;
        // console.log("视频的时长为:",duration); //单位：秒
        resolve(duration);
      });
    });
  }
  function handleProgress(event: any, file: UploadFile): void {
    // 有时候 file 可能为 null/undefined，这里先做防御处理
    if (!file || !file.uid) {
      return;
    }
    const target = uploadingFiles.value.find((item: UploadFileItem) => item.uid === file.uid);
    if (target) {
      target.percentage = Math.floor(event.percent);
    }
  }

  // 初始化拖拽排序
  onMounted(() => {
    if (props.dragSort && !props.disabled) {
      nextTick(() => {
        const element = proxy.$refs.uploadFileList?.$el || (proxy.$refs.uploadFileList as HTMLElement);
        if (element) {
          Sortable.create(element, {
            ghostClass: 'file-upload-darg',
            onEnd: (evt) => {
              const movedItem = fileList.value.splice(evt.oldIndex, 1)[0];
              fileList.value.splice(evt.newIndex, 0, movedItem);
              emitValue();
            },
          });
        }
      });
    }
  });
</script>
<style scoped lang="scss">
  .file-upload-darg {
    opacity: 0.5;
    background: #c8ebfb;
  }
  .upload-file-uploader {
    margin-bottom: 10px;
  }
  .upload-file-list {
    margin-top: 0;
  }
  .upload-file-list .el-upload-list__item {
    border: 1px solid #e4e7ed;
    line-height: 2;
    margin-bottom: 10px;
    position: relative;
    transition: none !important;
  }
  .upload-file-list .ele-upload-list__item-content {
    display: flex;
    justify-content: space-between;
    align-items: center;
    color: inherit;

    padding: 6px;
    line-height: 1;
    .el-icon-document {
      line-height: 1.3;
    }
  }
  .ele-upload-list__item-content-action {
    flex-shrink: 0;
    padding-left: 8px;
  }
  .ele-upload-list__item-content-action .el-link {
    margin-right: 10px;
  }

  .upload-file-list__progress {
    width: 100%;
    display: flex;
    flex-direction: column;
    gap: 6px;
  }
  /* 上传中的文件头部：文件名 + 百分比 */
  .upload-file-list__progress-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    font-size: 13px;
    color: #606266;
  }
  .upload-file-list__progress-text {
    flex-shrink: 0;
  }
  /* 上传中的文件不显示默认的删除 icon */
  .upload-file-list.is-uploading .el-upload-list__item .el-icon-close-tip,
  .upload-file-list.is-uploading .el-upload-list__item .el-icon-close {
    display: none !important;
  }

  .el-upload-list__item {
    .el-progress {
      left: 0;
      bottom: 0;
      top: initial;
    }
    .el-progress__text {
      right: 5px;
    }
  }

  .hide {
    display: none;
  }
</style>
