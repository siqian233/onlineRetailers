开始项目:

# 1. 创建父项目, 在pom.xml中规范版本号 , 同步到gitee.

# 2. 在这个项目创建子项目:
  1. AIAgent 
  2. idGenerator
  3. gateway
  判断用户是否在线
  common模块:ResponseResult
  数据库对应的bean    
  FeignClient客户对应的接口.     
  interface IdGeneratorApi 
  interface AiAgentApi

# 3. 创建用户模块， API 功能 :
   1. 登录->登录成功，返回一个token+客户端保存token到本地的浏览器空间.
   2. 注册->密码要加密成md5,或sha  
     请求中包含: uname password,email,telephone,headpic(阿里云 oss ).    
     对password进行加密, 存用户数据
     返回: ResponseResult{ code: 1, data: user信息}
   3. 注销:返回: ResponseResult{ code: 1}
   4. 判断用户名是否已经存在  ->发个请求， 判断用户是否已经在数据库有.
     返回: ResponseResult{code: 1, msg: 用户名存在}
     ResponseResult{code: 0, msg: 用户名可用}
   5. 查询用户:  根据uid查询用户信息    
     返回:  ResponseResult{code: 1,data:{用户信息}}
   6. 更新信息:  请求包含:  uid  uname password,    email, telephone,  ishidden,    headpic(阿里云 oss )  .
     根据uid更新
     返回:  ResponseResult{code: 1,data:{用户信息}}

   用户表:   uid  uname password,    email, telephone,  ishidden,    headpic(阿里云 oss )  .
   引入spring security + jwt

# 4. 创建文件上传模块，要求如下:
   1. 上传一个或多个文件到OSS中存储，并返回图片的云存储地址.
     文件类型约定为:图片格式，或是 pdf, excel, word
     大小: 不超过 10m/文件
     一次上传总文件大小不超过 50M
   2. 在上传模块中引入图片查重校验。
       图片上传后，先用md5算法取指纹码，再到redis查，是否有这个指纹码，如果有，则取 此指纹码对应的  图片url地址返回.
       如果没有，则将图片存到OSS中，并将键值对:      指纹码->图片地址    存到redis中
       最后将上传后的结果返回给客户端.
   3. 将此服务在 common模块发布成  Feign Api.
   4. 注意，此服务在 gateway中配置时，需要校验 是否登录，只有登录用户才可访问 .