(defproject hellonico/clj-datacube "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [
                 [org.clojure/clojure "1.7.0-beta2"]
                 [clj-time "0.7.0"] ; need old version of joda time
                 [com.urbanairship/datacube "1.3.2"]
                 ; use the classifier when building against a particular version of hadoop
                 ;:classifier "hbase0.94.2-cdh4.2.0-hadoopCore2.0.0-mr1-cdh4.2.0-hadoop2.0.0-cdh4.2.0"
                 [org.apache.hadoop/hadoop-core "2.6.0-mr1-cdh5.4.0"]
                 [org.apache.hadoop/hadoop-common "2.7.0"]
                 [org.apache.hadoop/hadoop-hdfs "2.7.0"]
                 [org.apache.hbase/hbase "0.94.2-cdh4.2.1"]
                 [org.clojure/tools.logging "0.3.1"]
                 [org.clojure/data.csv "0.1.2"]
]
  :profiles {:dev
             {:dependencies
              [[midje "1.6.3"]
               [org.clojure/clojure "1.7.0-beta2"]]}}
  :repositories [
                 ;["rally"     "http://alm-build.f4tech.com:8080/nexus/content/groups/public"]
                 ;["releases"  {:url "http://alm-build:8080/nexus/content/repositories/releases"}]
                 ;["snapshots" {:url "http://alm-build:8080/nexus/content/repositories/snapshots" :update :always :snapshots true}]
                 ["cloudera" "https://repository.cloudera.com/artifactory/cloudera-repos/"]]
  :aot :all
  :main clj_datacube.core
)
