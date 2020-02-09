{:profiles/dev
  {:env
   {:dbtype "postgres"
    :subname "//localhost:5432/groove_api"
    :password "password"
    :secret "password"
    :istest false
    :port 3000}}
  :profiles/test
  {:env
   {:dbtype "postgres"
    :password "password"
    :subname "//localhost:5432/groove_api"
    :secret "password"
    :istest true
    :port 3001}}}


