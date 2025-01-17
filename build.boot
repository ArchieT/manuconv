(def project 'manuconv)
(def version "0.1.0-SNAPSHOT")

(set-env! :resource-paths #{"resources" "src"}
          :source-paths   #{"test"}
          :dependencies   '[[org.clojure/clojure "RELEASE"]
                            [adzerk/boot-test "RELEASE" :scope "test"]
                            [dk.ative/docjure "RELEASE"]
                            [clojure-lanterna "RELEASE"]])

(task-options!
 aot {:namespace   #{'manuconv.core}}
 pom {:project     project
      :version     version
      :description "FIXME: write description"
      :url         "https://github.com/ArchieT/manuconv"
      :scm         {:url "https://github.com/ArchieT/manuconv"}
      :license     nil}
 repl {:init-ns    'manuconv.core}
 jar {:main        'manuconv.core
      :file        (str "manuconv-" version "-standalone.jar")})

(deftask build
  "Build the project locally as a JAR."
  [d dir PATH #{str} "the set of directories to write to (target)."]
  (let [dir (if (seq dir) dir #{"target"})]
    (comp (aot) (pom) (uber) (jar) (target :dir dir))))

(deftask run
  "Run the project."
  [a args ARG [str] "the arguments for the application."]
  (with-pass-thru fs
    (require '[manuconv.core :as app])
    (apply (resolve 'app/-main) args)))

(require '[adzerk.boot-test :refer [test]])
