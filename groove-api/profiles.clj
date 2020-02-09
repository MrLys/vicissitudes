{:profiles/dev
  {:env
   {:dbtype "postgres"
    :subname "//localhost:5432/groove_api"
    :password "password"
    :secret "password"
    :port 3000}}
  :profiles/test
  {:env
   {:dbtype "postgres"
    :password "password"
    :subname "//localhost:5432/groove_api"
    :secret "password"
    :port 3001}}}


