输入使用http表单

返回数据使用json格式字符串

某些方法做了异常处理，某些没做，使用时需严格规范格式

#createEvent

##输入

Title：String

UserID：String

Content：String

StartTime：YYYY-mm-dd (具体到时分秒还未做，将来将会是YYYY-mm-dd HH:MM:SS的格式)

EndTime：YYYY-mm-dd（同上）

Share：String（值为“共享”或“不共享”）

## 输出

errCode：0为正常，否则为错误代码，具体代表哪个错误由前端决定



# changeEvent

## 输入

EventID：int 请使用queryEvents返回的EventID

Title：String

UserID：String

Content：String

StartTime：YYYY-mm-dd (具体到时分秒还未做，将来将会是YYYY-mm-dd HH:MM:SS的格式)

EndTime：YYYY-mm-dd（同上）

Share：String（值为“共享”或“不共享”）

##输出

errCode



#commentEvent

## 输入

EventID：int 请使用queryEvents返回的EventID

UserID：String 此处指评论者

Content：String

## 输出

errCode



#deleteEvent

## 输入

EventID：int 请使用queryEvents返回的EventID

##输出

errCode



# queryEvents

## 输入

UserID：String

Fin：int 指完成状态 0未完成 1已完成

## 输出

errCode：int

events：一个list，每个元素是一个dict，dict内包含：

​			eventID：int

​			permission int，权限，最低位为0表示不共享，为1表示共享，高位暂时空闲无用

​			approve 点赞个数，目前无用

​			fin 0/1

​			userID string

​			title string

​			content string

​			startTime YYYY-mm-dd

​			endTime YYYY-mm-dd

​	返回格式如下：{errCode:0, events:[{eventID:1, permission:0, ...}, {...}, ...]}



#queryComments

## 输入

UserID：String

## 输出

errCode：int

comments：一个list，每个元素是一个dict，dict内包含：

​			eventID：int 事件的ID

​			userID：string

​			title：string 事件的标题

​			content：string



# queryNearby

查询你好友的共享事件

## 输入

UserID：String

## 输出

同queryEvents



# updateFriends

增减朋友关系（单向朋友）

## 输入

userID：string 你的ID

uBID：string 你要修改的朋友的ID

friendStatus：int 0/1 0为删除这条朋友关系，1为增加这条朋友关系

## 输出

errCode



# queryFriends

## 输入

userID：string

## 输出

errcode

friends：list，每个元素是个dict，dict内包含
​				uBID：你的朋友的ID



# registe

##输入

userID：string

password：string

##输出

errCode



#logIn

##输入

userID：string

password：string

##输出

使用session设置cookie，userID：userID，不过后续只有登录登出改密码系列的操作使用了cookie，其他的操作每次都直接传输userID，因此尚未直接使用到cookie

errCode



#logOut

## 输入

userID：string

## 输出

清除session的cookie

errCode



#updatePassword

## 输入

userID：string

password：string

##输出

errcode