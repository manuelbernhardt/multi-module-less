import com.typesafe.less.sbt.LessPlugin.LessKeys
import com.typesafe.web.sbt.WebPlugin
import com.typesafe.jse.sbt.JsEnginePlugin
import com.typesafe.less.sbt.LessPlugin

name := "multi-module-less"

version := "1.0-SNAPSHOT"

libraryDependencies ++= Seq(
  jdbc,
  anorm,
  cache
)     

play.Project.playScalaSettings

def playProject(name: String) = play.Project(name = name, path = file("modules/" + name),
  settings =
    Defaults.defaultSettings ++
      play.Project.playScalaSettings ++
      WebPlugin.webSettings ++
      JsEnginePlugin.jsEngineSettings ++
      LessPlugin.lessSettings
  ).settings(
    lessEntryPoints := Nil
  )

lazy val core = playProject("core")

lazy val moduleA = playProject("moduleA").dependsOn(core)

lazy val root = playProject("multi-module-less").in(file(".")).settings(
  LessKeys.lessSources <<= (baseDirectory)(base => base / "app" / "assets" ** "*.less")
).dependsOn(moduleA).
  aggregate(moduleA)
