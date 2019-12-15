(ns groove-api.mail)
;; Credit must be given where credit is due!
;; This is shamelessly taken from https://nakkaya.com/2009/11/10/using-java-mail-api-from-clojure/
(defn mail [& m]
  (let [mail (apply hash-map m)
        props (java.util.Properties.)]

    (doto props
      (.put "mail.smtp.host" (:host mail))
      (.put "mail.smtp.port" (:port mail))
      (.put "mail.smtp.user" (:user mail))
      (.put "mail.smtp.socketFactory.port"  (:port mail))
      (.put "mail.smtp.auth" "true"))

    (if (= (:ssl mail) true)
      (doto props
        (.setProperty "mail.smtp.socketFactory.class" 
              "javax.net.ssl.SSLSocketFactory")
        (.setProperty "mail.smtp.socketFactory.fallback" "false")
        (.setProperty "mail.smtp.socketFactory.port" (str (:port mail)))
        (.put "mail.smtp.starttls.enable" "true")
        ))

    (let [authenticator (proxy [javax.mail.Authenticator] [] 
                          (getPasswordAuthentication 
                            []
                            (javax.mail.PasswordAuthentication. 
                              (:user mail) (:password mail))))
          recipients (reduce #(str % "," %2) (:to mail))
          session (javax.mail.Session/getInstance props authenticator)
          msg     (javax.mail.internet.MimeMessage. session)]

      (.setFrom msg (javax.mail.internet.InternetAddress. (:user mail)))

      (.setRecipients msg 
                      (javax.mail.Message$RecipientType/TO)
                      (javax.mail.internet.InternetAddress/parse recipients))

      (.setSubject msg (:subject mail))
      (.setText msg (:text mail))
      (javax.mail.Transport/send msg))))
