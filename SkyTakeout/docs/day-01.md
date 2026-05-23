# Day 01 后端骨架、数据库与接口文档

日期：2026-05-23

## 1. 今日目标

Day 1 的目标是让后端具备继续开发业务功能的基础能力：

- 后端可以启动。
- 基础数据表结构准备完成。
- 项目启动时可以初始化基础数据。
- Knife4j 接口文档可以访问。
- `/admin/employee/login` 能出现在接口文档中。
- 统一返回、全局异常、分页等公共能力继续保持可用。

本日只处理 Day 1 范围，没有进入 Day 2 的员工 CRUD、分类 CRUD 或前端登录页面。

## 2. 项目位置

项目仍然放在计划要求的目录下：

```text
F:\Java_Learning\SkyTakeout\sky-take-out-ai
```

本日主要修改后端子项目：

```text
F:\Java_Learning\SkyTakeout\sky-take-out-ai\sky-server
```

管理端子项目只做构建和 dev server 启动验证，没有修改业务代码：

```text
F:\Java_Learning\SkyTakeout\sky-take-out-ai\sky-admin-web
```

## 3. 后端包结构说明

当前后端采用“公共能力 + 配置 + 业务模块”的分包方式：

```text
com.sky
├─ common      公共返回、异常、分页、JWT、当前用户上下文
├─ config      Spring MVC、鉴权拦截器、配置属性、JWT Bean 配置
├─ employee    员工相关控制器、命令对象、登录返回对象、服务
├─ product     分类、菜品、套餐相关领域对象和服务
├─ cart        购物车相关对象和服务
├─ address     地址簿相关对象和服务
├─ order       订单状态机
└─ store       当前阶段的内存数据仓库
```

说明：

- `common` 对应 PLAN 中的统一返回、异常、JWT、上下文、分页。
- `config` 对应 PLAN 中的配置和拦截器。
- 业务代码当前按业务域分包，而不是统一堆在一个 `server` 包里。这样初学者更容易按功能找到代码。
- MyBatis Mapper 还没有进入业务实现，Day 1 先准备数据库表结构；Day 2 开始实现员工、分类时再逐步接入 Mapper。

## 4. 数据库表结构

新增文件：

```text
sky-server/src/main/resources/db/schema.sql
```

这个文件定义了 Day 1 要求的 11 张基础表：

- `employee`：员工表，后续用于后台管理员登录、员工管理。
- `category`：分类表，后续用于菜品分类和套餐分类。
- `dish`：菜品主表。
- `dish_flavor`：菜品口味表，一道菜可以有多个口味选项。
- `setmeal`：套餐主表。
- `setmeal_dish`：套餐和菜品的关联表。
- `user`：用户表，后续用于微信用户或 H5 模拟用户。
- `address_book`：用户地址簿表。
- `shopping_cart`：购物车表。
- `orders`：订单主表。
- `order_detail`：订单明细表。

每张业务表都包含通用审计字段：

```text
create_time
update_time
create_user
update_user
```

这些字段用于记录数据是谁创建、谁更新、什么时候创建、什么时候更新。Day 2 做 MyBatis 公共字段自动填充时会继续使用它们。

`schema.sql` 还增加了常用索引，例如：

- 分类类型索引：`idx_category_type`
- 菜品分类索引：`idx_dish_category`
- 套餐分类索引：`idx_setmeal_category`
- 用户地址索引：`idx_address_user`
- 用户购物车索引：`idx_cart_user`
- 用户订单索引：`idx_orders_user`
- 订单状态索引：`idx_orders_status`

## 5. 基础数据

新增文件：

```text
sky-server/src/main/resources/db/data.sql
```

这个文件导入演示基础数据：

- 管理员员工账号：`admin`
- 演示分类数据。
- 演示菜品数据。
- 演示菜品口味数据。
- 演示套餐数据。
- 演示套餐和菜品关联数据。

`insert` 语句都带了 `where not exists` 判断，目的是让初始化脚本可以重复执行，不会因为数据已经存在就报唯一键冲突。

## 6. 数据源配置

修改文件：

```text
sky-server/src/main/resources/application.yml
```

作用：

- 默认数据源使用 H2 内存数据库。
- H2 开启 MySQL 兼容模式，方便在没有本地 MySQL 服务时也能验证 SQL 表结构。
- Spring Boot 启动时自动执行：
  - `classpath:db/schema.sql`
  - `classpath:db/data.sql`

关键配置：

```yaml
spring:
  datasource:
    url: jdbc:h2:mem:sky_take_out;MODE=MySQL;DATABASE_TO_LOWER=TRUE;NON_KEYWORDS=USER,VALUE;DB_CLOSE_DELAY=-1
  sql:
    init:
      schema-locations: classpath:db/schema.sql
      data-locations: classpath:db/data.sql
      mode: always
```

新增文件：

```text
sky-server/src/main/resources/application-mysql.yml
```

作用：

- 预留真实 MySQL 8 连接配置。
- 使用 `mysql` profile 启动时连接 `localhost:3306/sky_take_out`。
- 启动时也会自动执行同一套 `schema.sql` 和 `data.sql`。

当前本机没有检测到 `mysql` 命令行工具，所以今天没有通过 mysql CLI 实际连库。后续如果本机启动 MySQL，并创建 `sky_take_out` 数据库，可使用 MySQL profile 验证真实 MySQL 连接。

## 7. Knife4j 与接口文档

今日验证了两个文档入口：

```text
http://localhost:8080/doc.html
http://localhost:8080/v3/api-docs
```

验证结果：

- `/doc.html` 返回 HTTP 200。
- 页面内容包含 `Knife4j`。
- `/v3/api-docs` 返回 HTTP 200。
- OpenAPI JSON 中包含 `/admin/employee/login`。

这说明 Knife4j 文档页面可访问，员工登录接口也已经被接口文档扫描到。

## 8. JWT 配置调整

新增文件：

```text
sky-server/src/main/java/com/sky/config/JwtConfig.java
```

作用：

- 专门创建 `JwtTokenService` Bean。
- 从 `SkyProperties` 读取：
  - `jwtSecret`
  - `jwtTtlSeconds`

修改文件：

```text
sky-server/src/main/java/com/sky/config/WebConfig.java
```

调整原因：

- 原来 `JwtTokenService` Bean 放在 `WebConfig` 里。
- `WebConfig` 又依赖 `AuthInterceptor`。
- `AuthInterceptor` 也需要 `JwtTokenService`。
- 这样启动测试时形成了循环依赖。

修复方式：

- 把 `JwtTokenService` Bean 移到独立的 `JwtConfig`。
- `WebConfig` 只保留 MVC 相关职责：
  - 注册鉴权拦截器。
  - 配置 CORS。
  - 配置上传文件静态访问路径。

这样职责更清楚，也解决了 Spring Bean 循环依赖问题。

## 9. 新增测试

新增文件：

```text
sky-server/src/test/java/com/sky/database/DatabaseSchemaContractTest.java
```

作用：

- 检查 `schema.sql` 是否包含 Day 1 要求的 11 张表。
- 检查是否包含公共审计字段。
- 检查 `data.sql` 是否包含员工基础数据和 `admin` 演示账号。

新增文件：

```text
sky-server/src/test/java/com/sky/docs/ApiDocumentationIntegrationTest.java
```

作用：

- 用 Spring Boot 随机端口启动后端测试环境。
- 请求 `/doc.html`，确认 Knife4j 页面能访问。
- 请求 `/v3/api-docs`，确认接口文档里能看到 `/admin/employee/login`。

## 10. 今天遇到的问题和解决方式

问题 1：数据库结构测试先失败。

- 原因：一开始还没有 `db/schema.sql` 和 `db/data.sql`。
- 解决：新增两个 SQL 文件，补齐 11 张基础表和基础数据。
- 结果：`DatabaseSchemaContractTest` 通过。

问题 2：接口文档集成测试启动失败。

- 原因：`WebConfig`、`AuthInterceptor`、`JwtTokenService` 之间形成循环依赖。
- 解决：新增 `JwtConfig`，把 `JwtTokenService` Bean 移出 `WebConfig`。
- 结果：后端测试环境可以正常启动。

问题 3：H2 执行 SQL 初始化失败。

- 原因：`user` 和 `value` 在 H2 中可能被当成关键字。
- 解决：H2 JDBC URL 增加 `NON_KEYWORDS=USER,VALUE`。
- 结果：H2 MySQL 兼容模式可以正常建表和导入数据。

## 11. 今日验证记录

后端数据库结构测试：

```powershell
.\mvnw.cmd -Dtest=DatabaseSchemaContractTest test
```

结果：通过，2 个测试成功。

后端 Knife4j 文档测试：

```powershell
.\mvnw.cmd -Dtest=ApiDocumentationIntegrationTest test
```

结果：通过，2 个测试成功。

后端完整测试：

```powershell
.\mvnw.cmd test
```

结果：通过，10 个测试成功，0 个失败。

后端打包：

```powershell
.\mvnw.cmd -DskipTests package
```

结果：`BUILD SUCCESS`。

后端真实启动烟测：

```powershell
java -jar target\sky-server-0.0.1-SNAPSHOT.jar
```

启动后访问：

```text
http://localhost:8080/doc.html
http://localhost:8080/v3/api-docs
```

结果：

- `/doc.html`：HTTP 200，包含 `Knife4j`。
- `/v3/api-docs`：HTTP 200，包含 `/admin/employee/login`。

管理端构建：

```powershell
npm run build
```

结果：构建成功。Vite/Rolldown 输出了第三方依赖注释和大 chunk 提示，这些是警告，不是失败。

管理端 dev server 验证：

```powershell
npm run dev -- --host 127.0.0.1 --port 5173 --strictPort
```

结果：

- `http://127.0.0.1:5173/` 返回 HTTP 200。
- 页面包含 Vue 根节点 `<div id="app"></div>`。

提交前工作区检查：

```powershell
git diff --check
git status --short
```

结果：

- `git diff --check` 通过，只有 Windows 换行格式提示，不是失败。
- `git status --short` 只包含 Day 1 预期新增和修改文件。

## 12. 今天完成后的状态

Day 1 完成后，项目具备以下能力：

- 后端能启动。
- 后端能自动初始化基础表和基础数据。
- Knife4j 文档能打开。
- 员工登录接口能在接口文档里看到。
- 统一响应、全局异常、分页对象等公共基础能力继续存在。
- 管理端可以构建，也可以启动 dev server。

下一天 Day 2 的重点应该是：

- 员工登录真正接入数据库。
- JWT 登录链路完善。
- 员工管理 CRUD。
- 分类管理 CRUD。
- 管理端登录页和员工、分类页面联调。
