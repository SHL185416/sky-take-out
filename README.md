# Q&A

## 启动方法

启动SkyApplicaiton后，

启动redis
启动nginx，从nginx的前端页面进入。
访问localhost:81

## @JsonFormat

在Spring框架中，@JsonFormat注解的作用是将Java对象转换为JSON字符串，或将JSON字符串转换为相应的Java对象。它用于指定日期、时间、数字、字符串等数据类型的格式。
使用@JsonFormat注解可以方便地处理JSON数据，因为它可以自动将Java对象转换为JSON字符串，无需手动进行转换。
例如，当从数据库中获取JSON数据并将其转换为Java对象时，可以使用@JsonFormat注解来指定日期和时间的格式，从而避免手动进行转换。
