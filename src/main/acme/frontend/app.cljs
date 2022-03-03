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

(defn ->js [var-name]
      (-> var-name
          (str/replace #"/" ".")
          (str/replace #"-" "_")))


(defn invoke [function-name & args]
      (let [fun (js/eval (->js function-name))]
           (apply fun args)))



;3 由cljs控制初始化流程（便于不用u3d，先通过浏览器调试）

(def console-log (.-log js/console)) ;console.log()

(defn ^:export qurey-foo [x] (* 2 x))


(defn create-callback-u3d [fn-name]
  ; create browser.RegisterFunction by u3d ZenFulcrum.EmbeddedBrowser
  (let [fn-fullname (format "window.%s" fn-name) ]
    (if (exists? (js/eval fn-fullname))
      (js/eval fn-fullname)
      (fn [x] (console-log "mock", fn-name, x)))))

;; (apply fun that)

(def list_fname-callback-u3d ["CallBackU3d_InitGrids"])
(for [name list_fname-callback-u3d]
  [(symbol name) create-callback-u3d(name)]
  )

;; (into {} [[:a "a"] [:b "b"]])


;; (defn init_grids_u3d [x]
;; (let []

;; )

(defn compile-time []
   ;初始化流程
  (console-log "初始化")
  ;; (init_grids_u3d 2.5)
  ;; (console-log (format "js/%s" "CallBackU3d_InitGrids"))
  ;; (console-log (api/ns-resolve :js "CallBackU3d_InitGrids"))
  ;; (call-callback-u3d "CallBackU3d_InitGrids" 2.5)

)



(defn init []
  (set! js/qurey_foo qurey-foo) ;qurey api for u3d runtime  regisit fn like "window.qurey_foo" in js
  (console-log "Hello World")
  ;; (console-log (api/ns-resolve 'cljs.js "qurey-foo"))
  ;; (console-log (find-ns (symbol "js/qurey-foo")))
  ;; (console-log  ((api/ns-resolve (symbol "cljs.js") (symbol "qurey-foo")) 2))

  (println   (let [fun (js/eval "window.qurey_foo")]
               (console-log (fun 5))))
  ; wait for u3d regisit fn
  ;; (js/setTimeout compile-time 3000)
  )