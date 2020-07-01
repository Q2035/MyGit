---
## job submission

计算机网络实训项目

基于SpringBoot的作业提交系统，支持定制化作业名，支持用户登录、用户管理、文件下载、文件上传、作业发布、邮件提醒等功能。

URL

| URL                                           | Method | Description                |
| --------------------------------------------- | ------ | -------------------------- |
| /job/user/index;/job/user/;/job/user/login    | Get    | 登录界面                   |
| /job/user/authentication?username=xpassword=x | Post   | 登录认证                   |
| /job/user/logout                              | Get    | 登出，返回登录界面         |
| /job/user/home                                | Get    | 用户主页面                 |
| /job/user/fileupload/{jobId}                  | Get    | 用户文件上传               |
| /job/admin/index;/job/admin/home              | Get    | 管理员主页面               |
| /job/admin/userdelete/{Id}                    | Get    | 删除用户                   |
| /job/manager/index                            | Get    | 用户管理员主界面           |
| /job/manager/jobadd                           | Get   | 管理员作业发布             |
| /job/manager/jobadd                           | Get    | 管理员发布作业界面         |
| /job/manager/filedownload/{jobId}             | Post   | 管理员下载作业提交文件     |                         |
| /job/manager/jobinfo                          | Get    | 管理员展示作业提交详情界面 |


