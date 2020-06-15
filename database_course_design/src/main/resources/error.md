## MyBatis
1. Expected one result (or null) to be returned by selectOne(), but found: 9
  SQL:select user.* from j_user user ,j_role role,j_user_role user_role where username = #{username}
  只需要把后面没用到的表去了就行

2. mybatis association 传递方法中的参数
  将需要传递的参数传入association的column属性中即可

  ![1592222449467](D:\Private\Code\Java\MyGit\database_course_design\src\main\resources\static\md_error\1592222449467.png)

  将column中的值传入到新的select