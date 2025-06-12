(ns web-note.core
  (:require [clojure.java.shell :refer [sh]]
            [clojure.java.io :as io]
            [compojure.core :refer :all]
            [compojure.route :as route]
            [hiccup.page :refer [html5 include-css]]
            [hiccup.element :refer [link-to]]
            [ring.adapter.jetty :refer [run-jetty]])
  (:gen-class))


(def HOME (System/getProperty "user.home"))
(def BASE (or (System/getenv "WEB_NOTE_HOME") (str HOME "/Documents/notes")))
(def PORT (parse-long (or (System/getenv "WEB_NOTE_PORT") "3000")))

(defn parse-org [org-file]
  (let* [script "export-org-to-html.el"
         path   (str BASE "/" org-file)
         cmd    ["emacs" "-x" script path]]
    (println (str "Running: " cmd))
    ;; TODO: if :exit not 0 or if it's throw error throw
    (:out (apply sh cmd))))

(defn page-wrapper [title & main]
  (html5
   [:head
    [:title title]
    (include-css "https://gongzhitaao.org/orgcss/org.css")]
   [:body
    [:header [:h1 title]]
    [:main main]
    [:footer "© 2025 Web Note"]]))

(defn homepage []
  (page-wrapper "Welcome"
                [:p "Home"]
                [:p (link-to "/list" "List")]))

(defn list-org-files []
  (let* [dir   (io/file BASE)
         files (when (.exists dir)
                 (sort (filter (fn [file] (.isFile file)) (.listFiles dir))))]
    (page-wrapper "Org Files"
                  [:h1 (str "Files: ~/")]
                  (if (seq files)
                    [:ul
                     (for [f files]
                       [:li (link-to (str "/view/" (.getName f)) (.getName f))])]
                    [:p "No files found."]))))

(defn org-page [path]
  (page-wrapper path
                (parse-org path)
                (link-to "/" "Back to home")))

(defroutes app-routes
  (GET "/"           [] (homepage))
  (GET "/list"       [] (list-org-files))
  (GET "/view/:path" [path] (org-page path))
  (route/not-found "404 - Not Found"))

(def app app-routes)

(defn -main [& args]
  (run-jetty app {:port PORT :join? false}))
