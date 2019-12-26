(ns groove-api.test-utils)

(defn assertEqual [expected actual]
  (if (not (= expected actual))
    (do
      (println (str "Expected: " expected))
      (println (str "Actual: " actual))
      false)
    true))
