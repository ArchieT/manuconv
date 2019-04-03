(ns manuconv.source.csv
  (:require [clojure.string :refer [split]]))

                                        ; c-style escapes treatment: characters like comma and newlines, and maybe double quotes, but also maybe others
                                        ; double quotes treatment
                                        ; double quotes quoted with a double quote: inside a double quoted string or not
                                        ; for excel, a value with leading zeros or spaces double quoted with a preceding equals sign
                                        ; : a field is then treated as a formula that returns a string
                                        ; to make it still be for excel but a little more strict to spec, we can then proceed
                                        ; : to take all of it in double quote with dq-dq-escaping
                                        ; : but, this solution once wasn't working in excel for mac

(comment :double-quotes-as-just-a-char)
(comment :no-dq-dq-escaping)
(comment :someday-later-maybe :backslash-escapable-double-quotes :only-inside-double-quotes :only-outside-double-quotes)
(comment :someday-later-maybe :backslash-escapable-comma-outside-double-quotes)
; (comment :double-quotes-only-starting-at-beginning)
(comment :double-quotes-preceded-with-only-equals-trimmed-of-equals)
(comment :someday-later-maybe :backslash-escaped-backslash :only-outside-double-quotes :both-in-and-out-of-double-quotes :backslash-escaped-equals)
(comment :someday-later-maybe :backslash-escapable-cr)
(comment :someday-later-maybe :backslash-escapable-lf)
(comment :for-now-lets-be-ok-with-this :dquote-allowed-cr)
(comment :for-now-lets-be-ok-with-this :dquote-allowed-lf)
(comment :for-now-lets-just-have-this-constant-as :newlines #{:crlf :cr :lf})
(comment :take-unquoted-unescaped-whitespace-before-comma)
(comment :someday-later-maybe :unescaped-dq-preserved-inside-broader-dq)

(defn cell-from-string-returning-rest [input-string params-set]
  (let [[accu rest-of-string]
        (loop [accu (list)
               str (lazy-seq input-string)]
          (let [head (first str)
                str  (next str)]
            (cond
              (and (= head \")
                   (not (params-set :double-quotes-as-just-a-char)))
              (let [[inside outside] (split-with (comp not #{\"}) str)
                    outside          (next outside)]
                (recur (lazy-cat accu inside) outside))

              (#{\tab \space} head)
              (let [[spaces rest] (split-with #{\tab \space} str)]
                (if (= \, (first rest))
                  (if (params-set :take-unquoted-unescaped-whitespace-before-comma)
                    [(lazy-cat accu spaces) rest]
                    [accu rest])
                  (recur (lazy-cat accu spaces) rest)))

              (= head \,) [accu str] ; TODO FIXME remove whitespace before comma

              (and (= head \=)
                   (= \" (first str))
                   (empty? accu)
                   (params-set :double-quotes-preceded-with-only-equals-trimmed-of-equals))
              (recur (list) str)

              ; TODO FIXME unmatched dquote till the end of string handling
              )))
        accu (str accu)]
    [accu rest-of-string]))

(defn row-from-string-returning-rest [str params-set]
  (map
   #(apply str )
   (next
    (iterate
     (fn [[_ str]]
       (cell-from-string-returning-rest str params-set)) [nil str]))))

(defn csv-from-string [str params-set]
  (next
   (iterate
    (fn [[_ str]]
                                        ;      (comment split-with #{\" \,} str)
      (cell-from-string-returning-rest str params-set)) [nil str])))
