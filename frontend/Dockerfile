# 多阶段构建 - 支持 ARM 和 X86
FROM --platform=$BUILDPLATFORM node:18-alpine AS builder

WORKDIR /app

# 复制package.json并安装依赖
COPY package*.json ./
RUN npm install

# 复制源代码并构建
COPY . .
RUN npm run build

# 运行阶段 - 使用跨平台nginx镜像
FROM nginx:alpine

# 设置时区
ENV TZ=Asia/Shanghai
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone

# 复制构建产物
COPY --from=builder /app/dist /usr/share/nginx/html

# 复制nginx配置
COPY nginx.conf /etc/nginx/conf.d/default.conf

# 暴露端口
EXPOSE 80

CMD ["nginx", "-g", "daemon off;"]
