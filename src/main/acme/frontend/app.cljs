(ns acme.frontend.app
  (:require-macros
   [cljs.core]
   [cljs.core :refer [exists?]])
  (:require
   [goog.string.format]
   [goog.string :refer (format)]
   [cljs.analyzer.api :as api]
   [clojure.string :as str]
   )
  
  )

;3 由cljs控制初始化流程（便于不用u3d，先通过浏览器调试）
;-----------------util--------------------------

(def console-log (.-log js/console)) ;console.log()

(defn to-json [x]
  (.stringify js/JSON (clj->js x))
  )

;-------------------------------------------

(defn create-callback-u3d [fn-name]
  ; create browser.RegisterFunction by u3d ZenFulcrum.EmbeddedBrowser
  (let [fn-fullname (format "window.%s" fn-name) ]
    (if (js/eval fn-fullname)
      (js/eval fn-fullname)
      (fn [x] (console-log "mock", fn-fullname, x)))))


; 根据回调函数名字，从window.XXX 构造函数 不能带- 只能用下划线
(def list-fname-callback-u3d ["CallBackU3d_compile_time_done" "CallBackU3d_query_result" "CallBackU3d_InitGrids"])

(def dict-fn-callbacku3d {})
  
(defn create_dict_fn_callback [list_name]
  (into {} (for [x  list_name]
             (hash-map x (create-callback-u3d x))))) 

(defn compile-time []
   ;初始化流程
  (console-log "初始化u3d注册的callback")
  (set! dict-fn-callbacku3d (create_dict_fn_callback list-fname-callback-u3d))
  (console-log dict-fn-callbacku3d)
  ;; (init_grids_u3d 2.5)
  ;; (console-log (format "js/%s" "CallBackU3d_InitGrids"))
  ;; (console-log (api/ns-resolve :js "CallBackU3d_InitGrids"))
  ; 通知u3d 初始化完成
  ((dict-fn-callbacku3d "CallBackU3d_compile_time_done") [])
  )


;----------------运行时 玩家发起查询-----------------------------




;-------------------玩家发起的查询qurey开头，通过u3d返回----------------------
(defn qurey_foo [x]
  ; 查询 通过接口返回结果 返回查询名 json(结果)
  (let [res (format "res qurey-foo = %s" x)
        res_json (to-json res)
        ]
    ((dict-fn-callbacku3d "CallBackU3d_query_result") "qurey_foo" res_json)
  )
)


(defn init []
  ; regisit fn like "window.qurey_foo" in js
  (set! js/qurey_foo qurey_foo) 
  (console-log "Hello World")
  ;; (console-log (api/ns-resolve 'cljs.js "qurey-foo"))
  ;; (console-log (find-ns (symbol "js/qurey-foo")))
  ;; (console-log  ((api/ns-resolve (symbol "cljs.js") (symbol "qurey-foo")) 2))


;; (into {} (for [x  ["a" "b" "c"]] 
;;                           (hash-map x
;;                                     (#(format "window.%s" %) x))))



  ;; (println  (let [fun (js/eval "window.qurey_foo")]
  ;;              (console-log (fun 5))))
  ;; (println (if (js/eval "window.CallBackU3d_InitGrids")
  ;;            (console-log "ok")
  ;;            (console-log "false")
  ;;            ))

  
  ; wait for u3d regisit fn
  (js/setTimeout compile-time 3000)
  )