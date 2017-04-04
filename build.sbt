name := "scraw"

scalaVersion := "2.11.8"

resolvers += "Artima Maven Repository" at "http://repo.artima.com/releases"

libraryDependencies +=  "org.scalaj" %% "scalaj-http" % "2.3.0"
libraryDependencies += "org.scala-lang.modules" %% "scala-parser-combinators" % "1.0.4"
libraryDependencies += "net.liftweb" %% "lift-json" % "3.0.1"
libraryDependencies += "com.github.melrief" %% "pureconfig" % "0.4.0"
libraryDependencies += "org.scalactic" %% "scalactic" % "3.0.1"
libraryDependencies += "org.scalatest" % "scalatest_2.11" % "3.0.1" % "test"


