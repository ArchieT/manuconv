(ns manuconv.core
  (:require [clojure.tools.cli :refer [parse-opts]])
  (:gen-class))

(def cli-options
  ["-i" "--interactive" "Interactive TUI mode"])

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (parse-opts args cli-options))
