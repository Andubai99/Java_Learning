# 第 0 天任务记录：项目初始化与执行约束锁定

日期：2026-05-23

## 今天完成了什么

第 0 天的目标是把项目从“只有计划文档”推进到“有明确目录、有三个子项目、有基础构建能力、有 Git 提交记录”的状态。

今天完成的事情：

- 创建并使用开发分支 `codex/sky-take-out-ai`。
- 按用户要求，把项目放在 `F:\Java_Learning\SkyTakeout\sky-take-out-ai`。
- 初始化三个子项目：
  - `sky-server`：Spring Boot 后端。
  - `sky-admin-web`：Vue 3 + Vite 管理端。
  - `sky-user-miniapp`：微信小程序用户端。
- 添加 `.gitignore`，避免提交临时依赖、构建产物、上传文件和 IDE 文件。
- 后端增加了基础工程、统一返回、异常、JWT、登录拦截、员工、分类、菜品、套餐、地址、购物车、订单状态机等基础代码。
- 管理端增加了最小可构建页面。
- 小程序端增加了最小可打开骨架。
- 更新 `PLAN.md`，把项目目录和一步一步执行纪律写入计划。
- 第 0 天内容已提交到 Git。

## 为什么要做这些

一个完整项目不能一开始就写业务页面。第 0 天先解决三件事：

1. 项目放在哪里。
2. 前端、后端、小程序三个工程如何组织。
3. 后续每天如何验证、记录、提交。

这样后面的第 1 天到第 8 天才能按计划推进，不会出现文件放错位置、漏提交、跳步骤的问题。

## 当前目录结构

```text
F:\Java_Learning
├── .gitignore
└── SkyTakeout
    ├── PLAN.md
    ├── docs
    │   └── day-00.md
    └── sky-take-out-ai
        ├── sky-server
        ├── sky-admin-web
        └── sky-user-miniapp
```

目录说明：

- `.gitignore`：Git 忽略规则，控制哪些文件不提交。
- `SkyTakeout/PLAN.md`：总计划，后续所有开发按这里执行。
- `SkyTakeout/docs`：每天任务记录文档目录。
- `SkyTakeout/sky-take-out-ai`：真正的项目源码目录。
- `sky-server`：后端工程。
- `sky-admin-web`：管理端前端工程。
- `sky-user-miniapp`：用户端微信小程序工程。

## 根目录文件

### `.gitignore`

作用：告诉 Git 哪些文件不要提交。

当前忽略内容包括：

- `target/`：Java/Maven 构建产物。
- `.mvn/apache-maven-*`：后端 Maven Wrapper 临时下载的 Maven。
- `node_modules/`：前端依赖目录。
- `dist/`：前端构建产物。
- `uploads/`：后续文件上传目录。
- `.idea/`、`.vscode/`：IDE 本地配置。

这类文件通常很大、可重新生成，或者只和本机环境有关，所以不应该进入仓库。

## 后端项目：`sky-server`

后端是 Spring Boot 项目，目标是提供 REST API、认证、业务逻辑、接口文档和后续数据库访问。

### `sky-server/pom.xml`

Maven 项目配置文件。

它声明了：

- 项目坐标：`com.sky:sky-server`。
- Java 版本：17。
- Spring Boot 版本。
- 后端依赖：
  - Spring Web：提供 HTTP API。
  - Validation：参数校验。
  - WebSocket：后续来单提醒。
  - Redis：后续缓存。
  - MyBatis：后续数据库访问。
  - Knife4j：接口文档。
  - Apache POI：后续 Excel 导出。
  - MySQL 驱动：连接 MySQL。
  - H2：本地测试可用的内存数据库。
  - Spring Boot Test：单元测试。

### `sky-server/mvnw.cmd`

Windows 下的 Maven Wrapper 启动脚本。

因为本机没有全局 `mvn` 命令，所以这个脚本会在需要时下载 Maven 到本地临时目录，然后执行 Maven 命令。这样后续可以用：

```powershell
.\mvnw.cmd test
```

来运行后端测试。

### `src/main/resources/application.yml`

Spring Boot 配置文件。

当前配置了：

- 后端端口：`8080`。
- 应用名：`sky-server`。
- H2 测试数据库连接。
- Redis 默认地址：`localhost:6379`。
- Knife4j 开启。
- JWT 密钥和过期时间。
- 文件上传目录：`uploads`。

后续接 MySQL 时，会继续调整数据库连接配置。

### `src/main/java/com/sky/SkyServerApplication.java`

后端启动入口。

运行这个类就会启动 Spring Boot 应用。它还启用了定时任务能力，后续第 6 天会用来扫描超时订单。

## 后端 common 包

`common` 包放所有模块都可能用到的公共能力。

### `BusinessException.java`

业务异常类。

当出现“账号不存在”“订单状态不允许流转”“菜品不能删除”这类业务错误时，抛出这个异常。

### `Result.java`

统一接口返回格式。

成功格式：

```json
{ "code": 1, "msg": null, "data": ... }
```

失败格式：

```json
{ "code": 0, "msg": "错误信息", "data": null }
```

### `GlobalExceptionHandler.java`

全局异常处理器。

它负责把 Java 异常转换成统一 JSON 响应，避免接口直接返回一大段错误堆栈。

### `PageResult.java`

分页返回对象。

包含：

- `total`：总条数。
- `records`：当前页数据。

员工、分类、菜品、套餐分页都会用到。

### `TokenSubject.java`

Token 中保存的登录身份。

包含：

- `id`：员工 ID 或用户 ID。
- `role`：角色，例如 `employee` 或 `user`。

### `JwtTokenService.java`

JWT 生成和解析服务。

当前实现了：

- 根据登录身份生成 token。
- 校验 token 签名。
- 校验 token 是否过期。
- 从 token 中解析当前登录人。

### `CurrentUser.java`

当前请求中的登录用户信息。

它保存从 token 中解析出的 `id` 和 `role`。

### `CurrentUserContext.java`

当前登录用户上下文。

后端每次请求开始时把当前用户放进去，业务代码需要知道“谁在操作”时可以从这里读取。

## 后端 config 包

`config` 包放 Spring Boot 配置。

### `SkyProperties.java`

读取 `application.yml` 中 `sky` 开头的配置。

当前包括：

- JWT 密钥。
- JWT 过期时间。
- 文件上传目录。

### `AuthInterceptor.java`

登录拦截器。

它检查请求头里是否有：

```text
Authorization: Bearer <token>
```

如果没有 token 或 token 无效，就返回未登录。如果 token 有效，就把当前登录人写入 `CurrentUserContext`。

### `WebConfig.java`

Web 配置类。

当前负责：

- 注册 `AuthInterceptor`。
- 放行登录接口、静态资源、接口文档。
- 配置跨域。
- 配置 `/uploads/**` 静态文件访问。
- 创建 `JwtTokenService` Bean。

## 后端 employee 包

`employee` 包负责管理员/员工相关能力。

### `Employee.java`

员工实体类。

字段包括：

- `id`
- `username`
- `password`
- `name`
- `phone`
- `status`
- `createTime`
- `updateTime`

### `EmployeeCommand.java`

员工新增或修改请求对象。

前端提交员工数据时使用。

### `EmployeeLoginCommand.java`

员工登录请求对象。

包含：

- `username`
- `password`

### `EmployeeLoginVO.java`

员工登录返回对象。

包含：

- 员工 ID。
- 用户名。
- 姓名。
- token。

### `EmployeeService.java`

员工业务服务。

当前实现了：

- 员工登录。
- 员工分页查询。
- 新增员工。
- 修改员工。
- 根据 ID 查询员工。
- 启用/禁用员工。

### `AdminEmployeeController.java`

员工管理接口。

当前接口前缀是：

```text
/admin/employee
```

提供登录、分页、新增、修改、查询、启禁用接口。

## 后端 product 包

`product` 包负责分类、菜品、套餐。

### `Category.java`

分类实体。

分类可以是菜品分类，也可以是套餐分类。

### `CategoryCommand.java`

分类新增或修改请求对象。

### `CategoryService.java`

分类业务服务。

当前实现了：

- 新增分类。
- 分类分页。
- 用户端分类列表。
- 修改分类。
- 启用/禁用分类。
- 删除分类。

删除分类时，如果分类下还有菜品或套餐，会阻止删除。

### `Dish.java`

菜品实体。

字段包括：

- 菜品 ID。
- 菜品名称。
- 分类 ID。
- 价格。
- 图片。
- 描述。
- 状态。
- 口味列表。

### `DishCommand.java`

菜品新增或修改请求对象。

### `DishService.java`

菜品业务服务。

当前实现了：

- 新增菜品。
- 菜品分页。
- 用户端查询启售菜品。
- 按 ID 查询。
- 修改菜品。
- 启售/停售。
- 删除或批量删除。

删除时会阻止删除已启售菜品。

### `Setmeal.java`

套餐实体。

字段包括：

- 套餐 ID。
- 套餐名称。
- 分类 ID。
- 价格。
- 图片。
- 描述。
- 状态。
- 关联菜品 ID 列表。

### `SetmealCommand.java`

套餐新增或修改请求对象。

### `SetmealService.java`

套餐业务服务。

当前实现了：

- 新增套餐。
- 套餐分页。
- 用户端查询启售套餐。
- 按 ID 查询。
- 修改套餐。
- 启售/停售。
- 删除或批量删除。

删除时会阻止删除已启售套餐。

### `ProductCache.java`

商品缓存类。

当前是内存缓存，用来模拟后续 Redis 缓存逻辑：

- 查询用户端菜品/套餐时可以缓存结果。
- 分类、菜品、套餐变化时清空缓存。

第 6 天会按计划补 Redis。

## 后端 address 包

`address` 包负责用户地址簿。

### `AddressBook.java`

地址实体。

字段包括：

- 地址 ID。
- 用户 ID。
- 收货人。
- 手机号。
- 省份。
- 城市。
- 详细地址。
- 是否默认地址。

### `AddressCommand.java`

新增或修改地址时的请求对象。

### `AddressBookService.java`

地址业务服务。

当前实现了：

- 新增地址。
- 修改地址。
- 地址列表。
- 按 ID 查询。
- 查询默认地址。
- 设置默认地址。
- 删除地址。

其中“设置默认地址”会把同一用户的其他地址都改成非默认，保证默认地址只有一个。

## 后端 cart 包

`cart` 包负责购物车。

### `CartItemCommand.java`

加入购物车或减少购物车时的请求对象。

包含商品 ID、套餐 ID、类型、名称、图片、金额。

### `ShoppingCartItem.java`

购物车明细实体。

字段包括：

- 明细 ID。
- 用户 ID。
- 菜品 ID。
- 套餐 ID。
- 类型。
- 名称。
- 单价。
- 总金额。
- 数量。
- 创建时间。

### `ShoppingCartService.java`

购物车业务服务。

当前实现了：

- 添加商品到购物车。
- 同一商品重复添加时数量累加。
- 减少商品数量。
- 查看购物车。
- 清空购物车。

## 后端 order 包

### `OrderStateMachine.java`

订单状态机。

定义订单状态：

- `1` 待付款。
- `2` 待接单。
- `3` 已接单。
- `4` 派送中。
- `5` 已完成。
- `6` 已取消。

并限制订单只能按计划允许的路径流转。例如已完成订单不能再取消。

## 后端 store 包

### `InMemorySkyStore.java`

内存数据仓库。

当前用于第 0 天和早期开发阶段，不依赖 MySQL 就能让服务和测试跑起来。

它保存：

- 员工列表。
- 分类列表。
- 菜品列表。
- 套餐列表。
- 地址列表。
- 购物车列表。
- 店铺营业状态。

它还初始化了演示数据：

- 管理员账号：`admin / 123456`。
- 示例分类。
- 示例菜品。
- 示例套餐。

后续正式接 MySQL 和 MyBatis 时，这部分会逐步替换为 Mapper 和数据库表。

## 后端测试文件

### `JwtTokenServiceTest.java`

验证 JWT 能正确生成和解析，并能拒绝被篡改的 token。

### `OrderStateMachineTest.java`

验证订单状态只能按计划允许的路径流转，并拒绝非法状态变化。

### `ShoppingCartServiceTest.java`

验证同一个菜品多次加入购物车时，数量和金额会累加。

### `AddressBookServiceTest.java`

验证同一个用户只能有一个默认地址。

## 管理端项目：`sky-admin-web`

管理端是 Vue 3 + Vite 项目，后续会实现管理员登录、员工管理、分类管理、菜品管理、套餐管理、订单管理、报表等页面。

### `package.json`

前端项目配置。

定义了命令：

- `npm run dev`：启动开发服务器，端口 5173。
- `npm run build`：构建生产版本。
- `npm run preview`：预览构建结果。

声明了依赖：

- Vue 3。
- Vite。
- Element Plus。
- Axios。
- ECharts。
- Vue Router。

### `package-lock.json`

锁定 npm 依赖的具体版本。

有了它，其他人安装依赖时能尽量得到相同版本，减少“我电脑能跑，你电脑不能跑”的问题。

### `vite.config.js`

Vite 配置文件。

当前注册了 Vue 插件，让 Vite 能识别和编译 `.vue` 文件。

### `index.html`

管理端 HTML 入口。

里面有：

```html
<div id="app"></div>
```

Vue 应用会挂载到这个节点上。

### `src/main.js`

管理端 JavaScript 入口。

它创建 Vue 应用，加载 Element Plus 和全局样式，然后挂载 `App.vue`。

### `src/App.vue`

当前的管理端首页骨架。

它显示“苍穹外卖管理端”说明文字。后续会替换成登录页和后台 Layout。

### `src/styles.css`

管理端全局样式。

当前设置了基础字体、背景色、居中布局和面板样式。

## 用户端小程序：`sky-user-miniapp`

用户端是微信小程序骨架，后续会实现登录、浏览商品、购物车、地址、下单。

### `project.config.json`

微信开发者工具项目配置。

定义了项目名、appid、编译类型和基本设置。

### `app.json`

小程序全局配置。

当前注册了首页：

```text
pages/index/index
```

并设置了导航栏标题“苍穹外卖”。

### `app.js`

小程序全局脚本。

当前保存后端 API 地址：

```text
http://localhost:8080
```

### `app.wxss`

小程序全局样式。

设置页面背景、文字颜色和字体。

### `pages/index/index.js`

首页逻辑。

当前只提供标题和说明文字数据。

### `pages/index/index.wxml`

首页结构。

负责显示标题和说明。

### `pages/index/index.wxss`

首页样式。

设置页面内边距、白色面板、标题和说明文字样式。

## 今天修改过的计划文档

### `SkyTakeout/PLAN.md`

今天对计划做了两次更新：

- 把项目目录从 `F:\Java_Learning\sky-take-out-ai` 改为 `F:\Java_Learning\SkyTakeout\sky-take-out-ai`。
- 增加执行纪律：必须一步一步执行、不得跨天混做、每天结束必须提交、失败先修复再继续。
- 增加每日文档规则：每天完成后在 `SkyTakeout/docs` 生成对应天数的详细记录文档。

## 今天执行过的验证

### 后端测试

命令：

```powershell
.\mvnw.cmd test
```

结果：

- 测试数：6。
- 失败：0。
- 错误：0。
- 构建结果：`BUILD SUCCESS`。

### 管理端依赖安全检查

命令：

```powershell
npm audit --audit-level=moderate
```

结果：

- `found 0 vulnerabilities`。

### 管理端构建

命令：

```powershell
npm run build
```

结果：

- 构建成功。
- Vite 输出了第三方依赖注释和 chunk 体积警告，这不是构建失败，不影响第 0 天提交。

### Git 状态检查

提交前检查了：

```powershell
git status --short --branch
```

确认工作区变化都进入提交，且构建产物和依赖目录被 `.gitignore` 忽略。

## 今天遇到的问题和处理

### 问题 1：项目最初放错目录

问题：一开始项目创建到了 `F:\Java_Learning\sky-take-out-ai`。

处理：根据用户要求，移动到：

```text
F:\Java_Learning\SkyTakeout\sky-take-out-ai
```

并把这个要求写入 `PLAN.md`。

### 问题 2：没有全局 Maven 命令

问题：本机执行 `mvn -version` 失败，说明没有全局 Maven。

处理：添加 `mvnw.cmd`，让后端可以通过项目内脚本运行 Maven。

### 问题 3：后端提前引用了未创建类

问题：`InMemorySkyStore` 一度提前引用了用户和订单类，导致编译失败。

处理：先移除这些提前引用，保持第 0 天代码可编译。用户和订单会在后续天数按计划补上。

### 问题 4：管理端第一次构建失败

问题：缺少 `vite.config.js`，导致 Vite 不能正确解析 `.vue` 文件。

处理：新增 `vite.config.js` 并注册 `@vitejs/plugin-vue`。

### 问题 5：管理端依赖有 audit 风险

问题：旧版 Vite 依赖链有中等风险提示。

处理：升级 Vite 和 Vue 插件，重新安装依赖，`npm audit` 变为 0 漏洞。

### 问题 6：第 0 天第一次结束时漏提交

问题：计划明确要求每天结束提交一次 Git commit，但第一次汇报时没有提交。

处理：用户指出后，已补提交，并把每日提交纪律写入计划。

## 今天的 Git 提交

### `14a6cde chore: initialize sky takeout project`

内容：

- 初始化三个子项目。
- 添加后端基础工程和测试。
- 添加管理端骨架。
- 添加小程序骨架。
- 添加 `.gitignore`。

### `7355992 docs: update plan execution constraints`

内容：

- 更新 `PLAN.md`。
- 固定项目目录到 `SkyTakeout` 下。
- 增加一步一步执行、验证、提交等纪律。

## 0 基础读者应该先看什么

建议阅读顺序：

1. 先看 `SkyTakeout/PLAN.md`，理解整个项目要做什么。
2. 再看本文件，理解第 0 天为什么先做工程初始化。
3. 看 `sky-server/pom.xml`，理解后端用了哪些技术。
4. 看 `sky-server/src/main/java/com/sky/SkyServerApplication.java`，找到后端入口。
5. 看 `sky-admin-web/package.json`，理解管理端如何启动和构建。
6. 看 `sky-user-miniapp/app.json`，理解小程序页面入口。

## 下一步

下一步进入第 1 天：

- 后端骨架完善。
- 数据库建表和基础数据。
- Knife4j 接口文档。
- 统一接口返回、全局异常、参数校验、分页结构。
- 验收 `sky-server` 启动、接口文档、MySQL 表结构和登录接口文档展示。
