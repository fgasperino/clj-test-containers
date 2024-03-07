(ns user
  (:require
   [clj-test-containers.core :as tc]
   [next.jdbc :as jdbc]
   [hikari-cp.core :as hikari])
  (:import
   [org.testcontainers.containers PostgreSQLContainer]))

;; Test Containers - PostgreSQL

(def version "postgres:16.2")

(defn start!
  []
  (-> (tc/init {:container (PostgreSQLContainer. version)
                :exposed-ports [5432]})
      (tc/start!)))

(defn stop!
  [container]
  (tc/stop! container))

;; Hikari connection pool

(def connection-pool-defaults
  {:auto-commit true
   :pool-name "test-pool"
   :adapter "postgresql"
   :username "test"
   :password "test"
   :database-name "test"
   :idle-timeout (* 30 1000)
   :max-lifetime (* 60 60 1000)
   :connection-timeout (* 10 1000)
   :validation-timeout (* 10 1000)
   :minimum-idle 0
   :maximum-pool-size 1})

(defn start-datasource!
  [parameters]
  (hikari/make-datasource (merge connection-pool-defaults parameters)))

(defn stop-datasource!
  [datasource]
  (hikari/close-datasource datasource))

;; JDBC

(defn execute!
  ([datasource statement]
   (execute! datasource statement nil))
  ([datasource statement parameters]
   (with-open [conn (jdbc/get-connection datasource)]
     (jdbc/execute! conn (apply conj [statement] parameters)))))

(comment

  ;; Start container
  (def container (start!))

  (let [ds (start-datasource! {:server-name (:host container)
                               :port-number (get (:mapped-ports container) 5432)
                               :username "test"
                               :password "test"
                               :database-name "test"})]
    (execute! ds "CREATE TABLE test (id SERIAL, name text);")
    (execute! ds "INSERT INTO test (name) VALUES ('testing');")
    (execute! ds "SELECT * FROM test;"))

  ;; Stop container.
  (stop! container)
  (tc/perform-cleanup!))
