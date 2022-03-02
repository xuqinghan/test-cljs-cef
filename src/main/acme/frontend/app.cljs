(ns acme.frontend.app
  (:require-macros [cljs.core :refer [exists?]])
  )

;3 由cljs控制初始化流程（便于不用u3d，先通过浏览器调试）

(def console-log (.-log js/console)) ;console.log()

(defn qurey_foo [x] (* 2 x))

(defn init_grids_u3d [x]
    (if (exists? js/CallBackU3d_InitGrids)
      (js/CallBackU3d_InitGrids x)  ; call browser.RegisterFunction by u3d ZenFulcrum.EmbeddedBrowser
      (console-log "mock init_grids_u3d", x)
      ))

(defn compile-time []
    ;初始化流程
  (console-log "初始化")
  (init_grids_u3d 2.5)
)


(defn init []
  (set! js/qurey_foo qurey_foo) ;qurey api for u3d runtime  regisit fn like "window.qurey_foo" in js
  (console-log "Hello World")
  (js/setTimeout compile-time 3000)

  )