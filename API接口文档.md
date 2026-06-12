# 东软环保公众监督系统 - API 接口文档

## 基础信息

- **Base URL**: `http://localhost:8080`
- **认证方式**: 登录后返回 JWT Token，后续请求在 Header 中携带 `Authorization: Bearer <token>`
- **响应格式**: 统一 JSON
  ```json
  { "code": 200, "msg": "操作成功", "data": {...} }
  ```
- **分页格式**: `GET /api/xxx?page=1&size=10`，返回 `{ "records": [...], "total": 100, "current": 1, "size": 10 }`

---

## 测试账号

| 角色 | role | 登录方式 | 标识 | 密码 |
|------|------|----------|------|------|
| 公众监督员 | 0 | 手机号 | 13800000001 | 123456 |
| AQI网格员 | 1 | 编码 | GRID001 | 123456 |
| 系统管理员 | 2 | 编码 | ADMIN001 | 123456 |
| 决策者 | 3 | 编码 | DECIS001 | 123456 |

---

## 一、公开接口（无需Token）

### 1.1 注册（仅限公众监督员）
```
POST /api/auth/register
Content-Type: application/json

{
  "phone": "13900000001",
  "password": "123456",
  "realName": "王小明",
  "age": 25,
  "gender": 1
}
```
响应：`{ "code": 200, "msg": "注册成功" }`

### 1.2 登录
```
POST /api/auth/login
Content-Type: application/json

// 监督员用手机号登录
{ "role": 0, "phone": "13800000001", "password": "123456" }

// 网格员/管理员/决策者用登录编码
{ "role": 2, "loginCode": "ADMIN001", "password": "123456" }
```
响应：
```json
{
  "code": 200,
  "msg": "登录成功",
  "data": {
    "token": "eyJhbGciOiJIUzUxMiJ9...",
    "userId": 6,
    "realName": "管理员",
    "role": 2
  }
}
```

---

## 二、NEPS 公众监督员端

### 2.1 获取网格区域树
```
GET /api/supervisor/regions
```
响应：
```json
[{
  "province": "辽宁",
  "cities": [
    { "city": "沈阳", "isCapital": 1, "cityType": "特大城市" },
    { "city": "大连", "isCapital": 0, "cityType": "特大城市" }
  ]
}]
```

### 2.2 提交监督反馈
```
POST /api/supervisor/feedback
Authorization: Bearer <token>

{
  "province": "辽宁",
  "city": "沈阳",
  "addressDetail": "铁西区建设西路100号",
  "estimatedAqiLevel": 3,
  "description": "今天下午天空灰蒙蒙，能闻到明显的煤烟味"
}
```

### 2.3 我的历史反馈列表
```
GET /api/supervisor/feedback/my?page=1&size=10
Authorization: Bearer <token>
```

### 2.4 反馈详情
```
GET /api/supervisor/feedback/{id}
Authorization: Bearer <token>
```

---

## 三、NEPG 网格员端

### 3.1 我的任务列表
```
GET /api/gridman/tasks
Authorization: Bearer <token>
```

### 3.2 任务详情
```
GET /api/gridman/tasks/{feedbackId}
Authorization: Bearer <token>
```

### 3.3 提交实测AQI数据
```
POST /api/gridman/aqi/submit
Authorization: Bearer <token>

{
  "feedbackId": 1,
  "so2Aqi": 45.00,
  "coAqi": 32.00,
  "pm25Aqi": 128.50
}
```
说明：提交后系统自动计算 `finalAqi = MAX(so2Aqi, coAqi, pm25Aqi)`，`>100` 判定超标。任务状态自动变为"已完成"。

---

## 四、NEPM 管理员端

### 4.1 反馈数据列表
```
GET /api/admin/feedback/list?page=1&size=10&province=辽宁&city=沈阳&status=0
Authorization: Bearer <token>
```
参数：`province` `city` `status` 均可选（status: 0待指派 1已指派 2已完成）

### 4.2 反馈详情
```
GET /api/admin/feedback/{id}
Authorization: Bearer <token>
```

### 4.3 指派网格员
```
POST /api/admin/feedback/assign
Authorization: Bearer <token>

{ "feedbackId": 1, "gridManId": 3 }
```
说明：系统会自动判定是本地指派还是异地指派。指派后网格员状态变为忙碌。

### 4.4 可用网格员列表
```
GET /api/admin/gridman/available?province=辽宁&city=沈阳
Authorization: Bearer <token>
```

### 4.5 推荐最佳网格员
```
GET /api/admin/gridman/recommend/{feedbackId}
Authorization: Bearer <token>
```
说明：自动按【本地 → 同省 → 全国】策略推荐空闲网格员。

### 4.6 AQI数据列表
```
GET /api/admin/aqi/list?page=1&size=10
Authorization: Bearer <token>
```

### 4.7 AQI数据详情
```
GET /api/admin/aqi/{id}
Authorization: Bearer <token>
```

### 4.8 省分组超标统计
```
GET /api/admin/statistics/province
Authorization: Bearer <token>
```

### 4.9 AQI指数分布统计
```
GET /api/admin/statistics/level-distribution
Authorization: Bearer <token>
```

### 4.10 AQI趋势统计（近12个月）
```
GET /api/admin/statistics/trend
Authorization: Bearer <token>
```

### 4.11 实时统计（总检测数/良好/超标）
```
GET /api/admin/statistics/realtime
Authorization: Bearer <token>
```

### 4.12 网格覆盖率统计
```
GET /api/admin/statistics/coverage
Authorization: Bearer <token>
```

---

## 五、NEPV 决策者大屏

### 5.1 综合数据概览
```
GET /api/statistics/overview
Authorization: Bearer <token>
```
返回所有统计数据汇总（省分组/AQI分布/趋势/实时/覆盖率）。

---

## 六、系统公告

### 6.1 已发布公告列表（公开）
```
GET /api/announcement/list?page=1&size=10
```

### 6.2 全部公告管理（管理员）
```
GET /api/announcement/admin/list?page=1&size=10
POST /api/announcement/admin/create              { "title":"标题", "content":"内容" }
PUT  /api/announcement/admin/publish/{id}
PUT  /api/announcement/admin/revoke/{id}
```

---

## 七、公众投诉

### 7.1 提交投诉
```
POST /api/complaint/submit
Authorization: Bearer <token>

{ "title":"投诉标题", "complaintType":1, "province":"辽宁", "city":"沈阳",
  "addressDetail":"详细地址", "description":"投诉描述" }
```
complaintType: 0水体污染 1大气污染 2噪音污染 3固废污染 4其他

### 7.2 我的投诉列表
```
GET /api/complaint/my?page=1&size=10
Authorization: Bearer <token>
```

### 7.3 待处理投诉（管理员）
```
GET /api/complaint/admin/pending?page=1&size=10
PUT /api/complaint/admin/handle/{id}?handlerId=6&result=处理结果&status=2
```
status: 1处理中 2已处理 3已驳回

---

## 八、环保政策

### 8.1 已发布政策列表（公开）
```
GET /api/policy/list?page=1&size=10&policyType=0
```
policyType(可选): 0国家法规 1地方条例 2行业标准 3环保科普

### 8.2 政策详情（浏览量+1）
```
GET /api/policy/detail/{id}
```

---

## 九、消息通知

### 9.1 未读数量
```
GET /api/notification/unread-count
Authorization: Bearer <token>
```

### 9.2 未读列表 / 全部列表
```
GET /api/notification/unread?page=1&size=10
GET /api/notification/all?page=1&size=10
```

### 9.3 标记已读
```
PUT /api/notification/read/{id}
PUT /api/notification/read-all
```

---

## 十、系统字典

### 10.1 根据编码获取字典
```
GET /api/dict/complaint_type          → 投诉类型
GET /api/dict/policy_type             → 政策类型
GET /api/dict/notification_type       → 通知类型
GET /api/dict/complaint_status        → 投诉状态
```

---

## 十一、操作日志（管理员）

```
GET /api/log/list?page=1&size=10&module=&operationType=
```

---

## 十二、网络巡检记录

### 12.1 提交巡检记录（网格员）
```
POST /api/inspection/submit
Authorization: Bearer <token>

{ "province":"辽宁", "city":"沈阳", "addressDetail":"xxx", "inspectionDate":"2025-06-12",
  "weather":"晴", "so2Value":35.00, "coValue":12.00, "pm25Value":68.00, "aqiLevel":2,
  "description":"正常", "remark":"无异常" }
```

### 12.2 我的巡检记录
```
GET /api/inspection/my?page=1&size=10
Authorization: Bearer <token>
```
