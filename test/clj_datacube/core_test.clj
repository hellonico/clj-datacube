(ns clj-datacube.core_test
  (:use midje.sweet
        clj-datacube.core)
  (:require [clj-time.core :as time])
  (:import [com.urbanairship.datacube Dimension Rollup]))

(facts "first cube"
  (defcube my-cube :long

    (string-dimension :name)
    (string-dimension :measure)
    (time-dimension :time 8)

    (rollup)
    (rollup :name)
    (rollup :name :time day-bucket)
    (rollup :name :measure))

  (write-value my-cube 102)
  (write-value my-cube 100 (at :name "name"))
  (write-value my-cube 100 (at :name "name"))
  (write-value my-cube 104 (at :name "other name") (at :measure "testing"))
  (write-value my-cube 105 (at :name "name2") (at :time (time/date-time 2013 06 02)))

  (fact "Cube contents"
    (count (:dimensions my-cube))                                  => 3
    (every? #(instance? Dimension %) (vals (:dimensions my-cube))) => true
    (:dimension-list my-cube)                                      => [:name :measure :time]
    (count (:rollups my-cube))                                     => 4
    (every? #(instance? Rollup %) (:rollups my-cube))              => true)

  (fact "Reading from no dimensions"
    (read-value my-cube) => 511)

  (fact "Multiple writes to a single dimension"
    (read-value my-cube (at :name "name")) => 200)

  (fact "Reading from multiple dimensions"
    (read-value my-cube (at :name "other name") (at :measure "testing")) => 104)

  (fact "Reading from day partitiond values"
     (read-value my-cube (at :name "name2") (at :time day-bucket (time/date-time 2013 06 01))) => 0
     (read-value my-cube (at :name "name2") (at :time day-bucket (time/date-time 2013 06 02))) => 105
     (read-value my-cube (at :name "name2") (at :time day-bucket (time/date-time 2013 06 03))) => 0
     ))

(facts "Type checking stored values"
  (defcube long-cube :long (rollup))
  (defcube int-cube :int (rollup))
  (defcube double-cube :double (rollup))

  (write-value long-cube 1000)
  (write-value int-cube 1000)
  (write-value double-cube 1000.0)

  (class (read-value long-cube))   => Long
  (class (read-value int-cube))    => Integer
  (class (read-value double-cube)) => Double
  )

(facts "Numeric and Boolean Dimensions"
  (defcube long-cube :long 
    (int-dimension :int)
    (long-dimension :long)
    (boolean-dimension :bool)
    (rollup :int)
    (rollup :int :long)
    (rollup :int :long :bool))
  (write-value long-cube 100 (at :int (int 1)))
  (write-value long-cube 200 (at :int (int 2)) (at :long (long 2)))
  (write-value long-cube 300 (at :int (int 3)) (at :long (long 2)) (at :bool true))

  (read-value long-cube (at :int (int 1)))                                      => 100
  (read-value long-cube (at :int (int 2)) (at :long (long 2)))                  => 200
  (read-value long-cube (at :int (int 3)) (at :long (long 2)) (at :bool true))  => 300
  (read-value long-cube (at :int (int 3)) (at :long (long 2)) (at :bool false)) => 0)

(facts "cube rollups on missing dimensions"
  (defcube broken :long
    (string-dimension :name)
    (rollup :unknown))  
  => (throws IllegalArgumentException #"Dimension :unknown not found")
)
