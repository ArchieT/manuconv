(ns manuconv.source.csv)

                                        ; c-style escapes treatment: characters like comma and newlines, and maybe double quotes, but also maybe others
                                        ; double quotes treatment
                                        ; double quotes quoted with a double quote: inside a double quoted string or not
                                        ; for excel, a value with leading zeros or spaces double quoted with a preceding equals sign
                                        ; : a field is then treated as a formula that returns a string
                                        ; to make it still be for excel but a little more strict to spec, we can then proceed
                                        ; : to take all of it in double quote with dq-dq-escaping
                                        ; : but, this solution once wasn't working in excel for mac

(comment :double-quotes-as-just-a-char)
(comment :backslash-escapable-double-quotes :only-inside-double-quotes :only-outside-double-quotes)
(comment :backslash-escapable-comma-outside-double-quotes)
; (comment :double-quotes-only-starting-at-beginning)
(comment :double-quotes-preceded-with-only-equals-trimmed-of-equals)
(comment :backslash-escaped-backslash :only-outside-double-quotes :both-in-and-out-of-double-quotes :backslash-escaped-equals)
(comment :backslash-escapable-cr)
(comment :backslash-escapable-lf)
(comment :dquote-allowed-cr)
(comment :dquote-allowed-lf)
(comment :newlines #{:crlf :cr :lf})
