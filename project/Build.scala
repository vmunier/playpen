import sbt._, Keys._, Path.userHome, Scoped.DefinableTask

import com.typesafe.sbt.SbtGhPages._
import com.typesafe.sbt.SbtGit._
import com.typesafe.sbt.SbtPgp._
import com.typesafe.sbt.SbtSite._
import org.typelevel.sbt.TypelevelPlugin._
import xerial.sbt.Sonatype._

import SonatypeKeys._
import TypelevelKeys._
import org.typelevel.sbt.Developer
import sbtrelease.ReleasePlugin.ReleaseKeys._

import scala.language.implicitConversions

object Build extends Build {
  val repoUser = "beamly"
  val repoProj = "playpen"

  val playpen = project in file(".") smartSettings (
    organization := "com.beamly.playpen",
    name := "playpen",

    description := "A way to Play! safely",
    homepage := Some(url(s"https://github.com/$repoUser/$repoProj")),
    startYear := Some(2014),
    licenses := Seq("Apache License, Version 2.0" -> url("http://www.apache.org/licenses/LICENSE-2.0.txt")),

    scalaVersion := "2.11.4",
    crossScalaVersions := Seq(scalaVersion.value, "2.10.4"),
    scalacOptions ++= Seq("-encoding", "utf8"),
    scalacOptions ++= Seq("-deprecation", "-feature", "-unchecked", "-Xlint"),
    scalacOptions  += "-Xfatal-warnings",
    scalacOptions  += "-Xfuture",
    scalacOptions  += "-Yinline-warnings",
    scalacOptions  += "-Yno-adapted-args",
    scalacOptions  += "-Ywarn-dead-code",
    scalacOptions  += "-Ywarn-numeric-widen",
    scalacOptions ++= "-Ywarn-unused-import".ifScala211Plus.value.toSeq,
    scalacOptions  += "-Ywarn-value-discard",

  // wartremoverErrors ++= Warts.unsafe // Once sbt-wartremover 0.12+ is out
//  wartremoverErrors ++= Seq(
// // Wart.Any, // Removed to keep BuildInfo
//    Wart.Any2StringAdd, Wart.AsInstanceOf, Wart.DefaultArguments,
//    Wart.EitherProjectionPartial, Wart.IsInstanceOf, Wart.NonUnitStatements, Wart.Null, Wart.OptionPartial, Wart.Product,
//    Wart.Return, Wart.Serializable, Wart.TryPartial, Wart.Var, Wart.ListOps)

    resolvers += "typesafe-releases" at "http://repo.typesafe.com/typesafe/releases/",

    libraryDependencies += "joda-time"          % "joda-time"    % "2.5",
    libraryDependencies += "org.joda"           % "joda-convert" % "1.2",
    libraryDependencies += "com.typesafe.play" %% "play"         % "2.3.6",
    libraryDependencies += "org.slf4j"          % "slf4j-api"    % "1.7.7",

    typelevelDefaultSettings,
    typelevelBuildInfoSettings,

    crossBuild := true,

    pgpPublicRing := userHome / ".gnupg" / "beamly-pubring.gpg",
    pgpSecretRing := userHome / ".gnupg" / "beamly-secring.gpg",

    profileName := "com.beamly",

    githubProject := (repoUser, repoProj),
    githubDevs := Seq(Developer("Dale Wijnand", "dwijnand")),
    apiURL := Some(url(s"http://$repoUser.github.io/$repoProj/latest/api/")),
    autoAPIMappings := true,

    site.settings,
    site.includeScaladoc(),

    ghpages.settings,
    git.remoteRepo := s"git@github.com:$repoUser/$repoProj.git",

    watchSources ++= (baseDirectory.value * "*.sbt").get,
    watchSources ++= (baseDirectory.value / "project" * "*.scala").get)

  def scalaPartV = Def setting (CrossVersion partialVersion scalaVersion.value)

  implicit final class AnyWithIfScala11Plus[A](val _o: A) {
    def ifScala211Plus = Def setting (scalaPartV.value collect { case (2, y) if y >= 11 => _o })
  }

  implicit final class ProjectWithAlso(val _p: Project) {
    def also(ss: Seq[Setting[_]]) = _p settings (ss.toSeq: _*)

    def smartSettings(sls: SettingLine*) = _p also SmartSettings(sls: _*)
  }

  implicit final class SettingKeyWithRemove[A](val _sk: SettingKey[Seq[A]]) {
    def ~-=(p: A => Boolean): Setting[Seq[A]] = _sk ~= (_ filterNot p)
    def -=(a: A): Setting[Seq[A]] = _sk ~= (_ filterNot a.==)
  }
  implicit final class DefinableTaskWithRemove[A](val _sk: DefinableTask[Seq[A]]) {
    def ~-=(p: A => Boolean): Setting[Task[Seq[A]]] = _sk ~= (_ filterNot p)
    def -=(a: A): Setting[Task[Seq[A]]] = _sk ~= (_ filterNot a.==)
  }

  def SmartSettings(sls: SettingLine*): Seq[Setting[_]] = sls flatMap (_.ss)

  sealed trait SettingLine {def ss: Seq[Setting[_]] }
  implicit final class SingleSetting(val s: Setting[_]) extends SettingLine {val ss = Seq[Setting[_]](s) }
  implicit final class MultiSettings(val ss: Seq[Setting[_]]) extends SettingLine
}
