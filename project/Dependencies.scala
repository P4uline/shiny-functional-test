import sbt._

object Dependencies {
  val aaltoXml = "com.fasterxml" % "aalto-xml" % "1.2.2"

  val akkaV = "2.6.5"
  val akkaActor = "com.typesafe.akka" %% "akka-actor" % akkaV
  val akkaTestkit = "com.typesafe.akka" %% "akka-testkit" % akkaV

  val amazonS3 = "com.amazonaws" % "aws-java-sdk-s3" % "1.11.598"
  val archunit = "com.tngtech.archunit" % "archunit" % "0.10.2"
  val assertJCore = "org.assertj" % "assertj-core" % "3.16.1"
  val testContainers = "org.testcontainers" % "testcontainers" % "1.15.0-rc2"
  val testContainersMongo = "org.testcontainers" % "mongodb" % "1.15.0-rc2"

  val junit = "junit" % "junit" % "4.12"
  val junitInterface = "com.novocode" % "junit-interface" % "0.11"
  val mockitoCore = "org.mockito" % "mockito-core" % "2.22.0"
  val mongoDriver = "org.mongodb" % "mongodb-driver-sync" % "4.1.0"
  val playBootstrap = "com.adrianhurt" %% "play-bootstrap" % "1.4-P26-B4"

  val playV = "2.7.3" // Cf. la version de plugin play dans plugins.sbt
  val playTest = "com.typesafe.play" % "play-test_2.12" % playV

  val s3Mock = "com.adobe.testing" % "s3mock-junit4" % "2.1.26"
  val scalatest = "org.scalatest" %% "scalatest" % "3.0.5"
  val systemRules = "com.github.stefanbirkner" % "system-rules" % "1.19.0"
}
