<template>
  <div class="my-weditor" :style="{ zIndex: props.zIndex }">
    <Toolbar id="editor-toolbar" class="toolbar-wrapper" :editor="editor" :defaultConfig="toolbarConfig" :mode="mode" />
    <Editor id="content" class="custom-editor" v-model="html" :defaultConfig="editorConfig" :mode="mode" @onCreated="onCreated" @onChange="onChange" :style="{ height: props.minHeight }" />
    <!-- <div id="content" class="custom-editor" :style="{ minHeight: props.minHeight }"></div> -->
  </div>
</template>

<script setup lang="ts" name="WangEditor">
  /**
   * WangEditor 富文本编辑器组件
   *
   * @example 基础用法 v-model 双向绑定 + 内容变化事件
   * <wang-editor v-model="content" @change="handleChange" />
   *
   * @example 获取编辑器实例 通过 @created 获取编辑器实例
   * <wang-editor @created="handleEditorInit" />
   *
   * @example 自定义上传地址 通过 uploadImageUrl 配置图片上传地址，通过 uploadVideoUrl 配置视频上传地址
   * <wang-editor :upload-image-url="uploadImageUrl" :upload-video-url="uploadVideoUrl" />
   */
  import { Editor, Toolbar } from '@wangeditor/editor-for-vue';
  import '@wangeditor/editor/dist/css/style.css';

  // 接口定义
  interface Props {
    /** v-model 绑定值 */
    modelValue?: string;
    weditorId?: string;
    minHeight?: string;
    menusConfig?: string[];
    zIndex?: number;
    uploadImageUrl?: string;
    uploadVideoUrl?: string;
  }

  interface EditorInstance {
    getHtml: () => string;
    setHtml: (html: string) => void;
    destroy: () => void;
    getConfig: () => { toolbarKeys: string[] };
  }

  // Props 定义
  const props = withDefaults(defineProps<Props>(), {
    modelValue: '',
    weditorId: 'editor',
    minHeight: 100,
    menusConfig: () => [
      'headerSelect',
      'bold',
      'fontSize',
      'fontFamily',
      'italic',
      'underline',
      'through',
      'group-indent',
      'lineHeight',
      'color',
      'bgColor',
      'insertLink',
      'bulletedList',
      'numberedList',
      'todo',
      'group-justify',
      'blockquote',
      'sup',
      'sub',
      'clearStyle',
      'emotion',
      'group-image',
      'insertTable',
      'group-video',
      'code',
      'codeBlock',
      'divider',
      'undo',
      'redo',
      'fullScreen',
    ],
    zIndex: 100,
    uploadImageUrl: '',
    uploadVideoUrl: '',
  });

  // Emits 定义
  const emit = defineEmits<{
    (e: 'update:modelValue', value: string): void;
    (e: 'change', html: string): void;
    (e: 'created', editor: EditorInstance): void;
  }>();

  const baseUrl = import.meta.env.VITE_APP_BASE_API;
  // 响应式状态
  const editor = ref<EditorInstance | null>(null);
  const html = ref<string>('');
  const mode = ref<'default' | 'simple'>('default');

  // 所有可用的菜单项
  const allMenus = [
    { value: 'headerSelect', label: '标题' },
    { value: 'bold', label: '粗体' },
    { value: 'fontSize', label: '字号' },
    { value: 'fontFamily', label: '字体' },
    { value: 'italic', label: '斜体' },
    { value: 'underline', label: '下划线' },
    { value: 'through', label: '删除线' },
    { value: 'group-indent', label: '缩进', menuKeys: ['indent', 'delIndent'] },
    { value: 'lineHeight', label: '行高' },
    { value: 'color', label: '文字颜色' },
    { value: 'bgColor', label: '背景颜色' },
    { value: 'insertLink', label: '插入链接' },
    { value: 'bulletedList', label: '无序列表' },
    { value: 'numberedList', label: '有序列表' },
    { value: 'todo', label: '待办' },
    { value: 'group-justify', label: '对齐', menuKeys: ['justifyLeft', 'justifyRight', 'justifyCenter', 'justifyJustify'] },
    { value: 'blockquote', label: '引用' },
    { value: 'sup', label: '上标' },
    { value: 'sub', label: '下标' },
    { value: 'clearStyle', label: '清除格式' },
    { value: 'emotion', label: '表情' },
    { value: 'group-image', label: '图片', menuKeys: ['insertImage', 'uploadImage'] },
    { value: 'insertTable', label: '表格' },
    { value: 'group-video', label: '视频', menuKeys: ['insertVideo', 'uploadVideo'] },
    { value: 'code', label: '行内代码' },
    { value: 'codeBlock', label: '代码块' },
    { value: 'divider', label: '分割线' },
    { value: 'undo', label: '撤销' },
    { value: 'redo', label: '重做' },
    { value: 'fullScreen', label: '全屏' },
  ];

  // 工具栏配置 - 根据 menusConfig 动态计算排除的菜单
  const toolbarConfig = computed(() => {
    const selectedMenus = props.menusConfig || allMenus.map((menu) => menu.value);
    // 计算未被选中的菜单，加入排除列表
    const excludeKeys = allMenus.filter((menu) => !selectedMenus.includes(menu.value)).map((menu) => menu.value);
    // 额外排除一些不需要的菜单
    const extraExclude = ['codeBlock', 'fullScreen', 'group-video'];

    return {
      insertKeys: {
        index: 0,
        keys: [] as string[],
      },
      // toolbarKeys: [],
      excludeKeys: [...excludeKeys],
      modalAppendToBody: false,
    };
  });

  // 编辑器配置 - 使用 computed 响应 props 变化
  const editorConfig = computed(() => ({
    placeholder: '请输入内容...',
    MENU_CONF: {
      uploadImage: {
        server: `${baseUrl}${props.uploadImageUrl}`,
        timeout: 5 * 1000,
        fieldName: 'file',
        metaWithUrl: true,
        maxFileSize: 10 * 1024 * 1024,
        base64LimitSize: 5 * 1024,
        onBeforeUpload(file: File) {
          console.log('onBeforeUpload', file);
          return file;
        },
        onProgress(progress: number) {
          console.log('onProgress', progress);
        },
        onSuccess(file: File, res: any) {
          console.log('onSuccess', file, res);
        },
        onFailed(file: File, res: any) {
          console.log('onFailed', file, res);
        },
        onError(file: File, err: Error, res: any) {
          alert(err.message);
          console.error('onError', file, err, res);
        },
      },
      uploadVideo: {
        server: `${baseUrl}${props.uploadVideoUrl}`,
        timeout: 5 * 1000,
        fieldName: 'file',
        metaWithUrl: true,
        maxFileSize: 1000 * 1024 * 1024,
        onBeforeUpload(file: File) {
          console.log('onBeforeUpload', file);
          return file;
        },
        onProgress(progress: number) {
          console.log('onProgress', progress);
        },
        onSuccess(file: File, res: any) {
          console.log('onSuccess', file, res);
        },
        onFailed(file: File, res: any) {
          alert(res.message);
          console.log('onFailed', file, res);
        },
        onError(file: File, err: Error, res: any) {
          alert(err.message);
          console.error('onError', file, err, res);
        },
      },
    },
  }));

  // Editor 创建回调
  function onCreated(createdEditor: EditorInstance) {
    editor.value = Object.seal(createdEditor);
    console.log(editor.value, editor.value?.getConfig().toolbarKeys);
    console.log(editor.value?.getAllMenuKeys());
    editor.value.setHtml(props.modelValue);
    emit('created', createdEditor);
  }

  // 内容变化回调
  function onChange(_editor: EditorInstance) {
    const html = editor.value?.getHtml() || '';
    emit('update:modelValue', html);
    emit('change', html);
  }

  // 监听 modelValue 变化（v-model 绑定）
  watch(
    () => props.modelValue,
    (newVal, oldVal) => {
      if (editor.value && newVal !== oldVal) {
        editor.value.setHtml(newVal);
      }
    },
  );

  // 生命周期 - 组件销毁前
  onBeforeUnmount(() => {
    if (editor.value) {
      editor.value.destroy();
      editor.value = null;
    }
  });
</script>

<style scoped>
  .my-weditor {
    line-height: initial;
  }
  .toolbar-wrapper {
    width: 100%;
    border: 1px solid #e8e8e8;
    position: relative;
  }
  .custom-menu {
    position: absolute;
    display: flex;
    list-style: none;
    top: 50%;
    left: 16px;
    transform: translateY(-50%);
  }
  .custom-menu li {
    margin-left: 8px;
  }
  .custom-editor {
    width: 100%;
    overflow: auto;
    border: 1px solid #e8e8e8;
    border-top: none;
  }
  #editor-toolbar {
    /* width: 1350px; */
    background-color: #fcfcfc;
    margin: 0 auto;
    /* margin-left: 135px;
  border-left: 1px solid #e8e8e8; */
  }
  #content {
    min-height: v-bind('props.minHeight + "px"');
    height: auto;
    background-color: rgb(245, 245, 245);
    overflow-y: auto;
    position: relative;
  }
  #editor-container {
    width: 1024px;
    min-height: 500px;
    margin: 12px auto 24px auto;
    background-color: #fff;
    padding: 16px 32px;
    border: 1px solid #e8e8e8;
    box-shadow: 0 2px 10px rgba(0, 0, 0, 0.12);
  }
  #editor-text-area {
    min-height: 558px;
    margin-top: 20px;
  }
</style>

<style>
  .w-e-text-container [data-slate-editor] img.noAnswerPic {
    display: none !important;
  }
  .my-weditor .w-e-select-list {
    margin-top: 40px;
  }
  .my-weditor .w-e-modal {
    padding: 20px 15px 0;
  }
</style>
