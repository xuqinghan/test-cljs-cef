(ns acme.frontend.app
  (:require-macros
   [cljs.core :refer [exists? resolve]]
  )
  (:require
   [goog.string.format]
   [goog.string :refer (format)]
   [goog.exportSymbol :refer (^:export)]
   )
  
  )

;3 由cljs控制初始化流程（便于不用u3d，先通过浏览器调试）

(def console-log (.-log js/console)) ;console.log()

(defn ^:export qurey-foo [x] (* 2 x))

;; (defn call-callback-u3d [fn-name & that]
;;   (let [fn-fullname (format "js/%s" "CallBackU3d_InitGrids")
;;         s (symbol fn-fullname)
;;         ]
;;     (println (resolve s))
;;     )
;;   )

;; (defn init_grids_u3d [x]

;;     ))
    ;; (if (exists? f)
    ;;   (apply f that) ; call browser.RegisterFunction by u3d ZenFulcrum.EmbeddedBrowser
    ;;   (console-log "mock", fn-name, that)
    ;;   )


(defn compile-time []
   ;初始化流程
  (console-log "初始化")
  ;; (init_grids_u3d 2.5)
  (console-log (format "js/%s" "CallBackU3d_InitGrids"))
  ;; (call-callback-u3d "CallBackU3d_InitGrids" 2.5)
)


(defn init []
  (set! js/qurey-foo qurey-foo) ;qurey api for u3d runtime  regisit fn like "window.qurey_foo" in js
  (console-log "Hello World")
  ; wait for u3d regisit fn
  (js/setTimeout compile-time 3000)
  )