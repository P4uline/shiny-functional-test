import Dependencies._

name := """shiny-functional-test"""
organization := "com.shiny"

version := "1.0-SNAPSHOT"

lazy val root = (project in file("."))
  .enablePlugins(PlayJava)
  .settings(
    libraryDependencies ++= Seq(
      mongoDriver,
      amazonS3,
      assertJCore % Test,
      junit % Test,
      junitInterface % Test,
      testContainers % Test,
      testContainersMongo % Test,
      mockitoCore % Test,
      "pl.pragmatists" % "JUnitParams" % "1.1.1" % Test,
      akkaTestkit % Test,
      archunit % Test,
      assertJCore % Test,
      junit % Test,
      junitInterface % Test,
      testContainers % Test,
      testContainersMongo % Test,
      mockitoCore % Test, // Veuillez éviter d'utiliser Mockito dans 'it'
      playTest % Test,
      s3Mock % Test,
      scalatest % Test,
      systemRules % Test
    )
  )

scalaVersion := "2.12.12"

libraryDependencies += guice
