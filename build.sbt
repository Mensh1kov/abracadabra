ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "2.13.11"

lazy val root = (project in file("."))
  .settings(
    name := "abracadabra",
    libraryDependencies ++= Seq(
      "io.scalaland"      %% "chimney"               % "0.7.5",
      "org.typelevel"     %% "cats-core"             % "2.6.1",
      "org.typelevel"     %% "cats-effect"           % "3.4.8",
      "org.typelevel"     %% "mouse"                 % "1.2.1",
      "org.scalatest"     %% "scalatest"             % "3.2.9",
      "org.scalamock"     %% "scalamock"             % "5.1.0",
      "org.scalatestplus" %% "scalatestplus-mockito" % "1.0.0-M2",
      "co.fs2"            %% "fs2-core"              % "3.8.0",
      "io.estatico"       %% "newtype"               % "0.4.4",
      "dev.optics"        %% "monocle-core"          % "3.2.0",
      "dev.optics"        %% "monocle-macro"         % "3.1.0",
      "com.chuusai"       %% "shapeless"             % "2.4.0-M1",
      "eu.timepit"        %% "refined"               % "0.11.0",

      "org.typelevel"     %% "cats-laws"             % "2.10.0" % Test,
      "org.scalacheck"    %% "scalacheck"            % "1.14.1" % "test",
      "org.typelevel"     %% "discipline-core"       % "1.5.0",
      "org.scalameta"     %% "munit"                 % "0.7.29" % Test
    )
  )

scalacOptions += "-Ymacro-annotations"
