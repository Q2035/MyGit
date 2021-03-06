1. Expected one result (or null) to be returned by selectOne(), but found: 9
    SQL:select user.* from j_user user ,j_role role,j_user_role user_role where username = #{username}
    只需要把后面没用到的表去了就行

2. mybatis association 传递方法中的参数
    将需要传递的参数传入association的column属性中即可

  ![1592222449467](D:\Private\Code\Java\MyGit\database_course_design\src\main\resources\static\md_error\1592222449467.png)

  将column中的值传入到新的select

3. 日期格式化

   ~~~java
          DateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
           Date now = new Date();
           System.out.println(now);
           System.out.println(simpleDateFormat.format(now));
           Date parse = simpleDateFormat.parse("2020-02-03 23:32:11");
   ~~~

   使用SimpleDateFormat将字符串格式化Date时，应该注意所提供的字符串形式应当与SimpleDateFormat构造时所提供的字符串相符合。
  
4. MyBatis映射文件中不可出现小于号
    使用**&lt;**代替即可
    
5. String的split  

如果分割的字符为"."等转义字符，那么应该使用"\\."

6. Controller中返回值开头带斜杠

在IDE中运行正常，一旦打包就会出现异常

7. 从MySQL中导出部分数据

   ~~~shell
   mysql> select username,name from users into outfile 'a.txt';
   ~~~

   提示：

   ~~~shell
   ERROR 1290 (HY000): The MySQL server is running with the --secure-file-priv option so it cannot execute this statement
   ~~~

   查看变量"secure-file-priv"

   ~~~shell
   show variables like 'secure_file_priv';
   
   ~~~

   | Variable_name    | Value                 |
   | ---------------- | --------------------- |
   | secure_file_priv | /var/lib/mysql-files/ |
   于是将导出文件输出到此目录下：
   
   ~~~shell
   mysql> select username,name from users into outfile '/var/lib/mysql-files/a.txt';
   Query OK, 73 rows affected (0.01 sec)
   ~~~
   
   成功
   
   

