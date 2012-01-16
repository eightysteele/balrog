(ns balrog.views.welcome
  (:use [noir.core :only (defpage)]
        [noir.options :only (dev-mode?)]
        [hiccup.page-helpers :only (include-js javascript-tag)])
  (:require [balrog.views.common :as common]))

(defpage "/" []
  (common/layout 
   (include-js "cljs/bootstrap.js")
   (when (dev-mode?)
     (javascript-tag "goog.require('balrog.repl');"))
   (javascript-tag "goog.require('balrog.mainview')")
   [:div#map_canvas]))
