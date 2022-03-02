# 要点
- 由cljs控制初始化流程——便于先通过浏览器调试, 不用u3d
- 客户端初始化时： browser是主  u3d是从
- 推演运行时： u3d是主 
  - 1 u3d发起查询 调用 browser 的query_api
  - 2 browser执行query_api 在cljs db进行查询，计算
  - 3 browser执行callback_u3d 推送查询结果