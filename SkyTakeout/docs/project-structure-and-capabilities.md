# 项目结构与能力总览

最后更新：2026-05-24，完成 Day 1 后补充

## 1. 文档用途

这个文档用于快速了解 SkyTakeout 当前项目结构、已经具备的能力、关键接口、运行方式和未完成内容。

后续每天结束时，必须和当天的 `day-XX.md` 一起同步更新本文件，避免只记录当天细节而看不到项目整体变化。

## 2. 项目根目录

```text
F:\Java_Learning\SkyTakeout\sky-take-out-ai
```

当前包含三个子项目：

```text
sky-take-out-ai
├─ sky-server        Spring Boot 后端
├─ sky-admin-web     Vue 3 管理端
└─ sky-user-miniapp  微信小程序用户端骨架
```

配套文档目录：

```text
F:\Java_Learning\SkyTakeout\docs
├─ day-00.md
├─ day-01.md
└─ project-structure-and-capabilities.md
```

## 3. 后端项目结构

后端目录：

```text
sky-server
├─ mvnw.cmd
├─ pom.xml
└─ src
   ├─ main
   │  ├─ java/com/sky
   │  │  ├─ SkyServerApplication.java
   │  │  ├─ common
   │  │  ├─ config
   │  │  ├─ employee
   │  │  ├─ product
   │  │  ├─ cart
   │  │  ├─ address
   │  │  ├─ order
   │  │  └─ store
   │  └─ resources
   │     ├─ application.yml
   │     ├─ application-mysql.yml
   │     ├─ application-test.yml
   │     └─ db
   │        ├─ schema.sql
   │        └─ data.sql
   └─ test/java/com/sky
      ├─ address
      ├─ cart
      ├─ common
      ├─ database
      ├─ docs
      └─ order
```

### 后端包职责

| 包名 | 当前职责 |
| --- | --- |
| `com.sky.common` | 统一返回 `Result`、分页 `PageResult`、业务异常、全局异常处理、JWT、当前用户上下文 |
| `com.sky.config` | Web MVC 配置、鉴权拦截器、JWT Bean、配置属性 |
| `com.sky.employee` | 员工接口、员工登录命令对象、员工返回对象、员工服务 |
| `com.sky.product` | 分类、菜品、套餐的领域对象和内存服务 |
| `com.sky.cart` | 购物车领域对象和服务 |
| `com.sky.address` | 地址簿领域对象和服务 |
| `com.sky.order` | 订单状态机 |
| `com.sky.store` | 当前阶段使用的内存数据仓库 |

### 后端配置文件

| 文件 | 作用 |
| --- | --- |
| `application.yml` | 默认配置，当前默认连接真实 MySQL：`localhost:3306/sky_take_out` |
| `application-mysql.yml` | MySQL profile 配置，保留给显式 profile 使用 |
| `application-test.yml` | H2 内存数据库配置，用于无 MySQL 环境或快速测试 |
| `db/schema.sql` | 创建基础 11 张业务表和索引 |
| `db/data.sql` | 导入管理员、分类、菜品、套餐等演示基础数据 |

## 4. 管理端项目结构

管理端目录：

```text
sky-admin-web
├─ index.html
├─ package.json
├─ package-lock.json
├─ vite.config.js
└─ src
   ├─ App.vue
   ├─ main.js
   └─ styles.css
```

当前能力：

- Vue 3 + Vite 项目可以启动。
- Element Plus 已作为管理端 UI 基础依赖。
- 当前只有基础占位页面，Day 2 才开始实现登录页、布局、员工管理、分类管理。

## 5. 用户端项目结构

用户端目录：

```text
sky-user-miniapp
├─ app.js
├─ app.json
├─ app.wxss
├─ project.config.json
└─ pages/index
   ├─ index.js
   ├─ index.wxml
   └─ index.wxss
```

当前能力：

- 微信小程序目录骨架已创建。
- 当前只有首页占位页面。
- Day 4 开始实现用户登录、商品浏览、购物车、地址簿。

## 6. 当前已完成能力

截至 Day 1，已完成：

- 项目目录和三个子项目骨架已创建。
- 后端 Spring Boot 可以启动。
- 默认数据库已切换为 MySQL。
- MySQL 数据库 `sky_take_out` 可连接。
- 11 张基础表已由 `schema.sql` 定义。
- 基础演示数据已由 `data.sql` 导入。
- Knife4j 文档可通过 `http://localhost:8080/doc.html` 访问。
- `/admin/employee/login` 可在 OpenAPI 文档中看到。
- 统一接口返回格式已存在。
- 全局异常处理已存在。
- 分页返回对象已存在。
- JWT 签发服务已存在。
- 管理端可构建并可启动 dev server。

## 7. 当前关键接口

| 接口 | 方法 | 当前作用 | 是否需要 Token |
| --- | --- | --- | --- |
| `/admin/employee/login` | POST | 管理端员工登录，返回 token | 否 |
| `/admin/employee/page` | GET | 员工分页查询 | 是 |
| `/admin/employee` | POST | 新增员工 | 是 |
| `/admin/employee` | PUT | 修改员工 | 是 |
| `/admin/employee/{id}` | GET | 查询员工详情 | 是 |
| `/admin/employee/status/{status}` | POST | 启用或禁用员工 | 是 |
| `/doc.html` | GET | Knife4j 页面 | 否 |
| `/v3/api-docs` | GET | OpenAPI JSON | 否 |

说明：Day 1 重点是后端骨架和文档可访问。员工 CRUD 虽然已有内存实现骨架，但按计划 Day 2 才正式作为管理端登录、员工、分类任务验收。

## 8. 数据库结构

当前基础表：

| 表名 | 作用 |
| --- | --- |
| `employee` | 后台员工、管理员 |
| `category` | 菜品分类、套餐分类 |
| `dish` | 菜品主表 |
| `dish_flavor` | 菜品口味 |
| `setmeal` | 套餐主表 |
| `setmeal_dish` | 套餐和菜品关联 |
| `user` | 用户端用户 |
| `address_book` | 用户地址簿 |
| `shopping_cart` | 购物车 |
| `orders` | 订单主表 |
| `order_detail` | 订单明细 |

## 9. 当前运行方式

### 启动 MySQL

如果 MySQL 未运行，可临时启动：

```powershell
Start-Process -FilePath "C:\Program Files\MySQL\MySQL Server 8.4\bin\mysqld.exe" -ArgumentList '--basedir="C:\Program Files\MySQL\MySQL Server 8.4" --datadir="C:\Users\86180\mysql-sky-takeout-data" --port=3306 --bind-address=127.0.0.1' -WindowStyle Hidden
```

确认 MySQL：

```powershell
mysqladmin --protocol=tcp --host=127.0.0.1 --port=3306 --user=root --password=root ping
```

### 启动后端

```powershell
cd F:\Java_Learning\SkyTakeout\sky-take-out-ai\sky-server
.\mvnw.cmd spring-boot:run
```

访问：

```text
http://localhost:8080/doc.html
```

### 启动管理端

```powershell
cd F:\Java_Learning\SkyTakeout\sky-take-out-ai\sky-admin-web
npm run dev -- --host 127.0.0.1 --port 5173 --strictPort
```

访问：

```text
http://127.0.0.1:5173/
```

## 10. 当前验证命令

后端测试：

```powershell
cd F:\Java_Learning\SkyTakeout\sky-take-out-ai\sky-server
.\mvnw.cmd test
```

H2 test profile 文档测试：

```powershell
.\mvnw.cmd "-Dspring.profiles.active=test" -Dtest=ApiDocumentationIntegrationTest test
```

管理端构建：

```powershell
cd F:\Java_Learning\SkyTakeout\sky-take-out-ai\sky-admin-web
npm run build
```

## 11. 当前未完成能力

仍未完成，后续按 PLAN 继续：

- 员工登录真正接入 MyBatis 和 MySQL。
- 员工管理页面和接口完整联调。
- 分类管理页面和接口完整联调。
- 菜品、口味、套餐、文件上传。
- 用户端登录、浏览商品、购物车、地址簿。
- 下单、支付模拟、订单管理。
- Redis 缓存。
- 定时任务。
- WebSocket 来单和催单提醒。
- 报表、工作台、Excel 导出。
- README、演示脚本、简历描述。

## 12. 每日同步更新规则

以后每天任务完成后，必须同步更新：

- 当天文档：`SkyTakeout/docs/day-XX.md`
- 本总览文档：`SkyTakeout/docs/project-structure-and-capabilities.md`

更新本文件时至少检查：

- 新增或删除了哪些目录和文件。
- 哪些能力从“未完成”变成“已完成”。
- 新增了哪些关键接口。
- 数据库表结构是否变化。
- 启动方式或端口是否变化。
- 手动验收方式是否变化。
