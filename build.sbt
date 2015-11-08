lazy val root = (project in file(".")).
  enablePlugins(ScalaJSPlugin).
  settings(
    name := "scalascript",
    description := "AngularJS binding for Scala.js.",
    organization := "com.github.ldaniels528",
    version := "0.2.5",
    scalaVersion := "2.11.7",
    scalacOptions ++= Seq("-feature", "-deprecation"),
    homepage := Some(url("http://github.com.ldaniels528/scalascript")),
    addCompilerPlugin("org.scalamacros" % "paradise" % "2.0.1" cross CrossVersion.full),
    libraryDependencies ++= Seq(
      "be.doeraene" %%% "scalajs-jquery" % "0.8.1",
      "org.scala-js" %%% "scalajs-dom" % "0.8.1",
      "org.scala-lang" % "scala-reflect" % "2.11.7"
    )
  )
