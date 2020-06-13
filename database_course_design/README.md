最近事儿特多，Spring翻译也耽误了，搞不好最近显卡可能需要送保，还挺麻烦的。

一直在纠结到底是直接用Blog的数据库设计作为作业提交还是说新建一个项目从头开始自己来一遍

最后决定还是顺便做个作业提交的网站

---
## job submission

难搞噢

最先的肯定是User
> User
- id 主键
- username 用户名 UNIQUE
- password 密码
- nickname 昵称
- avatar 头像
- email 邮箱
- account_status 账户状态 封号、冻结、违规
- description 保留字段

用户角色权限控制
> Role
- id 主键
- role_name 角色名称
- role_description 角色描述

用户角色信息
> User_Role
- id 主键
- user_id 外键 用户id
- role_id 外键 角色id

黑名单用户
> BlackList
- id
- user_id 外键 用户id
- happen_time 发生时间
- ip_address ip地址
- reason 原因

管理员提交的作业提交任务
> Job
- id 任务编号
- job_description 任务描述（供前端使用）
- originator 外键 任务发起人
- start_time 任务开始时间
- deadline 截止时间，到期自动关闭任务
- submit_count
- total_count

需要提交任务名单
> Submit_Person
- id 唯一编号
- user_id 外键 参照User表
- job_id 外键 指向Job的id
- if_submit
- submit_time 提交时间

提交的任务的文件命名格式
> file_name
- id 唯一编号
- jos_id 绑定任务编号
- style 文件名格式

似乎差不都了