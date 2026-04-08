import axios from 'axios';

// ECharts图表配置接口
export interface ChartOption {
  title?: {
    text?: string;
    left?: string;
  };
  tooltip?: { trigger?: string };
  xAxis?: {
    type?: string;
    data?: string[];
  };
  yAxis?: {
    type?: string;
  };
  series?: Array<{
    data?: number[];
    type?: string;
    itemStyle?: {
      color?: string;
    };
  }>;
  color?: string[];
}

// 查询类型枚举
export enum QueryType {
  NO_SQL_NEEDED = 'NO_SQL_NEEDED',
  SQL_NEEDED_REPORT = 'SQL_NEEDED_REPORT',
  SQL_NEEDED_CHART = 'SQL_NEEDED_CHART',
  SQL_NEEDED_ONLY_TEXT = 'SQL_NEEDED_ONLY_TEXT',
  MAINTENANCE_CONSULTATION = 'MAINTENANCE_CONSULTATION',
}

// 请求类型枚举
export enum RequestType {
  ZHIPU_DIRECT = 'ZHIPU_DIRECT',
  BACKEND = 'BACKEND',
}

// 对话消息接口
interface ChatMessage {
  sender: 'user' | 'assistant';
  text: string;
  timestamp: number;
}

// 后端请求参数接口
interface BackendRequestParams {
  username: string;
  demand: string;
  messages: ChatMessage[];
  queryType?: QueryType;
  streaming?: boolean;
}

// 响应处理回调接口
interface ResponseHandlers {
  onMessage?: (data: { message: { content: { text: string } }; conversation_id: string }) => void;
  onClose?: () => void;
  onError?: (error: Error) => void;
}

// 后端响应数据接口
interface BackendResponse {
  code: number;
  username: string;
  result: string;
  chartUrl?: string;
  htmlContent?: string;
  queryType: QueryType;
  streaming?: boolean;
  streamingPrompt?: string;
  userDemand?: string;
  isRunDataQuery?: boolean;
  isAnalysisQuery?: boolean;
  conversationContext?: string;
  messages?: Array<{
    sender: 'user' | 'assistant';
    text: string;
    timestamp: number;
  }>;
}

// 统一的请求工具类（仅支持后端接口调用）
class RequestClient {
  private queryType: QueryType = QueryType.SQL_NEEDED_CHART;
  private requestType: RequestType = RequestType.BACKEND;
  private accessToken: string = ''; // 提供默认空字符串

  // 设置查询类型
  setQueryType(type: QueryType): void {
    this.queryType = type;
  }

  // 调用后端接口发送消息并返回响应数据
  async sendMessageToBackendWithResponse(requestParams: BackendRequestParams): Promise<BackendResponse> {
    try {
      // 获取后端API基础URL，支持环境变量配置
      const baseUrl = import.meta.env.VITE_API_BASE_URL || 'https://proj.gzzjxx.cn/mcpai/api';
      const url = `${baseUrl}/data/query`;

      const requestData: BackendRequestParams = {
        ...requestParams,
        streaming: false,
      };

      console.log('发送后端请求:', url, requestData);

      const response = await axios.post(url, requestData, {
        headers: {
          'Content-Type': 'application/json',
        },
        timeout: 180000, // 设置120秒超时
      });

      return response.data as BackendResponse;
    } catch (error) {
      console.error('后端请求失败:', error);
      throw error;
    }
  }

  // 调用后端接口发送消息（默认强制调用）
  async sendMessageToBackend(requestParams: BackendRequestParams, handlers: ResponseHandlers): Promise<void> {
    try {
      // 获取后端API基础URL，支持环境变量配置
      const baseUrl = import.meta.env.VITE_API_BASE_URL || 'https://proj.gzzjxx.cn/mcpai/api';
      const url = `${baseUrl}/data/query`;

      // 统一使用普通axios请求，不再区分流式和非流式
      const requestData: BackendRequestParams = {
        ...requestParams,
        // 强制不使用流式输出，由前端模拟
        streaming: false,
      };

      console.log('发送后端请求:', url, requestData);

      try {
        // 统一使用axios进行请求
        const response = await axios.post(url, requestData, {
          headers: {
            'Content-Type': 'application/json',
          },
          timeout: 180000, // 设置120秒超时
        });

        // 处理后端响应
        const result: BackendResponse = response.data;

        if (result.code === 200) {
          console.log('result.result', result.result);
          // 成功响应
          // 对于非图表请求，模拟流式输出效果
          if (result.queryType !== QueryType.SQL_NEEDED_CHART) {
            // 图表请求直接返回结果
            handlers.onMessage?.({
              message: {
                content: {
                  text: result.result,
                },
              },
              conversation_id: `conv_${Date.now()}`,
            });
          } else {
            // 图表请求直接返回结果
            handlers.onMessage?.({
              message: {
                content: {
                  text: result.result,
                },
              },
              conversation_id: `conv_${Date.now()}`,
            });

            // 如果有图表链接，额外发送一条消息
            if (result.chartUrl) {
              setTimeout(() => {
                handlers.onMessage?.({
                  message: {
                    content: {
                      text: `${result.result}\n\n${result.chartUrl}`,
                    },
                  },
                  conversation_id: `conv_${Date.now()}`,
                });
              }, 10);
            }

            setTimeout(() => handlers.onClose?.(), 100);
          }
        } else {
          // 后端返回错误
          handlers.onError?.(new Error('后端接口返回错误: ' + JSON.stringify(result)));
        }
      } catch (axiosError) {
        // 处理axios错误
        handlers.onError?.(new Error('请求失败: ' + (axiosError instanceof Error ? axiosError.message : String(axiosError))));
      }
    } catch (error) {
      console.error('发送消息到后端接口失败:', error);
      handlers.onError?.(error as Error);
    }
  }

  /**
   * 模拟流式响应
   */
  // private simulateStreamResponse(text: string, handlers: ResponseHandlers): void {
  //   const mockResponse = {
  //     message: {
  //       content: {
  //         text: '',
  //       },
  //     },
  //     conversation_id: `conv_${Date.now()}`,
  //   }

  //   let currentText = ''
  //   let index = 0

  //   // 按句子或段落进行分割，使流式输出更自然
  //   const segments = this.splitTextIntoSegments(text);
  //   let segmentIndex = 0;

  //   const interval = setInterval(() => {
  //     if (segmentIndex < segments.length) {
  //       // 添加下一个片段
  //       currentText += segments[segmentIndex];
  //       segmentIndex++;

  //       handlers.onMessage?.({
  //         ...mockResponse,
  //         message: {
  //           content: {
  //             text: currentText,
  //           },
  //         },
  //       })
  //     } else {
  //       clearInterval(interval)
  //       setTimeout(() => handlers.onClose?.(), 100)
  //     }
  //   }, 50) // 每50毫秒输出一个片段，使效果更流畅
  // }

  /**
   * 将文本分割成段落或句子，用于模拟更自然的流式输出
   */
  // private splitTextIntoSegments(text: string): string[] {
  //   // 按段落分割（两个换行符）
  //   const paragraphs = text.split('\n\n');
  //   const segments: string[] = [];

  //   for (const paragraph of paragraphs) {
  //     if (paragraph.trim() === '') continue;

  //     // 按句子分割（以中文句号、感叹号、问号结尾），避免在 URL 中按英文句点拆分导致链接被插入空格
  //     const sentences = paragraph.split(/(?<=[。！？!?])\s*/);

  //     if (sentences.length > 1) {
  //       // 如果段落中有多个句子，逐句添加
  //       for (const sentence of sentences) {
  //         if (sentence.trim() !== '') {
  //           segments.push(sentence + (sentences.indexOf(sentence) < sentences.length - 1 ? ' ' : ''));
  //         }
  //       }
  //     } else {
  //       // 如果只有一个句子或无法分割，整段添加
  //       segments.push(paragraph + '\n\n');
  //     }
  //   }

  //   // 如果按段落和句子分割后片段太少，按字符分割
  //   if (segments.length < 5) {
  //     segments.length = 0; // 清空
  //     let i = 0;
  //     while (i < text.length) {
  //       // 每次取10-20个字符
  //       const chunkSize = Math.min(Math.floor(Math.random() * 10) + 10, text.length - i);
  //       segments.push(text.substr(i, chunkSize));
  //       i += chunkSize;
  //     }
  //   }

  //   return segments;
  // }

  /**
   * 获取模拟响应数据（用于演示）
   */
  //   private getMockResponse(message: string, queryType: QueryType): string {
  //     if (queryType === QueryType.SQL_NEEDED_CHART) {
  //       // 返回模拟图表数据
  //       return JSON.stringify({
  //         title: { text: '学生体育成绩统计', left: 'center' },
  //         tooltip: { trigger: 'axis' },
  //         xAxis: {
  //           type: 'category',
  //           data: ['张三', '李四', '王五', '赵六', '钱七'],
  //         },
  //         yAxis: { type: 'value' },
  //         series: [
  //           {
  //             data: [85, 78, 92, 88, 76],
  //             type: 'bar',
  //             itemStyle: { color: '#1890ff' },
  //           },
  //         ],
  //       })
  //     } else if (queryType === QueryType.SQL_NEEDED_REPORT) {
  //       return `# 体育成绩分析报告

  // ## 总体情况
  // - 参与人数：35人
  // - 平均分：82.5分
  // - 优秀率：35%

  // ## 详细分析
  // 1. 跑步项目表现突出，平均成绩89分
  // 2. 力量项目需要加强训练
  // 3. 建议增加每周训练频次至3次

  // ## 改进建议
  // - 针对力量项目进行专项训练
  // - 加强团队协作训练`
  //     } else if (queryType === QueryType.SQL_NEEDED_ONLY_TEXT) {
  //       return '查询结果：2024年上半年共有35名学生参与体育测试，平均成绩为82.5分，其中优秀学生12名，良好学生15名，及格学生6名，不及格学生2名。'
  //     } else {
  //       return '这是一个普通的回复，不需要SQL查询。您的问题是关于体育训练的建议，我会根据专业知识为您提供指导。'
  //     }
  //   }

  // 统一的发送消息方法
  // async sendMessage(message: string, username: string, handlers: ResponseHandlers): Promise<void> {
  //   // 默认强制调用后端接口
  //   const requestParams: BackendRequestParams = {
  //     username,
  //     demand: message
  //   };
  //   await this.sendMessageToBackend(requestParams, handlers);
  // }

  // 获取下一步问题建议（仅智谱接口支持）
  async getFollowUpQuestions(_conversationId: string): Promise<string[]> {
    // 简化实现，始终返回默认建议
    return ['这个建议适合每天执行吗？', '有什么需要注意的事项吗？', '如何判断运动效果是否达标？'];
  }

  // 统一错误处理
  handleError(error: Error): string {
    let errorMessage = '操作失败，请稍后重试';

    // 处理不同类型的错误
    if (error.message.includes('Network Error')) {
      errorMessage = '网络连接异常，请检查网络后重试';
    } else if (error.message.includes('401')) {
      errorMessage = '未授权访问，请重新登录';
    } else if (error.message.includes('403')) {
      errorMessage = '没有权限访问该资源';
    } else if (error.message.includes('404')) {
      errorMessage = '请求的资源不存在';
    } else if (error.message.includes('500')) {
      errorMessage = '服务器内部错误，请稍后重试';
    } else if (error.message.includes('timeout')) {
      errorMessage = '请求超时，请稍后重试';
    } else if (error.message.includes('连接失败')) {
      errorMessage = '服务连接失败，请稍后再试';
    } else if (error.message.includes('无法连接到智能助手服务')) {
      errorMessage = '无法连接到智能助手服务，请检查网络或联系管理员';
    } else if (error.message) {
      errorMessage = error.message;
    }

    return errorMessage;
  }
}

// 导出单例实例
export const requestClient = new RequestClient();
