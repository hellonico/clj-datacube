(ns clj-datacube.core_test
  (:use midje.sweet
        clj-datacube.core)
  (:require [clj-time.core :as time])
  (:import [com.urbanairship.datacube Dimension Rollup]))

(facts "first cube"
  (defcube my-cube :long (map-db-harness) 10 1000 full-sync
    (dimension :name (string-dimension "name"))
    (dimension :measure (string-dimension "test"))
    (dimension :time (time-dimension "time" 8))
    (rollup)
    (rollup [:name :time day-bucket])
    (rollup [:name])
    (rollup [:name :measure]))

  (write-value my-cube 102)
  (write-value my-cube 100 :name "name")
  (write-value my-cube 100 :name "name")
  (write-value my-cube 104 :name "other name" :measure "testing")
  (write-value my-cube 105 :name "name2" :time (time/date-time 2013 06 02))

  (fact "Cube contents"
    (count (:dimensions my-cube))                                  => 3
    (every? #(instance? Dimension %) (vals (:dimensions my-cube))) => true
    (:dimension-list my-cube)                                      => [:name :measure :time]

    (count (:rollups my-cube))                                     => 4
    (every? #(instance? Rollup %) (:rollups my-cube))              => true)

  (fact "Reading from no dimensions"
    (read-value my-cube) => 511)

  (fact "Multiple writes to a single dimension"
    (read-value my-cube :name "name") => 200)

  (fact "Reading from multiple dimensions"
    (read-value my-cube :name "other name" :measure "testing") => 104)

  ;; (fact "Reading from day partitiond values"
    ;; (read-value my-cube :name "name2" :time day-bucket (time/date-time 2013 06 02)) => 105)
)