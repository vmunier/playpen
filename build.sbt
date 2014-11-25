import SonatypeKeys._
import TypelevelKeys._
import org.typelevel.sbt.Developer
import org.typelevel.sbt.Version._
import sbtrelease.ReleasePlugin.ReleaseKeys._

organization := "com.beamly.playpen"
name := "playpen"
// lastRelease in ThisBuild := Relative(0, Final)

description := "playpen: A way to Play! safely"
homepage := Some(url("https://github.com/beamly/playpen"))
startYear := Some(2014)
licenses := Seq("Apache License, Version 2.0" -> url("http://www.apache.org/licenses/LICENSE-2.0.txt"))

scalaVersion := "2.11.4"
crossScalaVersions := Seq(scalaVersion.value)
scalacOptions ++= Seq("-encoding", "utf8")
scalacOptions ++= Seq("-deprecation", "-feature", "-unchecked", "-Xlint")
scalacOptions  += "-Xfatal-warnings"
scalacOptions  += "-Xfuture"
scalacOptions  += "-Yinline-warnings"
scalacOptions  += "-Yno-adapted-args"
scalacOptions  += "-Ywarn-dead-code"
scalacOptions  += "-Ywarn-numeric-widen"
scalacOptions  += "-Ywarn-unused-import"
scalacOptions  += "-Ywarn-value-discard"

// wartremoverErrors ++= Warts.unsafe // Once sbt-wartremover 0.12+ is out
wartremoverErrors ++= Seq(Wart.Any, Wart.Any2StringAdd, Wart.AsInstanceOf, Wart.DefaultArguments,
  Wart.EitherProjectionPartial, Wart.IsInstanceOf, Wart.NonUnitStatements, Wart.Null, Wart.OptionPartial, Wart.Product,
  Wart.Return, Wart.Serializable, Wart.TryPartial, Wart.Var, Wart.ListOps)

typelevelDefaultSettings
typelevelBuildInfoSettings

crossBuild := true

pgpPublicRing := Path.userHome / ".gnupg" / "beamly-pubring.gpg"
pgpSecretRing := Path.userHome / ".gnupg" / "beamly-secring.gpg"

profileName := "com.beamly"

githubProject := ("beamly", "playpen")
githubDevs := Seq(Developer("Dale Wijnand", "dwijnand"))
apiURL := Some(url("http://beamly.github.io/playpen/latest/api/"))
autoAPIMappings := true

site.settings
site.includeScaladoc()

ghpages.settings
git.remoteRepo := "git@github.com:beamly/playpen.git"

watchSources ++= (baseDirectory.value * "*.sbt").get
watchSources ++= (baseDirectory.value / "project" * "*.scala").get
