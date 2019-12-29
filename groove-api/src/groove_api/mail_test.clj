(ns groove-api.mail-test)
;; Credit must be given where credit is due!
;; This is shamelessly taken from https://nakkaya.com/2009/11/10/using-java-mail-api-from-clojure/
(defn mail [& m]
  (let [mail (apply hash-map m)
        props (java.util.Properties.)]

    (doto props
      (.setProperty "mail.smtp.host" "mail.smtpbucket.com")
      (.setProperty "mail.smtp.port" "8025"))

    (if (= (:ssl mail) true)
      (doto props
        (.setProperty "mail.smtp.socketFactory.class"
                      "javax.net.ssl.SSLSocketFactory")
        (.setProperty "mail.smtp.socketFactory.fallback" "false")
        (.setProperty "mail.smtp.socketFactory.port" (str (:port mail)))
        (.put "mail.smtp.starttls.enable" "true")))

    (let  [session (javax.mail.Session/getDefaultInstance props)
           msg     (javax.mail.internet.MimeMessage. session)
           recipients (reduce #(str % "," %2) (:to mail))]

      (.setFrom msg (javax.mail.internet.InternetAddress. "noreply@rutta.no"))

      (.addRecipient msg
                     (javax.mail.Message$RecipientType/TO)
                     (javax.mail.internet.InternetAddress. (str (:to mail))))

      (.setSubject msg (:subject mail))
      (.setContent msg (:text mail) "text/html")
      (javax.mail.Transport/send msg))))
