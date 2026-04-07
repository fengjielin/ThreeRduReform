<template>
  <el-dialog :title="title" :model-value="visible" :width="width" top="5vh" append-to-body @update:model-value="visible = $event" @close="close" custom-class="viewer-dialog">
    <!-- 错误提示 -->
    <el-alert v-if="errorMessage" :title="errorMessage" type="error" :closable="true" @close="errorMessage = ''" show-icon style="margin-bottom: 15px" />
    <div id="viewer" style="position: relative" v-if="visible">
      <iframe v-if="!isVideo && !isImg" class="preview_iframe" frameborder="0" marginheight="0" scrolling="auto" :src="path" />
      <div v-else-if="isImg" class="img-wrapper">
        <ImagePreview :src="path" />
      </div>
      <div v-else class="video_content">
        <div v-if="!errorMessage" id="player" class="video_player" />
        <div v-else class="error-placeholder">
          <i class="el-icon-warning" style="font-size: 48px; color: #f56c6c; margin-bottom: 15px" />
          <p style="color: #666; font-size: 14px">{{ errorMessage }}</p>
        </div>
      </div>
    </div>
  </el-dialog>
</template>

<script setup lang="ts">
  import { isValidUrl } from '@/utils/ruoyi';
  import ImagePreview from '@/components/ImagePreview/index.vue';

  // Aliplayer 类型声明（外部脚本加载）
  declare const Aliplayer: any;

  // 使用说明：
  // 1. 需要在 HTML 中引入以下资源：
  //    <link rel="stylesheet" href="https://g.alicdn.com/de/prismplayer/2.9.0/skins/default/aliplayer-min.css" />
  //    引入脚本: https://g.alicdn.com/de/prismplayer/2.9.0/aliplayer-min.js
  // 本地引入： 阿里云播放器文件在 public 目录下，但是存在样式图片路径问题
  // <link rel="stylesheet" href="/aliplayer-min-v2.9.0.css" />
  //  /aliplayer-min-v2.9.0.js
  // 2. 文件预览组件
  //    import FileViewer from '@/components/FileViewer';
  //    <FileViewer ref="FileViewerRef" :fileData="fileData" v-if="showViewer" />
  // 3. 使用示例：
  //    const FileViewerRef = ref()
  //    const showViewer = ref(true)
  //    const fileData = ref({})
  //    const handleFileViewer = (row) => {
  //      showViewer.value = true
  //      fileData.value = { ...row, name: row.trnFileName, path: row.trnFileUrl }
  //      nextTick(() => {
  //        FileViewerRef.value?.show()
  //      })
  //    }

  interface FileData {
    name?: string;
    path?: string;
    cover?: string;
    type?: 'pdf' | 'image' | 'video' | 'other';
  }

  interface RecordData {
    viewDuration?: number;
    ramdom?: number;
  }

  interface Props {
    width?: string | number;
    fileData?: FileData;
    recordData?: RecordData;
    toolbar?: boolean;
    view?: string | number;
  }

  // Props 定义
  const props = withDefaults(defineProps<Props>(), {
    width: '600',
    fileData: () => ({}) as FileData,
    recordData: () => ({}) as RecordData,
    toolbar: true,
    view: '',
  });

  // Emits 定义
  const emit = defineEmits<{
    close: [];
    saveRecord: [];
  }>();

  // 环境变量
  const baseUrl = import.meta.env.VITE_APP_BASE_API || '';

  // 响应式状态
  const loading = ref(false);
  const visible = ref(false);
  const path = ref('');
  const player = ref<any>(null);
  const isImg = ref(false);
  const isVideo = ref(false);
  const videoUrl = ref('');
  const videoCover = ref('');
  const title = ref('');
  const oldTime = ref(0); // 记录播放时间
  const errorMessage = ref(''); // 错误提示信息

  // 获取文件后缀名
  const getFileExtension = (filename: string): string => {
    if (!filename) return '';
    let suffix = '';
    const name = filename.toLowerCase();
    const i = name.lastIndexOf('.');
    if (i > -1) {
      suffix = name.substring(i);
    }
    return suffix;
  };

  // 转换为URL
  const convertToUrl = (pathStr: string): string => {
    if (!pathStr) return '';
    // 如果已经是完整URL，直接返回
    if (isValidUrl(pathStr)) {
      return pathStr;
    }
    // 如果是相对路径，拼接baseUrl
    return baseUrl + pathStr;
  };

  // 视图类型
  type ViewerType = 'video' | 'image' | 'pdf' | 'other';

  // 根据类型或后缀获取视图类型
  const getViewerType = (type: string | undefined, path: string): ViewerType => {
    // 优先使用传入的类型
    if (type === 'video' || type === 'image' || type === 'pdf' || type === 'other') {
      return type;
    }
    // 根据后缀判断
    const suffix = getFileExtension(path).toLowerCase();
    if (suffix === '.mp4') return 'video';
    if (['.png', '.jpg', '.jpeg', '.gif'].includes(suffix)) return 'image';
    if (suffix === '.pdf') return 'pdf';
    return 'other';
  };

  // 渲染视频预览
  const renderVideo = (pathStr: string) => {
    isVideo.value = true;
    videoUrl.value = convertToUrl(pathStr);
    videoCover.value = convertToUrl(props.fileData?.cover || '');
    visible.value = true;
    nextTick(() => {
      loadPlayer();
    });
  };

  // 渲染图片预览
  const renderImage = (pathStr: string) => {
    isImg.value = true;
    path.value = convertToUrl(pathStr);
    visible.value = true;
  };

  // 渲染 PDF 预览
  const renderPdf = (pathStr: string) => {
    let url = convertToUrl(pathStr);
    if (!props.toolbar) {
      url = url + '#toolbar=0';
    }
    path.value = url;
    visible.value = true;
  };

  // 其他类型 - 新窗口打开
  const renderOther = (pathStr: string) => {
    window.open(convertToUrl(pathStr));
    close();
  };

  // 显示弹框
  const show = () => {
    // 重置状态
    isImg.value = false;
    isVideo.value = false;
    title.value = props.fileData?.name || '';
    errorMessage.value = '';

    const pathStr = props.fileData?.path || '';
    if (!pathStr) {
      errorMessage.value = '文件路径不能为空';
      return;
    }

    // 获取视图类型并渲染
    const viewerType = getViewerType(props.fileData?.type, pathStr);

    switch (viewerType) {
      case 'video':
        renderVideo(pathStr);
        break;
      case 'image':
        renderImage(pathStr);
        break;
      case 'pdf':
        renderPdf(pathStr);
        break;
      case 'other':
      default:
        renderOther(pathStr);
    }
  };

  // 关闭弹框
  const close = () => {
    visible.value = false;
    errorMessage.value = ''; // 清除错误信息
    emit('close');
    if (player.value) {
      player.value.pause(); // 关闭弹窗时，将视频暂停
    }
  };

  // 视频资源 加载播放器
  const loadPlayer = () => {
    // 检查 Aliplayer 是否已加载
    if (typeof Aliplayer === 'undefined') {
      console.error('Aliplayer is not loaded. Please include the script: https://g.alicdn.com/de/prismplayer/2.9.0/aliplayer-min.js');
      errorMessage.value = '视频播放器未加载，请刷新页面重试';
      return;
    }
    if (player.value) {
      player.value.dispose();
    }
    const width = parseFloat(String(props.width));
    player.value = new Aliplayer(
      {
        id: 'player',
        source: videoUrl.value,
        height: width / 1.5 + 'px',
        cover: videoCover.value || '',
        /* 设置封面时需要将 autoplay 和 preload 设置为 false */
        autoplay: false, // 自动播放
        preload: false, // 自动加载
        rePlay: false, // 循环播放
        language: 'zh-cn',
        playsinline: true,
        isLive: false,
        controlBarVisibility: 'always', // hover click always
      },
      (player: any) => {
        console.log('The player is created', player);
      },
    );
    loading.value = false;
  };

  // 记录资源打开记录，包括打开次数和完成情况，如果是文档或图片，打开即完成
  const saveRecord = () => {
    props.recordData.viewDuration = 60;
    emit('saveRecord');
  };

  // 播放器完成播放后，等待三秒后执行
  const finishRecord = () => {
    if (props.recordData) {
      props.recordData.ramdom = Math.random();
      props.recordData.viewDuration = 60;
    }
    saveRecord();
  };

  // 记录播放时间
  const recordTime = (event: Event) => {
    const target = event.target as HTMLVideoElement;
    const time = target.currentTime;
    // 四舍五入后取余，每隔60秒保存到后台
    const newTime = Math.round(time);
    if (oldTime.value !== newTime && Math.round(time) !== 0 && Math.round(time) % 60 === 0) {
      if (props.recordData) {
        props.recordData.ramdom = Math.random();
      }
      saveRecord();
      oldTime.value = newTime;
    }
  };

  // 视频错误回调
  const videoErrorCallback = (e: any) => {
    console.log(e);
    if (e?.target?.errMsg) {
      errorMessage.value = e.target.errMsg;
    } else {
      errorMessage.value = '视频加载失败，请稍后重试';
    }
  };

  // 暴露方法给父组件
  defineExpose({
    show,
    close,
  });
</script>

<style scoped lang="scss">
  .video_content {
    width: 100%;
    min-height: 50vh;
    position: relative;
    display: flex;
    justify-content: center;
    align-items: center;
  }

  .error-placeholder {
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;
    min-height: 50vh;
    padding: 40px 20px;
  }

  .preview_iframe {
    width: 100%;
    height: 85vh;
    margin: auto;
    background: #fff;
  }

  .img-wrapper {
    width: 100%;
    min-height: 450px;
    max-height: 85vh;
  }

  :deep(.el-dialog.viewer-dialog) {
    margin-top: 1vh !important;
  }
</style>

<style>
  .video_content .prism-player .prism-big-play-btn {
    width: 90px;
    height: 90px;
    left: 50% !important;
    top: 50% !important;
    transform: translate(-50%, -50%) !important;
  }

  .video_content .prism-player .prism-big-play-btn .outter {
    width: 90px;
    height: 90px;
  }
</style>
