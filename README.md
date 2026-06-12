# 东软环保公众监督系统 - 后端服务

## 项目简介
本项目是东软环保公众监督系统的后端服务，基于 Spring Boot + MyBatis-Plus + MySQL 构建，为四个端提供 RESTful API 支持：
- **NEPS** - 公众监督员端
- **NEPG** - AQI检测网格员端
- **NEPM** - 系统管理端
- **NEPV** - 可视化大屏端

## 技术栈
- **框架**: Spring Boot 2.7.18
- **ORM**: MyBatis-Plus 3.5.3.1
- **安全**: Spring Security + JWT
- **数据库**: MySQL 8.0
- **构建**: Maven

## 快速开始

### 1. 数据库初始化
```bash
mysql -u root -p --default-character-set=utf8mb4 environment < backend/src/main/resources/db/init.sql
```
或直接执行 `init.sql` 文件，它会自动创建 `environment` 数据库和所有表。

### 2. 配置数据库连接
编辑 `src/main/resources/application.yml`，修改数据库用户名和密码：
```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/environment?...
    username: root
    password: your_password
```

### 3. 启动项目
```bash
mvn spring-boot:run
```
或使用 IDEA 直接运行 `EnvironmentApplication.java`。

### 4. 测试账号
| 角色 | 登录标识 | 密码 |
|------|----------|------|
| 公众监督员 | 手机号: 13800000001 | 123456 |
| 网格员 | 编码: GRID001 | 123456 |
| 系统管理员 | 编码: ADMIN001 | 123456 |
| 决策者 | 编码: DECIS001 | 123456 |

## 项目结构
```
src/main/java/com/neusoft/environment/
├── EnvironmentApplication.java    # 启动类
├── config/                        # 配置类
│   ├── SecurityConfig.java        # Spring Security + JWT
│   ├── JwtAuthenticationFilter.java
│   ├── MyBatisPlusConfig.java
│   ├── CorsConfig.java            # 跨域配置
│   └── GlobalExceptionHandler.java
├── controller/                    # 控制器层
│   ├── AuthController.java        # 注册/登录
│   ├── SupervisorController.java  # 公众监督员
│   ├── GridManController.java     # 网格员
│   ├── AdminController.java       # 管理员
│   └── StatisticsController.java  # 决策者大屏
├── service/                       # 服务接口
├── mapper/                        # 数据访问层
├── entity/                        # 实体类（8张表）
├── dto/                           # 请求DTO
├── vo/                            # 响应VO
└── utils/                         # 工具类
    ├── JwtUtils.java              # JWT工具
    ├── PasswordUtils.java         # 密码加密
    └── AqiCalculator.java         # AQI计算
```

## API 接口概览

### 公开接口
| 方法 | 路径 | 说明 |
|------|------|------|
| POST | /api/auth/register | 公众监督员注册 |
| POST | /api/auth/login | 统一登录 |

### NEPS 公众监督员端
| 方法 | 路径 | 说明 |
|------|------|------|
| GET | /api/supervisor/regions | 获取网格区域列表 |
| POST | /api/supervisor/feedback | 提交监督反馈 |
| GET | /api/supervisor/feedback/my | 我的历史反馈 |
| GET | /api/supervisor/feedback/{id} | 反馈详情 |

### NEPG 网格员端
| 方法 | 路径 | 说明 |
|------|------|------|
| GET | /api/gridman/tasks | 我的任务列表 |
| GET | /api/gridman/tasks/{id} | 任务详情 |
| POST | /api/gridman/aqi/submit | 提交实测AQI |

### NEPM 管理员端
| 方法 | 路径 | 说明 |
|------|------|------|
| GET | /api/admin/feedback/list | 反馈数据列表 |
| GET | /api/admin/feedback/{id} | 反馈详情 |
| POST | /api/admin/feedback/assign | 指派网格员 |
| GET | /api/admin/gridman/available | 可用网格员 |
| GET | /api/admin/aqi/list | AQI数据列表 |
| GET | /api/admin/statistics/province | 省分组统计 |
| GET | /api/admin/statistics/level-distribution | AQI分布统计 |
| GET | /api/admin/statistics/trend | AQI趋势统计 |
| GET | /api/admin/statistics/realtime | 实时统计 |
| GET | /api/admin/statistics/coverage | 覆盖率统计 |

### NEPV 决策者大屏
| 方法 | 路径 | 说明 |
|------|------|------|
| GET | /api/statistics/overview | 大屏综合概览 |

## 核心业务逻辑

### AQI 计算
```
finalAQI = MAX(SO2AQI, COAQI, PM2.5AQI)
超标判定: AQI > 100 (轻度污染及以上)
```

### 网格员指派策略
1. **本地优先**: 同省市空闲网格员
2. **同省异地**: 同省其他市空闲网格员
3. **全国范围**: 全国空闲网格员
